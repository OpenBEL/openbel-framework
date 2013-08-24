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

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

public class BELScriptTest {

    public static void main(String[] args) throws RecognitionException {

        for (String test : "\"Test\", \"Records\", ,,\"Another\"".split("\\,")) {
            System.out.println(test.trim());
        }

        CharStream cstream =
                new ANTLRStringStream("SET BodyPart = {\"lung\" , \"liver\"}");
        BELScriptLexer lexer = new BELScriptLexer(cstream);
        TokenStream tstream = new CommonTokenStream(lexer);
        BELScriptParser parser = new BELScriptParser(tstream);
        parser.record();

        cstream =
                new ANTLRStringStream("SET DOCUMENT name = \"Test Document\"");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.record();

        cstream = new ANTLRStringStream("SET Species = \"9606\"");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.record();

        cstream =
                new ANTLRStringStream(
                        "SET records = {{\"lung\", \"liver\"}, {\"tissue1\", \"tissue2\"}}");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.record();

        cstream = new ANTLRStringStream("UNSET species");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.record();

        cstream = new ANTLRStringStream("UNSET {species ,tissue ,cell}");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.record();

        cstream = new ANTLRStringStream("UNSET ALL");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.record();

        cstream =
                new ANTLRStringStream(
                        "DEFINE DEFAULT NAMESPACE EG AS URL \"http://www.entrez-gene.org\"");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.record();

        cstream =
                new ANTLRStringStream(
                        "DEFINE ANNOTATION ExposureTime AS PATTERN \"[0-9]+ hours\"");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.record();

        cstream =
                new ANTLRStringStream(
                        "DEFINE ANNOTATION Dosage AS LIST {\"low\",\"medium\",\"high\"}");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.record();

        cstream = new ANTLRStringStream("# this is a document comment line");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.record();

        cstream =
                new ANTLRStringStream(
                        "proteinAbundance(HGNC:AKT1) increases geneAbundance(EG:208)        // Extracted from \\\n associated text.");
        lexer = new BELScriptLexer(cstream);
        tstream = new CommonTokenStream(lexer);
        parser = new BELScriptParser(tstream);
        parser.statement();

        System.out.println("ok");
    }
}
