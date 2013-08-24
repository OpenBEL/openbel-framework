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
package org.openbel.framework.api;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

/**
 *
 * @author julianjray
 *
 */
public interface Kam extends KamStoreObject {

    public KamEdge createEdge(Integer kamEdgeId, KamNode sourceNode,
            RelationshipType relationshipType, KamNode targetNode)
            throws InvalidArgument;

    public void removeEdge(final KamEdge kamEdge);

    public KamEdge replaceEdge(KamEdge kamEdge, FunctionEnum sourceFunction, String sourceLabel,
            RelationshipType relationship, FunctionEnum targetFunction, String targetLabel);

    public KamEdge replaceEdge(final KamEdge kamEdge, final KamEdge replacement);

    public KamNode createNode(Integer id, FunctionEnum functionType,
            String label) throws InvalidArgument;

    public void removeNode(KamNode kamNode);

    public KamNode replaceNode(KamNode kamNode, FunctionEnum function, String label);

    public KamNode replaceNode(KamNode kamNode, KamNode replacement);

    /**
     * Remove the {@code from} {@link KamNode} and collapse its adjacent
     * network to the {@code to} {@link KamNode}.  Any {@link KamEdge edges}
     * shared between {@code from} and {@code to} are necessarily deleted.
     *
     * <p>
     * For example given:
     *
     * <pre>
     *    A -> B -> C
     *         |
     *         V
     *    E <- D -> F
     * </pre>
     *
     * if we replace {@code B} with {@code D} we end up with:
     * <pre>
     *    A -> D -> C
     *        / \
     *       v   v
     *      E     F
     * </pre>
     * </p>
     *
     * @param from {@link KamNode} node to collapse
     * @param to {@link KamNode} node that is preserved and subsumes the
     * network of {@code from}
     */
    public void collapseNode(final KamNode from, KamNode to);

    /**
     *
     * @return {@link KamInfo}
     */
    KamInfo getKamInfo();

    /**
     * Creates an {@link NodeFilter} object
     * @return
     */
    NodeFilter createNodeFilter();

    /**
     * Creates an {@link EdgeFilter} object
     * @return
     */
    EdgeFilter createEdgeFilter();

    KamNode findNode(String label);

    KamNode findNode(String label, NodeFilter filter);

    KamNode findNode(Integer kamNodeId);

    KamNode findNode(Integer kamNodeId, NodeFilter filter);

    /**
     *
     * @param labelPattern
     * @return {@link Set} of {@link KamNode KAM nodes}
     */
    Set<KamNode> findNode(Pattern labelPattern);

    /**
     * @param labelPattern
     * @param filter
     * @return
     */
    Set<KamNode> findNode(Pattern labelPattern,
            NodeFilter filter);

    /**
     *
     * @param kamNode
     * @return {@link Set} of {@link KamNode KAM nodes}
     */
    Set<KamNode> getAdjacentNodes(KamNode kamNode);

    /**
     * Provides a list of nodes which are adjacent to the selected
     * node. If edge direction is Forward, Node j is adjacent to node i if there
     * is some edge (i,j) element of A in the graph, if graphDirection is Reverse
     * the determination is the existence of some edge (j, i)
     *
     * @param kamNode
     * @return {@link Set} of {@link KamNode KAM nodes}
     */
    Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection);

    /**
     *
     * @param kamNode
     * @param edgeDirection
     * @param edgeFilter
     * @return
     */
    Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter edgeFilter);

    /**
     *
     * @param kamNode
     * @param edgeDirection
     * @param filter
     * @return
     */
    Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, NodeFilter filter);

    Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeFilter edgeFilter, NodeFilter nodeFilter);

    /**
     *
     * @param kamNode
     * @param edgeDirection
     * @param edgeFilter
     * @param nodeFilter
     * @return
     */
    Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter edgeFilter,
            NodeFilter nodeFilter);

    /**
     *
     * @param kamNode
     * @return {@link Set} of {@link KamEdge KAM edges}
     */
    Set<KamEdge> getAdjacentEdges(KamNode kamNode);

    /**
     *
     * @param kamNode
     * @param filter
     * @return
     */
    Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeFilter filter);

    /**
     *
     * @param kamNode
     * @param edgeDirection
     * @return
     */
    Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeDirectionType edgeDirection);

    /**
     *
     * @param kamNode
     * @param edgeDirection
     * @return {@link Set} of {@link KamEdge KAM edges}
     */
    Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter filter);

    /**
     *
     * @param sourceNode
     * @param targetNode
     * @return
     */
    Set<KamEdge> getEdges(KamNode sourceNode, KamNode targetNode);

    /**
     *
     * @param sourceNode
     * @param targetNode
     * @param filter
     * @return
     */
    Set<KamEdge> getEdges(KamNode sourceNode,
            KamNode targetNode, EdgeFilter filter);

    /**
     *
     * @param kamEdgeId
     * @return boolean
     */
    KamEdge findEdge(Integer kamEdgeId);

    KamEdge findEdge(KamNode sourceNode,
            RelationshipType relationshipType, KamNode targetNode)
            throws InvalidArgument;

    /**
     *
     * @param kamNode
     * @return boolean
     */
    boolean contains(KamNode kamNode);

    /**
     *
     * @param kamEdge
     * @return boolean
     */
    boolean contains(KamEdge kamEdge);

    /**
     *
     * @return {@link Collection} of {@link KamEdge KAMEdges}
     */
    Collection<KamEdge> getEdges();

    /**
     *
     * @return {@link Collection} of {@link KamNode KAMNodes}
     */
    Collection<KamNode> getNodes();

    /**
     *
     * @param filter
     * @return
     */
    Collection<KamNode> getNodes(NodeFilter filter);

    /**
     *
     * @param filter
     * @return
     */
    Collection<KamEdge> getEdges(EdgeFilter filter);

    /**
     * Unions the set of edges with this Kam
     *
     * @param edges
     * @return
     * @throws InvalidArgument
     */
    void union(Collection<KamEdge> kamEdges) throws InvalidArgument;

    interface KamNode extends KamElement {
        /**
         * Retrieves the database {@link Integer id} for this object.
         *
         * @return the {@link Integer id}, which will be {@code non-null}
         */
        @Override
        Integer getId();

        /**
         *
         * @return {@link FunctionEnum}
         */
        FunctionEnum getFunctionType();

        /**
         *
         * @return {@link String}
         */
        String getLabel();
    }

    interface KamEdge extends KamElement {

        /**
         * Retrieves the database {@link Integer id} for this object.
         *
         * @return the {@link Integer id}, which will be {@code non-null}
         */
        @Override
        Integer getId();

        /**
         * Retrieve the {@link KamNode source node} of this edge.
         *
         * @return the {@link KamNode source node}
         */
        KamNode getSourceNode();

        /**
         * Retrieve the {@link KamNode target node} of this edge.
         *
         * @return the {@link KamNode target node}
         */
        KamNode getTargetNode();

        /**
         * Retrieve the {@link RelationshipType relationship} of this edge.
         *
         * @return the {@link RelationshipType relationship}
         */
        RelationshipType getRelationshipType();
    }
}
