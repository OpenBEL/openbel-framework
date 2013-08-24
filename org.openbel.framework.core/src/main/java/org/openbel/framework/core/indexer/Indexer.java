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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openbel.framework.common.protonetwork.model.SkinnyUUID;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

/**
 * TODO comment
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Indexer {

    public void indexNamespaceFile(long characterOffset, File dataFile,
            String indexPath) throws IndexingFailure {
        // open database and setup an object cache
        try {
            RecordManager recman =
                    RecordManagerFactory.createRecordManager(indexPath,
                            new Properties());
            PrimaryTreeMap<String, String> tree =
                    recman.treeMap(IndexerConstants.INDEX_TREE_KEY);
            tree.inverseHashView(IndexerConstants.INVERSE_KEY);

            // clear out tree data since we're rebuilding the index
            tree.clear();

            BufferedReader reader =
                    new BufferedReader(new FileReader(dataFile));
            reader.skip(characterOffset);

            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] lineTokens = line.split("\\|");

                if (lineTokens.length == 2 && lineTokens[0] != null
                        && lineTokens[1] != null) {
                    tree.put(lineTokens[0], lineTokens[1]);
                }
            }
            reader.close();

            // make the data persistent in the database
            recman.commit();
            recman.close();
        } catch (IOException ie) {
            final String name = dataFile.getAbsolutePath();
            throw new IndexingFailure(name, ie);
        }
    }

    public void indexEquivalenceFile(long characterOffset, File dataFile,
            String indexPath) throws IndexingFailure {
        // open database and setup an object cache
        try {
            RecordManager recman =
                    RecordManagerFactory.createRecordManager(indexPath,
                            new Properties());
            PrimaryTreeMap<String, SkinnyUUID> tree =
                    recman.treeMap(IndexerConstants.INDEX_TREE_KEY,
                            new SkinnyUUIDSerializer());
            tree.inverseHashView(IndexerConstants.INVERSE_KEY);

            // clear out tree data since we're rebuilding the index
            tree.clear();

            BufferedReader reader =
                    new BufferedReader(new FileReader(dataFile));
            reader.skip(characterOffset);

            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] lineTokens = line.split("\\|");

                if (lineTokens.length == 2 && lineTokens[0] != null
                        && lineTokens[1] != null) {
                    tree.put(lineTokens[0],
                            SkinnyUUID.fromString(lineTokens[1]));
                }
            }
            reader.close();

            // make the data persistent in the database
            recman.commit();
            recman.close();
        } catch (IOException ie) {
            final String name = dataFile.getAbsolutePath();
            throw new IndexingFailure(name, ie);
        }
    }
}
