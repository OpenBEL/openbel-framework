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
package org.openbel.framework.tools.pkam;

import org.openbel.framework.common.InvalidArgument;

/**
 * PKAMSerializationService defines a service to serialize a KamStore KAM
 * database into Portable KAM format.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface PKAMSerializationService {

    /**
     * Serializes KAM, identified by <tt>kamName</tt>, to a file, identified by
     * <tt>fileName</tt>, in Portable KAM (PKAM) format.
     *
     * @param kamName {@link String}, the kam name to serialize, which cannot
     * be null
     * @param filePath {@link String}, the file path to store serialized data,
     * which cannot be null
     * @param password {@link String}, the password to use when encrypting the
     * KAM, which cannot be null
     * @throw PKAMSerializationFailure Thrown if an error occurred serializing
     * the KAM identified by <tt>kamName</tt>
     * @throws InvalidArgument Thrown if either <tt>kamName</tt>,
     * <tt>filePath</tt>, or <tt>password</tt> is null
     */
    public void serializeKAM(final String kamName, String filePath,
            final String password)
            throws PKAMSerializationFailure;

    /**
     * Deserializes KAM from a file, identified by <tt>filePath</tt>, in
     * Portable KAM format.
     *
     * @param kamName {@link String}, the kam name to save the deserialized
     * KAM to, which can be null, indicating the kam name should be read from
     * the file
     * @param filePath {@link String}, the file path where the Portable KAM is
     * stored, which cannot be null
     * @param password {@link String}, the password to use when decrypting the
     * KAM, which cannot be null
     * @param noPreserve <tt>boolean</tt>, the noPreserve option needed once
     * the portable KAM file's kam name is read
     * @throws PKAMSerializationFailure Thrown if an error occurred
     * deserializing the KAM located at <tt>filePath</tt>
     * @throws InvalidArgument Thrown if <tt>filePath</tt> or <tt>password</tt>
     * is null
     */
    public void deserializeKAM(final String kamName, final String filePath,
            final String password, final boolean noPreserve)
            throws PKAMSerializationFailure;
}
