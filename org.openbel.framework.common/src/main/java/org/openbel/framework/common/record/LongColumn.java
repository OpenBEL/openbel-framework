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
 * Represents a {@link Column} of type {@link Long} sized to 8 bytes.
 */
public class LongColumn extends Column<Long> {
    private static final LongColumn selfNonNull;
    private static final LongColumn selfNull;
    private static final byte space = 8;
    private static final byte[] nullValue;
    static {
        selfNonNull = new LongColumn(false);
        selfNull = new LongColumn(true);
        final ByteBuffer buffer = allocate(space);
        buffer.putLong(0);
        nullValue = buffer.array();
    }

    /**
     * Creates a long column with a size of {@code 8} bytes.
     */
    private LongColumn(boolean nullable) {
        super(space, nullable);
    }

    /**
     * Returns the {@link LongColumn non-null long column} singleton.
     *
     * @return {@link LongColumn non-null long column}
     */
    public static LongColumn nonNullLongColumn() {
        return selfNonNull;
    }

    /**
     * Returns the {@link LongColumn nullable long column} singleton.
     *
     * @return {@link LongColumn nullable long column}
     */
    public static LongColumn nullLongColumn() {
        return selfNull;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long decodeData(byte[] buffer) {
        final ByteBuffer bytebuf = allocate(size);
        // buffer is guaranteed non-null and proper length
        bytebuf.put(buffer);
        bytebuf.rewind();
        return bytebuf.getLong();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte[] encodeType(Long t) {
        final ByteBuffer buffer = allocate(size);
        // t is guaranteed non-null by superclass
        buffer.putLong(t);
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
