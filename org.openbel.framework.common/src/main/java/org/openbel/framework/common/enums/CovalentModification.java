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

import static org.openbel.framework.common.BELUtilities.sizedHashMap;
import static org.openbel.framework.common.BELUtilities.sizedHashSet;

import java.util.Map;
import java.util.Set;

/**
 * Enumerated representation of covalent modification.
 * <p>
 * Portions of this enum have been automatically generated.
 * </p>
 */
public enum CovalentModification {

    /**
     * <p>
     * Phosphorylation (abbreviated P) is the addition of a phosphate (PO4)
     * group to a protein or other organic molecule. Phosphorylation activates
     * or deactivates many protein enzymes.
     * </p>
     */
    PHOSPHORYLATION(0, "Phosphorylation", "P"),

    /**
     * <p>
     * Glycosylation (abbreviated P) is the enzymatic process that attaches
     * glycans to proteins, lipids, or other organic molecules. This enzymatic
     * process produces one of the fundamental biopolymers found in cells (along
     * with DNA, RNA, and proteins). Glycosylation is a form of co-translational
     * and post-translational modification. Glycans serve a variety of
     * structural and functional roles in membrane and secreted proteins. The
     * majority of proteins synthesized in the rough ER undergo glycosylation.
     * It is an enzyme-directed site-specific process, as opposed to the
     * non-enzymatic chemical reaction of glycation. Glycosylation is also
     * present in the cytoplasm and nucleus as the O-GlcNAc modification. Five
     * classes of glycans are produced.
     * </p>
     */
    GLYCOSYLATION(1, "Glycosylation", "G"),

    /**
     * <p>
     * Ribosylation (abbreviated R) is the attachment of a ribose or ribosyl
     * group to a molecule, especially to a polypeptide or protein.
     * </p>
     */
    RIBOSYLATION(2, "Ribosylation", "R"),

    /**
     * <p>
     * Acetylation (abbreviated A) describes a reaction that introduces an
     * acetyl functional group into a chemical compound. (Deacetylation is the
     * removal of the acetyl group.)
     * </p>
     */
    ACETYLATION(3, "Acetylation", "A"),

    /**
     * <p>
     * Hydroxylation (abbreviated H) is a chemical process that introduces a
     * hydroxyl group (-OH) into an organic compound. In biochemistry,
     * hydroxylation reactions are often facilitated by enzymes called
     * hydroxylases. Hydroxylation is the first step in the oxidative
     * degradation of organic compounds in air. It is extremely important in
     * detoxification since hydroxylation converts lipophilic compounds into
     * water-soluble (hydrophilic) products that are more readily excreted. Some
     * drugs (e.g. steroids) are activated or deactivated by hydroxylation.
     * </p>
     */
    HYDROXYLATION(4, "Hydroxylation", "H"),

    /**
     * <p>
     * Symoylation (abbreviated S), is the small Ubiquitin-like Modifier or SUMO
     * proteins are a family of small proteins that are covalently attached to
     * and detached from other proteins in cells to modify their function.
     * SUMOylation is a post-translational modification involved in various
     * cellular processes, such as nuclear-cytosolic transport, transcriptional
     * regulation, apoptosis, protein stability, response to stress, and
     * progression through the cell cycle.
     * </p>
     */
    SUMOYLATION(5, "Sumoylation", "S"),

    /**
     * <p>
     * Farnesylation (abbreviated F), or prenylation, or isoprenylation, or
     * lipidation is the addition of hydrophobic molecules to a protein. It is
     * usually assumed that prenyl groups (3-methyl-2-buten-1-yl) facilitate
     * attachment to cell membranes, similar to lipid anchor like the GPI
     * anchor, though direct evidence is missing. Prenyl groups have been shown
     * to be important for protein-protein binding through specialized
     * prenyl-binding domains.
     * </p>
     */
    FARNESYLATION(6, "Farnesylation", "F"),

    /**
     * <p>
     * Methylation (abbreviated M) denotes the addition of a methyl group to a
     * substrate or the substitution of an atom or group by a methyl group.
     * Methylation is a form of alkylation with, to be specific, a methyl group,
     * rather than a larger carbon chain, replacing a hydrogen atom. These terms
     * are commonly used in chemistry, biochemistry, soil science, and the
     * biological sciences.
     * </p>
     */
    METHYLATION(7, "Methylation", "M"),

    /**
     * <p>
     * Ubiquitination (abbreviated U) is a small regulatory protein that has
     * been found in almost all tissues (ubiquitously) of eukaryotic organisms.
     * Among other functions, it directs protein recycling.
     * </p>
     */
    UBIQUITINATION(8, "Ubiquitination", "U");

    private final Integer value;
    private String displayValue;
    private String oneLetter;

    private static final Map<String, CovalentModification> STRINGTOENUM;
    static {
        STRINGTOENUM = sizedHashMap(values().length * 2);
        for (final CovalentModification e : values()) {
            STRINGTOENUM.put(e.displayValue, e);
            STRINGTOENUM.put(e.oneLetter, e);
        }
    }

    /**
     * Constructor for setting enum and display value.
     *
     * @param value Enum value
     * @param displayValue Display value
     * @param oneLetter One-letter value
     */
    private CovalentModification(Integer value, String displayValue,
            String oneLetter) {
        this.value = value;
        this.displayValue = displayValue;
        this.oneLetter = oneLetter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return displayValue;
    }

    /**
     * Returns the covalent modification's value.
     *
     * @return value
     * @see java.lang.Enum#ordinal() Contrast with {@code ordinal}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Returns the covalent modification's display value.
     *
     * @return display value
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Returns the covalent modification by its string representation.
     *
     * @param s CovalentModification string representation
     * @return CovalentModification, may be null if the provided string has no
     * CovalentModification representation
     */
    public static CovalentModification getCovalentModification(final String s) {
        CovalentModification e = STRINGTOENUM.get(s);
        if (e != null) return e;

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s))
                return STRINGTOENUM.get(dispval);
        }

        return null;
    }

    /**
     * Returns the one-letter abbreviation of this covalent modification.
     *
     * @return {@link String}
     */
    public String getOneLetter() {
        return oneLetter;
    }

    /**
     * Returns the one-letter abbreviation of a covalent modification.
     *
     * @param c {@link CovalentModification}
     * @return {@link String}
     */
    public static String getOneLetter(final CovalentModification c) {
        return c.oneLetter;
    }

    /**
     * Returns a set of all the one-letter covalent modification abbreviations.
     *
     * @return {@link Set}
     */
    public static Set<String> getOneLetters() {
        final Set<String> ret = sizedHashSet(values().length);
        for (final CovalentModification c : values()) {
            ret.add(c.oneLetter);
        }
        return ret;
    }

    /**
     * Returns {@code true} if the {@link AminoAcid amino acid} can be
     * acetylated, {@code false} otherwise.
     * <p>
     * Returns {@code true} for the following amino acids:
     * <ul>
     * <li>{@link AminoAcid#LYSINE}</li>
     * </ul>
     * </p>
     *
     * @param acid {@link AminoAcid}
     * @return boolean
     * @see #ACETYLATION
     */
    public static boolean isAcetylated(final AminoAcid acid) {
        switch (acid) {
        case LYSINE:
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the {@link AminoAcid amino acid} can be
     * farnesylated, {@code false} otherwise.
     * <p>
     * Returns {@code true} for the following amino acids:
     * <ul>
     * <li>{@link AminoAcid#CYSTEINE}</li>
     * </ul>
     * </p>
     *
     * @param acid {@link AminoAcid}
     * @return boolean
     * @see #FARNESYLATION
     */
    public static boolean isFarnesylated(final AminoAcid acid) {
        switch (acid) {
        case CYSTEINE:
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the {@link AminoAcid amino acid} can be
     * gycolsylated, {@code false} otherwise.
     * <p>
     * Returns {@code true} for the following amino acids:
     * <ul>
     * <li>{@link AminoAcid#ARGININE}</li>
     * <li>{@link AminoAcid#LYSINE}</li>
     * <li>{@link AminoAcid#PROLINE}</li>
     * <li>{@link AminoAcid#ASPARAGINE}</li>
     * <li>{@link AminoAcid#TYROSINE}</li>
     * <li>{@link AminoAcid#THREONINE}</li>
     * <li>{@link AminoAcid#SERINE}</li>
     * </ul>
     * </p>
     *
     * @param acid {@link AminoAcid}
     * @return boolean
     * @see #GLYCOSYLATION
     */
    public static boolean isGlycolsylated(final AminoAcid acid) {
        switch (acid) {
        case ARGININE:
        case LYSINE:
        case PROLINE:
        case ASPARAGINE:
        case TYROSINE:
        case THREONINE:
        case SERINE:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the {@link AminoAcid amino acid} can be
     * hydroxylated, {@code false} otherwise.
     * <p>
     * Returns {@code true} for the following amino acids:
     * <ul>
     * <li>{@link AminoAcid#LYSINE}</li>
     * <li>{@link AminoAcid#PROLINE}</li>
     * </ul>
     * </p>
     *
     * @param acid {@link AminoAcid}
     * @return boolean
     * @see #HYDROXYLATION
     */
    public static boolean isHydroxylated(final AminoAcid acid) {
        switch (acid) {
        case LYSINE:
        case PROLINE:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the {@link AminoAcid amino acid} can be
     * methylated, {@code false} otherwise.
     * <p>
     * Returns {@code true} for the following amino acids:
     * <ul>
     * <li>{@link AminoAcid#ARGININE}</li>
     * <li>{@link AminoAcid#LYSINE}</li>
     * <li>{@link AminoAcid#GLUTAMIC_ACID}</li>
     * <li>{@link AminoAcid#HISTIDINE}</li>
     * <li>{@link AminoAcid#GLUTAMINE}</li>
     * <li>{@link AminoAcid#ASPARTIC_ACID}</li>
     * </ul>
     * </p>
     *
     * @param acid {@link AminoAcid}
     * @return boolean
     * @see #METHYLATION
     */
    public static boolean isMethylated(final AminoAcid acid) {
        switch (acid) {
        case ARGININE:
        case LYSINE:
        case GLUTAMIC_ACID:
        case HISTIDINE:
        case GLUTAMINE:
        case ASPARTIC_ACID:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the {@link AminoAcid amino acid} can be
     * phosphorylated, {@code false} otherwise.
     * <p>
     * Returns {@code true} for the following amino acids:
     * <ul>
     * <li>{@link AminoAcid#SERINE}</li>
     * <li>{@link AminoAcid#THREONINE}</li>
     * <li>{@link AminoAcid#TYROSINE}</li>
     * <li>{@link AminoAcid#HISTIDINE}</li>
     * </ul>
     * </p>
     *
     * @param acid {@link AminoAcid}
     * @return boolean
     * @see #PHOSPHORYLATION
     */
    public static boolean isPhosphorylated(final AminoAcid acid) {
        switch (acid) {
        case SERINE:
        case THREONINE:
        case TYROSINE:
        case HISTIDINE:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the {@link AminoAcid amino acid} can be
     * ribosylated, {@code false} otherwise.
     * <p>
     * Returns {@code true} for the following amino acids:
     * <ul>
     * <li>{@link AminoAcid#ARGININE}</li>
     * <li>{@link AminoAcid#ASPARTIC_ACID}</li>
     * <li>{@link AminoAcid#GLUTAMIC_ACID}</li>
     * <li>{@link AminoAcid#LYSINE}</li>
     * </ul>
     * </p>
     *
     * @param acid {@link AminoAcid}
     * @return boolean
     * @see #RIBOSYLATION
     */
    public static boolean isRibosylated(final AminoAcid acid) {
        switch (acid) {
        case ARGININE:
        case ASPARTIC_ACID:
        case GLUTAMIC_ACID:
        case LYSINE:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the {@link AminoAcid amino acid} can be
     * sumoylated, {@code false} otherwise.
     * <p>
     * Returns {@code true} for the following amino acids:
     * <ul>
     * <li>{@link #LYSINE}</li>
     * </ul>
     * </p>
     *
     * @param acid {@link AminoAcid}
     * @return boolean
     * @see #SUMOYLATION
     */
    public static boolean isSumoylated(final AminoAcid acid) {
        switch (acid) {
        case LYSINE:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the {@link AminoAcid amino acid} can be
     * ubiquitinated, {@code false} otherwise.
     * <p>
     * Returns {@code true} for the following amino acids:
     * <ul>
     * <li>{@link #LYSINE}</li>
     * </ul>
     * </p>
     *
     * @param acid {@link AminoAcid}
     * @return boolean
     * @see #UBIQUITINATION
     */
    public static boolean isUbiquitinated(final AminoAcid acid) {
        switch (acid) {
        case LYSINE:
            return true;
        default:
            return false;
        }
    }
}
