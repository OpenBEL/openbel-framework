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
import org.openbel.framework.common.protonetwork.model.*;
import org.openbel.framework.common.protonetwork.model.AnnotationDefinitionTable.TableAnnotationDefinition;
import org.openbel.framework.common.protonetwork.model.AnnotationValueTable.TableAnnotationValue;
import org.openbel.framework.common.protonetwork.model.DocumentTable.DocumentHeader;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ParameterTable.TableParameter;
import org.openbel.framework.common.protonetwork.model.StatementAnnotationMapTable.AnnotationPair;
import org.openbel.framework.common.protonetwork.model.StatementTable.TableStatement;

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
            for (int j = 0; j < statements.size(); j++) {
                mergeStatement(j, statements.get(j), protoNetwork1,
                        protoNetwork2, did);
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
    protected Integer mergeStatement(int statementIndex,
            TableStatement statement,
            ProtoNetwork protoNetwork1, ProtoNetwork protoNetwork2,
            int documentId) {

        final ProtoNodeTable mpnt = protoNetwork1.getProtoNodeTable();
        final ProtoEdgeTable mpet = protoNetwork1.getProtoEdgeTable();
        final Map<Integer, Integer> nodeIndex = mpnt.getTermNodeIndex();

        // always merge subject term since it's present in all statement types
        Integer newSubjectTermId = mergeTerm(statement.getSubjectTermId(),
                protoNetwork1, protoNetwork2,
                documentId);

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
                    protoNetwork1, protoNetwork2, documentId);

            newStatementIndex = protoNetwork1.getStatementTable().addStatement(
                    new TableStatement(newSubjectTermId,
                            statement.getRelationshipName(), newObjectTermId),
                    stmtToMerge, documentId);

            // find new proto node ids and merge in proto edge
            int newSubjectNodeId = nodeIndex.get(newSubjectTermId);
            int newObjectNodeId = nodeIndex.get(newObjectTermId);
            mpet.addEdges(newStatementIndex, new ProtoEdgeTable.TableProtoEdge(
                    newSubjectNodeId, statement.getRelationshipName(),
                    newObjectNodeId));
        } else {
            // merge nested statement, no creation of proto edge

            // merge nested subject
            Integer newNestedSubjectTermId = mergeTerm(
                    Integer.valueOf(statement.getNestedSubject()),
                    protoNetwork1, protoNetwork2, documentId);
            // merge nested object
            Integer newNestedObjectTermId = mergeTerm(
                    Integer.valueOf(statement.getNestedObject()),
                    protoNetwork1, protoNetwork2, documentId);

            newStatementIndex = protoNetwork1.getStatementTable().addStatement(
                    new TableStatement(newSubjectTermId,
                            statement.getRelationshipName(),
                            newNestedSubjectTermId,
                            statement.getNestedRelationship(),
                            newNestedObjectTermId),
                    stmtToMerge, documentId);
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
    protected Integer mergeTerm(Integer termId, ProtoNetwork protoNetwork1,
            ProtoNetwork protoNetwork2, int documentId) {
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

        mpnt.addNode(newTermIndex, mtt.getTermValues().get(newTermIndex));

        return newTermIndex;
    }
}
