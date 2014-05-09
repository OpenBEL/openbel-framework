grammar 
    BELStatement;

options {
    language = Java;
    output = AST;
}

@lexer::header {
    package org.openbel.framework.common.bel.parser;

    import org.openbel.bel.model.BELParseErrorException;
}

@header {
    package org.openbel.framework.common.bel.parser;
  
    import org.openbel.bel.model.BELParseErrorException;
    import org.openbel.framework.common.model.BELObject;
    import org.openbel.framework.common.model.Namespace;
    import org.openbel.framework.common.model.Parameter;
    import org.openbel.framework.common.model.Statement;
    import org.openbel.framework.common.model.Term;
    import org.openbel.framework.common.enums.FunctionEnum;
    import org.openbel.framework.common.enums.RelationshipType;
}

@lexer::members {
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
}

@members {
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
}

statement returns [Statement r]:
    st=outer_term {
        final Statement s = new Statement($st.r);
        $r = s;
    }
    (
        rel=relationship {
            s.setRelationshipType($rel.r);
        }
        (
            (
                OPEN_PAREN 
                nst=outer_term {
                    final Statement ns = new Statement($nst.r);
                }
                nrel=relationship {
                    ns.setRelationshipType($nrel.r);
                } 
                not=outer_term {
                    ns.setObject(new Statement.Object($not.r));
                    s.setObject(new Statement.Object(ns));
                    $r = s;
                }
                CLOSE_PAREN
            )
            | 
                ot=outer_term {
                    s.setObject(new Statement.Object($ot.r));
                    $r = s;
                }
        )
    )? 
    ;

outer_term returns [Term r]:
    f=function {
        final Term outerTerm = new Term($f.r);
    }
    OPEN_PAREN
    (','? arg=argument { outerTerm.addFunctionArgument($arg.r); })*
    CLOSE_PAREN {
        $r = outerTerm;
    }
    ;
    
argument returns [BELObject r]:
    fp=param { $r = $fp.r; } |
    ff=term { $r = $ff.r; }
    ;

term returns [Term r]:
    pfv=function {
        final Term parentTerm = new Term($pfv.r);
    }
    OPEN_PAREN
    (
        ','?
        (it=term {
            parentTerm.addFunctionArgument($it.r);
        } |
        pp=param { parentTerm.addFunctionArgument($pp.r); })
    )*
    CLOSE_PAREN {
        $r = parentTerm;
    }
    ;

param returns [Parameter r]:
    nsp=NS_PREFIX?
    (
        NS_VALUE {
            Namespace ns = null;
        
            if($nsp != null) {
                String prefix = $nsp.getText();
                prefix = prefix.substring(0, prefix.length() - 1);
                ns = new Namespace(prefix, "FIX_ME");
            }
        
            $r = new Parameter();
            $r.setValue($NS_VALUE.getText());
            $r.setNamespace(ns);
        } |
        QUOTED_VALUE {
            Namespace ns = null;
            
            if($nsp != null) {
                String prefix = $nsp.getText();
                prefix = prefix.substring(0, prefix.length() - 1);
                ns = new Namespace(prefix, "FIX_ME");
            }
            
            $r = new Parameter();
            $r.setNamespace(ns);

            // Strip quotes
            String quoted = $QUOTED_VALUE.getText();
            $r.setValue(quoted.substring(1, quoted.length() - 1));
        }
    )
    ;

function returns [FunctionEnum r]:
    (
        fv='proteinAbundance'           {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='p'                          {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='rnaAbundance'               {$r = FunctionEnum.getFunctionEnum($fv.getText());} | 
        fv='r'                          {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='abundance'                  {$r = FunctionEnum.getFunctionEnum($fv.getText());} | 
        fv='a'                          {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='microRNAAbundance'          {$r = FunctionEnum.getFunctionEnum($fv.getText());} | 
        fv='m'                          {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='geneAbundance'              {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='g'                          {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='biologicalProcess'          {$r = FunctionEnum.getFunctionEnum($fv.getText());} | 
        fv='bp'                         {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='pathology'                  {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='path'                       {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='complexAbundance'           {$r = FunctionEnum.getFunctionEnum($fv.getText());} | 
        fv='complex'                    {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='translocation'              {$r = FunctionEnum.getFunctionEnum($fv.getText());} | 
        fv='tloc'                       {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='cellSecretion'              {$r = FunctionEnum.getFunctionEnum($fv.getText());} | 
        fv='sec'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='cellSurfaceExpression'      {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='surf'                       {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='reaction'                   {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='rxn'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='compositeAbundance'         {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='composite'                  {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='fusion'                     {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='fus'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='degradation'                {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='deg'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='molecularActivity'          {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='act'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='catalyticActivity'          {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='cat'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='kinaseActivity'             {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='kin'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='phosphataseActivity'        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='phos'                       {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='peptidaseActivity'          {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='pep'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='ribosylationActivity'       {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='ribo'                       {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='transcriptionalActivity'    {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='tscript'                    {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='transportActivity'          {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='tport'                      {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='gtpBoundActivity'           {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='gtp'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='chaperoneActivity'          {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='chap'                       {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='proteinModification'        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='pmod'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='substitution'               {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='sub'                        {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='truncation'                 {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='trunc'                      {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='reactants'                  {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='products'                   {$r = FunctionEnum.getFunctionEnum($fv.getText());} |
        fv='list'                       {$r = FunctionEnum.getFunctionEnum($fv.getText());}
    )
    ;
    
relationship returns [RelationshipType r]:
    (
        rv='increases'                  { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='->'                         { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='decreases'                  { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='-|'                         { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='directlyIncreases'          { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='=>'                         { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='directlyDecreases'          { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='=|'                         { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='causesNoChange'             { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='positiveCorrelation'        { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='negativeCorrelation'        { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='translatedTo'               { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='>>'                         { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='transcribedTo'              { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv=':>'                         { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='isA'                        { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='subProcessOf'               { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='rateLimitingStepOf'         { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='biomarkerFor'               { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='prognosticBiomarkerFor'     { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='orthologous'                { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='analogous'                  { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='association'                { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='--'                         { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='hasMembers'                 { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='hasComponents'              { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='hasMember'                  { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } } |
        rv='hasComponent'               { $r = RelationshipType.fromString($rv.getText()); if($r == null) { $r = RelationshipType.fromAbbreviation($rv.getText()); } }
    )
    ;
    
fragment LETTER:
    ('a'..'z' | 'A'..'Z')
    ;

fragment DIGIT:
    '0'..'9'
    ;
    
OPEN_PAREN:
    '('
    ;
    
CLOSE_PAREN:
    ')'
    ;

NS_PREFIX:
    LETTER (LETTER | DIGIT)* ':'
    ;

NS_VALUE:
    ('_' | LETTER | DIGIT)+
    ;

QUOTED_VALUE:
    '"' ( EscapeSequence | ~('\\'|'"') )* '"'
    ;

fragment EscapeSequence:
    '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    | UnicodeEscape
    | OctalEscape
    ;

fragment OctalEscape:
    '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    | '\\' ('0'..'7') ('0'..'7')
    | '\\' ('0'..'7')
    ;

fragment UnicodeEscape:
    '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment HexDigit:
    ('0'..'9'|'a'..'f'|'A'..'F')
    ;

WS : (' ' | '\t' | '\n' | '\r' | '\f')+ {$channel = HIDDEN;};
