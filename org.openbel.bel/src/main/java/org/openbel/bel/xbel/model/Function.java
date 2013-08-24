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
package org.openbel.bel.xbel.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for function.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="function">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="proteinAbundance"/>
 *     &lt;enumeration value="proteinModification"/>
 *     &lt;enumeration value="substitution"/>
 *     &lt;enumeration value="truncation"/>
 *     &lt;enumeration value="rnaAbundance"/>
 *     &lt;enumeration value="abundance"/>
 *     &lt;enumeration value="microRNAAbundance"/>
 *     &lt;enumeration value="geneAbundance"/>
 *     &lt;enumeration value="biologicalProcess"/>
 *     &lt;enumeration value="pathology"/>
 *     &lt;enumeration value="complexAbundance"/>
 *     &lt;enumeration value="translocation"/>
 *     &lt;enumeration value="cellSecretion"/>
 *     &lt;enumeration value="cellSurfaceExpression"/>
 *     &lt;enumeration value="reaction"/>
 *     &lt;enumeration value="compositeAbundance"/>
 *     &lt;enumeration value="fusion"/>
 *     &lt;enumeration value="reactants"/>
 *     &lt;enumeration value="products"/>
 *     &lt;enumeration value="degradation"/>
 *     &lt;enumeration value="molecularActivity"/>
 *     &lt;enumeration value="catalyticActivity"/>
 *     &lt;enumeration value="kinaseActivity"/>
 *     &lt;enumeration value="phosphataseActivity"/>
 *     &lt;enumeration value="peptidaseActivity"/>
 *     &lt;enumeration value="ribosylationActivity"/>
 *     &lt;enumeration value="transcriptionalActivity"/>
 *     &lt;enumeration value="transportActivity"/>
 *     &lt;enumeration value="gtpBoundActivity"/>
 *     &lt;enumeration value="chaperoneActivity"/>
 *     &lt;enumeration value="list"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "function")
@XmlEnum
public enum Function {

    /**
     *
     *                         The protein abundance BEL function.
     *
     *
     */
    @XmlEnumValue("proteinAbundance")
    PROTEIN_ABUNDANCE("proteinAbundance"),

    /**
     *
     *                         The protein modification BEL function for a protein abundance.
     *
     *
     */
    @XmlEnumValue("proteinModification")
    PROTEIN_MODIFICATION("proteinModification"),

    /**
     *
     *                         The substitution BEL function for a protein abundance.
     *
     *
     */
    @XmlEnumValue("substitution")
    SUBSTITUTION("substitution"),

    /**
     *
     *                         The truncation BEL function for a protein abundance.
     *
     *
     */
    @XmlEnumValue("truncation")
    TRUNCATION("truncation"),

    /**
     *
     *                         The rna abundance BEL function.
     *
     *
     */
    @XmlEnumValue("rnaAbundance")
    RNA_ABUNDANCE("rnaAbundance"),

    /**
     *
     *                         The abundance BEL function.
     *
     *
     */
    @XmlEnumValue("abundance")
    ABUNDANCE("abundance"),

    /**
     *
     *                         The micro rna abundance BEL function.
     *
     *
     */
    @XmlEnumValue("microRNAAbundance")
    MICRO_RNA_ABUNDANCE("microRNAAbundance"),

    /**
     *
     *                         The gene abundance BEL function.
     *
     *
     */
    @XmlEnumValue("geneAbundance")
    GENE_ABUNDANCE("geneAbundance"),

    /**
     *
     *                         The biological process BEL function.
     *
     *
     */
    @XmlEnumValue("biologicalProcess")
    BIOLOGICAL_PROCESS("biologicalProcess"),

    /**
     *
     *                         The pathology BEL function.
     *
     *
     */
    @XmlEnumValue("pathology")
    PATHOLOGY("pathology"),

    /**
     *
     *                         The complex abundance BEL function.
     *
     *
     */
    @XmlEnumValue("complexAbundance")
    COMPLEX_ABUNDANCE("complexAbundance"),

    /**
     *
     *                         The translocation BEL function.
     *
     *
     */
    @XmlEnumValue("translocation")
    TRANSLOCATION("translocation"),

    /**
     *
     *                         The cell secretion BEL function.
     *
     *
     */
    @XmlEnumValue("cellSecretion")
    CELL_SECRETION("cellSecretion"),

    /**
     *
     *                         The cell surface expression BEL function.
     *
     *
     */
    @XmlEnumValue("cellSurfaceExpression")
    CELL_SURFACE_EXPRESSION("cellSurfaceExpression"),

    /**
     *
     *                         The reaction BEL function.
     *
     *
     */
    @XmlEnumValue("reaction")
    REACTION("reaction"),

    /**
     *
     *                         The composite abundance BEL function.
     *
     *
     */
    @XmlEnumValue("compositeAbundance")
    COMPOSITE_ABUNDANCE("compositeAbundance"),

    /**
     *
     *                         The fusion BEL function.
     *
     *
     */
    @XmlEnumValue("fusion")
    FUSION("fusion"),

    /**
     *
     *                         The reactants BEL function.
     *
     *
     */
    @XmlEnumValue("reactants")
    REACTANTS("reactants"),

    /**
     *
     *                         The products BEL function.
     *
     *
     */
    @XmlEnumValue("products")
    PRODUCTS("products"),

    /**
     *
     *                         The degradation BEL function.
     *
     *
     */
    @XmlEnumValue("degradation")
    DEGRADATION("degradation"),

    /**
     *
     *                         The molecular activity BEL function.
     *
     *
     */
    @XmlEnumValue("molecularActivity")
    MOLECULAR_ACTIVITY("molecularActivity"),

    /**
     *
     *                         The catalytic activity BEL function.
     *
     *
     */
    @XmlEnumValue("catalyticActivity")
    CATALYTIC_ACTIVITY("catalyticActivity"),

    /**
     *
     *                         The kinase activity BEL function.
     *
     *
     */
    @XmlEnumValue("kinaseActivity")
    KINASE_ACTIVITY("kinaseActivity"),

    /**
     *
     *                         The phosphatase activity BEL function.
     *
     *
     */
    @XmlEnumValue("phosphataseActivity")
    PHOSPHATASE_ACTIVITY("phosphataseActivity"),

    /**
     *
     *                         The peptidase activity BEL function.
     *
     *
     */
    @XmlEnumValue("peptidaseActivity")
    PEPTIDASE_ACTIVITY("peptidaseActivity"),

    /**
     *
     *                         The ribosylation activity BEL function.
     *
     *
     */
    @XmlEnumValue("ribosylationActivity")
    RIBOSYLATION_ACTIVITY("ribosylationActivity"),

    /**
     *
     *                         The transcriptional activity BEL function.
     *
     *
     */
    @XmlEnumValue("transcriptionalActivity")
    TRANSCRIPTIONAL_ACTIVITY("transcriptionalActivity"),

    /**
     *
     *                         The transport activity BEL function.
     *
     *
     */
    @XmlEnumValue("transportActivity")
    TRANSPORT_ACTIVITY("transportActivity"),

    /**
     *
     *                         The gtp-bound activity BEL function.
     *
     *
     */
    @XmlEnumValue("gtpBoundActivity")
    GTP_BOUND_ACTIVITY("gtpBoundActivity"),

    /**
     *
     *                         The chaperone activity BEL function.
     *
     *
     */
    @XmlEnumValue("chaperoneActivity")
    CHAPERONE_ACTIVITY("chaperoneActivity"),

    /**
     *
     *                         The list BEL function.
     *
     *
     */
    @XmlEnumValue("list")
    LIST("list");
    private final String value;

    Function(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Function fromValue(String v) {
        for (Function c : Function.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
