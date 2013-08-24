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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.openbel.framework.common.enums.FunctionEnum.*;
import static org.openbel.framework.common.enums.RelationshipType.INCREASES;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.CommonModelFactory;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.core.compiler.expansion.DistributeStatementExpansionRule;

/**
 * NestedStatementExpansionRuleTest tests the nested statement expansion rule
 * to verify expansions are correct.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NestedStatementExpansionRuleTest {

    /**
     * Test that a definitional statement doesn't match the rule.
     */
    @Test
    public void testMatchDefinitionalStatement() {
        Term a = new Term(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "A")));
        Statement defStmt = new Statement(a);

        assertFalse(new DistributeStatementExpansionRule().match(defStmt));
    }

    /**
     * Test that a simple statement doesn't match the rule.
     */
    @Test
    public void testMatchSimpleStatement() {
        Term a = new Term(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "A")));
        Term b = new Term(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "B")));
        Statement simpleStmt = new Statement(a, null, null,
                new Statement.Object(b), INCREASES);

        assertFalse(new DistributeStatementExpansionRule().match(simpleStmt));
    }

    /**
     * Test that a nested statement does match the rule.
     */
    @Test
    public void testMatchNestedStatement() {
        Term a = new Term(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "A")));
        Term b = new Term(GENE_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "B")));
        Term c = new Term(RNA_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "C")));

        Statement bc = new Statement(b, null, null, new Statement.Object(c),
                INCREASES);
        Statement abc = new Statement(a, null, null, new Statement.Object(bc),
                INCREASES);

        assertTrue(new DistributeStatementExpansionRule().match(abc));
    }

    /**
     * Test that a deeply-nested statement does not match the statement
     * expansion rule.
     */
    @Test
    public void testDeeplyNestedExpansion() {
        Term a = new Term(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "A")));
        Term b = new Term(GENE_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "B")));
        Term c = new Term(RNA_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "C")));
        Term d = new Term(ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "D")));
        Term e = new Term(CATALYTIC_ACTIVITY,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "E")));

        Statement de = new Statement(d, null, null, new Statement.Object(e),
                INCREASES);
        Statement cde = new Statement(c, null, null, new Statement.Object(de),
                INCREASES);
        Statement bcde = new Statement(b, null, null,
                new Statement.Object(cde), INCREASES);
        Statement abcde = new Statement(a, null, null, new Statement.Object(
                bcde), INCREASES);

        assertFalse(new DistributeStatementExpansionRule().match(abcde));
    }

    /**
     * Test the correct statement expansion for a singularly-nested statement.
     */
    @Test
    public void testSimpleNestedExpansion() {
        Term a = new Term(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "A")));
        Term b = new Term(GENE_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "B")));
        Term c = new Term(RNA_ABUNDANCE,
                Arrays.asList((BELObject) CommonModelFactory.getInstance()
                        .createParameter(null, "C")));

        Statement bc = new Statement(b, null, null, new Statement.Object(c),
                INCREASES);
        Statement abc = new Statement(a, null, null, new Statement.Object(bc),
                INCREASES);

        List<Statement> expansion = null;
        expansion = new DistributeStatementExpansionRule().expand(abc);

        assertNotNull(expansion);

        if (expansion != null) {
            assertEquals(2, expansion.size());
        }
    }
}
