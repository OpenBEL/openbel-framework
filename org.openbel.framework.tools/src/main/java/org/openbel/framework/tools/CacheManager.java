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
package org.openbel.framework.tools;

import static org.openbel.framework.common.Strings.HASH;
import static org.openbel.framework.common.Strings.LIST_CACHE;
import static org.openbel.framework.common.Strings.LOAD_INDEX_FILE;
import static org.openbel.framework.common.Strings.LOAD_INDEX_FROM_SYSCONFIG;
import static org.openbel.framework.common.Strings.LOAD_RESOURCE;
import static org.openbel.framework.common.Strings.PURGE_CACHE;
import static org.openbel.framework.common.Strings.SYSTEM_CONFIG_PATH;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.common.enums.ExitCode.GENERAL_FAILURE;
import static org.openbel.framework.core.StandardOptions.ARG_SYSCFG;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_SYSCFG;
import static org.openbel.framework.core.StandardOptions.SHRT_OPT_SYSCFG;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.io.FileUtils;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.common.util.Hasher;
import org.openbel.framework.core.CommandLineApplication;
import org.openbel.framework.core.cache.CacheManagerService;
import org.openbel.framework.core.cache.DefaultCacheManagerService;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.CachedResource;
import org.openbel.framework.core.df.cache.DefaultCacheLookupService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.openbel.framework.core.namespace.DefaultNamespaceService;
import org.openbel.framework.core.namespace.NamespaceIndexerService;
import org.openbel.framework.core.namespace.NamespaceIndexerServiceImpl;
import org.openbel.framework.core.namespace.NamespaceService;

/**
 * CacheManager is a {@link CommandLineApplication} to manage the BELFramework
 * cache and provides the following functionality:<ul>
 * <li>List the contents of the cache.</li>
 * <li>Load a resource file into the cache.</li>
 * <li>Load resources from a resource index file into the cache.</li>
 * <li>Update all resources in the cache.</li>
 * <li>Purge all resources in the cache.</li>
 * <li>Generates a hash for a local file.</li></ul>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class CacheManager extends CommandLineApplication {

    private final static String CACHE_MANAGER_APP_NAME = "Cache Manager";
    private final static String CACHE_MANAGER_APP_DESC =
            "Manages the OpenBEL Framework resource cache.";

    private final static String LIST_CACHE_OPTION = "l";
    private final static String CACHE_RESOURCE_OPTION = "a";
    private final static String CACHE_INDEX_FILE = "i";
    private final static String CACHE_SYSCONFIG_FILE_OPTION = "c";
    private final static String PURGE_CACHE_OPTION = "p";
    private final static String GENERATE_CHECKSUM_OPTION = "g";

    /**
     * Defines the cache lookup to use.
     */
    private final CacheLookupService cacheLookupService;

    /**
     * Defines the cache manager to use.
     */
    private final CacheManagerService cacheMgrService;

    /**
     * Defines the resource cache service to use.
     */
    private final CacheableResourceService cacheService;

    /**
     * Defines the system configuration to use.
     */
    private SystemConfiguration config = null;

    /**
     * Constructs the CacheManager with command-line arguments.
     *
     * @param args {@link String}[] the command-line arguments
     */
    public CacheManager(String[] args) {
        super(args);

        final SimpleOutput reportable = new SimpleOutput();
        reportable.setErrorStream(System.err);
        reportable.setOutputStream(System.out);
        setReportable(reportable);

        printApplicationInfo();

        initializeSystemConfiguration();

        config = getSystemConfiguration();
        cacheLookupService = new DefaultCacheLookupService();
        cacheService = new DefaultCacheableResourceService();
        final NamespaceIndexerService nsindexer =
                new NamespaceIndexerServiceImpl();
        final NamespaceService namespace = new DefaultNamespaceService(
                cacheService, cacheLookupService, nsindexer);
        cacheMgrService = new DefaultCacheManagerService(
                cacheLookupService, namespace, getReportable());

        Option[] options = getOptions();
        if (options.length == 0) {
            printUsage();
            bail(GENERAL_FAILURE);
        }

        reportable.output("Using cache directory: "
                + config.getCacheDirectory().getAbsolutePath());

        for (int i = 0; i < options.length; i++) {
            if (i != 0) {
                reportable.output("");
            }

            handleOption(options[i]);
        }
    }

    /**
     * Handle the cli option that was provided.
     *
     * @param option {@link Option} the current option to handle
     */
    private void handleOption(Option option) {
        if (LIST_CACHE_OPTION.equals(option.getOpt())) {
            handleListCache();
        } else if (CACHE_RESOURCE_OPTION.equals(option.getOpt())) {
            handleLoadResource(option);
        } else if (CACHE_SYSCONFIG_FILE_OPTION.equals(option.getOpt())) {
            handleLoadDefaultIndex();
        } else if (CACHE_INDEX_FILE.equals(option.getOpt())) {
            handleLoadIndex(option.getValue());
        } else if (PURGE_CACHE_OPTION.equals(option.getOpt())) {
            handlePurgeCache();
        } else if (GENERATE_CHECKSUM_OPTION.equals(option.getOpt())) {
            handleGenerateHash(option);
        }
    }

    /**
     * Handle the list cache option.
     */
    protected void handleListCache() {
        reportable.output("Listing resources in the cache");
        List<CachedResource> resources = cacheLookupService.getResources();

        if (resources.isEmpty()) {
            reportable.output("No resources in the cache.");
        }

        for (CachedResource resource : resources) {
            reportable.output("\t" + resource.getType().getResourceFolderName()
                    + "\t" + resource.getRemoteLocation());
        }
    }

    protected void handleLoadDefaultIndex() {
        String resourceIndex = config.getResourceIndexURL();
        handleLoadIndex(resourceIndex);
    }

    /**
     * Handle the load resource option.
     *
     * @param option {@link Option} the option to fetch the cli argument from
     */
    protected void handleLoadResource(Option option) {
        String resourceLocation = option.getValue();

        reportable.output("Loading resource into the cache:");
        reportable.output("  " + resourceLocation);

        ResourceType type = ResourceType.fromLocation(resourceLocation);

        if (type == null) {
            reportable
                    .error("Resource type cannot be determined, consult help with -h.");
            bail(GENERAL_FAILURE);
        }

        cacheMgrService.updateResourceInCache(type, resourceLocation);
    }

    /**
     * Handle the load index option.
     *
     * @param indexLocation {@link String} the index location
     */
    protected void handleLoadIndex(String indexLocation) {
        reportable.output("Loading resource from index file:");
        reportable.output("  " + indexLocation);

        File indexFile = new File(indexLocation);
        if (!indexFile.exists() || !indexFile.canRead()) {
            // try the index as an online resource.
            try {
                ResolvedResource resolvedResource = cacheService
                        .resolveResource(ResourceType.RESOURCE_INDEX,
                                indexLocation);
                indexFile = resolvedResource.getCacheResourceCopy();
            } catch (Exception e) {
                reportable.error("Index could not be read");
                bail(GENERAL_FAILURE);
            }
        }

        try {
            ResourceIndex.INSTANCE.loadIndex(indexFile);
        } catch (Exception e) {
            reportable.error("Unable to load index");
            reportable.error("Reason: " + e.getMessage());
            bail(GENERAL_FAILURE);
        }

        Index index = ResourceIndex.INSTANCE.getIndex();
        cacheMgrService.updateResourceIndexInCache(index);
    }

    /**
     * Handle the purge cache option.
     */
    protected void handlePurgeCache() {
        reportable.output("Purging all resources from the cache");

        try {
            cacheMgrService.purgeResources();
        } catch (Exception e) {
            reportable.error("Unable to remove all resources in the cache");
            reportable.error("Reason: " + e.getMessage());
            bail(GENERAL_FAILURE);
        }
    }

    /**
     * Handle the generate hash option.
     *
     * @param option {@link Option} the option to fetch the cli argument from
     */
    protected void handleGenerateHash(Option option) {
        String hashFileLocation = option.getValue();

        reportable.output("Generating checksum for file: " + hashFileLocation);

        File hashFile = new File(hashFileLocation);

        if (!hashFile.exists() || !hashFile.canRead()) {
            reportable.error("File cannot be read");
            bail(GENERAL_FAILURE);
        }

        String hashContents = null;
        try {
            hashContents = FileUtils.readFileToString(hashFile);
        } catch (IOException e) {
            reportable.error("Unable to read file");
            reportable.error("Reason: " + e.getMessage());
            bail(GENERAL_FAILURE);
        }

        Hasher hasher = Hasher.INSTANCE;
        try {
            String generatedhash = hasher.hashValue(hashContents);
            reportable.output("Checksum: " + generatedhash);
        } catch (Exception e) {
            reportable.error("Unable to created checksum");
            reportable.error("Reason: " + e.getMessage());
            bail(GENERAL_FAILURE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationName() {
        return CACHE_MANAGER_APP_NAME;
    }

    /**
     * Returns {@value #CACHE_MANAGER_APP_NAME}.
     *
     * @return String
     */
    @Override
    public String getApplicationShortName() {
        // App name is short enough.
        return getApplicationName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationDescription() {
        return CACHE_MANAGER_APP_DESC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        StringBuilder bldr = new StringBuilder();
        bldr.append(" [-l ]");
        bldr.append(" [-a <resource url>]");
        bldr.append(" [-i <resource index url>]");
        bldr.append(" [-p ]");
        bldr.append(" [-c ]");
        return bldr.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Option> getCommandLineOptions() {
        final List<Option> ret = new LinkedList<Option>();

        String help;

        help = LIST_CACHE;
        ret.add(new Option(LIST_CACHE_OPTION, "list-cache", false, help));

        help = LOAD_RESOURCE;
        ret.add(new Option(CACHE_RESOURCE_OPTION, "cache-resource", true, help));

        help = LOAD_INDEX_FILE;
        ret.add(new Option(CACHE_INDEX_FILE, "index-file", true, help));

        help = LOAD_INDEX_FROM_SYSCONFIG;
        ret.add(new Option(CACHE_SYSCONFIG_FILE_OPTION, "sysconfig-index-file",
                false, help));

        help = PURGE_CACHE;
        ret.add(new Option(PURGE_CACHE_OPTION, "purge", false, help));

        help = HASH;
        ret.add(new Option(GENERATE_CHECKSUM_OPTION, "generate-checksum", true,
                help));

        help = SYSTEM_CONFIG_PATH;
        Option o = new Option(SHRT_OPT_SYSCFG, LONG_OPT_SYSCFG, true, help);
        o.setArgName(ARG_SYSCFG);
        ret.add(o);

        return ret;
    }

    /**
     * Main entrance to the application...Exit, stage left.
     *
     * @param args {@link String}[] the command-line arguments
     */
    public static void main(String... args) {
        CacheManager cm = new CacheManager(args);
        cm.end();
    }
}
