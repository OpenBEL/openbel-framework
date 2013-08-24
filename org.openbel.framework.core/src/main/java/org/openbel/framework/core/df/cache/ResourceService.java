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
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protocol.ResourceResolver;

/**
 * Defines a resource resolver for downloading any OpenBEL Framework resource.
 * This is a common service to resolve any OpenBEL Framework resource files.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface ResourceService {

    /**
     * Resolves a OpenBEL Framework resource by determining how to resolve the
     * <tt>resourceLocation</tt> using
     * {@link ResourceResolver#resolveResource(String, String)}.
     *
     * <p>
     * The file is resolved locally to:
     * <code>
     * localRoot + {@link File#separator} + {@link System#currentTimeMillis()} + localExtension
     * </code>
     * </p>
     *
     * @param resourceLocation {@link String}, the location of the resource,
     * which cannot be null
     * @param localPath {@link String}, the local folder path where the resource
     * should be saved to, which cannot be null
     * @param localFileName {@link String}, the local file name
     * @throws ResourceDownloadError Thrown if there was an I/O error resolving
     * the resource location.
     * @throws InvalidArgument Thrown if any of the arguments are null.
     */
    public File resolveResource(String resourceLocation, String localPath,
            String localFileName)
            throws ResourceDownloadError;
}
