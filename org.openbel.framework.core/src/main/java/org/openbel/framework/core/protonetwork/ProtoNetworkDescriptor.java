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

import org.openbel.framework.common.protonetwork.model.ProtoNetwork;

/**
 * ProtoNetworkDescriptor represents a file descriptor for a {@link ProtoNetwork}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface ProtoNetworkDescriptor {

    /**
     * Retrieves the base directory path containing the {@link ProtoNetwork} files.
     *
     * @return {@link String}, the proto network base directory path
     */
    public String getBasePath();
}
