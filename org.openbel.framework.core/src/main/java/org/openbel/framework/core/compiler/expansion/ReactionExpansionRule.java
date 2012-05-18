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
import static org.openbel.framework.common.enums.FunctionEnum.PRODUCTS;
import static org.openbel.framework.common.enums.FunctionEnum.REACTANTS;
import static org.openbel.framework.common.enums.FunctionEnum.REACTION;
import static org.openbel.framework.common.enums.RelationshipType.HAS_PRODUCT;
import static org.openbel.framework.common.enums.RelationshipType.REACTANT_IN;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.model.Statement.Object;

/**
 * All reactants connect to the reaction with REACTANT_IN relationship. A
 * reaction connects to its products with HAS_PRODUCT relationship.
 */
class ReactionExpansionRule extends TermExpansionRule {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Reaction expansion rule";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(Term term) {

        // XXX the checks here essentially reproduce the
        // semantics of the reaction function

        FunctionEnum fx = term.getFunctionEnum();
        if (fx != REACTION) {
            // Reaction functions only
            return false;
        }
        List<BELObject> args = term.getFunctionArguments();
        if (noItems(args) || args.size() != 2) {
            // Two function arguments only
            return false;
        }

        BELObject x = args.get(0);
        if (!(x instanceof Term)) {
            // First argument must be a term
            return false;
        }

        BELObject y = args.get(1);
        if (!(y instanceof Term)) {
            // Second argument must be a term
            return false;
        }

        Term xtrm = (Term) x;
        if (xtrm.getFunctionEnum() != REACTANTS) {
            // First argument must be a reactants function
            return false;
        }

        Term ytrm = (Term) y;
        if (ytrm.getFunctionEnum() != PRODUCTS) {
            // Second argument must be a products function
            return false;
        }

        List<BELObject> xtrmArgs = xtrm.getFunctionArguments();
        if (noItems(xtrmArgs)) {
            // At least one argument to the reactants function is necessary
            return false;
        }

        List<BELObject> ytrmArgs = ytrm.getFunctionArguments();
        if (noItems(ytrmArgs)) {
            // At least one argument to the products function is necessary
            return false;
        }

        for (final BELObject o : xtrmArgs) {
            // All arguments must be terms
            if (!(o instanceof Term)) {
                return false;
            }
        }

        for (final BELObject o : ytrmArgs) {
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
    public List<Statement> expand(Term reaction) {
        List<Statement> statements = new ArrayList<Statement>();

        BELObject firstArgument = reaction.getFunctionArguments().get(0);
        BELObject secondArgument = reaction.getFunctionArguments().get(1);
        // first argument of a reaction is the reactants
        Term reactants = (Term) firstArgument;
        // second argument of a reaction is the products
        Term products = (Term) secondArgument;

        // all reactants connect to reaction with REACTANT_IN relationship
        for (BELObject reactant : reactants.getFunctionArguments()) {
            Term sub = (Term) reactant;
            Object obj = new Object(reaction);
            Statement stmt = new Statement(sub, null, null, obj, REACTANT_IN);
            attachExpansionRuleCitation(stmt);
            statements.add(stmt);
        }

        // a reaction connects to its products with HAS_PRODUCT relationship
        for (BELObject product : products.getFunctionArguments()) {
            Term sub = (Term) product;
            Object obj = new Object(sub);
            Statement stmt =
                    new Statement(reaction, null, null, obj, HAS_PRODUCT);
            attachExpansionRuleCitation(stmt);
            statements.add(stmt);
        }

        return statements;
    }
}
