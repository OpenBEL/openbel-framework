/**
 * Copyright (C) 2012 Selventa, Inc.
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openbel.framework.common.AnnotationDefinitionResolutionException;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.external.CacheableAnnotationDefinitionService;
import org.openbel.framework.core.df.external.CacheableAnnotationDefinitionServiceImpl;

/**
 * DefaultAnnotationDefinitionService implements a service to populate an
 * {@link AnnotationDefinition} from an external resource location.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DefaultAnnotationDefinitionService implements
        AnnotationDefinitionService {

    private final CacheableAnnotationDefinitionService cacheableAnnotationDefinitionService;

    /**
     * Defines the map of parsed annotations. The entries are keyed by
     * annotation resource location with {@link AnnotationDefinition} as the
     * value.
     * 
     * <p>
     * This map in thread-safe as it uses {@link ConcurrentHashMap}.
     * </p>
     */
    private final Map<String, AnnotationDefinition> annotations =
            new ConcurrentHashMap<String, AnnotationDefinition>();

    public DefaultAnnotationDefinitionService(
            CacheableResourceService resourceCache,
            CacheLookupService cacheLookupService) {
        this.cacheableAnnotationDefinitionService =
                new CacheableAnnotationDefinitionServiceImpl(
                        resourceCache, cacheLookupService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationDefinition resolveAnnotationDefinition(
            String resourceLocation)
            throws AnnotationDefinitionResolutionException {

        synchronized (resourceLocation) {
            // return if cached
            if (annotations.containsKey(resourceLocation)) {
                return annotations.get(resourceLocation);
            }

            // resolve and parse
            AnnotationDefinition annotationDefinition =
                    cacheableAnnotationDefinitionService
                            .resolveAnnotationDefinition(resourceLocation);

            // cache annotation definition and return
            annotations.put(resourceLocation, annotationDefinition);
            return annotationDefinition;
        }
    }
}
