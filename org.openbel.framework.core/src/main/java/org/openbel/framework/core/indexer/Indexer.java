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
