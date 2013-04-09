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

import static org.apache.commons.io.FileUtils.copyFile;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.write;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.openbel.framework.common.BELUtilities.createDirectories;
import static org.openbel.framework.common.BELUtilities.nulls;
import static org.openbel.framework.common.PathConstants.SHA256_EXTENSION;
import static org.openbel.framework.common.Strings.UTF_8;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.filefilter.MagicNumberFileFilter;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.util.Hasher;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protocol.ResourceResolver;

/**
 * Static utility methods for dealing with the BELFramework cache.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class CacheUtil {

    /**
     * Prevent construction of static utility class.
     */
    private CacheUtil() {

    }

    /**
     * Defines the gzip magic number.
     */
    private static final byte[] GZIP_MAGIC_NUMBER = new byte[] {
            (byte) 0x1f,
            (byte) 0x8b };

    /**
     * Defines the static resource resolver instance to use in
     * {@link CacheUtil#resolveResource(String, String, String)}.
     */
    private static final ResourceResolver resolver = new ResourceResolver();

    /**
     * Defines the singleton {@link Hasher} to use when hashing locally cached
     * files.
     */
    private static final Hasher hasher = Hasher.INSTANCE;

    /**
     * Resolves a resource location to a local path.
     *
     * @param resourceLocation {@link String}, the resource location that
     * defines where to pull the file, which cannot be null
     * @param localPath {@link String}, the local path to store the resource
     * at, which cannot be null
     * @param localFileName {@link String}, the local file name to store the
     * resource as, which cannot be null
     * @return {@link File}, the resource resolved to a local file
     * @throws ResourceDownloadError Thrown if a resource download error
     * occurred while resolving the resource location
     * @throw {@link InvalidArgument} Thrown if any arguments are null
     */
    public static File resolveResource(String resourceLocation,
            String localPath,
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

    /**
     * Download the resource and hash the contents using {@link Hasher}.
     *
     * @param remoteLocation {@link String}, the remote resource location
     * @param type {@link ResourceType}, the resource type
     * @return {@link File} the downloaded resource file, decompressed if the
     * file is in GZIP format
     * @throws ResourceDownloadError Thrown if the remote resource could not
     * be downloaded
     * @throws IOException Thrown if an IO error occurred while handling the
     * resources
     */
    public static File
            downloadResource(String remoteLocation, ResourceType type)
                    throws ResourceDownloadError {
        String localResourcePath = getResourceFolderPath(type) + File.separator
                + encodeLocation(remoteLocation);

        String localResourceLocation =
                CacheLookupService.DEFAULT_RESOURCE_FILE_NAME
                        + type.getResourceExtension();

        File latestResource = resolveResource(remoteLocation,
                localResourcePath, localResourceLocation);

        // attempt to download resource hash
        String remoteHashLocation = remoteLocation + SHA256_EXTENSION;
        String localHashLocation = localResourceLocation + SHA256_EXTENSION;

        try {
            resolveResource(remoteHashLocation, localResourcePath,
                    localHashLocation);
        } catch (ResourceDownloadError e) {
            // remote hash does not exist but create local hash
            createLocalHash(latestResource, remoteLocation);
        }

        return latestResource;
    }

    /**
     * Returns the absolute path for the sub-directory resource folder using
     * the cache directory provided by
     * {@link SystemConfiguration#getCacheDirectory()}.
     *
     * <p>
     * If the sub-directory resource folder does not exist it will be created.
     * </p>
     *
     * <p>
     * If the {@link SystemConfiguration} object's cache directory does not
     * exist or the {@link SystemConfiguration} has not been created then an
     * {@link IllegalStateException} will be thrown.
     * </p>
     *
     * @return {@link String}, the absolute path for the sub-directory resource
     * folder
     * @throws IllegalStateException Thrown if the {@link SystemConfiguration}
     * object's cache directory does not exist, is not readable, is not writable,
     * or if the {@link SystemConfiguration} has not been created yet.
     */
    public static String getResourceFolderPath(ResourceType type) {
        String cachePath = getSystemConfiguration().getCacheDirectory()
                .getAbsolutePath();

        File cacheDir = new File(cachePath);
        if (!cacheDir.exists()) {
            throw new IllegalStateException(
                    "The system configuration's cache directory - "
                            + cacheDir.getAbsolutePath() + " - does not exist.");
        }

        if (!cacheDir.canRead()) {
            throw new IllegalStateException(
                    "The system configuration's cache directory - "
                            + cacheDir.getAbsolutePath()
                            + " - is not readable.");
        }

        if (!cacheDir.canWrite()) {
            throw new IllegalStateException(
                    "The system configuration's cache directory - "
                            + cacheDir.getAbsolutePath()
                            + " - is not writable.");
        }

        StringBuilder resourceFolderPath = new StringBuilder();
        resourceFolderPath.append(cachePath);

        if (!cachePath.endsWith("/")) {
            resourceFolderPath.append(File.separator);
        }

        resourceFolderPath.append(type.getResourceFolderName());
        return resourceFolderPath.toString();
    }

    /**
     * Creates a hash of the contents of the {@link File} <tt>resource</tt> and
     * writes it to a file alongside the real resource file.  The hash file
     * can be found by appending {@link ResourceType#SHA256_EXTENSION} to the
     * resource path.
     *
     * @param resource {@link File}, the local resource file
     * @param remoteLocation {@link String}, the remote resource location
     * @return {@link File}, the local hash file that was created
     * @throws IOException Thrown if there was an IO error reading the local
     * resource file, or writing the local hash file
     * @throws ResourceDownloadError
     */
    public static File createLocalHash(File resource, String remoteLocation)
            throws ResourceDownloadError {
        File localHashFile;
        try {
            String contents = readFileToString(resource);
            String hashValue = hasher.hashValue(contents);

            String absPath = resource.getAbsolutePath();
            String localHashPath = absPath + SHA256_EXTENSION;

            localHashFile = new File(localHashPath);
            write(localHashFile, hashValue, UTF_8);
            return localHashFile;
        } catch (IOException e) {
            throw new ResourceDownloadError(remoteLocation, e.getMessage());
        }
    }

    /**
     * Encodes the resource location as a one-way SHA256 hash.
     *
     * @param location {@link String}, the resource location
     * @return {@link String} the SHA256 hash of the resource location
     */
    public static String encodeLocation(String location) {
        return hasher.hashValue(location);
    }

    /**
     * Copies the resolved resource to the system's temporary directory pointed
     * to by the system property <tt>java.io.tmpdir</tt>.
     * <p>
     * GZIP Decompression occur on the file if the cached resource is found to
     * contain the {@link #GZIP_MAGIC_NUMBER}. See
     * {@link CacheUtil#copyWithDecompression(File, String, File)}.
     * </p>
     *
     * @param resource {@link File}, the resource file to copy from
     * @param resourceLocation {@link String}, the resource location url
     * @param copy {@link File}, the resource file to copy to
     * @return {@link File}, the resource file in the system's temp directory
     * @throws ResourceDownloadError Thrown if an IO error copying the resource
     */
    public static File copyToDirectory(File resource,
            final String resourceLocation, final File copy)
            throws ResourceDownloadError {
        copy.getParentFile().mkdirs();
        return copyWithDecompression(resource, resourceLocation, copy);
    }

    /**
     * Copies the <tt>resource</tt> to <tt>copy</tt>.  Decompression is
     * performed if the resource file is identified as a GZIP-encoded file.
     *
     * @param resource {@link File}, the resource to copy
     * @param resourceLocation {@link String}, the resource location url
     * @param copy {@link File}, the file to copy to
     * @return {@link File}, the copied file
     * @throws ResourceDownloadError Thrown if an IO error copying the resource
     */
    private static File copyWithDecompression(final File resource,
            final String resourceLocation, final File copy)
            throws ResourceDownloadError {
        GZIPInputStream gzipis = null;
        FileOutputStream fout = null;
        try {
            MagicNumberFileFilter mnff = new MagicNumberFileFilter(
                    GZIP_MAGIC_NUMBER);

            if (mnff.accept(resource)) {
                gzipis = new GZIPInputStream(new FileInputStream(
                        resource));
                byte[] buffer = new byte[8192];

                fout = new FileOutputStream(copy);

                int length;
                while ((length = gzipis.read(buffer, 0, 8192)) != -1) {
                    fout.write(buffer, 0, length);
                }
            } else {
                copyFile(resource, copy);
            }
        } catch (IOException e) {
            String msg = e.getMessage();
            ResourceDownloadError r =
                    new ResourceDownloadError(resourceLocation, msg);
            r.initCause(e);
            throw r;
        } finally {
            // clean up all I/O resources
            closeQuietly(fout);
            closeQuietly(gzipis);
        }

        return copy;
    }
}
