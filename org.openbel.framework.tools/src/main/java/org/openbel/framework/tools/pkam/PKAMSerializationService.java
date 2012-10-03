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
     * @throw PKAMSerializationFailure Thrown if an error occurred serializing
     * the KAM identified by <tt>kamName</tt>
     * @throws InvalidArgument Thrown if either <tt>kamName</tt>,
     * <tt>filePath</tt>, or <tt>password</tt> is null
     */
    public void serializeKAM(final String kamName, String filePath)
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
     * @param noPreserve <tt>boolean</tt>, the noPreserve option needed once
     * the portable KAM file's kam name is read
     * @throws PKAMSerializationFailure Thrown if an error occurred
     * deserializing the KAM located at <tt>filePath</tt>
     * @throws InvalidArgument Thrown if <tt>filePath</tt> or <tt>password</tt>
     * is null
     */
    public void deserializeKAM(final String kamName, final String filePath,
            final boolean noPreserve) throws PKAMSerializationFailure;
}
