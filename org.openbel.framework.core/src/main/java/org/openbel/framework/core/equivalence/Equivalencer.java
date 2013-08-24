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
package org.openbel.framework.core.equivalence;

import java.io.IOException;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;

/**
 * Peforms {@link ProtoNetwork proto-network} equivalencing.
 */
// FIXME make into an interface and move protonetwork out of constructor
public abstract class Equivalencer {

    /**
     * The {@link ProtoNetwork proto-network} to be equivalenced.
     */
    protected final ProtoNetwork network;

    /**
     * Constructs an equivalencer with the associated {@link ProtoNetwork
     * proto-network}.
     *
     * @param network {@link ProtoNetwork Proto-network}
     * @throws InvalidArgument Thrown if {@code network} is null
     */
    public Equivalencer(final ProtoNetwork network) {
        if (network == null) {
            throw new InvalidArgument("network", network);
        }
        this.network = network;
    }

    /**
     * Executes equivalencing, returning the number of items that were
     * equivalenced.
     *
     * @return int
     * @throws IOException Thrown if an I/O error occurred during equivalencing
     */
    public abstract int equivalence() throws IOException;

}
