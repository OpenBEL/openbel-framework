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
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.deleteDirectory;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.PathConstants.SHA256_EXTENSION;
import static org.openbel.framework.common.Strings.UTF_8;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.core.df.cache.CacheUtil.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * DefaultCacheableResourceService implements a cacheable resource service that
 * handles BELFramework resource locations.
 * <p>
 * This service manages the cache at
 * {@link SystemConfiguration#getCacheDirectory()}.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DefaultCacheableResourceService extends
        DefaultResourceService implements
        CacheableResourceService {

    /**
     * Defines the temporary resource {@link File} list to be clean up after
     * use.
     */
    private final List<File> tempResources;

    public DefaultCacheableResourceService() {
        final List<File> fl = sizedArrayList(128);
        tempResources = Collections.synchronizedList(fl);

        Runtime.getRuntime().addShutdownHook(new ResourceCleanupThread());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResolvedResource resolveResource(ResourceType type, String location)
            throws ResourceDownloadError {
        String resFolderPath = getResourceFolderPath(type);
        String encLocation = encodeLocation(location);
        String cachePath = asPath(resFolderPath, encLocation);

        String cacheFile = CacheLookupService.DEFAULT_RESOURCE_FILE_NAME
                + type.getResourceExtension();

        String cacheLocation = asPath(cachePath, cacheFile);

        File resource = new File(cacheLocation);
        if (resource.exists()) {
            // cached resource exists, compare against remote hash
            HashState hashState = hashCompare(location, resource);

            switch (hashState) {
            case HASH_MISMATCH:
                File newResource = downloadResourceForUse(location, type);
                return new ResolvedResource(newResource, true);
            case HASH_MATCH:
            case MISSING_REMOTE_HASH:
            default:
                File copiedFile = copyResource(resource, location);
                return new ResolvedResource(copiedFile, false);
            }
        }

        // download the latest version because it is not in the cache
        File newResource = downloadResourceForUse(location, type);
        return new ResolvedResource(newResource, true);
    }

    /**
     * Compare hashes of the remote resource to the local resource. The
     * comparison can result in the following:
     * <ul>
     * <li>If the resource's remote hash does not exist then return
     * {@link HashState#MISSING_REMOTE_HASH}.</li>
     * <li>If the resource's remote hash matches the local resource's hash then
     * return {@link HashState#HASH_MATCH}</li>
     * <li>If the resource's remote hash does not match the local resource's
     * hash then return {@link HashState#HASH_MISMATCH}.</li>
     * </ul>
     *
     * @param location {@link String}, the remote resource location
     * @param resource {@link File}, the resource's local copy in the cache
     * @return {@link HashState} the state of the hash comparison which is
     * useful for deciding how to deal further with the resource
     * @throws IOException Thrown if there was an IO error in handling hash
     * files
     */
    private HashState hashCompare(String location, File resource)
            throws ResourceDownloadError {
        String tmpPath = getSystemConfiguration().getCacheDirectory()
                .getAbsolutePath();

        String remoteHashLocation = location + SHA256_EXTENSION;

        String tempHashPath = asPath(
                ResourceType.TMP_FILE.getResourceFolderName(),
                encodeLocation(location),
                CacheLookupService.DEFAULT_RESOURCE_FILE_NAME
                        + SHA256_EXTENSION);

        // download resource hash
        File remoteHashFile = null;
        try {
            remoteHashFile = resolveResource(remoteHashLocation, tmpPath,
                    tempHashPath);
        } catch (ResourceDownloadError e) {
            return HashState.MISSING_REMOTE_HASH;
        }

        String absPath = resource.getAbsolutePath();
        File localHashFile = new File(absPath + SHA256_EXTENSION);

        if (!localHashFile.exists() || !localHashFile.canRead()) {
            localHashFile = createLocalHash(resource, location);
        }

        // compare hash files
        try {
            if (areHashFilesEqual(localHashFile, remoteHashFile, location)) {
                // hashes are equal, so cached resource is the latest
                // return cached resource
                return HashState.HASH_MATCH;
            }

            // hashes are not equal
            return HashState.HASH_MISMATCH;
        } finally {
            // delete the downloaded temporary hash
            if (!remoteHashFile.delete()) {
                remoteHashFile.deleteOnExit();
            }
        }
    }

    /**
     * Download and copy the resource for use.
     *
     * @param resourceLocation {@link String}, the resource location
     * @param type {@link ResourceType}, the type of resource
     * @return the resource copy {@link File} ready for processing
     * @throws ResourceDownloadError Throw if an error occurred resolving or
     * copying the resource
     */
    private File downloadResourceForUse(String resourceLocation,
            ResourceType type)
            throws ResourceDownloadError {
        File downloadResource = downloadResource(resourceLocation, type);
        return copyResource(downloadResource, resourceLocation);
    }

    /**
     * Copy the resource to the system's temporary resources.
     *
     * @param resource {@link File}, the resource to copy from
     * @param resourceLocation {@link String}, the resource location
     * @return the resource copy {@link File} ready for processing
     * @throws ResourceDownloadError Throw if an error occurred copying the
     * resource
     */
    private File
            copyResource(final File resource, final String resourceLocation)
                    throws ResourceDownloadError {
        final String tmpPath = asPath(getSystemConfiguration()
                .getCacheDirectory().getAbsolutePath(),
                ResourceType.TMP_FILE.getResourceFolderName());
        final String uuidDirName = UUID.randomUUID().toString();

        // load copy file and create parent directories
        final File copyFile = new File(asPath(tmpPath, uuidDirName,
                resource.getName()));

        // copy to temp directory
        File resourceCopy =
                copyToDirectory(resource, resourceLocation, copyFile);

        // hash uuid temp directory for cleanup later
        tempResources.add(new File(asPath(tmpPath, uuidDirName)));

        return resourceCopy;
    }

    /**
     * Compares the hashes contained in the resource files. The hashes in the
     * files are compared case-insensitively in order to support hexadecimal
     * variations on a-f and A-F.
     *
     * @param hashFile1 {@link File}, the first hash file
     * @param hashFile2 {@link File}, the second hash file
     * @param remoteLocation {@link String}, the resource remote location
     * @return <tt>boolean</tt> indicating <tt>true</tt>, if the hash files are
     * equal, or <tt>false</tt> if the hash files are not equal
     * @throws IOException Thrown if an IO error occurred reading the resource
     * hashes
     */
    private boolean areHashFilesEqual(File hashFile1, File hashFile2,
            String remoteLocation) throws ResourceDownloadError {
        try {
            String resource1Hash = readFileToString(hashFile1, UTF_8);
            String resource2Hash = readFileToString(hashFile2, UTF_8);

            return resource1Hash.trim().equalsIgnoreCase(resource2Hash.trim());
        } catch (IOException e) {
            throw new ResourceDownloadError(remoteLocation, e.getMessage());
        }
    }

    /**
     * ResourceCleanupThread defines a {@link Thread} to delete all temporary
     * resource directories created through this cacheable resource service.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    private class ResourceCleanupThread extends Thread {

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            if (hasItems(tempResources)) {
                for (final File tempDir : tempResources) {
                    deleteDirectory(tempDir);
                }
            }
        }
    }

    /**
     * Defines different states with resolving a hashed resource.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    private enum HashState {
        MISSING_REMOTE_HASH,
        HASH_MATCH,
        HASH_MISMATCH;
    }
}
