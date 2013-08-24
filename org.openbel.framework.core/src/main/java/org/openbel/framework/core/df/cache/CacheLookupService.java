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

import java.util.List;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.cfg.SystemConfiguration;

/**
 * CacheLookupService defines service-level lookup operations to the
 * BELFramework cache defined at {@link SystemConfiguration#getCacheDirectory()}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface CacheLookupService {

    /**
     * Defines the default file name to represent the cached resource - {@value}.
     */
    public final static String DEFAULT_RESOURCE_FILE_NAME = "resource";

    /**
     * Retrieves the cached resource for the location.
     *
     * @param resourceType {@link ResourceType}, the resource type of the
     * <tt>location</tt> provided, which cannot be null
     * @param location {@link String}, the remote resource location, which
     * cannot be null
     * @return {@link CachedResource} the cache entry for the resource
     * location or <tt>null</tt> if the resource is not in the cache
     * @throws InvalidArgument Thrown if the <tt>location</tt> or
     * <tt>resourceType</tt> is null
     */
    public CachedResource
            findInCache(ResourceType resourceType, String location);

    /**
     * Retrieves all resources that are present in the cache.
     *
     * @return {@link List} of {@link CachedResource}, the list of resources
     * present in the cache, which might be empty
     */
    public List<CachedResource> getResources();
}
