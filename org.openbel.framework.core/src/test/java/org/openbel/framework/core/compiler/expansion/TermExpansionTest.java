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

import static org.junit.Assert.assertThat;

import java.util.List;

import org.antlr.runtime.RecognitionException;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.core.compiler.expansion.ModificationExpansionRule;

/**
 * {@link TermExpansionTest} defines unit tests for
 * {@link TermExpansionRule term expansion rules} classes.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class TermExpansionTest {

    /**
     * Validate the rule match and expansion of the
     * {@link ModificationExpansionRule modification expansion rule}.
     *
     * @throws RecognitionException Thrown if test BEL expressions are not
     * parsed correctly which will result in a test failure
     */
    @Test
    public void testModificationExpansions()
            throws RecognitionException {
        final ModificationExpansionRule rule = new ModificationExpansionRule();

        Term term;
        List<Statement> stmts;

        // valid substitution
        term = BELParser.parseTerm("p(HGNC:TNF,sub(V,599,E))");
        assertThat(rule.match(term), is(true));
        stmts = rule.expand(term);
        assertThat(stmts.size(), is(1));
        assertThat(stmts.get(0).toBELShortForm(),
                is("p(HGNC:TNF) hasVariant p(HGNC:TNF,sub(V,599,E))"));

        // valid truncation
        term = BELParser.parseTerm("p(HGNC:TNF,trunc(S,1099))");
        assertThat(rule.match(term), is(true));
        stmts = rule.expand(term);
        assertThat(stmts.size(), is(1));
        assertThat(stmts.get(0).toBELShortForm(),
                is("p(HGNC:TNF) hasVariant p(HGNC:TNF,trunc(S,1099))"));

        // valid protein modification
        term = BELParser.parseTerm("p(HGNC:TNF,pmod(P,Y))");
        assertThat(rule.match(term), is(true));
        stmts = rule.expand(term);
        assertThat(stmts.size(), is(1));
        assertThat(stmts.get(0).toBELShortForm(),
                is("p(HGNC:TNF) hasModification p(HGNC:TNF,pmod(P,Y))"));

        // valid fusion
        term = BELParser.parseTerm("p(HGNC:TNF,fus(HGNC:ABL1))");
        assertThat(rule.match(term), is(true));
        stmts = rule.expand(term);
        assertThat(stmts.size(), is(1));
        assertThat(stmts.get(0).toBELShortForm(),
                is("p(HGNC:TNF) hasVariant p(HGNC:TNF,fus(HGNC:ABL1))"));

        // invalid based on outer function
        term = BELParser.parseTerm("complex(p(HGNC:TNF),p(HGNC:MAPK1))");
        assertThat(rule.match(term), is(false));

        // invalid based on missing modification argument
        term = BELParser.parseTerm("p(HGNC:TNF)");
        assertThat(rule.match(term), is(false));

        // invalid based on non-parameter first argument
        term = BELParser.parseTerm("p(r(HGNC:TNF),sub(V,599,E))");
        assertThat(rule.match(term), is(false));

        // invalid based on non-term second argument
        term = BELParser.parseTerm("p(HGNC:TNF,\"Substitution at P,Y\")");
        assertThat(rule.match(term), is(false));

        // invalid protein decorator function for second argument
        term = BELParser.parseTerm("p(HGNC:TNF,g(HGNC:AKT1))");
        assertThat(rule.match(term), is(false));
    }
}
