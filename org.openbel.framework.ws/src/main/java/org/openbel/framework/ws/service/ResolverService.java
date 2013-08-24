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
package org.openbel.framework.ws.service;

import java.util.List;

import org.openbel.framework.api.Kam;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.ws.model.Edge;
import org.openbel.framework.ws.model.KamEdge;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.Node;

/**
 * Defines a resolver service that resolves nodes and edges to a KAM.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface ResolverService {

    /**
     * Resolves a list of {@link Node} objects to a list of {@link KamNode}
     * objects if they exist in the KAM pointed to by <tt>handle</tt>.
     *
     * @param kam {@link Kam}, the KAM to find {@link KamNode} objects within
     * @param nodes list of {@link Node}, the nodes to resolve to the KAM
     * @return list of {@link KamNode} that were resolved to the KAM, and
     * a <tt>null</tt> array index if it does not resolve
     * @throws InvalidArgument Thrown if a node in <tt>nodes</tt> is invalid
     * @throws ResolverServiceException Thrown if an error occurred resolving the
     * <tt>belTerm</tt> to a {@link KamNode} in the KAM store
     */
    public List<KamNode> resolveNodes(Kam kam, List<Node> nodes)
            throws ResolverServiceException;

    /**
     * Resolves a list of {@link Edge} objects to a list of {@link KamEdge}
     * objects if they exist in the KAM pointed to by <tt>handle</tt>.
     *
     * @param kam {@link Kam}, the KAM to find {@link KamEdge} objects within
     * @param edges list of {@link Edge}, the edges to resolve to the KAM
     * @return list of {@link KamEdge} that were resolved to the KAM, and
     * a <tt>null</tt> array index if it does not resolve
     * @throws InvalidArgument Thrown if an edge in <tt>edges</tt> contains
     * invalid subject or object node
     * @throws ResolverServiceException Thrown if an error occurs while
     * resolving to KAM edges
     */
    public List<KamEdge> resolveEdges(final Kam kam,
            final List<Edge> edges) throws ResolverServiceException;
}
