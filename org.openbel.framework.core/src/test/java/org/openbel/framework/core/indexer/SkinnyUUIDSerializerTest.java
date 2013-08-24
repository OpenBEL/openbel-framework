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
package org.openbel.framework.core.indexer;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.core.indexer.IndexerConstants;
import org.openbel.framework.core.indexer.JDBMEquivalenceLookup;
import org.openbel.framework.core.indexer.SkinnyUUIDSerializer;

/**
 * Tests the {@link SkinnyUUIDSerializer} to ensure proper storage and retrieval of
 * {@link SkinnyUUID}s
 *
 * @author Steve Ungerer
 */
public class SkinnyUUIDSerializerTest {
    int numToTest = 1000;

    private File indexPath;
    private List<SkinnyUUID> uuids;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setup() {
        try {
            indexPath = tempFolder.newFolder();
            this.uuids = new ArrayList<SkinnyUUID>();
            RecordManager recman = RecordManagerFactory.createRecordManager(
                    indexPath.getAbsolutePath(), new Properties());

            PrimaryTreeMap<String, SkinnyUUID> tree =
                    recman.treeMap(IndexerConstants.INDEX_TREE_KEY,
                            new SkinnyUUIDSerializer());
            tree.inverseHashView(IndexerConstants.INVERSE_KEY);

            for (int i = 0; i < numToTest; i++) {
                SkinnyUUID uuid = new SkinnyUUID(UUID.randomUUID());
                tree.put(String.valueOf(i), uuid);
                uuids.add(uuid);
            }

            recman.commit();
            recman.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failure to create temporary folder for testing.");
        }
    }

    @Test
    public void testLookupSuccess() {
        try {
            JDBMEquivalenceLookup jdbmLookup =
                    new JDBMEquivalenceLookup(indexPath.getAbsolutePath());
            jdbmLookup.open();

            for (int i = 0; i < numToTest; i++) {
                SkinnyUUID uuid = uuids.get(i);
                assertEquals("Failed for " + i, uuid,
                        jdbmLookup.lookup(String.valueOf(i)));
            }

            jdbmLookup.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
