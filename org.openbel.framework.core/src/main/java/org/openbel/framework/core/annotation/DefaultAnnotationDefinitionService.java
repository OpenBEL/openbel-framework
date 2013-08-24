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
