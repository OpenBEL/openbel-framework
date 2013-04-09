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
package org.openbel.framework.ws.endpoint;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.Strings.DIALECT_REQUEST_NO_DIALECT_FOR_HANDLE;
import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_HANDLE;
import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_KAM_FOR_HANDLE;
import static org.openbel.framework.ws.utils.Converter.convert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.Equivalencer;
import org.openbel.framework.api.EquivalencerException;
import org.openbel.framework.api.KamCacheService;
import org.openbel.framework.api.KamDialect;
import org.openbel.framework.api.KAMStore;
import org.openbel.framework.api.KAMStoreException;
import org.openbel.framework.api.internal.KAMCatalogDao;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.ws.core.MissingRequest;
import org.openbel.framework.ws.core.RequestException;
import org.openbel.framework.ws.model.DialectHandle;
import org.openbel.framework.ws.model.EdgeDirectionType;
import org.openbel.framework.ws.model.EdgeFilter;
import org.openbel.framework.ws.model.FindKamEdgesRequest;
import org.openbel.framework.ws.model.FindKamEdgesResponse;
import org.openbel.framework.ws.model.FindKamNodesByIdsRequest;
import org.openbel.framework.ws.model.FindKamNodesByIdsResponse;
import org.openbel.framework.ws.model.FindKamNodesByLabelsRequest;
import org.openbel.framework.ws.model.FindKamNodesByLabelsResponse;
import org.openbel.framework.ws.model.FindKamNodesByNamespaceValuesRequest;
import org.openbel.framework.ws.model.FindKamNodesByNamespaceValuesResponse;
import org.openbel.framework.ws.model.FindKamNodesByPatternsRequest;
import org.openbel.framework.ws.model.FindKamNodesByPatternsResponse;
import org.openbel.framework.ws.model.GetAdjacentKamEdgesRequest;
import org.openbel.framework.ws.model.GetAdjacentKamEdgesResponse;
import org.openbel.framework.ws.model.GetAdjacentKamNodesRequest;
import org.openbel.framework.ws.model.GetAdjacentKamNodesResponse;
import org.openbel.framework.ws.model.GetKamRequest;
import org.openbel.framework.ws.model.GetKamResponse;
import org.openbel.framework.ws.model.KamEdge;
import org.openbel.framework.ws.model.KamHandle;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.NamespaceValue;
import org.openbel.framework.ws.model.NodeFilter;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.service.DialectCacheService;
import org.openbel.framework.ws.utils.Converter;
import org.openbel.framework.ws.utils.ObjectFactorySingleton;
import org.openbel.framework.ws.utils.Converter.KamStoreObjectRef;
import org.openbel.framework.ws.utils.InvalidIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * TODO Provide documentation, fix NPEs (the end point methods assume all
 * elements of a request are non-null and this is certainly not true)
 */
@Endpoint
public class KamEndPoint extends WebServiceEndpoint {
    private static final String GET_KAM_REQUEST = "GetKamRequest";
    private static final String FIND_KAM_NODES_BY_IDS_REQUEST =
            "FindKamNodesByIdsRequest";
    private static final String FIND_KAM_NODES_BY_LABELS_REQUEST =
            "FindKamNodesByLabelsRequest";
    private static final String FIND_KAM_NODES_BY_PATTERNS_REQUEST =
            "FindKamNodesByPatternsRequest";
    private static final String FIND_KAM_EDGES_REQUEST = "FindKamEdgesRequest";
    private static final String GET_ADJACENT_KAM_NODES_REQUEST =
            "GetAdjacentKamNodesRequest";
    private static final String GET_ADJACENT_KAM_EDGES_REQUEST =
            "GetAdjacentKamEdgesRequest";
    private static final String FIND_KAM_NODES_BY_NAMESPACE_VALUES_REQUEST =
            "FindKamNodesByNamespaceValuesRequest";
    private static final ObjectFactory OBJECT_FACTORY = ObjectFactorySingleton
            .getInstance();

    @Autowired
    private KAMCatalogDao kamCatalogDao;
    @Autowired
    private KamCacheService kamCacheService;
    @Autowired
    private DialectCacheService dialectCacheService;
    @Autowired
    private KAMStore kAMStore;

    private Equivalencer equivalencer = new Equivalencer();

    public KamEndPoint() {
        super();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_KAM_REQUEST)
    @ResponsePayload
    public GetKamResponse getKam(@RequestPayload GetKamRequest request)
            throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(GET_KAM_REQUEST);
        }

        // Make sure a Kam was specified in the request
        KamHandle kamHandle = request.getHandle();
        if (null == kamHandle) {
            throw new RequestException("KamHandle payload is missing");
        }

        // Get the real Kam from the KamCache
        final org.openbel.framework.api.Kam objKam = getKam(
                kamHandle, null);

        GetKamResponse response = OBJECT_FACTORY.createGetKamResponse();
        response.setKam(convert(objKam.getKamInfo()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = FIND_KAM_NODES_BY_IDS_REQUEST)
    @ResponsePayload
    public
            FindKamNodesByIdsResponse findKamNodesByIds(
                    @RequestPayload FindKamNodesByIdsRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(FIND_KAM_NODES_BY_IDS_REQUEST);
        }

        KamHandle kamHandle = request.getHandle();
        if (null == kamHandle) {
            throw new RequestException("Handle is missing");
        }

        List<String> ids = request.getIds();
        if (CollectionUtils.isEmpty(ids)) {
            throw new RequestException("IDs are missing");
        }

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());

        final org.openbel.framework.api.Kam objKam = getKam(
                kamHandle, dialect);

        final org.openbel.framework.api.NodeFilter filter =
                convertNodeFilterInRequest(
                        request.getFilter(), objKam);

        List<KamNode> kamNodes = new ArrayList<KamNode>();
        for (String id : ids) {
            KamStoreObjectRef kamElementRef;
            try {
                kamElementRef = KamStoreObjectRef.decode(id);
            } catch (InvalidIdException e) {
                throw new RequestException("Error with KAM node ids", e);
            }

            Integer elemid = kamElementRef.getKamStoreObjectId();
            org.openbel.framework.api.Kam.KamNode node;
            node = objKam.findNode(elemid, filter);

            if (node == null) {
                kamNodes.add(null);
            } else {
                final KamNode kamNode = convert(objKam.getKamInfo(), node);
                kamNodes.add(kamNode);
            }
        }

        FindKamNodesByIdsResponse response = OBJECT_FACTORY
                .createFindKamNodesByIdsResponse();
        response.getKamNodes().addAll(kamNodes);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = FIND_KAM_NODES_BY_LABELS_REQUEST)
    @ResponsePayload
    public
            FindKamNodesByLabelsResponse findKamNodesByLabels(
                    @RequestPayload FindKamNodesByLabelsRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(FIND_KAM_NODES_BY_LABELS_REQUEST);
        }

        KamHandle kamHandle = request.getHandle();
        if (null == kamHandle) {
            throw new RequestException("Handle is missing");
        }

        List<String> labels = request.getLabels();
        if (CollectionUtils.isEmpty(labels)) {
            throw new RequestException("Labels are missing");
        }

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());

        final org.openbel.framework.api.Kam objKam = getKam(
                kamHandle, dialect);

        final org.openbel.framework.api.NodeFilter filter =
                convertNodeFilterInRequest(
                        request.getFilter(), objKam);

        List<KamNode> kamNodes = new ArrayList<KamNode>();
        for (String label : labels) {
            org.openbel.framework.api.Kam.KamNode ksKamNode =
                    objKam
                            .findNode(label, filter);
            if (ksKamNode == null) {
                // add null for array symmetry
                kamNodes.add(null);
            } else {
                final KamNode kamNode = convert(objKam.getKamInfo(), ksKamNode);
                kamNodes.add(kamNode);
            }
        }

        FindKamNodesByLabelsResponse response =
                OBJECT_FACTORY.createFindKamNodesByLabelsResponse();
        response.getKamNodes().addAll(kamNodes);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = FIND_KAM_NODES_BY_PATTERNS_REQUEST)
    @ResponsePayload
    public
            FindKamNodesByPatternsResponse findKamNodesByPatterns(
                    @RequestPayload FindKamNodesByPatternsRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(FIND_KAM_NODES_BY_PATTERNS_REQUEST);
        }

        final KamHandle kamHandle = request.getHandle();
        if (null == kamHandle) {
            throw new RequestException("Handle is missing");
        }

        final List<String> patterns = request.getPatterns();
        if (CollectionUtils.isEmpty(patterns)) {
            throw new RequestException(
                    "Regular expression patterns are missing");
        }

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());

        final org.openbel.framework.api.Kam objKam = getKam(
                kamHandle, dialect);
        final org.openbel.framework.api.NodeFilter filter =
                convertNodeFilterInRequest(
                        request.getFilter(), objKam);

        List<KamNode> kamNodes = new ArrayList<KamNode>();
        for (String pattern : patterns) {
            Pattern javaPattern;
            try {
                javaPattern = compile(pattern);
            } catch (PatternSyntaxException e) {
                throw new RequestException("Could not compile pattern: "
                        + pattern, e);
            }

            final Set<org.openbel.framework.api.Kam.KamNode> nodes =
                    objKam
                            .findNode(javaPattern, filter);
            for (org.openbel.framework.api.Kam.KamNode node : nodes) {
                final KamNode kamNode = convert(objKam.getKamInfo(), node);
                kamNodes.add(kamNode);
            }
        }

        final FindKamNodesByPatternsResponse response =
                OBJECT_FACTORY.createFindKamNodesByPatternsResponse();
        response.getKamNodes().addAll(kamNodes);
        return response;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = FIND_KAM_EDGES_REQUEST)
    public FindKamEdgesResponse findKamEdges(
            @RequestPayload FindKamEdgesRequest request)
            throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(FIND_KAM_EDGES_REQUEST);
        }

        // Make sure a KAM was specified in the request
        KamHandle hndl = request.getHandle();
        if (hndl == null || hndl.getHandle() == null) {
            throw new RequestException(KAM_REQUEST_NO_HANDLE);
        }

        EdgeFilter filter = request.getFilter();
        if (filter == null) {
            throw new RequestException("Filter is missing.");
        }

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());

        final org.openbel.framework.api.Kam kam = getKam(hndl,
                dialect);

        return findEdges(filter, kam);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_ADJACENT_KAM_NODES_REQUEST)
    @ResponsePayload
    public
            GetAdjacentKamNodesResponse getAdjacentNodes(
                    @RequestPayload GetAdjacentKamNodesRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(GET_ADJACENT_KAM_NODES_REQUEST);
        }

        // Make sure a KamNode was specified
        KamNode kamNode = request.getKamNode();
        if (null == kamNode) {
            throw new RequestException("KamNode payload is missing");
        }

        // See if we got a DirectionType to use
        org.openbel.framework.api.EdgeDirectionType direction =
                null;
        EdgeDirectionType edgeDirectionType = request.getDirection();
        if (null != edgeDirectionType) {
            direction = convert(edgeDirectionType);
        }

        // Get the real Kam from the KamCache
        KamStoreObjectRef kamElementRef;
        try {
            kamElementRef = Converter.decodeNode(kamNode);
        } catch (InvalidIdException e) {
            throw new RequestException("Error processing KAM node", e);
        }

        org.openbel.framework.api.Kam objKam;
        final KamInfo kamInfo = getKamInfo(kamElementRef,
                "Error processing KAM node");
        objKam = kamCacheService.getKam(kamInfo.getName());

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());
        if (dialect != null) {
            objKam = new KamDialect(objKam, dialect);
        }

        if (objKam == null) {
            throw new RequestException("Error processing KAM node",
                    new InvalidIdException(kamElementRef.getEncodedString()));
        }

        final org.openbel.framework.api.NodeFilter nodes =
                convertNodeFilterInRequest(
                        request.getNodeFilter(), objKam);

        final org.openbel.framework.api.EdgeFilter edges =
                convertEdgeFilterInRequest(
                        request.getEdgeFilter(), objKam);

        // Get the real KamNode from the Kam
        Integer elemid = kamElementRef.getKamStoreObjectId();
        org.openbel.framework.api.Kam.KamNode objKamNode;
        objKamNode = objKam.findNode(elemid);

        // Process the request
        GetAdjacentKamNodesResponse response =
                OBJECT_FACTORY.createGetAdjacentKamNodesResponse();

        // Get the adjacent nodes
        Set<org.openbel.framework.api.Kam.KamNode> adjnodes;
        if (direction == null) {
            adjnodes = objKam.getAdjacentNodes(objKamNode, edges, nodes);
        } else {
            adjnodes = objKam.getAdjacentNodes(objKamNode, direction, edges,
                    nodes);
        }
        for (org.openbel.framework.api.Kam.KamNode node : adjnodes) {
            KamNode kn = convert(kamInfo, node);
            response.getKamNodes().add(kn);
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_ADJACENT_KAM_EDGES_REQUEST)
    @ResponsePayload
    public
            GetAdjacentKamEdgesResponse getAdjacentEdges(
                    @RequestPayload GetAdjacentKamEdgesRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(GET_ADJACENT_KAM_EDGES_REQUEST);
        }

        // Make sure a KamNode was specified
        KamNode kamNode = request.getKamNode();
        if (kamNode == null) {
            throw new RequestException("KamNode payload is missing");
        }

        if (kamNode.getFunction() == null) {
            final String msg = "KAM node is missing function";
            throw new RequestException(msg);
        }
        if (noLength(kamNode.getId())) {
            final String msg = "KAM node is missing id";
            throw new RequestException(msg);
        }
        if (noLength(kamNode.getLabel())) {
            final String msg = "KAM node is missing label";
            throw new RequestException(msg);
        }

        // See if we got a DirectionType to use
        org.openbel.framework.api.EdgeDirectionType direction =
                null;
        EdgeDirectionType edgeDirectionType = request.getDirection();
        if (edgeDirectionType != null) {
            direction = convert(edgeDirectionType);
        }

        // Get the real Kam from the KamCache
        KamStoreObjectRef kamElementRef;
        try {
            kamElementRef = Converter.decodeNode(kamNode);
        } catch (InvalidIdException e) {
            throw new RequestException("Error processing KAM node", e);
        }

        final KamInfo kamInfo = getKamInfo(kamElementRef,
                "Error processing KAM node");
        org.openbel.framework.api.Kam objKam;
        objKam = kamCacheService.getKam(kamInfo.getName());
        if (objKam == null) {
            throw new RequestException("Error processing KAM node",
                    new InvalidIdException(kamElementRef.getEncodedString()));
        }
        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());
        if (dialect != null) {
            objKam = new KamDialect(objKam, dialect);
        }

        final org.openbel.framework.api.EdgeFilter edges =
                convertEdgeFilterInRequest(
                        request.getFilter(), objKam);

        // Get the real KamNode from the Kam
        org.openbel.framework.api.Kam.KamNode objKamNode =
                objKam
                        .findNode(kamElementRef.getKamStoreObjectId());

        GetAdjacentKamEdgesResponse response =
                OBJECT_FACTORY.createGetAdjacentKamEdgesResponse();

        // Get the adjacent edges
        Set<org.openbel.framework.api.Kam.KamEdge> adjedges;
        if (direction == null) {
            adjedges = objKam.getAdjacentEdges(objKamNode, edges);
        } else {
            adjedges = objKam.getAdjacentEdges(objKamNode, direction, edges);
        }

        for (org.openbel.framework.api.Kam.KamEdge edge : adjedges) {
            KamEdge ke = convert(kamInfo, edge);
            response.getKamEdges().add(ke);
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = FIND_KAM_NODES_BY_NAMESPACE_VALUES_REQUEST)
    @ResponsePayload
    public
            FindKamNodesByNamespaceValuesResponse
            findKamNodesByNamespaceValues(
                    @RequestPayload FindKamNodesByNamespaceValuesRequest request)
                    throws Exception {
        // validate request
        if (request == null) {
            throw new MissingRequest(FIND_KAM_NODES_BY_NAMESPACE_VALUES_REQUEST);
        }

        final KamHandle kamHandle = request.getHandle();
        if (null == kamHandle) {
            throw new RequestException("Handle is missing");
        }

        if (request.getNamespaceValues() == null
                || request.getNamespaceValues().isEmpty()) {
            throw new RequestException("At least 1 NamespaceValue is required");
        }

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());
        final org.openbel.framework.api.Kam objKam = getKam(
                kamHandle, dialect);

        List<org.openbel.framework.api.Kam.KamNode> nodes = findKamNodesByNamespacevalues(
                request.getNamespaceValues(), request.getFilter(), objKam,
                kAMStore, equivalencer);

        KamInfo kamInfo = objKam.getKamInfo();
        FindKamNodesByNamespaceValuesResponse response =
                OBJECT_FACTORY.createFindKamNodesByNamespaceValuesResponse();
        for (org.openbel.framework.api.Kam.KamNode node : nodes) {
            response.getKamNodes().add(convert(kamInfo, node));
        }

        return response;
    }

    static List<org.openbel.framework.api.Kam.KamNode> findKamNodesByNamespacevalues(
            Collection<NamespaceValue> nvs, NodeFilter nodeFilter, org.openbel.framework.api.Kam kam,
            KAMStore kAMStore, Equivalencer equivalencer)
            throws KAMStoreException, EquivalencerException {
        // get uuids for namespaceValues
        Set<SkinnyUUID> uuids = new LinkedHashSet<SkinnyUUID>();
        // resource location : values
        Map<String, Set<String>> noEquivs = new HashMap<String, Set<String>>();
        for (NamespaceValue nv : nvs) {
            SkinnyUUID uuid = nv.getEquivalence() != null ? new SkinnyUUID(nv
                    .getEquivalence().getMsb(), nv.getEquivalence().getLsb())
                    : equivalencer
                            .getUUID(Converter.convert(nv.getNamespace()),
                                    nv.getValue());
            if (uuid != null) {
                uuids.add(uuid);
            } else {
                String rl = nv.getNamespace().getResourceLocation();
                Set<String> vals = noEquivs.get(rl);
                if (vals == null) {
                    vals = new HashSet<String>();
                    noEquivs.put(rl, vals);
                }
                vals.add(nv.getValue());
            }
        }

        List<org.openbel.framework.api.Kam.KamNode> nodes = new ArrayList<org.openbel.framework.api.Kam.KamNode>();
        // find kam nodes by uuids
        if (!uuids.isEmpty()) {
            for (SkinnyUUID uuid : uuids) {
                nodes.addAll(kAMStore.getKamNodes(kam, uuid));
            }
        }

        // find kam nodes that had no equivs
        if (!noEquivs.isEmpty()) {
            for (Map.Entry<String, Set<String>> entry : noEquivs.entrySet()) {
                org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace kamNs =
                        kAMStore.getNamespace(kam, entry.getKey());
                if (kamNs == null) {
                    // ns not present in kam
                    continue;
                }
                for (String v : entry.getValue()) {
                    nodes.addAll(kAMStore.getKamNodes(kam, kamNs, v));
                }
            }
        }

        // apply nodeFilter if specified
        final org.openbel.framework.api.NodeFilter filter =
                convertNodeFilterInRequest(
                        nodeFilter, kam);
        if (filter != null) {
            Iterator<org.openbel.framework.api.Kam.KamNode> it = nodes.iterator();
            while (it.hasNext()) {
                if (!filter.accept(it.next())) {
                    it.remove();
                }
            }
        }

        return nodes;
    }

    private FindKamEdgesResponse findEdges(EdgeFilter fltr,
            org.openbel.framework.api.Kam kam) {
        FindKamEdgesResponse ret = new FindKamEdgesResponse();

        org.openbel.framework.api.EdgeFilter fltr2;
        fltr2 = convert(kam, fltr);
        Collection<org.openbel.framework.api.Kam.KamEdge> edges;
        edges = kam.getEdges(fltr2);
        List<org.openbel.framework.api.Kam.KamEdge> edges2;
        edges2 = sizedArrayList(edges.size());
        edges2.addAll(edges);

        List<KamEdge> list = ret.getKamEdges();

        org.openbel.framework.api.Kam.KamEdge edge;
        Iterator<org.openbel.framework.api.Kam.KamEdge> iter;
        iter = edges2.iterator();
        while (iter.hasNext()) {
            edge = iter.next();
            list.add(convert(kam.getKamInfo(), edge));
        }

        return ret;
    }

    private Dialect getDialect(final DialectHandle dialectHandle)
            throws RequestException {
        if (dialectHandle == null) {
            return null;
        }
        Dialect dialect = dialectCacheService.getDialect(dialectHandle
                .getHandle());

        if (dialect == null) {
            final String fmt = DIALECT_REQUEST_NO_DIALECT_FOR_HANDLE;
            final String msg = format(fmt, dialectHandle.getHandle());
            throw new RequestException(msg);
        }

        return dialect;
    }

    private org.openbel.framework.api.Kam getKam(
            final KamHandle kamHandle, final Dialect dialect)
            throws RequestException {
        org.openbel.framework.api.Kam objKam;
        objKam = kamCacheService.getKam(kamHandle.getHandle());

        if (objKam == null) {
            final String fmt = KAM_REQUEST_NO_KAM_FOR_HANDLE;
            final String msg = format(fmt, kamHandle.getHandle());
            throw new RequestException(msg);
        }
        return dialect == null ? objKam : new KamDialect(objKam, dialect);
    }

    private static org.openbel.framework.api.NodeFilter
            convertNodeFilterInRequest(
                    final NodeFilter nodeFilter,
                    final org.openbel.framework.api.Kam objKam) {
        if (nodeFilter == null) {
            return null;
        }

        return convert(objKam, nodeFilter);
    }

    private static org.openbel.framework.api.EdgeFilter
            convertEdgeFilterInRequest(
                    final EdgeFilter edgeFilter,
                    final org.openbel.framework.api.Kam objKam) {
        if (edgeFilter == null) {
            return null;
        }

        return convert(objKam, edgeFilter);
    }

    private KamInfo getKamInfo(final KamStoreObjectRef kamElementRef,
            final String errorMsg) throws RequestException {
        KamInfo kamInfo = null;
        try {
            kamInfo = kamCatalogDao
                    .getKamInfoById(kamElementRef.getKamInfoId());
        } catch (SQLException e) {
            throw new RequestException(errorMsg, e);
        }
        if (kamInfo == null) {
            throw new RequestException(errorMsg, new InvalidIdException(
                    kamElementRef.getEncodedString()));
        }
        return kamInfo;
    }
}
