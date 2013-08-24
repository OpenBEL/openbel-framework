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
