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
