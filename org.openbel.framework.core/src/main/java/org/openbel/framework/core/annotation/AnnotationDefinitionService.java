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

import org.openbel.framework.common.AnnotationDefinitionResolutionException;
import org.openbel.framework.common.model.AnnotationDefinition;

/**
 * AnnotationDefinitionService defines a service to populate
 * an {@link AnnotationDefinition} from an external resource location.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface AnnotationDefinitionService {

    /**
     * Builds an {@link AnnotationDefinition} object resolved from an external
     * resource location defines in <tt>resourceLocation</tt>.
     *
     * @param resourceLocation {@link String}, the external resource location
     * of the annotation definition file, which cannot be null
     * @return {@link AnnotationDefinition} the annotation definition object
     * built from the external resource location
     * @throws AnnotationDefinitionResolutionException Thrown if an error
     * occurs resolving or building the {@link AnnotationDefinition}
     */
    public AnnotationDefinition resolveAnnotationDefinition(
            String resourceLocation)
            throws AnnotationDefinitionResolutionException;
}
