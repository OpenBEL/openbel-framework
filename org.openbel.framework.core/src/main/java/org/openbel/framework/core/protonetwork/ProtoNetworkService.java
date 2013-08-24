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

import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;

/**
 * Compiles {@link Document BEL common documents} to, writes, and merges
 * proto-networks.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface ProtoNetworkService {

    /**
     * Compiles {@link Document BEL common documents} to {@link ProtoNetwork
     * proto-networks}.
     *
     * @param document {@link Document} the document to compile
     * @return {@link ProtoNetwork} the compiled proto network
     */
    public ProtoNetwork compile(Document document);

    /**
     * Reads the {@link ProtoNetwork} from the binary descriptor.
     *
     * @param descriptor {@link ProtoNetworkDescriptor}, the proto network
     * descriptor
     * @return {@link ProtoNetwork} the read proto network
     * @throws ProtoNetworkError Thrown if an error occurred reading
     * the proto network
     */
    public ProtoNetwork read(ProtoNetworkDescriptor descriptor)
            throws ProtoNetworkError;

    /**
     * Writes the {@link ProtoNetwork proto-network}.
     *
     * @param protoNetworkRootPath {@link String}, the proto network root path
     * to write to
     * @param protoNetwork {@link ProtoNetwork}, the proto network to write
     */
    public ProtoNetworkDescriptor write(String protoNetworkRootPath,
            ProtoNetwork protoNetwork) throws ProtoNetworkError;

    /**
     * Deserialize and merge the proto network in <tt>protoNetworkSource</tt>
     * into the proto network in <tt>protoNetworkSource</tt>.
     *
     * <p>
     * If <tt>protoNetworkSource</tt> is null then
     * <tt>protoNetworkDestination</tt> will be unchanged.
     * </p>
     *
     * @param protoNetworkDestination {@link ProtoNetwork}, the target proto network
     * @param protoNetworkSource {@link ProtoNetwork}, the source proto network,
     * which may be null, in which case <tt>protoNetworkDestination</tt> will
     * be unchanged
     */
    public void merge(
            ProtoNetwork protoNetworkDestination,
            ProtoNetwork protoNetworkSource);
}
