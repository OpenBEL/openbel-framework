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
package org.openbel.framework.common;

import java.io.PrintStream;

/**
 * Prints errors and warnings via direct hand-off to the underlying streams.
 * <p>
 * <ol>
 * <li>Warnings will be printed with the line prefix {@code WARNING: }.</li>
 * <li>Errors will be printed with the line prefix {@code ERROR: }.</li>
 * </ol>
 * </p>
 *
 */
public final class SimpleOutput implements Reportable {

    /* Output stream - may be null. */
    private PrintStream outputstream;
    /* Error stream - may be null. */
    private PrintStream errorstream;

    /**
     * Creates a simple output.
     */
    public SimpleOutput() {
    }

    /**
     * Sets the stream that receives normal output.
     *
     * @param stream {@link PrintStream}
     */
    public void setOutputStream(PrintStream stream) {
        this.outputstream = stream;
    }

    /**
     * Sets the stream that receives errors and warnings.
     *
     * @param stream {@link PrintStream}
     */
    public void setErrorStream(final PrintStream stream) {
        this.errorstream = stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrintStream errorStream() {
        return errorstream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrintStream outputStream() {
        return outputstream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String... errors) {
        if (errors == null) return;
        if (errorstream == null) return;

        for (final String err : errors) {
            if (err == null) continue;
            errorstream.println(err);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warning(String... warnings) {
        if (warnings == null) return;
        if (errorstream == null) return;

        for (final String warning : warnings) {
            if (warning == null) continue;
            errorstream.println(warning);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void output(String... outputs) {
        if (outputs == null) return;
        if (outputstream == null) return;

        for (final String output : outputs) {
            if (output == null) continue;
            outputstream.println(output);
        }
    }

}
