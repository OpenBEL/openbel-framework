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
package org.openbel.framework.core.df.external;

import org.openbel.framework.common.AnnotationDefinitionResolutionException;
import org.openbel.framework.common.model.AnnotationDefinition;

/**
 * CacheableAnnotationDefinitionService defines a service to resolve BEL
 * annotation files and cache the data for subsequent calls.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface CacheableAnnotationDefinitionService {

    /**
     * Resolve the BEL annotation file an cache the data for subsequent calls.
     *
     * @param resourceLocation {@link String}, the resource location, which
     * cannot be null
     * @return {@link AnnotationDefinition} the annotation definition populated
     * from the BEL annotation <tt>resourceLocation</tt>
     * @throws AnnotationDefinitionResolutionException Thrown if an error
     * occurred while resolve an annotation resource
     */
    public abstract AnnotationDefinition resolveAnnotationDefinition(
            String resourceLocation)
            throws AnnotationDefinitionResolutionException;
}
