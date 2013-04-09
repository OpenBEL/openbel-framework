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
