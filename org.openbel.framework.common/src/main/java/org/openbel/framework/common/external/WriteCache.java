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
