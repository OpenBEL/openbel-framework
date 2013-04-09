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
package org.openbel.framework.core.indexer;

import static org.openbel.framework.common.BELUtilities.getFirstCause;
import static org.openbel.framework.common.BELUtilities.getFirstMessage;

import org.openbel.framework.common.BELFatalException;

/**
 * A BEL fatal exception indicating a failure before namespace indexing.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class IndexingFailure extends BELFatalException {
    private static final long serialVersionUID = 7468151126377501640L;
    private static final String DEFAULT_MSG =
            "Failed to process resource '%s'.";

    public IndexingFailure(String name) {
        super(name, buildMessage(name, null, null));
    }

    /**
     * Creates an indexing failure for the provided resource {@code name}, with
     * the supplied message.
     *
     * @param name Resource name failing validation
     * @param message Message indicative of indexing failure
     */
    public IndexingFailure(String name, String message) {
        super(name, buildMessage(name, message, null));
    }

    public IndexingFailure(String name, Throwable cause) {
        super(name, buildMessage(name, null, cause), cause);
    }

    /**
     * Creates an indexing failure for the provided resource {@code name}, with
     * the supplied message and cause.
     *
     * @param name Resource name failing validation
     * @param message Message indicative of indexing failure
     * @param cause The cause of the exception
     */
    public IndexingFailure(String name, String message, Throwable cause) {
        super(name, buildMessage(name, message, cause), cause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("INDEXING FAILURE");
        final String name = getName();
        if (name != null) {
            bldr.append(" in ");
            bldr.append(name);
        }
        bldr.append("\n\treason: ");

        final String msg = getMessage();
        if (msg != null)
            bldr.append(msg);
        else
            bldr.append("Unknown");

        bldr.append("\n");

        return bldr.toString();
    }

    private static String
            buildMessage(String name, String msg, Throwable cause) {

        final StringBuilder bldr = new StringBuilder();
        if (msg != null) {
            bldr.append(msg);
        } else {
            bldr.append(String.format(DEFAULT_MSG, name));
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
}
