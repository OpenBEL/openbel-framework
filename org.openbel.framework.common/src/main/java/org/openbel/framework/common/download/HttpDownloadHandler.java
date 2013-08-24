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
package org.openbel.framework.common.download;

import static java.lang.String.format;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.openbel.framework.common.InvalidArgument;

public class HttpDownloadHandler implements DownloadHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public DownloadFile download(URL url) throws IOException {
        if (url == null) {
            throw new InvalidArgument("url", url);
        }

        final HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        FileOutputStream fos = null;
        try {
            huc.setConnectTimeout(CONNECTION_TIMEOUT);
            huc.setInstanceFollowRedirects(true);

            final int code = huc.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new IOException(format("Bad response code: %d", code));
            }

            final String downloadDir = asPath(
                    getSystemConfiguration().getWorkingDirectory()
                            .getAbsolutePath(),
                    DOWNLOAD_DIRECTORY);
            createDirectory(downloadDir);

            final String filename = UUID.randomUUID().toString();
            String downloadFile = asPath(downloadDir, filename);
            fos = new FileOutputStream(downloadFile);
            copy(huc.getInputStream(), fos);

            // attempt to download checksum
            String checksum = null;
            final String checksumLocation = url.toString() + SHA256_EXTENSION;
            HttpURLConnection chksum = null;
            BufferedReader br = null;
            try {
                URL checksumUrl = new URL(checksumLocation);
                chksum = (HttpURLConnection) checksumUrl.openConnection();
                chksum.setConnectTimeout(CONNECTION_TIMEOUT);
                chksum.setInstanceFollowRedirects(true);

                br = new BufferedReader(new InputStreamReader(
                        chksum.getInputStream()));
                checksum = br.readLine();
            } catch (IOException e) {
                // checksum is optional, so do not propogate exception
            } finally {
                closeQuietly(br);
            }

            return new DownloadFile(new File(downloadFile), checksum);
        } finally {
            if (huc != null) {
                closeQuietly(huc.getInputStream());
            }
            closeQuietly(fos);
        }
    }
}
