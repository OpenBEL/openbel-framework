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
package org.openbel.framework.compiler;

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.Reportable;
import org.openbel.framework.common.index.Equivalence;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.common.model.DataFileIndex;
import org.openbel.framework.common.model.EquivalenceDataIndex;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.openbel.framework.core.equivalence.BucketEquivalencer;
import org.openbel.framework.core.equivalence.EquivalenceIndexerService;
import org.openbel.framework.core.equivalence.EquivalenceMapResolutionFailure;
import org.openbel.framework.core.equivalence.EquivalenceResource;
import org.openbel.framework.core.equivalence.EquivalenceResourceImpl;
import org.openbel.framework.core.equivalence.StatementEquivalencer;
import org.openbel.framework.core.equivalence.TermEquivalencer;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protonetwork.ProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.ProtoNetworkService;

/**
 * BEL compiler phase two implementation.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public final class PhaseTwoImpl implements DefaultPhaseTwo {
    private final CacheableResourceService cache;
    private final EquivalenceIndexerService equivalenceIndexerService;
    private final ProtoNetworkService protoNetworkService;
    private EquivalenceResource equivs;
    /** Reporting mechanism for errors, warnings, and output. */
    private Reportable reportable;

    /**
     * Creates the {@link DefaultPhaseTwo} implementation from the
     * {@code resourceResolverService}, {@code equivalenceIndexerService}, and
     * {@code protoNetworkService} services.
     *
     * @param cache {@link CacheableResourceService}, the resource resolver
     * service
     * @param equivalenceIndexerService {@link EquivalenceIndexerService}, the
     * equivalence indexer service
     * @param protoNetworkService {@link ProtoNetworkService}, the proto network
     * service
     */
    public PhaseTwoImpl(
            final CacheableResourceService cache,
            final EquivalenceIndexerService equivalenceIndexerService,
            final ProtoNetworkService protoNetworkService) {
        this.cache = cache;
        this.equivalenceIndexerService = equivalenceIndexerService;
        this.protoNetworkService = protoNetworkService;
    }

    /**
     * Injects the reporting mechanism.
     *
     * @param r {@link Reportable}, the reportable reporting mechanism
     */
    public void setReportable(final Reportable r) {
        this.reportable = r;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtoNetwork stage1Merger(
            Collection<ProtoNetworkDescriptor> protoNetworkDescriptors) {
        ProtoNetwork mergedNetwork = null;
        try {
            Iterator<ProtoNetworkDescriptor> it =
                    protoNetworkDescriptors.iterator();

            // Grab first proto network and iteratively merge the rest.
            ProtoNetworkDescriptor pnd = it.next();
            mergedNetwork = protoNetworkService.read(pnd);
            while (it.hasNext()) {
                ProtoNetwork nextPn = protoNetworkService.read(it.next());
                protoNetworkService.merge(mergedNetwork, nextPn);
            }
        } catch (ProtoNetworkError e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause != null) e.printStackTrace();
            error("Unable to merge proto networks into global network.");
        }

        return mergedNetwork;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<EquivalenceDataIndex> stage2LoadNamespaceEquivalences()
            throws EquivalenceMapResolutionFailure {

        Set<EquivalenceDataIndex> equivalenceDataIndexSet =
                new HashSet<EquivalenceDataIndex>();

        Index resourceIndex = ResourceIndex.INSTANCE.getIndex();

        if (hasItems(resourceIndex.getEquivalenceResources())) {
            for (Equivalence eqResource : resourceIndex
                    .getEquivalenceResources()) {
                String eqLocation = eqResource.getResourceLocation();
                try {
                    ResolvedResource resolved = cache.resolveResource(
                            ResourceType.EQUIVALENCES,
                            eqLocation);

                    DataFileIndex equivalenceIndex = equivalenceIndexerService
                            .indexEquivalence(eqLocation,
                                    resolved.getCacheResourceCopy());

                    equivalenceDataIndexSet.add(new EquivalenceDataIndex(
                            eqResource.getNamespaceResourceLocation()
                                    .getResourceLocation(), equivalenceIndex));
                } catch (ResourceDownloadError e) {
                    error("Unable to load equivalence resource for location: "
                            + eqLocation);
                } catch (IndexingFailure e) {
                    error("Unable to load equivalence resource for location: "
                            + eqLocation);
                }
            }
        }

        return equivalenceDataIndexSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int stage3EquivalenceParameters(ProtoNetwork network,
            Set<EquivalenceDataIndex> indexes) throws IOException {
        if (indexes == null) {
            throw new InvalidArgument("indexes", indexes);
        }

        // FIXME Better way to subset equivalences, like maybe by domain
        Set<String> pnNsLocations = new HashSet<String>();
        for (TableNamespace tn : network.getNamespaceTable().getNamespaces()) {
            pnNsLocations.add(tn.getResourceLocation());
        }

        Iterator<EquivalenceDataIndex> indexIt = indexes.iterator();
        while (indexIt.hasNext()) {
            EquivalenceDataIndex index = indexIt.next();

            // if proto network does not contain this index, throw it away
            if (!pnNsLocations.contains(index.getNamespaceResourceLocation())) {
                indexIt.remove();
            }
        }

        if (!indexes.isEmpty()) {
            equivs = new EquivalenceResourceImpl(indexes);
            equivs.openResources();
            BucketEquivalencer be = new BucketEquivalencer(network, equivs);
            return be.equivalence();
        }

        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int stage3EquivalenceTerms(ProtoNetwork network) {
        TermEquivalencer e = new TermEquivalencer(network, equivs);
        int equivalences = e.equivalence();
        try {
            equivs.closeResources();
        } catch (IOException ie) {
            // Ignore it
        }
        return equivalences;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int stage3EquivalenceStatements(ProtoNetwork network) {
        StatementEquivalencer e = new StatementEquivalencer(network);
        int equivalences = e.equivalence();
        return equivalences;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtoNetworkDescriptor stage4WriteEquivalentProtoNetwork(
            ProtoNetwork protoNetwork, String protoNetworkRoot) {
        if (protoNetwork == null) {
            throw new InvalidArgument("protoNetwork", protoNetwork);
        }
        if (protoNetworkRoot == null) {
            throw new InvalidArgument("protoNetworkRoot", protoNetworkRoot);
        }

        ProtoNetworkDescriptor protoNetworkDescriptor = null;
        try {
            protoNetworkDescriptor = protoNetworkService.write(
                    protoNetworkRoot, protoNetwork);
        } catch (ProtoNetworkError e) {
            error("Could not write out equivalent proto network.");
        }

        return protoNetworkDescriptor;
    }

    /**
     * Sends an "error" {@link String} to the {@link Reportable} object.
     *
     * @param s {@link String} the error message to report on
     */
    private void error(final String s) {
        reportable.error(s);
    }
}
