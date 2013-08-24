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
package org.openbel.framework.core.df.cache;

import java.io.File;

import org.openbel.framework.common.InvalidArgument;

/**
 * ResolvedResource represents the result of resolving a resource to the cache.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ResolvedResource {
    /**
     * Defines the cache resource copy ready for processing.
     */
    private final File cacheResourceCopy;

    /**
     * Defines whether or not this resource needed to be downloaded and cached
     * because a new copy existed at its remote location.
     */
    private final boolean cacheUpdated;

    /**
     * Constructs the ResolvedResource with the <tt>cachedResource</tt> and
     * the <tt>cacheUpdated</tt> flag.
     *
     * @param cachedResource {@link CachedResource}, the cached resource which
     * cannot be null
     * @param cacheUpdated <tt>boolean</tt>, <tt>true</tt> indicating a new
     * resource has been cached, <tt>false</tt> if the cached copy is
     * up-to-date
     * @throws InvalidArgument Thrown if <tt>cachedResource</tt> is null
     */
    public ResolvedResource(final File cacheResourceCopy,
            final boolean cacheUpdated) {
        if (cacheResourceCopy == null) {
            throw new InvalidArgument("cacheResourceCopy", cacheResourceCopy);
        }

        this.cacheResourceCopy = cacheResourceCopy;
        this.cacheUpdated = cacheUpdated;
    }

    /**
     * Returns the cached resource copy read for processing.
     *
     * @return {@link File}, the cache resource copy which cannot be null
     */
    public File getCacheResourceCopy() {
        return cacheResourceCopy;
    }

    /**
     * Returns the cache updated flag.
     *
     * @return <tt>boolean</tt>, the cache updated flag, <tt>true</tt>
     * indicating a new resource has been cached, <tt>false</tt> if the cached
     * copy is up-to-date
     */
    public boolean isCacheUpdated() {
        return cacheUpdated;
    }
}
