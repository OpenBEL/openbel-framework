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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * FileProtocolHandler implements an {@link ProtocolHandler} to
 * download a resource from a {@code ftp://} url.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class FTPProtocolHandler implements ProtocolHandler {
    /**
     * Defines the ftp client connector to use so establish an ftp
     * session and download a file via ftp.
     */
    private FTPConnector ftpConnector;

    /**
     * Creates the ftp protocol handler.
     */
    public FTPProtocolHandler() {
        this.ftpConnector = new FTPConnector();
    }

    /**
     * Creates the ftp protocol handler using an input stream to read
     * the user's password from.
     *
     * @param pwdInputStream {@link InputStream}, the input stream
     * to read the user's password from
     */
    public FTPProtocolHandler(InputStream pwdInputStream) {
        this.ftpConnector = new FTPConnector(pwdInputStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File downloadResource(String namespace,
            String namespaceDownloadLocation) throws ResourceDownloadError {
        try {
            URL ftpNamespaceUrl = new URL(namespace);
            ftpConnector.connectAndLogin(ftpNamespaceUrl);

            String filePath = ftpNamespaceUrl.getFile().substring(1); //remove leading '/' since it's merely a delimiter in the ftp url specification.
            ftpConnector.retrieveFile(filePath, namespaceDownloadLocation);

            return new File(namespaceDownloadLocation);
        } catch (UnknownHostException e) {
            final String url = namespaceDownloadLocation;
            final String msg = e.getMessage();
            throw new ResourceDownloadError(url, msg, e);
        } catch (MalformedURLException e) {
            final String url = namespaceDownloadLocation;
            final String msg = e.getMessage();
            throw new ResourceDownloadError(url, msg, e);
        } catch (IOException e) {
            final String url = namespaceDownloadLocation;
            final String msg = e.getMessage();
            throw new ResourceDownloadError(url, msg, e);
        }
    }
}
