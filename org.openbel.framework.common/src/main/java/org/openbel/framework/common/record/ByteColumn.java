/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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
import static java.nio.ByteBuffer.allocate;

import java.nio.ByteBuffer;

import org.openbel.framework.common.record.Column;

/**
 * Represents a {@link Column} of type {@link Byte} with a size of 1 byte.
 */
public class ByteColumn extends Column<Byte> {

    private static final ByteColumn selfNonNull;
    private static final ByteColumn selfNull;
    private static final byte space = 1;
    private static final byte[] nullValue;
    static {
        selfNonNull = new ByteColumn(false);
        selfNull = new ByteColumn(true);
        final ByteBuffer buffer = allocate(space);
        buffer.put((byte) 0);
        nullValue = buffer.array();
    }

    /**
     * Return the {@code non-null} {@link ByteColumn} instance.
     *
     * @return {@link ByteColumn}
     */
    public static ByteColumn nonNullByteColumn() {
        return selfNonNull;
    }

    /**
     * Return the {@code nullable} {@link ByteColumn} instance.
     *
     * @return {@link ByteColumn}
     */
    public static ByteColumn nullByteColumn() {
        return selfNull;
    }

    /**
     * Private constructor.
     *
     * @param nullable {@code boolean}; {@code nullable} value or not
     */
    private ByteColumn(boolean nullable) {
        super(space, nullable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Byte decodeData(byte[] buffer) {
        final ByteBuffer bytebuf = allocate(size);
        // buffer is guaranteed non-null and proper length
        bytebuf.put(buffer);
        bytebuf.rewind();
        return bytebuf.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte[] encodeType(Byte t) {
        final ByteBuffer buffer = allocate(size);
        // t is guaranteed non-null by superclass
        buffer.put(t);
        return buffer.array();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte[] getNullValue() {
        final byte[] ret = new byte[size];
        arraycopy(nullValue, 0, ret, 0, size);
        return ret;
    }
}
