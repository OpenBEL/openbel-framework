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
package org.openbel.framework.common.belscript;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.Strings.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.openbel.bel.model.BELDocumentProperty;
import org.openbel.framework.common.BELRuntimeException;
import org.openbel.framework.common.BELUtilities;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.common.model.*;

public class BELScriptExporter {

    private static final String DEFAULT_NAMESPACE_PREFIX = "DEFNS"; //BEL script format supports having a prefix for default namespace
    private static final String EVIDENCE_ANNOTATION_NAME = "Evidence"; //built-in annotation name for evidence
    private static final String CITATION_ANNOTATION_NAME = "Citation"; //built-in annotation name for citation

    private static final int TARGET_STATEMENT_GROUP_LEVEL = 1; //BEL Script only support 1 level of statement group

    private boolean useShortForm = false;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int statementGroupCounter = 0;

    public boolean isUseShortForm() {
        return useShortForm;
    }

    public void setUseShortForm(boolean useShortForm) {
        this.useShortForm = useShortForm;
    }

    public void export(Document document, OutputStream os)
            throws BELRuntimeException, IOException {
        if (document != null && os != null) {
            final Writer writer =
                    new OutputStreamWriter(os, Charset.forName(UTF_8));

            writeSectionComment("Document Properties Section", writer);
            writeDocumentProperties(document.getHeader(), writer);

            writeSectionComment("Definitions Section", writer);
            writeNamespaces(document.getNamespaceGroup(), writer);
            writeAnnotationDefinitions(document.getDefinitions(), writer);

            writeSectionComment("Statements Section", writer);

            Map<String, List<String>> inheritedAnnotationMap =
                    new HashMap<String, List<String>>(); //for inheritance of annotations by child statemnet groups
            Map<String, List<String>> currentAnnotationMap =
                    new HashMap<String, List<String>>(); //for tracking current annotations in belscript
            Stack<StatementGroup> statementGroupStack =
                    new Stack<StatementGroup>();
            for (StatementGroup statementGroup : document.getStatementGroups()) {
                writeStatementGroup(statementGroup, inheritedAnnotationMap,
                        currentAnnotationMap, statementGroupStack, writer);
            }

            writer.flush();
        }
    }

    protected void writeDocumentProperties(Header header, Writer writer)
            throws IOException {
        writeDocumentProperty(BELDocumentProperty.NAME.getName(),
                header.getName(), writer);
        writeDocumentProperty(BELDocumentProperty.DESCRIPTION.getName(),
                header.getDescription(), writer);
        writeDocumentProperty(BELDocumentProperty.VERSION.getName(),
                header.getVersion(), writer);
        writeDocumentProperty(BELDocumentProperty.COPYRIGHT.getName(),
                header.getCopyright(), writer);
        writeDocumentProperty(BELDocumentProperty.DISCLAIMER.getName(),
                header.getDisclaimer(), writer);
        writeDocumentProperty(BELDocumentProperty.AUTHOR.getName(),
                header.getAuthors(), writer);
        writeDocumentProperty(BELDocumentProperty.LICENSE.getName(),
                header.getLicenses(), writer);
        writeDocumentProperty(BELDocumentProperty.CONTACT_INFO.getName(),
                header.getContactInfo(), writer);
        writer.write("\n");
    }

    protected void writeDocumentProperty(String propertyName, String value,
            Writer writer) throws IOException {
        if (value != null) {
            writer.write("SET DOCUMENT " + propertyName + " = "
                    + doubleQuote(value) + "\n");
        }
    }

    protected void writeDocumentProperty(String propertyName,
            List<String> values, Writer writer) throws IOException {
        if (values != null && values.size() > 0) {
            writeDocumentProperty(propertyName, BELUtilities.join(values, "|"),
                    writer);
        }
    }

    protected void
            writeNamespaces(NamespaceGroup namespaceGroup, Writer writer)
                    throws IOException {
        if (namespaceGroup != null) {
            writeDefaultNamespace(DEFAULT_NAMESPACE_PREFIX,
                    namespaceGroup.getDefaultResourceLocation(), writer);

            final List<Namespace> nsl = namespaceGroup.getNamespaces();
            if (hasItems(nsl)) {
                for (Namespace namespace : nsl) {
                    writeNamespace(namespace, writer);
                }
            }
            writer.write("\n");
        }
    }

    protected void writeDefaultNamespace(String prefix, String location,
            Writer writer) throws IOException {
        if (location != null && prefix != null) {
            writer.write("DEFINE DEFAULT NAMESPACE " + prefix + " AS URL "
                    + doubleQuote(location) + "\n");
        }
    }

    protected void writeNamespace(Namespace namespace, Writer writer)
            throws IOException {
        if (namespace != null) {
            writer.write("DEFINE NAMESPACE " + namespace.getPrefix()
                    + " AS URL " + doubleQuote(namespace.getResourceLocation())
                    + "\n");
        }
    }

    protected void writeAnnotationDefinitions(
            List<AnnotationDefinition> annotationDefinitions, Writer writer)
            throws IOException {
        if (annotationDefinitions != null) {
            for (AnnotationDefinition annotationDefinition : annotationDefinitions) {
                writeAnnotationDefinition(annotationDefinition, writer);
            }
            writer.write("\n");
        }
    }

    protected void writeAnnotationDefinition(
            AnnotationDefinition annotationDefinition, Writer writer)
            throws IOException {
        if (annotationDefinition != null) {
            writer.write("DEFINE ANNOTATION " + annotationDefinition.getId()
                    + " AS ");
            if (annotationDefinition.getURL() != null) {
                writer.write(" URL "
                        + doubleQuote(annotationDefinition.getURL()) + "\n");
            } else {
                switch (annotationDefinition.getType()) {
                case ENUMERATION:
                    writer.write(" LIST "
                            + formatListValues(annotationDefinition.getEnums(),
                                    true) + "\n");
                    break;
                case REGULAR_EXPRESSION:
                    writer.write(" PATTERN "
                            + doubleQuote(annotationDefinition.getValue()) + "\n");
                    break;
                default:
                    throw new BELRuntimeException(
                            "Unrecognized annotation type: "
                                    + annotationDefinition.getType(),
                            ExitCode.CONVERSION_FAILURE);
                }
            }
        }
    }

    protected String formatListValues(List<String> values,
            boolean alwaysDoubleQuoteValue) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (values != null) {
            Iterator<String> v = values.iterator();
            while (v.hasNext()) {
                String value = v.next();
                boolean isAlphaNumeric = BELUtilities.isAlphanumeric(value);
                if (alwaysDoubleQuoteValue || isAlphaNumeric) {
                    sb.append(doubleQuote(value));
                }
                if (v.hasNext()) {
                    sb.append(",");
                }
            }
        }
        sb.append("}");
        return sb.toString();
    }

    protected String doubleQuote(String value) {
        if (value != null) {
            String escapedString =
                    value.replaceAll("([^\\\\])\\\"", "$1\\\\\"").replaceAll(
                            "^\\\"", "\\\\\"");
            return "\"" + escapedString + "\"";
        }

        return "";
    }

    protected void writeSectionComment(String comment, Writer writer)
            throws IOException {
        writer.write("##################################################################################\n");
        writer.write("# " + comment + "\n");
        writer.write("\n");
    }

    protected void writeStatementGroup(StatementGroup statementGroup,
            Map<String, List<String>> inheritedAnnotationMap,
            Map<String, List<String>> currentAnnotationMap,
            Stack<StatementGroup> statementGroupStack, Writer writer)
            throws IOException {

        if (statementGroup != null) {
            Map<String, List<String>> immediateAnnotationMap =
                    createAnnotationMap(statementGroup.getAnnotationGroup());
            Map<String, List<String>> effectiveAnnotationMap =
                    getEffectiveAnnotationMap(inheritedAnnotationMap,
                            immediateAnnotationMap);

            statementGroupStack.push(statementGroup);
            Map<String, List<String>> currentAnnotationMapSnapshot = null;
            if (statementGroupStack.size() == TARGET_STATEMENT_GROUP_LEVEL) {
                String sgname;
                if (statementGroup.getName() != null) {
                    sgname = "\"" + statementGroup.getName() + "\"";
                } else {
                    sgname = "\"Group " + ++statementGroupCounter + "\"";
                }
                writer.write("SET STATEMENT_GROUP = " + sgname + "\n");

                //save existing annotation statement, when group is unset, restore to this state
                currentAnnotationMapSnapshot =
                        new HashMap<String, List<String>>();
                currentAnnotationMapSnapshot.putAll(currentAnnotationMap);
            }
            //process all statements in this group
            List<Statement> statements = statementGroup.getStatements();
            if (statements != null) {
                for (Statement statement : statements) {
                    writeStatement(statement, effectiveAnnotationMap,
                            currentAnnotationMap, writer);
                }
            }
            //process all child statement groups
            List<StatementGroup> childGroups =
                    statementGroup.getStatementGroups();
            if (childGroups != null) {
                for (StatementGroup group : childGroups) {
                    writeStatementGroup(group, effectiveAnnotationMap,
                            currentAnnotationMap, statementGroupStack, writer);
                }
            }
            if (statementGroupStack.size() == TARGET_STATEMENT_GROUP_LEVEL) {
                writer.write("\nUNSET STATEMENT_GROUP\n\n");
                //restore annotation state when statement group ends
                currentAnnotationMap.clear();
                currentAnnotationMap.putAll(currentAnnotationMapSnapshot);
            }
            statementGroupStack.pop();
        }
    }

    protected void writeStatement(Statement statement,
            Map<String, List<String>> inheritedAnnotationMap,
            Map<String, List<String>> currentAnnotationMap, Writer writer)
            throws IOException {

        if (statement != null) {
            Map<String, List<String>> immediateAnnotationMap =
                    createAnnotationMap(statement.getAnnotationGroup());
            Map<String, List<String>> effectiveAnnotationMap =
                    getEffectiveAnnotationMap(inheritedAnnotationMap,
                            immediateAnnotationMap);
            writeAnnotations(effectiveAnnotationMap, currentAnnotationMap,
                    writer);

            if (isUseShortForm()) {
                writer.write(statement.toBELShortForm());
            } else {
                writer.write(statement.toBELLongForm());
            }
            if (BELUtilities.hasLength(statement.getComment())) {
                writer.write(" //");
                writer.write(statement.getComment());
            }
            writer.write("\n");
        }
    }

    protected Map<String, List<String>> createAnnotationMap(
            AnnotationGroup annotationGroup) {
        Map<String, List<String>> annotationMap =
                new HashMap<String, List<String>>();
        if (annotationGroup != null) {
            //add citation
            if (annotationGroup.getCitation() != null) {
                List<String> citation =
                        getCitationList(annotationGroup.getCitation());
                annotationMap.put(CITATION_ANNOTATION_NAME, citation);
            }
            //add evidence line
            if (annotationGroup.getEvidence() != null) {
                List<String> evidence = new ArrayList<String>();
                evidence.add(annotationGroup.getEvidence().getValue()
                        .replaceAll("\n", " "));
                annotationMap.put(EVIDENCE_ANNOTATION_NAME, evidence);
            }
            //add all other annotations
            List<Annotation> annotations = annotationGroup.getAnnotations();
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    String name = annotation.getDefinition().getId();
                    List<String> values = annotationMap.get(name);
                    if (values == null) {
                        values = new ArrayList<String>();
                        annotationMap.put(name, values);
                    }
                    values.add(annotation.getValue());
                }
            }
        }

        return annotationMap;
    }

    protected List<String> getCitationList(Citation c) {
        List<String> citation = new ArrayList<String>();
        citation.add(c.getType().getDisplayValue()); //1st element is type, required
        citation.add(c.getName()); //2nd element is name, required
        citation.add(c.getReference()); //3, required reference id
        if (c.getDate() != null) {
            citation.add(dateFormat.format(c.getDate().getTime())); //4, optional time
        } else {
            citation.add("");
        }
        if (c.getAuthors() != null) {
            citation.add(BELUtilities.join(c.getAuthors(), "|")); //5, optional | separated authors
        } else {
            citation.add("");
        }
        if (c.getComment() != null) {
            citation.add(c.getComment()); //6, optional comment
        } else {
            citation.add("");
        }
        return citation;
    }

    /**
     * Resolves the effective set of annotation by combining the inherited annotations with the immediate annotations associated
     * with the statement or statement group. If an annotation existing in both the inherited map and the immediate map, the immediate
     * annotation overrides the inherited.
     *
     * @param inheritedAnnotationMap
     * @param immediateAnnotationMap
     * @return
     */
    protected Map<String, List<String>> getEffectiveAnnotationMap(
            Map<String, List<String>> inheritedAnnotationMap,
            Map<String, List<String>> immediateAnnotationMap) {
        //resolve effective annotations
        Map<String, List<String>> effectiveAnnotationMap =
                new HashMap<String, List<String>>();
        //take all inherited annotation
        effectiveAnnotationMap.putAll(inheritedAnnotationMap);
        //add or override with annotations from the immediate entity
        effectiveAnnotationMap.putAll(immediateAnnotationMap);
        return effectiveAnnotationMap;
    }

    /**
     * Writes a series of BEL script control statements via the writer to match current annotations to the effective
     * annotations. At the end of this function, the content of currentAnnotationMap will be equivalent to the
     * content of effectiveAnnotationMap.
     *
     * @param effectiveAnnotationMap
     * @param currentAnnotationMap
     * @param writer
     * @throws IOException
     */
    protected void writeAnnotations(
            Map<String, List<String>> effectiveAnnotationMap,
            Map<String, List<String>> currentAnnotationMap, Writer writer)
            throws IOException, BELRuntimeException {

        List<Map.Entry<String, List<String>>> entriesToSet =
                new ArrayList<Map.Entry<String, List<String>>>();
        for (Map.Entry<String, List<String>> effEntry : effectiveAnnotationMap
                .entrySet()) {
            if (!currentAnnotationMap.containsKey(effEntry.getKey())) {
                //set annotation that are unique to the effective map
                entriesToSet.add(effEntry);
            } else {
                List<String> currentValues =
                        currentAnnotationMap.get(effEntry.getKey());
                List<String> effectiveValues = effEntry.getValue();
                if (currentValues == null || effectiveValues == null) {
                    throw new BELRuntimeException("Invalid annotation value",
                            ExitCode.PARSE_ERROR);
                }
                //set annotation that exist in both map, but values are different in effective map
                //this will override the annotation
                if (!currentValues.equals(effectiveValues)) {
                    entriesToSet.add(effEntry);
                }
            }
        }

        //remove annotations that are no longer in the effective set
        currentAnnotationMap.keySet()
                .removeAll(effectiveAnnotationMap.keySet());
        if (!currentAnnotationMap.keySet().isEmpty() || !entriesToSet.isEmpty()) {
            writer.write("\n");
            //unset
            for (String name : currentAnnotationMap.keySet()) {
                unsetAnnotation(name, writer);
            }
            //set
            for (Map.Entry<String, List<String>> entry : entriesToSet) {
                setAnnotation(entry.getKey(), entry.getValue(), writer);
            }
            writer.write("\n");
        }

        //current annotation map now reflects the effective annotations
        currentAnnotationMap.clear();
        currentAnnotationMap.putAll(effectiveAnnotationMap);
    }

    protected void
            setAnnotation(String name, List<String> values, Writer writer)
                    throws IOException, BELRuntimeException {
        if (values == null || values.isEmpty()) {
            throw new BELRuntimeException("Invalid annotation value",
                    ExitCode.PARSE_ERROR);
        }
        writer.write("SET " + name + " = ");

        if (values.size() == 1) {
            writer.write(doubleQuote(values.get(0)));
        } else {
            writer.write(formatListValues(values, true));
        }
        writer.write("\n");
    }

    protected void unsetAnnotation(String name, Writer writer)
            throws IOException {
        writer.write("UNSET " + name);
        writer.write("\n");
    }

}
