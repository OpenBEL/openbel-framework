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
