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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

/**
 * A {@link KamDialect} applies a {@link Dialect} to a {@link KamNode}s as
 * needed.<br>
 * When {@link KamNode}s are returned, the concrete object will be a wrapper
 * around the original {@link KamNode} that applies the {@link Dialect} to
 * generate a different label. Implementation considerations:
 * <ul>
 * <li>New wrapper objects are instantiated for every {@link KamNode} and
 * {@link KamEdge} returned. This prevents additional memory requirements if a
 * reference to the {@link KamDialect} is maintained by the user. There is
 * additional young generation memory needs that may be tweaked with changes to
 * gc if needed.</li>
 * <li>The {@link Dialect} is queried for every call to
 * {@link KamNode#getLabel()} is made. This allows {@link Dialect}
 * implementations to provide caching if desired without introducing additional
 * memory overhead.</li>
 * <li>This implementation assumes {@link NodeFilter}s and {@link EdgeFilter}s
 * do not perform any actions on the node labels. If/when this is changed, this
 * implementation must be modified to account for filters that now need access
 * to the label generated by the {@link Dialect}.</li>
 * </ul>
 *
 * @author Steve Ungerer
 */
public final class KamDialect implements Kam {

    private final Kam kam;
    private final Dialect dialect;

    /**
     * Construct a new {@link KamDialect}
     * @param kam The original {@link Kam}. Must not be <code>null</code>.
     * @param dialect The {@link Dialect} to apply. Must not be <code>null</code>.
     * @throws InvalidArgument Thrown if the provided {@link Kam} or {@link Dialect} is <code>null</code>.
     */
    public KamDialect(Kam kam, Dialect dialect) throws InvalidArgument {
        if (kam == null) {
            throw new InvalidArgument("Kam must not be null");
        }
        if (dialect == null) {
            throw new InvalidArgument("Dialect must not be null");
        }
        this.kam = kam;
        this.dialect = dialect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getId() {
        return kam.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamInfo getKamInfo() {
        return kam.getKamInfo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeFilter createNodeFilter() {
        return kam.createNodeFilter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EdgeFilter createEdgeFilter() {
        return kam.createEdgeFilter();
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
        for (KamNode kamNode : getNodes()) {
            boolean passedFilter = (filter == null || filter.accept(kamNode));

            if (passedFilter) {
                String nodeLabel = kamNode.getLabel();
                if (nodeLabel.equalsIgnoreCase(label)) {
                    return kamNode;
                }
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
        return wrapNode(kam.findNode(kamNodeId, filter));
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
        for (KamNode node : getNodes()) {
            boolean passedFilter = (filter == null || filter.accept(node));

            if (passedFilter && labelPattern.matcher(node.getLabel()).matches()) {
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
        return wrapNodes(kam.getAdjacentNodes(kamNode));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection) {
        return wrapNodes(kam.getAdjacentNodes(kamNode, edgeDirection));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter edgeFilter) {
        return wrapNodes(kam.getAdjacentNodes(kamNode, edgeDirection,
                edgeFilter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, NodeFilter filter) {
        return wrapNodes(kam.getAdjacentNodes(kamNode, edgeDirection, filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeFilter edgeFilter, NodeFilter nodeFilter) {
        return wrapNodes(kam.getAdjacentNodes(kamNode, edgeFilter, nodeFilter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter edgeFilter,
            NodeFilter nodeFilter) {
        return wrapNodes(kam.getAdjacentNodes(kamNode, edgeDirection,
                edgeFilter, nodeFilter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode) {
        return wrapEdges(kam.getAdjacentEdges(kamNode));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode, EdgeFilter filter) {
        return wrapEdges(kam.getAdjacentEdges(kamNode, filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeDirectionType edgeDirection) {
        return wrapEdges(kam.getAdjacentEdges(kamNode, edgeDirection));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter filter) {
        return wrapEdges(kam.getAdjacentEdges(kamNode, edgeDirection, filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getEdges(KamNode sourceNode, KamNode targetNode) {
        return wrapEdges(kam.getEdges(sourceNode, targetNode));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getEdges(KamNode sourceNode, KamNode targetNode,
            EdgeFilter filter) {
        return wrapEdges(kam.getEdges(sourceNode, targetNode, filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge findEdge(Integer kamEdgeId) {
        return wrapEdge(kam.findEdge(kamEdgeId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge findEdge(KamNode sourceNode,
            RelationshipType relationshipType, KamNode targetNode)
            throws InvalidArgument {
        return wrapEdge(kam.findEdge(sourceNode, relationshipType, targetNode));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(KamNode kamNode) {
        return kam.contains(kamNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(KamEdge kamEdge) {
        return kam.contains(kamEdge);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamNode> getNodes() {
        return wrapNodes(kam.getNodes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamNode> getNodes(NodeFilter filter) {
        return wrapNodes(kam.getNodes(filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamEdge> getEdges() {
        return wrapEdges(kam.getEdges());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamEdge> getEdges(EdgeFilter filter) {
        return wrapEdges(kam.getEdges(filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void union(Collection<KamEdge> kamEdges) throws InvalidArgument {
        // TODO does this need to be implemented?
        throw new UnsupportedOperationException(
                "union() is not supported by DialectKam");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge createEdge(Integer kamEdgeId, KamNode sourceNode,
            RelationshipType relationshipType, KamNode targetNode)
            throws InvalidArgument {
        return kam.createEdge(kamEdgeId, sourceNode, relationshipType,
                targetNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(KamEdge kamEdge) {
        kam.removeEdge(kamEdge);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge replaceEdge(KamEdge kamEdge, FunctionEnum sourceFunction,
            String sourceLabel, RelationshipType relationship,
            FunctionEnum targetFunction, String targetLabel) {
        return kam.replaceEdge(kamEdge, sourceFunction, sourceLabel,
                relationship, targetFunction, targetLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge replaceEdge(KamEdge kamEdge, KamEdge replacement) {
        return kam.replaceEdge(kamEdge, replacement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode createNode(Integer id, FunctionEnum functionType,
            String label) throws InvalidArgument {
        return kam.createNode(id, functionType, label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(KamNode kamNode) {
        kam.removeNode(kamNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode replaceNode(KamNode kamNode, FunctionEnum function,
            String label) {
        return kam.replaceNode(kamNode, function, label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode replaceNode(KamNode kamNode, KamNode replacement) {
        return kam.replaceNode(kamNode, replacement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collapseNode(KamNode from, KamNode to) {
        kam.collapseNode(from, to);
    }

    private Set<KamNode> wrapNodes(Collection<KamNode> nodes) {
        Set<KamNode> ret = new LinkedHashSet<KamNode>(nodes.size());
        for (KamNode n : nodes) {
            KamDialectNode d = wrapNode(n);
            if (d != null) {
                ret.add(d);
            }
        }
        return ret;
    }

    private Set<KamEdge> wrapEdges(Collection<KamEdge> edges) {
        Set<KamEdge> ret = new LinkedHashSet<KamEdge>(edges.size());
        for (KamEdge e : edges) {
            KamDialectEdge d = wrapEdge(e);
            if (d != null) {
                ret.add(d);
            }
        }
        return ret;
    }

    private KamDialectNode wrapNode(KamNode kamNode) {
        return kamNode == null ? null : new KamDialectNode(kamNode);
    }

    private KamDialectEdge wrapEdge(KamEdge kamEdge) {
        return kamEdge == null ? null : new KamDialectEdge(kamEdge);
    }

    protected final class KamDialectNode implements KamNode {
        private final KamNode kamNode;

        protected KamDialectNode(KamNode kamNode) {
            this.kamNode = kamNode;
        }

        @Override
        public Kam getKam() {
            return kamNode.getKam();
        }

        @Override
        public Integer getId() {
            return kamNode.getId();
        }

        @Override
        public FunctionEnum getFunctionType() {
            return kamNode.getFunctionType();
        }

        @Override
        public String getLabel() {
            return dialect.getLabel(kamNode);
        }

        /**
         * Delegate to original {@link KamNode} to ensure a
         * {@link KamDialectNode} can be used as a parameter for methods where
         * map lookups are performed.
         */
        @Override
        public int hashCode() {
            return kamNode.hashCode();
        }

        /**
         * Delegate to original {@link KamNode} to ensure a
         * {@link KamDialectNode} can be used as a parameter for methods where
         * map lookups are performed.
         */
        @Override
        public boolean equals(Object obj) {
            return kamNode.equals(obj);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return getLabel();
        }
    }

    protected final class KamDialectEdge implements KamEdge {
        private final KamEdge kamEdge;

        protected KamDialectEdge(KamEdge kamEdge) {
            this.kamEdge = kamEdge;
        }

        @Override
        public Kam getKam() {
            return kamEdge.getKam();
        }

        @Override
        public Integer getId() {
            return kamEdge.getId();
        }

        @Override
        public KamNode getSourceNode() {
            return wrapNode(kamEdge.getSourceNode());
        }

        @Override
        public KamNode getTargetNode() {
            return wrapNode(kamEdge.getTargetNode());
        }

        @Override
        public RelationshipType getRelationshipType() {
            return kamEdge.getRelationshipType();
        }

        /**
         * Delegate to original {@link KamEdge} to ensure a
         * {@link KamDialectEdge} can be used as a parameter for methods where
         * map lookups are performed.
         */
        @Override
        public int hashCode() {
            return kamEdge.hashCode();
        }

        /**
         * Delegate to original {@link KamEdge} to ensure a
         * {@link KamDialectEdge} can be used as a parameter for methods where
         * map lookups are performed.
         */
        @Override
        public boolean equals(Object obj) {
            return kamEdge.equals(obj);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getSourceNode().toString());
            sb.append(" ");
            sb.append(getRelationshipType().getDisplayValue());
            sb.append(" ");
            sb.append(getTargetNode().toString());
            return sb.toString();
        }
    }
}
