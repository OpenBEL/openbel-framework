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

import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.PathConstants.PROTO_NETWORK_FILENAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.IOUtils;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;

/**
 * BinaryProtoNetworkExternalizer is responsible for reading a
 * {@link ProtoNetwork} from a {@link ProtoNetworkDescriptor} and writing a
 * {@link ProtoNetwork} to a {@link ProtoNetworkDescriptor}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Updated 
 */

public class BinaryProtoNetworkExternalizer implements ProtoNetworkExternalizer {
    /**
     * Reads a proto network from a descriptor representing a binary file.
     *
     * @param protoNetworkDescriptor {@link ProtoNetworkDescriptor}, the proto
     * network descriptor
     * @return {@link ProtoNetwork}, the read proto network, which cannot be
     * null
     * @throws InvalidArgument Thrown if the {@code protoNetworkDescriptor} was
     * not of type {@link BinaryProtoNetworkDescriptor}
     * @throws ProtoNetworkError, if there was an error reading from the
     * {@link ProtoNetworkDescriptor}
     */
    @Override
    public ProtoNetwork readProtoNetwork(
            ProtoNetworkDescriptor protoNetworkDescriptor)
            throws ProtoNetworkError {
        if (!BinaryProtoNetworkDescriptor.class
                .isAssignableFrom(protoNetworkDescriptor.getClass())) {
            String err = "protoNetworkDescriptor cannot be of type ";
            err = err.concat(protoNetworkDescriptor.getClass().getName());
            err = err.concat(", need type ");
            err = err.concat(BinaryProtoNetworkDescriptor.class.getName());
            throw new InvalidArgument(err);
        }

        BinaryProtoNetworkDescriptor binaryProtoNetworkDescriptor =
                (BinaryProtoNetworkDescriptor) protoNetworkDescriptor;
        File pn = binaryProtoNetworkDescriptor.getProtoNetwork();
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(pn));

            ProtoNetwork network = new ProtoNetwork();
            network.readExternal(ois);
            return network;
        } catch (FileNotFoundException e) {
            final String msg = "Cannot find binary proto-network for path";
            throw new ProtoNetworkError(pn.getAbsolutePath(), msg, e);
        } catch (IOException e) {
            final String msg = "Cannot read binary proto-network for path";
            throw new ProtoNetworkError(pn.getAbsolutePath(), msg, e);
        } catch (ClassNotFoundException e) {
            final String msg = "Cannot read proto-network for path";
            throw new ProtoNetworkError(pn.getAbsolutePath(), msg, e);
        } finally {
            // clean up IO resources
            IOUtils.closeQuietly(ois);
        }
    }

    /**
     * Writes a binary-based proto network and produces a descriptor including
     * the binary file.
     *
     * @param protoNetwork {@link ProtoNetwork}, the proto network, which cannot
     * be null
     * @param protoNetworkRootPath {@link String}, the root path where proto
     * network files should be created
     * @return {@link ProtoNetworkDescriptor}, the proto network descriptor
     * @throws ProtoNetworkError, if there was an error writing the
     * {@link ProtoNetwork}
     */
    @Override
    public ProtoNetworkDescriptor writeProtoNetwork(ProtoNetwork protoNetwork,
            String protoNetworkRootPath) throws ProtoNetworkError {
        final String filename = PROTO_NETWORK_FILENAME;
        final File file = new File(asPath(protoNetworkRootPath, filename));

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            protoNetwork.writeExternal(oos);
            return new BinaryProtoNetworkDescriptor(file);
        } catch (IOException e) {
            final String name = file.getAbsolutePath();
            final String msg =
                    "Cannot create temporary binary file for proto-network";
            throw new ProtoNetworkError(name, msg, e);
        } finally {
            // clean up IO resources
            IOUtils.closeQuietly(oos);
        }
    }
}
