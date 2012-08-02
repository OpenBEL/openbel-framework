package org.openbel.framework.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.openbel.framework.ws.model.CitationType.*;
import static org.openbel.framework.ws.model.RelationshipType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openbel.framework.ws.model.*;

public class TestKamUtilsEndPoint {
    private final static ObjectFactory factory = new ObjectFactory();

    // This class is a JUnit test against com.seleventa.belframework.ws.endpoint.KamUtilsEndPoint
    // (which implements the LoadKam request offered by the web API).

    private static final String SMALL_CORPUS_KAM_NAME = "small_corpus";
    private static final long POLL_INTERVAL_MILLISECONDS = 500;

    private static final long SEED = 71073753702L;
    private static final Random random = new Random(SEED);
    private static boolean randomIsInclude = false;
    private static boolean defaultIsInclude = true;

    private static WebAPI api;
    private static Kam smallCorpus = null;
    private static List<BelDocument> belDocuments;
    private static List<AnnotationType> annotationTypes;
    private static List<Citation> citations;
    private static final List<RelationshipType> relationships;

    @BeforeClass
    public static void establishWebApi() {
        api = new WebAPIService().getWebAPISoap11();
        assertThat(api, is(not(nullValue())));

        final GetCatalogResponse catres = api.getCatalog(null);
        assertThat(catres, is(not(nullValue())));

        final List<Kam> catalog = catres.getKams();
        assertThat(catalog, is(not(nullValue())));
        assertThat(catalog.isEmpty(), is(false));

        // Find the KAM with the name "small_corpus".
        for (final Kam kam : catalog) {
            if (SMALL_CORPUS_KAM_NAME.equals(kam.getName())) {
                smallCorpus = kam;
                break;
            }
        }
        assertThat(smallCorpus, is(not(nullValue())));

        // Get the lists of BEL documents, annotation types, and citations
        // that will be needed for the tests.
        final KamHandle smallCorpusHandle = pollForKam(smallCorpus, null);
        belDocuments = getBelDocuments(smallCorpusHandle);
        annotationTypes = getAnnotationTypes(smallCorpusHandle);
        citations = getCitations(smallCorpusHandle);
        assertThat(4, is(belDocuments.size()));
        assertThat(21, is(annotationTypes.size()));
        assertThat(60, is(citations.size()));
    }

    @Test
    public void testLoadKamWithRandomFilter() {
        final KamFilter filter = factory.createKamFilter();
        filter.getAnnotationCriteria().add(getRandomAnnotationFilterCriteria());
        filter.getCitationCriteria().add(getRandomCitationFilterCriteria());
        filter.getDocumentCriteria().add(getRandomBelDocumentFilterCriteria());
        filter.getRelationshipCriteria().add(getRandomRelationshipTypeFilterCriteria());
        final KamHandle handle = pollForKam(smallCorpus, filter);
        releaseKam(handle);
    }

    @Test
    public void testLoadKamWithRandomAnnotationFilter() {

        final AnnotationFilterCriteria criteria = getRandomAnnotationFilterCriteria();
        final KamFilter filter = factory.createKamFilter();
        filter.getAnnotationCriteria().add(criteria);
        final KamHandle handle = pollForKam(smallCorpus, filter);
        releaseKam(handle);
    }

    @Test
    public void testLoadKamWithRandomCitationFilter() {

        final CitationFilterCriteria criteria = getRandomCitationFilterCriteria();
        final KamFilter filter = factory.createKamFilter();
        filter.getCitationCriteria().add(criteria);
        final KamHandle handle = pollForKam(smallCorpus, filter);
        releaseKam(handle);
    }

    @Test
    public void testLoadKamWithRandomDocumentFilter() {

        final BelDocumentFilterCriteria criteria = getRandomBelDocumentFilterCriteria();
        final KamFilter filter = factory.createKamFilter();
        filter.getDocumentCriteria().add(criteria);
        final KamHandle handle = pollForKam(smallCorpus, filter);
        releaseKam(handle);
    }

    @Test
    public void testLoadKamWithRandomRelationshipFilter() {

        RelationshipTypeFilterCriteria criteria = getRandomRelationshipTypeFilterCriteria();
        final KamFilter filter = factory.createKamFilter();
        filter.getRelationshipCriteria().add(criteria);
        final KamHandle handle = pollForKam(smallCorpus, filter);
        releaseKam(handle);
    }

    private static AnnotationFilterCriteria getRandomAnnotationFilterCriteria() {

        final AnnotationFilterCriteria criteria = factory.createAnnotationFilterCriteria();
        criteria.setAnnotationType(oneOf(annotationTypes));
        criteria.setIsInclude(randomIsInclude ? random.nextBoolean() : defaultIsInclude);
        criteria.getValueSet().addAll(Arrays.asList(new String[] { "example", "test", "sample" }));
        return criteria;
    }

    private static CitationFilterCriteria getRandomCitationFilterCriteria() {
        final CitationFilterCriteria criteria = factory.createCitationFilterCriteria();
        criteria.setIsInclude(randomIsInclude ? random.nextBoolean() : defaultIsInclude);
        criteria.getValueSet().add(oneOf(citations));
        return criteria;
    }

    private static BelDocumentFilterCriteria getRandomBelDocumentFilterCriteria() {
        final BelDocumentFilterCriteria criteria = factory.createBelDocumentFilterCriteria();
        criteria.setIsInclude(randomIsInclude ? random.nextBoolean() : defaultIsInclude);
        criteria.getValueSet().add(oneOf(belDocuments));
        return criteria;
    }

    private static RelationshipTypeFilterCriteria getRandomRelationshipTypeFilterCriteria() {
        final RelationshipTypeFilterCriteria criteria = factory.createRelationshipTypeFilterCriteria();
        criteria.setIsInclude(randomIsInclude ? random.nextBoolean() : defaultIsInclude);
        criteria.getValueSet().add(oneOf(relationships));
        return criteria;
    }

    private static <T> T oneOf(List<T> list) {
        int n = list.size();
        if (n == 0) {
            return null;
        }
        return list.get(random.nextInt(n));
    }

    private static KamHandle pollForKam(Kam kam, KamFilter kamFilter) {
        final LoadKamRequest loadKamRequest = factory.createLoadKamRequest();
        loadKamRequest.setKam(kam);
        loadKamRequest.setFilter(kamFilter);

        LoadKamResponse loadKamResponse = api.loadKam(loadKamRequest);
        assertThat(loadKamResponse, is(not(nullValue())));
        KAMLoadStatus status = loadKamResponse.getLoadStatus();
        assertThat(status, is(not(nullValue())));

        while (status != KAMLoadStatus.COMPLETE) {

            if (status == KAMLoadStatus.FAILED) {
                String msg = loadKamResponse.getMessage();
                fail("Received the KAM load status FAILED" + (msg != null ? ":  " + msg : "."));
            }

            assertThat(status, is(equalTo(KAMLoadStatus.IN_PROCESS)));

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

        final KamHandle handle = loadKamResponse.getHandle();
        assertThat(handle, is(not(nullValue())));
        return handle;
    }

    private static void releaseKam(KamHandle handle) {
        final ReleaseKamRequest request = factory.createReleaseKamRequest();
        request.setKam(handle);
        final ReleaseKamResponse response = api.releaseKam(request);
        assertThat(response, is(not(nullValue())));
    }

    private static List<BelDocument> getBelDocuments(KamHandle handle) {
        final GetBelDocumentsRequest request = factory.createGetBelDocumentsRequest();
        request.setHandle(handle);
        final GetBelDocumentsResponse response = api.getBelDocuments(request);
        assertThat(response, is(not(nullValue())));

        final List<BelDocument> documents = response.getDocuments();
        assertThat(documents, is(not(nullValue())));

        return documents;
    }

    private static List<AnnotationType> getAnnotationTypes(KamHandle handle) {
        final GetAnnotationTypesRequest request = factory.createGetAnnotationTypesRequest();
        request.setHandle(handle);
        final GetAnnotationTypesResponse response = api.getAnnotationTypes(request);
        assertThat(response, is(not(nullValue())));

        final List<AnnotationType> annotationTypes = response.getAnnotationTypes();
        assertThat(annotationTypes, is(not(nullValue())));

        return annotationTypes;
    }

    private static List<Citation> getCitations(KamHandle handle) {
        List<Citation> ret = new ArrayList<Citation>();
        final CitationType[] types = new CitationType[] { BOOK, JOURNAL, ONLINE_RESOURCE, PUBMED, OTHER };

        for (CitationType type : types) {
            ret.addAll(getCitations(handle, type));
        }

        return ret;
    }

    private static List<Citation> getCitations(KamHandle handle, CitationType citationType) {
        final GetCitationsRequest request = factory.createGetCitationsRequest();
        request.setHandle(handle);
        request.setCitationType(citationType);
        //request.setDocument(null);
        final GetCitationsResponse response = api.getCitations(request);
        assertThat(response, is(not(nullValue())));

        final List<Citation> citations = response.getCitations();
        assertThat(citations, is(not(nullValue())));

        return citations;
    }

    static {
        relationships = Arrays.asList(new RelationshipType[] {
            UNKNOWN,
            ACTS_IN,
            ANALOGOUS,
            ASSOCIATION,
            BIOMARKER_FOR,
            CAUSES_NO_CHANGE,
            DECREASES,
            DIRECTLY_DECREASES,
            DIRECTLY_INCREASES,
            HAS_COMPONENT,
            HAS_MEMBER,
            HAS_MODIFICATION,
            HAS_PRODUCT,
            HAS_VARIANT,
            INCLUDES,
            INCREASES,
            IS_A,
            NEGATIVE_CORRELATION,
            ORTHOLOGOUS,
            POSITIVE_CORRELATION,
            PROGNOSTIC_BIOMARKER_FOR,
            RATE_LIMITING_STEP_OF,
            REACTANT_IN,
            SUB_PROCESS_OF,
            TRANSCRIBED_TO,
            TRANSLATED_TO,
            TRANSLOCATES });
    }
}
