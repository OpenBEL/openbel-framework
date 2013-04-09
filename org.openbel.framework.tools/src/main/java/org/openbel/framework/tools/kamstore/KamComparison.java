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
package org.openbel.framework.tools.kamstore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class KamComparison {

    private final Map<String, Integer> index;
    private final String[] kamNames;

    // Topology information:
    private final int[] kamNodeCounts;
    private final int[] kamEdgeCounts;

    // Data information:
    private final int[] belDocumentCounts;
    private final int[] namespaceCounts;
    private final int[] annotationDefinitionCounts;
    private final int[] annotationCounts;
    private final int[] statementCounts;
    private final int[] termCounts;
    private final int[] parameterCounts;
    private final int[] uniqueParameterCounts;

    public KamComparison(String... kamNames) {
        final int length = kamNames.length;
        index = new HashMap<String, Integer>(length);
        for (int i = 0; i < length; ++i) {
            index.put(kamNames[i], i);
        }
        this.kamNames = kamNames;

        kamNodeCounts = new int[length];
        kamEdgeCounts = new int[length];

        belDocumentCounts = new int[length];
        namespaceCounts = new int[length];
        annotationDefinitionCounts = new int[length];
        annotationCounts = new int[length];
        statementCounts = new int[length];
        termCounts = new int[length];
        parameterCounts = new int[length];
        uniqueParameterCounts = new int[length];

        Arrays.fill(kamNodeCounts, 0);
        Arrays.fill(kamEdgeCounts, 0);
        Arrays.fill(belDocumentCounts, 0);
        Arrays.fill(namespaceCounts, 0);
        Arrays.fill(annotationDefinitionCounts, 0);
        Arrays.fill(annotationCounts, 0);
        Arrays.fill(statementCounts, 0);
        Arrays.fill(termCounts, 0);
        Arrays.fill(parameterCounts, 0);
        Arrays.fill(uniqueParameterCounts, 0);
    }

    public String[] getKamNames() {
        return Arrays.copyOf(kamNames, kamNames.length);
    }

    /**
     * Returns the average KAM node degree.
     *
     * Zero is returned if the average is undefined (in case
     * the number of KAM nodes is zero).
     */
    public double getAverageKamNodeDegree(String kamName) {
        final int nodes = getKamNodeCount(kamName);
        return (nodes != 0 ? ((double) 2 * getKamEdgeCount(kamName)) / nodes
                : 0.0);
    }

    /**
     * Returns the average KAM node in degree.
     *
     * Zero is returned if the average is undefined (in case
     * the number of KAM nodes is zero).
     */
    public double getAverageKamNodeInDegree(String kamName) {
        final int nodes = getKamNodeCount(kamName);
        return (nodes != 0 ? ((double) getKamEdgeCount(kamName)) / nodes : 0.0);
    }

    /**
     * Returns the average KAM node out degree.
     *
     * Zero is returned if the average is undefined (in case
     * the number of KAM nodes is zero).
     */
    public double getAverageKamNodeOutDegree(String kamName) {
        final int nodes = getKamNodeCount(kamName);
        return (nodes != 0 ? ((double) getKamEdgeCount(kamName)) / nodes : 0.0);
    }

    /**
     * Returns the density of the KAM calculated as the number of
     * KAM edges divided by the maximum number of edges possible between
     * the KAM nodes:
     * {@code E / (N * (N - 1))}
     * where N is the number of KAM nodes and E is the number of KAM edges.
     * Zero is returned in the cases that the above expression is undefined.
     */
    public double getDensity(String kamName) {
        final int nodes = getKamNodeCount(kamName);
        final int edges = getKamEdgeCount(kamName);
        return (nodes > 1 ? ((double) edges) / (nodes * (nodes - 1)) : 0.0);
    }

    /**
     * Returns the number of nodes.
     */
    public int getKamNodeCount(String kamName) {
        return kamNodeCounts[index.get(kamName)];
    }

    /**
     * Sets the number of nodes.
     */
    public void setKamNodeCount(String kamName, int kamNodeCount) {
        kamNodeCounts[index.get(kamName)] = kamNodeCount;
    }

    /**
     * Returns the number of edges.
     */
    public int getKamEdgeCount(String kamName) {
        return kamEdgeCounts[index.get(kamName)];
    }

    /**
     * Sets the number of edges.
     */
    public void setKamEdgeCount(String kamName, int kamEdgeCount) {
        kamEdgeCounts[index.get(kamName)] = kamEdgeCount;
    }

    /**
     * Returns the number of BEL documents.
     */
    public int getBELDocumentCount(String kamName) {
        return belDocumentCounts[index.get(kamName)];
    }

    /**
     * Sets the number of BEL documents.
     */
    public void setBELDocumentCount(String kamName, int belDocumentCount) {
        belDocumentCounts[index.get(kamName)] = belDocumentCount;
    }

    /**
     * Returns the number of namespaces.
     */
    public int getNamespaceCount(String kamName) {
        return namespaceCounts[index.get(kamName)];
    }

    /**
     * Sets the number of namespaces.
     */
    public void setNamespaceCount(String kamName, int namespaceCount) {
        namespaceCounts[index.get(kamName)] = namespaceCount;
    }

    /**
     * Returns the number of annotation definitions.
     */
    public int getAnnotationDefinitionCount(String kamName) {
        return annotationDefinitionCounts[index.get(kamName)];
    }

    /**
     * Sets the number of annotation definitions.
     */
    public void setAnnotationDefinitionCount(String kamName,
            int annotationDefinitionCount) {
        annotationDefinitionCounts[index.get(kamName)] =
                annotationDefinitionCount;
    }

    /**
     * Returns the number of annotations.
     */
    public int getAnnotationCount(String kamName) {
        return annotationCounts[index.get(kamName)];
    }

    /**
     * Sets the number of annotations.
     */
    public void setAnnotationCount(String kamName, int annotationCount) {
        annotationCounts[index.get(kamName)] = annotationCount;
    }

    /**
     * Returns the number of statements.
     */
    public int getStatementCount(String kamName) {
        return statementCounts[index.get(kamName)];
    }

    /**
     * Sets the number of statements.
     */
    public void setStatementCount(String kamName, int statementCount) {
        statementCounts[index.get(kamName)] = statementCount;
    }

    /**
     * Returns the number of terms.
     */
    public int getTermCount(String kamName) {
        return termCounts[index.get(kamName)];
    }

    /**
     * Sets the number of terms.
     */
    public void setTermCount(String kamName, int termCount) {
        termCounts[index.get(kamName)] = termCount;
    }

    /**
     * Returns the number of parameters.
     */
    public int getParameterCount(String kamName) {
        return parameterCounts[index.get(kamName)];
    }

    /**
     * Sets the number of parameters.
     */
    public void setParameterCount(String kamName, int parameterCount) {
        parameterCounts[index.get(kamName)] = parameterCount;
    }

    /**
     * Returns the number of unique parameters.
     */
    public int getUniqueParameterCount(String kamName) {
        return uniqueParameterCounts[index.get(kamName)];
    }

    /**
     * Sets the number of unique parameters.
     */
    public void
            setUniqueParameterCount(String kamName, int uniqueParameterCount) {
        uniqueParameterCounts[index.get(kamName)] = uniqueParameterCount;
    }
}
