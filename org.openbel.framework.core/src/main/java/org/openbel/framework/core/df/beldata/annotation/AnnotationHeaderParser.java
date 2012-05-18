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
package org.openbel.framework.core.df.beldata.annotation;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.df.beldata.AuthorBlock;
import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataHeaderParser;
import org.openbel.framework.core.df.beldata.BELDataInvalidPropertyException;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;
import org.openbel.framework.core.df.beldata.CitationBlock;
import org.openbel.framework.core.df.beldata.ProcessingBlock;

public class AnnotationHeaderParser extends BELDataHeaderParser {

    /**
     * Parses the annotation {@link File} into a {@link AnnotationHeader} object.
     * 
     * @param annotationFile {@link File}, the annotation file, which cannot be
     * null, must exist, and must be readable
     * @return {@link annotationHeader}, the parsed annotation header
     * @throws IOException Thrown if an IO error occurred reading the
     * <tt>annotationFile</tt>
     * @throws BELDataHeaderParseException Thrown if a parsing error occurred
     * when processing the <tt>annotationFile</tt>
     * @throws BELDataConversionException 
     * @throws BELDataMissingPropertyException 
     * @throws BELDataInvalidPropertyException 
     * @throws InvalidArgument Thrown if the <tt>annotationFile</tt> is null,
     * does not exist, or cannot be read
     */
    public AnnotationHeader parseAnnotation(String resourceLocation,
            File annotationFile) throws IOException,
            BELDataMissingPropertyException,
            BELDataConversionException, BELDataInvalidPropertyException {
        if (annotationFile == null) {
            throw new InvalidArgument("annotationFile", annotationFile);
        }

        if (!annotationFile.exists()) {
            throw new InvalidArgument("annotationFile does not exist");
        }

        if (!annotationFile.canRead()) {
            throw new InvalidArgument("annotationFile cannot be read");
        }

        Map<String, Properties> blockProperties = parse(annotationFile);

        AnnotationBlock annotationBlock =
                AnnotationBlock.create(resourceLocation,
                        blockProperties.get(AnnotationBlock.BLOCK_NAME));

        AuthorBlock authorblock = AuthorBlock.create(resourceLocation,
                blockProperties.get(AuthorBlock.BLOCK_NAME));

        CitationBlock citationBlock = CitationBlock.create(resourceLocation,
                blockProperties.get(CitationBlock.BLOCK_NAME));

        ProcessingBlock processingBlock = ProcessingBlock.create(
                resourceLocation,
                blockProperties.get(ProcessingBlock.BLOCK_NAME));

        return new AnnotationHeader(annotationBlock, authorblock,
                citationBlock,
                processingBlock);
    }
}
