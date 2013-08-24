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
package org.openbel.framework.api;

import static org.openbel.framework.common.BELUtilities.nulls;

import java.text.ParseException;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelTerm;

/**
 * Resolver defines a utility to resolve BEL expressions to elements within
 * a specific {@link Kam}.
 * <p>
 * BelTerm expressions can be resolved to {@link KamNode} elements within a
 * specific {@link Kam}.
 * </p>
 *
 * <p>
 * Edges in the form (subject BelTerm, Relationship, object BelTerm) can also
 * be resolved to {@link KamEdge} elements within a specific {@link Kam}.  This
 * implementation relies on resolving both the subject and object BelTerm to
 * {@link KamNode} elements.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Resolver {

    /**
     * Resolves a BelTerm, as a {@link String}, to an equivalent
     * {@link KamNode} in the {@link Kam}.
     *
     * @param kam {@link Kam}, the KAM to resolve against
     * @param kamStore {@link KAMStore}, the KAM store to use in resolving the
     * bel term expression
     * @param belTerm {@link String}, the BelTerm string, which cannot be null
     * or the empty string
     * @return the resolved {@link KamNode} for the BelTerm, or null if one
     * could not be found
     * @throws InvalidArgument Thrown if <tt>kam</tt>, <tt>kamStore</tt>, or
     * <tt>belTerm</tt> is null
     * @throws ParseException Thrown if <tt>belTerm</tt> cannot be parsed to a
     * valid BEL term expression
     * @throws ResolverException Thrown if an error occurred resolving the
     * BelTerm using the {@link KAMStore}
     */
    public KamNode resolve(final Kam kam, final KamStore kamStore,
            final String belTerm) throws ParseException,
            ResolverException {
        if (nulls(kam, kamStore, belTerm)) {
            throw new InvalidArgument(
                    "null parameter(s) provided to resolve API.");
        }

        KamNode kamNode = null;

        try {
            // Validate the term
            Term term = BELParser.parseTerm(belTerm);
            // Convert to long form
            String lookupTerm = term.toBELLongForm();

            // If we parsed Ok we can go ahead and lookup the string in the KAMStore
            kamNode = kamStore.getKamNode(kam, lookupTerm);

        } catch (KamStoreException e) {
            throw new ResolverException(e);
        }

        return kamNode;
    }

    /**
     * Resolves a BelTerm to an equivalent {@link KamNode} in the {@link Kam}.
     *
     * @param kam {@link Kam}, the KAM to resolve against
     * @param kamStore {@link KAMStore}, the KAM store to use in resolving the
     * bel term expression
     * @param belTerm {@link BelTerm}, the BelTerm which cannot be null
     * @return the resolved {@link KamNode} for the BelTerm, or null if one
     * could not be found
     * @throws InvalidArgument Thrown if <tt>kam</tt>, <tt>kamStore</tt>, or
     * <tt>belTerm</tt> is null
     * @throws ResolverException Thrown if an error occurred resolving the
     * BelTerm using the {@link KAMStore}
     */
    public KamNode resolve(final Kam kam, final KamStore kamStore,
            final BelTerm belTerm) throws ResolverException {
        if (nulls(kam, kamStore, belTerm)) {
            throw new InvalidArgument(
                    "null parameter(s) provided to resolve API.");
        }

        if (null == belTerm) {
            throw new InvalidArgument("belTerm", belTerm);
        }

        KamNode kamNode = null;

        try {
            // If we parsed Ok we can go ahead and lookup the string in the KAMStore
            kamNode = kamStore.getKamNode(kam, belTerm);

        } catch (KamStoreException e) {
            throw new ResolverException(e);
        }

        return kamNode;
    }

    /**
     * Resolves a subject BelTerm, relationship type, and object BelTerm to a
     * {@link KamEdge}, if it exists within the {@link Kam}.  If it does not
     * exist then a <tt>null</tt> is returned.
     * <p>
     * This resolve operation must resolve both the <tt>subjectBelTerm</tt> and
     * <tt>objectBelTerm</tt> to a {@link KamNode} in order to find a
     * {@link KamEdge}.
     * </p>
     *
     * @param kam {@link Kam}, the kam to resolve into, which cannot be null
     * @param kamStore {@link KAMStore}, the KAM store to use in resolving
     * the BelTerms and Edge, which cannot be null
     * @param subjectBelTerm {@link String}, the subject BelTerm to resolve to
     * a {@link KamNode}, which cannot be null
     * @param rtype {@link RelationshipType}, the relationship type of the
     * edge to resolve, which cannot be null
     * @param objectBelTerm {@link String}, the object BelTerm to resolve to a
     * {@link KamNode}, which cannot be null
     * @return the resolved {@link KamEdge} in <tt>kam</tt>, or <tt>null</tt>
     * if the edge does not exist
     * @throws InvalidArgument Thrown if <tt>kam</tt>, <tt>kamStore</tt>,
     * <tt>subjectBelTerm</tt>, <tt>rtype</tt>, or <tt>objectBelTerm</tt> is
     * null
     * @throws ResolverException Thrown if an error occurred resolving the
     * BelTerm using the {@link KAMStore}
     * @throws ParseException Thrown if <tt>subjectBelTerm</tt> or
     * <tt>objectBelTerm</tt> cannot be parsed to a valid BEL term expression
     */
    public KamEdge resolve(final Kam kam, final KamStore kamStore,
            final String subjectBelTerm, final RelationshipType rtype,
            final String objectBelTerm) throws ResolverException,
            ParseException {
        if (nulls(kam, kamStore, subjectBelTerm, rtype, objectBelTerm)) {
            throw new InvalidArgument(
                    "null parameter(s) provided to resolve API.");
        }

        // resolve subject bel term to kam node.
        final KamNode subjectKamNode = resolve(kam, kamStore, subjectBelTerm);
        if (subjectKamNode == null) {
            return null;
        }

        // resolve object bel term to kam node.
        final KamNode objectKamNode = resolve(kam, kamStore, objectBelTerm);

        return resolveEdge(kam, subjectKamNode, rtype, objectKamNode);
    }

    /**
     * Resolves a subject {@link BelTerm}, relationship type, and object
     * {@link BelTerm} to a {@link KamEdge}, if it exists within the
     * {@link Kam}.  If it does not exist then a <tt>null</tt> is returned.
     * <p>
     * This resolve operation must resolve both the <tt>subjectBelTerm</tt> and
     * <tt>objectBelTerm</tt> to a {@link KamNode} in order to find a
     * {@link KamEdge}.
     * </p>
     *
     * @param kam {@link Kam}, the kam to resolve into, which cannot be null
     * @param kamStore {@link KAMStore}, the KAM store to use in resolving
     * the BelTerms and Edge, which cannot be null
     * @param subjectBelTerm {@link BelTerm}, the subject BelTerm to resolve to
     * a {@link KamNode}, which cannot be null
     * @param rtype {@link RelationshipType}, the relationship type of the
     * edge to resolve, which cannot be null
     * @param objectBelTerm {@link BelTerm}, the object BelTerm to resolve to a
     * {@link KamNode}, which cannot be null
     * @return the resolved {@link KamEdge} in <tt>kam</tt>, or <tt>null</tt>
     * if the edge does not exist
     * @throws InvalidArgument Thrown if <tt>kam</tt>, <tt>kamStore</tt>,
     * <tt>subjectBelTerm</tt>, <tt>rtype</tt>, or <tt>objectBelTerm</tt> is
     * null
     * @throws ResolverException Thrown if an error occurred resolving the
     * BelTerm using the {@link KAMStore}
     */
    public KamEdge resolve(final Kam kam, final KamStore kamStore,
            final BelTerm subjectBelTerm, final RelationshipType rtype,
            final BelTerm objectBelTerm) throws ResolverException {
        if (nulls(kam, kamStore, subjectBelTerm, rtype, objectBelTerm)) {
            throw new InvalidArgument(
                    "null parameter(s) provided to resolve API.");
        }

        // resolve subject bel term to kam node.
        final KamNode subjectKamNode = resolve(kam, kamStore, subjectBelTerm);
        if (subjectKamNode == null) {
            return null;
        }

        // resolve object bel term to kam node.
        final KamNode objectKamNode = resolve(kam, kamStore, objectBelTerm);

        return resolveEdge(kam, subjectKamNode, rtype, objectKamNode);
    }

    /**
     * Resolve edge based on a subject {@link KamNode},
     * {@link RelationshipType}, and an object {@link KamNode}.
     *
     * @param kam {@link Kam}, the kam to resolve into
     * @param subjectKamNode {@link KamNode}, the subject kam node
     * @param rtype {@link RelationshipType}, the relationship type
     * @param objectKamNode {@link KamNode}, the object kam node
     * @return the resolved {@link KamEdge}, or null if it does not exist in
     * the {@link Kam}
     */
    private KamEdge resolveEdge(final Kam kam, final KamNode subjectKamNode,
            final RelationshipType rtype, final KamNode objectKamNode) {
        final KamEdge resolvedEdge = kam.findEdge(subjectKamNode, rtype,
                objectKamNode);
        return resolvedEdge;
    }
}
