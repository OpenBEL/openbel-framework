package org.openbel.framework.api;

import static org.openbel.framework.api.EdgeDirectionType.FORWARD;
import static org.openbel.framework.api.EdgeDirectionType.REVERSE;
import static org.openbel.framework.common.BELUtilities.constrainedHashSet;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.BELUtilities.sizedHashMap;
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

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.util.Pair;

public class DefaultOrthologize implements Orthologize {

    private static final RelationshipType[] INFERRED_ORTHOLOGIZED_EDGES =
            new RelationshipType[] {
                    ACTS_IN,
                    TRANSCRIBED_TO,
                    TRANSLATED_TO
            };
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<KamNode, KamNode> orthologousNodes(Kam kam, KAMStore kAMStore,
            SpeciesDialect dialect) {
        // create resource location set for species namespaces
        final List<org.openbel.framework.common.model.Namespace> spl = dialect
                .getSpeciesNamespaces();
        final Set<String> rlocs = constrainedHashSet(spl.size());
        for (final org.openbel.framework.common.model.Namespace n : spl) {
            rlocs.add(n.getResourceLocation());
        }

        final Collection<KamEdge> edges = kam.getEdges();
        final Map<Integer, Set<Integer>> oedges =
                new LinkedHashMap<Integer, Set<Integer>>();
        Map<KamNode, KamNode> onodes = new HashMap<KamNode, KamNode>();
        for (final KamEdge e : edges) {
            // only evaluate orthologous edges
            if (ORTHOLOGOUS.equals(e.getRelationshipType())) {
                final KamNode edgeSource = e.getSourceNode();
                final KamNode edgeTarget = e.getTargetNode();

                TermParameter speciesParam = findParameter(kam, kAMStore,
                        edgeSource, rlocs);
                if (speciesParam != null) {
                    // source node matches target species
                    Integer id = edgeSource.getId();
                    Set<Integer> adjacentEdges = oedges.get(id);
                    if (adjacentEdges == null) {
                        adjacentEdges = new LinkedHashSet<Integer>();
                        oedges.put(id, adjacentEdges);
                    }

                    // collect adjacent edges (except this edge) for the
                    // orthologous target node
                    final Set<KamEdge> orthoEdges = kam.getAdjacentEdges(edgeTarget);
                    for (final KamEdge orthoEdge : orthoEdges) {
                        if (orthoEdge != e) {
                            adjacentEdges.add(orthoEdge.getId());
                        }
                    }

                    onodes.put(edgeTarget, edgeSource);
                    continue;
                }

                speciesParam = findParameter(kam, kAMStore, edgeTarget, rlocs);
                if (speciesParam != null) {
                    // target node matches target species
                    Integer id = edgeTarget.getId();
                    Set<Integer> adjacentEdges = oedges.get(id);
                    if (adjacentEdges == null) {
                        adjacentEdges = new LinkedHashSet<Integer>();
                        oedges.put(id, adjacentEdges);
                    }

                    // collect adjacent edges (except this edge) for the
                    // orthologous source node
                    final Set<KamEdge> orthoEdges = kam.getAdjacentEdges(edgeSource);
                    for (final KamEdge orthoEdge : orthoEdges) {
                        if (orthoEdge != e) {
                            adjacentEdges.add(orthoEdge.getId());
                        }
                    }

                    onodes.put(edgeSource, edgeTarget);
                }
            }
        }
        return onodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrthologizedKam orthologize(Kam kam, KAMStore kAMStore,
            SpeciesDialect dialect) {
        Map<KamNode, KamNode> ortho = orthologousNodes(kam, kAMStore, dialect);
        
        EdgeFilter inferf = kam.createEdgeFilter();
        final RelationshipTypeFilterCriteria c =
                new RelationshipTypeFilterCriteria();
        c.getValues().addAll(Arrays.asList(INFERRED_ORTHOLOGIZED_EDGES));
        inferf.add(c);
        
        final Collection<KamNode> speciesNodes = ortho.values();
        final Set<KamNode> species = new LinkedHashSet<KamNode>(
                speciesNodes.size());
        species.addAll(speciesNodes);
        
        replaceOrthologousEdges(kam, ortho);
        removeOrthologousNodes(kam, ortho);
        Pair<Map<Integer, TermParameter>, Map<Integer, TermParameter>> tpm = 
                inferOrthologs(kam, kAMStore, dialect, inferf, species, ortho);
        
        Map<Integer, TermParameter> ntp = tpm.getFirst();
        Map<Integer, TermParameter> etp = tpm.getSecond();
        
        return new OrthologizedKam(kam, dialect, ntp, etp, ortho);
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
     * @param kam {@link Kam}
     * @param ortho {@link Map} of K: {@link KamNode ortho node} and
     * V: {@link KamNode species target node}
     */
    private static void replaceOrthologousEdges(Kam kam,
            Map<KamNode, KamNode> ortho) {
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
                KamNode species = ortho.get(sn);
                if (species != null) {
                    // replace the edge's source as the species node
                    KamNode speciesNode = kam.findNode(species.getId());
                    kam.removeEdge(edge);
                    kam.createEdge(edge.getId(), speciesNode,
                            edge.getRelationshipType(), tn);
                    continue;
                }

                // when edge's target node is the orthologous node
                // find the corresponding species
                species = ortho.get(tn);
                if (species != null) {
                    // replace the edge's target as the species node
                    KamNode speciesNode = kam.findNode(species.getId());
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
     * @param kam {@link Kam}
     * @param ortho {@link Map} of K: {@link Integer orthologous node id} and
     * V: {@link Integer species replacement node id}
     */
    private static void removeOrthologousNodes(Kam kam,
            Map<KamNode, KamNode> ortho) {
        for (KamNode orthoNode : ortho.keySet())
            kam.removeNode(orthoNode);
    }

    /**
     * Infers orthologous {@link KamEdge edges} downstream and upstream from
     * all {@link KamNode species replacement nodes}.
     *
     * @param kam {@link Kam}
     * @param inferf {@link EdgeFilter}
     * @param species {@link Set} of {@link Integer} species replacement node
     * ids
     */
    private static Pair<Map<Integer, TermParameter>, Map<Integer, TermParameter>> inferOrthologs(
            Kam kam, KAMStore kAMStore, SpeciesDialect dialect,
            EdgeFilter inferf, Set<KamNode> species,
            Map<KamNode, KamNode> ortho) {
        
        final List<org.openbel.framework.common.model.Namespace> spl = dialect
                .getSpeciesNamespaces();
        final Set<String> rlocs = constrainedHashSet(spl.size());
        for (final org.openbel.framework.common.model.Namespace n : spl) {
            rlocs.add(n.getResourceLocation());
        }
        
        Map<Integer, TermParameter> ntp = sizedHashMap(species.size());
        Map<Integer, TermParameter> etp = sizedHashMap(species.size());
        for (final KamNode snode : species) {
            if (snode != null) {
                // XXX term parameter looked up 2x; may impact perf/determinism
                // TODO redesign orthologousNodes / inferOrthologs
                TermParameter p = findParameter(kam, kAMStore, snode, rlocs);
    
                // recurse incoming connections from species node
                recurseConnections(kam, snode, p, inferf, REVERSE, ortho, ntp, etp);
                // recurse outgoing connections from species node
                recurseConnections(kam, snode, p, inferf, FORWARD, ortho, ntp, etp);
            }
        }
        
        return new Pair<Map<Integer, TermParameter>, Map<Integer, TermParameter>>(
                ntp, etp);
    }

    /**
     * Walks {@link OrthologizedKam#RELS certain relationship types} and infers
     * orthologous edges based on matching relationships.
     *
     * <p>
     * For instance if there are two {@code transcribedTo} edges from an
     * orthologized {@code geneAbundance} then we infer that the downstream
     * {@code rnaAbundance}s are also orthologous and collapse to the first
     * one.
     * </p>
     *
     * @param kam {@link Kam}
     * @param snode {@link KamNode} species node to walk from
     * @param param {@link TermParameter} for orthologous species node
     * @param direction {@link EdgeDirectionType} direction to walk
     * @param ortho {@link Map} of orthologous node to species node
     * @param ntp {@link Map} of node id to {@link TermParameter}
     * @param etp {@link Map} of edge id to {@link TermParameter}
     */
    private static void recurseConnections(Kam kam, KamNode snode,
            TermParameter param, EdgeFilter inferf,
            EdgeDirectionType direction, Map<KamNode, KamNode> ortho,
            Map<Integer, TermParameter> ntp, Map<Integer, TermParameter> etp) {
        // get adjacent edges that can be inferred
        final Set<KamEdge> out = kam.getAdjacentEdges(snode, direction, inferf);

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
                    ntp.put(opnode.getId(), param);
                    etp.put(e.getId(), param);
                    continue;
                }

                kam.collapseNode(opnode, node);
                
                // hang on to collapsed node
                ortho.put(opnode, node);
            } else {
                // handle all other edges by relationship type
                final RelationshipType rel = e.getRelationshipType();

                // lookup first seen relationship type
                KamNode node = rels.get(rel);

                // if not yet seen mark opposite node and edge as species collapse
                // target.  continue to next edge.
                if (node == null) {
                    rels.put(rel, opnode);
                    ntp.put(opnode.getId(), param);
                    etp.put(e.getId(), param);
                    continue;
                }

                // hang on to collapsed node
                ortho.put(opnode, node);
                kam.collapseNode(opnode, node);
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
            recurseConnections(kam, n, param, inferf, direction, ortho, ntp,
                    etp);
        }
    }
    
    /**
     * Matches {@link BelTerm terms} for a {@link KamNode node} against a
     * species-specific {@link Namespace}.
     *
     * @param node {@link KamNode} kam node to match
     * @param rlocs {@link Set} of {@link String resource location}, matches
     * against nodes to find orthologous targets
     * @return first species-matching {@link TermParameter term parameter} or
     * {@code null} if one could not be found
     * @throws InvalidArgument Thrown if {@code node} is null while retrieving
     * supporting terms
     * @throws KAMStoreException Thrown if the {@link Kam kam} could not be
     * determined while retrieving supporting terms or parameters
     */
    private static TermParameter findParameter(final Kam kam,
            final KAMStore kAMStore, final KamNode node, final Set<String> rlocs)
            throws KAMStoreException {

        // no resource locations to match against, no match
        if (noItems(rlocs)) {
            return null;
        }

        final List<BelTerm> terms = kAMStore.getSupportingTerms(node);
        for (final BelTerm term : terms) {
            final List<TermParameter> params = kAMStore.getTermParameters(
                    kam.getKamInfo(), term);
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
}
