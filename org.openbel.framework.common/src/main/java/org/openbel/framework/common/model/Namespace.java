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
package org.openbel.framework.common.model;

import org.openbel.framework.common.InvalidArgument;

/**
 * Defines a namespace whose values may be used in statements.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Namespace implements BELObject {
    private static final long serialVersionUID = 1565288369675483169L;

    /** The prefix of the default namespace. */
    public static final String DEFAULT_NAMESPACE_PREFIX = "";

    private final String prefix;
    private final String resourceLocation;

    /**
     * Creates a namespace with the required properties.
     *
     * @param prefix Non-null prefix
     * @param resourceLocation Non-null resource location
     * @throws InvalidArgument Thrown if {@code prefix} or
     * {@code resourceLocation} is null
     */
    public Namespace(final String prefix, final String resourceLocation) {
        if (prefix == null) {
            throw new InvalidArgument("null prefix");
        } else if (resourceLocation == null) {
            throw new InvalidArgument("null resource location");
        }
        this.prefix = prefix;
        this.resourceLocation = resourceLocation;
    }

    /**
     * Returns the prefix uniquely identifying a namespace within a document.
     *
     * @return Non-null string
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Returns the URI of the resource location defining this namespace.
     *
     * @return Non-null string
     */
    public String getResourceLocation() {
        return resourceLocation;
    }

    /**
     * Returns a string of the format {@code <prefix>/<resourceLocation>}.
     *
     * @return String
     */
    public String getDescription() {
        return prefix.concat(": ").concat(resourceLocation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Namespace [");

        // prefix is non-null by contract
        builder.append("prefix=");
        builder.append(prefix);
        builder.append(", ");

        // resource location is non-null by contract
        builder.append("resourceLocation=");
        builder.append(resourceLocation);

        builder.append("]");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result *= prime;
        // prefix is non-null by contract
        result += prefix.hashCode();

        result *= prime;
        // resource location is non-null by contract
        result += resourceLocation.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Namespace)) return false;

        final Namespace n = (Namespace) o;

        // prefix is non-null by contract
        if (!prefix.equals(n.prefix)) return false;

        // resource location is non-null by contract
        if (!resourceLocation.equals(n.resourceLocation)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toBELLongForm() {
        return prefix != null ? (prefix + ":") : "";
    }

    /**
     * {@inheritDoc}<br>
     * Same as {@link #toBELLongForm()}
     */
    @Override
    public String toBELShortForm() {
        return toBELLongForm();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Namespace clone() {
        return new Namespace(prefix, resourceLocation);
    }
}
