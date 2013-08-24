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

import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * CacheableResourceService defines a service to handle cacheable BELFramework
 * resource locations.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface CacheableResourceService extends ResourceService {

    /**
     * Resolves the BELFramework resource with a specific <tt>type</tt> and
     * located at <tt>location</tt>.
     *
     * @param type {@link ResourceType}, the resource type
     * @param location {@link String}, the resource location
     * @return {@link ResolvedResource}, the resource resolved in the cache
     * indicating the action taken
     * @throws ResourceDownloadError Thrown if the resource could not be
     * downloaded from <tt>location</tt> and if that resource was not located
     * in the cache
     */
    public ResolvedResource resolveResource(ResourceType type, String location)
            throws ResourceDownloadError;
}
