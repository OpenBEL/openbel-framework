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
package org.openbel.framework.ws.core;

import static org.openbel.framework.common.BELUtilities.getFirstCause;
import static org.openbel.framework.common.BELUtilities.getFirstMessage;
import static org.openbel.framework.common.BELUtilities.noLength;

import org.openbel.framework.common.InvalidArgument;

/**
 * Web service request exception indicating an error occurred in fulfilling a
 * request.
 * <p>
 * Under most conditions, the {@link #buildMessage(String, String)} produces
 * exception messages of the form
 * {@code "Exception Message (reason: Root Cause Exception Message)"}
 * </p>
 */
public class RequestException extends Exception {
    private static final long serialVersionUID = 4861329484953507068L;

    /**
     * Construct the request exception providing the request message.  This
     * constructor should be used when the cause of this exception is not
     * known.
     * <p>
     * This constructor should only be used when the calling method is
     * guaranteed to be the web service endpoint. It uses the thread's stack to
     * determine the calling method's name.
     * </p>
     * <p>
     * If the cause of this exception is known, use
     * {@link RequestException#RequestException(String, Throwable) the
     * appropriate constructor}.
     * </p>
     *
     * @param msg {@link String}, the request message
     */
    public RequestException(final String msg) {
        super(buildMessage(msg, null));
        if (noLength(msg)) {
            throw new InvalidArgument("msg", msg);
        }
    }

    /**
     * Construct the request exception providing the request message and the
     * cause of this exception.
     *
     * @param msg {@link String}, the request message
     * @param cause {@link Throwable}, the cause of this exception
     */
    public RequestException(final String msg, final Throwable cause) {
        super(buildMessage(msg, cause));
        if (noLength(msg)) {
            throw new InvalidArgument("msg", msg);
        }
    }

    /**
     * Static method to build request exception message using a message and an
     * underlying {@link Throwable cause}.
     *
     * @param msg {@link String}, the message
     * @param cause {@link Throwable}, the underlying {@link Throwable cause}
     * @return the full request exception message
     */
    private static String buildMessage(String msg, Throwable cause) {

        final StringBuilder bldr = new StringBuilder();
        if (msg != null) {
            bldr.append(msg);
        }

        if (cause != null) {
            bldr.append(" (reason: ");
            String reason = getFirstMessage(cause);
            if (reason == null) {
                reason = getFirstCause(cause).getClass().getSimpleName();
            }
            bldr.append(reason);
            bldr.append(")");
        }

        return bldr.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getMessage();
    }
}
