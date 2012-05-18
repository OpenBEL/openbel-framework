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
package org.openbel.framework.core.namespace;

import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.nulls;
import static org.openbel.framework.common.PathConstants.NAMESPACE_ROOT_DIRECTORY_NAME;
import static org.openbel.framework.common.Strings.INVALID_SYMBOLS;
import static org.openbel.framework.core.df.cache.ResourceType.NAMESPACES;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.core.compiler.SymbolWarning;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.CachedResource;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.indexer.JDBMNamespaceLookup;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * DefaultNamespaceService implements a service to handle all operations for
 * working with namespaces.
 * <p>
 * This class <strong><em>is thread-safe</em></strong> and allows concurrent
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public final class DefaultNamespaceService implements NamespaceService,
        Destroyable {

    private static final String NS_INDEX_FILE_NAME = "namespace.index";
    private boolean running;

    /**
     * Defines the map of open namespace indexes. The entries are keyed by
     * {@link Namespace} resource location with the {@link JDBMNamespaceLookup} index as
     * the value.
     * <p>
     * This map in thread-safe as it uses {@link ConcurrentHashMap}
     * </p>
     */
    private final Map<String, JDBMNamespaceLookup> openNamespaces =
            new ConcurrentHashMap<String, JDBMNamespaceLookup>();

    /**
     * Defines the map of available namespaces. The entries are the the
     * {@link Namespace} resource locations.
     * <p>
     * This map in thread-safe as it uses {@link ConcurrentHashMap} to create a
     * concurrent hash set.
     * </p>
     */
    private final Set<String> availableNamespaces = Collections
            .newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    /**
     * Defines the resource cache service dependency.
     */
    private final CacheableResourceService resourceCache;

    /**
     * Defines the cache lookup service dependency.
     */
    private final CacheLookupService cacheLookupService;

    /**
     * Defines the namespace indexer service dependency.
     */
    private final NamespaceIndexerService namespaceIndexerService;

    private boolean serviceDestroyed = false;

    public DefaultNamespaceService(
            final CacheableResourceService resourceCache,
            final CacheLookupService cacheLookupService,
            final NamespaceIndexerService namespaceIndexerService) {
        this.resourceCache = resourceCache;
        this.cacheLookupService = cacheLookupService;
        this.namespaceIndexerService = namespaceIndexerService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen(String resourceLocation) {
        if (resourceLocation == null) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }

        return openNamespaces.containsKey(resourceLocation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void compileNamespace(String resourceLocation)
            throws ResourceDownloadError, IndexingFailure {
        if (resourceLocation == null) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }

        synchronized (resourceLocation) {
            // compile
            final String indexPath = doCompile(resourceLocation);

            // close opened namespace index
            closeNamespace(resourceLocation);

            // reopen namespace index
            openNamespace(resourceLocation, indexPath);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verify(Parameter p) throws NamespaceSyntaxWarning,
            IndexingFailure,
            ResourceDownloadError {
        if (nulls(p, p.getValue())) {
            throw new InvalidArgument("p", p);
        }

        Namespace ns = p.getNamespace();
        if (ns == null) {
            return;
        }

        String resourceLocation = ns.getResourceLocation();
        if (resourceLocation == null) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }

        // check and open namespace for this parameter, if it has one
        synchronized (resourceLocation) {
            if (!isOpen(resourceLocation)) {
                final String indexPath = doCompile(resourceLocation);
                openNamespace(resourceLocation, indexPath);
            }
        }

        doVerify(p);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verify(Statement s) throws SymbolWarning, IndexingFailure,
            ResourceDownloadError {

        List<Parameter> parameters = s.getAllParameters();
        // first iteration to open all namespaces
        for (Parameter p : parameters) {
            if (p.getNamespace() == null
                    || p.getNamespace().getResourceLocation() == null) {
                continue;
            }
            String resourceLocation = p.getNamespace().getResourceLocation();

            synchronized (resourceLocation) {
                if (!isOpen(resourceLocation)) {
                    final String indexPath = doCompile(resourceLocation);
                    openNamespace(resourceLocation, indexPath);
                }
            }
        }

        final List<NamespaceSyntaxWarning> exceptions =
                new ArrayList<NamespaceSyntaxWarning>();
        for (Parameter p : s.getAllParameters()) {
            Namespace ns = p.getNamespace();
            if (ns == null) {
                continue;
            }

            try {
                doVerify(p);
            } catch (NamespaceSyntaxWarning w) {
                exceptions.add(w);
            }
        }

        if (!exceptions.isEmpty()) {
            // TODO review name
            final String name = s.toString();
            final String fmt = INVALID_SYMBOLS;
            final String msg = format(fmt, exceptions.size());
            throw new SymbolWarning(name, msg, exceptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verify(Document d) throws SymbolWarning, IndexingFailure,
            ResourceDownloadError {
        final List<NamespaceSyntaxWarning> exceptions =
                new ArrayList<NamespaceSyntaxWarning>();

        // open document namespaces if necessary
        for (Namespace ns : d.getAllNamespaces()) {
            String resourceLocation = ns.getResourceLocation();

            synchronized (resourceLocation) {
                if (!isOpen(resourceLocation)) {
                    final String indexPath = doCompile(resourceLocation);
                    openNamespace(resourceLocation, indexPath);
                }
            }
        }

        for (final Parameter p : d.getAllParameters()) {
            Namespace ns = p.getNamespace();
            if (ns == null) {
                continue;
            }

            try {
                doVerify(p);
            } catch (NamespaceSyntaxWarning e) {
                exceptions.add(e);
            }
        }

        if (!exceptions.isEmpty()) {
            final String name = d.getName();
            final String fmt = INVALID_SYMBOLS;
            final String msg = format(fmt, exceptions.size());
            throw new SymbolWarning(name, msg, exceptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String lookup(Parameter p) throws NamespaceSyntaxWarning,
            IndexingFailure {
        if (nulls(p, p.getValue())) {
            throw new InvalidArgument("parameter is null");
        }

        Namespace ns = p.getNamespace();
        if (ns == null) {
            return null;
        }

        String resourceLocation = ns.getResourceLocation();
        if (resourceLocation == null) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }

        // get opened namespace and lookup namespace parameter encoding
        JDBMNamespaceLookup il = openNamespaces.get(ns.getResourceLocation());
        if (il == null) {
            final String fmt = "Namespace '%s' is not open.";
            final String msg = String.format(fmt, resourceLocation);
            throw new IndexingFailure(resourceLocation, msg);
        }

        String encoding = il.lookup(p.getValue());

        if (encoding == null) {
            String rl = ns.getResourceLocation();
            String pref = ns.getPrefix();
            throw new NamespaceSyntaxWarning(rl, pref, p.getValue());
        }
        return encoding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> search(String resourceLocation, Pattern pattern)
            throws IndexingFailure, ResourceDownloadError {
        if (resourceLocation == null) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }
        if (pattern == null) {
            throw new InvalidArgument("pattern", pattern);
        }

        // check and open namespace for this resource location
        synchronized (resourceLocation) {
            if (!isOpen(resourceLocation)) {
                String indexPath = doCompile(resourceLocation);
                openNamespace(resourceLocation, indexPath);
            }
        }

        return doSearch(resourceLocation, pattern);
    }

    /**
     * Do compilation of the namespace resource.
     *
     * @param resourceLocation {@link String}, the resource location to compile
     * @throws IndexingFailure Thrown if an error occurred while indexing
     * @throws ResourceDownloadError Thrown if an error occurred downloading
     * resource
     */
    private String doCompile(String resourceLocation) throws IndexingFailure,
            ResourceDownloadError {
        final ResolvedResource resolved =
                resourceCache.resolveResource(NAMESPACES, resourceLocation);
        final File resourceCopy = resolved.getCacheResourceCopy();
        namespaceIndexerService.indexNamespace(
                resourceLocation, resolved.getCacheResourceCopy());

        final String indexPath = asPath(resourceCopy.getParent(),
                NAMESPACE_ROOT_DIRECTORY_NAME, NS_INDEX_FILE_NAME);
        return indexPath;
    }

    /**
     * Do a search on the values in namespace specified by the resource location
     *
     * @param resourceLocation resource location, e.g.,
     * "http://resource.belframework.org/belframework/1.0/ns/chebi-ids.belns" ,
     * can not be null
     * @param pattern {@link Pattern}, can not be null
     * @return {@link Set} of {@link String}s containing values that match
     */
    private Set<String> doSearch(String resourceLocation, Pattern pattern) {
        // get opened namespace
        JDBMNamespaceLookup il = openNamespaces.get(resourceLocation);

        Set<String> results = new HashSet<String>();
        for (String value : il.getKeySet()) {
            if (pattern.matcher(value).matches()) {
                results.add(value);
            }
        }

        return results;
    }

    /**
     * Do namespace value verification against a resource location. This
     * implementation assumes the namespace has been open prior to execution.
     *
     * @param p {@link Parameter}, the parameter to verify namespace value for
     * which cannot be null and must have a non-null namespace and value
     * @throws NamespaceSyntaxWarning Thrown if parameter's {@link Namespace} is
     * not null and it does not contain the parameter's value
     * @throws InvalidArgument Thrown if <tt>p</tt> argument is null, its value
     * is null, or if its namespace's resource location is null
     */
    private void doVerify(Parameter p) throws NamespaceSyntaxWarning {
        if (p.getValue() == null) {
            throw new InvalidArgument("parameter value is null");
        }

        Namespace ns = p.getNamespace();

        String resourceLocation = ns.getResourceLocation();
        if (resourceLocation == null) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }

        // get opened namespace and lookup namespace parameter encoding
        JDBMNamespaceLookup il = openNamespaces.get(ns.getResourceLocation());
        if (il == null) {
            throw new IllegalStateException("namespace index is not open.");
        }

        String encoding = il.lookup(p.getValue());
        if (encoding == null) {
            throw new NamespaceSyntaxWarning(ns.getResourceLocation(),
                    ns.getPrefix(),
                    p.getValue());
        }
    }

    /**
     * Determines if the {@link Namespace} resource location can be opened for
     * lookups. To be opened
     *
     * @param resourceLocation
     * @return
     */
    private boolean canOpen(String resourceLocation) {
        if (resourceLocation == null) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }

        synchronized (resourceLocation) {
            // find the resource in the cache
            CachedResource cachedResource = cacheLookupService
                    .findInCache(NAMESPACES, resourceLocation);

            if (cachedResource == null) {
                return false;
            }

            String indexPath = asPath(
                    cachedResource.getLocalFile().getAbsolutePath(),
                    NS_INDEX_FILE_NAME);

            JDBMNamespaceLookup il = new JDBMNamespaceLookup(indexPath);
            try {
                il.open();
                il.close();
            } catch (IOException e) {
                return false;
            }

            return true;
        }
    }

    private void openNamespace(final String resourceLocation,
            final String indexPath) throws IndexingFailure {
        if (resourceLocation == null) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }

        if (openNamespaces.containsKey(resourceLocation)) {
            throw new IllegalStateException("namespace " + resourceLocation
                    + " is already open.");
        }

        synchronized (resourceLocation) {
            JDBMNamespaceLookup il = new JDBMNamespaceLookup(indexPath);
            try {
                il.open();
            } catch (IOException e) {
                final String fmt = "Failed to open namespace '%s'.";
                final String msg = String.format(fmt, resourceLocation);
                throw new IndexingFailure(resourceLocation, msg, e);
            }

            openNamespaces.put(resourceLocation, il);
        }
    }

    private void closeNamespace(final String resourceLocation)
            throws IndexingFailure {
        synchronized (resourceLocation) {
            JDBMNamespaceLookup jdbmlookup =
                    openNamespaces.get(resourceLocation);
            if (jdbmlookup != null) {
                try {
                    jdbmlookup.close();
                    openNamespaces.remove(resourceLocation);
                } catch (IOException e) {
                    final String fmt = "Failed to close namespace '%s'.";
                    final String msg = String.format(fmt, resourceLocation);
                    throw new IndexingFailure(resourceLocation, msg, e);
                }
            }
        }
    }

    private void closeNamespaces() throws IndexingFailure {
        // close each JDBM btree index in turn
        for (Map.Entry<String, JDBMNamespaceLookup> entry : openNamespaces
                .entrySet()) {
            closeNamespace(entry.getKey());
        }

        // clear out the open namespaces map
        openNamespaces.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() throws DestroyFailedException {
        try {
            closeNamespaces();
            serviceDestroyed = true;
        } catch (IndexingFailure e) {
            throw new DestroyFailedException(
                    "Namespace service cannot be destroyed.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDestroyed() {
        return serviceDestroyed;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        List<CachedResource> resources = cacheLookupService.getResources();

        for (CachedResource resource : resources) {
            if (NAMESPACES.equals(resource.getType())) {
                String resourceLocation = resource.getRemoteLocation();

                if (canOpen(resourceLocation)) {
                    availableNamespaces.add(resourceLocation);
                }
            }
        }
        running = true;
    }

    public void stop() {
    }
}
