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
package org.openbel.framework.common.bel.parser;

import org.junit.Test;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.Term;

public class BELStatementParserTest {

    @Test
    public void testOneParam() {
        Term term = BELParser.parseTerm("proteinAbundance(HGNC:AKT1)");
        System.out.println(term.toBELLongForm());
    }

    @Test
    public void testMultipleParams() {
        Term term =
                BELParser
                        .parseTerm("list(HGNC:AKT1,HGNC:AKT2,GO:\"mito moto\")");
        System.out.println(term.toBELLongForm());
    }

    @Test
    public void testMultipleInnerTerms() {
        Term term =
                BELParser
                        .parseTerm("complexAbundance(proteinAbundance(HGNC:AKT1),proteinAbundance(HGNC:AKT2),abundance(CHEBI:TLH21))");
        System.out.println(term.toBELLongForm());
    }

    @Test
    public void testFunctionThenParams() {
        Term term =
                BELParser
                        .parseTerm("translocation(proteinAbundance(HGNC:DIABLO),GO:mitochondrion,GO:cytoplasm)");
        System.out.println(term.toBELLongForm());
    }

    @Test
    public void testEmptyModifcation() {
        Term term =
                BELParser
                        .parseTerm("proteinAbundance(HGNC:MAP3K1,modification())");
        System.out.println(term.toBELLongForm());
    }

    @Test
    public void testAminoAcidModifcation() {
        Term term =
                BELParser
                        .parseTerm("proteinAbundance(HGNC:MAP3K1,modification(P))");
        System.out.println(term.toBELLongForm());
    }

    @Test
    public void testThreeLevelNesting() {
        Term term =
                BELParser
                        .parseTerm("complexAbundance(complexAbundance(proteinAbundance(HGNC:GABPA),proteinAbundance(HGNC:GABPB1)),complexAbundance(proteinAbundance(HGNC:GABPA),proteinAbundance(HGNC:GABPB1)))");
        System.out.println(term.toBELLongForm());
    }

    @Test
    public void testNestedReaction() {
        Term term =
                BELParser
                        .parseTerm("reaction(reactants(complexAbundance(proteinAbundance(HGNC:ESR1),proteinAbundance(PFAM:\"Ncor Family\"))),products(complexAbundance(proteinAbundance(PFAM:\"Ncoa Family\"),proteinAbundance(HGNC:ESR1))))");
        System.out.println(term.toBELLongForm());
    }

    @Test
    public void testModificationTwoParams() {
        Term term =
                BELParser
                        .parseTerm("reaction(reactants(proteinAbundance(HGNC:AR)),products(proteinAbundance(HGNC:AR,proteinModification(A,K632))))");
        System.out.println(term.toBELLongForm());
    }

    @Test
    public void testCollidingParamWithFunction() {
        Term term = BELParser.parseTerm("proteinAbundance(MGI:t)");
        System.out.println(term.toBELLongForm());
    }
}
