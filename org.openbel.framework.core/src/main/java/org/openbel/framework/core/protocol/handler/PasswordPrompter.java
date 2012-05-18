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
