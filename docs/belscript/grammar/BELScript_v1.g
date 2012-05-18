grammar BELScript_v1;

options {
    // Target language for ANTLR to generate code in (defaults to Java).
    language = Java;

    // Controls the data structure the recognizer will generated.
    // (AST for abstract syntax trees or template for StringTemplate templates)
    output = AST;
}

// Introduce imaginary tokens. These tokens are not associated w/ any
// of our input but make nice root nodes for our abstract syntax tree.
tokens {

    // DOCSET a quoted value.
    DOCSET_QV;
    // DOCSET a list of values.
    DOCSET_LIST;
    // DOCSET an identifier.
    DOCSET_ID;

    // SG_SET a quoted value.
    SG_SET_QV;
    // SG_SET an identifier.
    SG_SET_ID;

    // ANNO_SET a quoted value.
    ANNO_SET_QV;
    // ANNO_SET a list of values.
    ANNO_SET_LIST;
    // ANNO_SET an identifier.
    ANNO_SET_ID;

    // ANNO_DEF a list.
    ANNO_DEF_LIST;
    // ANNO_DEF a URL.
    ANNO_DEF_URL;
    // ANNO_DEF a pattern.
    ANNO_DEF_PTRN;

    // PARAM_DEF a quoted value.
    PARAM_DEF_QV;
    // PARAM_DEF an identifier.
    PARAM_DEF_ID;

    // UNSET a statement group.
    UNSET_SG;

    // UNSET an identifier.
    UNSET_ID;
    // UNSET a list of identifiers.
    UNSET_ID_LIST;

    // Defines a document.
    DOCDEF;
    // Defines a namespace.
    NSDEF;
    // Defines the default namespace.
    DFLT_NSDEF;
    // Defines a term.
    TERMDEF;
    // Defines a statement.
    STMTDEF;
}

document
    :   (NEWLINE | DOCUMENT_COMMENT | record)+ EOF ->
        ^(DOCDEF record+)
    ;

record
    :   define_namespace
    |   define_annotation
    |   set_annotation
    |   set_document
    |   set_statement_group
    |   unset_statement_group
    |   unset
    |   statement
    ;

set_doc_expr
    :   KWRD_SET WS* KWRD_DOCUMENT WS*
    ;

set_document
    :   set_doc_expr document_property eq_clause val=QUOTED_VALUE ->
        ^(DOCSET_QV document_property $val)
    |   set_doc_expr document_property eq_clause val=VALUE_LIST ->
        ^(DOCSET_LIST document_property $val)
    |   set_doc_expr document_property eq_clause val=OBJECT_IDENT ->
        ^(DOCSET_ID document_property $val)
    ;

set_sg_expr
    :   KWRD_SET WS* KWRD_STMT_GROUP
    ;

set_statement_group
    :   set_sg_expr eq_clause val=QUOTED_VALUE -> ^(SG_SET_QV $val)
    |   set_sg_expr eq_clause val=OBJECT_IDENT -> ^(SG_SET_ID $val)
    ;

set_annotation
    :   KWRD_SET OBJECT_IDENT eq_clause val=QUOTED_VALUE ->
        ^(ANNO_SET_QV OBJECT_IDENT $val)
    |   KWRD_SET OBJECT_IDENT eq_clause val=VALUE_LIST ->
        ^(ANNO_SET_LIST OBJECT_IDENT $val)
    |   KWRD_SET OBJECT_IDENT eq_clause val=OBJECT_IDENT ->
        ^(ANNO_SET_ID OBJECT_IDENT $val)
    ;

unset_statement_group
    :   KWRD_UNSET KWRD_STMT_GROUP -> ^(UNSET_SG)
    ;

unset
    :   KWRD_UNSET val=OBJECT_IDENT -> ^(UNSET_ID $val)
    |   KWRD_UNSET val=IDENT_LIST -> ^(UNSET_ID_LIST $val)
    ;

define_namespace
    :   KWRD_DEFINE KWRD_DFLT KWRD_NS OBJECT_IDENT KWRD_AS KWRD_URL QUOTED_VALUE ->
        ^(DFLT_NSDEF OBJECT_IDENT QUOTED_VALUE)
    |   KWRD_DEFINE KWRD_NS OBJECT_IDENT KWRD_AS KWRD_URL QUOTED_VALUE ->
        ^(NSDEF OBJECT_IDENT QUOTED_VALUE)
    ;

define_anno_expr
    :   KWRD_DEFINE WS* KWRD_ANNO WS*
    ;

define_annotation
    :   define_anno_expr OBJECT_IDENT KWRD_AS KWRD_LIST val=VALUE_LIST ->
        ^(ANNO_DEF_LIST OBJECT_IDENT $val)
    |   define_anno_expr OBJECT_IDENT KWRD_AS KWRD_URL val=QUOTED_VALUE ->
        ^(ANNO_DEF_URL OBJECT_IDENT $val)
    |   define_anno_expr OBJECT_IDENT KWRD_AS KWRD_PATTERN val=QUOTED_VALUE ->
        ^(ANNO_DEF_PTRN OBJECT_IDENT $val)
    ;

document_property
    :   KWRD_AUTHORS
    |   KWRD_CONTACTINFO
    |   KWRD_COPYRIGHT
    |   KWRD_DESC
    |   KWRD_DISCLAIMER
    |   KWRD_LICENSES
    |   KWRD_NAME
    |   KWRD_VERSION
    ;

argument
    :   COMMA? term -> term
    |   COMMA? param -> param
    ;

term
    :   function LP (argument)* RP ->
        ^(TERMDEF function argument*)
    ;

// The rewrite rules for statement could be better captured using semantic
// predicates to pick the correct rewrite rule at runtime.
statement
    :   subject=term (rel=relationship (LP obj_sub=term obj_rel=relationship obj_obj=term RP | obj=term))? comment=STATEMENT_COMMENT? ->
        ^(STMTDEF $comment? $subject $rel? $obj? $obj_sub? $obj_rel? $obj_obj?)
    ;

ns_prefix
    :   OBJECT_IDENT COLON!
    ;

param
    :   ns_prefix? OBJECT_IDENT -> ^(PARAM_DEF_ID ns_prefix? OBJECT_IDENT)
    |   ns_prefix? QUOTED_VALUE -> ^(PARAM_DEF_QV ns_prefix? QUOTED_VALUE)
    ;

function returns [String r]
    :   fv='proteinAbundance'           {$r = "p";}
    |   fv='p'                          {$r = "p";}
    |   fv='rnaAbundance'               {$r = "r";}
    |   fv='r'                          {$r = "r";}
    |   fv='abundance'                  {$r = "a";}
    |   fv='a'                          {$r = "a";}
    |   fv='microRNAAbundance'          {$r = "m";}
    |   fv='m'                          {$r = "m";}
    |   fv='geneAbundance'              {$r = "g";}
    |   fv='g'                          {$r = "g";}
    |   fv='biologicalProcess'          {$r = "bp";}
    |   fv='bp'                         {$r = "bp";}
    |   fv='pathology'                  {$r = "path";}
    |   fv='path'                       {$r = "path";}
    |   fv='complexAbundance'           {$r = "complex";}
    |   fv='complex'                    {$r = "complex";}
    |   fv='translocation'              {$r = "tloc";}
    |   fv='tloc'                       {$r = "tloc";}
    |   fv='cellSecretion'              {$r = "sec";}
    |   fv='sec'                        {$r = "sec";}
    |   fv='cellSurfaceExpression'      {$r = "surf";}
    |   fv='surf'                       {$r = "surf";}
    |   fv='reaction'                   {$r = "rxn";}
    |   fv='rxn'                        {$r = "rxn";}
    |   fv='compositeAbundance'         {$r = "composite";}
    |   fv='composite'                  {$r = "composite";}
    |   fv='fusion'                     {$r = "fus";}
    |   fv='fus'                        {$r = "fus";}
    |   fv='degradation'                {$r = "deg";}
    |   fv='deg'                        {$r = "deg";}
    |   fv='molecularActivity'          {$r = "act";}
    |   fv='act'                        {$r = "act";}
    |   fv='catalyticActivity'          {$r = "cat";}
    |   fv='cat'                        {$r = "cat";}
    |   fv='kinaseActivity'             {$r = "kin";}
    |   fv='kin'                        {$r = "kin";}
    |   fv='phosphataseActivity'        {$r = "phos";}
    |   fv='phos'                       {$r = "phos";}
    |   fv='peptidaseActivity'          {$r = "pep";}
    |   fv='pep'                        {$r = "pep";}
    |   fv='ribosylationActivity'       {$r = "ribo";}
    |   fv='ribo'                       {$r = "ribo";}
    |   fv='transcriptionalActivity'    {$r = "tscript";}
    |   fv='tscript'                    {$r = "tscript";}
    |   fv='transportActivity'          {$r = "tport";}
    |   fv='tport'                      {$r = "tport";}
    |   fv='gtpBoundActivity'           {$r = "gtp";}
    |   fv='gtp'                        {$r = "gtp";}
    |   fv='chaperoneActivity'          {$r = "chap";}
    |   fv='chap'                       {$r = "chap";}
    |   fv='proteinModification'        {$r = "pmod";}
    |   fv='pmod'                       {$r = "pmod";}
    |   fv='substitution'               {$r = "sub";}
    |   fv='sub'                        {$r = "sub";}
    |   fv='truncation'                 {$r = "trunc";}
    |   fv='trunc'                      {$r = "trunc";}
    |   fv='reactants'                  {$r = "reactants";}
    |   fv='products'                   {$r = "products";}
    |   fv='list'                       {$r = "list";}
    ;

relationship returns [String r]
    :   rv='increases'                  { $r = "increases";}
    |   rv='->'                         { $r = "increases";}
    |   rv='decreases'                  { $r = "decreases";}
    |   rv='-|'                         { $r = "decreases";}
    |   rv='directlyIncreases'          { $r = "directlyIncreases";}
    |   rv='=>'                         { $r = "directlyIncreases";}
    |   rv='directlyDecreases'          { $r = "directlyDecreases";}
    |   rv='=|'                         { $r = "directlyDecreases";}
    |   rv='causesNoChange'             { $r = "causesNoChange";}
    |   rv='positiveCorrelation'        { $r = "positiveCorrelation";}
    |   rv='negativeCorrelation'        { $r = "negativeCorrelation";}
    |   rv='translatedTo'               { $r = "translatedTo";}
    |   rv='>>'                         { $r = "translatedTo";}
    |   rv='transcribedTo'              { $r = "transcribedTo";}
    |   rv=':>'                         { $r = "transcribedTo";}
    |   rv='isA'                        { $r = "isA";}
    |   rv='subProcessOf'               { $r = "subProcessOf";}
    |   rv='rateLimitingStepOf'         { $r = "rateLimitingStepOf";}
    |   rv='biomarkerFor'               { $r = "biomarkerFor";}
    |   rv='prognosticBiomarkerFor'     { $r = "prognosticBiomarkerFor";}
    |   rv='orthologous'                { $r = "orthologous";}
    |   rv='analogous'                  { $r = "analogous";}
    |   rv='association'                { $r = "association";}
    |   rv='--'                         { $r = "association";}
    |   rv='hasMembers'                 { $r = "hasMembers";}
    |   rv='hasComponents'              { $r = "hasComponents";}
    |   rv='hasMember'                  { $r = "hasMember";}
    |   rv='hasComponent'               { $r = "hasComponent";}
    ;

eq_clause
    :   WS* EQ WS*
    ;

DOCUMENT_COMMENT
    :   '#' ~('\n' | '\r')*
    ;

STATEMENT_COMMENT
    :   '//' (('\\\n') | ('\\\r\n') | ~('\n' | '\r'))*
    ;

IDENT_LIST
    :   '{' OBJECT_IDENT (COMMA OBJECT_IDENT)* '}'
    ;

VALUE_LIST
    :   '{' (OBJECT_IDENT | QUOTED_VALUE | VALUE_LIST)? (COMMA (OBJECT_IDENT | QUOTED_VALUE | VALUE_LIST)?)* '}'
    ;

QUOTED_VALUE
    :   '"' ( ESCAPE_SEQUENCE | '\\\n' | '\\\r\n' | ~('\\'|'"') )* '"'
    ;

LP: '(';
RP: ')';
EQ: '=';
COLON: ':';
COMMA: ',';

NEWLINE
    :   '\u000d'? '\u000a' | '\u000d'
    ;

WS: (' ' | '\t' | '\n' | '\r'| '\f' | '\\\n' | '\\\r\n')+ { skip(); };

// Start of BELScript keywords - case insensitive tokens.

KWRD_ANNO
    :   ('A'|'a')('N'|'n')('N'|'n')('O'|'o')('T'|'t')('A'|'a')('T'|'t')('I'|'i')('O'|'o')('N'|'n')
    ;

KWRD_AS
    :   ('A'|'a')('S'|'s')
    ;

KWRD_AUTHORS
    :   ('A'|'a')('U'|'u')('T'|'t')('H'|'h')('O'|'o')('R'|'r')('S'|'s')
    ;

KWRD_CONTACTINFO
    :   ('C'|'c')('O'|'o')('N'|'n')('T'|'t')('A'|'a')('C'|'c')('T'|'t')('I'|'i')('N'|'n')('F'|'f')('O'|'o')
    ;

KWRD_COPYRIGHT
    :   ('C'|'c')('O'|'o')('P'|'p')('Y'|'y')('R'|'r')('I'|'i')('G'|'g')('H'|'h')('T'|'t')
    ;

KWRD_DFLT
    :   ('D'|'d')('E'|'e')('F'|'f')('A'|'a')('U'|'u')('L'|'l')('T'|'t')
    ;

KWRD_DEFINE
    :   ('D'|'d')('E'|'e')('F'|'f')('I'|'i')('N'|'n')('E'|'e')
    ;

KWRD_DESC
    :   ('D'|'d')('E'|'e')('S'|'s')('C'|'c')('R'|'r')('I'|'i')('P'|'p')('T'|'t')('I'|'i')('O'|'o')('N'|'n')
    ;

KWRD_DISCLAIMER
    :   ('D'|'d')('I'|'i')('S'|'s')('C'|'c')('L'|'l')('A'|'a')('I'|'i')('M'|'m')('E'|'e')('R'|'r')
    ;

KWRD_DOCUMENT
    :   ('D'|'d')('O'|'o')('C'|'c')('U'|'u')('M'|'m')('E'|'e')('N'|'n')('T'|'t')
    ;

KWRD_LICENSES
    :   ('L'|'l')('I'|'i')('C'|'c')('E'|'e')('N'|'n')('S'|'s')('E'|'e')('S'|'s')
    ;

KWRD_LIST
    :   ('L'|'l')('I'|'i')('S'|'s')('T'|'t')
    ;

KWRD_NAME
    :   ('N'|'n')('A'|'a')('M'|'m')('E'|'e')
    ;

KWRD_NS
    :   ('N'|'n')('A'|'a')('M'|'m')('E'|'e')('S'|'s')('P'|'p')('A'|'a')('C'|'c')('E'|'e')
    ;

KWRD_PATTERN
    :   ('P'|'p')('A'|'a')('T'|'t')('T'|'t')('E'|'e')('R'|'r')('N'|'n')
    ;

KWRD_SET
    :   ('S'|'s')('E'|'e')('T'|'t')
    ;

KWRD_STMT_GROUP
    :   ('S'|'s')('T'|'t')('A'|'a')('T'|'t')('E'|'e')('M'|'m')('E'|'e')('N'|'n')('T'|'t')('_')('G'|'g')('R'|'r')('O'|'o')('U'|'u')('P'|'p')
    ;

KWRD_UNSET
    :   ('U'|'u')('N'|'n')('S'|'s')('E'|'e')('T'|'t')
    ;

KWRD_URL
    :   ('U'|'u')('R'|'r')('L'|'l')
    ;

KWRD_VERSION
    :   ('V'|'v')('E'|'e')('R'|'r')('S'|'s')('I'|'i')('O'|'o')('N'|'n')
    ;

OBJECT_IDENT
    :   ('_' | LETTER | DIGIT)+
    ;

// A select few fragment rules. If used in excess, fragment rules may impact
// performance. Fragment lexer rules tell ANTLR that a rule will only be called
// by other rules, and to not yield a token to the parser.

fragment LETTER
    :   ('a'..'z' | 'A'..'Z')
    ;

fragment DIGIT
    :   '0'..'9'
    ;

fragment ESCAPE_SEQUENCE
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESCAPE
    |   OCTAL_ESCAPE
    ;

fragment OCTAL_ESCAPE
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment UNICODE_ESCAPE
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

fragment HEX_DIGIT
    :   ('0'..'9'|'a'..'f'|'A'..'F')
    ;

