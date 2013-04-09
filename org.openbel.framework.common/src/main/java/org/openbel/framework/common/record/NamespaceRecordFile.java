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
import static org.openbel.framework.common.record.IntegerColumn.nullIntegerColumn;
import static org.openbel.framework.common.record.LongColumn.nonNullLongColumn;

import java.io.File;
import java.io.IOException;

import org.openbel.framework.common.InvalidArgument;

/**
 * {@link NamespaceRecordFile} defines a {@link RecordFile record file} that
 * contains {@link NamespaceRecord namespace records}.
 *
 * <p>
 * This implementation specifically allows read and write based on the
 * {@link NamespaceRecord namespace record} used to construct this
 * {@link NamespaceRecordFile namespace record file}.
 * </p>
 *
 * <p>
 * The write access to a {@link NamespaceRecordFile namespace record file} is
 * only provided in append mode through the
 * {@link NamespaceRecordFile#append(NamespaceEntry)} api.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NamespaceRecordFile extends RecordFile {
    /**
     * Defines the {@link NamespaceRecord namespace record} to read and write
     * using an {@link NamespaceEntry entry} domain object.
     */
    private final NamespaceRecord nsrec;

    /**
     * Construct a {@link NamespaceRecordFile namespace record file} for
     * read/write purposes.
     *
     * @param path the {@link File file path} to this
     * {@link RecordFile record file}
     * @param mode the {@link RecordMode record mode} for this
     * {@link RecordFile record file}
     * @param nsrec the {@link NamespaceRecord namespace record} which
     * determines the {@link Column columns} in the
     * {@link RecordFile record file}, which cannot be null
     * @throws NullPointerException Thrown if <tt>nsrec</tt> is null
     */
    public NamespaceRecordFile(final File path, final RecordMode mode,
            final NamespaceRecord nsrec) {
        super(path, mode, nsrec.columns);
        this.nsrec = nsrec;
    }

    /**
     * Read a specific record number from the {@link RecordFile record file}
     * as a {@link NamespaceEntry namespace entry}.
     *
     * @param record {@code long} the record number to read
     * @return the {@link NamespaceEntry namespace entry}
     * @throws IOException Thrown if an IO exception occurred reading from the
     * {@link RecordFile record file}
     */
    public NamespaceEntry readEntry(final long record) throws IOException {
        final byte[] recbytes = super.read(record);
        return nsrec.read(recbytes);
    }

    /**
     * Read a specific record into {@link NamespaceEntry namespace entry}
     * object {@code e}.
     *
     * <p>
     * This reuses the entry object given in order to limit garbage collection.
     * </p>
     *
     * @param record {@code long} the record number to read
     * @param e the {@link NamespaceEntry entry} to populate from the record
     * contents
     * @throws IOException Thrown if an IO exception occurred reading from the
     * {@link RecordFile record file}
     */
    public void readToEntry(final long record, final NamespaceEntry e)
            throws IOException {
        final byte[] recbytes = super.read(record);
        nsrec.readTo(recbytes, e);
    }

    /**
     * Write a {@link NamespaceEntry namespace entry} to the end of the
     * {@link NamespaceRecordFile namespace record file}.
     *
     * @param e the {@link NamespaceEntry namespace entry} to append
     * @throws IOException Thrown if an IO exception occurred appending the
     * {@link NamespaceEntry namespace entry} to the
     * {@link NamespaceRecordFile record file}.
     */
    public void append(final NamespaceEntry e) throws IOException {
        final byte[] ebytes = nsrec.write(e);
        append(ebytes);
    }

    /**
     * {@link NamespaceRecord} defines a {@link Record record} that understands
     * how to read and write namespace data represented as {@code bytes} or the
     * encapsulated {@link NamespaceEntry namespace entry}.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    public static class NamespaceRecord extends Record<NamespaceEntry> {
        static final LongColumn hashCol = nonNullLongColumn();
        static final IntegerColumn encCol = nullIntegerColumn();
        static final StringColumn valCol = new StringColumn();

        public NamespaceRecord() {
            super(hashCol, encCol, valCol);
        }

        public NamespaceRecord(final int recordSize) {
            super(recordSize, hashCol, encCol, valCol);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected NamespaceEntry read(byte[] buffer) {
            long hash = hashCol.decode(readColumn(buffer, 0));
            int encoding = encCol.decode(readColumn(buffer, 1));
            String value = valCol.decode(readColumn(buffer, 2));
            return new NamespaceEntry(hash, encoding, value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void readTo(byte[] buffer, NamespaceEntry e) {
            e.hash = hashCol.decode(readColumn(buffer, 0));
            e.encoding = encCol.decode(readColumn(buffer, 1));
            e.value = valCol.decode(readColumn(buffer, 2));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected byte[] write(NamespaceEntry e) {
            byte[] record = emptyRecord();
            writeColumn(hashCol.encode(e.hash), 0, record);
            writeColumn(encCol.encode(e.encoding), 1, record);
            writeColumn(valCol.encode(e.value), 2, record);
            return record;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void writeTo(NamespaceEntry e, byte[] buffer) {
            // validate byte array length matches expected record size
            if (buffer.length != recordSize) {
                final String fmt =
                        "invalid buffer length of %d bytes, expected %d";
                final String msg = format(fmt, buffer.length, recordSize);
                throw new InvalidArgument(msg);
            }

            writeColumn(hashCol.encode(e.hash), 0, buffer);
            writeColumn(encCol.encode(e.encoding), 1, buffer);
            writeColumn(valCol.encode(e.value), 2, buffer);
        }
    }

    /**
     * {@link NamespaceEntry} encapsulates the data held in
     * {@link Column columns} defined by the
     * {@link NamespaceRecord namespace record}.  The following data is stored:
     * <ul>
     *   <li>
     *     encoding
     *     <ul>
     *       <li>32bit integer</li>
     *       <li>holds the encoding for this {@link NamespaceEntry entry}</li>
     *       <li>represented by {@link NamespaceRecord#encCol enc column}</li>
     *     </ul>
     *   </li>
     *   <li>
     *     value
     *     <ul>
     *       <li>varying-length</li>
     *       <li>holds the {@link String value} for this
     *       {@link NamespaceEntry entry}</li>
     *       <li>represented by {@link NamespaceRecord#valCol val column}</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    public static final class NamespaceEntry {
        private long hash;
        private int encoding;
        private String value;

        public NamespaceEntry() {

        }

        public NamespaceEntry(long hash, int encoding, String value) {
            if (value == null) {
                throw new InvalidArgument("value", value);
            }
            this.hash = hash;
            this.encoding = encoding;
            this.value = value;
        }

        public long getHash() {
            return hash;
        }

        public int getEncoding() {
            return encoding;
        }

        public String getValue() {
            return value;
        }
    }
}
