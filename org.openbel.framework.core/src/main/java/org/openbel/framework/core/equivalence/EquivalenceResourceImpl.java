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

import static org.openbel.framework.common.BELUtilities.sizedHashMap;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.model.DataFileIndex;
import org.openbel.framework.common.model.EquivalenceDataIndex;
import org.openbel.framework.core.indexer.EquivalenceLookup;
import org.openbel.framework.core.indexer.JDBMEquivalenceLookup;

/**
 * {@link EquivalenceResource} implementation.
 */
public class EquivalenceResourceImpl implements EquivalenceResource {
    private final Map<String, JDBMEquivalenceLookup> lookupMap;
    private boolean opened = false;

    /**
     * Constructs a equivalence resource implementation for parameter
     * equivalencing against a set of data indices.
     *
     * @param set {@link Set} of {@link EquivalenceDataIndex equivalence data
     * indices}
     * @throws InvalidArgument Thrown if {@code set} is null
     */
    public EquivalenceResourceImpl(Set<EquivalenceDataIndex> set) {
        if (set == null) {
            throw new InvalidArgument("set", set);
        }

        lookupMap = sizedHashMap(set.size());
        for (final EquivalenceDataIndex edi : set) {
            String key = edi.getNamespaceResourceLocation();
            DataFileIndex dfi = edi.getEquivalenceIndex();
            lookupMap.put(key, new JDBMEquivalenceLookup(dfi.getIndexPath()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeResources() throws IOException {
        if (!opened) return;
        for (final JDBMEquivalenceLookup lookup : lookupMap.values()) {
            lookup.close();
        }
        opened = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EquivalenceLookup forResourceLocation(String rl) {
        if (!opened) {
            throw new IllegalStateException("resources not opened");
        }
        return lookupMap.get(rl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openResources() throws IOException {
        for (final JDBMEquivalenceLookup lookup : lookupMap.values()) {
            lookup.open();
        }
        opened = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean opened() {
        return opened;
    }
}
