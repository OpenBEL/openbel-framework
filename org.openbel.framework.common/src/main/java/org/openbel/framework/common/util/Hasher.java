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
package org.openbel.framework.common.util;

import static org.openbel.framework.common.Strings.SHA_256;
import static org.openbel.framework.common.Strings.UTF_8;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.openbel.framework.common.MissingAlgorithmException;
import org.openbel.framework.common.MissingEncodingException;

/**
 * Hasher is a singleton used to hash {@link String} input and produce a
 * byte array.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public enum Hasher {
    INSTANCE;

    /**
     * Defines the SHA-256 message digest to hash with.
     */
    private final MessageDigest sha256Digest;

    /**
     * Defines the hex character set.
     */
    private final String HEXES = "0123456789abcdef";

    /**
     * Constructs the Hasher by creating a "SHA-256" message digest.
     *
     * @throws MissingAlgorithmException Unchecked exception thrown if the
     * "SHA-256" algorithm is missing on the current environment
     */
    private Hasher() {
        try {
            sha256Digest = MessageDigest.getInstance(SHA_256);
        } catch (NoSuchAlgorithmException e) {
            throw new MissingAlgorithmException(SHA_256, e);
        }
    }

    /**
     * Hash a {@link String} value, read as "UTF-8", using the SHA-256
     * algorithm.
     *
     * @param value {@link String}, the value to hash
     * @return {@link String} the hash string represented in base 16
     * @throws MissingAlgorithmException Unchecked exception thrown if the
     * SHA-256 algorithm is missing on the current environment
     * @throws MissingEncodingException Unchecked exception thrown if the
     * UTF-8 character encoding is missing on the current environment
     */
    public String hashValue(String value) {
        if (value == null) {
            return null;
        }

        try {
            return hash(value.getBytes(UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new MissingAlgorithmException(SHA_256, e);
        } catch (UnsupportedEncodingException e) {
            throw new MissingEncodingException(UTF_8, e);
        }
    }

    /**
     * Hashes the <tt>input</tt> byte array.  The process is:<ul>
     * <li>Reset the SHA-256 digest.</li>
     * <li>Update the digest with the <tt>input</tt> byte array</li>
     * <li>Hash the <tt>digest</tt> data and return base 16 {@link String}</li>
     * </ul>
     *
     * @param input <tt>byte[]</tt> the byte array to hash
     * @return {@link String} the hash string represented in base 16
     * @throws NoSuchAlgorithmException Thrown if the "SHA-256" algorithm is
     * not available on the current runtime
     */
    private String hash(byte[] input) throws NoSuchAlgorithmException {
        sha256Digest.reset();
        sha256Digest.update(input);
        return getHex(sha256Digest.digest());
    }

    /**
     * Converts the <tt>byte[]</tt> to a hexadecimal string.
     *
     * @param bytes <tt>byte[]</tt>, the byte array to convert to hexadecimal
     * @return {@link String} representing the hexadecimal string, using
     * lowercase a-f as the six additional symbols
     */
    private String getHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        final StringBuilder hex = new StringBuilder(2 * bytes.length);

        for (final byte b : bytes) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
                    HEXES.charAt((b & 0x0F)));
        }

        return hex.toString();
    }
}
