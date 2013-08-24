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

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.Kam;
import org.openbel.framework.api.KamCacheService;
import org.openbel.framework.api.KamStoreException;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.ws.core.MissingRequest;
import org.openbel.framework.ws.core.RequestException;
import org.openbel.framework.ws.dialect.DialectFactory;
import org.openbel.framework.ws.model.DialectHandle;
import org.openbel.framework.ws.model.GetCustomDialectRequest;
import org.openbel.framework.ws.model.GetCustomDialectResponse;
import org.openbel.framework.ws.model.GetDefaultDialectRequest;
import org.openbel.framework.ws.model.GetDefaultDialectResponse;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.model.ReleaseDialectRequest;
import org.openbel.framework.ws.model.ReleaseDialectResponse;
import org.openbel.framework.ws.service.DialectCacheService;
import org.openbel.framework.ws.utils.Converter;
import org.openbel.framework.ws.utils.ObjectFactorySingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Endpoint for {@link Dialect}s
 *
 * @author Steve Ungerer
 */
@Endpoint
public class DialectEndPoint extends WebServiceEndpoint {

    private static final String GET_DEFAULT_DIALECT_REQUEST =
            "GetDefaultDialectRequest";
    private static final String GET_CUSTOM_DIALECT_REQUEST =
            "GetCustomDialectRequest";
    private static final String RELEASE_DIALECT_REQUEST =
            "ReleaseDialectRequest";
    private static final ObjectFactory OBJECT_FACTORY = ObjectFactorySingleton
            .getInstance();

    @Autowired(required = true)
    private DialectCacheService dialectService;
    @Autowired(required = true)
    private DialectFactory dialectFactory;
    @Autowired(required = true)
    private KamCacheService kamCache;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_DEFAULT_DIALECT_REQUEST)
    @ResponsePayload
    public
            GetDefaultDialectResponse getDefaultDialect(
                    @RequestPayload GetDefaultDialectRequest request)
                    throws RequestException {

        if (request == null) {
            throw new MissingRequest(GET_DEFAULT_DIALECT_REQUEST);
        }

        Kam kam = kamCache.getKam(request.getKam().getHandle());
        if (kam == null) {
            throw new RequestException("Invalid KamHandle: "
                    + request.getKam().getHandle());
        }

        Dialect d = dialectFactory.createDefaultDialect(kam.getKamInfo());

        GetDefaultDialectResponse resp =
                OBJECT_FACTORY.createGetDefaultDialectResponse();
        DialectHandle dh = OBJECT_FACTORY.createDialectHandle();
        dh.setHandle(dialectService.cacheDialect(d));
        resp.setDialect(dh);
        return resp;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_CUSTOM_DIALECT_REQUEST)
    @ResponsePayload
    public
            GetCustomDialectResponse getCustomDialect(
                    @RequestPayload GetCustomDialectRequest request)
                    throws RequestException {

        if (request == null) {
            throw new MissingRequest(GET_CUSTOM_DIALECT_REQUEST);
        }

        Kam kam = kamCache.getKam(request.getKam().getHandle());
        if (kam == null) {
            throw new RequestException("Invalid KamHandle: "
                    + request.getKam().getHandle());
        }

        List<Namespace> geneNamespaces = convert(request.getGeneNamespaces());
        List<Namespace> bpNamespaces = convert(request.getBpNamespaces());
        List<Namespace> chemNamespaces = convert(request.getChemNamespaces());

        Dialect d;
        try {
            d =
                    dialectFactory.createCustomDialect(kam.getKamInfo(),
                            geneNamespaces, bpNamespaces, chemNamespaces,
                            Converter.convert(request.getSyntax()),
                            request.isHideNamespacePrefixes());
        } catch (KamStoreException e) {
            throw new RequestException("Failed to construct dialect", e);
        }

        GetCustomDialectResponse resp =
                OBJECT_FACTORY.createGetCustomDialectResponse();
        DialectHandle dh = OBJECT_FACTORY.createDialectHandle();
        dh.setHandle(dialectService.cacheDialect(d));
        resp.setDialect(dh);
        return resp;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = RELEASE_DIALECT_REQUEST)
    @ResponsePayload
    public
            ReleaseDialectResponse releaseDialect(
                    @RequestPayload ReleaseDialectRequest request)
                    throws RequestException {

        if (request == null) {
            throw new MissingRequest(RELEASE_DIALECT_REQUEST);
        }

        DialectHandle dialect = request.getDialect();

        // Release the kam
        dialectService.releaseDialect(dialect.getHandle());

        // Set up the response
        return OBJECT_FACTORY.createReleaseDialectResponse();
    }

    private List<Namespace> convert(
            List<org.openbel.framework.ws.model.Namespace> ws) {
        if (ws == null || ws.isEmpty()) {
            return new ArrayList<Namespace>(0);
        }
        List<Namespace> l = new ArrayList<Namespace>(ws.size());
        for (org.openbel.framework.ws.model.Namespace n : ws) {
            l.add(Converter.convert(n));
        }
        return l;
    }

}
