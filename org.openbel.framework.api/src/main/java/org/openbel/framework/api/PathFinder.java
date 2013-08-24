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

import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;

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
