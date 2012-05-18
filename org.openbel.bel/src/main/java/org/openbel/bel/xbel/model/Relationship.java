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
package org.openbel.bel.xbel.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for relationship.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="relationship">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="increases"/>
 *     &lt;enumeration value="decreases"/>
 *     &lt;enumeration value="directlyIncreases"/>
 *     &lt;enumeration value="directlyDecreases"/>
 *     &lt;enumeration value="causesNoChange"/>
 *     &lt;enumeration value="positiveCorrelation"/>
 *     &lt;enumeration value="negativeCorrelation"/>
 *     &lt;enumeration value="translatedTo"/>
 *     &lt;enumeration value="transcribedTo"/>
 *     &lt;enumeration value="isA"/>
 *     &lt;enumeration value="subProcessOf"/>
 *     &lt;enumeration value="rateLimitingStepOf"/>
 *     &lt;enumeration value="biomarkerFor"/>
 *     &lt;enumeration value="prognosticBiomarkerFor"/>
 *     &lt;enumeration value="orthologous"/>
 *     &lt;enumeration value="analogous"/>
 *     &lt;enumeration value="association"/>
 *     &lt;enumeration value="hasMembers"/>
 *     &lt;enumeration value="hasComponents"/>
 *     &lt;enumeration value="hasMember"/>
 *     &lt;enumeration value="hasComponent"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "relationship")
@XmlEnum
public enum Relationship {

    @XmlEnumValue("increases")
    INCREASES("increases"),
    @XmlEnumValue("decreases")
    DECREASES("decreases"),
    @XmlEnumValue("directlyIncreases")
    DIRECTLY_INCREASES("directlyIncreases"),
    @XmlEnumValue("directlyDecreases")
    DIRECTLY_DECREASES("directlyDecreases"),
    @XmlEnumValue("causesNoChange")
    CAUSES_NO_CHANGE("causesNoChange"),
    @XmlEnumValue("positiveCorrelation")
    POSITIVE_CORRELATION("positiveCorrelation"),
    @XmlEnumValue("negativeCorrelation")
    NEGATIVE_CORRELATION("negativeCorrelation"),
    @XmlEnumValue("translatedTo")
    TRANSLATED_TO("translatedTo"),
    @XmlEnumValue("transcribedTo")
    TRANSCRIBED_TO("transcribedTo"),
    @XmlEnumValue("isA")
    IS_A("isA"),
    @XmlEnumValue("subProcessOf")
    SUB_PROCESS_OF("subProcessOf"),
    @XmlEnumValue("rateLimitingStepOf")
    RATE_LIMITING_STEP_OF("rateLimitingStepOf"),
    @XmlEnumValue("biomarkerFor")
    BIOMARKER_FOR("biomarkerFor"),
    @XmlEnumValue("prognosticBiomarkerFor")
    PROGNOSTIC_BIOMARKER_FOR("prognosticBiomarkerFor"),
    @XmlEnumValue("orthologous")
    ORTHOLOGOUS("orthologous"),
    @XmlEnumValue("analogous")
    ANALOGOUS("analogous"),
    @XmlEnumValue("association")
    ASSOCIATION("association"),
    @XmlEnumValue("hasMembers")
    HAS_MEMBERS("hasMembers"),
    @XmlEnumValue("hasComponents")
    HAS_COMPONENTS("hasComponents"),
    @XmlEnumValue("hasMember")
    HAS_MEMBER("hasMember"),
    @XmlEnumValue("hasComponent")
    HAS_COMPONENT("hasComponent");
    private final String value;

    Relationship(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Relationship fromValue(String v) {
        for (Relationship c : Relationship.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
