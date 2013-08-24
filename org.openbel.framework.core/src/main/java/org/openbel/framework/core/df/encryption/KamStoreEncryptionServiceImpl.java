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
