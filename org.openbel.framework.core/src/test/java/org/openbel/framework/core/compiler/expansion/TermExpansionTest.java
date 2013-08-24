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
