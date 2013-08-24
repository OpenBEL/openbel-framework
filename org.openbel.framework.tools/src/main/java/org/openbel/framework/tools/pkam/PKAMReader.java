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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import javax.crypto.CipherInputStream;

import org.openbel.framework.core.df.encryption.EncryptionServiceException;
import org.openbel.framework.core.df.encryption.KamStoreEncryptionServiceImpl;

/**
 * PKAMReader provides a {@link BufferedReader} that is created from
 * a series of other {@link InputStream}s.  Data will be processed sequentially
 * by the following streams:<ol>
 * <li>{@link FileInputStream} to read file data</li>
 * <li>{@link CipherInputStream} to decrypt data read from file</li>
 * <li>{@link GZIPInputStream} to uncompress decrypted data</li>
 * <li>{@link BufferedReader}, <tt>this</tt>, to read data line-by-line.</li></ol>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
class PKAMReader extends BufferedReader {

    /**
     * Construct a PKAMReader from a PKAM stored in <tt>fileName</tt>.   This
     * reader does decryption thus it needs the
     * {@link KamStoreEncryptionServiceImpl} and a <tt>password</tt>.
     *
     * @param fileName {@link String}, the PKAM file name
     * @param password {@link String}, the password to build the decryption
     * cipher from
     * @param encryptionService {@link KamStoreEncryptionServiceImpl}, the encryption
     * service used to decrypt
     * @throws EncryptionServiceException Thrown if an encryption error
     * occurred during decryption
     * @throws IOException Thrown if an IO error occurred establishing the
     * sequential {@link InputStream}s
     */
    public PKAMReader(final String fileName, final String password,
            final KamStoreEncryptionServiceImpl encryptionService)
            throws EncryptionServiceException, IOException {
        super(new InputStreamReader(
                new GZIPInputStream(encryptionService.newDecryptingInputStream(
                        new FileInputStream(fileName), password))));
    }

    /**
     * Construct a PKAMReader from a PKAM stored in <tt>fileName</tt>  This
     * writer does not decrypt since it assumes unencrypted data.
     *
     * @param fileName {@link String}, the PKAM file name
     * @throws IOException Thrown if an IO error occurred establishing the
     * sequential {@link InputStream}s
     */
    public PKAMReader(final String fileName) throws IOException {
        super(new InputStreamReader(new GZIPInputStream(new FileInputStream(
                fileName))));
    }
}
