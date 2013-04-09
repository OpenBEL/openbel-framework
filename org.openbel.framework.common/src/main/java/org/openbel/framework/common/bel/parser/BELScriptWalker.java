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
// $ANTLR 3.4 BELScriptWalker.g 2012-06-27 17:18:04

package org.openbel.framework.common.bel.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;
import org.antlr.runtime.tree.TreeNodeStream;
import org.antlr.runtime.tree.TreeParser;
import org.antlr.runtime.tree.TreeRuleReturnScope;
import org.openbel.bel.model.*;
import org.openbel.bel.model.BELParseErrorException.DefineAnnotationBeforeUsageException;
import org.openbel.bel.model.BELParseErrorException.DocumentDescriptionException;
import org.openbel.bel.model.BELParseErrorException.DocumentNameException;
import org.openbel.bel.model.BELParseErrorException.DocumentVersionException;
import org.openbel.bel.model.BELParseErrorException.InvalidCitationException;
import org.openbel.bel.model.BELParseErrorException.InvalidEvidenceException;
import org.openbel.bel.model.BELParseErrorException.NamespaceUndefinedException;
import org.openbel.bel.model.BELParseErrorException.SetDocumentPropertiesFirstException;
import org.openbel.bel.model.BELParseErrorException.UnsetDocumentPropertiesException;
import org.openbel.bel.model.BELParseWarningException.UnsetUndefinedAnnotationException;

@SuppressWarnings({ "unused" })
public class BELScriptWalker extends TreeParser {
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
    public TreeParser[] getDelegates() {
        return new TreeParser[] {};
    }

    // delegators

    public BELScriptWalker(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }

    public BELScriptWalker(TreeNodeStream input, RecognizerSharedState state) {
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
        return BELScriptWalker.tokenNames;
    }

    @Override
    public String getGrammarFileName() {
        return "BELScriptWalker.g";
    }

    private final Map<BELDocumentProperty, String> docprop =
            new HashMap<BELDocumentProperty, String>();
    private int lastDocumentPropertyLocation = 0;
    private final Set<BELAnnotationDefinition> adlist =
            new LinkedHashSet<BELAnnotationDefinition>();
    private final Map<String, BELAnnotationDefinition> definedAnnotations =
            new LinkedHashMap<String, BELAnnotationDefinition>();
    private final Map<String, BELNamespaceDefinition> definedNamespaces =
            new LinkedHashMap<String, BELNamespaceDefinition>();
    private final Set<BELNamespaceDefinition> nslist =
            new LinkedHashSet<BELNamespaceDefinition>();

    private BELStatementGroup activeStatementGroup;
    private BELStatementGroup documentStatementGroup = new BELStatementGroup();
    private List<BELStatementGroup> statementGroups =
            new ArrayList<BELStatementGroup>();

    private final Map<String, BELAnnotation> sgAnnotationContext =
            new LinkedHashMap<String, BELAnnotation>();
    private final Map<String, BELAnnotation> annotationContext =
            new LinkedHashMap<String, BELAnnotation>();
    private BELCitation citationContext;
    private BELEvidence evidenceContext;

    private final List<BELStatement> stmtlist = new ArrayList<BELStatement>();
    private final List<BELParseErrorException> syntaxErrors =
            new ArrayList<BELParseErrorException>();
    private final List<BELParseWarningException> syntaxWarnings =
            new ArrayList<BELParseWarningException>();

    private static final SimpleDateFormat iso8601DateFormat =
            new SimpleDateFormat("yyyy-MM-dd");

    public List<BELParseErrorException> getSyntaxErrors() {
        return syntaxErrors;
    }

    public List<BELParseWarningException> getSyntaxWarnings() {
        return syntaxWarnings;
    }

    @Override
    public void emitErrorMessage(String msg) {
    }

    public void addError(BELParseErrorException e) {
        syntaxErrors.add(e);
    }

    public void addWarning(BELParseWarningException e) {
        syntaxWarnings.add(e);
    }

    public static class document_return extends TreeRuleReturnScope {
        public BELDocument doc;
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "document"
    // BELScriptWalker.g:96:1: document returns [BELDocument doc] : ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF ;
    public final BELScriptWalker.document_return document()
            throws RecognitionException {
        BELScriptWalker.document_return retval =
                new BELScriptWalker.document_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree NEWLINE1 = null;
        CommonTree DOCUMENT_COMMENT2 = null;
        CommonTree EOF4 = null;
        BELScriptWalker.record_return record3 = null;

        CommonTree NEWLINE1_tree = null;
        CommonTree DOCUMENT_COMMENT2_tree = null;
        CommonTree EOF4_tree = null;

        try {
            // BELScriptWalker.g:96:35: ( ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF )
            // BELScriptWalker.g:97:5: ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF
            {
                root_0 = (CommonTree) adaptor.nil();

                // BELScriptWalker.g:97:5: ( NEWLINE | DOCUMENT_COMMENT | record )+
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
                    // BELScriptWalker.g:97:6: NEWLINE
                    {
                        _last = (CommonTree) input.LT(1);
                        NEWLINE1 =
                                (CommonTree) match(input, NEWLINE,
                                        FOLLOW_NEWLINE_in_document67);
                        NEWLINE1_tree = (CommonTree) adaptor.dupNode(NEWLINE1);

                        adaptor.addChild(root_0, NEWLINE1_tree);

                    }
                        break;
                    case 2:
                    // BELScriptWalker.g:97:16: DOCUMENT_COMMENT
                    {
                        _last = (CommonTree) input.LT(1);
                        DOCUMENT_COMMENT2 =
                                (CommonTree) match(input, DOCUMENT_COMMENT,
                                        FOLLOW_DOCUMENT_COMMENT_in_document71);
                        DOCUMENT_COMMENT2_tree =
                                (CommonTree) adaptor.dupNode(DOCUMENT_COMMENT2);

                        adaptor.addChild(root_0, DOCUMENT_COMMENT2_tree);

                    }
                        break;
                    case 3:
                    // BELScriptWalker.g:97:35: record
                    {
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_record_in_document75);
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

                _last = (CommonTree) input.LT(1);
                EOF4 = (CommonTree) match(input, EOF, FOLLOW_EOF_in_document79);
                EOF4_tree = (CommonTree) adaptor.dupNode(EOF4);

                adaptor.addChild(root_0, EOF4_tree);

                if (!docprop.containsKey(BELDocumentProperty.NAME)) {
                    addError(new DocumentNameException(
                            lastDocumentPropertyLocation, 0));
                } else if (!docprop
                        .containsKey(BELDocumentProperty.DESCRIPTION)) {
                    addError(new DocumentDescriptionException(
                            lastDocumentPropertyLocation, 0));
                } else if (!docprop.containsKey(BELDocumentProperty.VERSION)) {
                    addError(new DocumentVersionException(
                            lastDocumentPropertyLocation, 0));
                } else {
                    if (documentStatementGroup.getStatements().isEmpty()) {
                        // statements are only contained in explicitly-defined statement groups
                        retval.doc =
                                new BELDocument(
                                        BELDocumentHeader.create(docprop),
                                        adlist, nslist, statementGroups);
                    } else {
                        // statements are defined in the implicit document statement group and possibly child statement groups
                        documentStatementGroup
                                .setChildStatementGroups(statementGroups);
                        retval.doc =
                                new BELDocument(
                                        BELDocumentHeader.create(docprop),
                                        adlist, nslist,
                                        Arrays.asList(documentStatementGroup));
                    }
                }

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "document"

    public static class record_return extends TreeRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "record"
    // BELScriptWalker.g:117:1: record : ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement ) ;
    public final BELScriptWalker.record_return record()
            throws RecognitionException {
        BELScriptWalker.record_return retval =
                new BELScriptWalker.record_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        BELScriptWalker.define_namespace_return define_namespace5 = null;

        BELScriptWalker.define_annotation_return define_annotation6 = null;

        BELScriptWalker.set_annotation_return set_annotation7 = null;

        BELScriptWalker.set_document_return set_document8 = null;

        BELScriptWalker.set_statement_group_return set_statement_group9 = null;

        BELScriptWalker.unset_statement_group_return unset_statement_group10 =
                null;

        BELScriptWalker.unset_return unset11 = null;

        BELScriptWalker.statement_return statement12 = null;

        try {
            // BELScriptWalker.g:117:7: ( ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement ) )
            // BELScriptWalker.g:118:5: ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement )
            {
                root_0 = (CommonTree) adaptor.nil();

                // BELScriptWalker.g:118:5: ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement )
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
                // BELScriptWalker.g:118:6: define_namespace
                {
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_define_namespace_in_record98);
                    define_namespace5 = define_namespace();

                    state._fsp--;

                    adaptor.addChild(root_0, define_namespace5.getTree());

                }
                    break;
                case 2:
                // BELScriptWalker.g:118:25: define_annotation
                {
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_define_annotation_in_record102);
                    define_annotation6 = define_annotation();

                    state._fsp--;

                    adaptor.addChild(root_0, define_annotation6.getTree());

                }
                    break;
                case 3:
                // BELScriptWalker.g:118:45: set_annotation
                {
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_set_annotation_in_record106);
                    set_annotation7 = set_annotation();

                    state._fsp--;

                    adaptor.addChild(root_0, set_annotation7.getTree());

                }
                    break;
                case 4:
                // BELScriptWalker.g:118:62: set_document
                {
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_set_document_in_record110);
                    set_document8 = set_document();

                    state._fsp--;

                    adaptor.addChild(root_0, set_document8.getTree());

                }
                    break;
                case 5:
                // BELScriptWalker.g:118:77: set_statement_group
                {
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_set_statement_group_in_record114);
                    set_statement_group9 = set_statement_group();

                    state._fsp--;

                    adaptor.addChild(root_0, set_statement_group9.getTree());

                }
                    break;
                case 6:
                // BELScriptWalker.g:118:99: unset_statement_group
                {
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_unset_statement_group_in_record118);
                    unset_statement_group10 = unset_statement_group();

                    state._fsp--;

                    adaptor.addChild(root_0, unset_statement_group10.getTree());

                }
                    break;
                case 7:
                // BELScriptWalker.g:118:123: unset
                {
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_unset_in_record122);
                    unset11 = unset();

                    state._fsp--;

                    adaptor.addChild(root_0, unset11.getTree());

                }
                    break;
                case 8:
                // BELScriptWalker.g:118:131: statement
                {
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_statement_in_record126);
                    statement12 = statement();

                    state._fsp--;

                    adaptor.addChild(root_0, statement12.getTree());

                }
                    break;

                }

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "record"

    public static class set_document_return extends TreeRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "set_document"
    // BELScriptWalker.g:121:1: set_document : ( 'SET' dkt= DOCUMENT_KEYWORD ) prop= document_property '=' (qv= QUOTED_VALUE |oi= OBJECT_IDENT ) ;
    public final BELScriptWalker.set_document_return set_document()
            throws RecognitionException {
        BELScriptWalker.set_document_return retval =
                new BELScriptWalker.set_document_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree dkt = null;
        CommonTree qv = null;
        CommonTree oi = null;
        CommonTree string_literal13 = null;
        CommonTree char_literal14 = null;
        BELScriptWalker.document_property_return prop = null;

        CommonTree dkt_tree = null;
        CommonTree qv_tree = null;
        CommonTree oi_tree = null;
        CommonTree string_literal13_tree = null;
        CommonTree char_literal14_tree = null;

        try {
            // BELScriptWalker.g:121:13: ( ( 'SET' dkt= DOCUMENT_KEYWORD ) prop= document_property '=' (qv= QUOTED_VALUE |oi= OBJECT_IDENT ) )
            // BELScriptWalker.g:122:5: ( 'SET' dkt= DOCUMENT_KEYWORD ) prop= document_property '=' (qv= QUOTED_VALUE |oi= OBJECT_IDENT )
            {
                root_0 = (CommonTree) adaptor.nil();

                // BELScriptWalker.g:122:5: ( 'SET' dkt= DOCUMENT_KEYWORD )
                // BELScriptWalker.g:122:6: 'SET' dkt= DOCUMENT_KEYWORD
                {
                    _last = (CommonTree) input.LT(1);
                    string_literal13 =
                            (CommonTree) match(input, 47,
                                    FOLLOW_47_in_set_document144);
                    string_literal13_tree =
                            (CommonTree) adaptor.dupNode(string_literal13);

                    adaptor.addChild(root_0, string_literal13_tree);

                    _last = (CommonTree) input.LT(1);
                    dkt =
                            (CommonTree) match(input, DOCUMENT_KEYWORD,
                                    FOLLOW_DOCUMENT_KEYWORD_in_set_document148);
                    dkt_tree = (CommonTree) adaptor.dupNode(dkt);

                    adaptor.addChild(root_0, dkt_tree);

                }

                _last = (CommonTree) input.LT(1);
                pushFollow(FOLLOW_document_property_in_set_document153);
                prop = document_property();

                state._fsp--;

                adaptor.addChild(root_0, prop.getTree());

                _last = (CommonTree) input.LT(1);
                char_literal14 =
                        (CommonTree) match(input, 29,
                                FOLLOW_29_in_set_document155);
                char_literal14_tree =
                        (CommonTree) adaptor.dupNode(char_literal14);

                adaptor.addChild(root_0, char_literal14_tree);

                // BELScriptWalker.g:122:61: (qv= QUOTED_VALUE |oi= OBJECT_IDENT )
                int alt3 = 2;
                int LA3_0 = input.LA(1);

                if ((LA3_0 == QUOTED_VALUE)) {
                    alt3 = 1;
                }
                else if ((LA3_0 == OBJECT_IDENT)) {
                    alt3 = 2;
                }
                else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 3, 0, input);

                    throw nvae;

                }
                switch (alt3) {
                case 1:
                // BELScriptWalker.g:122:62: qv= QUOTED_VALUE
                {
                    _last = (CommonTree) input.LT(1);
                    qv =
                            (CommonTree) match(input, QUOTED_VALUE,
                                    FOLLOW_QUOTED_VALUE_in_set_document160);
                    qv_tree = (CommonTree) adaptor.dupNode(qv);

                    adaptor.addChild(root_0, qv_tree);

                }
                    break;
                case 2:
                // BELScriptWalker.g:122:80: oi= OBJECT_IDENT
                {
                    _last = (CommonTree) input.LT(1);
                    oi =
                            (CommonTree) match(input, OBJECT_IDENT,
                                    FOLLOW_OBJECT_IDENT_in_set_document166);
                    oi_tree = (CommonTree) adaptor.dupNode(oi);

                    adaptor.addChild(root_0, oi_tree);

                }
                    break;

                }

                if (!annotationContext.isEmpty() || !stmtlist.isEmpty()) {
                    addError(new SetDocumentPropertiesFirstException(
                            dkt.getLine(), dkt.getCharPositionInLine()));
                }

                final String keywordValue;
                if (qv != null) {
                    keywordValue = qv.toString();
                } else if (oi != null) {
                    keywordValue = oi.toString();
                } else {
                    throw new IllegalStateException(
                            "Did not understand document keyword value, expecting quoted value or object identifier.");
                }

                docprop.put(prop.r, keywordValue);
                lastDocumentPropertyLocation = dkt.getLine();

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "set_document"

    public static class set_statement_group_return extends TreeRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "set_statement_group"
    // BELScriptWalker.g:141:1: set_statement_group : 'SET' STATEMENT_GROUP_KEYWORD '=' (qv= QUOTED_VALUE |oi= OBJECT_IDENT ) ;
    public final BELScriptWalker.set_statement_group_return
            set_statement_group() throws RecognitionException {
        BELScriptWalker.set_statement_group_return retval =
                new BELScriptWalker.set_statement_group_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree qv = null;
        CommonTree oi = null;
        CommonTree string_literal15 = null;
        CommonTree STATEMENT_GROUP_KEYWORD16 = null;
        CommonTree char_literal17 = null;

        CommonTree qv_tree = null;
        CommonTree oi_tree = null;
        CommonTree string_literal15_tree = null;
        CommonTree STATEMENT_GROUP_KEYWORD16_tree = null;
        CommonTree char_literal17_tree = null;

        try {
            // BELScriptWalker.g:141:20: ( 'SET' STATEMENT_GROUP_KEYWORD '=' (qv= QUOTED_VALUE |oi= OBJECT_IDENT ) )
            // BELScriptWalker.g:142:5: 'SET' STATEMENT_GROUP_KEYWORD '=' (qv= QUOTED_VALUE |oi= OBJECT_IDENT )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                string_literal15 =
                        (CommonTree) match(input, 47,
                                FOLLOW_47_in_set_statement_group185);
                string_literal15_tree =
                        (CommonTree) adaptor.dupNode(string_literal15);

                adaptor.addChild(root_0, string_literal15_tree);

                _last = (CommonTree) input.LT(1);
                STATEMENT_GROUP_KEYWORD16 =
                        (CommonTree) match(input, STATEMENT_GROUP_KEYWORD,
                                FOLLOW_STATEMENT_GROUP_KEYWORD_in_set_statement_group187);
                STATEMENT_GROUP_KEYWORD16_tree =
                        (CommonTree) adaptor.dupNode(STATEMENT_GROUP_KEYWORD16);

                adaptor.addChild(root_0, STATEMENT_GROUP_KEYWORD16_tree);

                _last = (CommonTree) input.LT(1);
                char_literal17 =
                        (CommonTree) match(input, 29,
                                FOLLOW_29_in_set_statement_group189);
                char_literal17_tree =
                        (CommonTree) adaptor.dupNode(char_literal17);

                adaptor.addChild(root_0, char_literal17_tree);

                // BELScriptWalker.g:142:39: (qv= QUOTED_VALUE |oi= OBJECT_IDENT )
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
                // BELScriptWalker.g:142:40: qv= QUOTED_VALUE
                {
                    _last = (CommonTree) input.LT(1);
                    qv =
                            (CommonTree) match(input, QUOTED_VALUE,
                                    FOLLOW_QUOTED_VALUE_in_set_statement_group194);
                    qv_tree = (CommonTree) adaptor.dupNode(qv);

                    adaptor.addChild(root_0, qv_tree);

                }
                    break;
                case 2:
                // BELScriptWalker.g:142:58: oi= OBJECT_IDENT
                {
                    _last = (CommonTree) input.LT(1);
                    oi =
                            (CommonTree) match(input, OBJECT_IDENT,
                                    FOLLOW_OBJECT_IDENT_in_set_statement_group200);
                    oi_tree = (CommonTree) adaptor.dupNode(oi);

                    adaptor.addChild(root_0, oi_tree);

                }
                    break;

                }

                final String name;
                if (qv != null) {
                    name = qv.toString();
                } else if (oi != null) {
                    name = oi.toString();
                } else {
                    throw new IllegalStateException(
                            "Did not understand statement group value, expecting quoted value or object identifier.");
                }

                activeStatementGroup = new BELStatementGroup(name);
                statementGroups.add(activeStatementGroup);

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "set_statement_group"

    public static class set_annotation_return extends TreeRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "set_annotation"
    // BELScriptWalker.g:157:1: set_annotation : 'SET' an= OBJECT_IDENT '=' (qv= QUOTED_VALUE |list= VALUE_LIST |oi= OBJECT_IDENT ) ;
    public final BELScriptWalker.set_annotation_return set_annotation()
            throws RecognitionException {
        BELScriptWalker.set_annotation_return retval =
                new BELScriptWalker.set_annotation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree an = null;
        CommonTree qv = null;
        CommonTree list = null;
        CommonTree oi = null;
        CommonTree string_literal18 = null;
        CommonTree char_literal19 = null;

        CommonTree an_tree = null;
        CommonTree qv_tree = null;
        CommonTree list_tree = null;
        CommonTree oi_tree = null;
        CommonTree string_literal18_tree = null;
        CommonTree char_literal19_tree = null;

        try {
            // BELScriptWalker.g:157:15: ( 'SET' an= OBJECT_IDENT '=' (qv= QUOTED_VALUE |list= VALUE_LIST |oi= OBJECT_IDENT ) )
            // BELScriptWalker.g:158:5: 'SET' an= OBJECT_IDENT '=' (qv= QUOTED_VALUE |list= VALUE_LIST |oi= OBJECT_IDENT )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                string_literal18 =
                        (CommonTree) match(input, 47,
                                FOLLOW_47_in_set_annotation219);
                string_literal18_tree =
                        (CommonTree) adaptor.dupNode(string_literal18);

                adaptor.addChild(root_0, string_literal18_tree);

                _last = (CommonTree) input.LT(1);
                an =
                        (CommonTree) match(input, OBJECT_IDENT,
                                FOLLOW_OBJECT_IDENT_in_set_annotation223);
                an_tree = (CommonTree) adaptor.dupNode(an);

                adaptor.addChild(root_0, an_tree);

                _last = (CommonTree) input.LT(1);
                char_literal19 =
                        (CommonTree) match(input, 29,
                                FOLLOW_29_in_set_annotation225);
                char_literal19_tree =
                        (CommonTree) adaptor.dupNode(char_literal19);

                adaptor.addChild(root_0, char_literal19_tree);

                // BELScriptWalker.g:158:31: (qv= QUOTED_VALUE |list= VALUE_LIST |oi= OBJECT_IDENT )
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
                // BELScriptWalker.g:158:32: qv= QUOTED_VALUE
                {
                    _last = (CommonTree) input.LT(1);
                    qv =
                            (CommonTree) match(input, QUOTED_VALUE,
                                    FOLLOW_QUOTED_VALUE_in_set_annotation230);
                    qv_tree = (CommonTree) adaptor.dupNode(qv);

                    adaptor.addChild(root_0, qv_tree);

                }
                    break;
                case 2:
                // BELScriptWalker.g:158:50: list= VALUE_LIST
                {
                    _last = (CommonTree) input.LT(1);
                    list =
                            (CommonTree) match(input, VALUE_LIST,
                                    FOLLOW_VALUE_LIST_in_set_annotation236);
                    list_tree = (CommonTree) adaptor.dupNode(list);

                    adaptor.addChild(root_0, list_tree);

                }
                    break;
                case 3:
                // BELScriptWalker.g:158:68: oi= OBJECT_IDENT
                {
                    _last = (CommonTree) input.LT(1);
                    oi =
                            (CommonTree) match(input, OBJECT_IDENT,
                                    FOLLOW_OBJECT_IDENT_in_set_annotation242);
                    oi_tree = (CommonTree) adaptor.dupNode(oi);

                    adaptor.addChild(root_0, oi_tree);

                }
                    break;

                }

                final String name = an.getText();

                BELAnnotationDefinition ad = definedAnnotations.get(name);
                if (ad != null) {
                    // read annotation value
                    final BELAnnotation annotation;
                    if (qv != null) {
                        annotation = new BELAnnotation(ad, qv.getText());
                    } else if (oi != null) {
                        annotation = new BELAnnotation(ad, oi.getText());
                    } else {
                        if (list == null) {
                            throw new IllegalStateException(
                                    "Did not understand annotation value, expecting annotation list form.");
                        }

                        String listvalues = list.getText();
                        listvalues =
                                listvalues
                                        .substring(1, listvalues.length() - 1);
                        annotation =
                                new BELAnnotation(ad, Arrays.asList(ParserUtil
                                        .parseListRecord(listvalues)));
                    }

                    if (activeStatementGroup != null) {
                        // add to local statement group scope
                        sgAnnotationContext.put(name, annotation);
                    } else {
                        // add to main statement group scope
                        annotationContext.put(name, annotation);
                    }
                } else if (!name.equals("Citation") && !name.equals("Evidence")) {
                    // throw if annotation is not defined and it's not the intrinsics: Citation or EvidenceLine
                    addError(new DefineAnnotationBeforeUsageException(
                            an.getLine(), an.getCharPositionInLine()));
                }

                if (name.equals("Citation")) {
                    // redefinition of citation so clear out citation context
                    citationContext = null;

                    if (list == null) {
                        addError(new InvalidCitationException(an.getLine(),
                                an.getCharPositionInLine()));
                    } else {
                        String listvalues = list.getText();
                        String[] tokens =
                                ParserUtil.parseListRecord(listvalues);

                        String type = null;
                        String cname = null;
                        String reference = null;
                        Date publicationDate = null;
                        String[] authors = null;
                        String comment = null;

                        // (required) parse type
                        if (tokens.length > 0 && tokens[0] != null) {
                            type = tokens[0];
                            if (!("Book".equals(type) || "Journal".equals(type)
                                    || "Online Resource".equals(type)
                                    || "Other".equals(type) || "PubMed"
                                        .equals(type))) {
                                addError(new InvalidCitationException(
                                        an.getLine(),
                                        an.getCharPositionInLine()));
                            }
                        } else {
                            addError(new InvalidCitationException(an.getLine(),
                                    an.getCharPositionInLine()));
                        }

                        // (required) parse name
                        if (tokens.length > 1 && tokens[1] != null) {
                            if ("".equals(tokens[1].trim())) {
                                addError(new InvalidCitationException(
                                        an.getLine(),
                                        an.getCharPositionInLine()));
                            } else {
                                cname = tokens[1];
                            }
                        } else {
                            addError(new InvalidCitationException(an.getLine(),
                                    an.getCharPositionInLine()));
                        }

                        // (required) parse reference
                        if (tokens.length > 2 && tokens[2] != null) {
                            if ("".equals(tokens[2].trim())) {
                                addError(new InvalidCitationException(
                                        an.getLine(),
                                        an.getCharPositionInLine()));
                            } else {
                                reference = tokens[2];
                            }
                        }

                        // (optional) parse date of publication
                        if (tokens.length > 3 && tokens[3] != null) {
                            if (!"".equals(tokens[3].trim())) {
                                try {
                                    publicationDate =
                                            iso8601DateFormat.parse(tokens[3]);
                                } catch (ParseException e) {
                                    addError(new InvalidCitationException(
                                            an.getLine(),
                                            an.getCharPositionInLine()));
                                }

                                if (publicationDate == null) {
                                    addError(new InvalidCitationException(
                                            an.getLine(),
                                            an.getCharPositionInLine()));
                                }
                            }
                        }

                        // (optional) parse authors
                        if (tokens.length > 4 && tokens[4] != null) {
                            authors = ParserUtil.parseValueSeparated(tokens[4]);
                        }

                        // (optional) parse comments
                        if (tokens.length > 5 && tokens[5] != null) {
                            comment = tokens[5];
                        }

                        citationContext =
                                new BELCitation(type, cname, publicationDate,
                                        reference, authors == null ? null
                                                : Arrays.asList(authors),
                                        comment);
                    }
                } else if (name.equals("Evidence")) {
                    // redefinition of evidence so clear out evidence context
                    evidenceContext = null;

                    if (qv == null || "".equals(qv.getText().trim())) {
                        addError(new InvalidEvidenceException(an.getLine(),
                                an.getCharPositionInLine()));
                    } else {
                        evidenceContext = new BELEvidence(qv.getText());
                    }
                }

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "set_annotation"

    public static class unset_statement_group_return extends
            TreeRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "unset_statement_group"
    // BELScriptWalker.g:278:1: unset_statement_group : 'UNSET' STATEMENT_GROUP_KEYWORD ;
    public final BELScriptWalker.unset_statement_group_return
            unset_statement_group() throws RecognitionException {
        BELScriptWalker.unset_statement_group_return retval =
                new BELScriptWalker.unset_statement_group_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree string_literal20 = null;
        CommonTree STATEMENT_GROUP_KEYWORD21 = null;

        CommonTree string_literal20_tree = null;
        CommonTree STATEMENT_GROUP_KEYWORD21_tree = null;

        try {
            // BELScriptWalker.g:278:22: ( 'UNSET' STATEMENT_GROUP_KEYWORD )
            // BELScriptWalker.g:279:5: 'UNSET' STATEMENT_GROUP_KEYWORD
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                string_literal20 =
                        (CommonTree) match(input, 48,
                                FOLLOW_48_in_unset_statement_group261);
                string_literal20_tree =
                        (CommonTree) adaptor.dupNode(string_literal20);

                adaptor.addChild(root_0, string_literal20_tree);

                _last = (CommonTree) input.LT(1);
                STATEMENT_GROUP_KEYWORD21 =
                        (CommonTree) match(input, STATEMENT_GROUP_KEYWORD,
                                FOLLOW_STATEMENT_GROUP_KEYWORD_in_unset_statement_group263);
                STATEMENT_GROUP_KEYWORD21_tree =
                        (CommonTree) adaptor.dupNode(STATEMENT_GROUP_KEYWORD21);

                adaptor.addChild(root_0, STATEMENT_GROUP_KEYWORD21_tree);

                activeStatementGroup = null;
                sgAnnotationContext.clear();

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "unset_statement_group"

    public static class unset_return extends TreeRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "unset"
    // BELScriptWalker.g:285:1: unset : 'UNSET' (an= OBJECT_IDENT |list= IDENT_LIST ) ;
    public final BELScriptWalker.unset_return unset()
            throws RecognitionException {
        BELScriptWalker.unset_return retval =
                new BELScriptWalker.unset_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree an = null;
        CommonTree list = null;
        CommonTree string_literal22 = null;

        CommonTree an_tree = null;
        CommonTree list_tree = null;
        CommonTree string_literal22_tree = null;

        try {
            // BELScriptWalker.g:285:6: ( 'UNSET' (an= OBJECT_IDENT |list= IDENT_LIST ) )
            // BELScriptWalker.g:286:5: 'UNSET' (an= OBJECT_IDENT |list= IDENT_LIST )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                string_literal22 =
                        (CommonTree) match(input, 48, FOLLOW_48_in_unset281);
                string_literal22_tree =
                        (CommonTree) adaptor.dupNode(string_literal22);

                adaptor.addChild(root_0, string_literal22_tree);

                // BELScriptWalker.g:286:13: (an= OBJECT_IDENT |list= IDENT_LIST )
                int alt6 = 2;
                int LA6_0 = input.LA(1);

                if ((LA6_0 == OBJECT_IDENT)) {
                    alt6 = 1;
                }
                else if ((LA6_0 == IDENT_LIST)) {
                    alt6 = 2;
                }
                else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 6, 0, input);

                    throw nvae;

                }
                switch (alt6) {
                case 1:
                // BELScriptWalker.g:286:14: an= OBJECT_IDENT
                {
                    _last = (CommonTree) input.LT(1);
                    an =
                            (CommonTree) match(input, OBJECT_IDENT,
                                    FOLLOW_OBJECT_IDENT_in_unset286);
                    an_tree = (CommonTree) adaptor.dupNode(an);

                    adaptor.addChild(root_0, an_tree);

                }
                    break;
                case 2:
                // BELScriptWalker.g:286:32: list= IDENT_LIST
                {
                    _last = (CommonTree) input.LT(1);
                    list =
                            (CommonTree) match(input, IDENT_LIST,
                                    FOLLOW_IDENT_LIST_in_unset292);
                    list_tree = (CommonTree) adaptor.dupNode(list);

                    adaptor.addChild(root_0, list_tree);

                }
                    break;

                }

                if (an != null) {
                    String annotationName = an.getText();
                    if (definedAnnotations.containsKey(annotationName)) {
                        annotationContext.remove(annotationName);
                    } else if (docprop.containsKey(BELDocumentProperty
                            .getDocumentProperty(annotationName))) {
                        addError(new UnsetDocumentPropertiesException(
                                an.getLine(), an.getCharPositionInLine()));
                    } else {
                        addWarning(new UnsetUndefinedAnnotationException(
                                an.getLine(), an.getCharPositionInLine()));
                    }
                }

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "unset"

    public static class define_namespace_return extends TreeRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "define_namespace"
    // BELScriptWalker.g:300:1: define_namespace : ( 'DEFINE' ( (isdefault= 'DEFAULT' )? 'NAMESPACE' ) ) name= OBJECT_IDENT 'AS' 'URL' rloc= QUOTED_VALUE ;
    public final BELScriptWalker.define_namespace_return define_namespace()
            throws RecognitionException {
        BELScriptWalker.define_namespace_return retval =
                new BELScriptWalker.define_namespace_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree isdefault = null;
        CommonTree name = null;
        CommonTree rloc = null;
        CommonTree string_literal23 = null;
        CommonTree string_literal24 = null;
        CommonTree string_literal25 = null;
        CommonTree string_literal26 = null;

        CommonTree isdefault_tree = null;
        CommonTree name_tree = null;
        CommonTree rloc_tree = null;
        CommonTree string_literal23_tree = null;
        CommonTree string_literal24_tree = null;
        CommonTree string_literal25_tree = null;
        CommonTree string_literal26_tree = null;

        try {
            // BELScriptWalker.g:300:17: ( ( 'DEFINE' ( (isdefault= 'DEFAULT' )? 'NAMESPACE' ) ) name= OBJECT_IDENT 'AS' 'URL' rloc= QUOTED_VALUE )
            // BELScriptWalker.g:301:5: ( 'DEFINE' ( (isdefault= 'DEFAULT' )? 'NAMESPACE' ) ) name= OBJECT_IDENT 'AS' 'URL' rloc= QUOTED_VALUE
            {
                root_0 = (CommonTree) adaptor.nil();

                // BELScriptWalker.g:301:5: ( 'DEFINE' ( (isdefault= 'DEFAULT' )? 'NAMESPACE' ) )
                // BELScriptWalker.g:301:6: 'DEFINE' ( (isdefault= 'DEFAULT' )? 'NAMESPACE' )
                {
                    _last = (CommonTree) input.LT(1);
                    string_literal23 =
                            (CommonTree) match(input, 39,
                                    FOLLOW_39_in_define_namespace312);
                    string_literal23_tree =
                            (CommonTree) adaptor.dupNode(string_literal23);

                    adaptor.addChild(root_0, string_literal23_tree);

                    // BELScriptWalker.g:301:15: ( (isdefault= 'DEFAULT' )? 'NAMESPACE' )
                    // BELScriptWalker.g:301:16: (isdefault= 'DEFAULT' )? 'NAMESPACE'
                    {
                        // BELScriptWalker.g:301:16: (isdefault= 'DEFAULT' )?
                        int alt7 = 2;
                        int LA7_0 = input.LA(1);

                        if ((LA7_0 == 38)) {
                            alt7 = 1;
                        }
                        switch (alt7) {
                        case 1:
                        // BELScriptWalker.g:301:17: isdefault= 'DEFAULT'
                        {
                            _last = (CommonTree) input.LT(1);
                            isdefault =
                                    (CommonTree) match(input, 38,
                                            FOLLOW_38_in_define_namespace318);
                            isdefault_tree =
                                    (CommonTree) adaptor.dupNode(isdefault);

                            adaptor.addChild(root_0, isdefault_tree);

                        }
                            break;

                        }

                        _last = (CommonTree) input.LT(1);
                        string_literal24 =
                                (CommonTree) match(input, 44,
                                        FOLLOW_44_in_define_namespace322);
                        string_literal24_tree =
                                (CommonTree) adaptor.dupNode(string_literal24);

                        adaptor.addChild(root_0, string_literal24_tree);

                    }

                }

                _last = (CommonTree) input.LT(1);
                name =
                        (CommonTree) match(input, OBJECT_IDENT,
                                FOLLOW_OBJECT_IDENT_in_define_namespace328);
                name_tree = (CommonTree) adaptor.dupNode(name);

                adaptor.addChild(root_0, name_tree);

                _last = (CommonTree) input.LT(1);
                string_literal25 =
                        (CommonTree) match(input, 34,
                                FOLLOW_34_in_define_namespace330);
                string_literal25_tree =
                        (CommonTree) adaptor.dupNode(string_literal25);

                adaptor.addChild(root_0, string_literal25_tree);

                _last = (CommonTree) input.LT(1);
                string_literal26 =
                        (CommonTree) match(input, 49,
                                FOLLOW_49_in_define_namespace332);
                string_literal26_tree =
                        (CommonTree) adaptor.dupNode(string_literal26);

                adaptor.addChild(root_0, string_literal26_tree);

                _last = (CommonTree) input.LT(1);
                rloc =
                        (CommonTree) match(input, QUOTED_VALUE,
                                FOLLOW_QUOTED_VALUE_in_define_namespace336);
                rloc_tree = (CommonTree) adaptor.dupNode(rloc);

                adaptor.addChild(root_0, rloc_tree);

                final String nametext = name.getText();
                final String rloctext = rloc.getText();

                BELNamespaceDefinition belnsd;
                if (isdefault != null) {
                    belnsd =
                            new BELNamespaceDefinition(nametext, rloctext, true);
                } else {
                    belnsd =
                            new BELNamespaceDefinition(nametext, rloctext,
                                    false);
                }

                nslist.add(belnsd);
                definedNamespaces.put(nametext, belnsd);

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "define_namespace"

    public static class define_annotation_return extends TreeRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "define_annotation"
    // BELScriptWalker.g:317:1: define_annotation : ( 'DEFINE' 'ANNOTATION' ) name= OBJECT_IDENT 'AS' ( ( (type= 'URL' |type= 'PATTERN' ) value= QUOTED_VALUE ) | (type= 'LIST' value= VALUE_LIST ) ) ;
    public final BELScriptWalker.define_annotation_return define_annotation()
            throws RecognitionException {
        BELScriptWalker.define_annotation_return retval =
                new BELScriptWalker.define_annotation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree name = null;
        CommonTree type = null;
        CommonTree value = null;
        CommonTree string_literal27 = null;
        CommonTree string_literal28 = null;
        CommonTree string_literal29 = null;

        CommonTree name_tree = null;
        CommonTree type_tree = null;
        CommonTree value_tree = null;
        CommonTree string_literal27_tree = null;
        CommonTree string_literal28_tree = null;
        CommonTree string_literal29_tree = null;

        try {
            // BELScriptWalker.g:317:18: ( ( 'DEFINE' 'ANNOTATION' ) name= OBJECT_IDENT 'AS' ( ( (type= 'URL' |type= 'PATTERN' ) value= QUOTED_VALUE ) | (type= 'LIST' value= VALUE_LIST ) ) )
            // BELScriptWalker.g:318:5: ( 'DEFINE' 'ANNOTATION' ) name= OBJECT_IDENT 'AS' ( ( (type= 'URL' |type= 'PATTERN' ) value= QUOTED_VALUE ) | (type= 'LIST' value= VALUE_LIST ) )
            {
                root_0 = (CommonTree) adaptor.nil();

                // BELScriptWalker.g:318:5: ( 'DEFINE' 'ANNOTATION' )
                // BELScriptWalker.g:318:6: 'DEFINE' 'ANNOTATION'
                {
                    _last = (CommonTree) input.LT(1);
                    string_literal27 =
                            (CommonTree) match(input, 39,
                                    FOLLOW_39_in_define_annotation355);
                    string_literal27_tree =
                            (CommonTree) adaptor.dupNode(string_literal27);

                    adaptor.addChild(root_0, string_literal27_tree);

                    _last = (CommonTree) input.LT(1);
                    string_literal28 =
                            (CommonTree) match(input, 33,
                                    FOLLOW_33_in_define_annotation357);
                    string_literal28_tree =
                            (CommonTree) adaptor.dupNode(string_literal28);

                    adaptor.addChild(root_0, string_literal28_tree);

                }

                _last = (CommonTree) input.LT(1);
                name =
                        (CommonTree) match(input, OBJECT_IDENT,
                                FOLLOW_OBJECT_IDENT_in_define_annotation362);
                name_tree = (CommonTree) adaptor.dupNode(name);

                adaptor.addChild(root_0, name_tree);

                _last = (CommonTree) input.LT(1);
                string_literal29 =
                        (CommonTree) match(input, 34,
                                FOLLOW_34_in_define_annotation364);
                string_literal29_tree =
                        (CommonTree) adaptor.dupNode(string_literal29);

                adaptor.addChild(root_0, string_literal29_tree);

                // BELScriptWalker.g:318:52: ( ( (type= 'URL' |type= 'PATTERN' ) value= QUOTED_VALUE ) | (type= 'LIST' value= VALUE_LIST ) )
                int alt9 = 2;
                int LA9_0 = input.LA(1);

                if ((LA9_0 == 46 || LA9_0 == 49)) {
                    alt9 = 1;
                }
                else if ((LA9_0 == 42)) {
                    alt9 = 2;
                }
                else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 9, 0, input);

                    throw nvae;

                }
                switch (alt9) {
                case 1:
                // BELScriptWalker.g:318:53: ( (type= 'URL' |type= 'PATTERN' ) value= QUOTED_VALUE )
                {
                    // BELScriptWalker.g:318:53: ( (type= 'URL' |type= 'PATTERN' ) value= QUOTED_VALUE )
                    // BELScriptWalker.g:318:54: (type= 'URL' |type= 'PATTERN' ) value= QUOTED_VALUE
                    {
                        // BELScriptWalker.g:318:54: (type= 'URL' |type= 'PATTERN' )
                        int alt8 = 2;
                        int LA8_0 = input.LA(1);

                        if ((LA8_0 == 49)) {
                            alt8 = 1;
                        }
                        else if ((LA8_0 == 46)) {
                            alt8 = 2;
                        }
                        else {
                            NoViableAltException nvae =
                                    new NoViableAltException("", 8, 0, input);

                            throw nvae;

                        }
                        switch (alt8) {
                        case 1:
                        // BELScriptWalker.g:318:55: type= 'URL'
                        {
                            _last = (CommonTree) input.LT(1);
                            type =
                                    (CommonTree) match(input, 49,
                                            FOLLOW_49_in_define_annotation371);
                            type_tree = (CommonTree) adaptor.dupNode(type);

                            adaptor.addChild(root_0, type_tree);

                        }
                            break;
                        case 2:
                        // BELScriptWalker.g:318:68: type= 'PATTERN'
                        {
                            _last = (CommonTree) input.LT(1);
                            type =
                                    (CommonTree) match(input, 46,
                                            FOLLOW_46_in_define_annotation377);
                            type_tree = (CommonTree) adaptor.dupNode(type);

                            adaptor.addChild(root_0, type_tree);

                        }
                            break;

                        }

                        _last = (CommonTree) input.LT(1);
                        value =
                                (CommonTree) match(input, QUOTED_VALUE,
                                        FOLLOW_QUOTED_VALUE_in_define_annotation382);
                        value_tree = (CommonTree) adaptor.dupNode(value);

                        adaptor.addChild(root_0, value_tree);

                    }

                }
                    break;
                case 2:
                // BELScriptWalker.g:318:106: (type= 'LIST' value= VALUE_LIST )
                {
                    // BELScriptWalker.g:318:106: (type= 'LIST' value= VALUE_LIST )
                    // BELScriptWalker.g:318:107: type= 'LIST' value= VALUE_LIST
                    {
                        _last = (CommonTree) input.LT(1);
                        type =
                                (CommonTree) match(input, 42,
                                        FOLLOW_42_in_define_annotation390);
                        type_tree = (CommonTree) adaptor.dupNode(type);

                        adaptor.addChild(root_0, type_tree);

                        _last = (CommonTree) input.LT(1);
                        value =
                                (CommonTree) match(input, VALUE_LIST,
                                        FOLLOW_VALUE_LIST_in_define_annotation394);
                        value_tree = (CommonTree) adaptor.dupNode(value);

                        adaptor.addChild(root_0, value_tree);

                    }

                }
                    break;

                }

                final String nametext = name.getText();

                if (type != null && value != null) {
                    final String typetext = type.toString();
                    String valuetext = value.toString();
                    BELAnnotationType atype;

                    BELAnnotationDefinition ad;
                    if ("URL".equals(typetext)) {
                        atype = BELAnnotationType.URL;
                        ad =
                                new BELAnnotationDefinition(nametext, atype,
                                        valuetext);
                    } else if ("PATTERN".equals(typetext)) {
                        atype = BELAnnotationType.PATTERN;
                        ad =
                                new BELAnnotationDefinition(nametext, atype,
                                        valuetext);
                    } else {
                        atype = BELAnnotationType.LIST;
                        valuetext =
                                valuetext.substring(1, valuetext.length() - 1);
                        ad =
                                new BELAnnotationDefinition(nametext, atype,
                                        Arrays.asList(ParserUtil
                                                .parseListRecord(valuetext)));
                    }

                    adlist.add(ad);
                    definedAnnotations.put(nametext, ad);
                }

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "define_annotation"

    public static class document_property_return extends TreeRuleReturnScope {
        public BELDocumentProperty r;
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "document_property"
    // BELScriptWalker.g:345:1: document_property returns [BELDocumentProperty r] : (pv= 'Authors' |pv= 'ContactInfo' |pv= 'Copyright' |pv= 'Description' |pv= 'Disclaimer' |pv= 'Licenses' |pv= 'Name' |pv= 'Version' );
    public final BELScriptWalker.document_property_return document_property()
            throws RecognitionException {
        BELScriptWalker.document_property_return retval =
                new BELScriptWalker.document_property_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree pv = null;

        CommonTree pv_tree = null;

        try {
            // BELScriptWalker.g:345:50: (pv= 'Authors' |pv= 'ContactInfo' |pv= 'Copyright' |pv= 'Description' |pv= 'Disclaimer' |pv= 'Licenses' |pv= 'Name' |pv= 'Version' )
            int alt10 = 8;
            switch (input.LA(1)) {
            case 35: {
                alt10 = 1;
            }
                break;
            case 36: {
                alt10 = 2;
            }
                break;
            case 37: {
                alt10 = 3;
            }
                break;
            case 40: {
                alt10 = 4;
            }
                break;
            case 41: {
                alt10 = 5;
            }
                break;
            case 43: {
                alt10 = 6;
            }
                break;
            case 45: {
                alt10 = 7;
            }
                break;
            case 50: {
                alt10 = 8;
            }
                break;
            default:
                NoViableAltException nvae =
                        new NoViableAltException("", 10, 0, input);

                throw nvae;

            }

            switch (alt10) {
            case 1:
            // BELScriptWalker.g:346:5: pv= 'Authors'
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pv =
                        (CommonTree) match(input, 35,
                                FOLLOW_35_in_document_property420);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r =
                        BELDocumentProperty.getDocumentProperty(pv.getText());

            }
                break;
            case 2:
            // BELScriptWalker.g:347:5: pv= 'ContactInfo'
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pv =
                        (CommonTree) match(input, 36,
                                FOLLOW_36_in_document_property436);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r =
                        BELDocumentProperty.getDocumentProperty(pv.getText());

            }
                break;
            case 3:
            // BELScriptWalker.g:348:5: pv= 'Copyright'
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pv =
                        (CommonTree) match(input, 37,
                                FOLLOW_37_in_document_property448);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r =
                        BELDocumentProperty.getDocumentProperty(pv.getText());

            }
                break;
            case 4:
            // BELScriptWalker.g:349:5: pv= 'Description'
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pv =
                        (CommonTree) match(input, 40,
                                FOLLOW_40_in_document_property462);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r =
                        BELDocumentProperty.getDocumentProperty(pv.getText());

            }
                break;
            case 5:
            // BELScriptWalker.g:350:5: pv= 'Disclaimer'
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pv =
                        (CommonTree) match(input, 41,
                                FOLLOW_41_in_document_property474);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r =
                        BELDocumentProperty.getDocumentProperty(pv.getText());

            }
                break;
            case 6:
            // BELScriptWalker.g:351:5: pv= 'Licenses'
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pv =
                        (CommonTree) match(input, 43,
                                FOLLOW_43_in_document_property487);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r =
                        BELDocumentProperty.getDocumentProperty(pv.getText());

            }
                break;
            case 7:
            // BELScriptWalker.g:352:5: pv= 'Name'
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pv =
                        (CommonTree) match(input, 45,
                                FOLLOW_45_in_document_property502);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r =
                        BELDocumentProperty.getDocumentProperty(pv.getText());

            }
                break;
            case 8:
            // BELScriptWalker.g:353:5: pv= 'Version'
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pv =
                        (CommonTree) match(input, 50,
                                FOLLOW_50_in_document_property521);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r =
                        BELDocumentProperty.getDocumentProperty(pv.getText());

            }
                break;

            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "document_property"

    public static class statement_return extends TreeRuleReturnScope {
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "statement"
    // BELScriptWalker.g:356:1: statement : st= outer_term (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term ) )? (comment= STATEMENT_COMMENT )? ;
    public final BELScriptWalker.statement_return statement()
            throws RecognitionException {
        BELScriptWalker.statement_return retval =
                new BELScriptWalker.statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree comment = null;
        CommonTree OPEN_PAREN30 = null;
        CommonTree CLOSE_PAREN31 = null;
        BELScriptWalker.outer_term_return st = null;

        BELScriptWalker.relationship_return rel = null;

        BELScriptWalker.outer_term_return nst = null;

        BELScriptWalker.relationship_return nrel = null;

        BELScriptWalker.outer_term_return not = null;

        BELScriptWalker.outer_term_return ot = null;

        CommonTree comment_tree = null;
        CommonTree OPEN_PAREN30_tree = null;
        CommonTree CLOSE_PAREN31_tree = null;

        try {
            // BELScriptWalker.g:356:10: (st= outer_term (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term ) )? (comment= STATEMENT_COMMENT )? )
            // BELScriptWalker.g:357:5: st= outer_term (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term ) )? (comment= STATEMENT_COMMENT )?
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pushFollow(FOLLOW_outer_term_in_statement545);
                st = outer_term();

                state._fsp--;

                adaptor.addChild(root_0, st.getTree());

                // BELScriptWalker.g:357:19: (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term ) )?
                int alt12 = 2;
                int LA12_0 = input.LA(1);

                if (((LA12_0 >= 25 && LA12_0 <= 28)
                        || (LA12_0 >= 30 && LA12_0 <= 32)
                        || (LA12_0 >= 54 && LA12_0 <= 55) || LA12_0 == 57
                        || LA12_0 == 61 || LA12_0 == 70
                        || (LA12_0 >= 73 && LA12_0 <= 74)
                        || (LA12_0 >= 81 && LA12_0 <= 86)
                        || (LA12_0 >= 93 && LA12_0 <= 94) || LA12_0 == 103
                        || LA12_0 == 105 || LA12_0 == 109 || LA12_0 == 118
                        || LA12_0 == 123 || LA12_0 == 125)) {
                    alt12 = 1;
                }
                switch (alt12) {
                case 1:
                // BELScriptWalker.g:357:20: rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term )
                {
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_relationship_in_statement550);
                    rel = relationship();

                    state._fsp--;

                    adaptor.addChild(root_0, rel.getTree());

                    // BELScriptWalker.g:357:37: ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term )
                    int alt11 = 2;
                    int LA11_0 = input.LA(1);

                    if ((LA11_0 == OPEN_PAREN)) {
                        alt11 = 1;
                    }
                    else if (((LA11_0 >= 51 && LA11_0 <= 53) || LA11_0 == 56
                            || (LA11_0 >= 58 && LA11_0 <= 60)
                            || (LA11_0 >= 62 && LA11_0 <= 69)
                            || (LA11_0 >= 71 && LA11_0 <= 72)
                            || (LA11_0 >= 75 && LA11_0 <= 80)
                            || (LA11_0 >= 87 && LA11_0 <= 92)
                            || (LA11_0 >= 95 && LA11_0 <= 102) || LA11_0 == 104
                            || (LA11_0 >= 106 && LA11_0 <= 108)
                            || (LA11_0 >= 110 && LA11_0 <= 117)
                            || (LA11_0 >= 119 && LA11_0 <= 122)
                            || LA11_0 == 124 || (LA11_0 >= 126 && LA11_0 <= 130))) {
                        alt11 = 2;
                    }
                    else {
                        NoViableAltException nvae =
                                new NoViableAltException("", 11, 0, input);

                        throw nvae;

                    }
                    switch (alt11) {
                    case 1:
                    // BELScriptWalker.g:357:38: ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN )
                    {
                        // BELScriptWalker.g:357:38: ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN )
                        // BELScriptWalker.g:357:39: OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN
                        {
                            _last = (CommonTree) input.LT(1);
                            OPEN_PAREN30 =
                                    (CommonTree) match(input, OPEN_PAREN,
                                            FOLLOW_OPEN_PAREN_in_statement554);
                            OPEN_PAREN30_tree =
                                    (CommonTree) adaptor.dupNode(OPEN_PAREN30);

                            adaptor.addChild(root_0, OPEN_PAREN30_tree);

                            _last = (CommonTree) input.LT(1);
                            pushFollow(FOLLOW_outer_term_in_statement558);
                            nst = outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, nst.getTree());

                            _last = (CommonTree) input.LT(1);
                            pushFollow(FOLLOW_relationship_in_statement562);
                            nrel = relationship();

                            state._fsp--;

                            adaptor.addChild(root_0, nrel.getTree());

                            _last = (CommonTree) input.LT(1);
                            pushFollow(FOLLOW_outer_term_in_statement566);
                            not = outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, not.getTree());

                            _last = (CommonTree) input.LT(1);
                            CLOSE_PAREN31 =
                                    (CommonTree) match(input, CLOSE_PAREN,
                                            FOLLOW_CLOSE_PAREN_in_statement568);
                            CLOSE_PAREN31_tree =
                                    (CommonTree) adaptor.dupNode(CLOSE_PAREN31);

                            adaptor.addChild(root_0, CLOSE_PAREN31_tree);

                        }

                    }
                        break;
                    case 2:
                    // BELScriptWalker.g:357:113: ot= outer_term
                    {
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_outer_term_in_statement575);
                        ot = outer_term();

                        state._fsp--;

                        adaptor.addChild(root_0, ot.getTree());

                    }
                        break;

                    }

                }
                    break;

                }

                // BELScriptWalker.g:357:137: (comment= STATEMENT_COMMENT )?
                int alt13 = 2;
                int LA13_0 = input.LA(1);

                if ((LA13_0 == STATEMENT_COMMENT)) {
                    alt13 = 1;
                }
                switch (alt13) {
                case 1:
                // BELScriptWalker.g:357:137: comment= STATEMENT_COMMENT
                {
                    _last = (CommonTree) input.LT(1);
                    comment =
                            (CommonTree) match(input, STATEMENT_COMMENT,
                                    FOLLOW_STATEMENT_COMMENT_in_statement582);
                    comment_tree = (CommonTree) adaptor.dupNode(comment);

                    adaptor.addChild(root_0, comment_tree);

                }
                    break;

                }

                final StringBuilder stmtBuilder = new StringBuilder();
                stmtBuilder.append(st.r);

                if (rel != null) {
                    stmtBuilder.append(" ").append(rel.r);

                    if (ot != null) {
                        stmtBuilder.append(" ").append(ot.r);
                    } else {
                        stmtBuilder.append("(");

                        if (nst != null && nrel != null && not != null) {
                            stmtBuilder.append(nst.r).append(" ")
                                    .append(nrel.r).append(" ").append(not.r);
                        }
                        stmtBuilder.append(")");
                    }
                }

                String commentText = null;
                if (comment != null) {
                    commentText = comment.getText();
                }

                // build effective annotations from main statement group context and then local statement group context, if any
                final Map<String, BELAnnotation> effectiveAnnotations =
                        new LinkedHashMap<String, BELAnnotation>(
                                annotationContext);
                if (activeStatementGroup != null) {
                    effectiveAnnotations.putAll(sgAnnotationContext);
                }

                final List<BELAnnotation> annotations =
                        new ArrayList<BELAnnotation>(
                                effectiveAnnotations.values());

                // build statement and keep track of it for validation purposes
                final BELStatement stmt =
                        new BELStatement(stmtBuilder.toString(), annotations,
                                citationContext, evidenceContext, commentText);
                stmtlist.add(stmt);

                // add statement to scoped statement group
                if (activeStatementGroup != null) {
                    activeStatementGroup.getStatements().add(stmt);
                } else {
                    documentStatementGroup.getStatements().add(stmt);
                }

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "statement"

    public static class outer_term_return extends TreeRuleReturnScope {
        public String r;
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "outer_term"
    // BELScriptWalker.g:402:1: outer_term returns [String r] : f= function op= OPEN_PAREN ( (c= ',' )? a= argument )* cp= CLOSE_PAREN ;
    public final BELScriptWalker.outer_term_return outer_term()
            throws RecognitionException {
        BELScriptWalker.outer_term_return retval =
                new BELScriptWalker.outer_term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree op = null;
        CommonTree c = null;
        CommonTree cp = null;
        BELScriptWalker.function_return f = null;

        BELScriptWalker.argument_return a = null;

        CommonTree op_tree = null;
        CommonTree c_tree = null;
        CommonTree cp_tree = null;

        final StringBuilder tBuilder = new StringBuilder();

        try {
            // BELScriptWalker.g:406:5: (f= function op= OPEN_PAREN ( (c= ',' )? a= argument )* cp= CLOSE_PAREN )
            // BELScriptWalker.g:407:5: f= function op= OPEN_PAREN ( (c= ',' )? a= argument )* cp= CLOSE_PAREN
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pushFollow(FOLLOW_function_in_outer_term613);
                f = function();

                state._fsp--;

                adaptor.addChild(root_0, f.getTree());

                tBuilder.append(f.r);

                _last = (CommonTree) input.LT(1);
                op =
                        (CommonTree) match(input, OPEN_PAREN,
                                FOLLOW_OPEN_PAREN_in_outer_term619);
                op_tree = (CommonTree) adaptor.dupNode(op);

                adaptor.addChild(root_0, op_tree);

                tBuilder.append(op.getText());

                // BELScriptWalker.g:411:7: ( (c= ',' )? a= argument )*
                loop15: do {
                    int alt15 = 2;
                    int LA15_0 = input.LA(1);

                    if (((LA15_0 >= NS_PREFIX && LA15_0 <= OBJECT_IDENT)
                            || LA15_0 == QUOTED_VALUE || LA15_0 == 24
                            || (LA15_0 >= 51 && LA15_0 <= 53) || LA15_0 == 56
                            || (LA15_0 >= 58 && LA15_0 <= 60)
                            || (LA15_0 >= 62 && LA15_0 <= 69)
                            || (LA15_0 >= 71 && LA15_0 <= 72)
                            || (LA15_0 >= 75 && LA15_0 <= 80)
                            || (LA15_0 >= 87 && LA15_0 <= 92)
                            || (LA15_0 >= 95 && LA15_0 <= 102) || LA15_0 == 104
                            || (LA15_0 >= 106 && LA15_0 <= 108)
                            || (LA15_0 >= 110 && LA15_0 <= 117)
                            || (LA15_0 >= 119 && LA15_0 <= 122)
                            || LA15_0 == 124 || (LA15_0 >= 126 && LA15_0 <= 130))) {
                        alt15 = 1;
                    }

                    switch (alt15) {
                    case 1:
                    // BELScriptWalker.g:411:8: (c= ',' )? a= argument
                    {
                        // BELScriptWalker.g:411:9: (c= ',' )?
                        int alt14 = 2;
                        int LA14_0 = input.LA(1);

                        if ((LA14_0 == 24)) {
                            alt14 = 1;
                        }
                        switch (alt14) {
                        case 1:
                        // BELScriptWalker.g:411:9: c= ','
                        {
                            _last = (CommonTree) input.LT(1);
                            c =
                                    (CommonTree) match(input, 24,
                                            FOLLOW_24_in_outer_term626);
                            c_tree = (CommonTree) adaptor.dupNode(c);

                            adaptor.addChild(root_0, c_tree);

                        }
                            break;

                        }

                        if (c != null) {
                            tBuilder.append(c.getText());
                        }

                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_argument_in_outer_term633);
                        a = argument();

                        state._fsp--;

                        adaptor.addChild(root_0, a.getTree());

                        tBuilder.append(a.r);

                    }
                        break;

                    default:
                        break loop15;
                    }
                } while (true);

                _last = (CommonTree) input.LT(1);
                cp =
                        (CommonTree) match(input, CLOSE_PAREN,
                                FOLLOW_CLOSE_PAREN_in_outer_term641);
                cp_tree = (CommonTree) adaptor.dupNode(cp);

                adaptor.addChild(root_0, cp_tree);

                tBuilder.append(cp.getText());
                retval.r = tBuilder.toString();

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "outer_term"

    public static class argument_return extends TreeRuleReturnScope {
        public String r;
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "argument"
    // BELScriptWalker.g:423:1: argument returns [String r] : (p= param |t= term );
    public final BELScriptWalker.argument_return argument()
            throws RecognitionException {
        BELScriptWalker.argument_return retval =
                new BELScriptWalker.argument_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        BELScriptWalker.param_return p = null;

        BELScriptWalker.term_return t = null;

        try {
            // BELScriptWalker.g:423:28: (p= param |t= term )
            int alt16 = 2;
            int LA16_0 = input.LA(1);

            if (((LA16_0 >= NS_PREFIX && LA16_0 <= OBJECT_IDENT) || LA16_0 == QUOTED_VALUE)) {
                alt16 = 1;
            }
            else if (((LA16_0 >= 51 && LA16_0 <= 53) || LA16_0 == 56
                    || (LA16_0 >= 58 && LA16_0 <= 60)
                    || (LA16_0 >= 62 && LA16_0 <= 69)
                    || (LA16_0 >= 71 && LA16_0 <= 72)
                    || (LA16_0 >= 75 && LA16_0 <= 80)
                    || (LA16_0 >= 87 && LA16_0 <= 92)
                    || (LA16_0 >= 95 && LA16_0 <= 102) || LA16_0 == 104
                    || (LA16_0 >= 106 && LA16_0 <= 108)
                    || (LA16_0 >= 110 && LA16_0 <= 117)
                    || (LA16_0 >= 119 && LA16_0 <= 122) || LA16_0 == 124 || (LA16_0 >= 126 && LA16_0 <= 130))) {
                alt16 = 2;
            }
            else {
                NoViableAltException nvae =
                        new NoViableAltException("", 16, 0, input);

                throw nvae;

            }
            switch (alt16) {
            case 1:
            // BELScriptWalker.g:424:5: p= param
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pushFollow(FOLLOW_param_in_argument665);
                p = param();

                state._fsp--;

                adaptor.addChild(root_0, p.getTree());

                retval.r = p.r;

            }
                break;
            case 2:
            // BELScriptWalker.g:427:5: t= term
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pushFollow(FOLLOW_term_in_argument677);
                t = term();

                state._fsp--;

                adaptor.addChild(root_0, t.getTree());

                retval.r = t.r;

            }
                break;

            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "argument"

    public static class term_return extends TreeRuleReturnScope {
        public String r;
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "term"
    // BELScriptWalker.g:432:1: term returns [String r] : f= function op= OPEN_PAREN ( (c= ',' )? (t= term |p= param ) )* cp= CLOSE_PAREN ;
    public final BELScriptWalker.term_return term() throws RecognitionException {
        BELScriptWalker.term_return retval = new BELScriptWalker.term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree op = null;
        CommonTree c = null;
        CommonTree cp = null;
        BELScriptWalker.function_return f = null;

        BELScriptWalker.term_return t = null;

        BELScriptWalker.param_return p = null;

        CommonTree op_tree = null;
        CommonTree c_tree = null;
        CommonTree cp_tree = null;

        final StringBuilder termBuilder = new StringBuilder();

        try {
            // BELScriptWalker.g:436:5: (f= function op= OPEN_PAREN ( (c= ',' )? (t= term |p= param ) )* cp= CLOSE_PAREN )
            // BELScriptWalker.g:437:5: f= function op= OPEN_PAREN ( (c= ',' )? (t= term |p= param ) )* cp= CLOSE_PAREN
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                pushFollow(FOLLOW_function_in_term707);
                f = function();

                state._fsp--;

                adaptor.addChild(root_0, f.getTree());

                termBuilder.append(f.r);

                _last = (CommonTree) input.LT(1);
                op =
                        (CommonTree) match(input, OPEN_PAREN,
                                FOLLOW_OPEN_PAREN_in_term713);
                op_tree = (CommonTree) adaptor.dupNode(op);

                adaptor.addChild(root_0, op_tree);

                termBuilder.append(op.getText());

                // BELScriptWalker.g:441:6: ( (c= ',' )? (t= term |p= param ) )*
                loop19: do {
                    int alt19 = 2;
                    int LA19_0 = input.LA(1);

                    if (((LA19_0 >= NS_PREFIX && LA19_0 <= OBJECT_IDENT)
                            || LA19_0 == QUOTED_VALUE || LA19_0 == 24
                            || (LA19_0 >= 51 && LA19_0 <= 53) || LA19_0 == 56
                            || (LA19_0 >= 58 && LA19_0 <= 60)
                            || (LA19_0 >= 62 && LA19_0 <= 69)
                            || (LA19_0 >= 71 && LA19_0 <= 72)
                            || (LA19_0 >= 75 && LA19_0 <= 80)
                            || (LA19_0 >= 87 && LA19_0 <= 92)
                            || (LA19_0 >= 95 && LA19_0 <= 102) || LA19_0 == 104
                            || (LA19_0 >= 106 && LA19_0 <= 108)
                            || (LA19_0 >= 110 && LA19_0 <= 117)
                            || (LA19_0 >= 119 && LA19_0 <= 122)
                            || LA19_0 == 124 || (LA19_0 >= 126 && LA19_0 <= 130))) {
                        alt19 = 1;
                    }

                    switch (alt19) {
                    case 1:
                    // BELScriptWalker.g:441:7: (c= ',' )? (t= term |p= param )
                    {
                        // BELScriptWalker.g:441:8: (c= ',' )?
                        int alt17 = 2;
                        int LA17_0 = input.LA(1);

                        if ((LA17_0 == 24)) {
                            alt17 = 1;
                        }
                        switch (alt17) {
                        case 1:
                        // BELScriptWalker.g:441:8: c= ','
                        {
                            _last = (CommonTree) input.LT(1);
                            c =
                                    (CommonTree) match(input, 24,
                                            FOLLOW_24_in_term719);
                            c_tree = (CommonTree) adaptor.dupNode(c);

                            adaptor.addChild(root_0, c_tree);

                        }
                            break;

                        }

                        if (c != null) {
                            termBuilder.append(c.getText());
                        }

                        // BELScriptWalker.g:445:7: (t= term |p= param )
                        int alt18 = 2;
                        int LA18_0 = input.LA(1);

                        if (((LA18_0 >= 51 && LA18_0 <= 53) || LA18_0 == 56
                                || (LA18_0 >= 58 && LA18_0 <= 60)
                                || (LA18_0 >= 62 && LA18_0 <= 69)
                                || (LA18_0 >= 71 && LA18_0 <= 72)
                                || (LA18_0 >= 75 && LA18_0 <= 80)
                                || (LA18_0 >= 87 && LA18_0 <= 92)
                                || (LA18_0 >= 95 && LA18_0 <= 102)
                                || LA18_0 == 104
                                || (LA18_0 >= 106 && LA18_0 <= 108)
                                || (LA18_0 >= 110 && LA18_0 <= 117)
                                || (LA18_0 >= 119 && LA18_0 <= 122)
                                || LA18_0 == 124 || (LA18_0 >= 126 && LA18_0 <= 130))) {
                            alt18 = 1;
                        }
                        else if (((LA18_0 >= NS_PREFIX && LA18_0 <= OBJECT_IDENT) || LA18_0 == QUOTED_VALUE)) {
                            alt18 = 2;
                        }
                        else {
                            NoViableAltException nvae =
                                    new NoViableAltException("", 18, 0, input);

                            throw nvae;

                        }
                        switch (alt18) {
                        case 1:
                        // BELScriptWalker.g:445:8: t= term
                        {
                            _last = (CommonTree) input.LT(1);
                            pushFollow(FOLLOW_term_in_term727);
                            t = term();

                            state._fsp--;

                            adaptor.addChild(root_0, t.getTree());

                            termBuilder.append(t.r);

                        }
                            break;
                        case 2:
                        // BELScriptWalker.g:447:9: p= param
                        {
                            _last = (CommonTree) input.LT(1);
                            pushFollow(FOLLOW_param_in_term735);
                            p = param();

                            state._fsp--;

                            adaptor.addChild(root_0, p.getTree());

                            termBuilder.append(p.r);

                        }
                            break;

                        }

                    }
                        break;

                    default:
                        break loop19;
                    }
                } while (true);

                _last = (CommonTree) input.LT(1);
                cp =
                        (CommonTree) match(input, CLOSE_PAREN,
                                FOLLOW_CLOSE_PAREN_in_term744);
                cp_tree = (CommonTree) adaptor.dupNode(cp);

                adaptor.addChild(root_0, cp_tree);

                termBuilder.append(cp.getText());
                retval.r = termBuilder.toString();

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "term"

    public static class param_return extends TreeRuleReturnScope {
        public String r;
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "param"
    // BELScriptWalker.g:456:10: fragment param returns [String r] : (nsp= NS_PREFIX )? (id= OBJECT_IDENT |quo= QUOTED_VALUE ) ;
    public final BELScriptWalker.param_return param()
            throws RecognitionException {
        BELScriptWalker.param_return retval =
                new BELScriptWalker.param_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree nsp = null;
        CommonTree id = null;
        CommonTree quo = null;

        CommonTree nsp_tree = null;
        CommonTree id_tree = null;
        CommonTree quo_tree = null;

        try {
            // BELScriptWalker.g:456:34: ( (nsp= NS_PREFIX )? (id= OBJECT_IDENT |quo= QUOTED_VALUE ) )
            // BELScriptWalker.g:457:5: (nsp= NS_PREFIX )? (id= OBJECT_IDENT |quo= QUOTED_VALUE )
            {
                root_0 = (CommonTree) adaptor.nil();

                // BELScriptWalker.g:457:8: (nsp= NS_PREFIX )?
                int alt20 = 2;
                int LA20_0 = input.LA(1);

                if ((LA20_0 == NS_PREFIX)) {
                    alt20 = 1;
                }
                switch (alt20) {
                case 1:
                // BELScriptWalker.g:457:8: nsp= NS_PREFIX
                {
                    _last = (CommonTree) input.LT(1);
                    nsp =
                            (CommonTree) match(input, NS_PREFIX,
                                    FOLLOW_NS_PREFIX_in_param772);
                    nsp_tree = (CommonTree) adaptor.dupNode(nsp);

                    adaptor.addChild(root_0, nsp_tree);

                }
                    break;

                }

                // BELScriptWalker.g:457:20: (id= OBJECT_IDENT |quo= QUOTED_VALUE )
                int alt21 = 2;
                int LA21_0 = input.LA(1);

                if ((LA21_0 == OBJECT_IDENT)) {
                    alt21 = 1;
                }
                else if ((LA21_0 == QUOTED_VALUE)) {
                    alt21 = 2;
                }
                else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 21, 0, input);

                    throw nvae;

                }
                switch (alt21) {
                case 1:
                // BELScriptWalker.g:457:21: id= OBJECT_IDENT
                {
                    _last = (CommonTree) input.LT(1);
                    id =
                            (CommonTree) match(input, OBJECT_IDENT,
                                    FOLLOW_OBJECT_IDENT_in_param778);
                    id_tree = (CommonTree) adaptor.dupNode(id);

                    adaptor.addChild(root_0, id_tree);

                }
                    break;
                case 2:
                // BELScriptWalker.g:457:39: quo= QUOTED_VALUE
                {
                    _last = (CommonTree) input.LT(1);
                    quo =
                            (CommonTree) match(input, QUOTED_VALUE,
                                    FOLLOW_QUOTED_VALUE_in_param784);
                    quo_tree = (CommonTree) adaptor.dupNode(quo);

                    adaptor.addChild(root_0, quo_tree);

                }
                    break;

                }

                final StringBuilder pBuilder = new StringBuilder();

                if (nsp != null) {
                    String prefix = nsp.getText();
                    if (!definedNamespaces.containsKey(prefix.substring(0,
                            prefix.length() - 1))) {
                        addError(new NamespaceUndefinedException(nsp.getLine(),
                                nsp.getCharPositionInLine()));
                    }

                    pBuilder.append(prefix);
                }

                if (id != null) {
                    pBuilder.append(id.getText());
                }

                if (quo != null) {
                    pBuilder.append(quo.getText());
                }

                retval.r = pBuilder.toString();

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "param"

    public static class function_return extends TreeRuleReturnScope {
        public String r;
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "function"
    // BELScriptWalker.g:481:1: function returns [String r] : (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' ) ;
    public final BELScriptWalker.function_return function()
            throws RecognitionException {
        BELScriptWalker.function_return retval =
                new BELScriptWalker.function_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree fv = null;

        CommonTree fv_tree = null;

        try {
            // BELScriptWalker.g:481:28: ( (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' ) )
            // BELScriptWalker.g:482:5: (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' )
            {
                root_0 = (CommonTree) adaptor.nil();

                // BELScriptWalker.g:482:5: (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' )
                int alt22 = 59;
                switch (input.LA(1)) {
                case 106: {
                    alt22 = 1;
                }
                    break;
                case 95: {
                    alt22 = 2;
                }
                    break;
                case 114: {
                    alt22 = 3;
                }
                    break;
                case 108: {
                    alt22 = 4;
                }
                    break;
                case 52: {
                    alt22 = 5;
                }
                    break;
                case 51: {
                    alt22 = 6;
                }
                    break;
                case 91: {
                    alt22 = 7;
                }
                    break;
                case 90: {
                    alt22 = 8;
                }
                    break;
                case 78: {
                    alt22 = 9;
                }
                    break;
                case 77: {
                    alt22 = 10;
                }
                    break;
                case 56: {
                    alt22 = 11;
                }
                    break;
                case 58: {
                    alt22 = 12;
                }
                    break;
                case 97: {
                    alt22 = 13;
                }
                    break;
                case 96: {
                    alt22 = 14;
                }
                    break;
                case 67: {
                    alt22 = 15;
                }
                    break;
                case 66: {
                    alt22 = 16;
                }
                    break;
                case 126: {
                    alt22 = 17;
                }
                    break;
                case 121: {
                    alt22 = 18;
                }
                    break;
                case 62: {
                    alt22 = 19;
                }
                    break;
                case 116: {
                    alt22 = 20;
                }
                    break;
                case 63: {
                    alt22 = 21;
                }
                    break;
                case 120: {
                    alt22 = 22;
                }
                    break;
                case 111: {
                    alt22 = 23;
                }
                    break;
                case 115: {
                    alt22 = 24;
                }
                    break;
                case 69: {
                    alt22 = 25;
                }
                    break;
                case 68: {
                    alt22 = 26;
                }
                    break;
                case 76: {
                    alt22 = 27;
                }
                    break;
                case 75: {
                    alt22 = 28;
                }
                    break;
                case 72: {
                    alt22 = 29;
                }
                    break;
                case 71: {
                    alt22 = 30;
                }
                    break;
                case 92: {
                    alt22 = 31;
                }
                    break;
                case 53: {
                    alt22 = 32;
                }
                    break;
                case 60: {
                    alt22 = 33;
                }
                    break;
                case 59: {
                    alt22 = 34;
                }
                    break;
                case 88: {
                    alt22 = 35;
                }
                    break;
                case 87: {
                    alt22 = 36;
                }
                    break;
                case 101: {
                    alt22 = 37;
                }
                    break;
                case 100: {
                    alt22 = 38;
                }
                    break;
                case 99: {
                    alt22 = 39;
                }
                    break;
                case 98: {
                    alt22 = 40;
                }
                    break;
                case 113: {
                    alt22 = 41;
                }
                    break;
                case 112: {
                    alt22 = 42;
                }
                    break;
                case 124: {
                    alt22 = 43;
                }
                    break;
                case 130: {
                    alt22 = 44;
                }
                    break;
                case 127: {
                    alt22 = 45;
                }
                    break;
                case 122: {
                    alt22 = 46;
                }
                    break;
                case 80: {
                    alt22 = 47;
                }
                    break;
                case 79: {
                    alt22 = 48;
                }
                    break;
                case 65: {
                    alt22 = 49;
                }
                    break;
                case 64: {
                    alt22 = 50;
                }
                    break;
                case 107: {
                    alt22 = 51;
                }
                    break;
                case 102: {
                    alt22 = 52;
                }
                    break;
                case 119: {
                    alt22 = 53;
                }
                    break;
                case 117: {
                    alt22 = 54;
                }
                    break;
                case 129: {
                    alt22 = 55;
                }
                    break;
                case 128: {
                    alt22 = 56;
                }
                    break;
                case 110: {
                    alt22 = 57;
                }
                    break;
                case 104: {
                    alt22 = 58;
                }
                    break;
                case 89: {
                    alt22 = 59;
                }
                    break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 22, 0, input);

                    throw nvae;

                }

                switch (alt22) {
                case 1:
                // BELScriptWalker.g:483:9: fv= 'proteinAbundance'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 106,
                                    FOLLOW_106_in_function819);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 2:
                // BELScriptWalker.g:484:9: fv= 'p'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 95,
                                    FOLLOW_95_in_function845);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 3:
                // BELScriptWalker.g:485:9: fv= 'rnaAbundance'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 114,
                                    FOLLOW_114_in_function886);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 4:
                // BELScriptWalker.g:486:9: fv= 'r'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 108,
                                    FOLLOW_108_in_function916);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 5:
                // BELScriptWalker.g:487:9: fv= 'abundance'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 52,
                                    FOLLOW_52_in_function957);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 6:
                // BELScriptWalker.g:488:9: fv= 'a'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 51,
                                    FOLLOW_51_in_function990);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 7:
                // BELScriptWalker.g:489:9: fv= 'microRNAAbundance'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 91,
                                    FOLLOW_91_in_function1031);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 8:
                // BELScriptWalker.g:490:9: fv= 'm'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 90,
                                    FOLLOW_90_in_function1056);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 9:
                // BELScriptWalker.g:491:9: fv= 'geneAbundance'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 78,
                                    FOLLOW_78_in_function1097);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 10:
                // BELScriptWalker.g:492:9: fv= 'g'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 77,
                                    FOLLOW_77_in_function1126);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 11:
                // BELScriptWalker.g:493:9: fv= 'biologicalProcess'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 56,
                                    FOLLOW_56_in_function1167);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 12:
                // BELScriptWalker.g:494:9: fv= 'bp'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 58,
                                    FOLLOW_58_in_function1192);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 13:
                // BELScriptWalker.g:495:9: fv= 'pathology'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 97,
                                    FOLLOW_97_in_function1232);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 14:
                // BELScriptWalker.g:496:9: fv= 'path'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 96,
                                    FOLLOW_96_in_function1265);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 15:
                // BELScriptWalker.g:497:9: fv= 'complexAbundance'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 67,
                                    FOLLOW_67_in_function1303);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 16:
                // BELScriptWalker.g:498:9: fv= 'complex'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 66,
                                    FOLLOW_66_in_function1329);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 17:
                // BELScriptWalker.g:499:9: fv= 'translocation'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 126,
                                    FOLLOW_126_in_function1364);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 18:
                // BELScriptWalker.g:500:9: fv= 'tloc'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 121,
                                    FOLLOW_121_in_function1393);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 19:
                // BELScriptWalker.g:501:9: fv= 'cellSecretion'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 62,
                                    FOLLOW_62_in_function1431);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 20:
                // BELScriptWalker.g:502:9: fv= 'sec'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 116,
                                    FOLLOW_116_in_function1460);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 21:
                // BELScriptWalker.g:503:9: fv= 'cellSurfaceExpression'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 63,
                                    FOLLOW_63_in_function1499);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 22:
                // BELScriptWalker.g:504:9: fv= 'surf'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 120,
                                    FOLLOW_120_in_function1520);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 23:
                // BELScriptWalker.g:505:9: fv= 'reaction'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 111,
                                    FOLLOW_111_in_function1558);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 24:
                // BELScriptWalker.g:506:9: fv= 'rxn'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 115,
                                    FOLLOW_115_in_function1592);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 25:
                // BELScriptWalker.g:507:9: fv= 'compositeAbundance'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 69,
                                    FOLLOW_69_in_function1631);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 26:
                // BELScriptWalker.g:508:9: fv= 'composite'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 68,
                                    FOLLOW_68_in_function1655);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 27:
                // BELScriptWalker.g:509:9: fv= 'fusion'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 76,
                                    FOLLOW_76_in_function1688);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 28:
                // BELScriptWalker.g:510:9: fv= 'fus'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 75,
                                    FOLLOW_75_in_function1724);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 29:
                // BELScriptWalker.g:511:9: fv= 'degradation'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 72,
                                    FOLLOW_72_in_function1763);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 30:
                // BELScriptWalker.g:512:9: fv= 'deg'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 71,
                                    FOLLOW_71_in_function1794);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 31:
                // BELScriptWalker.g:513:9: fv= 'molecularActivity'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 92,
                                    FOLLOW_92_in_function1833);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 32:
                // BELScriptWalker.g:514:9: fv= 'act'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 53,
                                    FOLLOW_53_in_function1858);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 33:
                // BELScriptWalker.g:515:9: fv= 'catalyticActivity'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 60,
                                    FOLLOW_60_in_function1897);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 34:
                // BELScriptWalker.g:516:9: fv= 'cat'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 59,
                                    FOLLOW_59_in_function1922);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 35:
                // BELScriptWalker.g:517:9: fv= 'kinaseActivity'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 88,
                                    FOLLOW_88_in_function1961);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 36:
                // BELScriptWalker.g:518:9: fv= 'kin'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 87,
                                    FOLLOW_87_in_function1989);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 37:
                // BELScriptWalker.g:519:9: fv= 'phosphataseActivity'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 101,
                                    FOLLOW_101_in_function2028);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 38:
                // BELScriptWalker.g:520:9: fv= 'phos'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 100,
                                    FOLLOW_100_in_function2051);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 39:
                // BELScriptWalker.g:521:9: fv= 'peptidaseActivity'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 99,
                                    FOLLOW_99_in_function2089);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 40:
                // BELScriptWalker.g:522:9: fv= 'pep'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 98,
                                    FOLLOW_98_in_function2114);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 41:
                // BELScriptWalker.g:523:9: fv= 'ribosylationActivity'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 113,
                                    FOLLOW_113_in_function2153);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 42:
                // BELScriptWalker.g:524:9: fv= 'ribo'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 112,
                                    FOLLOW_112_in_function2175);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 43:
                // BELScriptWalker.g:525:9: fv= 'transcriptionalActivity'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 124,
                                    FOLLOW_124_in_function2213);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 44:
                // BELScriptWalker.g:526:9: fv= 'tscript'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 130,
                                    FOLLOW_130_in_function2232);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 45:
                // BELScriptWalker.g:527:9: fv= 'transportActivity'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 127,
                                    FOLLOW_127_in_function2267);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 46:
                // BELScriptWalker.g:528:9: fv= 'tport'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 122,
                                    FOLLOW_122_in_function2292);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 47:
                // BELScriptWalker.g:529:9: fv= 'gtpBoundActivity'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 80,
                                    FOLLOW_80_in_function2329);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 48:
                // BELScriptWalker.g:530:9: fv= 'gtp'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 79,
                                    FOLLOW_79_in_function2355);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 49:
                // BELScriptWalker.g:531:9: fv= 'chaperoneActivity'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 65,
                                    FOLLOW_65_in_function2394);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 50:
                // BELScriptWalker.g:532:9: fv= 'chap'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 64,
                                    FOLLOW_64_in_function2419);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 51:
                // BELScriptWalker.g:533:9: fv= 'proteinModification'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 107,
                                    FOLLOW_107_in_function2457);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 52:
                // BELScriptWalker.g:534:9: fv= 'pmod'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 102,
                                    FOLLOW_102_in_function2480);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 53:
                // BELScriptWalker.g:535:9: fv= 'substitution'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 119,
                                    FOLLOW_119_in_function2518);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 54:
                // BELScriptWalker.g:536:9: fv= 'sub'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 117,
                                    FOLLOW_117_in_function2548);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 55:
                // BELScriptWalker.g:537:9: fv= 'truncation'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 129,
                                    FOLLOW_129_in_function2587);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 56:
                // BELScriptWalker.g:538:9: fv= 'trunc'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 128,
                                    FOLLOW_128_in_function2619);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 57:
                // BELScriptWalker.g:539:9: fv= 'reactants'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 110,
                                    FOLLOW_110_in_function2656);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 58:
                // BELScriptWalker.g:540:9: fv= 'products'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 104,
                                    FOLLOW_104_in_function2689);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 59:
                // BELScriptWalker.g:541:9: fv= 'list'
                {
                    _last = (CommonTree) input.LT(1);
                    fv =
                            (CommonTree) match(input, 89,
                                    FOLLOW_89_in_function2723);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;

                }

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "function"

    public static class relationship_return extends TreeRuleReturnScope {
        public String r;
        CommonTree tree;

        @Override
        public Object getTree() {
            return tree;
        }
    }

    // $ANTLR start "relationship"
    // BELScriptWalker.g:545:1: relationship returns [String r] : (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' ) ;
    public final BELScriptWalker.relationship_return relationship()
            throws RecognitionException {
        BELScriptWalker.relationship_return retval =
                new BELScriptWalker.relationship_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree rv = null;

        CommonTree rv_tree = null;

        try {
            // BELScriptWalker.g:545:32: ( (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' ) )
            // BELScriptWalker.g:546:5: (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' )
            {
                root_0 = (CommonTree) adaptor.nil();

                // BELScriptWalker.g:546:5: (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' )
                int alt23 = 28;
                switch (input.LA(1)) {
                case 85: {
                    alt23 = 1;
                }
                    break;
                case 26: {
                    alt23 = 2;
                }
                    break;
                case 70: {
                    alt23 = 3;
                }
                    break;
                case 27: {
                    alt23 = 4;
                }
                    break;
                case 74: {
                    alt23 = 5;
                }
                    break;
                case 30: {
                    alt23 = 6;
                }
                    break;
                case 73: {
                    alt23 = 7;
                }
                    break;
                case 31: {
                    alt23 = 8;
                }
                    break;
                case 61: {
                    alt23 = 9;
                }
                    break;
                case 103: {
                    alt23 = 10;
                }
                    break;
                case 93: {
                    alt23 = 11;
                }
                    break;
                case 125: {
                    alt23 = 12;
                }
                    break;
                case 32: {
                    alt23 = 13;
                }
                    break;
                case 123: {
                    alt23 = 14;
                }
                    break;
                case 28: {
                    alt23 = 15;
                }
                    break;
                case 86: {
                    alt23 = 16;
                }
                    break;
                case 118: {
                    alt23 = 17;
                }
                    break;
                case 109: {
                    alt23 = 18;
                }
                    break;
                case 57: {
                    alt23 = 19;
                }
                    break;
                case 105: {
                    alt23 = 20;
                }
                    break;
                case 94: {
                    alt23 = 21;
                }
                    break;
                case 54: {
                    alt23 = 22;
                }
                    break;
                case 55: {
                    alt23 = 23;
                }
                    break;
                case 25: {
                    alt23 = 24;
                }
                    break;
                case 84: {
                    alt23 = 25;
                }
                    break;
                case 82: {
                    alt23 = 26;
                }
                    break;
                case 83: {
                    alt23 = 27;
                }
                    break;
                case 81: {
                    alt23 = 28;
                }
                    break;
                default:
                    NoViableAltException nvae =
                            new NoViableAltException("", 23, 0, input);

                    throw nvae;

                }

                switch (alt23) {
                case 1:
                // BELScriptWalker.g:547:9: rv= 'increases'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 85,
                                    FOLLOW_85_in_relationship2785);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 2:
                // BELScriptWalker.g:548:9: rv= '->'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 26,
                                    FOLLOW_26_in_relationship2818);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 3:
                // BELScriptWalker.g:549:9: rv= 'decreases'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 70,
                                    FOLLOW_70_in_relationship2858);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 4:
                // BELScriptWalker.g:550:9: rv= '-|'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 27,
                                    FOLLOW_27_in_relationship2891);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 5:
                // BELScriptWalker.g:551:9: rv= 'directlyIncreases'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 74,
                                    FOLLOW_74_in_relationship2931);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 6:
                // BELScriptWalker.g:552:9: rv= '=>'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 30,
                                    FOLLOW_30_in_relationship2956);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 7:
                // BELScriptWalker.g:553:9: rv= 'directlyDecreases'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 73,
                                    FOLLOW_73_in_relationship2996);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 8:
                // BELScriptWalker.g:554:9: rv= '=|'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 31,
                                    FOLLOW_31_in_relationship3021);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 9:
                // BELScriptWalker.g:555:9: rv= 'causesNoChange'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 61,
                                    FOLLOW_61_in_relationship3061);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 10:
                // BELScriptWalker.g:556:9: rv= 'positiveCorrelation'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 103,
                                    FOLLOW_103_in_relationship3089);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 11:
                // BELScriptWalker.g:557:9: rv= 'negativeCorrelation'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 93,
                                    FOLLOW_93_in_relationship3112);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 12:
                // BELScriptWalker.g:558:9: rv= 'translatedTo'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 125,
                                    FOLLOW_125_in_relationship3135);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 13:
                // BELScriptWalker.g:559:9: rv= '>>'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 32,
                                    FOLLOW_32_in_relationship3165);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 14:
                // BELScriptWalker.g:560:9: rv= 'transcribedTo'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 123,
                                    FOLLOW_123_in_relationship3205);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 15:
                // BELScriptWalker.g:561:9: rv= ':>'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 28,
                                    FOLLOW_28_in_relationship3234);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 16:
                // BELScriptWalker.g:562:9: rv= 'isA'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 86,
                                    FOLLOW_86_in_relationship3274);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 17:
                // BELScriptWalker.g:563:9: rv= 'subProcessOf'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 118,
                                    FOLLOW_118_in_relationship3313);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 18:
                // BELScriptWalker.g:564:9: rv= 'rateLimitingStepOf'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 109,
                                    FOLLOW_109_in_relationship3343);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 19:
                // BELScriptWalker.g:565:9: rv= 'biomarkerFor'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 57,
                                    FOLLOW_57_in_relationship3367);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 20:
                // BELScriptWalker.g:566:9: rv= 'prognosticBiomarkerFor'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 105,
                                    FOLLOW_105_in_relationship3397);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 21:
                // BELScriptWalker.g:567:9: rv= 'orthologous'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 94,
                                    FOLLOW_94_in_relationship3417);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 22:
                // BELScriptWalker.g:568:9: rv= 'analogous'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 54,
                                    FOLLOW_54_in_relationship3448);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 23:
                // BELScriptWalker.g:569:9: rv= 'association'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 55,
                                    FOLLOW_55_in_relationship3481);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 24:
                // BELScriptWalker.g:570:9: rv= '--'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 25,
                                    FOLLOW_25_in_relationship3512);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 25:
                // BELScriptWalker.g:571:9: rv= 'hasMembers'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 84,
                                    FOLLOW_84_in_relationship3552);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 26:
                // BELScriptWalker.g:572:9: rv= 'hasComponents'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 82,
                                    FOLLOW_82_in_relationship3584);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 27:
                // BELScriptWalker.g:573:9: rv= 'hasMember'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 83,
                                    FOLLOW_83_in_relationship3613);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 28:
                // BELScriptWalker.g:574:9: rv= 'hasComponent'
                {
                    _last = (CommonTree) input.LT(1);
                    rv =
                            (CommonTree) match(input, 81,
                                    FOLLOW_81_in_relationship3646);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;

                }

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        }

        finally {
            // do for sure before leaving
        }
        return retval;
    }

    // $ANTLR end "relationship"

    // Delegated rules

    public static final BitSet FOLLOW_NEWLINE_in_document67 = new BitSet(
            new long[] { 0xDD39808000002080L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_DOCUMENT_COMMENT_in_document71 =
            new BitSet(new long[] { 0xDD39808000002080L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_record_in_document75 = new BitSet(
            new long[] { 0xDD39808000002080L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_EOF_in_document79 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_define_namespace_in_record98 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_define_annotation_in_record102 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_annotation_in_record106 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_document_in_record110 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_statement_group_in_record114 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_unset_statement_group_in_record118 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_unset_in_record122 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_statement_in_record126 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_47_in_set_document144 = new BitSet(
            new long[] { 0x0000000000000100L });
    public static final BitSet FOLLOW_DOCUMENT_KEYWORD_in_set_document148 =
            new BitSet(new long[] { 0x00042B3800000000L });
    public static final BitSet FOLLOW_document_property_in_set_document153 =
            new BitSet(new long[] { 0x0000000020000000L });
    public static final BitSet FOLLOW_29_in_set_document155 = new BitSet(
            new long[] { 0x0000000000048000L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_set_document160 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_document166 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_47_in_set_statement_group185 =
            new BitSet(new long[] { 0x0000000000100000L });
    public static final BitSet FOLLOW_STATEMENT_GROUP_KEYWORD_in_set_statement_group187 =
            new BitSet(new long[] { 0x0000000020000000L });
    public static final BitSet FOLLOW_29_in_set_statement_group189 =
            new BitSet(new long[] { 0x0000000000048000L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_set_statement_group194 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_statement_group200 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_47_in_set_annotation219 = new BitSet(
            new long[] { 0x0000000000008000L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_annotation223 =
            new BitSet(new long[] { 0x0000000020000000L });
    public static final BitSet FOLLOW_29_in_set_annotation225 = new BitSet(
            new long[] { 0x0000000000448000L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_set_annotation230 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_VALUE_LIST_in_set_annotation236 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_annotation242 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_48_in_unset_statement_group261 =
            new BitSet(new long[] { 0x0000000000100000L });
    public static final BitSet FOLLOW_STATEMENT_GROUP_KEYWORD_in_unset_statement_group263 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_48_in_unset281 = new BitSet(
            new long[] { 0x0000000000008800L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_unset286 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENT_LIST_in_unset292 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_39_in_define_namespace312 = new BitSet(
            new long[] { 0x0000104000000000L });
    public static final BitSet FOLLOW_38_in_define_namespace318 = new BitSet(
            new long[] { 0x0000100000000000L });
    public static final BitSet FOLLOW_44_in_define_namespace322 = new BitSet(
            new long[] { 0x0000000000008000L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_define_namespace328 =
            new BitSet(new long[] { 0x0000000400000000L });
    public static final BitSet FOLLOW_34_in_define_namespace330 = new BitSet(
            new long[] { 0x0002000000000000L });
    public static final BitSet FOLLOW_49_in_define_namespace332 = new BitSet(
            new long[] { 0x0000000000040000L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_define_namespace336 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_39_in_define_annotation355 = new BitSet(
            new long[] { 0x0000000200000000L });
    public static final BitSet FOLLOW_33_in_define_annotation357 = new BitSet(
            new long[] { 0x0000000000008000L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_define_annotation362 =
            new BitSet(new long[] { 0x0000000400000000L });
    public static final BitSet FOLLOW_34_in_define_annotation364 = new BitSet(
            new long[] { 0x0002440000000000L });
    public static final BitSet FOLLOW_49_in_define_annotation371 = new BitSet(
            new long[] { 0x0000000000040000L });
    public static final BitSet FOLLOW_46_in_define_annotation377 = new BitSet(
            new long[] { 0x0000000000040000L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_define_annotation382 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_42_in_define_annotation390 = new BitSet(
            new long[] { 0x0000000000400000L });
    public static final BitSet FOLLOW_VALUE_LIST_in_define_annotation394 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_35_in_document_property420 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_36_in_document_property436 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_37_in_document_property448 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_40_in_document_property462 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_41_in_document_property474 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_43_in_document_property487 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_45_in_document_property502 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_50_in_document_property521 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_outer_term_in_statement545 = new BitSet(
            new long[] { 0x22C00001DE080002L, 0x28402280607E0640L });
    public static final BitSet FOLLOW_relationship_in_statement550 =
            new BitSet(new long[] { 0xDD38000000010000L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_OPEN_PAREN_in_statement554 = new BitSet(
            new long[] { 0xDD38000000000000L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_outer_term_in_statement558 = new BitSet(
            new long[] { 0x22C00001DE000000L, 0x28402280607E0640L });
    public static final BitSet FOLLOW_relationship_in_statement562 =
            new BitSet(new long[] { 0xDD38000000000000L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_outer_term_in_statement566 = new BitSet(
            new long[] { 0x0000000000000010L });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_statement568 = new BitSet(
            new long[] { 0x0000000000080002L });
    public static final BitSet FOLLOW_outer_term_in_statement575 = new BitSet(
            new long[] { 0x0000000000080002L });
    public static final BitSet FOLLOW_STATEMENT_COMMENT_in_statement582 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_function_in_outer_term613 = new BitSet(
            new long[] { 0x0000000000010000L });
    public static final BitSet FOLLOW_OPEN_PAREN_in_outer_term619 = new BitSet(
            new long[] { 0xDD3800000104C010L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_24_in_outer_term626 = new BitSet(
            new long[] { 0xDD3800000004C000L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_argument_in_outer_term633 = new BitSet(
            new long[] { 0xDD3800000104C010L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_outer_term641 =
            new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_param_in_argument665 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_term_in_argument677 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_function_in_term707 = new BitSet(
            new long[] { 0x0000000000010000L });
    public static final BitSet FOLLOW_OPEN_PAREN_in_term713 = new BitSet(
            new long[] { 0xDD3800000104C010L, 0xD7BFDD7F9F81F9BFL,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_24_in_term719 = new BitSet(new long[] {
            0xDD3800000004C000L, 0xD7BFDD7F9F81F9BFL, 0x0000000000000007L });
    public static final BitSet FOLLOW_term_in_term727 = new BitSet(new long[] {
            0xDD3800000104C010L, 0xD7BFDD7F9F81F9BFL, 0x0000000000000007L });
    public static final BitSet FOLLOW_param_in_term735 = new BitSet(new long[] {
            0xDD3800000104C010L, 0xD7BFDD7F9F81F9BFL, 0x0000000000000007L });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_term744 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_NS_PREFIX_in_param772 = new BitSet(
            new long[] { 0x0000000000048000L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_param778 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_param784 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_106_in_function819 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_95_in_function845 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_114_in_function886 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_108_in_function916 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_52_in_function957 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_51_in_function990 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_91_in_function1031 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_90_in_function1056 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_78_in_function1097 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_77_in_function1126 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_56_in_function1167 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_58_in_function1192 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_97_in_function1232 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_96_in_function1265 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_67_in_function1303 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_66_in_function1329 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_126_in_function1364 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_121_in_function1393 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_62_in_function1431 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_116_in_function1460 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_63_in_function1499 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_120_in_function1520 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_111_in_function1558 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_115_in_function1592 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_69_in_function1631 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_68_in_function1655 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_76_in_function1688 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_75_in_function1724 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_72_in_function1763 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_71_in_function1794 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_92_in_function1833 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_53_in_function1858 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_60_in_function1897 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_59_in_function1922 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_88_in_function1961 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_87_in_function1989 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_101_in_function2028 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_100_in_function2051 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_99_in_function2089 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_98_in_function2114 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_113_in_function2153 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_112_in_function2175 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_124_in_function2213 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_130_in_function2232 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_127_in_function2267 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_122_in_function2292 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_80_in_function2329 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_79_in_function2355 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_65_in_function2394 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_64_in_function2419 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_107_in_function2457 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_102_in_function2480 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_119_in_function2518 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_117_in_function2548 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_129_in_function2587 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_128_in_function2619 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_110_in_function2656 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_104_in_function2689 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_89_in_function2723 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_85_in_relationship2785 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_26_in_relationship2818 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_70_in_relationship2858 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_27_in_relationship2891 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_74_in_relationship2931 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_30_in_relationship2956 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_73_in_relationship2996 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_31_in_relationship3021 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_61_in_relationship3061 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_103_in_relationship3089 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_93_in_relationship3112 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_125_in_relationship3135 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_32_in_relationship3165 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_123_in_relationship3205 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_28_in_relationship3234 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_86_in_relationship3274 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_118_in_relationship3313 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_109_in_relationship3343 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_57_in_relationship3367 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_105_in_relationship3397 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_94_in_relationship3417 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_54_in_relationship3448 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_55_in_relationship3481 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_25_in_relationship3512 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_84_in_relationship3552 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_82_in_relationship3584 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_83_in_relationship3613 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_81_in_relationship3646 = new BitSet(
            new long[] { 0x0000000000000002L });

}
