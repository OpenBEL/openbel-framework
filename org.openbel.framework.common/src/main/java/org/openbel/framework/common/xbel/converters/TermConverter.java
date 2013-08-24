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
package org.openbel.framework.common.xbel.converters;

import static org.openbel.framework.common.enums.FunctionEnum.fromString;

import java.util.ArrayList;
import java.util.List;

import org.openbel.bel.xbel.model.Function;
import org.openbel.bel.xbel.model.JAXBElement;
import org.openbel.bel.xbel.model.XBELParameter;
import org.openbel.bel.xbel.model.XBELTerm;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Term;

/**
 * Converter class for converting between {@link XBELTerm} and {@link Term}.
 *
 */
public final class TermConverter extends JAXBConverter<XBELTerm, Term> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Term convert(final XBELTerm source) {
        if (source == null) return null;

        // Set term function
        Function f = source.getFunction();
        FunctionEnum funcEnum = fromString(f.value());

        // Destination type
        Term dest = new Term(funcEnum);

        // Schema defines either a list of arg values or terms
        List<JAXBElement> prmtrms = source.getParameterOrTerm();
        final List<BELObject> args = new ArrayList<BELObject>();

        final ParameterConverter pConverter = new ParameterConverter();
        for (final JAXBElement el : prmtrms) {
            if (el instanceof XBELParameter) {
                // Defer to ParameterConverter
                args.add(pConverter.convert((XBELParameter) el));
            } else if (el instanceof XBELTerm) {
                args.add(convert((XBELTerm) el));
            }
        }

        dest.setFunctionArguments(args);

        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELTerm convert(Term source) {
        if (source == null) return null;

        XBELTerm xt = new XBELTerm();

        FunctionEnum functionEnum = source.getFunctionEnum();
        Function func = Function.fromValue(functionEnum.getDisplayValue());
        xt.setFunction(func);

        List<BELObject> functionArgs = source.getFunctionArguments();
        List<JAXBElement> prmtrms = xt.getParameterOrTerm();

        final ParameterConverter pConverter = new ParameterConverter();
        for (final BELObject bo : functionArgs) {
            if (bo instanceof Parameter) {
                // Defer to ParameterConverter
                prmtrms.add(pConverter.convert((Parameter) bo));
            } else if (bo instanceof Term) {
                prmtrms.add(convert((Term) bo));
            }
        }

        return xt;
    }
}
