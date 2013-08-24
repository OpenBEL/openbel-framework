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
