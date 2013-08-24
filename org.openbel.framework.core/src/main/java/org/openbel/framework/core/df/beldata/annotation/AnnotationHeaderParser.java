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
