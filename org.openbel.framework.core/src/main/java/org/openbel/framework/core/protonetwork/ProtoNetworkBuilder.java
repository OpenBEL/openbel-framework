/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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
package org.openbel.framework.core.protonetwork;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.enums.FunctionEnum.LIST;
import static org.openbel.framework.common.enums.RelationshipType.HAS_COMPONENT;
import static org.openbel.framework.common.enums.RelationshipType.HAS_COMPONENTS;
import static org.openbel.framework.common.enums.RelationshipType.HAS_MEMBER;
import static org.openbel.framework.common.enums.RelationshipType.HAS_MEMBERS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.*;
import org.openbel.framework.common.protonetwork.model.*;
import org.openbel.framework.common.protonetwork.model.AnnotationDefinitionTable.TableAnnotationDefinition;
import org.openbel.framework.common.protonetwork.model.DocumentTable.DocumentHeader;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ParameterTable.TableParameter;
import org.openbel.framework.common.protonetwork.model.StatementAnnotationMapTable.AnnotationPair;
import org.openbel.framework.common.protonetwork.model.StatementTable.TableStatement;
import org.openbel.framework.common.util.PackUtils;

/**
 * ProtoNetworkBuilder breaks down a {@link Document} into a proto network that
 * individual identifies the following:
 * <ul>
 * <li>Document header information</li>
 * <li>Namespaces</li>
 * <li>Which document defines which namespace</li>
 * <li>Parameters</li>
 * <li>Terms</li>
 * <li>Which parameters are defined in which terms and in what order</li>
 * <li>Statements</li>
 * <li>Which document defines which statement</li>
 * <li>Annotation definitions</li>
 * <li>Annotation values</li>
 * <li>Which statements reference which annotations</li>
 * </ul>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ProtoNetworkBuilder {

    /**
     * Defines the BEL {@link Document} that will be traversed to build the
     * {@link ProtoNetwork}.
     */
    private Document doc;

    /**
     * Defines the date format to use for dates ({@code ISO 8601}).
     */
    private SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates the ProtoNetworkBuilder from the {@link Document} that should be
     * compiled.
     *
     * @param doc {@link Document}, the document to compile
     */
    public ProtoNetworkBuilder(Document doc) {
        this.doc = doc;
    }

    /**
     * Builds the {@link ProtoNetwork} at the {@link Document} level.
     *
     * @return {@link ProtoNetwork}, the built proto network
     */
    public ProtoNetwork buildProtoNetwork() {
        ProtoNetwork pn = new ProtoNetwork();

        // handle document header
        DocumentTable dt = pn.getDocumentTable();
        dt.addDocumentHeader(new DocumentHeader(doc.getHeader()));

        // handle namespaces
        NamespaceGroup nsg = doc.getNamespaceGroup();
        if (nsg != null) {
            NamespaceTable nt = pn.getNamespaceTable();

            if (nsg.getDefaultResourceLocation() != null) {
                org.openbel.framework.common.model.Namespace dns =
                        new Namespace("", nsg.getDefaultResourceLocation());
                nt.addNamespace(new TableNamespace(dns), 0);
            }

            // associate namespaces to document id 0
            final List<Namespace> nsl = nsg.getNamespaces();
            if (hasItems(nsl)) {
                for (org.openbel.framework.common.model.Namespace ns : nsl) {
                    nt.addNamespace(new TableNamespace(ns), 0);
                }
            }
        }

        // handle annotation definitions
        if (hasItems(doc.getDefinitions())) {
            for (AnnotationDefinition ad : doc.getDefinitions()) {
                AnnotationDefinitionTable adt =
                        pn.getAnnotationDefinitionTable();
                adt.addAnnotationDefinition(new TableAnnotationDefinition(ad),
                        0);
            }
        }

        // handle statement groups
        for (StatementGroup statementGroup : doc.getStatementGroups()) {
            buildProtoNetwork(pn, statementGroup,
                    new HashMap<String, Annotation>());
        }

        // create proto nodes
        final TermTable tt = pn.getTermTable();
        final ProtoNodeTable pnt = pn.getProtoNodeTable();
        final List<String> labels = tt.getTermValues();
        for (int i = 0, n = labels.size(); i < n; i++) {
            pnt.addNode(i, labels.get(i));
        }

        // create proto edges, process only simple edges now
        final Map<Integer, Integer> termNodeMap = pnt.getTermNodeIndex();
        final StatementTable st = pn.getStatementTable();
        final ProtoEdgeTable pet = pn.getProtoEdgeTable();
        final List<TableStatement> stmts = st.getStatements();
        for (int i = 0, n = stmts.size(); i < n; i++) {
            final TableStatement ts = stmts.get(i);
            final int subjectTerm = ts.getSubjectTermId();
            if (ts.getObjectTermId() != null) {
                final int objectTerm = ts.getObjectTermId();

                // find proto node index for subject / object terms
                final int source = termNodeMap.get(subjectTerm);
                final int target = termNodeMap.get(objectTerm);

                // create proto edge and add to table
                pet.addEdges(i, new ProtoEdgeTable.TableProtoEdge(source, ts
                        .getRelationshipName(), target));
            }
        }

        return pn;
    }

    /**
     * Builds the {@link ProtoNetwork} at the {@link StatementGroup} level.
     *
     * @param pn {@link ProtoNetwork}, the proto network
     * @param statementGroup {@link StatementGroup}, the statement group
     * @param annotationMap {@link Map}, the parent statement group annotations
     * accumulated to this point
     */
    protected void
            buildProtoNetwork(ProtoNetwork pn,
                    StatementGroup statementGroup,
                    Map<String, Annotation> annotationMap) {
        handleAnnotationGroup(pn, annotationMap,
                statementGroup.getAnnotationGroup());

        // build each statement
        for (Statement statement : statementGroup.getStatements()) {
            if (MacroStatementHelper.isMacroStatement(statement)) {
                try {
                    List<Statement> expandedStatements =
                            MacroStatementHelper.expand(statement);
                    for (Statement expandedStatement : expandedStatements) {
                        buildProtoNetwork(pn, expandedStatement, annotationMap);
                    }
                } catch (ProtoNetworkError e) {
                    //invalid statement expansion, skip
                }
            } else {
                buildProtoNetwork(pn, statement, annotationMap);
            }

        }

        // handle nested statement groups
        if (hasItems(statementGroup.getStatementGroups())) {
            for (StatementGroup childStatementGroup : statementGroup
                    .getStatementGroups()) {
                buildProtoNetwork(pn, childStatementGroup,
                        new HashMap<String, Annotation>(annotationMap));
            }
        }
    }

    /**
     * Builds the {@link ProtoNetwork} at the {@link Statement} level.
     *
     * @param pn {@link ProtoNetwork}, the proto network
     * @param statement {@link Statement}, the statement to evaluate
     * @param annotationMap {@link Map}, the parent statement group annotations
     * accumulated to this point
     * @return {@code int[]}, the statement indexes in the
     * {@link StatementTable}
     */
    protected int buildProtoNetwork(ProtoNetwork pn,
            Statement statement, Map<String, Annotation> annotationMap) {
        // build new list of annotations from statement group + individual
        // statement
        Map<String, Annotation> localAnnotationMap =
                new HashMap<String, Annotation>(
                        annotationMap);
        handleAnnotationGroup(pn, localAnnotationMap,
                statement.getAnnotationGroup());

        // assembly annotation definitions and values up to this point
        Set<AnnotationPair> annotationPairs = buildAnnotationPairs(
                pn, localAnnotationMap);

        // handle statement subject and object
        int subjectTerm = buildProtoNetwork(pn, statement.getSubject());

        // compiling only one document, so index is 0
        final int did = 0;
        final int sid;
        final StatementTable st = pn.getStatementTable();

        if (statement.getObject() == null) {
            // definitional statement
            sid =
                    st.addStatement(new TableStatement(subjectTerm), statement,
                            did);
        } else if (statement.getObject().getStatement() == null) {
            // simple statement
            int objectTerm = buildProtoNetwork(pn, statement.getObject()
                    .getTerm());
            sid = st.addStatement(new TableStatement(subjectTerm, statement
                    .getRelationshipType().getDisplayValue(),
                    objectTerm), statement, did);
        } else {
            // nested statement
            final Statement nested = statement.getObject().getStatement();
            int nestedSubject = buildProtoNetwork(pn, nested.getSubject());
            int nestedObject = buildProtoNetwork(pn, nested.getObject()
                    .getTerm());
            sid = st.addStatement(
                    new TableStatement(subjectTerm, statement
                            .getRelationshipType().getDisplayValue(),
                            nestedSubject, nested.getRelationshipType()
                                    .getDisplayValue(), nestedObject),
                    statement, did);
        }

        // associate annotations to new statement
        StatementAnnotationMapTable samt = pn.getStatementAnnotationMapTable();
        if (hasItems(annotationPairs)) {
            samt.addStatementAnnotation(sid, annotationPairs);
        }

        return sid;
    }

    /**
     * Builds the {@link ProtoNetwork} at the {@link Term} level. The found
     * parameters are handled via
     * {@link #handleParameter(ProtoNetwork, Parameter)} and indexed against the
     * term.
     *
     * @param pn {@link ProtoNetwork}, the proto network
     * @param term {@link Term}, the term to evaluate
     * @return {@code int}, the term index in the {@link TermTable}
     */
    public int buildProtoNetwork(final ProtoNetwork pn, final Term term) {
        int ti = pn.getTermTable().addTerm(term);

        List<Integer> pvals = new ArrayList<Integer>();
        List<Parameter> ps = term.getAllParametersLeftToRight();

        for (Parameter p : ps) {
            pvals.add(handleParameter(pn, p));
        }

        TermParameterMapTable tpmt = pn.getTermParameterMapTable();
        if (!tpmt.getTermParameterIndex().containsKey(ti)) {
            tpmt.addTermParameterMapping(ti, pvals);
        }

        return ti;
    }

    /**
     * Builds the {@link ProtoNetwork} at the {@link Parameter} level.
     *
     * @param pn {@link ProtoNetwork}, the proto network
     * @param p {@link Parameter}, the parameter to evaluate
     * @return {@code int}, the parameter index in the {@link ParameterTable}
     */
    protected int handleParameter(ProtoNetwork pn, Parameter p) {
        ParameterTable pt = pn.getParameterTable();

        Integer npidx;
        if (p.getNamespace() != null) {
            // add parameter with namespace
            TableNamespace ns = new TableNamespace(p.getNamespace());
            npidx = pt.addTableParameter(new TableParameter(ns, p.getValue()));
        } else if (doc.getNamespaceGroup() != null
                && doc.getNamespaceGroup().getDefaultResourceLocation() != null) {
            // add parameter with user-defined default namespace
            TableNamespace ns = new TableNamespace(doc.getNamespaceGroup()
                    .getDefaultResourceLocation());
            npidx = pt.addTableParameter(new TableParameter(ns, p.getValue()));
        } else {
            // add parameter with TEXT namespace
            npidx = pt.addTableParameter(new TableParameter(p.getValue()));
        }

        return npidx;
    }

    /**
     * Processes an {@link AnnotationGroup} for citations, evidence, and
     * user-defined annotations.
     *
     * @param protoNetwork {@link ProtoNetwork}, the proto network
     * @param annotationMap {@link Map}, the annotations map to add to
     * @param ag {@link AnnotationGroup}, the annotation group to process
     */
    protected void handleAnnotationGroup(ProtoNetwork protoNetwork,
            Map<String, Annotation> annotationMap, AnnotationGroup ag) {
        if (ag != null) {
            // handle citation
            if (ag.getCitation() != null) {
                handleCitationAnnotations(protoNetwork, ag.getCitation(),
                        annotationMap);
            }

            // handle evidence
            if (ag.getEvidence() != null) {
                handleEvidenceAnnotations(protoNetwork, ag.getEvidence(),
                        annotationMap);
            }

            // handle user annotations (which were already defined)
            if (hasItems(ag.getAnnotations())) {
                for (Annotation a : ag.getAnnotations()) {
                    annotationMap.put(a.getDefinition().getId(), a);
                }
            }
        }
    }

    /**
     * Build a {@link Set} of {@link AnnotationPair} objects from a {@link Map}
     * of {@link Annotation} values.
     *
     * @param protoNetwork {@link ProtoNetwork}, the proto network
     * @param annotationMap {@link Map}, the annotations map
     * @return {@link Set}, the {@link Set} of annotation definition and value
     * pairs
     */
    protected Set<AnnotationPair> buildAnnotationPairs(
            ProtoNetwork protoNetwork,
            Map<String, Annotation> annotationMap) {
        // build annotation value to annotation definition map
        Set<AnnotationPair> valueDefinitions =
                new LinkedHashSet<AnnotationPair>();
        AnnotationValueTable avt = protoNetwork.getAnnotationValueTable();
        AnnotationDefinitionTable adt =
                protoNetwork.getAnnotationDefinitionTable();

        Set<Annotation> annotationMapValues = new HashSet<Annotation>(
                annotationMap.values());
        for (Annotation a : annotationMapValues) {
            int adid =
                    adt.getDefinitionIndex().get(
                            new TableAnnotationDefinition(a.getDefinition()));
            int aid = avt.addAnnotationValue(adid, a.getValue());
            valueDefinitions.add(new AnnotationPair(adid, aid));
        }

        return valueDefinitions;
    }

    /**
     * Breaks down {@link Citation} annotations into individual name-value
     * annotations.
     *
     * @param protoNetwork {@link ProtoNetwork}, the proto network
     * @param citation {@link Citation}, the citation
     * @param annotationMap {@link Map}, the annotations map to add to
     */
    protected void handleCitationAnnotations(ProtoNetwork protoNetwork,
            Citation citation, Map<String, Annotation> annotationMap) {
        AnnotationDefinitionTable adt = protoNetwork
                .getAnnotationDefinitionTable();

        final CommonModelFactory af = CommonModelFactory.getInstance();

        // handle citation name (always exists)
        CitationNameAnnotationDefinition cnad =
                new CitationNameAnnotationDefinition();
        adt.addAnnotationDefinition(new TableAnnotationDefinition(
                cnad), 0);
        annotationMap.put(
                CitationNameAnnotationDefinition.ANNOTATION_DEFINITION_ID,
                af.createAnnotation(citation.getName(), cnad));

        // handle citation id (if not null)
        if (citation.getReference() != null) {
            CitationReferenceAnnotationDefinition ciad =
                    new CitationReferenceAnnotationDefinition();
            adt.addAnnotationDefinition(new TableAnnotationDefinition(ciad),
                    0);
            annotationMap
                    .put(CitationReferenceAnnotationDefinition.ANNOTATION_DEFINITION_ID,
                            af.createAnnotation(citation.getReference(), ciad));
        }

        // handle citation comment (if not null)
        if (citation.getComment() != null) {
            CitationCommentAnnotationDefinition cdad =
                    new CitationCommentAnnotationDefinition();
            adt.addAnnotationDefinition(new TableAnnotationDefinition(cdad),
                    0);
            annotationMap
                    .put(CitationCommentAnnotationDefinition.ANNOTATION_DEFINITION_ID,
                            af.createAnnotation(citation.getComment(), cdad));
        }

        // handle citation date (if not null)
        if (citation.getDate() != null) {
            CitationDateAnnotationDefinition cdtad =
                    new CitationDateAnnotationDefinition();
            adt.addAnnotationDefinition(
                    new TableAnnotationDefinition(cdtad), 0);
            annotationMap.put(
                    CitationDateAnnotationDefinition.ANNOTATION_DEFINITION_ID,
                    af.createAnnotation(
                            datef.format(citation.getDate().getTime()),
                            cdtad));
        }

        // handle citation authors (if not null)
        if (citation.getAuthors() != null) {
            CitationAuthorsAnnotationDefinition cuad =
                    new CitationAuthorsAnnotationDefinition();
            adt.addAnnotationDefinition(new TableAnnotationDefinition(cuad),
                    0);
            annotationMap
                    .put(CitationAuthorsAnnotationDefinition.ANNOTATION_DEFINITION_ID,
                            af.createAnnotation(PackUtils.packValues(citation
                                    .getAuthors()), cuad));
        }

        // handle citation type (if not null)
        if (citation.getType() != null) {
            CitationTypeAnnotationDefinition ctad =
                    new CitationTypeAnnotationDefinition();
            adt.addAnnotationDefinition(new TableAnnotationDefinition(ctad),
                    0);
            annotationMap.put(
                    CitationTypeAnnotationDefinition.ANNOTATION_DEFINITION_ID,
                    af.createAnnotation(citation.getType().getDisplayValue(),
                            ctad));
        }
    }

    /**
     * Breaks down {@link Evidence} annotations into individual name-value
     * annotations.
     *
     * @param protoNetwork {@link ProtoNetwork}, the proto network
     * @param evidence {@link Evidence}, the evidence
     * @param annotationMap {@link Map}, the annotations map to add to
     */
    protected void handleEvidenceAnnotations(ProtoNetwork protoNetwork,
            Evidence evidence, Map<String, Annotation> annotationMap) {
        AnnotationDefinitionTable adt =
                protoNetwork.getAnnotationDefinitionTable();
        EvidenceAnnotationDefinition ead =
                new EvidenceAnnotationDefinition();
        adt.addAnnotationDefinition(new TableAnnotationDefinition(ead), 0);
        annotationMap.put(
                EvidenceAnnotationDefinition.ANNOTATION_DEFINITION_ID,
                CommonModelFactory.getInstance().createAnnotation(
                        evidence.getValue(), ead));
    }

    /**
     * Helper class to handle expansion of macro statements.
     */
    private static class MacroStatementHelper {

        /**
         * Expands a macro statement to the actual statements. If the statement
         * cannot be expanded, the original statement is returned wrapped in a
         * List.
         *
         * @param statement
         * @return {@link List} list of expanded statements
         */
        public static List<Statement> expand(Statement statement)
                throws ProtoNetworkError {
            List<Statement> statements = null;
            if (statement != null) {
                switch (statement.getRelationshipType()) {
                case HAS_MEMBERS:
                    statements = expandHasMembers(statement);
                    break;
                case HAS_COMPONENTS:
                    statements = expandHasComponents(statement);
                    break;
                default:
                    statements = new ArrayList<Statement>();
                    statements.add(statement);
                }
            }
            return statements;
        }

        /**
         * Determines if <tt>statement</tt> is a macro statement which
         * can be expanded.
         *
         * @param statement {@link Statement}, the statement to determine if
         * it can be expanded
         * @return the macro statement result, <tt>true</tt> if
         * <tt>statement</tt> is a macro, <tt>false</tt> if <tt>statement</tt>
         * is <tt>null</tt>, <tt>false</tt> if <tt>statement</tt> is not macro
         */
        public static boolean isMacroStatement(Statement statement) {
            if (statement == null) {
                return false;
            }
            RelationshipType rel = statement.getRelationshipType();
            return HAS_MEMBERS == rel || HAS_COMPONENTS == rel;
        }

        /**
         * Expand a statement with a {@link RelationshipType#HAS_MEMBERS}
         * relationship.
         *
         * @param statement {@link Statement}, the statement to expand
         * @return {@link List} of {@link Statement}, the expanded statements
         * @throws ProtoNetworkError Thrown if statement expansion cannot be
         * carried out
         */
        protected static List<Statement> expandHasMembers(Statement statement)
                throws ProtoNetworkError {
            return expandTermList(statement, HAS_MEMBER);
        }

        /**
         * Expand a statement with a {@link RelationshipType#HAS_COMPONENTS}
         * relationship.
         *
         * @param statement {@link Statement}, the statement to expand
         * @return {@link List} of {@link Statement}, the expanded statements
         * @throws ProtoNetworkError Thrown if statement expansion cannot be
         * carried out
         */
        protected static List<Statement>
                expandHasComponents(Statement statement)
                        throws ProtoNetworkError {
            return expandTermList(statement, HAS_COMPONENT);
        }

        /**
         * Expand terms for <tt>statement</tt> into separate {@link Statement}.
         *
         * @param statement {@link Statement}, the statement to expand
         * @param relationshipType {@link RelationshipType}, the relationship
         * type
         * @return {@link List} of {@link Statement}, the expanded statements
         * @throws ProtoNetworkError Thrown if statement expansion cannot be
         * carried out
         */
        protected static List<Statement> expandTermList(Statement statement,
                RelationshipType relationshipType) throws ProtoNetworkError {
            List<Statement> statements = new ArrayList<Statement>();
            if (statement.getObject() == null
                    || statement.getObject().getTerm() == null) {
                throw new ProtoNetworkError(
                        "Unable to expand macro statement",
                        "Object term is null");
            }
            if (statement.getObject().getTerm().getFunctionEnum() != LIST) {
                throw new ProtoNetworkError(
                        "Unable to expand macro statement",
                        "Object term is not a term list");
            }
            List<BELObject> objectTerms =
                    statement.getObject().getTerm().getFunctionArguments();

            if (noItems(objectTerms))
                return statements;

            for (BELObject bo : objectTerms) {
                Statement s = statement.clone();
                s.setRelationshipType(relationshipType);
                s.setObject(new Statement.Object((Term) bo));
                statements.add(s);
            }
            return statements;
        }
    }

}
