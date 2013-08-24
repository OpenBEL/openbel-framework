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
package org.openbel.framework.tools.kamstore;

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.openbel.framework.api.Kam;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.AnnotationFilterCriteria;
import org.openbel.framework.api.KamStore;
import org.openbel.framework.api.KamStoreException;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.Reportable;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.CitationAuthorsAnnotationDefinition;
import org.openbel.framework.common.model.CitationCommentAnnotationDefinition;
import org.openbel.framework.common.model.CitationDateAnnotationDefinition;
import org.openbel.framework.common.model.CitationNameAnnotationDefinition;
import org.openbel.framework.common.model.CitationTypeAnnotationDefinition;
import org.openbel.framework.internal.KAMStoreConstants;
import org.openbel.framework.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.internal.KAMStoreDaoImpl.Annotation;
import org.openbel.framework.internal.KAMStoreDaoImpl.AnnotationDefinitionType;
import org.openbel.framework.internal.KAMStoreDaoImpl.AnnotationType;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelDocumentInfo;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelStatement;

@SuppressWarnings("unused")
public final class KamSummarizer {
    private static final String RANDOM_VALUE = RandomStringUtils
            .randomAlphabetic(30);

    private final Reportable reportable;

    /**
     * Constructs the KamSummarizer
     */
    public KamSummarizer(final Reportable reportable) {
        this.reportable = reportable;
    }

    public void printKamSummary(final KamStore kamStore,
            final KamSummary summary)
            throws InvalidArgument, KamStoreException {

        reportable.output(String.format("\n\nSummarizing KAM: %s", summary
                .getKamInfo().getName()));
        reportable.output(String.format("\tLast Compiled:\t%s", summary
                .getKamInfo().getLastCompiled()));
        reportable.output(String.format("\tDescription:\t%s", summary
                .getKamInfo().getDescription()));
        reportable.output();
        reportable.output(String.format("\tNum BEL Documents:\t%d",
                summary.getNumOfBELDocuments()));
        reportable.output(String.format("\tNum Namespaces:\t\t%d",
                summary.getNumOfNamespaces()));
        reportable.output(String.format("\tNum Annotation Types:\t\t%d",
                summary.getNumOfAnnotationTypes()));

        if (hasItems(summary.getAnnotations())) {
            reportable.output("\tAnnotation Types:");
            for (AnnotationType annotation : summary.getAnnotations()) {
                reportable
                        .output(String.format("\t\t%s", annotation.getName()));

                switch (annotation.getAnnotationDefinitionType()) {
                case ENUMERATION:
                    reportable.output("\t\t\tType: List");
                    break;
                case REGULAR_EXPRESSION:
                    reportable.output("\t\t\tType: Pattern");
                    break;
                case URL:
                    reportable.output("\t\t\tType: URL");
                    reportable.output("\t\t\tLocation: " + annotation.getUrl());
                    break;
                }

                if (annotation.getAnnotationDefinitionType() != AnnotationDefinitionType.URL) {
                    reportable.output(String.format("\t\t\tDescription: %s",
                            annotation.getDescription()));
                    reportable.output(String.format("\t\t\tUsage: %s",
                            annotation.getUsage()));
                    reportable.output(String
                            .format("\t\t\tDomain: %s",
                                    buildAnnotationDomain(kamStore, summary,
                                            annotation)));
                }
            }
        }

        reportable.output();
        reportable.output(String.format("\tNum Statements:\t%d",
                summary.getNumOfBELStatements()));
        printNetworkSummary(summary, reportable);

        //print filtered kam summaries if they are available
        if (summary.getFilteredKamSummaries() != null
                && !summary.getFilteredKamSummaries().isEmpty()) {
            reportable.output("\nSummary by Annotation Type");

            for (String filteredKam : summary.getFilteredKamSummaries()
                    .keySet()) {
                reportable.output(filteredKam + ":");
                KamSummary filteredSummary =
                        summary.getFilteredKamSummaries().get(filteredKam);
                reportable.output(String.format("\tNum Statements:\t%d",
                        filteredSummary.getNumOfBELStatements()));
                reportable.output(String.format("\tNum Edges:\t%d",
                        filteredSummary.getNumOfEdges()));
            }
        }
    }

    public static KamSummary
            summarizeKam(final KamStore kamStore, final Kam kam)
                    throws InvalidArgument, KamStoreException {
        KamSummary summary;
        summary = new KamSummary();
        summary.setKamInfo(kam.getKamInfo());

        Set<Integer> stmtIds = new HashSet<Integer>();
        for (KamEdge edge : kam.getEdges()) {
            for (BelStatement stmt : kamStore.getSupportingEvidence(edge)) {
                stmtIds.add(stmt.getId());
            }
        }

        summary.setNumOfBELStatements(stmtIds.size());
        summary.setNumOfNodes(kam.getNodes().size());
        summary.setNumOfEdges(kam.getEdges().size());
        summary.setNumOfBELDocuments(kamStore.getBelDocumentInfos(
                kam.getKamInfo()).size());
        summary.setNumOfNamespaces(kamStore.getNamespaces(kam.getKamInfo())
                .size());

        List<AnnotationType> annotationTypes =
                kamStore.getAnnotationTypes(kam.getKamInfo());
        Collections.sort(annotationTypes, new Comparator<AnnotationType>() {

            @Override
            public int compare(AnnotationType at1, AnnotationType at2) {
                if (at1 == null || at1.getName() == null) {
                    return -1;
                } else if (at2 == null || at2.getName() == null) {
                    return 1;
                }

                return at1.getName().compareToIgnoreCase(at2.getName());
            }
        });

        //filter out multiple citation annotation types
        List<AnnotationType> filteredAnnotationTypes =
                new ArrayList<AnnotationType>();
        for (AnnotationType at : annotationTypes) {
            if (!(CitationAuthorsAnnotationDefinition.ANNOTATION_DEFINITION_ID
                    .equals(at.getName())
                    ||
                    CitationCommentAnnotationDefinition.ANNOTATION_DEFINITION_ID
                            .equals(at.getName())
                    ||
                    CitationDateAnnotationDefinition.ANNOTATION_DEFINITION_ID
                            .equals(at.getName())
                    ||
                    CitationNameAnnotationDefinition.ANNOTATION_DEFINITION_ID
                            .equals(at.getName()) || CitationTypeAnnotationDefinition.ANNOTATION_DEFINITION_ID
                        .equals(at.getName()))) {
                //filter all citation annotation type but the reference to get a more accurate count of citations
                //reference is required for a citation
                filteredAnnotationTypes.add(at);
            }
        }
        summary.setAnnotations(filteredAnnotationTypes);
        summary.setNumOfAnnotationTypes(filteredAnnotationTypes.size());

        //breakdown human, mouse, rat and summary sub-network
        summary.setFilteredKamSummaries(summarizeAnnotationSpecificEdges(
                kamStore, kam, filteredAnnotationTypes));

        return summary;
    }

    /**
     * Summarize nodes and edges
     * @param edges
     * @param annotationStatements
     * @return
     */
    public static KamSummary summarizeKamNetwork(Collection<KamEdge> edges,
            int statementCount) {
        KamSummary summary = new KamSummary();

        Set<KamNode> nodes = new HashSet<KamNode>(); //unique set of nodes
        for (KamEdge edge : edges) {
            nodes.add(edge.getSourceNode());
            nodes.add(edge.getTargetNode());
        }

        summary.setNumOfBELStatements(statementCount);
        summary.setNumOfNodes(nodes.size());
        summary.setNumOfEdges(edges.size());
        return summary;
    }

    /**
     * returns the number of rnaAbundance nodes.
     * @param nodes
     * @return
     */
    private int getNumRnaNodes(Collection<KamNode> nodes) {
        int count = 0;
        for (KamNode node : nodes) {
            if (node.getFunctionType() == FunctionEnum.RNA_ABUNDANCE) {
                count++;
            }
        }
        return count;
    }

    /**
     * return number of protein with phosphorylation modification
     * @param nodes
     * @return
     */
    private int getPhosphoProteinNodes(Collection<KamNode> nodes) {
        int count = 0;
        for (KamNode node : nodes) {
            if (node.getFunctionType() == FunctionEnum.PROTEIN_ABUNDANCE
                    && node.getLabel().indexOf("proteinModification(P") > -1) {
                count++;
            }
        }
        return count;
    }

    /**
     * returns number unique gene reference
     * @param edges
     * @return
     */
    private int getUniqueGeneReference(Collection<KamNode> nodes) {
        //count all protienAbundance reference
        Set<String> uniqueLabels = new HashSet<String>();
        for (KamNode node : nodes) {
            if (node.getFunctionType() == FunctionEnum.PROTEIN_ABUNDANCE
                    && StringUtils.countMatches(node.getLabel(), "(") == 1
                    && StringUtils.countMatches(node.getLabel(), ")") == 1) {
                uniqueLabels.add(node.getLabel());
            }
        }

        return uniqueLabels.size();
    }

    /**
     * returns number of inceases and directly_increases edges.
     * @param edges
     * @return
     */
    private int getIncreasesEdges(Collection<KamEdge> edges) {
        int count = 0;
        for (KamEdge edge : edges) {
            if (edge.getRelationshipType() == RelationshipType.INCREASES
                    || edge.getRelationshipType() == RelationshipType.DIRECTLY_INCREASES) {
                count++;
            }
        }
        return count;
    }

    /**
     * returns number of deceases and directly_decreases edges.
     * @param edges
     * @return
     */
    private int getDecreasesEdges(Collection<KamEdge> edges) {
        int count = 0;
        for (KamEdge edge : edges) {
            if (edge.getRelationshipType() == RelationshipType.DECREASES
                    || edge.getRelationshipType() == RelationshipType.DIRECTLY_DECREASES) {
                count++;
            }
        }
        return count;
    }

    private int getUpstreamCount(String label, Collection<KamEdge> edges) {
        int count = 0;
        for (KamEdge edge : edges) {
            if (edge.getSourceNode().getLabel().equals(label) && isCausal(edge)) {
                count++;
            }
        }
        return count;
    }

    /**
     * returns nodes with causal downstream to rnaAbundance() nodes.
     * @param edges
     * @return
     */
    private Map<String, Integer> getTranscriptionalControls(
            Collection<KamEdge> edges) {
        Map<String, Integer> controlCountMap = new HashMap<String, Integer>();
        for (KamEdge edge : edges) {
            if (edge.getTargetNode().getFunctionType() == FunctionEnum.RNA_ABUNDANCE
                    && isCausal(edge)) {
                if (controlCountMap
                        .containsKey(edge.getSourceNode().getLabel())) {
                    int count =
                            controlCountMap
                                    .get(edge.getSourceNode().getLabel());
                    count = count + 1;
                    controlCountMap.put(edge.getSourceNode().getLabel(), count);
                } else {
                    controlCountMap.put(edge.getSourceNode().getLabel(), 1);
                }
            }
        }

        return controlCountMap;
    }

    /**
     * returns nodes with 4+ causal downstream to rnaAbundance() nodes.
     * @param edges
     * @return
     */
    private Map<String, Integer> getHypotheses(Collection<KamEdge> edges) {
        Map<String, Integer> controlCountMap =
                getTranscriptionalControls(edges);
        Map<String, Integer> hypCountMap = new HashMap<String, Integer>();
        for (String hyp : controlCountMap.keySet()) {
            if (controlCountMap.get(hyp) >= 4) {
                hypCountMap.put(hyp, controlCountMap.get(hyp));
            }
        }
        return hypCountMap;
    }

    /**
     * returns true if the edge has one of the 4 causal relationship types.
     * @param edge
     * @return
     */
    private boolean isCausal(KamEdge edge) {
        return edge.getRelationshipType() == RelationshipType.INCREASES
                || edge.getRelationshipType() == RelationshipType.DIRECTLY_INCREASES
                || edge.getRelationshipType() == RelationshipType.DECREASES
                || edge.getRelationshipType() == RelationshipType.DIRECTLY_DECREASES;
    }

    private Collection<KamEdge> filterEdges(final KamStore kamStore,
            final Kam kam, final String annotationName)
            throws KamStoreException {
        KamFilter filter = kam.getKamInfo().createKamFilter();
        AnnotationFilterCriteria criteria =
                new AnnotationFilterCriteria(getAnnotationType(kamStore, kam,
                        annotationName));
        criteria.add(RANDOM_VALUE);
        criteria.setInclude(false);
        filter.add(criteria);
        Kam filteredKam = kamStore.getKam(kam.getKamInfo(), filter);
        return filteredKam.getEdges();
    }

    /**
     * Returns the first annotation type matching the specified name
     * @param kam
     * @param name
     * @return AnnotationType, maybe null
     */
    private AnnotationType getAnnotationType(final KamStore kamStore,
            final Kam kam, final String name) throws KamStoreException {

        AnnotationType annoType = null;
        List<BelDocumentInfo> belDocs =
                kamStore.getBelDocumentInfos(kam.getKamInfo());
        //loop through all BEL documents used for this KAM
        for (BelDocumentInfo doc : belDocs) {
            //check annotation type on each document
            List<AnnotationType> annoTypes = doc.getAnnotationTypes();
            for (AnnotationType a : annoTypes) {
                if (a.getName().equals(name)) {
                    annoType = a;
                    break;
                }
            }
            if (annoType != null) {
                break;
            }
        }
        return annoType;
    }

    private static Map<String, KamSummary> summarizeAnnotationSpecificEdges(
            final KamStore kamStore, final Kam kam,
            final List<AnnotationType> annotations)
            throws KamStoreException {

        Map<String, KamSummary> summaries =
                new LinkedHashMap<String, KamSummary>();
        for (AnnotationType annotation : annotations) {

            if (ArrayUtils.contains(
                    KAMStoreConstants.CITATION_ANNOTATION_DEFINITION_IDS,
                    annotation.getName())) {
                continue;
            }

            Set<KamEdge> annotationEdges = new HashSet<Kam.KamEdge>();
            Set<Integer> annotationStatements = new HashSet<Integer>();
            for (final KamEdge edge : kam.getEdges()) {
                List<BelStatement> supportingEvidence =
                        kamStore.getSupportingEvidence(edge);
                if (hasItems(supportingEvidence)) {
                    for (BelStatement stmt : supportingEvidence) {
                        if (hasItems(stmt.getAnnotationList())) {
                            for (Annotation stmtAnnotation : stmt
                                    .getAnnotationList()) {
                                if (annotation.getName().equals(
                                        stmtAnnotation.getAnnotationType()
                                                .getName())) {

                                    annotationEdges.add(edge);
                                    annotationStatements.add(stmt.getId());
                                }
                            }
                        }
                    }
                }
            }

            KamSummary annotationNetworkSummary =
                    summarizeKamNetwork(annotationEdges,
                            annotationStatements.size());
            summaries.put(annotation.getName(), annotationNetworkSummary);
        }

        return summaries;
    }

    private String buildAnnotationDomain(final KamStore kamStore,
            final KamSummary summary,
            final AnnotationType annotationType) throws KamStoreException {
        List<String> domainValues =
                kamStore.getAnnotationTypeDomainValues(summary.getKamInfo(),
                        annotationType);

        summary.getAnnotationDomains().put(annotationType, domainValues);

        StringBuilder sb = new StringBuilder();
        for (String domainValue : domainValues) {
            sb.append(domainValue).append("|");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private void printNetworkSummary(final KamSummary summary,
            final Reportable reportable) throws InvalidArgument,
            KamStoreException {
        reportable.output(String.format("\tNum Nodes:\t%d",
                summary.getNumOfNodes()));
        reportable.output(String.format("\tNum Edges:\t%d",
                summary.getNumOfEdges()));
        reportable.output();
    }

    private String printEdge(final KamEdge kamEdge) {
        return String.format("%s %s %s", kamEdge.getSourceNode().getLabel(),
                kamEdge.getRelationshipType().getDisplayValue(), kamEdge
                        .getTargetNode().getLabel());
    }

    private void printBelStatement(final BelStatement belStatement) {
        reportable.output(String.format("\tBEL Statement: %d\t%s",
                belStatement.getId(), belStatement));
        reportable.output(String.format("\t\tNum Annotations: %d", belStatement
                .getAnnotationList().size()));
        for (Annotation annotation : belStatement.getAnnotationList()) {
            reportable.output(String.format("\t\t%s -> %s", annotation
                    .getAnnotationType().getName(), annotation.getValue()));
        }
    }
}
