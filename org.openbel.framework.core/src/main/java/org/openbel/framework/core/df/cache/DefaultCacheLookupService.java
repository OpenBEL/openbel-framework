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
package org.openbel.framework.core.df.cache;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.openbel.framework.common.BELUtilities.nulls;
import static org.openbel.framework.common.PathConstants.SHA256_EXTENSION;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.util.Hasher;

/**
 * CacheLookupService implements service-level lookup operations to the
 * BELFramework cache defined at {@link SystemConfiguration#getCacheDirectory()}.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DefaultCacheLookupService implements CacheLookupService {

    private static final Hasher hasher = Hasher.INSTANCE;

    /**
     * Constructs the lookup service.
     */
    public DefaultCacheLookupService() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CachedResource
            findInCache(ResourceType resourceType, String location) {
        if (nulls(resourceType, location)) {
            throw new InvalidArgument("parameter(s) are null");
        }

        String hashCompare = hasher.hashValue(location);

        for (CachedResource cacheEntry : getResources()) {
            if (hashCompare.equals(cacheEntry.getRemoteLocation())) {
                File resourceDirectory = cacheEntry.getLocalFile();
                for (File f : resourceDirectory.listFiles()) {
                    if (f.getName().equals(
                            CacheLookupService.DEFAULT_RESOURCE_FILE_NAME
                                    + resourceType.getResourceExtension())) {
                        return cacheEntry;
                    }
                }
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CachedResource> getResources() {
        SystemConfiguration config = getSystemConfiguration();
        File cacheDirectory = config.getCacheDirectory();

        List<CachedResource> cachedResource = new ArrayList<CachedResource>();

        for (File cacheSubDirectory : cacheDirectory.listFiles()) {
            if (cacheSubDirectory.isDirectory()) {
                ResourceType resourceType = ResourceType
                        .fromFolder(cacheSubDirectory
                                .getName());

                // if the resource type could not be determined, skip this folder
                if (resourceType == null) {
                    continue;
                }

                findResourcesOfType(cacheSubDirectory, resourceType,
                        cachedResource);
            }
        }

        return cachedResource;
    }

    /**
     * Find resources under a particular cache sub-directory identified by the
     * {@link ResourceType} <tt>resourceType</tt>. 
     * 
     * @param cacheSubDirectory {@link File}, the cache sub-directory
     * @param resourceType {@link ResourceType}, the resource type
     * @param cachedResource {@link List} of {@link CachedResource}, the list
     * of cached resources that are found
     */
    protected void findResourcesOfType(File cacheSubDirectory,
            ResourceType resourceType,
            List<CachedResource> cachedResource) {
        for (File resourceFile : cacheSubDirectory.listFiles()) {
            if (resourceFile.isDirectory()) {
                StringBuilder builder = new StringBuilder();
                builder.append(resourceFile.getAbsolutePath());
                builder.append(File.separator);
                builder.append(DEFAULT_RESOURCE_FILE_NAME);
                builder.append(resourceType.getResourceExtension());
                builder.append(SHA256_EXTENSION);

                String hash = null;
                try {
                    hash = readFileToString(new File(builder
                            .toString()));
                } catch (IOException e) {
                    // the hash is not required for the cached resource, so proceed
                }

                cachedResource.add(new CachedResource(resourceFile,
                        resourceFile.getName(), resourceType, hash));
            }
        }
    }
}
