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
package org.openbel.framework.core.namespace;

import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openbel.framework.common.SystemConfigurationFile;
import org.openbel.framework.common.cfg.SystemConfigurationBasedTest;
import org.openbel.framework.common.model.CommonModelFactory;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheLookupService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.namespace.DefaultNamespaceService;
import org.openbel.framework.core.namespace.NamespaceIndexerService;
import org.openbel.framework.core.namespace.NamespaceIndexerServiceImpl;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.namespace.NamespaceSyntaxWarning;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * NamespaceServiceTest provides a unit-test for the {@link NamespaceService}.
 * This goal of this unit-test is to validate the namespace operations with a
 * focus on concurrency.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
@SystemConfigurationFile(path = "src/test/resources/org/openbel/framework/core/namespace/belframework.cfg")
public class DefaultNamespaceServiceTest extends SystemConfigurationBasedTest {
    private NamespaceService namespaceService;

    @Before
    @Override
    public void setup() {
        super.setup();

        final CacheLookupService cacheLookup = new DefaultCacheLookupService();
        final NamespaceIndexerService nsindexer =
                new NamespaceIndexerServiceImpl();
        final CacheableResourceService cacheService =
                new DefaultCacheableResourceService();
        namespaceService = new DefaultNamespaceService(
                cacheService, cacheLookup, nsindexer);
    }

    @After
    @Override
    public void teardown() {
        super.teardown();
    }

    @Test
    @Ignore
    public void testVerifyParameter() {
        URL nsresource = getClass().getResource(
                "/org/openbel/framework/core/namespace/test.belns");
        String nsFileUrl = nsresource.toString();

        if (nsFileUrl == null) {
            fail("Cannot read namespace location: " + nsFileUrl);
        }

        final Parameter p = CommonModelFactory.getInstance().createParameter(
                new Namespace("COMPLEX", nsFileUrl),
                "Propionyl Coa Carboxylase Complex");

        try {
            namespaceService.verify(p);
        } catch (NamespaceSyntaxWarning e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IndexingFailure e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (ResourceDownloadError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
