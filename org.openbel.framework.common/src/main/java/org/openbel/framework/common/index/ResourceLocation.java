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
package org.openbel.framework.common.index;

import org.openbel.framework.common.InvalidArgument;

/**
 * ResourceLocation encapsulates the {@link String} location of the
 * resource.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ResourceLocation {
    private final String resourceLocation;

    /**
     * Constructs the ResourceLocation object with the {@link String}
     * resource location.
     *
     * @param resourceLocation {@link String}, the resource location
     * @throws InvalidArgument Thrown if the <tt>resourceLocation</tt>,
     * {@link String} is null
     */
    protected ResourceLocation(String resourceLocation) {
        if (resourceLocation == null) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }

        this.resourceLocation = resourceLocation;
    }

    /**
     * Returns the {@link String} resource location.
     *
     * @return {@link String}, the resource location
     */
    public String getResourceLocation() {
        return resourceLocation;
    }
}
