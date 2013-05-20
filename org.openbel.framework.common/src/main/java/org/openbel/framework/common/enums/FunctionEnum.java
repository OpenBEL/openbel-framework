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
package org.openbel.framework.common.enums;

import static java.util.Collections.unmodifiableSet;
import static org.openbel.framework.common.BELUtilities.constrainedHashMap;
import static org.openbel.framework.common.BELUtilities.constrainedHashSet;

import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.Strings;
import org.openbel.framework.common.lang.Abundance;
import org.openbel.framework.common.lang.BiologicalProcess;
import org.openbel.framework.common.lang.CatalyticActivity;
import org.openbel.framework.common.lang.CellSecretion;
import org.openbel.framework.common.lang.CellSurfaceExpression;
import org.openbel.framework.common.lang.ChaperoneActivity;
import org.openbel.framework.common.lang.ComplexAbundance;
import org.openbel.framework.common.lang.CompositeAbundance;
import org.openbel.framework.common.lang.Degradation;
import org.openbel.framework.common.lang.Function;
import org.openbel.framework.common.lang.Fusion;
import org.openbel.framework.common.lang.GTPBoundActivity;
import org.openbel.framework.common.lang.GeneAbundance;
import org.openbel.framework.common.lang.KinaseActivity;
import org.openbel.framework.common.lang.MicroRNAAbundance;
import org.openbel.framework.common.lang.MolecularActivity;
import org.openbel.framework.common.lang.Pathology;
import org.openbel.framework.common.lang.PeptidaseActivity;
import org.openbel.framework.common.lang.PhosphataseActivity;
import org.openbel.framework.common.lang.Products;
import org.openbel.framework.common.lang.ProteinAbundance;
import org.openbel.framework.common.lang.ProteinModification;
import org.openbel.framework.common.lang.RNAAbundance;
import org.openbel.framework.common.lang.Reactants;
import org.openbel.framework.common.lang.Reaction;
import org.openbel.framework.common.lang.RibosylationActivity;
import org.openbel.framework.common.lang.Substitution;
import org.openbel.framework.common.lang.TermList;
import org.openbel.framework.common.lang.TranscriptionalActivity;
import org.openbel.framework.common.lang.Translocation;
import org.openbel.framework.common.lang.TransportActivity;
import org.openbel.framework.common.lang.Truncation;

/**
 * Enumerated representation of function.
 * <p>
 * Portions of this enum have been automatically generated.
 * </p>
 */
public enum FunctionEnum {

    /**
     * Abundance term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    ABUNDANCE(0,
            Strings.ABUNDANCE,
            Strings.ABUNDANCE_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Biological process term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#BIOLOGICAL_PROCESS}</dd>
     * </dl>
     */
    BIOLOGICAL_PROCESS(1,
            Strings.BIOLOGICAL_PROCESS,
            Strings.BIOLOGICAL_PROCESS_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.BIOLOGICAL_PROCESS;
        }
    },

    /**
     * Catalytic activity term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    CATALYTIC_ACTIVITY(2,
            Strings.CATALYTIC_ACTIVITY,
            Strings.CATALYTIC_ACTIVITY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Cell secretion term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    CELL_SECRETION(3,
            Strings.CELL_SECRETION,
            Strings.CELL_SECRETION_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Cell surface expression term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    CELL_SURFACE_EXPRESSION(4,
            Strings.CELL_SURFACE_EXPRESSION,
            Strings.CELL_SURFACE_EXPRESSION_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Chaperone activity term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    CHAPERONE_ACTIVITY(5,
            Strings.CHAPERONE_ACTIVITY,
            Strings.CHAPERONE_ACTIVITY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Complex abundance term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#COMPLEX_ABUNDANCE}</dd>
     * </dl>
     */
    COMPLEX_ABUNDANCE(6,
            Strings.COMPLEX_ABUNDANCE,
            Strings.COMPLEX_ABUNDANCE_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.COMPLEX_ABUNDANCE;
        }
    },

    /**
     * Composite abundance term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    COMPOSITE_ABUNDANCE(7,
            Strings.COMPOSITE_ABUNDANCE,
            Strings.COMPOSITE_ABUNDANCE_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Degradation term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    DEGRADATION(8, Strings.DEGRADATION, Strings.DEGRADATION_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Fusion term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#PROTEIN_ABUNDANCE}</dd>
     * </dl>
     */
    FUSION(9, Strings.FUSION, Strings.FUSION_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.PROTEIN_ABUNDANCE;
        }
    },

    /**
     * Gene abundance term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#GENE_ABUNDANCE}</dd>
     * </dl>
     */
    GENE_ABUNDANCE(10,
            Strings.GENE_ABUNDANCE,
            Strings.GENE_ABUNDANCE_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.GENE_ABUNDANCE;
        }
    },

    /**
     * GTP-bound activity term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    GTP_BOUND_ACTIVITY(11,
            Strings.GTP_BOUND_ACTIVITY,
            Strings.GTP_BOUND_ACTIVITY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Kinase activity term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    KINASE_ACTIVITY(12,
            Strings.KINASE_ACTIVITY,
            Strings.KINASE_ACTIVITY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * MicroRNA abundance term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#MICRORNA_ABUNDANCE}</dd>
     * </dl>
     */
    MICRORNA_ABUNDANCE(13,
            Strings.MICRO_RNA_ABUNDANCE,
            Strings.MICRO_RNA_ABUNDANCE_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.MICRORNA_ABUNDANCE;
        }
    },

    /**
     * Molecular activity term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    MOLECULAR_ACTIVITY(14,
            Strings.MOLECULAR_ACTIVITY,
            Strings.MOLECULAR_ACTIVITY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Pathology term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#PATHOLOGY}</dd>
     * </dl>
     */
    PATHOLOGY(15, Strings.PATHOLOGY, Strings.PATHOLOGY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.PATHOLOGY;
        }
    },

    /**
     * Peptidase activity term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    PEPTIDASE_ACTIVITY(16,
            Strings.PEPTIDASE_ACTIVITY,
            Strings.PEPTIDASE_ACTIVITY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Phosphatase activity term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    PHOSPHATASE_ACTIVITY(17,
            Strings.PHOSPHATASE_ACTIVITY,
            Strings.PHOSPHATASE_ACTIVITY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Products term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#PRODUCTS}</dd>
     * </dl>
     */
    PRODUCTS(18, Strings.PRODUCTS) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.PRODUCTS;
        }
    },

    /**
     * Protein abundance term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#PROTEIN_ABUNDANCE}</dd>
     * </dl>
     */
    PROTEIN_ABUNDANCE(19,
            Strings.PROTEIN_ABUNDANCE,
            Strings.PROTEIN_ABUNDANCE_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.PROTEIN_ABUNDANCE;
        }
    },

    /**
     * ProteinModification term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#PROTEIN_MODIFICATION}</dd>
     * </dl>
     */
    PROTEIN_MODIFICATION(20,
            Strings.PROTEIN_MODIFICATION,
            Strings.PROTEIN_MODIFICATION_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.PROTEIN_MODIFICATION;
        }
    },

    /**
     * Reactants term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#REACTANTS}</dd>
     * </dl>
     */
    REACTANTS(21, Strings.REACTANTS) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.REACTANTS;
        }
    },

    /**
     * Reaction term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    REACTION(22, Strings.REACTION, Strings.REACTION_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Ribosylation activity term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    RIBOSYLATION_ACTIVITY(23,
            Strings.RIBOSYLATION_ACTIVITY,
            Strings.RIBOSYLATION_ACTIVITY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * RNA abundance term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#GENE_ABUNDANCE}</dd>
     * </dl>
     */
    RNA_ABUNDANCE(24, Strings.RNA_ABUNDANCE, Strings.RNA_ABUNDANCE_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.GENE_ABUNDANCE;
        }
    },

    /**
     * Substitution term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#SUBSTITUTION}</dd>
     * </dl>
     */
    SUBSTITUTION(25,
            Strings.SUBSTITUTION,
            Strings.SUBSTITUTION_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.SUBSTITUTION;
        }
    },

    /**
     * List term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#LIST}</dd>
     * </dl>
     */
    LIST(26, Strings.LIST) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.LIST;
        }
    },

    /**
     * Transcriptional activity term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    TRANSCRIPTIONAL_ACTIVITY(27,
            Strings.TRANSCRIPTIONAL_ACTIVITY,
            Strings.TRANSCRIPTIONAL_ACTIVITY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Translocation term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    TRANSLOCATION(28,
            Strings.TRANSLOCATION,
            Strings.TRANSLOCATION_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Transport activity term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#ABUNDANCE}</dd>
     * </dl>
     */
    TRANSPORT_ACTIVITY(29,
            Strings.TRANSPORT_ACTIVITY,
            Strings.TRANSPORT_ACTIVITY_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.ABUNDANCE;
        }
    },

    /**
     * Truncation term function.
     * <dl>
     * <dt>Function return type:</dt>
     * <dd>{@link ReturnType#TRUNCATION}</dd>
     * </dl>
     */
    TRUNCATION(30, Strings.TRUNCATION, Strings.TRUNCATION_ABBREV) {
        /**
         * {@inheritDoc}
         */
        @Override
        public ReturnType getReturnType() {
            return ReturnType.TRUNCATION;
        }
    };

    private static final Map<String, FunctionEnum> DISPVAL_TO_ENUM;
    private static final Map<String, FunctionEnum> ABBR_TO_ENUM;
    private static final Map<Integer, FunctionEnum> VALUETOENUM;
    private static final Map<FunctionEnum, Function> FUNCTIONS;
    private static final Set<FunctionEnum> PROTEIN_DECORATION_FUNCTIONS;
    private static final Set<FunctionEnum> ABUNDANCE_FUNCTIONS;
    private static final Set<FunctionEnum> ACTIVITY_FUNCTIONS;
    private static final Set<FunctionEnum> MUTATION_FUNCTIONS;

    static {
        final int length = values().length;
        DISPVAL_TO_ENUM = constrainedHashMap(length);
        VALUETOENUM = constrainedHashMap(length);
        FUNCTIONS = constrainedHashMap(length);

        int abbrs = 0;
        for (final FunctionEnum e : values()) {
            if (e.abbreviation != null) {
                abbrs++;
            }
        }
        ABBR_TO_ENUM = constrainedHashMap(abbrs);

        for (final FunctionEnum e : values()) {
            DISPVAL_TO_ENUM.put(e.displayValue, e);
            VALUETOENUM.put(e.value, e);
            FUNCTIONS.put(e, getFunction(e));
            if (e.abbreviation != null) {
                ABBR_TO_ENUM.put(e.abbreviation, e);
            }
        }

        final Set<FunctionEnum> decorators = constrainedHashSet(4);
        decorators.add(PROTEIN_MODIFICATION);
        decorators.add(SUBSTITUTION);
        decorators.add(TRUNCATION);
        decorators.add(FUSION);
        PROTEIN_DECORATION_FUNCTIONS = unmodifiableSet(decorators);

        final Set<FunctionEnum> abundances = constrainedHashSet(7);
        abundances.add(ABUNDANCE);
        abundances.add(COMPLEX_ABUNDANCE);
        abundances.add(COMPOSITE_ABUNDANCE);
        abundances.add(GENE_ABUNDANCE);
        abundances.add(MICRORNA_ABUNDANCE);
        abundances.add(PROTEIN_ABUNDANCE);
        abundances.add(RNA_ABUNDANCE);
        ABUNDANCE_FUNCTIONS = unmodifiableSet(abundances);

        final Set<FunctionEnum> activities = constrainedHashSet(10);
        activities.add(CATALYTIC_ACTIVITY);
        activities.add(CHAPERONE_ACTIVITY);
        activities.add(GTP_BOUND_ACTIVITY);
        activities.add(KINASE_ACTIVITY);
        activities.add(MOLECULAR_ACTIVITY);
        activities.add(PEPTIDASE_ACTIVITY);
        activities.add(PHOSPHATASE_ACTIVITY);
        activities.add(RIBOSYLATION_ACTIVITY);
        activities.add(TRANSCRIPTIONAL_ACTIVITY);
        activities.add(TRANSPORT_ACTIVITY);
        ACTIVITY_FUNCTIONS = unmodifiableSet(activities);

        final Set<FunctionEnum> mutations = constrainedHashSet(3);
        mutations.add(SUBSTITUTION);
        mutations.add(TRUNCATION);
        mutations.add(FUSION);
        MUTATION_FUNCTIONS = unmodifiableSet(mutations);
    }

    /**
     * Enumeration abbreviation.
     */
    private String abbreviation;

    /**
     * Enumeration display value.
     */
    private String displayValue;

    /**
     * Value unique to each enumeration.
     */
    private final Integer value;

    /**
     * Constructor for setting enum, display value, and abbreviation.
     *
     * @param val Enum value
     * @param dispVal Display value
     * @param abbr Abbreviation
     */
    private FunctionEnum(Integer val, String dispVal, String abbr) {
        this.value = val;
        this.displayValue = dispVal;
        this.abbreviation = abbr;
    }

    /**
     * Constructor for setting enum and display value.
     *
     * @param val Enum value
     * @param dispVal Display value
     */
    private FunctionEnum(Integer val, String dispVal) {
        this.value = val;
        this.displayValue = dispVal;
    }

    /**
     * Returns the function enum's abbreviation.
     *
     * @return abbreviation; may be null
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Returns the long form of the function name.
     *
     * @return display value
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Returns the shortest form of the function.  This will generally be the
     * value of {@link FunctionEnum#getAbbreviation()}, but will be
     * {@link FunctionEnum#getDisplayValue()} if the abbreviated value is
     * {@code null}.
     *
     * @return {@link String}
     */
    public String getShortestForm() {
        return abbreviation != null ? abbreviation : displayValue;
    }

    /**
     * Returns this function enum's {@link ReturnType return type}.
     *
     * @return ReturnType
     */
    public ReturnType getReturnType() {
        throw new AbstractMethodError();
    }

    /**
     * Returns the function enum's value.
     *
     * @return value
     * @see java.lang.Enum#ordinal() Contrast with {@code ordinal}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return displayValue;
    }

    /**
     * Returns this function enum's language function.
     *
     * @return {@link Function}
     */
    public Function getFunction() {
        return FUNCTIONS.get(this);
    }

    /**
     * Returns the function enum by its string representation, which may be
     * null.
     *
     * @param s Function enum string representation
     * @return FunctionEnum- null if the provided string has no function
     * representation
     * @see #getFunctionEnum(String)
     */
    public static FunctionEnum fromString(final String s) {
        return getFunctionEnum(s);
    }

    /**
     * Returns the function enum for the <tt>value</tt> representation.
     *
     * @param value {@link Integer}, the value for the function enum
     * @return {@link FunctionEnum}, the function enum, or null if:
     * <ul>
     * <li><tt>value</tt> parameter is null</li>
     * <li><tt>value</tt> parameter does not map to a function enum</li>
     * </ul>
     */
    public static FunctionEnum fromValue(final Integer value) {
        if (value == null) {
            return null;
        }

        return VALUETOENUM.get(value);
    }

    /**
     * Returns the function enum's language function.
     *
     * @param e Function enum
     * @return {@link Function}
     */
    public static Function getFunction(final FunctionEnum e) {
        switch (e) {
        case ABUNDANCE:
            return new Abundance();
        case BIOLOGICAL_PROCESS:
            return new BiologicalProcess();
        case CATALYTIC_ACTIVITY:
            return new CatalyticActivity();
        case CELL_SECRETION:
            return new CellSecretion();
        case CELL_SURFACE_EXPRESSION:
            return new CellSurfaceExpression();
        case CHAPERONE_ACTIVITY:
            return new ChaperoneActivity();
        case COMPLEX_ABUNDANCE:
            return new ComplexAbundance();
        case COMPOSITE_ABUNDANCE:
            return new CompositeAbundance();
        case DEGRADATION:
            return new Degradation();
        case FUSION:
            return new Fusion();
        case GENE_ABUNDANCE:
            return new GeneAbundance();
        case GTP_BOUND_ACTIVITY:
            return new GTPBoundActivity();
        case KINASE_ACTIVITY:
            return new KinaseActivity();
        case MICRORNA_ABUNDANCE:
            return new MicroRNAAbundance();
        case MOLECULAR_ACTIVITY:
            return new MolecularActivity();
        case PATHOLOGY:
            return new Pathology();
        case PEPTIDASE_ACTIVITY:
            return new PeptidaseActivity();
        case PHOSPHATASE_ACTIVITY:
            return new PhosphataseActivity();
        case PRODUCTS:
            return new Products();
        case PROTEIN_ABUNDANCE:
            return new ProteinAbundance();
        case PROTEIN_MODIFICATION:
            return new ProteinModification();
        case REACTANTS:
            return new Reactants();
        case REACTION:
            return new Reaction();
        case RIBOSYLATION_ACTIVITY:
            return new RibosylationActivity();
        case RNA_ABUNDANCE:
            return new RNAAbundance();
        case SUBSTITUTION:
            return new Substitution();
        case TRANSCRIPTIONAL_ACTIVITY:
            return new TranscriptionalActivity();
        case TRANSLOCATION:
            return new Translocation();
        case TRANSPORT_ACTIVITY:
            return new TransportActivity();
        case TRUNCATION:
            return new Truncation();
        case LIST:
            return new TermList();
        default:
            throw new UnsupportedOperationException(e.toString());
        }
    }

    /**
     * Returns {@code true} if {@code count} is a valid number of arguments for
     * the enum's {@link #getFunction() function}.
     * <p>
     * This method simply delegates to {@link Function#validArgumentCount(int)}.
     * </p>
     *
     * @param e Function enum
     * @param count Argument count
     * @return boolean
     */
    public static boolean isValidArgumentCount(final FunctionEnum e, int count) {
        return e.getFunction().validArgumentCount(count);
    }

    /**
     * Returns {@code true} if {@code count} is a valid number of arguments for
     * the {@link #getFunction() function}.
     * <p>
     * This method simply delegates to {@link Function#validArgumentCount(int)}.
     * </p>
     *
     * @param count Argument count
     * @return boolean
     */
    public boolean isValidArgumentCount(int count) {
        return getFunction().validArgumentCount(count);
    }

    /**
     * Returns the function enum by its display or abbreviation string
     * representation (case-insensitive), which may be null.
     * <p>
     * This method is favored in place of {@link #fromString(String)} as it
     * provides disambiguation with other enums when used as a static import.
     * </p>
     *
     * @param s Function enum string representation
     * @return FunctionEnum - null if the provided string has no function
     * representation
     */
    public static FunctionEnum getFunctionEnum(final String s) {
        FunctionEnum e = DISPVAL_TO_ENUM.get(s);
        if (e != null) {
            return e;
        }
        e = ABBR_TO_ENUM.get(s);
        if (e != null) {
            return e;
        }

        for (final String val : DISPVAL_TO_ENUM.keySet()) {
            if (val.equalsIgnoreCase(s)) return DISPVAL_TO_ENUM.get(val);
        }

        for (final String val : ABBR_TO_ENUM.keySet()) {
            if (val.equalsIgnoreCase(s)) return ABBR_TO_ENUM.get(val);
        }

        return null;
    }

    /**
     * Returns the set of {@link FunctionEnum functions} that are considered
     * protein <i>decorators</i>.
     * <p>
     * This set contains:
     * <ul>
     * <li>{@link #PROTEIN_MODIFICATION}</li>
     * <li>{@link #SUBSTITUTION}</li>
     * <li>{@link #TRUNCATION}</li>
     * </ul>
     * </p>
     *
     * @return an unmodifiable {@link Set} of protein decoration
     * {@link FunctionEnum functions}
     */
    public static Set<FunctionEnum> getProteinDecorators() {
        return PROTEIN_DECORATION_FUNCTIONS;
    }

    /**
     * Returns the set of {@link FunctionEnum functions} that are considered
     * abundance functions.
     * <p>
     * This set contains:
     * <ul>
     * <li>{@link #ABUNDANCE}</li>
     * <li>{@link #COMPLEX_ABUNDANCE}</li>
     * <li>{@link #COMPOSITE_ABUNDANCE}</li>
     * <li>{@link #GENE_ABUNDANCE}</li>
     * <li>{@link #MICRORNA_ABUNDANCE}</li>
     * <li>{@link #PROTEIN_ABUNDANCE}</li>
     * <li>{@link #RNA_ABUNDANCE}</li>
     * </ul>
     * </p>
     *
     * @return an unmodifiable {@link Set} of abundance {@link FunctionEnum
     * functions}
     */
    public static Set<FunctionEnum> getAbundances() {
        return ABUNDANCE_FUNCTIONS;
    }

    /**
     * Returns the set of {@link FunctionEnum functions} that are considered
     * activity functions.
     * <p>
     * This set contains:
     * <ul>
     * <li>{@link #CATALYTIC_ACTIVITY}</li>
     * <li>{@link #CHAPERONE_ACTIVITY}</li>
     * <li>{@link #GTP_BOUND_ACTIVITY}</li>
     * <li>{@link #KINASE_ACTIVITY}</li>
     * <li>{@link #MOLECULAR_ACTIVITY}</li>
     * <li>{@link #PEPTIDASE_ACTIVITY}</li>
     * <li>{@link #PHOSPHATASE_ACTIVITY}</li>
     * <li>{@link #RIBOSYLATION_ACTIVITY}</li>
     * <li>{@link #TRANSCRIPTIONAL_ACTIVITY}</li>
     * <li>{@link #TRANSPORT_ACTIVITY}</li>
     * </ul>
     * </p>
     *
     * @return an unmodifiable {@link Set} of activity {@link FunctionEnum
     * functions}
     */
    public static Set<FunctionEnum> getActivities() {
        return ACTIVITY_FUNCTIONS;
    }

    /**
     * Returns the set of {@link FunctionEnum functions} that are considered
     * mutation functions.
     * <p>
     * This set contains:
     * <ul>
     * <li>{@link #SUBSTITUTION}</li>
     * <li>{@link #TRUNCATION}</li>
     * <li>{@link #FUSION}</li>
     * </ul>
     * </p>
     *
     * @return an unmodifiable {@link Set} of mutation {@link FunctionEnum
     * functions}
     */
    public static Set<FunctionEnum> getMutations() {
        return MUTATION_FUNCTIONS;
    }

    /**
     * Returns {@code true} if this {@link FunctionEnum function} is considered
     * a protein <i>decorator</i>, {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isProteinDecorator() {
        return isProteinDecorator(this);
    }

    /**
     * Returns {@code true} if the {@link FunctionEnum function} is considered a
     * protein <i>decorator</i>, {@code false} otherwise.
     *
     * @param f {@link FunctionEnum}, the function, which may be null
     * @return boolean
     */
    public static boolean isProteinDecorator(final FunctionEnum f) {
        if (f == null) {
            return false;
        }
        switch (f) {
        case PROTEIN_MODIFICATION:
        case SUBSTITUTION:
        case TRUNCATION:
        case FUSION:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if this {@link FunctionEnum function} is considered
     * a mutation, {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isMutation() {
        return isMutation(this);
    }

    /**
     * Returns {@code true} if the {@link FunctionEnum function} is considered a
     * mutation, {@code false} otherwise.
     *
     * @param f {@link FunctionEnum}, the function, which may be null
     * @return boolean
     */
    public static boolean isMutation(final FunctionEnum f) {
        if (f == null) {
            return false;
        }
        switch (f) {
        case SUBSTITUTION:
        case TRUNCATION:
        case FUSION:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if this function's arguments follow a logical order,
     * {@code false} if order is not important.
     *
     * @return boolean
     */
    public boolean isSequential() {
        return isSequential(this);
    }

    /**
     * Returns {@code true} if the {@link FunctionEnum function's} argument
     * follow a logical order, {@code false} if order is not important.
     *
     * @param f {@link FunctionEnum}, the function, which may be null
     * @return boolean
     */
    public static boolean isSequential(final FunctionEnum f) {
        if (f == null) {
            return false;
        }
        switch (f) {
        case COMPLEX_ABUNDANCE:
        case COMPOSITE_ABUNDANCE:
        case PRODUCTS:
        case REACTANTS:
        case LIST:
            return false;
        default:
            return true;
        }
    }

    /**
     * Returns {@code true} if this function is considered a translocating
     * function, {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isTranslocating() {
        return isTranslocating(this);
    }

    /**
     * Returns {@code true} if the {@link FunctionEnum function} is considered a
     * translocating function, {@code false} otherwise.
     *
     * @param f {@link FunctionEnum}, the function, which may be null
     * @return boolean
     */
    public static boolean isTranslocating(final FunctionEnum f) {
        if (f == null) {
            return false;
        }
        switch (f) {
        case TRANSLOCATION:
        case CELL_SECRETION:
        case CELL_SURFACE_EXPRESSION:
            return true;
        default:
            return false;
        }

    }
}
