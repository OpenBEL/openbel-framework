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
// $ANTLR 3.4 BELScript.g 2012-06-27 17:18:02

package org.openbel.framework.common.bel.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;
import org.openbel.bel.model.BELParseErrorException;

@SuppressWarnings({ "unused" })
public class BELScriptParser extends Parser {
    public static final String[] tokenNames = new String[] {
            "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CLOSE_PAREN", "COMMA",
            "DIGIT", "DOCUMENT_COMMENT", "DOCUMENT_KEYWORD", "EscapeSequence",
            "HexDigit", "IDENT_LIST", "LETTER", "NEWLINE", "NS_PREFIX",
            "OBJECT_IDENT", "OPEN_PAREN", "OctalEscape", "QUOTED_VALUE",
            "STATEMENT_COMMENT", "STATEMENT_GROUP_KEYWORD", "UnicodeEscape",
            "VALUE_LIST", "WS", "','", "'--'", "'->'", "'-|'", "':>'", "'='",
            "'=>'", "'=|'", "'>>'", "'ANNOTATION'", "'AS'", "'Authors'",
            "'ContactInfo'", "'Copyright'", "'DEFAULT'", "'DEFINE'",
            "'Description'", "'Disclaimer'", "'LIST'", "'Licenses'",
            "'NAMESPACE'", "'Name'", "'PATTERN'", "'SET'", "'UNSET'", "'URL'",
            "'Version'", "'a'", "'abundance'", "'act'", "'analogous'",
            "'association'", "'biologicalProcess'", "'biomarkerFor'", "'bp'",
            "'cat'", "'catalyticActivity'", "'causesNoChange'",
            "'cellSecretion'", "'cellSurfaceExpression'", "'chap'",
            "'chaperoneActivity'", "'complex'", "'complexAbundance'",
            "'composite'", "'compositeAbundance'", "'decreases'", "'deg'",
            "'degradation'", "'directlyDecreases'", "'directlyIncreases'",
            "'fus'", "'fusion'", "'g'", "'geneAbundance'", "'gtp'",
            "'gtpBoundActivity'", "'hasComponent'", "'hasComponents'",
            "'hasMember'", "'hasMembers'", "'increases'", "'isA'", "'kin'",
            "'kinaseActivity'", "'list'", "'m'", "'microRNAAbundance'",
            "'molecularActivity'", "'negativeCorrelation'", "'orthologous'",
            "'p'", "'path'", "'pathology'", "'pep'", "'peptidaseActivity'",
            "'phos'", "'phosphataseActivity'", "'pmod'",
            "'positiveCorrelation'", "'products'", "'prognosticBiomarkerFor'",
            "'proteinAbundance'", "'proteinModification'", "'r'",
            "'rateLimitingStepOf'", "'reactants'", "'reaction'", "'ribo'",
            "'ribosylationActivity'", "'rnaAbundance'", "'rxn'", "'sec'",
            "'sub'", "'subProcessOf'", "'substitution'", "'surf'", "'tloc'",
            "'tport'", "'transcribedTo'", "'transcriptionalActivity'",
            "'translatedTo'", "'translocation'", "'transportActivity'",
            "'trunc'", "'truncation'", "'tscript'"
    };

    public static final int EOF = -1;
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
    public static final int T__104 = 104;
    public static final int T__105 = 105;
    public static final int T__106 = 106;
    public static final int T__107 = 107;
    public static final int T__108 = 108;
    public static final int T__109 = 109;
    public static final int T__110 = 110;
    public static final int T__111 = 111;
    public static final int T__112 = 112;
    public static final int T__113 = 113;
    public static final int T__114 = 114;
    public static final int T__115 = 115;
    public static final int T__116 = 116;
    public static final int T__117 = 117;
    public static final int T__118 = 118;
    public static final int T__119 = 119;
    public static final int T__120 = 120;
    public static final int T__121 = 121;
    public static final int T__122 = 122;
    public static final int T__123 = 123;
    public static final int T__124 = 124;
    public static final int T__125 = 125;
    public static final int T__126 = 126;
    public static final int T__127 = 127;
    public static final int T__128 = 128;
    public static final int T__129 = 129;
    public static final int T__130 = 130;
    public static final int CLOSE_PAREN = 4;
    public static final int COMMA = 5;
    public static final int DIGIT = 6;
    public static final int DOCUMENT_COMMENT = 7;
    public static final int DOCUMENT_KEYWORD = 8;
    public static final int EscapeSequence = 9;
    public static final int HexDigit = 10;
    public static final int IDENT_LIST = 11;
    public static final int LETTER = 12;
    public static final int NEWLINE = 13;
    public static final int NS_PREFIX = 14;
    public static final int OBJECT_IDENT = 15;
    public static final int OPEN_PAREN = 16;
    public static final int OctalEscape = 17;
    public static final int QUOTED_VALUE = 18;
    public static final int STATEMENT_COMMENT = 19;
    public static final int STATEMENT_GROUP_KEYWORD = 20;
    public static final int UnicodeEscape = 21;
    public static final int VALUE_LIST = 22;
    public static final int WS = 23;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators

    public BELScriptParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }

    public BELScriptParser(TokenStream input, RecognizerSharedState state) {
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
        return BELScriptParser.tokenNames;
    }

    @Override
    public String getGrammarFileName() {
        return "BELScript.g";
    }

    private final List<BELParseErrorException> syntaxErrors =
            new ArrayList<BELParseErrorException>();
    private final Stack<String> paraphrases = new Stack<String>();

    public List<BELParseErrorException> getSyntaxErrors() {
        return syntaxErrors;
    }

    @Override
    public void emitErrorMessage(String msg) {
    }

    @Override
    public void displayRecognitionError(String[] tokenNames,
            RecognitionException e) {
        String context = "";
        if (paraphrases.size() > 0) {
            context = paraphrases.peek();
        }
        syntaxErrors.add(new BELParseErrorException.SyntaxException(e.line,
                e.charPositionInLine, context, e));
    }

    public static class document_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "document"
    // BELScript.g:44:1: document : ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF ;
    public final BELScriptParser.document_return document()
            throws RecognitionException {
        BELScriptParser.document_return retval =
                new BELScriptParser.document_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NEWLINE1 = null;
        Token DOCUMENT_COMMENT2 = null;
        Token EOF4 = null;
        BELScriptParser.record_return record3 = null;

        Object NEWLINE1_tree = null;
        Object DOCUMENT_COMMENT2_tree = null;
        Object EOF4_tree = null;

        try {
            // BELScript.g:44:9: ( ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF )
            // BELScript.g:45:5: ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF
            {
                root_0 = adaptor.nil();

                // BELScript.g:45:5: ( NEWLINE | DOCUMENT_COMMENT | record )+
                int cnt1 = 0;
                loop1: do {
                    int alt1 = 4;
                    switch (input.LA(1)) {
                    case NEWLINE: {
                        alt1 = 1;
                    }
                        break;
                    case DOCUMENT_COMMENT: {
                        alt1 = 2;
                    }
                        break;
                    case 39:
                    case 47:
                    case 48:
                    case 51:
                    case 52:
                    case 53:
                    case 56:
                    case 58:
                    case 59:
                    case 60:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                    case 68:
                    case 69:
                    case 71:
                    case 72:
                    case 75:
                    case 76:
                    case 77:
                    case 78:
                    case 79:
                    case 80:
                    case 87:
                    case 88:
                    case 89:
                    case 90:
                    case 91:
                    case 92:
                    case 95:
                    case 96:
                    case 97:
                    case 98:
                    case 99:
                    case 100:
                    case 101:
                    case 102:
                    case 104:
                    case 106:
                    case 107:
                    case 108:
                    case 110:
                    case 111:
                    case 112:
                    case 113:
                    case 114:
                    case 115:
                    case 116:
                    case 117:
                    case 119:
                    case 120:
                    case 121:
                    case 122:
                    case 124:
                    case 126:
                    case 127:
                    case 128:
                    case 129:
                    case 130: {
                        alt1 = 3;
                    }
                        break;

                    }

                    switch (alt1) {
                    case 1:
                    // BELScript.g:45:6: NEWLINE
                    {
                        NEWLINE1 =
                                (Token) match(input, NEWLINE,
                                        FOLLOW_NEWLINE_in_document62);
                        NEWLINE1_tree =
                                adaptor.create(NEWLINE1);
                        adaptor.addChild(root_0, NEWLINE1_tree);

                    }
                        break;
                    case 2:
                    // BELScript.g:45:16: DOCUMENT_COMMENT
                    {
                        DOCUMENT_COMMENT2 =
                                (Token) match(input, DOCUMENT_COMMENT,
                                        FOLLOW_DOCUMENT_COMMENT_in_document66);
                        DOCUMENT_COMMENT2_tree =
                                 adaptor.create(DOCUMENT_COMMENT2);
                        adaptor.addChild(root_0, DOCUMENT_COMMENT2_tree);

                    }
                        break;
                    case 3:
                    // BELScript.g:45:35: record
                    {
                        pushFollow(FOLLOW_record_in_document70);
                        record3 = record();

                        state._fsp--;

                        adaptor.addChild(root_0, record3.getTree());

                    }
                        break;

                    default:
                        if (cnt1 >= 1) break loop1;
                        EarlyExitException eee =
                                new EarlyExitException(1, input);
                        throw eee;
                    }
                    cnt1++;
                } while (true);

                EOF4 = (Token) match(input, EOF, FOLLOW_EOF_in_document74);
                EOF4_tree =
                         adaptor.create(EOF4);
                adaptor.addChild(root_0, EOF4_tree);

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "document"

    public static class record_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "record"
    // BELScript.g:48:1: record : ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement ) ;
    public final BELScriptParser.record_return record()
            throws RecognitionException {
        BELScriptParser.record_return retval =
                new BELScriptParser.record_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        BELScriptParser.define_namespace_return define_namespace5 = null;

        BELScriptParser.define_annotation_return define_annotation6 = null;

        BELScriptParser.set_annotation_return set_annotation7 = null;

        BELScriptParser.set_document_return set_document8 = null;

        BELScriptParser.set_statement_group_return set_statement_group9 = null;

        BELScriptParser.unset_statement_group_return unset_statement_group10 =
                null;

        BELScriptParser.unset_return unset11 = null;

        BELScriptParser.statement_return statement12 = null;

        try {
            // BELScript.g:48:7: ( ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement ) )
            // BELScript.g:49:5: ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement )
            {
                root_0 =  adaptor.nil();

                // BELScript.g:49:5: ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement )
                int alt2 = 8;
                switch (input.LA(1)) {
                case 39: {
                    int LA2_1 = input.LA(2);

                    if ((LA2_1 == 33)) {
                        alt2 = 2;
                    }
                    else if ((LA2_1 == 38 || LA2_1 == 44)) {
                        alt2 = 1;
                    }
                    else {
                        NoViableAltException nvae =
                                new NoViableAltException("", 2, 1, input);

                        throw nvae;

                    }
                }
                    break;
                case 47: {
                    switch (input.LA(2)) {
                    case OBJECT_IDENT: {
                        alt2 = 3;
                    }
                        break;
                    case DOCUMENT_KEYWORD: {
                        alt2 = 4;
                    }
                        break;
                    case STATEMENT_GROUP_KEYWORD: {
                        alt2 = 5;
                    }
                        break;
                    default:
                        NoViableAltException nvae =
                                new NoViableAltException("", 2, 2, input);

                        throw nvae;

                    }

                }
                    break;
                case 48: {
                    int LA2_3 = input.LA(2);

                    if ((LA2_3 == STATEMENT_GROUP_KEYWORD)) {
                        alt2 = 6;
                    }
                    else if ((LA2_3 == IDENT_LIST || LA2_3 == OBJECT_IDENT)) {
                        alt2 = 7;
                    }
                    else {
                        NoViableAltException nvae =
                                new NoViableAltException("", 2, 3, input);

                        throw nvae;

                    }
                }
                    break;
                case 51:
                case 52:
                case 53:
                case 56:
                case 58:
                case 59:
                case 60:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 71:
                case 72:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                case 92:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 104:
                case 106:
                case 107:
                case 108:
                case 110:
                case 111:
                case 112:
                case 113:
                case 114:
                case 115:
                case 116:
                case 117:
                case 119:
                case 120:
                case 121:
                case 122:
                case 124:
                case 126:
                case 127:
                case 128:
                case 129:
                case 130: {
                    alt2 = 8;
                }
                    break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 2, 0, input);

                    throw nvae;

                }

                switch (alt2) {
                case 1:
                // BELScript.g:49:6: define_namespace
                {
                    pushFollow(FOLLOW_define_namespace_in_record91);
                    define_namespace5 = define_namespace();

                    state._fsp--;

                    adaptor.addChild(root_0, define_namespace5.getTree());

                }
                    break;
                case 2:
                // BELScript.g:49:25: define_annotation
                {
                    pushFollow(FOLLOW_define_annotation_in_record95);
                    define_annotation6 = define_annotation();

                    state._fsp--;

                    adaptor.addChild(root_0, define_annotation6.getTree());

                }
                    break;
                case 3:
                // BELScript.g:49:45: set_annotation
                {
                    pushFollow(FOLLOW_set_annotation_in_record99);
                    set_annotation7 = set_annotation();

                    state._fsp--;

                    adaptor.addChild(root_0, set_annotation7.getTree());

                }
                    break;
                case 4:
                // BELScript.g:49:62: set_document
                {
                    pushFollow(FOLLOW_set_document_in_record103);
                    set_document8 = set_document();

                    state._fsp--;

                    adaptor.addChild(root_0, set_document8.getTree());

                }
                    break;
                case 5:
                // BELScript.g:49:77: set_statement_group
                {
                    pushFollow(FOLLOW_set_statement_group_in_record107);
                    set_statement_group9 = set_statement_group();

                    state._fsp--;

                    adaptor.addChild(root_0, set_statement_group9.getTree());

                }
                    break;
                case 6:
                // BELScript.g:49:99: unset_statement_group
                {
                    pushFollow(FOLLOW_unset_statement_group_in_record111);
                    unset_statement_group10 = unset_statement_group();

                    state._fsp--;

                    adaptor.addChild(root_0, unset_statement_group10.getTree());

                }
                    break;
                case 7:
                // BELScript.g:49:123: unset
                {
                    pushFollow(FOLLOW_unset_in_record115);
                    unset11 = unset();

                    state._fsp--;

                    adaptor.addChild(root_0, unset11.getTree());

                }
                    break;
                case 8:
                // BELScript.g:49:131: statement
                {
                    pushFollow(FOLLOW_statement_in_record119);
                    statement12 = statement();

                    state._fsp--;

                    adaptor.addChild(root_0, statement12.getTree());

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "record"

    public static class set_document_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "set_document"
    // BELScript.g:52:1: set_document : ( 'SET' DOCUMENT_KEYWORD ) document_property '=' ( OBJECT_IDENT |vl= VALUE_LIST | quoted_value ) ;
    public final BELScriptParser.set_document_return set_document()
            throws RecognitionException {
        BELScriptParser.set_document_return retval =
                new BELScriptParser.set_document_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token vl = null;
        Token string_literal13 = null;
        Token DOCUMENT_KEYWORD14 = null;
        Token char_literal16 = null;
        Token OBJECT_IDENT17 = null;
        BELScriptParser.document_property_return document_property15 = null;

        BELScriptParser.quoted_value_return quoted_value18 = null;

        Object vl_tree = null;
        Object string_literal13_tree = null;
        Object DOCUMENT_KEYWORD14_tree = null;
        Object char_literal16_tree = null;
        Object OBJECT_IDENT17_tree = null;

        paraphrases.push("in set document.");
        try {
            // BELScript.g:55:5: ( ( 'SET' DOCUMENT_KEYWORD ) document_property '=' ( OBJECT_IDENT |vl= VALUE_LIST | quoted_value ) )
            // BELScript.g:56:5: ( 'SET' DOCUMENT_KEYWORD ) document_property '=' ( OBJECT_IDENT |vl= VALUE_LIST | quoted_value )
            {
                root_0 =  adaptor.nil();

                // BELScript.g:56:5: ( 'SET' DOCUMENT_KEYWORD )
                // BELScript.g:56:6: 'SET' DOCUMENT_KEYWORD
                {
                    string_literal13 =
                            (Token) match(input, 47,
                                    FOLLOW_47_in_set_document160);
                    string_literal13_tree =
                             adaptor.create(string_literal13);
                    adaptor.addChild(root_0, string_literal13_tree);

                    DOCUMENT_KEYWORD14 =
                            (Token) match(input, DOCUMENT_KEYWORD,
                                    FOLLOW_DOCUMENT_KEYWORD_in_set_document162);
                    DOCUMENT_KEYWORD14_tree =
                             adaptor.create(DOCUMENT_KEYWORD14);
                    adaptor.addChild(root_0, DOCUMENT_KEYWORD14_tree);

                }

                pushFollow(FOLLOW_document_property_in_set_document165);
                document_property15 = document_property();

                state._fsp--;

                adaptor.addChild(root_0, document_property15.getTree());

                char_literal16 =
                        (Token) match(input, 29, FOLLOW_29_in_set_document167);
                char_literal16_tree =
                         adaptor.create(char_literal16);
                adaptor.addChild(root_0, char_literal16_tree);

                // BELScript.g:56:52: ( OBJECT_IDENT |vl= VALUE_LIST | quoted_value )
                int alt3 = 3;
                switch (input.LA(1)) {
                case OBJECT_IDENT: {
                    alt3 = 1;
                }
                    break;
                case VALUE_LIST: {
                    alt3 = 2;
                }
                    break;
                case QUOTED_VALUE: {
                    alt3 = 3;
                }
                    break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 3, 0, input);

                    throw nvae;

                }

                switch (alt3) {
                case 1:
                // BELScript.g:56:53: OBJECT_IDENT
                {
                    OBJECT_IDENT17 =
                            (Token) match(input, OBJECT_IDENT,
                                    FOLLOW_OBJECT_IDENT_in_set_document170);
                    OBJECT_IDENT17_tree =
                             adaptor.create(OBJECT_IDENT17);
                    adaptor.addChild(root_0, OBJECT_IDENT17_tree);

                }
                    break;
                case 2:
                // BELScript.g:56:68: vl= VALUE_LIST
                {
                    vl =
                            (Token) match(input, VALUE_LIST,
                                    FOLLOW_VALUE_LIST_in_set_document176);
                    vl_tree =
                             adaptor.create(vl);
                    adaptor.addChild(root_0, vl_tree);

                }
                    break;
                case 3:
                // BELScript.g:56:84: quoted_value
                {
                    pushFollow(FOLLOW_quoted_value_in_set_document180);
                    quoted_value18 = quoted_value();

                    state._fsp--;

                    adaptor.addChild(root_0, quoted_value18.getTree());

                }
                    break;

                }

                // https://github.com/OpenBEL/openbel-framework/issues/14
                if (vl != null) vl.setText(vl.getText().replace("\\\\", "\\"));

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

            paraphrases.pop();
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "set_document"

    public static class set_statement_group_return extends
            ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "set_statement_group"
    // BELScript.g:63:1: set_statement_group : 'SET' STATEMENT_GROUP_KEYWORD '=' ( quoted_value | OBJECT_IDENT ) ;
    public final BELScriptParser.set_statement_group_return
            set_statement_group() throws RecognitionException {
        BELScriptParser.set_statement_group_return retval =
                new BELScriptParser.set_statement_group_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal19 = null;
        Token STATEMENT_GROUP_KEYWORD20 = null;
        Token char_literal21 = null;
        Token OBJECT_IDENT23 = null;
        BELScriptParser.quoted_value_return quoted_value22 = null;

        Object string_literal19_tree = null;
        Object STATEMENT_GROUP_KEYWORD20_tree = null;
        Object char_literal21_tree = null;
        Object OBJECT_IDENT23_tree = null;

        paraphrases.push("in set statement group.");
        try {
            // BELScript.g:66:5: ( 'SET' STATEMENT_GROUP_KEYWORD '=' ( quoted_value | OBJECT_IDENT ) )
            // BELScript.g:67:5: 'SET' STATEMENT_GROUP_KEYWORD '=' ( quoted_value | OBJECT_IDENT )
            {
                root_0 =  adaptor.nil();

                string_literal19 =
                        (Token) match(input, 47,
                                FOLLOW_47_in_set_statement_group226);
                string_literal19_tree =
                         adaptor.create(string_literal19);
                adaptor.addChild(root_0, string_literal19_tree);

                STATEMENT_GROUP_KEYWORD20 =
                        (Token) match(input, STATEMENT_GROUP_KEYWORD,
                                FOLLOW_STATEMENT_GROUP_KEYWORD_in_set_statement_group228);
                STATEMENT_GROUP_KEYWORD20_tree =
                         adaptor.create(STATEMENT_GROUP_KEYWORD20);
                adaptor.addChild(root_0, STATEMENT_GROUP_KEYWORD20_tree);

                char_literal21 =
                        (Token) match(input, 29,
                                FOLLOW_29_in_set_statement_group230);
                char_literal21_tree =
                         adaptor.create(char_literal21);
                adaptor.addChild(root_0, char_literal21_tree);

                // BELScript.g:67:39: ( quoted_value | OBJECT_IDENT )
                int alt4 = 2;
                int LA4_0 = input.LA(1);

                if ((LA4_0 == QUOTED_VALUE)) {
                    alt4 = 1;
                }
                else if ((LA4_0 == OBJECT_IDENT)) {
                    alt4 = 2;
                }
                else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 4, 0, input);

                    throw nvae;

                }
                switch (alt4) {
                case 1:
                // BELScript.g:67:40: quoted_value
                {
                    pushFollow(FOLLOW_quoted_value_in_set_statement_group233);
                    quoted_value22 = quoted_value();

                    state._fsp--;

                    adaptor.addChild(root_0, quoted_value22.getTree());

                }
                    break;
                case 2:
                // BELScript.g:67:55: OBJECT_IDENT
                {
                    OBJECT_IDENT23 =
                            (Token) match(input, OBJECT_IDENT,
                                    FOLLOW_OBJECT_IDENT_in_set_statement_group237);
                    OBJECT_IDENT23_tree =
                             adaptor.create(OBJECT_IDENT23);
                    adaptor.addChild(root_0, OBJECT_IDENT23_tree);

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

            paraphrases.pop();
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "set_statement_group"

    public static class set_annotation_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "set_annotation"
    // BELScript.g:70:1: set_annotation : 'SET' OBJECT_IDENT '=' ( quoted_value |vl= VALUE_LIST | OBJECT_IDENT ) ;
    public final BELScriptParser.set_annotation_return set_annotation()
            throws RecognitionException {
        BELScriptParser.set_annotation_return retval =
                new BELScriptParser.set_annotation_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token vl = null;
        Token string_literal24 = null;
        Token OBJECT_IDENT25 = null;
        Token char_literal26 = null;
        Token OBJECT_IDENT28 = null;
        BELScriptParser.quoted_value_return quoted_value27 = null;

        Object vl_tree = null;
        Object string_literal24_tree = null;
        Object OBJECT_IDENT25_tree = null;
        Object char_literal26_tree = null;
        Object OBJECT_IDENT28_tree = null;

        paraphrases.push("in set annotation.");
        try {
            // BELScript.g:73:5: ( 'SET' OBJECT_IDENT '=' ( quoted_value |vl= VALUE_LIST | OBJECT_IDENT ) )
            // BELScript.g:74:5: 'SET' OBJECT_IDENT '=' ( quoted_value |vl= VALUE_LIST | OBJECT_IDENT )
            {
                root_0 =  adaptor.nil();

                string_literal24 =
                        (Token) match(input, 47, FOLLOW_47_in_set_annotation277);
                string_literal24_tree =
                         adaptor.create(string_literal24);
                adaptor.addChild(root_0, string_literal24_tree);

                OBJECT_IDENT25 =
                        (Token) match(input, OBJECT_IDENT,
                                FOLLOW_OBJECT_IDENT_in_set_annotation279);
                OBJECT_IDENT25_tree =
                         adaptor.create(OBJECT_IDENT25);
                adaptor.addChild(root_0, OBJECT_IDENT25_tree);

                char_literal26 =
                        (Token) match(input, 29, FOLLOW_29_in_set_annotation281);
                char_literal26_tree =
                         adaptor.create(char_literal26);
                adaptor.addChild(root_0, char_literal26_tree);

                // BELScript.g:74:28: ( quoted_value |vl= VALUE_LIST | OBJECT_IDENT )
                int alt5 = 3;
                switch (input.LA(1)) {
                case QUOTED_VALUE: {
                    alt5 = 1;
                }
                    break;
                case VALUE_LIST: {
                    alt5 = 2;
                }
                    break;
                case OBJECT_IDENT: {
                    alt5 = 3;
                }
                    break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 5, 0, input);

                    throw nvae;

                }

                switch (alt5) {
                case 1:
                // BELScript.g:74:29: quoted_value
                {
                    pushFollow(FOLLOW_quoted_value_in_set_annotation284);
                    quoted_value27 = quoted_value();

                    state._fsp--;

                    adaptor.addChild(root_0, quoted_value27.getTree());

                }
                    break;
                case 2:
                // BELScript.g:74:44: vl= VALUE_LIST
                {
                    vl =
                            (Token) match(input, VALUE_LIST,
                                    FOLLOW_VALUE_LIST_in_set_annotation290);
                    vl_tree =
                             adaptor.create(vl);
                    adaptor.addChild(root_0, vl_tree);

                }
                    break;
                case 3:
                // BELScript.g:74:60: OBJECT_IDENT
                {
                    OBJECT_IDENT28 =
                            (Token) match(input, OBJECT_IDENT,
                                    FOLLOW_OBJECT_IDENT_in_set_annotation294);
                    OBJECT_IDENT28_tree =
                             adaptor.create(OBJECT_IDENT28);
                    adaptor.addChild(root_0, OBJECT_IDENT28_tree);

                }
                    break;

                }

                // https://github.com/OpenBEL/openbel-framework/issues/14
                if (vl != null) vl.setText(vl.getText().replace("\\\\", "\\"));

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

            paraphrases.pop();
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "set_annotation"

    public static class unset_statement_group_return extends
            ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "unset_statement_group"
    // BELScript.g:81:1: unset_statement_group : 'UNSET' STATEMENT_GROUP_KEYWORD ;
    public final BELScriptParser.unset_statement_group_return
            unset_statement_group() throws RecognitionException {
        BELScriptParser.unset_statement_group_return retval =
                new BELScriptParser.unset_statement_group_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal29 = null;
        Token STATEMENT_GROUP_KEYWORD30 = null;

        Object string_literal29_tree = null;
        Object STATEMENT_GROUP_KEYWORD30_tree = null;

        paraphrases.push("in unset statement group.");
        try {
            // BELScript.g:84:5: ( 'UNSET' STATEMENT_GROUP_KEYWORD )
            // BELScript.g:85:5: 'UNSET' STATEMENT_GROUP_KEYWORD
            {
                root_0 =  adaptor.nil();

                string_literal29 =
                        (Token) match(input, 48,
                                FOLLOW_48_in_unset_statement_group340);
                string_literal29_tree =
                         adaptor.create(string_literal29);
                adaptor.addChild(root_0, string_literal29_tree);

                STATEMENT_GROUP_KEYWORD30 =
                        (Token) match(input, STATEMENT_GROUP_KEYWORD,
                                FOLLOW_STATEMENT_GROUP_KEYWORD_in_unset_statement_group342);
                STATEMENT_GROUP_KEYWORD30_tree =
                         adaptor.create(STATEMENT_GROUP_KEYWORD30);
                adaptor.addChild(root_0, STATEMENT_GROUP_KEYWORD30_tree);

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

            paraphrases.pop();
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "unset_statement_group"

    public static class unset_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "unset"
    // BELScript.g:88:1: unset : 'UNSET' ( OBJECT_IDENT | IDENT_LIST ) ;
    public final BELScriptParser.unset_return unset()
            throws RecognitionException {
        BELScriptParser.unset_return retval =
                new BELScriptParser.unset_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal31 = null;
        Token set32 = null;

        Object string_literal31_tree = null;
        Object set32_tree = null;

        paraphrases.push("in unset.");
        try {
            // BELScript.g:91:5: ( 'UNSET' ( OBJECT_IDENT | IDENT_LIST ) )
            // BELScript.g:92:5: 'UNSET' ( OBJECT_IDENT | IDENT_LIST )
            {
                root_0 =  adaptor.nil();

                string_literal31 =
                        (Token) match(input, 48, FOLLOW_48_in_unset381);
                string_literal31_tree =
                         adaptor.create(string_literal31);
                adaptor.addChild(root_0, string_literal31_tree);

                set32 = input.LT(1);

                if (input.LA(1) == IDENT_LIST || input.LA(1) == OBJECT_IDENT) {
                    input.consume();
                    adaptor.addChild(root_0,
                             adaptor.create(set32)
                            );
                    state.errorRecovery = false;
                }
                else {
                    MismatchedSetException mse =
                            new MismatchedSetException(null, input);
                    throw mse;
                }

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

            paraphrases.pop();
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "unset"

    public static class define_namespace_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "define_namespace"
    // BELScript.g:95:1: define_namespace : ( 'DEFINE' ( ( 'DEFAULT' )? 'NAMESPACE' ) ) OBJECT_IDENT 'AS' 'URL' quoted_value ;
    public final BELScriptParser.define_namespace_return define_namespace()
            throws RecognitionException {
        BELScriptParser.define_namespace_return retval =
                new BELScriptParser.define_namespace_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal33 = null;
        Token string_literal34 = null;
        Token string_literal35 = null;
        Token OBJECT_IDENT36 = null;
        Token string_literal37 = null;
        Token string_literal38 = null;
        BELScriptParser.quoted_value_return quoted_value39 = null;

        Object string_literal33_tree = null;
        Object string_literal34_tree = null;
        Object string_literal35_tree = null;
        Object OBJECT_IDENT36_tree = null;
        Object string_literal37_tree = null;
        Object string_literal38_tree = null;

        paraphrases.push("in define namespace.");
        try {
            // BELScript.g:98:5: ( ( 'DEFINE' ( ( 'DEFAULT' )? 'NAMESPACE' ) ) OBJECT_IDENT 'AS' 'URL' quoted_value )
            // BELScript.g:99:5: ( 'DEFINE' ( ( 'DEFAULT' )? 'NAMESPACE' ) ) OBJECT_IDENT 'AS' 'URL' quoted_value
            {
                root_0 =  adaptor.nil();

                // BELScript.g:99:5: ( 'DEFINE' ( ( 'DEFAULT' )? 'NAMESPACE' ) )
                // BELScript.g:99:6: 'DEFINE' ( ( 'DEFAULT' )? 'NAMESPACE' )
                {
                    string_literal33 =
                            (Token) match(input, 39,
                                    FOLLOW_39_in_define_namespace429);
                    string_literal33_tree =
                             adaptor.create(string_literal33);
                    adaptor.addChild(root_0, string_literal33_tree);

                    // BELScript.g:99:15: ( ( 'DEFAULT' )? 'NAMESPACE' )
                    // BELScript.g:99:16: ( 'DEFAULT' )? 'NAMESPACE'
                    {
                        // BELScript.g:99:16: ( 'DEFAULT' )?
                        int alt6 = 2;
                        int LA6_0 = input.LA(1);

                        if ((LA6_0 == 38)) {
                            alt6 = 1;
                        }
                        switch (alt6) {
                        case 1:
                        // BELScript.g:99:17: 'DEFAULT'
                        {
                            string_literal34 =
                                    (Token) match(input, 38,
                                            FOLLOW_38_in_define_namespace433);
                            string_literal34_tree =
                                     adaptor.create(string_literal34);
                            adaptor.addChild(root_0, string_literal34_tree);

                        }
                            break;

                        }

                        string_literal35 =
                                (Token) match(input, 44,
                                        FOLLOW_44_in_define_namespace437);
                        string_literal35_tree =
                                 adaptor.create(string_literal35);
                        adaptor.addChild(root_0, string_literal35_tree);

                    }

                }

                OBJECT_IDENT36 =
                        (Token) match(input, OBJECT_IDENT,
                                FOLLOW_OBJECT_IDENT_in_define_namespace441);
                OBJECT_IDENT36_tree =
                         adaptor.create(OBJECT_IDENT36);
                adaptor.addChild(root_0, OBJECT_IDENT36_tree);

                string_literal37 =
                        (Token) match(input, 34,
                                FOLLOW_34_in_define_namespace443);
                string_literal37_tree =
                         adaptor.create(string_literal37);
                adaptor.addChild(root_0, string_literal37_tree);

                string_literal38 =
                        (Token) match(input, 49,
                                FOLLOW_49_in_define_namespace445);
                string_literal38_tree =
                         adaptor.create(string_literal38);
                adaptor.addChild(root_0, string_literal38_tree);

                pushFollow(FOLLOW_quoted_value_in_define_namespace447);
                quoted_value39 = quoted_value();

                state._fsp--;

                adaptor.addChild(root_0, quoted_value39.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

            paraphrases.pop();
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "define_namespace"

    public static class define_annotation_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "define_annotation"
    // BELScript.g:102:1: define_annotation : ( 'DEFINE' 'ANNOTATION' ) OBJECT_IDENT 'AS' ( ( ( 'URL' | 'PATTERN' ) quoted_value ) | ( 'LIST' vl= VALUE_LIST ) ) ;
    public final BELScriptParser.define_annotation_return define_annotation()
            throws RecognitionException {
        BELScriptParser.define_annotation_return retval =
                new BELScriptParser.define_annotation_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token vl = null;
        Token string_literal40 = null;
        Token string_literal41 = null;
        Token OBJECT_IDENT42 = null;
        Token string_literal43 = null;
        Token set44 = null;
        Token string_literal46 = null;
        BELScriptParser.quoted_value_return quoted_value45 = null;

        Object vl_tree = null;
        Object string_literal40_tree = null;
        Object string_literal41_tree = null;
        Object OBJECT_IDENT42_tree = null;
        Object string_literal43_tree = null;
        Object set44_tree = null;
        Object string_literal46_tree = null;

        paraphrases.push("in define annotation.");
        try {
            // BELScript.g:105:5: ( ( 'DEFINE' 'ANNOTATION' ) OBJECT_IDENT 'AS' ( ( ( 'URL' | 'PATTERN' ) quoted_value ) | ( 'LIST' vl= VALUE_LIST ) ) )
            // BELScript.g:106:5: ( 'DEFINE' 'ANNOTATION' ) OBJECT_IDENT 'AS' ( ( ( 'URL' | 'PATTERN' ) quoted_value ) | ( 'LIST' vl= VALUE_LIST ) )
            {
                root_0 =  adaptor.nil();

                // BELScript.g:106:5: ( 'DEFINE' 'ANNOTATION' )
                // BELScript.g:106:6: 'DEFINE' 'ANNOTATION'
                {
                    string_literal40 =
                            (Token) match(input, 39,
                                    FOLLOW_39_in_define_annotation487);
                    string_literal40_tree =
                             adaptor.create(string_literal40);
                    adaptor.addChild(root_0, string_literal40_tree);

                    string_literal41 =
                            (Token) match(input, 33,
                                    FOLLOW_33_in_define_annotation489);
                    string_literal41_tree =
                             adaptor.create(string_literal41);
                    adaptor.addChild(root_0, string_literal41_tree);

                }

                OBJECT_IDENT42 =
                        (Token) match(input, OBJECT_IDENT,
                                FOLLOW_OBJECT_IDENT_in_define_annotation492);
                OBJECT_IDENT42_tree =
                         adaptor.create(OBJECT_IDENT42);
                adaptor.addChild(root_0, OBJECT_IDENT42_tree);

                string_literal43 =
                        (Token) match(input, 34,
                                FOLLOW_34_in_define_annotation494);
                string_literal43_tree =
                         adaptor.create(string_literal43);
                adaptor.addChild(root_0, string_literal43_tree);

                // BELScript.g:106:47: ( ( ( 'URL' | 'PATTERN' ) quoted_value ) | ( 'LIST' vl= VALUE_LIST ) )
                int alt7 = 2;
                int LA7_0 = input.LA(1);

                if ((LA7_0 == 46 || LA7_0 == 49)) {
                    alt7 = 1;
                }
                else if ((LA7_0 == 42)) {
                    alt7 = 2;
                }
                else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 7, 0, input);

                    throw nvae;

                }
                switch (alt7) {
                case 1:
                // BELScript.g:106:48: ( ( 'URL' | 'PATTERN' ) quoted_value )
                {
                    // BELScript.g:106:48: ( ( 'URL' | 'PATTERN' ) quoted_value )
                    // BELScript.g:106:49: ( 'URL' | 'PATTERN' ) quoted_value
                    {
                        set44 = input.LT(1);

                        if (input.LA(1) == 46 || input.LA(1) == 49) {
                            input.consume();
                            adaptor.addChild(root_0,
                                     adaptor.create(set44)
                                    );
                            state.errorRecovery = false;
                        }
                        else {
                            MismatchedSetException mse =
                                    new MismatchedSetException(null, input);
                            throw mse;
                        }

                        pushFollow(FOLLOW_quoted_value_in_define_annotation506);
                        quoted_value45 = quoted_value();

                        state._fsp--;

                        adaptor.addChild(root_0, quoted_value45.getTree());

                    }

                }
                    break;
                case 2:
                // BELScript.g:106:85: ( 'LIST' vl= VALUE_LIST )
                {
                    // BELScript.g:106:85: ( 'LIST' vl= VALUE_LIST )
                    // BELScript.g:106:86: 'LIST' vl= VALUE_LIST
                    {
                        string_literal46 =
                                (Token) match(input, 42,
                                        FOLLOW_42_in_define_annotation512);
                        string_literal46_tree =
                                 adaptor.create(string_literal46);
                        adaptor.addChild(root_0, string_literal46_tree);

                        vl =
                                (Token) match(input, VALUE_LIST,
                                        FOLLOW_VALUE_LIST_in_define_annotation516);
                        vl_tree =
                                 adaptor.create(vl);
                        adaptor.addChild(root_0, vl_tree);

                    }

                }
                    break;

                }

                // https://github.com/OpenBEL/openbel-framework/issues/14
                if (vl != null) vl.setText(vl.getText().replace("\\\\", "\\"));

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

            paraphrases.pop();
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "define_annotation"

    public static class quoted_value_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "quoted_value"
    // BELScript.g:113:1: quoted_value : qv= QUOTED_VALUE ;
    public final BELScriptParser.quoted_value_return quoted_value()
            throws RecognitionException {
        BELScriptParser.quoted_value_return retval =
                new BELScriptParser.quoted_value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token qv = null;

        Object qv_tree = null;

        try {
            // BELScript.g:114:5: (qv= QUOTED_VALUE )
            // BELScript.g:114:7: qv= QUOTED_VALUE
            {
                root_0 =  adaptor.nil();

                qv =
                        (Token) match(input, QUOTED_VALUE,
                                FOLLOW_QUOTED_VALUE_in_quoted_value543);
                qv_tree =
                         adaptor.create(qv);
                adaptor.addChild(root_0, qv_tree);

                // https://github.com/OpenBEL/openbel-framework/issues/14
                qv.setText(qv.getText().replace("\\\\", "\\"));

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "quoted_value"

    public static class document_property_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "document_property"
    // BELScript.g:121:1: document_property : ( 'Authors' | 'ContactInfo' | 'Copyright' | 'Description' | 'Disclaimer' | 'Licenses' | 'Name' | 'Version' );
    public final BELScriptParser.document_property_return document_property()
            throws RecognitionException {
        BELScriptParser.document_property_return retval =
                new BELScriptParser.document_property_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set47 = null;

        Object set47_tree = null;

        try {
            // BELScript.g:121:18: ( 'Authors' | 'ContactInfo' | 'Copyright' | 'Description' | 'Disclaimer' | 'Licenses' | 'Name' | 'Version' )
            // BELScript.g:
            {
                root_0 =  adaptor.nil();

                set47 = input.LT(1);

                if ((input.LA(1) >= 35 && input.LA(1) <= 37)
                        || (input.LA(1) >= 40 && input.LA(1) <= 41)
                        || input.LA(1) == 43 || input.LA(1) == 45
                        || input.LA(1) == 50) {
                    input.consume();
                    adaptor.addChild(root_0,
                             adaptor.create(set47)
                            );
                    state.errorRecovery = false;
                }
                else {
                    MismatchedSetException mse =
                            new MismatchedSetException(null, input);
                    throw mse;
                }

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "document_property"

    public static class statement_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "statement"
    // BELScript.g:132:1: statement : outer_term ( relationship ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term ) )? ( STATEMENT_COMMENT )? ;
    public final BELScriptParser.statement_return statement()
            throws RecognitionException {
        BELScriptParser.statement_return retval =
                new BELScriptParser.statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OPEN_PAREN50 = null;
        Token CLOSE_PAREN54 = null;
        Token STATEMENT_COMMENT56 = null;
        BELScriptParser.outer_term_return outer_term48 = null;

        BELScriptParser.relationship_return relationship49 = null;

        BELScriptParser.outer_term_return outer_term51 = null;

        BELScriptParser.relationship_return relationship52 = null;

        BELScriptParser.outer_term_return outer_term53 = null;

        BELScriptParser.outer_term_return outer_term55 = null;

        Object OPEN_PAREN50_tree = null;
        Object CLOSE_PAREN54_tree = null;
        Object STATEMENT_COMMENT56_tree = null;

        paraphrases.push("in statement.");
        try {
            // BELScript.g:135:5: ( outer_term ( relationship ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term ) )? ( STATEMENT_COMMENT )? )
            // BELScript.g:136:5: outer_term ( relationship ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term ) )? ( STATEMENT_COMMENT )?
            {
                root_0 =  adaptor.nil();

                pushFollow(FOLLOW_outer_term_in_statement677);
                outer_term48 = outer_term();

                state._fsp--;

                adaptor.addChild(root_0, outer_term48.getTree());

                // BELScript.g:136:16: ( relationship ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term ) )?
                int alt9 = 2;
                int LA9_0 = input.LA(1);

                if (((LA9_0 >= 25 && LA9_0 <= 28)
                        || (LA9_0 >= 30 && LA9_0 <= 32)
                        || (LA9_0 >= 54 && LA9_0 <= 55) || LA9_0 == 57
                        || LA9_0 == 61 || LA9_0 == 70
                        || (LA9_0 >= 73 && LA9_0 <= 74)
                        || (LA9_0 >= 81 && LA9_0 <= 86)
                        || (LA9_0 >= 93 && LA9_0 <= 94) || LA9_0 == 103
                        || LA9_0 == 105 || LA9_0 == 109 || LA9_0 == 118
                        || LA9_0 == 123 || LA9_0 == 125)) {
                    alt9 = 1;
                }
                switch (alt9) {
                case 1:
                // BELScript.g:136:17: relationship ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term )
                {
                    pushFollow(FOLLOW_relationship_in_statement680);
                    relationship49 = relationship();

                    state._fsp--;

                    adaptor.addChild(root_0, relationship49.getTree());

                    // BELScript.g:136:30: ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term )
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);

                    if ((LA8_0 == OPEN_PAREN)) {
                        alt8 = 1;
                    }
                    else if (((LA8_0 >= 51 && LA8_0 <= 53) || LA8_0 == 56
                            || (LA8_0 >= 58 && LA8_0 <= 60)
                            || (LA8_0 >= 62 && LA8_0 <= 69)
                            || (LA8_0 >= 71 && LA8_0 <= 72)
                            || (LA8_0 >= 75 && LA8_0 <= 80)
                            || (LA8_0 >= 87 && LA8_0 <= 92)
                            || (LA8_0 >= 95 && LA8_0 <= 102) || LA8_0 == 104
                            || (LA8_0 >= 106 && LA8_0 <= 108)
                            || (LA8_0 >= 110 && LA8_0 <= 117)
                            || (LA8_0 >= 119 && LA8_0 <= 122) || LA8_0 == 124 || (LA8_0 >= 126 && LA8_0 <= 130))) {
                        alt8 = 2;
                    }
                    else {
                        NoViableAltException nvae =
                                new NoViableAltException("", 8, 0, input);

                        throw nvae;

                    }
                    switch (alt8) {
                    case 1:
                    // BELScript.g:136:31: ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN )
                    {
                        // BELScript.g:136:31: ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN )
                        // BELScript.g:136:32: OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN
                        {
                            OPEN_PAREN50 =
                                    (Token) match(input, OPEN_PAREN,
                                            FOLLOW_OPEN_PAREN_in_statement684);
                            OPEN_PAREN50_tree =
                                     adaptor.create(OPEN_PAREN50);
                            adaptor.addChild(root_0, OPEN_PAREN50_tree);

                            pushFollow(FOLLOW_outer_term_in_statement686);
                            outer_term51 = outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, outer_term51.getTree());

                            pushFollow(FOLLOW_relationship_in_statement688);
                            relationship52 = relationship();

                            state._fsp--;

                            adaptor.addChild(root_0, relationship52.getTree());

                            pushFollow(FOLLOW_outer_term_in_statement690);
                            outer_term53 = outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, outer_term53.getTree());

                            CLOSE_PAREN54 =
                                    (Token) match(input, CLOSE_PAREN,
                                            FOLLOW_CLOSE_PAREN_in_statement692);
                            CLOSE_PAREN54_tree =
                                     adaptor.create(CLOSE_PAREN54);
                            adaptor.addChild(root_0, CLOSE_PAREN54_tree);

                        }

                    }
                        break;
                    case 2:
                    // BELScript.g:136:93: outer_term
                    {
                        pushFollow(FOLLOW_outer_term_in_statement697);
                        outer_term55 = outer_term();

                        state._fsp--;

                        adaptor.addChild(root_0, outer_term55.getTree());

                    }
                        break;

                    }

                }
                    break;

                }

                // BELScript.g:136:107: ( STATEMENT_COMMENT )?
                int alt10 = 2;
                int LA10_0 = input.LA(1);

                if ((LA10_0 == STATEMENT_COMMENT)) {
                    alt10 = 1;
                }
                switch (alt10) {
                case 1:
                // BELScript.g:136:107: STATEMENT_COMMENT
                {
                    STATEMENT_COMMENT56 =
                            (Token) match(input, STATEMENT_COMMENT,
                                    FOLLOW_STATEMENT_COMMENT_in_statement702);
                    STATEMENT_COMMENT56_tree =
                             adaptor.create(STATEMENT_COMMENT56);
                    adaptor.addChild(root_0, STATEMENT_COMMENT56_tree);

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

            paraphrases.pop();
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "statement"

    public static class outer_term_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "outer_term"
    // BELScript.g:139:1: outer_term : function OPEN_PAREN ( ( ',' )? argument )* CLOSE_PAREN ;
    public final BELScriptParser.outer_term_return outer_term()
            throws RecognitionException {
        BELScriptParser.outer_term_return retval =
                new BELScriptParser.outer_term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OPEN_PAREN58 = null;
        Token char_literal59 = null;
        Token CLOSE_PAREN61 = null;
        BELScriptParser.function_return function57 = null;

        BELScriptParser.argument_return argument60 = null;

        Object OPEN_PAREN58_tree = null;
        Object char_literal59_tree = null;
        Object CLOSE_PAREN61_tree = null;

        try {
            // BELScript.g:139:11: ( function OPEN_PAREN ( ( ',' )? argument )* CLOSE_PAREN )
            // BELScript.g:140:5: function OPEN_PAREN ( ( ',' )? argument )* CLOSE_PAREN
            {
                root_0 =  adaptor.nil();

                pushFollow(FOLLOW_function_in_outer_term723);
                function57 = function();

                state._fsp--;

                adaptor.addChild(root_0, function57.getTree());

                OPEN_PAREN58 =
                        (Token) match(input, OPEN_PAREN,
                                FOLLOW_OPEN_PAREN_in_outer_term725);
                OPEN_PAREN58_tree =
                         adaptor.create(OPEN_PAREN58);
                adaptor.addChild(root_0, OPEN_PAREN58_tree);

                // BELScript.g:140:25: ( ( ',' )? argument )*
                loop12: do {
                    int alt12 = 2;
                    int LA12_0 = input.LA(1);

                    if (((LA12_0 >= NS_PREFIX && LA12_0 <= OBJECT_IDENT)
                            || LA12_0 == QUOTED_VALUE || LA12_0 == 24
                            || (LA12_0 >= 51 && LA12_0 <= 53) || LA12_0 == 56
                            || (LA12_0 >= 58 && LA12_0 <= 60)
                            || (LA12_0 >= 62 && LA12_0 <= 69)
                            || (LA12_0 >= 71 && LA12_0 <= 72)
                            || (LA12_0 >= 75 && LA12_0 <= 80)
                            || (LA12_0 >= 87 && LA12_0 <= 92)
                            || (LA12_0 >= 95 && LA12_0 <= 102) || LA12_0 == 104
                            || (LA12_0 >= 106 && LA12_0 <= 108)
                            || (LA12_0 >= 110 && LA12_0 <= 117)
                            || (LA12_0 >= 119 && LA12_0 <= 122)
                            || LA12_0 == 124 || (LA12_0 >= 126 && LA12_0 <= 130))) {
                        alt12 = 1;
                    }

                    switch (alt12) {
                    case 1:
                    // BELScript.g:140:26: ( ',' )? argument
                    {
                        // BELScript.g:140:26: ( ',' )?
                        int alt11 = 2;
                        int LA11_0 = input.LA(1);

                        if ((LA11_0 == 24)) {
                            alt11 = 1;
                        }
                        switch (alt11) {
                        case 1:
                        // BELScript.g:140:26: ','
                        {
                            char_literal59 =
                                    (Token) match(input, 24,
                                            FOLLOW_24_in_outer_term728);
                            char_literal59_tree =
                                     adaptor.create(char_literal59);
                            adaptor.addChild(root_0, char_literal59_tree);

                        }
                            break;

                        }

                        pushFollow(FOLLOW_argument_in_outer_term731);
                        argument60 = argument();

                        state._fsp--;

                        adaptor.addChild(root_0, argument60.getTree());

                    }
                        break;

                    default:
                        break loop12;
                    }
                } while (true);

                CLOSE_PAREN61 =
                        (Token) match(input, CLOSE_PAREN,
                                FOLLOW_CLOSE_PAREN_in_outer_term735);
                CLOSE_PAREN61_tree =
                         adaptor.create(CLOSE_PAREN61);
                adaptor.addChild(root_0, CLOSE_PAREN61_tree);

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "outer_term"

    public static class argument_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "argument"
    // BELScript.g:143:1: argument : ( param | term );
    public final BELScriptParser.argument_return argument()
            throws RecognitionException {
        BELScriptParser.argument_return retval =
                new BELScriptParser.argument_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        BELScriptParser.param_return param62 = null;

        BELScriptParser.term_return term63 = null;

        try {
            // BELScript.g:143:9: ( param | term )
            int alt13 = 2;
            int LA13_0 = input.LA(1);

            if (((LA13_0 >= NS_PREFIX && LA13_0 <= OBJECT_IDENT) || LA13_0 == QUOTED_VALUE)) {
                alt13 = 1;
            }
            else if (((LA13_0 >= 51 && LA13_0 <= 53) || LA13_0 == 56
                    || (LA13_0 >= 58 && LA13_0 <= 60)
                    || (LA13_0 >= 62 && LA13_0 <= 69)
                    || (LA13_0 >= 71 && LA13_0 <= 72)
                    || (LA13_0 >= 75 && LA13_0 <= 80)
                    || (LA13_0 >= 87 && LA13_0 <= 92)
                    || (LA13_0 >= 95 && LA13_0 <= 102) || LA13_0 == 104
                    || (LA13_0 >= 106 && LA13_0 <= 108)
                    || (LA13_0 >= 110 && LA13_0 <= 117)
                    || (LA13_0 >= 119 && LA13_0 <= 122) || LA13_0 == 124 || (LA13_0 >= 126 && LA13_0 <= 130))) {
                alt13 = 2;
            }
            else {
                NoViableAltException nvae =
                        new NoViableAltException("", 13, 0, input);

                throw nvae;

            }
            switch (alt13) {
            case 1:
            // BELScript.g:144:5: param
            {
                root_0 =  adaptor.nil();

                pushFollow(FOLLOW_param_in_argument755);
                param62 = param();

                state._fsp--;

                adaptor.addChild(root_0, param62.getTree());

            }
                break;
            case 2:
            // BELScript.g:144:13: term
            {
                root_0 =  adaptor.nil();

                pushFollow(FOLLOW_term_in_argument759);
                term63 = term();

                state._fsp--;

                adaptor.addChild(root_0, term63.getTree());

            }
                break;

            }
            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "argument"

    public static class term_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "term"
    // BELScript.g:147:1: term : function OPEN_PAREN ( ( ',' )? ( term | param ) )* CLOSE_PAREN ;
    public final BELScriptParser.term_return term() throws RecognitionException {
        BELScriptParser.term_return retval = new BELScriptParser.term_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OPEN_PAREN65 = null;
        Token char_literal66 = null;
        Token CLOSE_PAREN69 = null;
        BELScriptParser.function_return function64 = null;

        BELScriptParser.term_return term67 = null;

        BELScriptParser.param_return param68 = null;

        Object OPEN_PAREN65_tree = null;
        Object char_literal66_tree = null;
        Object CLOSE_PAREN69_tree = null;

        try {
            // BELScript.g:147:5: ( function OPEN_PAREN ( ( ',' )? ( term | param ) )* CLOSE_PAREN )
            // BELScript.g:148:5: function OPEN_PAREN ( ( ',' )? ( term | param ) )* CLOSE_PAREN
            {
                root_0 =  adaptor.nil();

                pushFollow(FOLLOW_function_in_term775);
                function64 = function();

                state._fsp--;

                adaptor.addChild(root_0, function64.getTree());

                OPEN_PAREN65 =
                        (Token) match(input, OPEN_PAREN,
                                FOLLOW_OPEN_PAREN_in_term777);
                OPEN_PAREN65_tree =
                         adaptor.create(OPEN_PAREN65);
                adaptor.addChild(root_0, OPEN_PAREN65_tree);

                // BELScript.g:148:25: ( ( ',' )? ( term | param ) )*
                loop16: do {
                    int alt16 = 2;
                    int LA16_0 = input.LA(1);

                    if (((LA16_0 >= NS_PREFIX && LA16_0 <= OBJECT_IDENT)
                            || LA16_0 == QUOTED_VALUE || LA16_0 == 24
                            || (LA16_0 >= 51 && LA16_0 <= 53) || LA16_0 == 56
                            || (LA16_0 >= 58 && LA16_0 <= 60)
                            || (LA16_0 >= 62 && LA16_0 <= 69)
                            || (LA16_0 >= 71 && LA16_0 <= 72)
                            || (LA16_0 >= 75 && LA16_0 <= 80)
                            || (LA16_0 >= 87 && LA16_0 <= 92)
                            || (LA16_0 >= 95 && LA16_0 <= 102) || LA16_0 == 104
                            || (LA16_0 >= 106 && LA16_0 <= 108)
                            || (LA16_0 >= 110 && LA16_0 <= 117)
                            || (LA16_0 >= 119 && LA16_0 <= 122)
                            || LA16_0 == 124 || (LA16_0 >= 126 && LA16_0 <= 130))) {
                        alt16 = 1;
                    }

                    switch (alt16) {
                    case 1:
                    // BELScript.g:148:26: ( ',' )? ( term | param )
                    {
                        // BELScript.g:148:26: ( ',' )?
                        int alt14 = 2;
                        int LA14_0 = input.LA(1);

                        if ((LA14_0 == 24)) {
                            alt14 = 1;
                        }
                        switch (alt14) {
                        case 1:
                        // BELScript.g:148:26: ','
                        {
                            char_literal66 =
                                    (Token) match(input, 24,
                                            FOLLOW_24_in_term780);
                            char_literal66_tree =
                                     adaptor.create(char_literal66);
                            adaptor.addChild(root_0, char_literal66_tree);

                        }
                            break;

                        }

                        // BELScript.g:148:31: ( term | param )
                        int alt15 = 2;
                        int LA15_0 = input.LA(1);

                        if (((LA15_0 >= 51 && LA15_0 <= 53) || LA15_0 == 56
                                || (LA15_0 >= 58 && LA15_0 <= 60)
                                || (LA15_0 >= 62 && LA15_0 <= 69)
                                || (LA15_0 >= 71 && LA15_0 <= 72)
                                || (LA15_0 >= 75 && LA15_0 <= 80)
                                || (LA15_0 >= 87 && LA15_0 <= 92)
                                || (LA15_0 >= 95 && LA15_0 <= 102)
                                || LA15_0 == 104
                                || (LA15_0 >= 106 && LA15_0 <= 108)
                                || (LA15_0 >= 110 && LA15_0 <= 117)
                                || (LA15_0 >= 119 && LA15_0 <= 122)
                                || LA15_0 == 124 || (LA15_0 >= 126 && LA15_0 <= 130))) {
                            alt15 = 1;
                        }
                        else if (((LA15_0 >= NS_PREFIX && LA15_0 <= OBJECT_IDENT) || LA15_0 == QUOTED_VALUE)) {
                            alt15 = 2;
                        }
                        else {
                            NoViableAltException nvae =
                                    new NoViableAltException("", 15, 0, input);

                            throw nvae;

                        }
                        switch (alt15) {
                        case 1:
                        // BELScript.g:148:32: term
                        {
                            pushFollow(FOLLOW_term_in_term784);
                            term67 = term();

                            state._fsp--;

                            adaptor.addChild(root_0, term67.getTree());

                        }
                            break;
                        case 2:
                        // BELScript.g:148:39: param
                        {
                            pushFollow(FOLLOW_param_in_term788);
                            param68 = param();

                            state._fsp--;

                            adaptor.addChild(root_0, param68.getTree());

                        }
                            break;

                        }

                    }
                        break;

                    default:
                        break loop16;
                    }
                } while (true);

                CLOSE_PAREN69 =
                        (Token) match(input, CLOSE_PAREN,
                                FOLLOW_CLOSE_PAREN_in_term793);
                CLOSE_PAREN69_tree =
                         adaptor.create(CLOSE_PAREN69);
                adaptor.addChild(root_0, CLOSE_PAREN69_tree);

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "term"

    public static class param_return extends ParserRuleReturnScope {
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "param"
    // BELScript.g:152:10: fragment param : ( NS_PREFIX )? ( OBJECT_IDENT | quoted_value ) ;
    public final BELScriptParser.param_return param()
            throws RecognitionException {
        BELScriptParser.param_return retval =
                new BELScriptParser.param_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NS_PREFIX70 = null;
        Token OBJECT_IDENT71 = null;
        BELScriptParser.quoted_value_return quoted_value72 = null;

        Object NS_PREFIX70_tree = null;
        Object OBJECT_IDENT71_tree = null;

        try {
            // BELScript.g:152:15: ( ( NS_PREFIX )? ( OBJECT_IDENT | quoted_value ) )
            // BELScript.g:153:5: ( NS_PREFIX )? ( OBJECT_IDENT | quoted_value )
            {
                root_0 =  adaptor.nil();

                // BELScript.g:153:5: ( NS_PREFIX )?
                int alt17 = 2;
                int LA17_0 = input.LA(1);

                if ((LA17_0 == NS_PREFIX)) {
                    alt17 = 1;
                }
                switch (alt17) {
                case 1:
                // BELScript.g:153:5: NS_PREFIX
                {
                    NS_PREFIX70 =
                            (Token) match(input, NS_PREFIX,
                                    FOLLOW_NS_PREFIX_in_param813);
                    NS_PREFIX70_tree =
                             adaptor.create(NS_PREFIX70);
                    adaptor.addChild(root_0, NS_PREFIX70_tree);

                }
                    break;

                }

                // BELScript.g:153:16: ( OBJECT_IDENT | quoted_value )
                int alt18 = 2;
                int LA18_0 = input.LA(1);

                if ((LA18_0 == OBJECT_IDENT)) {
                    alt18 = 1;
                }
                else if ((LA18_0 == QUOTED_VALUE)) {
                    alt18 = 2;
                }
                else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 18, 0, input);

                    throw nvae;

                }
                switch (alt18) {
                case 1:
                // BELScript.g:153:17: OBJECT_IDENT
                {
                    OBJECT_IDENT71 =
                            (Token) match(input, OBJECT_IDENT,
                                    FOLLOW_OBJECT_IDENT_in_param817);
                    OBJECT_IDENT71_tree =
                             adaptor.create(OBJECT_IDENT71);
                    adaptor.addChild(root_0, OBJECT_IDENT71_tree);

                }
                    break;
                case 2:
                // BELScript.g:153:32: quoted_value
                {
                    pushFollow(FOLLOW_quoted_value_in_param821);
                    quoted_value72 = quoted_value();

                    state._fsp--;

                    adaptor.addChild(root_0, quoted_value72.getTree());

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "param"

    public static class function_return extends ParserRuleReturnScope {
        public String r;
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "function"
    // BELScript.g:156:1: function returns [String r] : (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' ) ;
    public final BELScriptParser.function_return function()
            throws RecognitionException {
        BELScriptParser.function_return retval =
                new BELScriptParser.function_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token fv = null;

        Object fv_tree = null;

        try {
            // BELScript.g:156:28: ( (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' ) )
            // BELScript.g:157:5: (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' )
            {
                root_0 =  adaptor.nil();

                // BELScript.g:157:5: (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' )
                int alt19 = 59;
                switch (input.LA(1)) {
                case 106: {
                    alt19 = 1;
                }
                    break;
                case 95: {
                    alt19 = 2;
                }
                    break;
                case 114: {
                    alt19 = 3;
                }
                    break;
                case 108: {
                    alt19 = 4;
                }
                    break;
                case 52: {
                    alt19 = 5;
                }
                    break;
                case 51: {
                    alt19 = 6;
                }
                    break;
                case 91: {
                    alt19 = 7;
                }
                    break;
                case 90: {
                    alt19 = 8;
                }
                    break;
                case 78: {
                    alt19 = 9;
                }
                    break;
                case 77: {
                    alt19 = 10;
                }
                    break;
                case 56: {
                    alt19 = 11;
                }
                    break;
                case 58: {
                    alt19 = 12;
                }
                    break;
                case 97: {
                    alt19 = 13;
                }
                    break;
                case 96: {
                    alt19 = 14;
                }
                    break;
                case 67: {
                    alt19 = 15;
                }
                    break;
                case 66: {
                    alt19 = 16;
                }
                    break;
                case 126: {
                    alt19 = 17;
                }
                    break;
                case 121: {
                    alt19 = 18;
                }
                    break;
                case 62: {
                    alt19 = 19;
                }
                    break;
                case 116: {
                    alt19 = 20;
                }
                    break;
                case 63: {
                    alt19 = 21;
                }
                    break;
                case 120: {
                    alt19 = 22;
                }
                    break;
                case 111: {
                    alt19 = 23;
                }
                    break;
                case 115: {
                    alt19 = 24;
                }
                    break;
                case 69: {
                    alt19 = 25;
                }
                    break;
                case 68: {
                    alt19 = 26;
                }
                    break;
                case 76: {
                    alt19 = 27;
                }
                    break;
                case 75: {
                    alt19 = 28;
                }
                    break;
                case 72: {
                    alt19 = 29;
                }
                    break;
                case 71: {
                    alt19 = 30;
                }
                    break;
                case 92: {
                    alt19 = 31;
                }
                    break;
                case 53: {
                    alt19 = 32;
                }
                    break;
                case 60: {
                    alt19 = 33;
                }
                    break;
                case 59: {
                    alt19 = 34;
                }
                    break;
                case 88: {
                    alt19 = 35;
                }
                    break;
                case 87: {
                    alt19 = 36;
                }
                    break;
                case 101: {
                    alt19 = 37;
                }
                    break;
                case 100: {
                    alt19 = 38;
                }
                    break;
                case 99: {
                    alt19 = 39;
                }
                    break;
                case 98: {
                    alt19 = 40;
                }
                    break;
                case 113: {
                    alt19 = 41;
                }
                    break;
                case 112: {
                    alt19 = 42;
                }
                    break;
                case 124: {
                    alt19 = 43;
                }
                    break;
                case 130: {
                    alt19 = 44;
                }
                    break;
                case 127: {
                    alt19 = 45;
                }
                    break;
                case 122: {
                    alt19 = 46;
                }
                    break;
                case 80: {
                    alt19 = 47;
                }
                    break;
                case 79: {
                    alt19 = 48;
                }
                    break;
                case 65: {
                    alt19 = 49;
                }
                    break;
                case 64: {
                    alt19 = 50;
                }
                    break;
                case 107: {
                    alt19 = 51;
                }
                    break;
                case 102: {
                    alt19 = 52;
                }
                    break;
                case 119: {
                    alt19 = 53;
                }
                    break;
                case 117: {
                    alt19 = 54;
                }
                    break;
                case 129: {
                    alt19 = 55;
                }
                    break;
                case 128: {
                    alt19 = 56;
                }
                    break;
                case 110: {
                    alt19 = 57;
                }
                    break;
                case 104: {
                    alt19 = 58;
                }
                    break;
                case 89: {
                    alt19 = 59;
                }
                    break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 19, 0, input);

                    throw nvae;

                }

                switch (alt19) {
                case 1:
                // BELScript.g:158:9: fv= 'proteinAbundance'
                {
                    fv = (Token) match(input, 106, FOLLOW_106_in_function854);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 2:
                // BELScript.g:159:9: fv= 'p'
                {
                    fv = (Token) match(input, 95, FOLLOW_95_in_function880);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 3:
                // BELScript.g:160:9: fv= 'rnaAbundance'
                {
                    fv = (Token) match(input, 114, FOLLOW_114_in_function921);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 4:
                // BELScript.g:161:9: fv= 'r'
                {
                    fv = (Token) match(input, 108, FOLLOW_108_in_function952);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 5:
                // BELScript.g:162:9: fv= 'abundance'
                {
                    fv = (Token) match(input, 52, FOLLOW_52_in_function993);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 6:
                // BELScript.g:163:9: fv= 'a'
                {
                    fv = (Token) match(input, 51, FOLLOW_51_in_function1027);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 7:
                // BELScript.g:164:9: fv= 'microRNAAbundance'
                {
                    fv = (Token) match(input, 91, FOLLOW_91_in_function1068);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 8:
                // BELScript.g:165:9: fv= 'm'
                {
                    fv = (Token) match(input, 90, FOLLOW_90_in_function1094);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 9:
                // BELScript.g:166:9: fv= 'geneAbundance'
                {
                    fv = (Token) match(input, 78, FOLLOW_78_in_function1135);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 10:
                // BELScript.g:167:9: fv= 'g'
                {
                    fv = (Token) match(input, 77, FOLLOW_77_in_function1164);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 11:
                // BELScript.g:168:9: fv= 'biologicalProcess'
                {
                    fv = (Token) match(input, 56, FOLLOW_56_in_function1205);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 12:
                // BELScript.g:169:9: fv= 'bp'
                {
                    fv = (Token) match(input, 58, FOLLOW_58_in_function1231);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 13:
                // BELScript.g:170:9: fv= 'pathology'
                {
                    fv = (Token) match(input, 97, FOLLOW_97_in_function1271);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 14:
                // BELScript.g:171:9: fv= 'path'
                {
                    fv = (Token) match(input, 96, FOLLOW_96_in_function1304);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 15:
                // BELScript.g:172:9: fv= 'complexAbundance'
                {
                    fv = (Token) match(input, 67, FOLLOW_67_in_function1342);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 16:
                // BELScript.g:173:9: fv= 'complex'
                {
                    fv = (Token) match(input, 66, FOLLOW_66_in_function1369);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 17:
                // BELScript.g:174:9: fv= 'translocation'
                {
                    fv = (Token) match(input, 126, FOLLOW_126_in_function1404);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 18:
                // BELScript.g:175:9: fv= 'tloc'
                {
                    fv = (Token) match(input, 121, FOLLOW_121_in_function1434);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 19:
                // BELScript.g:176:9: fv= 'cellSecretion'
                {
                    fv = (Token) match(input, 62, FOLLOW_62_in_function1472);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 20:
                // BELScript.g:177:9: fv= 'sec'
                {
                    fv = (Token) match(input, 116, FOLLOW_116_in_function1502);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 21:
                // BELScript.g:178:9: fv= 'cellSurfaceExpression'
                {
                    fv = (Token) match(input, 63, FOLLOW_63_in_function1541);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 22:
                // BELScript.g:179:9: fv= 'surf'
                {
                    fv = (Token) match(input, 120, FOLLOW_120_in_function1562);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 23:
                // BELScript.g:180:9: fv= 'reaction'
                {
                    fv = (Token) match(input, 111, FOLLOW_111_in_function1600);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 24:
                // BELScript.g:181:9: fv= 'rxn'
                {
                    fv = (Token) match(input, 115, FOLLOW_115_in_function1634);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 25:
                // BELScript.g:182:9: fv= 'compositeAbundance'
                {
                    fv = (Token) match(input, 69, FOLLOW_69_in_function1673);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 26:
                // BELScript.g:183:9: fv= 'composite'
                {
                    fv = (Token) match(input, 68, FOLLOW_68_in_function1697);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 27:
                // BELScript.g:184:9: fv= 'fusion'
                {
                    fv = (Token) match(input, 76, FOLLOW_76_in_function1730);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 28:
                // BELScript.g:185:9: fv= 'fus'
                {
                    fv = (Token) match(input, 75, FOLLOW_75_in_function1766);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 29:
                // BELScript.g:186:9: fv= 'degradation'
                {
                    fv = (Token) match(input, 72, FOLLOW_72_in_function1805);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 30:
                // BELScript.g:187:9: fv= 'deg'
                {
                    fv = (Token) match(input, 71, FOLLOW_71_in_function1836);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 31:
                // BELScript.g:188:9: fv= 'molecularActivity'
                {
                    fv = (Token) match(input, 92, FOLLOW_92_in_function1875);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 32:
                // BELScript.g:189:9: fv= 'act'
                {
                    fv = (Token) match(input, 53, FOLLOW_53_in_function1900);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 33:
                // BELScript.g:190:9: fv= 'catalyticActivity'
                {
                    fv = (Token) match(input, 60, FOLLOW_60_in_function1939);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 34:
                // BELScript.g:191:9: fv= 'cat'
                {
                    fv = (Token) match(input, 59, FOLLOW_59_in_function1964);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 35:
                // BELScript.g:192:9: fv= 'kinaseActivity'
                {
                    fv = (Token) match(input, 88, FOLLOW_88_in_function2003);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 36:
                // BELScript.g:193:9: fv= 'kin'
                {
                    fv = (Token) match(input, 87, FOLLOW_87_in_function2031);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 37:
                // BELScript.g:194:9: fv= 'phosphataseActivity'
                {
                    fv = (Token) match(input, 101, FOLLOW_101_in_function2070);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 38:
                // BELScript.g:195:9: fv= 'phos'
                {
                    fv = (Token) match(input, 100, FOLLOW_100_in_function2093);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 39:
                // BELScript.g:196:9: fv= 'peptidaseActivity'
                {
                    fv = (Token) match(input, 99, FOLLOW_99_in_function2131);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 40:
                // BELScript.g:197:9: fv= 'pep'
                {
                    fv = (Token) match(input, 98, FOLLOW_98_in_function2156);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 41:
                // BELScript.g:198:9: fv= 'ribosylationActivity'
                {
                    fv = (Token) match(input, 113, FOLLOW_113_in_function2195);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 42:
                // BELScript.g:199:9: fv= 'ribo'
                {
                    fv = (Token) match(input, 112, FOLLOW_112_in_function2217);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 43:
                // BELScript.g:200:9: fv= 'transcriptionalActivity'
                {
                    fv = (Token) match(input, 124, FOLLOW_124_in_function2255);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 44:
                // BELScript.g:201:9: fv= 'tscript'
                {
                    fv = (Token) match(input, 130, FOLLOW_130_in_function2274);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 45:
                // BELScript.g:202:9: fv= 'transportActivity'
                {
                    fv = (Token) match(input, 127, FOLLOW_127_in_function2309);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 46:
                // BELScript.g:203:9: fv= 'tport'
                {
                    fv = (Token) match(input, 122, FOLLOW_122_in_function2334);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 47:
                // BELScript.g:204:9: fv= 'gtpBoundActivity'
                {
                    fv = (Token) match(input, 80, FOLLOW_80_in_function2371);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 48:
                // BELScript.g:205:9: fv= 'gtp'
                {
                    fv = (Token) match(input, 79, FOLLOW_79_in_function2397);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 49:
                // BELScript.g:206:9: fv= 'chaperoneActivity'
                {
                    fv = (Token) match(input, 65, FOLLOW_65_in_function2436);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 50:
                // BELScript.g:207:9: fv= 'chap'
                {
                    fv = (Token) match(input, 64, FOLLOW_64_in_function2461);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 51:
                // BELScript.g:208:9: fv= 'proteinModification'
                {
                    fv = (Token) match(input, 107, FOLLOW_107_in_function2499);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 52:
                // BELScript.g:209:9: fv= 'pmod'
                {
                    fv = (Token) match(input, 102, FOLLOW_102_in_function2522);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 53:
                // BELScript.g:210:9: fv= 'substitution'
                {
                    fv = (Token) match(input, 119, FOLLOW_119_in_function2560);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 54:
                // BELScript.g:211:9: fv= 'sub'
                {
                    fv = (Token) match(input, 117, FOLLOW_117_in_function2590);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 55:
                // BELScript.g:212:9: fv= 'truncation'
                {
                    fv = (Token) match(input, 129, FOLLOW_129_in_function2629);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 56:
                // BELScript.g:213:9: fv= 'trunc'
                {
                    fv = (Token) match(input, 128, FOLLOW_128_in_function2661);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 57:
                // BELScript.g:214:9: fv= 'reactants'
                {
                    fv = (Token) match(input, 110, FOLLOW_110_in_function2698);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 58:
                // BELScript.g:215:9: fv= 'products'
                {
                    fv = (Token) match(input, 104, FOLLOW_104_in_function2731);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 59:
                // BELScript.g:216:9: fv= 'list'
                {
                    fv = (Token) match(input, 89, FOLLOW_89_in_function2765);
                    fv_tree =
                             adaptor.create(fv);
                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "function"

    public static class relationship_return extends ParserRuleReturnScope {
        public String r;
        Object tree;

        @Override
        public Object getTree() {
            return tree;
        }
    };

    // $ANTLR start "relationship"
    // BELScript.g:220:1: relationship returns [String r] : (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' ) ;
    public final BELScriptParser.relationship_return relationship()
            throws RecognitionException {
        BELScriptParser.relationship_return retval =
                new BELScriptParser.relationship_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token rv = null;

        Object rv_tree = null;

        try {
            // BELScript.g:220:32: ( (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' ) )
            // BELScript.g:221:5: (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' )
            {
                root_0 =  adaptor.nil();

                // BELScript.g:221:5: (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' )
                int alt20 = 28;
                switch (input.LA(1)) {
                case 85: {
                    alt20 = 1;
                }
                    break;
                case 26: {
                    alt20 = 2;
                }
                    break;
                case 70: {
                    alt20 = 3;
                }
                    break;
                case 27: {
                    alt20 = 4;
                }
                    break;
                case 74: {
                    alt20 = 5;
                }
                    break;
                case 30: {
                    alt20 = 6;
                }
                    break;
                case 73: {
                    alt20 = 7;
                }
                    break;
                case 31: {
                    alt20 = 8;
                }
                    break;
                case 61: {
                    alt20 = 9;
                }
                    break;
                case 103: {
                    alt20 = 10;
                }
                    break;
                case 93: {
                    alt20 = 11;
                }
                    break;
                case 125: {
                    alt20 = 12;
                }
                    break;
                case 32: {
                    alt20 = 13;
                }
                    break;
                case 123: {
                    alt20 = 14;
                }
                    break;
                case 28: {
                    alt20 = 15;
                }
                    break;
                case 86: {
                    alt20 = 16;
                }
                    break;
                case 118: {
                    alt20 = 17;
                }
                    break;
                case 109: {
                    alt20 = 18;
                }
                    break;
                case 57: {
                    alt20 = 19;
                }
                    break;
                case 105: {
                    alt20 = 20;
                }
                    break;
                case 94: {
                    alt20 = 21;
                }
                    break;
                case 54: {
                    alt20 = 22;
                }
                    break;
                case 55: {
                    alt20 = 23;
                }
                    break;
                case 25: {
                    alt20 = 24;
                }
                    break;
                case 84: {
                    alt20 = 25;
                }
                    break;
                case 82: {
                    alt20 = 26;
                }
                    break;
                case 83: {
                    alt20 = 27;
                }
                    break;
                case 81: {
                    alt20 = 28;
                }
                    break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 20, 0, input);

                    throw nvae;

                }

                switch (alt20) {
                case 1:
                // BELScript.g:222:9: rv= 'increases'
                {
                    rv =
                            (Token) match(input, 85,
                                    FOLLOW_85_in_relationship2831);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 2:
                // BELScript.g:223:9: rv= '->'
                {
                    rv =
                            (Token) match(input, 26,
                                    FOLLOW_26_in_relationship2865);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 3:
                // BELScript.g:224:9: rv= 'decreases'
                {
                    rv =
                            (Token) match(input, 70,
                                    FOLLOW_70_in_relationship2905);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 4:
                // BELScript.g:225:9: rv= '-|'
                {
                    rv =
                            (Token) match(input, 27,
                                    FOLLOW_27_in_relationship2939);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 5:
                // BELScript.g:226:9: rv= 'directlyIncreases'
                {
                    rv =
                            (Token) match(input, 74,
                                    FOLLOW_74_in_relationship2979);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 6:
                // BELScript.g:227:9: rv= '=>'
                {
                    rv =
                            (Token) match(input, 30,
                                    FOLLOW_30_in_relationship3004);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 7:
                // BELScript.g:228:9: rv= 'directlyDecreases'
                {
                    rv =
                            (Token) match(input, 73,
                                    FOLLOW_73_in_relationship3044);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 8:
                // BELScript.g:229:9: rv= '=|'
                {
                    rv =
                            (Token) match(input, 31,
                                    FOLLOW_31_in_relationship3069);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 9:
                // BELScript.g:230:9: rv= 'causesNoChange'
                {
                    rv =
                            (Token) match(input, 61,
                                    FOLLOW_61_in_relationship3109);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 10:
                // BELScript.g:231:9: rv= 'positiveCorrelation'
                {
                    rv =
                            (Token) match(input, 103,
                                    FOLLOW_103_in_relationship3137);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 11:
                // BELScript.g:232:9: rv= 'negativeCorrelation'
                {
                    rv =
                            (Token) match(input, 93,
                                    FOLLOW_93_in_relationship3160);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 12:
                // BELScript.g:233:9: rv= 'translatedTo'
                {
                    rv =
                            (Token) match(input, 125,
                                    FOLLOW_125_in_relationship3183);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 13:
                // BELScript.g:234:9: rv= '>>'
                {
                    rv =
                            (Token) match(input, 32,
                                    FOLLOW_32_in_relationship3213);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 14:
                // BELScript.g:235:9: rv= 'transcribedTo'
                {
                    rv =
                            (Token) match(input, 123,
                                    FOLLOW_123_in_relationship3253);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 15:
                // BELScript.g:236:9: rv= ':>'
                {
                    rv =
                            (Token) match(input, 28,
                                    FOLLOW_28_in_relationship3282);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 16:
                // BELScript.g:237:9: rv= 'isA'
                {
                    rv =
                            (Token) match(input, 86,
                                    FOLLOW_86_in_relationship3322);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 17:
                // BELScript.g:238:9: rv= 'subProcessOf'
                {
                    rv =
                            (Token) match(input, 118,
                                    FOLLOW_118_in_relationship3361);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 18:
                // BELScript.g:239:9: rv= 'rateLimitingStepOf'
                {
                    rv =
                            (Token) match(input, 109,
                                    FOLLOW_109_in_relationship3391);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 19:
                // BELScript.g:240:9: rv= 'biomarkerFor'
                {
                    rv =
                            (Token) match(input, 57,
                                    FOLLOW_57_in_relationship3415);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 20:
                // BELScript.g:241:9: rv= 'prognosticBiomarkerFor'
                {
                    rv =
                            (Token) match(input, 105,
                                    FOLLOW_105_in_relationship3445);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 21:
                // BELScript.g:242:9: rv= 'orthologous'
                {
                    rv =
                            (Token) match(input, 94,
                                    FOLLOW_94_in_relationship3465);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 22:
                // BELScript.g:243:9: rv= 'analogous'
                {
                    rv =
                            (Token) match(input, 54,
                                    FOLLOW_54_in_relationship3496);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 23:
                // BELScript.g:244:9: rv= 'association'
                {
                    rv =
                            (Token) match(input, 55,
                                    FOLLOW_55_in_relationship3529);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 24:
                // BELScript.g:245:9: rv= '--'
                {
                    rv =
                            (Token) match(input, 25,
                                    FOLLOW_25_in_relationship3560);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 25:
                // BELScript.g:246:9: rv= 'hasMembers'
                {
                    rv =
                            (Token) match(input, 84,
                                    FOLLOW_84_in_relationship3600);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 26:
                // BELScript.g:247:9: rv= 'hasComponents'
                {
                    rv =
                            (Token) match(input, 82,
                                    FOLLOW_82_in_relationship3632);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 27:
                // BELScript.g:248:9: rv= 'hasMember'
                {
                    rv =
                            (Token) match(input, 83,
                                    FOLLOW_83_in_relationship3661);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 28:
                // BELScript.g:249:9: rv= 'hasComponent'
                {
                    rv =
                            (Token) match(input, 81,
                                    FOLLOW_81_in_relationship3694);
                    rv_tree =
                             adaptor.create(rv);
                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree =  adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree =
                     adaptor.errorNode(input, retval.start,
                            input.LT(-1), re);

        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "relationship"

    // Delegated rules

    public static final BitSet FOLLOW_NEWLINE_in_document62 = new BitSet(
            new long[] { 0xDD39808000002080L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_DOCUMENT_COMMENT_in_document66 =
            new BitSet(new long[] { 0xDD39808000002080L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_record_in_document70 = new BitSet(
            new long[] { 0xDD39808000002080L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_EOF_in_document74 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_define_namespace_in_record91 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_define_annotation_in_record95 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_annotation_in_record99 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_document_in_record103 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_statement_group_in_record107 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_unset_statement_group_in_record111 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_unset_in_record115 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_statement_in_record119 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_47_in_set_document160 = new BitSet(
            new long[] { 0x0000000000000100L });
    public static final BitSet FOLLOW_DOCUMENT_KEYWORD_in_set_document162 =
            new BitSet(new long[] { 0x00042B3800000000L });
    public static final BitSet FOLLOW_document_property_in_set_document165 =
            new BitSet(new long[] { 0x0000000020000000L });
    public static final BitSet FOLLOW_29_in_set_document167 = new BitSet(
            new long[] { 0x0000000000448000L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_document170 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_VALUE_LIST_in_set_document176 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_quoted_value_in_set_document180 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_47_in_set_statement_group226 =
            new BitSet(new long[] { 0x0000000000100000L });
    public static final BitSet FOLLOW_STATEMENT_GROUP_KEYWORD_in_set_statement_group228 =
            new BitSet(new long[] { 0x0000000020000000L });
    public static final BitSet FOLLOW_29_in_set_statement_group230 =
            new BitSet(new long[] { 0x0000000000048000L });
    public static final BitSet FOLLOW_quoted_value_in_set_statement_group233 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_statement_group237 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_47_in_set_annotation277 = new BitSet(
            new long[] { 0x0000000000008000L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_annotation279 =
            new BitSet(new long[] { 0x0000000020000000L });
    public static final BitSet FOLLOW_29_in_set_annotation281 = new BitSet(
            new long[] { 0x0000000000448000L });
    public static final BitSet FOLLOW_quoted_value_in_set_annotation284 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_VALUE_LIST_in_set_annotation290 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_annotation294 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_48_in_unset_statement_group340 =
            new BitSet(new long[] { 0x0000000000100000L });
    public static final BitSet FOLLOW_STATEMENT_GROUP_KEYWORD_in_unset_statement_group342 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_48_in_unset381 = new BitSet(
            new long[] { 0x0000000000008800L });
    public static final BitSet FOLLOW_set_in_unset383 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_39_in_define_namespace429 = new BitSet(
            new long[] { 0x0000104000000000L });
    public static final BitSet FOLLOW_38_in_define_namespace433 = new BitSet(
            new long[] { 0x0000100000000000L });
    public static final BitSet FOLLOW_44_in_define_namespace437 = new BitSet(
            new long[] { 0x0000000000008000L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_define_namespace441 =
            new BitSet(new long[] { 0x0000000400000000L });
    public static final BitSet FOLLOW_34_in_define_namespace443 = new BitSet(
            new long[] { 0x0002000000000000L });
    public static final BitSet FOLLOW_49_in_define_namespace445 = new BitSet(
            new long[] { 0x0000000000040000L });
    public static final BitSet FOLLOW_quoted_value_in_define_namespace447 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_39_in_define_annotation487 = new BitSet(
            new long[] { 0x0000000200000000L });
    public static final BitSet FOLLOW_33_in_define_annotation489 = new BitSet(
            new long[] { 0x0000000000008000L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_define_annotation492 =
            new BitSet(new long[] { 0x0000000400000000L });
    public static final BitSet FOLLOW_34_in_define_annotation494 = new BitSet(
            new long[] { 0x0002440000000000L });
    public static final BitSet FOLLOW_set_in_define_annotation498 = new BitSet(
            new long[] { 0x0000000000040000L });
    public static final BitSet FOLLOW_quoted_value_in_define_annotation506 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_42_in_define_annotation512 = new BitSet(
            new long[] { 0x0000000000400000L });
    public static final BitSet FOLLOW_VALUE_LIST_in_define_annotation516 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_quoted_value543 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_outer_term_in_statement677 = new BitSet(
            new long[] { 0x22C00001DE080002L, 0x28402280607E0640L });
    public static final BitSet FOLLOW_relationship_in_statement680 =
            new BitSet(new long[] { 0xDD38000000010000L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_OPEN_PAREN_in_statement684 = new BitSet(
            new long[] { 0xDD38000000000000L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_outer_term_in_statement686 = new BitSet(
            new long[] { 0x22C00001DE000000L, 0x28402280607E0640L });
    public static final BitSet FOLLOW_relationship_in_statement688 =
            new BitSet(new long[] { 0xDD38000000000000L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_outer_term_in_statement690 = new BitSet(
            new long[] { 0x0000000000000010L });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_statement692 = new BitSet(
            new long[] { 0x0000000000080002L });
    public static final BitSet FOLLOW_outer_term_in_statement697 = new BitSet(
            new long[] { 0x0000000000080002L });
    public static final BitSet FOLLOW_STATEMENT_COMMENT_in_statement702 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_function_in_outer_term723 = new BitSet(
            new long[] { 0x0000000000010000L });
    public static final BitSet FOLLOW_OPEN_PAREN_in_outer_term725 = new BitSet(
            new long[] { 0xDD3800000104C010L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_24_in_outer_term728 = new BitSet(
            new long[] { 0xDD3800000004C000L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_argument_in_outer_term731 = new BitSet(
            new long[] { 0xDD3800000104C010L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_outer_term735 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_param_in_argument755 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_term_in_argument759 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_function_in_term775 = new BitSet(
            new long[] { 0x0000000000010000L });
    public static final BitSet FOLLOW_OPEN_PAREN_in_term777 = new BitSet(
            new long[] { 0xDD3800000104C010L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_24_in_term780 = new BitSet(new long[] {
            0xDD3800000004C000L, 0xD7BFDD7F9F81F9BFL, 0x0000000000000007L });
    public static final BitSet FOLLOW_term_in_term784 = new BitSet(new long[] {
            0xDD3800000104C010L, 0xD7BFDD7F9F81F9BFL, 0x0000000000000007L });
    public static final BitSet FOLLOW_param_in_term788 = new BitSet(new long[] {
            0xDD3800000104C010L, 0xD7BFDD7F9F81F9BFL, 0x0000000000000007L });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_term793 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_NS_PREFIX_in_param813 = new BitSet(
            new long[] { 0x0000000000048000L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_param817 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_quoted_value_in_param821 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_106_in_function854 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_95_in_function880 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_114_in_function921 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_108_in_function952 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_52_in_function993 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_51_in_function1027 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_91_in_function1068 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_90_in_function1094 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_78_in_function1135 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_77_in_function1164 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_56_in_function1205 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_58_in_function1231 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_97_in_function1271 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_96_in_function1304 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_67_in_function1342 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_66_in_function1369 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_126_in_function1404 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_121_in_function1434 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_62_in_function1472 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_116_in_function1502 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_63_in_function1541 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_120_in_function1562 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_111_in_function1600 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_115_in_function1634 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_69_in_function1673 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_68_in_function1697 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_76_in_function1730 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_75_in_function1766 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_72_in_function1805 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_71_in_function1836 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_92_in_function1875 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_53_in_function1900 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_60_in_function1939 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_59_in_function1964 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_88_in_function2003 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_87_in_function2031 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_101_in_function2070 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_100_in_function2093 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_99_in_function2131 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_98_in_function2156 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_113_in_function2195 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_112_in_function2217 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_124_in_function2255 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_130_in_function2274 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_127_in_function2309 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_122_in_function2334 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_80_in_function2371 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_79_in_function2397 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_65_in_function2436 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_64_in_function2461 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_107_in_function2499 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_102_in_function2522 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_119_in_function2560 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_117_in_function2590 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_129_in_function2629 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_128_in_function2661 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_110_in_function2698 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_104_in_function2731 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_89_in_function2765 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_85_in_relationship2831 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_26_in_relationship2865 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_70_in_relationship2905 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_27_in_relationship2939 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_74_in_relationship2979 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_30_in_relationship3004 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_73_in_relationship3044 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_31_in_relationship3069 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_61_in_relationship3109 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_103_in_relationship3137 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_93_in_relationship3160 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_125_in_relationship3183 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_32_in_relationship3213 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_123_in_relationship3253 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_28_in_relationship3282 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_86_in_relationship3322 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_118_in_relationship3361 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_109_in_relationship3391 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_57_in_relationship3415 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_105_in_relationship3445 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_94_in_relationship3465 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_54_in_relationship3496 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_55_in_relationship3529 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_25_in_relationship3560 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_84_in_relationship3600 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_82_in_relationship3632 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_83_in_relationship3661 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_81_in_relationship3694 = new BitSet(
            new long[] { 0x0000000000000002L });

}
