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

import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_HANDLE;
import static org.openbel.framework.ws.model.ObjectFactory.*;

import java.util.List;

import org.openbel.framework.ws.core.MissingRequest;
import org.openbel.framework.ws.core.RequestException;
import org.openbel.framework.ws.model.*;
import org.openbel.framework.ws.service.KamStoreService;
import org.openbel.framework.ws.service.KamStoreServiceException;
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

        GetCitationsResponse response = createGetCitationsResponse();
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

        GetBelDocumentsResponse response = createGetBelDocumentsResponse();
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
                createGetAnnotationTypesResponse();

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

        GetNamespacesResponse response = createGetNamespacesResponse();
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

        GetCatalogResponse response = createGetCatalogResponse();
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
                createGetSupportingEvidenceResponse();

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
                createGetSupportingTermsResponse();
        for (BelTerm belTerm : kamStoreService.getSupportingTerms(kamNode)) {
            response.getTerms().add(belTerm);
        }

        return response;
    }

    public void setKamStoreService(KamStoreService kamStoreService) {
        this.kamStoreService = kamStoreService;
    }
}
