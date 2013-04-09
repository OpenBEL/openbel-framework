/**
 * Copyright (C) 2012-2013 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
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
