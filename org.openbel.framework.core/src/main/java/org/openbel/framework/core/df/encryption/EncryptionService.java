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
