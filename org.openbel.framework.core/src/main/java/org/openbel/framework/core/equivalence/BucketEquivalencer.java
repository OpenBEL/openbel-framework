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
package org.openbel.framework.core.equivalence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.model.EquivalenceDataIndex;
import org.openbel.framework.common.protonetwork.model.ParameterTable;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ParameterTable.TableParameter;
import org.openbel.framework.core.indexer.EquivalenceLookup;

/**
 * Equivalences {@link ProtoNetwork} parameters across a set of
 * {@link EquivalenceDataIndex equivalence data indices} associated with a
 * parameter domain. For example:
 * <p>
 *
 * <pre>
 * <code>
 * [...]
 * Set<EquivalenceDataIndex> eqSet = sizedHashSet(6);
 * eqSet.add(ncbiEG);
 * eqSet.add(hgncSyms);
 * eqSet.add(mgiSyms);
 * eqSet.add(rgdSyms);
 * eqSet.add(spAccNum);
 * eqSet.add(spEntNam);
 * BucketEquivalencer be = new BucketEquivalencer(network, eqSet);
 * be.equivalence();
 * [...]
 * </code>
 * </pre>
 *
 * </p>
 */
public final class BucketEquivalencer extends Equivalencer {
    private EquivalenceResource equivs;

    /**
     * Constructs a bucket equivalencer for parameter equivalencing against a
     * set of data indices.
     *
     * @param pn {@link ProtoNetwork Proto-network}
     * @param equivs {@link EquivalenceResource}
     * @throws InvalidArgument Thrown if {@code pn} or {@code equivs} is null
     */
    public BucketEquivalencer(ProtoNetwork pn, EquivalenceResource equivs) {
        super(pn);
        if (equivs == null) {
            throw new InvalidArgument("equivalence resource may not be null");
        }
        this.equivs = equivs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int equivalence() throws IOException {
        int result = process();
        return result;
    }

    private int process() {
        int equivalences = 0;

        // Establish access to all the necessary proto-network parts
        final ParameterTable pt = network.getParameterTable();
        final TableParameter[] params = pt.getTableParameterArray();
        final Map<TableParameter, Integer> tpIdx = pt.getTableParameterIndex();

        // Get the global index, clear it, and mark the baseline global index
        final Map<Integer, Integer> globalIndex = pt.getGlobalIndex();
        final Map<Integer, SkinnyUUID> globalUUIDs = pt.getGlobalUUIDs();
        globalIndex.clear();
        globalUUIDs.clear();
        int gi = 0;

        // Map UUID strings to global index (inverse of globalUUIDs)
        Map<SkinnyUUID, Integer> uuidMap = new HashMap<SkinnyUUID, Integer>();

        // Note the number of parameters
        final int numParams = params.length;

        // First pass, this algorithm notes which bucket is
        // associated with each parameter and what the parameter's
        // index is in the table parameter index map

        // Index parameters to their respective buckets
        EquivalenceLookup[] jArray = new EquivalenceLookup[numParams];
        // Index parameters to their parameter index
        int[] parameterIndices = new int[numParams];
        for (int i = 0; i < numParams; i++) {
            final TableParameter param = params[i];
            TableNamespace ns = param.getNamespace();

            // Sanity check - namespace may be null
            if (ns == null) {
                jArray[i] = null;
                parameterIndices[i] = tpIdx.get(param);
                continue;
            }

            // Resource location is never null
            final String rl = ns.getResourceLocation();

            // Which bucket are we mapped to?
            final EquivalenceLookup bucket = equivs.forResourceLocation(rl);

            // This parameter's namespace is not mapped to the current eqs
            if (bucket == null) {
                jArray[i] = null;
                parameterIndices[i] = tpIdx.get(param);
                continue;
            }

            // Index the parameter i to its respective bucket
            jArray[i] = bucket;
            // Index the parameter i to its parameter index
            parameterIndices[i] = tpIdx.get(param);
        }

        // Second pass
        for (int i = 0; i < numParams; i++) {
            EquivalenceLookup bucket = jArray[i];
            final int pIndex = parameterIndices[i];

            // Does this parameter have a bucket?
            if (bucket == null) {
                globalIndex.put(pIndex, gi);
                // no equivalence
                gi++;
                continue;
            }

            final TableParameter param = params[i];
            final String value = param.getValue();
            final SkinnyUUID lookup = bucket.lookup(value);

            // Null lookup means no equivalence
            if (lookup == null) {
                // Parameter index gets the current gi
                globalIndex.put(pIndex, gi);
                // no equivalence
                gi++;
                continue;
            }

            // Check noted UUIDs
            Integer currentGlobalIndex = uuidMap.get(lookup);
            // Non-null currentGlobalIndex means we've found an equivalence
            if (currentGlobalIndex != null) {
                equivalences++;
                globalIndex.put(pIndex, currentGlobalIndex);
                // uuid already present in globalUUIDs
                continue;
            }

            // Note this UUID
            uuidMap.put(lookup, gi);
            globalUUIDs.put(gi, lookup);
            globalIndex.put(pIndex, gi);
            gi++;
        }

        return equivalences;
    }
}
