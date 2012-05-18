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
