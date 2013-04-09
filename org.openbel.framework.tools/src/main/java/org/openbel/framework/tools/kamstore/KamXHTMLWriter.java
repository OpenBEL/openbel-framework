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
