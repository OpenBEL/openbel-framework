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
 * Enumerated representation of amino acid.
 * <p>
 * Portions of this enum have been automatically generated from <a
 * href="http://en.wikipedia.org/wiki/Amino_acid">here</a>.
 * </p>
 *
 * @see CovalentModification#isAcetylated(AminoAcid)
 * @see CovalentModification#isFarnesylated(AminoAcid)
 * @see CovalentModification#isGlycolsylated(AminoAcid)
 * @see CovalentModification#isHydroxylated(AminoAcid)
 * @see CovalentModification#isMethylated(AminoAcid)
 * @see CovalentModification#isPhosphorylated(AminoAcid)
 * @see CovalentModification#isRibosylated(AminoAcid)
 * @see CovalentModification#isSumoylated(AminoAcid)
 * @see CovalentModification#isUbiquitinated(AminoAcid)
 */
public enum AminoAcid {

    /**
     * <p>
     * Alanine (abbreviated as Ala or A) is an amino acid with the chemical
     * formula CH3CH(NH2)COOH. The L-isomer is one of the 22 proteinogenic amino
     * acids, i.e., the building blocks of proteins. Its codons are GCU, GCC,
     * GCA, and GCG. It is classified as a nonpolar amino acid. L-Alanine is
     * second only to leucine in rate of occurrence, accounting for 7.8% of the
     * primary structure in a sample of 1,150 proteins. D-Alanine occurs in
     * bacterial cell walls and in some peptide antibiotics.
     * </p>
     */
    ALANINE(0, "Alanine", "Ala", "A"),

    /**
     * <p>
     * Arginine (abbreviated as Arg or R) is an amino acid. The L-form is one
     * of the 20 most common natural amino acids. At the level of molecular
     * genetics, in the structure of the messenger ribonucleic acid mRNA, CGU,
     * CGC, CGA, CGG, AGA, and AGG, are the triplets of nucleotide bases or
     * codons that codify for arginine during protein synthesis. In mammals,
     * arginine is classified as a semiessential or conditionally essential
     * amino acid, depending on the developmental stage and health status of the
     * individual. Preterm infants are unable to synthesize or create arginine
     * internally, making the amino acid nutritionally essential for them.
     * Arginine was first isolated from a lupin seedling extract in 1886 by the
     * Swiss chemist Ernst Schultze. In general, most people do not need to take
     * arginine supplements because the body usually produces enough.
     * </p>
     */
    ARGININE(1, "Arginine", "Arg", "R"),

    /**
     * <p>
     * Asparagine (abbreviated as Asn or N; Asx or B represent either asparagine
     * or aspartic acid) is one of the 20 most common natural amino acids on
     * Earth. It has carboxamide as the side-chain's functional group. It is not
     * an essential amino acid. Its codons are AAU and AAC. A reaction between
     * asparagine and reducing sugars or reactive carbonyls produces acrylamide
     * (acrylic amide) in food when heated to sufficient temperature. These
     * products occur in baked goods such as French fries, potato chips, and
     * roasted coffee.
     * </p>
     */
    ASPARAGINE(2, "Asparagine", "Asn", "N"),

    /**
     * <p>
     * Aspartic acid (abbreviated as Asp or D; Asx or B represent either
     * aspartic acid or asparagine) is an amino acid with the chemical formula
     * HOOCCH(NH2)CH2COOH. The carboxylate anion, salt, or ester of aspartic
     * acid is known as aspartate. The L-isomer of aspartate is one of the 20
     * proteinogenic amino acids, i.e., the building blocks of proteins. Its
     * codons are GAU and GAC.
     * <p>
     * Aspartic acid is, together with glutamic acid, classified as an acidic
     * amino acid with a pKa of 4.0. Aspartate is pervasive in biosynthesis. As
     * with all amino acids, the presence of acid protons depends on the
     * residue's local chemical environment and the pH of the solution.
     * </p>
     */
    ASPARTIC_ACID(3, "Aspartic acid", "Asp", "D"),

    /**
     * <p>
     * Cysteine (abbreviated as Cys or C) is an amino acid with the chemical
     * formula HO2CCH(NH2)CH2SH. It is a non-essential amino acid, which means
     * that it is biosynthesized in humans. Its codons are UGU and UGC. The side
     * chain on cysteine is thiol, which is nonpolar and thus cysteine is
     * usually classified as a hydrophobic amino acid. The thiol side chain
     * often participates in enzymatic reactions, serving as a nucleophile. The
     * thiol is susceptible to oxidization to give the disulfide derivative
     * cystine, which serves an important structural role in many proteins.
     * Cysteine is named after cystine.
     * </p>
     */
    CYSTEINE(4, "Cysteine", "Cys", "C"),

    /**
     * <p>
     * Glutamic acid (abbreviated as Glu or E) is one of the 20 proteinogenic
     * amino acids, and its codons are GAA and GAG. It is a non-essential amino
     * acid. The carboxylate anions and salts of glutamic acid are known as
     * glutamates. In neuroscience, glutamate is an important neurotransmitter
     * that plays a key role in long-term potentiation and is important for
     * learning and memory.
     * </p>
     */
    GLUTAMIC_ACID(5, "Glutamic acid", "Glu", "E"),

    /**
     * <p>
     * Glutamine (abbreviated as Gln or Q) is one of the 20 amino acids encoded
     * by the standard genetic code. It is not recognized as an essential amino
     * acid but may become conditionally essential in certain situations,
     * including intensive athletic training or certain gastrointestinal
     * disorders. Its side-chain is an amide formed by replacing the side-chain
     * hydroxyl of glutamic acid with an amine functional group. Therefore, it
     * can be considered the amide of glutamic acid. Its codons are CAA and CAG.
     * In human blood, glutamine is the most abundant free amino acid, with a
     * concentration of about 500-900 umol/l.
     * </p>
     */
    GLUTAMINE(6, "Glutamine", "Gln", "Q"),

    /**
     * <p>
     * Glycine (abbreviated as Gly or G) is an organic compound with the formula
     * NH2CH2COOH. With only two hydrogen atoms as its 'side chain', glycine is
     * the smallest of the 20 amino acids commonly found in proteins. Its codons
     * are GGU, GGC, GGA, GGG. Glycine is a colourless, sweet-tasting
     * crystalline solid. It is unique among the proteinogenic amino acids in
     * that it is not chiral. It can fit into hydrophilic or hydrophobic
     * environments, due to its two hydrogen atom side chain.
     * </p>
     */
    GLYCINE(7, "Glycine", "Gly", "G"),

    /**
     * <p>
     * Histidine (abbreviated as His or H) Histidine, an essential amino acid,
     * has a positively charged imidazole functional group. It is the one of the
     * 22 proteinogenic amino acids. Its codons are CAU and CAC. Histidine was
     * first isolated by German physician Albrecht Kossel in 1896. Histidine is
     * an essential amino acid in humans and other mammals. It was initially
     * thought that it was only essential for infants, but longer-term studies
     * established that it is also essential for adult humans.
     * </p>
     */
    HISTIDINE(8, "Histidine", "His", "H"),

    /**
     * <p>
     * Isoleucine (abbreviated as Ile or I) is an amino acid with the chemical
     * formula HO2CCH(NH2)CH(CH3)CH2CH3. It is an essential amino acid, which
     * means that humans cannot synthesize it, so it must be ingested. Its
     * codons are AUU, AUC and AUA. With a hydrocarbon side chain, isoleucine is
     * classified as a hydrophobic amino acid. Together with threonine,
     * isoleucine is one of two common amino acids that have a chiral side
     * chain. Four stereoisomers of isoleucine are possible, including two
     * possible diastereomers of L-isoleucine. However, isoleucine present in
     * nature exists in one enantiomeric form, (2S,3S)-2-amino-3-methylpentanoic
     * acid.
     * </p>
     */
    ISOLEUCINE(9, "Isoleucine", "Ile", "I"),

    /**
     * <p>
     * Leucine (abbreviated as Leu or L) is a branched-chain amino acid with
     * the chemical formula HO2CCH(NH2)CH2CH(CH3)2. It is an essential amino
     * acid, which means that humans cannot synthesize it. Its codons are UUA,
     * UUG, CUU, CUC, CUA, and CUG. With a hydrocarbon side chain, leucine is
     * classified as a hydrophobic amino acid. It has an isobutyl R group.
     * Leucine is a major component of the sub units in ferritin, astacin and
     * other 'buffer' proteins.
     * </p>
     */
    LEUCINE(10, "Leucine", "Leu", "L"),

    /**
     * <p>
     * Lysine (abbreviated as Lys or K) is an amino acid with the chemical
     * formula HO2CCH(NH2)(CH2)4NH2. It is an essential amino acid, which means
     * that the human body cannot synthesize it. Its codons are AAA and AAG.
     * </p>
     * <p>
     * Lysine is a base, as are arginine and histidine. The amino group often
     * participates in hydrogen bonding and as a general base in catalysis.
     * Common posttranslational modifications include methylation of the amino
     * group, giving methyl-, dimethyl-, and trimethyllysine. The latter occurs
     * in calmodulin. Other posttranslational modifications at lysine residues
     * include acetylation and ubiquitination. Collagen contains hydroxylysine
     * which is derived from lysine by lysyl hydroxylase. O-Glycosylation of
     * hydroxylysine residues in the endoplasmic reticulum or Golgi apparatus is
     * used to mark certain proteins for secretion from the cell.
     * </p>
     */
    LYSINE(11, "Lysine", "Lys", "K"),

    /**
     * <p>
     * Methionine abbreviated as Met or M) is an amino acid with the chemical
     * formula HO2CCH(NH2)CH2CH2SCH3. This essential amino acid is classified as
     * nonpolar.
     * </p>
     */
    METHIONINE(12, "Methionine", "Met", "M"),

    /**
     * <p>
     * Phenylalanine (abbreviated as Phe or F) is an amino acid with the
     * formula C6H5CH2CH(NH2)COOH. This essential amino acid is classified as
     * nonpolar because of the hydrophobic nature of the benzyl side chain.
     * L-Phenylalanine (LPA) is an electrically neutral amino acid, one of the
     * twenty common amino acids used to biochemically form proteins, coded for
     * by DNA. The codons for L-phenylalanine are UUU and UUC. Phenylalanine is
     * a precursor for tyrosine, the monoamine signaling molecules dopamine,
     * norepinephrine (noradrenaline), and epinephrine (adrenaline), and the
     * skin pigment melanin.
     * </p>
     * <p>
     * Phenylalanine is found naturally in the breast milk of mammals. It is
     * used in the manufacture of food and drink products and sold as a
     * nutritional supplement for its reputed analgesic and antidepressant
     * effects. It is a direct precursor to the neuromodulator phenylethylamine,
     * a commonly used dietary supplement.
     * </p>
     */
    PHENYLALANINE(13, "Phenylalanine", "Phe", "F"),

    /**
     * <p>
     * Proline (abbreviated as Pro or P) is an amino acid, one of the twenty
     * DNA-encoded amino acids. Its codons are CCU, CCC, CCA, and CCG. It is not
     * an essential amino acid, which means that the human body can synthesize
     * it. It is unique among the 20 protein-forming amino acids in that the
     * amino group is secondary. The more common L form has S stereochemistry.
     * </p>
     */
    PROLINE(14, "Proline", "Pro", "P"),

    /**
     * <p>
     * Serine (abbreviated as Ser or S) is an amino acid with the formula
     * HO2CCH(NH2)CH2OH.
     * </p>
     */
    SERINE(15, "Serine", "Ser", "S"),

    /**
     * <p>
     * Threonine (abbreviated as Thr or T) is an amino acid with the chemical
     * formula HO2CCH(NH2)CH(OH)CH3. Its codons are ACU, ACA, ACC, and ACG. This
     * essential amino acid is classified as polar. Together with serine,
     * threonine is one of two proteinogenic amino acids bearing an alcohol
     * group (tyrosine is not an alcohol but a phenol, since its hydroxyl group
     * is bonded directly to an aromatic ring, giving it different acid/base and
     * oxidative properties). It is also one of two common amino acids that bear
     * a chiral side chain, along with isoleucine.
     * </p>
     * <p>
     * The threonine residue is susceptible to numerous posttranslational
     * modifications. The hydroxy side-chain can undergo O-linked glycosylation.
     * In addition, threonine residues undergo phosphorylation through the
     * action of a threonine kinase. In its phosphorylated form, it can be
     * referred to as phosphothreonine.
     * </p>
     */
    THREONINE(16, "Threonine", "Thr", "T"),

    /**
     * <p>
     * Tryptophan (IUPAC-IUBMB abbreviation: Trp or W; IUPAC abbreviation: L-Trp
     * or D-Trp; sold for medical use as Tryptan) is one of the 20 standard
     * amino acids, as well as an essential amino acid in the human diet. It is
     * encoded in the standard genetic code as the codon UGG. The slight
     * mispronunciation "tWiptophan" can be used as a mnemonic for its single
     * letter IUPAC code W. Only the L-stereoisomer of tryptophan is used in
     * structural or enzyme proteins, but the D-stereoisomer is occasionally
     * found in naturally produced peptides (for example, the marine venom
     * peptide contryphan). The distinguishing structural characteristic of
     * tryptophan is that it contains an indole functional group. It is an
     * essential amino acid as demonstrated by its growth effects on rats.
     * </p>
     */
    TRYPTOPHAN(17, "Tryptophan", "Trp", "W"),

    /**
     * <p>
     * Tyrosine (abbreviated as Tyr or Y) or 4-hydroxyphenylalanine, is one of
     * the 20 amino acids that are used by cells to synthesize proteins. Its
     * codons are UAC and UAU. It is a non-essential amino acid with a polar
     * side group. The word "tyrosine" is from the Greek tyri, meaning cheese,
     * as it was first discovered in 1846 by German chemist Justus von Liebig in
     * the protein casein from cheese.
     * </p>
     */
    TYROSINE(18, "Tyrosine", "Tyr", "Y"),

    /**
     * <p>
     * Valine (abbreviated as Val or V) is an amino acid with the chemical
     * formula HO2CCH(NH2)CH(CH3)2. L-Valine is one of 20 proteinogenic amino
     * acids. Its codons are GUU, GUC, GUA, and GUG. This essential amino acid
     * is classified as nonpolar. Human dietary sources include cottage cheese,
     * fish, poultry, peanuts, sesame seeds, and lentils. Along with leucine and
     * isoleucine, valine is a branched-chain amino acid. It is named after the
     * plant valerian. In sickle-cell disease, valine substitutes for the
     * hydrophilic amino acid glutamic acid in hemoglobin. Because valine is
     * hydrophobic, the hemoglobin does not fold correctly.
     * </p>
     */
    VALINE(19, "Valine", "Val", "V");

    private final Integer value;
    private String displayValue;
    private String threeLetter;
    private String oneLetter;

    private static final Map<String, AminoAcid> STRINGTOENUM;
    static {
        STRINGTOENUM = sizedHashMap(values().length * 3);
        for (final AminoAcid e : values()) {
            STRINGTOENUM.put(e.displayValue, e);
            STRINGTOENUM.put(e.threeLetter, e);
            STRINGTOENUM.put(e.oneLetter, e);
        }
    }

    /**
     * Constructor for setting enum, display, three-letter, and one-letter
     * values.
     *
     * @param value Enum value
     * @param displayValue Display value
     * @param threeLetter Three-letter value
     * @param oneLetter One-letter value
     */
    private AminoAcid(Integer value, String displayValue, String threeLetter,
            String oneLetter) {
        this.value = value;
        this.displayValue = displayValue;
        this.threeLetter = threeLetter;
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
     * Returns the amino acid's value.
     *
     * @return value
     * @see java.lang.Enum#ordinal() Contrast with {@code ordinal}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Returns the amino acid's display value.
     *
     * @return display value
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Returns the amino acid by its string representation.
     *
     * @param s Amino acid {@link String string} representation
     * @return {@link AminoAcid}, may be null if the provided string has no
     * amino acid representation
     */
    public static AminoAcid getAminoAcid(final String s) {
        AminoAcid e = STRINGTOENUM.get(s);
        if (e != null) return e;

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s))
                return STRINGTOENUM.get(dispval);
        }

        return null;
    }

    /**
     * Returns the one-letter abbreviation of this amino acid.
     *
     * @return {@link String}
     */
    public String getOneLetter() {
        return oneLetter;
    }

    /**
     * Returns the one-letter abbreviation of an amino acid.
     *
     * @param a {@link AminoAcid}
     * @return {@link String}
     */
    public static String getOneLetter(final AminoAcid a) {
        return a.oneLetter;
    }

    /**
     * Returns a set of all the one-letter amino acid abbreviations.
     *
     * @return {@link Set}
     */
    public static Set<String> getOneLetters() {
        final Set<String> ret = sizedHashSet(values().length);
        for (final AminoAcid a : values()) {
            ret.add(a.oneLetter);
        }
        return ret;
    }

    /**
     * Returns the three-letter abbreivation of this amino acid.
     *
     * @return {@link String}
     */
    public String getThreeLetter() {
        return threeLetter;
    }

    /**
     * Returns the three-letter abbreviation of an amino acid.
     *
     * @param a {@link AminoAcid}
     * @return {@link String}
     */
    public static String getThreeLetter(final AminoAcid a) {
        return a.threeLetter;
    }

    /**
     * Returns a {@link Set set} of all the three-letter amino acid
     * abbreviations.
     *
     * @return {@link Set}
     */
    public static Set<String> getThreeLetters() {
        final Set<String> ret = sizedHashSet(values().length);
        for (final AminoAcid a : values()) {
            ret.add(a.threeLetter);
        }
        return ret;
    }

    /**
     * Returns {@code true} if this amino acid's side-chain polarity is polar,
     * {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isPolar() {
        return isPolar(this);
    }

    /**
     * Returns {@code true} if the amino acid's side-chain polariy is polar,
     * {@code false} otherwise.
     *
     * @param a {@link AminoAcid}
     * @return boolean
     */
    public static boolean isPolar(final AminoAcid a) {
        switch (a) {
        case ASPARAGINE:
        case ASPARTIC_ACID:
        case GLUTAMIC_ACID:
        case GLUTAMINE:
        case HISTIDINE:
        case LYSINE:
        case SERINE:
        case THREONINE:
        case TYROSINE:
            return true;
        default:
            return false;

        }
    }

    /**
     * Returns {@code true} if this amino acid's side-chain polarity is
     * non-polar, {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isNonPolar() {
        return isNonPolar(this);
    }

    /**
     * Returns {@code true} if the amino acid's side-chain polariy is non-polar,
     * {@code false} otherwise.
     *
     * @param a {@link AminoAcid}
     * @return boolean
     */
    public static boolean isNonPolar(final AminoAcid a) {
        switch (a) {
        case ALANINE:
        case ARGININE:
        case CYSTEINE:
        case GLYCINE:
        case ISOLEUCINE:
        case LEUCINE:
        case METHIONINE:
        case PHENYLALANINE:
        case PROLINE:
        case TRYPTOPHAN:
        case VALINE:
            return true;
        default:
            return false;
        }
    }
}
