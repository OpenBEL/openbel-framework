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

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

/**
 * Abstract base class for all encryption service implementation
 * @author tchu
 *
 */
public abstract class AbstractEncryptionService implements EncryptionService {

    protected int defaultInitializationVectorSize = 16; //defaults to 128-bit

    /* (non-Javadoc)
     * @see org.openbel.framework.core.encryption.EncryptionService#generateInitialziationVector()
     */
    @Override
    public String generateInitializationVector() {
        return generateInitializationVector(defaultInitializationVectorSize);
    }

    /**
     * Generate a initialization vector of the specified length.
     * @param length	number of bytes to generate
     * @return iv
     */
    protected String generateInitializationVector(int length) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[length];
        random.nextBytes(bytes);
        return encodeBase64(bytes);
    }

    /**
     * Returns a base64 encoded string for the bytes specified.
     * 
     * @param bytes
     * @return {@link Base64#encodeBase64(byte[]) Base64-encoded} string from a
     * byte array
     */
    protected String encodeBase64(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * Returns a base64 decoded byte array.
     * @param s
     * @return {@link Base64#decodeBase64(String) Base64-decoded} byte array
     */
    protected byte[] decodeBase64(String s) {
        return Base64.decodeBase64(s);
    }

}
