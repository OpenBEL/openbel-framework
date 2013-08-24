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

import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * ProtocolHandler defines an object that understands how to read data from
 * a protocol.  The supported protocols by the OpenBEL Framework are:<ul>
 * <li>http</li>
 * <li>https</li>
 * <li>file</li>
 * <li>ftp</li>
 * <li>sftp</li>
 * </ul>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface ProtocolHandler {

    /**
     * Retrieves a resource, identified by {@code name},
     * from the {@code url}.
     *
     * @param url {@link String}, the resource name
     * @param path {@link String}, the output path
     * @return {@link File}, the downloaded resource file
     * @throws ResourceDownloadError - Thrown if there was an error
     * retrieving the resource from the {@code url}.
     */
    public File downloadResource(String url,
            String path) throws ResourceDownloadError;
}
