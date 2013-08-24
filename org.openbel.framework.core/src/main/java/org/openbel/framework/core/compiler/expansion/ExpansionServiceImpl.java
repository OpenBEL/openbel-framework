/**
 *  Copyright 2013 OpenBEL Consortium
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openbel.framework.core.compiler.expansion;

import static org.openbel.framework.common.BELUtilities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNodeTable;
import org.openbel.framework.common.protonetwork.model.StatementTable;
import org.openbel.framework.common.protonetwork.model.StatementTable.TableStatement;
import org.openbel.framework.common.protonetwork.model.TermTable;
import org.openbel.framework.core.protonetwork.ProtoNetworkBuilder;

/**
 * ExpansionServiceImpl implements a service to expand {@link Term terms} and
 * {@link Statement statements} in a {@link Document BEL document}.
 * <p>
 * The expansion service uses a set of {@link TermExpansionRule term expansion
 * rules} and {@link StatementExpansionRule statement expansion rules} to
 * encapsulate rule matching and logic for expansion scenarios.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ExpansionServiceImpl implements ExpansionService {

    // statically define and initialize term expansion rules.
    private final static List<ExpansionRule<Term>> termExpansionRules;
    static {
        termExpansionRules = sizedArrayList(7);
        termExpansionRules.add(new ReactionExpansionRule());
        termExpansionRules.add(new TranslocationExpansionRule());
        termExpansionRules.add(new DegradationExpansionRule());
        termExpansionRules.add(new MolecularActivityExpansionRule());
        termExpansionRules.add(new ComplexAbundanceExpansionRule());
        termExpansionRules.add(new CompositeAbundanceExpansionRule());
        termExpansionRules.add(new ModificationExpansionRule());
    }

    // statically define and initialize statement expansion rules.
    private final static ExtractStatementExpansionRule extractRule =
            new ExtractStatementExpansionRule();
    private final static DistributeStatementExpansionRule distributedRule =
            new DistributeStatementExpansionRule();
    private final static ReciprocalExpansionRule reciprocalRule =
            new ReciprocalExpansionRule();

    /**
     * {@inheritDoc}
     */
    @Override
    public void expandTerms(final Document doc, final ProtoNetwork pn) {
        final ProtoNetworkBuilder pnb = new ProtoNetworkBuilder(doc);
        final StatementTable st = pn.getStatementTable();
        final List<TableStatement> stmts = st.getStatements();
        final Map<Integer, Statement> si = st.getIndexedStatements();

        final List<Statement> expansions = new ArrayList<Statement>();
        for (int i = 0, n = stmts.size(); i < n; i++) {
            final Statement stmt = si.get(i);

            // expand subject term
            expandTerm(stmt.getSubject(), expansions);

            if (stmt.getObject() != null) {
                if (stmt.getObject().getTerm() != null) {
                    // expand the rest of simple statement
                    expandTerm(stmt.getObject().getTerm(), expansions);
                } else {
                    // expand the rest of nested statement
                    final Term nsub = stmt.getObject().getStatement()
                            .getSubject();
                    final Term nobj = stmt.getObject().getStatement()
                            .getObject().getTerm();

                    expandTerm(nsub, expansions);
                    expandTerm(nobj, expansions);
                }
            }

            for (final Statement expansion : expansions) {
                createEdge(expansion, i, pn, pnb);
            }

            expansions.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void expandStatements(final Document doc, final ProtoNetwork pn,
            final boolean stmtExpand) {
        final ProtoNetworkBuilder pnb = new ProtoNetworkBuilder(doc);
        final ArrayList<ExpansionRule<Statement>> rules = sizedArrayList(2);
        if (stmtExpand) {
            rules.add(distributedRule);
        } else {
            rules.add(extractRule);
        }
        rules.add(reciprocalRule);

        final StatementTable st = pn.getStatementTable();
        final List<TableStatement> ts = st.getStatements();
        final Map<Integer, Statement> index = st.getIndexedStatements();

        for (int i = 0, n = ts.size(); i < n; i++) {
            final Statement stmt = index.get(i);

            if (stmt == null) {
                continue;
            }

            // check each statement expansion rule
            for (ExpansionRule<Statement> rule : rules) {
                if (!rule.match(stmt)) {
                    continue;
                }
                final List<Statement> stmtExp = rule.expand(stmt);
                for (final Statement exp : stmtExp) {

                    if (exp.isSubjectOnly()) {
                        createNode(exp.getSubject(), i, pn, pnb);
                    } else if (exp.isStatementTriple()) {
                        createEdge(exp, i, pn, pnb);
                    } else {
                        throw new UnsupportedOperationException(
                                "Cannot provide a nested statement as an " +
                                        "expansion.");
                    }
                }
            }
        }
    }

    /**
     * Apply all matching {@link TermExpansionRule term expansion rules} defined
     * in <tt>termExpansionRules</tt> to the {@link Term outer term} and all the
     * {@link Term inner terms}.
     * <p>
     * This method is called recursively to evaluate the {@link Term inner
     * terms}.
     * </p>
     *
     * @param term {@link Term}, the current term to process for expansion
     * @param allExpansions {@link List} of {@link Statement}, all statements
     * currently collected through term expansion
     * @throws TermExpansionException Thrown if a {@link TermExpansionRule}
     * encounters an error with the {@link Term term} being expanded.
     */
    private void expandTerm(Term term, List<Statement> allExpansions) {
        for (final ExpansionRule<Term> rule : termExpansionRules) {
            if (rule.match(term)) {
                List<Statement> termExpansions = rule.expand(term);
                allExpansions.addAll(termExpansions);
            }

            final List<Term> innerTerms = term.getTerms();
            for (final Term innerTerm : innerTerms) {
                expandTerm(innerTerm, allExpansions);
            }
        }
    }

    /**
     * Creates a proto node for the {@link Term term}.
     * <p>
     * There is no way to associate statement support to a proto node so the
     * {@code supporting} statement index is unused.
     * </p>
     *
     * @param term the {@link Term BEL term} to store, which cannot be
     * {@code null}
     * @param supporting the {@code int} statement index which is currently
     * unused
     * @param pn the {@link ProtoNetwork proto network} to expand, which cannot
     * be {@code null}
     * @param pnb the {@link ProtoNetworkBuilder proto network builder} used to
     * create proto nodes and edges, which cannot be {@code null}
     * @return the proto node index
     * @throws InvalidArgument Thrown if {@code term}, {@code pn}, or
     * {@code pnb} is {@code null}
     */
    private int createNode(final Term term, final int supporting,
            final ProtoNetwork pn, final ProtoNetworkBuilder pnb) {
        final TermTable tt = pn.getTermTable();
        final ProtoNodeTable pnt = pn.getProtoNodeTable();

        int subIndex = pnb.buildProtoNetwork(pn, term);
        String subLbl = tt.getTermValues().get(subIndex);
        return pnt.addNode(subIndex, subLbl);
    }

    /**
     * Creates a proto edge for the {@link Statement statement} while
     * preserving the original, supporting {@link Statement statement}.
     *
     * @param stmt the {@link Statement statement} to store, which cannot be
     * {@code null}
     * @param supporting the {@code int} statement index to associate as the
     * original, supported statement
     * @param pn the {@link ProtoNetwork proto network} to expand, which cannot
     * be {@code null}
     * @param pnb the {@link ProtoNetworkBuilder proto network builder} used
     * to create proto nodes and edges, which cannot be {@code null}
     * @throws InvalidArgument Thrown if {@code stmt}, {@code pn}, or
     * {@code pnb} is {@code null}
     */
    private void createEdge(final Statement stmt, final int supporting,
            final ProtoNetwork pn, final ProtoNetworkBuilder pnb) {
        // add subject as term and proto node
        final Term subject = stmt.getSubject();
        final Integer sourceIndex = createNode(subject, supporting, pn, pnb);

        // add object as term and proto node
        final Term object = stmt.getObject().getTerm();
        final Integer objectIndex = createNode(object, supporting, pn, pnb);

        final ProtoEdgeTable pet = pn.getProtoEdgeTable();
        pet.addEdges(supporting, new ProtoEdgeTable.TableProtoEdge(sourceIndex,
                stmt.getRelationshipType().getDisplayValue(), objectIndex));
    }
}
