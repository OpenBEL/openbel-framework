package org.openbel.framework.api;

import static org.openbel.framework.api.EdgeDirectionType.FORWARD;
import static org.openbel.framework.api.EdgeDirectionType.REVERSE;
import static org.openbel.framework.common.BELUtilities.constrainedHashSet;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.enums.RelationshipType.ACTS_IN;
import static org.openbel.framework.common.enums.RelationshipType.ORTHOLOGOUS;
import static org.openbel.framework.common.enums.RelationshipType.TRANSCRIBED_TO;
import static org.openbel.framework.common.enums.RelationshipType.TRANSLATED_TO;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.internal.KAMStoreDaoImpl.TermParameter;

public class KamSpecies implements Kam {

    private static final RelationshipType[] RELS = new RelationshipType[] {
        ACTS_IN,
        TRANSCRIBED_TO,
        TRANSLATED_TO
    };

    /**
     * The original {@link Kam kam} to base species-specific filtering on.
     */
    private final KamDialect kam;

    /**
     * Defines {@link KamEdge KAM edges} that should be traversed when
     * inferring orthologous relationships.
     */
    private final EdgeFilter inferFilter;

    private SpeciesDialect speciesDialect;

    private KamStore kamStore;

    private Map<Integer, Integer> onodes;
    private Map<Integer, String> sparams;

    /**
     * Constructs a {@link SpeciesKam species-specific kam} from another
     * {@link Kam kam} by:
     * <ol>
     * <li>Removing orthologous edges.  This is done at construction of
     * {@link SpeciesKam}.</li>
     * <li>Redirecting (remove/create) ortholog's edges to the species node
     * being collapsed to.  This is done at construction of
     * {@link SpeciesKam}.</li>
     * <li>Remove ortholog nodes.  This is done at construction of
     * {@link SpeciesKam}.  TODO The ortholog terms do not collapse!</li>
     * </ol>
     *
     * @param kam {@link Kam}, which cannot be {@code null}
     * @param onodes {@link Set} of {@link Integer kam node id} for
     * orthologous node, which cannot be {@code null}
     * @throws KamStoreException
     * @throws InvalidArgument Thrown if {@code kam} or {@code orthoEdges} is
     * {@code null}
     */
    public KamSpecies(final Kam kam,
            final SpeciesDialect speciesDialect,
            final KamStore kamStore) throws InvalidArgument, KamStoreException {
        this.speciesDialect = speciesDialect;
        this.kamStore = kamStore;
        this.kam = new KamDialect(kam, speciesDialect);

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

        replaceOrthologousEdges(kam, onodes);
        removeOrthologousNodes(onodes);
        inferOrthologs(species, sparams);
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
        return kam.findNode(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode findNode(String label, NodeFilter filter) {
        return kam.findNode(label, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode findNode(Integer kamNodeId) {
        return kam.findNode(kamNodeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode findNode(Integer kamNodeId, NodeFilter filter) {
        return kam.findNode(kamNodeId, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> findNode(Pattern labelPattern) {
        return kam.findNode(labelPattern);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> findNode(Pattern labelPattern, NodeFilter filter) {
        return kam.findNode(labelPattern, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode) {
        return kam.getAdjacentNodes(kamNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection) {
        return kam.getAdjacentNodes(kamNode, edgeDirection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter edgeFilter) {
        return kam.getAdjacentNodes(kamNode, edgeDirection, edgeFilter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, NodeFilter filter) {
        return kam.getAdjacentNodes(kamNode, edgeDirection, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeFilter edgeFilter, NodeFilter nodeFilter) {
        return kam.getAdjacentNodes(kamNode, edgeFilter, nodeFilter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamNode> getAdjacentNodes(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter edgeFilter,
            NodeFilter nodeFilter) {
        return kam.getAdjacentNodes(kamNode, edgeDirection, edgeFilter,
                nodeFilter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode) {
        return kam.getAdjacentEdges(kamNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode, EdgeFilter filter) {
        return kam.getAdjacentEdges(kamNode, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeDirectionType edgeDirection) {
        return kam.getAdjacentEdges(kamNode, edgeDirection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getAdjacentEdges(KamNode kamNode,
            EdgeDirectionType edgeDirection, EdgeFilter filter) {
        return kam.getAdjacentEdges(kamNode, edgeDirection, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getEdges(KamNode sourceNode, KamNode targetNode) {
        return kam.getEdges(sourceNode, targetNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<KamEdge> getEdges(KamNode sourceNode, KamNode targetNode,
            EdgeFilter filter) {
        return kam.getEdges(sourceNode, targetNode, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge findEdge(Integer kamEdgeId) {
        return kam.findEdge(kamEdgeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamEdge findEdge(KamNode sourceNode,
            RelationshipType relationshipType, KamNode targetNode)
            throws InvalidArgument {
        return kam.findEdge(sourceNode, relationshipType, targetNode);
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
    public Collection<KamEdge> getEdges() {
        return kam.getEdges();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamNode> getNodes() {
        return kam.getNodes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamNode> getNodes(NodeFilter filter) {
        return kam.getNodes(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<KamEdge> getEdges(EdgeFilter filter) {
        return kam.getEdges(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void union(Collection<KamEdge> kamEdges) throws InvalidArgument {
        kam.union(kamEdges);
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

    private void findOrthologs() throws InvalidArgument, KamStoreException {
        // create resource location set for species namespaces
        final List<org.openbel.framework.common.model.Namespace> spl = speciesDialect
                .getSpeciesNamespaces();
        final Set<String> rlocs = constrainedHashSet(spl.size());
        for (final org.openbel.framework.common.model.Namespace n : spl) {
            rlocs.add(n.getResourceLocation());
        }

        final Collection<KamEdge> edges = kam.getEdges();
        final Map<Integer, Set<Integer>> oedges =
                new LinkedHashMap<Integer, Set<Integer>>();
        onodes = new LinkedHashMap<Integer, Integer>();
        sparams = new HashMap<Integer, String>();
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
                    final Set<KamEdge> orthoEdges = kam.getAdjacentEdges(tn);
                    for (final KamEdge orthoEdge : orthoEdges) {
                        if (orthoEdge != e) {
                            adjacentEdges.add(orthoEdge.getId());
                        }
                    }

                    Integer speciesNodeId = sn.getId();
                    onodes.put(tn.getId(), speciesNodeId);

                    String param = speciesParam.getNamespace().getPrefix()
                            + ":" + speciesParam.getParameterValue();
                    sparams.put(speciesNodeId, param);
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
                    final Set<KamEdge> orthoEdges = kam.getAdjacentEdges(sn);
                    for (final KamEdge orthoEdge : orthoEdges) {
                        if (orthoEdge != e) {
                            adjacentEdges.add(orthoEdge.getId());
                        }
                    }

                    Integer speciesNodeId = tn.getId();
                    onodes.put(sn.getId(), speciesNodeId);

                    String param = speciesParam.getNamespace().getPrefix()
                            + ":" + speciesParam.getParameterValue();
                    sparams.put(speciesNodeId, param);
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
            final Set<String> rlocs) throws InvalidArgument,
            KamStoreException {

        // no resource locations to match against, no match
        if (noItems(rlocs)) {
            return null;
        }

        final List<BelTerm> terms = kamStore.getSupportingTerms(node);
        for (final BelTerm term : terms) {
            final List<TermParameter> params = kamStore.getTermParameters(
                    kam.getKamInfo(),
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
        final Collection<KamNode> nodes = kam.getNodes();
        for (final KamNode node : nodes) {
            if (onodes.containsKey(node.getId())) {
                kam.removeNode(node);
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
    private void inferOrthologs(final Set<Integer> species,
            final Map<Integer, String> sparams) {
        for (final Integer sid : species) {
            final KamNode snode = findNode(sid);
            final String sparam = sparams.get(snode.getId());

            // recurse incoming connectsion from species node
            recurseConnections(snode, sparam, REVERSE);

            // recurse outgoing connections from species node
            recurseConnections(snode, sparam, FORWARD);
        }
    }

    /**
     * Walks {@link SpeciesKam#RELS certain relationship types} and infers
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
     * @param sparams
     * @param direction {@link EdgeDirectionType} direction to walk
     */
    private void recurseConnections(final KamNode snode,
            String sparam, final EdgeDirectionType direction) {
        // map edges
        final Set<KamEdge> out = getAdjacentEdges(snode, direction, inferFilter);
        final Map<RelationshipType, Set<KamEdge>> emap = mapEdges(out);

        // consolidate edges
        final Set<Entry<RelationshipType, Set<KamEdge>>> entries = emap
                .entrySet();
        for (final Entry<RelationshipType, Set<KamEdge>> entry : entries) {
            final Set<KamEdge> edges = entry.getValue();

            if (edges.size() > 1) {
                final Iterator<KamEdge> it = edges.iterator();
                final KamEdge first = it.next();
                final KamNode collapseTo = first.getTargetNode();

                while (it.hasNext()) {
                    final KamEdge e = it.next();
                    final KamNode n = e.getTargetNode();

                    if (collapseNodes(collapseTo, n, e.getRelationshipType())) {
                        kam.collapseNode(n, collapseTo);
                        kam.replaceNode(n, new OrthologousNode(n, sparam));
                    }
                }

                recurseConnections(collapseTo, sparam, direction);
            }
        }
    }

    /**
     * Compares {@link KamNode nodes} for collapsibility when inferring
     * orthologous {@link KamEdge edges}.
     *
     * @param n1 {@link KamNode} node 1
     * @param n2 {@link KamNode} node 2
     * @param rel {@link KamEdge} edge relationship common to {@code n1} and
     * {@code n2}
     * @return {@code true} if both nodes can collapse, {@code false} otherwise
     */
    private boolean collapseNodes(final KamNode n1, final KamNode n2,
            final RelationshipType rel) {
        // activity nodes are incompatible when the functions are different
        if (rel == ACTS_IN && n1.getFunctionType() != n2.getFunctionType()) {
            return false;
        }

        return true;
    }

    /**
     * Maps {@link KamEdge edges} to a restricted set of
     * {@link RelationshipType relationships}.  This set is governed by
     * {@link SpeciesKam#RELS} and applied as a {@link EdgeFilter filter}.
     *
     * @param in {@link Set} of {@link KamEdge} edges
     * @return {@link Map} of K: {@link RelationshipType} and V: {@link Set} of
     * {@link KamEdge}
     */
    private Map<RelationshipType, Set<KamEdge>> mapEdges(final Set<KamEdge> in) {
        final Map<RelationshipType, Set<KamEdge>> emap = new LinkedHashMap<RelationshipType, Set<KamEdge>>(
                inferFilter.getFilterCriteria().size());
        for (final KamEdge e : in) {
            final RelationshipType rel = e.getRelationshipType();
            Set<KamEdge> edges = emap.get(rel);
            if (edges == null) {
                edges = new LinkedHashSet<Kam.KamEdge>();
                emap.put(rel, edges);
            }
            edges.add(e);
        }

        return emap;
    }

    private final class OrthologousNode implements KamNode {

        private final KamNode node;
        private final String label;

        private OrthologousNode(final KamNode node, final String label) {
            this.node = node;
            this.label = label;
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
            return label;
        }

        @Override
        public int hashCode() {
            return node.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return node.equals(o);
        }
    }
}
