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
package org.openbel.framework.ws.model;

import java.util.HashMap;
import java.util.Map;

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

    UNKNOWN("unknown"),
    ABUNDANCE("abundance"),
    BIOLOGICAL_PROCESS("biologicalProcess"),
    CATALYTIC_ACTIVITY("catalyticActivity"),
    CELL_SECRETION("cellSecretion"),
    CELL_SURFACE_EXPRESSION("cellSurfaceExpression"),
    CHAPERONE_ACTIVITY("chaperoneActivity"),
    COMPLEX_ABUNDANCE("complexAbundance"),
    COMPOSITE_ABUNDANCE("compositeAbundance"),
    DEGRADATION("degradation"),
    GENE_ABUNDANCE("geneAbundance"),
    GTP_BOUND_ACTIVITY("gtpBoundActivity"),
    KINASE_ACTIVITY("kinaseActivity"),
    LIST("list"),
    MICRORNA_ABUNDANCE("microRNAAbundance"),
    MOLECULAR_ACTIVITY("molecularActivity"),
    PATHOLOGY("pathology"),
    PEPTIDASE_ACTIVITY("peptidaseActivity"),
    PHOSPHATASE_ACTIVITY("phosphataseActivity"),
    PROTEIN_ABUNDANCE("proteinAbundance"),
    REACTION("reaction"),
    RIBOSYLATION_ACTIVITY("ribosylationActivity"),
    RNA_ABUNDANCE("rnaAbundance"),
    TRANSCRIPTIONAL_ACTIVITY("transcriptionalActivity"),
    TRANSLOCATION("translocation"),
    TRANSPORT_ACTIVITY("transportActivity");

    private static final Map<String, FunctionType> mapping = 
            new HashMap<String, FunctionType>(values().length);
    
    static {
        for (FunctionType r : values()) {
            mapping.put(r.displayValue, r);
        }
    }
    
    private String displayValue;
    
    private FunctionType(final String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String value() {
        return name();
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
    
    public static FunctionType fromValue(String v) {
        // try display value
        FunctionType r = mapping.get(v);
        if (r != null) return r;

        // try enum name
        return valueOf(v);
    }
}
