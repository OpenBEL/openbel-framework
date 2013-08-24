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

import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;
import static org.openbel.framework.common.record.IntegerColumn.nonNullIntegerColumn;
import static org.openbel.framework.common.record.LongColumn.nonNullLongColumn;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.record.IntegerColumn;
import org.openbel.framework.common.record.LongColumn;
import org.openbel.framework.common.record.Record;
import org.openbel.framework.common.record.StringColumn;

/**
 * {@link Record} unit tests.
 */
public class RecordTest {
    MockRecord test;
    Random random;

    /**
     * Test case setup.
     */
    @Before
    public void before() {
        test = new MockRecord();
        random = new Random(currentTimeMillis());
    }

    /**
     * Tests writing and reading back.
     */
    @Test
    public void testReadBack() {
        // long/int/string -> 8 + 4 + 13
        assertEquals(25, test.recordSize);
        Entry write = new Entry();
        write.hash = random.nextLong();
        write.number = random.nextInt();
        write.string = valueOf(currentTimeMillis());
        byte[] bytes = test.write(write);
        Entry read = test.read(bytes);
        assertEquals(write.hash, read.hash);
        assertEquals(write.number, read.number);
        assertEquals(write.string, read.string);
    }

    private static class MockRecord extends Record<Entry> {
        private final static LongColumn longCol = nonNullLongColumn();
        private final static IntegerColumn intCol = nonNullIntegerColumn();
        private final static StringColumn strCol = new StringColumn(13);

        public MockRecord() {
            super(longCol, intCol, strCol);
        }

        @Override
        protected Entry read(byte[] buffer) {
            Entry ret = new Entry();
            ret.hash = longCol.decode(readColumn(buffer, 0));
            ret.number = intCol.decode(readColumn(buffer, 1));
            ret.string = strCol.decode(readColumn(buffer, 2));
            return ret;
        }

        @Override
        protected void readTo(byte[] buffer, Entry t) {
            t.hash = longCol.decode(readColumn(buffer, 0));
            t.number = intCol.decode(readColumn(buffer, 1));
            t.string = strCol.decode(readColumn(buffer, 2));
        }

        @Override
        protected byte[] write(Entry t) {
            byte[] record = new byte[recordSize];
            byte[] bytesL = longCol.encode(t.hash);
            writeColumn(bytesL, 0, record);
            byte[] bytesI = intCol.encode(t.number);
            writeColumn(bytesI, 1, record);
            byte[] strbytes = strCol.encode(t.string);
            writeColumn(strbytes, 2, record);
            return record;
        }

        @Override
        protected void writeTo(Entry t, byte[] buffer) {
            byte[] bytesL = longCol.encode(t.hash);
            writeColumn(bytesL, 0, buffer);
            byte[] bytesI = intCol.encode(t.number);
            writeColumn(bytesI, 1, buffer);
            byte[] strbytes = strCol.encode(t.string);
            writeColumn(strbytes, 2, buffer);
        }
    }

    private static class Entry {
        long hash;
        int number;
        String string;
    }
}
