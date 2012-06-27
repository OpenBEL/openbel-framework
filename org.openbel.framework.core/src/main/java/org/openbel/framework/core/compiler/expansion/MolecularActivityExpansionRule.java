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
import static org.openbel.framework.common.enums.FunctionEnum.getActivities;
import static org.openbel.framework.common.enums.RelationshipType.ACTS_IN;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.model.Statement.Object;

/**
 * Molecular activities connect to its abundance term with ACTS_IN relationship.
 */
class MolecularActivityExpansionRule extends ExpansionRule<Term> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Molecular Activity expansion rule";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(Term term) {
        FunctionEnum fx = term.getFunctionEnum();
        if (!getActivities().contains(fx)) {
            // Activity functions only
            return false;
        }

        List<BELObject> args = term.getFunctionArguments();
        if (noItems(args)) {
            // Must have at least one function argument
            return false;
        }

        BELObject x = args.get(0);
        if (!(x instanceof Term)) {
            // First argument must be a term
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Statement> expand(Term activity) {
        List<Statement> statements = new ArrayList<Statement>();

        BELObject firstArgument = activity.getFunctionArguments().get(0);
        Term abundance = (Term) firstArgument; // only argument of a moleculary
                                               // activity term is the abundance
        // Molecular activities connect to its abundance term with ACTS_IN
        // relationship
        final Object obj = new Object(activity);
        Statement statement =
                new Statement(abundance, null, null, obj, ACTS_IN);
        attachExpansionRuleCitation(statement);
        statements.add(statement);

        return statements;
    }
}
