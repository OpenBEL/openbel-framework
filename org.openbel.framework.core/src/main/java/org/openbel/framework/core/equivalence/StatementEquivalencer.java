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
package org.openbel.framework.core.equivalence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable.TableProtoEdge;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNodeTable;

/**
 * StatementEquivalencer equivalences {@link ProtoNetwork} statements by
 * comparing statement signatures based on global term ids.
 *
 * This algorithm has O(n log(n)) complexity.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class StatementEquivalencer extends Equivalencer {

    /**
     * Constructs the statement equivalencer with a {@link ProtoNetwork} to
     * equivalence.
     *
     * @param pn {@link ProtoNetwork} the proto network to equivalence
     */
    public StatementEquivalencer(ProtoNetwork pn) {
        super(pn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int equivalence() {
        ProtoNodeTable pnt = network.getProtoNodeTable();
        ProtoEdgeTable pet = network.getProtoEdgeTable();

        List<TableProtoEdge> edges = pet.getProtoEdges();
        Map<Integer, Set<Integer>> edgeStmts = pet.getEdgeStatements();
        Map<Integer, Integer> eqn = pnt.getEquivalences();
        Map<Integer, Integer> eqe = pet.getEquivalences();

        return equivalenceInternal(edges, edgeStmts, eqn, eqe);
    }

    protected static int equivalenceInternal(List<TableProtoEdge> edges,
            Map<Integer, Set<Integer>> edgeStmts, Map<Integer, Integer> eqn,
            Map<Integer, Integer> eqe) {
        eqe.clear();
        int eqct = 0;

        final Map<TableProtoEdge, Integer> edgeCache =
                new HashMap<ProtoEdgeTable.TableProtoEdge, Integer>();
        final Map<Integer, Integer> revEq = new HashMap<Integer, Integer>();
        int eq = 0;
        for (int i = 0, n = edges.size(); i < n; i++) {
            final TableProtoEdge edge = edges.get(i);

            // translate into a proto node equivalent edge
            final Integer eqs = eqn.get(edge.getSource());
            final Integer eqt = eqn.get(edge.getTarget());
            final TableProtoEdge eqEdge =
                    new TableProtoEdge(eqs, edge.getRel(), eqt);

            // we have seen this proto edge so equivalence
            Integer cachedEdge = edgeCache.get(eqEdge);
            if (cachedEdge != null) {
                // reassign equivalence index to previously seen equivalent edge
                eqe.put(i, cachedEdge);

                // add the current edge's statements to the equivalenced one
                final Set<Integer> curEdgeStmts = edgeStmts.get(i);
                final Set<Integer> eqEdgeStmts =
                        edgeStmts.get(revEq.get(cachedEdge));
                eqEdgeStmts.addAll(curEdgeStmts);
                curEdgeStmts.addAll(eqEdgeStmts);

                eqct++;
                continue;
            }

            edgeCache.put(eqEdge, eq);
            eqe.put(i, eq);
            revEq.put(eq, i);

            eq++;
        }

        return eqct;
    }

}
