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
package org.openbel.framework.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerated representation of annotation type.
 * <p>
 * Portions of this enum have been automatically generated.
 * </p>
 */
public enum AnnotationType {

    /**
     * An annotation whose value must match one from an enumerated list.
     */
    ENUMERATION(0, "listAnnotation"),

    /**
     * An annotation whose value must match a regular expression.
     */
    REGULAR_EXPRESSION(1, "patternAnnotation");

    /**
     * Value unique to each enumeration.
     */
    private final Integer value;
    /**
     * Enumeration display value.
     */
    private String displayValue;

    /**
     * Static cache of annotation type by string representation.
     */
    private static final Map<String, AnnotationType> STRINGTOENUM =
            new HashMap<String, AnnotationType>(values().length, 1F);
    /**
     * Static cache of annotation type by value representation.
     */
    private static final Map<Integer, AnnotationType> VALUETOENUM =
            new HashMap<Integer, AnnotationType>(values().length, 1F);
    static {
        for (final AnnotationType e : values()) {
            STRINGTOENUM.put(e.toString(), e);
            VALUETOENUM.put(e.value, e);
        }
    }

    /**
     * Constructor for setting enum and display value.
     * 
     * @param value Enum value
     * @param displayValue Display value
     */
    private AnnotationType(Integer value, String displayValue) {
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
     * Returns the annotation type's value.
     * 
     * @return value
     * @see java.lang.Enum#ordinal() Contrast with {@code ordinal}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Returns the annotation type's display value.
     * 
     * @return display value
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Returns the annotation type by its string representation, which may be
     * null.
     * 
     * @param s Annotation type string representation
     * @return AnnotationType - null if the provided string has no annotation
     * type representation
     * @see #getAnnotationType(String)
     */
    public static AnnotationType fromString(final String s) {
        return getAnnotationType(s);
    }

    /**
     * Returns the annotation type for the <tt>value</tt>.
     * 
     * @param value {@link Integer}, the value for the annotation type
     * @return {@link AnnotationType}, the annotation type, or null if:
     * <ul>
     * <li><tt>value</tt> parameter is null</li>
     * <li><tt>value</tt> parameter does not map to an annotation type</li>
     * </ul>
     */
    public static AnnotationType fromValue(final Integer value) {
        if (value == null) {
            return null;
        }

        return VALUETOENUM.get(value);
    }

    /**
     * Returns the annotation type by its string representation
     * (case-insensitive), which may be null.
     * <p>
     * This method is favored in place of {@link #fromString(String)} as it
     * provides disambiguation with other enums when used as a static import.
     * </p>
     * 
     * @param s Annotation type string representation
     * @return AnnotationType - null if the provided string has no annotation
     * type representation
     */
    public static AnnotationType getAnnotationType(final String s) {
        AnnotationType e = STRINGTOENUM.get(s);
        if (e != null) return e;

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s)) return STRINGTOENUM.get(dispval);
        }

        return null;
    }
}
