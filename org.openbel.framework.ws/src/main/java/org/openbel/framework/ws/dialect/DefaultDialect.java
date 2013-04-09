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
package org.openbel.framework.ws.dialect;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.api.KAMStore;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.Term;

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
    private final KAMStore kAMStore;

    private Map<String, String> labelCache =
            new ConcurrentHashMap<String, String>();

    /**
     * Package constructor
     * @param kamInfo
     * @param kAMStore
     */
    DefaultDialect(KamInfo kamInfo, KAMStore kAMStore) {
        this.kamInfo = kamInfo;
        this.kAMStore = kAMStore;
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
                List<BelTerm> terms = kAMStore.getSupportingTerms(kamNode);
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
