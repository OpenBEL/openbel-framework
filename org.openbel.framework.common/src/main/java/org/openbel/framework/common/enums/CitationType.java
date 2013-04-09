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
package org.openbel.framework.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerated representation of citation type.
 * <p>
 * Portions of this enum have been automatically generated.
 * </p>
 */
public enum CitationType {

    /**
     * A citation derived from a book.
     * <p>
     * Display value: {@code "Book"}
     * </p>
     */
    BOOK(0, "Book"),

    /**
     * A citation derived from a journal.
     * <p>
     * Display value: {@code "Journal"}
     * </p>
     */
    JOURNAL(1, "Journal"),

    /**
     * A citation derived from an online resource.
     * <p>
     * Display value: {@code "Online Resource"}
     * </p>
     */
    ONLINE_RESOURCE(2, "Online Resource"),

    /**
     * A citation derived from a PubMed citation.
     * <p>
     * Display value: {@code "PubMed"}
     * </p>
     */
    PUBMED(3, "PubMed"),

    /**
     * A citation derived from a whitepaper.
     * <p>
     * Display value: {@code "Book"}
     * </p>
     */
    OTHER(4, "Other");

    /* Value unique to each enumeration. */
    private final Integer value;
    /* Enumeration display value. */
    private String displayValue;

    /* Static cache of enum by string representation. */
    private static final Map<String, CitationType> STRINGTOENUM =
            new HashMap<String, CitationType>(values().length, 1F);

    static {
        for (final CitationType e : values())
            STRINGTOENUM.put(e.toString(), e);
    }

    /**
     * Constructor for setting enum and display value.
     *
     * @param value Enum value
     * @param displayValue Display value
     */
    private CitationType(Integer value, String displayValue) {
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
     * Returns the citation type's value.
     *
     * @return value
     * @see java.lang.Enum#ordinal() Contrast with {@code ordinal}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Returns the citation type's display value.
     *
     * @return display value
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Returns the citation type by its string representation.
     *
     * @param s Citation type string representation
     * @return CitationType - null if the provided string has no citation type
     * representation
     * @see #getCitationType(String)
     */
    public static CitationType fromString(final String s) {
        return getCitationType(s);
    }

    /**
     * Returns the citation type by its string representation
     * (case-insensitive).
     * <p>
     * This method is favored in place of {@link #fromString(String)} as it
     * provides disambiguation with other enums when used as a static import.
     * </p>
     *
     * @param s Citation type string representation
     * @return CitationType - null if the provided string has no citation type
     * representation
     */
    public static CitationType getCitationType(final String s) {
        CitationType e = STRINGTOENUM.get(s);
        if (e != null) return e;

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s)) return STRINGTOENUM.get(dispval);
        }

        return null;
    }
}
