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

import java.io.Externalizable;

import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;

/**
 * ProtoNetworkExternalizer represents an externalizable {@link ProtoNetwork}.  This
 * approach was necessary because the {@link Externalizable} and readObject/writeObject
 * methods only supports a single output stream and the {@link TextProtoNetworkExternalizer}
 * requires writing a {@link ProtoNetwork} to multiple table files.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface ProtoNetworkExternalizer {

    /**
     * Reads a proto network from a descriptor.
     *
     * @param protoNetworkDescriptor {@link ProtoNetworkDescriptor}, the proto network descriptor
     * @return {@link ProtoNetwork}, the read proto network, which cannot be null
     * @throws ProtoNetworkError, if there was an error reading from the {@link ProtoNetworkDescriptor}
     */
    public ProtoNetwork readProtoNetwork(
            ProtoNetworkDescriptor protoNetworkDescriptor)
            throws ProtoNetworkError;

    /**
     * Writes a proto network and produces a descriptor.
     *
     * @param protoNetwork {@link ProtoNetwork}, the proto network, which cannot be null
     * @param protoNetworkRootPath {@link String}, the root path where proto network files should be created
     * @return {@link ProtoNetworkDescriptor}, the proto network descriptor
     * @throws ProtoNetworkError, if there was an error writing the {@link ProtoNetwork}
     */
    public ProtoNetworkDescriptor writeProtoNetwork(ProtoNetwork protoNetwork,
            String protoNetworkRootPath) throws ProtoNetworkError;
}
