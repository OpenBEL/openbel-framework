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

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.api.BasicPathFinder;
import org.openbel.framework.api.Kam;
import org.openbel.framework.api.PathFinder;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.SimplePath;
import org.openbel.framework.ws.utils.Converter;
import org.openbel.framework.ws.utils.Converter.KamStoreObjectRef;
import org.openbel.framework.ws.utils.InvalidIdException;
import org.springframework.stereotype.Service;

@Service
public class PathFindServiceImpl implements PathFindService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SimplePath> findPaths(final Kam kam,
            final List<KamNode> sources,
            final List<KamNode> targets, final int maxSearchDepth)
            throws PathFindServiceException {
        final PathFinder pathFinder = new BasicPathFinder(kam);

        final Kam.KamNode[] sourceKamNodes =
                convert(kam, sources);

        final Kam.KamNode[] targetKamNodes =
                convert(kam, targets);

        final org.openbel.framework.api.SimplePath[] paths = pathFinder
                .findPaths(sourceKamNodes, targetKamNodes);

        final List<SimplePath> wsSimplePaths = new ArrayList<SimplePath>(
                paths.length);

        for (final org.openbel.framework.api.SimplePath path : paths) {
            wsSimplePaths.add(Converter.convert(path, kam.getKamInfo()));
        }

        return wsSimplePaths;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SimplePath> scan(final Kam kam,
            List<KamNode> sources,
            final int maxSearchDepth)
            throws PathFindServiceException {
        final PathFinder pathFinder = new BasicPathFinder(kam);

        final Kam.KamNode[] sourceKamNodes =
                convert(kam, sources);

        final org.openbel.framework.api.SimplePath[] paths = pathFinder
                .scan(sourceKamNodes);

        final List<SimplePath> wsSimplePaths = new ArrayList<SimplePath>(
                paths.length);

        for (final org.openbel.framework.api.SimplePath path : paths) {
            wsSimplePaths.add(Converter.convert(path, kam.getKamInfo()));
        }

        return wsSimplePaths;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SimplePath> interconnect(final Kam kam,
            List<KamNode> sources,
            final int maxSearchDepth)
            throws PathFindServiceException {
        final PathFinder pathFinder = new BasicPathFinder(kam, maxSearchDepth);

        final Kam.KamNode[] sourceKamNodes =
                convert(kam, sources);

        final org.openbel.framework.api.SimplePath[] paths = pathFinder
                .interconnect(sourceKamNodes);

        final List<SimplePath> wsSimplePaths = new ArrayList<SimplePath>(
                paths.length);

        for (final org.openbel.framework.api.SimplePath path : paths) {
            wsSimplePaths.add(Converter.convert(path, kam.getKamInfo()));
        }

        return wsSimplePaths;
    }

    private Kam.KamNode[] convert(
            final Kam kam, final List<KamNode> wsn)
            throws PathFindServiceException {
        final Kam.KamNode[] nodes =
                new Kam.KamNode[wsn
                        .size()];

        for (int i = 0; i < wsn.size(); i++) {
            final KamNode wsKamNode = wsn.get(i);

            KamStoreObjectRef kamElementRef;
            try {
                kamElementRef = Converter.decodeNode(wsKamNode);
            } catch (InvalidIdException e) {
                throw new PathFindServiceException(
                        "Error retreving KAM associated with node", e);
            }

            int kamNodeId = kamElementRef.getKamStoreObjectId();
            Kam.KamNode kamNode = kam.findNode(kamNodeId);

            if (kamNode == null) {
                throw new PathFindServiceException("KamNode with id '"
                        + wsKamNode.getId()
                        + "' does not exist in KAM '"
                        + kam.getKamInfo().getName() + "'.");
            }

            nodes[i] = kamNode;
        }

        return nodes;
    }
}
