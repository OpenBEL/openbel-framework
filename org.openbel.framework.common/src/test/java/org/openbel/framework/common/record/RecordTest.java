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
