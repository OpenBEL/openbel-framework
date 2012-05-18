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
