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

import static org.openbel.framework.common.BELUtilities.entries;
import static org.openbel.framework.common.BELUtilities.index;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;
import org.openbel.framework.common.model.Namespace;

/**
 * NamespaceTable holds the namespaces seen from documents. This class manages
 * the {@code namespaces} through the {@link #addNamespace(TableNamespace, int)}
 * operation.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class NamespaceTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767005L;

    /**
     * Defines a unique collection to hold namespace values. Note: This
     * collection is backed by {@link LinkedHashSet} which is not thread-safe.
     */
    private Set<TableNamespace> namespaces =
            new LinkedHashSet<TableNamespace>();

    /**
     * Defines a map from {@link TableNamespace} to an index representing
     * insertion order in {@code namespaces}. Note: This collection is backed by
     * {@link HashMap} which is not thread-safe.
     */
    private Map<TableNamespace, Integer> namespaceIndex =
            new HashMap<TableNamespace, Integer>();

    /**
     * Defines a map from index to {@link TableNamespace}. Note: This collection
     * is backed by {@code HashMap} which is not threadsafe.
     */
    private Map<Integer, TableNamespace> indexNamespace =
            new HashMap<Integer, NamespaceTable.TableNamespace>();

    /**
     * Defines a map from document index to a {@link List} of namespace index.
     * Note: This collection is backed by {@code LinkedHashMap} which is not
     * threadsafe.
     */
    private Map<Integer, List<Integer>> documentNamespaces =
            new LinkedHashMap<Integer, List<Integer>>();

    /**
     * Adds a namespace to the {@code namespaces} set. The insertion index is
     * captured by {@code namespaceIndex}. The association of document to
     * namespaces is also captured using the {@code documentNamespaces} map.
     *
     * @param namespace {@link TableNamespace}, the namespace to add, which
     * cannot be null
     * @param did {@code int}, the document id that this {@code namespace} is
     * contained in
     * @return {@code int}, the index of the added namespace ({@code > 0})
     * @throws InvalidArgument Thrown if {@code namespace} is null
     */
    public int addNamespace(TableNamespace namespace, int did) {
        if (namespaces == null) {
            throw new InvalidArgument("namespace is null");
        }

        if (namespaces.add(namespace)) {
            int nextIndex = namespaces.size() - 1;
            namespaceIndex.put(namespace, nextIndex);
            indexNamespace.put(nextIndex, namespace);
        }

        Integer nsindex = namespaceIndex.get(namespace);
        List<Integer> nsi = documentNamespaces.get(did);
        if (nsi == null) {
            nsi = new ArrayList<Integer>();
            documentNamespaces.put(did, nsi);
        }
        nsi.add(nsindex);

        return nsindex;
    }

    /**
     * Returns the table namespace by index.
     *
     * @param nid Table namespace index
     * @return TableNamespace
     */
    public TableNamespace getTableNamespace(int nid) {
        return indexNamespace.get(nid);
    }

    /**
     * Returns the namespace table's {@code namespaces} set. This set is
     * unmodifiable to preserve the state of the namespace table.
     *
     * @return {@link Set}, which cannot be null or modified
     */
    public Set<TableNamespace> getNamespaces() {
        return Collections.unmodifiableSet(namespaces);
    }

    /**
     * Returns the map of {@link TableNamespace} to index. This map is
     * unmodifiable to preserve the state of the namespace table.
     *
     * @return {@link Map}, which cannot be null or modified
     */
    public Map<TableNamespace, Integer> getNamespaceIndex() {
        return Collections.unmodifiableMap(namespaceIndex);
    }

    /**
     * Returns the map of index to {@link TableNamespace}. This map is
     * unmodifiable to preserve the state of the namespace table.
     *
     * @return {@link Map}, which cannot be null or modified
     */
    public Map<Integer, TableNamespace> getIndexNamespace() {
        return Collections.unmodifiableMap(indexNamespace);
    }

    /**
     * Returns the map of document index to {@link List} of {@link Integer}
     * namespace indexes. This map is unmodifiable to preserve the state of the
     * namespace table.
     *
     * @return {@link Map}, which cannot be null or modified
     */
    public Map<Integer, List<Integer>> getDocumentNamespaces() {
        return Collections.unmodifiableMap(documentNamespaces);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((indexNamespace == null) ? 0 : indexNamespace.hashCode());
        result = prime * result
                + ((namespaceIndex == null) ? 0 : namespaceIndex.hashCode());
        result = prime * result
                + ((namespaces == null) ? 0 : namespaces.hashCode());
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
        NamespaceTable other = (NamespaceTable) obj;
        if (indexNamespace == null) {
            if (other.indexNamespace != null)
                return false;
        } else if (!indexNamespace.equals(other.indexNamespace))
            return false;
        if (namespaceIndex == null) {
            if (other.namespaceIndex != null)
                return false;
        } else if (!namespaceIndex.equals(other.namespaceIndex))
            return false;
        if (namespaces == null) {
            if (other.namespaces != null)
                return false;
        } else if (!namespaces.equals(other.namespaces))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        // 1: read number of table namespaces
        final int size = in.readInt();
        for (int i = 0; i < size; i++) {
            final TableNamespace ns = new TableNamespace();
            // 1: read table namespace
            ns.readExternal(in);
            // 2: read number of documents
            final int documentsSize = in.readInt();
            for (int j = 0; j < documentsSize; j++) {
                // 1: read document index
                addNamespace(ns, in.readInt());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        TableNamespace[] tblnss = index(TableNamespace.class, indexNamespace);
        // 1: write number of table namespaces
        out.writeInt(tblnss.length);
        for (int i = 0; i < tblnss.length; i++) {
            final TableNamespace ns = tblnss[i];
            // 1: write table namespace
            ns.writeExternal(out);
            Set<Integer> documents = new HashSet<Integer>();
            for (Map.Entry<Integer, List<Integer>> entry : entries(documentNamespaces)) {
                if (entry.getValue().contains(i)) {
                    documents.add(entry.getKey());
                }
            }
            // 2: write number of documents
            out.writeInt(documents.size());
            for (Integer document : documents) {
                // 1: write document index
                out.writeInt(document.intValue());
            }
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
     * Table namespace encapsulates a document namespace. The table namespace
     * holds the {@code prefix} and {@code resourceLocation} URL, but the prefix
     * can be null if the namespace was the default on the document.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     * @version 1.3 Derives from {@link ExternalType}
     */
    public static class TableNamespace extends ExternalType {
        private static final long serialVersionUID = -6486699009265767004L;

        /**
         * Defines the prefix used to represent the table namespace. The prefix
         * can be null for a document's default namespace.
         */
        private/* final */String prefix;

        /**
         * Defines the resource location URL where the namespace file is
         * located.
         */
        private/* final */String resourceLocation;

        /**
         * Defines the hash for {@link TableNamespace this table namespace}.
         */
        private/* final */int hash;

        /**
         * Creates a table namespace from the {@code resourceLocation} URL and
         * sets a null {@code prefix}.
         *
         * @param resourceLocation {@link String}, the resource location URL,
         * which cannot be null
         * @throws InvalidArgument Thrown if the {@code resourceLocation} is
         * null
         */
        public TableNamespace(String resourceLocation) {
            if (resourceLocation == null) {
                throw new InvalidArgument("resourceLocation is null");
            }

            this.prefix = null;
            this.resourceLocation = resourceLocation;
            this.hash = computeHash();
        }

        /**
         * Creates a table namespace from the bel
         * {@link org.openbel.framework.common.model.Namespace}.
         *
         * @param belNamespace
         * {@link org.openbel.framework.common.model.Namespace}, the bel
         * namespace, which cannot be null
         * @throws InvalidArgument Thrown if {@code belNamespace} is null
         */
        public TableNamespace(Namespace belNamespace) {
            if (belNamespace == null) {
                throw new InvalidArgument("belNamespace is null");
            }

            this.prefix = belNamespace.getPrefix();
            this.resourceLocation = belNamespace.getResourceLocation();
            this.hash = computeHash();
        }

        /**
         * Creates a table namespace. This public, no-argument constructor is
         * required when implementing Externalizable but it is not meant to be
         * used for anything else.
         */
        public TableNamespace() {
        }

        /**
         * Returns the table namespace prefix.
         *
         * @return {@link String}, the table namespace prefix, which can be null
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * Returns the table namespace resource location.
         *
         * @return {@link String}, the namespace resource location, which cannot
         * be null
         */
        public String getResourceLocation() {
            return resourceLocation;
        }

        /**
         * Compute the hashCode of {@link TableNamespace this table namespace}.
         *
         * @return the hashCode
         */
        private int computeHash() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((prefix == null) ? 0 : prefix.hashCode());
            result = prime
                    * result
                    + ((resourceLocation == null) ? 0 : resourceLocation
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
            TableNamespace other = (TableNamespace) obj;
            if (prefix == null) {
                if (other.prefix != null)
                    return false;
            } else if (!prefix.equals(other.prefix))
                return false;
            if (resourceLocation == null) {
                if (other.resourceLocation != null)
                    return false;
            } else if (!resourceLocation.equals(other.resourceLocation))
                return false;
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in) throws IOException,
                ClassNotFoundException {
            prefix = readString(in);
            resourceLocation = readString(in);
            hash = computeHash();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _to(ObjectOutput out) throws IOException {
            out.writeObject(prefix);
            out.writeObject(resourceLocation);
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
