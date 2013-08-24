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

import static org.openbel.framework.common.BELUtilities.asPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataInvalidPropertyException;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;
import org.openbel.framework.core.indexer.IndexingFailure;

/**
 * AnnotationHeaderProcessor processes an annotation {@link File} to produce a
 * {@link AnnotationHeader} that is written to the file system.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class AnnotationHeaderProcessor {

    /**
     * Defines the byte offset in the annotation file where the annotation header
     * parsing ended.  This allows further parsing of a annotation file from a
     * known offset.
     */
    private long characterStopOffset = -1;

    /**
     * Returns the byte offset in the annotation file where the annotation header
     * parsing ended.
     *
     * @return <tt>long</tt> the character offset where the parsing of a
     * annotation file ended
     */
    public long getCharacterStopOffset() {
        return characterStopOffset;
    }

    /**
     * Processes a annotation {@link File} into a {@link annotationHeader} that
     * is then written to the file system.
     *
     * @param rawannotation {@link File}, the raw annotation file to parse
     * @param annotationHomeFile {@link File}, the annotation header file to
     * write to
     * @return {@link AnnotationHeader} the parsed annotation header
     * @throws IndexingFailure Throws an annotation indexing exception is the
     * annotation header processing fails
     */
    public AnnotationHeader processAnnotationHeader(String resourceLocation,
            File rawannotation, File annotationHomeFile)
            throws IndexingFailure {
        AnnotationHeaderParser ahParser = new AnnotationHeaderParser();
        AnnotationHeader annotationHeader;

        try {
            annotationHeader = ahParser.parseAnnotation(resourceLocation,
                    rawannotation);
            characterStopOffset = ahParser.getNextCharacterOffset();
        } catch (IOException e) {
            throw new IndexingFailure(resourceLocation, e);
        } catch (BELDataMissingPropertyException e) {
            throw new IndexingFailure(resourceLocation, e);
        } catch (BELDataConversionException e) {
            throw new IndexingFailure(resourceLocation, e);
        } catch (BELDataInvalidPropertyException e) {
            throw new IndexingFailure(resourceLocation, e);
        }

        String output = asPath(annotationHomeFile.getAbsolutePath(),
                "annotation.header");

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(
                    new FileOutputStream(output));
            oos.writeObject(annotationHeader);
        } catch (IOException e) {
            final String msg = "Error writing annotation header.";
            throw new IndexingFailure(resourceLocation, msg, e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {}
            }
        }

        return annotationHeader;
    }
}
