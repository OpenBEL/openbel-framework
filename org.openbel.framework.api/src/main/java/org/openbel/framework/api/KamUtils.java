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

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.api.Kam.KamEdge;
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
}
