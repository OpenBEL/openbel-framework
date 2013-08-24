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
