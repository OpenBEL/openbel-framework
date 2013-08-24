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

import static java.lang.System.arraycopy;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.BELUtilities.nulls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;

/**
 * {@code AbstractPathFinder} is the base path finding class implementing the
 * checks necessary to support each interface method.
 */
public abstract class AbstractPathFinder implements PathFinder {

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePath[] findPaths(KamNode source, KamNode target) {
        if (source == null) {
            throw new InvalidArgument("source", source);
        }
        if (target == null) {
            throw new InvalidArgument("target", target);
        }

        Kam srcKAM = source.getKam();
        Kam tgtKAM = target.getKam();

        if (!sameKAMs(srcKAM, tgtKAM)) {
            throw new InvalidArgument("Source/target KAMs are not equal");
        }

        return findPaths(srcKAM, source, target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePath[] findPaths(KamNode[] sources, KamNode[] targets) {
        if (noItems(sources)) {
            throw new InvalidArgument("sources", sources);
        }
        if (noItems(targets)) {
            throw new InvalidArgument("targets", targets);
        }
        if (nulls((Object[]) sources)) {
            throw new InvalidArgument("Source nodes contains null elements");
        }
        if (nulls((Object[]) targets)) {
            throw new InvalidArgument("Target nodes contains null elements");
        }

        KamNode[] nodes = concat(sources, targets);
        Kam[] kams = kams(nodes);
        if (!sameKAMs(kams)) {
            throw new InvalidArgument("Source/target KAMs are not equal");
        }

        return findPaths(kams[0], sources, targets);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePath[] interconnect(KamNode[] nodes) {
        if (noItems(nodes)) {
            throw new InvalidArgument("nodes", nodes);
        }
        if (nulls((Object[]) nodes)) {
            throw new InvalidArgument("Nodes contains null elements");
        }

        Kam[] kams = kams(nodes);
        if (!sameKAMs(kams)) {
            throw new InvalidArgument("Node KAMs are not equal");
        }

        return interconnect(kams[0], nodes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePath[] scan(KamNode source) {
        if (source == null) {
            throw new InvalidArgument("source", source);
        }

        Kam kam = source.getKam();
        return scan(kam, source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimplePath[] scan(KamNode[] sources) {
        if (noItems(sources)) {
            throw new InvalidArgument("sources", sources);
        }
        if (nulls((Object[]) sources)) {
            throw new InvalidArgument("Source nodes contains null elements");
        }

        Kam[] kams = kams(sources);
        if (!sameKAMs(kams)) {
            throw new InvalidArgument("Source KAMs are not equal");
        }

        return scan(kams[0], sources);
    }

    /**
     * Concatenate arrays {@code nodes1} and {@code nodes2}.
     *
     * @param nodes1 KAM nodes
     * @param nodes2 KAM nodes
     * @return KamNode[]
     */
    KamNode[] concat(KamNode[] nodes1, KamNode[] nodes2) {
        KamNode[] ret = new KamNode[nodes1.length + nodes2.length];
        arraycopy(nodes1, 0, ret, 0, nodes1.length);
        arraycopy(nodes2, 0, ret, nodes1.length, nodes2.length);
        return ret;
    }

    /**
     * Returns {@code true} if the kams are all equal, {@code false} otherwise.
     *
     * @param kams KAMs
     * @return boolean
     */
    boolean sameKAMs(Kam... kams) {
        Set<Kam> set = new HashSet<Kam>(kams.length);
        for (int i = 0; i < kams.length; i++) {
            set.add(kams[i]);
        }
        if (set.size() == 1) {
            return true;
        }
        return false;
    }

    /**
     * Returns the KAMs associated with each KAM node.
     *
     * @param nodes KAM nodes
     * @return Kam array
     */
    Kam[] kams(KamNode... nodes) {
        List<Kam> kams = new ArrayList<Kam>(nodes.length);
        for (int i = 0; i < nodes.length; i++) {
            KamNode node = nodes[i];
            kams.add(node.getKam());
        }
        return kams.toArray(new Kam[0]);
    }

    /**
     * Returns all paths from source to target within the kam.
     *
     * @param kam KAM; contains the source and target nodes
     * @param src Non-null source KAM node
     * @param tgt Non-null target KAM node
     * @return {@link SimplePath SimplePath[]}
     */
    protected abstract SimplePath[] findPaths(
            Kam kam, KamNode src, KamNode tgt);

    /**
     * Returns all paths between all pairs of source and target nodes.
     * <p>
     * Each KAM node array is guaranteed to contain no null elements.
     * </p>
     *
     * @param kam KAM; contains the source and target nodes
     * @param srcs Non-null source KAM nodes
     * @param tgts Non-null target KAM nodes
     * @return {@link SimplePath SimplePath[]}
     */
    protected abstract SimplePath[] findPaths(
            Kam kam, KamNode[] srcs, KamNode[] tgts);

    /**
     * Returns all paths between all pairs of KAM nodes.
     * <p>
     * The KAM node array {@code nodes} is guaranteed to contain no null
     * elements.
     * </p>
     *
     * @param kam KAM; contains the nodes
     * @param nodes Non-null KAM nodes
     * @return {@link SimplePath SimplePath[]}
     */
    protected abstract SimplePath[] interconnect(
            Kam kam, KamNode[] nodes);

    /**
     * Returns all paths scanning out from the source node.
     *
     * @param kam KAM; contains the source node
     * @param src Non-null source KAM node
     * @return {@link SimplePath SimplePath[]}
     */
    protected abstract SimplePath[] scan(
            Kam kam, KamNode src);

    /**
     * Returns all paths scanning out from each source node.
     * <p>
     * The KAM node array {@code srcs} is guaranteed to contain no null
     * elements.
     * </p>
     *
     * @param kam KAM; contains the source node
     * @param srcs kam KAM; contains the source nodes
     * @return {@link SimplePath SimplePath[]}
     */
    protected abstract SimplePath[] scan(
            Kam kam, KamNode[] srcs);

}
