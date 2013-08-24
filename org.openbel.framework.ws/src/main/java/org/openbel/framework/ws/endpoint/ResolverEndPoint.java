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
package org.openbel.framework.ws.endpoint;

import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.common.Strings.DIALECT_REQUEST_NO_DIALECT_FOR_HANDLE;
import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_KAM_FOR_HANDLE;

import java.util.List;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.KamCacheService;
import org.openbel.framework.api.KamDialect;
import org.openbel.framework.api.Resolver;
import org.openbel.framework.ws.core.MissingRequest;
import org.openbel.framework.ws.core.RequestException;
import org.openbel.framework.ws.model.DialectHandle;
import org.openbel.framework.ws.model.Edge;
import org.openbel.framework.ws.model.KamEdge;
import org.openbel.framework.ws.model.KamHandle;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.Node;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.model.RelationshipType;
import org.openbel.framework.ws.model.ResolveEdgesRequest;
import org.openbel.framework.ws.model.ResolveEdgesResponse;
import org.openbel.framework.ws.model.ResolveNodesRequest;
import org.openbel.framework.ws.model.ResolveNodesResponse;
import org.openbel.framework.ws.service.DialectCacheService;
import org.openbel.framework.ws.service.ResolverService;
import org.openbel.framework.ws.service.ResolverServiceException;
import org.openbel.framework.ws.utils.ObjectFactorySingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * ResolverEndPoint defines a web-service end point to expose the
 * {@link Resolver} API.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
@Endpoint
public class ResolverEndPoint extends WebServiceEndpoint {
    private static final String RESOLVE_EDGES_REQUEST = "ResolveEdgesRequest";
    private static final String RESOLVE_NODES_REQUEST = "ResolveNodesRequest";
    private static final ObjectFactory OBJECT_FACTORY = ObjectFactorySingleton
            .getInstance();

    @Autowired(required = true)
    private ResolverService resolverService;

    @Autowired(required = true)
    private KamCacheService kamCacheService;

    @Autowired(required = true)
    private DialectCacheService dialectCacheService;

    public ResolverEndPoint() {
        super();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = RESOLVE_NODES_REQUEST)
    @ResponsePayload
    public ResolveNodesResponse resolveKamNodes(
            @RequestPayload ResolveNodesRequest request)
            throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(RESOLVE_NODES_REQUEST);
        }

        final KamHandle handle = request.getHandle();
        if (handle == null) {
            throw new RequestException("KamHandle payload is missing");
        }

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());

        final org.openbel.framework.api.Kam kam = verifyKam(handle, dialect);

        final List<Node> nodes = request.getNodes();
        if (nodes.isEmpty()) {
            throw new RequestException("No node to resolve");
        }

        final ResolveNodesResponse response = OBJECT_FACTORY
                .createResolveNodesResponse();

        try {
            List<KamNode> kamNodes = resolverService.resolveNodes(kam, nodes);
            for (final KamNode kamNode : kamNodes) {
                response.getKamNodes().add(kamNode);
            }

            return response;
        } catch (ResolverServiceException e) {
            final String msg = "error resolving nodes";
            throw new RequestException(msg, e);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = RESOLVE_EDGES_REQUEST)
    @ResponsePayload
    public ResolveEdgesResponse resolveKamEdges(
            @RequestPayload ResolveEdgesRequest request)
            throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(RESOLVE_EDGES_REQUEST);
        }

        final KamHandle handle = request.getHandle();
        if (handle == null) {
            throw new RequestException("KamHandle payload is missing");
        }

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());

        final org.openbel.framework.api.Kam kam = verifyKam(handle, dialect);

        final List<Edge> edges = request.getEdges();
        if (edges.isEmpty()) {
            throw new RequestException("No edge to resolve");
        }

        // Sanity check the edges (see #83)
        for (final Edge e : edges) {
            Node src = e.getSource(), tgt = e.getTarget();
            if (src == null || tgt == null) {
                final String msg = "edge missing source and target nodes";
                throw new RequestException(msg);
            }
            RelationshipType rel = e.getRelationship();
            if (rel == null) {
                final String msg = "edge missing relationship";
                throw new RequestException(msg);
            }
            if (noLength(src.getLabel())) {
                final String msg = "edge source node label is missing";
                throw new RequestException(msg);
            }
            if (noLength(tgt.getLabel())) {
                final String msg = "edge target node label is missing";
                throw new RequestException(msg);
            }
        }

        final ResolveEdgesResponse response = OBJECT_FACTORY
                .createResolveEdgesResponse();

        try {
            List<KamEdge> kamEdges = resolverService.resolveEdges(kam, edges);
            for (final KamEdge kamEdge : kamEdges) {
                response.getKamEdges().add(kamEdge);
            }

            return response;
        } catch (ResolverServiceException e) {
            final String msg = "error resolving edges";
            throw new RequestException(msg, e);
        }
    }

    private Dialect getDialect(final DialectHandle dialectHandle)
            throws RequestException {
        if (dialectHandle == null) {
            return null;
        }
        Dialect dialect =
                dialectCacheService.getDialect(dialectHandle.getHandle());

        if (dialect == null) {
            final String fmt = DIALECT_REQUEST_NO_DIALECT_FOR_HANDLE;
            final String msg = format(fmt, dialectHandle.getHandle());
            throw new RequestException(msg);
        }

        return dialect;
    }

    private org.openbel.framework.api.Kam verifyKam(final KamHandle handle,
            Dialect dialect)
            throws RequestException {
        org.openbel.framework.api.Kam kam = null;
        kam = kamCacheService.getKam(handle.getHandle());

        if (kam == null) {
            final String fmt = KAM_REQUEST_NO_KAM_FOR_HANDLE;
            final String msg = format(fmt, handle.getHandle());
            throw new RequestException(msg);
        }

        return dialect == null
                ? kam
                : new KamDialect(kam, dialect);
    }
}
