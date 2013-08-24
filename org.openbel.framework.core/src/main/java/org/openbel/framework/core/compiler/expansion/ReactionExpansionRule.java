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
class ReactionExpansionRule extends ExpansionRule<Term> {

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
