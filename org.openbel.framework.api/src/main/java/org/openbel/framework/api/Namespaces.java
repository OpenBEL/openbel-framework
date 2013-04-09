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
package org.openbel.framework.api;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.isNumeric;
import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;

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

            final SystemConfiguration sysConfig = getSystemConfiguration();
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
