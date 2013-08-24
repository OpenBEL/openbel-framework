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
