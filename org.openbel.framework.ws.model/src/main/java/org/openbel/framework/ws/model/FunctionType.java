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
 * <p>Java class for FunctionType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FunctionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="UNKNOWN"/>
 *     &lt;enumeration value="ABUNDANCE"/>
 *     &lt;enumeration value="BIOLOGICAL_PROCESS"/>
 *     &lt;enumeration value="CATALYTIC_ACTIVITY"/>
 *     &lt;enumeration value="CELL_SECRETION"/>
 *     &lt;enumeration value="CELL_SURFACE_EXPRESSION"/>
 *     &lt;enumeration value="CHAPERONE_ACTIVITY"/>
 *     &lt;enumeration value="COMPLEX_ABUNDANCE"/>
 *     &lt;enumeration value="COMPOSITE_ABUNDANCE"/>
 *     &lt;enumeration value="DEGRADATION"/>
 *     &lt;enumeration value="GENE_ABUNDANCE"/>
 *     &lt;enumeration value="GTP_BOUND_ACTIVITY"/>
 *     &lt;enumeration value="KINASE_ACTIVITY"/>
 *     &lt;enumeration value="LIST"/>
 *     &lt;enumeration value="MICRORNA_ABUNDANCE"/>
 *     &lt;enumeration value="MOLECULAR_ACTIVITY"/>
 *     &lt;enumeration value="PATHOLOGY"/>
 *     &lt;enumeration value="PEPTIDASE_ACTIVITY"/>
 *     &lt;enumeration value="PHOSPHATASE_ACTIVITY"/>
 *     &lt;enumeration value="PROTEIN_ABUNDANCE"/>
 *     &lt;enumeration value="REACTION"/>
 *     &lt;enumeration value="RIBOSYLATION_ACTIVITY"/>
 *     &lt;enumeration value="RNA_ABUNDANCE"/>
 *     &lt;enumeration value="TRANSCRIPTIONAL_ACTIVITY"/>
 *     &lt;enumeration value="TRANSLOCATION"/>
 *     &lt;enumeration value="TRANSPORT_ACTIVITY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "FunctionType")
@XmlEnum
public enum FunctionType {

    /**
     *
     *                         Represents the state where the node's function is
     *                         unknown.
     *
     *
     */
    UNKNOWN,
    ABUNDANCE,
    BIOLOGICAL_PROCESS,
    CATALYTIC_ACTIVITY,
    CELL_SECRETION,
    CELL_SURFACE_EXPRESSION,
    CHAPERONE_ACTIVITY,
    COMPLEX_ABUNDANCE,
    COMPOSITE_ABUNDANCE,
    DEGRADATION,
    GENE_ABUNDANCE,
    GTP_BOUND_ACTIVITY,
    KINASE_ACTIVITY,
    LIST,
    MICRORNA_ABUNDANCE,
    MOLECULAR_ACTIVITY,
    PATHOLOGY,
    PEPTIDASE_ACTIVITY,
    PHOSPHATASE_ACTIVITY,
    PROTEIN_ABUNDANCE,
    REACTION,
    RIBOSYLATION_ACTIVITY,
    RNA_ABUNDANCE,
    TRANSCRIPTIONAL_ACTIVITY,
    TRANSLOCATION,
    TRANSPORT_ACTIVITY;

    public String value() {
        return name();
    }

    public static FunctionType fromValue(String v) {
        return valueOf(v);
    }

}
