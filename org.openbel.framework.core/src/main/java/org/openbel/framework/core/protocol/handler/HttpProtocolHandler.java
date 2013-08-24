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
import java.net.HttpURLConnection;
import java.net.URL;

import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * {@link HttpProtocolHandler} implements an
 * {@link AbstractHttpProtocolHandler} to download a resource from an
 * {@code http://} url.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class HttpProtocolHandler extends AbstractHttpProtocolHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public File downloadResource(String namespace,
            String namespaceDownloadLocation) throws ResourceDownloadError {
        try {
            HttpURLConnection httpUrlConnection =
                    (HttpURLConnection) new URL(namespace).openConnection();
            return downloadResource(httpUrlConnection,
                    namespaceDownloadLocation);
        } catch (IOException e) {
            final String url = namespaceDownloadLocation;
            final String msg = e.getMessage();
            throw new ResourceDownloadError(url, msg, e);
        }
    }
}
