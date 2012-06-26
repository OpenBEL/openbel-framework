// $ANTLR 3.4 BELScript.g 2012-06-26 18:53:52

    package org.openbel.framework.common.bel.parser;
    
    import java.util.List;
    import java.util.ArrayList;
    import java.util.Stack;
    
    import org.openbel.bel.model.BELParseErrorException;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class BELScriptParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CLOSE_PAREN", "COMMA", "DIGIT", "DOCUMENT_COMMENT", "DOCUMENT_KEYWORD", "EscapeSequence", "HexDigit", "IDENT_LIST", "LETTER", "NEWLINE", "NS_PREFIX", "OBJECT_IDENT", "OPEN_PAREN", "OctalEscape", "QUOTED_VALUE", "STATEMENT_COMMENT", "STATEMENT_GROUP_KEYWORD", "UnicodeEscape", "VALUE_LIST", "WS", "','", "'--'", "'->'", "'-|'", "':>'", "'='", "'=>'", "'=|'", "'>>'", "'ANNOTATION'", "'AS'", "'Authors'", "'ContactInfo'", "'Copyright'", "'DEFAULT'", "'DEFINE'", "'Description'", "'Disclaimer'", "'LIST'", "'Licenses'", "'NAMESPACE'", "'Name'", "'PATTERN'", "'SET'", "'UNSET'", "'URL'", "'Version'", "'a'", "'abundance'", "'act'", "'analogous'", "'association'", "'biologicalProcess'", "'biomarkerFor'", "'bp'", "'cat'", "'catalyticActivity'", "'causesNoChange'", "'cellSecretion'", "'cellSurfaceExpression'", "'chap'", "'chaperoneActivity'", "'complex'", "'complexAbundance'", "'composite'", "'compositeAbundance'", "'decreases'", "'deg'", "'degradation'", "'directlyDecreases'", "'directlyIncreases'", "'fus'", "'fusion'", "'g'", "'geneAbundance'", "'gtp'", "'gtpBoundActivity'", "'hasComponent'", "'hasComponents'", "'hasMember'", "'hasMembers'", "'increases'", "'isA'", "'kin'", "'kinaseActivity'", "'list'", "'m'", "'microRNAAbundance'", "'molecularActivity'", "'negativeCorrelation'", "'orthologous'", "'p'", "'path'", "'pathology'", "'pep'", "'peptidaseActivity'", "'phos'", "'phosphataseActivity'", "'pmod'", "'positiveCorrelation'", "'products'", "'prognosticBiomarkerFor'", "'proteinAbundance'", "'proteinModification'", "'r'", "'rateLimitingStepOf'", "'reactants'", "'reaction'", "'ribo'", "'ribosylationActivity'", "'rnaAbundance'", "'rxn'", "'sec'", "'sub'", "'subProcessOf'", "'substitution'", "'surf'", "'tloc'", "'tport'", "'transcribedTo'", "'transcriptionalActivity'", "'translatedTo'", "'translocation'", "'transportActivity'", "'trunc'", "'truncation'", "'tscript'"
    };

    public static final int EOF=-1;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__50=50;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__59=59;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__73=73;
    public static final int T__74=74;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int T__77=77;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__84=84;
    public static final int T__85=85;
    public static final int T__86=86;
    public static final int T__87=87;
    public static final int T__88=88;
    public static final int T__89=89;
    public static final int T__90=90;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int T__95=95;
    public static final int T__96=96;
    public static final int T__97=97;
    public static final int T__98=98;
    public static final int T__99=99;
    public static final int T__100=100;
    public static final int T__101=101;
    public static final int T__102=102;
    public static final int T__103=103;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int T__110=110;
    public static final int T__111=111;
    public static final int T__112=112;
    public static final int T__113=113;
    public static final int T__114=114;
    public static final int T__115=115;
    public static final int T__116=116;
    public static final int T__117=117;
    public static final int T__118=118;
    public static final int T__119=119;
    public static final int T__120=120;
    public static final int T__121=121;
    public static final int T__122=122;
    public static final int T__123=123;
    public static final int T__124=124;
    public static final int T__125=125;
    public static final int T__126=126;
    public static final int T__127=127;
    public static final int T__128=128;
    public static final int T__129=129;
    public static final int T__130=130;
    public static final int CLOSE_PAREN=4;
    public static final int COMMA=5;
    public static final int DIGIT=6;
    public static final int DOCUMENT_COMMENT=7;
    public static final int DOCUMENT_KEYWORD=8;
    public static final int EscapeSequence=9;
    public static final int HexDigit=10;
    public static final int IDENT_LIST=11;
    public static final int LETTER=12;
    public static final int NEWLINE=13;
    public static final int NS_PREFIX=14;
    public static final int OBJECT_IDENT=15;
    public static final int OPEN_PAREN=16;
    public static final int OctalEscape=17;
    public static final int QUOTED_VALUE=18;
    public static final int STATEMENT_COMMENT=19;
    public static final int STATEMENT_GROUP_KEYWORD=20;
    public static final int UnicodeEscape=21;
    public static final int VALUE_LIST=22;
    public static final int WS=23;

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
    public String[] getTokenNames() { return BELScriptParser.tokenNames; }
    public String getGrammarFileName() { return "BELScript.g"; }


        private final List<BELParseErrorException> syntaxErrors = new ArrayList<BELParseErrorException>();
        private final Stack<String> paraphrases = new Stack<String>();
        
        public List<BELParseErrorException> getSyntaxErrors() {
            return syntaxErrors;
        }
        
        @Override
        public void emitErrorMessage(String msg) {
        }
        
        @Override
        public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
            String context = "";
            if (paraphrases.size() > 0) {
                context = paraphrases.peek();
            }
            syntaxErrors.add(new BELParseErrorException.SyntaxException(e.line, e.charPositionInLine, context, e));
        }


    public static class document_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "document"
    // BELScript.g:44:1: document : ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF ;
    public final BELScriptParser.document_return document() throws RecognitionException {
        BELScriptParser.document_return retval = new BELScriptParser.document_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token NEWLINE1=null;
        Token DOCUMENT_COMMENT2=null;
        Token EOF4=null;
        BELScriptParser.record_return record3 =null;


        Object NEWLINE1_tree=null;
        Object DOCUMENT_COMMENT2_tree=null;
        Object EOF4_tree=null;

        try {
            // BELScript.g:44:9: ( ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF )
            // BELScript.g:45:5: ( NEWLINE | DOCUMENT_COMMENT | record )+ EOF
            {
            root_0 = (Object)adaptor.nil();


            // BELScript.g:45:5: ( NEWLINE | DOCUMENT_COMMENT | record )+
            int cnt1=0;
            loop1:
            do {
                int alt1=4;
                switch ( input.LA(1) ) {
                case NEWLINE:
                    {
                    alt1=1;
                    }
                    break;
                case DOCUMENT_COMMENT:
                    {
                    alt1=2;
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
                case 130:
                    {
                    alt1=3;
                    }
                    break;

                }

                switch (alt1) {
            	case 1 :
            	    // BELScript.g:45:6: NEWLINE
            	    {
            	    NEWLINE1=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_document62); 
            	    NEWLINE1_tree = 
            	    (Object)adaptor.create(NEWLINE1)
            	    ;
            	    adaptor.addChild(root_0, NEWLINE1_tree);


            	    }
            	    break;
            	case 2 :
            	    // BELScript.g:45:16: DOCUMENT_COMMENT
            	    {
            	    DOCUMENT_COMMENT2=(Token)match(input,DOCUMENT_COMMENT,FOLLOW_DOCUMENT_COMMENT_in_document66); 
            	    DOCUMENT_COMMENT2_tree = 
            	    (Object)adaptor.create(DOCUMENT_COMMENT2)
            	    ;
            	    adaptor.addChild(root_0, DOCUMENT_COMMENT2_tree);


            	    }
            	    break;
            	case 3 :
            	    // BELScript.g:45:35: record
            	    {
            	    pushFollow(FOLLOW_record_in_document70);
            	    record3=record();

            	    state._fsp--;

            	    adaptor.addChild(root_0, record3.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            EOF4=(Token)match(input,EOF,FOLLOW_EOF_in_document74); 
            EOF4_tree = 
            (Object)adaptor.create(EOF4)
            ;
            adaptor.addChild(root_0, EOF4_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "document"


    public static class record_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "record"
    // BELScript.g:48:1: record : ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement ) ;
    public final BELScriptParser.record_return record() throws RecognitionException {
        BELScriptParser.record_return retval = new BELScriptParser.record_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        BELScriptParser.define_namespace_return define_namespace5 =null;

        BELScriptParser.define_annotation_return define_annotation6 =null;

        BELScriptParser.set_annotation_return set_annotation7 =null;

        BELScriptParser.set_document_return set_document8 =null;

        BELScriptParser.set_statement_group_return set_statement_group9 =null;

        BELScriptParser.unset_statement_group_return unset_statement_group10 =null;

        BELScriptParser.unset_return unset11 =null;

        BELScriptParser.statement_return statement12 =null;



        try {
            // BELScript.g:48:7: ( ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement ) )
            // BELScript.g:49:5: ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement )
            {
            root_0 = (Object)adaptor.nil();


            // BELScript.g:49:5: ( define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement )
            int alt2=8;
            switch ( input.LA(1) ) {
            case 39:
                {
                int LA2_1 = input.LA(2);

                if ( (LA2_1==33) ) {
                    alt2=2;
                }
                else if ( (LA2_1==38||LA2_1==44) ) {
                    alt2=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;

                }
                }
                break;
            case 47:
                {
                switch ( input.LA(2) ) {
                case OBJECT_IDENT:
                    {
                    alt2=3;
                    }
                    break;
                case DOCUMENT_KEYWORD:
                    {
                    alt2=4;
                    }
                    break;
                case STATEMENT_GROUP_KEYWORD:
                    {
                    alt2=5;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 2, input);

                    throw nvae;

                }

                }
                break;
            case 48:
                {
                int LA2_3 = input.LA(2);

                if ( (LA2_3==STATEMENT_GROUP_KEYWORD) ) {
                    alt2=6;
                }
                else if ( (LA2_3==IDENT_LIST||LA2_3==OBJECT_IDENT) ) {
                    alt2=7;
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
            case 130:
                {
                alt2=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }

            switch (alt2) {
                case 1 :
                    // BELScript.g:49:6: define_namespace
                    {
                    pushFollow(FOLLOW_define_namespace_in_record91);
                    define_namespace5=define_namespace();

                    state._fsp--;

                    adaptor.addChild(root_0, define_namespace5.getTree());

                    }
                    break;
                case 2 :
                    // BELScript.g:49:25: define_annotation
                    {
                    pushFollow(FOLLOW_define_annotation_in_record95);
                    define_annotation6=define_annotation();

                    state._fsp--;

                    adaptor.addChild(root_0, define_annotation6.getTree());

                    }
                    break;
                case 3 :
                    // BELScript.g:49:45: set_annotation
                    {
                    pushFollow(FOLLOW_set_annotation_in_record99);
                    set_annotation7=set_annotation();

                    state._fsp--;

                    adaptor.addChild(root_0, set_annotation7.getTree());

                    }
                    break;
                case 4 :
                    // BELScript.g:49:62: set_document
                    {
                    pushFollow(FOLLOW_set_document_in_record103);
                    set_document8=set_document();

                    state._fsp--;

                    adaptor.addChild(root_0, set_document8.getTree());

                    }
                    break;
                case 5 :
                    // BELScript.g:49:77: set_statement_group
                    {
                    pushFollow(FOLLOW_set_statement_group_in_record107);
                    set_statement_group9=set_statement_group();

                    state._fsp--;

                    adaptor.addChild(root_0, set_statement_group9.getTree());

                    }
                    break;
                case 6 :
                    // BELScript.g:49:99: unset_statement_group
                    {
                    pushFollow(FOLLOW_unset_statement_group_in_record111);
                    unset_statement_group10=unset_statement_group();

                    state._fsp--;

                    adaptor.addChild(root_0, unset_statement_group10.getTree());

                    }
                    break;
                case 7 :
                    // BELScript.g:49:123: unset
                    {
                    pushFollow(FOLLOW_unset_in_record115);
                    unset11=unset();

                    state._fsp--;

                    adaptor.addChild(root_0, unset11.getTree());

                    }
                    break;
                case 8 :
                    // BELScript.g:49:131: statement
                    {
                    pushFollow(FOLLOW_statement_in_record119);
                    statement12=statement();

                    state._fsp--;

                    adaptor.addChild(root_0, statement12.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "record"


    public static class set_document_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "set_document"
    // BELScript.g:52:1: set_document : ( 'SET' DOCUMENT_KEYWORD ) document_property '=' ( OBJECT_IDENT | VALUE_LIST | QUOTED_VALUE ) ;
    public final BELScriptParser.set_document_return set_document() throws RecognitionException {
        BELScriptParser.set_document_return retval = new BELScriptParser.set_document_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal13=null;
        Token DOCUMENT_KEYWORD14=null;
        Token char_literal16=null;
        Token set17=null;
        BELScriptParser.document_property_return document_property15 =null;


        Object string_literal13_tree=null;
        Object DOCUMENT_KEYWORD14_tree=null;
        Object char_literal16_tree=null;
        Object set17_tree=null;

         paraphrases.push("in set document."); 
        try {
            // BELScript.g:55:5: ( ( 'SET' DOCUMENT_KEYWORD ) document_property '=' ( OBJECT_IDENT | VALUE_LIST | QUOTED_VALUE ) )
            // BELScript.g:56:5: ( 'SET' DOCUMENT_KEYWORD ) document_property '=' ( OBJECT_IDENT | VALUE_LIST | QUOTED_VALUE )
            {
            root_0 = (Object)adaptor.nil();


            // BELScript.g:56:5: ( 'SET' DOCUMENT_KEYWORD )
            // BELScript.g:56:6: 'SET' DOCUMENT_KEYWORD
            {
            string_literal13=(Token)match(input,47,FOLLOW_47_in_set_document160); 
            string_literal13_tree = 
            (Object)adaptor.create(string_literal13)
            ;
            adaptor.addChild(root_0, string_literal13_tree);


            DOCUMENT_KEYWORD14=(Token)match(input,DOCUMENT_KEYWORD,FOLLOW_DOCUMENT_KEYWORD_in_set_document162); 
            DOCUMENT_KEYWORD14_tree = 
            (Object)adaptor.create(DOCUMENT_KEYWORD14)
            ;
            adaptor.addChild(root_0, DOCUMENT_KEYWORD14_tree);


            }


            pushFollow(FOLLOW_document_property_in_set_document165);
            document_property15=document_property();

            state._fsp--;

            adaptor.addChild(root_0, document_property15.getTree());

            char_literal16=(Token)match(input,29,FOLLOW_29_in_set_document167); 
            char_literal16_tree = 
            (Object)adaptor.create(char_literal16)
            ;
            adaptor.addChild(root_0, char_literal16_tree);


            set17=(Token)input.LT(1);

            if ( input.LA(1)==OBJECT_IDENT||input.LA(1)==QUOTED_VALUE||input.LA(1)==VALUE_LIST ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set17)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

             paraphrases.pop(); 
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "set_document"


    public static class set_statement_group_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "set_statement_group"
    // BELScript.g:59:1: set_statement_group : 'SET' STATEMENT_GROUP_KEYWORD '=' ( QUOTED_VALUE | OBJECT_IDENT ) ;
    public final BELScriptParser.set_statement_group_return set_statement_group() throws RecognitionException {
        BELScriptParser.set_statement_group_return retval = new BELScriptParser.set_statement_group_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal18=null;
        Token STATEMENT_GROUP_KEYWORD19=null;
        Token char_literal20=null;
        Token set21=null;

        Object string_literal18_tree=null;
        Object STATEMENT_GROUP_KEYWORD19_tree=null;
        Object char_literal20_tree=null;
        Object set21_tree=null;

         paraphrases.push("in set statement group."); 
        try {
            // BELScript.g:62:5: ( 'SET' STATEMENT_GROUP_KEYWORD '=' ( QUOTED_VALUE | OBJECT_IDENT ) )
            // BELScript.g:63:5: 'SET' STATEMENT_GROUP_KEYWORD '=' ( QUOTED_VALUE | OBJECT_IDENT )
            {
            root_0 = (Object)adaptor.nil();


            string_literal18=(Token)match(input,47,FOLLOW_47_in_set_statement_group218); 
            string_literal18_tree = 
            (Object)adaptor.create(string_literal18)
            ;
            adaptor.addChild(root_0, string_literal18_tree);


            STATEMENT_GROUP_KEYWORD19=(Token)match(input,STATEMENT_GROUP_KEYWORD,FOLLOW_STATEMENT_GROUP_KEYWORD_in_set_statement_group220); 
            STATEMENT_GROUP_KEYWORD19_tree = 
            (Object)adaptor.create(STATEMENT_GROUP_KEYWORD19)
            ;
            adaptor.addChild(root_0, STATEMENT_GROUP_KEYWORD19_tree);


            char_literal20=(Token)match(input,29,FOLLOW_29_in_set_statement_group222); 
            char_literal20_tree = 
            (Object)adaptor.create(char_literal20)
            ;
            adaptor.addChild(root_0, char_literal20_tree);


            set21=(Token)input.LT(1);

            if ( input.LA(1)==OBJECT_IDENT||input.LA(1)==QUOTED_VALUE ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set21)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

             paraphrases.pop(); 
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "set_statement_group"


    public static class set_annotation_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "set_annotation"
    // BELScript.g:66:1: set_annotation : 'SET' OBJECT_IDENT '=' ( QUOTED_VALUE | VALUE_LIST | OBJECT_IDENT ) ;
    public final BELScriptParser.set_annotation_return set_annotation() throws RecognitionException {
        BELScriptParser.set_annotation_return retval = new BELScriptParser.set_annotation_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal22=null;
        Token OBJECT_IDENT23=null;
        Token char_literal24=null;
        Token set25=null;

        Object string_literal22_tree=null;
        Object OBJECT_IDENT23_tree=null;
        Object char_literal24_tree=null;
        Object set25_tree=null;

         paraphrases.push("in set annotation."); 
        try {
            // BELScript.g:69:5: ( 'SET' OBJECT_IDENT '=' ( QUOTED_VALUE | VALUE_LIST | OBJECT_IDENT ) )
            // BELScript.g:70:5: 'SET' OBJECT_IDENT '=' ( QUOTED_VALUE | VALUE_LIST | OBJECT_IDENT )
            {
            root_0 = (Object)adaptor.nil();


            string_literal22=(Token)match(input,47,FOLLOW_47_in_set_annotation269); 
            string_literal22_tree = 
            (Object)adaptor.create(string_literal22)
            ;
            adaptor.addChild(root_0, string_literal22_tree);


            OBJECT_IDENT23=(Token)match(input,OBJECT_IDENT,FOLLOW_OBJECT_IDENT_in_set_annotation271); 
            OBJECT_IDENT23_tree = 
            (Object)adaptor.create(OBJECT_IDENT23)
            ;
            adaptor.addChild(root_0, OBJECT_IDENT23_tree);


            char_literal24=(Token)match(input,29,FOLLOW_29_in_set_annotation273); 
            char_literal24_tree = 
            (Object)adaptor.create(char_literal24)
            ;
            adaptor.addChild(root_0, char_literal24_tree);


            set25=(Token)input.LT(1);

            if ( input.LA(1)==OBJECT_IDENT||input.LA(1)==QUOTED_VALUE||input.LA(1)==VALUE_LIST ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set25)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

             paraphrases.pop(); 
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "set_annotation"


    public static class unset_statement_group_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "unset_statement_group"
    // BELScript.g:73:1: unset_statement_group : 'UNSET' STATEMENT_GROUP_KEYWORD ;
    public final BELScriptParser.unset_statement_group_return unset_statement_group() throws RecognitionException {
        BELScriptParser.unset_statement_group_return retval = new BELScriptParser.unset_statement_group_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal26=null;
        Token STATEMENT_GROUP_KEYWORD27=null;

        Object string_literal26_tree=null;
        Object STATEMENT_GROUP_KEYWORD27_tree=null;

         paraphrases.push("in unset statement group."); 
        try {
            // BELScript.g:76:5: ( 'UNSET' STATEMENT_GROUP_KEYWORD )
            // BELScript.g:77:5: 'UNSET' STATEMENT_GROUP_KEYWORD
            {
            root_0 = (Object)adaptor.nil();


            string_literal26=(Token)match(input,48,FOLLOW_48_in_unset_statement_group324); 
            string_literal26_tree = 
            (Object)adaptor.create(string_literal26)
            ;
            adaptor.addChild(root_0, string_literal26_tree);


            STATEMENT_GROUP_KEYWORD27=(Token)match(input,STATEMENT_GROUP_KEYWORD,FOLLOW_STATEMENT_GROUP_KEYWORD_in_unset_statement_group326); 
            STATEMENT_GROUP_KEYWORD27_tree = 
            (Object)adaptor.create(STATEMENT_GROUP_KEYWORD27)
            ;
            adaptor.addChild(root_0, STATEMENT_GROUP_KEYWORD27_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

             paraphrases.pop(); 
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "unset_statement_group"


    public static class unset_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "unset"
    // BELScript.g:80:1: unset : 'UNSET' ( OBJECT_IDENT | IDENT_LIST ) ;
    public final BELScriptParser.unset_return unset() throws RecognitionException {
        BELScriptParser.unset_return retval = new BELScriptParser.unset_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal28=null;
        Token set29=null;

        Object string_literal28_tree=null;
        Object set29_tree=null;

         paraphrases.push("in unset."); 
        try {
            // BELScript.g:83:5: ( 'UNSET' ( OBJECT_IDENT | IDENT_LIST ) )
            // BELScript.g:84:5: 'UNSET' ( OBJECT_IDENT | IDENT_LIST )
            {
            root_0 = (Object)adaptor.nil();


            string_literal28=(Token)match(input,48,FOLLOW_48_in_unset365); 
            string_literal28_tree = 
            (Object)adaptor.create(string_literal28)
            ;
            adaptor.addChild(root_0, string_literal28_tree);


            set29=(Token)input.LT(1);

            if ( input.LA(1)==IDENT_LIST||input.LA(1)==OBJECT_IDENT ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set29)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

             paraphrases.pop(); 
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "unset"


    public static class define_namespace_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "define_namespace"
    // BELScript.g:87:1: define_namespace : ( 'DEFINE' ( ( 'DEFAULT' )? 'NAMESPACE' ) ) OBJECT_IDENT 'AS' 'URL' QUOTED_VALUE ;
    public final BELScriptParser.define_namespace_return define_namespace() throws RecognitionException {
        BELScriptParser.define_namespace_return retval = new BELScriptParser.define_namespace_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal30=null;
        Token string_literal31=null;
        Token string_literal32=null;
        Token OBJECT_IDENT33=null;
        Token string_literal34=null;
        Token string_literal35=null;
        Token QUOTED_VALUE36=null;

        Object string_literal30_tree=null;
        Object string_literal31_tree=null;
        Object string_literal32_tree=null;
        Object OBJECT_IDENT33_tree=null;
        Object string_literal34_tree=null;
        Object string_literal35_tree=null;
        Object QUOTED_VALUE36_tree=null;

         paraphrases.push("in define namespace."); 
        try {
            // BELScript.g:90:5: ( ( 'DEFINE' ( ( 'DEFAULT' )? 'NAMESPACE' ) ) OBJECT_IDENT 'AS' 'URL' QUOTED_VALUE )
            // BELScript.g:91:5: ( 'DEFINE' ( ( 'DEFAULT' )? 'NAMESPACE' ) ) OBJECT_IDENT 'AS' 'URL' QUOTED_VALUE
            {
            root_0 = (Object)adaptor.nil();


            // BELScript.g:91:5: ( 'DEFINE' ( ( 'DEFAULT' )? 'NAMESPACE' ) )
            // BELScript.g:91:6: 'DEFINE' ( ( 'DEFAULT' )? 'NAMESPACE' )
            {
            string_literal30=(Token)match(input,39,FOLLOW_39_in_define_namespace413); 
            string_literal30_tree = 
            (Object)adaptor.create(string_literal30)
            ;
            adaptor.addChild(root_0, string_literal30_tree);


            // BELScript.g:91:15: ( ( 'DEFAULT' )? 'NAMESPACE' )
            // BELScript.g:91:16: ( 'DEFAULT' )? 'NAMESPACE'
            {
            // BELScript.g:91:16: ( 'DEFAULT' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==38) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // BELScript.g:91:17: 'DEFAULT'
                    {
                    string_literal31=(Token)match(input,38,FOLLOW_38_in_define_namespace417); 
                    string_literal31_tree = 
                    (Object)adaptor.create(string_literal31)
                    ;
                    adaptor.addChild(root_0, string_literal31_tree);


                    }
                    break;

            }


            string_literal32=(Token)match(input,44,FOLLOW_44_in_define_namespace421); 
            string_literal32_tree = 
            (Object)adaptor.create(string_literal32)
            ;
            adaptor.addChild(root_0, string_literal32_tree);


            }


            }


            OBJECT_IDENT33=(Token)match(input,OBJECT_IDENT,FOLLOW_OBJECT_IDENT_in_define_namespace425); 
            OBJECT_IDENT33_tree = 
            (Object)adaptor.create(OBJECT_IDENT33)
            ;
            adaptor.addChild(root_0, OBJECT_IDENT33_tree);


            string_literal34=(Token)match(input,34,FOLLOW_34_in_define_namespace427); 
            string_literal34_tree = 
            (Object)adaptor.create(string_literal34)
            ;
            adaptor.addChild(root_0, string_literal34_tree);


            string_literal35=(Token)match(input,49,FOLLOW_49_in_define_namespace429); 
            string_literal35_tree = 
            (Object)adaptor.create(string_literal35)
            ;
            adaptor.addChild(root_0, string_literal35_tree);


            QUOTED_VALUE36=(Token)match(input,QUOTED_VALUE,FOLLOW_QUOTED_VALUE_in_define_namespace431); 
            QUOTED_VALUE36_tree = 
            (Object)adaptor.create(QUOTED_VALUE36)
            ;
            adaptor.addChild(root_0, QUOTED_VALUE36_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

             paraphrases.pop(); 
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "define_namespace"


    public static class define_annotation_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "define_annotation"
    // BELScript.g:94:1: define_annotation : ( 'DEFINE' 'ANNOTATION' ) OBJECT_IDENT 'AS' ( ( ( 'URL' | 'PATTERN' ) QUOTED_VALUE ) | ( 'LIST' VALUE_LIST ) ) ;
    public final BELScriptParser.define_annotation_return define_annotation() throws RecognitionException {
        BELScriptParser.define_annotation_return retval = new BELScriptParser.define_annotation_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal37=null;
        Token string_literal38=null;
        Token OBJECT_IDENT39=null;
        Token string_literal40=null;
        Token set41=null;
        Token QUOTED_VALUE42=null;
        Token string_literal43=null;
        Token VALUE_LIST44=null;

        Object string_literal37_tree=null;
        Object string_literal38_tree=null;
        Object OBJECT_IDENT39_tree=null;
        Object string_literal40_tree=null;
        Object set41_tree=null;
        Object QUOTED_VALUE42_tree=null;
        Object string_literal43_tree=null;
        Object VALUE_LIST44_tree=null;

         paraphrases.push("in define annotation."); 
        try {
            // BELScript.g:97:5: ( ( 'DEFINE' 'ANNOTATION' ) OBJECT_IDENT 'AS' ( ( ( 'URL' | 'PATTERN' ) QUOTED_VALUE ) | ( 'LIST' VALUE_LIST ) ) )
            // BELScript.g:98:5: ( 'DEFINE' 'ANNOTATION' ) OBJECT_IDENT 'AS' ( ( ( 'URL' | 'PATTERN' ) QUOTED_VALUE ) | ( 'LIST' VALUE_LIST ) )
            {
            root_0 = (Object)adaptor.nil();


            // BELScript.g:98:5: ( 'DEFINE' 'ANNOTATION' )
            // BELScript.g:98:6: 'DEFINE' 'ANNOTATION'
            {
            string_literal37=(Token)match(input,39,FOLLOW_39_in_define_annotation471); 
            string_literal37_tree = 
            (Object)adaptor.create(string_literal37)
            ;
            adaptor.addChild(root_0, string_literal37_tree);


            string_literal38=(Token)match(input,33,FOLLOW_33_in_define_annotation473); 
            string_literal38_tree = 
            (Object)adaptor.create(string_literal38)
            ;
            adaptor.addChild(root_0, string_literal38_tree);


            }


            OBJECT_IDENT39=(Token)match(input,OBJECT_IDENT,FOLLOW_OBJECT_IDENT_in_define_annotation476); 
            OBJECT_IDENT39_tree = 
            (Object)adaptor.create(OBJECT_IDENT39)
            ;
            adaptor.addChild(root_0, OBJECT_IDENT39_tree);


            string_literal40=(Token)match(input,34,FOLLOW_34_in_define_annotation478); 
            string_literal40_tree = 
            (Object)adaptor.create(string_literal40)
            ;
            adaptor.addChild(root_0, string_literal40_tree);


            // BELScript.g:98:47: ( ( ( 'URL' | 'PATTERN' ) QUOTED_VALUE ) | ( 'LIST' VALUE_LIST ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==46||LA4_0==49) ) {
                alt4=1;
            }
            else if ( (LA4_0==42) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // BELScript.g:98:48: ( ( 'URL' | 'PATTERN' ) QUOTED_VALUE )
                    {
                    // BELScript.g:98:48: ( ( 'URL' | 'PATTERN' ) QUOTED_VALUE )
                    // BELScript.g:98:49: ( 'URL' | 'PATTERN' ) QUOTED_VALUE
                    {
                    set41=(Token)input.LT(1);

                    if ( input.LA(1)==46||input.LA(1)==49 ) {
                        input.consume();
                        adaptor.addChild(root_0, 
                        (Object)adaptor.create(set41)
                        );
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    QUOTED_VALUE42=(Token)match(input,QUOTED_VALUE,FOLLOW_QUOTED_VALUE_in_define_annotation490); 
                    QUOTED_VALUE42_tree = 
                    (Object)adaptor.create(QUOTED_VALUE42)
                    ;
                    adaptor.addChild(root_0, QUOTED_VALUE42_tree);


                    }


                    }
                    break;
                case 2 :
                    // BELScript.g:98:85: ( 'LIST' VALUE_LIST )
                    {
                    // BELScript.g:98:85: ( 'LIST' VALUE_LIST )
                    // BELScript.g:98:86: 'LIST' VALUE_LIST
                    {
                    string_literal43=(Token)match(input,42,FOLLOW_42_in_define_annotation496); 
                    string_literal43_tree = 
                    (Object)adaptor.create(string_literal43)
                    ;
                    adaptor.addChild(root_0, string_literal43_tree);


                    VALUE_LIST44=(Token)match(input,VALUE_LIST,FOLLOW_VALUE_LIST_in_define_annotation498); 
                    VALUE_LIST44_tree = 
                    (Object)adaptor.create(VALUE_LIST44)
                    ;
                    adaptor.addChild(root_0, VALUE_LIST44_tree);


                    }


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

             paraphrases.pop(); 
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "define_annotation"


    public static class document_property_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "document_property"
    // BELScript.g:101:1: document_property : ( 'Authors' | 'ContactInfo' | 'Copyright' | 'Description' | 'Disclaimer' | 'Licenses' | 'Name' | 'Version' );
    public final BELScriptParser.document_property_return document_property() throws RecognitionException {
        BELScriptParser.document_property_return retval = new BELScriptParser.document_property_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set45=null;

        Object set45_tree=null;

        try {
            // BELScript.g:101:18: ( 'Authors' | 'ContactInfo' | 'Copyright' | 'Description' | 'Disclaimer' | 'Licenses' | 'Name' | 'Version' )
            // BELScript.g:
            {
            root_0 = (Object)adaptor.nil();


            set45=(Token)input.LT(1);

            if ( (input.LA(1) >= 35 && input.LA(1) <= 37)||(input.LA(1) >= 40 && input.LA(1) <= 41)||input.LA(1)==43||input.LA(1)==45||input.LA(1)==50 ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set45)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "document_property"


    public static class statement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "statement"
    // BELScript.g:112:1: statement : outer_term ( relationship ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term ) )? ( STATEMENT_COMMENT )? ;
    public final BELScriptParser.statement_return statement() throws RecognitionException {
        BELScriptParser.statement_return retval = new BELScriptParser.statement_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OPEN_PAREN48=null;
        Token CLOSE_PAREN52=null;
        Token STATEMENT_COMMENT54=null;
        BELScriptParser.outer_term_return outer_term46 =null;

        BELScriptParser.relationship_return relationship47 =null;

        BELScriptParser.outer_term_return outer_term49 =null;

        BELScriptParser.relationship_return relationship50 =null;

        BELScriptParser.outer_term_return outer_term51 =null;

        BELScriptParser.outer_term_return outer_term53 =null;


        Object OPEN_PAREN48_tree=null;
        Object CLOSE_PAREN52_tree=null;
        Object STATEMENT_COMMENT54_tree=null;

         paraphrases.push("in statement."); 
        try {
            // BELScript.g:115:5: ( outer_term ( relationship ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term ) )? ( STATEMENT_COMMENT )? )
            // BELScript.g:116:5: outer_term ( relationship ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term ) )? ( STATEMENT_COMMENT )?
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_outer_term_in_statement628);
            outer_term46=outer_term();

            state._fsp--;

            adaptor.addChild(root_0, outer_term46.getTree());

            // BELScript.g:116:16: ( relationship ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term ) )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( ((LA6_0 >= 25 && LA6_0 <= 28)||(LA6_0 >= 30 && LA6_0 <= 32)||(LA6_0 >= 54 && LA6_0 <= 55)||LA6_0==57||LA6_0==61||LA6_0==70||(LA6_0 >= 73 && LA6_0 <= 74)||(LA6_0 >= 81 && LA6_0 <= 86)||(LA6_0 >= 93 && LA6_0 <= 94)||LA6_0==103||LA6_0==105||LA6_0==109||LA6_0==118||LA6_0==123||LA6_0==125) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // BELScript.g:116:17: relationship ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term )
                    {
                    pushFollow(FOLLOW_relationship_in_statement631);
                    relationship47=relationship();

                    state._fsp--;

                    adaptor.addChild(root_0, relationship47.getTree());

                    // BELScript.g:116:30: ( ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN ) | outer_term )
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==OPEN_PAREN) ) {
                        alt5=1;
                    }
                    else if ( ((LA5_0 >= 51 && LA5_0 <= 53)||LA5_0==56||(LA5_0 >= 58 && LA5_0 <= 60)||(LA5_0 >= 62 && LA5_0 <= 69)||(LA5_0 >= 71 && LA5_0 <= 72)||(LA5_0 >= 75 && LA5_0 <= 80)||(LA5_0 >= 87 && LA5_0 <= 92)||(LA5_0 >= 95 && LA5_0 <= 102)||LA5_0==104||(LA5_0 >= 106 && LA5_0 <= 108)||(LA5_0 >= 110 && LA5_0 <= 117)||(LA5_0 >= 119 && LA5_0 <= 122)||LA5_0==124||(LA5_0 >= 126 && LA5_0 <= 130)) ) {
                        alt5=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 5, 0, input);

                        throw nvae;

                    }
                    switch (alt5) {
                        case 1 :
                            // BELScript.g:116:31: ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN )
                            {
                            // BELScript.g:116:31: ( OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN )
                            // BELScript.g:116:32: OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN
                            {
                            OPEN_PAREN48=(Token)match(input,OPEN_PAREN,FOLLOW_OPEN_PAREN_in_statement635); 
                            OPEN_PAREN48_tree = 
                            (Object)adaptor.create(OPEN_PAREN48)
                            ;
                            adaptor.addChild(root_0, OPEN_PAREN48_tree);


                            pushFollow(FOLLOW_outer_term_in_statement637);
                            outer_term49=outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, outer_term49.getTree());

                            pushFollow(FOLLOW_relationship_in_statement639);
                            relationship50=relationship();

                            state._fsp--;

                            adaptor.addChild(root_0, relationship50.getTree());

                            pushFollow(FOLLOW_outer_term_in_statement641);
                            outer_term51=outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, outer_term51.getTree());

                            CLOSE_PAREN52=(Token)match(input,CLOSE_PAREN,FOLLOW_CLOSE_PAREN_in_statement643); 
                            CLOSE_PAREN52_tree = 
                            (Object)adaptor.create(CLOSE_PAREN52)
                            ;
                            adaptor.addChild(root_0, CLOSE_PAREN52_tree);


                            }


                            }
                            break;
                        case 2 :
                            // BELScript.g:116:93: outer_term
                            {
                            pushFollow(FOLLOW_outer_term_in_statement648);
                            outer_term53=outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, outer_term53.getTree());

                            }
                            break;

                    }


                    }
                    break;

            }


            // BELScript.g:116:107: ( STATEMENT_COMMENT )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==STATEMENT_COMMENT) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // BELScript.g:116:107: STATEMENT_COMMENT
                    {
                    STATEMENT_COMMENT54=(Token)match(input,STATEMENT_COMMENT,FOLLOW_STATEMENT_COMMENT_in_statement653); 
                    STATEMENT_COMMENT54_tree = 
                    (Object)adaptor.create(STATEMENT_COMMENT54)
                    ;
                    adaptor.addChild(root_0, STATEMENT_COMMENT54_tree);


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

             paraphrases.pop(); 
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "statement"


    public static class outer_term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "outer_term"
    // BELScript.g:119:1: outer_term : function OPEN_PAREN ( ( ',' )? argument )* CLOSE_PAREN ;
    public final BELScriptParser.outer_term_return outer_term() throws RecognitionException {
        BELScriptParser.outer_term_return retval = new BELScriptParser.outer_term_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OPEN_PAREN56=null;
        Token char_literal57=null;
        Token CLOSE_PAREN59=null;
        BELScriptParser.function_return function55 =null;

        BELScriptParser.argument_return argument58 =null;


        Object OPEN_PAREN56_tree=null;
        Object char_literal57_tree=null;
        Object CLOSE_PAREN59_tree=null;

        try {
            // BELScript.g:119:11: ( function OPEN_PAREN ( ( ',' )? argument )* CLOSE_PAREN )
            // BELScript.g:120:5: function OPEN_PAREN ( ( ',' )? argument )* CLOSE_PAREN
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_function_in_outer_term674);
            function55=function();

            state._fsp--;

            adaptor.addChild(root_0, function55.getTree());

            OPEN_PAREN56=(Token)match(input,OPEN_PAREN,FOLLOW_OPEN_PAREN_in_outer_term676); 
            OPEN_PAREN56_tree = 
            (Object)adaptor.create(OPEN_PAREN56)
            ;
            adaptor.addChild(root_0, OPEN_PAREN56_tree);


            // BELScript.g:120:25: ( ( ',' )? argument )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0 >= NS_PREFIX && LA9_0 <= OBJECT_IDENT)||LA9_0==QUOTED_VALUE||LA9_0==24||(LA9_0 >= 51 && LA9_0 <= 53)||LA9_0==56||(LA9_0 >= 58 && LA9_0 <= 60)||(LA9_0 >= 62 && LA9_0 <= 69)||(LA9_0 >= 71 && LA9_0 <= 72)||(LA9_0 >= 75 && LA9_0 <= 80)||(LA9_0 >= 87 && LA9_0 <= 92)||(LA9_0 >= 95 && LA9_0 <= 102)||LA9_0==104||(LA9_0 >= 106 && LA9_0 <= 108)||(LA9_0 >= 110 && LA9_0 <= 117)||(LA9_0 >= 119 && LA9_0 <= 122)||LA9_0==124||(LA9_0 >= 126 && LA9_0 <= 130)) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // BELScript.g:120:26: ( ',' )? argument
            	    {
            	    // BELScript.g:120:26: ( ',' )?
            	    int alt8=2;
            	    int LA8_0 = input.LA(1);

            	    if ( (LA8_0==24) ) {
            	        alt8=1;
            	    }
            	    switch (alt8) {
            	        case 1 :
            	            // BELScript.g:120:26: ','
            	            {
            	            char_literal57=(Token)match(input,24,FOLLOW_24_in_outer_term679); 
            	            char_literal57_tree = 
            	            (Object)adaptor.create(char_literal57)
            	            ;
            	            adaptor.addChild(root_0, char_literal57_tree);


            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_argument_in_outer_term682);
            	    argument58=argument();

            	    state._fsp--;

            	    adaptor.addChild(root_0, argument58.getTree());

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            CLOSE_PAREN59=(Token)match(input,CLOSE_PAREN,FOLLOW_CLOSE_PAREN_in_outer_term686); 
            CLOSE_PAREN59_tree = 
            (Object)adaptor.create(CLOSE_PAREN59)
            ;
            adaptor.addChild(root_0, CLOSE_PAREN59_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "outer_term"


    public static class argument_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "argument"
    // BELScript.g:123:1: argument : ( param | term );
    public final BELScriptParser.argument_return argument() throws RecognitionException {
        BELScriptParser.argument_return retval = new BELScriptParser.argument_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        BELScriptParser.param_return param60 =null;

        BELScriptParser.term_return term61 =null;



        try {
            // BELScript.g:123:9: ( param | term )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( ((LA10_0 >= NS_PREFIX && LA10_0 <= OBJECT_IDENT)||LA10_0==QUOTED_VALUE) ) {
                alt10=1;
            }
            else if ( ((LA10_0 >= 51 && LA10_0 <= 53)||LA10_0==56||(LA10_0 >= 58 && LA10_0 <= 60)||(LA10_0 >= 62 && LA10_0 <= 69)||(LA10_0 >= 71 && LA10_0 <= 72)||(LA10_0 >= 75 && LA10_0 <= 80)||(LA10_0 >= 87 && LA10_0 <= 92)||(LA10_0 >= 95 && LA10_0 <= 102)||LA10_0==104||(LA10_0 >= 106 && LA10_0 <= 108)||(LA10_0 >= 110 && LA10_0 <= 117)||(LA10_0 >= 119 && LA10_0 <= 122)||LA10_0==124||(LA10_0 >= 126 && LA10_0 <= 130)) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // BELScript.g:124:5: param
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_param_in_argument706);
                    param60=param();

                    state._fsp--;

                    adaptor.addChild(root_0, param60.getTree());

                    }
                    break;
                case 2 :
                    // BELScript.g:124:13: term
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_term_in_argument710);
                    term61=term();

                    state._fsp--;

                    adaptor.addChild(root_0, term61.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "argument"


    public static class term_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "term"
    // BELScript.g:127:1: term : function OPEN_PAREN ( ( ',' )? ( term | param ) )* CLOSE_PAREN ;
    public final BELScriptParser.term_return term() throws RecognitionException {
        BELScriptParser.term_return retval = new BELScriptParser.term_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OPEN_PAREN63=null;
        Token char_literal64=null;
        Token CLOSE_PAREN67=null;
        BELScriptParser.function_return function62 =null;

        BELScriptParser.term_return term65 =null;

        BELScriptParser.param_return param66 =null;


        Object OPEN_PAREN63_tree=null;
        Object char_literal64_tree=null;
        Object CLOSE_PAREN67_tree=null;

        try {
            // BELScript.g:127:5: ( function OPEN_PAREN ( ( ',' )? ( term | param ) )* CLOSE_PAREN )
            // BELScript.g:128:5: function OPEN_PAREN ( ( ',' )? ( term | param ) )* CLOSE_PAREN
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_function_in_term726);
            function62=function();

            state._fsp--;

            adaptor.addChild(root_0, function62.getTree());

            OPEN_PAREN63=(Token)match(input,OPEN_PAREN,FOLLOW_OPEN_PAREN_in_term728); 
            OPEN_PAREN63_tree = 
            (Object)adaptor.create(OPEN_PAREN63)
            ;
            adaptor.addChild(root_0, OPEN_PAREN63_tree);


            // BELScript.g:128:25: ( ( ',' )? ( term | param ) )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0 >= NS_PREFIX && LA13_0 <= OBJECT_IDENT)||LA13_0==QUOTED_VALUE||LA13_0==24||(LA13_0 >= 51 && LA13_0 <= 53)||LA13_0==56||(LA13_0 >= 58 && LA13_0 <= 60)||(LA13_0 >= 62 && LA13_0 <= 69)||(LA13_0 >= 71 && LA13_0 <= 72)||(LA13_0 >= 75 && LA13_0 <= 80)||(LA13_0 >= 87 && LA13_0 <= 92)||(LA13_0 >= 95 && LA13_0 <= 102)||LA13_0==104||(LA13_0 >= 106 && LA13_0 <= 108)||(LA13_0 >= 110 && LA13_0 <= 117)||(LA13_0 >= 119 && LA13_0 <= 122)||LA13_0==124||(LA13_0 >= 126 && LA13_0 <= 130)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // BELScript.g:128:26: ( ',' )? ( term | param )
            	    {
            	    // BELScript.g:128:26: ( ',' )?
            	    int alt11=2;
            	    int LA11_0 = input.LA(1);

            	    if ( (LA11_0==24) ) {
            	        alt11=1;
            	    }
            	    switch (alt11) {
            	        case 1 :
            	            // BELScript.g:128:26: ','
            	            {
            	            char_literal64=(Token)match(input,24,FOLLOW_24_in_term731); 
            	            char_literal64_tree = 
            	            (Object)adaptor.create(char_literal64)
            	            ;
            	            adaptor.addChild(root_0, char_literal64_tree);


            	            }
            	            break;

            	    }


            	    // BELScript.g:128:31: ( term | param )
            	    int alt12=2;
            	    int LA12_0 = input.LA(1);

            	    if ( ((LA12_0 >= 51 && LA12_0 <= 53)||LA12_0==56||(LA12_0 >= 58 && LA12_0 <= 60)||(LA12_0 >= 62 && LA12_0 <= 69)||(LA12_0 >= 71 && LA12_0 <= 72)||(LA12_0 >= 75 && LA12_0 <= 80)||(LA12_0 >= 87 && LA12_0 <= 92)||(LA12_0 >= 95 && LA12_0 <= 102)||LA12_0==104||(LA12_0 >= 106 && LA12_0 <= 108)||(LA12_0 >= 110 && LA12_0 <= 117)||(LA12_0 >= 119 && LA12_0 <= 122)||LA12_0==124||(LA12_0 >= 126 && LA12_0 <= 130)) ) {
            	        alt12=1;
            	    }
            	    else if ( ((LA12_0 >= NS_PREFIX && LA12_0 <= OBJECT_IDENT)||LA12_0==QUOTED_VALUE) ) {
            	        alt12=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 12, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt12) {
            	        case 1 :
            	            // BELScript.g:128:32: term
            	            {
            	            pushFollow(FOLLOW_term_in_term735);
            	            term65=term();

            	            state._fsp--;

            	            adaptor.addChild(root_0, term65.getTree());

            	            }
            	            break;
            	        case 2 :
            	            // BELScript.g:128:39: param
            	            {
            	            pushFollow(FOLLOW_param_in_term739);
            	            param66=param();

            	            state._fsp--;

            	            adaptor.addChild(root_0, param66.getTree());

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            CLOSE_PAREN67=(Token)match(input,CLOSE_PAREN,FOLLOW_CLOSE_PAREN_in_term744); 
            CLOSE_PAREN67_tree = 
            (Object)adaptor.create(CLOSE_PAREN67)
            ;
            adaptor.addChild(root_0, CLOSE_PAREN67_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "term"


    public static class param_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "param"
    // BELScript.g:132:10: fragment param : ( NS_PREFIX )? ( OBJECT_IDENT | QUOTED_VALUE ) ;
    public final BELScriptParser.param_return param() throws RecognitionException {
        BELScriptParser.param_return retval = new BELScriptParser.param_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token NS_PREFIX68=null;
        Token set69=null;

        Object NS_PREFIX68_tree=null;
        Object set69_tree=null;

        try {
            // BELScript.g:132:15: ( ( NS_PREFIX )? ( OBJECT_IDENT | QUOTED_VALUE ) )
            // BELScript.g:133:5: ( NS_PREFIX )? ( OBJECT_IDENT | QUOTED_VALUE )
            {
            root_0 = (Object)adaptor.nil();


            // BELScript.g:133:5: ( NS_PREFIX )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==NS_PREFIX) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // BELScript.g:133:5: NS_PREFIX
                    {
                    NS_PREFIX68=(Token)match(input,NS_PREFIX,FOLLOW_NS_PREFIX_in_param764); 
                    NS_PREFIX68_tree = 
                    (Object)adaptor.create(NS_PREFIX68)
                    ;
                    adaptor.addChild(root_0, NS_PREFIX68_tree);


                    }
                    break;

            }


            set69=(Token)input.LT(1);

            if ( input.LA(1)==OBJECT_IDENT||input.LA(1)==QUOTED_VALUE ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set69)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

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
        public Object getTree() { return tree; }
    };


    // $ANTLR start "function"
    // BELScript.g:136:1: function returns [String r] : (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' ) ;
    public final BELScriptParser.function_return function() throws RecognitionException {
        BELScriptParser.function_return retval = new BELScriptParser.function_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token fv=null;

        Object fv_tree=null;

        try {
            // BELScript.g:136:28: ( (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' ) )
            // BELScript.g:137:5: (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' )
            {
            root_0 = (Object)adaptor.nil();


            // BELScript.g:137:5: (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' )
            int alt15=59;
            switch ( input.LA(1) ) {
            case 106:
                {
                alt15=1;
                }
                break;
            case 95:
                {
                alt15=2;
                }
                break;
            case 114:
                {
                alt15=3;
                }
                break;
            case 108:
                {
                alt15=4;
                }
                break;
            case 52:
                {
                alt15=5;
                }
                break;
            case 51:
                {
                alt15=6;
                }
                break;
            case 91:
                {
                alt15=7;
                }
                break;
            case 90:
                {
                alt15=8;
                }
                break;
            case 78:
                {
                alt15=9;
                }
                break;
            case 77:
                {
                alt15=10;
                }
                break;
            case 56:
                {
                alt15=11;
                }
                break;
            case 58:
                {
                alt15=12;
                }
                break;
            case 97:
                {
                alt15=13;
                }
                break;
            case 96:
                {
                alt15=14;
                }
                break;
            case 67:
                {
                alt15=15;
                }
                break;
            case 66:
                {
                alt15=16;
                }
                break;
            case 126:
                {
                alt15=17;
                }
                break;
            case 121:
                {
                alt15=18;
                }
                break;
            case 62:
                {
                alt15=19;
                }
                break;
            case 116:
                {
                alt15=20;
                }
                break;
            case 63:
                {
                alt15=21;
                }
                break;
            case 120:
                {
                alt15=22;
                }
                break;
            case 111:
                {
                alt15=23;
                }
                break;
            case 115:
                {
                alt15=24;
                }
                break;
            case 69:
                {
                alt15=25;
                }
                break;
            case 68:
                {
                alt15=26;
                }
                break;
            case 76:
                {
                alt15=27;
                }
                break;
            case 75:
                {
                alt15=28;
                }
                break;
            case 72:
                {
                alt15=29;
                }
                break;
            case 71:
                {
                alt15=30;
                }
                break;
            case 92:
                {
                alt15=31;
                }
                break;
            case 53:
                {
                alt15=32;
                }
                break;
            case 60:
                {
                alt15=33;
                }
                break;
            case 59:
                {
                alt15=34;
                }
                break;
            case 88:
                {
                alt15=35;
                }
                break;
            case 87:
                {
                alt15=36;
                }
                break;
            case 101:
                {
                alt15=37;
                }
                break;
            case 100:
                {
                alt15=38;
                }
                break;
            case 99:
                {
                alt15=39;
                }
                break;
            case 98:
                {
                alt15=40;
                }
                break;
            case 113:
                {
                alt15=41;
                }
                break;
            case 112:
                {
                alt15=42;
                }
                break;
            case 124:
                {
                alt15=43;
                }
                break;
            case 130:
                {
                alt15=44;
                }
                break;
            case 127:
                {
                alt15=45;
                }
                break;
            case 122:
                {
                alt15=46;
                }
                break;
            case 80:
                {
                alt15=47;
                }
                break;
            case 79:
                {
                alt15=48;
                }
                break;
            case 65:
                {
                alt15=49;
                }
                break;
            case 64:
                {
                alt15=50;
                }
                break;
            case 107:
                {
                alt15=51;
                }
                break;
            case 102:
                {
                alt15=52;
                }
                break;
            case 119:
                {
                alt15=53;
                }
                break;
            case 117:
                {
                alt15=54;
                }
                break;
            case 129:
                {
                alt15=55;
                }
                break;
            case 128:
                {
                alt15=56;
                }
                break;
            case 110:
                {
                alt15=57;
                }
                break;
            case 104:
                {
                alt15=58;
                }
                break;
            case 89:
                {
                alt15=59;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }

            switch (alt15) {
                case 1 :
                    // BELScript.g:138:9: fv= 'proteinAbundance'
                    {
                    fv=(Token)match(input,106,FOLLOW_106_in_function805); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 2 :
                    // BELScript.g:139:9: fv= 'p'
                    {
                    fv=(Token)match(input,95,FOLLOW_95_in_function831); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 3 :
                    // BELScript.g:140:9: fv= 'rnaAbundance'
                    {
                    fv=(Token)match(input,114,FOLLOW_114_in_function872); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 4 :
                    // BELScript.g:141:9: fv= 'r'
                    {
                    fv=(Token)match(input,108,FOLLOW_108_in_function903); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 5 :
                    // BELScript.g:142:9: fv= 'abundance'
                    {
                    fv=(Token)match(input,52,FOLLOW_52_in_function944); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 6 :
                    // BELScript.g:143:9: fv= 'a'
                    {
                    fv=(Token)match(input,51,FOLLOW_51_in_function978); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 7 :
                    // BELScript.g:144:9: fv= 'microRNAAbundance'
                    {
                    fv=(Token)match(input,91,FOLLOW_91_in_function1019); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 8 :
                    // BELScript.g:145:9: fv= 'm'
                    {
                    fv=(Token)match(input,90,FOLLOW_90_in_function1045); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 9 :
                    // BELScript.g:146:9: fv= 'geneAbundance'
                    {
                    fv=(Token)match(input,78,FOLLOW_78_in_function1086); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 10 :
                    // BELScript.g:147:9: fv= 'g'
                    {
                    fv=(Token)match(input,77,FOLLOW_77_in_function1115); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 11 :
                    // BELScript.g:148:9: fv= 'biologicalProcess'
                    {
                    fv=(Token)match(input,56,FOLLOW_56_in_function1156); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 12 :
                    // BELScript.g:149:9: fv= 'bp'
                    {
                    fv=(Token)match(input,58,FOLLOW_58_in_function1182); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 13 :
                    // BELScript.g:150:9: fv= 'pathology'
                    {
                    fv=(Token)match(input,97,FOLLOW_97_in_function1222); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 14 :
                    // BELScript.g:151:9: fv= 'path'
                    {
                    fv=(Token)match(input,96,FOLLOW_96_in_function1255); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 15 :
                    // BELScript.g:152:9: fv= 'complexAbundance'
                    {
                    fv=(Token)match(input,67,FOLLOW_67_in_function1293); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 16 :
                    // BELScript.g:153:9: fv= 'complex'
                    {
                    fv=(Token)match(input,66,FOLLOW_66_in_function1320); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 17 :
                    // BELScript.g:154:9: fv= 'translocation'
                    {
                    fv=(Token)match(input,126,FOLLOW_126_in_function1355); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 18 :
                    // BELScript.g:155:9: fv= 'tloc'
                    {
                    fv=(Token)match(input,121,FOLLOW_121_in_function1385); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 19 :
                    // BELScript.g:156:9: fv= 'cellSecretion'
                    {
                    fv=(Token)match(input,62,FOLLOW_62_in_function1423); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 20 :
                    // BELScript.g:157:9: fv= 'sec'
                    {
                    fv=(Token)match(input,116,FOLLOW_116_in_function1453); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 21 :
                    // BELScript.g:158:9: fv= 'cellSurfaceExpression'
                    {
                    fv=(Token)match(input,63,FOLLOW_63_in_function1492); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 22 :
                    // BELScript.g:159:9: fv= 'surf'
                    {
                    fv=(Token)match(input,120,FOLLOW_120_in_function1513); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 23 :
                    // BELScript.g:160:9: fv= 'reaction'
                    {
                    fv=(Token)match(input,111,FOLLOW_111_in_function1551); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 24 :
                    // BELScript.g:161:9: fv= 'rxn'
                    {
                    fv=(Token)match(input,115,FOLLOW_115_in_function1585); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 25 :
                    // BELScript.g:162:9: fv= 'compositeAbundance'
                    {
                    fv=(Token)match(input,69,FOLLOW_69_in_function1624); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 26 :
                    // BELScript.g:163:9: fv= 'composite'
                    {
                    fv=(Token)match(input,68,FOLLOW_68_in_function1648); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 27 :
                    // BELScript.g:164:9: fv= 'fusion'
                    {
                    fv=(Token)match(input,76,FOLLOW_76_in_function1681); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 28 :
                    // BELScript.g:165:9: fv= 'fus'
                    {
                    fv=(Token)match(input,75,FOLLOW_75_in_function1717); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 29 :
                    // BELScript.g:166:9: fv= 'degradation'
                    {
                    fv=(Token)match(input,72,FOLLOW_72_in_function1756); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 30 :
                    // BELScript.g:167:9: fv= 'deg'
                    {
                    fv=(Token)match(input,71,FOLLOW_71_in_function1787); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 31 :
                    // BELScript.g:168:9: fv= 'molecularActivity'
                    {
                    fv=(Token)match(input,92,FOLLOW_92_in_function1826); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 32 :
                    // BELScript.g:169:9: fv= 'act'
                    {
                    fv=(Token)match(input,53,FOLLOW_53_in_function1851); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 33 :
                    // BELScript.g:170:9: fv= 'catalyticActivity'
                    {
                    fv=(Token)match(input,60,FOLLOW_60_in_function1890); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 34 :
                    // BELScript.g:171:9: fv= 'cat'
                    {
                    fv=(Token)match(input,59,FOLLOW_59_in_function1915); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 35 :
                    // BELScript.g:172:9: fv= 'kinaseActivity'
                    {
                    fv=(Token)match(input,88,FOLLOW_88_in_function1954); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 36 :
                    // BELScript.g:173:9: fv= 'kin'
                    {
                    fv=(Token)match(input,87,FOLLOW_87_in_function1982); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 37 :
                    // BELScript.g:174:9: fv= 'phosphataseActivity'
                    {
                    fv=(Token)match(input,101,FOLLOW_101_in_function2021); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 38 :
                    // BELScript.g:175:9: fv= 'phos'
                    {
                    fv=(Token)match(input,100,FOLLOW_100_in_function2044); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 39 :
                    // BELScript.g:176:9: fv= 'peptidaseActivity'
                    {
                    fv=(Token)match(input,99,FOLLOW_99_in_function2082); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 40 :
                    // BELScript.g:177:9: fv= 'pep'
                    {
                    fv=(Token)match(input,98,FOLLOW_98_in_function2107); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 41 :
                    // BELScript.g:178:9: fv= 'ribosylationActivity'
                    {
                    fv=(Token)match(input,113,FOLLOW_113_in_function2146); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 42 :
                    // BELScript.g:179:9: fv= 'ribo'
                    {
                    fv=(Token)match(input,112,FOLLOW_112_in_function2168); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 43 :
                    // BELScript.g:180:9: fv= 'transcriptionalActivity'
                    {
                    fv=(Token)match(input,124,FOLLOW_124_in_function2206); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 44 :
                    // BELScript.g:181:9: fv= 'tscript'
                    {
                    fv=(Token)match(input,130,FOLLOW_130_in_function2225); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 45 :
                    // BELScript.g:182:9: fv= 'transportActivity'
                    {
                    fv=(Token)match(input,127,FOLLOW_127_in_function2260); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 46 :
                    // BELScript.g:183:9: fv= 'tport'
                    {
                    fv=(Token)match(input,122,FOLLOW_122_in_function2285); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 47 :
                    // BELScript.g:184:9: fv= 'gtpBoundActivity'
                    {
                    fv=(Token)match(input,80,FOLLOW_80_in_function2322); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 48 :
                    // BELScript.g:185:9: fv= 'gtp'
                    {
                    fv=(Token)match(input,79,FOLLOW_79_in_function2348); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 49 :
                    // BELScript.g:186:9: fv= 'chaperoneActivity'
                    {
                    fv=(Token)match(input,65,FOLLOW_65_in_function2387); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 50 :
                    // BELScript.g:187:9: fv= 'chap'
                    {
                    fv=(Token)match(input,64,FOLLOW_64_in_function2412); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 51 :
                    // BELScript.g:188:9: fv= 'proteinModification'
                    {
                    fv=(Token)match(input,107,FOLLOW_107_in_function2450); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 52 :
                    // BELScript.g:189:9: fv= 'pmod'
                    {
                    fv=(Token)match(input,102,FOLLOW_102_in_function2473); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 53 :
                    // BELScript.g:190:9: fv= 'substitution'
                    {
                    fv=(Token)match(input,119,FOLLOW_119_in_function2511); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 54 :
                    // BELScript.g:191:9: fv= 'sub'
                    {
                    fv=(Token)match(input,117,FOLLOW_117_in_function2541); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 55 :
                    // BELScript.g:192:9: fv= 'truncation'
                    {
                    fv=(Token)match(input,129,FOLLOW_129_in_function2580); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 56 :
                    // BELScript.g:193:9: fv= 'trunc'
                    {
                    fv=(Token)match(input,128,FOLLOW_128_in_function2612); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 57 :
                    // BELScript.g:194:9: fv= 'reactants'
                    {
                    fv=(Token)match(input,110,FOLLOW_110_in_function2649); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 58 :
                    // BELScript.g:195:9: fv= 'products'
                    {
                    fv=(Token)match(input,104,FOLLOW_104_in_function2682); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;
                case 59 :
                    // BELScript.g:196:9: fv= 'list'
                    {
                    fv=(Token)match(input,89,FOLLOW_89_in_function2716); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = fv.getText();

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

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
        public Object getTree() { return tree; }
    };


    // $ANTLR start "relationship"
    // BELScript.g:200:1: relationship returns [String r] : (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' ) ;
    public final BELScriptParser.relationship_return relationship() throws RecognitionException {
        BELScriptParser.relationship_return retval = new BELScriptParser.relationship_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token rv=null;

        Object rv_tree=null;

        try {
            // BELScript.g:200:32: ( (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' ) )
            // BELScript.g:201:5: (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' )
            {
            root_0 = (Object)adaptor.nil();


            // BELScript.g:201:5: (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' )
            int alt16=28;
            switch ( input.LA(1) ) {
            case 85:
                {
                alt16=1;
                }
                break;
            case 26:
                {
                alt16=2;
                }
                break;
            case 70:
                {
                alt16=3;
                }
                break;
            case 27:
                {
                alt16=4;
                }
                break;
            case 74:
                {
                alt16=5;
                }
                break;
            case 30:
                {
                alt16=6;
                }
                break;
            case 73:
                {
                alt16=7;
                }
                break;
            case 31:
                {
                alt16=8;
                }
                break;
            case 61:
                {
                alt16=9;
                }
                break;
            case 103:
                {
                alt16=10;
                }
                break;
            case 93:
                {
                alt16=11;
                }
                break;
            case 125:
                {
                alt16=12;
                }
                break;
            case 32:
                {
                alt16=13;
                }
                break;
            case 123:
                {
                alt16=14;
                }
                break;
            case 28:
                {
                alt16=15;
                }
                break;
            case 86:
                {
                alt16=16;
                }
                break;
            case 118:
                {
                alt16=17;
                }
                break;
            case 109:
                {
                alt16=18;
                }
                break;
            case 57:
                {
                alt16=19;
                }
                break;
            case 105:
                {
                alt16=20;
                }
                break;
            case 94:
                {
                alt16=21;
                }
                break;
            case 54:
                {
                alt16=22;
                }
                break;
            case 55:
                {
                alt16=23;
                }
                break;
            case 25:
                {
                alt16=24;
                }
                break;
            case 84:
                {
                alt16=25;
                }
                break;
            case 82:
                {
                alt16=26;
                }
                break;
            case 83:
                {
                alt16=27;
                }
                break;
            case 81:
                {
                alt16=28;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }

            switch (alt16) {
                case 1 :
                    // BELScript.g:202:9: rv= 'increases'
                    {
                    rv=(Token)match(input,85,FOLLOW_85_in_relationship2782); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 2 :
                    // BELScript.g:203:9: rv= '->'
                    {
                    rv=(Token)match(input,26,FOLLOW_26_in_relationship2816); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 3 :
                    // BELScript.g:204:9: rv= 'decreases'
                    {
                    rv=(Token)match(input,70,FOLLOW_70_in_relationship2856); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 4 :
                    // BELScript.g:205:9: rv= '-|'
                    {
                    rv=(Token)match(input,27,FOLLOW_27_in_relationship2890); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 5 :
                    // BELScript.g:206:9: rv= 'directlyIncreases'
                    {
                    rv=(Token)match(input,74,FOLLOW_74_in_relationship2930); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 6 :
                    // BELScript.g:207:9: rv= '=>'
                    {
                    rv=(Token)match(input,30,FOLLOW_30_in_relationship2955); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 7 :
                    // BELScript.g:208:9: rv= 'directlyDecreases'
                    {
                    rv=(Token)match(input,73,FOLLOW_73_in_relationship2995); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 8 :
                    // BELScript.g:209:9: rv= '=|'
                    {
                    rv=(Token)match(input,31,FOLLOW_31_in_relationship3020); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 9 :
                    // BELScript.g:210:9: rv= 'causesNoChange'
                    {
                    rv=(Token)match(input,61,FOLLOW_61_in_relationship3060); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 10 :
                    // BELScript.g:211:9: rv= 'positiveCorrelation'
                    {
                    rv=(Token)match(input,103,FOLLOW_103_in_relationship3088); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 11 :
                    // BELScript.g:212:9: rv= 'negativeCorrelation'
                    {
                    rv=(Token)match(input,93,FOLLOW_93_in_relationship3111); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 12 :
                    // BELScript.g:213:9: rv= 'translatedTo'
                    {
                    rv=(Token)match(input,125,FOLLOW_125_in_relationship3134); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 13 :
                    // BELScript.g:214:9: rv= '>>'
                    {
                    rv=(Token)match(input,32,FOLLOW_32_in_relationship3164); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 14 :
                    // BELScript.g:215:9: rv= 'transcribedTo'
                    {
                    rv=(Token)match(input,123,FOLLOW_123_in_relationship3204); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 15 :
                    // BELScript.g:216:9: rv= ':>'
                    {
                    rv=(Token)match(input,28,FOLLOW_28_in_relationship3233); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 16 :
                    // BELScript.g:217:9: rv= 'isA'
                    {
                    rv=(Token)match(input,86,FOLLOW_86_in_relationship3273); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 17 :
                    // BELScript.g:218:9: rv= 'subProcessOf'
                    {
                    rv=(Token)match(input,118,FOLLOW_118_in_relationship3312); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 18 :
                    // BELScript.g:219:9: rv= 'rateLimitingStepOf'
                    {
                    rv=(Token)match(input,109,FOLLOW_109_in_relationship3342); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 19 :
                    // BELScript.g:220:9: rv= 'biomarkerFor'
                    {
                    rv=(Token)match(input,57,FOLLOW_57_in_relationship3366); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 20 :
                    // BELScript.g:221:9: rv= 'prognosticBiomarkerFor'
                    {
                    rv=(Token)match(input,105,FOLLOW_105_in_relationship3396); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 21 :
                    // BELScript.g:222:9: rv= 'orthologous'
                    {
                    rv=(Token)match(input,94,FOLLOW_94_in_relationship3416); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 22 :
                    // BELScript.g:223:9: rv= 'analogous'
                    {
                    rv=(Token)match(input,54,FOLLOW_54_in_relationship3447); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 23 :
                    // BELScript.g:224:9: rv= 'association'
                    {
                    rv=(Token)match(input,55,FOLLOW_55_in_relationship3480); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 24 :
                    // BELScript.g:225:9: rv= '--'
                    {
                    rv=(Token)match(input,25,FOLLOW_25_in_relationship3511); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 25 :
                    // BELScript.g:226:9: rv= 'hasMembers'
                    {
                    rv=(Token)match(input,84,FOLLOW_84_in_relationship3551); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 26 :
                    // BELScript.g:227:9: rv= 'hasComponents'
                    {
                    rv=(Token)match(input,82,FOLLOW_82_in_relationship3583); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 27 :
                    // BELScript.g:228:9: rv= 'hasMember'
                    {
                    rv=(Token)match(input,83,FOLLOW_83_in_relationship3612); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;
                case 28 :
                    // BELScript.g:229:9: rv= 'hasComponent'
                    {
                    rv=(Token)match(input,81,FOLLOW_81_in_relationship3645); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = rv.getText(); 

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "relationship"

    // Delegated rules


 

    public static final BitSet FOLLOW_NEWLINE_in_document62 = new BitSet(new long[]{0xDD39808000002080L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_DOCUMENT_COMMENT_in_document66 = new BitSet(new long[]{0xDD39808000002080L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_record_in_document70 = new BitSet(new long[]{0xDD39808000002080L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_EOF_in_document74 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_define_namespace_in_record91 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_define_annotation_in_record95 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_annotation_in_record99 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_document_in_record103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_statement_group_in_record107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unset_statement_group_in_record111 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unset_in_record115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_record119 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_set_document160 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_DOCUMENT_KEYWORD_in_set_document162 = new BitSet(new long[]{0x00042B3800000000L});
    public static final BitSet FOLLOW_document_property_in_set_document165 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_set_document167 = new BitSet(new long[]{0x0000000000448000L});
    public static final BitSet FOLLOW_set_in_set_document169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_set_statement_group218 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_STATEMENT_GROUP_KEYWORD_in_set_statement_group220 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_set_statement_group222 = new BitSet(new long[]{0x0000000000048000L});
    public static final BitSet FOLLOW_set_in_set_statement_group224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_set_annotation269 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_OBJECT_IDENT_in_set_annotation271 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_set_annotation273 = new BitSet(new long[]{0x0000000000448000L});
    public static final BitSet FOLLOW_set_in_set_annotation275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_unset_statement_group324 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_STATEMENT_GROUP_KEYWORD_in_unset_statement_group326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_unset365 = new BitSet(new long[]{0x0000000000008800L});
    public static final BitSet FOLLOW_set_in_unset367 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_define_namespace413 = new BitSet(new long[]{0x0000104000000000L});
    public static final BitSet FOLLOW_38_in_define_namespace417 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_define_namespace421 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_OBJECT_IDENT_in_define_namespace425 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_define_namespace427 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_define_namespace429 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_QUOTED_VALUE_in_define_namespace431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_define_annotation471 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_define_annotation473 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_OBJECT_IDENT_in_define_annotation476 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_define_annotation478 = new BitSet(new long[]{0x0002440000000000L});
    public static final BitSet FOLLOW_set_in_define_annotation482 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_QUOTED_VALUE_in_define_annotation490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_define_annotation496 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_VALUE_LIST_in_define_annotation498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_outer_term_in_statement628 = new BitSet(new long[]{0x22C00001DE080002L,0x28402280607E0640L});
    public static final BitSet FOLLOW_relationship_in_statement631 = new BitSet(new long[]{0xDD38000000010000L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_OPEN_PAREN_in_statement635 = new BitSet(new long[]{0xDD38000000000000L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_outer_term_in_statement637 = new BitSet(new long[]{0x22C00001DE000000L,0x28402280607E0640L});
    public static final BitSet FOLLOW_relationship_in_statement639 = new BitSet(new long[]{0xDD38000000000000L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_outer_term_in_statement641 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_CLOSE_PAREN_in_statement643 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_outer_term_in_statement648 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_STATEMENT_COMMENT_in_statement653 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_outer_term674 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_OPEN_PAREN_in_outer_term676 = new BitSet(new long[]{0xDD3800000104C010L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_24_in_outer_term679 = new BitSet(new long[]{0xDD3800000004C000L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_argument_in_outer_term682 = new BitSet(new long[]{0xDD3800000104C010L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_CLOSE_PAREN_in_outer_term686 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_param_in_argument706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_argument710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_term726 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_OPEN_PAREN_in_term728 = new BitSet(new long[]{0xDD3800000104C010L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_24_in_term731 = new BitSet(new long[]{0xDD3800000004C000L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_term_in_term735 = new BitSet(new long[]{0xDD3800000104C010L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_param_in_term739 = new BitSet(new long[]{0xDD3800000104C010L,0xD7BFDD7F9F81F9BFL,0x0000000000000007L});
    public static final BitSet FOLLOW_CLOSE_PAREN_in_term744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NS_PREFIX_in_param764 = new BitSet(new long[]{0x0000000000048000L});
    public static final BitSet FOLLOW_set_in_param767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_106_in_function805 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_function831 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_114_in_function872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_function903 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_function944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_function978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_function1019 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_function1045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_78_in_function1086 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_function1115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_56_in_function1156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_function1182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_function1222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_96_in_function1255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_function1293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_function1320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_126_in_function1355 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_121_in_function1385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_function1423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_116_in_function1453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_function1492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_120_in_function1513 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_111_in_function1551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_115_in_function1585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_function1624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_function1648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_function1681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_75_in_function1717 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_function1756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_function1787 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_function1826 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_function1851 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_function1890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_function1915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_88_in_function1954 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_87_in_function1982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_101_in_function2021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_function2044 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_function2082 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_function2107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_113_in_function2146 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_112_in_function2168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_124_in_function2206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_130_in_function2225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_127_in_function2260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_122_in_function2285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_80_in_function2322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_79_in_function2348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_function2387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_function2412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_107_in_function2450 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_102_in_function2473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_119_in_function2511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_117_in_function2541 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_129_in_function2580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_128_in_function2612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_110_in_function2649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_104_in_function2682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_function2716 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_relationship2782 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_relationship2816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_70_in_relationship2856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_relationship2890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_relationship2930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_relationship2955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_relationship2995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_relationship3020 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_relationship3060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_103_in_relationship3088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_relationship3111 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_125_in_relationship3134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_relationship3164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_123_in_relationship3204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_relationship3233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_86_in_relationship3273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_118_in_relationship3312 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_109_in_relationship3342 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_57_in_relationship3366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_relationship3396 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_relationship3416 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_relationship3447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_relationship3480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_relationship3511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_84_in_relationship3551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_82_in_relationship3583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_relationship3612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_81_in_relationship3645 = new BitSet(new long[]{0x0000000000000002L});

}