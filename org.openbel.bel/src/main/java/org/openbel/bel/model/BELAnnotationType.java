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
package org.openbel.bel.model;

import java.util.HashMap;
import java.util.Map;

public enum BELAnnotationType {

    LIST(0, "LIST"),
    PATTERN(1, "PATTERN"),
    URL(2, "URL");

    private final Integer value;
    private String displayValue;

    private static final Map<String, BELAnnotationType> STRINGTOENUM =
            new HashMap<String, BELAnnotationType>(values().length, 1F);
    private static final Map<Integer, BELAnnotationType> VALUETOENUM =
            new HashMap<Integer, BELAnnotationType>(values().length, 1F);
    static {
        for (final BELAnnotationType e : values()) {
            STRINGTOENUM.put(e.toString(), e);
            VALUETOENUM.put(e.value, e);
        }
    }

    private BELAnnotationType(Integer value, String displayValue) {
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

    public Integer getValue() {
        return value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static BELAnnotationType fromString(final String s) {
        return getAnnotationType(s);
    }

    public static BELAnnotationType fromValue(final Integer value) {
        if (value == null) {
            return null;
        }

        return VALUETOENUM.get(value);
    }

    public static BELAnnotationType getAnnotationType(final String s) {
        BELAnnotationType e = STRINGTOENUM.get(s);
        if (e != null) return e;

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s)) return STRINGTOENUM.get(dispval);
        }

        return null;
    }
}
