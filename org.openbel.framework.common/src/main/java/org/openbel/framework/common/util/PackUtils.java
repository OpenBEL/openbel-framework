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
package org.openbel.framework.common.util;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.noLength;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Utilities for packing and unpacking of {@link String strings}.
 *
 * @author James McMahon
 */
public class PackUtils {

    /**
     * Defines the delimiter {@link String} for string-packed values.
     */
    public static final String PACKED_VALUES_DELIMITER = "##";

    /**
     * Pack values from <tt>List<String> unpackedValues</tt> into a single
     * {@link String} using {@link #PACKED_VALUES_DELIMITER}.
     *
     * @param unpackedValues <tt>List<String></tt> the list of strings to pack,
     * which should not be a {@code null} or empty {@link List list}
     * @return {@link String}, the packed {@link String} containing all values
     * delimited by {@link #PACKED_VALUES_DELIMITER}, or {@code null} if
     * {@code unpackedValues} is {@code null} or empty
     */
    public static String packValues(List<String> unpackedValues) {
        if (hasItems(unpackedValues)) {
            final Iterator<String> it = unpackedValues.iterator();
            final StringBuilder b = new StringBuilder();
            while (it.hasNext()) {
                b.append(it.next());
                if (it.hasNext()) {
                    b.append(PACKED_VALUES_DELIMITER);
                }
            }

            return b.toString();
        }

        return null;
    }

    /**
     * Unpack values from <tt>packedValues</tt> using
     * {@link #PACKED_VALUES_DELIMITER}.
     *
     * @param packedValues {@link String} the packed values, which should not
     * be {@code null}
     * @return <tt>{@link List}<{@link String></tt>, the unpacked strings, or
     * {@code null} if {@code packedValues} is {@code null}
     */
    public static List<String> unpackValues(String packedValues) {
        if (noLength(packedValues)) {
            return null;
        }

        final String[] values = packedValues.split(PACKED_VALUES_DELIMITER);
        return Arrays.asList(values);
    }

    /**
     * Private constructor for utility class.
     */
    private PackUtils() {

    }
}
