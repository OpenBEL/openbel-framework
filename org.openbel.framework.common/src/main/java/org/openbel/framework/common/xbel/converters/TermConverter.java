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
