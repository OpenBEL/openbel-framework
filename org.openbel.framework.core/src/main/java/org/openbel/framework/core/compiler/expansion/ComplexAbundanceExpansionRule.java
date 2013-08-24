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
class ComplexAbundanceExpansionRule extends ExpansionRule<Term> {

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
