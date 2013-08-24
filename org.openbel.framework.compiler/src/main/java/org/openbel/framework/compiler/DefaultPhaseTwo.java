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
package org.openbel.framework.compiler;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.EquivalenceDataIndex;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.core.equivalence.EquivalenceMapResolutionFailure;
import org.openbel.framework.core.protonetwork.ProtoNetworkDescriptor;

/**
 * BEL compiler phase two interface.
 * <p>
 * Phase two compilation consists of:
 * <ol>
 * <li><b>Stage 1 -- Merge Proto Networks</b><br>
 * Merge each {@link Document}'s Phase I {@link ProtoNetwork} representation.</li>
 *
 * <li><b>Stage 2 -- Load Namespace Equivalences</b><br>
 * Load namespace equivalence indices.</li>
 *
 * <li><b>Stage 3 -- Equivalence Merged Proto Network</b><br>
 * Equivalence the Phase II / Stage 1 merged {@link ProtoNetwork}. This stage
 * only needs to execute if the user provided namespace equivalences in Stage 2.
 * </li>
 *
 * <li><b>Stage 4 -- Externalize the Merged/Equivalenced Proto Network</b><br>
 * Externalize the merged/equivalenced {@link ProtoNetwork}.</li>
 * </ol>
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface DefaultPhaseTwo {

    /**
     * Executes stage one merging of each phase I {@link ProtoNetwork}.
     *
     * @param protoNetworkDescriptors Collection of
     * {@link ProtoNetworkDescriptor}, the proto network descriptors in order to
     * load the {@link ProtoNetwork}s
     * @return {@link ProtoNetwork}, the merged proto network, which cannot be
     * null
     */
    public ProtoNetwork stage1Merger(
            Collection<ProtoNetworkDescriptor> protoNetworkDescriptors);

    /**
     * Executes stage two loading of namespace equivalence indices. The
     * namespace equivalences are determined from the loaded
     * {@link ResourceIndex}.
     *
     * @return {@link Set} of {@link EquivalenceDataIndex}, the set of
     * equivalence data indices, which may by empty but not null
     * @throws EquivalenceMapResolutionFailure Thrown if the equivalence map
     * file could not be resolved
     */
    public Set<EquivalenceDataIndex> stage2LoadNamespaceEquivalences()
            throws EquivalenceMapResolutionFailure;

    /**
     * Executes stage three parameter equivalencing of the merged
     * {@link ProtoNetwork}.
     * <p>
     * Note this method will modify the proto-network {@code protoNetwork}.
     * </p>
     *
     * @param protoNetwork {@link ProtoNetwork}, the merged proto-network to
     * equivalence
     * @param indexes {@link Set} of {@link EquivalenceDataIndex} equivalence
     * data indexes, which cannot be null
     * @return int Number of parameters equivalenced
     * @throws IOException Thrown if an I/O error occurred during equivalencing
     * @throws InvalidArgument Thrown if <tt>indexes</tt> is null
     */
    public int stage3EquivalenceParameters(ProtoNetwork protoNetwork,
            Set<EquivalenceDataIndex> indexes) throws IOException;

    /**
     * Executes stage three equivalencing of the merged {@link ProtoNetwork}.
     * <p>
     * Note this method will modify the proto-network {@code protoNetwork}.
     * </p>
     *
     * @param protoNetwork {@link ProtoNetwork}, the merged proto-network to
     * equivalence
     * @return int Number of terms equivalenced
     */
    public int stage3EquivalenceTerms(ProtoNetwork protoNetwork);

    /**
     * Executes stage three equivalencing of the merged {@link ProtoNetwork}.
     * <p>
     * Note this method will modify the proto-network {@code protoNetwork}.
     * </p>
     *
     * @param protoNetwork {@link ProtoNetwork}, the merged proto-network to
     * equivalence
     * @return int Number of statements equivalenced
     */
    public int stage3EquivalenceStatements(ProtoNetwork protoNetwork);

    /**
     * Executes stage four externalization of the merged/equivalenced
     * {@link ProtoNetwork}.
     *
     * @param protoNetwork {@link ProtoNetwork}, the proto network to
     * externalize
     * @param protoNetworkRoot {@link String}, the proto network root path to
     * write to, which cannot be null
     * @return {@link ProtoNetworkDescriptor}, the proto network descriptor,
     * which cannot be null
     */
    public ProtoNetworkDescriptor stage4WriteEquivalentProtoNetwork(
            ProtoNetwork protoNetwork, String protoNetworkRoot);
}
