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
package org.openbel.framework.core.namespace;

import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.PathConstants.NAMESPACE_ROOT_DIRECTORY_NAME;

import java.io.File;

import org.openbel.framework.common.model.DataFileIndex;
import org.openbel.framework.core.df.beldata.namespace.NamespaceHeader;
import org.openbel.framework.core.df.beldata.namespace.NamespaceHeaderProcessor;
import org.openbel.framework.core.indexer.Indexer;
import org.openbel.framework.core.indexer.IndexingFailure;

/**
 * NamespaceIndexerServiceImpl implements a {@link NamespaceIndexerService}
 * which is responsible for parsing an indexing a BEL namespace document. The
 * procedure is as follows:
 * <ul>
 * <li>Parse the BEL namespace header into a {@link NamespaceHeader} and
 * maintain the character stop byte offset in the file.</li>
 * <li>Serialize the {@link NamespaceHeader} to a {@link File}.</li>
 * <li>Build efficient namespace index by parsing the {@code rawNamespaceFile}
 * starting from the character stop byte offset.</li>
 * <li>Create a {@link DataFileIndex} that will encapsulate both the namespace
 * header and index files.</li>
 * </ul>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NamespaceIndexerServiceImpl implements NamespaceIndexerService {

    /**
     * Creates a namespace indexer service implementation.
     */
    public NamespaceIndexerServiceImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataFileIndex indexNamespace(final String resourceLocation,
            final File rawNamespaceFile) throws IndexingFailure {

        final File outputPath = rawNamespaceFile.getParentFile();
        final String home = outputPath.getAbsolutePath();
        String nsHome = asPath(home, NAMESPACE_ROOT_DIRECTORY_NAME);

        File nsRoot = new File(nsHome);
        nsRoot.mkdir();

        NamespaceHeaderProcessor proc = new NamespaceHeaderProcessor();
        File hdrFile = proc.processNamespaceHeader(
                resourceLocation, rawNamespaceFile, nsRoot);

        long characterStopOffset = proc.getCharacterStopOffset();

        final String nsVersionPath = hdrFile.getParent();
        String nsIdxLoc = asPath(nsVersionPath, "namespace.index");

        Indexer namespaceIndexer = new Indexer();
        namespaceIndexer.indexNamespaceFile(characterStopOffset,
                rawNamespaceFile,
                nsIdxLoc);

        return new DataFileIndex(resourceLocation, nsIdxLoc, hdrFile);
    }
}
