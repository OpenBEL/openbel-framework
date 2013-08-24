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

import static java.io.File.createTempFile;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.System.arraycopy;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.util.Arrays.fill;
import static org.junit.Assert.*;
import static org.openbel.framework.common.record.LongColumn.nonNullLongColumn;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.record.Column;
import org.openbel.framework.common.record.LongColumn;
import org.openbel.framework.common.record.RecordFile;
import org.openbel.framework.common.record.RecordMode;
import org.openbel.framework.common.record.StringColumn;

/**
 * {@link RecordFile} unit tests.
 */
public class RecordFileTest {
    MockRecordFile test;
    Column<?>[] columns;
    MockRecordFile nullTest;
    LongColumn hashColumn;
    StringColumn nullColumn;
    Random random;

    /**
     * Test case setup.
     */
    @Before
    public void before() throws IOException {
        File test = createTempFile("tmp", valueOf(currentTimeMillis()));
        test.deleteOnExit();
        File nullTest = createTempFile("tmp", valueOf(currentTimeMillis()));
        nullTest.deleteOnExit();
        columns = new Column[2];
        columns[0] = nonNullLongColumn();
        columns[1] = columns[0];
        this.test = new MockRecordFile(test, columns);
        this.test.mocksize = 16;

        columns = new Column[2];
        hashColumn = nonNullLongColumn();
        columns[0] = hashColumn;
        nullColumn = new StringColumn(10, true);
        columns[1] = nullColumn;
        this.nullTest = new MockRecordFile(nullTest, columns);
        this.nullTest.mocksize = 8 + columns[1].size;
        random = new Random(currentTimeMillis());
    }

    /**
     * Test {@link RecordFile} appending.
     */
    @Test
    public void testAppend() {
        assertEquals(test.mocksize, test.size);
        byte[] buffer = new byte[test.recordSize];
        random.nextBytes(buffer);
        try {
            test.append(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception: " + e);
        }
        assertEquals(buffer.length + test.mocksize, test.size);
        assertTrue(test.recordCt == 1);
    }

    /**
     * Test {@link RecordFile} writing.
     */
    @Test
    public void testWrite() {
        testAppend();

        // size shouldn't be zero
        assertFalse(test.size == 0);
        // file shouldn't be only metadata
        assertFalse(test.size == test.mocksize);
        // should be one record
        assertTrue(test.recordCt == 1);

        long size = test.size;
        byte[] buffer = new byte[test.recordSize];
        random.nextBytes(buffer);
        try {
            // replace record zero
            test.write(0, buffer);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception: " + e);
        }
        // assert size hasn't changed after record replacement
        assertEquals(size, test.size);
        // should be one record
        assertTrue(test.recordCt == 1);

        int column = 0;
        buffer = new byte[columns[column].size];
        random.nextBytes(buffer);
        try {
            // replace record zero, column
            test.write(0, column, buffer);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception: " + e);
        }
        assertEquals(size, test.size);
        // should be one record
        assertTrue(test.recordCt == 1);
    }

    /**
     * Test {@link RecordFile} reading.
     */
    @Test
    public void testRead() throws IOException {
        assertEquals(test.mocksize, test.size);
        byte[] write = new byte[test.recordSize];
        random.nextBytes(write);
        try {
            test.append(write);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unexpected exception: " + e);
        }

        assertEquals(write.length + test.mocksize, test.size);
        // should be one record
        assertTrue(test.recordCt == 1);

        byte[] read = test.read(0);
        assertTrue(write.length == read.length);
        assertArrayEquals(write, read);
    }

    /**
     * Test reading and writing of {@code null} values in {@link RecordFile}.
     */
    @Test
    public void testReadWriteNull() throws IOException {
        // write long value
        byte[] hashBytes = hashColumn.encode(10L);
        // write null for string column
        byte[] nullBytes = nullColumn.encode(null);

        // write record and assert sizes
        byte[] write = new byte[nullTest.recordSize];
        arraycopy(hashBytes, 0, write, 0, hashBytes.length);
        arraycopy(nullBytes, 0, write, hashBytes.length, nullBytes.length);
        nullTest.append(write);
        assertEquals(write.length + nullTest.mocksize, nullTest.size);
        assertTrue(nullTest.recordCt == 1);

        // read record and assert sizes
        byte[] read = nullTest.read(0);
        assertTrue(write.length == read.length);
        assertArrayEquals(write, read);

        // read long value and assert value
        byte[] longBytes = new byte[8];
        arraycopy(read, 0, longBytes, 0, 8);
        Long hash = hashColumn.decode(longBytes);
        assertEquals(Long.valueOf(10), hash);

        // read null str value and assert null
        byte[] strBytes = new byte[10];
        arraycopy(read, 8, strBytes, 0, 10);
        String strval = nullColumn.decode(strBytes);
        assertNull(strval);
    }

    /**
     * Test {@link RecordFile#iterator()}.
     */
    @Test
    public void testForEach() {
        assertEquals(test.mocksize, test.size);

        // How many records?
        final int records = 100;
        byte[] write = new byte[test.recordSize];
        for (int i = 0; i < records; i++) {
            random.nextBytes(write);
            try {
                test.append(write);
            } catch (IOException e) {
                e.printStackTrace();
                fail("unexpected exception: " + e);
            }
        }

        int count = 0;
        for (byte[] buffer : test) {
            assertNotNull(buffer);
            count++;
        }
        assertTrue((records * test.recordSize + test.mocksize) == test.size);
        assertEquals(records, count);
    }

    /**
     * Test {@link RecordFile#iterator()} throwing
     * {@link ConcurrentModificationException}..
     */
    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModification() {
        assertEquals(test.mocksize, test.size);

        // How many records?
        final int records = 2;
        byte[] write = new byte[test.recordSize];
        for (int i = 0; i < records; i++) {
            random.nextBytes(write);
            try {
                test.append(write);
            } catch (IOException e) {
                e.printStackTrace();
                fail("unexpected exception: " + e);
            }
        }

        int count = 0;
        for (byte[] buffer : test) {
            assertNotNull(buffer);
            try {
                test.append(write);
            } catch (IOException e) {
                e.printStackTrace();
                fail("unexpected exception: " + e);
            }
            count++;
        }
        assertTrue((records * test.recordSize) == test.size);
        assertEquals(records, count);
    }

    /**
     * Test {@link RecordFile} read/write speed.
     *
     * @throws InterruptedException
     */
    @Test
    public void loadtest() throws InterruptedException {
        byte[] buffer = new byte[test.recordSize];
        fill(buffer, (byte) 127);
        long t1 = currentTimeMillis();
        final int records = 1000000;

        for (int i = 0; i < records; i++) {
            try {
                test.append(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }

        long t2 = currentTimeMillis();

        for (int i = 0; i < records; i++) {
            try {
                test.read(i, buffer);
            } catch (IOException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }

        long t3 = currentTimeMillis();

        int bytes = records * test.mocksize;
        int mb = (int) (bytes / 1e6);
        long write_time = t2 - t1;
        long read_time = t3 - t2;

        String fmt = "Wrote %d bytes (%d MB) in %d ms.";
        String msg = format(fmt, bytes, mb, write_time);
        out.println(msg);

        fmt = "Read %d bytes (%d MB) in %d ms.";
        msg = format(fmt, bytes, mb, read_time);
        out.println(msg);
    }

    private static class MockRecordFile extends RecordFile {
        int mocksize;

        public MockRecordFile(File path, Column<?>... columns) {
            super(path, RecordMode.READ_WRITE, columns);
        }
    }

}
