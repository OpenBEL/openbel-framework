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
