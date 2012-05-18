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
package org.openbel.framework.common.lang;

import static java.util.Collections.unmodifiableSet;
import static org.openbel.framework.common.BELUtilities.constrainedHashMap;
import static org.openbel.framework.common.BELUtilities.constrainedHashSet;
import static org.openbel.framework.common.BELUtilities.nulls;
import static org.openbel.framework.common.enums.SemanticStatus.VALID;

import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.enums.SemanticStatus;

/**
 * The base class of the immutable BEL function model.
 * <p>
 * TODO: The function model currently lacks unit tests
 * </p>
 */
public abstract class Function {
    private final Set<Signature> signatures;
    private final String name;
    private final String abbreviation;
    private final String briefDescription;
    private final int hash;

    /**
     * Creates a function.
     * 
     * @param name Function name
     * @param abbr The function's abbreviation; its short notation (may be null)
     * @param brfDesc A brief, <b>one-line</b> description of the function
     * @param sigs The supported signatures
     * @see Signature Refer here for the format of {@code sigs}
     */
    protected Function(String name, String abbr, String brfDesc, String... sigs) {
        if (nulls(name, brfDesc, sigs) || sigs.length == 0) {
            final String me = getClass().getName();
            throw new RuntimeException(me + " is not a valid function");
        }

        this.name = name;
        this.abbreviation = abbr;
        this.briefDescription = brfDesc;
        hash = getClass().getName().hashCode();

        signatures = constrainedHashSet(sigs.length);
        for (final String signature : sigs) {
            final Signature internalsig = new Signature(signature);
            signatures.add(internalsig);
        }
    }

    /**
     * Returns a read-only version of the function's signature set.
     * 
     * @return Read-only {@link Set set} of {@link Signature signatures}
     */
    public Set<Signature> getSignatures() {
        return unmodifiableSet(signatures);
    }

    /**
     * Returns the function's name.
     * 
     * @return {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the function's abbreviation.
     * 
     * @return {@link String}, may be null
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Returns the function's brief description.
     * 
     * @return {@link String}
     */
    public String getBriefDescription() {
        return briefDescription;
    }

    /**
     * Returns {@code true} if the provided signature is valid for this
     * function, {@code false} otherwise.
     * <p>
     * You can access the semantic status of invalid signatures (those returning
     * {@code false} here) via {@link #getStatus(Signature) getStatus}.
     * </p>
     * 
     * @param sig {@link Signature}
     * @return boolean
     * @see #getStatus(Signature)
     */
    public boolean validSignature(final Signature sig) {
        if (signatures.contains(sig)) return true;

        for (final Signature signature : signatures) {
            final SemanticStatus status = signature.matches(sig);
            if (status == VALID) return true;
        }
        return false;
    }

    /**
     * Returns a map of {@link Signature signatures} to {@link SemanticStatus
     * semantic status}. The provided signature is checked against all
     * signatures of this function.
     * 
     * @param sig {@link Signature}
     * @return Map of signatures to semantic status
     * @see #validSignature(Signature)
     */
    public Map<Signature, SemanticStatus> getStatus(final Signature sig) {
        final int sigs_size = signatures.size();
        Map<Signature, SemanticStatus> ret = constrainedHashMap(sigs_size);

        for (final Signature signature : signatures) {
            final SemanticStatus status = signature.matches(sig);
            if (status == null) continue;
            ret.put(signature, status);
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("Function [\n");

        bldr.append("\tname: ");
        bldr.append(name);
        bldr.append("\n");

        bldr.append("\tabbreviation: ");
        if (abbreviation != null)
            bldr.append(abbreviation);
        bldr.append("\n");

        bldr.append("\tbrief description: ");
        bldr.append(briefDescription);
        bldr.append("\n");

        bldr.append("\tsignatures:\n");
        for (final Signature sig : signatures) {
            bldr.append("\t\t");
            bldr.append(sig);
            bldr.append("\n");
        }

        bldr.append("]");
        return bldr.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object obj) {
        if (obj == null) return false;
        return this.getClass().equals(obj.getClass());
    }

    /**
     * Returns {@code true} if the argument {@code count} is valid for this
     * function, {@code false} otherwise.
     * 
     * @param count Argument count
     * @return boolean
     */
    public abstract boolean validArgumentCount(int count);
}
