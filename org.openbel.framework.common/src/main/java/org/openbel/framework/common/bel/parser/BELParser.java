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

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.openbel.bel.model.BELDocument;
import org.openbel.bel.model.BELParseErrorException;
import org.openbel.bel.model.BELParseWarningException;
import org.openbel.framework.common.bel.parser.BELScriptWalker.document_return;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;

public class BELParser {

    private BELParser() {
    }

    public static final BELParseResults parse(final String belScriptSyntax) {
        CharStream stream = new ANTLRStringStream(belScriptSyntax);
        BELScriptLexer lexer = new BELScriptLexer(stream);
        TokenStream tstream = new CommonTokenStream(lexer);
        BELScriptParser parser = new BELScriptParser(tstream);

        // parse and extract syntax errors
        BELScriptParser.document_return result = null;
        try {
        result = parser.document();
        } catch (RecognitionException e) {}
        List<BELParseErrorException> syntaxErrors = parser.getSyntaxErrors();
        List<BELParseWarningException> syntaxWarnings =
                new ArrayList<BELParseWarningException>();

        BELDocument doc = null;
        if (result != null) {
            // walk and extract semantic errors
            CommonTreeNodeStream nodeStream =
                    new CommonTreeNodeStream(result.tree);
            BELScriptWalker walker = new BELScriptWalker(nodeStream);
            document_return document = null;
            try {
            document = walker.document();
            } catch (RecognitionException e) {}

            if (document != null) {
                doc = document.doc;
            }

            syntaxWarnings.addAll(walker.getSyntaxWarnings());
            syntaxErrors.addAll(walker.getSyntaxErrors());
        }

        return new BELParseResults(syntaxWarnings, syntaxErrors, doc);
    }

    public static final Term parseTerm(final String belTermSyntax) {
        CharStream stream = new ANTLRStringStream(belTermSyntax);
        BELStatementLexer lexer = new BELStatementLexer(stream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        BELStatementParser bsp = new BELStatementParser(tokenStream);
        return bsp.outer_term().r;
    }

    public static final Statement
            parseStatement(final String belStatementSyntax) {
        CharStream stream = new ANTLRStringStream(belStatementSyntax);
        BELStatementLexer lexer = new BELStatementLexer(stream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        BELStatementParser bsp = new BELStatementParser(tokenStream);
        return bsp.statement().r;
    }
}
