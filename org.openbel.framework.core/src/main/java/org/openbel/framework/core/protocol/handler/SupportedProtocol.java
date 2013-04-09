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
package org.openbel.framework.core.protocol.handler;

/**
 * SupportedProtocol defines the url schemes supported by the
 * OpenBEL Framework.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public enum SupportedProtocol {
    FILE("file"),
    HTTP("http"),
    HTTPS("https"),
    FTP("ftp"),
    SFTP("sftp");

    /**
     * Defines the protocol scheme value.
     */
    private String scheme;

    /**
     * Creates the supported protocol using the protocol {@code scheme}.
     *
     * @param scheme {@link String}, the supported protocol scheme
     */
    private SupportedProtocol(String scheme) {
        this.scheme = scheme;
    }

    /**
     * Retrieves the protocol scheme.
     *
     * @return {@link String}, the protocol scheme, which cannot be null
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * Retrieve the SupportedProtocol for the protocol {@code scheme}.
     *
     * @param scheme {@link String}, the protocol scheme
     * @return {@link SupportedProtocol}, the supported protocol for the
     * protocol scheme, or null if the {@code scheme} did not match a
     * SupportedProtocol.
     */
    public static SupportedProtocol getByProtocolScheme(String scheme) {
        for (SupportedProtocol supportedProtocol : values()) {
            if (supportedProtocol.getScheme().equalsIgnoreCase(scheme)) {
                return supportedProtocol;
            }
        }

        return null;
    }
}
