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
import static org.openbel.framework.common.BELUtilities.*;

import java.util.Map;
import java.util.Set;

/**
 * Enumerated representation of relationship.
 * <p>
 * Portions of this enum have been automatically generated.
 * </p>
 */
public enum RelationshipType {

    /**
     * <h1>{@code A -> B}</h1><br>
     * For terms A and B, {@code A increases B or A -> B} indicates that
     * increases in A have been observed to cause increases in B.
     * {@code A increases B} also represents cases where decreases in A have
     * been observed to cause decreases in B, for example, in recording the
     * results of gene deletion or other inhibition experiments. A is a BEL Term
     * and B is either a BEL Term or a BEL Statement. The relationship does not
     * indicate that the changes in A are either necessary for changes in B, nor
     * does it indicate that changes in A are sufficient to cause changes in B.
     */
    INCREASES(0, "increases", "->"),

    /**
     * <h1>{@code A -| B}</h1><br>
     * For terms A and B, {@code A decreases B or A -| B} indicates that
     * increases in A have been observed to cause decreases in B.
     * {@code A decreases B} also represents cases where decreases in A have
     * been observed to cause increases in B, for example, in recording the
     * results of gene deletion or other inhibition experiments. A is a BEL Term
     * and B is either a BEL Term or a BEL Statement. The relationship does not
     * indicate that the changes in A are either necessary for changes in B, nor
     * does it indicate that changes in A are sufficient to cause changes in B.
     */
    DECREASES(1, "decreases", "-|"),

    /**
     * <h1>{@code A => B}</h1><br>
     * For terms A and B, {@code A directlyIncreases B or A => B} indicate A
     * increases B and that the mechanism of the causal relationship is based on
     * physical interaction of entities related to A and B. This is a direct
     * version of the increases relationship.
     */
    DIRECTLY_INCREASES(2, "directlyIncreases", "=>"),

    /**
     * <h1>{@code A =| B}</h1><br>
     * For terms A and B, {@code A directlyDecreases B or A =| B} indicates A
     * decreases B and that the mechanism of the causal relationship is based on
     * physical interaction of entities related to A and B. This is a direct
     * version of the decreases relationship.
     */
    DIRECTLY_DECREASES(3, "directlyDecreases", "=|"),

    /**
     * <h1>{@code A causesNoChange B}</h1><br>
     * For terms A and B, {@code A causesNoChange B} indicates that B was
     * observed not to change in response to changes in A. Statements using this
     * relationship correspond to cases where explicit measurement of B
     * demonstrates lack of significant change, not for cases where the state of
     * B is unknown.
     */
    CAUSES_NO_CHANGE(4, "causesNoChange"),

    /**
     * <h1>{@code A positiveCorrelation B}</h1><br>
     * For terms A and B, {@code A positiveCorrelation B} indicates that changes
     * in A and B have been observed to be positively correlated, thus B
     * positiveCorrelation A is equivalent to A positiveCorrelation B.
     */
    POSITIVE_CORRELATION(5, "positiveCorrelation"),

    /**
     * <h1>{@code A negativeCorrelation B}</h1><br>
     * For terms A and B, {@code A negativeCorrelation B} indicates that changes
     * in A and B have been observed to be negatively correlated. The order of
     * the subject and object does not affect the interpretation of the
     * statement, thus B negativeCorrelation A is equivalent to A
     * negativeCorrelation B.
     */
    NEGATIVE_CORRELATION(6, "negativeCorrelation"),

    /**
     * <h1>{@code R >> P}</h1><br>
     * For rnaAbundance term R and proteinAbundance term P,
     * {@code R translatedTo P} or {@code R >> P} indicates that members of P
     * are produced by the translation of members of R. For example:
     * {@code r(HGNC:AKT1) >> p(HGNC:AKT1)} indicates that AKT1 protein is
     * produced by translation of AKT1 RNA.
     */
    TRANSLATED_TO(7, "translatedTo", ">>"),

    /**
     * <h1>{@code G :> R}</h1><br>
     * For rnaAbundance term R and geneAbundance term G,
     * {@code G transcribedTo R} or {@code G
     * :> R} indicates that members of R are produced by the transcription of
     * members of G. For example: {@code g(HGNC:AKT1) :> r(HGNC:AKT1)} indicates
     * that the human AKT1 RNA is transcribed from the human AKT1 gene.
     */
    TRANSCRIBED_TO(8, "transcribedTo", ":>"),

    /**
     * <h1>{@code A isA B}</h1><br>
     * For terms A and B, {@code A isA B} indicates that A is a subset of B. All
     * terms in BEL represent classes, but given that classes implicitly have
     * instances, one can also interpret {@code A isA B} to mean that any
     * instance of A must also be an instance of B. This relationship can be
     * used to represent GO and MeSH hierarchies:
     * {@code pathology(MESH:Psoriasis) isA pathology(MESH:"Skin Diseases")}
     */
    IS_A(9, "isA"),

    /**
     * <h1>{@code A subProcessOf B}</h1><br>
     * For process, activity, or transformation term A and process term B,
     * {@code A
     * subProcessOf B} indicates that instances of process B, by default,
     * include one or more instances of A in their composition. For example, the
     * reduction of HMG-CoA to mevalonate is a subprocess of cholesterol
     * biosynthesis:
     *
     * <pre>
     * <code>
     * rxn(reactants(a(CHEBI:"(S)-3-hydroxy-3-methylglutaryl-CoA"),\
     * a(CHEBI:NADPH), a(CHEBI:hydron)), products(a(CHEBI:Mevalonate),\
     * a(CHEBI:"CoA-SH"), a(CHEBI:"NADP+"))) subProcessOf bp(GO:"cholesterol\
     * biosynthetic process")
     * </code>
     * </pre>
     */
    SUB_PROCESS_OF(10, "subProcessOf"),

    /**
     * <h1>{@code A rateLimitingStepOf B}</h1><br>
     * For process, activity, or transformation term A and process term B,
     * {@code A
     * rateLimitingStepOf B} indicates {@code A subProcessOf B} and
     * {@code A -> B}. For example, the catalytic activity of HMG CoA reductase
     * is a rate-limiting step for cholesterol biosynthesis:
     *
     * <pre>
     * <code>
     * cat(p(HGNC:HMGCR)) rateLimitingStepOf\
     * bp(GO:"cholesterol biosynthetic process")
     * </code>
     * </pre>
     */
    RATE_LIMITING_STEP_OF(11, "rateLimitingStepOf"),

    /**
     * <h1>{@code A biomarkerFor P}</h1><br>
     * For term A and process term P, {@code A biomarkerFor P} indicates that
     * changes in or detection of A is used in some way to be a biomarker for
     * pathology or biological process P.
     */
    BIOMARKER_FOR(12, "biomarkerFor"),

    /**
     * <h1>{@code A prognosticBiomarkerFor P}</h1><br>
     * For term A and process term P, {@code A prognosticBiomarkerFor P}
     * indicates that changes in or detection of A is used in some way to be a
     * prognostic biomarker for the subsequent development of pathology or
     * biological process P.
     */
    PROGNOSTIC_BIOMARKER_FOR(13, "prognosticBiomarkerFor"),

    /**
     * <h1>{@code A orthologous B}</h1><br>
     * For geneAbundance terms A and B, {@code A orthologousTo B} indicates that
     * A and B represent abundances of genes in different species which are
     * sequence similar and which are therefore presumed to share a common
     * ancestral gene. For example,
     * {@code g(HGNC:AKT1) orthologousTo g(MGI:AKT1)} indicates that the mouse
     * and human AKT1 genes are orthologous.
     */
    ORTHOLOGOUS(14, "orthologous"),

    /**
     * <h1>{@code A analogous B}</h1><br>
     * For terms A and B, {@code A analogousTo B} indicates that A and B
     * represent abundances or molecular activities in different species which
     * function in a similar manner.
     */
    ANALOGOUS(15, "analogous"),

    /**
     * <h1>{@code A -- B}</h1><br>
     * For terms A and B, {@code A association B} or {@code A -- B} indicates
     * that A and B are associated in an unspecified manner. This relationship
     * is used when not enough information about the association is available to
     * describe it using more specific relationships, like increases or
     * positiveCorrelation.
     */
    ASSOCIATION(16, "association", "--"),

    /**
     * <h1>{@code A hasMembers (B, C, D)}</h1><br>
     * The hasMembers relationship is a special form which enables the
     * assignment of multiple member classes in a single statement where the
     * object of the statement is a set of abundance terms. A statement using
     * hasMembers is exactly equivalent to multiple hasMember statements. A term
     * may not appear in both the subject and object of a of the same hasMembers
     * statement. For the abundance terms A, B, C and D, {@code A hasMembers B,
     * C, D} indicates that A is defined by its member abundance classes B, C
     * and D.
     */
    HAS_MEMBERS(17, "hasMembers"),

    /**
     * <h1>{@code A hasComponents (B, C, D)}</h1><br>
     * The hasComponents relationship is a special form which enables the
     * assignment of multiple complex components in a single statement where the
     * object of the statement is a set of abundance terms. A statement using
     * hasComponents is exactly equivalent to multiple hasComponent statements.
     * A term may not appear in both the subject and object of the same
     * hasComponents statement. For the abundance terms A, B, C and D, {@code A
     * hasComponents B, C, D} indicates that A has components B, C and D.
     */
    HAS_COMPONENTS(18, "hasComponents"),

    /**
     * <h1>{@code A hasMember A1}</h1><br>
     * For term abundances A and B, {@code A hasMember B} designates B as a
     * member class of A. A member class is a distinguished sub-class. A is
     * defined as a group by all of the members assigned to it. The member
     * classes may or may not be overlapping and may or may not entirely cover
     * all instances of A. A term may not appear in both the subject and object
     * of the same hasMember statement.
     */
    HAS_MEMBER(19, "hasMember"),

    /**
     * <h1>{@code A hasComponent A1}</h1><br>
     * For complexAbundance term A and abundance term B,
     * {@code A hasComponent B} designates B as a component of A, that complexes
     * that are instances of A have instances of B as possible components. Note
     * that, the stoichiometry of A is not described, nor is it stated that B is
     * a required component. The use of hasComponent relationships is
     * complementary to the use of functionally composed complexes and is
     * intended to enable the assignment of components to complexes designated
     * by names in external vocabularies. The assignment of components can
     * potentially enable the reconciliation of equivalent complexes at
     * knowledge assembly time.
     */
    HAS_COMPONENT(20, "hasComponent"),

    /**
     * <h1>{@code A actsIn f(A)}</h1><br>
     * This relationship links an abundance term to the activity term for the
     * same abundance. This relationship is direct because it is a <i>self</i>
     * relationship, the abundance acts in its own activity. For protein
     * abundance p(A) and its molecular activity kin(p(A), {@code p(A) actsIn
     * kin(p(A))}. This relationship is introduced by the BEL Compiler and may
     * not be used by statements in BEL documents.
     */
    ACTS_IN(21, "actsIn"),

    /**
     * <h1>{@code compositeAbundance(A,B) includes A}</h1><br>
     * This relationship links each individual abundance term in a
     * {@code compositeAbundance(<list>)} to the compositeAbundance. For
     * example, {@code compositeAbundance(A, B) includes A} and
     * {@code compositeAbundance(A, B) includes B}. This relationship is direct
     * because it is a <i>self</i> relationship. This relationship is introduced
     * by the BEL Compiler and may not be used by statements in BEL documents.
     */
    INCLUDES(22, "includes"),

    /**
     * <h1>{@code translocation(A, ns1:v1, ns2:v2) translocates A}</h1><br>
     * This relationship links the abundance term in a translocation() to the
     * translocation. This relationship is direct because it is a <i>self</i>
     * relationship. The translocated abundance is directly acted on by the
     * translocation process. This relationship is introduced by the BEL
     * Compiler and may not be used by statements in BEL documents.
     */
    TRANSLOCATES(23, "translocates"),

    /**
     * <h1>{@code reaction(reactants(A), products(B)) hasProduct B}</h1><br>
     * This relationship links abundance terms from the {@code products(<list>)}
     * in a reaction to the reaction. This is a direct relationship because it
     * is a <i>self</i> relationship. Products are produced directly by a
     * reaction. This relationship is introduced by the BEL Compiler and may not
     * be used by statements in BEL documents.
     */
    HAS_PRODUCT(24, "hasProduct"),

    /**
     * <h1>{@code A reactantIn reaction(reactants(A), products(B))}</h1><br>
     * This relationship links abundance terms from the
     * {@code reactants(<list>)} in a reaction to the reaction. This is a direct
     * relationship because it is a <i>self</i> relationship. Reactants are
     * consumed directly by a reaction. This relationship is introduced by the
     * BEL Compiler and may not be used by statements in BEL documents.
     */
    REACTANT_IN(25, "reactantIn"),

    /**
     * <h1>{@code p(A) hasModification p(A, pmod(P, S, 473))}</h1><br>
     * This relationship links abundance terms modified by the pmod() function
     * to the unmodified abundance term. This is a direct relationship because
     * it is a <i>self</i> relationship. This relationship is introduced by the
     * BEL Compiler and may not be used by statements in BEL documents.
     */
    HAS_MODIFICATION(26, "hasModification"),

    /**
     * <h1>{@code p(A) hasVariant p(A, sub(G, 12, V))}</h1><br>
     * This relationship links abundance terms modified by the substitution(),
     * fusion(), or truncation() functions to the unmodified abundance term.
     * This relationship is introduced by the BEL Compiler and does not need to
     * be used by statements in BEL documents.
     */
    HAS_VARIANT(27, "hasVariant");

    private final Integer value;
    private final String displayValue;
    private final String abbreviation;

    private static final Map<String, RelationshipType> STRINGTOENUM;
    private static final Map<String, RelationshipType> ABBRTOENUM;
    private static final Map<Integer, RelationshipType> VALUETOENUM;
    private static final Set<RelationshipType> CAUSAL_RELS;
    private static final Set<RelationshipType> DIRECT_RELS;
    private static final Set<RelationshipType> INDIRECT_RELS;
    private static final Set<RelationshipType> CORRELATIVE_RELS;
    private static final Set<RelationshipType> GENOMIC_RELS;
    private static final Set<RelationshipType> DIRECTED_RELS;
    private static final Set<RelationshipType> SELF_RELS;
    private static final Set<RelationshipType> INC_RELS;
    private static final Set<RelationshipType> DEC_RELS;
    private static final Set<RelationshipType> INJ_RELS;

    static {
        // These maps will never resize
        STRINGTOENUM = constrainedHashMap(values().length);
        VALUETOENUM = constrainedHashMap(values().length);

        int abbrs = 0;
        int causals = 0;
        int correlatives = 0;
        int genomics = 0;
        int directs = 0;
        int indirects = 0;
        int selfs = 0;
        int increasing = 0;
        int decreasing = 0;
        int directed = 0;
        int injected = 0;

        for (final RelationshipType r : values()) {
            if (r.isCausal()) {
                causals++;
            }
            if (r.isDirect()) {
                directs++;
            } else {
                indirects++;
            }
            if (r.isCorrelative()) {
                correlatives++;
            }
            if (r.isGenomic()) {
                genomics++;
            }
            if (r.isDirected()) {
                directed++;
            }
            if (r.isSelf()) {
                selfs++;
            }
            if (r.abbreviation != null) {
                abbrs++;
            }
            if (r.isIncreasing()) {
                increasing++;
            }
            if (r.isDecreasing()) {
                decreasing++;
            }
            if (r.isInjected()) {
                injected++;
            }
        }

        Set<RelationshipType> causalRels = sizedHashSet(causals);
        Set<RelationshipType> directRels = sizedHashSet(directs);
        Set<RelationshipType> indirectRels = sizedHashSet(indirects);
        Set<RelationshipType> correlRels = sizedHashSet(correlatives);
        Set<RelationshipType> genomicRels = sizedHashSet(genomics);
        Set<RelationshipType> directedRels = sizedHashSet(directed);
        Set<RelationshipType> selfRels = sizedHashSet(selfs);
        Set<RelationshipType> incRels = sizedHashSet(increasing);
        Set<RelationshipType> decRels = sizedHashSet(decreasing);
        Set<RelationshipType> injRels = sizedHashSet(injected);
        ABBRTOENUM = sizedHashMap(abbrs);

        for (final RelationshipType r : values()) {
            STRINGTOENUM.put(r.toString(), r);
            VALUETOENUM.put(r.value, r);
            if (r.isCausal()) {
                causalRels.add(r);
            }
            if (r.isCorrelative()) {
                correlRels.add(r);
            }
            if (r.isDirect()) {
                directRels.add(r);
            } else {
                indirectRels.add(r);
            }
            if (r.isGenomic()) {
                genomicRels.add(r);
            }
            if (r.isDirected()) {
                directedRels.add(r);
            }
            if (r.isSelf()) {
                selfRels.add(r);
            }
            if (r.abbreviation != null) {
                ABBRTOENUM.put(r.abbreviation, r);
            }
            if (r.isIncreasing()) {
                incRels.add(r);
            }
            if (r.isDecreasing()) {
                decRels.add(r);
            }
            if (r.isInjected()) {
                injRels.add(r);
            }
        }

        // prevent modification to all backing sets
        CAUSAL_RELS = unmodifiableSet(causalRels);
        DIRECT_RELS = unmodifiableSet(directRels);
        INDIRECT_RELS = unmodifiableSet(indirectRels);
        CORRELATIVE_RELS = unmodifiableSet(correlRels);
        GENOMIC_RELS = unmodifiableSet(genomicRels);
        DIRECTED_RELS = unmodifiableSet(directedRels);
        SELF_RELS = unmodifiableSet(selfRels);
        INC_RELS = unmodifiableSet(incRels);
        DEC_RELS = unmodifiableSet(decRels);
        INJ_RELS = unmodifiableSet(injRels);
    }

    /**
     * Constructor for setting enum and display value.
     *
     * @param value Enum value
     * @param displayValue Display value
     */
    private RelationshipType(Integer value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
        this.abbreviation = null;
    }

    /**
     * Constructor for setting enum, display, and abbreviation value.
     *
     * @param value Enum value
     * @param displayValue Display value
     * @param abbrev Abbreviation
     */
    private RelationshipType(Integer value, String displayValue, String abbrev) {
        this.value = value;
        this.displayValue = displayValue;
        this.abbreviation = abbrev;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return displayValue;
    }

    /**
     * Returns the relationship type's value.
     *
     * @return value
     * @see java.lang.Enum#ordinal() Contrast with {@code ordinal}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Returns the relationship type's display value.
     *
     * @return display value
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Returns the relationship type's abbreviation, if one is present.
     *
     * @return abbreviation, which may be null
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Returns the relationship type for the value.
     *
     * @param value {@link Integer}, the value to find an enum instance for
     * @return {@link RelationshipType}, the relationship type, which can be
     * null if:
     * <ul>
     * <li><tt>value</tt> parameter is null</li>
     * <li><tt>value</tt> parameter does not map to a {@link RelationshipType}</li>
     * </ul>
     */
    public static RelationshipType fromValue(Integer value) {
        if (value == null) {
            return null;
        }

        return VALUETOENUM.get(value);
    }

    /**
     * Returns the relationship type by its string representation.
     *
     * @param s Relationship string representation
     * @return RelationshipType
     * @see #getRelationshipType(String)
     */
    public static RelationshipType fromString(final String s) {
        return getRelationshipType(s);
    }

    /**
     * Returns the relationship type by its case-insensitive abbreviation, if
     * one is present.
     *
     * @param s Relationship type abbreviation
     * @return RelationshipType, which may be null
     */
    public static RelationshipType fromAbbreviation(final String s) {
        RelationshipType r = ABBRTOENUM.get(s);
        if (r != null) return r;

        for (final String abbr : ABBRTOENUM.keySet()) {
            if (abbr.equalsIgnoreCase(s)) return ABBRTOENUM.get(abbr);
        }

        return null;
    }

    /**
     * Returns the relationship type by its string representation
     * (case-insensitive).
     * <p>
     * This method is favored in place of {@link #fromString(String)} as it
     * provides disambiguation with other enums when used as a static import.
     * </p>
     *
     * @param s Relationship type string representation
     * @return Relationship type - null if the provided string has no
     * relationship type representation
     */
    public static RelationshipType getRelationshipType(final String s) {
        RelationshipType r = STRINGTOENUM.get(s);
        if (r != null) return r;

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s)) return STRINGTOENUM.get(dispval);
        }

        return null;
    }

    /**
     * Returns {@code true} if the relationship type {@code r} is a
     * <i>causal</i> relationship, and {@code false} otherwise.
     * <p>
     * For further reference, see <i>BEL Language Overview</i>, Appendix B.
     * </p>
     *
     * @param r Relationship type
     * @return boolean
     */
    public static boolean isCausal(final RelationshipType r) {
        return r.isCausal();
    }

    /**
     * Returns {@code true} if this relationship type is a <i>causal</i>
     * relationship, {@code false} otherwise.
     * <p>
     * For further reference, see <i>BEL Language Overview</i>, Appendix B.
     * </p>
     * <p>
     * The set of causal relationships are:
     * <ol>
     * <li>{@link #CAUSES_NO_CHANGE}</li>
     * <li>{@link #DECREASES}</li>
     * <li>{@link #DIRECTLY_DECREASES}</li>
     * <li>{@link #DIRECTLY_INCREASES}</li>
     * <li>{@link #INCREASES}</li>
     * </ol>
     * </p>
     *
     * @return boolean
     */
    public boolean isCausal() {
        switch (this) {
        case CAUSES_NO_CHANGE:
        case DECREASES:
        case DIRECTLY_DECREASES:
        case DIRECTLY_INCREASES:
        case INCREASES:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the relationship type {@code r} is a
     * <i>direct</i> relationship, {@code false} otherwise.
     * <p>
     * For further reference, see <i>BEL Language Overview</i>, Appendix B.
     * </p>
     *
     * @param r Relationship type
     * @return boolean
     */
    public static boolean isDirect(final RelationshipType r) {
        return r.isDirect();
    }

    /**
     * Returns {@code true} if this relationship type is a <i>direct</i>
     * relationship, {@code false} otherwise.
     * <p>
     * For further reference, see <i>BEL Language Overview</i>, Appendix B.
     * </p>
     * <p>
     * The set of direct relationships are:
     * <ol>
     * <li>{@link #ACTS_IN}</li>
     * <li>{@link #DIRECTLY_DECREASES}</li>
     * <li>{@link #DIRECTLY_INCREASES}</li>
     * <li>{@link #HAS_COMPONENT}</li>
     * <li>{@link #HAS_COMPONENTS}</li>
     * <li>{@link #HAS_MODIFICATION}</li>
     * <li>{@link #HAS_PRODUCT}</li>
     * <li>{@link #HAS_VARIANT}</li>
     * <li>{@link #REACTANT_IN}</li>
     * <li>{@link #TRANSCRIBED_TO}</li>
     * <li>{@link #TRANSLATED_TO}</li>
     * </ol>
     * </p>
     *
     * @return boolean
     */
    public boolean isDirect() {
        switch (this) {
        case ACTS_IN:
        case DIRECTLY_DECREASES:
        case DIRECTLY_INCREASES:
        case HAS_COMPONENT:
        case HAS_COMPONENTS:
        case HAS_MODIFICATION:
        case HAS_PRODUCT:
        case HAS_VARIANT:
        case REACTANT_IN:
        case TRANSCRIBED_TO:
        case TRANSLATED_TO:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if this relationship type is not a <i>direct</i>
     * relationship, {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isIndirect() {
        return !isDirect();
    }

    /**
     * Returns {@code true} if the relationship type {@code r} is a
     * <i>increasing</i> relationship, {@code false} otherwise.
     *
     * @param r Relationship type
     * @return boolean
     */
    public static boolean isIncreasing(final RelationshipType r) {
        return r.isIncreasing();
    }

    /**
     * Returns {@code true} if this relationship type is an <i>increasing</i>
     * relationship, {@code false} otherwise.
     * <p>
     * The set of increasing relationships are:
     * <ol>
     * <li>{@link #INCREASES}</li>
     * <li>{@link #DIRECTLY_INCREASES}</li>
     * <li>{@link #RATE_LIMITING_STEP_OF}</li>
     * <li>{@link #POSITIVE_CORRELATION}</li>
     * </p>
     */
    public boolean isIncreasing() {
        switch (this) {
        case INCREASES:
        case DIRECTLY_INCREASES:
        case RATE_LIMITING_STEP_OF:
        case POSITIVE_CORRELATION:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the relationship type {@code r} is a
     * <i>decreasing</i> relationship, {@code false} otherwise.
     *
     * @param r Relationship type
     * @return boolean
     */
    public static boolean isDecreasing(final RelationshipType r) {
        return r.isDecreasing();
    }

    /**
     * Returns {@code true} if this relationship type is a <i>decreasing</i>
     * relationship, {@code false} otherwise.
     * <p>
     * The set of decreasing relationships are:
     * <ol>
     * <li>{@link #DIRECTLY_DECREASES}</li>
     * <li>{@link #DECREASES}</li>
     * <li>{@link #NEGATIVE_CORRELATION}</li>
     * </p>
     */
    public boolean isDecreasing() {
        switch (this) {
        case DIRECTLY_DECREASES:
        case DECREASES:
        case NEGATIVE_CORRELATION:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the relationship type {@code r} is a <i>self</i>
     * relationship, {@code false} otherwise.
     * <p>
     * For further reference, see the <i>BEL Relationship Types
     * Specification</i>.
     * </p>
     *
     * @param r Relationship type
     * @return boolean
     */
    public static boolean isSelf(final RelationshipType r) {
        return r.isSelf();
    }

    /**
     * Returns {@code true} if this relationship type is a <i>self</i>
     * relationship, {@code false} otherwise.
     * <p>
     * For further reference, see the <i>BEL Relationship Types
     * Specification</i>.
     * </p>
     * <p>
     * The set of self relationships are:
     * <ol>
     * <li>{@link #ACTS_IN}</li>
     * <li>{@link #HAS_PRODUCT}</li>
     * <li>{@link #REACTANT_IN}</li>
     * <li>{@link #TRANSLOCATES}</li>
     * <li>{@link #INCLUDES}</li>
     * </ol>
     * </p>
     *
     * @return boolean
     */
    public boolean isSelf() {
        switch (this) {
        case ACTS_IN:
        case HAS_PRODUCT:
        case REACTANT_IN:
        case TRANSLOCATES:
        case INCLUDES:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the relationship type {@code r} is a
     * <i>correlative</i> relationship, {@code false} otherwise.
     * <p>
     * For further reference, see <i>BEL Language Overview</i>, Appendix B.
     * </p>
     *
     * @param r Relationship type
     * @return boolean
     */
    public static boolean isCorrelative(final RelationshipType r) {
        return r.isCorrelative();
    }

    /**
     * <p>
     * The set of correlative relationships are:
     * <ol>
     * <li>{@link #NEGATIVE_CORRELATION}</li>
     * <li>{@link #POSITIVE_CORRELATION}</li>
     * </ol>
     * </p>
     */
    public boolean isCorrelative() {
        switch (this) {
        case NEGATIVE_CORRELATION:
        case POSITIVE_CORRELATION:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the relationship type {@code r} is a
     * <i>genomic</i> relationship, {@code false} otherwise.
     * <p>
     * For further reference, see <i>BEL Language Overview</i>, Appendix B.
     * </p>
     *
     * @param r Relationship type
     * @return boolean
     */
    public static boolean isGenomic(final RelationshipType r) {
        return r.isGenomic();
    }

    /**
     * <p>
     * The set of genomic relationships are:
     * <ol>
     * <li>{@link #ANALOGOUS}</li>
     * <li>{@link #ORTHOLOGOUS}</li>
     * <li>{@link #TRANSCRIBED_TO}</li>
     * <li>{@link #TRANSLATED_TO}</li>
     * </ol>
     * </p>
     */
    public boolean isGenomic() {
        switch (this) {
        case ANALOGOUS:
        case ORTHOLOGOUS:
        case TRANSCRIBED_TO:
        case TRANSLATED_TO:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the relationship type is a directed relationship,
     * {@code false} otherwise.
     *
     * @param r the {@link RelationshipType relationship type}
     * @return boolean
     */
    public static boolean isDirected(final RelationshipType r) {
        return r.isDirected();
    }

    /**
     * <p>
     * The set of directed relationships include all except:
     * <ol>
     * <li>{@link RelationshipType#ANALOGOUS}</li>
     * <li>{@link RelationshipType#ASSOCIATION}</li>
     * <li>{@link RelationshipType#NEGATIVE_CORRELATION}</li>
     * <li>{@link RelationshipType#ORTHOLOGOUS}</li>
     * <li>{@link RelationshipType#POSITIVE_CORRELATION}</li>
     * </ol>
     *
     * @return {@code true} if directed, {@code false} if not directed
     */
    public boolean isDirected() {
        switch (this) {
        case ANALOGOUS:
        case ASSOCIATION:
        case NEGATIVE_CORRELATION:
        case ORTHOLOGOUS:
        case POSITIVE_CORRELATION:
            return false;
        default:
            return true;
        }
    }

    /**
     * Returns {@code true} if a statement with the relationship type may have
     * an object term using the {@link FunctionEnum#LIST LIST} function type,
     * {@code false} otherwise.
     *
     * @param r the {@link RelationshipType relationship type}
     * @return boolean
     */
    public static boolean isListable(final RelationshipType r) {
        return r.isListable();
    }

    /**
     * <p>
     * The set of relationships that may be used with the
     * {@link FunctionEnum#LIST LIST} function type include only:
     * <ol>
     * <li>{@link RelationshipType#HAS_COMPONENTS}</li>
     * <li>{@link RelationshipType#HAS_MEMBERS}</li>
     * </ol>
     *
     * @return {@code true} if possible to use with {@link FunctionEnum#LIST
     * LIST}, {@code false} otherwise
     */
    public boolean isListable() {
        switch (this) {
        case HAS_COMPONENTS:
        case HAS_MEMBERS:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns {@code true} if the relationship type is an injected
     * relationship, {@code false} otherwise.
     * <p>
     * These relationships are injected by the BEL Compiler and not suitable for
     * use in BEL Script or XBEL formats.
     * </p>
     *
     * @param r the {@link RelationshipType relationship type}
     * @return boolean
     */
    public static boolean isInjected(final RelationshipType r) {
        return r.isInjected();
    }

    /**
     * <p>
     * The set of relationships injected by the BEL Compiler include only:
     * <ol>
     * <li>{@link RelationshipType#ACTS_IN}</li>
     * <li>{@link RelationshipType#INCLUDES}</li>
     * <li>{@link RelationshipType#HAS_MODIFICATION}</li>
     * <li>{@link RelationshipType#HAS_PRODUCT}</li>
     * <li>{@link RelationshipType#HAS_VARIANT}</li>
     * <li>{@link RelationshipType#REACTANT_IN}</li>
     * <li>{@link RelationshipType#TRANSLOCATES}</li>
     * </ol>
     *
     * @return {@code true} if this is an injected relationship, {@code false}
     * otherwise
     */
    public boolean isInjected() {
        switch (this) {
        case ACTS_IN:
        case INCLUDES:
        case HAS_MODIFICATION:
        case HAS_PRODUCT:
        case HAS_VARIANT:
        case REACTANT_IN:
        case TRANSLOCATES:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns the set of {@link RelationshipType relationship types} that are
     * causal.
     *
     * @return an unmodifiable {@link Set} of causal {@link RelationshipType
     * relationship types}
     */
    public static Set<RelationshipType> getCausalRelationships() {
        return CAUSAL_RELS;
    }

    /**
     * Returns the set of {@link RelationshipType relationship types} that are
     * direct.
     *
     * @return an unmodifiable {@link Set} of direct {@link RelationshipType
     * relationship types}
     */
    public static Set<RelationshipType> getDirectRelationships() {
        return DIRECT_RELS;
    }

    /**
     * Returns the set of {@link RelationshipType relationship types} that are
     * indirect.
     *
     * @return an unmodifiable {@link Set} of indirect {@link RelationshipType
     * relationship types}
     */
    public static Set<RelationshipType> getIndirectRelationships() {
        return INDIRECT_RELS;
    }

    /**
     * Returns the set of {@link RelationshipType relationship types} that are
     * correlative.
     *
     * @return an unmodifiable {@link Set} of correlative
     * {@link RelationshipType relationship types}
     */
    public static Set<RelationshipType> getCorrelativeRelationships() {
        return CORRELATIVE_RELS;
    }

    /**
     * Returns the set of {@link RelationshipType relationship types} that are
     * genomic.
     *
     * @return an unmodifiable {@link Set} of genomic {@link RelationshipType
     * relationship types}
     */
    public static Set<RelationshipType> getGenomicRelationships() {
        return GENOMIC_RELS;
    }

    /**
     * Returns the set of {@link RelationshipType relationship types} that are
     * directed.
     *
     * @return an unmodifiable {@link Set} of directed {@link RelationshipType
     * relationship types}.
     */
    public static Set<RelationshipType> getDirectedRelationships() {
        return DIRECTED_RELS;
    }

    /**
     * Returns the set of {@link RelationshipType relationship types} that are
     * self relationships.
     *
     * @return an unmodifiable {@link Set} of self {@link RelationshipType
     * relationship types}
     */
    public static Set<RelationshipType> getSelfRelationships() {
        return SELF_RELS;
    }

    /**
     * Returns the set of {@link RelationshipType relationship types} that are
     * increasing relationships.
     *
     * @return an unmodifiable {@link Set} of increasing
     * {@link RelationshipType relationship types}
     */
    public static Set<RelationshipType> getIncreasingRelationships() {
        return INC_RELS;
    }

    /**
     * Returns the set of {@link RelationshipType relationship types} that are
     * decreasing relationships.
     *
     * @return an unmodifiable {@link Set} of decreasing
     * {@link RelationshipType relationship types}
     */
    public static Set<RelationshipType> getDecreasingRelationshipTypes() {
        return DEC_RELS;
    }

    /**
     * Returns the set of {@link RelationshipType relationship types} that are
     * injected relationships.
     *
     * @return an unmodifiable {@link Set} of injected {@link RelationshipType
     * relationship types}
     */
    public static Set<RelationshipType> getInjectedRelationshipTypes() {
        return INJ_RELS;
    }
}
