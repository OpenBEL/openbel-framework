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
package org.openbel.framework.common;

import java.util.Map.Entry;

/**
 * Interface defining a function that can be applied over every
 * {@link java.util.Map.Entry entry} within a map.
 */
public interface MapFunction<K, V> {

    /**
     * Applies the function to the provided key/value pair.
     *
     * @param key Map entry {@link Entry#getKey() key}
     * @param value Map entry {@link Entry#getValue() value}
     */
    public abstract void apply(K key, V value);

}
