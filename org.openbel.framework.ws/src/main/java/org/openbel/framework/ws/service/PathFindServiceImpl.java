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
package org.openbel.framework.ws.service;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.api.BasicPathFinder;
import org.openbel.framework.api.PathFinder;
import org.openbel.framework.core.kamstore.model.Kam;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.SimplePath;
import org.openbel.framework.ws.utils.Converter;
import org.openbel.framework.ws.utils.InvalidIdException;
import org.openbel.framework.ws.utils.Converter.KamStoreObjectRef;
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

        final org.openbel.framework.core.kamstore.model.Kam.KamNode[] sourceKamNodes =
                convert(kam, sources);

        final org.openbel.framework.core.kamstore.model.Kam.KamNode[] targetKamNodes =
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

        final org.openbel.framework.core.kamstore.model.Kam.KamNode[] sourceKamNodes =
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

        final org.openbel.framework.core.kamstore.model.Kam.KamNode[] sourceKamNodes =
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

    private org.openbel.framework.core.kamstore.model.Kam.KamNode[] convert(
            final Kam kam, final List<KamNode> wsn)
            throws PathFindServiceException {
        final org.openbel.framework.core.kamstore.model.Kam.KamNode[] nodes =
                new org.openbel.framework.core.kamstore.model.Kam.KamNode[wsn
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
            org.openbel.framework.core.kamstore.model.Kam.KamNode kamNode = kam
                    .findNode(kamNodeId);

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
