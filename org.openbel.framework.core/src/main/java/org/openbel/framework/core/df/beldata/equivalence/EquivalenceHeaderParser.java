/**
 * Copyright (C) 2012 Selventa, Inc.
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
