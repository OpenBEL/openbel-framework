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

import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_HANDLE;

import java.util.List;

import org.openbel.framework.ws.core.MissingRequest;
import org.openbel.framework.ws.core.RequestException;
import org.openbel.framework.ws.model.AnnotationType;
import org.openbel.framework.ws.model.BelDocument;
import org.openbel.framework.ws.model.BelStatement;
import org.openbel.framework.ws.model.BelTerm;
import org.openbel.framework.ws.model.Citation;
import org.openbel.framework.ws.model.CitationType;
import org.openbel.framework.ws.model.GetAnnotationTypesRequest;
import org.openbel.framework.ws.model.GetAnnotationTypesResponse;
import org.openbel.framework.ws.model.GetBelDocumentsRequest;
import org.openbel.framework.ws.model.GetBelDocumentsResponse;
import org.openbel.framework.ws.model.GetCatalogRequest;
import org.openbel.framework.ws.model.GetCatalogResponse;
import org.openbel.framework.ws.model.GetCitationsRequest;
import org.openbel.framework.ws.model.GetCitationsResponse;
import org.openbel.framework.ws.model.GetNamespacesRequest;
import org.openbel.framework.ws.model.GetNamespacesResponse;
import org.openbel.framework.ws.model.GetSupportingEvidenceRequest;
import org.openbel.framework.ws.model.GetSupportingEvidenceResponse;
import org.openbel.framework.ws.model.GetSupportingTermsRequest;
import org.openbel.framework.ws.model.GetSupportingTermsResponse;
import org.openbel.framework.ws.model.Kam;
import org.openbel.framework.ws.model.KamEdge;
import org.openbel.framework.ws.model.KamFilter;
import org.openbel.framework.ws.model.KamHandle;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.Namespace;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.service.KamStoreService;
import org.openbel.framework.ws.service.KamStoreServiceException;
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
public class KamStoreEndPoint extends WebServiceEndpoint {
    private static final String GET_CITATIONS_REQUEST = "GetCitationsRequest";
    private static final String GET_BEL_DOCUMENTS_REQUEST =
            "GetBelDocumentsRequest";
    private static final String GET_ANNOTATION_TYPES_REQUEST =
            "GetAnnotationTypesRequest";
    private static final String GET_NAMESPACES_REQUEST = "GetNamespacesRequest";
    private static final String GET_CATALOG_REQUEST = "GetCatalogRequest";
    private static final String GET_SUPPORTING_EVIDENCE_REQUEST =
            "GetSupportingEvidenceRequest";
    private static final String GET_SUPPORTING_TERMS_REQUEST =
            "GetSupportingTermsRequest";
    private static final ObjectFactory OBJECT_FACTORY = ObjectFactorySingleton
            .getInstance();

    @Autowired(required = true)
    private KamStoreService kamStoreService;

    public KamStoreEndPoint() {
        super();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_CITATIONS_REQUEST)
    @ResponsePayload
    public GetCitationsResponse getCitations(
            @RequestPayload GetCitationsRequest request)
            throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(GET_CITATIONS_REQUEST);
        }

        // Make sure a Kam was specified in the request
        KamHandle kamHandle = request.getHandle();
        if (null == kamHandle) {
            throw new RequestException("Kam payload is missing");
        }

        CitationType citationType = request.getCitationType();
        if (null == citationType) {
            throw new RequestException("citationType payload is missing");
        }

        List<String> referenceIds = request.getReferenceIds();

        // Get the optional BelDocument
        BelDocument belDocument = request.getDocument();

        // Create the response

        GetCitationsResponse response = OBJECT_FACTORY
                .createGetCitationsResponse();
        try {
            for (Citation citation : kamStoreService.getCitations(kamHandle,
                    citationType, referenceIds, belDocument)) {
                response.getCitations().add(citation);
            }
        } catch (KamStoreServiceException e) {
            final String msg = "error getting citations";
            throw new RequestException(msg, e);
        }

        return response;
    }

    /**
     * @param belDocumentsRequest
     * @return
     * @throws Exception
     */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_BEL_DOCUMENTS_REQUEST)
    public
            GetBelDocumentsResponse getBelDocuments(
                    @RequestPayload GetBelDocumentsRequest request)
                    throws RequestException {

        // validate request
        if (request == null) {
            throw new MissingRequest(GET_BEL_DOCUMENTS_REQUEST);
        }

        // Sanity check the full request payload
        KamHandle kamHandle = request.getHandle();
        if (kamHandle == null || kamHandle.getHandle() == null) {
            throw new RequestException(KAM_REQUEST_NO_HANDLE);
        }

        List<BelDocument> documents;
        try {
            documents = kamStoreService.getBelDocuments(kamHandle);
        } catch (KamStoreServiceException e) {
            String msg = "getting BEL documents:";
            throw new RequestException(msg, e);
        }

        GetBelDocumentsResponse response = OBJECT_FACTORY
                .createGetBelDocumentsResponse();
        for (BelDocument document : documents) {
            response.getDocuments().add(document);
        }

        return response;
    }

    /**
     *
     * @param annotationTypesRequest
     * @return
     * @throws Exception
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_ANNOTATION_TYPES_REQUEST)
    @ResponsePayload
    public
            GetAnnotationTypesResponse getAnnotationTypes(
                    @RequestPayload GetAnnotationTypesRequest request)
                    throws Exception {

        // validate request
        if (request == null) {
            throw new MissingRequest(GET_ANNOTATION_TYPES_REQUEST);
        }

        // Make sure a Kam was specified in the request
        KamHandle kamHandle = request.getHandle();
        if (null == kamHandle) {
            throw new KamStoreServiceException("Kam payload is missing");
        }

        GetAnnotationTypesResponse response =
                OBJECT_FACTORY.createGetAnnotationTypesResponse();

        List<AnnotationType> types =
                kamStoreService.getAnnotationTypes(kamHandle);
        for (AnnotationType type : types) {
            response.getAnnotationTypes().add(type);
        }

        return response;
    }

    /**
     *
     * @param namespacesRequest
     * @return
     * @throws Exception
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_NAMESPACES_REQUEST)
    @ResponsePayload
    public GetNamespacesResponse getNamespaces(
            @RequestPayload GetNamespacesRequest request) throws Exception {

        // validate request
        if (request == null) {
            throw new MissingRequest(GET_NAMESPACES_REQUEST);
        }

        // Make sure a Kam was specified in the request
        KamHandle kamHandle = request.getHandle();
        if (null == kamHandle) {
            throw new KamStoreServiceException("Kam payload is missing");
        }

        GetNamespacesResponse response = OBJECT_FACTORY
                .createGetNamespacesResponse();
        for (Namespace namespace : kamStoreService.getNamespaces(kamHandle)) {
            response.getNamespaces().add(namespace);
        }
        return response;
    }

    /**
     *
     * @param catalogRequest
     * @return
     * @throws Exception
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_CATALOG_REQUEST)
    @ResponsePayload
    public
            GetCatalogResponse
            getCatalog(
                    @SuppressWarnings("unused") @RequestPayload GetCatalogRequest request)
                    throws Exception {

        GetCatalogResponse response = OBJECT_FACTORY.createGetCatalogResponse();
        for (Kam kam : kamStoreService.getCatalog()) {
            response.getKams().add(kam);
        }

        return response;
    }

    /**
     *
     * @param supportingEvidenceRequest
     * @return
     * @throws Exception
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_SUPPORTING_EVIDENCE_REQUEST)
    @ResponsePayload
    public
            GetSupportingEvidenceResponse getSupportingEvidence(
                    @RequestPayload GetSupportingEvidenceRequest request)
                    throws Exception {

        // validate request
        if (request == null) {
            throw new MissingRequest(GET_SUPPORTING_EVIDENCE_REQUEST);
        }

        // Make sure a KamEdge was specified in the request
        KamEdge kamEdge = request.getKamEdge();
        if (null == kamEdge) {
            throw new KamStoreServiceException("KamEdge payload is missing");
        }
        // Check for the optional KamFilter
        KamFilter kamFilter = request.getFilter();

        GetSupportingEvidenceResponse response =
                OBJECT_FACTORY.createGetSupportingEvidenceResponse();

        for (BelStatement belStatement : kamStoreService.getSupportingEvidence(
                kamEdge, kamFilter)) {
            response.getStatements().add(belStatement);
        }

        return response;
    }

    /**
     *
     * @param supportingTermsRequest
     * @return
     * @throws Exception
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = GET_SUPPORTING_TERMS_REQUEST)
    @ResponsePayload
    public
            GetSupportingTermsResponse getSupportingTerms(
                    @RequestPayload GetSupportingTermsRequest request)
                    throws Exception {

        // validate request
        if (request == null) {
            throw new MissingRequest(GET_SUPPORTING_TERMS_REQUEST);
        }

        // Make sure a KamNode was specified in the request
        KamNode kamNode = request.getKamNode();
        if (null == kamNode) {
            throw new KamStoreServiceException("KamNode payload is missing");
        }

        GetSupportingTermsResponse response =
                OBJECT_FACTORY.createGetSupportingTermsResponse();
        for (BelTerm belTerm : kamStoreService.getSupportingTerms(kamNode)) {
            response.getTerms().add(belTerm);
        }

        return response;
    }

    public void setKamStoreService(KamStoreService kamStoreService) {
        this.kamStoreService = kamStoreService;
    }
}
