package org.openbel.framework.test;

import static org.openbel.framework.ws.client.ObjectFactory.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import org.openbel.framework.ws.client.*;

public class TestObjectIdRendering {

    private static final String SMALL_CORPUS_KAM_NAME = "small_corpus";
    private static final long POLL_INTERVAL_MILLISECONDS = 500;

    private static final long SEED = 71073753702L;
    private static final Random random = new Random(SEED);

    private static final int PREFIX_LENGTH = 1;
    private static final int EXPECTED_RENDERED_ID_LENGTH = 12;

    private static WebAPI api;

    @BeforeClass
    public static void establishWebApi() throws Exception {
        api = new WebAPIService().getWebAPISoap11();
        assertThat(api, is(not(nullValue())));
    }

    /**
     * Checks the IDs rendered for each type of web API object by the web API methods
     * LoadKam, GetKam, GetBelDocuments, GetAnnotationTypes, GetNamespaces,
     * FindNodesByPatterns, FindKamNodesByIds, FindKamNodesByLabels, GetAdjacentNodes,
     * GetAdjacentEdges, GetSupportingTerms, FindPaths, Interconnect, Scan, FindEdges,
     * and GetSupportingEvidence.
     *
     * This method checks that each object ID has a fixed length and that objects of the
     * same type have the same ID prefix.
     */
    @Test
    public void testThatObjectIdsAreRenderedCorrectly() {
        Map<Class<?>, String> prefixes = new HashMap<Class<?>, String>();

        // Find the KAM with the name "small_corpus".
        Kam kam = getKam(SMALL_CORPUS_KAM_NAME);

        checkId(prefixes, Kam.class, kam.getId());

        LoadKamRequest loadKamRequest = createLoadKamRequest();
        loadKamRequest.setKam(kam);
        LoadKamResponse loadKamResponse = api.loadKam(loadKamRequest);
        assertThat(loadKamResponse, is(not(nullValue())));
        KAMLoadStatus status = loadKamResponse.getLoadStatus();
        assertThat(status, is(not(nullValue())));
        while (status != KAMLoadStatus.COMPLETE) {
            if (status == KAMLoadStatus.FAILED) {
                return;
            }

            // Continue to poll after the time interval.
            try {
                Thread.sleep(POLL_INTERVAL_MILLISECONDS);
            } catch (InterruptedException ex) {
                // Do nothing.
            }

            loadKamResponse = api.loadKam(loadKamRequest);
            assertThat(loadKamResponse, is(not(nullValue())));
            status = loadKamResponse.getLoadStatus();
            assertThat(status, is(not(nullValue())));
        }

        final KamHandle kamHandle = loadKamResponse.getHandle();
        assertThat(kamHandle, is(not(nullValue())));

        try {
            // Test GetKam
            GetKamRequest getKamRequest = createGetKamRequest();
            getKamRequest.setHandle(kamHandle);
            GetKamResponse getKamResponse = api.getKam(getKamRequest);
            assertThat(getKamResponse, is(not(nullValue())));
            Kam k = getKamResponse.getKam();
            assertThat(k, is(not(nullValue())));
            checkId(prefixes, Kam.class, k.getId());

            // Test GetBelDocuments
            GetBelDocumentsRequest getBelDocumentsRequest = createGetBelDocumentsRequest();
            getBelDocumentsRequest.setHandle(kamHandle);
            GetBelDocumentsResponse getBelDocumentsResponse = api.getBelDocuments(getBelDocumentsRequest);
            assertThat(getBelDocumentsResponse, is(not(nullValue())));
            List<BelDocument> documents = getBelDocumentsResponse.getDocuments();
            assertThat(documents, is(not(nullValue())));
            assertNotEmpty(documents, "GetBelDocuments");
            for (BelDocument document : documents) {
                checkId(prefixes, BelDocument.class, document.getId());
            }

            // Test GetAnnotationTypes
            GetAnnotationTypesRequest getAnnotationTypesRequest = createGetAnnotationTypesRequest();
            getAnnotationTypesRequest.setHandle(kamHandle);
            GetAnnotationTypesResponse getAnnotationTypesResponse =
                    api.getAnnotationTypes(getAnnotationTypesRequest);
            assertThat(getAnnotationTypesResponse, is(not(nullValue())));
            List<AnnotationType> annotationTypes = getAnnotationTypesResponse.getAnnotationTypes();
            assertThat(annotationTypes, is(not(nullValue())));
            assertNotEmpty(annotationTypes, "GetAnnotationTypes");
            for (AnnotationType annotationType : annotationTypes) {
                checkId(prefixes, AnnotationType.class, annotationType.getId());
            }

            // Test GetNamespaces
            GetNamespacesRequest getNamespacesRequest = createGetNamespacesRequest();
            getNamespacesRequest.setHandle(kamHandle);
            GetNamespacesResponse getNamespacesResponse = api.getNamespaces(getNamespacesRequest);
            assertThat(getNamespacesResponse, is(not(nullValue())));
            List<Namespace> namespaces = getNamespacesResponse.getNamespaces();
            assertThat(namespaces, is(not(nullValue())));
            assertNotEmpty(namespaces, "GetNamespaces");
            for (Namespace namespace : namespaces) {
                checkId(prefixes, Namespace.class, namespace.getId());
            }

            // Test FindNodesByPatterns
            FindKamNodesByPatternsRequest findKamNodesByPatternsRequest =
                    createFindKamNodesByPatternsRequest();
            findKamNodesByPatternsRequest.setHandle(kamHandle);
            findKamNodesByPatternsRequest.getPatterns().add(".*");
            FindKamNodesByPatternsResponse findKamNodesByPatternsResponse =
                    api.findKamNodesByPatterns(findKamNodesByPatternsRequest);
            assertThat(findKamNodesByPatternsResponse, is(not(nullValue())));
            List<KamNode> kamNodes = findKamNodesByPatternsResponse.getKamNodes();
            assertThat(kamNodes, is(not(nullValue())));
            assertNotEmpty(kamNodes, "FindNodesByPatterns");
            for (KamNode kamNode : kamNodes) {
                checkId(prefixes, KamNode.class, kamNode.getId());
            }

            final KamNode kamNode0 = oneOf(kamNodes);

            // Test FindKamNodesByIds, FindKamNodesByLabels
            FindKamNodesByIdsRequest findKamNodesByIdsRequest =
                    createFindKamNodesByIdsRequest();
            findKamNodesByIdsRequest.setHandle(kamHandle);
            findKamNodesByIdsRequest.getIds().add(kamNode0.getId());
            FindKamNodesByIdsResponse findKamNodesByIdsResponse =
                    api.findKamNodesByIds(findKamNodesByIdsRequest);
            assertThat(findKamNodesByIdsResponse, is(not(nullValue())));
            List<KamNode> kamNodesById = findKamNodesByIdsResponse.getKamNodes();
            assertThat(kamNodesById, is(not(nullValue())));
            assertNotEmpty(kamNodesById, "FindKamNodesByIds");
            for (KamNode kamNode : kamNodesById) {
                checkId(prefixes, KamNode.class, kamNode.getId());
            }

            FindKamNodesByLabelsRequest findKamNodesByLabelsRequest =
                    createFindKamNodesByLabelsRequest();
            findKamNodesByLabelsRequest.setHandle(kamHandle);
            findKamNodesByLabelsRequest.getLabels().add(kamNode0.getLabel());
            FindKamNodesByLabelsResponse findKamNodesByLabelsResponse =
                    api.findKamNodesByLabels(findKamNodesByLabelsRequest);
            assertThat(findKamNodesByLabelsResponse, is(not(nullValue())));
            List<KamNode> kamNodesByLabels = findKamNodesByLabelsResponse.getKamNodes();
            assertThat(kamNodesByLabels, is(not(nullValue())));
            assertNotEmpty(kamNodesByLabels, "FindKamNodesByLabels");
            for (KamNode kamNode : kamNodesByLabels) {
                checkId(prefixes, KamNode.class, kamNode.getId());
            }


            // Test GetAdjacentNodes and GetAdjacentEdges
            GetAdjacentKamNodesRequest getAdjacentKamNodesRequest = createGetAdjacentKamNodesRequest();
            getAdjacentKamNodesRequest.setKamNode(kamNode0);
            GetAdjacentKamNodesResponse getAdjacentKamNodesResponse =
                    api.getAdjacentKamNodes(getAdjacentKamNodesRequest);
            assertThat(getAdjacentKamNodesResponse, is(not(nullValue())));
            List<KamNode> adjacentKamNodes = getAdjacentKamNodesResponse.getKamNodes();
            assertThat(adjacentKamNodes, is(not(nullValue())));
            assertNotEmpty(adjacentKamNodes, "GetAdjacentNodes");
            for (KamNode adjacentKamNode : adjacentKamNodes) {
                checkId(prefixes, KamNode.class, adjacentKamNode.getId());
            }

            GetAdjacentKamEdgesRequest getAdjacentKamEdgesRequest = createGetAdjacentKamEdgesRequest();
            getAdjacentKamEdgesRequest.setKamNode(kamNode0);
            GetAdjacentKamEdgesResponse getAdjacentKamEdgesResponse =
                    api.getAdjacentKamEdges(getAdjacentKamEdgesRequest);
            assertThat(getAdjacentKamEdgesResponse, is(not(nullValue())));
            List<KamEdge> adjacentKamEdges = getAdjacentKamEdgesResponse.getKamEdges();
            assertThat(adjacentKamEdges, is(not(nullValue())));
            assertNotEmpty(adjacentKamEdges, "GetAdjacentEdges");
            for (KamEdge adjacentKamEdge : adjacentKamEdges) {
                checkId(prefixes, KamEdge.class, adjacentKamEdge.getId());
            }


            // Test GetSupportingTerms
            GetSupportingTermsRequest getSupportingTermsRequest =
                    createGetSupportingTermsRequest();
            getSupportingTermsRequest.setKamNode(kamNode0);
            GetSupportingTermsResponse getSupportingTermsResponse =
                    api.getSupportingTerms(getSupportingTermsRequest);
            assertThat(getSupportingTermsResponse, is(not(nullValue())));
            List<BelTerm> terms = getSupportingTermsResponse.getTerms();
            assertThat(terms, is(not(nullValue())));
            assertNotEmpty(terms, "GetSupportingTerms");
            for (BelTerm term : terms) {
                checkId(prefixes, BelTerm.class, term.getId());
            }


            // Test FindPaths, Interconnect, and Scan
            final KamNode kamNode1 = oneOf(adjacentKamNodes);
            FindPathsRequest findPathsRequest = createFindPathsRequest();
            findPathsRequest.getSources().add(kamNode0);
            findPathsRequest.getTargets().add(kamNode1);
            FindPathsResponse findPathsResponse = api.findPaths(findPathsRequest);
            assertThat(findPathsResponse, is(not(nullValue())));
            List<SimplePath> paths = findPathsResponse.getPaths();
            assertThat(paths, is(not(nullValue())));
            assertNotEmpty(paths, "FindPaths");
            for (SimplePath path : paths) {
                checkPathIds(prefixes, path);
            }

            InterconnectRequest interconnectRequest = createInterconnectRequest();
            interconnectRequest.getSources().add(kamNode0);
            interconnectRequest.getSources().add(kamNode1);
            InterconnectResponse interconnectResponse = api.interconnect(interconnectRequest);
            assertThat(interconnectResponse, is(not(nullValue())));
            List<SimplePath> interconnectPaths = interconnectResponse.getPaths();
            assertThat(interconnectPaths, is(not(nullValue())));
            assertNotEmpty(interconnectPaths, "Interconnect");
            for (SimplePath path : interconnectPaths) {
                checkPathIds(prefixes, path);
            }

            ScanRequest scanRequest = createScanRequest();
            scanRequest.getSources().add(kamNode0);
            scanRequest.getSources().add(kamNode1);
            ScanResponse scanResponse = api.scan(scanRequest);
            assertThat(scanResponse, is(not(nullValue())));
            List<SimplePath> scanPaths = scanResponse.getPaths();
            assertThat(scanPaths, is(not(nullValue())));
            assertNotEmpty(scanPaths, "Scan");
            for (SimplePath path : scanPaths) {
                checkPathIds(prefixes, path);
            }


            // Test FindEdges
            FindKamEdgesRequest findKamEdgesRequest = createFindKamEdgesRequest();
            findKamEdgesRequest.setHandle(kamHandle);
            EdgeFilter edgeFilter = createEdgeFilter();
            RelationshipTypeFilterCriteria criterion = createRelationshipTypeFilterCriteria();
            criterion.setIsInclude(false);
            edgeFilter.getRelationshipCriteria().add(criterion);
            findKamEdgesRequest.setFilter(edgeFilter);
            FindKamEdgesResponse findKamEdgesResponse = api.findKamEdges(findKamEdgesRequest);
            assertThat(findKamEdgesResponse, is(not(nullValue())));
            List<KamEdge> kamEdges = findKamEdgesResponse.getKamEdges();
            assertThat(kamEdges, is(not(nullValue())));
            assertNotEmpty(kamEdges, "FindEdges");
            for (KamEdge kamEdge : kamEdges) {
                checkId(prefixes, KamEdge.class, kamEdge.getId());
            }

            final KamEdge kamEdge0 = oneOf(kamEdges);

            // Test GetSupportingEvidence
            GetSupportingEvidenceRequest getSupportingEvidenceRequest =
                    createGetSupportingEvidenceRequest();
            getSupportingEvidenceRequest.setKamEdge(kamEdge0);
            GetSupportingEvidenceResponse getSupportingEvidenceResponse =
                    api.getSupportingEvidence(getSupportingEvidenceRequest);
            assertThat(getSupportingEvidenceResponse, is(not(nullValue())));
            List<BelStatement> statements = getSupportingEvidenceResponse.getStatements();
            assertThat(statements, is(not(nullValue())));
            assertNotEmpty(statements, "GetSupportingEvidence");
            for (BelStatement statement : statements) {
                checkId(prefixes, BelStatement.class, statement.getId());
            }

        } finally {
            ReleaseKamRequest releaseKamRequest = createReleaseKamRequest();
            releaseKamRequest.setKam(kamHandle);
            ReleaseKamResponse releaseKamResponse = api.releaseKam(releaseKamRequest);
            assertThat(releaseKamResponse, is(not(nullValue())));
        }
    }

    private static Kam getKam(final String kamName) {
        final GetCatalogResponse catres = api.getCatalog(null);
        assertThat(catres, is(not(nullValue())));

        final List<Kam> catalog = catres.getKams();
        assertThat(catalog, is(not(nullValue())));

        Kam retKam = null;
        for (final Kam kam : catalog) {
            if (kamName.equals(kam.getName())) {
                retKam = kam;
                break;
            }
        }
        assertThat(retKam, is(not(nullValue())));
        return retKam;
    }

    private static <T> void assertNotEmpty(final List<T> list, final String methodName) {
        if (list.isEmpty()) {
            fail(String.format(
                    "The test case is incomplete because %s returned no IDs to check.",
                    methodName));
        }
    }

    private static void checkId(final Map<Class<?>, String> prefixes, final Class<?> type, final String id) {
        if (! prefixes.containsKey(type)) {
            prefixes.put(type, id.substring(0, PREFIX_LENGTH));
        } else {
            final String prefix = prefixes.get(type);
            if (! id.startsWith(prefix)) {
                fail(String.format(
                        "Object ID \"%s\" should have the prefix \"%s\" that corresponds to its " +
                        "type (%s).", id, prefix, type.getSimpleName()));
            } else if (! (id.length() == EXPECTED_RENDERED_ID_LENGTH)) {
                fail(String.format("Object ID \"%s\" does not have the expected length (%d).",
                        id, EXPECTED_RENDERED_ID_LENGTH));
            }
        }
    }

    private static void checkPathIds(final Map<Class<?>, String> prefixes, final SimplePath path) {
        KamNode source = path.getSource(), target = path.getTarget();
        List<KamEdge> edges = path.getEdges();
        assertThat(source, is(not(nullValue())));
        assertThat(target, is(not(nullValue())));
        assertThat(edges, is(not(nullValue())));

        checkId(prefixes, KamNode.class, source.getId());
        checkId(prefixes, KamNode.class, target.getId());
        for (KamEdge edge : edges) {
            checkId(prefixes, KamEdge.class, edge.getId());
        }
    }

    private static <T> T oneOf(List<T> list) {
        int n = list.size();
        if (n == 0) {
            return null;
        }
        return list.get(random.nextInt(n));
    }
}
