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
 * Read operation caching mechanism associated with
 * {@link CachingExternalizable}.
 * <p>
 * This caching mechanism should only be used to cache objects of a specific
 * type.
 * </p>
 *
 * @see WriteCache
 */
public class ReadCache {
    private final Map<Integer, Object> cache;
    private final Map<Object, Integer> keys;

    /**
     * Constructs a read cache. Prefer {@link #ReadCache(int)} to this
     * constructor.
     *
     * @see #ReadCache(int)
     */
    public ReadCache() {
        cache = new HashMap<Integer, Object>();
        keys = new HashMap<Object, Integer>();
    }

    /**
     * Constructs a read cache with the specified initial capacity.
     *
     * @param capacity Initial capacity of the backing map
     * @see HashMap#HashMap(int)
     */
    public ReadCache(int capacity) {
        cache = constrainedHashMap(capacity);
        keys = constrainedHashMap(capacity);
    }

    /**
     * Retrieves a cached object by its key.
     *
     * @param key {@link Integer} key of object to retrieve; null is allowable
     * here though nothing will ever be returned
     * @return Cached {@link Object}; may be null if nothing is cached for the
     * provided key
     */
    public Object get(Integer key) {
        synchronized (this) {
            return cache.get(key);
        }
    }

    /**
     * Caches an object by the provided key.
     *
     * @param key {@link Integer} key
     * @param value {@link Object} to cache
     */
    public void cache(Integer key, Object value) {
        synchronized (this) {
            Integer curkey = keys.get(value);
            // Already cached?
            if (curkey != null) return;
            cache.put(key, value);
            keys.put(value, key);
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
