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
