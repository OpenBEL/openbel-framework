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
package org.openbel.framework.common.protonetwork.model;

import static org.openbel.framework.common.BELUtilities.entries;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;

/**
 * AnnotationValueTable holds the unique annotation value {@link String}s. This
 * class manages the annotation values through the
 * {@link #addAnnotationValue(String)} operation.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class AnnotationValueTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767013L;

    /**
     * Defines a unique collection to hold annotation values. Note: This
     * collection is backed by {@link LinkedHashSet} which is not thread-safe.
     */
    private Set<TableAnnotationValue> annotationValues =
            new LinkedHashSet<TableAnnotationValue>();

    /**
     * Defines a map from {@link String} annotation value to an index
     * representing insertion order in {@code annotationValues}. Note: This
     * collection is backed by {@link HashMap} which is not thread-safe.
     */
    private Map<TableAnnotationValue, Integer> valueIndex =
            new HashMap<TableAnnotationValue, Integer>();

    /**
     * Defines a map from the index to the {@link String}. Note: This collection
     * is backed by {@link HashMap} which is not thread-safe.
     */
    // XXX Documentation says backed by a hashmap - why a tree map?
    private Map<Integer, TableAnnotationValue> indexValue =
            new TreeMap<Integer, TableAnnotationValue>();

    /**
     * Adds an annotation value to the {@code annotationValues} set. The
     * insertion index is captured by {@code valueIndex}.
     *
     * @param annotationValue {@link String}, the annotation value to add, which
     * cannot be null
     * @return {@code int}, the index of the added annotation value, which must
     * be &gt;= 0.
     * @throws InvalidArgument Thrown if the {@code annotationValue} argument is
     * null
     */
    public int addAnnotationValue(int annotationDefinitionId,
            String annotationValue) {
        if (annotationValue == null) {
            throw new InvalidArgument("annotationValue is null.");
        }

        TableAnnotationValue tav = new TableAnnotationValue(
                annotationDefinitionId, annotationValue);

        int nextIndex = annotationValues.size();
        if (annotationValues.add(tav)) {
            valueIndex.put(tav, nextIndex);
            indexValue.put(nextIndex, tav);
            return nextIndex;
        }

        return valueIndex.get(tav);
    }

    /**
     * Returns the annotation value table's {@code annotationValues} set. This
     * set is unmodifiable to preserve the state of the annotation value table.
     *
     * @return {@link Set}, which cannot be null or modified
     */
    public Set<TableAnnotationValue> getAnnotationValues() {
        return Collections.unmodifiableSet(annotationValues);
    }

    /**
     * Returns the map of {@link String} to index. This map is unmodifiable to
     * preserve the state of the annotation value table.
     *
     * @return {@link Map}, which cannot be null or modified
     */
    public Map<TableAnnotationValue, Integer> getValueIndex() {
        return Collections.unmodifiableMap(valueIndex);
    }

    /**
     * Returns the map of index to {@link String}. This map is unmodifiable to
     * preserve the state of the annotation value table.
     *
     * @return {@link Map}, which cannot be null or modified
     */
    public Map<Integer, TableAnnotationValue> getIndexValue() {
        return indexValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime
                        * result
                        + ((annotationValues == null) ? 0 : annotationValues
                                .hashCode());
        result = prime * result
                + ((indexValue == null) ? 0 : indexValue.hashCode());
        result = prime * result
                + ((valueIndex == null) ? 0 : valueIndex.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AnnotationValueTable other = (AnnotationValueTable) obj;
        if (annotationValues == null) {
            if (other.annotationValues != null)
                return false;
        } else if (!annotationValues.equals(other.annotationValues))
            return false;
        if (indexValue == null) {
            if (other.indexValue != null)
                return false;
        } else if (!indexValue.equals(other.indexValue))
            return false;
        if (valueIndex == null) {
            if (other.valueIndex != null)
                return false;
        } else if (!valueIndex.equals(other.valueIndex))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        final int size = in.readInt();
        TableAnnotationValue tav;
        for (int i = 0; i < size; ++i) {
            tav = new TableAnnotationValue();
            tav.readExternal(in);
            addAnnotationValue(tav.getAnnotationDefinitionId(),
                    tav.getAnnotationValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        final int size = indexValue.size();
        out.writeInt(size);
        TableAnnotationValue[] tavs = new TableAnnotationValue[size];
        Set<Entry<Integer, TableAnnotationValue>> entries = entries(indexValue);
        for (final Entry<Integer, TableAnnotationValue> e : entries) {
            Integer key = e.getKey();
            TableAnnotationValue value = e.getValue();
            tavs[key] = value;
        }
        for (int i = 0; i < size; ++i) {
            tavs[i].writeExternal(out);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in, ReadCache cache)
            throws IOException,
            ClassNotFoundException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out, WriteCache cache)
            throws IOException {
        throw new UnsupportedOperationException();
    }

    public static class TableAnnotationValue extends ExternalType {
        private static final long serialVersionUID = -6486699009265767014L;
        private/* final */int annotationDefinitionId;
        private/* final */String annotationValue;
        private/* final */int hash;

        public TableAnnotationValue(final int annotationDefinitionId,
                final String annotationValue) {
            this.annotationDefinitionId = annotationDefinitionId;
            this.annotationValue = annotationValue;
            this.hash = computeHash();
        }

        /**
         * This public, no-argument constructor is required when implementing
         * Externalizable but it is not meant to be used for anything else.
         */
        public TableAnnotationValue() {
        }

        public int getAnnotationDefinitionId() {
            return annotationDefinitionId;
        }

        public String getAnnotationValue() {
            return annotationValue;
        }

        /**
         * Compute the hash for {@link TableAnnotationValue this table
         * annotation value}.
         *
         * @return the hash
         */
        private int computeHash() {
            final int prime = 31;
            int result = 1;
            result = prime * result + annotationDefinitionId;
            result = prime
                    * result
                    + ((annotationValue == null) ? 0 : annotationValue
                            .hashCode());
            return result;
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
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TableAnnotationValue other = (TableAnnotationValue) obj;
            if (annotationDefinitionId != other.annotationDefinitionId)
                return false;
            if (annotationValue == null) {
                if (other.annotationValue != null)
                    return false;
            } else if (!annotationValue.equals(other.annotationValue))
                return false;
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in) throws IOException,
                ClassNotFoundException {
            annotationDefinitionId = readInteger(in);
            annotationValue = readString(in);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _to(ObjectOutput out) throws IOException {
            writeInteger(out, annotationDefinitionId);
            out.writeObject(annotationValue);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in, ReadCache cache)
                throws IOException,
                ClassNotFoundException {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _to(ObjectOutput out, WriteCache cache)
                throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}
