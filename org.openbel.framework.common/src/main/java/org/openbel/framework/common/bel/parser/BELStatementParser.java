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

import org.antlr.runtime.BitSet;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;

public class BELStatementParser extends Parser {
    public static final String[] tokenNames = new String[] { "<invalid>",
            "<EOR>", "<DOWN>", "<UP>", "OPEN_PAREN",
            "CLOSE_PAREN", "NS_PREFIX", "NS_VALUE", "QUOTED_VALUE", "LETTER",
            "DIGIT", "EscapeSequence",
            "UnicodeEscape", "OctalEscape", "HexDigit", "WS", "','",
            "'proteinAbundance'", "'p'", "'rnaAbundance'",
            "'r'", "'abundance'", "'a'", "'microRNAAbundance'", "'m'",
            "'geneAbundance'", "'g'", "'biologicalProcess'",
            "'bp'", "'pathology'", "'path'", "'complexAbundance'", "'complex'",
            "'translocation'", "'tloc'",
            "'cellSecretion'", "'sec'", "'cellSurfaceExpression'", "'surf'",
            "'reaction'", "'rxn'",
            "'compositeAbundance'", "'composite'", "'fusion'", "'fus'",
            "'degradation'", "'deg'",
            "'molecularActivity'", "'act'", "'catalyticActivity'", "'cat'",
            "'kinaseActivity'", "'kin'",
            "'phosphataseActivity'", "'phos'", "'peptidaseActivity'", "'pep'",
            "'ribosylationActivity'", "'ribo'",
            "'transcriptionalActivity'", "'tscript'", "'transportActivity'",
            "'tport'", "'gtpBoundActivity'", "'gtp'",
            "'chaperoneActivity'", "'chap'", "'proteinModification'", "'pmod'",
            "'substitution'", "'sub'",
            "'truncation'", "'trunc'", "'reactants'", "'products'", "'list'",
            "'increases'", "'->'", "'decreases'",
            "'-|'", "'directlyIncreases'", "'=>'", "'directlyDecreases'",
            "'=|'", "'causesNoChange'",
            "'positiveCorrelation'", "'negativeCorrelation'", "'translatedTo'",
            "'>>'", "'transcribedTo'", "':>'",
            "'isA'", "'subProcessOf'", "'rateLimitingStepOf'",
            "'biomarkerFor'", "'prognosticBiomarkerFor'",
            "'orthologous'", "'analogous'", "'association'", "'--'",
            "'hasMembers'", "'hasComponents'", "'hasMember'",
            "'hasComponent'" };
    public static final int EOF = -1;
    public static final int T__16 = 16;
    public static final int T__17 = 17;
    public static final int T__18 = 18;
    public static final int T__19 = 19;
    public static final int T__20 = 20;
    public static final int T__21 = 21;
    public static final int T__22 = 22;
    public static final int T__23 = 23;
    public static final int T__24 = 24;
    public static final int T__25 = 25;
    public static final int T__26 = 26;
    public static final int T__27 = 27;
    public static final int T__28 = 28;
    public static final int T__29 = 29;
    public static final int T__30 = 30;
    public static final int T__31 = 31;
    public static final int T__32 = 32;
    public static final int T__33 = 33;
    public static final int T__34 = 34;
    public static final int T__35 = 35;
    public static final int T__36 = 36;
    public static final int T__37 = 37;
    public static final int T__38 = 38;
    public static final int T__39 = 39;
    public static final int T__40 = 40;
    public static final int T__41 = 41;
    public static final int T__42 = 42;
    public static final int T__43 = 43;
    public static final int T__44 = 44;
    public static final int T__45 = 45;
    public static final int T__46 = 46;
    public static final int T__47 = 47;
    public static final int T__48 = 48;
    public static final int T__49 = 49;
    public static final int T__50 = 50;
    public static final int T__51 = 51;
    public static final int T__52 = 52;
    public static final int T__53 = 53;
    public static final int T__54 = 54;
    public static final int T__55 = 55;
    public static final int T__56 = 56;
    public static final int T__57 = 57;
    public static final int T__58 = 58;
    public static final int T__59 = 59;
    public static final int T__60 = 60;
    public static final int T__61 = 61;
    public static final int T__62 = 62;
    public static final int T__63 = 63;
    public static final int T__64 = 64;
    public static final int T__65 = 65;
    public static final int T__66 = 66;
    public static final int T__67 = 67;
    public static final int T__68 = 68;
    public static final int T__69 = 69;
    public static final int T__70 = 70;
    public static final int T__71 = 71;
    public static final int T__72 = 72;
    public static final int T__73 = 73;
    public static final int T__74 = 74;
    public static final int T__75 = 75;
    public static final int T__76 = 76;
    public static final int T__77 = 77;
    public static final int T__78 = 78;
    public static final int T__79 = 79;
    public static final int T__80 = 80;
    public static final int T__81 = 81;
    public static final int T__82 = 82;
    public static final int T__83 = 83;
    public static final int T__84 = 84;
    public static final int T__85 = 85;
    public static final int T__86 = 86;
    public static final int T__87 = 87;
    public static final int T__88 = 88;
    public static final int T__89 = 89;
    public static final int T__90 = 90;
    public static final int T__91 = 91;
    public static final int T__92 = 92;
    public static final int T__93 = 93;
    public static final int T__94 = 94;
    public static final int T__95 = 95;
    public static final int T__96 = 96;
    public static final int T__97 = 97;
    public static final int T__98 = 98;
    public static final int T__99 = 99;
    public static final int T__100 = 100;
    public static final int T__101 = 101;
    public static final int T__102 = 102;
    public static final int T__103 = 103;
    public static final int OPEN_PAREN = 4;
    public static final int CLOSE_PAREN = 5;
    public static final int NS_PREFIX = 6;
    public static final int NS_VALUE = 7;
    public static final int QUOTED_VALUE = 8;
    public static final int LETTER = 9;
    public static final int DIGIT = 10;
    public static final int EscapeSequence = 11;
    public static final int UnicodeEscape = 12;
    public static final int OctalEscape = 13;
    public static final int HexDigit = 14;
    public static final int WS = 15;

    // delegates
    // delegators

    public BELStatementParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }

    public BELStatementParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);

    }

    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    @Override
    public String[] getTokenNames() {
        return BELStatementParser.tokenNames;
    }

    @Override
    public String getGrammarFileName() {
        return "BELStatement.g";
    }

    public static class statement_return extends ParserRuleReturnScope {
        public Statement r;
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "statement"
    // BELStatement.g:28:1: statement returns [Statement r] : st= outer_term (rel= relationship ( ( OPEN_PAREN nst=
    // outer_term nrel= relationship not= outer_term CLOSE_PAREN ) | ot= outer_term ) )? ;
    public final BELStatementParser.statement_return statement() {
        BELStatementParser.statement_return retval =
                new BELStatementParser.statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OPEN_PAREN1 = null;
        Token CLOSE_PAREN2 = null;
        BELStatementParser.outer_term_return st = null;

        BELStatementParser.relationship_return rel = null;

        BELStatementParser.outer_term_return nst = null;

        BELStatementParser.relationship_return nrel = null;

        BELStatementParser.outer_term_return not = null;

        BELStatementParser.outer_term_return ot = null;

        Object OPEN_PAREN1_tree = null;
        Object CLOSE_PAREN2_tree = null;

        try {
            // BELStatement.g:28:32: (st= outer_term (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel=
            // relationship not= outer_term CLOSE_PAREN ) | ot= outer_term ) )? )
            // BELStatement.g:29:5: st= outer_term (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship
            // not= outer_term CLOSE_PAREN ) | ot= outer_term ) )?
            {
                root_0 = adaptor.nil();

                pushFollow(FOLLOW_outer_term_in_statement72);
                st = outer_term();

                state._fsp--;

                adaptor.addChild(root_0, st.getTree());

                final Statement s = new Statement(st.r);
                retval.r = s;

                // BELStatement.g:33:5: (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not=
                // outer_term CLOSE_PAREN ) | ot= outer_term ) )?
                int alt2 = 2;
                int LA2_0 = input.LA(1);

                if (((LA2_0 >= 76 && LA2_0 <= 103))) {
                    alt2 = 1;
                }
                switch (alt2) {
                case 1:
                // BELStatement.g:34:9: rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not=
                // outer_term CLOSE_PAREN ) | ot= outer_term )
                {
                    pushFollow(FOLLOW_relationship_in_statement92);
                    rel = relationship();

                    state._fsp--;

                    adaptor.addChild(root_0, rel.getTree());

                    s.setRelationshipType(rel.r);

                    // BELStatement.g:37:9: ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term
                    // CLOSE_PAREN ) | ot= outer_term )
                    int alt1 = 2;
                    int LA1_0 = input.LA(1);

                    if ((LA1_0 == OPEN_PAREN)) {
                        alt1 = 1;
                    } else if (((LA1_0 >= 17 && LA1_0 <= 75))) {
                        alt1 = 2;
                    } else {
                        NoViableAltException nvae =
                                new NoViableAltException("", 1, 0, input);

                        throw nvae;
                    }
                    switch (alt1) {
                    case 1:
                    // BELStatement.g:38:13: ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN
                    // )
                    {
                        // BELStatement.g:38:13: ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term
                        // CLOSE_PAREN )
                        // BELStatement.g:39:17: OPEN_PAREN nst= outer_term nrel= relationship not= outer_term
                        // CLOSE_PAREN
                        {
                            OPEN_PAREN1 =
                                    (Token) match(input, OPEN_PAREN,
                                            FOLLOW_OPEN_PAREN_in_statement136);
                            OPEN_PAREN1_tree = adaptor.create(OPEN_PAREN1);
                            adaptor.addChild(root_0, OPEN_PAREN1_tree);

                            pushFollow(FOLLOW_outer_term_in_statement157);
                            nst = outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, nst.getTree());

                            final Statement ns = new Statement(nst.r);

                            pushFollow(FOLLOW_relationship_in_statement179);
                            nrel = relationship();

                            state._fsp--;

                            adaptor.addChild(root_0, nrel.getTree());

                            ns.setRelationshipType(nrel.r);

                            pushFollow(FOLLOW_outer_term_in_statement202);
                            not = outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, not.getTree());

                            ns.setObject(new Statement.Object(not.r));
                            s.setObject(new Statement.Object(ns));
                            retval.r = s;

                            CLOSE_PAREN2 =
                                    (Token) match(input, CLOSE_PAREN,
                                            FOLLOW_CLOSE_PAREN_in_statement222);
                            CLOSE_PAREN2_tree = adaptor.create(CLOSE_PAREN2);
                            adaptor.addChild(root_0, CLOSE_PAREN2_tree);

                        }

                    }
                        break;
                    case 2:
                    // BELStatement.g:54:17: ot= outer_term
                    {
                        pushFollow(FOLLOW_outer_term_in_statement271);
                        ot = outer_term();

                        state._fsp--;

                        adaptor.addChild(root_0, ot.getTree());

                        s.setObject(new Statement.Object(ot.r));
                        retval.r = s;

                    }
                        break;

                    }

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                    adaptor.errorNode(input, retval.start, input.LT(-1), re);

        } finally {}
        return retval;
    }

    // $ANTLR end "statement"

    public static class outer_term_return extends ParserRuleReturnScope {
        public Term r;
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "outer_term"
    // BELStatement.g:62:1: outer_term returns [Term r] : f= function OPEN_PAREN ( ( ',' )? arg= argument )* CLOSE_PAREN
    // ;
    public final BELStatementParser.outer_term_return outer_term() {
        BELStatementParser.outer_term_return retval =
                new BELStatementParser.outer_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OPEN_PAREN3 = null;
        Token char_literal4 = null;
        Token CLOSE_PAREN5 = null;
        BELStatementParser.function_return f = null;

        BELStatementParser.argument_return arg = null;

        Object OPEN_PAREN3_tree = null;
        Object char_literal4_tree = null;
        Object CLOSE_PAREN5_tree = null;

        try {
            // BELStatement.g:62:28: (f= function OPEN_PAREN ( ( ',' )? arg= argument )* CLOSE_PAREN )
            // BELStatement.g:63:5: f= function OPEN_PAREN ( ( ',' )? arg= argument )* CLOSE_PAREN
            {
                root_0 = adaptor.nil();

                pushFollow(FOLLOW_function_in_outer_term313);
                f = function();

                state._fsp--;

                adaptor.addChild(root_0, f.getTree());

                final Term outerTerm = new Term(f.r);

                OPEN_PAREN3 =
                        (Token) match(input, OPEN_PAREN,
                                FOLLOW_OPEN_PAREN_in_outer_term321);
                OPEN_PAREN3_tree = adaptor.create(OPEN_PAREN3);
                adaptor.addChild(root_0, OPEN_PAREN3_tree);

                // BELStatement.g:67:5: ( ( ',' )? arg= argument )*
                loop4: do {
                    int alt4 = 2;
                    int LA4_0 = input.LA(1);

                    if (((LA4_0 >= NS_PREFIX && LA4_0 <= QUOTED_VALUE) || (LA4_0 >= 16 && LA4_0 <= 75))) {
                        alt4 = 1;
                    }

                    switch (alt4) {
                    case 1:
                    // BELStatement.g:67:6: ( ',' )? arg= argument
                    {
                        // BELStatement.g:67:6: ( ',' )?
                        int alt3 = 2;
                        int LA3_0 = input.LA(1);

                        if ((LA3_0 == 16)) {
                            alt3 = 1;
                        }
                        switch (alt3) {
                        case 1:
                        // BELStatement.g:67:6: ','
                        {
                            char_literal4 =
                                    (Token) match(input, 16,
                                            FOLLOW_16_in_outer_term328);
                            char_literal4_tree = adaptor.create(char_literal4);
                            adaptor.addChild(root_0, char_literal4_tree);

                        }
                            break;

                        }

                        pushFollow(FOLLOW_argument_in_outer_term333);
                        arg = argument();

                        state._fsp--;

                        adaptor.addChild(root_0, arg.getTree());
                        outerTerm.addFunctionArgument(arg.r);

                    }
                        break;

                    default:
                        break loop4;
                    }
                } while (true);

                CLOSE_PAREN5 =
                        (Token) match(input, CLOSE_PAREN,
                                FOLLOW_CLOSE_PAREN_in_outer_term343);
                CLOSE_PAREN5_tree = adaptor.create(CLOSE_PAREN5);
                adaptor.addChild(root_0, CLOSE_PAREN5_tree);

                retval.r = outerTerm;

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                    adaptor.errorNode(input, retval.start, input.LT(-1), re);

        } finally {}
        return retval;
    }

    // $ANTLR end "outer_term"

    public static class argument_return extends ParserRuleReturnScope {
        public BELObject r;
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "argument"
    // BELStatement.g:73:1: argument returns [BELObject r] : (fp= param | ff= term );
    public final BELStatementParser.argument_return argument() {
        BELStatementParser.argument_return retval =
                new BELStatementParser.argument_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        BELStatementParser.param_return fp = null;

        BELStatementParser.term_return ff = null;

        try {
            // BELStatement.g:73:31: (fp= param | ff= term )
            int alt5 = 2;
            int LA5_0 = input.LA(1);

            if (((LA5_0 >= NS_PREFIX && LA5_0 <= QUOTED_VALUE))) {
                alt5 = 1;
            } else if (((LA5_0 >= 17 && LA5_0 <= 75))) {
                alt5 = 2;
            } else {
                NoViableAltException nvae =
                        new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
            case 1:
            // BELStatement.g:74:5: fp= param
            {
                root_0 = adaptor.nil();

                pushFollow(FOLLOW_param_in_argument371);
                fp = param();

                state._fsp--;

                adaptor.addChild(root_0, fp.getTree());
                retval.r = fp.r;

            }
                break;
            case 2:
            // BELStatement.g:75:5: ff= term
            {
                root_0 = adaptor.nil();

                pushFollow(FOLLOW_term_in_argument383);
                ff = term();

                state._fsp--;

                adaptor.addChild(root_0, ff.getTree());
                retval.r = ff.r;

            }
                break;

            }
            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                    adaptor.errorNode(input, retval.start, input.LT(-1), re);

        } finally {}
        return retval;
    }

    // $ANTLR end "argument"

    public static class term_return extends ParserRuleReturnScope {
        public Term r;
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "term"
    // BELStatement.g:78:1: term returns [Term r] : pfv= function OPEN_PAREN ( ( ',' )? (it= term | pp= param ) )*
    // CLOSE_PAREN ;
    public final BELStatementParser.term_return term() {
        BELStatementParser.term_return retval =
                new BELStatementParser.term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OPEN_PAREN6 = null;
        Token char_literal7 = null;
        Token CLOSE_PAREN8 = null;
        BELStatementParser.function_return pfv = null;

        BELStatementParser.term_return it = null;

        BELStatementParser.param_return pp = null;

        Object OPEN_PAREN6_tree = null;
        Object char_literal7_tree = null;
        Object CLOSE_PAREN8_tree = null;

        try {
            // BELStatement.g:78:22: (pfv= function OPEN_PAREN ( ( ',' )? (it= term | pp= param ) )* CLOSE_PAREN )
            // BELStatement.g:79:5: pfv= function OPEN_PAREN ( ( ',' )? (it= term | pp= param ) )* CLOSE_PAREN
            {
                root_0 = adaptor.nil();

                pushFollow(FOLLOW_function_in_term407);
                pfv = function();

                state._fsp--;

                adaptor.addChild(root_0, pfv.getTree());

                final Term parentTerm = new Term(pfv.r);

                OPEN_PAREN6 =
                        (Token) match(input, OPEN_PAREN,
                                FOLLOW_OPEN_PAREN_in_term415);
                OPEN_PAREN6_tree = adaptor.create(OPEN_PAREN6);
                adaptor.addChild(root_0, OPEN_PAREN6_tree);

                // BELStatement.g:83:5: ( ( ',' )? (it= term | pp= param ) )*
                loop8: do {
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);

                    if (((LA8_0 >= NS_PREFIX && LA8_0 <= QUOTED_VALUE) || (LA8_0 >= 16 && LA8_0 <= 75))) {
                        alt8 = 1;
                    }

                    switch (alt8) {
                    case 1:
                    // BELStatement.g:84:9: ( ',' )? (it= term | pp= param )
                    {
                        // BELStatement.g:84:9: ( ',' )?
                        int alt6 = 2;
                        int LA6_0 = input.LA(1);

                        if ((LA6_0 == 16)) {
                            alt6 = 1;
                        }
                        switch (alt6) {
                        case 1:
                        // BELStatement.g:84:9: ','
                        {
                            char_literal7 =
                                    (Token) match(input, 16,
                                            FOLLOW_16_in_term431);
                            char_literal7_tree = adaptor.create(char_literal7);
                            adaptor.addChild(root_0, char_literal7_tree);

                        }
                            break;

                        }

                        // BELStatement.g:85:9: (it= term | pp= param )
                        int alt7 = 2;
                        int LA7_0 = input.LA(1);

                        if (((LA7_0 >= 17 && LA7_0 <= 75))) {
                            alt7 = 1;
                        } else if (((LA7_0 >= NS_PREFIX && LA7_0 <= QUOTED_VALUE))) {
                            alt7 = 2;
                        } else {
                            NoViableAltException nvae =
                                    new NoViableAltException("", 7, 0, input);

                            throw nvae;
                        }
                        switch (alt7) {
                        case 1:
                        // BELStatement.g:85:10: it= term
                        {
                            pushFollow(FOLLOW_term_in_term445);
                            it = term();

                            state._fsp--;

                            adaptor.addChild(root_0, it.getTree());

                            parentTerm.addFunctionArgument(it.r);

                        }
                            break;
                        case 2:
                        // BELStatement.g:88:9: pp= param
                        {
                            pushFollow(FOLLOW_param_in_term461);
                            pp = param();

                            state._fsp--;

                            adaptor.addChild(root_0, pp.getTree());
                            parentTerm.addFunctionArgument(pp.r);

                        }
                            break;

                        }

                    }
                        break;

                    default:
                        break loop8;
                    }
                } while (true);

                CLOSE_PAREN8 =
                        (Token) match(input, CLOSE_PAREN,
                                FOLLOW_CLOSE_PAREN_in_term477);
                CLOSE_PAREN8_tree = adaptor.create(CLOSE_PAREN8);
                adaptor.addChild(root_0, CLOSE_PAREN8_tree);

                retval.r = parentTerm;

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                    adaptor.errorNode(input, retval.start, input.LT(-1), re);

        } finally {}
        return retval;
    }

    // $ANTLR end "term"

    public static class param_return extends ParserRuleReturnScope {
        public Parameter r;
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "param"
    // BELStatement.g:95:1: param returns [Parameter r] : (nsp= NS_PREFIX )? ( NS_VALUE | QUOTED_VALUE ) ;
    public final BELStatementParser.param_return param() {
        BELStatementParser.param_return retval =
                new BELStatementParser.param_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token nsp = null;
        Token NS_VALUE9 = null;
        Token QUOTED_VALUE10 = null;

        Object nsp_tree = null;
        Object NS_VALUE9_tree = null;
        Object QUOTED_VALUE10_tree = null;

        try {
            // BELStatement.g:95:28: ( (nsp= NS_PREFIX )? ( NS_VALUE | QUOTED_VALUE ) )
            // BELStatement.g:96:5: (nsp= NS_PREFIX )? ( NS_VALUE | QUOTED_VALUE )
            {
                root_0 = adaptor.nil();

                // BELStatement.g:96:8: (nsp= NS_PREFIX )?
                int alt9 = 2;
                int LA9_0 = input.LA(1);

                if ((LA9_0 == NS_PREFIX)) {
                    alt9 = 1;
                }
                switch (alt9) {
                case 1:
                // BELStatement.g:96:8: nsp= NS_PREFIX
                {
                    nsp =
                            (Token) match(input, NS_PREFIX,
                                    FOLLOW_NS_PREFIX_in_param501);
                    nsp_tree = adaptor.create(nsp);
                    adaptor.addChild(root_0, nsp_tree);

                }
                    break;

                }

                // BELStatement.g:97:5: ( NS_VALUE | QUOTED_VALUE )
                int alt10 = 2;
                int LA10_0 = input.LA(1);

                if ((LA10_0 == NS_VALUE)) {
                    alt10 = 1;
                } else if ((LA10_0 == QUOTED_VALUE)) {
                    alt10 = 2;
                } else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 10, 0, input);

                    throw nvae;
                }
                switch (alt10) {
                case 1:
                // BELStatement.g:98:9: NS_VALUE
                {
                    NS_VALUE9 =
                            (Token) match(input, NS_VALUE,
                                    FOLLOW_NS_VALUE_in_param518);
                    NS_VALUE9_tree = adaptor.create(NS_VALUE9);
                    adaptor.addChild(root_0, NS_VALUE9_tree);

                    Namespace ns = null;

                    if (nsp != null) {
                        String prefix = nsp.getText();
                        prefix = prefix.substring(0, prefix.length() - 1);
                        ns = new Namespace(prefix, "FIX_ME");
                    }

                    retval.r = new Parameter();
                    retval.r.setValue(NS_VALUE9.getText());
                    retval.r.setNamespace(ns);

                }
                    break;
                case 2:
                // BELStatement.g:111:9: QUOTED_VALUE
                {
                    QUOTED_VALUE10 =
                            (Token) match(input, QUOTED_VALUE,
                                    FOLLOW_QUOTED_VALUE_in_param532);
                    QUOTED_VALUE10_tree = adaptor.create(QUOTED_VALUE10);
                    adaptor.addChild(root_0, QUOTED_VALUE10_tree);

                    Namespace ns = null;

                    if (nsp != null) {
                        String prefix = nsp.getText();
                        prefix = prefix.substring(0, prefix.length() - 1);
                        ns = new Namespace(prefix, "FIX_ME");
                    }

                    retval.r = new Parameter();
                    retval.r.setValue(QUOTED_VALUE10.getText());
                    retval.r.setNamespace(ns);

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                    adaptor.errorNode(input, retval.start, input.LT(-1), re);

        } finally {}
        return retval;
    }

    // $ANTLR end "param"

    public static class function_return extends ParserRuleReturnScope {
        public FunctionEnum r;
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "function"
    // BELStatement.g:127:1: function returns [FunctionEnum r] : (fv= 'proteinAbundance' | fv= 'p' | fv= 'rnaAbundance'
    // | fv= 'r' | fv= 'abundance' | fv= 'a' | fv= 'microRNAAbundance' | fv= 'm' | fv= 'geneAbundance' | fv= 'g' | fv=
    // 'biologicalProcess' | fv= 'bp' | fv= 'pathology' | fv= 'path' | fv= 'complexAbundance' | fv= 'complex' | fv=
    // 'translocation' | fv= 'tloc' | fv= 'cellSecretion' | fv= 'sec' | fv= 'cellSurfaceExpression' | fv= 'surf' | fv=
    // 'reaction' | fv= 'rxn' | fv= 'compositeAbundance' | fv= 'composite' | fv= 'fusion' | fv= 'fus' | fv=
    // 'degradation' | fv= 'deg' | fv= 'molecularActivity' | fv= 'act' | fv= 'catalyticActivity' | fv= 'cat' | fv=
    // 'kinaseActivity' | fv= 'kin' | fv= 'phosphataseActivity' | fv= 'phos' | fv= 'peptidaseActivity' | fv= 'pep' | fv=
    // 'ribosylationActivity' | fv= 'ribo' | fv= 'transcriptionalActivity' | fv= 'tscript' | fv= 'transportActivity' |
    // fv= 'tport' | fv= 'gtpBoundActivity' | fv= 'gtp' | fv= 'chaperoneActivity' | fv= 'chap' | fv=
    // 'proteinModification' | fv= 'pmod' | fv= 'substitution' | fv= 'sub' | fv= 'truncation' | fv= 'trunc' | fv=
    // 'reactants' | fv= 'products' | fv= 'list' ) ;
    public final BELStatementParser.function_return function() {
        BELStatementParser.function_return retval =
                new BELStatementParser.function_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token fv = null;

        Object fv_tree = null;

        try {
            // BELStatement.g:127:34: ( (fv= 'proteinAbundance' | fv= 'p' | fv= 'rnaAbundance' | fv= 'r' | fv=
            // 'abundance' | fv= 'a' | fv= 'microRNAAbundance' | fv= 'm' | fv= 'geneAbundance' | fv= 'g' | fv=
            // 'biologicalProcess' | fv= 'bp' | fv= 'pathology' | fv= 'path' | fv= 'complexAbundance' | fv= 'complex' |
            // fv= 'translocation' | fv= 'tloc' | fv= 'cellSecretion' | fv= 'sec' | fv= 'cellSurfaceExpression' | fv=
            // 'surf' | fv= 'reaction' | fv= 'rxn' | fv= 'compositeAbundance' | fv= 'composite' | fv= 'fusion' | fv=
            // 'fus' | fv= 'degradation' | fv= 'deg' | fv= 'molecularActivity' | fv= 'act' | fv= 'catalyticActivity' |
            // fv= 'cat' | fv= 'kinaseActivity' | fv= 'kin' | fv= 'phosphataseActivity' | fv= 'phos' | fv=
            // 'peptidaseActivity' | fv= 'pep' | fv= 'ribosylationActivity' | fv= 'ribo' | fv= 'transcriptionalActivity'
            // | fv= 'tscript' | fv= 'transportActivity' | fv= 'tport' | fv= 'gtpBoundActivity' | fv= 'gtp' | fv=
            // 'chaperoneActivity' | fv= 'chap' | fv= 'proteinModification' | fv= 'pmod' | fv= 'substitution' | fv=
            // 'sub' | fv= 'truncation' | fv= 'trunc' | fv= 'reactants' | fv= 'products' | fv= 'list' ) )
            // BELStatement.g:128:5: (fv= 'proteinAbundance' | fv= 'p' | fv= 'rnaAbundance' | fv= 'r' | fv= 'abundance'
            // | fv= 'a' | fv= 'microRNAAbundance' | fv= 'm' | fv= 'geneAbundance' | fv= 'g' | fv= 'biologicalProcess' |
            // fv= 'bp' | fv= 'pathology' | fv= 'path' | fv= 'complexAbundance' | fv= 'complex' | fv= 'translocation' |
            // fv= 'tloc' | fv= 'cellSecretion' | fv= 'sec' | fv= 'cellSurfaceExpression' | fv= 'surf' | fv= 'reaction'
            // | fv= 'rxn' | fv= 'compositeAbundance' | fv= 'composite' | fv= 'fusion' | fv= 'fus' | fv= 'degradation' |
            // fv= 'deg' | fv= 'molecularActivity' | fv= 'act' | fv= 'catalyticActivity' | fv= 'cat' | fv=
            // 'kinaseActivity' | fv= 'kin' | fv= 'phosphataseActivity' | fv= 'phos' | fv= 'peptidaseActivity' | fv=
            // 'pep' | fv= 'ribosylationActivity' | fv= 'ribo' | fv= 'transcriptionalActivity' | fv= 'tscript' | fv=
            // 'transportActivity' | fv= 'tport' | fv= 'gtpBoundActivity' | fv= 'gtp' | fv= 'chaperoneActivity' | fv=
            // 'chap' | fv= 'proteinModification' | fv= 'pmod' | fv= 'substitution' | fv= 'sub' | fv= 'truncation' | fv=
            // 'trunc' | fv= 'reactants' | fv= 'products' | fv= 'list' )
            {
                root_0 = adaptor.nil();

                // BELStatement.g:128:5: (fv= 'proteinAbundance' | fv= 'p' | fv= 'rnaAbundance' | fv= 'r' | fv=
                // 'abundance' | fv= 'a' | fv= 'microRNAAbundance' | fv= 'm' | fv= 'geneAbundance' | fv= 'g' | fv=
                // 'biologicalProcess' | fv= 'bp' | fv= 'pathology' | fv= 'path' | fv= 'complexAbundance' | fv=
                // 'complex' | fv= 'translocation' | fv= 'tloc' | fv= 'cellSecretion' | fv= 'sec' | fv=
                // 'cellSurfaceExpression' | fv= 'surf' | fv= 'reaction' | fv= 'rxn' | fv= 'compositeAbundance' | fv=
                // 'composite' | fv= 'fusion' | fv= 'fus' | fv= 'degradation' | fv= 'deg' | fv= 'molecularActivity' |
                // fv= 'act' | fv= 'catalyticActivity' | fv= 'cat' | fv= 'kinaseActivity' | fv= 'kin' | fv=
                // 'phosphataseActivity' | fv= 'phos' | fv= 'peptidaseActivity' | fv= 'pep' | fv= 'ribosylationActivity'
                // | fv= 'ribo' | fv= 'transcriptionalActivity' | fv= 'tscript' | fv= 'transportActivity' | fv= 'tport'
                // | fv= 'gtpBoundActivity' | fv= 'gtp' | fv= 'chaperoneActivity' | fv= 'chap' | fv=
                // 'proteinModification' | fv= 'pmod' | fv= 'substitution' | fv= 'sub' | fv= 'truncation' | fv= 'trunc'
                // | fv= 'reactants' | fv= 'products' | fv= 'list' )
                int alt11 = 59;
                switch (input.LA(1)) {
                case 17: {
                    alt11 = 1;
                }
                    break;
                case 18: {
                    alt11 = 2;
                }
                    break;
                case 19: {
                    alt11 = 3;
                }
                    break;
                case 20: {
                    alt11 = 4;
                }
                    break;
                case 21: {
                    alt11 = 5;
                }
                    break;
                case 22: {
                    alt11 = 6;
                }
                    break;
                case 23: {
                    alt11 = 7;
                }
                    break;
                case 24: {
                    alt11 = 8;
                }
                    break;
                case 25: {
                    alt11 = 9;
                }
                    break;
                case 26: {
                    alt11 = 10;
                }
                    break;
                case 27: {
                    alt11 = 11;
                }
                    break;
                case 28: {
                    alt11 = 12;
                }
                    break;
                case 29: {
                    alt11 = 13;
                }
                    break;
                case 30: {
                    alt11 = 14;
                }
                    break;
                case 31: {
                    alt11 = 15;
                }
                    break;
                case 32: {
                    alt11 = 16;
                }
                    break;
                case 33: {
                    alt11 = 17;
                }
                    break;
                case 34: {
                    alt11 = 18;
                }
                    break;
                case 35: {
                    alt11 = 19;
                }
                    break;
                case 36: {
                    alt11 = 20;
                }
                    break;
                case 37: {
                    alt11 = 21;
                }
                    break;
                case 38: {
                    alt11 = 22;
                }
                    break;
                case 39: {
                    alt11 = 23;
                }
                    break;
                case 40: {
                    alt11 = 24;
                }
                    break;
                case 41: {
                    alt11 = 25;
                }
                    break;
                case 42: {
                    alt11 = 26;
                }
                    break;
                case 43: {
                    alt11 = 27;
                }
                    break;
                case 44: {
                    alt11 = 28;
                }
                    break;
                case 45: {
                    alt11 = 29;
                }
                    break;
                case 46: {
                    alt11 = 30;
                }
                    break;
                case 47: {
                    alt11 = 31;
                }
                    break;
                case 48: {
                    alt11 = 32;
                }
                    break;
                case 49: {
                    alt11 = 33;
                }
                    break;
                case 50: {
                    alt11 = 34;
                }
                    break;
                case 51: {
                    alt11 = 35;
                }
                    break;
                case 52: {
                    alt11 = 36;
                }
                    break;
                case 53: {
                    alt11 = 37;
                }
                    break;
                case 54: {
                    alt11 = 38;
                }
                    break;
                case 55: {
                    alt11 = 39;
                }
                    break;
                case 56: {
                    alt11 = 40;
                }
                    break;
                case 57: {
                    alt11 = 41;
                }
                    break;
                case 58: {
                    alt11 = 42;
                }
                    break;
                case 59: {
                    alt11 = 43;
                }
                    break;
                case 60: {
                    alt11 = 44;
                }
                    break;
                case 61: {
                    alt11 = 45;
                }
                    break;
                case 62: {
                    alt11 = 46;
                }
                    break;
                case 63: {
                    alt11 = 47;
                }
                    break;
                case 64: {
                    alt11 = 48;
                }
                    break;
                case 65: {
                    alt11 = 49;
                }
                    break;
                case 66: {
                    alt11 = 50;
                }
                    break;
                case 67: {
                    alt11 = 51;
                }
                    break;
                case 68: {
                    alt11 = 52;
                }
                    break;
                case 69: {
                    alt11 = 53;
                }
                    break;
                case 70: {
                    alt11 = 54;
                }
                    break;
                case 71: {
                    alt11 = 55;
                }
                    break;
                case 72: {
                    alt11 = 56;
                }
                    break;
                case 73: {
                    alt11 = 57;
                }
                    break;
                case 74: {
                    alt11 = 58;
                }
                    break;
                case 75: {
                    alt11 = 59;
                }
                    break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 11, 0, input);

                    throw nvae;
                }

                switch (alt11) {
                case 1:
                // BELStatement.g:129:9: fv= 'proteinAbundance'
                {
                    fv = (Token) match(input, 17, FOLLOW_17_in_function572);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 2:
                // BELStatement.g:130:9: fv= 'p'
                {
                    fv = (Token) match(input, 18, FOLLOW_18_in_function598);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 3:
                // BELStatement.g:131:9: fv= 'rnaAbundance'
                {
                    fv = (Token) match(input, 19, FOLLOW_19_in_function639);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 4:
                // BELStatement.g:132:9: fv= 'r'
                {
                    fv = (Token) match(input, 20, FOLLOW_20_in_function670);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 5:
                // BELStatement.g:133:9: fv= 'abundance'
                {
                    fv = (Token) match(input, 21, FOLLOW_21_in_function711);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 6:
                // BELStatement.g:134:9: fv= 'a'
                {
                    fv = (Token) match(input, 22, FOLLOW_22_in_function745);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 7:
                // BELStatement.g:135:9: fv= 'microRNAAbundance'
                {
                    fv = (Token) match(input, 23, FOLLOW_23_in_function786);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 8:
                // BELStatement.g:136:9: fv= 'm'
                {
                    fv = (Token) match(input, 24, FOLLOW_24_in_function812);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 9:
                // BELStatement.g:137:9: fv= 'geneAbundance'
                {
                    fv = (Token) match(input, 25, FOLLOW_25_in_function853);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 10:
                // BELStatement.g:138:9: fv= 'g'
                {
                    fv = (Token) match(input, 26, FOLLOW_26_in_function882);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 11:
                // BELStatement.g:139:9: fv= 'biologicalProcess'
                {
                    fv = (Token) match(input, 27, FOLLOW_27_in_function923);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 12:
                // BELStatement.g:140:9: fv= 'bp'
                {
                    fv = (Token) match(input, 28, FOLLOW_28_in_function949);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 13:
                // BELStatement.g:141:9: fv= 'pathology'
                {
                    fv = (Token) match(input, 29, FOLLOW_29_in_function989);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 14:
                // BELStatement.g:142:9: fv= 'path'
                {
                    fv = (Token) match(input, 30, FOLLOW_30_in_function1022);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 15:
                // BELStatement.g:143:9: fv= 'complexAbundance'
                {
                    fv = (Token) match(input, 31, FOLLOW_31_in_function1060);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 16:
                // BELStatement.g:144:9: fv= 'complex'
                {
                    fv = (Token) match(input, 32, FOLLOW_32_in_function1087);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 17:
                // BELStatement.g:145:9: fv= 'translocation'
                {
                    fv = (Token) match(input, 33, FOLLOW_33_in_function1122);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 18:
                // BELStatement.g:146:9: fv= 'tloc'
                {
                    fv = (Token) match(input, 34, FOLLOW_34_in_function1152);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 19:
                // BELStatement.g:147:9: fv= 'cellSecretion'
                {
                    fv = (Token) match(input, 35, FOLLOW_35_in_function1190);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 20:
                // BELStatement.g:148:9: fv= 'sec'
                {
                    fv = (Token) match(input, 36, FOLLOW_36_in_function1220);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 21:
                // BELStatement.g:149:9: fv= 'cellSurfaceExpression'
                {
                    fv = (Token) match(input, 37, FOLLOW_37_in_function1259);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 22:
                // BELStatement.g:150:9: fv= 'surf'
                {
                    fv = (Token) match(input, 38, FOLLOW_38_in_function1280);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 23:
                // BELStatement.g:151:9: fv= 'reaction'
                {
                    fv = (Token) match(input, 39, FOLLOW_39_in_function1318);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 24:
                // BELStatement.g:152:9: fv= 'rxn'
                {
                    fv = (Token) match(input, 40, FOLLOW_40_in_function1352);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 25:
                // BELStatement.g:153:9: fv= 'compositeAbundance'
                {
                    fv = (Token) match(input, 41, FOLLOW_41_in_function1391);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 26:
                // BELStatement.g:154:9: fv= 'composite'
                {
                    fv = (Token) match(input, 42, FOLLOW_42_in_function1415);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 27:
                // BELStatement.g:155:9: fv= 'fusion'
                {
                    fv = (Token) match(input, 43, FOLLOW_43_in_function1448);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 28:
                // BELStatement.g:156:9: fv= 'fus'
                {
                    fv = (Token) match(input, 44, FOLLOW_44_in_function1484);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 29:
                // BELStatement.g:157:9: fv= 'degradation'
                {
                    fv = (Token) match(input, 45, FOLLOW_45_in_function1523);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 30:
                // BELStatement.g:158:9: fv= 'deg'
                {
                    fv = (Token) match(input, 46, FOLLOW_46_in_function1554);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 31:
                // BELStatement.g:159:9: fv= 'molecularActivity'
                {
                    fv = (Token) match(input, 47, FOLLOW_47_in_function1593);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 32:
                // BELStatement.g:160:9: fv= 'act'
                {
                    fv = (Token) match(input, 48, FOLLOW_48_in_function1618);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 33:
                // BELStatement.g:161:9: fv= 'catalyticActivity'
                {
                    fv = (Token) match(input, 49, FOLLOW_49_in_function1657);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 34:
                // BELStatement.g:162:9: fv= 'cat'
                {
                    fv = (Token) match(input, 50, FOLLOW_50_in_function1682);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 35:
                // BELStatement.g:163:9: fv= 'kinaseActivity'
                {
                    fv = (Token) match(input, 51, FOLLOW_51_in_function1721);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 36:
                // BELStatement.g:164:9: fv= 'kin'
                {
                    fv = (Token) match(input, 52, FOLLOW_52_in_function1749);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 37:
                // BELStatement.g:165:9: fv= 'phosphataseActivity'
                {
                    fv = (Token) match(input, 53, FOLLOW_53_in_function1788);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 38:
                // BELStatement.g:166:9: fv= 'phos'
                {
                    fv = (Token) match(input, 54, FOLLOW_54_in_function1811);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 39:
                // BELStatement.g:167:9: fv= 'peptidaseActivity'
                {
                    fv = (Token) match(input, 55, FOLLOW_55_in_function1849);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 40:
                // BELStatement.g:168:9: fv= 'pep'
                {
                    fv = (Token) match(input, 56, FOLLOW_56_in_function1874);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 41:
                // BELStatement.g:169:9: fv= 'ribosylationActivity'
                {
                    fv = (Token) match(input, 57, FOLLOW_57_in_function1913);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 42:
                // BELStatement.g:170:9: fv= 'ribo'
                {
                    fv = (Token) match(input, 58, FOLLOW_58_in_function1935);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 43:
                // BELStatement.g:171:9: fv= 'transcriptionalActivity'
                {
                    fv = (Token) match(input, 59, FOLLOW_59_in_function1973);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 44:
                // BELStatement.g:172:9: fv= 'tscript'
                {
                    fv = (Token) match(input, 60, FOLLOW_60_in_function1992);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 45:
                // BELStatement.g:173:9: fv= 'transportActivity'
                {
                    fv = (Token) match(input, 61, FOLLOW_61_in_function2027);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 46:
                // BELStatement.g:174:9: fv= 'tport'
                {
                    fv = (Token) match(input, 62, FOLLOW_62_in_function2052);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 47:
                // BELStatement.g:175:9: fv= 'gtpBoundActivity'
                {
                    fv = (Token) match(input, 63, FOLLOW_63_in_function2089);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 48:
                // BELStatement.g:176:9: fv= 'gtp'
                {
                    fv = (Token) match(input, 64, FOLLOW_64_in_function2115);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 49:
                // BELStatement.g:177:9: fv= 'chaperoneActivity'
                {
                    fv = (Token) match(input, 65, FOLLOW_65_in_function2154);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 50:
                // BELStatement.g:178:9: fv= 'chap'
                {
                    fv = (Token) match(input, 66, FOLLOW_66_in_function2179);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 51:
                // BELStatement.g:179:9: fv= 'proteinModification'
                {
                    fv = (Token) match(input, 67, FOLLOW_67_in_function2217);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 52:
                // BELStatement.g:180:9: fv= 'pmod'
                {
                    fv = (Token) match(input, 68, FOLLOW_68_in_function2240);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 53:
                // BELStatement.g:181:9: fv= 'substitution'
                {
                    fv = (Token) match(input, 69, FOLLOW_69_in_function2279);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 54:
                // BELStatement.g:182:9: fv= 'sub'
                {
                    fv = (Token) match(input, 70, FOLLOW_70_in_function2309);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 55:
                // BELStatement.g:183:9: fv= 'truncation'
                {
                    fv = (Token) match(input, 71, FOLLOW_71_in_function2348);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 56:
                // BELStatement.g:184:9: fv= 'trunc'
                {
                    fv = (Token) match(input, 72, FOLLOW_72_in_function2380);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 57:
                // BELStatement.g:185:9: fv= 'reactants'
                {
                    fv = (Token) match(input, 73, FOLLOW_73_in_function2417);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 58:
                // BELStatement.g:186:9: fv= 'products'
                {
                    fv = (Token) match(input, 74, FOLLOW_74_in_function2450);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;
                case 59:
                // BELStatement.g:187:9: fv= 'list'
                {
                    fv = (Token) match(input, 75, FOLLOW_75_in_function2484);
                    fv_tree = adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                    adaptor.errorNode(input, retval.start, input.LT(-1), re);

        } finally {}
        return retval;
    }

    // $ANTLR end "function"

    public static class relationship_return extends ParserRuleReturnScope {
        public RelationshipType r;
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "relationship"
    // BELStatement.g:191:1: relationship returns [RelationshipType r] : (rv= 'increases' | rv= '->' | rv= 'decreases' |
    // rv= '-|' | rv= 'directlyIncreases' | rv= '=>' | rv= 'directlyDecreases' | rv= '=|' | rv= 'causesNoChange' | rv=
    // 'positiveCorrelation' | rv= 'negativeCorrelation' | rv= 'translatedTo' | rv= '>>' | rv= 'transcribedTo' | rv=
    // ':>' | rv= 'isA' | rv= 'subProcessOf' | rv= 'rateLimitingStepOf' | rv= 'biomarkerFor' | rv=
    // 'prognosticBiomarkerFor' | rv= 'orthologous' | rv= 'analogous' | rv= 'association' | rv= '--' | rv= 'hasMembers'
    // | rv= 'hasComponents' | rv= 'hasMember' | rv= 'hasComponent' ) ;
    public final BELStatementParser.relationship_return relationship() {
        BELStatementParser.relationship_return retval =
                new BELStatementParser.relationship_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token rv = null;

        Object rv_tree = null;

        try {
            // BELStatement.g:191:42: ( (rv= 'increases' | rv= '->' | rv= 'decreases' | rv= '-|' | rv=
            // 'directlyIncreases' | rv= '=>' | rv= 'directlyDecreases' | rv= '=|' | rv= 'causesNoChange' | rv=
            // 'positiveCorrelation' | rv= 'negativeCorrelation' | rv= 'translatedTo' | rv= '>>' | rv= 'transcribedTo' |
            // rv= ':>' | rv= 'isA' | rv= 'subProcessOf' | rv= 'rateLimitingStepOf' | rv= 'biomarkerFor' | rv=
            // 'prognosticBiomarkerFor' | rv= 'orthologous' | rv= 'analogous' | rv= 'association' | rv= '--' | rv=
            // 'hasMembers' | rv= 'hasComponents' | rv= 'hasMember' | rv= 'hasComponent' ) )
            // BELStatement.g:192:5: (rv= 'increases' | rv= '->' | rv= 'decreases' | rv= '-|' | rv= 'directlyIncreases'
            // | rv= '=>' | rv= 'directlyDecreases' | rv= '=|' | rv= 'causesNoChange' | rv= 'positiveCorrelation' | rv=
            // 'negativeCorrelation' | rv= 'translatedTo' | rv= '>>' | rv= 'transcribedTo' | rv= ':>' | rv= 'isA' | rv=
            // 'subProcessOf' | rv= 'rateLimitingStepOf' | rv= 'biomarkerFor' | rv= 'prognosticBiomarkerFor' | rv=
            // 'orthologous' | rv= 'analogous' | rv= 'association' | rv= '--' | rv= 'hasMembers' | rv= 'hasComponents' |
            // rv= 'hasMember' | rv= 'hasComponent' )
            {
                root_0 = adaptor.nil();

                // BELStatement.g:192:5: (rv= 'increases' | rv= '->' | rv= 'decreases' | rv= '-|' | rv=
                // 'directlyIncreases' | rv= '=>' | rv= 'directlyDecreases' | rv= '=|' | rv= 'causesNoChange' | rv=
                // 'positiveCorrelation' | rv= 'negativeCorrelation' | rv= 'translatedTo' | rv= '>>' | rv=
                // 'transcribedTo' | rv= ':>' | rv= 'isA' | rv= 'subProcessOf' | rv= 'rateLimitingStepOf' | rv=
                // 'biomarkerFor' | rv= 'prognosticBiomarkerFor' | rv= 'orthologous' | rv= 'analogous' | rv=
                // 'association' | rv= '--' | rv= 'hasMembers' | rv= 'hasComponents' | rv= 'hasMember' | rv=
                // 'hasComponent' )
                int alt12 = 28;
                switch (input.LA(1)) {
                case 76: {
                    alt12 = 1;
                }
                    break;
                case 77: {
                    alt12 = 2;
                }
                    break;
                case 78: {
                    alt12 = 3;
                }
                    break;
                case 79: {
                    alt12 = 4;
                }
                    break;
                case 80: {
                    alt12 = 5;
                }
                    break;
                case 81: {
                    alt12 = 6;
                }
                    break;
                case 82: {
                    alt12 = 7;
                }
                    break;
                case 83: {
                    alt12 = 8;
                }
                    break;
                case 84: {
                    alt12 = 9;
                }
                    break;
                case 85: {
                    alt12 = 10;
                }
                    break;
                case 86: {
                    alt12 = 11;
                }
                    break;
                case 87: {
                    alt12 = 12;
                }
                    break;
                case 88: {
                    alt12 = 13;
                }
                    break;
                case 89: {
                    alt12 = 14;
                }
                    break;
                case 90: {
                    alt12 = 15;
                }
                    break;
                case 91: {
                    alt12 = 16;
                }
                    break;
                case 92: {
                    alt12 = 17;
                }
                    break;
                case 93: {
                    alt12 = 18;
                }
                    break;
                case 94: {
                    alt12 = 19;
                }
                    break;
                case 95: {
                    alt12 = 20;
                }
                    break;
                case 96: {
                    alt12 = 21;
                }
                    break;
                case 97: {
                    alt12 = 22;
                }
                    break;
                case 98: {
                    alt12 = 23;
                }
                    break;
                case 99: {
                    alt12 = 24;
                }
                    break;
                case 100: {
                    alt12 = 25;
                }
                    break;
                case 101: {
                    alt12 = 26;
                }
                    break;
                case 102: {
                    alt12 = 27;
                }
                    break;
                case 103: {
                    alt12 = 28;
                }
                    break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 12, 0, input);

                    throw nvae;
                }

                switch (alt12) {
                case 1:
                // BELStatement.g:193:9: rv= 'increases'
                {
                    rv =
                            (Token) match(input, 76,
                                    FOLLOW_76_in_relationship2550);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 2:
                // BELStatement.g:194:9: rv= '->'
                {
                    rv =
                            (Token) match(input, 77,
                                    FOLLOW_77_in_relationship2583);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 3:
                // BELStatement.g:195:9: rv= 'decreases'
                {
                    rv =
                            (Token) match(input, 78,
                                    FOLLOW_78_in_relationship2623);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 4:
                // BELStatement.g:196:9: rv= '-|'
                {
                    rv =
                            (Token) match(input, 79,
                                    FOLLOW_79_in_relationship2656);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 5:
                // BELStatement.g:197:9: rv= 'directlyIncreases'
                {
                    rv =
                            (Token) match(input, 80,
                                    FOLLOW_80_in_relationship2696);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 6:
                // BELStatement.g:198:9: rv= '=>'
                {
                    rv =
                            (Token) match(input, 81,
                                    FOLLOW_81_in_relationship2721);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 7:
                // BELStatement.g:199:9: rv= 'directlyDecreases'
                {
                    rv =
                            (Token) match(input, 82,
                                    FOLLOW_82_in_relationship2761);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 8:
                // BELStatement.g:200:9: rv= '=|'
                {
                    rv =
                            (Token) match(input, 83,
                                    FOLLOW_83_in_relationship2786);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 9:
                // BELStatement.g:201:9: rv= 'causesNoChange'
                {
                    rv =
                            (Token) match(input, 84,
                                    FOLLOW_84_in_relationship2826);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 10:
                // BELStatement.g:202:9: rv= 'positiveCorrelation'
                {
                    rv =
                            (Token) match(input, 85,
                                    FOLLOW_85_in_relationship2854);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 11:
                // BELStatement.g:203:9: rv= 'negativeCorrelation'
                {
                    rv =
                            (Token) match(input, 86,
                                    FOLLOW_86_in_relationship2877);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 12:
                // BELStatement.g:204:9: rv= 'translatedTo'
                {
                    rv =
                            (Token) match(input, 87,
                                    FOLLOW_87_in_relationship2900);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 13:
                // BELStatement.g:205:9: rv= '>>'
                {
                    rv =
                            (Token) match(input, 88,
                                    FOLLOW_88_in_relationship2930);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 14:
                // BELStatement.g:206:9: rv= 'transcribedTo'
                {
                    rv =
                            (Token) match(input, 89,
                                    FOLLOW_89_in_relationship2970);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 15:
                // BELStatement.g:207:9: rv= ':>'
                {
                    rv =
                            (Token) match(input, 90,
                                    FOLLOW_90_in_relationship2999);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 16:
                // BELStatement.g:208:9: rv= 'isA'
                {
                    rv =
                            (Token) match(input, 91,
                                    FOLLOW_91_in_relationship3039);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 17:
                // BELStatement.g:209:9: rv= 'subProcessOf'
                {
                    rv =
                            (Token) match(input, 92,
                                    FOLLOW_92_in_relationship3078);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 18:
                // BELStatement.g:210:9: rv= 'rateLimitingStepOf'
                {
                    rv =
                            (Token) match(input, 93,
                                    FOLLOW_93_in_relationship3108);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 19:
                // BELStatement.g:211:9: rv= 'biomarkerFor'
                {
                    rv =
                            (Token) match(input, 94,
                                    FOLLOW_94_in_relationship3132);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 20:
                // BELStatement.g:212:9: rv= 'prognosticBiomarkerFor'
                {
                    rv =
                            (Token) match(input, 95,
                                    FOLLOW_95_in_relationship3162);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 21:
                // BELStatement.g:213:9: rv= 'orthologous'
                {
                    rv =
                            (Token) match(input, 96,
                                    FOLLOW_96_in_relationship3182);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 22:
                // BELStatement.g:214:9: rv= 'analogous'
                {
                    rv =
                            (Token) match(input, 97,
                                    FOLLOW_97_in_relationship3213);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 23:
                // BELStatement.g:215:9: rv= 'association'
                {
                    rv =
                            (Token) match(input, 98,
                                    FOLLOW_98_in_relationship3246);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 24:
                // BELStatement.g:216:9: rv= '--'
                {
                    rv =
                            (Token) match(input, 99,
                                    FOLLOW_99_in_relationship3277);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 25:
                // BELStatement.g:217:9: rv= 'hasMembers'
                {
                    rv =
                            (Token) match(input, 100,
                                    FOLLOW_100_in_relationship3317);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 26:
                // BELStatement.g:218:9: rv= 'hasComponents'
                {
                    rv =
                            (Token) match(input, 101,
                                    FOLLOW_101_in_relationship3349);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 27:
                // BELStatement.g:219:9: rv= 'hasMember'
                {
                    rv =
                            (Token) match(input, 102,
                                    FOLLOW_102_in_relationship3378);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;
                case 28:
                // BELStatement.g:220:9: rv= 'hasComponent'
                {
                    rv =
                            (Token) match(input, 103,
                                    FOLLOW_103_in_relationship3411);
                    rv_tree = adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = RelationshipType.fromString(rv.getText());
                    if (retval.r == null) {
                        retval.r =
                                RelationshipType.fromAbbreviation(rv.getText());
                    }

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                    adaptor.errorNode(input, retval.start, input.LT(-1), re);

        } finally {}
        return retval;
    }

    // $ANTLR end "relationship"

    // Delegated rules

    public static final BitSet FOLLOW_outer_term_in_statement72 = new BitSet(
            new long[] { 0x0000000000000002L,
                    0x000000FFFFFFF000L });
    public static final BitSet FOLLOW_relationship_in_statement92 = new BitSet(
            new long[] { 0xFFFFFFFFFFFE0010L,
                    0x0000000000000FFFL });
    public static final BitSet FOLLOW_OPEN_PAREN_in_statement136 = new BitSet(
            new long[] { 0xFFFFFFFFFFFE0010L,
                    0x0000000000000FFFL });
    public static final BitSet FOLLOW_outer_term_in_statement157 = new BitSet(
            new long[] { 0x0000000000000000L,
                    0x000000FFFFFFF000L });
    public static final BitSet FOLLOW_relationship_in_statement179 =
            new BitSet(new long[] { 0xFFFFFFFFFFFE0010L,
                    0x0000000000000FFFL });
    public static final BitSet FOLLOW_outer_term_in_statement202 = new BitSet(
            new long[] { 0x0000000000000020L });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_statement222 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_outer_term_in_statement271 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_function_in_outer_term313 = new BitSet(
            new long[] { 0x0000000000000010L });
    public static final BitSet FOLLOW_OPEN_PAREN_in_outer_term321 = new BitSet(
            new long[] { 0xFFFFFFFFFFFF01F0L,
                    0x0000000000000FFFL });
    public static final BitSet FOLLOW_16_in_outer_term328 = new BitSet(
            new long[] { 0xFFFFFFFFFFFF01D0L,
                    0x0000000000000FFFL });
    public static final BitSet FOLLOW_argument_in_outer_term333 = new BitSet(
            new long[] { 0xFFFFFFFFFFFF01F0L,
                    0x0000000000000FFFL });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_outer_term343 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_param_in_argument371 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_term_in_argument383 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_function_in_term407 = new BitSet(
            new long[] { 0x0000000000000010L });
    public static final BitSet FOLLOW_OPEN_PAREN_in_term415 = new BitSet(
            new long[] { 0xFFFFFFFFFFFF01F0L,
                    0x0000000000000FFFL });
    public static final BitSet FOLLOW_16_in_term431 = new BitSet(
            new long[] { 0xFFFFFFFFFFFF01D0L, 0x0000000000000FFFL });
    public static final BitSet FOLLOW_term_in_term445 = new BitSet(new long[] {
            0xFFFFFFFFFFFF01F0L,
            0x0000000000000FFFL });
    public static final BitSet FOLLOW_param_in_term461 = new BitSet(new long[] {
            0xFFFFFFFFFFFF01F0L,
            0x0000000000000FFFL });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_term477 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_NS_PREFIX_in_param501 = new BitSet(
            new long[] { 0x0000000000000180L });
    public static final BitSet FOLLOW_NS_VALUE_in_param518 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_param532 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_17_in_function572 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_18_in_function598 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_19_in_function639 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_20_in_function670 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_21_in_function711 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_22_in_function745 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_23_in_function786 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_24_in_function812 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_25_in_function853 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_26_in_function882 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_27_in_function923 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_28_in_function949 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_29_in_function989 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_30_in_function1022 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_31_in_function1060 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_32_in_function1087 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_33_in_function1122 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_34_in_function1152 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_35_in_function1190 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_36_in_function1220 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_37_in_function1259 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_38_in_function1280 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_39_in_function1318 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_40_in_function1352 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_41_in_function1391 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_42_in_function1415 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_43_in_function1448 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_44_in_function1484 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_45_in_function1523 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_46_in_function1554 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_47_in_function1593 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_48_in_function1618 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_49_in_function1657 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_50_in_function1682 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_51_in_function1721 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_52_in_function1749 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_53_in_function1788 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_54_in_function1811 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_55_in_function1849 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_56_in_function1874 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_57_in_function1913 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_58_in_function1935 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_59_in_function1973 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_60_in_function1992 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_61_in_function2027 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_62_in_function2052 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_63_in_function2089 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_64_in_function2115 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_65_in_function2154 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_66_in_function2179 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_67_in_function2217 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_68_in_function2240 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_69_in_function2279 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_70_in_function2309 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_71_in_function2348 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_72_in_function2380 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_73_in_function2417 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_74_in_function2450 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_75_in_function2484 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_76_in_relationship2550 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_77_in_relationship2583 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_78_in_relationship2623 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_79_in_relationship2656 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_80_in_relationship2696 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_81_in_relationship2721 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_82_in_relationship2761 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_83_in_relationship2786 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_84_in_relationship2826 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_85_in_relationship2854 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_86_in_relationship2877 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_87_in_relationship2900 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_88_in_relationship2930 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_89_in_relationship2970 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_90_in_relationship2999 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_91_in_relationship3039 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_92_in_relationship3078 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_93_in_relationship3108 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_94_in_relationship3132 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_95_in_relationship3162 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_96_in_relationship3182 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_97_in_relationship3213 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_98_in_relationship3246 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_99_in_relationship3277 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_100_in_relationship3317 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_101_in_relationship3349 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_102_in_relationship3378 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_103_in_relationship3411 = new BitSet(
            new long[] { 0x0000000000000002L });
}
