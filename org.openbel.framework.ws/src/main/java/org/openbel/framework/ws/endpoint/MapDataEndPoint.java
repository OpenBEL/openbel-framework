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
import static org.openbel.framework.common.Strings.DIALECT_REQUEST_NO_DIALECT_FOR_HANDLE;
import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_KAM_FOR_HANDLE;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.Equivalencer;
import org.openbel.framework.api.EquivalencerException;
import org.openbel.framework.api.KamCacheService;
import org.openbel.framework.api.KamDialect;
import org.openbel.framework.api.KamStore;
import org.openbel.framework.api.KamStoreException;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
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
    private KamStore kamStore;

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
                            kamStore,
                            equivalencer);
        } catch (EquivalencerException e) {
            throw new RequestException("Error mapping data", e);
        } catch (KamStoreException e) {
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
