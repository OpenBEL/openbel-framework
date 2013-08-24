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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.protonetwork.model.ParameterTable;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ParameterTable.TableParameter;
import org.openbel.framework.core.equivalence.BucketEquivalencer;
import org.openbel.framework.core.equivalence.EquivalenceResource;
import org.openbel.framework.core.indexer.EquivalenceLookup;

/**
 * Tests around {@link BucketEquivalencer}
 *
 * @author Steve Ungerer
 */
public class BucketEquivalencerTest {

    private TestEquivResource er;
    private ProtoNetwork pn;
    private List<SkinnyUUID> uuids;
    private BucketEquivalencer be;

    @Before
    public void setUp() {
        pn = new ProtoNetwork();
        er = new TestEquivResource();
        uuids = new ArrayList<SkinnyUUID>(1000);
        TableNamespace ns1 = new TableNamespace("namespace1");
        TableNamespace ns2 = new TableNamespace("namespace2");
        MockEquivLookup lookup1 = new MockEquivLookup();
        MockEquivLookup lookup2 = new MockEquivLookup();
        er.addLookup(ns1.getResourceLocation(), lookup1);
        er.addLookup(ns2.getResourceLocation(), lookup2);

        for (int i = 0; i < 1000; i++) {
            String v1 = "p1" + String.valueOf(i);
            String v2 = "p2" + String.valueOf(i);
            pn.getParameterTable().addTableParameter(
                    new TableParameter(ns1, v1));
            pn.getParameterTable().addTableParameter(
                    new TableParameter(ns2, v2));
            SkinnyUUID u = new SkinnyUUID(UUID.randomUUID());
            uuids.add(u);
            lookup1.addLookup(v1, u);
            lookup2.addLookup(v2, u);
        }
        be = new BucketEquivalencer(pn, er);
    }

    /**
     * Test the number of equivalent parameters found matches what's expected
     * @throws Exception
     */
    @Test
    public void testEquivalencingCount() throws Exception {
        int equivs = be.equivalence();
        Assert.assertEquals("Incorrect number of equivalences", uuids.size(),
                equivs);
    }

    /**
     * Test the appropriate number of UUIDs were generated
     * @throws Exception
     */
    @Test
    public void testUUIDSize() throws Exception {
        be.equivalence();
        Map<Integer, SkinnyUUID> uuidMap =
                pn.getParameterTable().getGlobalUUIDs();
        Assert.assertEquals("Incorrect UUID size", uuids.size(), uuidMap.size());
    }

    /**
     * Test that every equivalent parameter has the same global ID and the expected UUID
     * @throws Exception
     */
    @Test
    public void validateGlobalUUIDs() throws Exception {
        be.equivalence();
        ParameterTable pt = pn.getParameterTable();
        TableParameter[] tpa = pt.getTableParameterArray();
        int j = 0; // idx to lookup UUIDs from list
        for (int i = 0; i < tpa.length; i = i + 2) { // increment for each pair of equivalent term params
            TableParameter tp1 = tpa[i];
            TableParameter tp2 = tpa[i + 1];
            Integer idx1 = pt.getTableParameterIndex().get(tp1);
            Integer idx2 = pt.getTableParameterIndex().get(tp2);
            Integer gi1 = pt.getGlobalIndex().get(idx1);
            Integer gi2 = pt.getGlobalIndex().get(idx2);
            Assert.assertEquals("Global ID for indices " + i + "and " + (i + 1)
                    + " failed equality", gi1, gi2);
            Assert.assertEquals("Global UUID for indices " + i + "and "
                    + (i + 1) + " failed equality", uuids.get(j++), pt
                    .getGlobalUUIDs().get(gi1));
        }
    }

    private class TestEquivResource implements EquivalenceResource {
        private Map<String, EquivalenceLookup> m =
                new HashMap<String, EquivalenceLookup>();

        public void
                addLookup(String resourceLocation, EquivalenceLookup lookup) {
            m.put(resourceLocation, lookup);
        }

        @Override
        public void openResources() throws IOException {
            // no-op
        }

        @Override
        public void closeResources() throws IOException {
            // no-op
        }

        @Override
        public boolean opened() {
            return true;
        }

        @Override
        public EquivalenceLookup forResourceLocation(String rl)
                throws IllegalStateException {
            return m.get(rl);
        }

    }

    private class MockEquivLookup implements EquivalenceLookup {
        private Map<String, SkinnyUUID> m = new HashMap<String, SkinnyUUID>();

        public void addLookup(String key, SkinnyUUID value) {
            m.put(key, value);
        }

        @Override
        public SkinnyUUID lookup(String key) {
            return m.get(key);
        }

    }
}
