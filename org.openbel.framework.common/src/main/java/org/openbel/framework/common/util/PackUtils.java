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
