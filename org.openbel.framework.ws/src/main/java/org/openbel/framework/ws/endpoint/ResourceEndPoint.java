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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.openbel.framework.api.EquivalencerException;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceLocation;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.ws.core.MissingRequest;
import org.openbel.framework.ws.core.RequestException;
import org.openbel.framework.ws.model.EquivalenceId;
import org.openbel.framework.ws.model.FindEquivalencesRequest;
import org.openbel.framework.ws.model.FindEquivalencesResponse;
import org.openbel.framework.ws.model.FindNamespaceEquivalenceRequest;
import org.openbel.framework.ws.model.FindNamespaceEquivalenceResponse;
import org.openbel.framework.ws.model.FindNamespaceValuesRequest;
import org.openbel.framework.ws.model.FindNamespaceValuesResponse;
import org.openbel.framework.ws.model.GetAllNamespacesRequest;
import org.openbel.framework.ws.model.GetAllNamespacesResponse;
import org.openbel.framework.ws.model.Namespace;
import org.openbel.framework.ws.model.NamespaceDescriptor;
import org.openbel.framework.ws.model.NamespaceValue;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.service.EquivalencerService;
import org.openbel.framework.ws.service.NamespaceResourceService;
import org.openbel.framework.ws.utils.ObjectFactorySingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * ResourceEndPoint defines webservice endpoints to expose functionality related
 * to BEL resources: namespaces, equivalences, etc.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @author Steve Ungerer
 */
@Endpoint
public class ResourceEndPoint extends WebServiceEndpoint {
    private static final String FIND_NAMESPACE_EQUIVALENCE_REQUEST =
            "FindNamespaceEquivalenceRequest";
    private static final String FIND_EQUIVALENCES_REQUEST =
            "FindEquivalencesRequest";
    private static final String FIND_NAMESPACE_VALUES_REQUEST =
            "FindNamespaceValuesRequest";
    private static final String GET_ALL_NAMESPACES_REQUEST =
            "GetAllNamespacesRequest";
    private static final ObjectFactory OBJECT_FACTORY = ObjectFactorySingleton
            .getInstance();

    @Autowired(required = true)
    private EquivalencerService equivalencerService;

    @Autowired(required = true)
    private NamespaceService namespaceService;

    @Autowired(required = true)
    private Index resourceIndex;

    @Autowired(required = true)
    private NamespaceResourceService namespaceResourceService;

    public ResourceEndPoint() {
        super();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = FIND_NAMESPACE_EQUIVALENCE_REQUEST)
    @ResponsePayload
    public
            FindNamespaceEquivalenceResponse findNamespaceEquivalence(
                    @RequestPayload FindNamespaceEquivalenceRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(FIND_NAMESPACE_EQUIVALENCE_REQUEST);
        }

        final NamespaceValue sourceNsValue = verifyNamespaceValue(request
                .getNamespaceValue());

        final Namespace targetNamespace = verifyNamespace(request
                .getTargetNamespace());

        try {
            final NamespaceValue equivalentNsValue = equivalencerService
                    .findNamespaceEquivalence(sourceNsValue, targetNamespace);
            FindNamespaceEquivalenceResponse response =
                    OBJECT_FACTORY.createFindNamespaceEquivalenceResponse();
            response.setNamespaceValue(equivalentNsValue);
            return response;
        } catch (EquivalencerException e) {
            final String fmt = "error equivalencing %s";
            final String msg = format(fmt, sourceNsValue.getValue());
            throw new RequestException(msg, e);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = FIND_EQUIVALENCES_REQUEST)
    @ResponsePayload
    public
            FindEquivalencesResponse findEquivalences(
                    @RequestPayload FindEquivalencesRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(FIND_EQUIVALENCES_REQUEST);
        }

        final NamespaceValue sourceNsValue = verifyNamespaceValue(request
                .getNamespaceValue());

        try {
            final List<NamespaceValue> equivalences = equivalencerService
                    .findEquivalences(sourceNsValue);
            FindEquivalencesResponse response =
                    OBJECT_FACTORY.createFindEquivalencesResponse();
            response.getNamespaceValues().addAll(equivalences);
            return response;
        } catch (EquivalencerException e) {
            final String fmt = "error equivalencing %s";
            final String msg = format(fmt, sourceNsValue.getValue());
            throw new RequestException(msg, e);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = FIND_NAMESPACE_VALUES_REQUEST)
    @ResponsePayload
    public
            FindNamespaceValuesResponse findNamespaceValues(
                    @RequestPayload FindNamespaceValuesRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(FIND_NAMESPACE_VALUES_REQUEST);
        }

        List<Namespace> namespaces;
        if (request.getNamespaces() == null
                || request.getNamespaces().isEmpty()) {
            // get all namespaces we know about
            namespaces =
                    new ArrayList<Namespace>(resourceIndex
                            .getNamespaceResources().size());
            for (ResourceLocation rl : resourceIndex.getNamespaceResources()) {
                Namespace ns = new Namespace();
                ns.setPrefix(""); // empty prefix, doesn't matter, just can't be null
                ns.setResourceLocation(rl.getResourceLocation());
                namespaces.add(ns);
            }
        } else {
            namespaces = request.getNamespaces();
        }

        List<Pattern> patterns =
                new ArrayList<Pattern>(request.getPatterns().size());
        for (String s : request.getPatterns()) {
            patterns.add(verifyPattern(s));
        }

        FindNamespaceValuesResponse response =
                OBJECT_FACTORY.createFindNamespaceValuesResponse();
        for (Namespace ns : namespaces) {
            ns = verifyNamespace(ns);
            try {
                // get namespace values
                List<String> vals = new ArrayList<String>();
                for (Pattern p : patterns) {
                    vals.addAll(
                            namespaceService.search(ns.getResourceLocation(), p));
                }

                // get equivalences
                List<NamespaceValue> values = new ArrayList<NamespaceValue>();
                for (String v : vals) {
                    NamespaceValue nv = new NamespaceValue();
                    nv.setNamespace(ns);
                    nv.setValue(v);
                    EquivalenceId eq = equivalencerService.getEquivalenceId(nv);
                    nv.setEquivalence(eq);
                    values.add(nv);
                }
                response.getNamespaceValues().addAll(values);
            } catch (IndexingFailure ife) {
                final String msg = "error searching namespaces";
                throw new RequestException(msg, ife);
            } catch (ResourceDownloadError rde) {
                final String msg = "error searching namespaces";
                throw new RequestException(msg, rde);
            } catch (EquivalencerException e) {
                final String msg = "error equivalencing patterns";
                throw new RequestException(msg, e);
            }
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_ALL_NAMESPACES_REQUEST)
    @ResponsePayload
    public
            GetAllNamespacesResponse
            getAllNamespaces(
                    @SuppressWarnings("unused") @RequestPayload GetAllNamespacesRequest request) {
        GetAllNamespacesResponse response = OBJECT_FACTORY
                .createGetAllNamespacesResponse();
        for (NamespaceDescriptor nd : namespaceResourceService
                .getAllNamespaceDescriptors()) {
            response.getNamespaceDescriptors().add(nd);
        }
        return response;
    }

    private NamespaceValue verifyNamespaceValue(final NamespaceValue nsValue)
            throws RequestException {
        if (nsValue == null) {
            final String msg = "missing namespace value";
            throw new RequestException(msg);
        }

        verifyNamespace(nsValue.getNamespace());

        if (noLength(nsValue.getValue())) {
            final String msg = "missing namespace value";
            throw new RequestException(msg);
        }

        return nsValue;
    }

    private Namespace verifyNamespace(final Namespace ns)
            throws RequestException {
        if (ns == null) {
            final String msg = "missing namespace";
            throw new RequestException(msg);
        }

        if (ns.getResourceLocation() == null
                || noLength(ns.getResourceLocation())) {
            final String msg = "invalid namespace";
            throw new RequestException(msg);
        }

        return ns;
    }

    private Pattern verifyPattern(final String s) throws RequestException {
        try {
            return Pattern.compile(s, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            final String msg = String.format("Invalid pattern: %s", s);
            throw new RequestException(msg);
        }
    }
}
