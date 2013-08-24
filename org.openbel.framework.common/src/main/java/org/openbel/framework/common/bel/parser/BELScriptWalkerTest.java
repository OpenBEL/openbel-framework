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

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.openbel.bel.model.*;

/**
 * TODO Convert to true unit test and remove hardcoded paths.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class BELScriptWalkerTest {

    public static void main(String[] args) throws IOException {
        parseBELScript(new File(
                "/home/abargnesi/projects-bel/docs/BEL Script/example1.bel"));
        parseBELScript(new File(
                "/home/abargnesi/projects-bel/docs/BEL Script/fullabstract1.bel"));
        parseBELScript(new File(
                "/home/abargnesi/projects-bel/docs/BEL Script/fullabstract2.bel"));
        parseBELScript(new File(
                "/home/abargnesi/projects-bel/docs/BEL Script/fullabstract3.bel"));
    }

    private static void parseBELScript(File belscriptFile) throws IOException {
        BufferedReader reader =
                new BufferedReader(new FileReader(belscriptFile));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        reader.close();

        parseBELScript(sb.toString());
    }

    private static void parseBELScript(String belscript) {
        BELParseResults results = BELParser.parse(belscript);

        if (!results.getSyntaxWarnings().isEmpty()) {
            for (BELParseWarningException syntaxWarning : results
                    .getSyntaxWarnings()) {
                System.out.println(syntaxWarning.getMessage());
            }
        }

        if (!results.getSyntaxErrors().isEmpty()) {
            for (BELParseErrorException syntaxError : results.getSyntaxErrors()) {
                System.out.println(syntaxError.getMessage());
            }
        } else {
            report(results.getDocument());
        }
    }

    private static void report(BELDocument doc) {
        // header
        System.out.println("\n-header-");
        BELDocumentHeader docheader = doc.getDocumentHeader();
        System.out.println("Authors are: " + docheader.getAuthor());
        System.out.println("Copyright is: " + docheader.getCopyright());
        System.out.println("ContactInfo is: " + docheader.getContactInfo());
        System.out.println("Description is: " + docheader.getDescription());
        System.out.println("Disclaimer is: " + docheader.getDisclaimer());
        System.out.println("Licenses are: " + docheader.getLicense());
        System.out.println("Name is: " + docheader.getName());
        System.out.println("Version is: " + docheader.getVersion());

        // annotation definitions
        System.out.println("\n-annotations-");
        for (BELAnnotationDefinition anndef : doc.getAnnotationDefinitions()) {
            switch (anndef.getAnnotationType()) {
            case PATTERN:
            case URL:
                System.out.println(anndef.getName() + "("
                        + anndef.getAnnotationType() + "), Domain: "
                        + anndef.getValue());
                break;
            case LIST:
                System.out.println(anndef.getName() + "("
                        + anndef.getAnnotationType() + "), Domain: "
                        + anndef.getList());
                break;
            }
        }

        System.out.println("\n-namespaces-");
        // namespace definitions
        for (BELNamespaceDefinition nsdef : doc.getNamespaceDefinitions()) {
            if (nsdef.isNsDefault()) {
                System.out.println("DEFAULT NAMESPACE, Location: "
                        + nsdef.getResourceLocation());
            } else {
                System.out.println(nsdef.getPrefix() + ", Location: "
                        + nsdef.getResourceLocation());
            }
        }

        System.out.println("\n-statements-");
        // statements
        for (BELStatementGroup stmtGroup : doc.getBelStatementGroups()) {

            for (BELStatement stmt : stmtGroup.getStatements()) {
                if (stmt.getComment() != null) {
                    System.out.println("\n" + stmt.getStatementSyntax());
                    System.out.println("Comment: " + stmt.getComment());
                } else {
                    System.out.println("\n" + stmt.getStatementSyntax());
                }

                final BELCitation citation = stmt.getCitation();
                if (citation != null) {
                    System.out.println("Citation: " + citation.getName());
                }

                final BELEvidence evidence = stmt.getEvidence();
                if (evidence != null) {
                    System.out.println("Evidence: "
                            + evidence.getEvidenceLine());
                }

                List<BELAnnotation> annotations = stmt.getAnnotations();
                if (hasItems(annotations)) {
                    for (BELAnnotation annotation : annotations) {
                        System.out.print("Annotation ("
                                + annotation.getAnnotationDefinition()
                                        .getName()
                                + "): ");
                        int i = 0;
                        for (String value : annotation.getValues()) {
                            if (i++ != 0) {
                                System.out.print(", ");
                            }
                            System.out.print(value);
                        }
                        System.out.println();
                    }
                }
            }
        }
    }
}
