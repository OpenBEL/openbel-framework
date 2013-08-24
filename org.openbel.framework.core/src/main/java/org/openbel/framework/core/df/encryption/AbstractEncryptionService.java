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
