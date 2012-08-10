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
import static org.openbel.framework.common.Strings.DIALECT_REQUEST_NO_DIALECT_FOR_HANDLE;
import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_KAM_FOR_HANDLE;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.Equivalencer;
import org.openbel.framework.api.EquivalencerException;
import org.openbel.framework.api.KamCacheService;
import org.openbel.framework.api.KamDialect;
import org.openbel.framework.api.KAMStore;
import org.openbel.framework.api.KAMStoreException;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.ws.core.MissingRequest;
import org.openbel.framework.ws.core.RequestException;
import org.openbel.framework.ws.model.DialectHandle;
import org.openbel.framework.ws.model.KamHandle;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.MapDataRequest;
import org.openbel.framework.ws.model.MapDataResponse;
import org.openbel.framework.ws.model.Namespace;
import org.openbel.framework.ws.model.NamespaceValue;
import org.openbel.framework.ws.model.NodeFilter;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.service.DialectCacheService;
import org.openbel.framework.ws.utils.Converter;
import org.openbel.framework.ws.utils.ObjectFactorySingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * {@link MapDataEndPoint} is a {@link WebServiceEndpoint webservice endpoint}
 * to provide endpoints to map namespace data to a {@link Kam}.
 *
 * @see MapDataService#mapData(Kam, Namespace, List, NodeFilter)
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
@Deprecated
@Endpoint
public class MapDataEndPoint extends WebServiceEndpoint {
    private static final String MAP_DATA_REQUEST = "MapDataRequest";
    private static final ObjectFactory OBJECT_FACTORY = ObjectFactorySingleton
            .getInstance();

    /**
     * Dependent service that provides access to cached {@link Kam kams}
     */
    @Autowired(required = true)
    private KamCacheService kamCacheService;

    @Autowired(required = true)
    private DialectCacheService dialectCacheService;

    @Autowired(required = true)
    private KAMStore kAMStore;

    private Equivalencer equivalencer = new Equivalencer();

    /**
     *
     * Endpoint method to handle the {@link MapDataEndPoint#MAP_DATA_REQUEST}
     * request. Receives {@link Namespace namespace} values and returns
     * {@link KamNode kam nodes}.
     *
     * @param req {@link MapDataRequest}, the endpoint request
     * @return the endpoint response
     * @throws RequestException Thrown if an error occurred executing this
     *             endpoint method
     * @deprecated Use
     *             {@link KamEndPoint#findKamNodesByNamespaceValues(org.openbel.framework.ws.model.FindKamNodesByNamespaceValuesRequest)}
     */
    @Deprecated
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = MAP_DATA_REQUEST)
    @ResponsePayload
    public MapDataResponse mapData(@RequestPayload MapDataRequest req)
            throws RequestException {
        // validate request
        if (req == null) {
            throw new MissingRequest(MAP_DATA_REQUEST);
        }

        // validate kam
        final KamHandle handle = req.getHandle();
        if (handle == null || handle.getHandle() == null) {
            throw new RequestException("KamHandle is missing");
        }
        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(req.getDialect());
        final org.openbel.framework.api.Kam kam = getKam(
                handle, dialect);

        final Namespace ns = req.getNamespace();
        if (ns == null) {
            throw new RequestException("Namespace is missing");
        }

        final List<String> values = req.getValues();
        if (values.isEmpty()) {
            throw new RequestException("Values are missing");
        }

        // read optional filter
        final NodeFilter nf = req.getNodeFilter();

        // convert namespace/values to NamespaceValue objects for delegation
        List<NamespaceValue> nvs = new ArrayList<NamespaceValue>(values.size());
        for (String s : values) {
            NamespaceValue nv = OBJECT_FACTORY.createNamespaceValue();
            nv.setNamespace(ns);
            nv.setValue(s);
            nvs.add(nv);
        }

        // delegate to findKamNodesByNamespaceValues
        List<org.openbel.framework.api.Kam.KamNode> nodes;
        try {
            nodes =
                    KamEndPoint.findKamNodesByNamespacevalues(nvs, nf, kam,
                            kAMStore,
                            equivalencer);
        } catch (EquivalencerException e) {
            throw new RequestException("Error mapping data", e);
        } catch (KAMStoreException e) {
            throw new RequestException("Error mapping data", e);
        }

        KamInfo kamInfo = kam.getKamInfo();
        final MapDataResponse res = OBJECT_FACTORY.createMapDataResponse();
        for (org.openbel.framework.api.Kam.KamNode kn : nodes) {
            res.getKamNodes().add(Converter.convert(kamInfo, kn));
        }

        return res;
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
}
