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
