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
