/**
 * Copyright (C) 2012 Selventa, Inc.
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
package org.openbel.framework.core.protocol;

import java.io.File;

import org.openbel.framework.core.protocol.handler.FTPProtocolHandler;
import org.openbel.framework.core.protocol.handler.FileProtocolHandler;
import org.openbel.framework.core.protocol.handler.HttpProtocolHandler;
import org.openbel.framework.core.protocol.handler.HttpsProtocolHandler;
import org.openbel.framework.core.protocol.handler.ProtocolHandler;
import org.openbel.framework.core.protocol.handler.SftpProtocolHandler;
import org.openbel.framework.core.protocol.handler.SupportedProtocol;

/**
 * ResourceResolver delegates the handling of a resource location
 * to a {@link ProtocolHandler} based on the url scheme.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ResourceResolver {

    /**
     * Retrieves a resource from the {@code resourceLocation} and saves
     * it to {@code downloadLocation}.
     * 
     * @param resourceLocation {@link String}, the resource location url
     * @param downloadLocation {@link String}, the download location path
     * @return {@link File}, the local downloaded resource file
     * @throws ResourceDownloadError Thrown if an error occurred
     * downloading the resource
     */
    public File resolveResource(String resourceLocation,
            String downloadLocation) throws ResourceDownloadError {
        int indexOf = resourceLocation.indexOf(':');
        if (indexOf == -1) {
            final String url = downloadLocation;
            final String msg = "Unknown protocol";
            throw new ResourceDownloadError(url, msg);
        }

        String protocol = resourceLocation.substring(0, indexOf);
        SupportedProtocol supportedProtocol =
                SupportedProtocol.getByProtocolScheme(protocol);

        if (supportedProtocol == null) {
            final String url = downloadLocation;
            final String msg = "No protocol for " + protocol;
            throw new ResourceDownloadError(url, msg);
        }

        ProtocolHandler protocolHandler = null;
        switch (supportedProtocol) {
        case FILE:
            protocolHandler = new FileProtocolHandler();
            break;
        case HTTP:
            protocolHandler = new HttpProtocolHandler();
            break;
        case HTTPS:
            protocolHandler = new HttpsProtocolHandler();
            break;
        case SFTP:
            protocolHandler = new SftpProtocolHandler();
            break;
        case FTP:
            protocolHandler = new FTPProtocolHandler();
            break;
        default:
            final String url = downloadLocation;
            final String msg = "No case for " + supportedProtocol;
            throw new ResourceDownloadError(url, msg);
        }

        return protocolHandler.downloadResource(resourceLocation,
                downloadLocation);
    }
}
