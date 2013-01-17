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

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;

/**
 * Utility class for operating on Kams
 * 
 * @author julianjray
 *
 */
public final class KamUtils {

    private KamUtils() {
        // only static methods; no instantiation
    }

    /**
     * 
     * @param kam
     * @return
     * @throws InvalidArgument Thrown if {@code kam} is null
     */
    public static Kam newInstance(Kam kam) {
        if (kam == null) {
            throw new InvalidArgument("kam", kam);
        }
        return new KamImpl(kam.getKamInfo());
    }

    /**
     * 
     * @param kam
     * @param edgeFilter
     * @return
     * @throws InvalidArgument
     */
    public static Kam filter(Kam kam, EdgeFilter edgeFilter)
            throws InvalidArgument {

        Kam newKam = newInstance(kam);
        newKam.union(kam.getEdges(edgeFilter));
        return newKam;
    }

    /**
     * 
     * @param kam1
     * @param kam2
     * @return
     * @throws InvalidArgument
     */
    public static Kam intersection(Kam kam1, Kam kam2) throws InvalidArgument {

        if (!kam1.getKamInfo().equals(kam2.getKamInfo())) {
            throw new InvalidArgument(
                    "Kam1 and Kam2 must reference the same kam in the KamStore");
        }
        List<KamEdge> kamEdges = new ArrayList<KamEdge>();

        // Check for edges in Kam1 that are also in Kam2
        for (KamEdge kamEdge : kam1.getEdges()) {
            if (kam2.contains(kamEdge)) {
                kamEdges.add(kamEdge);
            }
        }

        // Create a new Kam with the difference set from both Kams
        Kam newKam = newInstance(kam1);
        newKam.union(kamEdges);
        return newKam;

    }

    /**
     * 
     * @param kam
     * @param kamEdges
     * @return
     * @throws InvalidArgument
     */
    public static Kam intersection(Kam kam, List<KamEdge> kamEdges)
            throws InvalidArgument {

        // Make sure that all the edges in the edge list are associated with the Kam
        for (KamEdge kamEdge : kamEdges) {
            if (!kamEdge.getKam().getKamInfo().equals(kam.getKamInfo())) {
                throw new InvalidArgument(
                        "Kamedges do not reference the same Kam.");
            }
        }

        // Check for edges in Kam1 that are also in the list
        List<KamEdge> intersectionKamEdges = new ArrayList<KamEdge>();
        for (KamEdge kamEdge : kamEdges) {
            if (kam.contains(kamEdge)) {
                intersectionKamEdges.add(kamEdge);
            }
        }

        return union(kam, intersectionKamEdges);

    }

    /**
     * 
     * @param kam
     * @param kamEdges
     * @return
     * @throws InvalidArgument
     */
    public static Kam difference(Kam kam, List<KamEdge> kamEdges)
            throws InvalidArgument {

        // Make sure that all the edges in the edge list are associated with the Kam
        for (KamEdge kamEdge : kamEdges) {
            if (!kamEdge.getKam().getKamInfo().equals(kam.getKamInfo())) {
                throw new InvalidArgument(
                        "Kamedges do not reference the same Kam.");
            }
        }

        // Create a new kam instance and add the edges
        Kam kam2 = newInstance(kam);
        kam2.union(kamEdges);

        // perform a kam to kam difference
        return difference(kam, kam2);
    }

    /**
     * 
     * @param kam1
     * @param kam2
     * @return
     * @throws InvalidArgument
     */
    public static Kam union(Kam kam1, Kam kam2) throws InvalidArgument {

        if (!kam1.getKamInfo().equals(kam2.getKamInfo())) {
            throw new InvalidArgument(
                    "Kam1 and Kam2 must reference the same kam in the KamStore");
        }
        Kam newKam = newInstance(kam1);
        newKam.union(kam1.getEdges());
        newKam.union(kam2.getEdges());

        return newKam;
    }

    /**
     * 
     * @param kam1
     * @param edges
     * @return
     * @throws InvalidArgument
     */
    public static Kam union(Kam kam1, List<KamEdge> kamEdges)
            throws InvalidArgument {

        Kam newKam = newInstance(kam1);
        newKam.union(kam1.getEdges());
        newKam.union(kamEdges);

        return newKam;
    }

    /**
     * 
     * @param kam1
     * @param kam2
     * @return
     * @throws InvalidArgument
     */
    public static Kam difference(Kam kam1, Kam kam2) throws InvalidArgument {

        if (!kam1.getKamInfo().equals(kam2.getKamInfo())) {
            throw new InvalidArgument(
                    "Kam1 and Kam2 must reference the same kam in the KamStore");
        }
        List<KamEdge> kamEdges = new ArrayList<KamEdge>();

        // First check for edges in Kam1 that are not in Kam2
        for (KamEdge kamEdge : kam1.getEdges()) {
            if (!kam2.contains(kamEdge)) {
                kamEdges.add(kamEdge);
            }
        }
        // Now check for edges in Kam2 that are not in Kam1
        for (KamEdge kamEdge : kam2.getEdges()) {
            if (!kam1.contains(kamEdge)) {
                kamEdges.add(kamEdge);
            }
        }
        // Create a new Kam with the difference set from both Kams
        Kam newKam = newInstance(kam1);
        newKam.union(kamEdges);
        return newKam;
    }
    
    /**
     * Copy the {@link Kam} by creating a new {@link KamImpl} instance with
     * new {@link KamNode}s and {@link KamEdge}s.
     *
     * @param kam {@link Kam}
     * @return the new instance {@link Kam}
     */
    public static Kam copy(final Kam kam) {
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
}
