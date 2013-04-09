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
package org.openbel.framework.tools.kamstore;

import static java.lang.String.format;
import static org.openbel.framework.tools.kamstore.KamComparator.DATA_LABELS;
import static org.openbel.framework.tools.kamstore.KamComparator.PRECISION;
import static org.openbel.framework.tools.kamstore.KamComparator.TOPOLOGY_LABELS;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class KamComparisonXHTMLWriter extends KamXHTMLWriter {

    public KamComparisonXHTMLWriter(OutputStreamWriter writer) {
        super("KAM Comparison", writer);
    }

    public void writeKamComparison(KamComparison cmp) throws IOException {
        final String[] kamNames = cmp.getKamNames();
        final Object[] scratch = new Object[kamNames.length];

        writer.write("<table>");
        writeTableHeader1("Topology", kamNames);
        writeMappedRow(cmp, TOPOLOGY_LABELS[0], "getKamNodeCount", kamNames,
                scratch);
        writeMappedRow(cmp, TOPOLOGY_LABELS[1], "getKamEdgeCount", kamNames,
                scratch);
        writeMappedRow(cmp, TOPOLOGY_LABELS[2], "getAverageKamNodeDegree",
                kamNames, scratch);
        writeMappedRow(cmp, TOPOLOGY_LABELS[3], "getAverageKamNodeInDegree",
                kamNames, scratch);
        writeMappedRow(cmp, TOPOLOGY_LABELS[4], "getAverageKamNodeOutDegree",
                kamNames, scratch);
        writeMappedRow(cmp, TOPOLOGY_LABELS[5], "getDensity", kamNames, scratch);
        writer.write("</table>");
        writeNewLine();

        writer.write("<table>");
        writeTableHeader1("Data", kamNames);
        writeMappedRow(cmp, DATA_LABELS[0], "getBELDocumentCount", kamNames,
                scratch);
        writeMappedRow(cmp, DATA_LABELS[1], "getNamespaceCount", kamNames,
                scratch);
        writeMappedRow(cmp, DATA_LABELS[2], "getAnnotationDefinitionCount",
                kamNames, scratch);
        writeMappedRow(cmp, DATA_LABELS[3], "getAnnotationCount", kamNames,
                scratch);
        writeMappedRow(cmp, DATA_LABELS[4], "getStatementCount", kamNames,
                scratch);
        writeMappedRow(cmp, DATA_LABELS[5], "getTermCount", kamNames, scratch);
        writeMappedRow(cmp, DATA_LABELS[6], "getParameterCount", kamNames,
                scratch);
        writeMappedRow(cmp, DATA_LABELS[7], "getUniqueParameterCount",
                kamNames, scratch);
        writer.write("</table>");
        writeNewLine();
        writeNewLine();
    }

    private void writeMappedRow(KamComparison cmp, String desc,
            String methodName, String[] arr, Object[] scratch)
            throws IOException {
        try {
            final Method f =
                    KamComparison.class.getDeclaredMethod(methodName,
                            String.class);

            final String floatingPointFormat = "%." + PRECISION + "f";

            for (int i = 0; i < arr.length; ++i) {
                final Object value = f.invoke(cmp, arr[i]);
                if (value instanceof Double) {
                    scratch[i] = format(floatingPointFormat, (Double) value);
                } else {
                    scratch[i] = value;
                }
            }
            writeTableRow1(desc, scratch);
        } catch (NoSuchMethodException e) {
            // Do nothing.
        } catch (SecurityException e) {
            // Do nothing.
        } catch (IllegalAccessException e) {
            // Do nothing.
        } catch (IllegalArgumentException e) {
            // Do nothing.
        } catch (InvocationTargetException e) {
            // Do nothing.
        }
    }
}
