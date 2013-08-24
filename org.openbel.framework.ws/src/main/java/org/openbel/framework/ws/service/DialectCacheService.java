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
package org.openbel.framework.ws.service;

import org.openbel.framework.api.Dialect;

/**
 * Provides server-side caching of {@link Dialect}s
 *
 * @author Steve Ungerer
 */
public interface DialectCacheService {

    /**
     * Obtain a {@link Dialect} for a handle
     *
     * @param dialectHandle
     * @return
     */
    Dialect getDialect(String dialectHandle);

    /**
     * Store a {@link Dialect} in the cache. If a {@link Dialect} was previously
     * cached, the returned handle will be for the previously cached instance.
     *
     * @return The handle for the cached {@link Dialect}
     */
    String cacheDialect(Dialect dialect);

    /**
     * Release a {@link Dialect} from the cache
     *
     * @param dialectHandle The handle of the {@link Dialect} to release
     */
    void releaseDialect(String dialectHandle);

}
