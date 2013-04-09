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
