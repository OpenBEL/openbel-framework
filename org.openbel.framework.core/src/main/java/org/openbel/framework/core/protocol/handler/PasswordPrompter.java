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
package org.openbel.framework.core.protocol.handler;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.openbel.framework.common.InvalidArgument;

/**
 * PasswordPrompter provides a utility to retrieve a password from
 * an input stream.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
class PasswordPrompter {

    /**
     * Retrieve a password in the {@link InputStream} with an optional
     * {@code prompt} message if reading from {@link System#in}.
     *
     * @param in {@link InputStream}, the input stream to retrieve the
     * password from, which cannot be null
     * @param prompt {@link String}, the message to prompt, which will
     * only be used if {@link InputStream} equals {@link System#in}.
     * @return {@code char[]}, the password characters
     * @throws IOException - Thrown if an io error occurred reading the
     * password from the {@link InputStream}.
     */
    public static final char[] getPassword(InputStream in, String prompt)
            throws IOException {
        if (in == null) {
            throw new InvalidArgument("in is null");
        }

        char[] pwd;
        if (in.equals(System.in)) {
            System.out.print(prompt);

            Console console = System.console();

            if (console == null) {
                throw new IOException(
                        "Cannot obtain the console with which to read the password from.");
            }

            pwd = console.readPassword();
        } else {
            BufferedReader pwdReader =
                    new BufferedReader(new InputStreamReader(in));
            String buffer = pwdReader.readLine();
            if (buffer == null) return null;
            pwd = buffer.toCharArray();
            pwdReader.close();
        }

        return pwd;
    }
}
