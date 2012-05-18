/**
 * Copyright (C) 2012 Selventa, Inc.
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
