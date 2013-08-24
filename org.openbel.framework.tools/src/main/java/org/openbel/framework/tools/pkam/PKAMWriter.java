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
package org.openbel.framework.tools.pkam;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;

import javax.crypto.CipherOutputStream;

import org.openbel.framework.core.df.encryption.EncryptionServiceException;
import org.openbel.framework.core.df.encryption.KamStoreEncryptionServiceImpl;

/**
 * PKAMWriter provides a {@link BufferedWriter} that is created from
 * a series of other {@link OutputStream}s.  Data will be processed sequentially
 * by the following streams:<ol>
 * <li>{@link BufferedWriter}, <tt>this</tt>, to write initial data</li>
 * <li>{@link GZIPOutputStream} to compress initial written data</li>
 * <li>{@link CipherOutputStream} to encrypt compressed data</li>
 * <li>{@link FileOutputStream} to write encrypted data to a file</li></ol>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
class PKAMWriter extends BufferedWriter {

    /**
     * Construct a PKAMWriter that writes a PKAM to <tt>fileName</tt>.  This
     * writer does encryption thus it needs the
     * {@link KamStoreEncryptionServiceImpl} and a <tt>password</tt>.
     *
     * @param fileName {@link String}, the PKAM file name
     * @param password {@link String}, the password to build the encryption
     * cipher from
     * @param encryptionService {@link KamStoreEncryptionServiceImpl}, the encryption
     * service used to encrypt
     * @throws EncryptionServiceException Thrown if an encryption error
     * occurred during encryption
     * @throws IOException Thrown if an IO error occurred establishing the
     * sequential {@link OutputStream}s
     */
    public PKAMWriter(final String fileName, final String password,
            final KamStoreEncryptionServiceImpl encryptionService)
            throws IOException,
            EncryptionServiceException {
        super(new OutputStreamWriter(new GZIPOutputStream(encryptionService
                .newEncryptingOutputStream(new FileOutputStream(fileName),
                        password))));
    }

    /**
     * Construct a PKAMWriter that writes a PKAM to <tt>fileName</tt>  This
     * writer does not encrypt.
     *
     * @param fileName {@link String}, the PKAM file name
     * @throws IOException Thrown if an IO error occurred establishing the
     * sequential {@link OutputStream}s
     */
    public PKAMWriter(final String fileName) throws IOException {
        super(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(
                fileName))));
    }
}
