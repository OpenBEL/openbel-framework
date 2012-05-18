package org.openbel.framework.test;

import static org.openbel.framework.common.cfg.SystemConfiguration.createSystemConfiguration;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.junit.BeforeClass;
import org.junit.Test;

import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.common.model.EquivalenceDataIndex;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.compiler.PhaseOneImpl;
import org.openbel.framework.compiler.DefaultPhaseTwo;
import org.openbel.framework.compiler.PhaseTwoImpl;
import org.openbel.framework.compiler.DefaultPhaseOne;
import org.openbel.framework.compiler.DefaultPhaseOne.Stage1Output;
import org.openbel.framework.core.BELConverterServiceImpl;
import org.openbel.framework.core.BELValidatorServiceImpl;
import org.openbel.framework.core.XBELConverterServiceImpl;
import org.openbel.framework.core.XBELValidatorServiceImpl;
import org.openbel.framework.core.annotation.DefaultAnnotationDefinitionService;
import org.openbel.framework.core.annotation.DefaultAnnotationService;
import org.openbel.framework.core.compiler.SemanticServiceImpl;
import org.openbel.framework.core.compiler.expansion.ExpansionServiceImpl;
import org.openbel.framework.core.equivalence.BucketEquivalencer;
import org.openbel.framework.core.equivalence.EquivalenceIndexerServiceImpl;
import org.openbel.framework.core.equivalence.EquivalenceMapResolutionFailure;
import org.openbel.framework.core.equivalence.StatementEquivalencer;
import org.openbel.framework.core.equivalence.TermEquivalencer;
import org.openbel.framework.core.namespace.DefaultNamespaceService;
import org.openbel.framework.core.namespace.NamespaceIndexerServiceImpl;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.protonetwork.ProtoNetworkBuilder;
import org.openbel.framework.core.protonetwork.ProtoNetworkServiceImpl;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheLookupService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * {@link EquivalenceIT} defines an integration test on the
 * {@link PhaseTwo phase two} equivalencing system.  The classes under test
 * are:
 * <ul>
 * <li>{@link BucketEquivalencer} for parameter equivalencing.</li>
 * <li>{@link TermEquivalencer} for term equivalencing.</li>
 * <li>{@link StatementEquivalencer} for statement equivalencing.</li>
 * </ul>
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class EquivalenceIT {
    private static final String DOC_PATH =
            "src/test/resources/org/openbel/framework/test/functional/kamstore/equivalence.bel";

    private static ProtoNetwork pn;

    /**
     * Sets up test environment:
     * <ul>
     * <li>Creates a {@link PhaseOneImpl phase one implementation} in order to
     * compile the test BEL file in {@link EquivalenceIT#DOC_PATH}.</li>
     * <li>Compiles test BEL file to a {@link ProtoNetwork pn} for
     * testing.</li>
     * <li>Sets up a {@link SystemConfiguration system config} from the target
     * BELFRAMEWORK_HOME.  This environment must be set for this test to
     * run.</li>
     * <li>Loads up the {@link ResourceIndex resource index} from the
     * {@link SystemConfiguration system config} settings.</li>
     * </ul>
     */
    @BeforeClass
    public static void createProtoNetwork() {
        final CacheableResourceService crs = new DefaultCacheableResourceService();
        final CacheLookupService cls = new DefaultCacheLookupService();
        final NamespaceService nss = new DefaultNamespaceService( crs, cls,
                new NamespaceIndexerServiceImpl());

        final DefaultPhaseOne p1;
        try {
            p1 = new PhaseOneImpl(
                    new XBELValidatorServiceImpl(),
                    new XBELConverterServiceImpl(),
                    new BELValidatorServiceImpl(),
                    new BELConverterServiceImpl(),
                    nss,
                    new SemanticServiceImpl(nss), new ExpansionServiceImpl(),
                    new ProtoNetworkServiceImpl(),
                    new DefaultAnnotationService(),
                    new DefaultAnnotationDefinitionService(crs, cls));
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to create Phase One due to exception");
            return;
        }

        // read in BEL document and compile to proto network
        final File doc = new File(DOC_PATH);
        assertThat("test BEL document cannot be read", doc.canRead(), is(true));
        Stage1Output p1s1 = p1.stage1BELValidation(doc);
        final ProtoNetworkBuilder bldr = new ProtoNetworkBuilder(p1s1.getDocument());
        pn = bldr.buildProtoNetwork();

        // Create system configuration, BELFRAMEWORK_HOME must be set.
        try {
            createSystemConfiguration(null);
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to load system configuration, set BELFRAMEWORK_HOME");
        }

        // Load resource index from system configuration settings.
        final CacheableResourceService cache = new DefaultCacheableResourceService();
        String resourceIndexURL = getSystemConfiguration().getResourceIndexURL();
        try {
            final ResolvedResource resource = cache.resolveResource(
                    ResourceType.RESOURCE_INDEX, resourceIndexURL);
            ResourceIndex.INSTANCE.loadIndex(resource.getCacheResourceCopy());
        } catch (ResourceDownloadError e) {
            fail("failed to load resource index, download error");
        } catch (FileNotFoundException e) {
            fail("failed to load resource index, not found");
        } catch (XMLStreamException e) {
            fail("failed to load resource index, xml error");
        }
    }

    /**
     * Test parameter, term, and statement equivalencing by:
     * <ol>
     * <li>Loading equivalences using {@link PhaseTwoImpl phase two}.</li>
     * <li>Running {@link BucketEquivalencer} for parameter equivalencing and
     * validating returning count.</li>
     * <li>Running {@link TermEquivalencer} for term equivalencing and
     * validating returning count.</li>
     * <li>Running {@link StatementEquivalencer} for statement equivalencing
     * and validating returning count.</li>
     * </ol>
     */
    @Test
    public void testEquivalence() {
        // load equivalences using PhaseTwo
        final DefaultPhaseTwo p2 = new PhaseTwoImpl(
                new DefaultCacheableResourceService(),
                new EquivalenceIndexerServiceImpl(),
                new ProtoNetworkServiceImpl());

        assertThat("test protonetwork is null", pn, notNullValue());

        final Set<EquivalenceDataIndex> equivs;
        try {
            equivs = p2.stage2LoadNamespaceEquivalences();
        } catch (EquivalenceMapResolutionFailure e) {
            e.printStackTrace();
            fail("encountered exception when loading equivalences");
            return;
        }

        assertThat("equivs are null", equivs, notNullValue());
        assertThat("equivs set is empty", equivs.size(), greaterThan(0));

        // test equivalencing
        try {
            // test parameter equivalencer
            int params = p2.stage3EquivalenceParameters(pn, equivs);
            assertThat("incorrect number of parmeter equivalences", params, is(9));

            // test term equivalencer
            int terms = p2.stage3EquivalenceTerms(pn);
            assertThat("incorrect number of term equivalences", terms, is(5));

            // test statement equivalencer
            int statements = p2.stage3EquivalenceStatements(pn);
            assertThat("incorrect number of statement equivalences", statements, is(3));
        } catch (IOException e) {
            e.printStackTrace();
            fail("encountered exception running parmaeter equivalencer");
        }
    }
}
