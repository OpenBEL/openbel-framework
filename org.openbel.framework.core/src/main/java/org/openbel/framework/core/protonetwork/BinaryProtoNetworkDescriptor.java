/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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
package org.openbel.framework.core.protonetwork;

import java.io.File;

/**
 * BinaryProtoNetworkDescriptor represents the file descriptor for the binary-encoded
 * proto network.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class BinaryProtoNetworkDescriptor implements ProtoNetworkDescriptor {

    /**
     * Defines a {@link File} to represent the proto network binary file.
     */
    private File binaryProtoNetworkFile;

    /**
     * Creates the BinaryProtoNetworkDescriptor from a binary {@code binaryProtoNetworkFile} file.
     *
     * @param binaryProtoNetworkFile {@link File}, the proto network binary file
     */
    public BinaryProtoNetworkDescriptor(File binaryProtoNetworkFile) {
        this.binaryProtoNetworkFile = binaryProtoNetworkFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBasePath() {
        return binaryProtoNetworkFile.getParent();
    }

    /**
     * Retrieves the proto network file, which could be null.
     *
     * @return {@link File}, the proto network binary file
     */
    public File getProtoNetwork() {
        return binaryProtoNetworkFile;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime
                        * result
                        + ((binaryProtoNetworkFile == null) ? 0
                                : binaryProtoNetworkFile.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BinaryProtoNetworkDescriptor other = (BinaryProtoNetworkDescriptor) obj;
        if (binaryProtoNetworkFile == null) {
            if (other.binaryProtoNetworkFile != null)
                return false;
        } else if (!binaryProtoNetworkFile.equals(other.binaryProtoNetworkFile))
            return false;
        return true;
    }
}
