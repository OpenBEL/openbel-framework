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
