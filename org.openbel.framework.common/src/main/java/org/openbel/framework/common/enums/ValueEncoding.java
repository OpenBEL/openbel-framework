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
package org.openbel.framework.common.enums;

import static java.lang.Character.toUpperCase;
import static java.lang.System.out;
import static org.openbel.framework.common.BELUtilities.constrainedHashMap;

import java.util.Map;

/**
 * Enumerated representation of a namespace value's encoding.
 * <p>
 * A namespace value's encoding defines which functions it is allowed in. The
 * list of value encodings and their respective single-character discriminators
 * follows:
 * <ol>
 * <li><i>biologicalProcess</i>: {@code B}</li>
 * <li><i>pathology</i>: {@code O}</li>
 * <li><i>rnaAbundance</i>: {@code R}</li>
 * <li><i>microRNAAbundance</i>: {@code M}</li>
 * <li><i>proteinAbundance</i>: {@code P}</li>
 * <li><i>geneAbundance</i>: {@code G}</li>
 * <li><i>abundance</i>: {@code A}</li>
 * <li><i>complexAbundance</i>: {@code C}</li>
 * </ol>
 * Portions of this enum have been automatically generated.
 * </p>
 */
public enum ValueEncoding {

    /**
     * Indicates the encoded value can participate in functions requiring
     * biological process arguments.
     *
     * @see #PATHOLOGY
     */
    BIOLOGICAL_PROCESS('B', "biologicalProcess") {
        @Override
        public boolean isAssignableFrom(ValueEncoding v) {
            switch (v) {
            case BIOLOGICAL_PROCESS:
            case PATHOLOGY:
                return true;
            default:
                return false;
            }
        }

        @Override
        public boolean isAssignableTo(ValueEncoding v) {
            if (v == BIOLOGICAL_PROCESS) return true;
            return false;
        }
    },

    /**
     * Indicates the encoded value can participate in functions requiring
     * pathology arguments.
     * <p>
     * Pathology encodings are valid for:
     * <ul>
     * <li>{@link #BIOLOGICAL_PROCESS}</li>
     * </ul>
     * </p>
     *
     * @see #BIOLOGICAL_PROCESS
     */
    PATHOLOGY('O', "pathology") {
        @Override
        public boolean isAssignableFrom(ValueEncoding v) {
            if (v == PATHOLOGY) return true;
            return false;
        }

        @Override
        public boolean isAssignableTo(ValueEncoding v) {
            switch (v) {
            case PATHOLOGY:
            case BIOLOGICAL_PROCESS:
                return true;
            default:
                return false;
            }
        }
    },

    /**
     * Indicates the encoded value can participate in functions requiring RNA
     * abundance arguments.
     * <p>
     * RNA abundance encodings are valid for:
     * <ul>
     * <li>{@link #ABUNDANCE}</li>
     * </ul>
     * </p>
     *
     * @see #ABUNDANCE
     * @see #MICRO_RNA_ABUNDANCE
     */
    RNA_ABUNDANCE('R', "rnaAbundance") {
        @Override
        public boolean isAssignableFrom(ValueEncoding v) {
            switch (v) {
            case RNA_ABUNDANCE:
            case MICRO_RNA_ABUNDANCE:
                return true;
            default:
                return false;
            }
        }

        @Override
        public boolean isAssignableTo(ValueEncoding v) {
            switch (v) {
            case RNA_ABUNDANCE:
            case ABUNDANCE:
                return true;
            default:
                return false;
            }
        }
    },

    /**
     * Indicates the encoded value can participate in functions requiring mRNA
     * abundance arguments.
     * <p>
     * MicroRNA abundance encodings are valid for:
     * <ul>
     * <li>{@link #RNA_ABUNDANCE}</li>
     * <li>{@link #ABUNDANCE}</li>
     * </ul>
     * </p>
     *
     * @see #RNA_ABUNDANCE
     * @see #ABUNDANCE
     */
    MICRO_RNA_ABUNDANCE('M', "microRNAAbundance") {
        @Override
        public boolean isAssignableFrom(ValueEncoding v) {
            if (v == MICRO_RNA_ABUNDANCE) return true;
            return false;
        }

        @Override
        public boolean isAssignableTo(ValueEncoding v) {
            switch (v) {
            case MICRO_RNA_ABUNDANCE:
            case RNA_ABUNDANCE:
            case ABUNDANCE:
                return true;
            default:
                return false;
            }
        }
    },

    /**
     * Indicates the encoded value can participate in functions requiring
     * protein abundance arguments.
     * <p>
     * Protein abundance encodings are valid for:
     * <ul>
     * <li>{@link #ABUNDANCE}</li>
     * </ul>
     * </p>
     *
     * @see #ABUNDANCE
     */
    PROTEIN_ABUNDANCE('P', "proteinAbundance") {
        @Override
        public boolean isAssignableFrom(ValueEncoding v) {
            if (v == PROTEIN_ABUNDANCE) return true;
            return false;
        }

        @Override
        public boolean isAssignableTo(ValueEncoding v) {
            switch (v) {
            case PROTEIN_ABUNDANCE:
            case ABUNDANCE:
                return true;
            default:
                return false;
            }
        }
    },

    /**
     * Indicates the encoded value can participate in functions requiring gene
     * abundance arguments.
     * <p>
     * Gene abundance encodings are valid for:
     * <ul>
     * <li>{@link #ABUNDANCE}</li>
     * </ul>
     * </p>
     *
     * @see #ABUNDANCE
     */
    GENE_ABUNDANCE('G', "geneAbundance") {
        @Override
        public boolean isAssignableFrom(ValueEncoding v) {
            if (v == GENE_ABUNDANCE) return true;
            return false;
        }

        @Override
        public boolean isAssignableTo(ValueEncoding v) {
            switch (v) {
            case GENE_ABUNDANCE:
            case ABUNDANCE:
                return true;
            default:
                return false;
            }
        }
    },

    /**
     * Indicates the encoded value can participate in functions requiring
     * abundance arguments.
     *
     * @see #RNA_ABUNDANCE
     * @see #MICRO_RNA_ABUNDANCE
     * @see #PROTEIN_ABUNDANCE
     * @see #GENE_ABUNDANCE
     */
    ABUNDANCE('A', "abundance") {
        @Override
        public boolean isAssignableFrom(ValueEncoding v) {
            switch (v) {
            case ABUNDANCE:
            case RNA_ABUNDANCE:
            case MICRO_RNA_ABUNDANCE:
            case PROTEIN_ABUNDANCE:
            case GENE_ABUNDANCE:
            case COMPLEX_ABUNDANCE:
                return true;
            default:
                return false;
            }
        }

        @Override
        public boolean isAssignableTo(ValueEncoding v) {
            if (v == ABUNDANCE) return true;
            return false;
        }
    },

    /**
     * Indicates the encoded value can participate in functions requiring
     * complex abundance arguments.
     * <p>
     * Complex abundance encodings are valid for:
     * <ul>
     * <li>{@link #ABUNDANCE}</li>
     * </ul>
     * </p>
     *
     * @see #ABUNDANCE
     */
    COMPLEX_ABUNDANCE('C', "complexAbundance") {
        @Override
        public boolean isAssignableFrom(ValueEncoding v) {
            switch (v) {
            case COMPLEX_ABUNDANCE:
                return true;
            default:
                return false;
            }
        }

        @Override
        public boolean isAssignableTo(ValueEncoding v) {
            switch (v) {
            case COMPLEX_ABUNDANCE:
            case ABUNDANCE:
                return true;
            default:
                return false;
            }
        }

    };

    /**
     * String representation of a value encoding supporting all encodings.
     * <p>
     * All value encodings are types of either {@link #ABUNDANCE abundances} or
     * {@link #BIOLOGICAL_PROCESS biological processes}, therefore a value
     * encoding representative of all value encodings is one that is both.
     * </p>
     */
    public final static String ALL_VALUE_ENCODINGS = "AB";

    /* Value unique to each enumeration. */
    private final char value;
    /* Enumeration display value. */
    private String displayValue;

    /* Static cache of enum by string representation (display value). */
    private static final Map<String, ValueEncoding> STRINGTOENUM;
    /* Static cache of enum by character value. */
    private static final Map<Character, ValueEncoding> CHARTOENUM;

    static {
        int count = values().length;
        STRINGTOENUM = constrainedHashMap(count);
        CHARTOENUM = constrainedHashMap(count);
        for (final ValueEncoding e : values()) {
            STRINGTOENUM.put(e.toString(), e);
            final Character c = toUpperCase(e.value);
            CHARTOENUM.put(c, e);
        }
    }

    /**
     * Constructor for setting enum and display value.
     *
     * @param value Enum value
     * @param displayValue Display value
     */
    private ValueEncoding(char value, String displayValue) {
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

    /**
     * Returns the value encoding's unique character value.
     *
     * @return char
     */
    public char getValue() {
        return value;
    }

    /**
     * Returns the value encoding's display value.
     *
     * @return display value
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Returns {@code true} if this value encoding can be assigned to the
     * provided value encoding.
     * <p>
     * In other words, this value encoding can be <i>cast</i> to the provided
     * value encoding; {@code this} <b>is-a</b> {@code v}.
     * </p>
     *
     * @param v ValueEncoding
     * @return boolean
     */
    public boolean isAssignableTo(final ValueEncoding v) {
        throw new AbstractMethodError();
    }

    /**
     * Returns {@code true} if the provided value encoding can be assigned to
     * this value encoding.
     * <p>
     * In other words, the provided value encoding can be <i>cast</i> to this
     * encoding; {@code v} <b>is-a</b> {@code this}.
     * </p>
     *
     * @param v
     * @return boolean
     */
    public boolean isAssignableFrom(final ValueEncoding v) {
        throw new AbstractMethodError();
    }

    /**
     * Returns the value encoding by its string or character value
     * representation (case-insensitive).
     *
     * @param s ValueEncoding string representation
     * @return ValueEncoding, may be null if the provided string has no value
     * encoding representation as either {@link #getDisplayValue()} or
     * {@link #getValue()}
     */
    public static ValueEncoding getValueEncoding(final String s) {
        ValueEncoding e = STRINGTOENUM.get(s);
        if (e != null) {
            return e;
        }

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s))
                return STRINGTOENUM.get(dispval);
        }

        if (s.length() != 1) {
            return null;
        }

        Character c = s.charAt(0);
        c = toUpperCase(c);

        // Fallback to trying character value representations.
        for (final Character cv : CHARTOENUM.keySet()) {
            if (cv.equals(c)) {
                return CHARTOENUM.get(cv);
            }
        }

        return null;
    }

    /**
     * Returns the value encoding by its uppercase {@link Character character}
     * representation.
     *
     * @param c Character; converted to uppercase
     * @return ValueEncoding; may be null if the provided character has no value
     */
    public static ValueEncoding getValueEncoding(final Character c) {
        return CHARTOENUM.get(toUpperCase(c));
    }

    /**
     * Dumps the value encoding relationships built into the enumeration to
     * standard out.
     *
     * @param args
     */
    public static void main(String... args) {
        for (final ValueEncoding v1 : values()) {
            out.println(v1);
            for (final ValueEncoding v2 : values()) {
                if (v1.isAssignableFrom(v2))
                    out.println("\tsubtype " + v2);
                if (v1.isAssignableTo(v2))
                    out.println("\tcan be " + v2);

            }
        }
    }
}
