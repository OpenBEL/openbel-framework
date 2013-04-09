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
package org.openbel.framework.core.indexer;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
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
 * JDBMLookup unit tests for equivalence data
 */
public class JDBMEquivalenceLookupTest {
    private File indexPath;
    private String key = "1";
    private SkinnyUUID value;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setup() {
        try {
            indexPath = tempFolder.newFolder();
            value = new SkinnyUUID(UUID.randomUUID());

            RecordManager recman = RecordManagerFactory.createRecordManager(
                    indexPath.getAbsolutePath(), new Properties());
            PrimaryTreeMap<String, SkinnyUUID> tree =
                    recman.treeMap(IndexerConstants.INDEX_TREE_KEY,
                            new SkinnyUUIDSerializer());
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
            JDBMEquivalenceLookup jdbmLookup =
                    new JDBMEquivalenceLookup(indexPath.getAbsolutePath());
            jdbmLookup.open();

            assertEquals(1, jdbmLookup.getRecordCount());
            assertEquals(value, jdbmLookup.lookup(key));

            jdbmLookup.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testLookupFailure() {
        try {
            JDBMEquivalenceLookup jdbmLookup =
                    new JDBMEquivalenceLookup(indexPath.getAbsolutePath());
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
            JDBMEquivalenceLookup jdbmLookup =
                    new JDBMEquivalenceLookup(indexPath.getAbsolutePath());
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
            JDBMEquivalenceLookup jdbmLookup =
                    new JDBMEquivalenceLookup(indexPath.getAbsolutePath());
            jdbmLookup.open();

            assertEquals(1, jdbmLookup.getRecordCount());
            assertNull(jdbmLookup.reverseLookup(new SkinnyUUID(UUID
                    .randomUUID())));

            jdbmLookup.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
