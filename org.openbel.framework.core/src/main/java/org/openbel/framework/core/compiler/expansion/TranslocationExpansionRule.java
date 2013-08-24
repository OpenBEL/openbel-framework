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
