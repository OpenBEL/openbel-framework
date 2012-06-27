grammar BELScript;

options {
    language = Java;
    output = AST;
}

@header {
    package org.openbel.framework.common.bel.parser;
    
    import java.util.List;
    import java.util.ArrayList;
    import java.util.Stack;
    
    import org.openbel.bel.model.BELParseErrorException;
}

@lexer::header {
    package org.openbel.framework.common.bel.parser;
}

@members {
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
}

document:
    (NEWLINE | DOCUMENT_COMMENT | record)+ EOF
    ;

record:
    (define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement)
    ;

set_document
    @init { paraphrases.push("in set document."); }
    @after { paraphrases.pop(); }
    :
    ('SET' DOCUMENT_KEYWORD) document_property '=' (OBJECT_IDENT | vl=VALUE_LIST | quoted_value)
    {
        // https://github.com/OpenBEL/openbel-framework/issues/14
        if ($vl != null) $vl.setText($vl.getText().replace("\\\\", "\\"));
    }
    ;

set_statement_group
    @init { paraphrases.push("in set statement group."); }
    @after { paraphrases.pop(); }
    :
    'SET' STATEMENT_GROUP_KEYWORD '=' (quoted_value | OBJECT_IDENT)
    ;

set_annotation
    @init { paraphrases.push("in set annotation."); }
    @after { paraphrases.pop(); }
    :
    'SET' OBJECT_IDENT '=' (quoted_value | vl=VALUE_LIST | OBJECT_IDENT)
    {
        // https://github.com/OpenBEL/openbel-framework/issues/14
        if ($vl != null) $vl.setText($vl.getText().replace("\\\\", "\\"));
    }
    ;

unset_statement_group
    @init { paraphrases.push("in unset statement group."); }
    @after { paraphrases.pop(); }
    :
    'UNSET' STATEMENT_GROUP_KEYWORD
    ;

unset
    @init { paraphrases.push("in unset."); }
    @after { paraphrases.pop(); }
    :
    'UNSET' (OBJECT_IDENT | IDENT_LIST)
    ;

define_namespace
    @init { paraphrases.push("in define namespace."); }
    @after { paraphrases.pop(); }
    :
    ('DEFINE' (('DEFAULT')? 'NAMESPACE')) OBJECT_IDENT 'AS' 'URL' quoted_value
    ;

define_annotation
    @init { paraphrases.push("in define annotation."); }
    @after { paraphrases.pop(); }
    :
    ('DEFINE' 'ANNOTATION') OBJECT_IDENT 'AS' ((('URL' | 'PATTERN') quoted_value) | ('LIST' vl=VALUE_LIST))
    {
        // https://github.com/OpenBEL/openbel-framework/issues/14
        if ($vl != null) $vl.setText($vl.getText().replace("\\\\", "\\"));
    }
    ;

quoted_value
    : qv=QUOTED_VALUE
    {
        // https://github.com/OpenBEL/openbel-framework/issues/14
        $qv.setText($qv.getText().replace("\\\\", "\\"));
    }
    ;

document_property:
    'Authors'     |
    'ContactInfo' |
    'Copyright'   |
    'Description' |
    'Disclaimer'  |
    'Licenses'    |
    'Name'        |
    'Version'
    ;

statement
    @init { paraphrases.push("in statement."); }
    @after { paraphrases.pop(); }
    :
    outer_term (relationship ((OPEN_PAREN outer_term relationship outer_term CLOSE_PAREN) | outer_term))? STATEMENT_COMMENT?
    ;
    
outer_term:
    function OPEN_PAREN (','? argument)* CLOSE_PAREN
    ;
    
argument:
    param | term
    ;

term:
    function OPEN_PAREN (','? (term | param))* CLOSE_PAREN
    ;

/* XXX OBJECT_IDENT is used for namespace value because otherwise parsing will fail using a token like (LETTER | DIGIT)+ */
fragment param:
    NS_PREFIX? (OBJECT_IDENT | quoted_value)
    ;

function returns [String r]:
    (
        fv='proteinAbundance'           {$r = $fv.getText();} |
        fv='p'                          {$r = $fv.getText();} |
        fv='rnaAbundance'               {$r = $fv.getText();} | 
        fv='r'                          {$r = $fv.getText();} |
        fv='abundance'                  {$r = $fv.getText();} | 
        fv='a'                          {$r = $fv.getText();} |
        fv='microRNAAbundance'          {$r = $fv.getText();} | 
        fv='m'                          {$r = $fv.getText();} |
        fv='geneAbundance'              {$r = $fv.getText();} |
        fv='g'                          {$r = $fv.getText();} |
        fv='biologicalProcess'          {$r = $fv.getText();} | 
        fv='bp'                         {$r = $fv.getText();} |
        fv='pathology'                  {$r = $fv.getText();} |
        fv='path'                       {$r = $fv.getText();} |
        fv='complexAbundance'           {$r = $fv.getText();} | 
        fv='complex'                    {$r = $fv.getText();} |
        fv='translocation'              {$r = $fv.getText();} | 
        fv='tloc'                       {$r = $fv.getText();} |
        fv='cellSecretion'              {$r = $fv.getText();} | 
        fv='sec'                        {$r = $fv.getText();} |
        fv='cellSurfaceExpression'      {$r = $fv.getText();} |
        fv='surf'                       {$r = $fv.getText();} |
        fv='reaction'                   {$r = $fv.getText();} |
        fv='rxn'                        {$r = $fv.getText();} |
        fv='compositeAbundance'         {$r = $fv.getText();} |
        fv='composite'                  {$r = $fv.getText();} |
        fv='fusion'                     {$r = $fv.getText();} |
        fv='fus'                        {$r = $fv.getText();} |
        fv='degradation'                {$r = $fv.getText();} |
        fv='deg'                        {$r = $fv.getText();} |
        fv='molecularActivity'          {$r = $fv.getText();} |
        fv='act'                        {$r = $fv.getText();} |
        fv='catalyticActivity'          {$r = $fv.getText();} |
        fv='cat'                        {$r = $fv.getText();} |
        fv='kinaseActivity'             {$r = $fv.getText();} |
        fv='kin'                        {$r = $fv.getText();} |
        fv='phosphataseActivity'        {$r = $fv.getText();} |
        fv='phos'                       {$r = $fv.getText();} |
        fv='peptidaseActivity'          {$r = $fv.getText();} |
        fv='pep'                        {$r = $fv.getText();} |
        fv='ribosylationActivity'       {$r = $fv.getText();} |
        fv='ribo'                       {$r = $fv.getText();} |
        fv='transcriptionalActivity'    {$r = $fv.getText();} |
        fv='tscript'                    {$r = $fv.getText();} |
        fv='transportActivity'          {$r = $fv.getText();} |
        fv='tport'                      {$r = $fv.getText();} |
        fv='gtpBoundActivity'           {$r = $fv.getText();} |
        fv='gtp'                        {$r = $fv.getText();} |
        fv='chaperoneActivity'          {$r = $fv.getText();} |
        fv='chap'                       {$r = $fv.getText();} |
        fv='proteinModification'        {$r = $fv.getText();} |
        fv='pmod'                       {$r = $fv.getText();} |
        fv='substitution'               {$r = $fv.getText();} |
        fv='sub'                        {$r = $fv.getText();} |
        fv='truncation'                 {$r = $fv.getText();} |
        fv='trunc'                      {$r = $fv.getText();} |
        fv='reactants'                  {$r = $fv.getText();} |
        fv='products'                   {$r = $fv.getText();} |
        fv='list'                       {$r = $fv.getText();}
    )
    ;
    
relationship returns [String r]:
    (
        rv='increases'                  { $r = $rv.getText(); } | 
        rv='->'                         { $r = $rv.getText(); } |
        rv='decreases'                  { $r = $rv.getText(); } | 
        rv='-|'                         { $r = $rv.getText(); } |
        rv='directlyIncreases'          { $r = $rv.getText(); } |
        rv='=>'                         { $r = $rv.getText(); } |
        rv='directlyDecreases'          { $r = $rv.getText(); } |
        rv='=|'                         { $r = $rv.getText(); } |
        rv='causesNoChange'             { $r = $rv.getText(); } |
        rv='positiveCorrelation'        { $r = $rv.getText(); } |
        rv='negativeCorrelation'        { $r = $rv.getText(); } |
        rv='translatedTo'               { $r = $rv.getText(); } |
        rv='>>'                         { $r = $rv.getText(); } |
        rv='transcribedTo'              { $r = $rv.getText(); } |
        rv=':>'                         { $r = $rv.getText(); } |
        rv='isA'                        { $r = $rv.getText(); } |
        rv='subProcessOf'               { $r = $rv.getText(); } |
        rv='rateLimitingStepOf'         { $r = $rv.getText(); } |
        rv='biomarkerFor'               { $r = $rv.getText(); } |
        rv='prognosticBiomarkerFor'     { $r = $rv.getText(); } |
        rv='orthologous'                { $r = $rv.getText(); } |
        rv='analogous'                  { $r = $rv.getText(); } |
        rv='association'                { $r = $rv.getText(); } |
        rv='--'                         { $r = $rv.getText(); } |
        rv='hasMembers'                 { $r = $rv.getText(); } |
        rv='hasComponents'              { $r = $rv.getText(); } |
        rv='hasMember'                  { $r = $rv.getText(); } |
        rv='hasComponent'               { $r = $rv.getText(); }
    )
    ;

DOCUMENT_COMMENT:
    '#' ~('\n' | '\r')* {$channel=HIDDEN;}
    ;

STATEMENT_COMMENT:
    '//' (('\\\n') | ('\\\r\n') | ~('\n' | '\r'))*
    ;

DOCUMENT_KEYWORD:
    'DOCUMENT'
    ;
    
STATEMENT_GROUP_KEYWORD:
    'STATEMENT_GROUP'
    ;

IDENT_LIST:
    '{' OBJECT_IDENT (COMMA OBJECT_IDENT)* '}'
    ;

VALUE_LIST:
    '{' (OBJECT_IDENT | QUOTED_VALUE | VALUE_LIST)? (COMMA (OBJECT_IDENT | QUOTED_VALUE | VALUE_LIST)?)* '}'
    ;

OBJECT_IDENT:
    ('_' | LETTER | DIGIT)+
    ;

QUOTED_VALUE:
    '"' ( EscapeSequence | '\\\n' | '\\\r\n' | ~('\\'|'"') )* '"'
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
    
NEWLINE:
    '\u000d'? '\u000a' | '\u000d'
    ;

WS : (' ' | '\t' | '\n' | '\r'| '\f' | '\\\n' | '\\\r\n')+ {$channel = HIDDEN;};

fragment COMMA:
    ' '* ',' ' '*
    ;

fragment LETTER:
    ('a'..'z' | 'A'..'Z')
    ;

fragment DIGIT:
    '0'..'9'
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
