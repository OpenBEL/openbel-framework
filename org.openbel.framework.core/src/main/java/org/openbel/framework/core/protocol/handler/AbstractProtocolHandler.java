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
