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
package org.openbel.framework.ws.core;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Cache object is a hashmap with values wrapped around in soft references. Objects held by the hashmap will
 * be cleared when garbage collector has determined it needs to reclaim the memory.
 * 
 * @author tchu
 *
 */
public class Cache<T extends Cacheable> {
    private static final Logger logger = LoggerFactory.getLogger(Cache.class);

    private Map<String, SoftReference<T>> cacheMap =
            new HashMap<String, SoftReference<T>>();
    private Map<String, Long> timeMap = new HashMap<String, Long>();
    private MaintenanceThread maintenanceThread = null;

    public Cache() {
        logger.debug("In Constructor");
    }

    /**
     * Starts the maintenance thread.
     */
    protected synchronized void startMaintenanceThread() {
        if (!this.hasMaintenanceThread()) {
            this.maintenanceThread = new MaintenanceThread(this, timeMap);
            this.maintenanceThread.start();
        }
    }

    /**
     * Adds an object to cache. Key for this cache entry is determined by Cacheable.getCacheKey()
     * There is no guarantee that this object will be there by the time the next get(key) is called.
     * 
     * @param value
     */
    public synchronized void put(T value) throws CacheException {
        cacheMap.put(value.getCacheKey(), new SoftReference<T>(value));
    }

    /**
     * Adds an object to cache. There is no guarantee that this object will be there by the time 
     * the next get(key) is called. If no other references are pointing to the cached object,
     * the supplied value are guaranteed to be removed from cache after timeToClearMins + 1 minutes.
     * 
     * @param value
     * @param timeToClearMins	
     */
    public synchronized void put(T value, long timeToClearMins)
            throws CacheException {
        this.put(value);
        if (!this.hasMaintenanceThread()) {
            startMaintenanceThread();
        }
        timeMap.put(value.getCacheKey(), new Long(System.currentTimeMillis()
                + (timeToClearMins * 60 * 1000)));
    }

    /**
     * Retrieves an object from cache. If the object was cleared due to memory demand, null will 
     * be returned.
     * 
     * @param key
     * @return value
     */
    public T get(String key) {
        SoftReference<T> ref = cacheMap.get(key);
        if (ref != null) {
            if (ref.get() == null) {
                logger.debug("Cached item was cleared: " + key);
            }
            return ref.get(); //maybe still be null
        }

        return null;
    }

    /**
     * Clears the cache. All keys and values will be eligible for memory collection.
     */
    public void clear() {
        cacheMap.clear();
        timeMap.clear();
    }

    /**
     * Returns true if cache contains this key. Note: Even if this cache contains the key, it
     * doesn't mean the value object is still in the cache. 
     * @param key
     * @return true/false
     */
    public boolean containsKey(String key) {
        return cacheMap.containsKey(key);
    }

    protected boolean isEmpty() {
        return cacheMap.isEmpty();
    }

    /**
     * returns a set of all keys in the cache regardless whether they have been cleared or not.
     * @return
     */
    public Set<String> keySet() {
        return cacheMap.keySet();
    }

    /**
     * Remove an entry from the cache
     * @param key
     */
    public synchronized void remove(String key) {
        cacheMap.remove(key);
        timeMap.remove(key);
    }

    /**
     * returns the number of entries in the cache
     * @return
     */
    public int size() {
        return cacheMap.size();
    }

    /**
     * returns true	if and only if there is a maintenance thread attached to this cache
     * @return true	if and only if there is a maintenance thread attached to this cache
     */
    protected boolean hasMaintenanceThread() {
        return this.maintenanceThread != null;
    }

}

/**
* A MaintenanceThread is created when a  Cache object is instantiated. The role of this
* thread is to monitor the time limited cache entries and remove them from the cache when
* they expire
* 
* @author tchu
*/
class MaintenanceThread extends Thread {
    private static final Logger log = LoggerFactory
            .getLogger(MaintenanceThread.class);

    private Map<String, Long> timeMap = null;
    private Cache<? extends Cacheable> cacheObj = null;
    private boolean stopRequested = false;
    private final long DEFAULT_CHECKING_INTERVAL = 15 * 1000; //check thread every 15 secs

    public MaintenanceThread(Cache<? extends Cacheable> cacheObj,
            Map<String, Long> timeMap) {
        this.cacheObj = cacheObj;
        this.timeMap = timeMap;
    }

    public void requestStop() {
        stopRequested = true;
    }

    @Override
    public void run() {
        while (!stopRequested) {
            if (timeMap.size() > 0) {
                Set<String> entriesToRemove = findEntriesToRemove();
                if (entriesToRemove.size() > 0) {
                    Iterator<String> k = entriesToRemove.iterator();
                    while (k.hasNext()) {
                        String key = k.next();
                        synchronized (cacheObj) {
                            cacheObj.remove(key);
                        }
                    }
                }
            }
            try {
                sleep(DEFAULT_CHECKING_INTERVAL);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                log.debug(
                        "Exception encountered when trying to sleep. Will retry in next sleep cycle.",
                        ie);
                //do nothing
            }
        }
    }

    /**
     * 
     * @return Set of entries to remove
     */
    private Set<String> findEntriesToRemove() {
        Set<String> entriesToRemove = new HashSet<String>();
        for (String key : timeMap.keySet()) {
            Long expiration = timeMap.get(key);
            if (expiration != null) {
                //expiration should never be null if set up correctly
                if (System.currentTimeMillis() >= expiration.longValue()) {
                    entriesToRemove.add(key);
                }
            }
        }
        return entriesToRemove;
    }
}
