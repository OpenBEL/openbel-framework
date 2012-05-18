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
        } catch (RecognitionException e) {
            // swallow since we have already captured syntax errors
        }
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
            } catch (RecognitionException e) {
                // swallow since we have already captured syntax errors
            }

            if (document != null) {
                doc = document.doc;
            }

            syntaxWarnings.addAll(walker.getSyntaxWarnings());
            syntaxErrors.addAll(walker.getSyntaxErrors());
        }

        return new BELParseResults(syntaxWarnings, syntaxErrors, doc);
    }

    public static final Term parseTerm(final String belTermSyntax)
            throws RecognitionException {
        CharStream stream = new ANTLRStringStream(belTermSyntax);
        BELStatementLexer lexer = new BELStatementLexer(stream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        BELStatementParser bsp = new BELStatementParser(tokenStream);
        return bsp.outer_term().r;
    }

    public static final Statement
            parseStatement(final String belStatementSyntax)
                    throws RecognitionException {
        CharStream stream = new ANTLRStringStream(belStatementSyntax);
        BELStatementLexer lexer = new BELStatementLexer(stream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        BELStatementParser bsp = new BELStatementParser(tokenStream);
        return bsp.statement().r;
    }
}
