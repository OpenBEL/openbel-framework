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
package org.openbel.framework.api;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.isNumeric;
import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.common.index.ResourceLocation;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;
import org.openbel.framework.core.df.beldata.namespace.NamespaceHeader;
import org.openbel.framework.core.df.beldata.namespace.NamespaceHeaderParser;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * {@link Namespaces} provides access to all namespaces supported by the
 * BEL Framework instance.
 *
 * <p>
 * The object is a singleton and is created/obtained
 * by calling the synchronized static {@link Namespaces#loadNamespaces()}.
 * Subsequent calls to {@link Namespaces#loadNamespaces()} will simply return
 * the singleton instance without requiring a reload.  This implies that
 * {@link Namespaces namespaces} cannot be reloaded without a JVM restart.
 * </p>
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class Namespaces {

    private static Namespaces instance;
    private static Map<String, NamespaceHeader> headers;

    private Namespaces() {
        // private singleton constructor
    }

    /**
     * Retrieve the {@link NamespaceHeader namespace headers} for all supported
     * namespaces.
     *
     * @return {@link Collection} of {@link NamespaceHeader}, which cannot be
     * {@code null} but could be empty
     */
    public Collection<NamespaceHeader> getHeaders() {
        return headers.values();
    }

    /**
     * Finds the {@link NamespaceHeader namespace header} for the namespace's
     * {@link String resource location}.
     *
     * @param rloc namespace {@link String resource location}, which cannot be
     * {@code null}
     * @return {@link NamespaceHeader namespace header} or {@code null} if one
     * is not found
     * @throws InvalidArgument Thrown if {@code rloc} is {@code null} or empty
     */
    public NamespaceHeader findNamespaceHeader(final String rloc) {
        if (noLength(rloc)) {
            throw new InvalidArgument("rloc", rloc);
        }

        return headers.get(rloc);
    }

    /**
     * Retrieve {@link Namespace namespaces} for a species tax id.
     *
     * @param speciesTaxId {@code int} species tax id
     * @return unmodifiable {@link List} of {@link Namespace}, which may be
     * empty
     */
    public List<Namespace> getSpeciesNamespaces(final int speciesTaxId) {
        List<Namespace> rlocs = sizedArrayList(headers.size());
        for (final Map.Entry<String, NamespaceHeader> e : headers.entrySet()) {
            NamespaceHeader hdr = e.getValue();
            if (matchesSpecies(hdr, speciesTaxId)) {
                rlocs.add(new Namespace(hdr.getNamespaceBlock().getKeyword(), e.getKey()));
            }
        }
        return Collections.unmodifiableList(rlocs);
    }

    /**
     * Load {@link Namespace namespaces} supported by the BEL Framework
     * instance.  This synchrononized static method creates the singleton
     * instance of {@link Namespaces} and loads the namespaces on first call.
     *
     * @return {@link Namespaces} singleton instance
     * @throws IOException Thrown if an error occurred loading BEL Framework
     * resources
     * @throws ResourceDownloadError Thrown if a download error occurred with
     * a namespace resource location
     * @throws XMLStreamException Thrown if an xml error occurred parsing the
     * resource index file
     * @throws BELDataMissingPropertyException Thrown if the namespace
     * header definition is invalid
     * @throws BELDataConversionException Thrown if the namespace
     * header definition is invalid
     */
    public static synchronized Namespaces loadNamespaces() throws IOException,
            ResourceDownloadError, XMLStreamException,
            BELDataMissingPropertyException, BELDataConversionException {
        if (instance == null) {
            instance = new Namespaces();

            final CacheableResourceService c = new DefaultCacheableResourceService();
            final NamespaceHeaderParser p = new NamespaceHeaderParser();

            final SystemConfiguration sysConfig = SystemConfiguration
                    .createSystemConfiguration(null);
            final String resourceIndexURL = sysConfig.getResourceIndexURL();
            File indexFile = new File(resourceIndexURL);
            if (!indexFile.exists() || !indexFile.canRead()) {
                // try the index as an online resource.
                ResolvedResource resolvedResource = c.resolveResource(
                        ResourceType.RESOURCE_INDEX, resourceIndexURL);
                indexFile = resolvedResource.getCacheResourceCopy();
            }

            final ResourceIndex ri = ResourceIndex.INSTANCE;
            ri.loadIndex(indexFile);
            final Index index = ri.getIndex();


            final List<ResourceLocation> nsr = index.getNamespaceResources();
            final Map<String, NamespaceHeader> hvals;
            if (hasItems(nsr)) {
                hvals = new LinkedHashMap<String, NamespaceHeader>(index
                        .getNamespaceResources().size());
                for (ResourceLocation rl : index.getNamespaceResources()) {
                    String loc = rl.getResourceLocation();
                    ResolvedResource nsResource = c.resolveResource(
                            ResourceType.NAMESPACES, loc);
                    NamespaceHeader header = p.parseNamespace(loc,
                            nsResource.getCacheResourceCopy());
                    hvals.put(loc, header);
                }
            } else {
                hvals = new LinkedHashMap<String, NamespaceHeader>();
            }

            headers = Collections.unmodifiableMap(hvals);
        }

        return instance;
    }

    /**
     * Match a {@link NamespaceHeader namespace header} to a species taxonomy
     * id.
     *
     * @param hdr {@link NamespaceHeader}
     * @param species {@code int} species
     * @return {@code true} if the namespace data is specific to
     * {@code species}, {@code false} otherwise
     */
    private boolean matchesSpecies(final NamespaceHeader hdr, int species) {
        final String speciesHdr = hdr.getNamespaceBlock().getSpeciesString();

        // empty header, no match
        if (noLength(speciesHdr)) {
            return false;
        }

        // non-numeric, no match
        if (!isNumeric(speciesHdr)) {
            return false;
        }

        // convert and match
        int v = Integer.parseInt(speciesHdr);
        return v == species;
    }
}
