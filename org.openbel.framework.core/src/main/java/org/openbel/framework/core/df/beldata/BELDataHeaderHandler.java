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
package org.openbel.framework.core.df.beldata;

/**
 * BELDataHeaderHandler defines a parser handler to interpret header blocks and
 * block properties coming from the {@link BELDataHeaderParser}.  It is intended for
 * the class implementation of {@link BELDataHeaderHandler} to interpret the values.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface BELDataHeaderHandler {

    /**
     * Process a {@code headerBlock} coming from the {@link BELDataHeaderParser}.  This
     * operation is called when a BELData file header block has been read.  To abort processing
     * of the file after this point throw {@link AbortParsingException}.
     *
     * @param headerBlock {@link String}, the header block just parsed from {@link BELDataHeaderParser}
     * @param tokenStartOffset {@code long}, the offset in the file to the start of the header block token
     * @throws AbortParsingException, can be thrown by the class implementation to stop parsing
     */
    public void onHeaderBlock(String headerBlock, long tokenStartOffset)
            throws AbortParsingException;

    /**
     * Process a {@code propertyKey} and {@code propertyValue} coming from the {@link BELDataHeaderParser}.
     * This operation is called when a BELDData file header property has been read.  To abort processing
     * of the file after this point throw {@link AbortParsingException}.
     *
     * @param propertyKey {@link String}, the property key just parsed from {@link BELDataHeaderParser}
     * @param propertyValue {@link String}, the property value just parsed from {@link BELDataHeaderParser}
     * @param tokenStartOffset {@code long}, the offset in the file to the start of the block property
     * @throws AbortParsingException, can be thrown by the class implementation to stop parsing
     */
    public void onBlockProperty(String propertyKey, String propertyValue,
            long tokenStartOffset)
            throws AbortParsingException;
}
