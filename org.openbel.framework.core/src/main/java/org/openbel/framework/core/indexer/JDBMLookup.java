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
package org.openbel.framework.core.indexer;

import static jdbm.RecordManagerFactory.createRecordManager;
import static org.openbel.framework.common.BELUtilities.noLength;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;

import jdbm.InverseHashView;
import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.Serializer;

/**
 * This class is a low-level means of performing direct lookups against JDBM
 * indices.
 * <p>
 * Bad use of this class can cause file descriptor leaks (see {@link #open()},
 * {@link #close()}, and {@link #finalize()}. A better option may be to use
 * service-level constructs.
 * </p>
 */
abstract class JDBMLookup<K extends Comparable<K>, V> {
    private final String indexPath;
    private final Serializer<V> valueSerializer;
    PrimaryTreeMap<K, V> tmap;
    InverseHashView<K, V> invTmap;
    private final int hash;

    /**
     * Constructs a JDBM lookup for the associated index path.
     *
     * @param indexPath Index path
     * @throws InvalidArgument Thrown if {@code indexPath} is null or empty
     */
    JDBMLookup(String indexPath) {
        this(indexPath, null);
    }

    /**
     * Constructs a JDBM lookup for the associated index path with the provided
     * value {@link Serializer}.
     * @param indexPath Index path
     * @param valueSerializer {@link Serializer} for values. May be <code>null</code>.
     * @throws InvalidArgument Thrown if {@code indexPath} is null or empty
     */
    JDBMLookup(String indexPath, Serializer<V> valueSerializer) {
        if (noLength(indexPath)) {
            throw new InvalidArgument("indexPath", indexPath);
        }
        this.indexPath = indexPath;
        this.valueSerializer = valueSerializer;
        hash = indexPath.hashCode();
    }

    /**
     * Returns the index path associated with this JDBM lookup.
     *
     * @return String
     */
    public String getIndexPath() {
        return indexPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof JDBMLookup)) return false;
        @SuppressWarnings("unchecked")
        JDBMLookup<K, V> other = (JDBMLookup<K, V>) obj;
        // indexPath is non-null by contract
        if (!indexPath.equals(other.indexPath)) return false;
        return true;
    }

    /**
     * Closes the record manager, if it has been left open.
     * <p>
     * <blockquote> ... it is a grave error to depend on a finalizer to close
     * files, because open file descriptors are a limited resource. If many
     * files are left open because the JVM is tardy in executing finalizers, a
     * program may fail because it can no longer open files. </blockquote>
     * </p>
     *
     * @see {@link #close()}
     */
    @Override
    protected void finalize() throws Throwable {
        if (tmap == null) {
            return;
        }
        RecordManager rm = tmap.getRecordManager();
        if (rm == null) {
            return;
        }

        // We don't want any exceptions propagating back up the stack
        // and causing problems finalizing this object.
        try {
            rm.close();
        } catch (IOException ioex) {
            // Assume it has been closed.
        }
    }

    /**
     * Opens the index, for subsequent lookups.
     *
     * @throws IOException Thrown if an I/O related exception occurs while
     * creating or opening the record manager
     */
    public synchronized void open() throws IOException {
        if (tmap != null) {
            return;
        }
        final RecordManager rm = createRecordManager(indexPath);
        if (valueSerializer == null) {
            tmap = rm.treeMap(IndexerConstants.INDEX_TREE_KEY);
        } else {
            tmap = rm.treeMap(IndexerConstants.INDEX_TREE_KEY, valueSerializer);
        }
        if (tmap.isEmpty()) {
            rm.close();
            throw new IOException("tree map is empty");
        }
        invTmap = tmap.inverseHashView(IndexerConstants.INVERSE_KEY);
    }

    /**
     * Returns the number of records.
     *
     * @return int
     */
    public int getRecordCount() {
        return tmap.size();
    }

    /**
     * Closes the index.
     *
     * @throws IOException Thrown when one of the underlying I/O operations fail
     * @throws IllegalStateException Thrown if {@link #open()} has not been
     * invoked
     */
    public synchronized void close() throws IOException {
        if (tmap == null) {
            throw new IllegalStateException("not open");
        }
        final RecordManager rm = tmap.getRecordManager();
        rm.close();
        tmap = null;
    }

    /**
     * Performs a lookup for the given {@code key}.
     *
     * @param key Key
     * @return String; may be null for the given key
     */
    public synchronized V lookup(final String key) {
        if (tmap == null) {
            throw new IllegalStateException("not open");
        }
        return tmap.get(key);
    }

    /**
     * Performs a reverse lookup for the given {@code value}.
     * If multiple keys resolve to the same value, this method
     * will return the first key match as per the JDBM impl.
     *
     * @param value
     * @return
     * @see InverseHashView#findKeyForValue(Object)
     */
    public synchronized K reverseLookup(final V value) {
        if (tmap == null) {
            throw new IllegalStateException("not open");
        }
        return invTmap.findKeyForValue(value);
    }

    /**
     * @return an unmodifiable {@link Set} containing {@link String}s
     *         representing namespace values.
     */
    public synchronized Set<K> getKeySet() {
        if (tmap == null) {
            throw new IllegalStateException("not open");
        }
        return Collections.unmodifiableSet(tmap.keySet());
    }
}
