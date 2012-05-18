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
class ModificationExpansionRule extends TermExpansionRule {

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
