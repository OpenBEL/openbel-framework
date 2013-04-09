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
package org.openbel.framework.common.record;

import static org.openbel.framework.common.BELUtilities.constrainedHashMap;

import java.util.Map;

/**
 * Enumerated representation of record mode.
 * <p>
 * Portions of this enum have been automatically generated.
 * </p>
 *
 * @author Nick Bargnesi
 */
public enum RecordMode {

    /**
     * Read-only mode.
     */
    READ_ONLY(0, "READ_ONLY"),

    /**
     * Read/write mode.
     */
    READ_WRITE(1, "READ_WRITE");

    /* Value unique to each enumeration. */
    private final Integer value;
    /* Enumeration display value. */
    private String displayValue;

    /* Static cache of enum by string representation. */
    private static final Map<String, RecordMode> STRINGTOENUM =
            constrainedHashMap(values().length);

    static {
        for (final RecordMode e : values())
            STRINGTOENUM.put(e.toString(), e);
    }

    /**
     * Constructor for setting enum and display value.
     *
     * @param value Enum value
     * @param displayValue Display value
     */
    private RecordMode(Integer value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return displayValue;
    }

    /**
     * Returns the record mode's value.
     *
     * @return value
     * @see java.lang.Enum#ordinal() Contrast with {@code ordinal}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Returns the record mode's display value.
     *
     * @return display value
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Returns the record mode by its string representation.
     *
     * @param s {@link RecordMode} string representation
     * @return {@link RecordMode}, may be null if the provided string has no
     * {@link RecordMode} representation
     */
    public static RecordMode getRecordMode(final String s) {
        RecordMode e = STRINGTOENUM.get(s);
        if (e != null) return e;

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s))
                return STRINGTOENUM.get(dispval);
        }

        return null;
    }
}
