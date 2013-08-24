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
