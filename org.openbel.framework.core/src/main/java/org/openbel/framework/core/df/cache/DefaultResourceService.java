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

import static org.openbel.framework.common.BELUtilities.createDirectories;
import static org.openbel.framework.common.BELUtilities.nulls;

import java.io.File;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protocol.ResourceResolver;

/**
 * Implements a default resource service that can download BELFramework
 * resources.
 *
 * <p>
 * This class is intended to download resources that are used and then thrown
 * away. If resource caching is desired then use {@link CacheableResourceService}.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DefaultResourceService implements ResourceService {

    /**
     * Defines the {@link ResourceResolver} instance that actually resolves the
     * resource location passed in.
     */
    private final ResourceResolver resolver;

    /**
     * Creates a resource resolver service implementation.
     */
    public DefaultResourceService() {
        resolver = new ResourceResolver();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File resolveResource(String resourceLocation, String localPath,
            String localFileName)
            throws ResourceDownloadError {
        if (nulls(resourceLocation, localPath, localFileName)) {
            throw new InvalidArgument("null argument(s) provided");
        }

        createDirectories(localPath);

        StringBuilder builder = new StringBuilder();
        builder.append(localPath).append(File.separator).append(localFileName);

        // resolve resource
        File resourceFile = resolver.resolveResource(resourceLocation,
                builder.toString());
        return resourceFile;
    }
}
