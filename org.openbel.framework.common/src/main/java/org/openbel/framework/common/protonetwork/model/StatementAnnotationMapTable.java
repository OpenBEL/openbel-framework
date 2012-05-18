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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;

/**
 * StatementAnnotationMapTable holds the index mapping from statement id to
 * annotation definition / value index pairs.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class StatementAnnotationMapTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767018L;

    /**
     * Defines a map from statement index value to a set of annotation pairs.
     * Note: This collection is backed by {@link LinkedHashMap} which is not
     * thread-safe.
     */
    private Map<Integer, Set<AnnotationPair>> statementAnnotationPairsIndex =
            new LinkedHashMap<Integer, Set<AnnotationPair>>();

    /**
     * Adds a mapping of {@code statementIndex} to
     * {@code annotationValueDefinitionPairs} in the
     * {@code statementAnnotationIndex} map. A {@code statementIndex} will not
     * be indexed if the {@code annotationValueDefinitionPairs} collection is
     * empty.
     * 
     * @param statementIndex {@link Integer}, the statement index as the key,
     * must not be null
     * @param annotationPairs {@link List} of {@code Integer[]}, the annotation
     * value / definition index, which cannot be null, and if empty the
     * {@code statementIndex} will not be indexed
     * @throws InvalidArgument Thrown if {@code statementIndex} or
     * {@code annotationValueDefinitionPairs} is null
     */
    public void addStatementAnnotation(Integer statementIndex,
            Set<AnnotationPair> annotationPairs) {
        if (statementIndex == null) {
            throw new InvalidArgument("statementIndex is null");
        }

        if (annotationPairs == null) {
            throw new InvalidArgument("annotationValueDefinitionPairs is null.");
        }

        if (annotationPairs.isEmpty()) {
            return;
        }

        Set<AnnotationPair> annotationValueDefinitions =
                statementAnnotationPairsIndex
                        .get(statementIndex);
        if (annotationValueDefinitions == null) {
            statementAnnotationPairsIndex.put(statementIndex,
                    annotationPairs);
        } else {
            annotationValueDefinitions.addAll(annotationPairs);
        }
    }

    /**
     * Returns the statement annotation map table's statement index to
     * annotation pairs map. This map is mutable to allow reindexing during
     * phase II proto network merging.
     * 
     * @return {@link Map}, which cannot be null
     */
    public Map<Integer, Set<AnnotationPair>> getStatementAnnotationPairsIndex() {
        return statementAnnotationPairsIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((statementAnnotationPairsIndex == null) ? 0
                        : statementAnnotationPairsIndex.hashCode());
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
        StatementAnnotationMapTable other = (StatementAnnotationMapTable) obj;
        if (statementAnnotationPairsIndex == null) {
            if (other.statementAnnotationPairsIndex != null)
                return false;
        } else if (!statementAnnotationPairsIndex
                .equals(other.statementAnnotationPairsIndex))
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
        for (int i = 0; i < size; i++) {
            final int statementIndex = in.readInt();
            final int annotationPairsSize = in.readInt();
            Set<AnnotationPair> annotationPairs =
                    new HashSet<AnnotationPair>(annotationPairsSize);
            for (int j = 0; j < annotationPairsSize; ++j) {
                AnnotationPair pair = new AnnotationPair();
                pair.readExternal(in);
                annotationPairs.add(pair);
            }
            addStatementAnnotation(statementIndex, annotationPairs);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        final int size = statementAnnotationPairsIndex.size();
        out.writeInt(size);
        for (Map.Entry<Integer, Set<AnnotationPair>> entry : statementAnnotationPairsIndex
                .entrySet()) {
            out.writeInt(entry.getKey().intValue());
            final Set<AnnotationPair> annotationPairs = entry.getValue();
            final int annotationPairsSize = annotationPairs.size();
            out.writeInt(annotationPairsSize);
            for (AnnotationPair pair : annotationPairs) {
                pair.writeExternal(out);
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
     * AnnotationPair represents an integer pair of annotation definition id and
     * an annotation value id. An {@code Integer[]} array would not work since a
     * {@link Set} cannot perform equality between them.
     * 
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     * @version 1.3 Derives from {@link ExternalType}
     */
    public static class AnnotationPair extends ExternalType {
        private static final long serialVersionUID = -6486699009265767019L;

        /**
         * Defines the annotation definition id.
         */
        private/* final */int annotationDefinitionId;

        /**
         * Defines the annotation value id.
         */
        private/* final */int annotationValueId;

        /**
         * Defines the hash for {@link AnnotationPair this annotation pair}.
         */
        private/* final */int hash;

        /**
         * Creates an annotation pair from a {@code annotationDefinitionId} and
         * an {@code annotationValueId}.
         * 
         * @param annotationDefinitionId {@code int}, the annotation definition
         * id
         * @param annotationValueId {@code int}, the annotation value id
         */
        public AnnotationPair(int annotationDefinitionId, int annotationValueId) {
            this.annotationDefinitionId = annotationDefinitionId;
            this.annotationValueId = annotationValueId;
            this.hash = computeHash();
        }

        /**
         * Creates an annotation pair. This public, no-argument constructor is
         * required when implementing Externalizable but it is not meant to be
         * used for anything else.
         */
        public AnnotationPair() {
        }

        /**
         * Returns the annotation definition id.
         * 
         * @return {@code int}, the annotation definition id
         */
        public int getAnnotationDefinitionId() {
            return annotationDefinitionId;
        }

        /**
         * Returns the annotation value id.
         * 
         * @return {@code int}, the annotation value id
         */
        public int getAnnotationValueId() {
            return annotationValueId;
        }

        /**
         * Computes the hash for {@link AnnotationPair this annotation pair}.
         * 
         * @return the computed hash
         */
        private int computeHash() {
            final int prime = 31;
            int result = 1;
            result = prime * result + annotationDefinitionId;
            result = prime * result + annotationValueId;
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
            AnnotationPair other = (AnnotationPair) obj;
            if (annotationDefinitionId != other.annotationDefinitionId)
                return false;
            if (annotationValueId != other.annotationValueId)
                return false;
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in) throws IOException,
                ClassNotFoundException {
            annotationDefinitionId = in.readInt();
            annotationValueId = in.readInt();
            hash = computeHash();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _to(ObjectOutput out) throws IOException {
            out.writeInt(annotationDefinitionId);
            out.writeInt(annotationValueId);
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
