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
package org.openbel.framework.core.equivalence;

import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.asHashSet;
import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.List;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Term;

/**
 * Immutable specialization of a common model {@link Term term} allowing for
 * Java equivalence relations to function in tandem with framework equivalencing
 * of terms and parameters.
 */
public class EquivalentTerm extends Term {
    private static final long serialVersionUID = 2797745347329358353L;
    private final int hash;

    /**
     * Creates an equivalent term with the required function and optional
     * arguments property.
     *
     * @param f {@link FunctionEnum}
     * @param functionArgs {@link List} of {@link BELObject BEL object} function
     * arguments
     * @throws InvalidArgument Thrown if {@code f} is null or
     * {@code functionArgs} contains something other than
     * {@link EquivalentParameter equivalent parameters} and
     * {@link EquivalentTerm equivalent terms}
     */
    public EquivalentTerm(FunctionEnum f, List<BELObject> functionArgs) {
        super(f, functionArgs);
        if (hasItems(functionArgs)) {
            for (final BELObject arg : functionArgs) {
                if (!(arg instanceof EquivalentTerm)
                        && !(arg instanceof EquivalentParameter)) {
                    final String fmt = "unknown function argument: %s";
                    throw new InvalidArgument(format(fmt, arg.getClass()));
                }
            }
        }
        this.hash = hash();
    }

    private int hash() {
        final int prime = 31;
        int result = prime;

        List<BELObject> args = getFunctionArguments();
        if (hasItems(args)) {
            result *= prime;
            for (final BELObject arg : args) {
                result += arg.hashCode();
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquivalentTerm)) {
            return false;
        }
        EquivalentTerm e = (EquivalentTerm) o;

        FunctionEnum f = getFunctionEnum();
        if (f != e.getFunctionEnum()) {
            return false;
        }

        List<BELObject> myargs = getFunctionArguments();
        List<BELObject> otherargs = e.getFunctionArguments();
        if (myargs == null) {
            if (otherargs != null) {
                return false;
            }
            return true;
        }

        if (myargs.size() != otherargs.size()) {
            return false;
        }

        if (f.isSequential()) {
            return myargs.equals(otherargs);
        }

        // Non-sequential functions can be equivalent with each other as long
        // as they have the same set of arguments. Order is not important.

        Set<BELObject> set1 = asHashSet(getFunctionArguments());
        Set<BELObject> set2 = asHashSet(e.getFunctionArguments());
        set1.removeAll(set2);
        if (set1.isEmpty()) {
            return true;
        }
        return false;
    }
}
