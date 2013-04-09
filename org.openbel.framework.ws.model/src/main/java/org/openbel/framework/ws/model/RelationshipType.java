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
package org.openbel.framework.ws.model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RelationshipType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RelationshipType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="UNKNOWN"/>
 *     &lt;enumeration value="ACTS_IN"/>
 *     &lt;enumeration value="ANALOGOUS"/>
 *     &lt;enumeration value="ASSOCIATION"/>
 *     &lt;enumeration value="BIOMARKER_FOR"/>
 *     &lt;enumeration value="CAUSES_NO_CHANGE"/>
 *     &lt;enumeration value="DECREASES"/>
 *     &lt;enumeration value="DIRECTLY_DECREASES"/>
 *     &lt;enumeration value="DIRECTLY_INCREASES"/>
 *     &lt;enumeration value="HAS_COMPONENT"/>
 *     &lt;enumeration value="HAS_MEMBER"/>
 *     &lt;enumeration value="HAS_MODIFICATION"/>
 *     &lt;enumeration value="HAS_PRODUCT"/>
 *     &lt;enumeration value="HAS_VARIANT"/>
 *     &lt;enumeration value="INCLUDES"/>
 *     &lt;enumeration value="INCREASES"/>
 *     &lt;enumeration value="IS_A"/>
 *     &lt;enumeration value="NEGATIVE_CORRELATION"/>
 *     &lt;enumeration value="ORTHOLOGOUS"/>
 *     &lt;enumeration value="POSITIVE_CORRELATION"/>
 *     &lt;enumeration value="PROGNOSTIC_BIOMARKER_FOR"/>
 *     &lt;enumeration value="RATE_LIMITING_STEP_OF"/>
 *     &lt;enumeration value="REACTANT_IN"/>
 *     &lt;enumeration value="SUB_PROCESS_OF"/>
 *     &lt;enumeration value="TRANSCRIBED_TO"/>
 *     &lt;enumeration value="TRANSLATED_TO"/>
 *     &lt;enumeration value="TRANSLOCATES"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "RelationshipType")
@XmlEnum
public enum RelationshipType {

    UNKNOWN("unknown"),
    ACTS_IN("actsIn"),
    ANALOGOUS("analogous"),
    ASSOCIATION("association"),
    BIOMARKER_FOR("biomarkerFor"),
    CAUSES_NO_CHANGE("causesNoChange"),
    DECREASES("decreases"),
    DIRECTLY_DECREASES("directlyDecreases"),
    DIRECTLY_INCREASES("directlyIncreases"),
    HAS_COMPONENT("hasComponent"),
    HAS_MEMBER("hasMember"),
    HAS_MODIFICATION("hasModification"),
    HAS_PRODUCT("hasProduct"),
    HAS_VARIANT("hasVariant"),
    INCLUDES("includes"),
    INCREASES("increases"),
    IS_A("isA"),
    NEGATIVE_CORRELATION("negativeCorrelation"),
    ORTHOLOGOUS("orthologous"),
    POSITIVE_CORRELATION("positiveCorrelation"),
    PROGNOSTIC_BIOMARKER_FOR("prognosticBiomarkerFor"),
    RATE_LIMITING_STEP_OF("rateLimitingStepOf"),
    REACTANT_IN("reactantIn"),
    SUB_PROCESS_OF("subProcessOf"),
    TRANSCRIBED_TO("transcribedTo"),
    TRANSLATED_TO("translatedTo"),
    TRANSLOCATES("translocates");

    private static final Map<String, RelationshipType> mapping =
            new HashMap<String, RelationshipType>(values().length);

    static {
        for (RelationshipType r : values()) {
            mapping.put(r.displayValue, r);
        }
    }

    private String displayValue;

    private RelationshipType(final String displayValue) {
        this.displayValue = displayValue;
    }

    public String value() {
        return name();
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static RelationshipType fromValue(String v) {
        // try display value
        RelationshipType r = mapping.get(v);
        if (r != null) return r;

        // try enum name
        return valueOf(v);
    }
}
