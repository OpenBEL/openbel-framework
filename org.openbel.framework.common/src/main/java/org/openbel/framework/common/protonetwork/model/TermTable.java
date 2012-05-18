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

import static org.openbel.framework.common.BELUtilities.entries;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.index;
import static org.openbel.framework.common.BELUtilities.sizedHashMap;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openbel.framework.common.BELUtilities;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Term;

/**
 * TermTable holds the term values for statements. This class manages the
 * insertion index and occurrence count state through the {@code addTerm}
 * operation.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class TermTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767007L;
    public static final String PARAMETER_SUBSTITUTION = "#";

    /**
     * Defines a list to hold term values. Note: This collection is backed by
     * {@link ArrayList} which is not thread-safe.
     */
    private List<String> termValues = new ArrayList<String>();

    /**
     * Defines a map that contains term strings to avoid holding duplicate
     * instances of the same {@link String}. Note: This collection is backed by
     * {@link HashMap} which is not thread-safe.
     */
    private Map<String, String> termStrings = new HashMap<String, String>();

    /**
     * Defines a map to hold term index (key) to global term index (value).
     * Note: This map is backed {@link HashMap} which is not thread-safe. This
     * map is manipulated by external users of the class.
     */
    private Map<Integer, Integer> globalTermIndex =
            new HashMap<Integer, Integer>();

    /**
     * Defines a {@link Term} map, of term to index, that prevents adding
     * another {@link Term} to <tt>termValues</tt>.
     */
    private Map<Term, Integer> visitedTerms = BELUtilities
            .sizedHashMap(512);

    /**
     * Defines a {@link Map} of {@link Integer} index to common-model
     * {@link Term}. Note this map is backed by {@link HashMap} which is not
     * thread-safe.
     */
    private Map<Integer, Term> indexedTerms = BELUtilities.sizedHashMap(512);

    /**
     * Adds a term to the {@code terms} set. The insertion index and term
     * occurrences are also indexed in the {@code index} and {@code count} map.
     * 
     * @param term {@link Term}, the term to be added as a {@link String} to the
     * {@code terms} set, which cannot be null
     * @throws InvalidArgument Thrown if {@code term} is null
     */
    public int addTerm(Term term) {
        if (term == null) {
            throw new InvalidArgument("term is null");
        }

        // if we have already seen this term, return its index
        Integer visitedIndex = visitedTerms.get(term);
        if (visitedIndex != null) {
            return visitedIndex;
        }

        // replace term parameters with placeholders and convert to a string
        StringBuilder sb = new StringBuilder();
        replaceParameters(sb, term, term.getAllParametersLeftToRight());
        String termExpression = sb.toString();

        // if term expression was already created, use that reference
        final String sharedTermExpression = termStrings.get(termExpression);
        if (sharedTermExpression != null) {
            termExpression = sharedTermExpression;
        } else {
            termStrings.put(termExpression, termExpression);
        }

        // add this new term
        int termIndex = termValues.size();
        termValues.add(termExpression);

        // index the new term
        visitedTerms.put(term, termIndex);
        indexedTerms.put(termIndex, term);
        globalTermIndex.put(termIndex, globalTermIndex.size());

        return termIndex;
    }

    /**
     * Constructs the BEL syntax for a {@link Term} replacing a
     * {@link Parameter} if it exists in {@code psub}.
     * 
     * @param sb {@link StringBuilder}, the container for the BEL syntax
     * @param t {@link Term}, the term to convert to BEL syntax
     * @param psub {@link List} of {@link Parameter}, the parameters to replace
     * in the BEL syntax
     */
    private static void replaceParameters(StringBuilder sb, Term t,
            List<Parameter> psub) {
        sb.append(t.getFunctionEnum().getDisplayValue()).append("(");
        if (hasItems(t.getFunctionArguments())) {
            for (BELObject bo : t.getFunctionArguments()) {
                if (Term.class.isAssignableFrom(bo.getClass())) {
                    replaceParameters(sb, (Term) bo, psub);
                } else {
                    Parameter ip = (Parameter) bo;
                    if (psub.contains(ip)) {
                        sb.append(PARAMETER_SUBSTITUTION);
                    } else {
                        sb.append(ip.getValue());
                    }
                }
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
        }
    }

    /**
     * Removes a term by index, from the list of terms within the table.
     * 
     * @param tid Term index
     */
    public void removeTerm(final int tid) {
        termValues.remove(tid);
    }

    /**
     * Returns the term table's {@code termValues} list. This list is
     * unmodifiable to preserve the state of the term table.
     * 
     * @return {@link List}, which cannot be null or modified
     */
    public List<String> getTermValues() {
        return Collections.unmodifiableList(termValues);
    }

    /**
     * Returns the visited common-model {@link Term} objects to term index map.
     * This map is unmodifiable to preserve the state of the term table.
     * 
     * @return {@link Map} of {@link Term} to {@link Integer} index, which
     * cannot be null or modified
     */
    public Map<Term, Integer> getVisitedTerms() {
        return Collections.unmodifiableMap(visitedTerms);
    }

    /**
     * Returns the {@link Integer} to visited common-model {@link Term} map.
     * This map is unmodifiable to preserve the state of the term table.
     * 
     * @return {@link Map} of {@link Integer} index to common-model {@link Term}
     * , which cannot be null or modified
     */
    public Map<Integer, Term> getIndexedTerms() {
        return Collections.unmodifiableMap(indexedTerms);
    }

    /**
     * Returns the term table's {@code globalTermIndex} map of term index to
     * global term index.
     * 
     * @return {@link Map} of term index (key) to global term index (value)
     */
    public Map<Integer, Integer> getGlobalTermIndex() {
        return globalTermIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((termValues == null) ? 0 : termValues.hashCode());
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
        TermTable other = (TermTable) obj;
        if (termValues == null) {
            if (other.termValues != null)
                return false;
        } else if (!termValues.equals(other.termValues))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        // 1: read number of terms
        final int size = in.readInt();
        indexedTerms = sizedHashMap(size);
        for (int i = 0; i < size; ++i) {
            // 1: read each term
            final Term term = (Term) in.readObject();
            addTerm(term);
        }

        // 2: read number of global terms
        int gtisize = in.readInt();
        globalTermIndex = sizedHashMap(gtisize);
        for (int i = 0; i < gtisize; i++) {
            // 1: read global term key
            int key = in.readInt();
            // 2: read global term value
            int value = in.readInt();
            globalTermIndex.put(key, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(final ObjectOutput out) throws IOException {
        Term[] terms = index(Term.class, indexedTerms);
        // 1: write number of terms
        out.writeInt(terms.length);
        for (int i = 0; i < terms.length; i++) {
            // 1: write each term
            out.writeObject(terms[i]);
        }
        // 2: write number of global terms
        out.writeInt(globalTermIndex.size());
        for (final Entry<Integer, Integer> e : entries(globalTermIndex)) {
            Integer key = e.getKey();
            Integer value = e.getValue();
            // 1: write global term key
            out.writeInt(key);
            // 2: write global term value
            out.writeInt(value);
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
