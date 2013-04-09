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
