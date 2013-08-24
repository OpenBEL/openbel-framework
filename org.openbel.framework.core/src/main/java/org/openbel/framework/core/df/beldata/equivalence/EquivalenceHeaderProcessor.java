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
package org.openbel.framework.core.df.beldata.equivalence;

import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.createDirectories;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;
import org.openbel.framework.core.indexer.IndexingFailure;

public class EquivalenceHeaderProcessor {

    private long characterStopOffset = -1;

    public long getCharacterStopOffset() {
        return characterStopOffset;
    }

    public File processEquivalenceHeader(final String resourceLocation,
            final File rawEquivalence, final File equivalenceHomeFile)
            throws IndexingFailure {
        EquivalenceHeaderParser equivalenceParser =
                new EquivalenceHeaderParser();
        EquivalenceHeader equivalenceHeader = null;
        final String name = rawEquivalence.getAbsolutePath();

        try {
            equivalenceHeader = equivalenceParser.parseEquivalence(
                    resourceLocation, rawEquivalence);
            characterStopOffset = equivalenceParser.getNextCharacterOffset();
        } catch (IOException e) {
            throw new IndexingFailure(name, e);
        } catch (BELDataMissingPropertyException e) {
            throw new IndexingFailure(name, e);
        } catch (BELDataConversionException e) {
            throw new IndexingFailure(name, e);
        }

        final EquivalenceBlock eqblock =
                equivalenceHeader.getEquivalenceBlock();
        final String eqhome = equivalenceHomeFile.getAbsolutePath();
        String eqname = eqblock.getNameString().replace(' ', '-');
        String eqversion = eqblock.getVersionString().replace(' ', '-');
        String path = asPath(eqhome, eqname, eqversion);

        createDirectories(path);
        final String outputPath = asPath(path, "equivalence.header");

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(outputPath));
            oos.writeObject(equivalenceHeader);
            return new File(outputPath);
        } catch (IOException e) {
            final String msg = "Error writing equivalencer header.";
            throw new IndexingFailure(resourceLocation, msg, e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {}
            }
        }
    }
}
