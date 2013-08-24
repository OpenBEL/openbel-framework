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
 * Interface for warning/error reporting.
 * <p>
 * TODO improve class name
 * </p>
 *
 */
public interface Reportable {

    /**
     * Gets the error stream.
     *
     * @return {@link PrintStream}
     */
    public PrintStream errorStream();

    /**
     * Gets the output stream.
     *
     * @return {@link PrintStream}
     */
    public PrintStream outputStream();

    /**
     * Prints the errors to the error stream.
     *
     * @param errors Error messages
     */
    public void error(String... errors);

    /**
     * Prints the warnings to the error stream.
     *
     * @param warnings Warning messages
     */
    public void warning(String... warnings);

    /**
     *  Prints the messages to the output stream.
     *
     * @param outputs Messages
     */
    public void output(String... outputs);

}
