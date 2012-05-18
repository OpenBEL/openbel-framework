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
package org.openbel.framework.core.cache;

import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.core.df.cache.CacheUtil.downloadResource;
import static org.openbel.framework.core.df.cache.ResourceType.fromLocation;

import java.io.File;
import java.io.IOException;

import org.openbel.framework.common.Reportable;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceLocation;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CachedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * DefaultCacheManagerService implements a service to manage the framework
 * cache by loading, updating and purging cached resources.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DefaultCacheManagerService implements CacheManagerService {
    private final CacheLookupService lookupService;
    private final NamespaceService nsService;
    private final Reportable reportable;

    public DefaultCacheManagerService(CacheLookupService lookupService,
            NamespaceService nsService, Reportable reportable) {
        this.lookupService = lookupService;
        this.nsService = nsService;
        this.reportable = reportable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purgeResources() throws IOException {
        File cacheDirectory = getSystemConfiguration().getCacheDirectory();

        for (File cacheFile : cacheDirectory.listFiles()) {
            if (cacheFile.isDirectory()) {
                ResourceType rt = ResourceType.fromFolder(cacheFile.getName());
                if (rt != null) {
                    // we own it, so delete it.
                    deleteDirectory(cacheFile);
                    reportable.output("Deleted cache directory: "
                            + cacheFile.getAbsolutePath());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateResourceInCache(ResourceType type, String location) {
        try {
            if (ResourceType.NAMESPACES.equals(type)) {
                rebuildNamespace(location);
            } else {
                downloadResource(location, type);
            }

            reportable.output("Updated resource: " + location);
        } catch (ResourceDownloadError e) {
            reportable.error(e.getUserFacingMessage());
        } catch (IndexingFailure e) {
            reportable.error(e.getUserFacingMessage());
        } catch (IOException e) {
            logExceptionSimple(location, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateResourceIndexInCache(Index index) {
        // update annotations in the index
        if (hasItems(index.getAnnotationDefinitionResources())) {
            for (ResourceLocation rloc : index
                    .getAnnotationDefinitionResources()) {
                try {
                    downloadResource(rloc.getResourceLocation(),
                            ResourceType.ANNOTATIONS);
                    reportable.output("Updated annotation resource: "
                            + rloc.getResourceLocation());
                } catch (ResourceDownloadError e) {
                    reportable.error(e.getUserFacingMessage());
                }
            }
        }

        // update namespaces in the index
        if (hasItems(index.getNamespaceResources())) {
            for (ResourceLocation rloc : index.getNamespaceResources()) {
                try {
                    rebuildNamespace(rloc.getResourceLocation());
                    reportable.output("Updated namespace resource: "
                            + rloc.getResourceLocation());
                } catch (ResourceDownloadError e) {
                    reportable.error(e.getUserFacingMessage());
                } catch (IndexingFailure e) {
                    reportable.error(e.getUserFacingMessage());
                } catch (IOException e) {
                    logExceptionSimple(rloc.getResourceLocation(), e);
                }
            }
        }

        // update equivalences in the index
        if (hasItems(index.getEquivalenceResources())) {
            for (ResourceLocation rloc : index.getEquivalenceResources()) {
                try {
                    downloadResource(rloc.getResourceLocation(),
                            ResourceType.EQUIVALENCES);
                    reportable.output("Updated equivalence resource: "
                            + rloc.getResourceLocation());
                } catch (ResourceDownloadError e) {
                    reportable.error(e.getUserFacingMessage());
                }
            }
        }

        // update protein family
        if (index.getProteinFamilyResource() != null) {
            String pfamLocation =
                    index.getProteinFamilyResource().getResourceLocation();
            try {
                downloadResource(pfamLocation, fromLocation(pfamLocation));
                reportable.output("Updated protein family resource: "
                        + pfamLocation);
            } catch (ResourceDownloadError e) {
                reportable.error(e.getUserFacingMessage());
            }
        }

        // update named complexes
        if (index.getNamedComplexesResource() != null) {
            String ncLocation =
                    index.getNamedComplexesResource().getResourceLocation();
            try {
                downloadResource(ncLocation, fromLocation(ncLocation));
                reportable.output("Updated named complex resource: "
                        + ncLocation);
            } catch (ResourceDownloadError e) {
                reportable.error(e.getUserFacingMessage());
            }
        }

        // update gene scaffolding
        if (index.getGeneScaffoldingResource() != null) {
            String gsLocation =
                    index.getGeneScaffoldingResource().getResourceLocation();
            try {
                downloadResource(gsLocation, fromLocation(gsLocation));
                reportable.output("Updated gene scaffolding resource: "
                        + gsLocation);
            } catch (ResourceDownloadError e) {
                reportable.error(e.getUserFacingMessage());
            }
        }
    }

    private void rebuildNamespace(String resourceLocation)
            throws IOException, ResourceDownloadError, IndexingFailure {
        CachedResource cachedResource = lookupService.findInCache(
                ResourceType.NAMESPACES, resourceLocation);

        // if cached resource exists, remove cached namespace folder
        // to force resolving and indexing of namespace
        if (cachedResource != null) {
            File resourceDirectory = cachedResource.getLocalFile()
                    .getParentFile();
            deleteDirectory(resourceDirectory);
        }

        // compile namespace
        nsService.compileNamespace(resourceLocation);
    }

    /**
     * Reports an {@link Exception} as an error to the {@link Reportable}.
     * The error output logged will use the <tt>location</tt> and the
     * exception's message if it is not null.
     *
     * @param location {@link String}, the resource location behind the
     * exception
     * @param e {@link Exception}, the exception to output message from
     */
    private void logExceptionSimple(String location, Exception e) {
        StringBuilder bldr = new StringBuilder();
        bldr.append("ERROR UPDATING CACHE");
        if (location != null) {
            bldr.append(" for ");
            bldr.append(location);
        }
        bldr.append("\n\treason: ");

        final String msg = e.getMessage();
        if (msg != null)
            bldr.append(msg);
        else
            bldr.append("Unknown");

        bldr.append("\n");

        reportable.error(bldr.toString());
    }
}
