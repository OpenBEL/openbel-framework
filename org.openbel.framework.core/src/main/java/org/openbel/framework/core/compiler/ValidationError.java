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
package org.openbel.framework.core.compiler;

import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.common.BELUtilities.sizedHashSet;

import java.util.Set;

import org.openbel.framework.common.BELErrorException;

/**
 * A BEL error indicating an error occurred validating XBEL XML content.
 */
public class ValidationError extends BELErrorException {
    private static final long serialVersionUID = -8771039993079525403L;
    private final int line;
    private final int column;

    final static Set<String> XERCES_ERROR_CODES;
    static {
        XERCES_ERROR_CODES = sizedHashSet(6);

        // Xerces general validation errors
        XERCES_ERROR_CODES.add("^cvc-.*: ");

        // Xerces validation constraint errors (3)
        XERCES_ERROR_CODES.add("^cos-.*: ");
        XERCES_ERROR_CODES.add("^derivation-.*: ");
        XERCES_ERROR_CODES.add("^rcase-.*: ");

        // Schema for schemas errors
        XERCES_ERROR_CODES.add("^s4s-.*: ");

        // Schema validation errors
        XERCES_ERROR_CODES.add("^src-.*: ");
    }

    /**
     * Creates a validation error for the provided resource {@code name},
     * with the supplied message, at the location specified by {@code line} and
     * {@code column}.
     *
     * @param name Resource name failing validation
     * @param msg Message indicative of validation failure
     * @param line Line location of failure
     * @param column Column location of failure
     */
    public ValidationError(String name, String msg, int line, int column) {
        super(name, translateValidationMessage(msg));
        this.line = line;
        this.column = column;
    }

    /**
     * Creates a validation error for the provided resource {@code name},
     * with the supplied message and cause, and at the location specified by
     * {@code line} and {@code column}.
     *
     * @param name Resource name failing validation
     * @param msg Message indicative of validation failure
     * @param cause The cause of the error
     * @param line Line location of failure
     * @param column Column location of failure
     */
    public ValidationError(String name, String msg, Throwable cause,
            int line, int column) {
        super(name, translateValidationMessage(msg), cause);
        this.line = line;
        this.column = column;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("VALIDATION ERROR");
        final String name = getName();
        if (name != null) {
            bldr.append(" in ");
            bldr.append(name);
        }

        bldr.append(":");
        bldr.append(line);
        bldr.append(":");
        bldr.append(column);
        bldr.append(": ");

        final String msg = getMessage();
        if (msg != null)
            bldr.append(msg);
        else
            bldr.append("Unknown");

        return bldr.toString();
    }

    private static String translateValidationMessage(String error) {
        if (noLength(error)) {
            return error;
        }

        for (final String code : XERCES_ERROR_CODES) {
            final String codeRE = code.concat(".*");
            if (error.matches(codeRE)) {
                // Strip the error code, returning only the error message.
                return error.replaceAll(code, "");
            }
        }
        return error;
    }
}
