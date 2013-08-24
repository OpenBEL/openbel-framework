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

import static org.openbel.framework.common.BELUtilities.createDirectories;

import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;

/**
 * Proto-network service implementation.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ProtoNetworkServiceImpl implements ProtoNetworkService {

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtoNetwork compile(Document document) {
        ProtoNetworkBuilder protoNetworkBuilder = new ProtoNetworkBuilder(
                document);
        return protoNetworkBuilder.buildProtoNetwork();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtoNetwork read(ProtoNetworkDescriptor descriptor)
            throws ProtoNetworkError {
        ProtoNetworkExternalizer ext = new BinaryProtoNetworkExternalizer();
        return ext.readProtoNetwork(descriptor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtoNetworkDescriptor write(String protoNetworkRoot,
            ProtoNetwork protoNetwork) throws ProtoNetworkError {
        createDirectories(protoNetworkRoot);

        BinaryProtoNetworkExternalizer binaryExternalizer =
                new BinaryProtoNetworkExternalizer();
        ProtoNetworkDescriptor protoNetworkDescriptor = binaryExternalizer
                .writeProtoNetwork(protoNetwork, protoNetworkRoot);

        return protoNetworkDescriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(ProtoNetwork protoNetworkDestination,
            ProtoNetwork protoNetworkSource) {

        if (protoNetworkSource == null) {
            return;
        }

        ProtoNetworkMerger protoNetworkMerger = new ProtoNetworkMerger();
        protoNetworkMerger.merge(protoNetworkDestination, protoNetworkSource);
    }
}
