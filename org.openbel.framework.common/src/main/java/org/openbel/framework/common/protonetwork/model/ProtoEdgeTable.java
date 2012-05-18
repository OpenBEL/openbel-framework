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
package org.openbel.framework.common.protonetwork.model;

import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;
import org.openbel.framework.common.protonetwork.model.StatementTable.TableStatement;

/**
 * {@link ProtoEdgeTable} defines a symbol table to hold proto edges for the
 * {@link ProtoNetwork proto network}. The proto edge is composed of the triple:
 * 
 * <pre>
 * source node, relationship, target node
 * </pre>
 * <p>
 * This symbol table holds the association
 * </p>
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class ProtoEdgeTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767016L;

    /**
     * Defines a {@link List list} to hold the {@link TableProtoEdge proto
     * edges}. This {@link Collection collection} is not thread-safe.
     */
    private List<TableProtoEdge> protoEdges =
            new ArrayList<ProtoEdgeTable.TableProtoEdge>();

    /**
     * Defines a {@link Map map} with {@link Integer statement index} key and
     * {@link List set of proto edge indexes} as the value. This
     * {@link Collection collection} is not thread-safe.
     */
    private final Map<Integer, Set<Integer>> stmtEdges =
            new HashMap<Integer, Set<Integer>>();

    /**
     * Defines a {@link Map map} with {@link Integer edge index} key and
     * {@link List set of statement index} as the value. This {@link Collection
     * collection} is not thread-safe.
     */
    private final Map<Integer, Set<Integer>> edgeStmts =
            new HashMap<Integer, Set<Integer>>();

    /**
     * Defines a {@link Map map} with {@link TableProtoEdge proto edges} key and
     * {@link Integer proto edge index} as the value. This {@link Collection
     * collection} is not thread-safe.
     */
    private final Map<TableProtoEdge, Integer> visited =
            new HashMap<ProtoEdgeTable.TableProtoEdge, Integer>();

    private Map<Integer, Integer> equivalences =
            new HashMap<Integer, Integer>();

    /**
     * Adds 1 or more {@link TableProtoEdge proto edges} to the table and
     * associates them with the supporting {@link TableStatement statement}
     * index.
     * <p>
     * There must be at least one {@link TableProtoEdge proto edge} to
     * associate to a statement otherwise an exception is thrown.
     * </p>
     * 
     * @param stmt the {@link TableStatement statement} index to associate the
     * {@link TableProtoEdge proto edges} to, which cannot be null
     * @param newEdges the {@link TableProtoEdge proto edges} array to add to
     * the table, which cannot be null or have a length less than 1
     * @throws InvalidArgument May be thrown if <tt>stmt</tt> is <tt>null</tt>,
     * <tt>newEdges</tt> is null, or <tt>newEdges</tt> is <tt> <= 0</tt>
     */
    public void addEdges(final Integer stmt, final TableProtoEdge... newEdges) {
        if (stmt == null) {
            throw new InvalidArgument("stmt", stmt);
        }

        if (newEdges == null || newEdges.length <= 0) {
            throw new InvalidArgument("newEdges", newEdges);
        }

        Set<Integer> edges = stmtEdges.get(stmt);
        if (edges == null) {
            edges = new HashSet<Integer>();
            stmtEdges.put(stmt, edges);
        }

        for (final TableProtoEdge newEdge : newEdges) {
            int idx = protoEdges.size();
            protoEdges.add(newEdge);

            Integer existingEdge = visited.get(newEdge);
            if (existingEdge != null) {
                getEquivalences().put(idx, existingEdge);
            } else {
                visited.put(newEdge, idx);
                getEquivalences().put(idx, getEquivalences().size());
            }

            edges.add(idx);
            Set<Integer> stmts = new LinkedHashSet<Integer>();
            stmts.add(stmt);
            edgeStmts.put(idx, stmts);
        }
    }

    /**
     * Return the {@link List proto edges list}. This list is modifiable to
     * allow the edge equivalencing process to filter duplicates.
     * 
     * @return the {@link List proto edges list}
     */
    public List<TableProtoEdge> getProtoEdges() {
        return protoEdges;
    }

    /**
     * Return the {@link Map statement to edges associations map}. This map is
     * modifiable to allow the edge equivalencing process to filter duplicates.
     * 
     * @return the {@link Map statement to edges associations map}
     */
    public Map<Integer, Set<Integer>> getStatementEdges() {
        return stmtEdges;
    }

    /**
     * Return the {@link Map edge to statements associations map}. This map is
     * modifiable to allow the edge equivalencing process to filter duplicates.
     * 
     * @return the {@link Map edge to statements associations map}
     */
    public Map<Integer, Set<Integer>> getEdgeStatements() {
        return edgeStmts;
    }

    /**
     * Return the {@link Map proto edges to index map}. This map is unmodifiable
     * to prevent the visited state from being changed.
     * 
     * @return the {@link Map edge to statements associations map}
     */
    public Map<TableProtoEdge, Integer> getVisitedEdges() {
        return Collections.unmodifiableMap(visited);
    }

    public Map<Integer, Integer> getEquivalences() {
        return equivalences;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((protoEdges == null) ? 0 : protoEdges.hashCode());
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
        ProtoEdgeTable other = (ProtoEdgeTable) obj;
        if (protoEdges == null) {
            if (other.protoEdges != null)
                return false;
        } else if (!protoEdges.equals(other.protoEdges))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        final int sizeProtoEdges = in.readInt();
        protoEdges = sizedArrayList(sizeProtoEdges);
        for (int i = 0; i < sizeProtoEdges; i++) {
            TableProtoEdge tpe = new TableProtoEdge();
            tpe.readExternal(in);
            protoEdges.add(tpe);
        }

        final int sizeStmtEdges = in.readInt();
        for (int i = 0; i < sizeStmtEdges; i++) {
            final int key = in.readInt();
            final int valueSize = in.readInt();
            final Set<Integer> value = new HashSet<Integer>();
            for (int j = 0; j < valueSize; j++) {
                value.add(in.readInt());
            }
            stmtEdges.put(key, value);
        }
        final int sizeEdgeStmts = in.readInt();
        for (int i = 0; i < sizeEdgeStmts; i++) {
            final int key = in.readInt();
            final int valueSize = in.readInt();
            final Set<Integer> value = new HashSet<Integer>();
            for (int j = 0; j < valueSize; j++) {
                value.add(in.readInt());
            }
            edgeStmts.put(key, value);
        }
        final int sizeVisited = in.readInt();
        for (int i = 0; i < sizeVisited; i++) {
            TableProtoEdge key = (TableProtoEdge) in.readObject();
            final int value = in.readInt();
            visited.put(key, value);
        }

        equivalences = (Map<Integer, Integer>) in.readObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        final int sizeProtoEdges = protoEdges.size();
        out.writeInt(sizeProtoEdges);
        for (int i = 0; i < sizeProtoEdges; i++) {
            TableProtoEdge tpe = protoEdges.get(i);
            tpe.writeExternal(out);
        }

        final int sizeStmtEdges = stmtEdges.size();
        out.writeInt(sizeStmtEdges);
        for (Map.Entry<Integer, Set<Integer>> entry : stmtEdges.entrySet()) {
            out.writeInt(entry.getKey().intValue());
            final Set<Integer> value = entry.getValue();

            out.writeInt(value.size());
            for (Integer j : value) {
                out.writeInt(j.intValue());
            }
        }
        final int sizeEdgeStmts = edgeStmts.size();
        out.writeInt(sizeEdgeStmts);
        for (Map.Entry<Integer, Set<Integer>> entry : edgeStmts.entrySet()) {
            out.writeInt(entry.getKey().intValue());
            final Set<Integer> value = entry.getValue();
            out.writeInt(value.size());
            for (Integer j : value) {
                out.writeInt(j.intValue());
            }
        }
        final int sizeVisited = visited.size();
        out.writeInt(sizeVisited);
        for (Map.Entry<TableProtoEdge, Integer> entry : visited.entrySet()) {
            out.writeObject(entry.getKey());
            out.writeInt(entry.getValue().intValue());
        }
        out.writeObject(equivalences);
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
     * {@link TableProtoEdge} defines the container object for a proto network
     * edge. The {@link TableProtoEdge proto network edge} is made up of:
     * 
     * <pre>
     * source (int), rel (RelationshipType), target (int)
     * </pre>
     * 
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     * @version 1.3 Derives from {@link ExternalType}
     */
    public static final class TableProtoEdge extends ExternalType {
        private static final long serialVersionUID = -6486699009265767015L;

        /**
         * The source node index.
         */
        private/* final */int source;

        /**
         * The {@link String relationship type}.
         */
        private/* final */String rel;

        /**
         * The target node index.
         */
        private/* final */int target;

        /**
         * This object's hashCode which is computed on construction.
         */
        private/* final */int hash;

        /**
         * Create the {@link TableProtoEdge proto edge} with the triple:
         * 
         * <pre>
         * source, rel, target
         * </pre>
         * 
         * @param source the {@code int} source index
         * @param relName the {@link String relationship name}
         * @param target the {@code int} target index
         */
        public TableProtoEdge(final int source, final String relName,
                final int target) {
            this.source = source;
            this.rel = relName;
            this.target = target;
            this.hash = computeHash();
        }

        /**
         * Creates a {@link TableProtoEdge proto edge}. This public, no-argument
         * constructor is required when implementing Externalizable but it is
         * not meant to be used for anything else.
         */
        public TableProtoEdge() {
        }

        /**
         * Return the source.
         * 
         * @return the source
         */
        public int getSource() {
            return source;
        }

        /**
         * Return the {@link String relationship name}.
         * 
         * @return the {@link String relationship name}
         */
        public String getRel() {
            return rel;
        }

        /**
         * Return the target.
         * 
         * @return the target
         */
        public int getTarget() {
            return target;
        }

        /**
         * Compute the hash since {@link TableProtoEdge this} is immutable.
         * 
         * @return the computed hash {@code int}
         */
        private int computeHash() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((rel == null) ? 0 : rel.hashCode());
            result = prime * result + source;
            result = prime * result + target;
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
            TableProtoEdge other = (TableProtoEdge) obj;
            if (!rel.equals(other.rel))
                return false;
            if (source != other.source)
                return false;
            if (target != other.target)
                return false;
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in) throws IOException,
                ClassNotFoundException {
            source = in.readInt();
            rel = readString(in);
            target = in.readInt();
            hash = computeHash();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _to(ObjectOutput out) throws IOException {
            out.writeInt(source);
            out.writeObject(rel);
            out.writeInt(target);
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
