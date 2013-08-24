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
package org.openbel.framework.core.compiler;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.enums.FunctionEnum.PROTEIN_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.GENE_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.RNA_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.FUSION;
import static org.openbel.framework.common.enums.FunctionEnum.PROTEIN_MODIFICATION;
import static org.openbel.framework.common.enums.FunctionEnum.SUBSTITUTION;
import static org.openbel.framework.common.enums.FunctionEnum.TRUNCATION;
import static org.openbel.framework.common.Strings.*;
import static org.openbel.framework.common.enums.AminoAcid.getAminoAcid;
import static org.openbel.framework.common.enums.CovalentModification.getCovalentModification;
import static org.openbel.framework.common.enums.ValueEncoding.getValueEncoding;
import static org.openbel.framework.common.lang.Signature.WILDCARD_ENCODING;
import static org.openbel.framework.common.lang.Signature.encode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.enums.SemanticStatus;
import org.openbel.framework.common.enums.ValueEncoding;
import org.openbel.framework.common.lang.Function;
import org.openbel.framework.common.lang.Signature;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.StatementGroup;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.model.Statement.Object;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.namespace.NamespaceSyntaxWarning;

/**
 * BEL semantic service implementation.
 */
public final class SemanticServiceImpl implements SemanticService {
    private final NamespaceService nsService;

    /**
     * Creates a semantic service implementation.
     *
     * @param nsService {@link NamespaceService}, the namespace service
     */
    public SemanticServiceImpl(final NamespaceService nsService) {
        this.nsService = nsService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkParameterizedTerm(final Term term) throws SemanticWarning {
        final FunctionEnum funcEnum = term.getFunctionEnum();

        // Construct the signature
        final StringBuilder bldr = new StringBuilder();
        bldr.append(funcEnum.getDisplayValue());
        bldr.append("(");

        List<BELObject> functionArguments = term.getFunctionArguments();
        if (hasItems(functionArguments)) {
            for (int i = 0; i < functionArguments.size(); i++) {
                final BELObject bo = functionArguments.get(i);

                String arg = null;
                if (bo instanceof Parameter) {
                    arg = processParameter((Parameter) bo);
                } else if (bo instanceof Term) {
                    arg = processTerm((Term) bo);
                    if (arg == null) continue;
                } else {
                    String type = bo.getClass().getName();
                    final String err = "unknown function argument " + type;
                    throw new UnsupportedOperationException(err);
                }
                if (i != 0) bldr.append(",");
                bldr.append(arg);
            }
        }
        bldr.append(")");
        bldr.append(funcEnum.getReturnType().getDisplayValue());

        Signature sig = null;
        try {
            sig = new Signature(bldr.toString());
        } catch (InvalidArgument e) {
            final String lf = term.toBELLongForm();
            final String err = e.getMessage();
            throw new SemanticWarning(lf, err);
        }

        final Function function = funcEnum.getFunction();

        if (!function.validSignature(sig)) {
            final Map<Signature, SemanticStatus> map = function.getStatus(sig);
            final String lf = term.toBELLongForm();
            final String err = format(SEMANTIC_TERM_FAILURE, lf);
            throw new SemanticWarning(null, err, sig, map);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkTerm(final Term term) throws SemanticWarning {
        final FunctionEnum funcEnum = term.getFunctionEnum();

        // Construct the signature
        final StringBuilder bldr = new StringBuilder();
        bldr.append(funcEnum.getDisplayValue());
        bldr.append("(");

        List<BELObject> functionArguments = term.getFunctionArguments();
        if (hasItems(functionArguments)) {
            for (int i = 0; i < functionArguments.size(); i++) {
                final BELObject bo = functionArguments.get(i);

                String arg = null;
                if (bo instanceof Term) {
                    arg = processTerm((Term) bo);
                    if (arg == null) continue;
                } else {
                    String type = bo.getClass().getName();
                    final String err = "unhandled function argument " + type;
                    throw new UnsupportedOperationException(err);
                }
                if (i != 0) bldr.append(",");
                bldr.append(arg);
            }
        }
        bldr.append(")");
        bldr.append(funcEnum.getReturnType().getDisplayValue());

        Signature sig = null;
        try {
            sig = new Signature(bldr.toString());
        } catch (InvalidArgument e) {
            final String lf = term.toBELLongForm();
            final String err = e.getMessage();
            throw new SemanticWarning(lf, err);
        }

        final Function function = funcEnum.getFunction();

        if (!function.validSignature(sig)) {
            final Map<Signature, SemanticStatus> map = function.getStatus(sig);
            final String lf = term.toBELLongForm();
            final String err = format(SEMANTIC_TERM_FAILURE, lf);
            throw new SemanticWarning(null, err, sig, map);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkRelationship(final Statement statement)
            throws SemanticWarning {
        RelationshipType r = statement.getRelationshipType();
        Object o = statement.getObject();

        if (o != null) {

            // Relationship must be present.
            if (r == null) {
                final String err = SEMANTIC_STATEMENT_REQUIRES_RELATIONSHIP;
                final String name = statement.toBELShortForm();
                throw new SemanticWarning(name, err, null);
            }

        } else {

            // Relationship must be absent.
            if (r != null) {
                final String err = SEMANTIC_STATEMENT_PROVIDES_RELATIONSHIP;
                final String name = statement.toBELShortForm();
                throw new SemanticWarning(name, err, null);
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkListUsage(final Statement statement,
            final Document document)
            throws SemanticWarning {
        Object object = statement.getObject();

        if (object != null && !statement.hasNestedStatement() &&
                object.getTerm().getFunctionEnum() == FunctionEnum.LIST &&
                !statement.getRelationshipType().isListable()) {

            if (document != null) {
                pruneStatement(statement, document);
            }

            final String err = SEMANTIC_LIST_IMPROPER_CONTEXT;
            final String name = statement.toBELShortForm();
            throw new SemanticWarning(name, err, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkNested(final Statement statement) throws SemanticWarning {
        // Ignore non-nested statements
        if (!statement.hasNestedStatement()) {
            return;
        }

        final RelationshipType relationship = statement.getRelationshipType();
        // No relationship? Ignore it, this is a different semantic check
        if (relationship == null) {
            return;
        }

        // Nested statement is causal, gold star for you
        if (relationship.isCausal()) {
            return;
        }

        // Try to provide context for where the warning is
        final StringBuilder bldr = new StringBuilder();
        bldr.append("Nested statement with non-causal relationship ");
        bldr.append(relationship);

        final String err = SEMANTIC_NESTED_REQUIRE_CAUSAL;
        final String name = bldr.toString();
        throw new SemanticWarning(name, err);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkMultiNested(final Statement stmt, final Document doc)
            throws SemanticWarning {
        // is the statement nested?
        if (!stmt.hasNestedStatement()) {
            // not nested
            return;
        }

        // does it contain more than one nested statement?
        Statement objstmt = stmt.getObject().getStatement();
        if (objstmt.getObject().getStatement() == null) {
            // object term in nested statement
            return;
        }

        // Multi-nested statement, prune stmt then throw semantic warning
        pruneStatement(stmt, doc);

        // throw semantic warning
        final StringBuilder bldr = new StringBuilder();
        bldr.append("statement nested more than once, " +
                "pruning statement.");

        final String err = SEMANTIC_MULTI_NESTED;
        final String name = bldr.toString();
        throw new SemanticWarning(name, err);
    }

    /**
     * Prunes the provided {@link Statement statement} from the {@link Document document}.
     */
    private void pruneStatement(final Statement stmt, final Document doc) {
        final List<StatementGroup> stmtgroups = doc.getAllStatementGroups();
        for (final StatementGroup stmtgroup : stmtgroups) {
            final Iterator<Statement> sgi = stmtgroup.getStatements()
                    .iterator();
            while (sgi.hasNext()) {
                final Statement sgs = sgi.next();
                if (stmt.equals(sgs)) {
                    // prune
                    sgi.remove();
                    return;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SemanticWarning> checkAbundanceSubsets(final Term term) {
        final FunctionEnum fe = term.getFunctionEnum();
        if (fe != PROTEIN_ABUNDANCE && fe != GENE_ABUNDANCE
                && fe != RNA_ABUNDANCE) {
            return emptyList();
        }

        List<Term> nestedTerms = term.getTerms();
        if (noItems(nestedTerms)) {
            return emptyList();
        }

        List<SemanticWarning> ret = new ArrayList<SemanticWarning>();
        switch (fe) {
        case GENE_ABUNDANCE:
        case RNA_ABUNDANCE:
            for (final Term nestedTerm : nestedTerms) {
                if (nestedTerm.getFunctionEnum() == FUSION) {
                    ret.addAll(checkPFusion(nestedTerm));
                }
            }
            break;
        case PROTEIN_ABUNDANCE:
            for (final Term nestedTerm : nestedTerms) {
                if (!nestedTerm.getFunctionEnum().isProteinDecorator()) {
                    continue;
                }
                ret.addAll(checkAbundanceSubset(nestedTerm));
            }
            break;
        default:
            break;
        }
        return ret;
    }

    private List<SemanticWarning> checkAbundanceSubset(final Term term) {
        final FunctionEnum fe = term.getFunctionEnum();
        if (fe == PROTEIN_MODIFICATION) {
            return checkPModification(term);
        } else if (fe == SUBSTITUTION) {
            return checkPSubstitution(term);
        } else if (fe == TRUNCATION) {
            return checkPTruncation(term);
        } else if (fe == FUSION) {
            return checkPFusion(term);
        }
        return emptyList();
    }

    private List<SemanticWarning> checkPModification(final Term term) {
        List<SemanticWarning> ret = new ArrayList<SemanticWarning>();

        int numargs = term.getNumberOfArguments();

        // Indicates signatures do not match, return
        // (no semantic warning should be generated here for this)
        if (!PROTEIN_MODIFICATION.isValidArgumentCount(numargs)) {
            return emptyList();
        }

        List<Parameter> parameters = term.getParameters();

        // Check mod type
        Parameter parameter = parameters.get(0);
        String pval = parameter.getValue();

        if (!validPType(pval)) {
            String msg;
            if (pval == null) {
                msg = SEMANTIC_NOT_CM;
            } else {
                msg = format(SEMANTIC_PTYPE_NOT_CM, pval);
            }
            ret.add(new SemanticWarning(term.toBELLongForm(), msg));
        }

        if (numargs == 1) {
            return ret;
        }

        // Check mod code
        parameter = parameters.get(1);
        pval = parameter.getValue();

        if (!validAcid(pval)) {
            String msg;
            if (pval == null) {
                msg = SEMANTIC_NOT_AA;
            } else {
                msg = format(SEMANTIC_CODE_NOT_AA, pval);
            }
            ret.add(new SemanticWarning(term.toBELLongForm(), msg));
        }

        if (numargs == 2) {
            return ret;
        }

        // Check mod position
        parameter = parameters.get(2);
        pval = parameter.getValue();

        if (!validPosition(pval)) {
            String msg;
            if (pval == null) {
                msg = SEMANTIC_NOT_INT;
            } else {
                msg = format(SEMANTIC_POS_NOT_INT, pval);
            }
            ret.add(new SemanticWarning(term.toBELLongForm(), msg));
        }
        return ret;
    }

    private List<SemanticWarning> checkPSubstitution(final Term term) {
        List<SemanticWarning> ret = new ArrayList<SemanticWarning>();

        int numargs = term.getNumberOfArguments();

        // Indicates signatures do not match, return
        // (no semantic warning should be generated here for this)
        if (!SUBSTITUTION.isValidArgumentCount(numargs)) {
            return emptyList();
        }

        List<Parameter> parameters = term.getParameters();
        // Sanity check three parameters
        if (numargs != 3) {
            return ret;
        }

        // Check amino acid variant
        Parameter parameter = parameters.get(0);
        String pval = parameter.getValue();

        if (!validAcid(pval)) {
            String msg;
            if (pval == null) {
                msg = SEMANTIC_NOT_AA;
            } else {
                msg = format(SEMANTIC_CODE_NOT_AA, pval);
            }
            ret.add(new SemanticWarning(term.toBELLongForm(), msg));
        }

        // Check mod position
        parameter = parameters.get(1);
        pval = parameter.getValue();

        if (!validPosition(pval)) {
            String msg;
            if (pval == null) {
                msg = SEMANTIC_NOT_INT;
            } else {
                msg = format(SEMANTIC_POS_NOT_INT, pval);
            }
            ret.add(new SemanticWarning(term.toBELLongForm(), msg));
        }

        // Check amino acid variant
        parameter = parameters.get(2);
        pval = parameter.getValue();

        if (!validAcid(pval)) {
            String msg;
            if (pval == null) {
                msg = SEMANTIC_NOT_AA;
            } else {
                msg = format(SEMANTIC_CODE_NOT_AA, pval);
            }
            ret.add(new SemanticWarning(term.toBELLongForm(), msg));
        }

        return ret;
    }

    private List<SemanticWarning> checkPTruncation(final Term term) {
        List<SemanticWarning> ret = new ArrayList<SemanticWarning>();

        int numargs = term.getNumberOfArguments();

        // Indicates signatures do not match, return
        // (no semantic warning should be generated here for this)
        if (!TRUNCATION.isValidArgumentCount(numargs)) {
            return emptyList();
        }

        List<Parameter> parameters = term.getParameters();
        // Sanity check one parameter
        if (numargs != 1) {
            return ret;
        }

        // Check mod position
        Parameter parameter = parameters.get(0);
        String pval = parameter.getValue();

        if (!validPosition(pval)) {
            String msg;
            if (pval == null) {
                msg = SEMANTIC_NOT_INT;
            } else {
                msg = format(SEMANTIC_POS_NOT_INT, pval);
            }
            ret.add(new SemanticWarning(term.toBELLongForm(), msg));
        }

        return ret;
    }

    private List<SemanticWarning> checkPFusion(final Term term) {
        List<SemanticWarning> ret = new ArrayList<SemanticWarning>();

        int numargs = term.getNumberOfArguments();

        // Indicates signatures do not match, return
        // (no semantic warning should be generated here for this)
        if (!FUSION.isValidArgumentCount(numargs)) {
            return emptyList();
        }

        List<Parameter> parameters = term.getParameters();
        // Sanity check three parameters - we only semantically validate last
        // two are valid positions
        if (parameters.size() != 3) {
            return ret;
        }

        for (int i = 1; i <= 2; i++) {
            Parameter parameter = parameters.get(i);
            String pval = parameter.getValue();

            if (!validPosition(pval)) {
                String msg;
                if (pval == null) {
                    msg = SEMANTIC_NOT_INT;
                } else {
                    msg = format(SEMANTIC_POS_NOT_INT, pval);
                }
                ret.add(new SemanticWarning(term.toBELLongForm(), msg));
            }
        }

        return ret;
    }

    /*
     * Check protein abundance 'type' is valid.
     */
    private boolean validPType(String pval) {
        if (getCovalentModification(pval) == null) {
            return false;
        }
        return true;
    }

    /*
     * Check amino acid is valid.
     */
    private boolean validAcid(String pval) {
        if (getAminoAcid(pval) == null) {
            return false;
        }
        return true;
    }

    /*
     * Check position is valid.
     */
    private boolean validPosition(String pval) {
        if (pval == null) {
            return false;
        }
        try {
            int ival = parseInt(pval);
            if (ival < 1) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /*
     * Returns the parameter formatted as a string, suitable for semantic
     * checking.
     */
    private String processParameter(final Parameter p) {
        try {
            String encoding = nsService.lookup(p);
            if (encoding == null) {
                return encode(WILDCARD_ENCODING);
            }

            final ValueEncoding ve = getValueEncoding(encoding);
            if (ve == null) {
                return encode(encoding);
            }
            return encode(ve.getDisplayValue());
        } catch (NamespaceSyntaxWarning e) {
            return encode(WILDCARD_ENCODING);
        } catch (IndexingFailure idxf) {
            return encode(WILDCARD_ENCODING);
        }
    }

    /*
     * Returns the term formatted as a string, suitable for semantic checking.
     */
    private String processTerm(final Term t) {
        return encode(t.getFunctionEnum().getReturnType());
    }
}
