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
package org.openbel.framework.core.compiler.expansion;

import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.enums.RelationshipType.TRANSLOCATES;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.model.Statement.Object;

/**
 * Tranlocation connects the abundance term with TRANSLOCATES relationship.
 */
class TranslocationExpansionRule extends ExpansionRule<Term> {

    @Override
    public String getName() {
        return "Translocation expansion rule";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(Term term) {
        FunctionEnum fx = term.getFunctionEnum();
        if (!fx.isTranslocating()) {
            // Translocating functions only
            return false;
        }

        List<BELObject> args = term.getFunctionArguments();
        if (noItems(args)) {
            // At least one function argument is required
            return false;
        }

        BELObject x = args.get(0);
        if (!(x instanceof Term)) {
            // First argument must be a term
            return false;
        }
        return true;
    }

    @Override
    public List<Statement> expand(Term translocation) {
        List<Statement> statements = new ArrayList<Statement>();

        BELObject firstArgument = translocation.getFunctionArguments().get(0);

        Term abundance = (Term) firstArgument; // first argument of a
                                               // translocation term is the
                                               // abundance
        // tranlocation connects the abundance term with TRANSLOCATES
        // relationship
        Statement statement =
                new Statement(translocation, null, null, new Object(abundance),
                        TRANSLOCATES);
        attachExpansionRuleCitation(statement);
        statements.add(statement);

        return statements;
    }
}
