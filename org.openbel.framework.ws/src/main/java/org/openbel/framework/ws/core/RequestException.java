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
