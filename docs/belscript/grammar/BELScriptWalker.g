tree grammar BELScriptWalker;

options {
  language = Java;
  tokenVocab=BELScript;
  ASTLabelType=CommonTree;
  output=AST;
}

@header {
    package org.openbel.framework.common.bel.parser;

    import java.text.ParseException;

    import java.text.SimpleDateFormat;
    import java.util.Set;
    import java.util.Map;
    import java.util.HashMap;
    import java.util.LinkedHashMap;
    import java.util.LinkedHashSet;
    import java.util.Arrays;
    import java.util.List;
    import java.util.ArrayList;
    import java.util.Date;

    import org.openbel.bel.model.BELDocument;
    import org.openbel.bel.model.BELDocumentHeader;
    import org.openbel.bel.model.BELAnnotation;
    import org.openbel.bel.model.BELCitation;
    import org.openbel.bel.model.BELEvidence;
    import org.openbel.bel.model.BELAnnotationDefinition;
    import org.openbel.bel.model.BELAnnotationType;
    import org.openbel.bel.model.BELNamespaceDefinition;
    import org.openbel.bel.model.BELDocumentProperty;
    import org.openbel.bel.model.BELStatement;
    import org.openbel.bel.model.BELStatementGroup;
    import org.openbel.bel.model.BELParseErrorException;
    import org.openbel.bel.model.BELParseWarningException;
    import org.openbel.bel.model.BELParseErrorException.DefineAnnotationBeforeUsageException;
    import org.openbel.bel.model.BELParseErrorException.SetDocumentPropertiesFirstException;
    import org.openbel.bel.model.BELParseErrorException.DocumentNameException;
    import org.openbel.bel.model.BELParseErrorException.DocumentDescriptionException;
    import org.openbel.bel.model.BELParseErrorException.DocumentVersionException;
    import org.openbel.bel.model.BELParseErrorException.UnsetDocumentPropertiesException;
    import org.openbel.bel.model.BELParseWarningException.UnsetUndefinedAnnotationException;
    import org.openbel.bel.model.BELParseErrorException.NamespaceUndefinedException;
    import org.openbel.bel.model.BELParseErrorException.InvalidCitationException;
    import org.openbel.bel.model.BELParseErrorException.InvalidEvidenceException;
    import org.openbel.framework.common.enums.FunctionEnum;
}

@members {
    private final Map<BELDocumentProperty, String> docprop = new HashMap<BELDocumentProperty, String>();
    private int lastDocumentPropertyLocation = 0;
    private final Set<BELAnnotationDefinition> adlist = new LinkedHashSet<BELAnnotationDefinition>();
    private final Map<String, BELAnnotationDefinition> definedAnnotations = new LinkedHashMap<String, BELAnnotationDefinition>();
    private final Map<String, BELNamespaceDefinition> definedNamespaces = new LinkedHashMap<String, BELNamespaceDefinition>();
    private final Set<BELNamespaceDefinition> nslist = new LinkedHashSet<BELNamespaceDefinition>();

    private BELStatementGroup activeStatementGroup;
    private BELStatementGroup documentStatementGroup = new BELStatementGroup();
    private List<BELStatementGroup> statementGroups = new ArrayList<BELStatementGroup>();

    private final Map<String, BELAnnotation> sgAnnotationContext = new LinkedHashMap<String, BELAnnotation>();
    private final Map<String, BELAnnotation> annotationContext = new LinkedHashMap<String, BELAnnotation>();
    private BELCitation citationContext;
    private BELEvidence evidenceContext;

    private final List<BELStatement> stmtlist = new ArrayList<BELStatement>();
    private final List<BELParseErrorException> syntaxErrors = new ArrayList<BELParseErrorException>();
    private final List<BELParseWarningException> syntaxWarnings = new ArrayList<BELParseWarningException>();

    private static final SimpleDateFormat iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
}

document returns [BELDocument doc]:
    (NEWLINE | DOCUMENT_COMMENT | record)+ EOF {
        if (!docprop.containsKey(BELDocumentProperty.NAME)) {
            addError(new DocumentNameException(lastDocumentPropertyLocation, 0));
        } else if (!docprop.containsKey(BELDocumentProperty.DESCRIPTION)) {
            addError(new DocumentDescriptionException(lastDocumentPropertyLocation, 0));
        } else if (!docprop.containsKey(BELDocumentProperty.VERSION)) {
            addError(new DocumentVersionException(lastDocumentPropertyLocation, 0));
        } else {
            if (documentStatementGroup.getStatements().isEmpty()) {
                // statements are only contained in explicitly-defined statement groups
                $doc = new BELDocument(BELDocumentHeader.create(docprop), adlist, nslist, statementGroups);
            } else {
                // statements are defined in the implicit document statement group and possibly child statement groups
                documentStatementGroup.setChildStatementGroups(statementGroups);
                $doc = new BELDocument(BELDocumentHeader.create(docprop), adlist, nslist, Arrays.asList(documentStatementGroup));
            }
        }
    }
    ;

record:
    (define_namespace | define_annotation | set_annotation | set_document | set_statement_group | unset_statement_group | unset | statement)
    ;

set_document:
    ('SET' dkt=DOCUMENT_KEYWORD) prop=document_property '=' (qv=QUOTED_VALUE | oi=OBJECT_IDENT) {
        if (!annotationContext.isEmpty() || !stmtlist.isEmpty()) {
            addError(new SetDocumentPropertiesFirstException(dkt.getLine(), dkt.getCharPositionInLine()));
        }

        final String keywordValue;
        if ($qv != null) {
            keywordValue = $qv.toString();
        } else if ($oi != null) {
            keywordValue = $oi.toString();
        } else {
            throw new IllegalStateException("Did not understand document keyword value, expecting quoted value or object identifier.");
        }

        docprop.put(prop.r, keywordValue);
        lastDocumentPropertyLocation = dkt.getLine();
    }
    ;

set_statement_group:
    'SET' STATEMENT_GROUP_KEYWORD '=' (qv=QUOTED_VALUE | oi=OBJECT_IDENT) {
        final String name;
        if (qv != null) {
            name = qv.toString();
        } else if (oi != null) {
            name = oi.toString();
        } else {
            throw new IllegalStateException("Did not understand statement group value, expecting quoted value or object identifier.");
        }

        activeStatementGroup = new BELStatementGroup(name);
        statementGroups.add(activeStatementGroup);
    }
    ;

set_annotation:
    'SET' an=OBJECT_IDENT '=' (qv=QUOTED_VALUE | list=VALUE_LIST | oi=OBJECT_IDENT) {
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
                    throw new IllegalStateException("Did not understand annotation value, expecting annotation list form.");
                }

                String listvalues = list.getText();
                listvalues = listvalues.substring(1, listvalues.length() - 1);
                annotation = new BELAnnotation(ad, Arrays.asList(ParserUtil.parseListRecord(listvalues)));
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
            addError(new DefineAnnotationBeforeUsageException(an.getLine(), an.getCharPositionInLine()));
        }

        if (name.equals("Citation")) {
            // redefinition of citation so clear out citation context
            citationContext = null;

            if (list == null) {
                addError(new InvalidCitationException(an.getLine(), an.getCharPositionInLine()));
            } else {
                String listvalues = list.getText();
                String[] tokens = ParserUtil.parseListRecord(listvalues);

                String type = null;
                String cname = null;
                String reference = null;
                Date publicationDate = null;
                String[] authors = null;
                String comment = null;

                // (required) parse type
                if (tokens.length > 0 && tokens[0] != null) {
	                type = tokens[0];
	                if (!("Book".equals(type) || "Journal".equals(type) || "Online Resource".equals(type) || "Other".equals(type) || "PubMed".equals(type))) {
	                    addError(new InvalidCitationException(an.getLine(), an.getCharPositionInLine()));
	                }
                } else {
                    addError(new InvalidCitationException(an.getLine(), an.getCharPositionInLine()));
                }

                // (required) parse name
                if (tokens.length > 1 && tokens[1] != null) {
	                if ("".equals(tokens[1].trim())) {
	                    addError(new InvalidCitationException(an.getLine(), an.getCharPositionInLine()));
	                } else {
	                    cname = tokens[1];
	                }
                } else {
                    addError(new InvalidCitationException(an.getLine(), an.getCharPositionInLine()));
                }

                // (required) parse reference
                if (tokens.length > 2 && tokens[2] != null) {
                    if ("".equals(tokens[2].trim())) {
                        addError(new InvalidCitationException(an.getLine(), an.getCharPositionInLine()));
                    } else {
                        reference = tokens[2];
                    }
                }

                // (optional) parse date of publication
                if (tokens.length > 3 && tokens[3] != null) {
	                if (!"".equals(tokens[3].trim())) {
	                    try {
	                        publicationDate = iso8601DateFormat.parse(tokens[3]);
	                    } catch (ParseException e) {
	                        addError(new InvalidCitationException(an.getLine(), an.getCharPositionInLine()));
	                    }

	                    if (publicationDate == null) {
	                        addError(new InvalidCitationException(an.getLine(), an.getCharPositionInLine()));
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

                citationContext = new BELCitation(type, cname, publicationDate, reference, authors == null ? null : Arrays.asList(authors), comment);
            }
        } else if (name.equals("Evidence")) {
            // redefinition of evidence so clear out evidence context
            evidenceContext = null;

            if (qv == null || "".equals(qv.getText().trim())) {
                addError(new InvalidEvidenceException(an.getLine(), an.getCharPositionInLine()));
            } else {
                evidenceContext = new BELEvidence(qv.getText());
            }
        }
    }
    ;

unset_statement_group:
    'UNSET' STATEMENT_GROUP_KEYWORD {
        activeStatementGroup = null;
        sgAnnotationContext.clear();
    }
    ;

unset:
    'UNSET' (an=OBJECT_IDENT | list=IDENT_LIST) {
        if (an != null) {
            String annotationName = an.getText();
            if ("ALL".equals(annotationName)) {
                if (activeStatementGroup == null)
                    annotationContext.clear();
                else
                    sgAnnotationContext.clear();
            } else if (definedAnnotations.containsKey(annotationName)) {
                if (activeStatementGroup == null)
                    annotationContext.remove(annotationName);
                else
                    sgAnnotationContext.remove(annotationName);
            } else if (docprop.containsKey(BELDocumentProperty.getDocumentProperty(annotationName))) {
                addError(new UnsetDocumentPropertiesException(an.getLine(), an.getCharPositionInLine()));
            } else {
                addWarning(new UnsetUndefinedAnnotationException(an.getLine(), an.getCharPositionInLine()));
            }
        }
    }
    ;

define_namespace:
    ('DEFINE' ((isdefault='DEFAULT')? 'NAMESPACE')) name=OBJECT_IDENT 'AS' 'URL' rloc=QUOTED_VALUE {
        final String nametext = $name.getText();
        final String rloctext = $rloc.getText();

        BELNamespaceDefinition belnsd;
        if (isdefault != null) {
            belnsd = new BELNamespaceDefinition(nametext, rloctext, true);
        } else {
            belnsd = new BELNamespaceDefinition(nametext, rloctext, false);
        }

        nslist.add(belnsd);
        definedNamespaces.put(nametext, belnsd);
    }
    ;

define_annotation:
    ('DEFINE' 'ANNOTATION') name=OBJECT_IDENT 'AS' (((type='URL' | type='PATTERN') value=QUOTED_VALUE) | (type='LIST' value=VALUE_LIST)) {
        final String nametext = $name.getText();

        if (type != null && value != null) {
	        final String typetext = type.toString();
	        String valuetext = value.toString();
	        BELAnnotationType atype;

	        BELAnnotationDefinition ad;
	        if ("URL".equals(typetext)) {
	            atype = BELAnnotationType.URL;
	            ad = new BELAnnotationDefinition(nametext, atype, valuetext);
	        } else if ("PATTERN".equals(typetext)) {
	            atype = BELAnnotationType.PATTERN;
	            ad = new BELAnnotationDefinition(nametext, atype, valuetext);
	        } else {
	            atype = BELAnnotationType.LIST;
	            valuetext = valuetext.substring(1, valuetext.length() - 1);
	            ad = new BELAnnotationDefinition(nametext, atype, Arrays.asList(ParserUtil.parseListRecord(valuetext)));
	        }

	        adlist.add(ad);
	        definedAnnotations.put(nametext, ad);
        }
    }
    ;

document_property returns [BELDocumentProperty r]:
    pv='Authors'     {$r = BELDocumentProperty.getDocumentProperty($pv.getText());} |
    pv='ContactInfo' {$r = BELDocumentProperty.getDocumentProperty($pv.getText());} |
    pv='Copyright'   {$r = BELDocumentProperty.getDocumentProperty($pv.getText());} |
    pv='Description' {$r = BELDocumentProperty.getDocumentProperty($pv.getText());} |
    pv='Disclaimer'  {$r = BELDocumentProperty.getDocumentProperty($pv.getText());} |
    pv='Licenses'    {$r = BELDocumentProperty.getDocumentProperty($pv.getText());} |
    pv='Name'        {$r = BELDocumentProperty.getDocumentProperty($pv.getText());} |
    pv='Version'     {$r = BELDocumentProperty.getDocumentProperty($pv.getText());}
    ;

statement:
    st=outer_term (rel=relationship ((OPEN_PAREN nst=outer_term nrel=relationship not=outer_term CLOSE_PAREN) | ot=outer_term))? comment=STATEMENT_COMMENT? {
        final StringBuilder stmtBuilder = new StringBuilder();
        stmtBuilder.append(st.r);

        if (rel != null) {
            stmtBuilder.append(" ").append(rel.r);

            if (ot != null) {
                stmtBuilder.append(" ").append(ot.r);
            } else {
                stmtBuilder.append("(");

                if (nst != null && nrel != null && not != null) {
                    stmtBuilder.append(nst.r).append(" ").append(nrel.r).append(" ").append(not.r);
                }
                stmtBuilder.append(")");
            }
        }

        String commentText = null;
        if (comment != null) {
            commentText = comment.getText();
        }

        // build effective annotations from main statement group context and then local statement group context, if any
        final Map<String, BELAnnotation> effectiveAnnotations = new LinkedHashMap<String, BELAnnotation>(annotationContext);
        if (activeStatementGroup != null) {
            effectiveAnnotations.putAll(sgAnnotationContext);
        }

        final List<BELAnnotation> annotations = new ArrayList<BELAnnotation>(effectiveAnnotations.values());

        // build statement and keep track of it for validation purposes
        final BELStatement stmt = new BELStatement(stmtBuilder.toString(), annotations, citationContext, evidenceContext, commentText);
        stmtlist.add(stmt);

        // add statement to scoped statement group
        if (activeStatementGroup != null) {
            activeStatementGroup.getStatements().add(stmt);
        } else {
            documentStatementGroup.getStatements().add(stmt);
        }
    }
    ;

outer_term returns [String r]
@init {
    final StringBuilder tBuilder = new StringBuilder();
}
:
    f=function {
        tBuilder.append(f.r);
    } op=OPEN_PAREN {
        tBuilder.append(op.getText());
    } (c=','? {
        if (c != null) {
            tBuilder.append(c.getText());
        }
    } a=argument {
        tBuilder.append(a.r);
    })* cp=CLOSE_PAREN {
        tBuilder.append(cp.getText());
        $r = tBuilder.toString();
    }
    ;

argument returns [String r]:
    p=param {
        $r = p.r;
    } |
    t=term {
        $r = t.r;
    }
    ;

term returns [String r]
@init {
    final StringBuilder termBuilder = new StringBuilder();
}
:
    f=function {
        termBuilder.append(f.r);
    } op=OPEN_PAREN {
        termBuilder.append(op.getText());
    }(c=','? {
        if (c != null) {
            termBuilder.append(c.getText());
        }
    } (t=term {
        termBuilder.append(t.r);
    } | p=param {
        termBuilder.append(p.r);
    }))* cp=CLOSE_PAREN {
        termBuilder.append(cp.getText());
        $r = termBuilder.toString();
    }
    ;

/* XXX OBJECT_IDENT is used for namespace value because otherwise parsing will fail using a token like (LETTER | DIGIT)+ */
fragment param returns [String r]:
    nsp=NS_PREFIX? (id=OBJECT_IDENT | quo=QUOTED_VALUE) {
        final StringBuilder pBuilder = new StringBuilder();

        if (nsp != null) {
            String prefix = nsp.getText();
            if (!definedNamespaces.containsKey(prefix.substring(0, prefix.length() - 1))) {
                addError(new NamespaceUndefinedException(nsp.getLine(), nsp.getCharPositionInLine()));
            }

            pBuilder.append(prefix);
        }

        if (id != null) {
            pBuilder.append(id.getText());
        }

        if (quo != null) {
            pBuilder.append(quo.getText());
        }

        $r = pBuilder.toString();
    }
    ;

function returns [String r]:
    (
        fv='proteinAbundance'           { $r = $fv.getText(); } |
        fv='p'                          { $r = $fv.getText(); } |
        fv='rnaAbundance'               { $r = $fv.getText(); } |
        fv='r'                          { $r = $fv.getText(); } |
        fv='abundance'                  { $r = $fv.getText(); } |
        fv='a'                          { $r = $fv.getText(); } |
        fv='microRNAAbundance'          { $r = $fv.getText(); } |
        fv='m'                          { $r = $fv.getText(); } |
        fv='geneAbundance'              { $r = $fv.getText(); } |
        fv='g'                          { $r = $fv.getText(); } |
        fv='biologicalProcess'          { $r = $fv.getText(); } |
        fv='bp'                         { $r = $fv.getText(); } |
        fv='pathology'                  { $r = $fv.getText(); } |
        fv='path'                       { $r = $fv.getText(); } |
        fv='complexAbundance'           { $r = $fv.getText(); } |
        fv='complex'                    { $r = $fv.getText(); } |
        fv='translocation'              { $r = $fv.getText(); } |
        fv='tloc'                       { $r = $fv.getText(); } |
        fv='cellSecretion'              { $r = $fv.getText(); } |
        fv='sec'                        { $r = $fv.getText(); } |
        fv='cellSurfaceExpression'      { $r = $fv.getText(); } |
        fv='surf'                       { $r = $fv.getText(); } |
        fv='reaction'                   { $r = $fv.getText(); } |
        fv='rxn'                        { $r = $fv.getText(); } |
        fv='compositeAbundance'         { $r = $fv.getText(); } |
        fv='composite'                  { $r = $fv.getText(); } |
        fv='fusion'                     { $r = $fv.getText(); } |
        fv='fus'                        { $r = $fv.getText(); } |
        fv='degradation'                { $r = $fv.getText(); } |
        fv='deg'                        { $r = $fv.getText(); } |
        fv='molecularActivity'          { $r = $fv.getText(); } |
        fv='act'                        { $r = $fv.getText(); } |
        fv='catalyticActivity'          { $r = $fv.getText(); } |
        fv='cat'                        { $r = $fv.getText(); } |
        fv='kinaseActivity'             { $r = $fv.getText(); } |
        fv='kin'                        { $r = $fv.getText(); } |
        fv='phosphataseActivity'        { $r = $fv.getText(); } |
        fv='phos'                       { $r = $fv.getText(); } |
        fv='peptidaseActivity'          { $r = $fv.getText(); } |
        fv='pep'                        { $r = $fv.getText(); } |
        fv='ribosylationActivity'       { $r = $fv.getText(); } |
        fv='ribo'                       { $r = $fv.getText(); } |
        fv='transcriptionalActivity'    { $r = $fv.getText(); } |
        fv='tscript'                    { $r = $fv.getText(); } |
        fv='transportActivity'          { $r = $fv.getText(); } |
        fv='tport'                      { $r = $fv.getText(); } |
        fv='gtpBoundActivity'           { $r = $fv.getText(); } |
        fv='gtp'                        { $r = $fv.getText(); } |
        fv='chaperoneActivity'          { $r = $fv.getText(); } |
        fv='chap'                       { $r = $fv.getText(); } |
        fv='proteinModification'        { $r = $fv.getText(); } |
        fv='pmod'                       { $r = $fv.getText(); } |
        fv='substitution'               { $r = $fv.getText(); } |
        fv='sub'                        { $r = $fv.getText(); } |
        fv='truncation'                 { $r = $fv.getText(); } |
        fv='trunc'                      { $r = $fv.getText(); } |
        fv='reactants'                  { $r = $fv.getText(); } |
        fv='products'                   { $r = $fv.getText(); } |
        fv='list'                       { $r = $fv.getText(); }
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
