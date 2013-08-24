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
package org.openbel.framework.core.df.beldata.namespace;

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

/**
 * NamespaceHeaderParser parses a namespace file into a
 * {@link NamespaceHeader} object.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NamespaceHeaderParser extends BELDataHeaderParser {

    /**
     * Parses the namespace {@link File} into a {@link NamespaceHeader} object.
     *
     * @param namespaceFile {@link File}, the namespace file, which cannot be
     * null, must exist, and must be readable
     * @return {@link NamespaceHeader}, the parsed namespace header
     * @throws IOException Thrown if an IO error occurred reading the
     * <tt>namespaceFile</tt>
     * @throws BELDataConversionException
     * @throws BELDataMissingPropertyException
     * @throws InvalidArgument Thrown if the <tt>namespaceFile</tt> is null,
     * does not exist, or cannot be read
     */
    public NamespaceHeader parseNamespace(String resourceLocation,
            File namespaceFile) throws IOException,
            BELDataMissingPropertyException,
            BELDataConversionException {
        if (namespaceFile == null) {
            throw new InvalidArgument("namespaceFile", namespaceFile);
        }

        if (!namespaceFile.exists()) {
            throw new InvalidArgument("namespaceFile does not exist");
        }

        if (!namespaceFile.canRead()) {
            throw new InvalidArgument("namespaceFile cannot be read");
        }

        Map<String, Properties> blockProperties = parse(namespaceFile);

        NamespaceBlock nsblock = NamespaceBlock.create(resourceLocation,
                blockProperties.get(NamespaceBlock.BLOCK_NAME));

        AuthorBlock authorblock = AuthorBlock.create(resourceLocation,
                blockProperties.get(AuthorBlock.BLOCK_NAME));

        CitationBlock citationBlock = CitationBlock.create(resourceLocation,
                blockProperties.get(CitationBlock.BLOCK_NAME));

        ProcessingBlock processingBlock = ProcessingBlock.create(
                resourceLocation,
                blockProperties.get(ProcessingBlock.BLOCK_NAME));

        return new NamespaceHeader(nsblock, authorblock, citationBlock,
                processingBlock);
    }
}
