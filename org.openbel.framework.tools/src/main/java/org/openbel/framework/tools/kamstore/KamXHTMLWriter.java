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

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.openbel.framework.common.enums.BELFrameworkVersion;

public abstract class KamXHTMLWriter {

    protected final String header;
    protected final OutputStreamWriter writer;

    protected KamXHTMLWriter(String header, OutputStreamWriter writer) {
        this.header = header;
        this.writer = writer;
    }

    public void open() throws IOException {
        writeOpenHTML();
        writeHeader(header);
        writeOpenBody();
        writeCopyright();
    }

    public void close() throws IOException {
        writeCloseBody();
        writeCloseHTML();
        writer.flush();
        writer.close();
    }

    protected void writeNewLine() throws IOException {
        writer.write("<br/>");
    }

    protected void writeDiv(String s) throws IOException {
        writer.write("<div>" + s + "</div>");
    }

    protected void writeOpenHTML() throws IOException {
        writer.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\" \n"
                +
                "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
                +
                "   xsi:schemaLocation=\"http://www.w3.org/1999/xhtml \n" +
                "      http://www.w3.org/2002/08/xhtml/xhtml1-strict.xsd\">");
    }

    protected void writeCloseHTML() throws IOException {
        writer.write("</html>");
    }

    protected void writeOpenBody() throws IOException {
        writer.write("<body>");
    }

    protected void writeCloseBody() throws IOException {
        writer.write("</body>");
    }

    protected void writeHeader(String title) throws IOException {
        writer.write("<head><title>" + title + "</title></head>");
    }

    protected void writeTableHeader1(String value0, String... values)
            throws IOException {
        writer.write("<tr>");
        writer.write("<th>");
        writer.write(value0);
        writer.write("</th>");
        for (int i = 0; i < values.length; ++i) {
            writer.write("<th>");
            writer.write(values[i]);
            writer.write("</th>");
        }
        writer.write("</tr>");
    }

    protected void writeTableHeader(String... values) throws IOException {
        writer.write("<tr>");
        for (int i = 0; i < values.length; ++i) {
            writer.write("<th>");
            writer.write(values[i]);
            writer.write("</th>");
        }
        writer.write("</tr>");
    }

    protected void writeTableRow1(Object value0, Object... values)
            throws IOException {
        writer.write("<tr>");
        writer.write("<td>");
        writer.write(value0.toString());
        writer.write("</td>");
        for (int i = 0; i < values.length; i++) {
            writer.write("<td>");
            writer.write(values[i].toString());
            writer.write("</td>");
        }
        writer.write("</tr>");
    }

    protected void writeTableRow(Object... values) throws IOException {
        writer.write("<tr>");
        for (int i = 0; i < values.length; i++) {
            writer.write("<td>");
            writer.write(values[i].toString());
            writer.write("</td>");
        }
        writer.write("</tr>");
    }

    private void writeCopyright() throws IOException {
        writeDiv(BELFrameworkVersion.VERSION_LABEL + ": KAM Summarizer");
        writeDiv("Copyright (c) 2011-2012, Selventa. All Rights Reserved.");
        writeNewLine();
        writeNewLine();
    }
}
