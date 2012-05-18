package org.openbel.framework.test;

import static org.openbel.framework.ws.client.ObjectFactory.createFindKamEdgesRequest;
import static org.openbel.framework.ws.client.ObjectFactory.createFindKamNodesByPatternsRequest;
import static org.openbel.framework.ws.client.ObjectFactory.createLoadKamRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import org.openbel.framework.ws.client.EdgeFilter;
import org.openbel.framework.ws.client.FindKamEdgesRequest;
import org.openbel.framework.ws.client.FindKamEdgesResponse;
import org.openbel.framework.ws.client.FindKamNodesByPatternsRequest;
import org.openbel.framework.ws.client.FindKamNodesByPatternsResponse;
import org.openbel.framework.ws.client.GetCatalogResponse;
import org.openbel.framework.ws.client.KAMLoadStatus;
import org.openbel.framework.ws.client.Kam;
import org.openbel.framework.ws.client.KamEdge;
import org.openbel.framework.ws.client.KamHandle;
import org.openbel.framework.ws.client.KamNode;
import org.openbel.framework.ws.client.LoadKamRequest;
import org.openbel.framework.ws.client.LoadKamResponse;
import org.openbel.framework.ws.client.RelationshipType;
import org.openbel.framework.ws.client.RelationshipTypeFilterCriteria;
import org.openbel.framework.ws.client.WebAPI;
import org.openbel.framework.ws.client.WebAPIService;

public class BELCompilerIT {
    private static WebAPI api;
    private static Map<String, Kam> name2Kams;
    private static EdgeFilter allEdges;
    static {
        // create edge filter for all rtypes
        allEdges = new EdgeFilter();
        final RelationshipTypeFilterCriteria rcrit =
                new RelationshipTypeFilterCriteria();
        rcrit.getValueSet().addAll(Arrays.asList(RelationshipType.values()));
        allEdges.getRelationshipCriteria().add(rcrit);
    }

    @BeforeClass
    public static void establishWebAPI() {
        api = new WebAPIService().getWebAPISoap11();
        assertThat(api, is(not(nullValue())));
        final GetCatalogResponse catres = api.getCatalog(null);
        assertThat(catres, is(not(nullValue())));

        final List<Kam> catalog = catres.getKams();
        assertThat(catalog, is(not(nullValue())));
        assertThat(catalog.isEmpty(), is(false));

        name2Kams = new HashMap<String, Kam>(catalog.size());
        for (final Kam kam : catalog) {
            name2Kams.put(kam.getName(), kam);
        }
    }

    @Test
    public void testUnitTest1() {
        runKamTest("test1a", 6, 5);
        runKamTest("test1b", 10, 10);
        runKamTest("test1c", 2, 1);
    }

    @Test
    public void testUnitTest2() {
        runKamTest("test2a", 13, 12);
        runKamTest("test2b", 4, 3);
    }

    @Test
    public void testUnitTest3() {
        runKamTest("test3a", 19, 18);
    }

    @Test
    public void testUnitTest4() {
        runKamTest("test4a", 34, 32);
        runKamTest("test4b", 14, 12);
    }

    @Test
    public void testUnitTest5() {
        runKamTest("test5a", 4, 4);
    }

    @Test
    public void testUnitTest6() {
        runKamTest("test6a", 5, 4);
    }

    @Test
    public void testUnitTest7() {
        runKamTest("test7a", 13, 12);
    }

    @Test
    public void testUnitTest8() {
        runKamTest("test8a", 14, 15);
    }

    @Test
    public void testUnitTest9() {
        runKamTest("test9a", 18, 16);
    }

    @Test
    public void testUnitTest10() {
        runKamTest("test10a", 25, 24);
    }

    @Test
    public void testUnitTest12() {
        runKamTest("test12", 12, 12);
    }

    @Test
    public void testUnitTest13() {
        runKamTest("test13", 4, 4);
    }

    @Test
    public void testUnitTest14() {
        runKamTest("test14", 4, 4);
    }

    @Test
    public void testUnitTest15() {
        runKamTest("test15", 8, 6);
    }

    @Test
    public void testUnitTest16() {
        runKamTest("test16", 46, 46);
    }

    private void runKamTest(final String kamName, final int nodes,
            final int edges) {
        final Kam kam = name2Kams.get(kamName);
        assertThat(kam, is(not(nullValue())));

        final LoadKamRequest lkreq = createLoadKamRequest();
        lkreq.setKam(kam);
        LoadKamResponse lkres = api.loadKam(lkreq);
        KAMLoadStatus status = lkres.getLoadStatus();
        while (status == KAMLoadStatus.IN_PROCESS) {
            // sleep 1/2 a second and retry
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // do nothing
            }

            lkres = api.loadKam(lkreq);
            status = lkres.getLoadStatus();
        }

        final KamHandle handle = lkres.getHandle();
        assertThat(handle, is(not(nullValue())));

        final FindKamNodesByPatternsRequest fknreq =
                createFindKamNodesByPatternsRequest();
        fknreq.getPatterns().add(".*");
        fknreq.setHandle(handle);

        final FindKamNodesByPatternsResponse fknres = api
                .findKamNodesByPatterns(fknreq);
        final List<KamNode> kamNodes = fknres.getKamNodes();

        assertThat(kamNodes, is(not(nullValue())));
        assertThat(kamNodes.size(), is(nodes));

        final FindKamEdgesRequest fkereq = createFindKamEdgesRequest();
        fkereq.setFilter(allEdges);
        fkereq.setHandle(handle);

        final FindKamEdgesResponse fkeres = api.findKamEdges(fkereq);
        final List<KamEdge> kamEdges = fkeres.getKamEdges();

        assertThat(kamEdges, is(not(nullValue())));
        assertThat(kamEdges.size(), is(edges));
    }
}
