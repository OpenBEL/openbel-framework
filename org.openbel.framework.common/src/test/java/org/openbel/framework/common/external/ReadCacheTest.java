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
package org.openbel.framework.common.external;

import static org.junit.Assert.assertTrue;
import static org.openbel.framework.common.BELUtilities.entries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.external.ReadCache;

public class ReadCacheTest {
    private ReadCache subject;

    @Before
    public void setUp() {
        subject = new ReadCache();
    }

    @Test
    public void test() {
        assertTrue(subject.size() == 0);
        int cached_count = 10;
        List<UUID> uuids = new ArrayList<UUID>();
        Map<UUID, Integer> truth = new HashMap<UUID, Integer>();
        for (int i = 0; i < cached_count; i++) {
            UUID uuid = UUID.randomUUID();
            uuids.add(uuid);
            subject.cache(i, uuid);
            truth.put(uuid, i);
        }
        assertTrue(subject.size() == cached_count);
        for (final Entry<UUID, Integer> e : entries(truth)) {
            UUID key = e.getKey();
            Integer value = e.getValue();
            assertTrue(subject.get(value) == key);
        }
    }

}
