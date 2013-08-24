/**
 *  Copyright 2013 OpenBEL Consortium
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openbel.framework.common.bel.parser;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.openbel.bel.model.BELAnnotation;
import org.openbel.bel.model.BELAnnotationDefinition;
import org.openbel.bel.model.BELCitation;
import org.openbel.bel.model.BELDocument;
import org.openbel.bel.model.BELDocumentHeader;
import org.openbel.bel.model.BELEvidence;
import org.openbel.bel.model.BELStatement;
import org.openbel.bel.model.BELStatementGroup;

/**
 * {@link BELScriptParser} tests.
 */
public class BELScriptParserTest {

    /**
     * Test BEL Script lexer/parser handling of annotation patterns with escape
     * sequences.
     *
     * @see https://github.com/OpenBEL/openbel-framework/issues/14
     */
    @Test
    public void testIssue14() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("SET DOCUMENT Name = \"Name\"\n");
        bldr.append("SET DOCUMENT Description = \"Description\"\n");
        bldr.append("SET DOCUMENT Version = \"1.0\"\n");
        bldr.append("DEFINE ANNOTATION X AS PATTERN ");

        // Note the same string would be captured in BEL Script as:
        // "0|1|0?\\.[\\d]+|1\\.[0]+"
        bldr.append("\"0|1|0?\\\\.[\\\\d]+|1\\\\.[0]+\"\n");

        String belscript = bldr.toString();

        BELParseResults parse = BELParser.parse(belscript);
        assertTrue(parse.getSyntaxWarnings().isEmpty());
        assertTrue(parse.getSyntaxErrors().isEmpty());

        BELDocument doc = parse.getDocument();
        assertNotNull(doc);

        Set<BELAnnotationDefinition> annodefs = doc.getAnnotationDefinitions();
        assertNotNull(annodefs);
        assertTrue(annodefs.size() == 1);

        String value = annodefs.iterator().next().getValue();
        assertTrue(value.equals("0|1|0?\\.[\\d]+|1\\.[0]+"));
    }

    /**
     * Test BEL Script lexer/parser handling of document properties with escape
     * sequences.
     */
    @Test
    public void testDocumentProperty() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("SET DOCUMENT Name = \"Tabbed\tname\\t\"\n");
        bldr.append("SET DOCUMENT Description = \"Description\"\n");
        bldr.append("SET DOCUMENT Version = \"1.0\"\n");
        String belscript = bldr.toString();

        BELParseResults parse = BELParser.parse(belscript);
        assertTrue(parse.getSyntaxWarnings().isEmpty());
        assertTrue(parse.getSyntaxErrors().isEmpty());

        BELDocument doc = parse.getDocument();
        assertNotNull(doc);

        BELDocumentHeader header = doc.getDocumentHeader();
        assertNotNull(header);

        String name = header.getName();
        assertNotNull(name);
        assertTrue(name.equals("Tabbed\tname\\t"));
    }

    /**
     * Test BEL Script lexer/parser handling of statement groups with escape
     * sequences.
     */
    @Test
    public void testStatementGroup() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("SET DOCUMENT Name = \"Name\"\n");
        bldr.append("SET DOCUMENT Description = \"Description\"\n");
        bldr.append("SET DOCUMENT Version = \"1.0\"\n");
        bldr.append("SET STATEMENT_GROUP = \"\u03B2-cell study\"\n");
        String belscript = bldr.toString();

        BELParseResults parse = BELParser.parse(belscript);
        assertTrue(parse.getSyntaxWarnings().isEmpty());
        assertTrue(parse.getSyntaxErrors().isEmpty());

        BELDocument doc = parse.getDocument();
        assertNotNull(doc);

        List<BELStatementGroup> groups = doc.getBelStatementGroups();
        assertTrue(groups.size() == 1);
        BELStatementGroup group = groups.get(0);

        assertTrue(group.getName().equals("\"β-cell study\""));
    }

    /**
     * Test BEL Script lexer/parser handling of annotations with escape
     * sequences.
     *
     * @see https://github.com/OpenBEL/openbel-framework/issues/14
     */
    @Test
    public void testAnnotation() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("SET DOCUMENT Name = \"Name\"\n");
        bldr.append("SET DOCUMENT Description = \"Description\"\n");
        bldr.append("SET DOCUMENT Version = \"1.0\"\n");
        bldr.append("DEFINE ANNOTATION X AS PATTERN ");

        // Note the same string would be captured in BEL Script as:
        // "0|1|0?\\.[\\d]+|1\\.[0]+"
        bldr.append("\"0|1|0?\\\\.[\\\\d]+|1\\\\.[0]+\"\n");

        bldr.append("SET STATEMENT_GROUP = \"\u03B2-cell study\"\n");
        bldr.append("SET X = \"0.42\"\n");
        bldr.append("p(X) -> p(Y)\n");
        bldr.append("UNSET STATEMENT_GROUP\n");

        String belscript = bldr.toString();

        BELParseResults parse = BELParser.parse(belscript);
        assertTrue(parse.getSyntaxWarnings().isEmpty());
        assertTrue(parse.getSyntaxErrors().isEmpty());

        BELDocument doc = parse.getDocument();
        assertNotNull(doc);

        List<BELStatementGroup> groups = doc.getBelStatementGroups();
        BELStatementGroup group = groups.get(0);
        List<BELStatement> stmts = group.getStatements();
        assertTrue(stmts.size() == 1);

        BELStatement stmt = stmts.get(0);
        List<BELAnnotation> annos = stmt.getAnnotations();
        assertTrue(annos.size() == 1);

        BELAnnotation anno = annos.get(0);
        assertTrue(anno.getValues().size() == 1);

        assertTrue("0.42".equals(anno.getValues().get(0)));
    }

    /**
     * Test BEL Script lexer/parser handling of citations with escape sequences.
     */
    @Test
    public void testCitation() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("SET DOCUMENT Name = \"Name\"\n");
        bldr.append("SET DOCUMENT Description = \"Description\"\n");
        bldr.append("SET DOCUMENT Version = \"1.0\"\n");

        bldr.append("SET STATEMENT_GROUP = \"\u03B2-cell study\"\n");
        bldr.append("SET Evidence = \"Evidence\nneeded\"\n");
        bldr.append("SET Citation = {\"PubMed\", \"X\", \"Y\", \"\", \"\", \"\"}\n");
        bldr.append("p(X) -> p(Y)\n");
        bldr.append("UNSET STATEMENT_GROUP\n");

        String belscript = bldr.toString();

        BELParseResults parse = BELParser.parse(belscript);
        for (final Object o : parse.getSyntaxWarnings()) {
            System.out.println(o);
        }
        for (final Object o : parse.getSyntaxErrors()) {
            System.out.println(o);
        }
        assertTrue(parse.getSyntaxWarnings().isEmpty());
        assertTrue(parse.getSyntaxErrors().isEmpty());

        BELDocument doc = parse.getDocument();
        assertNotNull(doc);

        List<BELStatementGroup> groups = doc.getBelStatementGroups();
        BELStatementGroup group = groups.get(0);
        List<BELStatement> stmts = group.getStatements();
        assertTrue(stmts.size() == 1);

        BELStatement stmt = stmts.get(0);

        BELEvidence evidence = stmt.getEvidence();
        assertNotNull(evidence);

        BELCitation citation = stmt.getCitation();
        assertNotNull(citation);

        assertEquals("Evidence\nneeded", evidence.getEvidenceLine());
        assertEquals("PubMed", citation.getType());
        assertEquals("X", citation.getName());
        assertEquals("Y", citation.getReference());
    }
}
