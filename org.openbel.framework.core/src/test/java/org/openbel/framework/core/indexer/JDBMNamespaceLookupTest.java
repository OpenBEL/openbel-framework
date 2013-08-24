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
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openbel.framework.core.indexer.IndexerConstants;
import org.openbel.framework.core.indexer.JDBMNamespaceLookup;

/**
 * JDBMLookup unit tests, replacing NamespaceLookupTest.
 */
public class JDBMNamespaceLookupTest {
    private File indexPath;
    private String key = "1";
    private String value = "test";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setup() {
        try {
            indexPath = tempFolder.newFolder();
            RecordManager recman = RecordManagerFactory.createRecordManager(
                    indexPath.getAbsolutePath(), new Properties());
            PrimaryTreeMap<String, String> tree =
                    recman.treeMap(IndexerConstants.INDEX_TREE_KEY);
            tree.inverseHashView(IndexerConstants.INVERSE_KEY);
            tree.put(key, value);
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
            JDBMNamespaceLookup jdbmLookup =
                    new JDBMNamespaceLookup(indexPath.getAbsolutePath());
            jdbmLookup.open();

            assertEquals(1, jdbmLookup.getRecordCount());
            assertEquals(value, jdbmLookup.lookup("1"));

            jdbmLookup.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testLookupFailure() {
        try {
            JDBMNamespaceLookup jdbmLookup =
                    new JDBMNamespaceLookup(indexPath.getAbsolutePath());
            jdbmLookup.open();

            assertEquals(1, jdbmLookup.getRecordCount());
            assertNull(jdbmLookup.lookup("0"));

            jdbmLookup.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testInverseLookupSuccess() {
        try {
            JDBMNamespaceLookup jdbmLookup =
                    new JDBMNamespaceLookup(indexPath.getAbsolutePath());
            jdbmLookup.open();

            assertEquals(1, jdbmLookup.getRecordCount());
            assertEquals(key, jdbmLookup.reverseLookup(value));

            jdbmLookup.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testInverseLookupFailure() {
        try {
            JDBMNamespaceLookup jdbmLookup =
                    new JDBMNamespaceLookup(indexPath.getAbsolutePath());
            jdbmLookup.open();

            assertEquals(1, jdbmLookup.getRecordCount());
            assertNull(jdbmLookup.reverseLookup("0"));

            jdbmLookup.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
