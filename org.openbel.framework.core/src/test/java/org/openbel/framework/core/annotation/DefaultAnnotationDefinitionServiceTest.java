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
package org.openbel.framework.core.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.AnnotationDefinitionResolutionException;
import org.openbel.framework.common.SystemConfigurationFile;
import org.openbel.framework.common.cfg.SystemConfigurationBasedTest;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.core.annotation.AnnotationDefinitionService;
import org.openbel.framework.core.annotation.DefaultAnnotationDefinitionService;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheLookupService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;

/**
 * DefaultAnnotationDefinitionServiceTest tests the
 * {@link DefaultAnnotationDefinitionService}'s ability to resolve and
 * parse .belanno files.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
@SystemConfigurationFile(path = "src/test/resources/org/openbel/framework/core/namespace/belframework.cfg")
public class DefaultAnnotationDefinitionServiceTest extends
        SystemConfigurationBasedTest {
    private AnnotationDefinitionService annotationDefService;

    @Before
    @Override
    public void setup() {
        super.setup();

        CacheableResourceService cacheService =
                new DefaultCacheableResourceService();
        CacheLookupService cacheLookup = new DefaultCacheLookupService();
        annotationDefService = new DefaultAnnotationDefinitionService(
                cacheService, cacheLookup);
    }

    @After
    @Override
    public void teardown() {
        super.teardown();
    }

    @Test
    public void testAnnotationResolution() {
        URL belannoUrl = getClass().getResource(
                "/org/openbel/framework/core/annotation/test.belanno");

        try {
            AnnotationDefinition anndef = annotationDefService
                    .resolveAnnotationDefinition(belannoUrl.toString());

            assertNotNull(anndef);
            assertNotNull(anndef.getEnums());
            assertFalse(anndef.getEnums().isEmpty());

            assertEquals("9606", anndef.getEnums().get(0));
            assertEquals("10090", anndef.getEnums().get(1));
            assertEquals("10116", anndef.getEnums().get(2));
        } catch (AnnotationDefinitionResolutionException e) {
            e.printStackTrace();
            fail(e.getUserFacingMessage());
        }
    }
}
