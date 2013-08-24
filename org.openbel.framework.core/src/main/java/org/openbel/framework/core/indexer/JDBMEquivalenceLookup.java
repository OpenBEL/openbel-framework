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

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;

/**
 * Provides lookups via JDBM within BEL Equivalence Files (.beleq).<br>
 * This lookup contains namespace values as the key, UUID (as a
 * {@link SkinnyUUID}) as the value.
 *
 * @author Steve Ungerer
 */
public final class JDBMEquivalenceLookup extends JDBMLookup<String, SkinnyUUID>
        implements EquivalenceLookup {

    /**
     * Constructs a JDBM lookup for the associated index path.
     *
     * @param indexPath
     *            Index path
     * @throws InvalidArgument
     *             Thrown if {@code indexPath} is null or empty
     */
    public JDBMEquivalenceLookup(String indexPath) {
        super(indexPath, new SkinnyUUIDSerializer());
    }

}
