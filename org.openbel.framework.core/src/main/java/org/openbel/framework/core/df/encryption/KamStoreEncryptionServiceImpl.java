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
package org.openbel.framework.core.df.encryption;

import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import org.openbel.framework.common.cfg.SystemConfiguration;

/**
 * A simplified version of AES encryption. Encryption key is constructed from a passphrase. 
 * Initialization vector is not used for this implementation.
 * 
 */
public class KamStoreEncryptionServiceImpl extends AesEncryptionServiceImpl {

    private byte zerodInitializationVector[] =
            new byte[defaultInitializationVectorSize];

    @Override
    protected synchronized void populateSystemKey()
            throws EncryptionServiceException {
        try {
            SystemConfiguration systemConfig =
                    SystemConfiguration.createSystemConfiguration(null);
            if (getSystemIv() == null || getSystemKey() == null) {
                setSystemIv(getDefaultInitializationVector());
                setSystemKey(getEncryptionKeyFromPassphrase(systemConfig
                        .getKamstoreEncryptionPassphrase()));
            }

        } catch (Exception e) {
            throw new EncryptionServiceException(
                    "Unable to retrieve encryption key from system configuration.",
                    e);
        }
    }

    /**
     * construct a base64 encoded initialization vector a a byte array of all zeros.
     * @return
     */
    private String getDefaultInitializationVector() {
        return encodeBase64(zerodInitializationVector);
    }

    /**
     * construct a base64 encoded encryption key by XOR'ing passphrase into a byte array
     * corresponding to the encryption key length
     * @return
     */
    private String getEncryptionKeyFromPassphrase(String passphrase) {
        byte[] bytes = passphrase.getBytes();
        byte[] key = new byte[keyLength / 8]; //8 bits in a byte
        //initialize key
        for (int i = 0; i < key.length; i++) {
            key[i] = 0;
        }
        //chop passphrase into byte arrays corresponding to encryption key length
        //XOR them together to construct encryption key
        for (int i = 0; i < bytes.length; i++) {
            int position = i % key.length;
            key[position] = (byte) (key[position] ^ bytes[i]); //XOR
        }
        return encodeBase64(key);
    }

    /**
     * Creates a new Cipher input stream using the specified passphrase.
     * @param inputStream
     * @param passphrase
     * @return
     * @throws EncryptionServiceException
     */
    public CipherInputStream newDecryptingInputStream(InputStream inputStream,
            String passphrase) throws EncryptionServiceException {
        return super.newDecryptingInputStream(inputStream,
                getEncryptionKeyFromPassphrase(passphrase),
                getDefaultInitializationVector());
    }

    /**
     * Creates a new Cipher output stream using the specified passphrase.
     * @param outputStream
     * @param passphrase
     * @return
     * @throws EncryptionServiceException
     */
    public CipherOutputStream newEncryptingOutputStream(
            OutputStream outputStream, String passphrase)
            throws EncryptionServiceException {
        return super.newEncryptingOutputStream(outputStream,
                getEncryptionKeyFromPassphrase(passphrase),
                getDefaultInitializationVector());
    }

}
