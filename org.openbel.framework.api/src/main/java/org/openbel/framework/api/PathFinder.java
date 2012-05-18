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

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.kamstore.model.Kam;
import org.openbel.framework.core.kamstore.model.Kam.KamNode;

/**
 * PathFinder defines a utility to find paths within a {@link Kam}. The path
 * finder must implement:
 * <ul>
 * <li>Find all paths from source set to a target set</li>
 * <li>Scan outwards from a source set</li>
 * <li>Interconnect between nodes in a source set</li>
 * </ul>
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface PathFinder {

    /**
     * Finds all paths, if there are any, between the source and target
     * {@link KamNode}.
     * 
     * @param source {@link KamNode}, the source KAM node to search from, which
     * cannot be null
     * @param target {@link KamNode}, the target KAM node to search to, which
     * cannot be null
     * @return a populated {@link SimplePath KamPath[]}, or an empty array if no
     * paths were found
     * @throws InvalidArgument Thrown if <tt>source</tt> or <tt>target</tt> is
     * null, or if they do not exist within the {@link Kam} to traverse
     */
    public SimplePath[] findPaths(final KamNode source, final KamNode target);

    /**
     * Finds all paths, if there are any, between all pairs of {@link KamNode}
     * sources and targets.
     * 
     * @param sources {@link KamNode KamNode[]}, array of source KAM nodes to
     * search from, which cannot be null or empty
     * @param targets {@link KamNode KamNode[]}, array of target KAM nodes to
     * search to, which cannot be null or empty
     * @return a populated {@link SimplePath KamPath[]}, or an empty array if no
     * paths were found
     * @throws InvalidArgument Thrown if <tt>sources</tt> or <tt>targets</tt> is
     * null, empty, or if any {@link KamNode} does not exist within the
     * {@link Kam} to traverse
     */
    public SimplePath[] findPaths(final KamNode[] sources,
            final KamNode[] targets);

    /**
     * Finds all paths, if there are any, that interconnect any pair of
     * {@link KamNode nodes}.
     * 
     * @param nodes {@link KamNode KamNode[]}, array of KAM nodes to
     * interconnect, which cannot be null and must contain at least two
     * {@link KamNode KAM nodes}
     * @return a populated {@link SimplePath KamPath[]}, or an empty array if no
     * paths were found
     * @throws InvalidArgument Thrown if <tt>sources</tt> is null or does not
     * contain at least two {@link KamNode KAM nodes}, or if any {@link KamNode
     * KAM node} does not exist within the {@link Kam} to traverse
     */
    public SimplePath[] interconnect(final KamNode[] sources);

    /**
     * Finds all paths, if there are any, that scan out from the source
     * {@link KamNode}.
     * <p>
     * The implementation of the scan algorithm should impose path constraints
     * in order to prevent infinite graph traversal.
     * </p>
     * 
     * @param source {@link KamNode}, the source KAM node to scan out from,
     * which cannot be null
     * @return a populated {@link SimplePath KamPath[]}, or an empty array if no
     * paths were found
     * @throws InvalidArgument Thrown if <tt>source</tt> is null, or if
     * <tt>source</tt> does not exist within the {@link Kam} to traverse
     */
    public SimplePath[] scan(final KamNode source);

    /**
     * Finds all paths, if there are any, that scan out from the {@link KamNode}
     * sources.
     * <p>
     * The implementation of the scan algorithm should impose constraints in
     * order to prevent infinite graph traversal.
     * </p>
     * 
     * @param sources {@link KamNode KamNode[]}, array of source KAM nodes to
     * scan out from, which cannot be null or empty
     * @return a populated {@link SimplePath KamPath[]}, or an empty array if no
     * paths were found
     * @throws InvalidArgument Thrown if <tt>sources</tt> is null, empty, or if
     * any {@link KamNode} does not exist within the {@link Kam} to traverse
     */
    public SimplePath[] scan(final KamNode[] sources);
}
