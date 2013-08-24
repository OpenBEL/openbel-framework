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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable.TableProtoEdge;

/**
 * {@link ProtoNodeTable} defines the symbol table to hold proto nodes for the
 * {@link ProtoNetwork proto network}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class ProtoNodeTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767000L;

    /**
     * Defines a {@link List list} to hold {@link String proto nodes}. This
     * {@link Collection collection} is not thread-safe.
     */
    private final List<String> protoNodes = new ArrayList<String>();

    /**
     * Defines a {@link Map map} to hold {@link Integer term index} keys to
     * {@link Integer proto node index} value. This {@link Collection
     * collection} is not thread-safe.
     */
    private final Map<Integer, Integer> termNodeIndex =
            new HashMap<Integer, Integer>();

    /**
     * Defines a {@link Map map} to hold {@link Integer node index} keys to
     * {@link Integer term index} value. This {@link Collection collection} is
     * not thread-safe.
     */
    private final Map<Integer, Integer> nodeTermIndex =
            new HashMap<Integer, Integer>();

    /**
     * Defines a {@link Map map} to hold old {@link Integer node index} keys to
     * new {@link Integer node index} value. This {@link Collection collection}
     * is not thread-safe.
     */
    private/* final */Map<Integer, Integer> remappedNodes =
            new HashMap<Integer, Integer>();

    private/* final */Map<Integer, Integer> equivalences =
            new HashMap<Integer, Integer>();

    /**
     * Adds a proto node {@link String label} for a specific {@link Integer term
     * index}. If the {@link Integer term index} has already been added then its
     * {@link Integer proto node index} will be returned.
     * <p>
     * This operation maintains the {@link Map map} of term index to node index.
     * </p>
     *
     * @param termIndex {@link Integer} the term index created from the addition
     * to the {@link TermTable term table}
     * @param label {@link String} the placeholder label from the
     * {@link TermTable term table}
     * @return the {@link Integer proto node index}
     * @throws InvalidArgument Thrown if {@code label} is {@code null}
     */
    public Integer addNode(final int termIndex, final String label) {
        if (label == null) {
            throw new InvalidArgument("label", label);
        }

        // if we have already seen this term index, return
        Integer visitedIndex = termNodeIndex.get(termIndex);
        if (visitedIndex != null) {
            return visitedIndex;
        }

        // add this new proto node
        int pnIndex = protoNodes.size();
        protoNodes.add(label);

        // bi-directionally map term index to node index
        termNodeIndex.put(termIndex, pnIndex);
        nodeTermIndex.put(pnIndex, termIndex);
        getEquivalences().put(pnIndex, getEquivalences().size());

        return pnIndex;
    }

    /**
     * Return the {@link List proto nodes list}. This list is modifiable to
     * allow the edge equivalencing process to filter duplicates.
     *
     * @return the {@link List proto nodes list}
     */
    public List<String> getProtoNodes() {
        return Collections.unmodifiableList(protoNodes);
    }

    /**
     * Return the {@link Map proto nodes index map}. This map is modifiable to
     * allow the edge equivalencing process to filter duplicates.
     *
     * @return the {@link Map proto nodes index map}, keys are {@link Integer
     * term index}, values are {@link Integer proto node index}
     */
    public Map<Integer, Integer> getTermNodeIndex() {
        return Collections.unmodifiableMap(termNodeIndex);
    }

    /**
     * Return the {@link Map node term index map}. This map is modifiable to
     * allow the edge equivalencing process to filter duplicates.
     *
     * @return the {@link Map visited term index map}, keys are {@link Integer
     * term index}, values are {@link Integer node index}.
     */
    public Map<Integer, Integer> getNodeTermIndex() {
        return Collections.unmodifiableMap(nodeTermIndex);
    }

    /**
     * Return the {@link Map remapped old to new nodes map}. This map is used to
     * hold equivalenced nodes so {@link TableProtoEdge proto edges} can be
     * properly equivalenced. This map is modifiable to allow equivalencing to
     * main the remapped state.
     *
     * @return the {@link Map remapped old to new nodes map}
     */
    public Map<Integer, Integer> getRemappedNodes() {
        return remappedNodes;
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
                + ((protoNodes == null) ? 0 : protoNodes.hashCode());
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
        ProtoNodeTable other = (ProtoNodeTable) obj;
        if (protoNodes == null) {
            if (other.protoNodes != null)
                return false;
        } else if (!protoNodes.equals(other.protoNodes))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        final int size = in.readInt();
        for (int i = 0; i < size; ++i) {
            final String label = readString(in);
            final int termIndex = in.readInt();
            addNode(termIndex, label);
        }

        remappedNodes.clear();
        remappedNodes = (Map<Integer, Integer>) in.readObject();
        equivalences.clear();
        equivalences = (Map<Integer, Integer>) in.readObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        final int size = protoNodes.size();
        out.writeInt(size);
        int i = 0;
        for (String protoNode : protoNodes) {
            out.writeObject(protoNode);
            out.writeInt(nodeTermIndex.get(i).intValue());
            i++;
        }

        out.writeObject(remappedNodes);
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
}
