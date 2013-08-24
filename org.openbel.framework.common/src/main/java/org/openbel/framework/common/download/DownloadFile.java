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

import static org.openbel.framework.common.BELUtilities.computeHashSHA256;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.openbel.framework.common.InvalidArgument;

/**
 * {@link DownloadFile} defines a downloaded {@link File file} with an optional
 * {@link String SHA-256 checksum value} to validate the downloaded cdata.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DownloadFile {
    private final File downloadedFile;
    private final String checksum;

    /**
     * Constructor taking a {@link File downloaded file} and a {@code null}
     * checksum.
     *
     * @param downloadedFile the {@link File downloaded file} which cannot be
     * {@code null}
     * @throws InvalidArgument Thrown if {@code downloadedFile} is {@code null}
     */
    protected DownloadFile(final File downloadedFile) {
        if (downloadedFile == null) {
            throw new InvalidArgument("downloadedFile", downloadedFile);
        }

        this.downloadedFile = downloadedFile;
        this.checksum = null;
    }

    /**
     * Constructor taking a {@link File downloaded file} and a
     * {@link String checksum value}.
     *
     * @param downloadedFile the {@link File downloaded file} which cannot be
     * {@code null}
     * @param checksum the SHA-256 {@link String checksum value} for the
     * downloaded file contents
     * @throws InvalidArgument Thrown if {@code downloadedFile} is {@code null}
     */
    protected DownloadFile(final File downloadedFile, final String checksum) {
        if (downloadedFile == null) {
            throw new InvalidArgument("downloadedFile", downloadedFile);
        }

        this.downloadedFile = downloadedFile;
        this.checksum = checksum;
    }

    /**
     * Validates the {@code downloadedFile}'s contents against a SHA-256
     * checksum provided with it.
     *
     * @return {@code true} if the computed checksum matches {@code checksum},
     * {@code false} if it does not match or if the {@code checksum} is
     * {@code null}.
     * @throws IOException Thrown if an IO error occurred while reading the
     * {@link File downloadedFile}.
     */
    public boolean validateContent() throws IOException {
        if (checksum == null) {
            return false;
        }

        final FileInputStream fis = new FileInputStream(downloadedFile);
        final String computedHash = computeHashSHA256(fis);

        return computedHash.equals(checksum);
    }
}
