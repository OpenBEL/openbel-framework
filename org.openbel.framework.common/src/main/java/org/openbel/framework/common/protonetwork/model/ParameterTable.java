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
import static org.openbel.framework.common.BELUtilities.sizedHashMap;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.StatementTable.TableStatement;

/**
 * ParameterTable holds the parameter values for terms. This class manages the
 * insertion index and occurrence count state through the
 * {@link #addTableParameter(TableParameter)} operation.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class ParameterTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767006L;

    private Set<TableParameter> parameters =
            new LinkedHashSet<TableParameter>();

    private Map<TableParameter, Integer> parameterIndex =
            new HashMap<TableParameter, Integer>();

    /**
     * Defines a map of parameter index to global index. Note: This map is
     * backed by {@link HashMap} which is not thread-safe.
     */
    private Map<Integer, Integer> globalIndex = new HashMap<Integer, Integer>();

    private Map<Integer, TableParameter> indexParameter =
            new HashMap<Integer, TableParameter>();

    private Map<TableParameter, Integer> count =
            new HashMap<TableParameter, Integer>();

    /**
     * Defines a map of global index to {@link SkinnyUUID}
     */
    private Map<Integer, SkinnyUUID> globalUUIDs =
            new HashMap<Integer, SkinnyUUID>();

    public int addTableParameter(TableParameter parameter) {
        if (parameter == null) {
            throw new InvalidArgument("parameter is null");
        }

        if (parameters.add(parameter)) {
            int nextIndex = parameterIndex.size();

            parameterIndex.put(parameter, nextIndex);
            indexParameter.put(nextIndex, parameter);
            globalIndex.put(nextIndex, globalIndex.size());

            count.put(parameter, 1);
        } else {
            count.put(parameter, count.get(parameter) + 1);
        }

        return parameterIndex.get(parameter);
    }

    /**
     * Removes a parameter by index, from the table's internal set and backing
     * maps and adjusts indices.
     * <p>
     * If no parameter exists for the specified index, this method is a no-op.
     * </p>
     *
     * @param pid Table parameter index
     */
    public void removeTableParameter(final int pid) {
        final TableParameter p = indexParameter.get(pid);
        if (p == null) return;

        // Pull the reference count
        final Integer pcount = count.get(p);

        // Alter maps and set only if 1 ref
        if (pcount == 1 && parameters.remove(p)) {
            Integer value = parameterIndex.remove(p);
            indexParameter.remove(value);
            count.remove(p);
        } else {
            count.put(p, pcount - 1);
        }
    }

    /**
     * Returns the table parameter by index.
     *
     * @param pid Table parameter index
     * @return TableParameter
     */
    public TableParameter getTableParameter(final int pid) {
        return indexParameter.get(pid);
    }

    /**
     * Returns an unmodifiable set view of the table parameters.
     *
     * @return Read-only {@link Set set}
     */
    public Set<TableParameter> getTableParameters() {
        return Collections.unmodifiableSet(parameters);
    }

    /**
     * Returns {@link #getTableParameters()} as an array.
     *
     * @return {@code TableParameter[]}
     */
    public TableParameter[] getTableParameterArray() {
        return parameters.toArray(new TableParameter[0]);
    }

    /**
     * @return Map with K: TableParameter; V: index
     */
    public Map<TableParameter, Integer> getTableParameterIndex() {
        return Collections.unmodifiableMap(parameterIndex);
    }

    /**
     * @return Map with K: index; V: TableParameter
     */
    public Map<Integer, TableParameter> getIndexTableParameter() {
        return Collections.unmodifiableMap(indexParameter);
    }

    public Map<TableParameter, Integer> getCount() {
        return Collections.unmodifiableMap(count);
    }

    /**
     * @return Map with K: parameter index; V: global index
     */
    public Map<Integer, Integer> getGlobalIndex() {
        return globalIndex;
    }

    /**
     * @return Map with K: parameter index; V: {@link SkinnyUUID} UUID
     */
    public Map<Integer, SkinnyUUID> getGlobalUUIDs() {
        return globalUUIDs;
    }

    /**
     * @return all UUIDs present in the parameter table
     */
    public Set<SkinnyUUID> getUUIDs() {
        return Collections.unmodifiableSet(
                new LinkedHashSet<SkinnyUUID>(globalUUIDs.values()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        // 1: read parameter size
        final int size = in.readInt();
        // size collections accordingly
        parameters = new LinkedHashSet<TableParameter>(size);
        indexParameter = sizedHashMap(size);
        parameterIndex = sizedHashMap(size);
        count = sizedHashMap(size);
        for (int i = 0; i < size; i++) {
            TableParameter tp = new TableParameter();
            // 1: read each table parameter
            tp.readExternal(in);
            parameters.add(tp);
            // 2: read index
            Integer idx = readInteger(in);
            indexParameter.put(idx, tp);
            parameterIndex.put(tp, idx);
            // 3: read table parameter count
            Integer pct = readInteger(in);
            count.put(tp, pct);
        }

        // 2: read number of global indices
        final int size2 = in.readInt();
        // size map accordingly
        globalIndex = sizedHashMap(size2);
        for (int i = 0; i < size2; i++) {
            // read each key/value
            Integer key = readInteger(in);
            Integer value = readInteger(in);
            globalIndex.put(key, value);
        }

        // 3: read number of uuids
        final int size3 = in.readInt();
        globalUUIDs = sizedHashMap(size3);
        for (int i = 0; i < size3; i++) {
            Integer key = readInteger(in);
            Long msb = readLong(in);
            Long lsb = readLong(in);
            globalUUIDs.put(key, new SkinnyUUID(msb, lsb));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        // 1: write parameters and indices
        out.writeInt(parameters.size());
        for (TableParameter tp : parameters) {
            // 1: write each table parameter
            tp.writeExternal(out);
            // 2: write index
            writeInteger(out, parameterIndex.get(tp));
            // 3: write table parameter count
            writeInteger(out, count.get(tp));
        }

        // 2: write number of global indices
        out.writeInt(globalIndex.size());
        for (final Entry<Integer, Integer> e : entries(globalIndex)) {
            // write each key/value
            writeInteger(out, e.getKey());
            writeInteger(out, e.getValue());
        }

        // 3: write number of uuids
        out.writeInt(globalUUIDs.size());
        for (final Entry<Integer, SkinnyUUID> e : entries(globalUUIDs)) {
            writeInteger(out, e.getKey());
            writeLong(out, e.getValue().getMostSignificantBits());
            writeLong(out, e.getValue().getLeastSignificantBits());
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

    /**
     * @version 1.3 Derives from {@link ExternalType}
     */
    public static class TableParameter extends ExternalType {
        private static final long serialVersionUID = -6486699009265767005L;

        private/* final */TableNamespace namespace;

        private/* final */String value;

        private/* final */int hash;

        public TableParameter(String value) {
            if (value == null) {
                throw new InvalidArgument("value is null.");
            }

            this.namespace = null;
            this.value = value;
            this.hash = computeHash();
        }

        public TableParameter(TableNamespace namespace, String value) {
            if (namespace == null) {
                throw new InvalidArgument("namespace is null.");
            }

            if (value == null) {
                throw new InvalidArgument("value is null.");
            }

            this.namespace = namespace;
            this.value = value;
            this.hash = computeHash();
        }

        /*
         * This public, no-argument constructor is required when implementing Externalizable
         * but it is not meant to be used for anything else.
         */
        public TableParameter() {
        }

        public TableNamespace getNamespace() {
            return namespace;
        }

        public String getValue() {
            return value;
        }

        /**
         * Compute the hashCode of {@link TableStatement this table statement}.
         *
         * @return the hashCode
         */
        private int computeHash() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((namespace == null) ? 0 : namespace.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
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
            TableParameter other = (TableParameter) obj;
            if (namespace == null) {
                if (other.namespace != null)
                    return false;
            } else if (!namespace.equals(other.namespace))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in) throws IOException,
                ClassNotFoundException {
            readNamespace(in);
            value = readString(in);
            hash = computeHash();
        }

        void readNamespace(ObjectInput in) throws IOException,
                ClassNotFoundException {
            byte nsnull = in.readByte();
            if (nsnull == 1) {
                namespace = new TableNamespace();
                namespace.readExternal(in);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _to(ObjectOutput out) throws IOException {
            writeNamespace(out);
            out.writeObject(value);
        }

        void writeNamespace(ObjectOutput out) throws IOException {
            if (namespace == null) {
                out.writeByte(0);
            } else {
                out.writeByte(1);
                namespace.writeExternal(out);
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((count == null) ? 0 : count.hashCode());
        result =
                prime * result
                        + ((globalIndex == null) ? 0 : globalIndex.hashCode());
        result =
                prime
                        * result
                        + ((indexParameter == null) ? 0 : indexParameter
                                .hashCode());
        result =
                prime
                        * result
                        + ((parameterIndex == null) ? 0 : parameterIndex
                                .hashCode());
        result =
                prime * result
                        + ((parameters == null) ? 0 : parameters.hashCode());
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
        ParameterTable other = (ParameterTable) obj;
        if (count == null) {
            if (other.count != null)
                return false;
        } else if (!count.equals(other.count))
            return false;
        if (globalIndex == null) {
            if (other.globalIndex != null)
                return false;
        } else if (!globalIndex.equals(other.globalIndex))
            return false;
        if (indexParameter == null) {
            if (other.indexParameter != null)
                return false;
        } else if (!indexParameter.equals(other.indexParameter))
            return false;
        if (parameterIndex == null) {
            if (other.parameterIndex != null)
                return false;
        } else if (!parameterIndex.equals(other.parameterIndex))
            return false;
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        } else if (!parameters.equals(other.parameters))
            return false;
        return true;
    }
}
