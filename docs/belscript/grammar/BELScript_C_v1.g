grammar BELScript_C_v1;

options {
    // Target language for ANTLR to generate code in (defaults to Java).
    language = C;

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

function returns [char *f]
    :   fv='proteinAbundance'           { retval.f = "proteinAbundance"; }
    |   fv='p'                          { retval.f = "p"; }
    |   fv='rnaAbundance'               { retval.f = "r"; }
    |   fv='r'                          { retval.f = "r"; }
    |   fv='abundance'                  { retval.f = "a"; }
    |   fv='a'                          { retval.f = "a"; }
    |   fv='microRNAAbundance'          { retval.f = "m"; }
    |   fv='m'                          { retval.f = "m"; }
    |   fv='geneAbundance'              { retval.f = "g"; }
    |   fv='g'                          { retval.f = "g"; }
    |   fv='biologicalProcess'          { retval.f = "bp"; }
    |   fv='bp'                         { retval.f = "bp"; }
    |   fv='pathology'                  { retval.f = "path"; }
    |   fv='path'                       { retval.f = "path"; }
    |   fv='complexAbundance'           { retval.f = "complex"; }
    |   fv='complex'                    { retval.f = "complex"; }
    |   fv='translocation'              { retval.f = "tloc"; }
    |   fv='tloc'                       { retval.f = "tloc"; }
    |   fv='cellSecretion'              { retval.f = "sec"; }
    |   fv='sec'                        { retval.f = "sec"; }
    |   fv='cellSurfaceExpression'      { retval.f = "surf"; }
    |   fv='surf'                       { retval.f = "surf"; }
    |   fv='reaction'                   { retval.f = "rxn"; }
    |   fv='rxn'                        { retval.f = "rxn"; }
    |   fv='compositeAbundance'         { retval.f = "composite"; }
    |   fv='composite'                  { retval.f = "composite"; }
    |   fv='fusion'                     { retval.f = "fus"; }
    |   fv='fus'                        { retval.f = "fus"; }
    |   fv='degradation'                { retval.f = "deg"; }
    |   fv='deg'                        { retval.f = "deg"; }
    |   fv='molecularActivity'          { retval.f = "act"; }
    |   fv='act'                        { retval.f = "act"; }
    |   fv='catalyticActivity'          { retval.f = "cat"; }
    |   fv='cat'                        { retval.f = "cat"; }
    |   fv='kinaseActivity'             { retval.f = "kin"; }
    |   fv='kin'                        { retval.f = "kin"; }
    |   fv='phosphataseActivity'        { retval.f = "phos"; }
    |   fv='phos'                       { retval.f = "phos"; }
    |   fv='peptidaseActivity'          { retval.f = "pep"; }
    |   fv='pep'                        { retval.f = "pep"; }
    |   fv='ribosylationActivity'       { retval.f = "ribo"; }
    |   fv='ribo'                       { retval.f = "ribo"; }
    |   fv='transcriptionalActivity'    { retval.f = "tscript"; }
    |   fv='tscript'                    { retval.f = "tscript"; }
    |   fv='transportActivity'          { retval.f = "tport"; }
    |   fv='tport'                      { retval.f = "tport"; }
    |   fv='gtpBoundActivity'           { retval.f = "gtp"; }
    |   fv='gtp'                        { retval.f = "gtp"; }
    |   fv='chaperoneActivity'          { retval.f = "chap"; }
    |   fv='chap'                       { retval.f = "chap"; }
    |   fv='proteinModification'        { retval.f = "pmod"; }
    |   fv='pmod'                       { retval.f = "pmod"; }
    |   fv='substitution'               { retval.f = "sub"; }
    |   fv='sub'                        { retval.f = "sub"; }
    |   fv='truncation'                 { retval.f = "trunc"; }
    |   fv='trunc'                      { retval.f = "trunc"; }
    |   fv='reactants'                  { retval.f = "reactants"; }
    |   fv='products'                   { retval.f = "products"; }
    |   fv='list'                       { retval.f = "list"; }
    ;

relationship returns [char *r]
    :   rv='increases'                  { retval.r = "increases";}
    |   rv='->'                         { retval.r = "increases";}
    |   rv='decreases'                  { retval.r = "decreases";}
    |   rv='-|'                         { retval.r = "decreases";}
    |   rv='directlyIncreases'          { retval.r = "directlyIncreases";}
    |   rv='=>'                         { retval.r = "directlyIncreases";}
    |   rv='directlyDecreases'          { retval.r = "directlyDecreases";}
    |   rv='=|'                         { retval.r = "directlyDecreases";}
    |   rv='causesNoChange'             { retval.r = "causesNoChange";}
    |   rv='positiveCorrelation'        { retval.r = "positiveCorrelation";}
    |   rv='negativeCorrelation'        { retval.r = "negativeCorrelation";}
    |   rv='translatedTo'               { retval.r = "translatedTo";}
    |   rv='>>'                         { retval.r = "translatedTo";}
    |   rv='transcribedTo'              { retval.r = "transcribedTo";}
    |   rv=':>'                         { retval.r = "transcribedTo";}
    |   rv='isA'                        { retval.r = "isA";}
    |   rv='subProcessOf'               { retval.r = "subProcessOf";}
    |   rv='rateLimitingStepOf'         { retval.r = "rateLimitingStepOf";}
    |   rv='biomarkerFor'               { retval.r = "biomarkerFor";}
    |   rv='prognosticBiomarkerFor'     { retval.r = "prognosticBiomarkerFor";}
    |   rv='orthologous'                { retval.r = "orthologous";}
    |   rv='analogous'                  { retval.r = "analogous";}
    |   rv='association'                { retval.r = "association";}
    |   rv='--'                         { retval.r = "association";}
    |   rv='hasMembers'                 { retval.r = "hasMembers";}
    |   rv='hasComponents'              { retval.r = "hasComponents";}
    |   rv='hasMember'                  { retval.r = "hasMember";}
    |   rv='hasComponent'               { retval.r = "hasComponent";}
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
    :   '{' (OBJECT_IDENT | QUOTED_VALUE | VALUE_LIST)? (COMMA (' ')* (OBJECT_IDENT | QUOTED_VALUE | VALUE_LIST)?)* '}'
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

