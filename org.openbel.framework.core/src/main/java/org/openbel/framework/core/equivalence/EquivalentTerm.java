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
