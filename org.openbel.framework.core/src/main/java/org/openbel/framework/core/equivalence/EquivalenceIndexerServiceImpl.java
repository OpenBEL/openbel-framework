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

import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.PathConstants.EQUIVALENCE_ROOT_DIRECTORY_NAME;

import java.io.File;

import org.openbel.framework.common.model.DataFileIndex;
import org.openbel.framework.core.df.beldata.equivalence.EquivalenceHeaderProcessor;
import org.openbel.framework.core.indexer.Indexer;
import org.openbel.framework.core.indexer.IndexingFailure;

public class EquivalenceIndexerServiceImpl implements EquivalenceIndexerService {

    @Override
    public DataFileIndex indexEquivalence(String equivalenceResourceLocation,
            File rawEquivalenceFile) throws IndexingFailure {

        final File outputPath = rawEquivalenceFile.getParentFile();
        final String home = outputPath.getAbsolutePath();
        String equivalenceHomeLocation = asPath(home,
                EQUIVALENCE_ROOT_DIRECTORY_NAME);

        File equivalenceRootFile = new File(equivalenceHomeLocation);
        EquivalenceHeaderProcessor equivalenceHeaderProcessor =
                new EquivalenceHeaderProcessor();
        File equivalenceHeaderFile = equivalenceHeaderProcessor
                .processEquivalenceHeader(equivalenceResourceLocation,
                        rawEquivalenceFile, equivalenceRootFile);

        long characterStopOffset = equivalenceHeaderProcessor
                .getCharacterStopOffset();

        String equivalenceIndexLocation = asPath(
                equivalenceHeaderFile.getParent(), "equivalence.index");

        if (!new File(equivalenceIndexLocation).exists()) {
            Indexer equivalenceIndexer = new Indexer();
            equivalenceIndexer.indexEquivalenceFile(characterStopOffset,
                    rawEquivalenceFile, equivalenceIndexLocation);
        }

        return new DataFileIndex(equivalenceResourceLocation,
                equivalenceIndexLocation, equivalenceHeaderFile);
    }
}
