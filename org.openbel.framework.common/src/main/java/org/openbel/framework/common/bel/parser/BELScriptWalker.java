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

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.DFA;
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

public class BELScriptWalker extends TreeParser {
    public static final String[] tokenNames = new String[] {
            "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NEWLINE",
            "DOCUMENT_COMMENT", "DOCUMENT_KEYWORD", "OBJECT_IDENT",
            "VALUE_LIST", "QUOTED_VALUE", "STATEMENT_GROUP_KEYWORD",
            "IDENT_LIST", "OPEN_PAREN", "CLOSE_PAREN", "STATEMENT_COMMENT",
            "NS_PREFIX", "COMMA", "LETTER", "DIGIT", "EscapeSequence", "WS",
            "UnicodeEscape", "OctalEscape", "HexDigit", "'SET'", "'='",
            "'UNSET'", "'DEFINE'", "'DEFAULT'", "'NAMESPACE'", "'AS'", "'URL'",
            "'ANNOTATION'", "'PATTERN'", "'LIST'", "'Authors'",
            "'ContactInfo'", "'Copyright'", "'Description'", "'Disclaimer'",
            "'Licenses'", "'Name'", "'Version'", "','", "'proteinAbundance'",
            "'p'", "'rnaAbundance'", "'r'", "'abundance'", "'a'",
            "'microRNAAbundance'", "'m'", "'geneAbundance'", "'g'",
            "'biologicalProcess'", "'bp'", "'pathology'", "'path'",
            "'complexAbundance'", "'complex'", "'translocation'", "'tloc'",
            "'cellSecretion'", "'sec'", "'cellSurfaceExpression'", "'surf'",
            "'reaction'", "'rxn'", "'compositeAbundance'", "'composite'",
            "'fusion'", "'fus'", "'degradation'", "'deg'",
            "'molecularActivity'", "'act'", "'catalyticActivity'", "'cat'",
            "'kinaseActivity'", "'kin'", "'phosphataseActivity'", "'phos'",
            "'peptidaseActivity'", "'pep'", "'ribosylationActivity'", "'ribo'",
            "'transcriptionalActivity'", "'tscript'", "'transportActivity'",
            "'tport'", "'gtpBoundActivity'", "'gtp'", "'chaperoneActivity'",
            "'chap'", "'proteinModification'", "'pmod'", "'substitution'",
            "'sub'", "'truncation'", "'trunc'", "'reactants'", "'products'",
            "'list'", "'increases'", "'->'", "'decreases'", "'-|'",
            "'directlyIncreases'", "'=>'", "'directlyDecreases'", "'=|'",
            "'causesNoChange'", "'positiveCorrelation'",
            "'negativeCorrelation'", "'translatedTo'", "'>>'",
            "'transcribedTo'", "':>'", "'isA'", "'subProcessOf'",
            "'rateLimitingStepOf'", "'biomarkerFor'",
            "'prognosticBiomarkerFor'", "'orthologous'", "'analogous'",
            "'association'", "'--'", "'hasMembers'", "'hasComponents'",
            "'hasMember'", "'hasComponent'"
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
    public static final int NEWLINE = 4;
    public static final int DOCUMENT_COMMENT = 5;
    public static final int DOCUMENT_KEYWORD = 6;
    public static final int OBJECT_IDENT = 7;
    public static final int VALUE_LIST = 8;
    public static final int QUOTED_VALUE = 9;
    public static final int STATEMENT_GROUP_KEYWORD = 10;
    public static final int IDENT_LIST = 11;
    public static final int OPEN_PAREN = 12;
    public static final int CLOSE_PAREN = 13;
    public static final int STATEMENT_COMMENT = 14;
    public static final int NS_PREFIX = 15;
    public static final int COMMA = 16;
    public static final int LETTER = 17;
    public static final int DIGIT = 18;
    public static final int EscapeSequence = 19;
    public static final int WS = 20;
    public static final int UnicodeEscape = 21;
    public static final int OctalEscape = 22;
    public static final int HexDigit = 23;

    // delegates
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
        return "/home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g";
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
            new SimpleDateFormat(
                    "yyyy-MM-dd");

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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:96:1:
    // document returns [BELDocument doc] : ( NEWLINE | DOCUMENT_COMMENT |
    // record )+ EOF ;
    public final BELScriptWalker.document_return document()
            throws RecognitionException {
        BELScriptWalker.document_return retval =
                new BELScriptWalker.document_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree NEWLINE1 = null;
        CommonTree DOCUMENT_COMMENT2 = null;
        CommonTree EOF4 = null;
        BELScriptWalker.record_return record3 = null;

        CommonTree NEWLINE1_tree = null;
        CommonTree DOCUMENT_COMMENT2_tree = null;
        CommonTree EOF4_tree = null;

        try {
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:96:35:
            // ( ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:97:5:
            // ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF
            {
                root_0 = (CommonTree) adaptor.nil();

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:97:5:
                // ( NEWLINE | DOCUMENT_COMMENT | record )+
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
                    case 24:
                    case 26:
                    case 27:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                    case 68:
                    case 69:
                    case 70:
                    case 71:
                    case 72:
                    case 73:
                    case 74:
                    case 75:
                    case 76:
                    case 77:
                    case 78:
                    case 79:
                    case 80:
                    case 81:
                    case 82:
                    case 83:
                    case 84:
                    case 85:
                    case 86:
                    case 87:
                    case 88:
                    case 89:
                    case 90:
                    case 91:
                    case 92:
                    case 93:
                    case 94:
                    case 95:
                    case 96:
                    case 97:
                    case 98:
                    case 99:
                    case 100:
                    case 101:
                    case 102: {
                        alt1 = 3;
                    }
                        break;

                    }

                    switch (alt1) {
                    case 1:
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:97:6:
                    // NEWLINE
                    {

                        NEWLINE1 = (CommonTree) match(input, NEWLINE,
                                FOLLOW_NEWLINE_in_document67);
                        NEWLINE1_tree = (CommonTree) adaptor.dupNode(NEWLINE1);

                        adaptor.addChild(root_0, NEWLINE1_tree);

                    }
                        break;
                    case 2:
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:97:16:
                    // DOCUMENT_COMMENT
                    {

                        DOCUMENT_COMMENT2 = (CommonTree) match(input,
                                DOCUMENT_COMMENT,
                                FOLLOW_DOCUMENT_COMMENT_in_document71);
                        DOCUMENT_COMMENT2_tree = (CommonTree) adaptor
                                .dupNode(DOCUMENT_COMMENT2);

                        adaptor.addChild(root_0, DOCUMENT_COMMENT2_tree);

                    }
                        break;
                    case 3:
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:97:35:
                    // record
                    {

                        pushFollow(FOLLOW_record_in_document75);
                        record3 = record();

                        state._fsp--;

                        adaptor.addChild(root_0, record3.getTree());

                    }
                        break;

                    default:
                        if (cnt1 >= 1)
                            break loop1;
                        EarlyExitException eee =
                                new EarlyExitException(1, input);
                        throw eee;
                    }
                    cnt1++;
                } while (true);

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
                        // statements are only contained in explicitly-defined
                        // statement groups
                        retval.doc = new BELDocument(
                                BELDocumentHeader.create(docprop), adlist,
                                nslist, statementGroups);
                    } else {
                        // statements are defined in the implicit document
                        // statement group and possibly child statement groups
                        documentStatementGroup
                                .setChildStatementGroups(statementGroups);
                        retval.doc = new BELDocument(
                                BELDocumentHeader.create(docprop), adlist,
                                nslist, Arrays.asList(documentStatementGroup));
                    }
                }

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:117:1:
    // record : ( define_namespace | define_annotation | set_annotation |
    // set_document | set_statement_group | unset_statement_group | unset |
    // statement ) ;
    public final BELScriptWalker.record_return record()
            throws RecognitionException {
        BELScriptWalker.record_return retval =
                new BELScriptWalker.record_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

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
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:117:7:
            // ( ( define_namespace | define_annotation | set_annotation |
            // set_document | set_statement_group | unset_statement_group |
            // unset | statement ) )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:118:5:
            // ( define_namespace | define_annotation | set_annotation |
            // set_document | set_statement_group | unset_statement_group |
            // unset | statement )
            {
                root_0 = (CommonTree) adaptor.nil();

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:118:5:
                // ( define_namespace | define_annotation | set_annotation |
                // set_document | set_statement_group | unset_statement_group |
                // unset | statement )
                int alt2 = 8;
                alt2 = dfa2.predict(input);
                switch (alt2) {
                case 1:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:118:6:
                // define_namespace
                {

                    pushFollow(FOLLOW_define_namespace_in_record98);
                    define_namespace5 = define_namespace();

                    state._fsp--;

                    adaptor.addChild(root_0, define_namespace5.getTree());

                }
                    break;
                case 2:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:118:25:
                // define_annotation
                {

                    pushFollow(FOLLOW_define_annotation_in_record102);
                    define_annotation6 = define_annotation();

                    state._fsp--;

                    adaptor.addChild(root_0, define_annotation6.getTree());

                }
                    break;
                case 3:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:118:45:
                // set_annotation
                {

                    pushFollow(FOLLOW_set_annotation_in_record106);
                    set_annotation7 = set_annotation();

                    state._fsp--;

                    adaptor.addChild(root_0, set_annotation7.getTree());

                }
                    break;
                case 4:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:118:62:
                // set_document
                {

                    pushFollow(FOLLOW_set_document_in_record110);
                    set_document8 = set_document();

                    state._fsp--;

                    adaptor.addChild(root_0, set_document8.getTree());

                }
                    break;
                case 5:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:118:77:
                // set_statement_group
                {

                    pushFollow(FOLLOW_set_statement_group_in_record114);
                    set_statement_group9 = set_statement_group();

                    state._fsp--;

                    adaptor.addChild(root_0, set_statement_group9.getTree());

                }
                    break;
                case 6:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:118:99:
                // unset_statement_group
                {

                    pushFollow(FOLLOW_unset_statement_group_in_record118);
                    unset_statement_group10 = unset_statement_group();

                    state._fsp--;

                    adaptor.addChild(root_0, unset_statement_group10.getTree());

                }
                    break;
                case 7:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:118:123:
                // unset
                {

                    pushFollow(FOLLOW_unset_in_record122);
                    unset11 = unset();

                    state._fsp--;

                    adaptor.addChild(root_0, unset11.getTree());

                }
                    break;
                case 8:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:118:131:
                // statement
                {

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
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:121:1:
    // set_document : ( 'SET' dkt= DOCUMENT_KEYWORD ) prop= document_property
    // '=' (qv= QUOTED_VALUE | oi= OBJECT_IDENT ) ;
    public final BELScriptWalker.set_document_return set_document()
            throws RecognitionException {
        BELScriptWalker.set_document_return retval =
                new BELScriptWalker.set_document_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

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
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:121:13:
            // ( ( 'SET' dkt= DOCUMENT_KEYWORD ) prop= document_property '='
            // (qv= QUOTED_VALUE | oi= OBJECT_IDENT ) )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:122:5:
            // ( 'SET' dkt= DOCUMENT_KEYWORD ) prop= document_property '=' (qv=
            // QUOTED_VALUE | oi= OBJECT_IDENT )
            {
                root_0 = (CommonTree) adaptor.nil();

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:122:5:
                // ( 'SET' dkt= DOCUMENT_KEYWORD )
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:122:6:
                // 'SET' dkt= DOCUMENT_KEYWORD
                {

                    string_literal13 = (CommonTree) match(input, 24,
                            FOLLOW_24_in_set_document144);
                    string_literal13_tree = (CommonTree) adaptor
                            .dupNode(string_literal13);

                    adaptor.addChild(root_0, string_literal13_tree);

                    dkt = (CommonTree) match(input, DOCUMENT_KEYWORD,
                            FOLLOW_DOCUMENT_KEYWORD_in_set_document148);
                    dkt_tree = (CommonTree) adaptor.dupNode(dkt);

                    adaptor.addChild(root_0, dkt_tree);

                }

                pushFollow(FOLLOW_document_property_in_set_document153);
                prop = document_property();

                state._fsp--;

                adaptor.addChild(root_0, prop.getTree());

                char_literal14 = (CommonTree) match(input, 25,
                        FOLLOW_25_in_set_document155);
                char_literal14_tree = (CommonTree) adaptor
                        .dupNode(char_literal14);

                adaptor.addChild(root_0, char_literal14_tree);

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:122:61:
                // (qv= QUOTED_VALUE | oi= OBJECT_IDENT )
                int alt3 = 2;
                int LA3_0 = input.LA(1);

                if ((LA3_0 == QUOTED_VALUE)) {
                    alt3 = 1;
                } else if ((LA3_0 == OBJECT_IDENT)) {
                    alt3 = 2;
                } else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 3, 0, input);

                    throw nvae;
                }
                switch (alt3) {
                case 1:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:122:62:
                // qv= QUOTED_VALUE
                {

                    qv = (CommonTree) match(input, QUOTED_VALUE,
                            FOLLOW_QUOTED_VALUE_in_set_document160);
                    qv_tree = (CommonTree) adaptor.dupNode(qv);

                    adaptor.addChild(root_0, qv_tree);

                }
                    break;
                case 2:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:122:80:
                // oi= OBJECT_IDENT
                {

                    oi = (CommonTree) match(input, OBJECT_IDENT,
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
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:141:1:
    // set_statement_group : 'SET' STATEMENT_GROUP_KEYWORD '=' (qv= QUOTED_VALUE
    // | oi= OBJECT_IDENT ) ;
    public final BELScriptWalker.set_statement_group_return
            set_statement_group()
                    throws RecognitionException {
        BELScriptWalker.set_statement_group_return retval =
                new BELScriptWalker.set_statement_group_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

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
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:141:20:
            // ( 'SET' STATEMENT_GROUP_KEYWORD '=' (qv= QUOTED_VALUE | oi=
            // OBJECT_IDENT ) )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:142:5:
            // 'SET' STATEMENT_GROUP_KEYWORD '=' (qv= QUOTED_VALUE | oi=
            // OBJECT_IDENT )
            {
                root_0 = (CommonTree) adaptor.nil();

                string_literal15 = (CommonTree) match(input, 24,
                        FOLLOW_24_in_set_statement_group185);
                string_literal15_tree = (CommonTree) adaptor
                        .dupNode(string_literal15);

                adaptor.addChild(root_0, string_literal15_tree);

                STATEMENT_GROUP_KEYWORD16 =
                        (CommonTree) match(input,
                                STATEMENT_GROUP_KEYWORD,
                                FOLLOW_STATEMENT_GROUP_KEYWORD_in_set_statement_group187);
                STATEMENT_GROUP_KEYWORD16_tree = (CommonTree) adaptor
                        .dupNode(STATEMENT_GROUP_KEYWORD16);

                adaptor.addChild(root_0, STATEMENT_GROUP_KEYWORD16_tree);

                char_literal17 = (CommonTree) match(input, 25,
                        FOLLOW_25_in_set_statement_group189);
                char_literal17_tree = (CommonTree) adaptor
                        .dupNode(char_literal17);

                adaptor.addChild(root_0, char_literal17_tree);

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:142:39:
                // (qv= QUOTED_VALUE | oi= OBJECT_IDENT )
                int alt4 = 2;
                int LA4_0 = input.LA(1);

                if ((LA4_0 == QUOTED_VALUE)) {
                    alt4 = 1;
                } else if ((LA4_0 == OBJECT_IDENT)) {
                    alt4 = 2;
                } else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 4, 0, input);

                    throw nvae;
                }
                switch (alt4) {
                case 1:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:142:40:
                // qv= QUOTED_VALUE
                {

                    qv = (CommonTree) match(input, QUOTED_VALUE,
                            FOLLOW_QUOTED_VALUE_in_set_statement_group194);
                    qv_tree = (CommonTree) adaptor.dupNode(qv);

                    adaptor.addChild(root_0, qv_tree);

                }
                    break;
                case 2:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:142:58:
                // oi= OBJECT_IDENT
                {

                    oi = (CommonTree) match(input, OBJECT_IDENT,
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
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:157:1:
    // set_annotation : 'SET' an= OBJECT_IDENT '=' (qv= QUOTED_VALUE | list=
    // VALUE_LIST | oi= OBJECT_IDENT ) ;
    public final BELScriptWalker.set_annotation_return set_annotation()
            throws RecognitionException {
        BELScriptWalker.set_annotation_return retval =
                new BELScriptWalker.set_annotation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

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
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:157:15:
            // ( 'SET' an= OBJECT_IDENT '=' (qv= QUOTED_VALUE | list= VALUE_LIST
            // | oi= OBJECT_IDENT ) )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:158:5:
            // 'SET' an= OBJECT_IDENT '=' (qv= QUOTED_VALUE | list= VALUE_LIST |
            // oi= OBJECT_IDENT )
            {
                root_0 = (CommonTree) adaptor.nil();

                string_literal18 = (CommonTree) match(input, 24,
                        FOLLOW_24_in_set_annotation219);
                string_literal18_tree = (CommonTree) adaptor
                        .dupNode(string_literal18);

                adaptor.addChild(root_0, string_literal18_tree);

                an = (CommonTree) match(input, OBJECT_IDENT,
                        FOLLOW_OBJECT_IDENT_in_set_annotation223);
                an_tree = (CommonTree) adaptor.dupNode(an);

                adaptor.addChild(root_0, an_tree);

                char_literal19 = (CommonTree) match(input, 25,
                        FOLLOW_25_in_set_annotation225);
                char_literal19_tree = (CommonTree) adaptor
                        .dupNode(char_literal19);

                adaptor.addChild(root_0, char_literal19_tree);

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:158:31:
                // (qv= QUOTED_VALUE | list= VALUE_LIST | oi= OBJECT_IDENT )
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
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:158:32:
                // qv= QUOTED_VALUE
                {

                    qv = (CommonTree) match(input, QUOTED_VALUE,
                            FOLLOW_QUOTED_VALUE_in_set_annotation230);
                    qv_tree = (CommonTree) adaptor.dupNode(qv);

                    adaptor.addChild(root_0, qv_tree);

                }
                    break;
                case 2:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:158:50:
                // list= VALUE_LIST
                {

                    list = (CommonTree) match(input, VALUE_LIST,
                            FOLLOW_VALUE_LIST_in_set_annotation236);
                    list_tree = (CommonTree) adaptor.dupNode(list);

                    adaptor.addChild(root_0, list_tree);

                }
                    break;
                case 3:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:158:68:
                // oi= OBJECT_IDENT
                {

                    oi = (CommonTree) match(input, OBJECT_IDENT,
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
                        listvalues = listvalues.substring(1,
                                listvalues.length() - 1);
                        annotation = new BELAnnotation(ad,
                                Arrays.asList(ParserUtil
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
                    // throw if annotation is not defined and it's not the
                    // intrinsics: Citation or EvidenceLine
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
                        String[] tokens = ParserUtil
                                .parseListRecord(listvalues);

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
                                    publicationDate = iso8601DateFormat
                                            .parse(tokens[3]);
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
                                new BELCitation(
                                        type,
                                        cname,
                                        publicationDate,
                                        reference,
                                        authors == null ? null
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
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:278:1:
    // unset_statement_group : 'UNSET' STATEMENT_GROUP_KEYWORD ;
    public final BELScriptWalker.unset_statement_group_return
            unset_statement_group()
                    throws RecognitionException {
        BELScriptWalker.unset_statement_group_return retval =
                new BELScriptWalker.unset_statement_group_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree string_literal20 = null;
        CommonTree STATEMENT_GROUP_KEYWORD21 = null;

        CommonTree string_literal20_tree = null;
        CommonTree STATEMENT_GROUP_KEYWORD21_tree = null;

        try {
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:278:22:
            // ( 'UNSET' STATEMENT_GROUP_KEYWORD )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:279:5:
            // 'UNSET' STATEMENT_GROUP_KEYWORD
            {
                root_0 = (CommonTree) adaptor.nil();

                string_literal20 = (CommonTree) match(input, 26,
                        FOLLOW_26_in_unset_statement_group265);
                string_literal20_tree = (CommonTree) adaptor
                        .dupNode(string_literal20);

                adaptor.addChild(root_0, string_literal20_tree);

                STATEMENT_GROUP_KEYWORD21 =
                        (CommonTree) match(input,
                                STATEMENT_GROUP_KEYWORD,
                                FOLLOW_STATEMENT_GROUP_KEYWORD_in_unset_statement_group267);
                STATEMENT_GROUP_KEYWORD21_tree = (CommonTree) adaptor
                        .dupNode(STATEMENT_GROUP_KEYWORD21);

                adaptor.addChild(root_0, STATEMENT_GROUP_KEYWORD21_tree);

                activeStatementGroup = null;
                sgAnnotationContext.clear();

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:285:1:
    // unset : 'UNSET' (an= OBJECT_IDENT | list= IDENT_LIST ) ;
    public final BELScriptWalker.unset_return unset()
            throws RecognitionException {
        BELScriptWalker.unset_return retval =
                new BELScriptWalker.unset_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree an = null;
        CommonTree list = null;
        CommonTree string_literal22 = null;

        CommonTree an_tree = null;
        CommonTree list_tree = null;
        CommonTree string_literal22_tree = null;

        try {
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:285:6:
            // ( 'UNSET' (an= OBJECT_IDENT | list= IDENT_LIST ) )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:286:5:
            // 'UNSET' (an= OBJECT_IDENT | list= IDENT_LIST )
            {
                root_0 = (CommonTree) adaptor.nil();

                string_literal22 = (CommonTree) match(input, 26,
                        FOLLOW_26_in_unset285);
                string_literal22_tree = (CommonTree) adaptor
                        .dupNode(string_literal22);

                adaptor.addChild(root_0, string_literal22_tree);

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:286:13:
                // (an= OBJECT_IDENT | list= IDENT_LIST )
                int alt6 = 2;
                int LA6_0 = input.LA(1);

                if ((LA6_0 == OBJECT_IDENT)) {
                    alt6 = 1;
                } else if ((LA6_0 == IDENT_LIST)) {
                    alt6 = 2;
                } else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 6, 0, input);

                    throw nvae;
                }
                switch (alt6) {
                case 1:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:286:14:
                // an= OBJECT_IDENT
                {

                    an = (CommonTree) match(input, OBJECT_IDENT,
                            FOLLOW_OBJECT_IDENT_in_unset290);
                    an_tree = (CommonTree) adaptor.dupNode(an);

                    adaptor.addChild(root_0, an_tree);

                }
                    break;
                case 2:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:286:32:
                // list= IDENT_LIST
                {

                    list = (CommonTree) match(input, IDENT_LIST,
                            FOLLOW_IDENT_LIST_in_unset296);
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
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:300:1:
    // define_namespace : ( 'DEFINE' ( (isdefault= 'DEFAULT' )? 'NAMESPACE' ) )
    // name= OBJECT_IDENT 'AS' 'URL' rloc= QUOTED_VALUE ;
    public final BELScriptWalker.define_namespace_return define_namespace()
            throws RecognitionException {
        BELScriptWalker.define_namespace_return retval =
                new BELScriptWalker.define_namespace_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

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
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:300:17:
            // ( ( 'DEFINE' ( (isdefault= 'DEFAULT' )? 'NAMESPACE' ) ) name=
            // OBJECT_IDENT 'AS' 'URL' rloc= QUOTED_VALUE )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:301:5:
            // ( 'DEFINE' ( (isdefault= 'DEFAULT' )? 'NAMESPACE' ) ) name=
            // OBJECT_IDENT 'AS' 'URL' rloc= QUOTED_VALUE
            {
                root_0 = (CommonTree) adaptor.nil();

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:301:5:
                // ( 'DEFINE' ( (isdefault= 'DEFAULT' )? 'NAMESPACE' ) )
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:301:6:
                // 'DEFINE' ( (isdefault= 'DEFAULT' )? 'NAMESPACE' )
                {

                    string_literal23 = (CommonTree) match(input, 27,
                            FOLLOW_27_in_define_namespace316);
                    string_literal23_tree = (CommonTree) adaptor
                            .dupNode(string_literal23);

                    adaptor.addChild(root_0, string_literal23_tree);

                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:301:15:
                    // ( (isdefault= 'DEFAULT' )? 'NAMESPACE' )
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:301:16:
                    // (isdefault= 'DEFAULT' )? 'NAMESPACE'
                    {
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:301:16:
                        // (isdefault= 'DEFAULT' )?
                        int alt7 = 2;
                        int LA7_0 = input.LA(1);

                        if ((LA7_0 == 28)) {
                            alt7 = 1;
                        }
                        switch (alt7) {
                        case 1:
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:301:17:
                        // isdefault= 'DEFAULT'
                        {

                            isdefault = (CommonTree) match(input, 28,
                                    FOLLOW_28_in_define_namespace322);
                            isdefault_tree = (CommonTree) adaptor
                                    .dupNode(isdefault);

                            adaptor.addChild(root_0, isdefault_tree);

                        }
                            break;

                        }

                        string_literal24 = (CommonTree) match(input, 29,
                                FOLLOW_29_in_define_namespace326);
                        string_literal24_tree = (CommonTree) adaptor
                                .dupNode(string_literal24);

                        adaptor.addChild(root_0, string_literal24_tree);

                    }

                }

                name = (CommonTree) match(input, OBJECT_IDENT,
                        FOLLOW_OBJECT_IDENT_in_define_namespace332);
                name_tree = (CommonTree) adaptor.dupNode(name);

                adaptor.addChild(root_0, name_tree);

                string_literal25 = (CommonTree) match(input, 30,
                        FOLLOW_30_in_define_namespace334);
                string_literal25_tree = (CommonTree) adaptor
                        .dupNode(string_literal25);

                adaptor.addChild(root_0, string_literal25_tree);

                string_literal26 = (CommonTree) match(input, 31,
                        FOLLOW_31_in_define_namespace336);
                string_literal26_tree = (CommonTree) adaptor
                        .dupNode(string_literal26);

                adaptor.addChild(root_0, string_literal26_tree);

                rloc = (CommonTree) match(input, QUOTED_VALUE,
                        FOLLOW_QUOTED_VALUE_in_define_namespace340);
                rloc_tree = (CommonTree) adaptor.dupNode(rloc);

                adaptor.addChild(root_0, rloc_tree);

                final String nametext = name.getText();
                final String rloctext = rloc.getText();

                BELNamespaceDefinition belnsd;
                if (isdefault != null) {
                    belnsd = new BELNamespaceDefinition(nametext, rloctext,
                            true);
                } else {
                    belnsd = new BELNamespaceDefinition(nametext, rloctext,
                            false);
                }

                nslist.add(belnsd);
                definedNamespaces.put(nametext, belnsd);

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:317:1:
    // define_annotation : ( 'DEFINE' 'ANNOTATION' ) name= OBJECT_IDENT 'AS' ( (
    // (type= 'URL' | type= 'PATTERN' ) value= QUOTED_VALUE ) | (type= 'LIST'
    // value= VALUE_LIST ) ) ;
    public final BELScriptWalker.define_annotation_return define_annotation()
            throws RecognitionException {
        BELScriptWalker.define_annotation_return retval =
                new BELScriptWalker.define_annotation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

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
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:317:18:
            // ( ( 'DEFINE' 'ANNOTATION' ) name= OBJECT_IDENT 'AS' ( ( (type=
            // 'URL' | type= 'PATTERN' ) value= QUOTED_VALUE ) | (type= 'LIST'
            // value= VALUE_LIST ) ) )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:5:
            // ( 'DEFINE' 'ANNOTATION' ) name= OBJECT_IDENT 'AS' ( ( (type=
            // 'URL' | type= 'PATTERN' ) value= QUOTED_VALUE ) | (type= 'LIST'
            // value= VALUE_LIST ) )
            {
                root_0 = (CommonTree) adaptor.nil();

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:5:
                // ( 'DEFINE' 'ANNOTATION' )
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:6:
                // 'DEFINE' 'ANNOTATION'
                {

                    string_literal27 = (CommonTree) match(input, 27,
                            FOLLOW_27_in_define_annotation359);
                    string_literal27_tree = (CommonTree) adaptor
                            .dupNode(string_literal27);

                    adaptor.addChild(root_0, string_literal27_tree);

                    string_literal28 = (CommonTree) match(input, 32,
                            FOLLOW_32_in_define_annotation361);
                    string_literal28_tree = (CommonTree) adaptor
                            .dupNode(string_literal28);

                    adaptor.addChild(root_0, string_literal28_tree);

                }

                name = (CommonTree) match(input, OBJECT_IDENT,
                        FOLLOW_OBJECT_IDENT_in_define_annotation366);
                name_tree = (CommonTree) adaptor.dupNode(name);

                adaptor.addChild(root_0, name_tree);

                string_literal29 = (CommonTree) match(input, 30,
                        FOLLOW_30_in_define_annotation368);
                string_literal29_tree = (CommonTree) adaptor
                        .dupNode(string_literal29);

                adaptor.addChild(root_0, string_literal29_tree);

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:52:
                // ( ( (type= 'URL' | type= 'PATTERN' ) value= QUOTED_VALUE ) |
                // (type= 'LIST' value= VALUE_LIST ) )
                int alt9 = 2;
                int LA9_0 = input.LA(1);

                if ((LA9_0 == 31 || LA9_0 == 33)) {
                    alt9 = 1;
                } else if ((LA9_0 == 34)) {
                    alt9 = 2;
                } else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 9, 0, input);

                    throw nvae;
                }
                switch (alt9) {
                case 1:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:53:
                // ( (type= 'URL' | type= 'PATTERN' ) value= QUOTED_VALUE )
                {
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:53:
                    // ( (type= 'URL' | type= 'PATTERN' ) value= QUOTED_VALUE )
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:54:
                    // (type= 'URL' | type= 'PATTERN' ) value= QUOTED_VALUE
                    {
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:54:
                        // (type= 'URL' | type= 'PATTERN' )
                        int alt8 = 2;
                        int LA8_0 = input.LA(1);

                        if ((LA8_0 == 31)) {
                            alt8 = 1;
                        } else if ((LA8_0 == 33)) {
                            alt8 = 2;
                        } else {
                            NoViableAltException nvae =
                                    new NoViableAltException("", 8, 0, input);

                            throw nvae;
                        }
                        switch (alt8) {
                        case 1:
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:55:
                        // type= 'URL'
                        {

                            type = (CommonTree) match(input, 31,
                                    FOLLOW_31_in_define_annotation375);
                            type_tree = (CommonTree) adaptor.dupNode(type);

                            adaptor.addChild(root_0, type_tree);

                        }
                            break;
                        case 2:
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:68:
                        // type= 'PATTERN'
                        {

                            type = (CommonTree) match(input, 33,
                                    FOLLOW_33_in_define_annotation381);
                            type_tree = (CommonTree) adaptor.dupNode(type);

                            adaptor.addChild(root_0, type_tree);

                        }
                            break;

                        }

                        value = (CommonTree) match(input, QUOTED_VALUE,
                                FOLLOW_QUOTED_VALUE_in_define_annotation386);
                        value_tree = (CommonTree) adaptor.dupNode(value);

                        adaptor.addChild(root_0, value_tree);

                    }

                }
                    break;
                case 2:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:106:
                // (type= 'LIST' value= VALUE_LIST )
                {
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:106:
                    // (type= 'LIST' value= VALUE_LIST )
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:318:107:
                    // type= 'LIST' value= VALUE_LIST
                    {

                        type = (CommonTree) match(input, 34,
                                FOLLOW_34_in_define_annotation394);
                        type_tree = (CommonTree) adaptor.dupNode(type);

                        adaptor.addChild(root_0, type_tree);

                        value = (CommonTree) match(input, VALUE_LIST,
                                FOLLOW_VALUE_LIST_in_define_annotation398);
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
                        ad = new BELAnnotationDefinition(nametext, atype,
                                valuetext);
                    } else if ("PATTERN".equals(typetext)) {
                        atype = BELAnnotationType.PATTERN;
                        ad = new BELAnnotationDefinition(nametext, atype,
                                valuetext);
                    } else {
                        atype = BELAnnotationType.LIST;
                        valuetext = valuetext.substring(1,
                                valuetext.length() - 1);
                        ad = new BELAnnotationDefinition(nametext, atype,
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
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:345:1:
    // document_property returns [BELDocumentProperty r] : (pv= 'Authors' | pv=
    // 'ContactInfo' | pv= 'Copyright' | pv= 'Description' | pv= 'Disclaimer' |
    // pv= 'Licenses' | pv= 'Name' | pv= 'Version' );
    public final BELScriptWalker.document_property_return document_property()
            throws RecognitionException {
        BELScriptWalker.document_property_return retval =
                new BELScriptWalker.document_property_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree pv = null;

        CommonTree pv_tree = null;

        try {
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:345:50:
            // (pv= 'Authors' | pv= 'ContactInfo' | pv= 'Copyright' | pv=
            // 'Description' | pv= 'Disclaimer' | pv= 'Licenses' | pv= 'Name' |
            // pv= 'Version' )
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
            case 38: {
                alt10 = 4;
            }
                break;
            case 39: {
                alt10 = 5;
            }
                break;
            case 40: {
                alt10 = 6;
            }
                break;
            case 41: {
                alt10 = 7;
            }
                break;
            case 42: {
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
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:346:5:
            // pv= 'Authors'
            {
                root_0 = (CommonTree) adaptor.nil();

                pv = (CommonTree) match(input, 35,
                        FOLLOW_35_in_document_property424);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r = BELDocumentProperty
                        .getDocumentProperty(pv.getText());

            }
                break;
            case 2:
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:347:5:
            // pv= 'ContactInfo'
            {
                root_0 = (CommonTree) adaptor.nil();

                pv = (CommonTree) match(input, 36,
                        FOLLOW_36_in_document_property440);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r = BELDocumentProperty
                        .getDocumentProperty(pv.getText());

            }
                break;
            case 3:
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:348:5:
            // pv= 'Copyright'
            {
                root_0 = (CommonTree) adaptor.nil();

                pv = (CommonTree) match(input, 37,
                        FOLLOW_37_in_document_property452);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r = BELDocumentProperty
                        .getDocumentProperty(pv.getText());

            }
                break;
            case 4:
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:349:5:
            // pv= 'Description'
            {
                root_0 = (CommonTree) adaptor.nil();

                pv = (CommonTree) match(input, 38,
                        FOLLOW_38_in_document_property466);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r = BELDocumentProperty
                        .getDocumentProperty(pv.getText());

            }
                break;
            case 5:
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:350:5:
            // pv= 'Disclaimer'
            {
                root_0 = (CommonTree) adaptor.nil();

                pv = (CommonTree) match(input, 39,
                        FOLLOW_39_in_document_property478);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r = BELDocumentProperty
                        .getDocumentProperty(pv.getText());

            }
                break;
            case 6:
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:351:5:
            // pv= 'Licenses'
            {
                root_0 = (CommonTree) adaptor.nil();

                pv = (CommonTree) match(input, 40,
                        FOLLOW_40_in_document_property491);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r = BELDocumentProperty
                        .getDocumentProperty(pv.getText());

            }
                break;
            case 7:
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:352:5:
            // pv= 'Name'
            {
                root_0 = (CommonTree) adaptor.nil();

                pv = (CommonTree) match(input, 41,
                        FOLLOW_41_in_document_property506);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r = BELDocumentProperty
                        .getDocumentProperty(pv.getText());

            }
                break;
            case 8:
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:353:5:
            // pv= 'Version'
            {
                root_0 = (CommonTree) adaptor.nil();

                pv = (CommonTree) match(input, 42,
                        FOLLOW_42_in_document_property525);
                pv_tree = (CommonTree) adaptor.dupNode(pv);

                adaptor.addChild(root_0, pv_tree);

                retval.r = BELDocumentProperty
                        .getDocumentProperty(pv.getText());

            }
                break;

            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:356:1:
    // statement : st= outer_term (rel= relationship ( ( OPEN_PAREN nst=
    // outer_term nrel= relationship not= outer_term CLOSE_PAREN ) | ot=
    // outer_term ) )? (comment= STATEMENT_COMMENT )? ;
    public final BELScriptWalker.statement_return statement()
            throws RecognitionException {
        BELScriptWalker.statement_return retval =
                new BELScriptWalker.statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

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
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:356:10:
            // (st= outer_term (rel= relationship ( ( OPEN_PAREN nst= outer_term
            // nrel= relationship not= outer_term CLOSE_PAREN ) | ot= outer_term
            // ) )? (comment= STATEMENT_COMMENT )? )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:357:5:
            // st= outer_term (rel= relationship ( ( OPEN_PAREN nst= outer_term
            // nrel= relationship not= outer_term CLOSE_PAREN ) | ot= outer_term
            // ) )? (comment= STATEMENT_COMMENT )?
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_outer_term_in_statement549);
                st = outer_term();

                state._fsp--;

                adaptor.addChild(root_0, st.getTree());
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:357:19:
                // (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel=
                // relationship not= outer_term CLOSE_PAREN ) | ot= outer_term )
                // )?
                int alt12 = 2;
                int LA12_0 = input.LA(1);

                if (((LA12_0 >= 103 && LA12_0 <= 130))) {
                    alt12 = 1;
                }
                switch (alt12) {
                case 1:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:357:20:
                // rel= relationship ( ( OPEN_PAREN nst= outer_term nrel=
                // relationship not= outer_term CLOSE_PAREN ) | ot=
                // outer_term )
                {

                    pushFollow(FOLLOW_relationship_in_statement554);
                    rel = relationship();

                    state._fsp--;

                    adaptor.addChild(root_0, rel.getTree());
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:357:37:
                    // ( ( OPEN_PAREN nst= outer_term nrel= relationship not=
                    // outer_term CLOSE_PAREN ) | ot= outer_term )
                    int alt11 = 2;
                    int LA11_0 = input.LA(1);

                    if ((LA11_0 == OPEN_PAREN)) {
                        alt11 = 1;
                    } else if (((LA11_0 >= 44 && LA11_0 <= 102))) {
                        alt11 = 2;
                    } else {
                        NoViableAltException nvae =
                                new NoViableAltException("", 11, 0, input);

                        throw nvae;
                    }
                    switch (alt11) {
                    case 1:
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:357:38:
                    // ( OPEN_PAREN nst= outer_term nrel= relationship not=
                    // outer_term CLOSE_PAREN )
                    {
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:357:38:
                        // ( OPEN_PAREN nst= outer_term nrel= relationship not=
                        // outer_term CLOSE_PAREN )
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:357:39:
                        // OPEN_PAREN nst= outer_term nrel= relationship not=
                        // outer_term CLOSE_PAREN
                        {

                            OPEN_PAREN30 = (CommonTree) match(input,
                                    OPEN_PAREN,
                                    FOLLOW_OPEN_PAREN_in_statement558);
                            OPEN_PAREN30_tree = (CommonTree) adaptor
                                    .dupNode(OPEN_PAREN30);

                            adaptor.addChild(root_0, OPEN_PAREN30_tree);

                            pushFollow(FOLLOW_outer_term_in_statement562);
                            nst = outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, nst.getTree());

                            pushFollow(FOLLOW_relationship_in_statement566);
                            nrel = relationship();

                            state._fsp--;

                            adaptor.addChild(root_0, nrel.getTree());

                            pushFollow(FOLLOW_outer_term_in_statement570);
                            not = outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, not.getTree());

                            CLOSE_PAREN31 = (CommonTree) match(input,
                                    CLOSE_PAREN,
                                    FOLLOW_CLOSE_PAREN_in_statement572);
                            CLOSE_PAREN31_tree = (CommonTree) adaptor
                                    .dupNode(CLOSE_PAREN31);

                            adaptor.addChild(root_0, CLOSE_PAREN31_tree);

                        }

                    }
                        break;
                    case 2:
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:357:113:
                    // ot= outer_term
                    {

                        pushFollow(FOLLOW_outer_term_in_statement579);
                        ot = outer_term();

                        state._fsp--;

                        adaptor.addChild(root_0, ot.getTree());

                    }
                        break;

                    }

                }
                    break;

                }

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:357:137:
                // (comment= STATEMENT_COMMENT )?
                int alt13 = 2;
                int LA13_0 = input.LA(1);

                if ((LA13_0 == STATEMENT_COMMENT)) {
                    alt13 = 1;
                }
                switch (alt13) {
                case 1:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:357:137:
                // comment= STATEMENT_COMMENT
                {

                    comment = (CommonTree) match(input, STATEMENT_COMMENT,
                            FOLLOW_STATEMENT_COMMENT_in_statement586);
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

                // build effective annotations from main statement group context
                // and then local statement group context, if any
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
                final BELStatement stmt = new BELStatement(
                        stmtBuilder.toString(), annotations, citationContext,
                        evidenceContext, commentText);
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
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:402:1:
    // outer_term returns [String r] : f= function op= OPEN_PAREN ( (c= ',' )?
    // a= argument )* cp= CLOSE_PAREN ;
    public final BELScriptWalker.outer_term_return outer_term()
            throws RecognitionException {
        BELScriptWalker.outer_term_return retval =
                new BELScriptWalker.outer_term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

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
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:406:1:
            // (f= function op= OPEN_PAREN ( (c= ',' )? a= argument )* cp=
            // CLOSE_PAREN )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:407:5:
            // f= function op= OPEN_PAREN ( (c= ',' )? a= argument )* cp=
            // CLOSE_PAREN
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_function_in_outer_term617);
                f = function();

                state._fsp--;

                adaptor.addChild(root_0, f.getTree());

                tBuilder.append(f.r);

                op = (CommonTree) match(input, OPEN_PAREN,
                        FOLLOW_OPEN_PAREN_in_outer_term623);
                op_tree = (CommonTree) adaptor.dupNode(op);

                adaptor.addChild(root_0, op_tree);

                tBuilder.append(op.getText());

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:411:7:
                // ( (c= ',' )? a= argument )*
                loop15: do {
                    int alt15 = 2;
                    int LA15_0 = input.LA(1);

                    if ((LA15_0 == OBJECT_IDENT || LA15_0 == QUOTED_VALUE
                            || LA15_0 == NS_PREFIX || (LA15_0 >= 43 && LA15_0 <= 102))) {
                        alt15 = 1;
                    }

                    switch (alt15) {
                    case 1:
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:411:8:
                    // (c= ',' )? a= argument
                    {
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:411:9:
                        // (c= ',' )?
                        int alt14 = 2;
                        int LA14_0 = input.LA(1);

                        if ((LA14_0 == 43)) {
                            alt14 = 1;
                        }
                        switch (alt14) {
                        case 1:
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:411:9:
                        // c= ','
                        {

                            c = (CommonTree) match(input, 43,
                                    FOLLOW_43_in_outer_term630);
                            c_tree = (CommonTree) adaptor.dupNode(c);

                            adaptor.addChild(root_0, c_tree);

                        }
                            break;

                        }

                        if (c != null) {
                            tBuilder.append(c.getText());
                        }

                        pushFollow(FOLLOW_argument_in_outer_term637);
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

                cp = (CommonTree) match(input, CLOSE_PAREN,
                        FOLLOW_CLOSE_PAREN_in_outer_term645);
                cp_tree = (CommonTree) adaptor.dupNode(cp);

                adaptor.addChild(root_0, cp_tree);

                tBuilder.append(cp.getText());
                retval.r = tBuilder.toString();

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:423:1:
    // argument returns [String r] : (p= param | t= term );
    public final BELScriptWalker.argument_return argument()
            throws RecognitionException {
        BELScriptWalker.argument_return retval =
                new BELScriptWalker.argument_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        BELScriptWalker.param_return p = null;

        BELScriptWalker.term_return t = null;

        try {
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:423:28:
            // (p= param | t= term )
            int alt16 = 2;
            int LA16_0 = input.LA(1);

            if ((LA16_0 == OBJECT_IDENT || LA16_0 == QUOTED_VALUE || LA16_0 == NS_PREFIX)) {
                alt16 = 1;
            } else if (((LA16_0 >= 44 && LA16_0 <= 102))) {
                alt16 = 2;
            } else {
                NoViableAltException nvae =
                        new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
            case 1:
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:424:5:
            // p= param
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_param_in_argument673);
                p = param();

                state._fsp--;

                adaptor.addChild(root_0, p.getTree());

                retval.r = p.r;

            }
                break;
            case 2:
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:427:5:
            // t= term
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_term_in_argument685);
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
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:432:1:
    // term returns [String r] : f= function op= OPEN_PAREN ( (c= ',' )? (t=
    // term | p= param ) )* cp= CLOSE_PAREN ;
    public final BELScriptWalker.term_return term() throws RecognitionException {
        BELScriptWalker.term_return retval = new BELScriptWalker.term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

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
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:436:1:
            // (f= function op= OPEN_PAREN ( (c= ',' )? (t= term | p= param ) )*
            // cp= CLOSE_PAREN )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:437:5:
            // f= function op= OPEN_PAREN ( (c= ',' )? (t= term | p= param ) )*
            // cp= CLOSE_PAREN
            {
                root_0 = (CommonTree) adaptor.nil();

                pushFollow(FOLLOW_function_in_term715);
                f = function();

                state._fsp--;

                adaptor.addChild(root_0, f.getTree());

                termBuilder.append(f.r);

                op = (CommonTree) match(input, OPEN_PAREN,
                        FOLLOW_OPEN_PAREN_in_term721);
                op_tree = (CommonTree) adaptor.dupNode(op);

                adaptor.addChild(root_0, op_tree);

                termBuilder.append(op.getText());

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:441:6:
                // ( (c= ',' )? (t= term | p= param ) )*
                loop19: do {
                    int alt19 = 2;
                    int LA19_0 = input.LA(1);

                    if ((LA19_0 == OBJECT_IDENT || LA19_0 == QUOTED_VALUE
                            || LA19_0 == NS_PREFIX || (LA19_0 >= 43 && LA19_0 <= 102))) {
                        alt19 = 1;
                    }

                    switch (alt19) {
                    case 1:
                    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:441:7:
                    // (c= ',' )? (t= term | p= param )
                    {
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:441:8:
                        // (c= ',' )?
                        int alt17 = 2;
                        int LA17_0 = input.LA(1);

                        if ((LA17_0 == 43)) {
                            alt17 = 1;
                        }
                        switch (alt17) {
                        case 1:
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:441:8:
                        // c= ','
                        {

                            c = (CommonTree) match(input, 43,
                                    FOLLOW_43_in_term727);
                            c_tree = (CommonTree) adaptor.dupNode(c);

                            adaptor.addChild(root_0, c_tree);

                        }
                            break;

                        }

                        if (c != null) {
                            termBuilder.append(c.getText());
                        }

                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:445:7:
                        // (t= term | p= param )
                        int alt18 = 2;
                        int LA18_0 = input.LA(1);

                        if (((LA18_0 >= 44 && LA18_0 <= 102))) {
                            alt18 = 1;
                        } else if ((LA18_0 == OBJECT_IDENT
                                || LA18_0 == QUOTED_VALUE || LA18_0 == NS_PREFIX)) {
                            alt18 = 2;
                        } else {
                            NoViableAltException nvae =
                                    new NoViableAltException("", 18, 0, input);

                            throw nvae;
                        }
                        switch (alt18) {
                        case 1:
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:445:8:
                        // t= term
                        {

                            pushFollow(FOLLOW_term_in_term735);
                            t = term();

                            state._fsp--;

                            adaptor.addChild(root_0, t.getTree());

                            termBuilder.append(t.r);

                        }
                            break;
                        case 2:
                        // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:447:9:
                        // p= param
                        {

                            pushFollow(FOLLOW_param_in_term743);
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

                cp = (CommonTree) match(input, CLOSE_PAREN,
                        FOLLOW_CLOSE_PAREN_in_term752);
                cp_tree = (CommonTree) adaptor.dupNode(cp);

                adaptor.addChild(root_0, cp_tree);

                termBuilder.append(cp.getText());
                retval.r = termBuilder.toString();

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:456:10:
    // fragment param returns [String r] : (nsp= NS_PREFIX )? (id= OBJECT_IDENT
    // | quo= QUOTED_VALUE ) ;
    public final BELScriptWalker.param_return param()
            throws RecognitionException {
        BELScriptWalker.param_return retval =
                new BELScriptWalker.param_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree nsp = null;
        CommonTree id = null;
        CommonTree quo = null;

        CommonTree nsp_tree = null;
        CommonTree id_tree = null;
        CommonTree quo_tree = null;

        try {
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:456:34:
            // ( (nsp= NS_PREFIX )? (id= OBJECT_IDENT | quo= QUOTED_VALUE ) )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:457:5:
            // (nsp= NS_PREFIX )? (id= OBJECT_IDENT | quo= QUOTED_VALUE )
            {
                root_0 = (CommonTree) adaptor.nil();

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:457:8:
                // (nsp= NS_PREFIX )?
                int alt20 = 2;
                int LA20_0 = input.LA(1);

                if ((LA20_0 == NS_PREFIX)) {
                    alt20 = 1;
                }
                switch (alt20) {
                case 1:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:457:8:
                // nsp= NS_PREFIX
                {

                    nsp = (CommonTree) match(input, NS_PREFIX,
                            FOLLOW_NS_PREFIX_in_param780);
                    nsp_tree = (CommonTree) adaptor.dupNode(nsp);

                    adaptor.addChild(root_0, nsp_tree);

                }
                    break;

                }

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:457:20:
                // (id= OBJECT_IDENT | quo= QUOTED_VALUE )
                int alt21 = 2;
                int LA21_0 = input.LA(1);

                if ((LA21_0 == OBJECT_IDENT)) {
                    alt21 = 1;
                } else if ((LA21_0 == QUOTED_VALUE)) {
                    alt21 = 2;
                } else {
                    NoViableAltException nvae =
                            new NoViableAltException("", 21, 0, input);

                    throw nvae;
                }
                switch (alt21) {
                case 1:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:457:21:
                // id= OBJECT_IDENT
                {

                    id = (CommonTree) match(input, OBJECT_IDENT,
                            FOLLOW_OBJECT_IDENT_in_param786);
                    id_tree = (CommonTree) adaptor.dupNode(id);

                    adaptor.addChild(root_0, id_tree);

                }
                    break;
                case 2:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:457:39:
                // quo= QUOTED_VALUE
                {

                    quo = (CommonTree) match(input, QUOTED_VALUE,
                            FOLLOW_QUOTED_VALUE_in_param792);
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
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:481:1:
    // function returns [String r] : (fv= 'proteinAbundance' | fv= 'p' | fv=
    // 'rnaAbundance' | fv= 'r' | fv= 'abundance' | fv= 'a' | fv=
    // 'microRNAAbundance' | fv= 'm' | fv= 'geneAbundance' | fv= 'g' | fv=
    // 'biologicalProcess' | fv= 'bp' | fv= 'pathology' | fv= 'path' | fv=
    // 'complexAbundance' | fv= 'complex' | fv= 'translocation' | fv= 'tloc' |
    // fv= 'cellSecretion' | fv= 'sec' | fv= 'cellSurfaceExpression' | fv=
    // 'surf' | fv= 'reaction' | fv= 'rxn' | fv= 'compositeAbundance' | fv=
    // 'composite' | fv= 'fusion' | fv= 'fus' | fv= 'degradation' | fv= 'deg' |
    // fv= 'molecularActivity' | fv= 'act' | fv= 'catalyticActivity' | fv= 'cat'
    // | fv= 'kinaseActivity' | fv= 'kin' | fv= 'phosphataseActivity' | fv=
    // 'phos' | fv= 'peptidaseActivity' | fv= 'pep' | fv= 'ribosylationActivity'
    // | fv= 'ribo' | fv= 'transcriptionalActivity' | fv= 'tscript' | fv=
    // 'transportActivity' | fv= 'tport' | fv= 'gtpBoundActivity' | fv= 'gtp' |
    // fv= 'chaperoneActivity' | fv= 'chap' | fv= 'proteinModification' | fv=
    // 'pmod' | fv= 'substitution' | fv= 'sub' | fv= 'truncation' | fv= 'trunc'
    // | fv= 'reactants' | fv= 'products' | fv= 'list' ) ;
    public final BELScriptWalker.function_return function()
            throws RecognitionException {
        BELScriptWalker.function_return retval =
                new BELScriptWalker.function_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree fv = null;

        CommonTree fv_tree = null;

        try {
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:481:28:
            // ( (fv= 'proteinAbundance' | fv= 'p' | fv= 'rnaAbundance' | fv=
            // 'r' | fv= 'abundance' | fv= 'a' | fv= 'microRNAAbundance' | fv=
            // 'm' | fv= 'geneAbundance' | fv= 'g' | fv= 'biologicalProcess' |
            // fv= 'bp' | fv= 'pathology' | fv= 'path' | fv= 'complexAbundance'
            // | fv= 'complex' | fv= 'translocation' | fv= 'tloc' | fv=
            // 'cellSecretion' | fv= 'sec' | fv= 'cellSurfaceExpression' | fv=
            // 'surf' | fv= 'reaction' | fv= 'rxn' | fv= 'compositeAbundance' |
            // fv= 'composite' | fv= 'fusion' | fv= 'fus' | fv= 'degradation' |
            // fv= 'deg' | fv= 'molecularActivity' | fv= 'act' | fv=
            // 'catalyticActivity' | fv= 'cat' | fv= 'kinaseActivity' | fv=
            // 'kin' | fv= 'phosphataseActivity' | fv= 'phos' | fv=
            // 'peptidaseActivity' | fv= 'pep' | fv= 'ribosylationActivity' |
            // fv= 'ribo' | fv= 'transcriptionalActivity' | fv= 'tscript' | fv=
            // 'transportActivity' | fv= 'tport' | fv= 'gtpBoundActivity' | fv=
            // 'gtp' | fv= 'chaperoneActivity' | fv= 'chap' | fv=
            // 'proteinModification' | fv= 'pmod' | fv= 'substitution' | fv=
            // 'sub' | fv= 'truncation' | fv= 'trunc' | fv= 'reactants' | fv=
            // 'products' | fv= 'list' ) )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:482:5:
            // (fv= 'proteinAbundance' | fv= 'p' | fv= 'rnaAbundance' | fv= 'r'
            // | fv= 'abundance' | fv= 'a' | fv= 'microRNAAbundance' | fv= 'm' |
            // fv= 'geneAbundance' | fv= 'g' | fv= 'biologicalProcess' | fv=
            // 'bp' | fv= 'pathology' | fv= 'path' | fv= 'complexAbundance' |
            // fv= 'complex' | fv= 'translocation' | fv= 'tloc' | fv=
            // 'cellSecretion' | fv= 'sec' | fv= 'cellSurfaceExpression' | fv=
            // 'surf' | fv= 'reaction' | fv= 'rxn' | fv= 'compositeAbundance' |
            // fv= 'composite' | fv= 'fusion' | fv= 'fus' | fv= 'degradation' |
            // fv= 'deg' | fv= 'molecularActivity' | fv= 'act' | fv=
            // 'catalyticActivity' | fv= 'cat' | fv= 'kinaseActivity' | fv=
            // 'kin' | fv= 'phosphataseActivity' | fv= 'phos' | fv=
            // 'peptidaseActivity' | fv= 'pep' | fv= 'ribosylationActivity' |
            // fv= 'ribo' | fv= 'transcriptionalActivity' | fv= 'tscript' | fv=
            // 'transportActivity' | fv= 'tport' | fv= 'gtpBoundActivity' | fv=
            // 'gtp' | fv= 'chaperoneActivity' | fv= 'chap' | fv=
            // 'proteinModification' | fv= 'pmod' | fv= 'substitution' | fv=
            // 'sub' | fv= 'truncation' | fv= 'trunc' | fv= 'reactants' | fv=
            // 'products' | fv= 'list' )
            {
                root_0 = (CommonTree) adaptor.nil();

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:482:5:
                // (fv= 'proteinAbundance' | fv= 'p' | fv= 'rnaAbundance' | fv=
                // 'r' | fv= 'abundance' | fv= 'a' | fv= 'microRNAAbundance' |
                // fv= 'm' | fv= 'geneAbundance' | fv= 'g' | fv=
                // 'biologicalProcess' | fv= 'bp' | fv= 'pathology' | fv= 'path'
                // | fv= 'complexAbundance' | fv= 'complex' | fv=
                // 'translocation' | fv= 'tloc' | fv= 'cellSecretion' | fv=
                // 'sec' | fv= 'cellSurfaceExpression' | fv= 'surf' | fv=
                // 'reaction' | fv= 'rxn' | fv= 'compositeAbundance' | fv=
                // 'composite' | fv= 'fusion' | fv= 'fus' | fv= 'degradation' |
                // fv= 'deg' | fv= 'molecularActivity' | fv= 'act' | fv=
                // 'catalyticActivity' | fv= 'cat' | fv= 'kinaseActivity' | fv=
                // 'kin' | fv= 'phosphataseActivity' | fv= 'phos' | fv=
                // 'peptidaseActivity' | fv= 'pep' | fv= 'ribosylationActivity'
                // | fv= 'ribo' | fv= 'transcriptionalActivity' | fv= 'tscript'
                // | fv= 'transportActivity' | fv= 'tport' | fv=
                // 'gtpBoundActivity' | fv= 'gtp' | fv= 'chaperoneActivity' |
                // fv= 'chap' | fv= 'proteinModification' | fv= 'pmod' | fv=
                // 'substitution' | fv= 'sub' | fv= 'truncation' | fv= 'trunc' |
                // fv= 'reactants' | fv= 'products' | fv= 'list' )
                int alt22 = 59;
                switch (input.LA(1)) {
                case 44: {
                    alt22 = 1;
                }
                    break;
                case 45: {
                    alt22 = 2;
                }
                    break;
                case 46: {
                    alt22 = 3;
                }
                    break;
                case 47: {
                    alt22 = 4;
                }
                    break;
                case 48: {
                    alt22 = 5;
                }
                    break;
                case 49: {
                    alt22 = 6;
                }
                    break;
                case 50: {
                    alt22 = 7;
                }
                    break;
                case 51: {
                    alt22 = 8;
                }
                    break;
                case 52: {
                    alt22 = 9;
                }
                    break;
                case 53: {
                    alt22 = 10;
                }
                    break;
                case 54: {
                    alt22 = 11;
                }
                    break;
                case 55: {
                    alt22 = 12;
                }
                    break;
                case 56: {
                    alt22 = 13;
                }
                    break;
                case 57: {
                    alt22 = 14;
                }
                    break;
                case 58: {
                    alt22 = 15;
                }
                    break;
                case 59: {
                    alt22 = 16;
                }
                    break;
                case 60: {
                    alt22 = 17;
                }
                    break;
                case 61: {
                    alt22 = 18;
                }
                    break;
                case 62: {
                    alt22 = 19;
                }
                    break;
                case 63: {
                    alt22 = 20;
                }
                    break;
                case 64: {
                    alt22 = 21;
                }
                    break;
                case 65: {
                    alt22 = 22;
                }
                    break;
                case 66: {
                    alt22 = 23;
                }
                    break;
                case 67: {
                    alt22 = 24;
                }
                    break;
                case 68: {
                    alt22 = 25;
                }
                    break;
                case 69: {
                    alt22 = 26;
                }
                    break;
                case 70: {
                    alt22 = 27;
                }
                    break;
                case 71: {
                    alt22 = 28;
                }
                    break;
                case 72: {
                    alt22 = 29;
                }
                    break;
                case 73: {
                    alt22 = 30;
                }
                    break;
                case 74: {
                    alt22 = 31;
                }
                    break;
                case 75: {
                    alt22 = 32;
                }
                    break;
                case 76: {
                    alt22 = 33;
                }
                    break;
                case 77: {
                    alt22 = 34;
                }
                    break;
                case 78: {
                    alt22 = 35;
                }
                    break;
                case 79: {
                    alt22 = 36;
                }
                    break;
                case 80: {
                    alt22 = 37;
                }
                    break;
                case 81: {
                    alt22 = 38;
                }
                    break;
                case 82: {
                    alt22 = 39;
                }
                    break;
                case 83: {
                    alt22 = 40;
                }
                    break;
                case 84: {
                    alt22 = 41;
                }
                    break;
                case 85: {
                    alt22 = 42;
                }
                    break;
                case 86: {
                    alt22 = 43;
                }
                    break;
                case 87: {
                    alt22 = 44;
                }
                    break;
                case 88: {
                    alt22 = 45;
                }
                    break;
                case 89: {
                    alt22 = 46;
                }
                    break;
                case 90: {
                    alt22 = 47;
                }
                    break;
                case 91: {
                    alt22 = 48;
                }
                    break;
                case 92: {
                    alt22 = 49;
                }
                    break;
                case 93: {
                    alt22 = 50;
                }
                    break;
                case 94: {
                    alt22 = 51;
                }
                    break;
                case 95: {
                    alt22 = 52;
                }
                    break;
                case 96: {
                    alt22 = 53;
                }
                    break;
                case 97: {
                    alt22 = 54;
                }
                    break;
                case 98: {
                    alt22 = 55;
                }
                    break;
                case 99: {
                    alt22 = 56;
                }
                    break;
                case 100: {
                    alt22 = 57;
                }
                    break;
                case 101: {
                    alt22 = 58;
                }
                    break;
                case 102: {
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
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:483:9:
                // fv= 'proteinAbundance'
                {

                    fv =
                            (CommonTree) match(input, 44,
                                    FOLLOW_44_in_function827);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 2:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:484:9:
                // fv= 'p'
                {

                    fv =
                            (CommonTree) match(input, 45,
                                    FOLLOW_45_in_function853);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 3:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:485:9:
                // fv= 'rnaAbundance'
                {

                    fv =
                            (CommonTree) match(input, 46,
                                    FOLLOW_46_in_function894);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 4:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:486:9:
                // fv= 'r'
                {

                    fv =
                            (CommonTree) match(input, 47,
                                    FOLLOW_47_in_function925);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 5:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:487:9:
                // fv= 'abundance'
                {

                    fv =
                            (CommonTree) match(input, 48,
                                    FOLLOW_48_in_function966);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 6:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:488:9:
                // fv= 'a'
                {

                    fv = (CommonTree) match(input, 49,
                            FOLLOW_49_in_function1000);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 7:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:489:9:
                // fv= 'microRNAAbundance'
                {

                    fv = (CommonTree) match(input, 50,
                            FOLLOW_50_in_function1041);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 8:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:490:9:
                // fv= 'm'
                {

                    fv = (CommonTree) match(input, 51,
                            FOLLOW_51_in_function1067);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 9:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:491:9:
                // fv= 'geneAbundance'
                {

                    fv = (CommonTree) match(input, 52,
                            FOLLOW_52_in_function1108);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 10:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:492:9:
                // fv= 'g'
                {

                    fv = (CommonTree) match(input, 53,
                            FOLLOW_53_in_function1137);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 11:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:493:9:
                // fv= 'biologicalProcess'
                {

                    fv = (CommonTree) match(input, 54,
                            FOLLOW_54_in_function1178);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 12:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:494:9:
                // fv= 'bp'
                {

                    fv = (CommonTree) match(input, 55,
                            FOLLOW_55_in_function1204);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 13:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:495:9:
                // fv= 'pathology'
                {

                    fv = (CommonTree) match(input, 56,
                            FOLLOW_56_in_function1244);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 14:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:496:9:
                // fv= 'path'
                {

                    fv = (CommonTree) match(input, 57,
                            FOLLOW_57_in_function1277);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 15:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:497:9:
                // fv= 'complexAbundance'
                {

                    fv = (CommonTree) match(input, 58,
                            FOLLOW_58_in_function1315);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 16:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:498:9:
                // fv= 'complex'
                {

                    fv = (CommonTree) match(input, 59,
                            FOLLOW_59_in_function1342);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 17:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:499:9:
                // fv= 'translocation'
                {

                    fv = (CommonTree) match(input, 60,
                            FOLLOW_60_in_function1377);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 18:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:500:9:
                // fv= 'tloc'
                {

                    fv = (CommonTree) match(input, 61,
                            FOLLOW_61_in_function1407);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 19:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:501:9:
                // fv= 'cellSecretion'
                {

                    fv = (CommonTree) match(input, 62,
                            FOLLOW_62_in_function1445);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 20:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:502:9:
                // fv= 'sec'
                {

                    fv = (CommonTree) match(input, 63,
                            FOLLOW_63_in_function1475);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 21:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:503:9:
                // fv= 'cellSurfaceExpression'
                {

                    fv = (CommonTree) match(input, 64,
                            FOLLOW_64_in_function1514);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 22:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:504:9:
                // fv= 'surf'
                {

                    fv = (CommonTree) match(input, 65,
                            FOLLOW_65_in_function1535);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 23:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:505:9:
                // fv= 'reaction'
                {

                    fv = (CommonTree) match(input, 66,
                            FOLLOW_66_in_function1573);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 24:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:506:9:
                // fv= 'rxn'
                {

                    fv = (CommonTree) match(input, 67,
                            FOLLOW_67_in_function1607);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 25:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:507:9:
                // fv= 'compositeAbundance'
                {

                    fv = (CommonTree) match(input, 68,
                            FOLLOW_68_in_function1646);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 26:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:508:9:
                // fv= 'composite'
                {

                    fv = (CommonTree) match(input, 69,
                            FOLLOW_69_in_function1670);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 27:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:509:9:
                // fv= 'fusion'
                {

                    fv = (CommonTree) match(input, 70,
                            FOLLOW_70_in_function1703);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 28:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:510:9:
                // fv= 'fus'
                {

                    fv = (CommonTree) match(input, 71,
                            FOLLOW_71_in_function1739);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 29:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:511:9:
                // fv= 'degradation'
                {

                    fv = (CommonTree) match(input, 72,
                            FOLLOW_72_in_function1778);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 30:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:512:9:
                // fv= 'deg'
                {

                    fv = (CommonTree) match(input, 73,
                            FOLLOW_73_in_function1809);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 31:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:513:9:
                // fv= 'molecularActivity'
                {

                    fv = (CommonTree) match(input, 74,
                            FOLLOW_74_in_function1848);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 32:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:514:9:
                // fv= 'act'
                {

                    fv = (CommonTree) match(input, 75,
                            FOLLOW_75_in_function1873);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 33:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:515:9:
                // fv= 'catalyticActivity'
                {

                    fv = (CommonTree) match(input, 76,
                            FOLLOW_76_in_function1912);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 34:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:516:9:
                // fv= 'cat'
                {

                    fv = (CommonTree) match(input, 77,
                            FOLLOW_77_in_function1937);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 35:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:517:9:
                // fv= 'kinaseActivity'
                {

                    fv = (CommonTree) match(input, 78,
                            FOLLOW_78_in_function1976);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 36:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:518:9:
                // fv= 'kin'
                {

                    fv = (CommonTree) match(input, 79,
                            FOLLOW_79_in_function2004);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 37:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:519:9:
                // fv= 'phosphataseActivity'
                {

                    fv = (CommonTree) match(input, 80,
                            FOLLOW_80_in_function2043);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 38:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:520:9:
                // fv= 'phos'
                {

                    fv = (CommonTree) match(input, 81,
                            FOLLOW_81_in_function2066);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 39:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:521:9:
                // fv= 'peptidaseActivity'
                {

                    fv = (CommonTree) match(input, 82,
                            FOLLOW_82_in_function2104);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 40:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:522:9:
                // fv= 'pep'
                {

                    fv = (CommonTree) match(input, 83,
                            FOLLOW_83_in_function2129);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 41:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:523:9:
                // fv= 'ribosylationActivity'
                {

                    fv = (CommonTree) match(input, 84,
                            FOLLOW_84_in_function2168);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 42:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:524:9:
                // fv= 'ribo'
                {

                    fv = (CommonTree) match(input, 85,
                            FOLLOW_85_in_function2190);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 43:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:525:9:
                // fv= 'transcriptionalActivity'
                {

                    fv = (CommonTree) match(input, 86,
                            FOLLOW_86_in_function2228);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 44:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:526:9:
                // fv= 'tscript'
                {

                    fv = (CommonTree) match(input, 87,
                            FOLLOW_87_in_function2247);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 45:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:527:9:
                // fv= 'transportActivity'
                {

                    fv = (CommonTree) match(input, 88,
                            FOLLOW_88_in_function2282);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 46:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:528:9:
                // fv= 'tport'
                {

                    fv = (CommonTree) match(input, 89,
                            FOLLOW_89_in_function2307);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 47:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:529:9:
                // fv= 'gtpBoundActivity'
                {

                    fv = (CommonTree) match(input, 90,
                            FOLLOW_90_in_function2344);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 48:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:530:9:
                // fv= 'gtp'
                {

                    fv = (CommonTree) match(input, 91,
                            FOLLOW_91_in_function2370);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 49:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:531:9:
                // fv= 'chaperoneActivity'
                {

                    fv = (CommonTree) match(input, 92,
                            FOLLOW_92_in_function2409);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 50:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:532:9:
                // fv= 'chap'
                {

                    fv = (CommonTree) match(input, 93,
                            FOLLOW_93_in_function2434);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 51:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:533:9:
                // fv= 'proteinModification'
                {

                    fv = (CommonTree) match(input, 94,
                            FOLLOW_94_in_function2472);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 52:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:534:9:
                // fv= 'pmod'
                {

                    fv = (CommonTree) match(input, 95,
                            FOLLOW_95_in_function2495);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 53:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:535:9:
                // fv= 'substitution'
                {

                    fv = (CommonTree) match(input, 96,
                            FOLLOW_96_in_function2533);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 54:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:536:9:
                // fv= 'sub'
                {

                    fv = (CommonTree) match(input, 97,
                            FOLLOW_97_in_function2563);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 55:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:537:9:
                // fv= 'truncation'
                {

                    fv = (CommonTree) match(input, 98,
                            FOLLOW_98_in_function2602);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 56:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:538:9:
                // fv= 'trunc'
                {

                    fv = (CommonTree) match(input, 99,
                            FOLLOW_99_in_function2634);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 57:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:539:9:
                // fv= 'reactants'
                {

                    fv = (CommonTree) match(input, 100,
                            FOLLOW_100_in_function2671);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 58:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:540:9:
                // fv= 'products'
                {

                    fv = (CommonTree) match(input, 101,
                            FOLLOW_101_in_function2704);
                    fv_tree = (CommonTree) adaptor.dupNode(fv);

                    adaptor.addChild(root_0, fv_tree);

                    retval.r = fv.getText();

                }
                    break;
                case 59:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:541:9:
                // fv= 'list'
                {

                    fv = (CommonTree) match(input, 102,
                            FOLLOW_102_in_function2738);
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
        } finally {}
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
    // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:545:1:
    // relationship returns [String r] : (rv= 'increases' | rv= '->' | rv=
    // 'decreases' | rv= '-|' | rv= 'directlyIncreases' | rv= '=>' | rv=
    // 'directlyDecreases' | rv= '=|' | rv= 'causesNoChange' | rv=
    // 'positiveCorrelation' | rv= 'negativeCorrelation' | rv= 'translatedTo' |
    // rv= '>>' | rv= 'transcribedTo' | rv= ':>' | rv= 'isA' | rv=
    // 'subProcessOf' | rv= 'rateLimitingStepOf' | rv= 'biomarkerFor' | rv=
    // 'prognosticBiomarkerFor' | rv= 'orthologous' | rv= 'analogous' | rv=
    // 'association' | rv= '--' | rv= 'hasMembers' | rv= 'hasComponents' | rv=
    // 'hasMember' | rv= 'hasComponent' ) ;
    public final BELScriptWalker.relationship_return relationship()
            throws RecognitionException {
        BELScriptWalker.relationship_return retval =
                new BELScriptWalker.relationship_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree rv = null;

        CommonTree rv_tree = null;

        try {
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:545:32:
            // ( (rv= 'increases' | rv= '->' | rv= 'decreases' | rv= '-|' | rv=
            // 'directlyIncreases' | rv= '=>' | rv= 'directlyDecreases' | rv=
            // '=|' | rv= 'causesNoChange' | rv= 'positiveCorrelation' | rv=
            // 'negativeCorrelation' | rv= 'translatedTo' | rv= '>>' | rv=
            // 'transcribedTo' | rv= ':>' | rv= 'isA' | rv= 'subProcessOf' | rv=
            // 'rateLimitingStepOf' | rv= 'biomarkerFor' | rv=
            // 'prognosticBiomarkerFor' | rv= 'orthologous' | rv= 'analogous' |
            // rv= 'association' | rv= '--' | rv= 'hasMembers' | rv=
            // 'hasComponents' | rv= 'hasMember' | rv= 'hasComponent' ) )
            // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:546:5:
            // (rv= 'increases' | rv= '->' | rv= 'decreases' | rv= '-|' | rv=
            // 'directlyIncreases' | rv= '=>' | rv= 'directlyDecreases' | rv=
            // '=|' | rv= 'causesNoChange' | rv= 'positiveCorrelation' | rv=
            // 'negativeCorrelation' | rv= 'translatedTo' | rv= '>>' | rv=
            // 'transcribedTo' | rv= ':>' | rv= 'isA' | rv= 'subProcessOf' | rv=
            // 'rateLimitingStepOf' | rv= 'biomarkerFor' | rv=
            // 'prognosticBiomarkerFor' | rv= 'orthologous' | rv= 'analogous' |
            // rv= 'association' | rv= '--' | rv= 'hasMembers' | rv=
            // 'hasComponents' | rv= 'hasMember' | rv= 'hasComponent' )
            {
                root_0 = (CommonTree) adaptor.nil();

                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:546:5:
                // (rv= 'increases' | rv= '->' | rv= 'decreases' | rv= '-|' |
                // rv= 'directlyIncreases' | rv= '=>' | rv= 'directlyDecreases'
                // | rv= '=|' | rv= 'causesNoChange' | rv= 'positiveCorrelation'
                // | rv= 'negativeCorrelation' | rv= 'translatedTo' | rv= '>>' |
                // rv= 'transcribedTo' | rv= ':>' | rv= 'isA' | rv=
                // 'subProcessOf' | rv= 'rateLimitingStepOf' | rv=
                // 'biomarkerFor' | rv= 'prognosticBiomarkerFor' | rv=
                // 'orthologous' | rv= 'analogous' | rv= 'association' | rv=
                // '--' | rv= 'hasMembers' | rv= 'hasComponents' | rv=
                // 'hasMember' | rv= 'hasComponent' )
                int alt23 = 28;
                switch (input.LA(1)) {
                case 103: {
                    alt23 = 1;
                }
                    break;
                case 104: {
                    alt23 = 2;
                }
                    break;
                case 105: {
                    alt23 = 3;
                }
                    break;
                case 106: {
                    alt23 = 4;
                }
                    break;
                case 107: {
                    alt23 = 5;
                }
                    break;
                case 108: {
                    alt23 = 6;
                }
                    break;
                case 109: {
                    alt23 = 7;
                }
                    break;
                case 110: {
                    alt23 = 8;
                }
                    break;
                case 111: {
                    alt23 = 9;
                }
                    break;
                case 112: {
                    alt23 = 10;
                }
                    break;
                case 113: {
                    alt23 = 11;
                }
                    break;
                case 114: {
                    alt23 = 12;
                }
                    break;
                case 115: {
                    alt23 = 13;
                }
                    break;
                case 116: {
                    alt23 = 14;
                }
                    break;
                case 117: {
                    alt23 = 15;
                }
                    break;
                case 118: {
                    alt23 = 16;
                }
                    break;
                case 119: {
                    alt23 = 17;
                }
                    break;
                case 120: {
                    alt23 = 18;
                }
                    break;
                case 121: {
                    alt23 = 19;
                }
                    break;
                case 122: {
                    alt23 = 20;
                }
                    break;
                case 123: {
                    alt23 = 21;
                }
                    break;
                case 124: {
                    alt23 = 22;
                }
                    break;
                case 125: {
                    alt23 = 23;
                }
                    break;
                case 126: {
                    alt23 = 24;
                }
                    break;
                case 127: {
                    alt23 = 25;
                }
                    break;
                case 128: {
                    alt23 = 26;
                }
                    break;
                case 129: {
                    alt23 = 27;
                }
                    break;
                case 130: {
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
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:547:9:
                // rv= 'increases'
                {

                    rv = (CommonTree) match(input, 103,
                            FOLLOW_103_in_relationship2804);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 2:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:548:9:
                // rv= '->'
                {

                    rv = (CommonTree) match(input, 104,
                            FOLLOW_104_in_relationship2838);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 3:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:549:9:
                // rv= 'decreases'
                {

                    rv = (CommonTree) match(input, 105,
                            FOLLOW_105_in_relationship2878);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 4:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:550:9:
                // rv= '-|'
                {

                    rv = (CommonTree) match(input, 106,
                            FOLLOW_106_in_relationship2912);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 5:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:551:9:
                // rv= 'directlyIncreases'
                {

                    rv = (CommonTree) match(input, 107,
                            FOLLOW_107_in_relationship2952);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 6:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:552:9:
                // rv= '=>'
                {

                    rv = (CommonTree) match(input, 108,
                            FOLLOW_108_in_relationship2977);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 7:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:553:9:
                // rv= 'directlyDecreases'
                {

                    rv = (CommonTree) match(input, 109,
                            FOLLOW_109_in_relationship3017);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 8:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:554:9:
                // rv= '=|'
                {

                    rv = (CommonTree) match(input, 110,
                            FOLLOW_110_in_relationship3042);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 9:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:555:9:
                // rv= 'causesNoChange'
                {

                    rv = (CommonTree) match(input, 111,
                            FOLLOW_111_in_relationship3082);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 10:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:556:9:
                // rv= 'positiveCorrelation'
                {

                    rv = (CommonTree) match(input, 112,
                            FOLLOW_112_in_relationship3110);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 11:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:557:9:
                // rv= 'negativeCorrelation'
                {

                    rv = (CommonTree) match(input, 113,
                            FOLLOW_113_in_relationship3133);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 12:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:558:9:
                // rv= 'translatedTo'
                {

                    rv = (CommonTree) match(input, 114,
                            FOLLOW_114_in_relationship3156);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 13:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:559:9:
                // rv= '>>'
                {

                    rv = (CommonTree) match(input, 115,
                            FOLLOW_115_in_relationship3186);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 14:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:560:9:
                // rv= 'transcribedTo'
                {

                    rv = (CommonTree) match(input, 116,
                            FOLLOW_116_in_relationship3226);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 15:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:561:9:
                // rv= ':>'
                {

                    rv = (CommonTree) match(input, 117,
                            FOLLOW_117_in_relationship3255);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 16:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:562:9:
                // rv= 'isA'
                {

                    rv = (CommonTree) match(input, 118,
                            FOLLOW_118_in_relationship3295);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 17:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:563:9:
                // rv= 'subProcessOf'
                {

                    rv = (CommonTree) match(input, 119,
                            FOLLOW_119_in_relationship3334);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 18:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:564:9:
                // rv= 'rateLimitingStepOf'
                {

                    rv = (CommonTree) match(input, 120,
                            FOLLOW_120_in_relationship3364);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 19:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:565:9:
                // rv= 'biomarkerFor'
                {

                    rv = (CommonTree) match(input, 121,
                            FOLLOW_121_in_relationship3388);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 20:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:566:9:
                // rv= 'prognosticBiomarkerFor'
                {

                    rv = (CommonTree) match(input, 122,
                            FOLLOW_122_in_relationship3418);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 21:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:567:9:
                // rv= 'orthologous'
                {

                    rv = (CommonTree) match(input, 123,
                            FOLLOW_123_in_relationship3438);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 22:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:568:9:
                // rv= 'analogous'
                {

                    rv = (CommonTree) match(input, 124,
                            FOLLOW_124_in_relationship3469);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 23:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:569:9:
                // rv= 'association'
                {

                    rv = (CommonTree) match(input, 125,
                            FOLLOW_125_in_relationship3502);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 24:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:570:9:
                // rv= '--'
                {

                    rv = (CommonTree) match(input, 126,
                            FOLLOW_126_in_relationship3533);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 25:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:571:9:
                // rv= 'hasMembers'
                {

                    rv = (CommonTree) match(input, 127,
                            FOLLOW_127_in_relationship3573);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 26:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:572:9:
                // rv= 'hasComponents'
                {

                    rv = (CommonTree) match(input, 128,
                            FOLLOW_128_in_relationship3605);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 27:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:573:9:
                // rv= 'hasMember'
                {

                    rv = (CommonTree) match(input, 129,
                            FOLLOW_129_in_relationship3634);
                    rv_tree = (CommonTree) adaptor.dupNode(rv);

                    adaptor.addChild(root_0, rv_tree);

                    retval.r = rv.getText();

                }
                    break;
                case 28:
                // /home/abargnesi/projects/belfw/trunk/docs/bel/grammar/BELScriptWalker.g:574:9:
                // rv= 'hasComponent'
                {

                    rv = (CommonTree) match(input, 130,
                            FOLLOW_130_in_relationship3667);
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
        } finally {}
        return retval;
    }

    // $ANTLR end "relationship"

    // Delegated rules

    protected DFA2 dfa2 = new DFA2(this);
    static final String DFA2_eotS =
            "\14\uffff";
    static final String DFA2_eofS =
            "\14\uffff";
    static final String DFA2_minS =
            "\1\30\1\34\1\6\1\7\10\uffff";
    static final String DFA2_maxS =
            "\1\146\1\40\1\12\1\13\10\uffff";
    static final String DFA2_acceptS =
            "\4\uffff\1\10\1\2\1\1\1\3\1\4\1\5\1\6\1\7";
    static final String DFA2_specialS =
            "\14\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\2\1\uffff\1\3\1\1\20\uffff\73\4",
            "\2\6\2\uffff\1\5",
            "\1\10\1\7\2\uffff\1\11",
            "\1\13\2\uffff\1\12\1\13",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA
            .unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA
            .unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }

        @Override
        public String getDescription() {
            return "118:5: ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement )";
        }
    }

    public static final BitSet FOLLOW_NEWLINE_in_document67 = new BitSet(
            new long[] { 0xFFFFF0000D000030L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_DOCUMENT_COMMENT_in_document71 =
            new BitSet(
                    new long[] { 0xFFFFF0000D000030L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_record_in_document75 = new BitSet(
            new long[] { 0xFFFFF0000D000030L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_EOF_in_document79 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_define_namespace_in_record98 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_define_annotation_in_record102 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_annotation_in_record106 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_document_in_record110 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_statement_group_in_record114 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_unset_statement_group_in_record118 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_unset_in_record122 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_statement_in_record126 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_24_in_set_document144 = new BitSet(
            new long[] { 0x0000000000000040L });
    public static final BitSet FOLLOW_DOCUMENT_KEYWORD_in_set_document148 =
            new BitSet(
                    new long[] { 0x000007F800000000L });
    public static final BitSet FOLLOW_document_property_in_set_document153 =
            new BitSet(
                    new long[] { 0x0000000002000000L });
    public static final BitSet FOLLOW_25_in_set_document155 = new BitSet(
            new long[] { 0x0000000000000280L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_set_document160 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_document166 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_24_in_set_statement_group185 =
            new BitSet(
                    new long[] { 0x0000000000000400L });
    public static final BitSet FOLLOW_STATEMENT_GROUP_KEYWORD_in_set_statement_group187 =
            new BitSet(
                    new long[] { 0x0000000002000000L });
    public static final BitSet FOLLOW_25_in_set_statement_group189 =
            new BitSet(
                    new long[] { 0x0000000000000280L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_set_statement_group194 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_statement_group200 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_24_in_set_annotation219 = new BitSet(
            new long[] { 0x0000000000000080L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_annotation223 =
            new BitSet(
                    new long[] { 0x0000000002000000L });
    public static final BitSet FOLLOW_25_in_set_annotation225 = new BitSet(
            new long[] { 0x0000000000000380L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_set_annotation230 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_VALUE_LIST_in_set_annotation236 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_annotation242 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_26_in_unset_statement_group265 =
            new BitSet(
                    new long[] { 0x0000000000000400L });
    public static final BitSet FOLLOW_STATEMENT_GROUP_KEYWORD_in_unset_statement_group267 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_26_in_unset285 = new BitSet(
            new long[] { 0x0000000000000880L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_unset290 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IDENT_LIST_in_unset296 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_27_in_define_namespace316 = new BitSet(
            new long[] { 0x0000000030000000L });
    public static final BitSet FOLLOW_28_in_define_namespace322 = new BitSet(
            new long[] { 0x0000000020000000L });
    public static final BitSet FOLLOW_29_in_define_namespace326 = new BitSet(
            new long[] { 0x0000000000000080L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_define_namespace332 =
            new BitSet(
                    new long[] { 0x0000000040000000L });
    public static final BitSet FOLLOW_30_in_define_namespace334 = new BitSet(
            new long[] { 0x0000000080000000L });
    public static final BitSet FOLLOW_31_in_define_namespace336 = new BitSet(
            new long[] { 0x0000000000000200L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_define_namespace340 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_27_in_define_annotation359 = new BitSet(
            new long[] { 0x0000000100000000L });
    public static final BitSet FOLLOW_32_in_define_annotation361 = new BitSet(
            new long[] { 0x0000000000000080L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_define_annotation366 =
            new BitSet(
                    new long[] { 0x0000000040000000L });
    public static final BitSet FOLLOW_30_in_define_annotation368 = new BitSet(
            new long[] { 0x0000000680000000L });
    public static final BitSet FOLLOW_31_in_define_annotation375 = new BitSet(
            new long[] { 0x0000000000000200L });
    public static final BitSet FOLLOW_33_in_define_annotation381 = new BitSet(
            new long[] { 0x0000000000000200L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_define_annotation386 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_34_in_define_annotation394 = new BitSet(
            new long[] { 0x0000000000000100L });
    public static final BitSet FOLLOW_VALUE_LIST_in_define_annotation398 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_35_in_document_property424 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_36_in_document_property440 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_37_in_document_property452 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_38_in_document_property466 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_39_in_document_property478 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_40_in_document_property491 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_41_in_document_property506 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_42_in_document_property525 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_outer_term_in_statement549 = new BitSet(
            new long[] { 0x0000000000004002L, 0xFFFFFF8000000000L,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_relationship_in_statement554 =
            new BitSet(
                    new long[] { 0xFFFFF0000D001030L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_OPEN_PAREN_in_statement558 = new BitSet(
            new long[] { 0xFFFFF0000D000030L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_outer_term_in_statement562 = new BitSet(
            new long[] { 0x0000000000000000L, 0xFFFFFF8000000000L,
                    0x0000000000000007L });
    public static final BitSet FOLLOW_relationship_in_statement566 =
            new BitSet(
                    new long[] { 0xFFFFF0000D000030L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_outer_term_in_statement570 = new BitSet(
            new long[] { 0x0000000000002000L });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_statement572 = new BitSet(
            new long[] { 0x0000000000004002L });
    public static final BitSet FOLLOW_outer_term_in_statement579 = new BitSet(
            new long[] { 0x0000000000004002L });
    public static final BitSet FOLLOW_STATEMENT_COMMENT_in_statement586 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_function_in_outer_term617 = new BitSet(
            new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_OPEN_PAREN_in_outer_term623 = new BitSet(
            new long[] { 0xFFFFF8000D00A2B0L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_43_in_outer_term630 = new BitSet(
            new long[] { 0xFFFFF8000D0082B0L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_argument_in_outer_term637 = new BitSet(
            new long[] { 0xFFFFF8000D00A2B0L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_outer_term645 =
            new BitSet(
                    new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_param_in_argument673 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_term_in_argument685 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_function_in_term715 = new BitSet(
            new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_OPEN_PAREN_in_term721 = new BitSet(
            new long[] { 0xFFFFF8000D00A2B0L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_43_in_term727 = new BitSet(new long[] {
            0xFFFFF8000D0082B0L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_term_in_term735 = new BitSet(new long[] {
            0xFFFFF8000D00A2B0L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_param_in_term743 = new BitSet(new long[] {
            0xFFFFF8000D00A2B0L, 0x0000007FFFFFFFFFL });
    public static final BitSet FOLLOW_CLOSE_PAREN_in_term752 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_NS_PREFIX_in_param780 = new BitSet(
            new long[] { 0x0000000000000280L });
    public static final BitSet FOLLOW_OBJECT_IDENT_in_param786 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_QUOTED_VALUE_in_param792 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_44_in_function827 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_45_in_function853 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_46_in_function894 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_47_in_function925 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_48_in_function966 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_49_in_function1000 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_50_in_function1041 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_51_in_function1067 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_52_in_function1108 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_53_in_function1137 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_54_in_function1178 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_55_in_function1204 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_56_in_function1244 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_57_in_function1277 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_58_in_function1315 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_59_in_function1342 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_60_in_function1377 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_61_in_function1407 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_62_in_function1445 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_63_in_function1475 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_64_in_function1514 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_65_in_function1535 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_66_in_function1573 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_67_in_function1607 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_68_in_function1646 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_69_in_function1670 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_70_in_function1703 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_71_in_function1739 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_72_in_function1778 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_73_in_function1809 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_74_in_function1848 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_75_in_function1873 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_76_in_function1912 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_77_in_function1937 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_78_in_function1976 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_79_in_function2004 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_80_in_function2043 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_81_in_function2066 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_82_in_function2104 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_83_in_function2129 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_84_in_function2168 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_85_in_function2190 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_86_in_function2228 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_87_in_function2247 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_88_in_function2282 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_89_in_function2307 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_90_in_function2344 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_91_in_function2370 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_92_in_function2409 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_93_in_function2434 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_94_in_function2472 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_95_in_function2495 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_96_in_function2533 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_97_in_function2563 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_98_in_function2602 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_99_in_function2634 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_100_in_function2671 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_101_in_function2704 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_102_in_function2738 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_103_in_relationship2804 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_104_in_relationship2838 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_105_in_relationship2878 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_106_in_relationship2912 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_107_in_relationship2952 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_108_in_relationship2977 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_109_in_relationship3017 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_110_in_relationship3042 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_111_in_relationship3082 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_112_in_relationship3110 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_113_in_relationship3133 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_114_in_relationship3156 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_115_in_relationship3186 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_116_in_relationship3226 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_117_in_relationship3255 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_118_in_relationship3295 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_119_in_relationship3334 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_120_in_relationship3364 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_121_in_relationship3388 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_122_in_relationship3418 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_123_in_relationship3438 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_124_in_relationship3469 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_125_in_relationship3502 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_126_in_relationship3533 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_127_in_relationship3573 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_128_in_relationship3605 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_129_in_relationship3634 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_130_in_relationship3667 = new BitSet(
            new long[] { 0x0000000000000002L });

}
