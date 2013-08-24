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

import static org.openbel.framework.common.BELUtilities.noLength;

import org.openbel.framework.common.BELWarningException;
import org.openbel.framework.common.InvalidArgument;

/**
 * A BEL Warning indicating there is a syntactical problem with a given value
 * defined for a given resource location.
 *
 * @author Steve Ungerer
 */
public abstract class ResourceSyntaxWarning extends BELWarningException {
    private static final long serialVersionUID = -4287281952118301027L;

    /**
     * Defines the resource location where the value is to be looked up from.
     */
    private final String resourceLocation;

    /**
     * Defines the value with which there was a warning.
     */
    private final String value;

    /**
     * Creates the {@link ResourceSyntaxWarning} for a given resourceLocation
     * and value.
     *
     * @param resourceLocation
     * @param value
     */
    public ResourceSyntaxWarning(String resourceLocation, String value) {
        super(resourceLocation, value);
        if (noLength(resourceLocation)) {
            throw new InvalidArgument("invalid resource location");
        }
        if (noLength(value)) {
            throw new InvalidArgument("invalid value");
        }
        this.resourceLocation = resourceLocation;
        this.value = value;
    }

    /**
     * Returns the {@code resourceLocation}.
     *
     * @return {@link String}, the resource location
     */
    public String getResourceLocation() {
        return resourceLocation;
    }

    /**
     * Returns the {@code value}.
     *
     * @return {@link String}, the value
     */
    public String getValue() {
        return value;
    }

}
