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
package org.openbel.framework.api;

import static org.openbel.framework.api.EdgeDirectionType.BOTH;
import static org.openbel.framework.common.BELUtilities.sizedHashSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;

/**
 * Implements a basic {@link PathFinder} implementation with a max depth
 * constraint.
 *
 * <p>
 * The max search depth constraint default is defined at
 * {@link BasicPathFinder#DEFAULT_MAX_SEARCH_DEPTH}.  To override this
 * constraints create with {@link BasicPathFinder#BasicPathFinder(int)}.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class BasicPathFinder implements PathFinder {
    /**
     * Defines the default max search depth constraint to a depth of {@value}.
     */
    public final static int DEFAULT_MAX_SEARCH_DEPTH = 4;

    /**
     * Holds the KAM network to path find over.
     */
    private final Kam kam;

    /**
     * Holds the max search depth to use when path finding.
     */
    private final int maxSearchDepth;

    /**
     * Constructs the path finder using the default max search depth defined
     * in {@link BasicPathFinder#DEFAULT_MAX_SEARCH_DEPTH}.
     *
     * @param kam {@link Kam}, the KAM network to path find over, which cannot
     * be null
     */
    public BasicPathFinder(final Kam kam) {
        this.kam = kam;
        this.maxSearchDepth = DEFAULT_MAX_SEARCH_DEPTH;
    }

    /**
     * Constructs the path finder using the max search depth constraint defined
     * in the value <tt>maxSearchDepth</tt>.  This value must be greater than
     * zero.
     *
     * @param kam {@link Kam}, the KAM network to path find over, which cannot
     * be null
     * @param maxSearchDepth <tt>int</tt> the max search depth constraint to
     * use when path finding, which must be greater than zero
     */
    public BasicPathFinder(final Kam kam, final int maxSearchDepth) {
        this.kam = kam;
        this.maxSearchDepth = maxSearchDepth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePath[] findPaths(KamNode source, KamNode target) {
        if (source == null) {
            throw new InvalidArgument("Source kam node cannot be null.");
        }

        if (target == null) {
            throw new InvalidArgument("Target kam node cannot be null.");
        }

        if (!kam.contains(source)) {
            throw new InvalidArgument("Source does not exist in KAM.");
        }

        if (!kam.contains(target)) {
            throw new InvalidArgument("Target does not exist in KAM.");
        }

        final Set<KamNode> targets = sizedHashSet(1);
        targets.add(target);

        List<SimplePath> pathsFound = runDepthFirstSearch(kam, source, targets);
        return pathsFound.toArray(new SimplePath[pathsFound.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePath[] findPaths(KamNode[] sources, KamNode[] targets) {
        if (sources == null || sources.length == 0) {
            throw new InvalidArgument(
                    "Source kam nodes cannot be null or empty.");
        }

        if (targets == null || targets.length == 0) {
            throw new InvalidArgument(
                    "Target kam nodes cannot be null or empty.");
        }

        for (final KamNode source : sources) {
            if (!kam.contains(source)) {
                throw new InvalidArgument("Source does not exist in KAM.");
            }
        }

        final Set<KamNode> targetSet = new HashSet<KamNode>(targets.length);
        for (final KamNode target : targets) {
            if (!kam.contains(target)) {
                throw new InvalidArgument("Target does not exist in KAM.");
            }

            targetSet.add(target);
        }

        final List<SimplePath> pathsFound = new ArrayList<SimplePath>();
        for (final KamNode source : sources) {
            if (!kam.contains(source)) {
                throw new InvalidArgument("Source does not exist in KAM.");
            }

            pathsFound.addAll(runDepthFirstSearch(kam, source, targetSet));
        }

        return pathsFound.toArray(new SimplePath[pathsFound.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePath[] interconnect(KamNode[] sources) {
        if (sources == null || sources.length < 2) {
            throw new InvalidArgument(
                    "Source kam nodes cannot be null and must contain at least two source nodes.");
        }

        // build out target set, check that each node is in the KAM
        final Set<KamNode> targetSet = new HashSet<KamNode>(sources.length);
        for (int i = 0; i < sources.length; i++) {
            final KamNode source = sources[i];

            if (!kam.contains(source)) {
                throw new InvalidArgument("Source does not exist in KAM.");
            }

            targetSet.add(source);
        }

        final List<SimplePath> pathsFound = new ArrayList<SimplePath>();
        for (final KamNode source : sources) {
            // remove source from target before search to prevent search the same
            // paths twice in the bidirectional search
            targetSet.remove(source);
            pathsFound.addAll(runDepthFirstSearch(kam, source, targetSet));
        }
        return pathsFound.toArray(new SimplePath[pathsFound.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePath[] scan(KamNode source) {
        if (source == null) {
            throw new InvalidArgument("Source kam node cannot be null.");
        }

        if (!kam.contains(source)) {
            throw new InvalidArgument("Source does not exist in KAM.");
        }

        final List<SimplePath> pathsFound = runDepthFirstScan(kam, source);
        return pathsFound.toArray(new SimplePath[pathsFound.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePath[] scan(KamNode[] sources) {
        if (sources == null || sources.length == 0) {
            throw new InvalidArgument(
                    "Source kam nodes cannot be null or empty.");
        }

        for (final KamNode source : sources) {
            if (!kam.contains(source)) {
                throw new InvalidArgument("Source does not exist in KAM.");
            }
        }

        final List<SimplePath> pathsFound = new ArrayList<SimplePath>();
        for (final KamNode source : sources) {
            pathsFound.addAll(runDepthFirstScan(kam, source));
        }
        return pathsFound.toArray(new SimplePath[pathsFound.size()]);
    }

    /**
     * Initializes and executes the depth-first search from {@link KamNode}
     * source to a {@link Set} of {@link KamNode} targets.
     *
     * @param kam {@link Kam}, the kam to traverse
     * @param source {@link KamNode}, the source kam node
     * @param targets {@link Set} of {@link KamNode}, the target kam nodes
     * @return the resulting paths from this depth-first search
     */
    private List<SimplePath> runDepthFirstSearch(final Kam kam,
            final KamNode source,
            final Set<KamNode> targets) {
        final SetStack<KamNode> nodeStack = new SetStack<KamNode>();
        nodeStack.add(source);

        final SetStack<KamEdge> edgeStack = new SetStack<KamEdge>();
        final List<SimplePath> pathResults = new ArrayList<SimplePath>();

        int initialDepth = 0;
        runDepthFirstSearch(kam, source, source, targets, initialDepth,
                nodeStack,
                edgeStack, pathResults);
        return pathResults;
    }

    /**
     * Initializes and executes a depth-first scan from {@link KamNode} source
     * given a max search depth ({@link BasicPathFinder#maxSearchDepth}).
     *
     * @param kam {@link Kam}, the kam to traverse
     * @param source {@link KamNode}, the source kam node
     * @return the resulting paths from this depth-first scan
     */
    private List<SimplePath> runDepthFirstScan(final Kam kam,
            final KamNode source) {
        final SetStack<KamEdge> edgeStack = new SetStack<KamEdge>();
        final SetStack<KamNode> nodeStack = new SetStack<KamNode>();
        final List<SimplePath> pathResults = new ArrayList<SimplePath>();

        // push on start node
        nodeStack.push(source);

        int initialDepth = 0;
        runDepthFirstScan(kam, source, source, initialDepth, nodeStack,
                edgeStack,
                pathResults);

        return pathResults;
    }

    /**
     * Runs a recursive depth-first search from a {@link KamNode} in search of
     * the <tt>target</tt> node.  When a {@link SimplePath} is found to the
     * <tt>target</tt> the {@link Stack} of {@link KamEdge} is collected and
     * the algorithm continues.<br/><br/>
     * <p>
     * This depth-first search exhaustively walks the entire {@link Kam}
     * and finds all paths from <tt>source</tt> to <tt>target</tt>.
     * </p>
     *
     * @param kam {@link Kam}, the kam to traverse
     * @param cnode {@link KamNode} the current node to evaluate
     * @param source {@link KamNode} the source to search from
     * @param targets {@link Set} of {@link KamNode}, the targets to search to
     * @param nodeStack {@link Stack} of {@link KamNode} that holds the nodes
     * on the current path from the <tt>source</tt>
     * @param edgeStack {@link Stack} of {@link KamEdge} that holds the edges
     * on the current path from the <tt>source</tt>
     * @param pathResults the resulting paths from source to targets
     */
    private void runDepthFirstSearch(final Kam kam,
            final KamNode cnode,
            final KamNode source,
            final Set<KamNode> targets,
            int depth,
            final SetStack<KamNode> nodeStack,
            final SetStack<KamEdge> edgeStack,
            final List<SimplePath> pathResults) {

        depth += 1;

        if (depth > maxSearchDepth) {
            return;
        }

        // get adjacent edges
        final Set<KamEdge> edges = kam.getAdjacentEdges(cnode, BOTH);

        for (final KamEdge edge : edges) {
            if (pushEdge(edge, nodeStack, edgeStack)) {
                final KamNode edgeOppositeNode = nodeStack.peek();

                // we have found a path from source to target
                if (targets.contains(edgeOppositeNode)) {
                    final SimplePath newPath =
                            new SimplePath(kam, source, nodeStack.peek(),
                                    edgeStack.toStack());
                    pathResults.add(newPath);
                } else {
                    runDepthFirstSearch(kam, edgeOppositeNode, source, targets,
                            depth,
                            nodeStack, edgeStack, pathResults);
                }

                nodeStack.pop();
                edgeStack.pop();
            }
        }
    }

    /**
     * Runs a recursive depth-first scan from a {@link KamNode} source until a
     * max search depth ({@link BasicPathFinder#maxSearchDepth}) is reached.
     * When the max search depth is reached a {@link SimplePath} is added,
     * containing the {@link Stack} of {@link KamEdge}, and the algorithm continues.
     *
     * @param kam {@link Kam}, the kam to traverse
     * @param cnode {@link KamNode}, the current node to evaluate
     * @param source {@link KamNode}, the node to search from
     * @param depth <tt>int</tt>, the current depth of this scan recursion
     * @param nodeStack {@link Stack} of {@link KamNode}, the nodes on the
     * current scan from the <tt>source</tt>
     * @param edgeStack {@link Stack} of {@link KamEdge}, the edges on the
     * current scan from the <tt>source</tt>
     * @param pathResults the resulting paths scanned from source
     */
    private void runDepthFirstScan(final Kam kam,
            final KamNode cnode,
            final KamNode source,
            int depth,
            final SetStack<KamNode> nodeStack,
            final SetStack<KamEdge> edgeStack,
            final List<SimplePath> pathResults) {

        depth += 1;

        final Set<KamEdge> edges = kam.getAdjacentEdges(cnode, BOTH);
        for (final KamEdge edge : edges) {
            if (pushEdge(edge, nodeStack, edgeStack)) {

                if (depth == maxSearchDepth) {
                    final SimplePath newPath =
                            new SimplePath(kam, source, nodeStack.peek(),
                                    edgeStack.toStack());
                    pathResults.add(newPath);
                } else {
                    // continue depth first scan
                    runDepthFirstScan(kam, nodeStack.peek(), source, depth,
                            nodeStack,
                            edgeStack, pathResults);
                }

                edgeStack.pop();
                nodeStack.pop();
            } else if (endOfBranch(edgeStack, edge, edges.size())) {
                final SimplePath newPath =
                        new SimplePath(kam, source, nodeStack.peek(),
                                edgeStack.toStack());
                pathResults.add(newPath);
            }
        }
    }

    /**
     * Determines whether the end of a branch was found.  This can indicate
     * that a path should be captures up to the leaf node.
     *
     * @param edgeStack {@link Stack} of {@link KamEdge} that holds the edges
     * on the current path
     * @param edge {@link KamEdge}, the edge to evaluate
     * @param edgeCount <tt>int</tt>, the number of adjacent edges to the
     * last visited {@link KamNode}
     * @return <tt>true</tt> if this edge marks the end of a branch,
     * <tt>false</tt> otherwise if it does not
     */
    private boolean endOfBranch(final SetStack<KamEdge> edgeStack,
            KamEdge edge,
            int edgeCount) {
        if (edgeStack.contains(edge) && edgeCount == 1) {
            return true;
        }

        return false;
    }

    /**
     * Determines whether the edge can be traversed.  This implementation will
     * check if the {@link KamNode} or {@link KamEdge} have been visited to
     * ensure there are no cycles in the resulting paths.
     * <p>
     * If the edge can be travered it will be placed on the edge {@link Stack},
     * and the edge's unvisited node will be placed on the node {@link Stack}.
     * </p>
     *
     * @param edge {@link KamEdge}, the kam edge to evaluate
     * @param nodeStack {@link Stack} of {@link KamNode}, the nodes on the
     * current scan from the <tt>source</tt>
     * @param edgeStack {@link Stack} of {@link KamEdge}, the edges on the
     * current scan from the <tt>source</tt>
     * @return <tt>true</tt> if the edge will be traversed, <tt>false</tt> if
     * not
     */
    private boolean pushEdge(final KamEdge edge,
            final SetStack<KamNode> nodeStack,
            final SetStack<KamEdge> edgeStack) {

        if (edgeStack.contains(edge)) {
            return false;
        }

        final KamNode currentNode = nodeStack.peek();
        final KamNode edgeOppositeNode =
                (edge.getSourceNode() == currentNode ? edge
                        .getTargetNode() : edge.getSourceNode());

        if (nodeStack.contains(edgeOppositeNode)) {
            return false;
        }

        nodeStack.push(edgeOppositeNode);
        edgeStack.push(edge);
        return true;
    }

    /**
     * Data Structure with both (subsets of) Set and Stack APIs that
     * allows for O(1) addition and retrieval as well as O(1) contains. This leads
     * to an approximate 20% performance gain in tested pathfind operations.
     * <br>
     * The unique {@link KamElement} ID is used as the discriminator in an attempt
     * to avoid hashCode collisions. In debugging the full Kam however, this did not
     * lead to significant performance improvements.
     *
     * @author Steve Ungerer
     */
    private final static class SetStack<T extends KamElement> {
        private Stack<T> elements = new Stack<T>();
        private HashSet<Integer> set = new HashSet<Integer>();

        public boolean contains(T o) {
            return set.contains(o.getId());
        }

        public void add(T obj) {
            elements.add(obj);
            set.add(obj.getId());
        }

        public void push(T obj) {
            elements.push(obj);
            set.add(obj.getId());
        }

        public T peek() {
            return elements.peek();
        }

        public T pop() {
            T obj = elements.pop();
            set.remove(obj.getId());
            return obj;
        }

        public Stack<T> toStack() {
            return elements;
        }
    }
}
