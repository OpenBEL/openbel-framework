/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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
