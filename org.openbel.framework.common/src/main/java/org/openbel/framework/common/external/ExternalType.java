/**
 * Copyright (C) 2012 Selventa, Inc.
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
package org.openbel.framework.common.external;

import static java.lang.System.err;
import static org.openbel.framework.common.BELUtilities.nulls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

/**
 * This class provides a skeletal implementation of the
 * {@link CachingExternalizable} interface to minize the effort needed to
 * externalize a type.
 * 
 * @since 1.3
 */
public abstract class ExternalType implements CachingExternalizable {

    /**
     * Reads a {@link String} from the input.
     * 
     * @param in Non-null {@link ObjectInput}
     * @return {@link String}
     * @throws IOException Thrown if an I/O error occurred
     * @throws ClassNotFoundException Thrown if the class of the object being
     * read cannot be found
     */
    public static String readString(final ObjectInput in) throws IOException,
            ClassNotFoundException {
        try {
            Object o = in.readObject();
            return (String) o;
        } catch (OptionalDataException e) {
            StringBuilder bldr = new StringBuilder();
            if (e.length == 0) {
                bldr.append("(attempted to read past end of object data)");
            } else {
                bldr.append("(attempted to read object data when ");
                bldr.append(e.length);
                bldr.append(" byte(s) of primitive data is present)");
            }
            err.println(bldr);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Writes the {@link Integer} to the output.
     * <p>
     * This method and its equivalent {@link #readInteger(ObjectInput)
     * read-variant} store {@code i} in a more efficient way than serializing
     * the {@link Integer} class.
     * </p>
     * 
     * @param out Non-null {@link ObjectOutput}
     * @param i {@link Integer}; may be null
     * @throws IOException Thrown if an I/O error occurred
     * @see #readInteger(ObjectInput)
     */
    public static void writeInteger(final ObjectOutput out, final Integer i)
            throws IOException {
        if (i == null) {
            out.writeByte(0);
            return;
        }
        out.writeByte(1);
        out.writeInt(i);
    }

    /**
     * Writes the potentially null object to output. If {@code o} is null, one
     * byte is written to the output to indicate that. Otherwise, the written
     * size is the serialized object size plus an additional byte to indicate
     * non-null.
     * <p>
     * This method doesn't differ in functionality with
     * {@link ObjectOutput#writeObject(Object)}, including its support for
     * nulls. It exists as an analogue to the other methods here and as a point
     * for documenting how nulls are handled.
     * </p>
     * 
     * @param out Non-null {@link ObjectOutput}
     * @param o Object being written
     * @throws IOException Thrown if an I/O error occurs
     */
    public static void writeObject(final ObjectOutput out, final Object o)
            throws IOException {
        if (o == null) {
            out.writeByte(0);
            return;
        }
        out.writeByte(1);
        out.writeObject(o);
    }

    /**
     * Reads the potentially null object from input.
     * <p>
     * This method doesn't differ in functionality with
     * {@link ObjectInput#readObject()}, including its support for nulls. It
     * exists as an analogue to the other methods here and as a point for
     * documenting how nulls are handled.
     * </p>
     * 
     * @param in Non-null {@link ObjectInput}
     * @return {@link Object}
     * @throws IOException Thrown if an I/O error occurs
     * @throws ClassNotFoundException Thrown if the class of the object being
     * read cannot be found
     */
    public static Object readObject(final ObjectInput in)
            throws IOException, ClassNotFoundException {
        byte obyte = in.readByte();
        if (obyte == 0) {
            return null;
        }
        return in.readObject();
    }

    /**
     * Writes the {@link Long} to the output.
     * <p>
     * This method and its equivalent {@link #readLong(ObjectInput)
     * read-variant} store {@code l} in a more efficient way than serializing
     * the {@link Long} class.
     * </p>
     * 
     * @param out Non-null {@link ObjectOutput}
     * @param l {@link Long}; may be null
     * @throws IOException Thrown if an I/O error occurred
     * @see #readLong(ObjectInput)
     */
    public static void writeLong(final ObjectOutput out, final Long l)
            throws IOException {
        if (l == null) {
            out.writeByte(0);
            return;
        }
        out.writeByte(1);
        out.writeLong(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void writeExternal(ObjectOutput out) throws IOException {
        _to(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        _from(in);
    }

    /**
     * Reads an {@link Integer} from the input.
     * <p>
     * This method and its equivalent
     * {@link #writeInteger(ObjectOutput, Integer) write-variant} store
     * {@code i} in a more efficient way than serializing the {@link Integer}
     * class.
     * </p>
     * 
     * @param in Non-null {@link ObjectInput}
     * @return {@link Integer}
     * @throws IOException Thrown if an I/O error occurred
     */
    public static Integer readInteger(final ObjectInput in) throws IOException {
        byte b = in.readByte();
        if (b == 0) return null;
        return in.readInt();
    }

    /**
     * Reads a {@link Long} from the input.
     * <p>
     * This method and its equivalent {@link #writeLong(ObjectOutput, Long)
     * write-variant} store {@code l} in a more efficient way than serializing
     * the {@link Long} class.
     * </p>
     * 
     * @param in Non-null {@link ObjectInput}
     * @return {@link Long}
     * @throws IOException Thrown if an I/O error occurred
     */
    public static Long readLong(final ObjectInput in) throws IOException {
        byte b = in.readByte();
        if (b == 0) return null;
        return in.readLong();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void from(File f) throws IOException, ClassNotFoundException {
        if (nulls(f)) throw new NullPointerException();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            from(ois);
        } finally {
            if (fis != null) fis.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void from(File f, ReadCache cache) throws IOException,
            ClassNotFoundException {
        if (cache == null) {
            from(f);
            return;
        }
        if (nulls(f)) throw new NullPointerException();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            from(ois, cache);
        } finally {
            if (fis != null) fis.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        if (nulls(in)) throw new NullPointerException();
        // delegate to the equivalent internal method
        _from(in);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void from(ObjectInput in, ReadCache cache) throws IOException,
            ClassNotFoundException {
        if (cache == null) {
            from(in);
            return;
        }
        if (nulls(in)) throw new NullPointerException();
        // delegate to the equivalent internal method
        _from(in, cache);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void from(String path) throws IOException,
            ClassNotFoundException {
        if (nulls(path)) throw new NullPointerException();
        from(new File(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void from(String path, ReadCache cache) throws IOException,
            ClassNotFoundException {
        if (cache == null) {
            from(path);
            return;
        }
        if (nulls(path)) throw new NullPointerException();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path));
            ObjectInputStream ois = new ObjectInputStream(fis);
            from(ois, cache);
            return;
        } finally {
            if (fis != null) fis.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void to(File f) throws IOException {
        if (f == null) throw new NullPointerException();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            to(oos);
        } finally {
            if (fos != null) fos.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void to(File f, WriteCache cache) throws IOException {
        if (cache == null) {
            to(f);
            return;
        }
        if (f == null) throw new NullPointerException();
        // delegate to the equivalent internal method
        to(f, cache);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void to(ObjectOutput out) throws IOException {
        if (out == null) throw new NullPointerException();
        // delegate to the equivalent internal method
        _to(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void to(ObjectOutput out, WriteCache cache)
            throws IOException {
        if (cache == null) {
            to(out);
            return;
        }
        if (out == null) throw new NullPointerException();
        // delegate to the equivalent internal method
        _to(out, cache);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void to(String path) throws IOException {
        if (path == null) throw new NullPointerException();
        to(new File(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void to(String path, WriteCache cache) throws IOException {
        if (cache == null) {
            to(path);
            return;
        }
        if (path == null) throw new NullPointerException();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(path));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            to(oos, cache);
        } finally {
            if (fos != null) fos.close();
        }
    }

    /**
     * Reads itself from the input.
     * 
     * @param in Non-null {@link ObjectInput}
     * @throws IOException XXX WUTM
     * @throws ClassNotFoundException XXX WUTM
     */
    protected abstract void _from(ObjectInput in) throws IOException,
            ClassNotFoundException;

    /**
     * Reads itself from the input. The supplied cache is an ancillary structure
     * allowing the implementing type to reference a cached copy of an object by
     * its hash value.
     * <p>
     * The use of the cache is not required. However the use or disuse of the
     * cache should be symmetric according to both
     * {@link #_from(ObjectInput, ReadCache)} and
     * {@link #_to(ObjectOutput, WriteCache)}. Put differently, the cache use
     * should be identical in both methods.
     * </p>
     * 
     * @param in Non-null {@link ObjectInput}
     * @param cache Non-null Object {@link ReadCache}
     * @throws IOException XXX WUTM
     * @throws ClassNotFoundException XXX WUTM
     */
    protected abstract void _from(ObjectInput in, ReadCache cache)
            throws IOException, ClassNotFoundException;

    /**
     * Writes this to the output.
     * 
     * @param out {@link ObjectOutput}
     * @throws IOException XXX WUTM
     */
    protected abstract void _to(ObjectOutput out)
            throws IOException;

    /**
     * Writes this to the output. The supplied cache is an ancillary structure
     * allowing the implementing type to reference a cached copy of an object by
     * its hash value.
     * <p>
     * The use of the cache is not required. However the use or disuse of the
     * cache should be symmetric according to both
     * {@link #to(Object, ObjectOutput, WriteCache)} and
     * {@link #from(ObjectInput, ReadCache)}. Put differently, the cache use
     * should be identical in both methods.
     * </p>
     * 
     * @param out Non-null {@link ObjectOutput}
     * @param cache Non-null Object {@link WriteCache}
     * @throws IOException XXX WUTM
     */
    protected abstract void _to(ObjectOutput out,
            WriteCache cache) throws IOException;
}
