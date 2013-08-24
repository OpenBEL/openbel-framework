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
package org.openbel.framework.api;

import static java.lang.String.*;
import static java.util.concurrent.Executors.*;
import static org.apache.commons.lang.RandomStringUtils.*;
import static org.openbel.framework.api.KamCacheService.LoadStatus.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

/**
 * Default thread-safe implementation of a {@link KamCacheService}.
 * <p>
 * <h1>Maps</h1> This implementation maintains three separate maps internally.
 * <dl>
 * <dt><i>KAM map</i></dt>
 * <dd>Maintains a strong reference to a {@link Kam} object by its handle (a
 * string)</dd>
 * <dt><i>Unfiltered map</i></dt>
 * <dd>Maintains a reference to handles available by {@link KamInfo}. This
 * allows calls to the interface to return a handle to the KAM held in memory
 * when <i>KamFilter</i> is not provided</dd>
 * <dt><i>Filtered map</i></dt>
 * <dd>Maintains a reference to handles available by {@link KamInfo} and
 * {@link KamFilter}. This allows calls to the interface to return a handle to
 * the KAM held in memory when <i>KamFilter</i> is provided</dd>
 * </dl>
 * </p>
 * <p>
 * <h1>Thread-safety</h1> Access to any of the maps is done under a single read
 * lock. Mutating any of the maps is done under a single write lock. Both locks
 * are contended for in a fair fashion.
 * </p>
 * <p>
 * <h1>Concurrent Loading</h1> The number of KAMs that can be loaded
 * concurrently is controlled by the {@code CONCURRENT_LOAD} constant.
 * </p>
 *
 * @author julianray
 */
public class DefaultKamCacheService implements KamCacheService {
    private static final int CACHE_KEY_LENGTH = 24;
    /*
     * Effectively controls how many KAMs can be loaded concurrently.
     */
    protected static final int CONCURRENT_LOAD = 4;

    // Maps handles to KAMs
    private final Map<String, Kam> kamMap;

    // Maps unfiltered KAMs to handles
    private final Map<KamInfo, String> unfltrdMap;

    // Maps filtered KAMs to handles
    private final Map<FilteredKAMKey, String> fltrdMap;

    /** {@link KamStore} */
    protected final KamStore kamStore;

    private final ReadLock read;
    private final WriteLock write;
    private final ExecutorService execSvc;

    /**
     * Creates a default KAM cache service with a supplied {@link KamStore}.
     *
     * @param kamStore {@link KamStore}
     * @throws InvalidArgument Thrown if {@code kamStore} is null
     */
    public DefaultKamCacheService(KamStore kamStore) {
        if (kamStore == null) {
            throw new InvalidArgument("kamStore", kamStore);
        }
        this.kamStore = kamStore;

        kamMap = new HashMap<String, Kam>();
        unfltrdMap = new HashMap<KamInfo, String>();
        fltrdMap = new HashMap<FilteredKAMKey, String>();

        final ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock(true);
        read = rwlock.readLock();
        write = rwlock.writeLock();

        execSvc = newFixedThreadPool(CONCURRENT_LOAD, new _ThreadFactory());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String cacheKam(String kamName, Kam kam) {

        if (kam == null) {
            throw new InvalidArgument("kam", kam);
        }

        if (kamName == null) {
            kamName = kam.getKamInfo().getName();
            if (kamName == null) {
                throw new InvalidArgument("no KAM name available");
            }
        }

        read.lock();
        try {
            for (Entry<String, Kam> entry : kamMap.entrySet()) {
                final Kam value = entry.getValue();
                if (kamName.equals(value.getKamInfo().getName())) {
                    return entry.getKey();
                }
            }
        } finally {
            read.unlock();
        }

        // Generate a key and cache it
        String key = generateCacheKey();
        write.lock();
        try {
            while (kamMap.containsKey(key)) {
                key = generateCacheKey();
            }
            kamMap.put(key, kam);
        } finally {
            write.unlock();
        }
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Kam getKam(String handleOrName) {
        Kam ret;
        read.lock();
        try {
            ret = kamMap.get(handleOrName);
            if (ret != null) {
                return ret;
            }

            // fallback to retrieval by KAM name
            for (final Kam k : kamMap.values()) {
                if (k.getKamInfo().getName().equals(handleOrName)) {
                    return k;
                }
            }
            return null;
        } finally {
            read.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String loadKam(KamInfo ki, KamFilter kf)
            throws KamCacheServiceException {

        if (ki == null) {
            throw new InvalidArgument("KamInfo required");
        }

        String ret;
        Callable<String> callable;
        if (kf != null) {
            // Check the fltrdMap cache first
            FilteredKAMKey key = new FilteredKAMKey(ki, kf);
            read.lock();
            try {
                ret = fltrdMap.get(key);
            } finally {
                read.unlock();
            }
            if (ret != null) {
                // Cache hit
                return ret;
            }
            // Cache miss, create a callable to defer loading
            callable = new CacheCallable(ki, kf);
        } else {
            read.lock();
            try {
                ret = unfltrdMap.get(ki);
            } finally {
                read.unlock();
            }
            if (ret != null) {
                // Cache hit
                return ret;
            }
            // Cache miss, create a callable to defer loading
            callable = new CacheCallable(ki, null);
        }

        // Block, waiting for KAM to load
        try {
            ret = execSvc.submit(callable).get();
        } catch (ExecutionException e) {
            throw new KamCacheServiceException(e);
        } catch (InterruptedException e) {
            throw new KamCacheServiceException(e);
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoadKAMResult loadKamWithResult(KamInfo ki, KamFilter kf)
            throws KamCacheServiceException {

        if (ki == null) {
            throw new InvalidArgument("KamInfo required");
        }

        String handle;
        Callable<String> callable;
        if (kf != null) {
            // Check the fltrdMap cache first
            FilteredKAMKey key = new FilteredKAMKey(ki, kf);
            read.lock();
            try {
                handle = fltrdMap.get(key);
            } finally {
                read.unlock();
            }
            if (handle != null) {
                // Cache hit
                LoadKAMResult ret = new LoadKAMResult(handle, LOADED);
                return ret;
            }
            // Cache miss, create a callable to defer loading
            callable = new CacheCallable(ki, kf);
        } else {
            read.lock();
            try {
                handle = unfltrdMap.get(ki);
            } finally {
                read.unlock();
            }
            if (handle != null) {
                // Cache hit
                LoadKAMResult ret = new LoadKAMResult(handle, LOADED);
                return ret;
            }
            // Cache miss, create a callable to defer loading
            callable = new CacheCallable(ki, null);
        }

        // Submit and return
        execSvc.submit(callable);

        LoadKAMResult ret = new LoadKAMResult(null, LOADING);
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void releaseKam(String handle) {
        if (handle == null) {
            throw new InvalidArgument("handle", handle);
        }

        // Purge any cache entries
        purgeHandle(handle);
    }

    /**
     * Removes any cached entries for this KAM handle.
     * <p>
     * This method will block obtaining a write lock on the cache.
     * </p>
     *
     * @param handle String
     */
    private void purgeHandle(String handle) {
        write.lock();
        try {
            kamMap.remove(handle);

            Set<Entry<FilteredKAMKey, String>> entries = fltrdMap.entrySet();
            Iterator<Entry<FilteredKAMKey, String>> iter = entries.iterator();
            while (iter.hasNext()) {
                Entry<FilteredKAMKey, String> next = iter.next();
                if (handle.equals(next.getValue())) {
                    iter.remove();
                }
            }

            Set<Entry<KamInfo, String>> entries2 = unfltrdMap.entrySet();
            Iterator<Entry<KamInfo, String>> iter2 = entries2.iterator();
            while (iter2.hasNext()) {
                Entry<KamInfo, String> next = iter2.next();
                if (handle.equals(next.getValue())) {
                    iter2.remove();
                }
            }
        } finally {
            write.unlock();
        }
    }

    /**
     * Generates a random string to be used as a cache key.
     *
     * @return {@link String}
     */
    protected final static String generateCacheKey() {
        return randomAlphabetic(CACHE_KEY_LENGTH);
    }

    private class CacheCallable implements Callable<String> {
        private KamInfo ki;
        private KamFilter kf;

        /**
         * Callable task that loads a KAM and returns its handle.
         *
         * @param ki {@link KamInfo}; must be non-null
         * @param kf {@link KamFilter}; may be null
         */
        CacheCallable(final KamInfo ki, final KamFilter kf) {
            if (ki == null) {
                throw new InvalidArgument("ki cannot be null");
            }
            this.ki = ki;
            this.kf = kf;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String call() throws KamStoreException {
            String ret = null;

            if (kf != null) {
                // Load request is for a filtered KAM.
                FilteredKAMKey key = new FilteredKAMKey(ki, kf);
                read.lock();
                try {
                    ret = fltrdMap.get(key);
                } finally {
                    read.unlock();
                }
                if (ret != null) {
                    return ret;
                }

                Kam k = kamStore.getKam(ki, kf);

                write.lock();
                try {
                    // check again - now that we're alone, just you and I
                    ret = fltrdMap.get(key);
                    if (ret != null) {
                        return ret;
                    }

                    // cache the filtered KAM
                    ret = generateCacheKey();
                    kamMap.put(ret, k);
                    fltrdMap.put(key, ret);
                } finally {
                    write.unlock();
                }
            } else {
                // Load request is for an unfiltered KAM.
                read.lock();
                try {
                    ret = unfltrdMap.get(ki);
                } finally {
                    read.unlock();
                }
                if (ret != null) {
                    return ret;
                }

                // get an unfiltered KAM
                Kam k = kamStore.getKam(ki);

                write.lock();
                try {
                    // check again - now that we're alone, just you and I
                    ret = unfltrdMap.get(ki);
                    if (ret != null) {
                        return ret;
                    }

                    // cache the unfiltered KAM
                    ret = generateCacheKey();
                    kamMap.put(ret, k);
                    unfltrdMap.put(ki, ret);
                } finally {
                    write.unlock();
                }
            }
            return ret;
        }
    }

    private static class FilteredKAMKey {
        final int hash;
        final KamInfo info;
        final KamFilter fltr;

        /**
         * Filtered KAM key.
         *
         * @param info {@link KamInfo} assumed non-null
         * @param fltr {@link KamFilter} assumed non-null
         */
        FilteredKAMKey(KamInfo info, KamFilter fltr) {
            this.info = info;
            this.fltr = fltr;

            final int prime = 31;
            int hash2 = prime;

            hash2 *= prime;
            hash2 += info.hashCode();

            hash2 *= prime;
            hash2 += fltr.hashCode();

            this.hash = hash2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return hash;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (!(obj instanceof FilteredKAMKey)) return false;
            FilteredKAMKey f = (FilteredKAMKey) obj;

            // info is non-null by contract
            if (!info.equals(f.info)) return false;
            // fltr is non-null by contract
            if (!fltr.equals(f.fltr)) return false;
            return true;
        }

    }

    /**
     * Creates daemon threads with names {@code kam-cache-thread-%d}.
     */
    private class _ThreadFactory implements ThreadFactory {
        int thread_num = 0;

        /**
         * {@inheritDoc}
         */
        @Override
        public Thread newThread(Runnable r) {
            final Thread t = new Thread(r);
            t.setName(format("kam-cache-thread-%d", thread_num++));
            t.setDaemon(true);
            return t;
        }

    }
}
