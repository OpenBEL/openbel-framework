// $ANTLR 3.4 /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g 2013-01-08 15:04:16

    package org.openbel.framework.common.bel.parser;
  
    import java.util.ArrayList;
import java.util.List;

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
import org.openbel.bel.model.BELParseErrorException;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class BELStatementParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CLOSE_PAREN", "DIGIT", "EscapeSequence", "HexDigit", "LETTER", "NS_PREFIX", "NS_VALUE", "OPEN_PAREN", "OctalEscape", "QUOTED_VALUE", "UnicodeEscape", "WS", "','", "'--'", "'->'", "'-|'", "':>'", "'=>'", "'=|'", "'>>'", "'a'", "'abundance'", "'act'", "'analogous'", "'association'", "'biologicalProcess'", "'biomarkerFor'", "'bp'", "'cat'", "'catalyticActivity'", "'causesNoChange'", "'cellSecretion'", "'cellSurfaceExpression'", "'chap'", "'chaperoneActivity'", "'complex'", "'complexAbundance'", "'composite'", "'compositeAbundance'", "'decreases'", "'deg'", "'degradation'", "'directlyDecreases'", "'directlyIncreases'", "'fus'", "'fusion'", "'g'", "'geneAbundance'", "'gtp'", "'gtpBoundActivity'", "'hasComponent'", "'hasComponents'", "'hasMember'", "'hasMembers'", "'increases'", "'isA'", "'kin'", "'kinaseActivity'", "'list'", "'m'", "'microRNAAbundance'", "'molecularActivity'", "'negativeCorrelation'", "'orthologous'", "'p'", "'path'", "'pathology'", "'pep'", "'peptidaseActivity'", "'phos'", "'phosphataseActivity'", "'pmod'", "'positiveCorrelation'", "'products'", "'prognosticBiomarkerFor'", "'proteinAbundance'", "'proteinModification'", "'r'", "'rateLimitingStepOf'", "'reactants'", "'reaction'", "'ribo'", "'ribosylationActivity'", "'rnaAbundance'", "'rxn'", "'sec'", "'sub'", "'subProcessOf'", "'substitution'", "'surf'", "'tloc'", "'tport'", "'transcribedTo'", "'transcriptionalActivity'", "'translatedTo'", "'translocation'", "'transportActivity'", "'trunc'", "'truncation'", "'tscript'"
    };

    public static final int EOF=-1;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__19=19;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int T__22=22;
    public static final int T__23=23;
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
    public static final int CLOSE_PAREN=4;
    public static final int DIGIT=5;
    public static final int EscapeSequence=6;
    public static final int HexDigit=7;
    public static final int LETTER=8;
    public static final int NS_PREFIX=9;
    public static final int NS_VALUE=10;
    public static final int OPEN_PAREN=11;
    public static final int OctalEscape=12;
    public static final int QUOTED_VALUE=13;
    public static final int UnicodeEscape=14;
    public static final int WS=15;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

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
    public String[] getTokenNames() { return BELStatementParser.tokenNames; }
    public String getGrammarFileName() { return "/home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g"; }


        private final List<BELParseErrorException> errors = new ArrayList<BELParseErrorException>();
        
        public List<BELParseErrorException> getSyntaxErrors() {
            return errors;
        }
        
        @Override
        public void emitErrorMessage(String msg) {
        }
        
        @Override
        public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
            String context = "";
            errors.add(new BELParseErrorException.SyntaxException(e.line, e.charPositionInLine, context, e));
        }


    public static class statement_return extends ParserRuleReturnScope {
        public Statement r;
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "statement"
    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:61:1: statement returns [Statement r] : st= outer_term (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term ) )? ;
    public final BELStatementParser.statement_return statement() throws RecognitionException {
        BELStatementParser.statement_return retval = new BELStatementParser.statement_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OPEN_PAREN1=null;
        Token CLOSE_PAREN2=null;
        BELStatementParser.outer_term_return st =null;

        BELStatementParser.relationship_return rel =null;

        BELStatementParser.outer_term_return nst =null;

        BELStatementParser.relationship_return nrel =null;

        BELStatementParser.outer_term_return not =null;

        BELStatementParser.outer_term_return ot =null;


        Object OPEN_PAREN1_tree=null;
        Object CLOSE_PAREN2_tree=null;

        try {
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:61:32: (st= outer_term (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term ) )? )
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:62:5: st= outer_term (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term ) )?
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_outer_term_in_statement81);
            st=outer_term();

            state._fsp--;

            adaptor.addChild(root_0, st.getTree());


                    final Statement s = new Statement((st!=null?st.r:null));
                    retval.r = s;
                

            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:66:5: (rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term ) )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0 >= 17 && LA2_0 <= 23)||(LA2_0 >= 27 && LA2_0 <= 28)||LA2_0==30||LA2_0==34||LA2_0==43||(LA2_0 >= 46 && LA2_0 <= 47)||(LA2_0 >= 54 && LA2_0 <= 59)||(LA2_0 >= 66 && LA2_0 <= 67)||LA2_0==76||LA2_0==78||LA2_0==82||LA2_0==91||LA2_0==96||LA2_0==98) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:67:9: rel= relationship ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term )
                    {
                    pushFollow(FOLLOW_relationship_in_statement101);
                    rel=relationship();

                    state._fsp--;

                    adaptor.addChild(root_0, rel.getTree());


                                s.setRelationshipType((rel!=null?rel.r:null));
                            

                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:70:9: ( ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN ) |ot= outer_term )
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==OPEN_PAREN) ) {
                        alt1=1;
                    }
                    else if ( ((LA1_0 >= 24 && LA1_0 <= 26)||LA1_0==29||(LA1_0 >= 31 && LA1_0 <= 33)||(LA1_0 >= 35 && LA1_0 <= 42)||(LA1_0 >= 44 && LA1_0 <= 45)||(LA1_0 >= 48 && LA1_0 <= 53)||(LA1_0 >= 60 && LA1_0 <= 65)||(LA1_0 >= 68 && LA1_0 <= 75)||LA1_0==77||(LA1_0 >= 79 && LA1_0 <= 81)||(LA1_0 >= 83 && LA1_0 <= 90)||(LA1_0 >= 92 && LA1_0 <= 95)||LA1_0==97||(LA1_0 >= 99 && LA1_0 <= 103)) ) {
                        alt1=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 0, input);

                        throw nvae;

                    }
                    switch (alt1) {
                        case 1 :
                            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:71:13: ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN )
                            {
                            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:71:13: ( OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN )
                            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:72:17: OPEN_PAREN nst= outer_term nrel= relationship not= outer_term CLOSE_PAREN
                            {
                            OPEN_PAREN1=(Token)match(input,OPEN_PAREN,FOLLOW_OPEN_PAREN_in_statement145); 
                            OPEN_PAREN1_tree = 
                            (Object)adaptor.create(OPEN_PAREN1)
                            ;
                            adaptor.addChild(root_0, OPEN_PAREN1_tree);


                            pushFollow(FOLLOW_outer_term_in_statement166);
                            nst=outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, nst.getTree());


                                                final Statement ns = new Statement((nst!=null?nst.r:null));
                                            

                            pushFollow(FOLLOW_relationship_in_statement188);
                            nrel=relationship();

                            state._fsp--;

                            adaptor.addChild(root_0, nrel.getTree());


                                                ns.setRelationshipType((nrel!=null?nrel.r:null));
                                            

                            pushFollow(FOLLOW_outer_term_in_statement211);
                            not=outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, not.getTree());


                                                ns.setObject(new Statement.Object((not!=null?not.r:null)));
                                                s.setObject(new Statement.Object(ns));
                                                retval.r = s;
                                            

                            CLOSE_PAREN2=(Token)match(input,CLOSE_PAREN,FOLLOW_CLOSE_PAREN_in_statement231); 
                            CLOSE_PAREN2_tree = 
                            (Object)adaptor.create(CLOSE_PAREN2)
                            ;
                            adaptor.addChild(root_0, CLOSE_PAREN2_tree);


                            }


                            }
                            break;
                        case 2 :
                            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:87:17: ot= outer_term
                            {
                            pushFollow(FOLLOW_outer_term_in_statement280);
                            ot=outer_term();

                            state._fsp--;

                            adaptor.addChild(root_0, ot.getTree());


                                                s.setObject(new Statement.Object((ot!=null?ot.r:null)));
                                                retval.r = s;
                                            

                            }
                            break;

                    }


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
    // $ANTLR end "statement"


    public static class outer_term_return extends ParserRuleReturnScope {
        public Term r;
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "outer_term"
    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:95:1: outer_term returns [Term r] : f= function OPEN_PAREN ( ( ',' )? arg= argument )* CLOSE_PAREN ;
    public final BELStatementParser.outer_term_return outer_term() throws RecognitionException {
        BELStatementParser.outer_term_return retval = new BELStatementParser.outer_term_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OPEN_PAREN3=null;
        Token char_literal4=null;
        Token CLOSE_PAREN5=null;
        BELStatementParser.function_return f =null;

        BELStatementParser.argument_return arg =null;


        Object OPEN_PAREN3_tree=null;
        Object char_literal4_tree=null;
        Object CLOSE_PAREN5_tree=null;

        try {
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:95:28: (f= function OPEN_PAREN ( ( ',' )? arg= argument )* CLOSE_PAREN )
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:96:5: f= function OPEN_PAREN ( ( ',' )? arg= argument )* CLOSE_PAREN
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_function_in_outer_term322);
            f=function();

            state._fsp--;

            adaptor.addChild(root_0, f.getTree());


                    final Term outerTerm = new Term((f!=null?f.r:null));
                

            OPEN_PAREN3=(Token)match(input,OPEN_PAREN,FOLLOW_OPEN_PAREN_in_outer_term330); 
            OPEN_PAREN3_tree = 
            (Object)adaptor.create(OPEN_PAREN3)
            ;
            adaptor.addChild(root_0, OPEN_PAREN3_tree);


            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:100:5: ( ( ',' )? arg= argument )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= NS_PREFIX && LA4_0 <= NS_VALUE)||LA4_0==QUOTED_VALUE||LA4_0==16||(LA4_0 >= 24 && LA4_0 <= 26)||LA4_0==29||(LA4_0 >= 31 && LA4_0 <= 33)||(LA4_0 >= 35 && LA4_0 <= 42)||(LA4_0 >= 44 && LA4_0 <= 45)||(LA4_0 >= 48 && LA4_0 <= 53)||(LA4_0 >= 60 && LA4_0 <= 65)||(LA4_0 >= 68 && LA4_0 <= 75)||LA4_0==77||(LA4_0 >= 79 && LA4_0 <= 81)||(LA4_0 >= 83 && LA4_0 <= 90)||(LA4_0 >= 92 && LA4_0 <= 95)||LA4_0==97||(LA4_0 >= 99 && LA4_0 <= 103)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:100:6: ( ',' )? arg= argument
            	    {
            	    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:100:6: ( ',' )?
            	    int alt3=2;
            	    int LA3_0 = input.LA(1);

            	    if ( (LA3_0==16) ) {
            	        alt3=1;
            	    }
            	    switch (alt3) {
            	        case 1 :
            	            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:100:6: ','
            	            {
            	            char_literal4=(Token)match(input,16,FOLLOW_16_in_outer_term337); 
            	            char_literal4_tree = 
            	            (Object)adaptor.create(char_literal4)
            	            ;
            	            adaptor.addChild(root_0, char_literal4_tree);


            	            }
            	            break;

            	    }


            	    pushFollow(FOLLOW_argument_in_outer_term342);
            	    arg=argument();

            	    state._fsp--;

            	    adaptor.addChild(root_0, arg.getTree());

            	     outerTerm.addFunctionArgument((arg!=null?arg.r:null)); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            CLOSE_PAREN5=(Token)match(input,CLOSE_PAREN,FOLLOW_CLOSE_PAREN_in_outer_term352); 
            CLOSE_PAREN5_tree = 
            (Object)adaptor.create(CLOSE_PAREN5)
            ;
            adaptor.addChild(root_0, CLOSE_PAREN5_tree);



                    retval.r = outerTerm;
                

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
        public BELObject r;
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "argument"
    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:106:1: argument returns [BELObject r] : (fp= param |ff= term );
    public final BELStatementParser.argument_return argument() throws RecognitionException {
        BELStatementParser.argument_return retval = new BELStatementParser.argument_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        BELStatementParser.param_return fp =null;

        BELStatementParser.term_return ff =null;



        try {
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:106:31: (fp= param |ff= term )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( ((LA5_0 >= NS_PREFIX && LA5_0 <= NS_VALUE)||LA5_0==QUOTED_VALUE) ) {
                alt5=1;
            }
            else if ( ((LA5_0 >= 24 && LA5_0 <= 26)||LA5_0==29||(LA5_0 >= 31 && LA5_0 <= 33)||(LA5_0 >= 35 && LA5_0 <= 42)||(LA5_0 >= 44 && LA5_0 <= 45)||(LA5_0 >= 48 && LA5_0 <= 53)||(LA5_0 >= 60 && LA5_0 <= 65)||(LA5_0 >= 68 && LA5_0 <= 75)||LA5_0==77||(LA5_0 >= 79 && LA5_0 <= 81)||(LA5_0 >= 83 && LA5_0 <= 90)||(LA5_0 >= 92 && LA5_0 <= 95)||LA5_0==97||(LA5_0 >= 99 && LA5_0 <= 103)) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:107:5: fp= param
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_param_in_argument380);
                    fp=param();

                    state._fsp--;

                    adaptor.addChild(root_0, fp.getTree());

                     retval.r = (fp!=null?fp.r:null); 

                    }
                    break;
                case 2 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:108:5: ff= term
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_term_in_argument392);
                    ff=term();

                    state._fsp--;

                    adaptor.addChild(root_0, ff.getTree());

                     retval.r = (ff!=null?ff.r:null); 

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
        public Term r;
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "term"
    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:111:1: term returns [Term r] : pfv= function OPEN_PAREN ( ( ',' )? (it= term |pp= param ) )* CLOSE_PAREN ;
    public final BELStatementParser.term_return term() throws RecognitionException {
        BELStatementParser.term_return retval = new BELStatementParser.term_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token OPEN_PAREN6=null;
        Token char_literal7=null;
        Token CLOSE_PAREN8=null;
        BELStatementParser.function_return pfv =null;

        BELStatementParser.term_return it =null;

        BELStatementParser.param_return pp =null;


        Object OPEN_PAREN6_tree=null;
        Object char_literal7_tree=null;
        Object CLOSE_PAREN8_tree=null;

        try {
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:111:22: (pfv= function OPEN_PAREN ( ( ',' )? (it= term |pp= param ) )* CLOSE_PAREN )
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:112:5: pfv= function OPEN_PAREN ( ( ',' )? (it= term |pp= param ) )* CLOSE_PAREN
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_function_in_term416);
            pfv=function();

            state._fsp--;

            adaptor.addChild(root_0, pfv.getTree());


                    final Term parentTerm = new Term((pfv!=null?pfv.r:null));
                

            OPEN_PAREN6=(Token)match(input,OPEN_PAREN,FOLLOW_OPEN_PAREN_in_term424); 
            OPEN_PAREN6_tree = 
            (Object)adaptor.create(OPEN_PAREN6)
            ;
            adaptor.addChild(root_0, OPEN_PAREN6_tree);


            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:116:5: ( ( ',' )? (it= term |pp= param ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0 >= NS_PREFIX && LA8_0 <= NS_VALUE)||LA8_0==QUOTED_VALUE||LA8_0==16||(LA8_0 >= 24 && LA8_0 <= 26)||LA8_0==29||(LA8_0 >= 31 && LA8_0 <= 33)||(LA8_0 >= 35 && LA8_0 <= 42)||(LA8_0 >= 44 && LA8_0 <= 45)||(LA8_0 >= 48 && LA8_0 <= 53)||(LA8_0 >= 60 && LA8_0 <= 65)||(LA8_0 >= 68 && LA8_0 <= 75)||LA8_0==77||(LA8_0 >= 79 && LA8_0 <= 81)||(LA8_0 >= 83 && LA8_0 <= 90)||(LA8_0 >= 92 && LA8_0 <= 95)||LA8_0==97||(LA8_0 >= 99 && LA8_0 <= 103)) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:117:9: ( ',' )? (it= term |pp= param )
            	    {
            	    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:117:9: ( ',' )?
            	    int alt6=2;
            	    int LA6_0 = input.LA(1);

            	    if ( (LA6_0==16) ) {
            	        alt6=1;
            	    }
            	    switch (alt6) {
            	        case 1 :
            	            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:117:9: ','
            	            {
            	            char_literal7=(Token)match(input,16,FOLLOW_16_in_term440); 
            	            char_literal7_tree = 
            	            (Object)adaptor.create(char_literal7)
            	            ;
            	            adaptor.addChild(root_0, char_literal7_tree);


            	            }
            	            break;

            	    }


            	    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:118:9: (it= term |pp= param )
            	    int alt7=2;
            	    int LA7_0 = input.LA(1);

            	    if ( ((LA7_0 >= 24 && LA7_0 <= 26)||LA7_0==29||(LA7_0 >= 31 && LA7_0 <= 33)||(LA7_0 >= 35 && LA7_0 <= 42)||(LA7_0 >= 44 && LA7_0 <= 45)||(LA7_0 >= 48 && LA7_0 <= 53)||(LA7_0 >= 60 && LA7_0 <= 65)||(LA7_0 >= 68 && LA7_0 <= 75)||LA7_0==77||(LA7_0 >= 79 && LA7_0 <= 81)||(LA7_0 >= 83 && LA7_0 <= 90)||(LA7_0 >= 92 && LA7_0 <= 95)||LA7_0==97||(LA7_0 >= 99 && LA7_0 <= 103)) ) {
            	        alt7=1;
            	    }
            	    else if ( ((LA7_0 >= NS_PREFIX && LA7_0 <= NS_VALUE)||LA7_0==QUOTED_VALUE) ) {
            	        alt7=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 7, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt7) {
            	        case 1 :
            	            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:118:10: it= term
            	            {
            	            pushFollow(FOLLOW_term_in_term454);
            	            it=term();

            	            state._fsp--;

            	            adaptor.addChild(root_0, it.getTree());


            	                        parentTerm.addFunctionArgument((it!=null?it.r:null));
            	                    

            	            }
            	            break;
            	        case 2 :
            	            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:121:9: pp= param
            	            {
            	            pushFollow(FOLLOW_param_in_term470);
            	            pp=param();

            	            state._fsp--;

            	            adaptor.addChild(root_0, pp.getTree());

            	             parentTerm.addFunctionArgument((pp!=null?pp.r:null)); 

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            CLOSE_PAREN8=(Token)match(input,CLOSE_PAREN,FOLLOW_CLOSE_PAREN_in_term486); 
            CLOSE_PAREN8_tree = 
            (Object)adaptor.create(CLOSE_PAREN8)
            ;
            adaptor.addChild(root_0, CLOSE_PAREN8_tree);



                    retval.r = parentTerm;
                

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
        public Parameter r;
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "param"
    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:128:1: param returns [Parameter r] : (nsp= NS_PREFIX )? ( NS_VALUE | QUOTED_VALUE ) ;
    public final BELStatementParser.param_return param() throws RecognitionException {
        BELStatementParser.param_return retval = new BELStatementParser.param_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token nsp=null;
        Token NS_VALUE9=null;
        Token QUOTED_VALUE10=null;

        Object nsp_tree=null;
        Object NS_VALUE9_tree=null;
        Object QUOTED_VALUE10_tree=null;

        try {
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:128:28: ( (nsp= NS_PREFIX )? ( NS_VALUE | QUOTED_VALUE ) )
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:129:5: (nsp= NS_PREFIX )? ( NS_VALUE | QUOTED_VALUE )
            {
            root_0 = (Object)adaptor.nil();


            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:129:8: (nsp= NS_PREFIX )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==NS_PREFIX) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:129:8: nsp= NS_PREFIX
                    {
                    nsp=(Token)match(input,NS_PREFIX,FOLLOW_NS_PREFIX_in_param510); 
                    nsp_tree = 
                    (Object)adaptor.create(nsp)
                    ;
                    adaptor.addChild(root_0, nsp_tree);


                    }
                    break;

            }


            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:130:5: ( NS_VALUE | QUOTED_VALUE )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==NS_VALUE) ) {
                alt10=1;
            }
            else if ( (LA10_0==QUOTED_VALUE) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:131:9: NS_VALUE
                    {
                    NS_VALUE9=(Token)match(input,NS_VALUE,FOLLOW_NS_VALUE_in_param527); 
                    NS_VALUE9_tree = 
                    (Object)adaptor.create(NS_VALUE9)
                    ;
                    adaptor.addChild(root_0, NS_VALUE9_tree);



                                Namespace ns = null;
                            
                                if(nsp != null) {
                                    String prefix = nsp.getText();
                                    prefix = prefix.substring(0, prefix.length() - 1);
                                    ns = new Namespace(prefix, "FIX_ME");
                                }
                            
                                retval.r = new Parameter();
                                retval.r.setValue(NS_VALUE9.getText());
                                retval.r.setNamespace(ns);
                            

                    }
                    break;
                case 2 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:144:9: QUOTED_VALUE
                    {
                    QUOTED_VALUE10=(Token)match(input,QUOTED_VALUE,FOLLOW_QUOTED_VALUE_in_param541); 
                    QUOTED_VALUE10_tree = 
                    (Object)adaptor.create(QUOTED_VALUE10)
                    ;
                    adaptor.addChild(root_0, QUOTED_VALUE10_tree);



                                Namespace ns = null;
                                
                                if(nsp != null) {
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
        public FunctionEnum r;
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "function"
    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:160:1: function returns [FunctionEnum r] : (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' ) ;
    public final BELStatementParser.function_return function() throws RecognitionException {
        BELStatementParser.function_return retval = new BELStatementParser.function_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token fv=null;

        Object fv_tree=null;

        try {
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:160:34: ( (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' ) )
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:161:5: (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' )
            {
            root_0 = (Object)adaptor.nil();


            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:161:5: (fv= 'proteinAbundance' |fv= 'p' |fv= 'rnaAbundance' |fv= 'r' |fv= 'abundance' |fv= 'a' |fv= 'microRNAAbundance' |fv= 'm' |fv= 'geneAbundance' |fv= 'g' |fv= 'biologicalProcess' |fv= 'bp' |fv= 'pathology' |fv= 'path' |fv= 'complexAbundance' |fv= 'complex' |fv= 'translocation' |fv= 'tloc' |fv= 'cellSecretion' |fv= 'sec' |fv= 'cellSurfaceExpression' |fv= 'surf' |fv= 'reaction' |fv= 'rxn' |fv= 'compositeAbundance' |fv= 'composite' |fv= 'fusion' |fv= 'fus' |fv= 'degradation' |fv= 'deg' |fv= 'molecularActivity' |fv= 'act' |fv= 'catalyticActivity' |fv= 'cat' |fv= 'kinaseActivity' |fv= 'kin' |fv= 'phosphataseActivity' |fv= 'phos' |fv= 'peptidaseActivity' |fv= 'pep' |fv= 'ribosylationActivity' |fv= 'ribo' |fv= 'transcriptionalActivity' |fv= 'tscript' |fv= 'transportActivity' |fv= 'tport' |fv= 'gtpBoundActivity' |fv= 'gtp' |fv= 'chaperoneActivity' |fv= 'chap' |fv= 'proteinModification' |fv= 'pmod' |fv= 'substitution' |fv= 'sub' |fv= 'truncation' |fv= 'trunc' |fv= 'reactants' |fv= 'products' |fv= 'list' )
            int alt11=59;
            switch ( input.LA(1) ) {
            case 79:
                {
                alt11=1;
                }
                break;
            case 68:
                {
                alt11=2;
                }
                break;
            case 87:
                {
                alt11=3;
                }
                break;
            case 81:
                {
                alt11=4;
                }
                break;
            case 25:
                {
                alt11=5;
                }
                break;
            case 24:
                {
                alt11=6;
                }
                break;
            case 64:
                {
                alt11=7;
                }
                break;
            case 63:
                {
                alt11=8;
                }
                break;
            case 51:
                {
                alt11=9;
                }
                break;
            case 50:
                {
                alt11=10;
                }
                break;
            case 29:
                {
                alt11=11;
                }
                break;
            case 31:
                {
                alt11=12;
                }
                break;
            case 70:
                {
                alt11=13;
                }
                break;
            case 69:
                {
                alt11=14;
                }
                break;
            case 40:
                {
                alt11=15;
                }
                break;
            case 39:
                {
                alt11=16;
                }
                break;
            case 99:
                {
                alt11=17;
                }
                break;
            case 94:
                {
                alt11=18;
                }
                break;
            case 35:
                {
                alt11=19;
                }
                break;
            case 89:
                {
                alt11=20;
                }
                break;
            case 36:
                {
                alt11=21;
                }
                break;
            case 93:
                {
                alt11=22;
                }
                break;
            case 84:
                {
                alt11=23;
                }
                break;
            case 88:
                {
                alt11=24;
                }
                break;
            case 42:
                {
                alt11=25;
                }
                break;
            case 41:
                {
                alt11=26;
                }
                break;
            case 49:
                {
                alt11=27;
                }
                break;
            case 48:
                {
                alt11=28;
                }
                break;
            case 45:
                {
                alt11=29;
                }
                break;
            case 44:
                {
                alt11=30;
                }
                break;
            case 65:
                {
                alt11=31;
                }
                break;
            case 26:
                {
                alt11=32;
                }
                break;
            case 33:
                {
                alt11=33;
                }
                break;
            case 32:
                {
                alt11=34;
                }
                break;
            case 61:
                {
                alt11=35;
                }
                break;
            case 60:
                {
                alt11=36;
                }
                break;
            case 74:
                {
                alt11=37;
                }
                break;
            case 73:
                {
                alt11=38;
                }
                break;
            case 72:
                {
                alt11=39;
                }
                break;
            case 71:
                {
                alt11=40;
                }
                break;
            case 86:
                {
                alt11=41;
                }
                break;
            case 85:
                {
                alt11=42;
                }
                break;
            case 97:
                {
                alt11=43;
                }
                break;
            case 103:
                {
                alt11=44;
                }
                break;
            case 100:
                {
                alt11=45;
                }
                break;
            case 95:
                {
                alt11=46;
                }
                break;
            case 53:
                {
                alt11=47;
                }
                break;
            case 52:
                {
                alt11=48;
                }
                break;
            case 38:
                {
                alt11=49;
                }
                break;
            case 37:
                {
                alt11=50;
                }
                break;
            case 80:
                {
                alt11=51;
                }
                break;
            case 75:
                {
                alt11=52;
                }
                break;
            case 92:
                {
                alt11=53;
                }
                break;
            case 90:
                {
                alt11=54;
                }
                break;
            case 102:
                {
                alt11=55;
                }
                break;
            case 101:
                {
                alt11=56;
                }
                break;
            case 83:
                {
                alt11=57;
                }
                break;
            case 77:
                {
                alt11=58;
                }
                break;
            case 62:
                {
                alt11=59;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:162:9: fv= 'proteinAbundance'
                    {
                    fv=(Token)match(input,79,FOLLOW_79_in_function581); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 2 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:163:9: fv= 'p'
                    {
                    fv=(Token)match(input,68,FOLLOW_68_in_function607); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 3 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:164:9: fv= 'rnaAbundance'
                    {
                    fv=(Token)match(input,87,FOLLOW_87_in_function648); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 4 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:165:9: fv= 'r'
                    {
                    fv=(Token)match(input,81,FOLLOW_81_in_function679); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 5 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:166:9: fv= 'abundance'
                    {
                    fv=(Token)match(input,25,FOLLOW_25_in_function720); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 6 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:167:9: fv= 'a'
                    {
                    fv=(Token)match(input,24,FOLLOW_24_in_function754); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 7 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:168:9: fv= 'microRNAAbundance'
                    {
                    fv=(Token)match(input,64,FOLLOW_64_in_function795); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 8 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:169:9: fv= 'm'
                    {
                    fv=(Token)match(input,63,FOLLOW_63_in_function821); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 9 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:170:9: fv= 'geneAbundance'
                    {
                    fv=(Token)match(input,51,FOLLOW_51_in_function862); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 10 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:171:9: fv= 'g'
                    {
                    fv=(Token)match(input,50,FOLLOW_50_in_function891); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 11 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:172:9: fv= 'biologicalProcess'
                    {
                    fv=(Token)match(input,29,FOLLOW_29_in_function932); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 12 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:173:9: fv= 'bp'
                    {
                    fv=(Token)match(input,31,FOLLOW_31_in_function958); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 13 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:174:9: fv= 'pathology'
                    {
                    fv=(Token)match(input,70,FOLLOW_70_in_function998); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 14 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:175:9: fv= 'path'
                    {
                    fv=(Token)match(input,69,FOLLOW_69_in_function1031); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 15 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:176:9: fv= 'complexAbundance'
                    {
                    fv=(Token)match(input,40,FOLLOW_40_in_function1069); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 16 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:177:9: fv= 'complex'
                    {
                    fv=(Token)match(input,39,FOLLOW_39_in_function1096); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 17 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:178:9: fv= 'translocation'
                    {
                    fv=(Token)match(input,99,FOLLOW_99_in_function1131); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 18 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:179:9: fv= 'tloc'
                    {
                    fv=(Token)match(input,94,FOLLOW_94_in_function1161); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 19 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:180:9: fv= 'cellSecretion'
                    {
                    fv=(Token)match(input,35,FOLLOW_35_in_function1199); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 20 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:181:9: fv= 'sec'
                    {
                    fv=(Token)match(input,89,FOLLOW_89_in_function1229); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 21 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:182:9: fv= 'cellSurfaceExpression'
                    {
                    fv=(Token)match(input,36,FOLLOW_36_in_function1268); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 22 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:183:9: fv= 'surf'
                    {
                    fv=(Token)match(input,93,FOLLOW_93_in_function1289); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 23 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:184:9: fv= 'reaction'
                    {
                    fv=(Token)match(input,84,FOLLOW_84_in_function1327); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 24 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:185:9: fv= 'rxn'
                    {
                    fv=(Token)match(input,88,FOLLOW_88_in_function1361); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 25 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:186:9: fv= 'compositeAbundance'
                    {
                    fv=(Token)match(input,42,FOLLOW_42_in_function1400); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 26 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:187:9: fv= 'composite'
                    {
                    fv=(Token)match(input,41,FOLLOW_41_in_function1424); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 27 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:188:9: fv= 'fusion'
                    {
                    fv=(Token)match(input,49,FOLLOW_49_in_function1457); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 28 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:189:9: fv= 'fus'
                    {
                    fv=(Token)match(input,48,FOLLOW_48_in_function1493); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 29 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:190:9: fv= 'degradation'
                    {
                    fv=(Token)match(input,45,FOLLOW_45_in_function1532); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 30 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:191:9: fv= 'deg'
                    {
                    fv=(Token)match(input,44,FOLLOW_44_in_function1563); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 31 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:192:9: fv= 'molecularActivity'
                    {
                    fv=(Token)match(input,65,FOLLOW_65_in_function1602); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 32 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:193:9: fv= 'act'
                    {
                    fv=(Token)match(input,26,FOLLOW_26_in_function1627); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 33 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:194:9: fv= 'catalyticActivity'
                    {
                    fv=(Token)match(input,33,FOLLOW_33_in_function1666); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 34 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:195:9: fv= 'cat'
                    {
                    fv=(Token)match(input,32,FOLLOW_32_in_function1691); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 35 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:196:9: fv= 'kinaseActivity'
                    {
                    fv=(Token)match(input,61,FOLLOW_61_in_function1730); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 36 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:197:9: fv= 'kin'
                    {
                    fv=(Token)match(input,60,FOLLOW_60_in_function1758); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 37 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:198:9: fv= 'phosphataseActivity'
                    {
                    fv=(Token)match(input,74,FOLLOW_74_in_function1797); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 38 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:199:9: fv= 'phos'
                    {
                    fv=(Token)match(input,73,FOLLOW_73_in_function1820); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 39 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:200:9: fv= 'peptidaseActivity'
                    {
                    fv=(Token)match(input,72,FOLLOW_72_in_function1858); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 40 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:201:9: fv= 'pep'
                    {
                    fv=(Token)match(input,71,FOLLOW_71_in_function1883); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 41 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:202:9: fv= 'ribosylationActivity'
                    {
                    fv=(Token)match(input,86,FOLLOW_86_in_function1922); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 42 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:203:9: fv= 'ribo'
                    {
                    fv=(Token)match(input,85,FOLLOW_85_in_function1944); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 43 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:204:9: fv= 'transcriptionalActivity'
                    {
                    fv=(Token)match(input,97,FOLLOW_97_in_function1982); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 44 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:205:9: fv= 'tscript'
                    {
                    fv=(Token)match(input,103,FOLLOW_103_in_function2001); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 45 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:206:9: fv= 'transportActivity'
                    {
                    fv=(Token)match(input,100,FOLLOW_100_in_function2036); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 46 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:207:9: fv= 'tport'
                    {
                    fv=(Token)match(input,95,FOLLOW_95_in_function2061); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 47 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:208:9: fv= 'gtpBoundActivity'
                    {
                    fv=(Token)match(input,53,FOLLOW_53_in_function2098); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 48 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:209:9: fv= 'gtp'
                    {
                    fv=(Token)match(input,52,FOLLOW_52_in_function2124); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 49 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:210:9: fv= 'chaperoneActivity'
                    {
                    fv=(Token)match(input,38,FOLLOW_38_in_function2163); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 50 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:211:9: fv= 'chap'
                    {
                    fv=(Token)match(input,37,FOLLOW_37_in_function2188); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 51 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:212:9: fv= 'proteinModification'
                    {
                    fv=(Token)match(input,80,FOLLOW_80_in_function2226); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 52 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:213:9: fv= 'pmod'
                    {
                    fv=(Token)match(input,75,FOLLOW_75_in_function2249); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 53 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:214:9: fv= 'substitution'
                    {
                    fv=(Token)match(input,92,FOLLOW_92_in_function2288); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 54 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:215:9: fv= 'sub'
                    {
                    fv=(Token)match(input,90,FOLLOW_90_in_function2318); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 55 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:216:9: fv= 'truncation'
                    {
                    fv=(Token)match(input,102,FOLLOW_102_in_function2357); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 56 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:217:9: fv= 'trunc'
                    {
                    fv=(Token)match(input,101,FOLLOW_101_in_function2389); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 57 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:218:9: fv= 'reactants'
                    {
                    fv=(Token)match(input,83,FOLLOW_83_in_function2426); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 58 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:219:9: fv= 'products'
                    {
                    fv=(Token)match(input,77,FOLLOW_77_in_function2459); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

                    }
                    break;
                case 59 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:220:9: fv= 'list'
                    {
                    fv=(Token)match(input,62,FOLLOW_62_in_function2493); 
                    fv_tree = 
                    (Object)adaptor.create(fv)
                    ;
                    adaptor.addChild(root_0, fv_tree);


                    retval.r = FunctionEnum.getFunctionEnum(fv.getText());

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
        public RelationshipType r;
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "relationship"
    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:224:1: relationship returns [RelationshipType r] : (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' ) ;
    public final BELStatementParser.relationship_return relationship() throws RecognitionException {
        BELStatementParser.relationship_return retval = new BELStatementParser.relationship_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token rv=null;

        Object rv_tree=null;

        try {
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:224:42: ( (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' ) )
            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:225:5: (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' )
            {
            root_0 = (Object)adaptor.nil();


            // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:225:5: (rv= 'increases' |rv= '->' |rv= 'decreases' |rv= '-|' |rv= 'directlyIncreases' |rv= '=>' |rv= 'directlyDecreases' |rv= '=|' |rv= 'causesNoChange' |rv= 'positiveCorrelation' |rv= 'negativeCorrelation' |rv= 'translatedTo' |rv= '>>' |rv= 'transcribedTo' |rv= ':>' |rv= 'isA' |rv= 'subProcessOf' |rv= 'rateLimitingStepOf' |rv= 'biomarkerFor' |rv= 'prognosticBiomarkerFor' |rv= 'orthologous' |rv= 'analogous' |rv= 'association' |rv= '--' |rv= 'hasMembers' |rv= 'hasComponents' |rv= 'hasMember' |rv= 'hasComponent' )
            int alt12=28;
            switch ( input.LA(1) ) {
            case 58:
                {
                alt12=1;
                }
                break;
            case 18:
                {
                alt12=2;
                }
                break;
            case 43:
                {
                alt12=3;
                }
                break;
            case 19:
                {
                alt12=4;
                }
                break;
            case 47:
                {
                alt12=5;
                }
                break;
            case 21:
                {
                alt12=6;
                }
                break;
            case 46:
                {
                alt12=7;
                }
                break;
            case 22:
                {
                alt12=8;
                }
                break;
            case 34:
                {
                alt12=9;
                }
                break;
            case 76:
                {
                alt12=10;
                }
                break;
            case 66:
                {
                alt12=11;
                }
                break;
            case 98:
                {
                alt12=12;
                }
                break;
            case 23:
                {
                alt12=13;
                }
                break;
            case 96:
                {
                alt12=14;
                }
                break;
            case 20:
                {
                alt12=15;
                }
                break;
            case 59:
                {
                alt12=16;
                }
                break;
            case 91:
                {
                alt12=17;
                }
                break;
            case 82:
                {
                alt12=18;
                }
                break;
            case 30:
                {
                alt12=19;
                }
                break;
            case 78:
                {
                alt12=20;
                }
                break;
            case 67:
                {
                alt12=21;
                }
                break;
            case 27:
                {
                alt12=22;
                }
                break;
            case 28:
                {
                alt12=23;
                }
                break;
            case 17:
                {
                alt12=24;
                }
                break;
            case 57:
                {
                alt12=25;
                }
                break;
            case 55:
                {
                alt12=26;
                }
                break;
            case 56:
                {
                alt12=27;
                }
                break;
            case 54:
                {
                alt12=28;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }

            switch (alt12) {
                case 1 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:226:9: rv= 'increases'
                    {
                    rv=(Token)match(input,58,FOLLOW_58_in_relationship2559); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 2 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:227:9: rv= '->'
                    {
                    rv=(Token)match(input,18,FOLLOW_18_in_relationship2592); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 3 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:228:9: rv= 'decreases'
                    {
                    rv=(Token)match(input,43,FOLLOW_43_in_relationship2632); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 4 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:229:9: rv= '-|'
                    {
                    rv=(Token)match(input,19,FOLLOW_19_in_relationship2665); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 5 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:230:9: rv= 'directlyIncreases'
                    {
                    rv=(Token)match(input,47,FOLLOW_47_in_relationship2705); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 6 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:231:9: rv= '=>'
                    {
                    rv=(Token)match(input,21,FOLLOW_21_in_relationship2730); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 7 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:232:9: rv= 'directlyDecreases'
                    {
                    rv=(Token)match(input,46,FOLLOW_46_in_relationship2770); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 8 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:233:9: rv= '=|'
                    {
                    rv=(Token)match(input,22,FOLLOW_22_in_relationship2795); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 9 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:234:9: rv= 'causesNoChange'
                    {
                    rv=(Token)match(input,34,FOLLOW_34_in_relationship2835); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 10 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:235:9: rv= 'positiveCorrelation'
                    {
                    rv=(Token)match(input,76,FOLLOW_76_in_relationship2863); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 11 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:236:9: rv= 'negativeCorrelation'
                    {
                    rv=(Token)match(input,66,FOLLOW_66_in_relationship2886); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 12 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:237:9: rv= 'translatedTo'
                    {
                    rv=(Token)match(input,98,FOLLOW_98_in_relationship2909); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 13 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:238:9: rv= '>>'
                    {
                    rv=(Token)match(input,23,FOLLOW_23_in_relationship2939); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 14 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:239:9: rv= 'transcribedTo'
                    {
                    rv=(Token)match(input,96,FOLLOW_96_in_relationship2979); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 15 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:240:9: rv= ':>'
                    {
                    rv=(Token)match(input,20,FOLLOW_20_in_relationship3008); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 16 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:241:9: rv= 'isA'
                    {
                    rv=(Token)match(input,59,FOLLOW_59_in_relationship3048); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 17 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:242:9: rv= 'subProcessOf'
                    {
                    rv=(Token)match(input,91,FOLLOW_91_in_relationship3087); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 18 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:243:9: rv= 'rateLimitingStepOf'
                    {
                    rv=(Token)match(input,82,FOLLOW_82_in_relationship3117); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 19 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:244:9: rv= 'biomarkerFor'
                    {
                    rv=(Token)match(input,30,FOLLOW_30_in_relationship3141); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 20 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:245:9: rv= 'prognosticBiomarkerFor'
                    {
                    rv=(Token)match(input,78,FOLLOW_78_in_relationship3171); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 21 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:246:9: rv= 'orthologous'
                    {
                    rv=(Token)match(input,67,FOLLOW_67_in_relationship3191); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 22 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:247:9: rv= 'analogous'
                    {
                    rv=(Token)match(input,27,FOLLOW_27_in_relationship3222); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 23 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:248:9: rv= 'association'
                    {
                    rv=(Token)match(input,28,FOLLOW_28_in_relationship3255); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 24 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:249:9: rv= '--'
                    {
                    rv=(Token)match(input,17,FOLLOW_17_in_relationship3286); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 25 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:250:9: rv= 'hasMembers'
                    {
                    rv=(Token)match(input,57,FOLLOW_57_in_relationship3326); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 26 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:251:9: rv= 'hasComponents'
                    {
                    rv=(Token)match(input,55,FOLLOW_55_in_relationship3358); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 27 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:252:9: rv= 'hasMember'
                    {
                    rv=(Token)match(input,56,FOLLOW_56_in_relationship3387); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

                    }
                    break;
                case 28 :
                    // /home/tony/work/git/openbel-framework/docs/belscript/grammar/BELStatement.g:253:9: rv= 'hasComponent'
                    {
                    rv=(Token)match(input,54,FOLLOW_54_in_relationship3420); 
                    rv_tree = 
                    (Object)adaptor.create(rv)
                    ;
                    adaptor.addChild(root_0, rv_tree);


                     retval.r = RelationshipType.fromString(rv.getText()); if(retval.r == null) { retval.r = RelationshipType.fromAbbreviation(rv.getText()); } 

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


 

    public static final BitSet FOLLOW_outer_term_in_statement81 = new BitSet(new long[]{0x0FC0C80458FE0002L,0x000000050804500CL});
    public static final BitSet FOLLOW_relationship_in_statement101 = new BitSet(new long[]{0xF03F37FBA7000800L,0x000000FAF7FBAFF3L});
    public static final BitSet FOLLOW_OPEN_PAREN_in_statement145 = new BitSet(new long[]{0xF03F37FBA7000000L,0x000000FAF7FBAFF3L});
    public static final BitSet FOLLOW_outer_term_in_statement166 = new BitSet(new long[]{0x0FC0C80458FE0000L,0x000000050804500CL});
    public static final BitSet FOLLOW_relationship_in_statement188 = new BitSet(new long[]{0xF03F37FBA7000000L,0x000000FAF7FBAFF3L});
    public static final BitSet FOLLOW_outer_term_in_statement211 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_CLOSE_PAREN_in_statement231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_outer_term_in_statement280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_outer_term322 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_OPEN_PAREN_in_outer_term330 = new BitSet(new long[]{0xF03F37FBA7012610L,0x000000FAF7FBAFF3L});
    public static final BitSet FOLLOW_16_in_outer_term337 = new BitSet(new long[]{0xF03F37FBA7002600L,0x000000FAF7FBAFF3L});
    public static final BitSet FOLLOW_argument_in_outer_term342 = new BitSet(new long[]{0xF03F37FBA7012610L,0x000000FAF7FBAFF3L});
    public static final BitSet FOLLOW_CLOSE_PAREN_in_outer_term352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_param_in_argument380 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_argument392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_term416 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_OPEN_PAREN_in_term424 = new BitSet(new long[]{0xF03F37FBA7012610L,0x000000FAF7FBAFF3L});
    public static final BitSet FOLLOW_16_in_term440 = new BitSet(new long[]{0xF03F37FBA7002600L,0x000000FAF7FBAFF3L});
    public static final BitSet FOLLOW_term_in_term454 = new BitSet(new long[]{0xF03F37FBA7012610L,0x000000FAF7FBAFF3L});
    public static final BitSet FOLLOW_param_in_term470 = new BitSet(new long[]{0xF03F37FBA7012610L,0x000000FAF7FBAFF3L});
    public static final BitSet FOLLOW_CLOSE_PAREN_in_term486 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NS_PREFIX_in_param510 = new BitSet(new long[]{0x0000000000002400L});
    public static final BitSet FOLLOW_NS_VALUE_in_param527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTED_VALUE_in_param541 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_79_in_function581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_function607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_87_in_function648 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_81_in_function679 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_function720 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_function754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_function795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_function821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_function862 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_function891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_function932 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_function958 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_70_in_function998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_function1031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_function1069 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_function1096 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_function1131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_function1161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_function1199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_function1229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_function1268 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_function1289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_84_in_function1327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_88_in_function1361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_function1400 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_function1424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_function1457 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_function1493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_function1532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_function1563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_function1602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_function1627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_function1666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_function1691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_function1730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_function1758 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_function1797 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_function1820 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_function1858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_function1883 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_86_in_function1922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_function1944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_function1982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_103_in_function2001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_function2036 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_function2061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_function2098 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_function2124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_function2163 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_function2188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_80_in_function2226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_75_in_function2249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_function2288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_function2318 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_102_in_function2357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_101_in_function2389 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_function2426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_function2459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_function2493 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_relationship2559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_relationship2592 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_relationship2632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_relationship2665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_relationship2705 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_relationship2730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_relationship2770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_relationship2795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_relationship2835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_relationship2863 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_relationship2886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_relationship2909 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_relationship2939 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_96_in_relationship2979 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_relationship3008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_relationship3048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_relationship3087 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_82_in_relationship3117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_relationship3141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_78_in_relationship3171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_relationship3191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_relationship3222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_relationship3255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_relationship3286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_57_in_relationship3326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_relationship3358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_56_in_relationship3387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_relationship3420 = new BitSet(new long[]{0x0000000000000002L});

}