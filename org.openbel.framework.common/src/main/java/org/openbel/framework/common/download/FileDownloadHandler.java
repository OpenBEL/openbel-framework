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
package org.openbel.framework.common.download;

import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.closeQuietly;
import static org.openbel.framework.common.BELUtilities.copy;
import static org.openbel.framework.common.BELUtilities.createDirectory;
import static org.openbel.framework.common.DownloadConstants.CONNECTION_TIMEOUT;
import static org.openbel.framework.common.DownloadConstants.DOWNLOAD_DIRECTORY;
import static org.openbel.framework.common.PathConstants.SHA256_EXTENSION;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import org.openbel.framework.common.InvalidArgument;

/**
 * FileProtocolHandler implements an {@link AbstractProtocolHandler} to
 * download a resource from a {@code file://} url.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class FileDownloadHandler implements DownloadHandler {

    @Override
    public DownloadFile download(URL url) throws IOException {
        if (url == null) {
            throw new InvalidArgument("url", url);
        }

        URLConnection fileUrlConnection = null;
        FileOutputStream fos = null;
        try {
            fileUrlConnection = url.openConnection();
            fileUrlConnection.setConnectTimeout(CONNECTION_TIMEOUT);

            final String downloadDir = asPath(
                    getSystemConfiguration().getWorkingDirectory()
                            .getAbsolutePath(),
                    DOWNLOAD_DIRECTORY);
            createDirectory(downloadDir);

            final String filename = UUID.randomUUID().toString();
            String downloadFile = asPath(downloadDir, filename);
            fos = new FileOutputStream(downloadFile);
            copy(fileUrlConnection.getInputStream(), fos);

            // attempt to download checksum
            String checksum = null;
            final String checksumLocation = url.toString() + SHA256_EXTENSION;
            URLConnection chksum = null;
            BufferedReader br = null;
            try {
                URL checksumUrl = new URL(checksumLocation);
                chksum = checksumUrl.openConnection();
                chksum.setConnectTimeout(CONNECTION_TIMEOUT);

                br = new BufferedReader(new InputStreamReader(
                        chksum.getInputStream()));
                checksum = br.readLine();
            } catch (IOException e) {
                // checksum is optional, so do not propogate exception
            } finally {
                // close reader
                closeQuietly(br);
            }

            return new DownloadFile(new File(downloadFile), checksum);
        } finally {
            // close URL and file output streams
            if (fileUrlConnection != null) {
                closeQuietly(fileUrlConnection.getInputStream());
            }
            closeQuietly(fos);
        }
    }
}
