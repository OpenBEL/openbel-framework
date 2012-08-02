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
package org.openbel.framework.ws.endpoint;

import static java.lang.String.format;
import static org.openbel.framework.api.KamCacheService.LoadStatus.LOADING;
import static org.openbel.framework.api.KamUtils.difference;
import static org.openbel.framework.api.KamUtils.intersection;
import static org.openbel.framework.api.KamUtils.newInstance;
import static org.openbel.framework.api.KamUtils.union;
import static org.openbel.framework.common.BELUtilities.getFirstMessage;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_KAM;
import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_KAM_FOR_HANDLE;
import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_KAM_FOR_NAME;
import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_NAME;
import static org.openbel.framework.ws.model.KAMLoadStatus.FAILED;
import static org.openbel.framework.ws.utils.Converter.convert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.api.KamCacheService;
import org.openbel.framework.api.KamCacheService.LoadKAMResult;
import org.openbel.framework.api.KamCacheServiceException;
import org.openbel.framework.internal.KAMCatalogDao;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.ws.core.MissingRequest;
import org.openbel.framework.ws.core.RequestException;
import org.openbel.framework.ws.model.DifferenceKamsRequest;
import org.openbel.framework.ws.model.DifferenceKamsResponse;
import org.openbel.framework.ws.model.GetNewInstanceRequest;
import org.openbel.framework.ws.model.GetNewInstanceResponse;
import org.openbel.framework.ws.model.IntersectKamsRequest;
import org.openbel.framework.ws.model.IntersectKamsResponse;
import org.openbel.framework.ws.model.KAMLoadStatus;
import org.openbel.framework.ws.model.Kam;
import org.openbel.framework.ws.model.KamEdge;
import org.openbel.framework.ws.model.KamHandle;
import org.openbel.framework.ws.model.LoadKamRequest;
import org.openbel.framework.ws.model.LoadKamResponse;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.model.ReleaseKamRequest;
import org.openbel.framework.ws.model.ReleaseKamResponse;
import org.openbel.framework.ws.model.UnionKamsRequest;
import org.openbel.framework.ws.model.UnionKamsResponse;
import org.openbel.framework.ws.service.KamStoreServiceException;
import org.openbel.framework.ws.utils.Converter;
import org.openbel.framework.ws.utils.InvalidIdException;
import org.openbel.framework.ws.utils.ObjectFactorySingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * TODO Provide documentation
 */
@Endpoint
public class KamUtilsEndPoint extends WebServiceEndpoint {
    private static final String LOAD_KAM_REQUEST = "LoadKamRequest";
    private static final String RELEASE_KAM_REQUEST = "ReleaseKamRequest";
    private static final String GET_NEW_INSTANCE_REQUEST =
            "GetNewInstanceRequest";
    private static final String KAM_UNION_REQUEST = "UnionKamsRequest";
    private static final String KAM_INTERSECTION_REQUEST =
            "IntersectKamsRequest";
    private static final String KAM_DIFFERENCE_REQUEST =
            "DifferenceKamsRequest";
    private static final ObjectFactory OBJECT_FACTORY = ObjectFactorySingleton
            .getInstance();

    @Autowired(required = true)
    private KamCacheService kamCacheService;
    @Autowired
    private KAMCatalogDao kamCatalogDao;

    public KamUtilsEndPoint() {
        super();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOAD_KAM_REQUEST)
    @ResponsePayload
    public LoadKamResponse loadKam(@RequestPayload LoadKamRequest request)
            throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(LOAD_KAM_REQUEST);
        }

        Kam kam = request.getKam();
        if (kam == null) {
            throw new RequestException(KAM_REQUEST_NO_KAM);
        }
        if (kam.getName() == null) {
            throw new RequestException(KAM_REQUEST_NO_NAME);
        }

        KamInfo kamInfo = null;
        List<KamInfo> catalog;
        try {
            catalog = kamCatalogDao.getCatalog();
        } catch (SQLException e) {
            String msg = getFirstMessage(e);
            LoadKamResponse resp = OBJECT_FACTORY.createLoadKamResponse();
            resp.setLoadStatus(FAILED);
            resp.setMessage(msg);
            return resp;
        }

        for (final KamInfo ki : catalog) {
            if (ki.getName().equals(kam.getName())) {
                kamInfo = ki;
                break;
            }
        }

        if (kamInfo == null) {
            String errorMsg = KAM_REQUEST_NO_KAM_FOR_NAME;
            String msg = format(errorMsg, kam.getName());
            LoadKamResponse resp = OBJECT_FACTORY.createLoadKamResponse();
            resp.setLoadStatus(FAILED);
            resp.setMessage(msg);
            return resp;
        }

        KAMCatalogDao.KamFilter filter;
        try {
            filter = convert(request.getFilter(), kamInfo);
        } catch (InvalidIdException ex) {
            LoadKamResponse resp = OBJECT_FACTORY.createLoadKamResponse();
            resp.setLoadStatus(FAILED);
            resp.setMessage(ex.getMessage());
            return resp;
        }

        LoadKAMResult rslt;
        try {
            rslt = kamCacheService.loadKamWithResult(kamInfo, filter);
        } catch (KamCacheServiceException e) {
            String msg = getFirstMessage(e);
            LoadKamResponse resp = OBJECT_FACTORY.createLoadKamResponse();
            resp.setLoadStatus(FAILED);
            resp.setMessage(msg);
            return resp;
        }

        LoadKamResponse resp = OBJECT_FACTORY.createLoadKamResponse();
        if (rslt.getStatus() == LOADING) {
            resp.setLoadStatus(KAMLoadStatus.IN_PROCESS);
            return resp;
        }
        KamHandle kamHandle = OBJECT_FACTORY.createKamHandle();
        kamHandle.setHandle(rslt.getHandle());
        resp.setHandle(kamHandle);
        resp.setLoadStatus(KAMLoadStatus.COMPLETE);
        return resp;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = RELEASE_KAM_REQUEST)
    @ResponsePayload
    public ReleaseKamResponse releaseKam(
            @RequestPayload ReleaseKamRequest request) throws Exception {

        // validate request
        if (request == null) {
            throw new MissingRequest(RELEASE_KAM_REQUEST);
        }

        // Make sure a Kam was specified in the request
        KamHandle kamHandle = request.getKam();
        if (null == kamHandle) {
            throw new KamStoreServiceException("KamHandle payload is missing");
        }

        // Release the kam
        kamCacheService.releaseKam(kamHandle.getHandle());

        // Set up the response
        ReleaseKamResponse response = OBJECT_FACTORY.createReleaseKamResponse();
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_NEW_INSTANCE_REQUEST)
    @ResponsePayload
    public
            GetNewInstanceResponse getNewInstance(
                    @RequestPayload GetNewInstanceRequest request)
                    throws Exception {

        // validate request
        if (request == null) {
            throw new MissingRequest(GET_NEW_INSTANCE_REQUEST);
        }

        // Make sure a Kam was specified in the request
        KamHandle kamHandle = request.getHandle();
        if (null == kamHandle) {
            throw new KamStoreServiceException("KamHandle payload is missing");
        }

        // Get the real Kam from the KamCache
        org.openbel.framework.api.Kam objKam =
                kamCacheService.getKam(kamHandle.getHandle());
        if (objKam == null) {
            throw new RequestException(format(KAM_REQUEST_NO_KAM_FOR_HANDLE,
                    kamHandle.getHandle()));
        }

        // Use the KamUtils class to do the work
        org.openbel.framework.api.Kam objNewKamInstance;
        objNewKamInstance = newInstance(objKam);

        // Set up the response
        GetNewInstanceResponse response = OBJECT_FACTORY
                .createGetNewInstanceResponse();
        response.setHandle(cacheDerivedKam(objNewKamInstance));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = KAM_UNION_REQUEST)
    @ResponsePayload
    public UnionKamsResponse
            unionKams(@RequestPayload UnionKamsRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(KAM_UNION_REQUEST);
        }

        KamHandle kam1 = request.getKam1();
        if (null == kam1) {
            throw new RequestException("kamHandle for Kam1 payload is missing");
        }

        KamHandle kam2 = request.getKam2();
        List<KamEdge> kamEdges = request.getKamEdges();

        if (kam2 == null && noItems(kamEdges)) {
            final String msg = "Missing kam2 and kamEdges - nothing to do";
            throw new RequestException(msg);
        }

        org.openbel.framework.api.Kam objKam1;
        objKam1 = kamCacheService.getKam(kam1.getHandle());
        if (objKam1 == null) {
            throw new RequestException(format(KAM_REQUEST_NO_KAM_FOR_HANDLE,
                    kam1.getHandle()));
        }

        org.openbel.framework.api.Kam objNewKam = null;
        if (null != kam2) {
            org.openbel.framework.api.Kam objKam2;
            objKam2 = kamCacheService.getKam(kam2.getHandle());
            if (objKam2 == null) {
                throw new RequestException(format(
                        KAM_REQUEST_NO_KAM_FOR_HANDLE, kam2.getHandle()));
            }

            objNewKam = union(objKam1, objKam2);
        } else {
            List<org.openbel.framework.api.Kam.KamEdge> edges =
                    convertEdges(
                            kamEdges, objKam1);
            objNewKam = union(objKam1, edges);
        }

        UnionKamsResponse response = OBJECT_FACTORY.createUnionKamsResponse();
        response.setHandle(cacheDerivedKam(objNewKam));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = KAM_INTERSECTION_REQUEST)
    @ResponsePayload
    public
            IntersectKamsResponse intersectKams(
                    @RequestPayload IntersectKamsRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(KAM_INTERSECTION_REQUEST);
        }

        KamHandle kam1 = request.getKam1();
        if (null == kam1) {
            throw new RequestException("kamHandle for Kam1 payload is missing");
        }

        KamHandle kam2 = request.getKam2();
        List<KamEdge> kamEdges = request.getKamEdges();

        if (kam2 == null && noItems(kamEdges)) {
            final String msg = "Missing kam2 and kamEdges - nothing to do";
            throw new RequestException(msg);
        }

        org.openbel.framework.api.Kam objKam1;
        objKam1 = kamCacheService.getKam(kam1.getHandle());
        if (objKam1 == null) {
            throw new RequestException(format(KAM_REQUEST_NO_KAM_FOR_HANDLE,
                    kam1.getHandle()));
        }

        org.openbel.framework.api.Kam objNewKam = null;
        if (null != kam2) {
            // Get the real Kam from the KamCache
            org.openbel.framework.api.Kam objKam2;
            objKam2 = kamCacheService.getKam(kam2.getHandle());
            if (objKam2 == null) {
                throw new RequestException(format(
                        KAM_REQUEST_NO_KAM_FOR_HANDLE, kam2.getHandle()));
            }
            objNewKam = intersection(objKam1, objKam2);
        } else {
            List<org.openbel.framework.api.Kam.KamEdge> edges =
                    convertEdges(
                            kamEdges, objKam1);
            objNewKam = intersection(objKam1, edges);
        }

        IntersectKamsResponse response = OBJECT_FACTORY
                .createIntersectKamsResponse();
        response.setHandle(cacheDerivedKam(objNewKam));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = KAM_DIFFERENCE_REQUEST)
    @ResponsePayload
    public DifferenceKamsResponse differenceKams(
            @RequestPayload DifferenceKamsRequest request)
            throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(KAM_DIFFERENCE_REQUEST);
        }

        KamHandle kam1 = request.getKam1();
        if (null == kam1) {
            throw new RequestException("kamHandle for Kam1 payload is missing");
        }

        KamHandle kam2 = request.getKam2();
        List<KamEdge> kamEdges = request.getKamEdges();

        if (kam2 == null && noItems(kamEdges)) {
            final String msg = "Missing kam2 and kamEdges - nothing to do";
            throw new RequestException(msg);
        }

        org.openbel.framework.api.Kam objKam1;
        objKam1 = kamCacheService.getKam(kam1.getHandle());
        if (objKam1 == null) {
            throw new RequestException(format(KAM_REQUEST_NO_KAM_FOR_HANDLE,
                    kam1.getHandle()));
        }

        org.openbel.framework.api.Kam objNewKam = null;
        if (null != kam2) {
            org.openbel.framework.api.Kam objKam2;
            objKam2 = kamCacheService.getKam(kam2.getHandle());
            if (objKam2 == null) {
                throw new RequestException(format(
                        KAM_REQUEST_NO_KAM_FOR_HANDLE, kam2.getHandle()));
            }
            objNewKam = difference(objKam1, objKam2);
        } else {
            List<org.openbel.framework.api.Kam.KamEdge> edges =
                    convertEdges(
                            kamEdges, objKam1);
            objNewKam = difference(objKam1, edges);
        }

        DifferenceKamsResponse response = OBJECT_FACTORY
                .createDifferenceKamsResponse();
        response.setHandle(cacheDerivedKam(objNewKam));
        return response;
    }

    private KamHandle cacheDerivedKam(
            org.openbel.framework.api.Kam derivedKam) {

        String kamHandleString = kamCacheService.cacheKam(derivedKam
                .getKamInfo().getName(), derivedKam);

        KamHandle kamHandle = OBJECT_FACTORY.createKamHandle();
        kamHandle.setHandle(kamHandleString);

        return kamHandle;
    }

    private List<org.openbel.framework.api.Kam.KamEdge>
            convertEdges(
                    List<KamEdge> kamEdges,
                    org.openbel.framework.api.Kam objKam1)
                    throws RequestException {
        List<org.openbel.framework.api.Kam.KamEdge> edges =
                new ArrayList<org.openbel.framework.api.Kam.KamEdge>();
        for (final KamEdge wke : kamEdges) {
            try {
                org.openbel.framework.api.Kam.KamEdge edge =
                        Converter
                                .convert(wke, objKam1);

                // only add edge if it existed in kam1.
                if (edge != null) {
                    edges.add(edge);
                }
            } catch (InvalidIdException e) {
                throw new RequestException(
                        "KamEdge was not found in Kam1. ", e);
            }
        }
        return edges;
    }

    public void setKamCacheService(KamCacheService kamCacheService) {
        this.kamCacheService = kamCacheService;
    }

    public void setKamCatalogDao(KAMCatalogDao kamCatalogDao) {
        this.kamCatalogDao = kamCatalogDao;
    }
}
