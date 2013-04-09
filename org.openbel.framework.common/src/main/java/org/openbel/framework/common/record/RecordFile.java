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

import static java.lang.String.format;
import static java.lang.System.arraycopy;
import static java.nio.ByteBuffer.allocate;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.fill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.openbel.framework.common.InvalidArgument;

/**
 * Record files are a representation of files that contain fixed-length records.
 * As such, the size of these files are multiples of their associated record
 * size. The first record of every record file contains metadata indicating the
 * record size.
 * <p>
 * It is helpful to consider the contents of these objects in a tabular manner.
 *
 * <pre>
 * <i>row number</i>: column one, column two, [...], column n
 * </pre>
 *
 * An example of a record file's contents containing two columns, each with a
 * size of 8 bytes.
 *
 * <pre>
 * <i>0:</i> 0xffffffff 0xffffffff
 * <i>1:</i> 0xffffffff 0xffffffff
 * </pre>
 *
 * </p>
 * <p>
 * When a record file is created with a existing file, the size of the last
 * column will be set at runtime by reading the metadata. Record files are not
 * thread-safe.
 * </p>
 *
 * @author Nick Bargnesi
 */
public class RecordFile implements Iterable<byte[]> {
    private static final String RO_MSG;
    static {
        RO_MSG = "record file is read-only";
    }

    final String path;
    final RandomAccessFile raf;
    final Column<?>[] columns;
    final int recordSize;
    final boolean readonly;
    long recordCt;
    long size;

    /**
     * Creates a record file associated with the supplied {@link File path} and
     * {@link Column columns}.
     *
     * @param path {@link File Path}; may not be null. If the path does not
     * exist it will be created.
     * @param mode {@link RecordMode}; {@link RecordMode#READ_ONLY read-only} or
     * {@link RecordMode#READ_WRITE read/write}
     * @param columns {@link Column Columns} composing each record; may not be
     * null or have zero length
     * @throws InvalidArgument Thrown if {@code path} or {@code columns} is null
     * or invalid
     */
    public RecordFile(final File path, final RecordMode mode,
            final Column<?>... columns) {

        // TODO clean up this constructor

        if (path == null) {
            throw new InvalidArgument("path", path);
        } else if (mode == null) {
            throw new InvalidArgument("mode", mode);
        } else if (columns == null) {
            throw new InvalidArgument("columns", columns);
        } else if (columns.length == 0) {
            throw new InvalidArgument("invalid number of columns");
        } else if (!path.exists()) {
            if (mode == RecordMode.READ_ONLY) {
                // Read-only mode?
                final String fmt = "cannot read path: %s";
                final String msg = format(fmt, path);
                throw new InvalidArgument(msg);
            }
            // Read-write mode, create a new file
            try {
                boolean b = path.createNewFile();
                if (!b) {
                    throw new IOException();
                }
            } catch (IOException e) {
                final String fmt = "error creating: %s";
                final String msg = format(fmt, path);
                throw new InvalidArgument(msg, e);
            }
        } else if (!path.canRead()) {
            final String fmt = "cannot read path: %s";
            final String msg = format(fmt, path);
            throw new InvalidArgument(msg);
        }

        try {
            if (mode == RecordMode.READ_ONLY) {
                // Open a random access file in read-only mode
                this.raf = new RandomAccessFile(path, "r");
                readonly = true;
            } else {
                // Open a random access file in read/write mode
                this.raf = new RandomAccessFile(path, "rw");
                readonly = false;
            }
        } catch (FileNotFoundException e) {
            throw new InvalidArgument(e);
        }
        this.columns = columns;
        this.path = path.getPath();

        int size = 0, i = 0;
        for (; i < columns.length; i++) {
            Column<?> c = columns[i];
            if (c.size == 0 && i != (columns.length - 1)) {
                // only the last column's size may be zero
                final String msg = "only the last column size may be unknown";
                throw new InvalidArgument(msg);
            }
            size += c.size;
        }

        Column<?> last = columns[i - 1];
        if (last.size == 0) {
            final byte[] intbytes = new byte[4];
            int read;
            try {
                read = raf.read(intbytes);
            } catch (IOException e) {
                final String msg = "failed to read record size";
                throw new InvalidArgument(msg, e);
            }
            if (read != 4) {
                final String fmt = "read %d bytes, but expected %d";
                final String msg = format(fmt, read, 4);
                throw new InvalidArgument(msg);
            }
            final ByteBuffer bytebuf = allocate(4);
            bytebuf.put(intbytes);
            bytebuf.rewind();
            recordSize = bytebuf.getInt();
            last.size = recordSize - size;
        } else {
            recordSize = size;
        }

        if (recordSize <= 0) {
            final String fmt = "invalid record size: %d";
            final String msg = format(fmt, recordSize);
            throw new InvalidArgument(msg);
        }

        this.size = path.length();
        if (this.size == 0) {
            if (readonly) {
                throw new UnsupportedOperationException(RO_MSG);
            }
            // Empty file - write metadata
            final ByteBuffer buffer = allocate(4);
            buffer.putInt(recordSize);
            byte[] array = buffer.array();
            byte[] metadata = new byte[recordSize];
            fill(metadata, (byte) 0);
            arraycopy(array, 0, metadata, 0, 4);
            try {
                raf.seek(0);
                raf.write(metadata);
            } catch (IOException e) {
                final String msg = "failed to initialize metadata";
                throw new InvalidArgument(msg, e);
            }
            this.size = path.length();
        } else if ((this.size % recordSize) != 0) {
            final String fmt = "file size %d: not a multiple of %d";
            final String msg = format(fmt, this.size, recordSize);
            throw new InvalidArgument(msg);
        }
        recordCt = this.size / recordSize - 1;
    }

    /**
     * Reads a record from this record file.
     *
     * @param record The record to read
     * @return {@code byte[]}
     * @throws IOException Throw if an I/O error occurs during I/O seeks or
     * reads
     */
    public byte[] read(long record) throws IOException {
        // (+ recordSize) allows skipping metadata record
        long pos = record * recordSize + recordSize;
        if (pos >= size) {
            final String fmt = "bad seek to position: %d (size is %d)";
            final String msg = format(fmt, pos, size);
            throw new InvalidArgument(msg);
        }

        raf.seek(pos);
        byte[] ret = new byte[recordSize];
        int read = raf.read(ret);

        if (read != recordSize) {
            final String fmt = "read %d bytes, but expected %d";
            final String msg = format(fmt, read, recordSize);
            throw new InvalidArgument(msg);
        }
        return ret;
    }

    /**
     * Reads a record from this record file into the provided byte buffer.
     *
     * @param record The record to read
     * @param bytes Byte buffer to read into
     * @throws IOException Throw if an I/O error occurs during I/O seeks or
     * reads
     */
    public void read(long record, byte[] bytes) throws IOException {
        if (bytes.length != recordSize) {
            final String fmt = "invalid buffer length of %d bytes, expected %d";
            final String msg = format(fmt, bytes.length, recordSize);
            throw new InvalidArgument(msg);
        }

        // (+ recordSize) allows skipping metadata record
        long pos = record * recordSize + recordSize;

        if (pos >= size) {
            final String fmt = "bad seek to position: %d (size is %d)";
            final String msg = format(fmt, pos, size);
            throw new InvalidArgument(msg);
        }

        raf.seek(pos);
        int read = raf.read(bytes);

        if (read != recordSize) {
            final String fmt = "read %d bytes, but expected %d";
            final String msg = format(fmt, read, recordSize);
            throw new InvalidArgument(msg);
        }
    }

    /**
     * Reads a record for a specific columns from this record file.
     *
     * @param record The record to read
     * @param column The column to read from
     * @return {@code byte[]}
     * @throws IOException Thrown if an I/O error occurs during I/O seeks or
     * read or an invalid number of bytes were read
     */
    public byte[] read(long record, int column) throws IOException {
        // (+ recordSize) allows skipping metadata record
        long pos = record * recordSize + recordSize;

        int i = 0;
        for (; i < column; i++) {
            pos += columns[i].size;
        }
        if (pos >= size) {
            final String fmt = "bad seek to position: %d (size is %d)";
            final String msg = format(fmt, pos, size);
            throw new InvalidArgument(msg);
        }
        Column<?> c = columns[i - 1];

        raf.seek(pos);
        byte[] ret = new byte[c.size];
        int read = raf.read(ret);

        if (read != c.size) {
            final String fmt = "read %d bytes, but expected %d";
            final String msg = format(fmt, read, c.size);
            throw new IOException(msg);
        }
        return ret;
    }

    /**
     * Reads a record for a specific columns from this record file into the
     * provided byte buffer.
     *
     * @param record The record to read
     * @param column The column to read from
     * @param bytes Byte buffer to read into
     * @throws IOException Thrown if an I/O error occurs during I/O seeks or
     * read or an invalid number of bytes were read
     */
    public void read(long record, int column, byte[] bytes) throws IOException {
        // (+ recordSize) allows skipping metadata record
        long pos = record * recordSize + recordSize;

        int i = 0;
        for (; i < column; i++) {
            pos += columns[i].size;
        }
        if (pos >= size) {
            final String fmt = "bad seek to position: %d (size is %d)";
            final String msg = format(fmt, pos, size);
            throw new InvalidArgument(msg);
        }
        Column<?> c = columns[i - 1];

        raf.seek(pos);
        if (bytes.length != c.size) {
            final String fmt = "invalid buffer length of %d bytes, expected %d";
            final String msg = format(fmt, bytes.length, c.size);
            throw new InvalidArgument(msg);
        }
        int read = raf.read(bytes);

        if (read != c.size) {
            final String fmt = "read %d bytes, but expected %d";
            final String msg = format(fmt, read, c.size);
            throw new IOException(msg);
        }
    }

    /**
     * Writes a record to this record file, overwriting the previous contents.
     *
     * @param record The record being written
     * @param bytes The bytes to write
     * @throws IOException Thrown if an I/O error occurs during I/O seeks or
     * writes
     * @throws UnsupportedOperationException Thrown if the mode is set to
     * read-only
     * @throws InvalidArgument Thrown if a write of anything other than the size
     * of the record is requested
     */
    public void write(long record, byte[] bytes) throws IOException {
        if (readonly) {
            throw new UnsupportedOperationException(RO_MSG);
        }
        if (bytes.length != recordSize) {
            final String fmt = "invalid write of %d bytes, expected %d";
            final String msg = format(fmt, bytes.length, recordSize);
            throw new InvalidArgument(msg);
        }
        // (+ recordSize) allows skipping metadata record
        long pos = record * recordSize + recordSize;
        if (pos >= size) {
            final String fmt = "bad seek to position: %d (size is %d)";
            final String msg = format(fmt, pos, size);
            throw new InvalidArgument(msg);
        }
        raf.seek(pos);
        raf.write(bytes);
    }

    /**
     * Writes a record for a specific column to this record file, overwriting
     * the previous contents.
     *
     * @param record The record being written
     * @param column The column to write
     * @param bytes The bytes to write
     * @throws IOException Thrown if an I/O error occurs during I/O seeks or
     * writes
     * @throws InvalidArgument Thrown if a write of anything other than the size
     * of the column is requested
     * @throws UnsupportedOperationException Thrown if the mode is set to
     * read-only
     */
    public void write(long record, int column, byte[] bytes) throws IOException {
        if (readonly) {
            throw new UnsupportedOperationException(RO_MSG);
        }

        // (+ recordSize) allows skipping metadata record
        long pos = record * recordSize + recordSize;

        int i = 0;
        for (; i < column; i++) {
            pos += columns[i].size;
        }
        if (pos >= size) {
            final String fmt = "bad seek to position: %d (size is %d)";
            final String msg = format(fmt, pos, size);
            throw new InvalidArgument(msg);
        }
        Column<?> c = columns[i];
        if (bytes.length != c.size) {
            final String fmt = "invalid write of %d bytes, expected %d";
            final String msg = format(fmt, bytes.length, c.size);
            throw new InvalidArgument(msg);
        }

        raf.seek(pos);
        raf.write(bytes);
    }

    /**
     * Appends a record to this record file, recalculating the
     * {@link #getSize() size} and {@link #getRecordCount() record count}.
     *
     * @param bytes {@code byte[]} with length {@link #getRecordSize()}
     * @throws IOException Thrown if an I/O error occurs during write>
     * @throws InvalidArgument Thrown if the length of {@code bytes} is not
     * equal to the {@link #getRecordSize()}
     * @throws UnsupportedOperationException Thrown if the mode is set to
     * read-only
     */
    public void append(byte[] bytes) throws IOException {
        if (readonly) {
            throw new UnsupportedOperationException(RO_MSG);
        }

        if (bytes.length != recordSize) {
            final String fmt = "invalid write of %d bytes, expected %d";
            final String msg = format(fmt, bytes.length, recordSize);
            throw new InvalidArgument(msg);
        }
        raf.write(bytes);

        // write succeeded - adjust size accordingly
        size += bytes.length;
        recordCt = size / recordSize - 1;
    }

    /**
     * Reads the metadata from this record file.
     *
     * @return {@code byte[]}
     * @throws IOException Throw if an I/O error occurs during I/O seeks or
     * reads
     */
    public byte[] readMetadata() throws IOException {
        byte[] ret = read(0);
        return ret;
    }

    /**
     * Reads the metadata from this record file into the provided byte buffer.
     *
     * @param bytes Byte buffer to read into
     * @throws IOException Throw if an I/O error occurs during I/O seeks or
     * reads
     */
    public void readMetadata(byte[] bytes) throws IOException {
        read(0, bytes);
    }

    /**
     * Writes metadata to this record file overwriting existing metadata.
     *
     * @param bytes The metadata bytes to write
     * @throws IOException Thrown if an I/O error occurs during I/O seeks or
     * writes
     * @throws InvalidArgument Thrown if a write of anything other than the size
     * of the record is requested
     * @throws UnsupportedOperationException Thrown if the mode is set to
     * read-only
     * @deprecated Explicitly writing the metadata record can cause
     * inconsistent {@link RecordFile record files} if used incorrectly.
     * Metadata for the {@link RecordFile record file} should only be written
     * on construction.
     * @see RecordFile#RecordFile(File, RecordMode, Column...)
     */
    @Deprecated
    public void writeMetadata(byte[] bytes) throws IOException {
        if (readonly) {
            throw new UnsupportedOperationException(RO_MSG);
        }

        if (bytes.length != recordSize) {
            final String fmt = "invalid write of %d bytes, expected %d";
            final String msg = format(fmt, bytes.length, recordSize);
            throw new InvalidArgument(msg);
        }
        raf.seek(0);
        raf.write(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<byte[]> iterator() {
        return new Iter();
    }

    /**
     * Returns this record file's path.
     *
     * @return {@link String}
     */
    public final String getPath() {
        return path;
    }

    /**
     * Returns a copy of the record file's columns.
     *
     * @return Array of {@link Column columns}
     */
    public final Column<?>[] getColumns() {
        Column<?>[] ret = copyOf(columns, columns.length);
        return ret;
    }

    /**
     * Returns the size of the records.
     *
     * @return {@code int}
     */
    public final int getRecordSize() {
        return recordSize;
    }

    /**
     * Returns {@code true} if the record file is operating in read-only mode,
     * {@code false} otherwise.
     *
     * @return {@code boolean}
     */
    public final boolean isReadOnly() {
        return readonly;
    }

    /**
     * Returns {@code true} if the record file is operating in read/write mode,
     * {@code false} otherwise.
     *
     * @return {@code boolean}
     */
    public final boolean isReadWrite() {
        return !readonly;
    }

    /**
     * Returns the number of records in the file.
     *
     * @return {@code long}
     */
    public final long getRecordCount() {
        return recordCt;
    }

    /**
     * Returns the size of the record file, in bytes.
     *
     * @return {@code long}
     */
    public final long getSize() {
        return size;
    }

    class Iter implements Iterator<byte[]> {
        private final long expectedRecordCt;
        private long currentRecord;

        /**
         * New {@link RecordFile} iterator.
         */
        public Iter() {
            this.expectedRecordCt = recordCt;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            if (expectedRecordCt != recordCt) {
                throw new ConcurrentModificationException();
            }
            if (currentRecord < recordCt) {
                return true;
            }

            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public byte[] next() {
            byte[] ret;
            try {
                ret = read(currentRecord);
            } catch (IOException e) {
                return null;
            }
            currentRecord += 1;
            return ret;
        }

        /**
         * Throws {@link UnsupportedOperationException}.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
