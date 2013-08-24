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
