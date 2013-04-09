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
