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
