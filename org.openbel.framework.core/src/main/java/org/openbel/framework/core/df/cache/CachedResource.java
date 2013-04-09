/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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
