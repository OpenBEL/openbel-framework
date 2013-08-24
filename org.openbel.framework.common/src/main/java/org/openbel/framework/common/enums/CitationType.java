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
