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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Implementation of AES encryption. Uses CTR mode and PKCS5Padding. Defaults to
 * use 128-bit encryption key.
 *
 * @author tchu
 *
 */

public class AesEncryptionServiceImpl extends AbstractEncryptionService
        implements SymmetricEncryptionService {

    protected String algorithm = "AES";
    protected String mode = "CTR";
    protected String padding = "PKCS5Padding";

    protected int keyLength = 128;

    private String systemKey;
    private String systemIv;

    protected String getSystemKey() {
        return systemKey;
    }

    protected void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }

    protected String getSystemIv() {
        return systemIv;
    }

    protected void setSystemIv(String systemIv) {
        this.systemIv = systemIv;
    }

    protected synchronized void populateSystemKey()
            throws EncryptionServiceException {
        try {
            if (this.systemKey == null || this.systemIv == null) {
                //TODO: populate encryption key and IV
                //SystemConfiguration systemConfig = SystemConfiguration.createSystemConfiguration(null);
            }
        } catch (Exception e) {
            throw new EncryptionServiceException(
                    "Unable to retrieve encryption key from system configuration.",
                    e);
        }
    }

    /* (non-Javadoc)
     * @see org.openbel.framework.core.encryption.SymmetricEncryptionService#generateRandomKey()
     */
    @Override
    public String generateRandomKey() throws EncryptionServiceException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(keyLength);
            return encodeBase64(keyGenerator.generateKey().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionServiceException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.openbel.framework.core.encryption.EncryptionService#decrypt(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String decrypt(String base64EncodedMessage, String key,
            String iv) throws EncryptionServiceException {
        byte[] decrypted =
                processCipher(Cipher.DECRYPT_MODE,
                        decodeBase64(base64EncodedMessage), key, iv);
        return new String(decrypted);
    }

    /* (non-Javadoc)
     * @see org.openbel.framework.core.encryption.EncryptionService#decrypt(java.lang.String)
     */
    @Override
    public String decrypt(String base64EncodedMessage)
            throws EncryptionServiceException {
        return decrypt(base64EncodedMessage, null, null);
    }

    /* (non-Javadoc)
     * @see org.openbel.framework.core.encryption.EncryptionService#encrypt(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String encrypt(String message, String key, String iv)
            throws EncryptionServiceException {
        byte[] encrypted =
                processCipher(Cipher.ENCRYPT_MODE, message.getBytes(), key, iv);
        return encodeBase64(encrypted);
    }

    /* (non-Javadoc)
     * @see org.openbel.framework.core.encryption.EncryptionService#encrypt(java.lang.String)
     */
    @Override
    public String encrypt(String message)
            throws EncryptionServiceException {
        return encrypt(message, null, null);
    }

    /* (non-Javadoc)
     * @see org.openbel.framework.core.encryption.EncryptionService#decrypt(java.io.InputStream, java.io.OutputStream, java.lang.String, java.lang.String)
     */
    @Override
    public void decrypt(InputStream input, OutputStream output, String key,
            String iv) throws EncryptionServiceException {
        processCipher(Cipher.DECRYPT_MODE, input, output, key, iv);

    }

    /* (non-Javadoc)
     * @see org.openbel.framework.core.encryption.EncryptionService#decrypt(java.io.InputStream, java.io.OutputStream)
     */
    @Override
    public void decrypt(InputStream input, OutputStream output)
            throws EncryptionServiceException {
        processCipher(Cipher.DECRYPT_MODE, input, output, null, null);
    }

    /* (non-Javadoc)
     * @see org.openbel.framework.core.encryption.EncryptionService#encrypt(java.io.InputStream, java.io.OutputStream, java.lang.String, java.lang.String)
     */
    @Override
    public void encrypt(InputStream input, OutputStream output, String key,
            String iv) throws EncryptionServiceException {
        processCipher(Cipher.ENCRYPT_MODE, input, output, key, iv);
    }

    /* (non-Javadoc)
     * @see org.openbel.framework.core.encryption.EncryptionService#encrypt(java.io.InputStream, java.io.OutputStream)
     */
    @Override
    public void encrypt(InputStream input, OutputStream output)
            throws EncryptionServiceException {
        processCipher(Cipher.ENCRYPT_MODE, input, output, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CipherOutputStream newEncryptingOutputStream(
            final OutputStream outputStream) throws EncryptionServiceException {
        Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, null, null);
        return new CipherOutputStream(outputStream, cipher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CipherOutputStream newEncryptingOutputStream(
            final OutputStream outputStream, final String key, final String iv)
            throws EncryptionServiceException {
        Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, key, iv);
        return new CipherOutputStream(outputStream, cipher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CipherInputStream newDecryptingInputStream(
            final InputStream inputStream) throws EncryptionServiceException {
        Cipher cipher = createCipher(Cipher.DECRYPT_MODE, null, null);
        return new CipherInputStream(inputStream, cipher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CipherInputStream newDecryptingInputStream(
            final InputStream inputStream, final String key, final String iv)
            throws EncryptionServiceException {
        Cipher cipher = createCipher(Cipher.DECRYPT_MODE, key, iv);
        return new CipherInputStream(inputStream, cipher);
    }

    /**
     * Encrypts or decrypts a byte array.
     * @param cipherMode
     * @param input
     * @param key
     * @param iv
     * @return Processed byte array
     * @throws EncryptionServiceException
     */
    protected byte[] processCipher(int cipherMode, byte[] input, String key,
            String iv) throws EncryptionServiceException {

        byte[] message;

        if (input == null) {
            throw new EncryptionServiceException(
                    "Message to encrypt cannot be null.");
        }

        try {
            Cipher cipher = createCipher(cipherMode, key, iv);
            message = cipher.doFinal(input);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptionServiceException(e);
        } catch (BadPaddingException e) {
            throw new EncryptionServiceException(e);
        }
        return message;
    }

    /**
     * Encrypts or decrypts a input stream.
     * @param cipherMode
     * @param input
     * @param output
     * @param key
     * @param iv
     * @throws EncryptionServiceException
     */
    protected void processCipher(int cipherMode, InputStream input,
            OutputStream output, String key, String iv)
            throws EncryptionServiceException {

        if (input == null) {
            throw new EncryptionServiceException(
                    "Message to encrypt cannot be null.");
        }

        InputStream in = input;
        OutputStream out = output;

        try {
            Cipher cipher = createCipher(cipherMode, key, iv);
            byte[] data = new byte[1024]; //read 1024 bytes at a time

            switch (cipherMode) {
            case Cipher.DECRYPT_MODE:
                in = new CipherInputStream(input, cipher); //decrypt as we read
                break;
            case Cipher.ENCRYPT_MODE:
                out = new CipherOutputStream(output, cipher); //encrypt as we write
                break;
            default:
                throw new EncryptionServiceException(
                        "Cipher mode not supported: " + cipherMode);
            }
            int byteCounter = 0;
            do { //read input stream till end-of-line
                byteCounter = in.read(data);
                if (byteCounter > 0) {
                    out.write(data, 0, byteCounter);
                }
            } while (byteCounter > 0);
            out.flush();
        } catch (IOException e) {
            throw new EncryptionServiceException(e);
        }
    }

    protected Cipher createCipher(int cipherMode, String pkey, String piv)
            throws EncryptionServiceException {
        String iv = piv;
        String key = pkey;

        //use system iv and key if not specific iv and key are provided
        if (iv == null) {
            if (systemIv == null) {
                populateSystemKey();
            }
            iv = systemIv;
        }
        if (key == null) {
            if (systemKey == null) {
                populateSystemKey();
            }
            key = systemKey;
        }

        //require both iv and key for AES encryption
        if (iv == null || key == null) {
            throw new EncryptionServiceException(
                    "Encryption key and initialization vector are not set properly.");
        }

        try {
            SecretKeySpec secret =
                    new SecretKeySpec(decodeBase64(key), algorithm);
            Cipher cipher =
                    Cipher.getInstance(algorithm + "/" + mode + "/" + padding); //cipher transformation
            cipher.init(cipherMode, secret, new IvParameterSpec(
                    decodeBase64(iv))); //clear all previous specs as well
            return cipher;
        } catch (InvalidKeyException e) {
            throw new EncryptionServiceException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionServiceException(e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptionServiceException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new EncryptionServiceException(e);
        }
    }

}
