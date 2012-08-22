package org.openbel.framework.test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.openbel.framework.test.WebAPIHelper.createWebAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openbel.framework.ws.model.*;

/**
 * A JUnit test case to check that all BEL Framework Web API requests that take
 * a KAM handle must validate the handle, and, if the handle is invalid, must issue a
 * SOAP fault with a message containing the provided handle name.
 *
 * The requests that are checked are:
 * <ul>
 * <li>GetKam</li>
 * <li>GetBelDocuments</li>
 * <li>GetAnnotationTypes</li>
 * <li>GetNamespaces</li>
 * <li>GetCitations</li>
 * <li>GetNewInstance</li>
 * <li>MapData</li>
 * <li>UnionKams</li>
 * <li>IntersectKams</li>
 * <li>DifferenceKams</li>
 * <li>ResolveNodes</li>
 * <li>ResolveEdges</li>
 * <li>ReleaseKam</li>
 * <li>FindEdges</li>
 * <li>FindKamNodesByIds</li>
 * <li>FindKamNodesByLabels</li>
 * <li>FindKamNodesByPatterns</li>
 * </ul>
 */
public class TestEndPointKamHandleValidation {
    private final static ObjectFactory factory = new ObjectFactory();
    private static WebAPI webAPI = createWebAPI();
    private static final KamHandle invalidKamHandle = new KamHandle();
    private static final String invalidKamHandleName = "This is probably not a valid KAM handle!";
    static {
        invalidKamHandle.setHandle(invalidKamHandleName);
    }
    private static KamHandle validKamHandle = null;
    private static Namespace validNamespace = null;

    @BeforeClass
    public static void establishWebAPI() {
        assertThat(webAPI, is(not(nullValue())));

        // Acquire a valid KAM handle and a valid namespace which will
        // be used for some tests.
        final GetCatalogResponse catres = webAPI.getCatalog(null);
        assertThat(catres, is(not(nullValue())));
        final List<Kam> catalog = catres.getKams();
        if (hasItems(catalog)) {
            validKamHandle = loadKam(catalog.get(0));

            final GetNamespacesRequest nsRequest = factory.createGetNamespacesRequest();
            nsRequest.setHandle(validKamHandle);
            final GetNamespacesResponse nsResponse = webAPI.getNamespaces(nsRequest);
            if (nsResponse != null) {
                final List<Namespace> namespaces = nsResponse.getNamespaces();
                if (hasItems(namespaces)) {
                    validNamespace = namespaces.get(0);
                }
            }
        }
    }

    @Test
    public void testThatGetKamValidatesKamHandle() {
        check("getKam", factory.createGetKamRequest());
    }

    @Test
    public void testThatGetBelDocumentsValidatesKamHandle() {
        check("getBelDocuments", factory.createGetBelDocumentsRequest());
    }

    @Test
    public void testThatGetAnnotationTypesValidatesKamHandle() {
        check("getAnnotationTypes", factory.createGetAnnotationTypesRequest());
    }

    @Test
    public void testThatGetNamespacesValidatesKamHandle() {
        check("getNamespaces", factory.createGetNamespacesRequest());
    }

    @Test
    public void testThatGetCitationsValidatesKamHandle() {
        testThatGetCitationsValidatesKamHandle(CitationType.BOOK);
    }

    private void testThatGetCitationsValidatesKamHandle(final CitationType citationType) {
        final GetCitationsRequest request = factory.createGetCitationsRequest();
        request.setCitationType(citationType);
        check("getCitations", request);
    }

    @Test
    public void testThatGetNewInstanceValidatesKamHandle() {
        check("getNewInstance", factory.createGetNewInstanceRequest());
    }

    @Test
    public void testThatMapDataValidatesKamHandle() {
        if (validNamespace != null) {
            testThatMapDataValidatesKamHandle(validNamespace);
        }
    }

    private void testThatMapDataValidatesKamHandle(final Namespace namespace) {
        final MapDataRequest request = factory.createMapDataRequest();
        request.setNamespace(namespace);
        check("mapData", request);
    }

    @Test
    public void testThatUnionKamsValidatesKamHandle() {
        if (validKamHandle != null) {
            testThatUnionKamsValidatesKamHandle(validKamHandle);
        }
    }

    private void testThatUnionKamsValidatesKamHandle(final KamHandle validKamHandle) {
        final UnionKamsRequest request1 = factory.createUnionKamsRequest();
        request1.setKam2(validKamHandle);
        check("unionKams", request1, "setKam1");

        final UnionKamsRequest request2 = factory.createUnionKamsRequest();
        request2.setKam1(validKamHandle);
        check("unionKams", request2, "setKam2");
    }

    @Test
    public void testThatIntersectKamsValidatesKamHandle() {
        if (validKamHandle != null) {
            testThatIntersectKamsValidatesKamHandle(validKamHandle);
        }
    }

    private void testThatIntersectKamsValidatesKamHandle(final KamHandle validKamHandle) {
        final IntersectKamsRequest request1 = factory.createIntersectKamsRequest();
        request1.setKam2(validKamHandle);
        check("intersectKams", request1, "setKam1");

        final IntersectKamsRequest request2 = factory.createIntersectKamsRequest();
        request2.setKam1(validKamHandle);
        check("intersectKams", request2, "setKam2");
    }

    @Test
    public void testThatDifferenceKamsValidatesKamHandle() {
        if (validKamHandle != null) {
            testThatDifferenceKamsValidatesKamHandle(validKamHandle);
        }
    }

    private void testThatDifferenceKamsValidatesKamHandle(final KamHandle validKamHandle) {
        final DifferenceKamsRequest request1 = factory.createDifferenceKamsRequest();
        request1.setKam2(validKamHandle);
        check("differenceKams", request1, "setKam1");

        final DifferenceKamsRequest request2 = factory.createDifferenceKamsRequest();
        request2.setKam1(validKamHandle);
        check("differenceKams", request2, "setKam2");
    }

    @Test
    public void testThatResolveNodesValidatesKamHandle() {
        testThatResolveNodesValidatesKamHandle(new ArrayList<Node>());
    }

    private void testThatResolveNodesValidatesKamHandle(final List<Node> nodes) {
        final ResolveNodesRequest request = factory.createResolveNodesRequest();
        request.getNodes().addAll(nodes);
        check("resolveNodes", request);
    }

    @Test
    public void testThatResolveEdgesValidatesKamHandle() {
        testThatResolveEdgesValidatesKamHandle(new ArrayList<Edge>());
    }

    private void testThatResolveEdgesValidatesKamHandle(final List<Edge> edges) {
        final ResolveEdgesRequest request = factory.createResolveEdgesRequest();
        request.getEdges().addAll(edges);
        check("resolveEdges", request);
    }

    @Test
    public void testThatReleaseKamValidatesKamHandle() {
        check("releaseKam", factory.createReleaseKamRequest(), "setKam");
    }

    @Test
    public void testThatFindEdgesValidatesKamHandle() {
        testThatFindEdgesValidatesKamHandle(new EdgeFilter());
    }

    private void testThatFindEdgesValidatesKamHandle(final EdgeFilter edgeFilter) {
        final FindKamEdgesRequest request = factory.createFindKamEdgesRequest();
        request.setFilter(edgeFilter);
        check("findKamEdges", request);
    }

    @Test
    public void testThatFindKamNodesByIdsValidatesKamHandle() {
        testThatFindKamNodesByIdsValidatesKamHandle(
                asList(new String[] { "some id", "another id" }));
    }

    private void testThatFindKamNodesByIdsValidatesKamHandle(final List<String> ids) {
        final FindKamNodesByIdsRequest request = factory.createFindKamNodesByIdsRequest();
        request.getIds().addAll(ids);
        check("findKamNodesByIds", request);
    }

    @Test
    public void testThatFindKamNodesByLabelsValidatesKamHandle() {
        testThatFindKamNodesByLabelsValidatesKamHandle(
                asList(new String[] { "some label", "another label" }));
    }

    private void testThatFindKamNodesByLabelsValidatesKamHandle(final List<String> labels) {
        final FindKamNodesByLabelsRequest request = factory.createFindKamNodesByLabelsRequest();
        request.getLabels().addAll(labels);
        check("findKamNodesByLabels", request);
    }

    @Test
    public void testThatFindKamNodesByPatternsValidatesKamHandle() {
        testThatFindKamNodesByPatternsValidateKamHandle(asList(new String[] { ".*" }));
    }

    private void testThatFindKamNodesByPatternsValidateKamHandle(final List<String> patterns) {
        final FindKamNodesByPatternsRequest request = factory.createFindKamNodesByPatternsRequest();
        request.getPatterns().addAll(patterns);
        check("findKamNodesByPatterns", request);
    }


    /*
     * Utility functions:
     */

    private static <T> boolean hasItems(Collection<T> c) {
        return (c != null && ! c.isEmpty());
    }

    private static KamHandle loadKam(final Kam kam) {
        final LoadKamRequest lkreq = factory.createLoadKamRequest();
        lkreq.setKam(kam);
        LoadKamResponse lkres = webAPI.loadKam(lkreq);
        KAMLoadStatus status = lkres.getLoadStatus();
        while (status == KAMLoadStatus.IN_PROCESS) {
            // sleep 1/2 a second and retry
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // do nothing
            }

            lkres = webAPI.loadKam(lkreq);
            status = lkres.getLoadStatus();
        }

        final KamHandle handle = lkres.getHandle();
        assertThat(handle, is(not(nullValue())));
        return handle;
    }

    private <T> void check(final String apiMethodName, final T request) {
        check(apiMethodName, request, "setHandle");
    }

    private <T> void check(final String apiMethodName, final T request,
            final String setHandleMethodName) {

        Method apiMethod = null;
        Method setHandle = null;
        try {
            apiMethod = WebAPI.class.getDeclaredMethod(apiMethodName, request.getClass());
            setHandle = request.getClass().getDeclaredMethod(setHandleMethodName, KamHandle.class);
        } catch (NoSuchMethodException e) {
            fail("Caught NoSuchMethodException:  " + e.getMessage());
            return;
        } catch (SecurityException e) {
            fail("Caught SecurityException:  " + e.getMessage());
            return;
        }

        try {
            setHandle.invoke(request, invalidKamHandle);
            apiMethod.invoke(webAPI, request);
        } catch (IllegalAccessException e) {
            fail("Caught IllegalAccessException:  " + e.getMessage());
        } catch (IllegalArgumentException e) {
            fail("Caught IllegalArgumentException:  " + e.getMessage());
        } catch (InvocationTargetException e) {
            final Throwable cause = e.getCause();

            if (cause instanceof SOAPFaultException) {
                final SOAPFaultException ex = (SOAPFaultException) cause;

                final SOAPFault fault = ex.getFault();
                assertThat(fault, is(not(nullValue())));
                final String faultString = fault.getFaultString();
                assertThat(faultString, is(not(nullValue())));
                assertThat(faultString, containsString(invalidKamHandleName));

            } else {
                fail("Caught InvocationTargetException:  " + e.getMessage());
            }
        }
    }
}
