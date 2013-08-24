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

/**
 * Provides basic encryption support for both asymmetric and symmetric encryption
 * used in the BEL framework.
 *
 * @author tchu
 *
 */
public interface EncryptionService {

    /**
     * Generates a initialization vector (IV) for encryption blocks.
     *
     * @return String
     */
    public String generateInitializationVector();

    /**
     * Encrypts a message with the specified key and base64 encode the encrypted message.
     * @param message	the string to be encrypted
     * @param key	key used to encrypt the message
     * @param iv	initialization vector to use
     * @return	base64 encoded encrypted message
     * @throws EncryptionServiceException
     */
    public String encrypt(String message, String key, String iv)
            throws EncryptionServiceException;

    /**
     * Encrypts a message with the system key and base64 encode the encrypted message.
     * @param message	the string to be encrypted
     * @return	base64 encoded encrypted message
     * @throws EncryptionServiceException
     */
    public String encrypt(String message) throws EncryptionServiceException;

    /**
     * Decrypts a base64 encoded message with the specified key.
     * @param base64EncodeMessage	the base64 encoded message to be decrypted
     * @param key	key used to decrypt the message
     * @param iv	initialization vector to use
     * @return	decrypted message
     * @throws EncryptionServiceException
     */
    public String decrypt(String base64EncodeMessage, String key, String iv)
            throws EncryptionServiceException;

    /**
     * Decrypts a base64 encoded message with the system key.
     * @param base64EncodeMessage	the base64 encoded message to be decrypted
     * @return	decrypted message
     * @throws EncryptionServiceException
     */
    public String decrypt(String base64EncodeMessage)
            throws EncryptionServiceException;

    /**
     * Encrypts the input stream with the specified key, write the result to
     * the output stream. Encryption service provides two sets of encryption/decryption
     * operations: One for byte-based operations and one for stream-based operations.
     *
     * @param input	unencrypted message stream
     * @param output	encrypted message stream
     * @param key	key used to encrypt the message
     * @param iv	initialization vector to use
     * @throws EncryptionServiceException
     */
    public void encrypt(InputStream input, OutputStream output, String key,
            String iv)
            throws EncryptionServiceException;

    /**
     * Encrypts the input stream with the system key, write the result to
     * the output stream.
     * @param input	unencrypted message stream
     * @param output	encrypted message stream
     * @throws EncryptionServiceException
     */
    public void encrypt(InputStream input, OutputStream output)
            throws EncryptionServiceException;

    /**
     * Decrypts the input stream with the specified key, write the result to
     * the output stream.
     * @param input	encrypted message stream
     * @param output	decrypted message stream
     * @param key	key used to decrypt the message
     * @param iv	initialization vector to use
     * @throws EncryptionServiceException
     */
    public void decrypt(InputStream input, OutputStream output, String key,
            String iv)
            throws EncryptionServiceException;

    /**
     * Decrypts the input stream with the system key, write the result to
     * the output stream.
     * @param input	encrypted message stream
     * @param output	decrypted message stream
     * @throws EncryptionServiceException
     */
    public void decrypt(InputStream input, OutputStream output)
            throws EncryptionServiceException;

    /**
     * Creates a new {@link CipherOutputStream} suitable for encryption.
     *
     * @param outputStream {@link OutputStream}, the wrapped output stream
     * @return {@link CipherOutputStream}, the encrypting output stream
     * @throws EncryptionServiceException Thrown if an encryption error
     * occurred setting up the {@link CipherOutputStream}
     */
    public CipherOutputStream newEncryptingOutputStream(
            OutputStream outputStream) throws EncryptionServiceException;

    public CipherOutputStream newEncryptingOutputStream(
            final OutputStream outputStream, final String key, final String iv)
            throws EncryptionServiceException;

    /**
     * Creates a new {@link CipherInputStream} suitable for decryption.
     *
     * @param inputStream {@link InputStream}, the wrapped input stream
     * @return {@link CipherInputStream}, the decrypting input stream
     * @throws EncryptionServiceException Thrown if an encryption error
     * occurred setting up the {@link CipherInputStream}
     */
    public CipherInputStream newDecryptingInputStream(InputStream inputStream)
            throws EncryptionServiceException;

    public CipherInputStream newDecryptingInputStream(
            final InputStream inputStream, final String key, final String iv)
            throws EncryptionServiceException;
}
