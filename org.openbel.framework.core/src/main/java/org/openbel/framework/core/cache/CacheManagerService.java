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
package org.openbel.framework.core.cache;

import java.io.IOException;

import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.core.df.cache.ResourceType;

/**
 * CacheManagerService defines a service for managing the framework cache.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface CacheManagerService {

    /**
     * Purges all cached files from the cache directory located at
     * {@link SystemConfiguration#getCacheDirectory()}.
     * <p>
     * :-(*)
     * </p>
     * @throws IOException Thrown if IO errors occurred purging the cache
     */
    public void purgeResources() throws IOException;

    /**
     * Updates a single resource in the cache identified by it's {@link String}
     * <tt>location</tt>.  The <tt>type</tt> {@link ResourceType} must also be
     * provided in order to identify where it should live in the cache.
     *
     * @param type {@link ResourceType}, the resource type
     * @param location {@link String}, the resource location
     */
    public void updateResourceInCache(ResourceType type, String location);

    /**
     * Updates all resources identified in the resource index.
     *
     * @param index {@link Index}, the resource index to update resources from
     */
    public void updateResourceIndexInCache(Index index);
}
