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
