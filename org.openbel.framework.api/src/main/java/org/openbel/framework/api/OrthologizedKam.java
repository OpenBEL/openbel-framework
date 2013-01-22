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

import static org.openbel.framework.common.BELUtilities.nulls;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;

/**
 * {@link OrthologizedKam} defines a {@link Kam kam} that provides a
 * orthologized view using a {@link SpeciesDialect species dialect}.
 *
 * The {@link OrthologizedKam} orthologization occurs on construction in
 * {@link OrthologizedKam#OrthologizedKam(Kam, SpeciesDialect, KAMStore)}.
 * The existing {@link Kam} is cloned to preserve the original {@link Kam}.
 *
 * The process of {@link Kam} orthologization involves:
 * <ol>
 * <li>Removing orthologous edges.</li>
 * <li>Reconnect orthologous {@link KamEdge kam edges} to the collapsed
 * species nodes.</li>
 * <li>Remove ortholog {@link KamNode kam nodes}.  This is done at
 * construction of {@link OrthologizedKam}.</ol>
 *
 * @see SpeciesDialect
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class OrthologizedKam implements Kam {

    /**
     * Copy of the provided {@link Kam kam}.
     */
    private final Kam kam;
    
    /**
     * Dialect that determines which {@link KamNode kam nodes} to collapse.
     */
    private final SpeciesDialect dialect;
    
    /**
     * A map of kam node id to the species namespace parameter.
     */
    private final Map<Integer, TermParameter> ntp;
    
    /**
     * A map of kam edge id to the species namespace parameter. 
     */
    private final Map<Integer, TermParameter> etp;
    
    /**
     * A map of kam node that collapsed to species node.
     */
    private final Map<KamNode, KamNode> collapsed;

    /**
     * Constructs a {@link OrthologizedKam species-specific kam}.
     *
     * @param kam {@link Kam} to orthologize, which cannot be {@code null}
     * @param dialect {@link SpeciesDialect} to control species
     * ortholgization, which cannot be {@code null}
     * @param ntp {@link Map} of node id to {@link TermParameter}
     * @param etp {@link Map} of edge id to {@link TermParameter}
     * @param collapsed {@link Map} of collapsed orthologous node to species node
     * @throws InvalidArgument Thrown if {@code kam}, {@code speciesDialect},
     * or {@code kamStore} is {@code null}
     */
    OrthologizedKam(Kam kam, SpeciesDialect dialect,
            Map<Integer, TermParameter> ntp, Map<Integer, TermParameter> etp,
            Map<KamNode, KamNode> collapsed) throws KAMStoreException {
        if (nulls(kam, dialect, ntp, etp, collapsed))
            throw new InvalidArgument("parameter(s) are null");
        this.kam = kam;
        this.dialect = dialect;
        this.ntp = ntp;
        this.etp = etp;
        this.collapsed = collapsed;
    }

    /**
     * Returns the {@link Map} of orthologous {@link KamNode kam nodes} that
     * were collapsed to species target {@link KamNode kam nodes}.
     * 
     * @return {@link Map} of K: {@link KamNode}, V: {@link KamNode}
     */
    public Map<KamNode, KamNode> getCollapsedNodes() {
        return collapsed;
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
            KamNode w = wrapNode(n);
            if (w != null) {
                ret.add(w);
            }
        }
        return ret;
    }

    private Set<KamEdge> wrapEdges(Collection<KamEdge> edges) {
        Set<KamEdge> ret = new LinkedHashSet<KamEdge>(edges.size());
        for (KamEdge e : edges) {
            KamEdge w = wrapEdge(e);
            if (w != null) {
                ret.add(w);
            }
        }
        return ret;
    }

    /**
     * Wrap a {@link KamNode} as an {@link OrthologousNode} to allow conversion
     * of the node label by the {@link SpeciesDialect}.
     *
     * @param kamNode {@link KamNode}
     * @return the wrapped kam node,
     * <ol>
     * <li>{@code null} if {@code kamNode} input is {@code null}</li>
     * <li>{@link OrthologousNode} if {@code kamNode} has orthologized</li>
     * <li>the original {@code kamNode} input if it has not orthologized</li>
     * </ol>
     */
    private KamNode wrapNode(KamNode kamNode) {
        if (kamNode == null) {
            return null;
        }

        TermParameter param = ntp.get(kamNode.getId());
        if (param != null) {
            return new OrthologousNode(kamNode, param);
        }

        return kamNode;
    }

    /**
     * Wrap a {@link KamEdge} as an {@link OrthologousEdge} to allow conversion
     * of the edge label by the {@link SpeciesDialect}.  The edge's
     * {@link KamNode}s are also wrapped.
     *
     * @param kamEdge {@link KamEdge}
     * @return the wrapped kam edge,
     * <ol>
     * <li>{@code null} if {@code kamEdge} input is {@code null}</li>
     * <li>{@link OrthologousEdge} if {@code kamEdge} has orthologized</li>
     * <li>the original {@code kamEdge} input if it has not orthologized</li>
     * </ol>
     */
    private KamEdge wrapEdge(KamEdge kamEdge) {
        if (kamEdge == null) {
            return null;
        }

        TermParameter param = etp.get(kamEdge.getId());
        if (param != null) {
            return new OrthologousEdge(kamEdge, param);
        }

        return kamEdge;
    }

    protected final class OrthologousNode implements KamNode {

        private final KamNode node;
        private final TermParameter speciesParameter;

        private OrthologousNode(final KamNode node,
                final TermParameter speciesParameter) {
            this.node = node;
            this.speciesParameter = speciesParameter;
        }

        @Override
        public Kam getKam() {
            return node.getKam();
        }

        @Override
        public Integer getId() {
            return node.getId();
        }

        @Override
        public FunctionEnum getFunctionType() {
            return node.getFunctionType();
        }

        @Override
        public String getLabel() {
            return dialect.getLabel(node, speciesParameter);
        }

        public TermParameter getSpeciesParameter() {
            return speciesParameter;
        }

        /**
         * Delegate to original {@link KamNode} to ensure an
         * {@link OrthologousNode} can be used as a parameter for methods where
         * map lookups are performed.
         */
        @Override
        public int hashCode() {
            return node.hashCode();
        }

        /**
         * Delegate to original {@link KamNode} to ensure an
         * {@link OrthologousNode} can be used as a parameter for methods where
         * map lookups are performed.
         */
        @Override
        public boolean equals(Object obj) {
            return node.equals(obj);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return getLabel();
        }
    }

    private final class OrthologousEdge implements KamEdge {

        private final KamEdge kamEdge;
        private final TermParameter speciesParameter;

        protected OrthologousEdge(final KamEdge kamEdge,
                final TermParameter speciesParameter) {
            this.kamEdge = kamEdge;
            this.speciesParameter = speciesParameter;
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
            return new OrthologousNode(kamEdge.getSourceNode(),
                    speciesParameter);
        }

        @Override
        public KamNode getTargetNode() {
            return new OrthologousNode(kamEdge.getTargetNode(),
                    speciesParameter);
        }

        @Override
        public RelationshipType getRelationshipType() {
            return kamEdge.getRelationshipType();
        }

        /**
         * Delegate to original {@link KamEdge} to ensure an
         * {@link OrthologousEdge} can be used as a parameter for methods where
         * map lookups are performed.
         */
        @Override
        public int hashCode() {
            return kamEdge.hashCode();
        }

        /**
         * Delegate to original {@link KamEdge} to ensure an
         * {@link OrthologousEdge} can be used as a parameter for methods where
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
