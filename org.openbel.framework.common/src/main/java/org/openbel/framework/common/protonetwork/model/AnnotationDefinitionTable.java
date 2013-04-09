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
import static org.openbel.framework.common.BELUtilities.sizedHashMap;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.AnnotationType;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;
import org.openbel.framework.common.model.AnnotationDefinition;

/**
 * AnnotationDefinitionTable holds the document annotation definitions. This
 * class manages the annotation definitions through the
 * {@link #addAnnotationDefinition(TableAnnotationDefinition, int)} operation.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class AnnotationDefinitionTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767011L;
    public static final Integer URL_ANNOTATION_TYPE_ID = 2;
    public static final String URL_ANNOTATION_TYPE = "urlAnnotation";

    /**
     * Defines a unique collection to hold annotation definition values. Note:
     * This collection is backed by {@link LinkedHashSet} which is not
     * thread-safe.
     */
    private Set<TableAnnotationDefinition> annotationDefinitions =
            new LinkedHashSet<AnnotationDefinitionTable.TableAnnotationDefinition>();

    /**
     * Defines a map from {@link TableAnnotationDefinition} to an index
     * representing insertion order in {@code annotationDefinitions}. Note: This
     * collection is backed by {@link HashMap} which is not thread-safe.
     */
    private Map<TableAnnotationDefinition, Integer> definitionIndex =
            new HashMap<TableAnnotationDefinition, Integer>();

    /**
     * Defines a map from the index to the {@link TableAnnotationDefinition}.
     * Note: This collection is backed by {@link HashMap} which is not
     * thread-safe.
     */
    private Map<Integer, TableAnnotationDefinition> indexDefinition =
            new HashMap<Integer, AnnotationDefinitionTable.TableAnnotationDefinition>();

    /**
     * Defines a map from document index to a {@link Set} of annotation
     * definition index. Note: This collection is backed by
     * {@code LinkedHashMap} which is not threadsafe.
     */
    private Map<Integer, Set<Integer>> documentAnnotationDefinitions =
            new LinkedHashMap<Integer, Set<Integer>>();

    /**
     * Adds an annotation definition to the {@code annotationDefinitions} set.
     * The insertion index is captured by {@code definitionIndex}.
     *
     * @param annotationDefinition {@link TableAnnotationDefinition}, the
     * annotation definition to add, which cannot be null
     * @return {@code int}, the index of the added annotation definition, which
     * must be &gt;= 0.
     * @throws InvalidArgument Thrown if the {@code annotationDefinition}
     * argument is null
     */
    public int addAnnotationDefinition(
            TableAnnotationDefinition annotationDefinition, int did) {
        if (annotationDefinition == null) {
            throw new InvalidArgument("annotation definition is null");
        }

        if (annotationDefinitions.add(annotationDefinition)) {
            int nextIndex = annotationDefinitions.size() - 1;

            definitionIndex.put(annotationDefinition, nextIndex);
            indexDefinition.put(nextIndex, annotationDefinition);
        }

        Integer adi = definitionIndex.get(annotationDefinition);
        Set<Integer> dadi = documentAnnotationDefinitions.get(did);
        if (dadi == null) {
            dadi = new LinkedHashSet<Integer>();
            documentAnnotationDefinitions.put(did, dadi);
        }

        dadi.add(adi);
        return adi;
    }

    /**
     * Returns the annotation definition table's {@code annotationDefinitions}
     * set. This set is unmodifiable to preserve the state of the annotation
     * definition table.
     *
     * @return {@link Set}, which cannot be null or modified
     */
    public Set<TableAnnotationDefinition> getAnnotationDefinitions() {
        return Collections.unmodifiableSet(annotationDefinitions);
    }

    /**
     * Returns the map of {@link TableAnnotationDefinition} to index. This map
     * is unmodifiable to preserve the state of the annotation definition table.
     *
     * @return {@link Map}, which cannot be null or modified
     */
    public Map<TableAnnotationDefinition, Integer> getDefinitionIndex() {
        return Collections.unmodifiableMap(definitionIndex);
    }

    /**
     * Returns the map of index to {@link TableAnnotationDefinition}. This map
     * is unmodifiable to preserve the state of the annotation definition table.
     *
     * @return {@link Map}, which cannot be null or modified
     */
    public Map<Integer, TableAnnotationDefinition> getIndexDefinition() {
        return Collections.unmodifiableMap(indexDefinition);
    }

    /**
     * Returns the map of document index to a {@link Set} of annotation
     * definition index. This map is unmodifiable to preserve the state of the
     * annotation definition table.
     *
     * @return {@link Map}, which cannot be null or modified
     */
    public Map<Integer, Set<Integer>> getDocumentAnnotationDefinitions() {
        return documentAnnotationDefinitions;
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
                + ((annotationDefinitions == null) ? 0 : annotationDefinitions
                        .hashCode());
        result = prime * result
                + ((definitionIndex == null) ? 0 : definitionIndex.hashCode());
        result = prime
                * result
                + ((documentAnnotationDefinitions == null) ? 0
                        : documentAnnotationDefinitions.hashCode());
        result = prime * result
                + ((indexDefinition == null) ? 0 : indexDefinition.hashCode());
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
        AnnotationDefinitionTable other = (AnnotationDefinitionTable) obj;
        if (annotationDefinitions == null) {
            if (other.annotationDefinitions != null)
                return false;
        } else if (!annotationDefinitions.equals(other.annotationDefinitions))
            return false;
        if (definitionIndex == null) {
            if (other.definitionIndex != null)
                return false;
        } else if (!definitionIndex.equals(other.definitionIndex))
            return false;
        if (documentAnnotationDefinitions == null) {
            if (other.documentAnnotationDefinitions != null)
                return false;
        } else if (!documentAnnotationDefinitions
                .equals(other.documentAnnotationDefinitions))
            return false;
        if (indexDefinition == null) {
            if (other.indexDefinition != null)
                return false;
        } else if (!indexDefinition.equals(other.indexDefinition))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        // 1: read index def map size
        final int size = in.readInt();
        // size map accordingly
        indexDefinition = sizedHashMap(size);

        TableAnnotationDefinition tad;
        for (int i = 0; i < size; ++i) {
            tad = new TableAnnotationDefinition();
            // 1: read the table annotation definition
            tad.readExternal(in);
            // 2: read the number of documents
            final int documentsSize = in.readInt();
            for (int j = 0; j < documentsSize; ++j) {
                // 1: read each documents index
                addAnnotationDefinition(tad, in.readInt());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        TableAnnotationDefinition[] tads =
                index(TableAnnotationDefinition.class, indexDefinition);

        // 1: write index def map size
        out.writeInt(tads.length);

        for (int i = 0; i < tads.length; i++) {
            // 1: write the table annotation definition
            tads[i].writeExternal(out);
            final List<Integer> documents = new ArrayList<Integer>();
            Set<Entry<Integer, Set<Integer>>> e2 =
                    entries(documentAnnotationDefinitions);
            for (Map.Entry<Integer, Set<Integer>> e : e2) {
                if (e.getValue().contains(i)) {
                    documents.add(e.getKey());
                }
            }
            // 2: write the number of documents
            out.writeInt(documents.size());
            for (Integer document : documents) {
                // 1: write each documents index
                out.writeInt(document.intValue());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in, ReadCache cache) throws IOException,
            ClassNotFoundException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out, WriteCache cache) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * TableAnnotationDefinition encapsulates a document's annotation
     * definition.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     * @version 1.3 Derives from {@link ExternalType}
     */
    public static class TableAnnotationDefinition extends ExternalType {
        private static final long serialVersionUID = -6486699009265767012L;

        /**
         * Defines the table annotation definition name.
         */
        private/* final */String name;

        /**
         * Defines the table annotation definition description.
         */
        private/* final */String description;

        /**
         * Defines the table annotation definition usage text.
         */
        private/* final */String usage;

        /**
         * Defines the table annotation definition type as an {@code int} value.
         * This value maps to {@link AnnotationType#getValue()}.
         */
        private/* final */int annotationType;

        /**
         * Defines the annotation domain value for this table annotation
         * definition. The domain value is dependent on the
         * {@code annotationType}:
         * <ul>
         * <li>{@link AnnotationType#REGULAR_EXPRESSION} : a regular expression
         * string</li>
         * <li>{@link AnnotationType#ENUMERATION} : a packed string containing
         * values separated by a {@code |} character.</li>
         * <li>{@link AnnotationType#EXTERNAL} : an external annotation url
         * where the domain values are listed</li>
         * </ul>
         */
        private/* final */String annotationDomain;

        /**
         * Defines the hash for {@link TableAnnotationDefinition this table
         * annotation definition}.
         */
        private/* final */int hash;

        /**
         * Creates the TableAnnotationDefinition based on the BEL common model
         * {@link AnnotationDefinition}. <br/>
         * <br/>
         * If the annotation definition:
         * <ul>
         * <li>supplies a list annotation domain : set domain to the packed list
         * separated by {@link ProtoNetwork#VALUE_SEPARATOR}</li>
         * <li>supplies a pattern or external annotation domain : set domain to
         * the pattern or external url value</li>
         * </ul>
         *
         * @param belAnnotationDefinition {@link AnnotationDefinition}, the bel
         * annotation definition, which cannot be nul
         * @throws InvalidArgument - Thrown if {@code belAnnotationDefinition}
         * is null or {@link AnnotationDefinition#getType()} is {@code null}
         */
        public TableAnnotationDefinition(
                final AnnotationDefinition belAnnotationDefinition) {
            if (belAnnotationDefinition == null) {
                throw new InvalidArgument(
                        "bel annotation definition cannot be null.");
            }

            this.name = belAnnotationDefinition.getId();

            if (belAnnotationDefinition.getURL() != null) {
                // externally defined
                this.description = null;
                this.usage = null;

                this.annotationType = URL_ANNOTATION_TYPE_ID;
                this.annotationDomain = belAnnotationDefinition.getURL();
            } else {
                // defined in-line
                this.description = belAnnotationDefinition.getDescription();
                this.usage = belAnnotationDefinition.getUsage();

                AnnotationType type = belAnnotationDefinition.getType();
                if (type == null) {
                    throw new InvalidArgument("annotation type is null");
                }

                this.annotationType =
                        belAnnotationDefinition.getType().getValue();

                switch (type) {
                case ENUMERATION:
                    final StringBuilder bldr = new StringBuilder();
                    for (final String s : belAnnotationDefinition.getEnums()) {
                        bldr.append(s);
                        bldr.append(ProtoNetwork.VALUE_SEPARATOR);
                    }

                    if (bldr.length() > 0) {
                        this.annotationDomain =
                                bldr.substring(0, bldr.length() - 1);
                    } else {
                        this.annotationDomain = "";
                    }
                    break;
                case REGULAR_EXPRESSION:
                    this.annotationDomain = belAnnotationDefinition.getValue();
                    break;
                default:
                    this.annotationDomain = null;
                }
            }

            this.hash = computeHash();
        }

        /**
         * Creates the TableAnnotationDefinition This public, no-argument
         * constructor is required when implementing Externalizable but it is
         * not meant to be used for anything else.
         */
        public TableAnnotationDefinition() {
        }

        /**
         * Returns the table annotation definition name.
         *
         * @return {@link String}, the table annotation definition name, which
         * cannot be null
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the table annotation definition description.
         *
         * @return {@link String}, the annotation definition description, which
         * may be null, if the definition is externally defined
         */
        public String getDescription() {
            return description;
        }

        /**
         * Returns the table annotation definition usage.
         *
         * @return {@link String}, the annotation definition usage, which may be
         * null, if the definition is externally defined
         */
        public String getUsage() {
            return usage;
        }

        /**
         * Returns the annotation type {@code int} value. This value maps to
         * {@link AnnotationType#getValue()}.
         *
         * @return {@code int}, the annotation type value
         */
        public int getAnnotationType() {
            return annotationType;
        }

        /**
         * Returns the table annotation definition's domain value. The domain
         * value is dependent on the annotation type value:
         * <ul>
         * <li>{@value AnnotationType#ENUMERATION} : a packed string containing
         * values separated by a {@code |} character.</li>
         * <li>{@link AnnotationType#REGULAR_EXPRESSION} : a regular expression
         * string</li>
         * <li>{@link AnnotationDefinitionTable#URL_ANNOTATION_TYPE_ID} : a url
         * where the annotation is externally defined</li>
         * </ul>
         *
         * @return {@link String}, the annotation domain, which cannot be null
         */
        public String getAnnotationDomain() {
            return annotationDomain;
        }

        /**
         * Compute the hash for {@link TableAnnotationDefinition this table
         * annotation definition}.
         *
         * @return the hash
         */
        private int computeHash() {
            final int prime = 31;
            int result = 1;
            if (annotationDomain != null) {
                result = prime * result;
                result += annotationDomain.hashCode();
            }
            result = prime * result + annotationType;
            if (description != null) {
                result = prime * result;
                result += description.hashCode();
            }
            if (name != null) {
                result = prime * result;
                result += name.hashCode();
            }
            if (usage != null) {
                result = prime * result;
                result += usage.hashCode();
            }
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
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            TableAnnotationDefinition other = (TableAnnotationDefinition) obj;
            if (annotationDomain == null) {
                if (other.annotationDomain != null) {
                    return false;
                }
            } else if (!annotationDomain.equals(other.annotationDomain)) {
                return false;
            }
            if (annotationType != other.annotationType) {
                return false;
            }
            if (description == null) {
                if (other.description != null) {
                    return false;
                }
            } else if (!description.equals(other.description)) {
                return false;
            }
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            if (usage == null) {
                if (other.usage != null) {
                    return false;
                }
            } else if (!usage.equals(other.usage)) {
                return false;
            }
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in) throws IOException,
                ClassNotFoundException {
            name = readString(in);
            description = readString(in);
            usage = readString(in);
            annotationType = readInteger(in);
            annotationDomain = readString(in);
            hash = computeHash();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _to(ObjectOutput out) throws IOException {
            out.writeObject(name);
            out.writeObject(description);
            out.writeObject(usage);
            writeInteger(out, annotationType);
            out.writeObject(annotationDomain);
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
