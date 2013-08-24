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

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.df.beldata.AuthorBlock;
import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataHeaderParser;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;
import org.openbel.framework.core.df.beldata.CitationBlock;
import org.openbel.framework.core.df.beldata.ProcessingBlock;
import org.openbel.framework.core.df.beldata.namespace.NamespaceHeader;

/**
 * EquivalenceHeaderParser parses an equivalence file into a
 * {@link EquivalenceHeader} object.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
class EquivalenceHeaderParser extends BELDataHeaderParser {

    /**
     * Parses the equivalence {@link File} into a {@link EquivalenceHeader} object.
     *
     * @param equivalenceFile {@link File}, the equivalence file, which cannot be
     * null, must exist, and must be readable
     * @return {@link NamespaceHeader}, the parsed equivalence header
     * @throws IOException Thrown if an IO error occurred reading the
     * <tt>equivalenceFile</tt>
     * @throws BELDataHeaderParseException Thrown if a parsing error occurred
     * when processing the <tt>equivalenceFile</tt>
     * @throws BELDataConversionException
     * @throws BELDataMissingPropertyException
     * @throws InvalidArgument Thrown if the <tt>equivalenceFile</tt> is null,
     * does not exist, or cannot be read
     */
    public EquivalenceHeader parseEquivalence(String resourceLocation,
            File equivalenceFile) throws IOException,
            BELDataMissingPropertyException,
            BELDataConversionException {
        if (equivalenceFile == null) {
            throw new InvalidArgument("namespaceFile", equivalenceFile);
        }

        if (!equivalenceFile.exists()) {
            throw new InvalidArgument("namespaceFile does not exist");
        }

        if (!equivalenceFile.canRead()) {
            throw new InvalidArgument("namespaceFile cannot be read");
        }

        Map<String, Properties> blockProperties = parse(equivalenceFile);

        EquivalenceBlock equivalenceBlock =
                EquivalenceBlock.create(resourceLocation,
                        blockProperties.get(EquivalenceBlock.BLOCK_NAME));

        NamespaceReferenceBlock namespaceReferenceBlock =
                NamespaceReferenceBlock
                        .create(resourceLocation,
                                blockProperties
                                        .get(NamespaceReferenceBlock.BLOCK_NAME));

        AuthorBlock authorblock = AuthorBlock.create(resourceLocation,
                blockProperties.get(AuthorBlock.BLOCK_NAME));

        CitationBlock citationBlock = CitationBlock.create(resourceLocation,
                blockProperties.get(CitationBlock.BLOCK_NAME));

        ProcessingBlock processingBlock = ProcessingBlock.create(
                resourceLocation,
                blockProperties.get(ProcessingBlock.BLOCK_NAME));

        return new EquivalenceHeader(equivalenceBlock, namespaceReferenceBlock,
                authorblock, citationBlock, processingBlock);
    }
}
