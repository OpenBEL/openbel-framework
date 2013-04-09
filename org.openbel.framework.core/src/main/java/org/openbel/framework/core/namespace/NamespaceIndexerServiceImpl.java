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
