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
