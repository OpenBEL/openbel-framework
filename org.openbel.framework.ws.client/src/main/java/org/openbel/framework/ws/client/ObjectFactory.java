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
package org.openbel.framework.ws.client;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.openbel.framework.ws.client
 * package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create an instance of {@link GetSupportingEvidenceResponse }
     * 
     */
    public static GetSupportingEvidenceResponse
            createGetSupportingEvidenceResponse() {
        return new GetSupportingEvidenceResponse();
    }

    /**
     * Create an instance of {@link UnionKamsResponse }
     * 
     */
    public static UnionKamsResponse createUnionKamsResponse() {
        return new UnionKamsResponse();
    }

    /**
     * Create an instance of {@link KamHandle }
     * 
     */
    public static KamHandle createKamHandle() {
        return new KamHandle();
    }

    /**
     * Create an instance of {@link GetSupportingTermsRequest }
     * 
     */
    public static GetSupportingTermsRequest createGetSupportingTermsRequest() {
        return new GetSupportingTermsRequest();
    }

    /**
     * Create an instance of {@link FindKamNodesByLabelsRequest }
     * 
     */
    public static FindKamNodesByLabelsRequest
            createFindKamNodesByLabelsRequest() {
        return new FindKamNodesByLabelsRequest();
    }

    /**
     * Create an instance of {@link GetCitationsResponse }
     * 
     */
    public static GetCitationsResponse createGetCitationsResponse() {
        return new GetCitationsResponse();
    }

    /**
     * Create an instance of {@link MapDataRequest }
     * 
     * @deprecated use {@link #createFindKamNodesByNamespaceValuesRequest()}
     */
    @Deprecated
    public static MapDataRequest createMapDataRequest() {
        return new MapDataRequest();
    }

    /**
     * Create an instance of {@link ReleaseDialectResponse }
     * 
     */
    public static ReleaseDialectResponse createReleaseDialectResponse() {
        return new ReleaseDialectResponse();
    }

    /**
     * Create an instance of {@link FindKamNodesByIdsResponse }
     * 
     */
    public static FindKamNodesByIdsResponse createFindKamNodesByIdsResponse() {
        return new FindKamNodesByIdsResponse();
    }

    /**
     * Create an instance of {@link FindNamespaceEquivalenceResponse }
     * 
     */
    public static FindNamespaceEquivalenceResponse
            createFindNamespaceEquivalenceResponse() {
        return new FindNamespaceEquivalenceResponse();
    }

    /**
     * Create an instance of {@link NamespaceFilterCriteria }
     * 
     */
    public static NamespaceFilterCriteria createNamespaceFilterCriteria() {
        return new NamespaceFilterCriteria();
    }

    /**
     * Create an instance of {@link Namespace }
     * 
     */
    public static Namespace createNamespace() {
        return new Namespace();
    }

    /**
     * Create an instance of {@link GetDefaultDialectResponse }
     * 
     */
    public static GetDefaultDialectResponse createGetDefaultDialectResponse() {
        return new GetDefaultDialectResponse();
    }

    /**
     * Create an instance of {@link BelStatement }
     * 
     */
    public static BelStatement createBelStatement() {
        return new BelStatement();
    }

    /**
     * Create an instance of {@link FindNamespaceValuesRequest }
     * 
     */
    public static FindNamespaceValuesRequest createFindNamespaceValuesRequest() {
        return new FindNamespaceValuesRequest();
    }

    /**
     * Create an instance of {@link GetAdjacentKamEdgesRequest }
     * 
     */
    public static GetAdjacentKamEdgesRequest createGetAdjacentKamEdgesRequest() {
        return new GetAdjacentKamEdgesRequest();
    }

    /**
     * Create an instance of {@link KamEdge }
     * 
     */
    public static KamEdge createKamEdge() {
        return new KamEdge();
    }

    /**
     * Create an instance of {@link ResolveNodesRequest }
     * 
     */
    public static ResolveNodesRequest createResolveNodesRequest() {
        return new ResolveNodesRequest();
    }

    /**
     * Create an instance of {@link RelationshipTypeFilterCriteria }
     * 
     */
    public static RelationshipTypeFilterCriteria
            createRelationshipTypeFilterCriteria() {
        return new RelationshipTypeFilterCriteria();
    }

    /**
     * Create an instance of {@link GetNewInstanceRequest }
     * 
     */
    public static GetNewInstanceRequest createGetNewInstanceRequest() {
        return new GetNewInstanceRequest();
    }

    /**
     * Create an instance of {@link FilterCriteria }
     * 
     */
    public static FilterCriteria createFilterCriteria() {
        return new FilterCriteria();
    }

    /**
     * Create an instance of {@link ResolveEdgesResponse }
     * 
     */
    public static ResolveEdgesResponse createResolveEdgesResponse() {
        return new ResolveEdgesResponse();
    }

    /**
     * Create an instance of {@link FindNamespaceValuesResponse }
     * 
     */
    public static FindNamespaceValuesResponse
            createFindNamespaceValuesResponse() {
        return new FindNamespaceValuesResponse();
    }

    /**
     * Create an instance of {@link FindKamNodesByNamespaceValuesRequest }
     * 
     */
    public static FindKamNodesByNamespaceValuesRequest
            createFindKamNodesByNamespaceValuesRequest() {
        return new FindKamNodesByNamespaceValuesRequest();
    }

    /**
     * Create an instance of {@link SimplePath }
     * 
     */
    public static SimplePath createSimplePath() {
        return new SimplePath();
    }

    /**
     * Create an instance of {@link GetAnnotationTypesRequest }
     * 
     */
    public static GetAnnotationTypesRequest createGetAnnotationTypesRequest() {
        return new GetAnnotationTypesRequest();
    }

    /**
     * Create an instance of {@link KamNode }
     * 
     */
    public static KamNode createKamNode() {
        return new KamNode();
    }

    /**
     * Create an instance of {@link GetKamRequest }
     * 
     */
    public static GetKamRequest createGetKamRequest() {
        return new GetKamRequest();
    }

    /**
     * Create an instance of {@link BelTerm }
     * 
     */
    public static BelTerm createBelTerm() {
        return new BelTerm();
    }

    /**
     * Create an instance of {@link Annotation }
     * 
     */
    public static Annotation createAnnotation() {
        return new Annotation();
    }

    /**
     * Create an instance of {@link NodeFilter }
     * 
     */
    public static NodeFilter createNodeFilter() {
        return new NodeFilter();
    }

    /**
     * Create an instance of {@link GetCustomDialectRequest }
     * 
     */
    public static GetCustomDialectRequest createGetCustomDialectRequest() {
        return new GetCustomDialectRequest();
    }

    /**
     * Create an instance of {@link Kam }
     * 
     */
    public static Kam createKam() {
        return new Kam();
    }

    /**
     * Create an instance of {@link FindNamespaceEquivalenceRequest }
     * 
     */
    public static FindNamespaceEquivalenceRequest
            createFindNamespaceEquivalenceRequest() {
        return new FindNamespaceEquivalenceRequest();
    }

    /**
     * Create an instance of {@link GetAdjacentKamNodesResponse }
     * 
     */
    public static GetAdjacentKamNodesResponse
            createGetAdjacentKamNodesResponse() {
        return new GetAdjacentKamNodesResponse();
    }

    /**
     * Create an instance of {@link ScanResponse }
     * 
     */
    public static ScanResponse createScanResponse() {
        return new ScanResponse();
    }

    /**
     * Create an instance of {@link GetAdjacentKamNodesRequest }
     * 
     */
    public static GetAdjacentKamNodesRequest createGetAdjacentKamNodesRequest() {
        return new GetAdjacentKamNodesRequest();
    }

    /**
     * Create an instance of {@link BelDocumentFilterCriteria }
     * 
     */
    public static BelDocumentFilterCriteria createBelDocumentFilterCriteria() {
        return new BelDocumentFilterCriteria();
    }

    /**
     * Create an instance of {@link MapDataResponse }
     * 
     * @deprecated use {@link #createFindKamNodesByNamespaceValuesResponse()}
     */
    @Deprecated
    public static MapDataResponse createMapDataResponse() {
        return new MapDataResponse();
    }

    /**
     * Create an instance of {@link ReleaseDialectRequest }
     * 
     */
    public static ReleaseDialectRequest createReleaseDialectRequest() {
        return new ReleaseDialectRequest();
    }

    /**
     * Create an instance of {@link GetBelDocumentsResponse }
     * 
     */
    public static GetBelDocumentsResponse createGetBelDocumentsResponse() {
        return new GetBelDocumentsResponse();
    }

    /**
     * Create an instance of {@link GetNamespacesRequest }
     * 
     */
    public static GetNamespacesRequest createGetNamespacesRequest() {
        return new GetNamespacesRequest();
    }

    /**
     * Create an instance of {@link FindPathsResponse }
     * 
     */
    public static FindPathsResponse createFindPathsResponse() {
        return new FindPathsResponse();
    }

    /**
     * Create an instance of {@link GetDefaultDialectRequest }
     * 
     */
    public static GetDefaultDialectRequest createGetDefaultDialectRequest() {
        return new GetDefaultDialectRequest();
    }

    /**
     * Create an instance of {@link GetNewInstanceResponse }
     * 
     */
    public static GetNewInstanceResponse createGetNewInstanceResponse() {
        return new GetNewInstanceResponse();
    }

    /**
     * Create an instance of {@link GetAllNamespacesResponse }
     * 
     */
    public static GetAllNamespacesResponse createGetAllNamespacesResponse() {
        return new GetAllNamespacesResponse();
    }

    /**
     * Create an instance of {@link InterconnectResponse }
     * 
     */
    public static InterconnectResponse createInterconnectResponse() {
        return new InterconnectResponse();
    }

    /**
     * Create an instance of {@link GetCatalogRequest }
     * 
     */
    public static GetCatalogRequest createGetCatalogRequest() {
        return new GetCatalogRequest();
    }

    /**
     * Create an instance of {@link FindEquivalencesRequest }
     * 
     */
    public static FindEquivalencesRequest createFindEquivalencesRequest() {
        return new FindEquivalencesRequest();
    }

    /**
     * Create an instance of {@link UnionKamsRequest }
     * 
     */
    public static UnionKamsRequest createUnionKamsRequest() {
        return new UnionKamsRequest();
    }

    /**
     * Create an instance of {@link FindPathsRequest }
     * 
     */
    public static FindPathsRequest createFindPathsRequest() {
        return new FindPathsRequest();
    }

    /**
     * Create an instance of {@link GetCitationsRequest }
     * 
     */
    public static GetCitationsRequest createGetCitationsRequest() {
        return new GetCitationsRequest();
    }

    /**
     * Create an instance of {@link ReleaseKamRequest }
     * 
     */
    public static ReleaseKamRequest createReleaseKamRequest() {
        return new ReleaseKamRequest();
    }

    /**
     * Create an instance of {@link ReleaseKamResponse }
     * 
     */
    public static ReleaseKamResponse createReleaseKamResponse() {
        return new ReleaseKamResponse();
    }

    /**
     * Create an instance of {@link KamFilter }
     * 
     */
    public static KamFilter createKamFilter() {
        return new KamFilter();
    }

    /**
     * Create an instance of {@link GetAnnotationTypesResponse }
     * 
     */
    public static GetAnnotationTypesResponse createGetAnnotationTypesResponse() {
        return new GetAnnotationTypesResponse();
    }

    /**
     * Create an instance of {@link EquivalenceId }
     * 
     */
    public static EquivalenceId createEquivalenceId() {
        return new EquivalenceId();
    }

    /**
     * Create an instance of {@link LoadKamRequest }
     * 
     */
    public static LoadKamRequest createLoadKamRequest() {
        return new LoadKamRequest();
    }

    /**
     * Create an instance of {@link FindKamNodesByNamespaceValuesResponse }
     * 
     */
    public static FindKamNodesByNamespaceValuesResponse
            createFindKamNodesByNamespaceValuesResponse() {
        return new FindKamNodesByNamespaceValuesResponse();
    }

    /**
     * Create an instance of {@link IntersectKamsResponse }
     * 
     */
    public static IntersectKamsResponse createIntersectKamsResponse() {
        return new IntersectKamsResponse();
    }

    /**
     * Create an instance of {@link EdgeFilter }
     * 
     */
    public static EdgeFilter createEdgeFilter() {
        return new EdgeFilter();
    }

    /**
     * Create an instance of {@link FindKamNodesByPatternsRequest }
     * 
     */
    public static FindKamNodesByPatternsRequest
            createFindKamNodesByPatternsRequest() {
        return new FindKamNodesByPatternsRequest();
    }

    /**
     * Create an instance of {@link ResolveEdgesRequest }
     * 
     */
    public static ResolveEdgesRequest createResolveEdgesRequest() {
        return new ResolveEdgesRequest();
    }

    /**
     * Create an instance of {@link Edge }
     * 
     */
    public static Edge createEdge() {
        return new Edge();
    }

    /**
     * Create an instance of {@link NamespaceDescriptor }
     * 
     */
    public static NamespaceDescriptor createNamespaceDescriptor() {
        return new NamespaceDescriptor();
    }

    /**
     * Create an instance of {@link NamespaceValue }
     * 
     */
    public static NamespaceValue createNamespaceValue() {
        return new NamespaceValue();
    }

    /**
     * Create an instance of {@link Node }
     * 
     */
    public static Node createNode() {
        return new Node();
    }

    /**
     * Create an instance of {@link FindKamEdgesRequest }
     * 
     */
    public static FindKamEdgesRequest createFindKamEdgesRequest() {
        return new FindKamEdgesRequest();
    }

    /**
     * Create an instance of {@link AnnotationType }
     * 
     */
    public static AnnotationType createAnnotationType() {
        return new AnnotationType();
    }

    /**
     * Create an instance of {@link DifferenceKamsRequest }
     * 
     */
    public static DifferenceKamsRequest createDifferenceKamsRequest() {
        return new DifferenceKamsRequest();
    }

    /**
     * Create an instance of {@link GetAdjacentKamEdgesResponse }
     * 
     */
    public static GetAdjacentKamEdgesResponse
            createGetAdjacentKamEdgesResponse() {
        return new GetAdjacentKamEdgesResponse();
    }

    /**
     * Create an instance of {@link FunctionReturnTypeFilterCriteria }
     * 
     */
    public static FunctionReturnTypeFilterCriteria
            createFunctionReturnTypeFilterCriteria() {
        return new FunctionReturnTypeFilterCriteria();
    }

    /**
     * Create an instance of {@link GetKamResponse }
     * 
     */
    public static GetKamResponse createGetKamResponse() {
        return new GetKamResponse();
    }

    /**
     * Create an instance of {@link GetCatalogResponse }
     * 
     */
    public static GetCatalogResponse createGetCatalogResponse() {
        return new GetCatalogResponse();
    }

    /**
     * Create an instance of {@link DifferenceKamsResponse }
     * 
     */
    public static DifferenceKamsResponse createDifferenceKamsResponse() {
        return new DifferenceKamsResponse();
    }

    /**
     * Create an instance of {@link Citation }
     * 
     */
    public static Citation createCitation() {
        return new Citation();
    }

    /**
     * Create an instance of {@link GetAllNamespacesRequest }
     * 
     */
    public static GetAllNamespacesRequest createGetAllNamespacesRequest() {
        return new GetAllNamespacesRequest();
    }

    /**
     * Create an instance of {@link ScanRequest }
     * 
     */
    public static ScanRequest createScanRequest() {
        return new ScanRequest();
    }

    /**
     * Create an instance of {@link GetBelDocumentsRequest }
     * 
     */
    public static GetBelDocumentsRequest createGetBelDocumentsRequest() {
        return new GetBelDocumentsRequest();
    }

    /**
     * Create an instance of {@link GetCustomDialectResponse }
     * 
     */
    public static GetCustomDialectResponse createGetCustomDialectResponse() {
        return new GetCustomDialectResponse();
    }

    /**
     * Create an instance of {@link GetSupportingTermsResponse }
     * 
     */
    public static GetSupportingTermsResponse createGetSupportingTermsResponse() {
        return new GetSupportingTermsResponse();
    }

    /**
     * Create an instance of {@link FindKamNodesByIdsRequest }
     * 
     */
    public static FindKamNodesByIdsRequest createFindKamNodesByIdsRequest() {
        return new FindKamNodesByIdsRequest();
    }

    /**
     * Create an instance of {@link IntersectKamsRequest }
     * 
     */
    public static IntersectKamsRequest createIntersectKamsRequest() {
        return new IntersectKamsRequest();
    }

    /**
     * Create an instance of {@link BelDocument }
     * 
     */
    public static BelDocument createBelDocument() {
        return new BelDocument();
    }

    /**
     * Create an instance of {@link InterconnectRequest }
     * 
     */
    public static InterconnectRequest createInterconnectRequest() {
        return new InterconnectRequest();
    }

    /**
     * Create an instance of {@link GetNamespacesResponse }
     * 
     */
    public static GetNamespacesResponse createGetNamespacesResponse() {
        return new GetNamespacesResponse();
    }

    /**
     * Create an instance of {@link ResolveNodesResponse }
     * 
     */
    public static ResolveNodesResponse createResolveNodesResponse() {
        return new ResolveNodesResponse();
    }

    /**
     * Create an instance of {@link FindKamNodesByLabelsResponse }
     * 
     */
    public static FindKamNodesByLabelsResponse
            createFindKamNodesByLabelsResponse() {
        return new FindKamNodesByLabelsResponse();
    }

    /**
     * Create an instance of {@link FunctionTypeFilterCriteria }
     * 
     */
    public static FunctionTypeFilterCriteria createFunctionTypeFilterCriteria() {
        return new FunctionTypeFilterCriteria();
    }

    /**
     * Create an instance of {@link FindKamNodesByPatternsResponse }
     * 
     */
    public static FindKamNodesByPatternsResponse
            createFindKamNodesByPatternsResponse() {
        return new FindKamNodesByPatternsResponse();
    }

    /**
     * Create an instance of {@link AnnotationFilterCriteria }
     * 
     */
    public static AnnotationFilterCriteria createAnnotationFilterCriteria() {
        return new AnnotationFilterCriteria();
    }

    /**
     * Create an instance of {@link FindKamEdgesResponse }
     * 
     */
    public static FindKamEdgesResponse createFindKamEdgesResponse() {
        return new FindKamEdgesResponse();
    }

    /**
     * Create an instance of {@link CitationFilterCriteria }
     * 
     */
    public static CitationFilterCriteria createCitationFilterCriteria() {
        return new CitationFilterCriteria();
    }

    /**
     * Create an instance of {@link GetSupportingEvidenceRequest }
     * 
     */
    public static GetSupportingEvidenceRequest
            createGetSupportingEvidenceRequest() {
        return new GetSupportingEvidenceRequest();
    }

    /**
     * Create an instance of {@link LoadKamResponse }
     * 
     */
    public static LoadKamResponse createLoadKamResponse() {
        return new LoadKamResponse();
    }

    /**
     * Create an instance of {@link DialectHandle }
     * 
     */
    public static DialectHandle createDialectHandle() {
        return new DialectHandle();
    }

    /**
     * Create an instance of {@link GetBELFrameworkVersionResponse }
     * 
     */
    public static GetBELFrameworkVersionResponse
            createGetBELFrameworkVersionResponse() {
        return new GetBELFrameworkVersionResponse();
    }

    /**
     * Create an instance of {@link FindEquivalencesResponse }
     * 
     */
    public static FindEquivalencesResponse createFindEquivalencesResponse() {
        return new FindEquivalencesResponse();
    }
}
