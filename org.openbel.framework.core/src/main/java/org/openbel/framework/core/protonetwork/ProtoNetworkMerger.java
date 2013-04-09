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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.protonetwork.model.AnnotationDefinitionTable;
import org.openbel.framework.common.protonetwork.model.AnnotationDefinitionTable.TableAnnotationDefinition;
import org.openbel.framework.common.protonetwork.model.AnnotationValueTable;
import org.openbel.framework.common.protonetwork.model.AnnotationValueTable.TableAnnotationValue;
import org.openbel.framework.common.protonetwork.model.DocumentTable;
import org.openbel.framework.common.protonetwork.model.DocumentTable.DocumentHeader;
import org.openbel.framework.common.protonetwork.model.NamespaceTable;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ParameterTable;
import org.openbel.framework.common.protonetwork.model.ParameterTable.TableParameter;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable.TableProtoEdge;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNodeTable;
import org.openbel.framework.common.protonetwork.model.StatementAnnotationMapTable;
import org.openbel.framework.common.protonetwork.model.StatementAnnotationMapTable.AnnotationPair;
import org.openbel.framework.common.protonetwork.model.StatementTable;
import org.openbel.framework.common.protonetwork.model.StatementTable.TableStatement;
import org.openbel.framework.common.protonetwork.model.TermParameterMapTable;
import org.openbel.framework.common.protonetwork.model.TermTable;

/**
 * ProtoNetworkMerger merges two {@link ProtoNetwork} objects together. This
 * operation is mostly a union of the two proto networks, except for the term
 * tables which treat each entry as unique. For example, given the two
 * statements:
 * <pre>
 * geneAbundance(EG:207) :> rnaAbundance(EG:208)
 * geneAbundance(EG:208) :> rnaAbundance(EG:310)
 * </pre>
 * the merged parameter will be:
 * <pre>
 * (union)
 * 207
 * 208
 * 210
 * </pre>
 * whereas, the merged terms will be:
 * <pre>
 * (additive)
 * geneAbundance(#)
 * rnaAbundance(#)
 * geneAbundance(#)
 * rnaAbundance(#)
 * </pre>
 * The result of the merge will be in <tt>protoNetwork1</tt>.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ProtoNetworkMerger {

    /**
     * Merge the second proto network, <tt>protoNetwork2</tt>, into the first
     * proto network, <tt>protoNetwork1</tt>.  This will union each field of
     * the two proto networks, except terms, which will be combined with
     * possible overlap.
     *
     * @param protoNetwork1 {@link ProtoNetwork}, the first proto network,
     * which cannot be null
     * @param protoNetwork2 {@link ProtoNetwork}, the second proto network
     * which cannot be null
     * @throws InvalidArgument Thrown if either <tt>protoNetwork1</tt> or
     * <tt>protoNetwork2</tt> is null.
     */
    public void merge(ProtoNetwork protoNetwork1, ProtoNetwork protoNetwork2) {
        if (protoNetwork1 == null) {
            throw new InvalidArgument("protoNetwork1", protoNetwork1);
        }

        if (protoNetwork2 == null) {
            throw new InvalidArgument("protoNetwork2", protoNetwork2);
        }

        DocumentTable dt = protoNetwork2.getDocumentTable();

        for (DocumentHeader dh : dt.getDocumentHeaders()) {
            // add new document header
            int did = protoNetwork1.getDocumentTable().addDocumentHeader(dh);

            // union namespaces
            NamespaceTable nt = protoNetwork2.getNamespaceTable();
            for (TableNamespace ns : nt.getNamespaces()) {
                protoNetwork1.getNamespaceTable().addNamespace(ns, did);
            }

            // union annotation definition values
            Map<Integer, Integer> adremap = new HashMap<Integer, Integer>();
            AnnotationDefinitionTable adt =
                    protoNetwork2.getAnnotationDefinitionTable();
            for (TableAnnotationDefinition ad : adt.getAnnotationDefinitions()) {
                Integer oldDefinitionId = adt.getDefinitionIndex().get(ad);
                Integer newDefinitionId = protoNetwork1
                        .getAnnotationDefinitionTable()
                        .addAnnotationDefinition(ad, did);

                adremap.put(oldDefinitionId, newDefinitionId);
            }

            // union annotation values
            AnnotationValueTable avt = protoNetwork2.getAnnotationValueTable();
            for (TableAnnotationValue av : avt.getAnnotationValues()) {
                Integer newDefinitionId =
                        adremap.get(av.getAnnotationDefinitionId());

                protoNetwork1.getAnnotationValueTable().addAnnotationValue(
                        newDefinitionId, av.getAnnotationValue());
            }

            // union statements
            StatementTable statementTable = protoNetwork2.getStatementTable();
            List<TableStatement> statements = statementTable.getStatements();
            List<String> terms = protoNetwork2.getTermTable().getTermValues();
            Map<Integer, Integer> termMap = new HashMap<Integer, Integer>(
                    terms.size());
            for (int j = 0; j < statements.size(); j++) {
                mergeStatement(j, statements.get(j), protoNetwork1,
                        protoNetwork2, did, termMap);
            }
        }
    }

    /**
     * Merge the statement into the first {@link ProtoNetwork}
     * <tt>protoNetwork1</tt>.
     *
     * @param statementIndex <tt>int</tt>, the statement index
     * @param statement {@link TableStatement}, the table statement
     * to merge
     * @param protoNetwork1 {@link ProtoNetwork}, the first proto network to
     * merge into
     * @param protoNetwork2 {@link ProtoNetwork}, the second proto network to
     * merge from
     * @param documentId <tt>int</tt>, the new merged document index
     * @return {@link Integer}, the new merged statement index
     */
    private Integer mergeStatement(int statementIndex,
            TableStatement statement,
            ProtoNetwork protoNetwork1, ProtoNetwork protoNetwork2,
            int documentId, Map<Integer, Integer> termMap) {

        // always merge subject term since it's present in all statement types
        Integer newSubjectTermId = mergeTerm(statement.getSubjectTermId(),
                protoNetwork1, protoNetwork2,
                documentId, termMap);

        int newStatementIndex = 0;
        Statement stmtToMerge =
                protoNetwork2.getStatementTable().getIndexedStatements()
                        .get(statementIndex);
        if (statement.getRelationshipName() == null) {
            // merge definitional statement, no create of proto edge
            newStatementIndex =
                    protoNetwork1.getStatementTable().addStatement(
                            new TableStatement(
                                    newSubjectTermId, null, null), stmtToMerge,
                            documentId);
        } else if (statement.getObjectTermId() != null) {
            // merge simple statement
            Integer newObjectTermId = mergeTerm(
                    Integer.valueOf(statement.getObjectTermId()),
                    protoNetwork1, protoNetwork2, documentId, termMap);

            newStatementIndex = protoNetwork1.getStatementTable().addStatement(
                    new TableStatement(newSubjectTermId,
                            statement.getRelationshipName(), newObjectTermId),
                    stmtToMerge, documentId);
        } else {
            // merge nested statement, no creation of proto edge

            // merge nested subject
            Integer newNestedSubjectTermId = mergeTerm(
                    Integer.valueOf(statement.getNestedSubject()),
                    protoNetwork1, protoNetwork2, documentId, termMap);
            // merge nested object
            Integer newNestedObjectTermId = mergeTerm(
                    Integer.valueOf(statement.getNestedObject()),
                    protoNetwork1, protoNetwork2, documentId, termMap);

            newStatementIndex = protoNetwork1.getStatementTable().addStatement(
                    new TableStatement(newSubjectTermId,
                            statement.getRelationshipName(),
                            newNestedSubjectTermId,
                            statement.getNestedRelationship(),
                            newNestedObjectTermId),
                    stmtToMerge, documentId);
        }

        // remap edges for statement, if any exist
        ProtoEdgeTable et = protoNetwork2.getProtoEdgeTable();
        Map<Integer, Set<Integer>> stmtEdges = et.getStatementEdges();
        Set<Integer> edgeIndices = stmtEdges.get(statementIndex);
        if (hasItems(edgeIndices)) {
            remapEdges(protoNetwork1, protoNetwork2, documentId, termMap,
                    newStatementIndex, et.getProtoEdges(), edgeIndices);
        }

        // remap annotation definition + value
        Set<AnnotationPair> aps = protoNetwork2
                .getStatementAnnotationMapTable()
                .getStatementAnnotationPairsIndex().get(statementIndex);
        Set<AnnotationPair> newaps =
                new HashSet<StatementAnnotationMapTable.AnnotationPair>();

        if (hasItems(aps)) {
            for (AnnotationPair ap : aps) {
                // add original annotation definition to acquire new id
                TableAnnotationDefinition ad =
                        protoNetwork2
                                .getAnnotationDefinitionTable()
                                .getIndexDefinition().get(
                                        ap.getAnnotationDefinitionId());
                int newadid = protoNetwork1.getAnnotationDefinitionTable()
                        .addAnnotationDefinition(ad, documentId);

                // add original annotation value to acquire new id
                TableAnnotationValue av =
                        protoNetwork2.getAnnotationValueTable().getIndexValue()
                                .get(ap.getAnnotationValueId());
                int newavid = protoNetwork1.getAnnotationValueTable()
                        .addAnnotationValue(newadid,
                                av.getAnnotationValue());

                newaps.add(new AnnotationPair(newadid, newavid));
            }
        }

        // set new annotation pairs against new statement index
        protoNetwork1.getStatementAnnotationMapTable().addStatementAnnotation(
                newStatementIndex, newaps);

        return newStatementIndex;
    }

    /**
     * Remaps {@link TableProtoEdge proto edges} for a
     * {@link TableStatement statement}.  A new statement index is created from
     * a merge which requires the old {@link TableProtoEdge proto edges} to be
     * associated with it.
     *
     * @see https://github.com/OpenBEL/openbel-framework/issues/49
     * @param protoNetwork1 {@link ProtoNetwork}; merge into
     * @param protoNetwork2 {@link ProtoNetwork}; merge from
     * @param documentId {@code int}; bel document id
     * @param termMap {@link Map} of old term id to new proto node id
     * @param newStatementIndex {@code int} new merged statement id
     * @param edges {@link List}; merging statement's
     * {@link TableProtoEdge edges}
     * @param edgeIndices {@link Set}; set of old statement's edge indices
     */
    private void remapEdges(ProtoNetwork protoNetwork1,
            ProtoNetwork protoNetwork2, int documentId,
            Map<Integer, Integer> termMap, int newStatementIndex,
            List<TableProtoEdge> edges, Set<Integer> edgeIndices) {
        ProtoNodeTable nt = protoNetwork2.getProtoNodeTable();
        Map<Integer, Integer> nodeTermIndex = nt.getNodeTermIndex();

        TableProtoEdge[] remappedEdges = new TableProtoEdge[edgeIndices.size()];
        int i = 0;
        for (Integer edgeIndex : edgeIndices) {
            TableProtoEdge edge = edges.get(edgeIndex);
            int sourceBefore = edge.getSource();
            int targetBefore = edge.getTarget();

            Integer sourceTerm = nodeTermIndex.get(sourceBefore);
            Integer targetTerm = nodeTermIndex.get(targetBefore);
            Integer newSource = termMap.get(sourceTerm);
            if (newSource == null) {
                newSource = mergeTerm(sourceTerm, protoNetwork1, protoNetwork2,
                        documentId, termMap);
            }

            Integer newTarget = termMap.get(targetTerm);
            if (newTarget == null) {
                newTarget = mergeTerm(targetTerm, protoNetwork1, protoNetwork2,
                        documentId, termMap);
            }

            remappedEdges[i++] = new TableProtoEdge(newSource, edge.getRel(),
                    newTarget);
        }
        ProtoEdgeTable edgeTable = protoNetwork1.getProtoEdgeTable();
        edgeTable.addEdges(newStatementIndex, remappedEdges);
    }

    /**
     * Merge the term into the first {@link ProtoNetwork}
     * <tt>protoNetwork1</tt>.
     *
     * @param termId {@link Integer}, the original term index
     * @param protoNetwork1 {@link ProtoNetwork}, the first proto network
     * to merge into
     * @param protoNetwork2 {@link ProtoNetwork}, the second proto network
     * to merge from
     * @param documentId <tt>int</tt>, the new merged document index
     * @return {@link Integer}, the new merged term index
     */
    private Integer mergeTerm(Integer termId, ProtoNetwork protoNetwork1,
            ProtoNetwork protoNetwork2, int documentId,
            Map<Integer, Integer> termMap) {
        TermTable termTable = protoNetwork2.getTermTable();
        TermParameterMapTable mapTable =
                protoNetwork2.getTermParameterMapTable();
        ParameterTable parameterTable = protoNetwork2.getParameterTable();

        Term termToMerge = termTable.getIndexedTerms().get(termId);

        // tables for proto network we're merging into (1)
        final TermTable mtt = protoNetwork1.getTermTable();
        final ParameterTable mpt = protoNetwork1.getParameterTable();
        final TermParameterMapTable mtpmt =
                protoNetwork1.getTermParameterMapTable();
        final ProtoNodeTable mpnt = protoNetwork1.getProtoNodeTable();

        // if p1 contains an equivalent Term then return it's inndex without
        // adding a new one.
        Integer p1Index = mtt.getVisitedTerms().get(termToMerge);
        if (p1Index != null) {
            termMap.put(termId, p1Index);
            return p1Index;
        }

        List<Integer> newTermParameterIndices = new ArrayList<Integer>();
        List<Integer> termParameterIndices = mapTable.getTermParameterIndex()
                .get(termId);

        if (hasItems(termParameterIndices)) {
            for (Integer termParameterIndex : termParameterIndices) {
                TableParameter parameter = parameterTable.
                        getIndexTableParameter().get(termParameterIndex);
                newTermParameterIndices.add(mpt
                        .addTableParameter(parameter));
            }
        }

        Integer newTermIndex = mtt.addTerm(termToMerge);
        mtpmt.addTermParameterMapping(
                newTermIndex, newTermParameterIndices);

        Integer newNode = mpnt.addNode(newTermIndex, mtt.getTermValues().get(newTermIndex));

        termMap.put(termId, newNode);
        return newTermIndex;
    }
}
