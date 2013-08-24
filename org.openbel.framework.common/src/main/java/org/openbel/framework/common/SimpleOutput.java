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
