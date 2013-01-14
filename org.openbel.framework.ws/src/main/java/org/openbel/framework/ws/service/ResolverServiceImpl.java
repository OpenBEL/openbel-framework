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

import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.ws.utils.Converter.convert;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openbel.framework.api.Equivalencer;
import org.openbel.framework.api.EquivalencerException;
import org.openbel.framework.api.KAMStore;
import org.openbel.framework.api.Kam;
import org.openbel.framework.api.Resolver;
import org.openbel.framework.api.ResolverException;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.ws.model.Edge;
import org.openbel.framework.ws.model.KamEdge;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.Namespace;
import org.openbel.framework.ws.model.NamespaceDescriptor;
import org.openbel.framework.ws.model.Node;
import org.openbel.framework.ws.model.RelationshipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implements a resolver service that resolves nodes and edges to a KAM.
 * 
 * <p>
 * The namespace values do not have to exist in a specific KAM, but they must be
 * discoverable within the current BELFramework instance.
 * </p>
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
@Service
public class ResolverServiceImpl implements ResolverService {
    private final Resolver resolver;

    @Autowired
    private KAMStore kAMStore;
    @Autowired
    private EquivalencerService eqsvc;
    @Autowired(required = true)
    private NamespaceResourceService nsrsvc;

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
        final Equivalencer eq = getEquivalencer();

        Map<String, String> nsmap = namespaces(kam);

        for (final Node node : nodes) {
            if (node == null || node.getLabel() == null) {
                throw new InvalidArgument("Node is invalid.");
            }

            try {
                resolvedKamNodes.add(convert(kam.getKamInfo(), resolver
                        .resolve(kam, kAMStore, node.getLabel(), nsmap, eq)));
            } catch (ParseException e) {
                throw new ResolverServiceException(
                        "Unable to resolve BEL expression '" + node.getLabel()
                                + "' to a KAM node");
            } catch (ResolverException e) {
                throw new ResolverServiceException(
                        "Unable to resolve BEL expression '" + node.getLabel()
                                + "' to a KAM node");
            } catch (EquivalencerException e) {
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
        final Equivalencer eq = getEquivalencer();

        Map<String, String> nsmap = namespaces(kam);

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
                re = resolver.resolve(kam, kAMStore, subLbl, rel, objLbl,
                        nsmap, eq);
                resolvedKamEdges.add(convert(kam.getKamInfo(), re));
            } catch (ParseException e) {
                throw new ResolverServiceException(
                        "Unable to resolve BEL expression '"
                                + getEdgeExpression(edge) + "' to a KAM edge");
            } catch (ResolverException e) {
                throw new ResolverServiceException(
                        "Unable to resolve BEL expression '"
                                + getEdgeExpression(edge) + "' to a KAM edge");
            } catch (EquivalencerException e) {
                throw new ResolverServiceException(
                        "Unable to resolve BEL expression '"
                                + getEdgeExpression(edge) + "' to a KAM edge");
            }
        }

        return resolvedKamEdges;
    }

    /**
     * Assemble all known namespaces from the resource index and {@link Kam}.
     * 
     * @param kam
     *            {@link Kam}
     * @return {@link Map} of namespace {@link String prefix} to {@link String
     *         resource location}
     */
    private Map<String, String> namespaces(Kam kam) {
        Map<String, String> nsmap = new HashMap<String, String>();

        // find namespaces in resource index
        List<NamespaceDescriptor> ns = nsrsvc.getAllNamespaceDescriptors();
        for (NamespaceDescriptor n : ns) {
            Namespace the_ns = n.getNamespace();
            nsmap.put(the_ns.getPrefix(), the_ns.getResourceLocation());
        }

        // then find namespaces identified by the KAM
        List<org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace> nsl =
                kAMStore.getNamespaces(kam);
        for (org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace kam_ns : nsl) {
            nsmap.put(kam_ns.getPrefix(), kam_ns.getResourceLocation());
        }
        return nsmap;
    }

    /**
     * Build the BEL expression syntax for the {@link Edge}.
     * 
     * @param edge
     *            {@link Edge}, the edge to build BEL expression from
     * @return {@link String}, the BEL expression for the <tt>edge</tt>
     */
    private String getEdgeExpression(final Edge edge) {
        return edge.getSource() + edge.getRelationship().value()
                + edge.getTarget();
    }

    private Equivalencer getEquivalencer() {
        return ((EquivalencerServiceImpl) eqsvc).leak();
    }
}
