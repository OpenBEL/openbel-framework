package org.openbel.framework.test;

import static org.hamcrest.CoreMatchers.is;
import static org.openbel.framework.test.WebAPIHelper.createWebAPI;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openbel.framework.ws.model.BelStatement;
import org.openbel.framework.ws.model.EdgeFilter;
import org.openbel.framework.ws.model.FindKamEdgesRequest;
import org.openbel.framework.ws.model.FindKamEdgesResponse;
import org.openbel.framework.ws.model.FindKamNodesByPatternsRequest;
import org.openbel.framework.ws.model.FindKamNodesByPatternsResponse;
import org.openbel.framework.ws.model.GetCatalogResponse;
import org.openbel.framework.ws.model.GetSupportingEvidenceRequest;
import org.openbel.framework.ws.model.GetSupportingEvidenceResponse;
import org.openbel.framework.ws.model.KAMLoadStatus;
import org.openbel.framework.ws.model.Kam;
import org.openbel.framework.ws.model.KamEdge;
import org.openbel.framework.ws.model.KamHandle;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.LoadKamRequest;
import org.openbel.framework.ws.model.LoadKamResponse;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.model.RelationshipType;
import org.openbel.framework.ws.model.RelationshipTypeFilterCriteria;
import org.openbel.framework.ws.model.WebAPI;

public class BELCompilerIT {

    private static final ObjectFactory factory = new ObjectFactory();
    private static Map<String, Kam> name2Kams;
    private static EdgeFilter allEdges;
    private static WebAPI webAPI = createWebAPI();
    static {
        // factory.create edge filter for all rtypes
        allEdges = new EdgeFilter();
        final RelationshipTypeFilterCriteria rcrit =
                new RelationshipTypeFilterCriteria();
        rcrit.getValueSet().addAll(Arrays.asList(RelationshipType.values()));
        allEdges.getRelationshipCriteria().add(rcrit);
    }

    @BeforeClass
    public static void establishWebAPI() {
        assertThat(webAPI, is(not(nullValue())));

        final GetCatalogResponse catres = webAPI.getCatalog(null);
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
    public void unitTest1() {
        runKamTest("test1a", 6, 5);
        runKamTest("test1b", 10, 10);
        runKamTest("test1c", 2, 1);
    }

    @Test
    public void unitTest2() {
        runKamTest("test2a", 13, 12);
        runKamTest("test2b", 4, 3);
    }

    @Test
    public void unitTest3() {
        runKamTest("test3a", 19, 18);
    }

    @Test
    public void unitTest4() {
        runKamTest("test4a", 34, 32);
        runKamTest("test4b", 14, 12);
    }

    @Test
    public void unitTest5() {
        runKamTest("test5a", 4, 4);
    }

    @Test
    public void unitTest6() {
        runKamTest("test6a", 5, 4);
    }

    @Test
    public void unitTest7() {
        runKamTest("test7a", 16, 15);
    }

    @Test
    public void unitTest8() {
        runKamTest("test8a", 14, 15);
    }

    @Test
    public void unitTest9() {
        runKamTest("test9a", 18, 16);
    }

    @Test
    public void unitTest10() {
        runKamTest("test10a", 25, 24);
    }

    @Test
    public void unitTest12() {
        runKamTest("test12", 12, 12);
    }

    @Test
    public void unitTest13() {
        runKamTest("test13", 4, 4);
    }

    @Test
    public void unitTest14() {
        runKamTest("test14", 4, 4);
    }

    @Test
    public void unitTest15() {
        runKamTest("test15", 8, 6);
    }

    @Test
    public void unitTest16() {
        runKamTest("test16", 46, 46);
    }

    @Test
    public void unitTest17() {
        runKamTest("test17", 4, 3);
    }

    @Test
    public void unitTest18() {
        runKamTest("test18", 7, 6);

        // test 2 statements supporting the => edge
        List<KamEdge> edges = findEdge("test18",
                RelationshipType.DIRECTLY_INCREASES);
        assertThat(edges.size(), is(1));
        KamEdge edge = edges.iterator().next();
        validateStatementCount(edge, 2);
    }

    private void runKamTest(final String kamName, final int nodes,
            final int edges) {
        assertThat(kamName, is(not(nullValue())));

        final KamHandle handle = loadKam(kamName);

        final FindKamNodesByPatternsRequest fknreq =
                factory.createFindKamNodesByPatternsRequest();
        fknreq.getPatterns().add(".*");
        fknreq.setHandle(handle);

        final FindKamNodesByPatternsResponse fknres = webAPI
                .findKamNodesByPatterns(fknreq);
        final List<KamNode> kamNodes = fknres.getKamNodes();

        assertThat(kamNodes, is(not(nullValue())));
        assertThat(kamNodes.size(), is(nodes));

        final FindKamEdgesRequest fkereq = factory.createFindKamEdgesRequest();
        fkereq.setFilter(allEdges);
        fkereq.setHandle(handle);

        final FindKamEdgesResponse fkeres = webAPI.findKamEdges(fkereq);
        final List<KamEdge> kamEdges = fkeres.getKamEdges();

        assertThat(kamEdges, is(not(nullValue())));
        assertThat(kamEdges.size(), is(edges));
    }

    private void validateStatementCount(final KamEdge edge, final int sc) {
        assertThat(edge, is(not(nullValue())));

        GetSupportingEvidenceRequest evreq = factory
                .createGetSupportingEvidenceRequest();
        evreq.setKamEdge(edge);
        GetSupportingEvidenceResponse evres = webAPI.getSupportingEvidence(evreq);

        List<BelStatement> statements = evres.getStatements();
        assertThat(statements.size(), is(sc));
    }

    private List<KamEdge> findEdge(final String kamName,
            final RelationshipType r) {
        assertThat(kamName, is(not(nullValue())));
        assertThat(r, is(not(nullValue())));

        final KamHandle handle = loadKam(kamName);

        EdgeFilter f = factory.createEdgeFilter();
        RelationshipTypeFilterCriteria c = factory
                .createRelationshipTypeFilterCriteria();
        c.getValueSet().add(r);
        f.getRelationshipCriteria().add(c);

        final FindKamEdgesRequest ereq = factory.createFindKamEdgesRequest();
        ereq.setFilter(f);
        ereq.setHandle(handle);
        FindKamEdgesResponse eres = webAPI.findKamEdges(ereq);

        return eres.getKamEdges();
    }

    private KamHandle loadKam(final String kamName) {
        final Kam kam = name2Kams.get(kamName);
        assertThat(kam, is(not(nullValue())));

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
}
