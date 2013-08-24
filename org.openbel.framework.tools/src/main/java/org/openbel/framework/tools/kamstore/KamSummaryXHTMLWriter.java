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
package org.openbel.framework.tools.kamstore;

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KAMStoreDaoImpl.AnnotationDefinitionType;
import org.openbel.framework.internal.KAMStoreDaoImpl.AnnotationType;

public final class KamSummaryXHTMLWriter extends KamXHTMLWriter {

    public KamSummaryXHTMLWriter(OutputStreamWriter writer) {
        super("KAM Summary", writer);
    }

    public void write(KamSummary summary) throws IOException {
        writeDiv("Summary for KAM " + summary.getKamInfo().getName());
        writeTable(summary);
        writeAnnotationTypeTable(summary);
        writeNewLine();
        writeNewLine();
    }

    public void write(List<KamInfo> kamInfos) throws IOException {
        writeDiv("Available KAMs");
        writer.write("<table>");
        writeTableRow("Name", "Last Compiled", "Schema Name");
        for (KamInfo kamInfo : kamInfos) {
            writeTableRow(kamInfo.getName(), kamInfo.getLastCompiled(),
                    kamInfo.getLastCompiled());
        }
        writer.write("</table>");
        writeNewLine();
        writeNewLine();
    }

    private void writeFilteredKamSummary(String filteredKamTitle,
            KamSummary summary) throws IOException {
        writeDiv(filteredKamTitle);
        writer.write("<table>");
        writeTableRow("Number of Statements", summary.getNumOfBELStatements());
        writeTableRow("Number of Edges", summary.getNumOfEdges());
        writer.write("</table>");
        writeNewLine();
        writeNewLine();
    }

    private void writeTable(KamSummary summary) throws IOException {
        writer.write("<table>");
        writeTableRow("KAM Name", summary.getKamInfo().getName());

        writeTableRow("Last Compiled", summary.getKamInfo().getLastCompiled());
        writeTableRow("Description", summary.getKamInfo().getDescription());
        writeTableRow("Number of BEL Documents", summary.getNumOfBELDocuments());
        writeTableRow("Number of Namespaces", summary.getNumOfNamespaces());
        writeTableRow("Number of Annotation Types",
                summary.getNumOfAnnotationTypes());
        writeTableRow("Number of Statements", summary.getNumOfBELStatements());
        writeTableRow("Number of Nodes", summary.getNumOfNodes());
        writeTableRow("Number of Edges", summary.getNumOfEdges());
        writer.write("</table>");
        writeNewLine();
        writeNewLine();

        if (summary.getFilteredKamSummaries() != null
                && !summary.getFilteredKamSummaries().isEmpty()) {
            writeDiv("Summary by Annotation Type");
            writeNewLine();
            for (String filteredKam : summary.getFilteredKamSummaries()
                    .keySet()) {
                writeFilteredKamSummary(filteredKam, summary
                        .getFilteredKamSummaries().get(filteredKam));
            }
        }
    }

    private void writeAnnotationTypeTable(KamSummary summary)
            throws IOException {
        if (hasItems(summary.getAnnotations())) {
            writeDiv("Annotation Types");
            for (AnnotationType annotation : summary.getAnnotations()) {
                writer.write("<table>");
                writeTableRow("Name", annotation.getName());

                switch (annotation.getAnnotationDefinitionType()) {
                case ENUMERATION:
                    writeTableRow("Type", "List");
                    break;
                case REGULAR_EXPRESSION:
                    writeTableRow("Type", "Pattern");
                    break;
                case URL:
                    writeTableRow("Type", "URL");
                    writeTableRow("Location", annotation.getUrl());
                    break;
                }

                if (annotation.getAnnotationDefinitionType() != AnnotationDefinitionType.URL) {
                    writeTableRow("Description", annotation.getDescription());
                    writeTableRow("Usage", annotation.getUsage());

                    List<String> domains =
                            summary.getAnnotationDomains().get(annotation);
                    if (hasItems(domains)) {
                        writer.write("<ul>");
                        for (String domainValue : domains) {
                            writer.write("<li>");
                            writer.write(domainValue);
                            writer.write("</li>");
                        }
                        writer.write("</ul>");
                    }
                }

                writer.write("</table>");
            }
            writeNewLine();
        }
    }
}
