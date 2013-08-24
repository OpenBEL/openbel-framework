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
package org.openbel.framework.core.indexer;

import java.io.IOException;

import org.openbel.framework.common.protonetwork.model.SkinnyUUID;

import jdbm.Serializer;
import jdbm.SerializerInput;
import jdbm.SerializerOutput;

/**
 * Serialize a {@link SkinnyUUID} to 2 longs representing the most and least
 * significant bits. <br>
 * Note: JDBM provides the ability to pack longs when (de)serializing. This
 * functionality is not implemented as the storage requirements differed by less
 * than 0.1mb for 500k UUIDs with no noticeable impact on performance.
 *
 * @author Steve Ungerer
 */
public class SkinnyUUIDSerializer implements Serializer<SkinnyUUID> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(SerializerOutput out, SkinnyUUID obj)
            throws IOException {
        out.writeLong(obj.getMostSignificantBits());
        out.writeLong(obj.getLeastSignificantBits());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SkinnyUUID deserialize(SerializerInput in) throws IOException,
            ClassNotFoundException {
        return new SkinnyUUID(in.readLong(), in.readLong());
    }

}
