/**
 * Copyright (C) 2012 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
 */
package org.openbel.framework.core.compiler.expansion;

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
import org.openbel.framework.common.protonetwork.model.TermTable;
import org.openbel.framework.common.protonetwork.model.StatementTable.TableStatement;
import org.openbel.framework.core.protonetwork.ProtoNetworkBuilder;

/**
 * ExpansionServiceImpl implements a service to expand {@link Term terms} and
 * {@link Statement statements} in a {@link Document BEL document}.
 *
 * <p>
 * The expansion service uses a set of
 * {@link TermExpansionRule term expansion rules} and
 * {@link StatementExpansionRule statement expansion rules} to encapsulate
 * rule matching and logic for expansion scenarios.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ExpansionServiceImpl implements ExpansionService {

    // statically define and initialize term expansion rules.
    private final static List<TermExpansionRule> termExpansionRules;
    static {
        termExpansionRules = new ArrayList<TermExpansionRule>();
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
        final StatementExpansionRule[] rules = new StatementExpansionRule[2];
        if (stmtExpand) {
            rules[0] = distributedRule;
        } else {
            rules[0] = extractRule;
        }
        rules[1] = reciprocalRule;

        final StatementTable st = pn.getStatementTable();
        final List<TableStatement> ts = st.getStatements();
        final Map<Integer, Statement> index = st.getIndexedStatements();

        for (int i = 0, n = ts.size(); i < n; i++) {
            final Statement stmt = index.get(i);

            if (stmt == null) {
                continue;
            }

            // check each statement expansion rule
            for (StatementExpansionRule rule : rules) {
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
     * Apply all matching {@link TermExpansionRule term expansion rules}
     * defined in <tt>termExpansionRules</tt> to the {@link Term outer term}
     * and all the {@link Term inner terms}.
     *
     * <p>
     * This method is called recursively to evaluate the {@link Term inner terms}.
     * </p>
     *
     * @param term {@link Term}, the current term to process for expansion
     * @param allExpansions {@link List} of {@link Statement}, all statements
     * currently collected through term expansion
     * @throws TermExpansionException Thrown if a {@link TermExpansionRule}
     * encounters an error with the {@link Term term} being expanded.
     */
    private void expandTerm(Term term, List<Statement> allExpansions) {
        for (final TermExpansionRule rule : termExpansionRules) {
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
     * 
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
