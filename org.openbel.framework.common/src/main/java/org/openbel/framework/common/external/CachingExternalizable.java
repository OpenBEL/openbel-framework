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

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Describes a reference type that can be stored and retrieved from object I/O.
 * <p>
 * Hash-based caching is paramount in storing and retrieving external types. The
 * use of these caches allow us to minimize the amount of reads and writes
 * during externalizing operations.
 * </p>
 * <p>
 * This interface needs to be redesigned. Original use of {@link ReadCache} and
 * {@link WriteCache} objects used by the interface is no longer really
 * suitable.
 * </p>
 * 
 * @since 1.3
 */
public interface CachingExternalizable extends Externalizable {

    /**
     * Reads itself from the specified file.
     * 
     * @param f {@link File}; may not be null
     * @throws IOException XXX WUTM
     * @throws ClassNotFoundException XXX WUTM
     * @throws NullPointerException Thrown if {code f} is null
     */
    public void from(File f) throws IOException, ClassNotFoundException;

    /**
     * Reads itself from the specified file using an optional cache.
     * 
     * @param f {@link File}; may not be null
     * @param cache {@link ReadCache}; null indicates no caching is desired
     * (equivalent to {@link #from(File)})
     * @throws IOException XXX WUTM
     * @throws ClassNotFoundException XXX WUTM
     * @throws NullPointerException Thrown if {code f} is null
     */
    public void from(File f, ReadCache cache) throws IOException,
            ClassNotFoundException;

    /**
     * Reads itself from the specified input.
     * 
     * @param in {@link ObjectInput}; may not be null
     * @throws IOException XXX WUTM
     * @throws ClassNotFoundException XXX WUTM
     * @throws NullPointerException Thrown if {code in} is null
     */
    public void from(ObjectInput in) throws IOException, ClassNotFoundException;

    /**
     * Reads itself from the specified input using an optional cache.
     * 
     * @param in {@link ObjectInput}; may not be null
     * @param cache {@link ReadCache}; null indicates no caching is desired
     * (equivalent to {@link #from(ObjectInput)})
     * @throws IOException XXX WUTM
     * @throws ClassNotFoundException XXX WUTM
     * @throws NullPointerException Thrown if {code in} is null
     */
    public void from(ObjectInput in, ReadCache cache) throws IOException,
            ClassNotFoundException;

    /**
     * Reads itself from the specified path.
     * 
     * @param path {@link String}; may not be null
     * @throws IOException XXX WUTM
     * @throws ClassNotFoundException XXX WUTM
     * @throws NullPointerException Thrown if {code path} is null
     */
    public void from(String path) throws IOException, ClassNotFoundException;

    /**
     * Reads itself from the specified path using an optional cache.
     * 
     * @param path {@link String}; may not be null
     * @param cache {@link ReadCache}; null indicates no caching is desired
     * (equivalent to {@link #from(String)})
     * @throws IOException XXX WUTM
     * @throws ClassNotFoundException XXX WUTM
     * @throws NullPointerException Thrown if {code path} is null
     */
    public void from(String path, ReadCache cache) throws IOException,
            ClassNotFoundException;

    /**
     * Writes this to the specified file.
     * 
     * @param f {@link File}; may not be null
     * @throws IOException XXX WUTM
     * @throws NullPointerException Thrown if either argument is null
     */
    public void to(File f) throws IOException;

    /**
     * Writes this to the specified file and using an optional cache.
     * 
     * @param f {@link File}; may not be null
     * @param cache {@link WriteCache}; null indicates no caching is desired
     * (equivalent to {@link #to(Object, File)})
     * @throws IOException XXX WUTM
     * @throws NullPointerException Thrown if {@code f} is null
     */
    public void to(File f, WriteCache cache) throws IOException;

    /**
     * Writes this to the specified output.
     * 
     * @param out {@link ObjectOutput}; may not be null
     * @throws IOException XXX WUTM
     * @throws NullPointerException Thrown if {@code out} is null
     */
    public void to(ObjectOutput out) throws IOException;

    /**
     * Writes this to the specified output using an optional cache.
     * 
     * @param out {@link ObjectOutput}; may not be null
     * @param cache {@link WriteCache}; null indicates no caching is desired
     * (equivalent to {@link #to(Object, ObjectOutput)})
     * @throws IOException XXX WUTM
     * @throws NullPointerException Thrown if {@code out} is null
     */
    public void to(ObjectOutput out, WriteCache cache) throws IOException;

    /**
     * Writes this to the specified path.
     * 
     * @param path {@link String}; may not be null
     * @throws IOException XXX WUTM
     * @throws NullPointerException Thrown if {@code path} is null
     */
    public void to(String path) throws IOException;

    /**
     * Writes this to the specified path using an optional cache.
     * 
     * @param path {@link String}; may not be null
     * @param cache {@link WriteCache}; null indicates no caching is desired
     * (equivalent to {@link #to(Object, String)})
     * @throws IOException XXX WUTM
     * @throws NullPointerException Thrown if {code t} or {@code path} is null
     */
    public void to(String path, WriteCache cache) throws IOException;

}
