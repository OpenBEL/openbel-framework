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
package org.openbel.framework.ws.dialect;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.KamStore;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelTerm;

/**
 * Simple dialect that uses the BEL short form of the first supporting term
 * found for a {@link KamNode}.<br>
 * This implementation caches labels for nodes and is thus suitable only for a
 * single {@link Kam}. Usage across {@link Kam}s is not guarded against but will
 * cause unpredictable results.<br>
 * Construction of this class is limited to
 * {@link DialectFactory#createDefaultDialect(KamInfo)}<br>
 * This implementation is thread-safe.
 *
 * @author Steve Ungerer
 */
public class DefaultDialect implements Dialect {

    private final KamInfo kamInfo;
    private final KamStore kamStore;

    private Map<String, String> labelCache =
            new ConcurrentHashMap<String, String>();

    /**
     * Package constructor
     * @param kamInfo
     * @param kamStore
     */
    DefaultDialect(KamInfo kamInfo, KamStore kamStore) {
        this.kamInfo = kamInfo;
        this.kamStore = kamStore;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel(KamNode kamNode) {
        String label = labelCache.get(kamNode.getLabel());
        if (label == null) {
            label = kamNode.getLabel();
            try {
                List<BelTerm> terms = kamStore.getSupportingTerms(kamNode);
                if (!terms.isEmpty()) {
                    BelTerm bt = terms.get(0);
                    Term t = BELParser.parseTerm(bt.getLabel());
                    label = t.toBELShortForm();
                }
            } catch (Exception e) {
                // TODO exception
            }
            labelCache.put(kamNode.getLabel(), label);
        }
        return label;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return kamInfo.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DefaultDialect)) {
            return false;
        }
        DefaultDialect rhs = (DefaultDialect) obj;
        return kamInfo.equals(rhs.kamInfo);
    }

}
