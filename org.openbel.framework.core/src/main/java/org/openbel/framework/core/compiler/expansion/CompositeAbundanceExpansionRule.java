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
import static org.openbel.framework.common.enums.FunctionEnum.COMPOSITE_ABUNDANCE;
import static org.openbel.framework.common.enums.RelationshipType.INCLUDES;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.model.Statement.Object;

/**
 * Composite Abundance connects to its abundance terms with INCLUDES
 * relationship.
 */
class CompositeAbundanceExpansionRule extends ExpansionRule<Term> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Composite Abundance expansion rule";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(Term term) {
        FunctionEnum fx = term.getFunctionEnum();
        if (fx != COMPOSITE_ABUNDANCE) {
            // Composite abundance functions only
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

        // compositeAbundance connects to its abundance terms with INCLUDES
        // relationship
        for (BELObject o : term.getFunctionArguments()) {
            Term t = (Term) o; // component of the composite term
            final Object obj = new Object(t);
            Statement s = new Statement(term, null, null, obj, INCLUDES);
            attachExpansionRuleCitation(s);
            statements.add(s);
        }
        return statements;
    }
}
