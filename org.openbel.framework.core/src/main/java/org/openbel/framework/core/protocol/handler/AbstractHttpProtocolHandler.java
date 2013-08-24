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
import java.net.HttpURLConnection;
import java.net.URLConnection;

import org.openbel.framework.core.protocol.ProtocolHandlerConstants;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * {@link AbstractHttpProtocolHandler} provides boiler-plate IO to read
 * from an {@link HttpURLConnection} and write to a {@link File}.  This
 * implementation is applicable to both an <em><strong>http</strong></em>
 * and <em><strong>https</strong></em> resource.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public abstract class AbstractHttpProtocolHandler extends
        AbstractProtocolHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    protected File downloadResource(URLConnection urlConnection,
            String namespaceDownloadLocation)
            throws ResourceDownloadError {
        HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
        try {
            httpUrlConnection
                    .setConnectTimeout(ProtocolHandlerConstants.CONNECTION_TIMEOUT);
            httpUrlConnection.setInstanceFollowRedirects(true);

            final int code = httpUrlConnection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                final String url = urlConnection.getURL().toString();
                final String msg = "Bad response code: " + code;
                throw new ResourceDownloadError(url, msg);
            }

            return super.downloadResource(httpUrlConnection,
                    namespaceDownloadLocation);
        } catch (InterruptedIOException e) {
            final String url = urlConnection.getURL().toString();
            final String msg = "Interrupted I/O";
            throw new ResourceDownloadError(url, msg, e);
        } catch (IOException e) {
            final String url = urlConnection.getURL().toString();
            final String msg = e.getMessage();
            throw new ResourceDownloadError(url, msg, e);
        }
    }
}
