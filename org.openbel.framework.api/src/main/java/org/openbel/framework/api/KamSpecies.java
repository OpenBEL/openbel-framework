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

import static org.openbel.framework.api.EdgeDirectionType.FORWARD;
import static org.openbel.framework.api.EdgeDirectionType.REVERSE;
import static org.openbel.framework.common.BELUtilities.constrainedHashSet;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.enums.RelationshipType.ACTS_IN;
import static org.openbel.framework.common.enums.RelationshipType.ORTHOLOGOUS;
import static org.openbel.framework.common.enums.RelationshipType.TRANSCRIBED_TO;
import static org.openbel.framework.common.enums.RelationshipType.TRANSLATED_TO;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;

/**
 * {@link KamSpecies} defines a {@link Kam kam} that provides a
 * species-specific view using a {@link SpeciesDialect species dialect}.
 *
 * The {@link KamSpecies} orthologization occurs on construction in
 * {@link KamSpecies#KamSpecies(Kam, SpeciesDialect, KamStore)}.  The
 * existing {@link Kam} is cloned to preserve the original {@link Kam}.
 *
 * The process of {@link Kam} orthologization involves:
 * <ol>
 * <li>Removing orthologous edges.</li>
 * <li>Redirecting (remove/create) ortholog's edges to the species node
 * being collapsed to.</li>
 * <li>Remove ortholog nodes.  This is done at construction of
 * {@link KamSpecies}.</ol>
 *
 * @see SpeciesDialect
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class KamSpecies implements Kam {

    private static final RelationshipType[] RELS = new RelationshipType[] {
        ACTS_IN,
        TRANSCRIBED_TO,
        TRANSLATED_TO
    };

    /**
     * A copy of the original {@link Kam kam} to filter on.
     */
    private final Kam kamCopy;

    /**
     * Defines {@link KamEdge KAM edges} that should be traversed when
     * inferring orthologous relationships.
     */
    private final EdgeFilter inferFilter;
    private final SpeciesDialect speciesDialect;
    private final KamStore kamStore;

    private Map<Integer, Integer> onodes;
    private Map<Integer, TermParameter> speciesParams;
    private Map<Integer, TermParameter> nodeParamMap =
            new HashMap<Integer, TermParameter>();
    private Map<Integer, TermParameter> edgeParamMap =
            new HashMap<Integer, TermParameter>();


    /**
     * Constructs a {@link KamSpecies species-specific kam}.
     *
     * @param kam {@link Kam} to orthologize, which cannot be {@code null}
     * @param speciesDialect {@link SpeciesDialect} to control species
     * ortholgization, which cannot be {@code null}
     * @param kamStore {@link KamStore} to find {@link BelTerm}s for
     * {@link KamNode}, which cannot be {@code null}
     * @throws KamStoreException Thrown if an issue arose accessing the
     * {@link KamStore}
     * @throws InvalidArgument Thrown if {@code kam}, {@code speciesDialect},
     * or {@code kamStore} is {@code null}
     */
    public KamSpecies(final Kam kam,
            final SpeciesDialect speciesDialect,
            final KamStore kamStore) throws KamStoreException {
        this.kamCopy = copy(kam);
        this.speciesDialect = speciesDialect;
        this.kamStore = kamStore;

        findOrthologs();

        // TODO Degredations using DIRECTLY_DECREASES
        // TODO Translocations using TRANSLOCATES
        inferFilter = createEdgeFilter();
        final RelationshipTypeFilterCriteria c =
                new RelationshipTypeFilterCriteria();
        c.getValues().addAll(Arrays.asList(RELS));
        inferFilter.add(c);

        final Collection<Integer> speciesNodes = onodes.values();
        final Set<Integer> species = new LinkedHashSet<Integer>(
                speciesNodes.size());
        species.addAll(speciesNodes);

        replaceOrthologousEdges(kamCopy, onodes);
        removeOrthologousNodes(onodes);
        inferOrthologs(species);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getId() {
        return kamCopy.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamInfo getKamInfo() {
        return kamCopy.getKamInfo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeFilter createNodeFilter() {
        return kamCopy.createNodeFilter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EdgeFilter createEdgeFilter() {
        return kamCopy.createEdgeFilter();
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
        return wrapNode(kamCopy.findNode(kamNodeId, filter));
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
        return wrapNodes(kamCopy.getAdjacentNodes(kamNode));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection) {
        return wrapNodes(kamCopy.getAdjacentNodes(kamNode, edgeDirection));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter edgeFilter) {
        return wrapNodes(kamCopy.getAdjacentNodes(kamNode, edgeDirection,
                edgeFilter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, NodeFilter filter) {
        return wrapNodes(kamCopy.getAdjacentNodes(kamNode, edgeDirection, filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeFilter edgeFilter, NodeFilter nodeFilter) {
        return wrapNodes(kamCopy.getAdjacentNodes(kamNode, edgeFilter, nodeFilter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter edgeFilter,
            NodeFilter nodeFilter) {
        return wrapNodes(kamCopy.getAdjacentNodes(kamNode, edgeDirection,
                edgeFilter, nodeFilter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode) {
        return wrapEdges(kamCopy.getAdjacentEdges(kamNode));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode, EdgeFilter filter) {
        return wrapEdges(kamCopy.getAdjacentEdges(kamNode, filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeDirectionType edgeDirection) {
        return wrapEdges(kamCopy.getAdjacentEdges(kamNode, edgeDirection));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter filter) {
        return wrapEdges(kamCopy.getAdjacentEdges(kamNode, edgeDirection, filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getEdges(KamNode sourceNode, KamNode targetNode) {
        return wrapEdges(kamCopy.getEdges(sourceNode, targetNode));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getEdges(KamNode sourceNode, KamNode targetNode,
            EdgeFilter filter) {
        return wrapEdges(kamCopy.getEdges(sourceNode, targetNode, filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge findEdge(Integer kamEdgeId) {
        return wrapEdge(kamCopy.findEdge(kamEdgeId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge findEdge(KamNode sourceNode,
            RelationshipType relationshipType, KamNode targetNode)
            throws InvalidArgument {
        return wrapEdge(kamCopy.findEdge(sourceNode, relationshipType, targetNode));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(KamNode kamNode) {
        return kamCopy.contains(kamNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(KamEdge kamEdge) {
        return kamCopy.contains(kamEdge);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamNode> getNodes() {
        return wrapNodes(kamCopy.getNodes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamNode> getNodes(NodeFilter filter) {
        return wrapNodes(kamCopy.getNodes(filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamEdge> getEdges() {
        return wrapEdges(kamCopy.getEdges());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamEdge> getEdges(EdgeFilter filter) {
        return wrapEdges(kamCopy.getEdges(filter));
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
        return kamCopy.createEdge(kamEdgeId, sourceNode, relationshipType,
                targetNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(KamEdge kamEdge) {
        kamCopy.removeEdge(kamEdge);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge replaceEdge(KamEdge kamEdge, FunctionEnum sourceFunction,
            String sourceLabel, RelationshipType relationship,
            FunctionEnum targetFunction, String targetLabel) {
        return kamCopy.replaceEdge(kamEdge, sourceFunction, sourceLabel,
                relationship, targetFunction, targetLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge replaceEdge(KamEdge kamEdge, KamEdge replacement) {
        return kamCopy.replaceEdge(kamEdge, replacement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode createNode(Integer id, FunctionEnum functionType,
            String label) throws InvalidArgument {
        return kamCopy.createNode(id, functionType, label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(KamNode kamNode) {
        kamCopy.removeNode(kamNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode replaceNode(KamNode kamNode, FunctionEnum function,
            String label) {
        return kamCopy.replaceNode(kamNode, function, label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode replaceNode(KamNode kamNode, KamNode replacement) {
        return kamCopy.replaceNode(kamNode, replacement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collapseNode(KamNode from, KamNode to) {
        kamCopy.collapseNode(from, to);
    }

    /**
     * Copy the {@link Kam} by creating a new {@link KamImpl} instance with
     * new {@link KamNode}s and {@link KamEdge}s.
     *
     * @param kam {@link Kam}
     * @return the new instance {@link Kam}
     */
    private Kam copy(final Kam kam) {
        if (kam == null) {
            return null;
        }

        final Kam copy = new KamImpl(kam.getKamInfo());

        final Collection<KamNode> nodes = kam.getNodes();
        if (hasItems(nodes)) {
            for (final KamNode node : nodes) {
                copy.createNode(node.getId(), node.getFunctionType(),
                        node.getLabel());
            }
        }

        final Collection<KamEdge> edges = kam.getEdges();
        if (hasItems(edges)) {
            for (final KamEdge edge : edges) {
                final KamNode source = copy.findNode(edge.getSourceNode()
                        .getId());
                final KamNode target = copy.findNode(edge.getTargetNode()
                        .getId());

                assert source != null;
                assert target != null;

                copy.createEdge(edge.getId(), source,
                        edge.getRelationshipType(), target);
            }
        }

        return copy;
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

        TermParameter param = nodeParamMap.get(kamNode.getId());
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

        TermParameter param = edgeParamMap.get(kamEdge.getId());
        if (param != null) {
            return new OrthologousEdge(kamEdge, param);
        }

        return kamEdge;
    }

    /**
     * Find orthologous {@link KamNode nodes} for the target species namespaces
     * using {@link SpeciesDialect#getSpeciesNamespaces()}.  Once the orthologs
     * are found the {@link KamEdge edges} for the opposite orthologs are
     * tracked.
     *
     * @throws KamStoreException Thrown if an error occurred accessing
     * {@link BelTerm terms} from the {@link KamStore kam store}
     */
    private void findOrthologs() throws KamStoreException {
        // create resource location set for species namespaces
        final List<org.openbel.framework.common.model.Namespace> spl = speciesDialect
                .getSpeciesNamespaces();
        final Set<String> rlocs = constrainedHashSet(spl.size());
        for (final org.openbel.framework.common.model.Namespace n : spl) {
            rlocs.add(n.getResourceLocation());
        }

        final Collection<KamEdge> edges = kamCopy.getEdges();
        final Map<Integer, Set<Integer>> oedges =
                new LinkedHashMap<Integer, Set<Integer>>();
        onodes = new LinkedHashMap<Integer, Integer>();
        speciesParams = new HashMap<Integer, TermParameter>();
        for (final KamEdge e : edges) {
            // only evaluate orthologous edges
            if (ORTHOLOGOUS.equals(e.getRelationshipType())) {
                final KamNode sn = e.getSourceNode();
                final KamNode tn = e.getTargetNode();

                TermParameter speciesParam = findParameter(sn, rlocs);
                if (speciesParam != null) {
                    // source node matches target species
                    Integer id = sn.getId();
                    Set<Integer> adjacentEdges = oedges.get(id);
                    if (adjacentEdges == null) {
                        adjacentEdges = new LinkedHashSet<Integer>();
                        oedges.put(id, adjacentEdges);
                    }

                    // collect adjacent edges (except this edge) for the
                    // orthologous target node
                    final Set<KamEdge> orthoEdges = kamCopy.getAdjacentEdges(tn);
                    for (final KamEdge orthoEdge : orthoEdges) {
                        if (orthoEdge != e) {
                            adjacentEdges.add(orthoEdge.getId());
                        }
                    }

                    Integer speciesNodeId = sn.getId();
                    onodes.put(tn.getId(), speciesNodeId);
                    speciesParams.put(speciesNodeId, speciesParam);
                    continue;
                }

                speciesParam = findParameter(tn, rlocs);
                if (speciesParam != null) {
                    // target node matches target species
                    Integer id = tn.getId();
                    Set<Integer> adjacentEdges = oedges.get(id);
                    if (adjacentEdges == null) {
                        adjacentEdges = new LinkedHashSet<Integer>();
                        oedges.put(id, adjacentEdges);
                    }

                    // collect adjacent edges (except this edge) for the
                    // orthologous source node
                    final Set<KamEdge> orthoEdges = kamCopy.getAdjacentEdges(sn);
                    for (final KamEdge orthoEdge : orthoEdges) {
                        if (orthoEdge != e) {
                            adjacentEdges.add(orthoEdge.getId());
                        }
                    }

                    Integer speciesNodeId = tn.getId();
                    onodes.put(sn.getId(), speciesNodeId);
                    speciesParams.put(speciesNodeId, speciesParam);
                }
            }
        }
    }

    /**
     * Matches {@link BelTerm terms} for a {@link KamNode node} against a
     * species-specific {@link Namespace}.
     *
     * @param node {@link KamNode} kam node to match
     * @param rlocs {@link Set} of {@link String resource location}, matches
     * against nodes to find orthologous targets
     * @return {@code true} if {@code node} is backed by a {@link BelTerm term}
     * using the {@code speciesNs} {@link Namespace namespace}
     * @throws InvalidArgument Thrown if {@code node} is null while retrieving
     * supporting terms
     * @throws KamStoreException Thrown if the {@link Kam kam} could not be
     * determined while retrieving supporting terms or parameters
     */
    private TermParameter findParameter(final KamNode node,
            final Set<String> rlocs) throws KamStoreException {

        // no resource locations to match against, no match
        if (noItems(rlocs)) {
            return null;
        }

        final List<BelTerm> terms = kamStore.getSupportingTerms(node);
        for (final BelTerm term : terms) {
            final List<TermParameter> params = kamStore.getTermParameters(
                    kamCopy.getKamInfo(),
                    term);
            for (final TermParameter p : params) {

                // skip empty namespace, continue
                final Namespace ns = p.getNamespace();
                if (ns == null) {
                    continue;
                }

                // if parameter namespace in rlocs, match
                if (rlocs.contains(ns.getResourceLocation())) {
                    return p;
                }
            }
        }

        return null;
    }

    /**
     * Replace orthologous relationships by collapsing to the species
     * {@link KamNode node}.  In particular:
     * <ul>
     * <li>Remove {@link RelationshipType#ORTHOLOGOUS} edges.</li>
     * <li>Redirect {@link KamNode ortholog node}'s edges to the
     * {@link KamNode species replacement node}.
     * </ul>
     *
     * @param kam {@link KamImpl} original kam
     * @param onodes {@link Map} of K: {@link Integer orthologous node id} and
     * V: {@link Integer species replacement node id}
     */
    private void replaceOrthologousEdges(final Kam kam,
            final Map<Integer, Integer> onodes) {
        final Collection<KamEdge> edges = kam.getEdges();
        for (final KamEdge edge : edges) {
            if (ORTHOLOGOUS.equals(edge.getRelationshipType())) {
                // remove all orthologous edges
                kam.removeEdge(edge);
            } else {
                // redirect ortholog's relationships to species replacement
                final KamNode sn = edge.getSourceNode();
                final KamNode tn = edge.getTargetNode();

                // when edge's source node is the orthologous node
                // find the corresponding species
                Integer species = onodes.get(sn.getId());
                if (species != null) {
                    // replace the edge's source as the species node
                    KamNode speciesNode = kam.findNode(species);
                    kam.removeEdge(edge);
                    kam.createEdge(edge.getId(), speciesNode,
                            edge.getRelationshipType(), tn);
                    continue;
                }

                // when edge's target node is the orthologous node
                // find the corresponding species
                species = onodes.get(tn.getId());
                if (species != null) {
                    // replace the edge's target as the species node
                    KamNode speciesNode = kam.findNode(species);
                    kam.removeEdge(edge);
                    kam.createEdge(edge.getId(), sn,
                            edge.getRelationshipType(), speciesNode);
                    continue;
                }
            }
        }
    }

    /**
     * Remove {@link KamNode orthologous nodes}.
     *
     * @param onodes {@link Map} of K: {@link Integer orthologous node id} and
     * V: {@link Integer species replacement node id}
     */
    private void removeOrthologousNodes(final Map<Integer, Integer> onodes) {
        // remove ortholog nodes since edge's are now collapsed to species node
        // replacements
        final Collection<KamNode> nodes = kamCopy.getNodes();
        for (final KamNode node : nodes) {
            if (onodes.containsKey(node.getId())) {
                kamCopy.removeNode(node);
            }
        }
    }

    /**
     * Infers orthologous {@link KamEdge edges} downstream and upstream from
     * all {@link KamNode species replacement nodes}.
     *
     * @param species {@link Set} of {@link Integer} species replacement node
     * ids
     */
    private void inferOrthologs(final Set<Integer> species) {
        for (final Integer sid : species) {
            final KamNode snode = findNode(sid);
            final TermParameter param = speciesParams.get(snode.getId());

            // recurse incoming connectsion from species node
            recurseConnections(snode, param, REVERSE);

            // recurse outgoing connections from species node
            recurseConnections(snode, param, FORWARD);
        }
    }

    /**
     * Walks {@link KamSpecies#RELS certain relationship types} and infers
     * orthologous edges based on matching relationships.
     *
     * <p>
     * For instance if there are two {@code transcribedTo} edges from an
     * orthologized {@code geneAbundance} then we infer that the downstream
     * {@code rnaAbundance}s are also orthologous and collapse to the first
     * one.
     * </p>
     *
     * @param snode {@link KamNode} species node to walk from
     * @param param {@link TermParameter} for orthologous species node
     * @param direction {@link EdgeDirectionType} direction to walk
     */
    private void recurseConnections(final KamNode snode,
            TermParameter param, final EdgeDirectionType direction) {
        // get adjacent edges that can be inferred
        final Set<KamEdge> out = getAdjacentEdges(snode, direction, inferFilter);

        // map ACTS_IN edges by activity function
        final Map<FunctionEnum, KamNode> acts =
                new HashMap<FunctionEnum, KamNode>();
        final Map<RelationshipType, KamNode> rels =
                new HashMap<RelationshipType, Kam.KamNode>();
        for (final KamEdge e : out) {
            // get correct edge opposite node based on search direction
            final KamNode opnode = (direction == FORWARD ? e.getTargetNode()
                    : e.getSourceNode());

            // handle ACTS_IN edge independently since we care about similar
            // activity functions
            if (e.getRelationshipType() == ACTS_IN) {
                final FunctionEnum actfun = opnode.getFunctionType();

                // lookup first seen node for activity function
                KamNode node = acts.get(actfun);

                // if not yet seen mark opposite node and edge as species collapse
                // target.  continue to next edge.
                if (node == null) {
                    acts.put(opnode.getFunctionType(), opnode);
                    nodeParamMap.put(opnode.getId(), param);
                    edgeParamMap.put(e.getId(), param);
                    continue;
                }

                kamCopy.collapseNode(opnode, node);
            } else {
                // handle all other edges by relationship type
                final RelationshipType rel = e.getRelationshipType();

                // lookup first seen relationship type
                KamNode node = rels.get(rel);

                // if not yet seen mark opposite node and edge as species collapse
                // target.  continue to next edge.
                if (node == null) {
                    rels.put(rel, opnode);
                    nodeParamMap.put(opnode.getId(), param);
                    edgeParamMap.put(e.getId(), param);
                    continue;
                }

                kamCopy.collapseNode(opnode, node);
            }
        }

        // recursively process all collapsed nodes
        Collection<KamNode> actn = acts.values();
        Collection<KamNode> reln = rels.values();
        final Set<KamNode> nodes = constrainedHashSet(actn.size()
                + reln.size());
        nodes.addAll(actn);
        nodes.addAll(reln);
        for (final KamNode n : nodes) {
            recurseConnections(n, param, direction);
        }
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
            return speciesDialect.getLabel(node, speciesParameter);
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
