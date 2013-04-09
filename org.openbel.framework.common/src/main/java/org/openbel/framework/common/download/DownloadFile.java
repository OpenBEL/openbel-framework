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
