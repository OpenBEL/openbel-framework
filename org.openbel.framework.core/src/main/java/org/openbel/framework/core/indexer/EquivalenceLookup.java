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

import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;

/**
 * API for obtaining universally unique identifiers for namespace values.
 * A single {@link EquivalenceLookup} instance is responsibile only for a
 * single {@link Namespace}.
 *
 * @author Steve Ungerer
 */
public interface EquivalenceLookup {

    /**
     * Performs a lookup for the given {@code key}.
     *
     * @param key Key - a value within a namespace.
     * @return {@link SkinnyUUID}; may be <code>null</code> for the given key
     */
    SkinnyUUID lookup(String key);

}
