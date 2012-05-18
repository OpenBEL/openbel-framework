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
