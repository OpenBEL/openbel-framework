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
package org.openbel.framework.common.download;

import java.net.URL;

import org.openbel.framework.common.BELErrorException;

/**
 * A BEL error indicating download of a resource resulted in an error.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DownloadError extends BELErrorException {
    private static final long serialVersionUID = -4975355022812364873L;

    /**
     * Creates a resource download error for the provided resource
     * {@code resourceLocation}, with the supplied message.
     *
     * @param url the {@link URL url} that failed to download
     * @param msg Message indicative of failure
     */
    public DownloadError(URL url, String msg) {
        super(url.toString(), msg);
    }

    /**
     * Creates a resource download error for the provided resource
     * {@code resourceLocation}, the supplied message, and cause.
     *
     * @param url the {@link URL url} that failed to download
     * @param msg Message indicative of failure
     * @param cause The cause of the exception
     */
    public DownloadError(URL url, String msg, Throwable cause) {
        super(url.toString(), msg, cause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("ERROR DOWNLOADING FILE");
        final String name = getName();
        if (name != null) {
            bldr.append(" for ");
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
