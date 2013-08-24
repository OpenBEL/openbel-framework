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
