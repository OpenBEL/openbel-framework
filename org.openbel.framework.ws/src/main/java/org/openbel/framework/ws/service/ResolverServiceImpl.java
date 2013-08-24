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

import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.ws.utils.Converter.convert;

import java.text.ParseException;
import java.util.List;

import org.openbel.framework.api.Kam;
import org.openbel.framework.api.KamStore;
import org.openbel.framework.api.Resolver;
import org.openbel.framework.api.ResolverException;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.ws.model.Edge;
import org.openbel.framework.ws.model.KamEdge;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.Node;
import org.openbel.framework.ws.model.RelationshipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implements a resolver service that resolves nodes and edges to a KAM.
 *
 * <p>
 * The namespace values do not have to exist in a specific KAM, but they must
 * be discoverable within the current BELFramework instance.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
@Service
public class ResolverServiceImpl implements ResolverService {
    private final Resolver resolver;

    @Autowired
    private KamStore kamStore;

    public ResolverServiceImpl() {
        resolver = new Resolver();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> resolveNodes(Kam kam, List<Node> nodes)
            throws ResolverServiceException {
        final List<KamNode> resolvedKamNodes = sizedArrayList(nodes.size());
        for (final Node node : nodes) {
            if (node == null || node.getLabel() == null) {
                throw new InvalidArgument("Node is invalid.");
            }

            try {
                resolvedKamNodes.add(convert(kam.getKamInfo(),
                        resolver.resolve(kam, kamStore,
                                node.getLabel())));
            } catch (ParseException e) {
                throw new ResolverServiceException(
                        "Unable to resolve BEL expression '" + node.getLabel()
                                + "' to a KAM node");
            } catch (ResolverException e) {
                throw new ResolverServiceException(
                        "Unable to resolve BEL expression '" + node.getLabel()
                                + "' to a KAM node");
            }
        }

        return resolvedKamNodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamEdge> resolveEdges(Kam kam, List<Edge> edges)
            throws ResolverServiceException {
        final List<KamEdge> resolvedKamEdges = sizedArrayList(edges.size());
        for (final Edge edge : edges) {

            // If relationship type is UNKNOWN then we cannot find it in the
            // KAM.
            if (edge.getRelationship() == RelationshipType.UNKNOWN) {
                continue;
            }

            final Node subject = edge.getSource();
            final Node object = edge.getTarget();

            if (subject == null) {
                throw new InvalidArgument("Edge subject is invalid.");
            }

            if (object == null) {
                throw new InvalidArgument("Edge object is invalid.");
            }

            String subLbl = subject.getLabel();
            String objLbl = object.getLabel();
            final Kam.KamEdge re;
            org.openbel.framework.common.enums.RelationshipType rel;
            rel = convert(edge.getRelationship());

            try {
                re = resolver.resolve(kam, kamStore, subLbl, rel, objLbl);
                resolvedKamEdges.add(convert(kam.getKamInfo(), re));
            } catch (ParseException e) {
                throw new ResolverServiceException(
                        "Unable to resolve BEL expression '"
                                + getEdgeExpression(edge) + "' to a KAM edge");
            } catch (ResolverException e) {
                throw new ResolverServiceException(
                        "Unable to resolve BEL expression '"
                                + getEdgeExpression(edge) + "' to a KAM edge");
            }
        }

        return resolvedKamEdges;
    }

    /**
     * Build the BEL expression syntax for the {@link Edge}.
     *
     * @param edge {@link Edge}, the edge to build BEL expression from
     * @return {@link String}, the BEL expression for the <tt>edge</tt>
     */
    private String getEdgeExpression(final Edge edge) {
        return edge.getSource() + edge.getRelationship().value()
                + edge.getTarget();
    }
}
