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
package org.openbel.framework.core.equivalence;

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLStreamException;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.index.Equivalence;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.common.model.DataFileIndex;
import org.openbel.framework.common.model.EquivalenceDataIndex;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.indexer.JDBMEquivalenceLookup;
import org.openbel.framework.core.protocol.ResourceDownloadError;

public final class DefaultParameterEquivalencer implements
        ParameterEquivalencer {
    private static DefaultParameterEquivalencer instance;
    private final Map<String, JDBMEquivalenceLookup> openEquivalences;
    private Index resourceIndex;
    private final CacheableResourceService cacheService;
    private final EquivalenceIndexerService eqIndexer;

    private DefaultParameterEquivalencer() throws IOException,
            ResourceDownloadError, XMLStreamException {
        openEquivalences =
                new ConcurrentHashMap<String, JDBMEquivalenceLookup>();
        cacheService = new DefaultCacheableResourceService();
        eqIndexer = new EquivalenceIndexerServiceImpl();

        if (!ResourceIndex.INSTANCE.isLoaded()) {
            // Load resource index defined by the BELFramework instance
            final SystemConfiguration sysConfig = SystemConfiguration
                    .createSystemConfiguration(null);

            final String resourceIndexURL = sysConfig.getResourceIndexURL();
            File indexFile = new File(resourceIndexURL);
            if (!indexFile.exists() || !indexFile.canRead()) {
                // try the index as an online resource.
                ResolvedResource resolvedResource = cacheService
                        .resolveResource(ResourceType.RESOURCE_INDEX,
                                resourceIndexURL);
                indexFile = resolvedResource.getCacheResourceCopy();
            }

            ResourceIndex.INSTANCE.loadIndex(indexFile);
        }
        resourceIndex = ResourceIndex.INSTANCE.getIndex();
    }

    public static synchronized DefaultParameterEquivalencer getInstance()
            throws ResourceDownloadError, IOException, XMLStreamException {
        if (instance == null) {
            instance = new DefaultParameterEquivalencer();
        }

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Parameter> findEquivalences(Parameter sourceParameter)
            throws ResourceDownloadError, IndexingFailure, IOException {
        openAllEquivalences();

        final List<Parameter> equivalentParameters = new ArrayList<Parameter>();

        Set<Entry<String, JDBMEquivalenceLookup>> equivalenceEntries =
                openEquivalences.entrySet();
        for (Entry<String, JDBMEquivalenceLookup> equivalenceEntry : equivalenceEntries) {
            final String equivalentValue = doFindEquivalence(sourceParameter
                    .getNamespace().getResourceLocation(),
                    equivalenceEntry.getKey(),
                    sourceParameter.getValue());

            if (equivalentValue != null) {
                equivalentParameters.add(new Parameter(new Namespace("",
                        equivalenceEntry.getKey()), equivalentValue));
            }
        }
        return equivalentParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter findEquivalence(Parameter sourceParameter,
            Namespace destinationNamespace) throws ResourceDownloadError,
            IndexingFailure, IOException {
        // open equivalences for source and destination namespaces.
        final String srl = sourceParameter.getNamespace().getResourceLocation();
        openEquivalence(srl);
        openEquivalence(destinationNamespace.getResourceLocation());

        final String equivalentValue = doFindEquivalence(sourceParameter
                .getNamespace().getResourceLocation(),
                destinationNamespace.getResourceLocation(),
                sourceParameter.getValue());

        if (equivalentValue == null) {
            return null;
        }

        return new Parameter(destinationNamespace, equivalentValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Parameter> findEquivalences(SkinnyUUID sourceUUID)
            throws ResourceDownloadError, IndexingFailure, IOException {
        openAllEquivalences();

        final List<Parameter> equivalentParameters = new ArrayList<Parameter>();

        Set<Entry<String, JDBMEquivalenceLookup>> equivalenceEntries =
                openEquivalences.entrySet();
        for (Entry<String, JDBMEquivalenceLookup> equivalenceEntry : equivalenceEntries) {
            final String equivalentValue = doFindEquivalence(
                    equivalenceEntry.getKey(),
                    sourceUUID);

            if (equivalentValue != null) {
                equivalentParameters.add(new Parameter(new Namespace("",
                        equivalenceEntry.getKey()), equivalentValue));
            }
        }
        return equivalentParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter findEquivalence(SkinnyUUID sourceUUID,
            Namespace destinationNamespace) throws ResourceDownloadError,
            IndexingFailure, IOException {
        // open equivalences for destination namespace.
        openEquivalence(destinationNamespace.getResourceLocation());

        final String equivalentValue = doFindEquivalence(
                destinationNamespace.getResourceLocation(),
                sourceUUID);

        if (equivalentValue == null) {
            return null;
        }

        return new Parameter(destinationNamespace, equivalentValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openAllEquivalences() throws ResourceDownloadError,
            IndexingFailure, IOException {
        if (hasItems(resourceIndex.getEquivalenceResources())) {
            // open all equivalences that are not already open
            for (Equivalence eqResource : resourceIndex
                    .getEquivalenceResources()) {

                if (!openEquivalences.containsKey(eqResource
                        .getNamespaceResourceLocation().getResourceLocation())) {
                    openEquivalence(eqResource);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeAllEquivalences() throws ResourceDownloadError,
            IndexingFailure, IOException {
        final Iterator<JDBMEquivalenceLookup> equivalenceIndexIt =
                openEquivalences.values().iterator();

        while (equivalenceIndexIt.hasNext()) {
            final JDBMEquivalenceLookup jdbmLookup = equivalenceIndexIt.next();

            // close the JDBM lookup
            jdbmLookup.close();

            // remove from open equivalences map
            equivalenceIndexIt.remove();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SkinnyUUID getUUID(Parameter param) throws ResourceDownloadError,
            IndexingFailure, IOException {
        String srl = param.getNamespace().getResourceLocation();
        openEquivalence(srl);
        JDBMEquivalenceLookup lookup = openEquivalences.get(srl);
        return lookup.lookup(param.getValue());
    }

    /**
     * Obtain the destinationNamespace equivalent value of sourceValue in sourceNamespace
     * @param sourceNamespace resourceLocation of source namespace
     * @param destinationNamespace resourceLocation of destination namespace
     * @param sourceValue
     * @return equivalent value, may be null.
     */
    private String doFindEquivalence(final String sourceNamespace,
            final String destinationNamespace, final String sourceValue) {
        JDBMEquivalenceLookup sourceLookup =
                openEquivalences.get(sourceNamespace);
        if (sourceLookup == null) {
            return null;
        }
        final SkinnyUUID sourceUUID = sourceLookup.lookup(sourceValue);

        if (sourceUUID == null) {
            return null;
        }
        return doFindEquivalence(destinationNamespace, sourceUUID);
    }

    /**
     * Obtain the destinationNamespace equivalent value of the sourceUUID.
     * @param destinationNamespace resourceLocation of destination namespace
     * @param sourceUUID
     * @return equivalent value, may be null
     */
    private String doFindEquivalence(final String destinationNamespace,
            final SkinnyUUID sourceUUID) {
        JDBMEquivalenceLookup destinationLookup = openEquivalences
                .get(destinationNamespace);

        return destinationLookup.reverseLookup(sourceUUID);
    }

    private void openEquivalence(final String namespaceResourceLocation)
            throws ResourceDownloadError, IndexingFailure, IOException {
        // if we've opened an equivalence for this namespace, do nothing.
        if (openEquivalences.containsKey(namespaceResourceLocation)) {
            return;
        }

        if (hasItems(resourceIndex.getEquivalenceResources())) {
            Equivalence equivalence = null;

            // find equivalence for namespace resource location
            for (Equivalence eqResource : resourceIndex
                    .getEquivalenceResources()) {
                if (namespaceResourceLocation.equals(eqResource
                        .getNamespaceResourceLocation().getResourceLocation())) {
                    equivalence = eqResource;
                    break;
                }
            }

            if (equivalence == null) {
                // TODO Better exception
                throw new InvalidArgument(
                        "Namespace '"
                                + namespaceResourceLocation
                                + "' is not defined or is not backed by an equivalence.");
            }

            openEquivalence(equivalence);
        }
    }

    private void openEquivalence(final Equivalence equivalence)
            throws ResourceDownloadError, IndexingFailure, IOException {

        synchronized (equivalence.getResourceLocation()) {

            ResolvedResource resolved =
                    cacheService.resolveResource(ResourceType.EQUIVALENCES,
                            equivalence.getResourceLocation());

            final DataFileIndex equivalenceIndex =
                    eqIndexer.indexEquivalence(
                            equivalence.getResourceLocation(),
                            resolved.getCacheResourceCopy());

            final EquivalenceDataIndex dataIndex = new EquivalenceDataIndex(
                    equivalence.getNamespaceResourceLocation()
                            .getResourceLocation(), equivalenceIndex);

            JDBMEquivalenceLookup jdbmLookup =
                    new JDBMEquivalenceLookup(dataIndex.getEquivalenceIndex()
                            .getIndexPath());
            jdbmLookup.open();

            openEquivalences.put(equivalence.getNamespaceResourceLocation()
                    .getResourceLocation(),
                    jdbmLookup);
        }
    }
}
