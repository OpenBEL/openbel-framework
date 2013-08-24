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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openbel.framework.core.protocol.ProtocolHandlerConstants;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * AbstractProtocolHandler provides boiler-plate IO to read from a
 * {@link InputStream} and write to a {@link File}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public abstract class AbstractProtocolHandler implements ProtocolHandler {

    /**
     * Retrieves a resource from the {@code urlc} and save it to
     * {@code downloadLocation}.
     *
     * @param urlc {@link URLConnection}, the url connection
     * @param downloadLocation {@link String}, the location to save the
     * resource content to
     * @throws IOException Thrown if an io error occurred downloading the
     * resource
     * @throws ResourceDownloadError Thrown if there was an I/O error
     * downloading the resource
     */
    protected File downloadResource(URLConnection urlc,
            String downloadLocation) throws ResourceDownloadError {
        if (!urlc.getDoInput()) {
            urlc.setDoInput(true);
        }

        File downloadFile = new File(downloadLocation);
        try {
            File downloaded = File.createTempFile(
                    ProtocolHandlerConstants.BEL_FRAMEWORK_TMP_FILE_PREFIX,
                    null);

            IOUtils.copy(
                    urlc.getInputStream(),
                    new FileOutputStream(downloaded));

            FileUtils.copyFile(downloaded, downloadFile);

            // delete temp file holding download
            if (!downloaded.delete()) {
                downloaded.deleteOnExit();
            }
        } catch (IOException e) {
            final String url = urlc.getURL().toString();
            final String msg = "I/O error";
            throw new ResourceDownloadError(url, msg, e);
        }

        return downloadFile;
    }
}
