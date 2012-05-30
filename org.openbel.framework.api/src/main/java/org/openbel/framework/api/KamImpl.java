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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KAMStoreDaoImpl.KamProtoEdge;
import org.openbel.framework.internal.KAMStoreDaoImpl.KamProtoNode;

/**
 *
 * @author julianjray
 *
 */
public final class KamImpl extends KamStoreObjectImpl implements Kam {

    private final KamInfo kamInfo;

    // Stores each edge by its identifier
    private Map<Integer, KamEdge> idEdgeMap =
            new LinkedHashMap<Integer, KamEdge>();
    private Map<KamEdge, Integer> edgeIdMap =
            new LinkedHashMap<KamEdge, Integer>();

    // Stores each vertex by its identifier
    private Map<Integer, KamNode> idNodeMap =
            new LinkedHashMap<Integer, KamNode>();
    private Map<KamNode, Integer> nodeIdMap =
            new LinkedHashMap<KamNode, Integer>();

    // Stores a primary map of edges incident to each source node.
    private Map<KamNode, Set<KamEdge>> nodeSourceMap =
            new LinkedHashMap<KamNode, Set<KamEdge>>();

    // Stores a primary map of edges incident to each source node.
    private Map<KamNode, Set<KamEdge>> nodeTargetMap =
            new LinkedHashMap<KamNode, Set<KamEdge>>();

    /**
     * Defines the hashCode that is calculated when the {@link Kam} is mutated.
     * The cost of calculating the hash code will be paid while mutating the
     * {@link Kam} so that we can optimize the analysis of a {@link Kam}.
     *
     * <p>
     * The following mutating operations will rebuild the hashCode:
     * <ul>
     * <li>{@link Kam#addNode(KamNode)}</li>
     * <li>{@link Kam#addEdge(KamEdge)}</li>
     * <li>{@link Kam#removeNode(KamNode)}</li>
     * <li>{@link Kam#removeEdge(KamEdge)}</li>
     * </ul>
     * </p>
     */
    private int hashCode;

    /**
     *
     * @param kamId
     * @param kamInfo
     * @param kamProtoNodeList
     * @param kamProtoEdgeList
     */
    public KamImpl(KamInfo kamInfo, Collection<KamProtoNode> kamProtoNodes,
            Collection<KamProtoEdge> kamProtoEdges) throws InvalidArgument {

        super(kamInfo.getId());
        this.kamInfo = kamInfo;

        if (kamProtoNodes == null) {
            throw new InvalidArgument("Node list cannot be null");
        }
        if (kamProtoEdges == null) {
            throw new InvalidArgument("Edge list cannot be null");
        }

        for (KamProtoNode kamProtoNode : kamProtoNodes) {
            createNode(kamProtoNode.getId(), kamProtoNode.getFunctionType(),
                    kamProtoNode.getLabel());
        }

        // Figure out which nodes we want to create. We only create ones which are attached to edges
        // and as edges might have been filtered, we need to weed out those nodes which are no longer
        // connected
        for (KamProtoEdge kamProtoEdge : kamProtoEdges) {
            KamNode sourceNode = findNode(kamProtoEdge.getSourceNode().getId());
            if (null == sourceNode) {
                throw new InvalidArgument("Can't find source node.");
                //sourceNode = createNode(kamProtoEdge.getSourceNode().getId(), kamProtoEdge.getSourceNode().getFunctionType(), kamProtoEdge.getSourceNode().getLabel());
            }
            KamNode targetNode = findNode(kamProtoEdge.getTargetNode().getId());
            if (null == targetNode) {
                throw new InvalidArgument("Can't find target node.");
                //targetNode = createNode(kamProtoEdge.getTargetNode().getId(), kamProtoEdge.getTargetNode().getFunctionType(), kamProtoEdge.getTargetNode().getLabel());
            }
            createEdge(kamProtoEdge.getId(), sourceNode,
                    kamProtoEdge.getRelationship(), targetNode);
        }
    }

    /**
     * Returns an empty version of a typed Kam. Will throw an NPE if the kaminfo is null.
     *
     * @param kamInfo
     */
    public KamImpl(KamInfo kamInfo) {
        super(kamInfo.getId());
        this.kamInfo = kamInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamInfo getKamInfo() {
        return kamInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeFilter createNodeFilter() {
        return new NodeFilter(this.kamInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EdgeFilter createEdgeFilter() {
        return new EdgeFilter(this.kamInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode findNode(String label) {
        return findNode(label, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode findNode(String label, NodeFilter filter) {
        for (KamNode kamNode : idNodeMap.values()) {
            String nodeLabel = kamNode.getLabel();
            if (nodeLabel.equalsIgnoreCase(label)) {
                if (filter != null) {
                    if (!filter.accept(kamNode)) {
                        return null;
                    }
                }

                return kamNode;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode findNode(Integer kamNodeId) {
        return findNode(kamNodeId, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode findNode(Integer kamNodeId, NodeFilter filter) {
        final KamNode kamNode = idNodeMap.get(kamNodeId);

        if (kamNode == null) {
            return null;
        }

        if (filter != null) {
            if (!filter.accept(kamNode)) {
                return null;
            }
        }

        return kamNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> findNode(Pattern labelPattern) {
        return findNode(labelPattern, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> findNode(Pattern labelPattern, NodeFilter filter) {
        Set<KamNode> results = new LinkedHashSet<KamNode>();
        for (KamNode node : idNodeMap.values()) {
            if (labelPattern.matcher(node.getLabel()).matches()) {

                // Check for a node filter
                if (null != filter) {
                    if (!filter.accept(node)) {
                        continue;
                    }
                }
                results.add(node);
            }
        }
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode) {
        return getAdjacentNodes(kamNode, EdgeDirectionType.BOTH, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection) {
        return getAdjacentNodes(kamNode, edgeDirection, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter edgeFilter) {
        return getAdjacentNodes(kamNode, edgeDirection, edgeFilter, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, NodeFilter filter) {
        return getAdjacentNodes(kamNode, edgeDirection, null, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeFilter edgeFilter, NodeFilter nodeFilter) {
        return getAdjacentNodes(kamNode, EdgeDirectionType.BOTH, edgeFilter,
                nodeFilter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter edgeFilter,
            NodeFilter nodeFilter) {

        Set<KamNode> adjacentNodes = new LinkedHashSet<KamNode>();

        KamNode node = null;
        if (EdgeDirectionType.FORWARD == edgeDirection
                || EdgeDirectionType.BOTH == edgeDirection) {
            for (KamEdge kamEdge : nodeSourceMap.get(kamNode)) {

                // Check for an edge filter
                if (null != edgeFilter) {
                    if (!edgeFilter.accept(kamEdge)) {
                        continue;
                    }
                }

                node = kamEdge.getTargetNode();

                // Check for a node filter
                if (null != nodeFilter) {
                    if (!nodeFilter.accept(node)) {
                        continue;
                    }
                }
                adjacentNodes.add(node);
            }
        }
        if (EdgeDirectionType.REVERSE == edgeDirection
                || EdgeDirectionType.BOTH == edgeDirection) {
            for (KamEdge kamEdge : nodeTargetMap.get(kamNode)) {

                // Check for an edge filter
                if (null != edgeFilter) {
                    if (!edgeFilter.accept(kamEdge)) {
                        continue;
                    }
                }

                node = kamEdge.getSourceNode();

                // Check for a node filter
                if (null != nodeFilter) {
                    if (!nodeFilter.accept(node)) {
                        continue;
                    }
                }
                adjacentNodes.add(node);
            }
        }
        return adjacentNodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode) {
        return getAdjacentEdges(kamNode, EdgeDirectionType.BOTH, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode, EdgeFilter filter) {
        return getAdjacentEdges(kamNode, EdgeDirectionType.BOTH, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeDirectionType edgeDirection) {
        return getAdjacentEdges(kamNode, edgeDirection, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter filter) {

        Set<KamEdge> adjacentEdges = new LinkedHashSet<KamEdge>();

        if (EdgeDirectionType.FORWARD == edgeDirection
                || EdgeDirectionType.BOTH == edgeDirection) {
            for (KamEdge kamEdge : nodeSourceMap.get(kamNode)) {

                // Check for an edge filter
                if (null != filter) {
                    if (!filter.accept(kamEdge)) {
                        continue;
                    }
                }
                adjacentEdges.add(kamEdge);
            }
        }
        if (EdgeDirectionType.REVERSE == edgeDirection
                || EdgeDirectionType.BOTH == edgeDirection) {
            for (KamEdge kamEdge : nodeTargetMap.get(kamNode)) {

                // Check for an edge filter
                if (null != filter) {
                    if (!filter.accept(kamEdge)) {
                        continue;
                    }
                }
                adjacentEdges.add(kamEdge);
            }
        }
        return adjacentEdges;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getEdges(KamNode sourceNode, KamNode targetNode) {
        return getEdges(sourceNode, targetNode, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getEdges(KamNode sourceNode, KamNode targetNode,
            EdgeFilter filter) {

        Set<KamEdge> edgeSet = new LinkedHashSet<KamEdge>();

        // Scan the source node and find all edges which reference the target node
        for (KamEdge kamEdge : nodeSourceMap.get(sourceNode)) {
            if (kamEdge.getTargetNode().equals(targetNode)) {
                // Check for an edge filter
                if (null != filter) {
                    if (!filter.accept(kamEdge)) {
                        continue;
                    }
                }
                edgeSet.add(kamEdge);
            }
        }
        return edgeSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge findEdge(Integer kamEdgeId) {
        return idEdgeMap.get(kamEdgeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge findEdge(KamNode sourceNode,
            RelationshipType relationshipType, KamNode targetNode)
            throws InvalidArgument {
        // Make sure the nodes are valid for this graph
        if (!contains(sourceNode)) {
            throw new InvalidArgument("source Node is not in graph");
        }
        if (!contains(targetNode)) {
            throw new InvalidArgument("target Node is not in graph");
        }

        // See if the edge already exists. The hash ignores the edgeId
        KamEdge kamEdge =
                new KamEdgeImpl(this, -1, sourceNode, relationshipType,
                        targetNode);
        if (edgeIdMap.keySet().contains(kamEdge)) {
            Integer kamEdgeId = edgeIdMap.get(kamEdge);
            kamEdge = idEdgeMap.get(kamEdgeId);
        } else {
            // Can't find it in the graph
            kamEdge = null;
        }
        return kamEdge;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(KamNode kamNode) {
        return nodeIdMap.containsKey(kamNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(KamEdge kamEdge) {
        return edgeIdMap.containsKey(kamEdge);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge createEdge(Integer kamEdgeId, KamNode sourceNode,
            RelationshipType relationshipType, KamNode targetNode)
            throws InvalidArgument {

        // Make sure the nodes are valid for this graph
        if (!contains(sourceNode)) {
            throw new InvalidArgument("source Node is not in graph");
        }
        if (!contains(targetNode)) {
            throw new InvalidArgument("target Node is not in graph");
        }

        // See if the edge already exists
        KamEdge kamEdge =
                new KamEdgeImpl(this, kamEdgeId, sourceNode, relationshipType,
                        targetNode);
        if (!edgeIdMap.containsKey(kamEdge)) {
            // add this edge into the graph
            addEdge(kamEdge);
        }
        return kamEdge;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(final KamEdge kamEdge) {
        // remove storage of kam edge
        idEdgeMap.remove(kamEdge.getId());
        edgeIdMap.remove(kamEdge);

        // break edge association with source and target nodes
        nodeSourceMap.get(kamEdge.getSourceNode()).remove(kamEdge);
        nodeTargetMap.get(kamEdge.getTargetNode()).remove(kamEdge);

        // rebuild hashcode
        this.hashCode = generateHashCode();
    }

    @Override
    public KamEdge replaceEdge(final KamEdge kamEdge, final KamEdge replacement) {
        // replace source node
        final int sourceNodeId = kamEdge.getSourceNode().getId();

        // establish id to replacement node
        idNodeMap.put(sourceNodeId, replacement.getSourceNode());
        nodeIdMap.put(replacement.getSourceNode(), sourceNodeId);

        //replace target node
        final int targetNodeId = kamEdge.getTargetNode().getId();

        // establish id to replacement node
        idNodeMap.put(targetNodeId, replacement.getTargetNode());
        nodeIdMap.put(replacement.getTargetNode(), targetNodeId);

        final int edgeId = kamEdge.getId();

        // establish id to replacement edge
        idEdgeMap.put(edgeId, replacement);
        edgeIdMap.put(replacement, edgeId);

        // reconnect network
        Set<KamEdge> sourceOutgoing =
                nodeSourceMap.remove(kamEdge.getSourceNode());
        sourceOutgoing.remove(kamEdge);
        sourceOutgoing.add(replacement);
        nodeSourceMap.put(replacement.getSourceNode(), sourceOutgoing);
        Set<KamEdge> targetIncoming =
                nodeTargetMap.remove(kamEdge.getTargetNode());
        targetIncoming.remove(kamEdge);
        targetIncoming.add(replacement);
        nodeTargetMap.put(replacement.getTargetNode(), targetIncoming);

        Set<KamEdge> sourceIncoming =
                nodeTargetMap.remove(kamEdge.getSourceNode());
        nodeTargetMap.put(replacement.getSourceNode(), sourceIncoming);

        Set<KamEdge> targetOutgoing =
                nodeSourceMap.remove(kamEdge.getTargetNode());
        nodeSourceMap.put(replacement.getTargetNode(), targetOutgoing);
        return replacement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge replaceEdge(KamEdge kamEdge, FunctionEnum sourceFunction,
            String sourceLabel,
            RelationshipType relationship, FunctionEnum targetFunction,
            String targetLabel) {

        // replace source node
        final int sourceNodeId = kamEdge.getSourceNode().getId();
        final KamNode sourceReplacement =
                new KamNodeImpl(this, sourceNodeId, sourceFunction, sourceLabel);

        // establish id to replacement node
        idNodeMap.put(sourceNodeId, sourceReplacement);
        nodeIdMap.put(sourceReplacement, sourceNodeId);

        //replace target node
        final int targetNodeId = kamEdge.getTargetNode().getId();
        final KamNode targetReplacement =
                new KamNodeImpl(this, targetNodeId, targetFunction, targetLabel);

        // establish id to replacement node
        idNodeMap.put(targetNodeId, targetReplacement);
        nodeIdMap.put(targetReplacement, targetNodeId);

        final int edgeId = kamEdge.getId();
        final KamEdge replacement = new KamEdgeImpl(this, edgeId,
                sourceReplacement, relationship, targetReplacement);

        // establish id to replacement edge
        idEdgeMap.put(edgeId, replacement);
        edgeIdMap.put(replacement, edgeId);

        // reconnect network
        Set<KamEdge> sourceOutgoing =
                nodeSourceMap.remove(kamEdge.getSourceNode());
        sourceOutgoing.remove(kamEdge);
        sourceOutgoing.add(replacement);
        nodeSourceMap.put(sourceReplacement, sourceOutgoing);
        Set<KamEdge> targetIncoming =
                nodeTargetMap.remove(kamEdge.getTargetNode());
        targetIncoming.remove(kamEdge);
        targetIncoming.add(replacement);
        nodeTargetMap.put(targetReplacement, targetIncoming);

        Set<KamEdge> sourceIncoming =
                nodeTargetMap.remove(kamEdge.getSourceNode());
        nodeTargetMap.put(sourceReplacement, sourceIncoming);

        Set<KamEdge> targetOutgoing =
                nodeSourceMap.remove(kamEdge.getTargetNode());
        nodeSourceMap.put(targetReplacement, targetOutgoing);
        return replacement;
    }

    private void addEdge(KamEdge kamEdge) {

        // Save the edge
        idEdgeMap.put(kamEdge.getId(), kamEdge);
        edgeIdMap.put(kamEdge, kamEdge.getId());

        // Index the edge into the node maps
        nodeSourceMap.get(kamEdge.getSourceNode()).add(kamEdge);
        nodeTargetMap.get(kamEdge.getTargetNode()).add(kamEdge);

        // rebuild hashcode
        this.hashCode = generateHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode createNode(Integer id, FunctionEnum functionType,
            String label) throws InvalidArgument {

        // See if the node already exists
        KamNode kamNode = findNode(id);

        if (null == kamNode) {
            kamNode = new KamNodeImpl(this, id, functionType, label);
            // add this node into the graph
            addNode(kamNode);
        } else {
            throw new InvalidArgument("node with id " + id
                    + " already exists in the graph.");
        }

        return kamNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(KamNode kamNode) {
        idNodeMap.remove(kamNode.getId());
        nodeIdMap.remove(kamNode);
        nodeSourceMap.remove(kamNode);
        nodeTargetMap.remove(kamNode);

        // rebuild hashcode
        this.hashCode = generateHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode replaceNode(KamNode kamNode, FunctionEnum function, String label) {
        final int nodeId = kamNode.getId();
        final KamNode replacement = new KamNodeImpl(this, nodeId, function, label);
        return replaceNode(kamNode, replacement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode replaceNode(KamNode kamNode, KamNode replacement) {
        final int nodeId = kamNode.getId();

        // establish id to replacement node
        idNodeMap.put(nodeId, replacement);
        nodeIdMap.put(replacement, nodeId);

        // reconnect network
        Set<KamEdge> egress = nodeSourceMap.remove(kamNode);
        nodeSourceMap.put(replacement, egress);
        Set<KamEdge> ingress = nodeTargetMap.remove(kamNode);
        nodeTargetMap.put(replacement, ingress);

        this.hashCode = generateHashCode();
        return replacement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collapseNode(final KamNode from, KamNode to) {
        // redirect FROM's outgoing edges to TO
        Set<KamEdge> edges = nodeSourceMap.get(from);
        Set<KamEdge> dest = nodeSourceMap.get(to);
        for (final KamEdge e : edges) {
            ((KamEdgeImpl) e).setSourceNode(to);

            edges.remove(e);
            dest.add(e);
        }

        // redirect FROM's incoming edges to TO
        edges = nodeTargetMap.get(from);
        dest = nodeTargetMap.get(to);
        for (final KamEdge e : edges) {
            ((KamEdgeImpl) e).setTargetNode(to);

            edges.remove(e);
            dest.add(e);
        }

        // remove the FROM node
        removeNode(from);
    }

    private void addNode(KamNode kamNode) {

        // Add it to the id index
        idNodeMap.put(kamNode.getId(), kamNode);
        nodeIdMap.put(kamNode, kamNode.getId());

        // Add the node to the primary node-edge map. This stores edges adjacent to the node
        // where the node is the source node for the edge
        nodeSourceMap.put(kamNode, new LinkedHashSet<KamEdge>());

        // Add the node to the secondary index where the node is the target node for
        // cached edges
        nodeTargetMap.put(kamNode, new LinkedHashSet<KamEdge>());

        // rebuild hashcode
        this.hashCode = generateHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamEdge> getEdges() {
        return getEdges(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamNode> getNodes() {
        return getNodes(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamNode> getNodes(NodeFilter filter) {
        Set<KamNode> kamNodes = new LinkedHashSet<KamNode>();
        for (KamNode kamNode : idNodeMap.values()) {

            // Check for a node filter
            if (null != filter) {
                if (!filter.accept(kamNode)) {
                    continue;
                }
            }
            kamNodes.add(kamNode);
        }
        return kamNodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamEdge> getEdges(EdgeFilter filter) {
        Set<KamEdge> kamEdges = new LinkedHashSet<KamEdge>();
        for (KamEdge kamEdge : edgeIdMap.keySet()) {

            // Check for an edge filter
            if (null != filter) {
                if (!filter.accept(kamEdge)) {
                    continue;
                }
            }
            kamEdges.add(kamEdge);
        }
        return kamEdges;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void union(Collection<KamEdge> kamEdges) throws InvalidArgument {
        for (KamEdge kamEdge : kamEdges) {
            if (!this.getKamInfo().equals(kamEdge.getKam().getKamInfo())) {
                throw new InvalidArgument("Edge does not belong to the KAM");
            }

            // See if the edge already exists
            if (!edgeIdMap.containsKey(kamEdge)) {

                // Make sure the nodes are valid for this kam, if not then add them
                if (!contains(kamEdge.getSourceNode())) {
                    addNode(kamEdge.getSourceNode());
                }
                if (!contains(kamEdge.getTargetNode())) {
                    addNode(kamEdge.getTargetNode());
                }
                // add this edge into the graph
                addEdge(kamEdge);
            }
        }
    }

    private int generateHashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((kamInfo == null) ? 0 : kamInfo.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.hashCode != 0 ? hashCode : generateHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(Kam.class.isAssignableFrom(obj.getClass()))) {
            return false;
        }
        Kam other = (Kam) obj;
        if (kamInfo == null) {
            if (other.getKamInfo() != null) {
                return false;
            }
        } else if (!kamInfo.equals(other.getKamInfo())) {
            return false;
        }
        return true;
    }

    /**
     * {@link KamEdge} represents an edge / assertion in the {@link Kam kam}
     * and is represented by the triple:
     *
     * <pre>
     * [source node, relationship, object node]
     * </pre>
     *
     * @author julianjray
     */
    public final class KamEdgeImpl extends KamElementImpl implements KamEdge {

        private KamNode sourceNode;
        private KamNode targetNode;
        private RelationshipType relationshipType;

        /**
         * Precalculate the kam edge hash code since the object is immutable.
         */
        private int hashCode;

        /**
         * Precalculate the {@link KamEdge kam edge} label since the object
         * is immutable.
         */
        private String label;

        /**
         * Private constructor to create a {@link KamEdge kam edge}.
         *
         * @param kam {@link Kam}, the kam that contains this edge
         * @param kamEdgeId {@link Integer}, the kam edge id
         * @param sourceNode {@link KamNode}, the kam source node of this edge
         * @param relationshipType {@link RelationshipType}, the relationship
         * of this edge
         * @param targetNode {@link KamNode}, the kam target node of this edge
         */
        private KamEdgeImpl(Kam kam, Integer kamEdgeId, KamNode sourceNode,
                RelationshipType relationshipType, KamNode targetNode) {
            super(kam, kamEdgeId);
            this.sourceNode = sourceNode;
            this.relationshipType = relationshipType;
            this.targetNode = targetNode;

            this.hashCode = generateHashCode();
            this.label = generateLabel();
        }

        /**
         * Retrieve the {@link KamNode source node} of this edge.
         *
         * @return the {@link KamNode source node}
         */
        @Override
        public KamNode getSourceNode() {
            return sourceNode;
        }

        /**
         * Changes the {@link KamNode source node} for this
         * {@link KamEdge edge}.  The {@link KamEdgeImpl#hashCode} and
         * {@link KamEdgeImpl#label} is then regenerated to preserve the
         * identity.
         *
         * @param sourceNode {@link KamNode} new node
         */
        private void setSourceNode(final KamNode sourceNode) {
            this.sourceNode = sourceNode;

            this.hashCode = generateHashCode();
            this.label = generateLabel();
        }

        /**
         * Retrieve the {@link KamNode target node} of this edge.
         *
         * @return the {@link KamNode target node}
         */
        @Override
        public KamNode getTargetNode() {
            return targetNode;
        }

        /**
         * Changes the {@link KamNode target node} for this
         * {@link KamEdge edge}.  The {@link KamEdgeImpl#hashCode} and
         * {@link KamEdgeImpl#label} is then regenerated to preserve the
         * identity.
         *
         * @param targetNode {@link KamNode} new node
         */
        private void setTargetNode(final KamNode targetNode) {
            this.targetNode = targetNode;

            this.hashCode = generateHashCode();
            this.label = generateLabel();
        }

        /**
         * Retrieve the {@link RelationshipType relationship} of this edge.
         *
         * @return the {@link RelationshipType relationship}
         */
        @Override
        public RelationshipType getRelationshipType() {
            return relationshipType;
        }

        /**
         * Generates the <tt>hashCode</tt> value for this immutable edge.
         *
         * <p>
         * This is used to calculate the hashCode once on construction.
         * </p>
         *
         * @return the <tt>int</tt> hashCode
         */
        private int generateHashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getKam().hashCode();
            result = prime
                    * result
                    + ((relationshipType == null) ? 0 : relationshipType
                            .hashCode());
            result = prime * result
                    + ((sourceNode == null) ? 0 : sourceNode.hashCode());
            result = prime * result
                    + ((targetNode == null) ? 0 : targetNode.hashCode());
            return result;
        }

        /**
         * Generates the {@link String label} of this edge.
         *
         * <p>
         * This is used to calculate the label once on construction.
         * </p>
         *
         * @return the {@link String label}
         */
        private String generateLabel() {
            StringBuilder sb = new StringBuilder();
            sb.append(sourceNode.toString());
            sb.append(" ");
            sb.append(relationshipType.getDisplayValue());
            sb.append(" ");
            sb.append(targetNode.toString());
            return sb.toString();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return hashCode;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(KamEdge.class.isAssignableFrom(obj.getClass()))) {
                return false;
            }
            KamEdge other = (KamEdge) obj;
            if (!getKam().equals(other.getKam())) {
                return false;
            }
            if (relationshipType != other.getRelationshipType()) {
                return false;
            }
            if (sourceNode == null) {
                if (other.getSourceNode() != null) {
                    return false;
                }
            } else if (!sourceNode.equals(other.getSourceNode())) {
                return false;
            }
            if (targetNode == null) {
                if (other.getTargetNode() != null) {
                    return false;
                }
            } else if (!targetNode.equals(other.getTargetNode())) {
                return false;
            }
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return label;
        }
    }

    /**
     * {@link KamNode} represents a node in the {@link Kam kam}.
     *
     * @author julianjray
     */
    public final class KamNodeImpl extends KamElementImpl implements KamNode {

        private final FunctionEnum functionType;
        private final String label;

        /**
         * Precalculate the kam node hash code since the object is immutable.
         */
        private final int hashCode;

        /**
         *
         * @param id
         * @param functionType
         * @param label
         */
        private KamNodeImpl(Kam kam, Integer id, FunctionEnum functionType,
                String label) {
            super(kam, id);
            this.functionType = functionType;
            this.label = label;

            this.hashCode = generateHashCode();
        }

        /**
         *
         * @return {@link FunctionEnum}
         */
        @Override
        public FunctionEnum getFunctionType() {
            return functionType;
        }

        /**
         *
         * @return {@link String}
         */
        @Override
        public String getLabel() {
            return label;
        }

        /**
         * Generates the <tt>hashCode</tt> value for this immutable node.
         *
         * <p>
         * This is used to calculate the hashCode once on construction.
         * </p>
         *
         * @return the <tt>int</tt> hashCode
         */
        private int generateHashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getKam().hashCode();
            result = prime * result
                    + ((functionType == null) ? 0 : functionType.hashCode());
            result = prime * result + ((label == null) ? 0 : label.hashCode());
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return hashCode;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!KamNode.class.isAssignableFrom(obj.getClass())) {
                return false;
            }
            KamNode other = (KamNode) obj;
            if (!getKam().equals(other.getKam())) {
                return false;
            }
            if (getId() == null) {
                if (other.getId() != null) {
                    return false;
                }
            } else if (!getId().equals(other.getId())) {
                return false;
            }
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return label;
        }
    }
}
