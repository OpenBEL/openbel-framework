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
package org.openbel.framework.common.external;

import static java.util.Collections.unmodifiableSet;
import static org.openbel.framework.common.BELUtilities.constrainedHashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Write operation caching mechanism associated with
 * {@link CachingExternalizable}.
 * <p>
 * This caching mechanism should only be used to cache objects of a specific
 * type.
 * </p>
 *
 * @see ReadCache
 */
public class WriteCache {
    private final Map<Integer, Object> cache;
    private final Map<Object, Integer> keys;
    private int next = 0;

    /**
     * Constructs a write cache. Prefer {@link #WriteCache(int)} to this
     * constructor.
     *
     * @see #WriteCache(int)
     */
    public WriteCache() {
        cache = new HashMap<Integer, Object>();
        keys = new HashMap<Object, Integer>();
    }

    /**
     * Constructs a write cache with the specified initial capacity.
     *
     * @param capacity Initial capacity of the backing map
     * @see HashMap#HashMap(int)
     */
    public WriteCache(int capacity) {
        cache = constrainedHashMap(capacity);
        keys = constrainedHashMap(capacity);
    }

    /**
     * Retrieves a cached object's key.
     *
     * @param value {@link Object} whose key should be retrieved
     * @return {@link Integer} key; may be null if the provided object is not
     * cached
     */
    public Integer get(Object value) {
        synchronized (this) {
            return keys.get(value);
        }
    }

    /**
     * Caches an object, returning its key.
     * <p>
     * If the object has already been cached, its existing key is returned.
     * </p>
     *
     * @param value {@link Object} to cache
     * @return Non-null {@link Integer}
     */
    public Integer cache(Object value) {
        synchronized (this) {
            Integer key = keys.get(value);
            // Already cached?
            if (key != null) return key;
            key = next;
            cache.put(key, value);
            keys.put(value, key);
            next++;
            return key;
        }
    }

    /**
     * Returns a read-only view of the cache's contents.
     *
     * @return {@link Set} of {@link Entry} objects
     */
    public Set<Entry<Integer, Object>> entries() {
        synchronized (this) {
            return unmodifiableSet(cache.entrySet());
        }
    }

    /**
     * Returns the number of items cached.
     *
     * @return {@code int}
     */
    public int size() {
        return cache.size();
    }
}
