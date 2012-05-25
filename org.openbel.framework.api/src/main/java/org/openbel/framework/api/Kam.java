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
