tree grammar BELScriptWalker_v1;

options {
    // Target language for ANTLR to generate code in (defaults to Java).
    language = Java;

    // Controls the data structure the recognizer will generated.
    // (AST for abstract syntax trees or template for StringTemplate templates)
    output = AST;

    // Import the vocabulary from our combined grammar.
    tokenVocab=BELScript_v1;

    // Tree types.
    ASTLabelType=CommonTree;
}

// Introduce imaginary tokens. These tokens are not associated w/ any
// of our input but make nice root nodes for our abstract syntax tree.
tokens {
       // Defines document authors.    
    AUTHDEF;
    // Defines document contact info.
    CONTACTDEF;
    // Defines document copyright.
    COPYDEF;
    // Defines document description.
    DESCDEF;
    // Defines document disclaimer.
    DISCDEF;
    // Defines document licenses.
    LICDEF;
    // Defines document name.
    NAMEDEF;
    // Defines document version.
    VERSIONDEF;
}

document
    :   ^(DOCDEF record+)
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
	:	^(DOCSET_QV document_property QUOTED_VALUE)
	|	^(DOCSET_LIST document_property VALUE_LIST)
	|	^(DOCSET_ID document_property OBJECT_IDENT)
    ;

set_statement_group
    :   ^(SG_SET_QV QUOTED_VALUE)
    |   ^(SG_SET_ID OBJECT_IDENT)
    ;

set_annotation
    :   ^(ANNO_SET_QV OBJECT_IDENT QUOTED_VALUE)
    |   ^(ANNO_SET_LIST OBJECT_IDENT VALUE_LIST)
    |   ^(ANNO_SET_ID OBJECT_IDENT OBJECT_IDENT)
    ;

unset_statement_group
    :   UNSET_SG
    ;

unset
    :   ^(UNSET_ID OBJECT_IDENT)
    |   ^(UNSET_ID_LIST IDENT_LIST)
    ;

define_namespace
    :   ^(DFLT_NSDEF OBJECT_IDENT QUOTED_VALUE)
    |   ^(NSDEF OBJECT_IDENT QUOTED_VALUE)
    ;

define_anno_expr
    :   KWRD_DEFINE WS* KWRD_ANNO WS*
    ;

define_annotation
    :   ^(ANNO_DEF_LIST OBJECT_IDENT VALUE_LIST)
    |   ^(ANNO_DEF_URL OBJECT_IDENT QUOTED_VALUE)
    ;

document_property
    :   KWRD_AUTHORS -> AUTHDEF
    |   KWRD_CONTACTINFO -> CONTACTDEF
    |   KWRD_COPYRIGHT -> COPYDEF
    |   KWRD_DESC -> DESCDEF
    |   KWRD_DISCLAIMER -> DISCDEF
    |   KWRD_LICENSES -> LICDEF
    |   KWRD_NAME -> NAMEDEF
    |   KWRD_VERSION -> VERSIONDEF
    ;

argument
    :   term
    |   param
    ;

term
    :   ^(TERMDEF function argument*)
    ;

statement
    :   ^(STMTDEF STATEMENT_COMMENT? term (relationship term (relationship term)?)?)
    ;

ns_prefix
    :   OBJECT_IDENT
    ;

param
    :   ^(PARAM_DEF_ID ns_prefix? OBJECT_IDENT)
    |   ^(PARAM_DEF_QV ns_prefix? QUOTED_VALUE)
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

