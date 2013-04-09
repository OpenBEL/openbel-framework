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
package org.openbel.framework.common.protonetwork.model;

import java.io.Serializable;
import java.util.UUID;

import org.openbel.framework.common.InvalidArgument;

/**
 * A 'skinny' form of a {@link UUID} that stores only the most and least
 * significant bits allowing the memory requirement of each UUID to be reduced.
 * <p>
 * For a 64 bit VM, a UUID will use 64 bytes of memory (4 longs @ 8 bytes + 4
 * ints @ 4 bytes + 16 byte object header) versus a SkinnyUUID using 36 bytes of
 * memory (2 longs @ 8 bytes + 1 int @ 4 bytes + 16 byte object header). <br>
 * Storing a 40char {@link String} for the UUID would require 120 bytes. (see
 * http://www.javamex.com/tutorials/memory/string_memory_usage.shtml)
 * </p>
 *
 * @author Steve Ungerer
 */
public class SkinnyUUID implements Serializable, Comparable<SkinnyUUID> {
    private static final long serialVersionUID = 570740213581352952L;

    private final long mostSigBits;
    private final long leastSigBits;

    private transient int hashCode = -1;

    /**
     * Construct a SkinnyUUID with the most and least significant bits.
     *
     * @param mostSigBits
     * @param leastSigBits
     */
    public SkinnyUUID(long mostSigBits, long leastSigBits) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }

    /**
     * Construct a SkinnyUUID from a source {@link UUID}
     *
     * @param uuid
     *            The {@link UUID}, must not be <code>null</code>
     * @throws InvalidArgument
     *             Thrown if <code>uuid</code> is <code>null</code>
     */
    public SkinnyUUID(UUID uuid) {
        if (uuid == null) {
            throw new InvalidArgument("UUID must not be null");
        }
        this.mostSigBits = uuid.getMostSignificantBits();
        this.leastSigBits = uuid.getLeastSignificantBits();
    }

    /**
     * Creates a <tt>SkinnyUUID</tt> from the string standard representation as
     * described in the {@link #toString} method.
     *
     * @param name
     *            a string that specifies a <tt>UUID</tt>.
     * @return a <tt>UUID</tt> with the specified value.
     * @throws IllegalArgumentException
     *             if name does not conform to the string representation as
     *             described in {@link #toString}.
     */
    public static SkinnyUUID fromString(String name) {
        String[] components = name.split("-");
        if (components.length != 5)
            throw new IllegalArgumentException("Invalid UUID string: " + name);
        for (int i = 0; i < 5; i++)
            components[i] = "0x" + components[i];

        long mostSigBits = Long.decode(components[0]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[1]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[2]).longValue();

        long leastSigBits = Long.decode(components[3]).longValue();
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(components[4]).longValue();

        return new SkinnyUUID(mostSigBits, leastSigBits);
    }

    /**
     * Returns the least significant 64 bits of this UUID's 128 bit value.
     *
     * @return the least significant 64 bits of this UUID's 128 bit value.
     * @see UUID#getLeastSignificantBits()
     */
    public long getLeastSignificantBits() {
        return leastSigBits;
    }

    /**
     * Returns the most significant 64 bits of this UUID's 128 bit value.
     *
     * @return the most significant 64 bits of this UUID's 128 bit value.
     * @see UUID#getMostSignificantBits()
     */
    public long getMostSignificantBits() {
        return mostSigBits;
    }

    /**
     * Returns a <code>String</code> object representing this
     * <code>SkinnyUUID</code>.
     *
     * <p>
     * The UUID string representation is as described by this BNF : <blockquote>
     *
     * <pre>
     * {@code
     * UUID                   = <time_low> "-" <time_mid> "-"
     *                          <time_high_and_version> "-"
     *                          <variant_and_sequence> "-"
     *                          <node>
     * time_low               = 4*<hexOctet>
     * time_mid               = 2*<hexOctet>
     * time_high_and_version  = 2*<hexOctet>
     * variant_and_sequence   = 2*<hexOctet>
     * node                   = 6*<hexOctet>
     * hexOctet               = <hexDigit><hexDigit>
     * hexDigit               =
     *       "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
     *       | "a" | "b" | "c" | "d" | "e" | "f"
     *       | "A" | "B" | "C" | "D" | "E" | "F"
     * }
     * </pre>
     *
     * </blockquote>
     *
     * @return a string representation of this <tt>SkinnyUUID</tt>.
     * @see UUID#toString()
     */
    @Override
    public String toString() {
        return (digits(mostSigBits >> 32, 8) + "-"
                + digits(mostSigBits >> 16, 4) + "-" + digits(mostSigBits, 4)
                + "-" + digits(leastSigBits >> 48, 4) + "-" + digits(
                    leastSigBits, 12));
    }

    /**
     * Returns val represented by the specified number of hex digits.
     *
     * @see UUID#digits()
     */
    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    /**
     * Returns a hash code for this <code>SkinnyUUID</code>.
     *
     * @return a hash code value for this <tt>SkinnyUUID</tt>.
     */
    @Override
    public int hashCode() {
        if (hashCode == -1) {
            hashCode = (int) ((mostSigBits >> 32) ^ mostSigBits
                    ^ (leastSigBits >> 32) ^ leastSigBits);
        }
        return hashCode;
    }

    /**
     * Compares this object to the specified object. The result is <tt>true</tt>
     * if and only if the argument is not <tt>null</tt>, is a
     * <tt>SkinnyUUID</tt> object, has the same variant, and contains the same
     * value, bit for bit, as this <tt>SkinnyUUID</tt>.
     *
     * @param obj
     *            the object to compare with.
     * @return <code>true</code> if the objects are the same; <code>false</code>
     *         otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SkinnyUUID)) {
            return false;
        }

        SkinnyUUID id = (SkinnyUUID) obj;
        return (mostSigBits == id.mostSigBits && leastSigBits == id.leastSigBits);
    }

    /**
     * Compares this SkinnyUUID with the specified SkinnyUUID.
     *
     * <p>
     * The first of two SkinnyUUIDs follows the second if the most significant
     * field in which the SkinnyUUIDs differ is greater for the first
     * SkinnyUUID.
     *
     * @param val
     *            <tt>SkinnyUUID</tt> to which this <tt>SkinnyUUID</tt> is to be
     *            compared.
     * @return -1, 0 or 1 as this <tt>SkinnyUUID</tt> is less than, equal to, or
     *         greater than <tt>val</tt>.
     * @see UUID#compareTo(UUID)
     */
    @Override
    public int compareTo(SkinnyUUID val) {
        // The ordering is intentionally set up so that the UUIDs
        // can simply be numerically compared as two numbers
        return (this.mostSigBits < val.mostSigBits ? -1
                : (this.mostSigBits > val.mostSigBits ? 1
                        : (this.leastSigBits < val.leastSigBits ? -1
                                : (this.leastSigBits > val.leastSigBits ? 1 : 0))));
    }

    /**
     * Reconstitute the <tt>SkinnyUUID</tt> instance from a stream (that is,
     * deserialize it). This is necessary to set the transient fields to their
     * correct uninitialized value so they will be recomputed on demand.
     *
     * @see UUID#readObject()
     */
    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();

        // Set "cached computation" fields to their initial values
        hashCode = -1;
    }

}
