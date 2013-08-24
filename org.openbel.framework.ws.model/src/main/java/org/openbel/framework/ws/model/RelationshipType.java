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
package org.openbel.framework.ws.model;

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

    /**
     *
     *                         Represents the state where the edge's relationship is
     *                         unknown.
     *
     *
     */
    UNKNOWN,
    ACTS_IN,
    ANALOGOUS,
    ASSOCIATION,
    BIOMARKER_FOR,
    CAUSES_NO_CHANGE,
    DECREASES,
    DIRECTLY_DECREASES,
    DIRECTLY_INCREASES,
    HAS_COMPONENT,
    HAS_MEMBER,
    HAS_MODIFICATION,
    HAS_PRODUCT,
    HAS_VARIANT,
    INCLUDES,
    INCREASES,
    IS_A,
    NEGATIVE_CORRELATION,
    ORTHOLOGOUS,
    POSITIVE_CORRELATION,
    PROGNOSTIC_BIOMARKER_FOR,
    RATE_LIMITING_STEP_OF,
    REACTANT_IN,
    SUB_PROCESS_OF,
    TRANSCRIBED_TO,
    TRANSLATED_TO,
    TRANSLOCATES;

    public String value() {
        return name();
    }

    public static RelationshipType fromValue(String v) {
        return valueOf(v);
    }

}
