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

import static java.lang.String.format;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

/**
 * @author julianjray
 */
public interface KamCacheService {

    /**
     * Retrieves a {@link Kam} by its handle (in the case of a
     * {@link #cacheKam(String, Kam) cached KAM}). If no Kam is found by handle,
     * this method attempts to find a Kam by name.
     *
     * @param handleOrName {@link String} handle or {@link Kam} name
     * @return {@link Kam}; may be null
     */
    public Kam getKam(String handleOrName);

    /**
     * Loads a {@link Kam} by {@link KamInfo}, returning a handle to it. This
     * method blocks while the {@link Kam} is loaded.
     *
     * @param ki {@link KamInfo}
     * @param kf {@link KamFilter} used to filter the {@link KamEdge edges} and
     * {@link KamNode} nodes before loading
     * @return {@link String} The {@link Kam} handle
     * @throws KamCacheServiceException TODO Document why this exception is
     * thrown.
     */
    public String loadKam(KamInfo ki, KamFilter kf)
            throws KamCacheServiceException;

    /**
     * Loads a {@link Kam} by {@link KamInfo}, returning a handle to it. This
     * method will not block and can be used to poll the service until the
     * {@link Kam} is available.
     *
     * @param kamInfo {@link KamInfo}
     * @param kamFilter {@link KamFilter} used to filter the {@link KamEdge
     * edges} and {@link KamNode} nodes before loading
     * @return {@link LoadKAMResult}
     * @throws KamCacheServiceException
     */
    public LoadKAMResult loadKamWithResult(KamInfo ki, KamFilter kf)
            throws KamCacheServiceException;

    /**
     * Cache the {@link Kam} returning a handle associated with the provided
     * {@code name}. If {@code name} is null, the {@link Kam} will be cached by
     * the {@link Kam#getKamInfo() KamInfo} {@link KamInfo#getName()}.
     *
     * @param name {@link String} to cache the {@link Kam} by; may be null
     * @param kam {@link Kam}, the kam to cache
     * @return {@link String} The {@link Kam} handle
     * @throws InvalidArgument Thrown if a KAM name was not provided and none
     * was available in the {@code kam} argument
     * @see #getKam(String) getKam - Retrieving a KAM by handle
     */
    public String cacheKam(String name, Kam kam);

    /**
     * @param kamHandle
     * @throws KamCacheServiceException
     */
    public void releaseKam(String kamHandle);

    static enum LoadStatus {
        /**
         * The KAM is loading.
         */
        LOADING,

        /**
         * The KAM has been (or was previously) loaded.
         */
        LOADED,
    }

    /**
     * Contains the result of a
     * {@link KamCacheService#loadKamWithResult(KamInfo, KamFilter) invocation}.
     */
    public static class LoadKAMResult {
        private final String handle;
        private final LoadStatus status;

        /**
         * Creates a LoadKAMResult for a KAM handle with a specific
         * {@link LoadStatus}.
         *
         * @param handle {@link String} KAM handle
         * @param status {@link LoadStatus}
         * @throws InvalidArgument Thrown if {@code handle} is null with a
         * {@link LoadStatus#LOADED} status or handle is non-null with a
         * {@link LoadStatus#LOADING} status
         */
        public LoadKAMResult(final String handle, final LoadStatus status) {
            if (status == LoadStatus.LOADED) {
                if (handle == null) {
                    final String fmt = "handle may not be null (status is: %s)";
                    throw new InvalidArgument(format(fmt, status));
                }
            } else if (handle != null) {
                final String fmt = "handle must be null (status is: %s)";
                throw new InvalidArgument(format(fmt, status));
            }
            this.handle = handle;
            this.status = status;
        }

        /**
         * Returns the KAM handle which may be null if {@link #getStatus()} is
         * {@link LoadStatus#LOADING}.
         *
         * @return {@link String}; may be null
         */
        public String getHandle() {
            return handle;
        }

        /**
         * Returns the {@link LoadStatus}.
         *
         * @return {@link LoadStatus}
         */
        public LoadStatus getStatus() {
            return status;
        }
    }

}
