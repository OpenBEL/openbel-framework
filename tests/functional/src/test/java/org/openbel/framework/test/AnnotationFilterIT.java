package org.openbel.framework.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.openbel.framework.common.util.TestUtilities.randomElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.api.AnnotationFilterCriteria;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.KamStoreException;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.internal.KAMCatalogDao.AnnotationFilter;
import org.openbel.framework.internal.KAMStoreDaoImpl.Annotation;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelStatement;

/**
 * Test filtering of statements by annotation on the KAM Edge.  The test
 * assumes that the {@link Constants#SMALL_CORPUS_KAM_NAME} is present in the
 * KAM store.
 *
 * <p>
 * The {@link KamEdge edge} to be tested is retrieved at random.
 * </p>
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class AnnotationFilterIT extends KAMStoreTest {

    private KamEdge edge;
    private Annotation ann1;
    private Annotation ann2;

    @Before
    public void setup() throws KamStoreException {
        setupKamStore(Constants.SMALL_CORPUS_KAM_NAME);

        // find first edge with two statements having at least one annotation
        final Collection<KamEdge> edges = testKam.getEdges();
        List<BelStatement> bs = new ArrayList<BelStatement>();
        boolean hasAnnotations = false;
        while (!hasAnnotations) {
            edge = randomElement(edges);
            bs = ks.getSupportingEvidence(edge);
            if (bs.size() == 1) {
                hasAnnotations = bs.get(0).getAnnotationList().size() >= 2;
            }
        }

        ann1 = bs.get(0).getAnnotationList().get(0);
        ann2 = bs.get(0).getAnnotationList().get(1);
    }

    @After
    public void teardown() {
        teardownKamStore();
    }

    @Test
    public void includeOnlyFilter() {
        AnnotationFilter filter = testKam.getKamInfo().createAnnotationFilter();

        AnnotationFilterCriteria criteria = new AnnotationFilterCriteria(
                ann1.getAnnotationType());
        criteria.setInclude(true);
        criteria.getValues().add(ann1.getValue());
        filter.add(criteria);

        List<BelStatement> filteredStmts = null;
        try {
            filteredStmts = ks.getSupportingEvidence(edge, filter);
        } catch (InvalidArgument e) {
            e.printStackTrace();
        } catch (KamStoreException e) {
            e.printStackTrace();
        }

        assertThat(filteredStmts, is(notNullValue()));

        if (filteredStmts != null) {
            assertThat(filteredStmts.size(), is(1));
        }
    }

    @Test
    public void excludeOnlyFilter() {
        AnnotationFilter filter = testKam.getKamInfo().createAnnotationFilter();

        AnnotationFilterCriteria criteria = new AnnotationFilterCriteria(
                ann2.getAnnotationType());
        criteria.setInclude(false);
        criteria.getValues().add(ann2.getValue());
        filter.add(criteria);

        List<BelStatement> filteredStmts = null;
        try {
            filteredStmts = ks.getSupportingEvidence(edge, filter);
        } catch (InvalidArgument e) {
            e.printStackTrace();
        } catch (KamStoreException e) {
            e.printStackTrace();
        }

        assertThat(filteredStmts, is(notNullValue()));

        if (filteredStmts != null) {
            assertThat(filteredStmts.size(), is(0));
        }
    }

    @Test
    public void includeExcludeFilter() {
        AnnotationFilter filter = testKam.getKamInfo().createAnnotationFilter();

        AnnotationFilterCriteria criteria1 = new AnnotationFilterCriteria(ann1.getAnnotationType());
        criteria1.setInclude(true);
        criteria1.getValues().add(ann1.getValue());
        filter.add(criteria1);
        AnnotationFilterCriteria criteria2 = new AnnotationFilterCriteria(ann2.getAnnotationType());
        criteria2.setInclude(false);
        criteria2.getValues().add(ann2.getValue());
        filter.add(criteria2);

        List<BelStatement> filteredStmts = null;
        try {
            filteredStmts = ks.getSupportingEvidence(edge, filter);
        } catch (InvalidArgument e) {
            e.printStackTrace();
        } catch (KamStoreException e) {
            e.printStackTrace();
        }

        assertThat(filteredStmts, is(notNullValue()));

        if (filteredStmts != null) {
            assertThat(filteredStmts.size(), is(0));
        }
    }
}
