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
