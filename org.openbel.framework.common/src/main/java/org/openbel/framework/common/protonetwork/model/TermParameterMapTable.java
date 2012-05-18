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

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;

/**
 * TermParameterMapTable holds the index mapping between term values and
 * parameter index values.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class TermParameterMapTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767003L;

    /**
     * Defines a map from term index value to an ordered list of parameter index
     * values. Note: This collection is backed by {@link HashMap} which is not
     * thread-safe.
     */
    private Map<Integer, List<Integer>> termParameterIndex =
            new HashMap<Integer, List<Integer>>();

    /**
     * Adds a mapping of {@code termIndex} to {@code parameterValues} in the
     * {@code termParameterIndex} map. A {@code termIndex} will not be indexed
     * if the {@code parameterValues} collection is empty.
     * 
     * @param termIndex {@link Integer}, the term index as the key, must not be
     * null
     * @param parameterValues {@link List} of {@link Integer}, the parameter
     * index values, which cannot be null, and if empty the {@code termIndex}
     * will not be indexed
     * @throws InvalidArgument Thrown if {@code termIndex} or
     * {@code parameterValues} is null
     */
    public void addTermParameterMapping(Integer termIndex,
            List<Integer> parameterValues) {
        if (termIndex == null) {
            throw new InvalidArgument(
                    "The 'termIndex' parameter must not be null.");
        }

        if (parameterValues == null) {
            throw new InvalidArgument(
                    "The 'parameterValues' parameter must not be null.");
        }

        if (parameterValues.isEmpty()) {
            return;
        }

        List<Integer> parameters = termParameterIndex.get(termIndex);
        if (parameters == null) {
            termParameterIndex.put(termIndex, parameterValues);
        } else {
            parameters.addAll(parameterValues);
        }
    }

    /**
     * Returns the parameter indices for the provided term index.
     * 
     * @param tid Term index
     * @return The list of parameter indices, which may be empty
     */
    public List<Integer> getParameterIndexes(final int tid) {
        List<Integer> ret = termParameterIndex.get(tid);
        if (ret == null) {
            ret = emptyList();
        }
        return unmodifiableList(ret);
    }

    /**
     * Returns the number of term mappings.
     * 
     * @return int
     */
    public int getNumberOfMappings() {
        return termParameterIndex.size();
    }

    /**
     * Returns the number of term-parameter mappings
     * 
     * @return int
     */
    public int getNumberOfTermParameterMappings() {
        int i = 0;
        for (final List<Integer> value : termParameterIndex.values())
            i += value.size();
        return i;
    }

    /**
     * Removes the mapping of the provided term index, {@code tid}, from the
     * backing map.
     * 
     * @param tid Term index
     */
    public void removeTermParameterMapping(final int tid) {
        termParameterIndex.remove(tid);
    }

    /**
     * Returns a read-only view into the term-parameter entry set.
     * 
     * @return {@link Set} of map {@link Entry entries}
     */
    public Set<Entry<Integer, List<Integer>>> entrySet() {
        return unmodifiableSet(termParameterIndex.entrySet());
    }

    /**
     * Returns the term parameter map table's term index to an ordered list of
     * parameter index values.
     * 
     * @return {@link Map}, which cannot be null
     */
    public Map<Integer, List<Integer>> getTermParameterIndex() {
        return termParameterIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((termParameterIndex == null) ? 0
                : termParameterIndex.hashCode());
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
        TermParameterMapTable other = (TermParameterMapTable) obj;
        if (termParameterIndex == null) {
            if (other.termParameterIndex != null)
                return false;
        } else if (!termParameterIndex.equals(other.termParameterIndex))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        // XXX this can be improved
        final int size = in.readInt();
        for (int i = 0; i < size; i++) {
            final int termIndex = in.readInt();
            final int parametersSize = in.readInt();
            List<Integer> parameters = new ArrayList<Integer>(parametersSize);
            for (int j = 0; j < parametersSize; j++) {
                parameters.add(in.readInt());
            }
            addTermParameterMapping(termIndex, parameters);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        // XXX this can be improved
        out.writeInt(termParameterIndex.size());
        for (Map.Entry<Integer, List<Integer>> entry : termParameterIndex
                .entrySet()) {
            // the keys and values of termParameterIndex are all non-null
            out.writeInt(entry.getKey().intValue());
            final List<Integer> parameters = entry.getValue();
            out.writeInt(parameters.size());
            for (Integer parameter : parameters) {
                out.writeInt(parameter.intValue());
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
}
