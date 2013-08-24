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

import static org.openbel.framework.common.BELUtilities.hasLength;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.compiler.ResourceSyntaxWarning;

/**
 * A BEL warning indicating a value was not found within the associated namespace location.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NamespaceSyntaxWarning extends ResourceSyntaxWarning {
    private static final long serialVersionUID = -7579321468657290594L;

    /**
     * Defines the prefix of the namespace associated with the lookup failure.
     */
    private String prefix;

    /**
     * Creates the {@link NamespaceSyntaxWarning} from a {@code resourceLocation} and and
     * {@code value}.
     *
     * @param resourceLocation {@link String}, the resource location, which
     * cannot be null
     * @param prefix {@link String}, the prefix, which may be null
     * @param value {@link String}, the value, which cannot be null
     * @throws InvalidArgument Thrown if {@code resourceLocation} or
     * {@code value} is null or empty
     */
    public NamespaceSyntaxWarning(String resourceLocation, String prefix,
            String value) {
        super(resourceLocation, value);
        this.prefix = prefix;
    }

    /**
     * Returns the {@code prefix}.
     *
     * @return {@link String}; may be null
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        String rl = getResourceLocation();
        String pref = getPrefix();
        String val = getValue();
        bldr.append("symbol ");
        bldr.append(val);
        bldr.append(" does not exist in ");
        if (hasLength(pref)) {
            bldr.append(pref);
        } else {
            bldr.append(rl);
        }
        return bldr.toString();
    }

}
