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
 * CachedResource represents a resource that is available in the cache and
 * has its contents hashed.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public final class CachedResource {
    /**
     * Defines the local location of the resource.
     */
    private File localFile;

    /**
     * Defines the remote location for this resource.
     */
    private String remoteLocation;

    /**
     * Defines the type of this resource.
     */
    private ResourceType type;

    /**
     * Defines the hash for this local file content.
     */
    private String hash;

    /**
     * Constructs the CachedResource with the local and remote locations
     * as well as the content hash for the local file.
     *
     * @param localFile {@link File}, the local resource file, which cannot be
     * null
     * @param remoteLocation {@link String}, the remote location of the
     * resource, which cannot be null
     * @param type {@link ResourceType}, the resource type, which cannot be
     * null
     * @param hash {@link String}, the local file content hash, which may
     * be null
     * @throws InvalidArgument Thrown if the <tt>localFile</tt>,
     * <tt>remoteLocation</tt>, or <tt>type</tt> is null
     */
    public CachedResource(File localFile, String remoteLocation,
            ResourceType type, String hash) {
        if (localFile == null) {
            throw new InvalidArgument("localFile", localFile);
        }

        if (remoteLocation == null) {
            throw new InvalidArgument("remoteLocation", remoteLocation);
        }

        if (type == null) {
            throw new InvalidArgument("type", type);
        }

        this.localFile = localFile;
        this.remoteLocation = remoteLocation;
        this.type = type;
        this.hash = hash;
    }

    /**
     * Returns the local resource file.
     *
     * @return {@link String}, the local resource file, which will not be null
     */
    public File getLocalFile() {
        return localFile;
    }

    /**
     * Returns the remote location.
     *
     * @return {@link String}, the remote location, which will not be null
     */
    public String getRemoteLocation() {
        return remoteLocation;
    }

    /**
     * Returns the type of this resource.
     *
     * @return {@link ResourceType}, the type of this resource, which will not
     * be null
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Returns the local file's content hash.
     *
     * @return {@link String}, the local file's content hash, which may be
     * null
     */
    public String getHash() {
        return hash;
    }
}
