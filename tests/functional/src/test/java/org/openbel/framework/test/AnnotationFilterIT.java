package org.openbel.framework.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.kamstore.data.jdbc.KAMCatalogDao.AnnotationFilter;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.AnnotationType;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.BelStatement;
import org.openbel.framework.core.kamstore.model.Kam.KamEdge;
import org.openbel.framework.core.kamstore.model.KamStoreException;
import org.openbel.framework.core.kamstore.model.filter.AnnotationFilterCriteria;

/**
 * Test filtering of statements by annotation on the KAM Edge:
 *
 * <p>
 * Small Corpus document
 * <br/>
 * id: <tt>{@value #TEST_EDGE_ID}</tt><br/>
 * edge label: <tt>proteinAbundance(78) actsIn gtpBoundActivity(proteinAbundance(78))</tt>
 * </p>
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class AnnotationFilterIT extends KAMStoreTest {
    private static final int TEST_EDGE_ID = 1973;

    @Before
    public void setup() {
        setupKamStore(Constants.SMALL_CORPUS_KAM_NAME);
    }

    @After
    public void teardown() {
        teardownKamStore();
    }

    @Test
    public void includeOnlyFilter() throws KamStoreException {
        AnnotationFilter filter = testKam.getKamInfo().createAnnotationFilter();

        AnnotationType cellLine = findAnnotationType("CellLine");
        if (cellLine == null) {
            fail("The CellLine annotation type is not in KAM.");
        }

        AnnotationFilterCriteria criteria = new AnnotationFilterCriteria(cellLine);
        criteria.setInclude(true);
        criteria.getValues().add("HeLa");
        filter.add(criteria);

        KamEdge testEdge = findEdge(TEST_EDGE_ID);
        if (testEdge == null) {
            fail("The KAMEdge with id " + TEST_EDGE_ID + " cannot be found in KAM.");
        }

        List<BelStatement> filteredStmts = null;
        try {
            filteredStmts = ks.getSupportingEvidence(testEdge, filter);
        } catch (InvalidArgument e) {
            e.printStackTrace();
        } catch (KamStoreException e) {
            e.printStackTrace();
        }

        assertThat(filteredStmts, is(notNullValue()));

        if (filteredStmts != null) {
            assertThat(filteredStmts.size(), is(2));
        }
    }

    @Test
    public void excludeOnlyFilter() {
        AnnotationFilter filter = testKam.getKamInfo().createAnnotationFilter();

        AnnotationType cellLine = findAnnotationType("CellLine");
        if (cellLine == null) {
            fail("The CellLine annotation type is not in KAM.");
        }

        AnnotationFilterCriteria criteria = new AnnotationFilterCriteria(cellLine);
        criteria.setInclude(false);
        criteria.getValues().add("HeLa");
        filter.add(criteria);

        KamEdge testEdge = findEdge(TEST_EDGE_ID);
        if (testEdge == null) {
            fail("The KAMEdge with id " + TEST_EDGE_ID + " cannot be found in KAM.");
        }

        List<BelStatement> filteredStmts = null;
        try {
            filteredStmts = ks.getSupportingEvidence(testEdge, filter);
        } catch (InvalidArgument e) {
            e.printStackTrace();
        } catch (KamStoreException e) {
            e.printStackTrace();
        }

        assertThat(filteredStmts, is(notNullValue()));

        if (filteredStmts != null) {
            assertThat(filteredStmts.size(), is(27));
        }
    }

    @Test
    public void includeExcludeFilter() {
        AnnotationFilter filter = testKam.getKamInfo().createAnnotationFilter();

        // Includes
        AnnotationType textLocation = findAnnotationType("TextLocation");
        if (textLocation == null) {
            fail("The TextLocation annotation type is not in KAM.");
        }
        AnnotationType cellLine = findAnnotationType("CellLine");
        if (cellLine == null) {
            fail("The TextLocation annotation type is not in KAM.");
        }
        AnnotationFilterCriteria criteria1 = new AnnotationFilterCriteria(textLocation);
        criteria1.setInclude(true);
        criteria1.getValues().add("Abstract");
        filter.add(criteria1);
        AnnotationFilterCriteria criteria2 = new AnnotationFilterCriteria(cellLine);
        criteria2.setInclude(true);
        criteria2.getValues().add("HeLa");
        filter.add(criteria2);

        // Exclude
        AnnotationType evidence = findAnnotationType("Evidence");
        if (evidence == null) {
            fail("The Evidence annotation type is not in KAM.");
        }

        AnnotationFilterCriteria criteria3 = new AnnotationFilterCriteria(evidence);
        criteria3.setInclude(false);
        criteria3.getValues().add("While the stimulation of Erk by alpha6beta4  was suppressed by dominant-negative Shc, Ras and RhoA, the activation of Jnk was inhibited by dominant-negative Ras and Rac1 and by the phosphoinositide 3-kinase inhibitor Wortmannin. ");
        filter.add(criteria3);

        KamEdge testEdge = findEdge(TEST_EDGE_ID);
        if (testEdge == null) {
            fail("The KAMEdge with id " + TEST_EDGE_ID + " cannot be found in KAM.");
        }

        List<BelStatement> filteredStmts = null;
        try {
            filteredStmts = ks.getSupportingEvidence(testEdge, filter);
        } catch (InvalidArgument e) {
            e.printStackTrace();
        } catch (KamStoreException e) {
            e.printStackTrace();
        }

        assertThat(filteredStmts, is(notNullValue()));

        if (filteredStmts != null) {
            assertThat(filteredStmts.size(), is(2));
        }
    }

    private KamEdge findEdge(final int edgeId) {
        KamEdge testEdge = null;
        Collection<KamEdge> edges = testKam.getEdges();
        for (KamEdge edge : edges) {
            if (edge.getId() == edgeId) {
                testEdge = edge;
            }
        }
        return testEdge;
    }

    private AnnotationType findAnnotationType(final String name) {
        Iterator<AnnotationType> types = null;
        try {
            types = ks.getAnnotationTypes(testKam).iterator();
        } catch (InvalidArgument e) {
            // stupid
        } catch (KamStoreException e) {
            e.printStackTrace();
            fail("Failed to obtain annotation types from KAMStore.");
        }

        if (types == null) {
            fail("KAM annotation types are null.");
            return null;
        }

        AnnotationType cellLine = null;
        while (types.hasNext()) {
            AnnotationType type = types.next();
            if (type.getName().equals(name)) {
                cellLine = type;
                break;
            }
        }
        return cellLine;
    }
}
