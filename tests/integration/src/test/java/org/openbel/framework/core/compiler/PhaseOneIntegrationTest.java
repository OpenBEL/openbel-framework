/**
 *  Copyright 2013 OpenBEL Consortium
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openbel.framework.core.compiler;

import static java.lang.System.err;
import static java.lang.System.out;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.cfg.SystemConfiguration.createSystemConfiguration;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.compiler.PhaseOneImpl;
import org.openbel.framework.compiler.DefaultPhaseOne;
import org.openbel.framework.compiler.DefaultPhaseOne.Stage1Output;
import org.openbel.framework.core.BELConverterService;
import org.openbel.framework.core.BELConverterServiceImpl;
import org.openbel.framework.core.BELValidatorService;
import org.openbel.framework.core.BELValidatorServiceImpl;
import org.openbel.framework.core.XBELConverterService;
import org.openbel.framework.core.XBELConverterServiceImpl;
import org.openbel.framework.core.XBELValidatorService;
import org.openbel.framework.core.XBELValidatorServiceImpl;
import org.openbel.framework.core.annotation.AnnotationDefinitionService;
import org.openbel.framework.core.annotation.AnnotationService;
import org.openbel.framework.core.annotation.DefaultAnnotationDefinitionService;
import org.openbel.framework.core.annotation.DefaultAnnotationService;
import org.openbel.framework.core.compiler.SemanticFailure;
import org.openbel.framework.core.compiler.SemanticService;
import org.openbel.framework.core.compiler.SemanticServiceImpl;
import org.openbel.framework.core.compiler.SymbolWarning;
import org.openbel.framework.core.compiler.expansion.ExpansionService;
import org.openbel.framework.core.compiler.expansion.ExpansionServiceImpl;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheLookupService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.namespace.DefaultNamespaceService;
import org.openbel.framework.core.namespace.NamespaceIndexerService;
import org.openbel.framework.core.namespace.NamespaceIndexerServiceImpl;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protonetwork.ProtoNetworkService;
import org.openbel.framework.core.protonetwork.ProtoNetworkServiceImpl;
import org.xml.sax.SAXException;

/**
 * PhaseOneIntegrationTest tests all XBEL files located in the
 * {@code org.openbel.framework.core.compiler} package. The test verifies that the
 * phase one {@link ProtoNetwork} is equivalent to the BEL {@link Document}
 * data.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class PhaseOneIntegrationTest {

    /**
     * Defines the {@link DefaultPhaseOne} bean to use for testing.
     */
    private DefaultPhaseOne phaseOne;

    /**
     * Set up each test.
     */
    @Before
    public void setup() {
        final String syscfg = "../../tools/dist/resources/config/belframework.cfg";
        final File exampleCfg = new File(syscfg);
        try {
            createSystemConfiguration(exampleCfg);
        } catch (IOException ioex) {
            ioex.printStackTrace();
            fail(ioex.getMessage());
        }

        final XBELValidatorService validator = createValidator();
        final XBELConverterService converter = createConverter();
        final BELValidatorService belValidator = new BELValidatorServiceImpl();
        final BELConverterService belConverter = new BELConverterServiceImpl();
        final NamespaceIndexerService nsindexer =
                new NamespaceIndexerServiceImpl();
        final CacheableResourceService cache =
                new DefaultCacheableResourceService();
        final CacheLookupService cacheLookup = new DefaultCacheLookupService();
        final NamespaceService nsService = new DefaultNamespaceService(
                cache, cacheLookup, nsindexer);
        final ProtoNetworkService protonetService =
                new ProtoNetworkServiceImpl();
        final SemanticService semantics = new SemanticServiceImpl(nsService);
        final ExpansionService expansion = new ExpansionServiceImpl();
        final AnnotationService annotationService =
                new DefaultAnnotationService();
        final AnnotationDefinitionService annotationDefinitionService =
                new DefaultAnnotationDefinitionService(cache, cacheLookup);

        phaseOne = new PhaseOneImpl(validator, converter,
                belValidator, belConverter, nsService, semantics,
                expansion, protonetService, annotationService,
                annotationDefinitionService);

        SimpleOutput sout = new SimpleOutput();
        sout.setOutputStream(out);
        sout.setErrorStream(err);
    }

    /**
     * Tear down each test.
     */
    @After
    public void teardown() {

    }

    /**
     * Run the phase one proto network test over XBEL files in the
     * docs module.
     */
    @Test
    public void testPhaseOneProtoNetworks() {
        File testAssetDirectory = new File("../../docs/xbel/examples/valid");

        // find xbel test files
        List<String> testAssets = Arrays.asList(testAssetDirectory
                .list(new XmlFileFilter()));

        for (String xbelFilePath : testAssets) {
            out.println("Building proto network for: " + xbelFilePath);

            File xbelFile = new File(asPath(
                    testAssetDirectory.getAbsolutePath(), xbelFilePath));

            try {
                Stage1Output s1o = phaseOne.stage1XBELValidation(xbelFile);
                phaseOne.stage2NamespaceCompilation(s1o.getDocument());
                phaseOne.stage3SymbolVerification(s1o.getDocument());
                phaseOne.stage4SemanticVerification(s1o.getDocument());
                ProtoNetwork pn = phaseOne.stage5Building(s1o.getDocument());
                phaseOne.stage6Expansion(s1o.getDocument(), pn, true);

                // assert proto network is valid.
                assertNotNull(pn);
            } catch (ResourceDownloadError e) {
                e.printStackTrace();
                fail("resource download error: " + e.getMessage());
            } catch (IndexingFailure e) {
                e.printStackTrace();
                fail("indexing failure: " + e.getMessage());
            } catch (SemanticFailure e) {
                e.printStackTrace();
                fail("semantic failure: " + e.getMessage());
            } catch (SymbolWarning e) {
                e.printStackTrace();
                fail("symbol warning exception: " + e.getMessage());
            }
        }
    }

    /**
     * {@link XmlFileFilter} that accepts xml files as long as they end
     * in a .xml extension.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    private static class XmlFileFilter implements FilenameFilter {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".xml");
        }
    }

    private XBELValidatorService createValidator() {
        try {
            return new XBELValidatorServiceImpl();
        } catch (SAXException e) {
            fail("SAX exception creating validator service");
        } catch (MalformedURLException e) {
            fail("Malformed URL exception creating validator service");
        } catch (IOException e) {
            fail("IO exception creating validator service");
        } catch (URISyntaxException e) {
            fail("URL syntax exception creating validator service");
        }
        return null;
    }

    private XBELConverterService createConverter() {
        try {
            return new XBELConverterServiceImpl();
        } catch (SAXException e) {
            fail("SAX exception creating converter service");
        } catch (MalformedURLException e) {
            fail("Malformed URL excpetion creating converter service");
        } catch (URISyntaxException e) {
            fail("URI Syntax excpetion creating converter service");
        } catch (IOException e) {
            fail("IO exception creating converter service");
        } catch (JAXBException e) {
            fail("JAXB exception creating converter service");
        }
        return null;
    }
}
