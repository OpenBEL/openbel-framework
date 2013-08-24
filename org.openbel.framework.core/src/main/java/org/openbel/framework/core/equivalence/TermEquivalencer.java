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

import static org.openbel.framework.common.BELUtilities.sizedHashMap;

import java.util.List;
import java.util.Map;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.protonetwork.model.ParameterTable;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNodeTable;
import org.openbel.framework.common.protonetwork.model.TermParameterMapTable;
import org.openbel.framework.common.protonetwork.model.TermTable;

/**
 * TermEquivalencer equivalences {@link ProtoNetwork} terms by comparing term
 * signatures based on global parameter ids. This algorithm has O(n log(n))
 * complexity.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class TermEquivalencer extends Equivalencer {
    final TermTable tt;
    final TermParameterMapTable tpmt;
    final ParameterTable pt;
    final ProtoNodeTable pnt;
    final EquivalenceResource equivs;
    final EquivalenceConverter eqCvtr;

    /**
     * Constructs the term equivalencer with a {@link ProtoNetwork} to
     * equivalence.
     *
     * @param pn {@link ProtoNetwork} the proto network to equivalence
     * @param equivs {@link EquivalenceResource}
     * @throws InvalidArgument Thrown if {@code pn} or {@code equivs} is null
     */
    public TermEquivalencer(ProtoNetwork pn, EquivalenceResource equivs) {
        super(pn);
        if (equivs == null) {
            throw new InvalidArgument("equivalence resource may not be null");
        }
        this.equivs = equivs;

        tt = pn.getTermTable();
        tpmt = pn.getTermParameterMapTable();
        pt = pn.getParameterTable();
        pnt = pn.getProtoNodeTable();
        eqCvtr = new EquivalenceConverter(equivs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int equivalence() {
        int eqct = 0;

        final List<String> nodes = pnt.getProtoNodes();
        final Map<Integer, Integer> nodeTerms = pnt.getNodeTermIndex();
        final int termct = nodeTerms.size();
        final Map<EquivalentTerm, Integer> nodeCache = sizedHashMap(termct);
        final Map<Integer, Integer> eqn = pnt.getEquivalences();

        // clear out equivalences, since we're rebuilding them
        eqn.clear();

        // Iterate each node
        int eq = 0;
        for (int i = 0, n = nodes.size(); i < n; i++) {
            final Integer termidx = nodeTerms.get(i);
            final Term term = tt.getIndexedTerms().get(termidx);
            // Convert to an equivalent term
            EquivalentTerm equiv = eqCvtr.convert(term);
            Integer eqId = nodeCache.get(equiv);
            if (eqId != null) {
                // We've seen an equivalent term before, use that.
                eqn.put(i, eqId);
                eqct++;
                continue;
            }

            nodeCache.put(equiv, eq);
            eqn.put(i, eq);
            eq++;
        }

        return eqct;
    }
}
