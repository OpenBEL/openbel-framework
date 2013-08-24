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
