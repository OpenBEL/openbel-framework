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
package org.openbel.framework.ws.utils;

import static java.lang.String.format;

import org.openbel.framework.api.KamElement;

/**
 * {@link InvalidIdException} represents an invalid id for a
 * {@link KamElement}.
 * 
 * <p>
 * This exception is useful when decoding and encoding ids and the id format
 * is invalid.
 * </p>
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class InvalidIdException extends Exception {
    private static final long serialVersionUID = -8513252613595602912L;

    /**
     * The message format string when id is invalid - {@value}
     */
    private static final String INVALID_MSG_FMT = "Invalid id '%s'";

    /**
     * The message for null ids - {@value}
     */
    private static final String NULL_MESSAGE = "Null id is invalid";

    /**
     * Construct the exception with a default message indicating the id was
     * <tt>null</tt>.  The message used is
     * {@link InvalidIdException#NULL_MESSAGE null msg}.
     */
    public InvalidIdException() {
        super(buildMessage(null));
    }

    /**
     * Construct the exception with the <tt>id</tt> value that failed to
     * decode.
     * 
     * <p>
     * The exception's message if constructed by
     * {@link InvalidIdException#buildMessage(String)} using the
     * {@link InvalidIdException#INVALID_MSG_FMT invalid id message format}.
     * </p>
     * 
     * @param id, {@link String} the id
     */
    public InvalidIdException(String id) {
        super(buildMessage(id));
    }

    /**
     * Build the exception's message using the <tt>id</tt> value.
     * 
     * @param id, {@link String} the id
     * @return the exception's message
     */
    private static String buildMessage(final String id) {
        if (id == null) {
            return NULL_MESSAGE;
        }

        return format(INVALID_MSG_FMT, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getMessage();
    }
}
