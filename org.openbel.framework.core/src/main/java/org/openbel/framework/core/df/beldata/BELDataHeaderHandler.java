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
