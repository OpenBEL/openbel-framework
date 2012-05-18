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
import static java.nio.ByteBuffer.allocate;

import java.nio.ByteBuffer;

/**
 * Represents a {@link Column} of type {@link Integer} sized to 4 bytes.
 * 
 * @author Nick Bargnesi
 */
public class IntegerColumn extends Column<Integer> {
    private static final IntegerColumn selfNonNull;
    private static final IntegerColumn selfNull;
    private static final byte space = 4;
    private static final byte[] nullValue;
    static {
        selfNonNull = new IntegerColumn(false);
        selfNull = new IntegerColumn(true);
        final ByteBuffer buffer = allocate(space);
        buffer.putInt(0);
        nullValue = buffer.array();
    }

    /**
     * Creates an integer column with a size of {@code 4} bytes.
     */
    private IntegerColumn(boolean nullable) {
        super(space, nullable);
    }

    /**
     * Returns the {@link IntegerColumn non-null integer column} singleton.
     * 
     * @return {@link IntegerColumn non-null integer column}
     */
    public static IntegerColumn nonNullIntegerColumn() {
        return selfNonNull;
    }

    /**
     * Returns the {@link IntegerColumn nullable integer column} singleton.
     * 
     * @return {@link IntegerColumn nullable integer column}
     */
    public static IntegerColumn nullIntegerColumn() {
        return selfNull;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer decodeData(byte[] buffer) {
        final ByteBuffer bytebuf = allocate(size);
        // buffer is guaranteed non-null and proper length
        bytebuf.put(buffer);
        bytebuf.rewind();
        return bytebuf.getInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte[] encodeType(Integer t) {
        final ByteBuffer buffer = allocate(size);
        // t is guaranteed non-null by superclass
        buffer.putInt(t);
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
