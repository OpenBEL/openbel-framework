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
