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
