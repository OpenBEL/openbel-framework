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

import static org.openbel.framework.common.BELUtilities.noItems;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.CommonConverter;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.core.indexer.EquivalenceLookup;

/**
 * Converts {@link Term} and {@link Parameter} common model objects to
 * equivalence variants.
 */
public class EquivalenceConverter {
    private final EquivalenceResource lookup;
    private final TermConverter termCvtr;
    private final ParameterConverter paramCvtr;

    /**
     * Create an equivalence converter with the associated
     * {@link EquivalenceResource}.
     *
     * @param lookup {@link EquivalenceResource}; may not be null
     * @throws InvalidArgument Thrown if {@code lookup} is null
     */
    public EquivalenceConverter(final EquivalenceResource lookup) {
        if (lookup == null) {
            throw new InvalidArgument("lookup resource may not be null");
        }
        this.lookup = lookup;
        termCvtr = new TermConverter();
        paramCvtr = new ParameterConverter();
    }

    /**
     * Returns an {@link EquivalentTerm} converted from the supplied
     * {@link Term}.
     *
     * @param t {@link Term}; may be null
     * @return {@link EquivalentTerm}; null if {@code t} was null
     */
    public EquivalentTerm convert(Term t) {
        if (t == null) return null;
        return termCvtr.convert(t);
    }

    /**
     * Converts {@link Term terms} to {@link EquivalentTerm equivalent terms}.
     */
    private final class TermConverter implements
            CommonConverter<Term, EquivalentTerm> {

        /**
         * {@inheritDoc}
         */
        @Override
        public Term convert(EquivalentTerm src) {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public EquivalentTerm convert(Term src) {
            FunctionEnum f = src.getFunctionEnum();

            final List<BELObject> args = new ArrayList<BELObject>();

            List<BELObject> srcArgs = src.getFunctionArguments();
            if (noItems(srcArgs)) {
                return new EquivalentTerm(f, null);
            }

            for (final BELObject arg : srcArgs) {
                if (arg instanceof Term) {
                    args.add(convert((Term) arg));
                } else if (arg instanceof Parameter) {
                    args.add(paramCvtr.convert((Parameter) arg));
                }
            }

            EquivalentTerm dest = new EquivalentTerm(f, args);
            return dest;
        }
    }

    /**
     * Returns an {@link EquivalentParameter} converted from the supplied
     * {@link Parameter}.
     *
     * @param t {@link Parameter}; may be null
     * @return {@link EquivalentParameter}; null if {@code p} was null
     */
    public EquivalentParameter convert(Parameter p) {
        if (p == null) return null;
        return paramCvtr.convert(p);
    }

    /**
     * Converts {@link Parameter parameters} to {@link EquivalentParameter
     * equivalent parameters} using the {@link EquivalenceResource}.
     */
    private final class ParameterConverter implements
            CommonConverter<Parameter, EquivalentParameter> {

        /**
         * {@inheritDoc}
         */
        @Override
        public Parameter convert(EquivalentParameter src) {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public EquivalentParameter convert(Parameter src) {
            SkinnyUUID bucket = null;
            Namespace ns = src.getNamespace();
            String val = src.getValue();
            if (ns != null) {
                String rl = ns.getResourceLocation();
                EquivalenceLookup el = lookup.forResourceLocation(rl);
                if (el != null) {
                    bucket = el.lookup(val);
                }
            }
            EquivalentParameter dest = new EquivalentParameter(ns, val, bucket);
            return dest;
        }

    }

}
