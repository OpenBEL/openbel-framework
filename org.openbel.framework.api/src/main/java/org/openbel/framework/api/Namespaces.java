package org.openbel.framework.api;

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
 * TODO
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class Namespaces {

    private static Namespaces instance;
    private static Map<String, NamespaceHeader> headers;

    private Namespaces() {
        // private singleton constructor
    }

    public Collection<NamespaceHeader> getHeaders() {
        return headers.values();
    }

    public NamespaceHeader lookupNamespace(final String rloc) {
        return headers.get(rloc);
    }

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

            final Map<String, NamespaceHeader> hvals =
                    new LinkedHashMap<String, NamespaceHeader>(
                            index.getNamespaceResources().size());
            for (ResourceLocation rl : index.getNamespaceResources()) {
                String loc = rl.getResourceLocation();
                ResolvedResource nsResource = c.resolveResource(
                        ResourceType.NAMESPACES, loc);
                NamespaceHeader header = p.parseNamespace(loc,
                        nsResource.getCacheResourceCopy());
                hvals.put(loc, header);
            }

            headers = Collections.unmodifiableMap(hvals);
        }

        return instance;
    }

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
