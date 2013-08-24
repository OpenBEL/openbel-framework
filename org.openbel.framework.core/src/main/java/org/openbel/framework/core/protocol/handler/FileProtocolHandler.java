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

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import java.net.URLConnection;

import org.openbel.framework.core.protocol.ProtocolHandlerConstants;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * FileProtocolHandler implements an {@link AbstractProtocolHandler} to
 * download a resource from a {@code file://} url.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class FileProtocolHandler extends AbstractProtocolHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public File downloadResource(String namespace,
            String namespaceDownloadLocation) throws ResourceDownloadError {
        URLConnection fileUrlConnection;
        try {
            fileUrlConnection = new URL(namespace).openConnection();
            fileUrlConnection
                    .setConnectTimeout(ProtocolHandlerConstants.CONNECTION_TIMEOUT);

            return downloadResource(fileUrlConnection,
                    namespaceDownloadLocation);
        } catch (InterruptedIOException e) {
            final String url = namespaceDownloadLocation;
            final String msg = "Interrupted I/O";
            throw new ResourceDownloadError(url, msg, e);
        } catch (IOException e) {
            final String url = namespaceDownloadLocation;
            final String msg = e.getMessage();
            throw new ResourceDownloadError(url, msg, e);
        }
    }
}
