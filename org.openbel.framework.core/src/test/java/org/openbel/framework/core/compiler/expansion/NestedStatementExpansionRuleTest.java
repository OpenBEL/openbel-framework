/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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
