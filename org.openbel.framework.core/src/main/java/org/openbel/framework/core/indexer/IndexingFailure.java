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
