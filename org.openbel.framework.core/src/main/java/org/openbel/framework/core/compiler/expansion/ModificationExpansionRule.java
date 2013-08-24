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
import static org.openbel.framework.common.enums.FunctionEnum.PROTEIN_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.isMutation;
import static org.openbel.framework.common.enums.FunctionEnum.isProteinDecorator;
import static org.openbel.framework.common.enums.RelationshipType.HAS_MODIFICATION;
import static org.openbel.framework.common.enums.RelationshipType.HAS_VARIANT;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.model.Statement.Object;

/**
 * Protein term connects to its protein Modifications with a HAS_MODIFICATION
 * relationship. Protein term connects to its mutation with a HAS_VARIANT
 * relationship.
 */
class ModificationExpansionRule extends ExpansionRule<Term> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "ProteinModification expansion rule";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(Term term) {
        FunctionEnum fx = term.getFunctionEnum();
        if (fx != PROTEIN_ABUNDANCE) {
            // Protein abundance functions only
            return false;
        }

        List<BELObject> args = term.getFunctionArguments();
        if (noItems(args) || args.size() != 2) {
            // Two function arguments only
            return false;
        }

        BELObject x = args.get(0);
        if (!(x instanceof Parameter)) {
            // First argument must be a parameter
            return false;
        }

        BELObject y = args.get(1);
        if (!(y instanceof Term)) {
            // Second argument must be a term
            return false;
        }

        Term ytrm = (Term) y;
        if (!isProteinDecorator(ytrm.getFunctionEnum())) {
            // Protein decorator functions only
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Statement> expand(Term term) {
        List<Statement> statements = new ArrayList<Statement>();

        // first argument of a protein mod term is the protein parameter
        BELObject proteinArgument = term.getFunctionArguments().get(0);
        Parameter pp = (Parameter) proteinArgument;

        // second argument of a protein mod term is the modification
        BELObject modArgument = term.getFunctionArguments().get(1);
        Term mt = (Term) modArgument;

        FunctionEnum fx = mt.getFunctionEnum();

        Term proteinTrm = new Term(PROTEIN_ABUNDANCE);
        proteinTrm.addFunctionArgument(pp);

        Statement stmt = null;
        final Object obj = new Object(term);
        if (isMutation(fx)) {
            // mutation
            // protein term connects to its mutation with HAS_VARIANT
            // relationship
            stmt = new Statement(proteinTrm, null, null, obj, HAS_VARIANT);
        } else {
            // modified protein
            // protein term connects to its modification with HAS_MODIFICATION
            // relationship
            stmt = new Statement(proteinTrm, null, null, obj, HAS_MODIFICATION);
        }

        attachExpansionRuleCitation(stmt);
        statements.add(stmt);

        return statements;
    }
}
