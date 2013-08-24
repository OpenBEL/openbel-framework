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
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.Strings.DIALECT_REQUEST_NO_DIALECT_FOR_HANDLE;

import java.sql.SQLException;
import java.util.List;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.Kam;
import org.openbel.framework.api.KamCacheService;
import org.openbel.framework.api.KamCacheServiceException;
import org.openbel.framework.internal.KAMCatalogDao;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.ws.core.MissingRequest;
import org.openbel.framework.ws.core.RequestException;
import org.openbel.framework.ws.model.DialectHandle;
import org.openbel.framework.ws.model.FindPathsRequest;
import org.openbel.framework.ws.model.FindPathsResponse;
import org.openbel.framework.ws.model.InterconnectRequest;
import org.openbel.framework.ws.model.InterconnectResponse;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.ScanRequest;
import org.openbel.framework.ws.model.ScanResponse;
import org.openbel.framework.ws.model.SimplePath;
import org.openbel.framework.ws.service.DialectCacheService;
import org.openbel.framework.ws.service.PathFindService;
import org.openbel.framework.ws.service.PathFindServiceException;
import org.openbel.framework.ws.utils.Converter;
import org.openbel.framework.ws.utils.Converter.KamStoreObjectRef;
import org.openbel.framework.ws.utils.InvalidIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * PathFindEndPoint defines the path find operations web service endpoints of:
 * <ul>
 * <li>Find Paths</li>
 * <li>Scan</li>
 * <li>Interconnect</li>
 * </ul>
 */
@Endpoint
public class PathFindEndPoint extends WebServiceEndpoint {
    private static final String FIND_PATHS_REQUEST = "FindPathsRequest";
    private static final String SCAN_REQUEST = "ScanRequest";
    private static final String INTERCONNECT_REQUEST = "InterconnectRequest";

    @Autowired(required = true)
    private DialectCacheService dialectCacheService;

    @Autowired(required = true)
    private KAMCatalogDao kamCatalogDao;

    @Autowired(required = true)
    private KamCacheService kamCacheService;

    @Autowired(required = true)
    private PathFindService pathFindService;

    public PathFindEndPoint() {
        super();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = FIND_PATHS_REQUEST)
    @ResponsePayload
    public FindPathsResponse
            findPaths(@RequestPayload FindPathsRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(FIND_PATHS_REQUEST);
        }

        final List<KamNode> sources = request.getSources();
        if (noItems(sources)) {
            throw new RequestException("Sources payload are missing");
        }

        final List<KamNode> targets = request.getTargets();
        if (noItems(targets)) {
            throw new RequestException("Targets payload are missing");
        }

        final int maxSearchDepth = request.getMaxDepth();

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());

        Kam kam;
        try {
            // read kam from first source node
            kam =
                    lookupKam(sources.get(0), dialect,
                            "Error retreving KAM associated with node");
        } catch (KamCacheServiceException e) {
            throw new RequestException(
                    "Error retreving KAM associated with node", e);
        } catch (InvalidIdException e) {
            throw new RequestException(
                    "Error retreving KAM associated with node", e);
        }

        try {
            final List<SimplePath> paths = pathFindService.findPaths(kam,
                    sources, targets, maxSearchDepth);

            final FindPathsResponse findPathsResponse = new FindPathsResponse();
            findPathsResponse.getPaths().addAll(paths);

            return findPathsResponse;
        } catch (PathFindServiceException e) {
            throw new RequestException(e.getMessage());
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = SCAN_REQUEST)
    @ResponsePayload
    public ScanResponse scan(@RequestPayload ScanRequest request)
            throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(SCAN_REQUEST);
        }

        final List<KamNode> sources = request.getSources();
        if (noItems(sources)) {
            throw new RequestException("Sources payload are missing");
        }

        final int maxSearchDepth = request.getMaxDepth();

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());

        Kam kam;
        try {
            // read kam from first source node
            kam =
                    lookupKam(sources.get(0), dialect,
                            "Error retreving KAM associated with node");
        } catch (KamCacheServiceException e) {
            throw new RequestException(
                    "Error retreving KAM associated with node", e);
        } catch (InvalidIdException e) {
            throw new RequestException(
                    "Error retreving KAM associated with node", e);
        }

        try {
            final List<SimplePath> paths = pathFindService.scan(kam,
                    sources, maxSearchDepth);

            final ScanResponse scanResponse = new ScanResponse();
            scanResponse.getPaths().addAll(paths);

            return scanResponse;
        } catch (PathFindServiceException e) {
            final String msg = "error in pathfind scan";
            throw new RequestException(msg, e);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = INTERCONNECT_REQUEST)
    @ResponsePayload
    public InterconnectResponse interconnect(
            @RequestPayload InterconnectRequest request)
            throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(INTERCONNECT_REQUEST);
        }

        final List<KamNode> sources = request.getSources();
        if (noItems(sources)) {
            throw new RequestException("Sources payload are missing");
        }

        final int maxSearchDepth = request.getMaxDepth();

        // Get the Dialect (may be null)
        final Dialect dialect = getDialect(request.getDialect());

        Kam kam;
        try {
            // read kam from first source node
            kam =
                    lookupKam(sources.get(0), dialect,
                            "Error retreving KAM associated with node");
        } catch (KamCacheServiceException e) {
            throw new RequestException(
                    "Error retreving KAM associated with node", e);
        } catch (InvalidIdException e) {
            throw new RequestException(
                    "Error retreving KAM associated with node", e);
        }

        try {
            final List<SimplePath> paths = pathFindService.interconnect(kam,
                    sources, maxSearchDepth);

            final InterconnectResponse interconnectResponse =
                    new InterconnectResponse();
            interconnectResponse.getPaths().addAll(paths);

            return interconnectResponse;
        } catch (PathFindServiceException e) {
            final String msg = "error in pathfind interconnect";
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

    /**
     * Lookup the {@link Kam} associated with the {@link KamNode}.
     *
     * @param kamNode {@link KamNode}, the kam node
     * @param dialect {@link Dialect} to apply to the kam, potentially <code>null</code>
     * @return the {@link Kam} associated with the {@link KamNode}, which
     * might be null
     * @throws KamCacheServiceException Thrown if an error occurred while
     * retrieving the {@link Kam} from the cache
     * @throws InvalidIdException Thrown if an error occurred while decoding the
     * {@link KamNode node}'s id
     */
    private Kam lookupKam(KamNode kamNode, Dialect dialect,
            final String errorMsg)
            throws KamCacheServiceException, InvalidIdException,
            RequestException {
        KamStoreObjectRef kamNodeRef = Converter.decodeNode(kamNode);
        KamInfo kamInfo = null;
        try {
            kamInfo = kamCatalogDao.getKamInfoById(kamNodeRef.getKamInfoId());
        } catch (SQLException e) {
            throw new RequestException(errorMsg, e);
        }
        if (kamInfo == null) {
            throw new InvalidIdException(kamNodeRef.getEncodedString());
        }
        final Kam kam = kamCacheService.getKam(kamInfo.getName());
        if (kam == null) {
            throw new InvalidIdException(kamNodeRef.getEncodedString());
        }
        return kam;
    }
}
