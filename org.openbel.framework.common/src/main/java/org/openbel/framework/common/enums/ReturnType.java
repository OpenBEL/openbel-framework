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

import static java.util.Collections.unmodifiableSet;
import static org.openbel.framework.common.BELUtilities.sizedHashSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Enumerated representation of a {@link FunctionEnum function}'s return type.
 * <p>
 * Portions of this enum have been automatically generated.
 * </p>
 */
public enum ReturnType {

    /** Abundance return type. */
    ABUNDANCE(0, "abundance"),

    /** Protein abundance return type. */
    PROTEIN_ABUNDANCE(1, "proteinAbundance"),

    /** Gene abundance return type. */
    GENE_ABUNDANCE(2, "geneAbundance"),

    /** MicroRNA abundance return type. */
    MICRORNA_ABUNDANCE(3, "microRNAAbundance"),

    /** Biological process return type. */
    BIOLOGICAL_PROCESS(4, "biologicalProcess"),

    /** Pathology return type. */
    PATHOLOGY(5, "pathology"),

    /** ProteinModification return type. */
    PROTEIN_MODIFICATION(6, "proteinModification"),

    /** Substitution return type. */
    SUBSTITUTION(7, "substitution"),

    /** Truncation return type. */
    TRUNCATION(8, "truncation"),

    /** Reactants return type. */
    REACTANTS(9, "reactants"),

    /** Products return type. */
    PRODUCTS(10, "products"),

    /** Complex abundance return type. */
    COMPLEX_ABUNDANCE(11, "complexAbundance"),

    /** RNA abundance return type. */
    RNA_ABUNDANCE(12, "rnaAbundance"),

    /** List return type. */
    LIST(13, "list"),

    /** Fusion return type. */
    FUSION(14, "fusion");

    /* Value unique to each enumeration. */
    private final Integer value;
    /* Enumeration display value. */
    private String displayValue;

    private static final Set<ReturnType> ABUNDANCE_RETURN_TYPES;
    private static final Set<ReturnType> PROCESS_RETURN_TYPES;

    /* Static cache of enum by string representation. */
    private static final Map<String, ReturnType> STRINGTOENUM =
            new HashMap<String, ReturnType>(values().length, 1F);

    static {
        for (final ReturnType e : values()) {
            STRINGTOENUM.put(e.toString(), e);
        }

        int abundances = 0;
        int processes = 0;

        for (final ReturnType r : values()) {
            if (r.isAbundance()) {
                abundances++;
            }
            if (r.isProcess()) {
                processes++;
            }
        }

        Set<ReturnType> abundanceReturnTypes = sizedHashSet(abundances);
        Set<ReturnType> processReturnTypes = sizedHashSet(processes);

        for (final ReturnType r : values()) {
            if (r.isAbundance()) {
                abundanceReturnTypes.add(r);
            }
            if (r.isProcess()) {
                processReturnTypes.add(r);
            }
        }

        // prevent modification to all backing sets
        ABUNDANCE_RETURN_TYPES = unmodifiableSet(abundanceReturnTypes);
        PROCESS_RETURN_TYPES = unmodifiableSet(processReturnTypes);

    }

    /**
     * Constructor for setting enum and display value.
     *
     * @param value Enum value
     * @param displayValue Display value
     */
    private ReturnType(Integer value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    /**
     * Returns {@code true} if this return type is assignable from {@code rt},
     * {@code false} otherwise.
     *
     * @param rt Return type
     * @return boolean
     */
    public boolean isAssignableFrom(final ReturnType rt) {
        return assignableFrom(this, rt);
    }

    /**
     * Returns {@code true} if the {@code supertype} is assignable from the
     * {@code subtype}, {@code false} otherwise.
     *
     * @param supertype Return type supertype
     * @param subtype Return type subtype
     * @return boolean
     */
    public static boolean assignableFrom(final ReturnType supertype,
            final ReturnType subtype) {
        switch (supertype) {
        case ABUNDANCE:
            if (subtype == GENE_ABUNDANCE) return true;
            if (subtype == MICRORNA_ABUNDANCE) return true;
            if (subtype == PROTEIN_ABUNDANCE) return true;
            if (subtype == COMPLEX_ABUNDANCE) return true;
            if (subtype == RNA_ABUNDANCE) return true;
            if (subtype == ABUNDANCE) return true;
            break;
        case BIOLOGICAL_PROCESS:
            if (subtype == PATHOLOGY) return true;
            if (subtype == BIOLOGICAL_PROCESS) return true;
            break;
        case GENE_ABUNDANCE:
            if (subtype == GENE_ABUNDANCE) return true;
            break;
        case RNA_ABUNDANCE:
            if (subtype == RNA_ABUNDANCE) return true;
            if (subtype == MICRORNA_ABUNDANCE) return true;
            break;
        case MICRORNA_ABUNDANCE:
            if (subtype == MICRORNA_ABUNDANCE) return true;
            break;
        case PATHOLOGY:
            if (subtype == PATHOLOGY) return true;
            break;
        case PRODUCTS:
            if (subtype == PRODUCTS) return true;
            break;
        case PROTEIN_ABUNDANCE:
            if (subtype == GENE_ABUNDANCE) return true;
            if (subtype == PROTEIN_ABUNDANCE) return true;
            break;
        case COMPLEX_ABUNDANCE:
            if (subtype == COMPLEX_ABUNDANCE) return true;
            break;
        case PROTEIN_MODIFICATION:
            if (subtype == PROTEIN_MODIFICATION) return true;
            break;
        case REACTANTS:
            if (subtype == REACTANTS) return true;
            break;
        case SUBSTITUTION:
            if (subtype == SUBSTITUTION) return true;
            break;
        case TRUNCATION:
            if (subtype == TRUNCATION) return true;
            break;
        case LIST:
            if (subtype == LIST) return true;
            break;
        case FUSION:
            if (subtype == FUSION) return true;
            break;
        default:
            throw new UnsupportedOperationException(supertype.toString());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return displayValue;
    }

    /**
     * Returns the ReturnType's value.
     *
     * @return value
     * @see java.lang.Enum#ordinal() Contrast with {@code ordinal}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Returns the ReturnType's display value.
     *
     * @return display value
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Returns the ReturnType by its string representation (case-insensitive).
     *
     * @param s ReturnType string representation
     * @return ReturnType - null if the provided string has no return type
     * representation
     */
    public static ReturnType getReturnType(final String s) {
        ReturnType e = STRINGTOENUM.get(s);
        if (e != null) {
            return e;
        }

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s)) {
                return STRINGTOENUM.get(dispval);
            }
        }

        return null;
    }

    /**
     * Returns {@code true} if {@code r} is an abundance, {@code false}
     * otherwise.
     *
     * @param r {@link ReturnType}
     * @return boolean
     */
    public static boolean isAbundance(final ReturnType r) {
        return r.isAbundance();
    }

    /**
     * Returns {@code true} if {@code r} is a process, {@code false} otherwise.
     *
     * @param r {@link ReturnType}
     * @return boolean
     */
    public static boolean isProcess(final ReturnType r) {
        return r.isProcess();
    }

    /**
     * Returns {@code true} if this is an abundance, {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isAbundance() {
        switch (this) {
        case ABUNDANCE:
        case PROTEIN_ABUNDANCE:
        case GENE_ABUNDANCE:
        case MICRORNA_ABUNDANCE:
        case COMPLEX_ABUNDANCE:
        case RNA_ABUNDANCE:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if this is a process, {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isProcess() {
        switch (this) {
        case BIOLOGICAL_PROCESS:
        case PATHOLOGY:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns the set of return type abundances.
     *
     * @return boolean
     */
    public static Set<ReturnType> getAbundances() {
        return ABUNDANCE_RETURN_TYPES;
    }

    /**
     * Returns the set of return type processes.
     *
     * @return boolean
     */
    public static Set<ReturnType> getProcesses() {
        return PROCESS_RETURN_TYPES;
    }
}
