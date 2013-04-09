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

import org.openbel.framework.common.InvalidArgument;

/**
 * Models a record for some {@code <T>} contained by a {@link RecordFile}.
 *
 * @param <T> The modeled item
 * @author Nick Bargnesi
 */
public abstract class Record<T> {
    protected final Column<?>[] columns;
    protected final int recordSize;

    /**
     * Creates a record associated with fixed-size columns.
     *
     * @param columns {@link Column Columns} composing each record; may not be
     * null or have zero length
     * @throws InvalidArgument Thrown if {@code columns} is null or invalid
     */
    public Record(Column<?>... columns) {
        if (columns == null) {
            throw new InvalidArgument("columns", columns);
        } else if (columns.length == 0) {
            throw new InvalidArgument("invalid number of columns");
        }
        this.columns = columns;

        int size = 0;
        for (final Column<?> c : columns) {
            if (c.size == 0) {
                throw new InvalidArgument("column size may not be 0");
            }
            size += c.size;
        }
        recordSize = size;
    }

    /**
     * Creates a record associated with some number of columns with the last
     * columns having an unknown size.
     *
     * @param recordSize The record size used to derive the size of the last
     * column
     * @param columns {@link Column Columns} composing each record; may not be
     * zero or have zero length.
     * @throws InvalidArgument Thrown if {@code columns} is null, zero-length,
     * or invalid
     */
    public Record(final int recordSize, Column<?>... columns) {
        if (columns == null) {
            throw new InvalidArgument("columns", columns);
        } else if (columns.length == 0) {
            throw new InvalidArgument("invalid number of columns");
        }
        this.columns = columns;

        int size = 0, i = 0;
        for (; i < columns.length; i++) {
            Column<?> c = columns[i];
            final boolean lastColumn = (i == (columns.length - 1));
            if (lastColumn && c.size != 0) {
                // last column's size must be zero
                final String msg = "the last column size must be unknown";
                throw new InvalidArgument(msg);
            } else if (!lastColumn && c.size == 0) {
                // column size may not be zero here
                final String msg = "only the last column size may be unknown";
                throw new InvalidArgument(msg);
            }
            size += c.size;
        }

        columns[i - 1].setSize(recordSize - size);
        this.recordSize = recordSize;
    }

    /**
     * Returns the {@code column's} byte array data contained in {@code buffer}.
     *
     * @param buffer {@code byte[]}; must be non-null and of the same length as
     * {@link #getRecordSize()}
     * @param column Column to read
     * @return {@code byte[]}
     * @throws InvalidArgument Thrown if {@code buffer} is null or invalid
     */
    public final byte[] readColumn(byte[] buffer, int column) {
        if (buffer == null) {
            throw new InvalidArgument("buffer", buffer);
        } else if (buffer.length != recordSize) {
            final String fmt = "invalid buffer (%d bytes, expected %d)";
            final String msg = format(fmt, buffer.length, recordSize);
            throw new InvalidArgument(msg);
        }

        int i = 0, offset = 0;
        for (; i < column; i++) {
            offset += columns[i].size;
        }
        Column<?> c = columns[i];

        byte[] ret = new byte[c.size];
        arraycopy(buffer, offset, ret, 0, c.size);
        return ret;
    }

    /**
     * Copies the {@code column's} byte array data contained in
     * {@code recBuffer} into the {@code colBuffer}.
     *
     * @param recBuffer {@code byte[]}; must be non-null and of the same length
     * as {@link #getRecordSize()}
     * @param colBuffer {@code byte[]}; must be non-null and of the same length
     * as the {@code column} size
     * @param column Column to read
     * @throws InvalidArgument Thrown either buffer or column is null or invalid
     */
    public final void readColumn(byte[] recBuffer,
            byte[] colBuffer,
            int column) {
        if (recBuffer == null) {
            throw new InvalidArgument("recBuffer", recBuffer);
        } else if (colBuffer == null) {
            throw new InvalidArgument("colBuffer", colBuffer);
        } else if (recBuffer.length != recordSize) {
            final String fmt = "invalid record buffer (%d bytes, expected %d)";
            final String msg = format(fmt, recBuffer.length, recordSize);
            throw new InvalidArgument(msg);
        }

        int i = 0, offset = 0;
        for (; i < column; i++) {
            offset += columns[i].size;
        }
        Column<?> c = columns[i];

        if (colBuffer.length != c.size) {
            final String fmt = "invalid column buffer (%d bytes, expected %d)";
            final String msg = format(fmt, colBuffer.length, c.size);
            throw new InvalidArgument(msg);
        }

        arraycopy(recBuffer, offset, colBuffer, 0, c.size);
    }

    /**
     * Writes the column's buffer byte array data into the {@code column}
     * contained by the record buffer.
     *
     * @param colBuffer Column buffer data
     * @param column Column to write
     * @param recBuffer Record buffer
     * @throws InvalidArgument Thrown if either buffer or column is invalid, or
     * either buffer is null
     */
    public final void writeColumn(byte[] colBuffer,
            int column,
            byte[] recBuffer) {
        if (colBuffer == null) {
            throw new InvalidArgument("colBuffer may not be null");
        } else if (recBuffer == null) {
            throw new InvalidArgument("recBuffer may not be null");
        } else if (column < 0 || column >= columns.length) {
            final String fmt = "invalid column: %d";
            final String msg = format(fmt, column);
            throw new InvalidArgument(msg);
        } else if (recBuffer.length != recordSize) {
            final String fmt = "invalid record buffer (%d bytes, expected %d)";
            final String msg = format(fmt, recBuffer.length, recordSize);
            throw new InvalidArgument(msg);
        }

        int i = 0, offset = 0;
        for (; i < column; i++) {
            offset += columns[i].size;
        }
        Column<?> c = columns[i];

        if (colBuffer.length != c.size) {
            final String fmt = "invalid column buffer (%d bytes, expected %d)";
            final String msg = format(fmt, colBuffer.length, c.size);
            throw new InvalidArgument(msg);
        }

        arraycopy(colBuffer, 0, recBuffer, offset, colBuffer.length);
        return;
    }

    /**
     * Returns the record size (the summation of all column sizes).
     *
     * @return {@code int}
     */
    public final int getRecordSize() {
        return recordSize;
    }

    /**
     * Reads {@code <T>} from a byte buffer.
     *
     * @param buffer {@code byte[]}; of size {@link #getRecordSize()}
     * @return {@code <T>}
     * @throws InvalidArgument Thrown if {@code buffer} is null or invalid
     */
    public final T readBuffer(byte[] buffer) {
        if (buffer == null) {
            throw new InvalidArgument("buffer", buffer);
        } else if (buffer.length != recordSize) {
            final String fmt = "invalid buffer (%d bytes, expected %d)";
            final String msg = format(fmt, buffer.length, recordSize);
            throw new InvalidArgument(msg);
        }
        return read(buffer);
    }

    /**
     * Reads {@code <T>} to the provided {@code buffer}.
     *
     * @param buffer {@code byte[]}; of size {@link #getRecordSize()}
     * @param t {@code <T>}
     * @throws InvalidArgument Thrown if either argument is null or if
     * {@code buffer} is invalid
     */
    public final void readToEntry(byte[] buffer, T t) {
        if (buffer == null) {
            throw new InvalidArgument("buffer", buffer);
        } else if (t == null) {
            throw new InvalidArgument("cannot read a null entry");
        } else if (buffer.length != recordSize) {
            final String fmt = "invalid buffer (%d bytes, expected %d)";
            final String msg = format(fmt, buffer.length, recordSize);
            throw new InvalidArgument(msg);
        }
        readTo(buffer, t);
    }

    /**
     * Writes {@code <T>} to a byte buffer.
     *
     * @param t {@code <T>}
     * @return {@code byte[]}; of size {@link #getRecordSize()}
     * @throws InvalidArgument Thrown if {@code t} is null
     */
    public final byte[] writeEntry(T t) {
        if (t == null) {
            throw new InvalidArgument("cannot write a null");
        }
        return write(t);
    }

    /**
     * Writes {@code <T>} to the provided {@code buffer}.
     *
     * @param t {@code <T>}
     * @param buffer {@code byte[]}; of size {@link #getRecordSize()}
     * @throws InvalidArgument Thrown if either argument is null or if
     */
    public final void writeToBuffer(T t, byte[] buffer) {
        if (t == null) {
            throw new InvalidArgument("cannot write a null");
        } else if (buffer == null) {
            throw new InvalidArgument("cannot write to a null buffer");
        } else if (buffer.length != recordSize) {
            final String fmt = "invalid buffer (%d bytes, expected %d)";
            final String msg = format(fmt, buffer.length, recordSize);
            throw new InvalidArgument(msg);
        }
        writeTo(t, buffer);
    }

    /**
     * Returns an empty, uninitialized record.
     *
     * @return {@code byte[]}
     * @see java.util.Arrays#fill(byte[], byte)
     */
    public byte[] emptyRecord() {
        return new byte[recordSize];
    }

    /**
     * Internal {@link #readBuffer(byte[])} implementation deferred to
     * {@link Record record} subclasses.
     *
     * @param buffer {@code byte[]}; guaranteed to be non-null and of the
     * correct size (@link {@link #getRecordSize()})
     * @return {@code <T>}
     */
    protected abstract T read(byte[] buffer);

    /**
     * Internal {@link Record#readToEntry(byte[], Object)} implementation
     * deferred to {@link Record record} subclasses.
     *
     * @param buffer {@code byte[]}; guaranteed to be non-null and of the
     * correct size (@link {@link #getRecordSize()})
     * @param t {@code <T>}; guaranteed to be non-null
     */
    protected abstract void readTo(byte[] buffer, T t);

    /**
     * Internal {@link Record#write(Object)} implementation deferred to
     * {@link Record record} subclasses.
     *
     * @param t {@code <T>}; guaranteed to be non-null
     * @return {@code byte[]}
     */
    protected abstract byte[] write(T t);

    /**
     * Internal {@link Record#writeToBuffer(Object, byte[])} implementation
     * deferred to {@link Record record} subclasses.
     *
     * @param t {@code <T>}; guaranteed to be non-null
     * @param buffer {@code byte[]}; guaranteed to be non-null and of the
     * correct size (@link {@link #getRecordSize()})
     */
    protected abstract void writeTo(T t, byte[] buffer);

}
