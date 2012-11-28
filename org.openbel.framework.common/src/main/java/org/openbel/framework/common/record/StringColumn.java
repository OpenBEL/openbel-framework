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
package org.openbel.framework.common.record;

import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;
import static org.openbel.framework.common.Strings.UTF_8;

import java.io.UnsupportedEncodingException;

/**
 * Represents a {@link Column} of type {@link String} where the length of the
 * string is arbitrary.
 * <p>
 * This column implementation pads strings with spaces during encoding if the
 * strings are less than the column's size. During encoding, the padding is
 * removed. Encoding strings with length greater than the column's size will
 * result in truncation.
 * </p>
 *
 * @author Nick Bargnesi
 */
public class StringColumn extends Column<String> {
    static final String EMPTY = "";
    static final byte space = 32;
    static final byte[] nullValue;
    static {
        nullValue = new byte[space];
        fill(nullValue, space);
        nullValue[0] = (byte) 0;
    }

    /**
     * Creates a non-null string column of the specified {@code length}.
     *
     * @param length Maximum length of the string
     */
    public StringColumn(int length) {
        super(length, false);
    }

    /**
     * Creates a string column of the specified {@code length} and nullability.
     *
     * @param length Maximum length of the string
     * @param nullable {@code true} if the columns is nullable, {@code false}
     * otherwise
     */
    public StringColumn(int length, boolean nullable) {
        super(length, nullable);
    }

    /**
     * Creates a string column of an unknown length and the specified
     * nullability.
     *
     * @param nullable {@code true} if the columns is nullable, {@code false}
     * otherwise
     */
    public StringColumn(boolean nullable) {
        super(nullable);
    }

    /**
     * Creates a non-null string column of an unknown length.
     */
    public StringColumn() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String decodeData(byte[] buffer) {
        // buffer is guaranteed non-null and proper length
        String ret = new String(buffer);
        ret = ret.trim();
        return ret.isEmpty() ? EMPTY : ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte[] encodeType(String t) {
        byte[] ret = new byte[size];
        fill(ret, space);
        byte[] tbytes;
        try {
            tbytes = t.getBytes(UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }

        int length;
        if (tbytes.length <= size) {
            length = tbytes.length;
        } else {
            length = size;
        }

        arraycopy(tbytes, 0, ret, 0, length);
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte[] getNullValue() {
        byte[] ret = new byte[size];
        arraycopy(nullValue, 0, ret, 0, size);
        return ret;
    }
}
