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

import java.util.Arrays;

import org.openbel.framework.common.InvalidArgument;

/**
 * Represents a distinct data type within a record.
 * <p>
 * A column defines the data type's size, decoding, and encoding algorithms.
 * </p>
 *
 * @author Nick Bargnesi
 */
public abstract class Column<T> {
    int size;
    private int hash;
    final boolean nullable;

    /**
     * Creates a non-nullable column of the specified {@code size}.
     * <p>
     * This column is defined as non-nullable by default.
     * </p>
     *
     * @param size Number of bytes defining this {@link Column column}
     */
    Column(int size) {
        if (size <= 0) {
            throw new InvalidArgument(format("%d: bad size", size));
        }
        this.nullable = false;
        setSize(size);
    }

    /**
     * Creates a column of the specified {@code size} and nullability.
     *
     * @param size {@code int}, the size of the column
     * @param nullable {@code boolean}, whether the column allows {@code null}
     */
    Column(int size, boolean nullable) {
        if (size <= 0) {
            throw new InvalidArgument(format("%d: bad size", size));
        }
        this.nullable = nullable;
        setSize(size);
    }

    /**
     * Creates a column of the specified nullability.
     * <p>
     * The size is not initialized here, and needs to be subsequently
     * initialized prior to this column being used.
     * </p>
     *
     * @param nullable {@code boolean}, whether the column allows {@code null}
     * @see #setSize(int)
     */
    Column(boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Creates a non-nullable column.
     * <p>
     * The size is not initialized here, and needs to be subsequently
     * initialized prior to this column being used.
     * </p>
     *
     * @see #setSize(int)
     */
    Column() {
        this.nullable = false;
    }

    /**
     * Decodes the column's {@code byte[]} buffer.
     * <p>
     * A {@code null} is returned if the column is nullable and the
     * {@code byte[]} buffer equals {@link Column#getNullValue() nullValue}.
     * </p>
     *
     * @param buffer {@code byte[]} of size {@link #getSize()}
     * @return {@code <T>}
     * @throws InvalidArgument Thrown if {@code buffer} is null or its length is
     * not equal to {@link #getSize()}
     */
    public T decode(byte[] buffer) {
        if (buffer == null) {
            throw new InvalidArgument("buffer", buffer);
        } else if (buffer.length != size) {
            final String fmt = "cannot decode %s bytes, expected %d";
            final String msg = format(fmt, buffer.length, size);
            throw new InvalidArgument(msg);
        }

        if (nullable) {
            final byte[] nullValue = getNullValue();
            if (Arrays.equals(buffer, nullValue)) {
                return null;
            }
        }

        return decodeData(buffer);
    }

    /**
     * Encodes the column's type.
     *
     * @param t {@code <T>}
     * @return {@code byte[]}
     */
    public byte[] encode(T t) {
        if (t == null) {
            if (!nullable) {
                throw new InvalidArgument("cannot encode a null type");
            }
            return getNullValue();
        }
        return encodeType(t);
    }

    /**
     * Internal {@link #decode(byte[])} implementation deferred to
     * {@link Column column} subclasses.
     *
     * @param buffer {@code byte[]}; guaranteed to be non-null and of the
     * correct size ({@link #getSize()})
     * @return {@code <T>}
     */
    protected abstract T decodeData(byte[] buffer);

    /**
     * Internal {@link #encode(Object)} implementation deferred to
     * {@link Column column} subclasses.
     *
     * @param t {@code <T>}; guaranteed to be non-null
     * @return {@code byte[]}
     */
    protected abstract byte[] encodeType(T t);

    /**
     * Retrieve the null value {@code byte[]} for this {@link Column column}.
     * <p>
     * Retrieval of {@code null} value deferred to subclass to allow specific
     * values to be used based on the {@code <T>} type.
     * </p>
     *
     * @return {@code byte[]}
     */
    protected abstract byte[] getNullValue();

    /**
     * Sets the column's size and recalculates its hash.
     *
     * @param size Number of bytes defining this {@link Column column}
     */
    void setSize(int size) {
        if (this.size != 0) {
            final String fmt = "column size is known: %d";
            final String msg = format(fmt, this.size);
            throw new IllegalStateException(msg);
        }
        this.size = size;
        this.hash = hash();
    }

    /**
     * Returns the size, in bytes, of this column.
     *
     * @return {@code int}
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the hash of the {@link Column}.
     * <p>
     * This hash should only be used when the size of the column is non-zero
     * (e.g., the column size is <i>known</i>).
     * </p>
     *
     * @return {@code int}
     */
    protected int getHash() {
        return hash;
    }

    private int hash() {
        final int prime = 31;
        int result = 1;
        result = prime * result + size;
        if (nullable)
            result *= prime + 1231;
        else
            result *= prime + 1237;
        return result;
    }
}
