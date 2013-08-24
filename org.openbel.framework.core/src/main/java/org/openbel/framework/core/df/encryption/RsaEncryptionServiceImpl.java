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

import org.openbel.framework.core.df.encryption.model.AsymmetricEncryptionKeyPair;

/**
 * Implementation of RSA public/public key encryption.
 *
 * @author tchu
 */
public class RsaEncryptionServiceImpl extends AbstractEncryptionService
        implements AsymmetricEncryptionService {

    //TODO implement

    @Override
    public AsymmetricEncryptionKeyPair generateRandomKey() {
        return null;
    }

    @Override
    public String decrypt(String base64EncodeMessage, String key,
            String iv) throws EncryptionServiceException {
        return null;
    }

    @Override
    public String decrypt(String base64EncodeMessage)
            throws EncryptionServiceException {
        return null;
    }

    @Override
    public String encrypt(String message, String key, String iv)
            throws EncryptionServiceException {
        return null;
    }

    @Override
    public String encrypt(String message)
            throws EncryptionServiceException {
        return null;
    }

    @Override
    public void decrypt(InputStream input, OutputStream output, String key,
            String iv) throws EncryptionServiceException {

    }

    @Override
    public void decrypt(InputStream input, OutputStream output)
            throws EncryptionServiceException {

    }

    @Override
    public void encrypt(InputStream input, OutputStream output, String key,
            String iv) throws EncryptionServiceException {

    }

    @Override
    public void encrypt(InputStream input, OutputStream output)
            throws EncryptionServiceException {

    }

    @Override
    public CipherOutputStream newEncryptingOutputStream(
            OutputStream outputStream) throws EncryptionServiceException {
        return null;
    }

    @Override
    public CipherOutputStream newEncryptingOutputStream(
            OutputStream outputStream, String key, String iv)
            throws EncryptionServiceException {
        return null;
    }

    @Override
    public CipherInputStream newDecryptingInputStream(InputStream inputStream)
            throws EncryptionServiceException {
        return null;
    }

    @Override
    public CipherInputStream newDecryptingInputStream(InputStream inputStream,
            String key, String iv) throws EncryptionServiceException {
        return null;
    }
}
