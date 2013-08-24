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
 * <p>Java class for FunctionReturnType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FunctionReturnType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ABUNDANCE"/>
 *     &lt;enumeration value="PROTEIN_ABUNDANCE"/>
 *     &lt;enumeration value="GENE_ABUNDANCE"/>
 *     &lt;enumeration value="MICRORNA_ABUNDANCE"/>
 *     &lt;enumeration value="RNA_ABUNDANCE"/>
 *     &lt;enumeration value="BIOLOGICAL_PROCESS"/>
 *     &lt;enumeration value="PATHOLOGY"/>
 *     &lt;enumeration value="COMPLEX_ABUNDANCE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "FunctionReturnType")
@XmlEnum
public enum FunctionReturnType {

    ABUNDANCE,
    PROTEIN_ABUNDANCE,
    GENE_ABUNDANCE,
    MICRORNA_ABUNDANCE,
    RNA_ABUNDANCE,
    BIOLOGICAL_PROCESS,
    PATHOLOGY,
    COMPLEX_ABUNDANCE;

    public String value() {
        return name();
    }

    public static FunctionReturnType fromValue(String v) {
        return valueOf(v);
    }

}
