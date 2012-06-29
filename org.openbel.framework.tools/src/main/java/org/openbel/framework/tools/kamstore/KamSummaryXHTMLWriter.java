/**
 * Copyright (C) 2012 Selventa, Inc.
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
