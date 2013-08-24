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
package org.openbel.framework.common.protonetwork.model;

import org.openbel.framework.common.BELErrorException;

/**
 * A BEL error indicating a processing error occurred for a proto-network.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ProtoNetworkError extends BELErrorException {
    private static final long serialVersionUID = -5579624675405660490L;

    /**
     * Creates a proto-network error for the provided resource {@code name},
     * with the supplied message.
     *
     * @param name Resource name on which the failure occurred
     * @param msg {@link String}, the message
     */
    public ProtoNetworkError(String name, String msg) {
        super(name, msg);
    }

    /**
     * Creates a proto-network error for the provided resource {@code name}, the
     * supplied message, and cause.
     *
     * @param name Resource name on which the failure occurred
     * @param msg {@link String}, the message
     * @param cause {@link Throwable}, the cause
     */
    public ProtoNetworkError(String name, String msg, Throwable cause) {
        super(name, msg, cause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("PROTO-NETWORK ERROR");
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
}
