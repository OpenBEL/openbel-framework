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
package org.openbel.framework.tools.kamstore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openbel.framework.internal.KAMStoreDaoImpl;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

public class KamSummary implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3821417973867497192L;
    private KamInfo kamInfo;
    private Integer numOfNodes;
    private Integer numOfEdges;
    private Integer numOfBELDocuments;
    private Integer numOfNamespaces;
    private Integer numOfAnnotationTypes;
    private List<KAMStoreDaoImpl.AnnotationType> annotations;
    private Map<KAMStoreDaoImpl.AnnotationType, List<String>> annotationDomains =
            new HashMap<KAMStoreDaoImpl.AnnotationType, List<String>>();

    private Integer numOfBELStatements;
    private Integer numOfEvidenceLines;
    private Integer numOfCitations;

    private Integer numOfSpecies;

    private Integer numOfRnaAbundanceNodes;
    private Integer numOfPhosphoProteinNodes;
    private Integer numOfUniqueGeneReferences;

    private Integer numOfTranscriptionalControls;
    private Integer numOfHypotheses;
    private Integer numOfIncreaseEdges;
    private Integer numOfDecreaseEdges;
    private Double averageHypothesisUpstreamNodes;

    private Map<String, KamSummary> filteredKamSummaries;

    private Map<String, Integer> statementBreakdownBySpeciesMap =
            new TreeMap<String, Integer>();

    protected KamInfo getKamInfo() {
        return kamInfo;
    }

    public void setKamInfo(KamInfo kamInfo) {
        this.kamInfo = kamInfo;
    }

    public Integer getNumOfNodes() {
        return numOfNodes;
    }

    public void setNumOfNodes(Integer numOfNodes) {
        this.numOfNodes = numOfNodes;
    }

    public Integer getNumOfEdges() {
        return numOfEdges;
    }

    public void setNumOfEdges(Integer numOfEdges) {
        this.numOfEdges = numOfEdges;
    }

    public Integer getNumOfBELDocuments() {
        return numOfBELDocuments;
    }

    public void setNumOfBELDocuments(Integer numOfBELDocuments) {
        this.numOfBELDocuments = numOfBELDocuments;
    }

    public Integer getNumOfNamespaces() {
        return numOfNamespaces;
    }

    public void setNumOfNamespaces(Integer numOfNamespaces) {
        this.numOfNamespaces = numOfNamespaces;
    }

    public List<KAMStoreDaoImpl.AnnotationType> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(
            final List<KAMStoreDaoImpl.AnnotationType> annotations) {
        this.annotations = annotations;
    }

    public Integer getNumOfAnnotationTypes() {
        return numOfAnnotationTypes;
    }

    public void setNumOfAnnotationTypes(Integer numOfAnnotations) {
        this.numOfAnnotationTypes = numOfAnnotations;
    }

    public Integer getNumOfBELStatements() {
        return numOfBELStatements;
    }

    public void setNumOfBELStatements(Integer numOfBELStatements) {
        this.numOfBELStatements = numOfBELStatements;
    }

    public Integer getNumOfEvidenceLines() {
        return numOfEvidenceLines;
    }

    public void setNumOfEvidenceLines(Integer numOfEvidenceLines) {
        this.numOfEvidenceLines = numOfEvidenceLines;
    }

    public Integer getNumOfCitations() {
        return numOfCitations;
    }

    public void setNumOfCitations(Integer numOfCitations) {
        this.numOfCitations = numOfCitations;
    }

    public Integer getNumOfSpecies() {
        return numOfSpecies;
    }

    public void setNumOfSpecies(Integer numOfSpecies) {
        this.numOfSpecies = numOfSpecies;
    }

    public Map<String, Integer> getStatementBreakdownBySpeciesMap() {
        return statementBreakdownBySpeciesMap;
    }

    public void setStatementBreakdownBySpeciesMap(
            Map<String, Integer> statementBreakdownBySpeciesMap) {
        this.statementBreakdownBySpeciesMap = statementBreakdownBySpeciesMap;
    }

    public Integer getNumOfRnaAbundanceNodes() {
        return numOfRnaAbundanceNodes;
    }

    public void setNumOfRnaAbundanceNodes(Integer numOfRnaAbundanceNodes) {
        this.numOfRnaAbundanceNodes = numOfRnaAbundanceNodes;
    }

    public Integer getNumOfPhosphoProteinNodes() {
        return numOfPhosphoProteinNodes;
    }

    public void setNumOfPhosphoProteinNodes(Integer numOfPhosphoProteinNodes) {
        this.numOfPhosphoProteinNodes = numOfPhosphoProteinNodes;
    }

    public Integer getNumOfUniqueGeneReferences() {
        return numOfUniqueGeneReferences;
    }

    public void setNumOfUniqueGeneReferences(Integer numOfUniqueGeneReferences) {
        this.numOfUniqueGeneReferences = numOfUniqueGeneReferences;
    }

    public Integer getNumOfTranscriptionalControls() {
        return numOfTranscriptionalControls;
    }

    public void setNumOfTranscriptionalControls(
            Integer numOfTranscriptionalControls) {
        this.numOfTranscriptionalControls = numOfTranscriptionalControls;
    }

    public Integer getNumOfIncreaseEdges() {
        return numOfIncreaseEdges;
    }

    public void setNumOfIncreaseEdges(Integer numOfIncreaseEdges) {
        this.numOfIncreaseEdges = numOfIncreaseEdges;
    }

    public Integer getNumOfDecreaseEdges() {
        return numOfDecreaseEdges;
    }

    public void setNumOfDecreaseEdges(Integer numOfDecreaseEdges) {
        this.numOfDecreaseEdges = numOfDecreaseEdges;
    }

    public Integer getNumOfHypotheses() {
        return numOfHypotheses;
    }

    public void setNumOfHypotheses(Integer numOfHypotheses) {
        this.numOfHypotheses = numOfHypotheses;
    }

    public Double getAverageHypothesisUpstreamNodes() {
        return averageHypothesisUpstreamNodes;
    }

    public void setAverageHypothesisUpstreamNodes(
            Double averageHypothesisUpstreamNodes) {
        this.averageHypothesisUpstreamNodes = averageHypothesisUpstreamNodes;
    }

    public Map<String, KamSummary> getFilteredKamSummaries() {
        return filteredKamSummaries;
    }

    public void setFilteredKamSummaries(
            Map<String, KamSummary> filteredKamSummaries) {
        this.filteredKamSummaries = filteredKamSummaries;
    }

    public Map<KAMStoreDaoImpl.AnnotationType, List<String>>
            getAnnotationDomains() {
        return annotationDomains;
    }

    public
            void
            setAnnotationDomains(
                    Map<KAMStoreDaoImpl.AnnotationType, List<String>> annotationDomains) {
        this.annotationDomains = annotationDomains;
    }
}
