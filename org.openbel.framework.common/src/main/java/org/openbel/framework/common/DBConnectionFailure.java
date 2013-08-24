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
package org.openbel.framework.common;

import org.openbel.framework.common.BELFatalException;

/**
 * DBConnectionFailure represents a fatal exception when a connection cannot
 * be made to either the DocStore or KAMStore.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DBConnectionFailure extends BELFatalException {
    private static final long serialVersionUID = -3235727792941839683L;

    /**
     * Constructs the exception with url, message, and cause.
     *
     * @param url {@link String}, the kam jdbc url
     * @param errorMsg {@link String}, the error message
     * @param cause {@link Throwable}, the exception's cause
     */
    public DBConnectionFailure(String url, String errorMsg) {
        super(url, errorMsg);
    }

    /**
     * Constructs the exception with url, message, and cause.
     *
     * @param url {@link String}, the kam jdbc url
     * @param errorMsg {@link String}, the error message
     * @param cause {@link Throwable}, the exception's cause
     */
    public DBConnectionFailure(String url, String errorMsg,
            Throwable cause) {
        super(url, errorMsg, cause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("DB CONNECTION FAILURE");

        final String name = getName();
        if (name != null) {
            bldr.append(" using url '");
            bldr.append(name);
            bldr.append("'");
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
}
