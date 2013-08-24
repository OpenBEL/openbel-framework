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
package org.openbel.framework.tools.pkam;

import org.openbel.framework.common.BELFatalException;

/**
 * PKAMSerializationFailure defines a {@link BELFatalException} that represents
 * a failure to serialize a particular KAM.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class PKAMSerializationFailure extends BELFatalException {
    private static final long serialVersionUID = 7235616337826361463L;

    /**
     * Constructs the exception with kam name and error message.
     *
     * @param kamName {@link String}, the kam name
     * @param errorMsg {@link String}, the error message
     */
    public PKAMSerializationFailure(final String kamName,
            final String errorMsg) {
        super(kamName, errorMsg);
    }

    /**
     * Constructs the exception with kam name, error message, and cause.
     *
     * @param kamName {@link String}, the kam name
     * @param errorMsg {@link String}, the error message
     * @param cause {@link Throwable}, the cause of this failure
     */
    public PKAMSerializationFailure(final String kamName,
            final String errorMsg,
            final Throwable cause) {
        super(kamName, errorMsg, cause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("PKAM Serialization Failure");

        final String name = getName();
        if (name != null) {
            bldr.append(" with kam name '");
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
