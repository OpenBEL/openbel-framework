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
package org.openbel.framework.core.compiler.expansion;

import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.enums.FunctionEnum.COMPLEX_ABUNDANCE;
import static org.openbel.framework.common.enums.RelationshipType.HAS_COMPONENT;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.model.Statement.Object;

/**
 * Complex connects to its components with HAS_COMPONENT relationship.
 */
class ComplexAbundanceExpansionRule extends TermExpansionRule {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Complex Abundance expansion rule";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(Term term) {
        FunctionEnum fx = term.getFunctionEnum();
        if (fx != COMPLEX_ABUNDANCE) {
            // Complex abundance functions only
            return false;
        }

        List<BELObject> args = term.getFunctionArguments();
        if (noItems(args)) {
            // At least one function argument is required
            return false;
        }

        for (final BELObject o : args) {
            // All arguments must be terms
            if (!(o instanceof Term)) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Statement> expand(Term term) {
        List<Statement> statements = new ArrayList<Statement>();

        // Complex connects to its components with HAS_COMPONENT relationship
        for (BELObject o : term.getFunctionArguments()) {
            Term t = (Term) o;
            Object obj = new Object(t);
            Statement s = new Statement(term, null, null, obj, HAS_COMPONENT);
            attachExpansionRuleCitation(s);
            statements.add(s);
        }
        return statements;
    }
}
