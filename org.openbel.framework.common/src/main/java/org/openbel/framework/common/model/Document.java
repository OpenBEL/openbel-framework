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
package org.openbel.framework.common.model;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.openbel.framework.common.BELUtilities.*;
import static org.openbel.framework.common.model.Namespace.DEFAULT_NAMESPACE_PREFIX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.InvalidArgument;

/**
 * BEL documents are structured text documents which contain BEL statements
 * along with sufficient additional information to fully describe and process
 * the document.
 * <p>
 * BEL documents contain a number of {@link Statement statements} organized into
 * {@link StatementGroup statement groups}. Documents are statement-iterable:
 * 
 * <pre>
 * <code>
 * for (final Statement statement : document) {
 *     // ...
 * }
 * </code>
 * </pre>
 * 
 * </p>
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Document implements BELModelObject, Iterable<Statement> {
    private static final long serialVersionUID = -6709772752903171478L;

    private final Header header;
    private NamespaceGroup namespaceGroup;
    private List<AnnotationDefinition> definitions =
            new ArrayList<AnnotationDefinition>();
    private final List<StatementGroup> statementGroups;

    /**
     * Creates a document with the provided header, initially containing the
     * supplied statement group.
     * 
     * @param header Non-null header
     * @param group Non-null statement group
     * @throws InvalidArgument Thrown if {@code header} or {@code group} is null
     */
    public Document(final Header header, final StatementGroup group) {
        if (group == null) {
            throw new InvalidArgument("group is null");
        }
        if (header == null) {
            throw new InvalidArgument("header is null");
        }

        statementGroups = new ArrayList<StatementGroup>();
        statementGroups.add(group);
        this.header = header;
    }

    /**
     * Creates a document with the provided header, containing the supplied
     * statement groups.
     * 
     * @param header Non-null header
     * @param groups Non-null, nonempty list of statement groups
     * @throws InvalidArgument Thrown if {@code header} is null or
     * {@code groups} is null or empty
     */
    public Document(final Header header, final List<StatementGroup> groups) {
        if (noItems(groups)) {
            throw new InvalidArgument("groups has no items");
        }
        if (header == null) {
            throw new InvalidArgument("header is null");
        }

        this.statementGroups = groups;
        this.header = header;
    }

    /**
     * Creates a document with the provided header, containing the supplied
     * statement groups and optional properties.
     * 
     * @param header Non-null header
     * @param groups Non-null, nonempty list of statement groups
     * @param nsGroup Namespace group
     * @param ad Annotation definition list
     * @throws InvalidArgument Thrown if {@code groups} has no items
     */
    public Document(final Header header, final List<StatementGroup> groups,
            NamespaceGroup nsGroup, List<AnnotationDefinition> ad) {
        if (noItems(groups)) {
            throw new InvalidArgument("groups has no items");
        }
        this.statementGroups = groups;
        this.header = header;
        this.namespaceGroup = nsGroup;
        this.definitions = ad;
    }

    /**
     * Returns the document's header.
     * 
     * @return Header
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Returns the document's namespace group.
     * 
     * @return NamespaceGroup, which may be null
     */
    public NamespaceGroup getNamespaceGroup() {
        return namespaceGroup;
    }

    /**
     * Returns all annotations contained within the document.
     * 
     * @return List of annotations, which may be empty
     */
    public List<Annotation> getAllAnnotations() {
        List<Annotation> ret = new ArrayList<Annotation>();
        for (StatementGroup sg : statementGroups) {
            ret.addAll(sg.getAllAnnotations());
        }
        return ret;
    }

    /**
     * Returns the set of all namespaces contained within the document.
     * 
     * @return Set of namespaces, which may be empty
     */
    public Set<Namespace> getAllNamespaces() {
        if (namespaceGroup == null) {
            return emptySet();
        }
        return new HashSet<Namespace>(namespaceGroup.getAllNamespaces());
    }

    /**
     * Returns the set of all terms contained within the document.
     * 
     * @return Set of terms, which may be empty
     */
    public Set<Term> getAllTerms() {
        final Set<Term> ret = new HashSet<Term>();
        for (final StatementGroup sg : statementGroups) {
            for (final Statement stmt : sg.getAllStatements()) {
                ret.addAll(stmt.getAllTerms());
            }
        }
        return ret;
    }

    /**
     * Returns the set of all parameters contained within the document.
     * 
     * @return Set of parameters, which may be empty
     */
    public Set<Parameter> getAllParameters() {
        final Set<Parameter> ret = new HashSet<Parameter>();
        for (final Term term : getAllTerms()) {
            List<Parameter> params = term.getParameters();
            if (params == null) {
                continue;
            }
            ret.addAll(params);
        }
        return ret;
    }

    /**
     * Returns a list of all statement groups contained within the document.
     * 
     * @return List of statement groups
     */
    public List<StatementGroup> getAllStatementGroups() {
        final List<StatementGroup> ret = new ArrayList<StatementGroup>();
        ret.addAll(statementGroups);
        for (final StatementGroup sg : statementGroups) {
            ret.addAll(sg.getAllStatementGroups());
        }
        return ret;
    }

    /**
     * Returns a list of all statements contained within the document.
     * 
     * @return List of statements, which may be empty
     */
    public List<Statement> getAllStatements() {
        final List<Statement> ret = new LinkedList<Statement>();
        for (final StatementGroup sg : statementGroups) {
            ret.addAll(sg.getAllStatements());
        }
        return ret;
    }

    /**
     * Returns the number of statements contained within the document.
     * 
     * @return int
     */
    public int getNumberOfStatements() {
        return getAllStatements().size();
    }

    /**
     * Sets the document's namespace group.
     * 
     * @param namespaceGroup Document's namespace group
     */
    public void setNamespaceGroup(NamespaceGroup namespaceGroup) {
        this.namespaceGroup = namespaceGroup;
    }

    /**
     * Returns the document's namespaces as a mapped keyed by namespace
     * {@link Namespace#getPrefix() namespace prefix}.
     * 
     * @return Map of namespaces to instances
     */
    public Map<String, Namespace> getNamespaceMap() {
        final Map<String, Namespace> ret = new HashMap<String, Namespace>();
        if (namespaceGroup == null) {
            return ret;
        }

        final List<Namespace> namespaces = namespaceGroup.getNamespaces();
        if (hasItems(namespaces)) {
            for (final Namespace ns : namespaces)
                ret.put(ns.getPrefix(), ns);
        }

        final String defns = namespaceGroup.getDefaultResourceLocation();
        if (defns != null) {
            Namespace ns = new Namespace(DEFAULT_NAMESPACE_PREFIX, defns);
            ret.put(DEFAULT_NAMESPACE_PREFIX, ns);
        }

        return ret;
    }

    /**
     * Returns the count of statements contained within the document.
     * 
     * @return int
     */
    public int getStatementCount() {
        int ret = 0;
        for (final StatementGroup sg : statementGroups) {
            ret += sg.getAllStatements().size();
        }
        return ret;
    }

    /**
     * Returns the count of namespaces contained within the document.
     * <p>
     * This method simply returns {@link Map#size()} on
     * {@link #getNamespaceMap()}.
     * </p>
     * 
     * @return int
     * @see #getNamespaceMap()
     */
    public int getNamespaceCount() {
        return getNamespaceMap().size();
    }

    /**
     * Returns the document's definitions.
     * 
     * @return List of annotation definitions, which may be null
     */
    public List<AnnotationDefinition> getDefinitions() {
        return definitions;
    }

    /**
     * Sets the document's definitions.
     * 
     * @param definitions Document's definitions
     */
    public void setDefinitions(List<AnnotationDefinition> definitions) {
        this.definitions = definitions;
    }

    /**
     * Returns the document's annotation definitions as a mapped keyed by
     * annotation definition {@link AnnotationDefinition#getId() identifier}.
     * 
     * @return Non-null map of annotation definition identifiers to instances,
     * which may be empty
     */
    public Map<String, AnnotationDefinition> getDefinitionMap() {
        if (definitions == null) {
            return emptyMap();
        }
        final Map<String, AnnotationDefinition> ret =
                new HashMap<String, AnnotationDefinition>(definitions.size());
        for (final AnnotationDefinition ad : definitions) {
            ret.put(ad.getId(), ad);
        }
        return ret;
    }

    /**
     * Returns the document's statement groups.
     * 
     * @return Non-null, nonempty list of statement groups
     */
    public List<StatementGroup> getStatementGroups() {
        return statementGroups;
    }

    /**
     * Adds a statement group to the document's statement groups.
     * 
     * @param group Statement group
     */
    public void addStatementGroup(StatementGroup group) {
        statementGroups.add(group);
    }

    /**
     * Returns the document's name, contained in its {@link Header#getName()
     * header}.
     * 
     * @return String
     */
    public String getName() {
        return header.getName();
    }

    /**
     * Resolves any property references in use within the document to their
     * corresponding instances.
     */
    public void resolveReferences() {
        // Resolve annotations to their definitions
        Map<String, AnnotationDefinition> defs = getDefinitionMap();
        final List<Annotation> annos = new ArrayList<Annotation>();

        // Get all the annotation used by the document's statement groups
        for (final StatementGroup sg : statementGroups) {
            annos.addAll(sg.getAllAnnotations());
        }

        // Resolve the references
        for (final Annotation a : annos) {
            AnnotationDefinition ad = a.getDefinition();
            // Skip annotations without definitions
            if (ad == null) {
                continue;
            }

            final String id = ad.getId();
            AnnotationDefinition documentDefinition = defs.get(id);

            // BEL document annotations must reference valid definitions...
            assert documentDefinition != null;
            // ... so sanity check that for now.

            // Resolve the reference to the instance
            a.setDefinition(documentDefinition);
        }

        // Resolve parameters to their namespaces
        Map<String, Namespace> nspcs = getNamespaceMap();
        final List<Parameter> params = new ArrayList<Parameter>();

        // Get all the parameters within the document
        for (final StatementGroup sg : statementGroups) {
            params.addAll(sg.getAllParameters());
        }

        // Get the default namespace if present
        final Namespace defaultNS = nspcs.get(DEFAULT_NAMESPACE_PREFIX);

        // Resolve the references
        for (final Parameter av : params) {
            Namespace ns = av.getNamespace();

            // Skip parameters without namespaces
            if (ns == null) {
                if (defaultNS != null) {
                    av.setNamespace(defaultNS);
                }
                continue;
            }

            final String prefix = ns.getPrefix();
            Namespace documentNamespace = nspcs.get(prefix);

            // BEL document parameters must reference valid namespaces...
            assert documentNamespace != null;
            // ... so sanity check that for now.

            // Resolve the reference to the instance
            av.setNamespace(documentNamespace);
        }
    }

    /**
     * Returns a map of {@link StatementGroup statement group} to a {@link Set
     * set} of {@link Statement statements}.
     * <p>
     * The returned map is not backed by the document. If the document is
     * modified in any way, the returned map may no longer reflect a correct
     * mapping of statement group to statement set.
     * </p>
     * 
     * @return {@link Map} of {@link StatementGroup} to {@link Set} of
     * {@link Statement Statements}
     */
    public Map<StatementGroup, Set<Statement>> mapStatements() {
        List<StatementGroup> groups = getStatementGroups();
        final int size = groups.size();
        Map<StatementGroup, Set<Statement>> ret = sizedHashMap(size);

        for (final StatementGroup group : groups) {
            List<Statement> stmts = group.getStatements();
            Set<Statement> stmtset;

            // No statements? Map to an empty set
            if (stmts == null) {
                stmtset = emptySet();
                ret.put(group, stmtset);
                continue;
            }

            stmtset = sizedHashSet(stmts.size());
            stmtset.addAll(stmts);
            ret.put(group, stmtset);
        }

        return ret;
    }

    /**
     * Returns an {@link Iterator iterator} over the document's
     * {@link Statement statements}.
     * <p>
     * The following code is guaranteed to be safe (no
     * {@link NullPointerException null pointer exceptions} will be thrown):
     * 
     * <pre>
     * <code>
     * for (final Statement statement : document) {
     * }
     * </code>
     * </pre>
     * 
     * </p>
     * 
     * @return {@link Iterator} of {@link Statement statements}
     */
    @Override
    public Iterator<Statement> iterator() {
        return getAllStatements().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Document [");

        // header is non-null by contract
        builder.append("header=");
        builder.append(header);
        builder.append(", ");

        if (namespaceGroup != null) {
            builder.append("namespaceGroup=");
            builder.append(namespaceGroup);
            builder.append(", ");
        }

        if (definitions != null) {
            builder.append("definitions=");
            builder.append(definitions);
            builder.append(", ");
        }

        // statement groups is non-null by contract
        builder.append("statementGroups=");
        builder.append(statementGroups);

        builder.append("]");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result *= prime;
        if (definitions != null) {
            result += definitions.hashCode();
        }

        result *= prime;
        result += header.hashCode();

        result *= prime;
        if (namespaceGroup != null) {
            result += namespaceGroup.hashCode();
        }

        result *= prime;
        // statement groups is non-null by contract
        result += statementGroups.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }

        final Document d = (Document) o;
        if (definitions == null) {
            if (d.definitions != null) {
                return false;
            }
        } else if (!definitions.equals(d.definitions)) {
            return false;
        }

        // header is non-null by contract
        if (!header.equals(d.header)) {
            return false;
        }

        if (namespaceGroup == null) {
            if (d.namespaceGroup != null) {
                return false;
            }
        } else if (!namespaceGroup.equals(d.namespaceGroup)) {
            return false;
        }

        // statement groups is non-null by contract
        if (!statementGroups.equals(d.statementGroups)) {
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document clone() {
        Header header2 = header.clone();

        NamespaceGroup namespaceGroup2 = null;
        if (namespaceGroup != null) {
            namespaceGroup2 = namespaceGroup.clone();
        }

        List<AnnotationDefinition> definitions2 = null;
        if (definitions != null) {
            definitions2 = sizedArrayList(definitions.size());
            for (AnnotationDefinition a : definitions)
                definitions2.add(a.clone());
        }

        List<StatementGroup> statementGroups2 = null;
        if (statementGroups != null) {
            statementGroups2 = sizedArrayList(statementGroups.size());
            for (StatementGroup s : statementGroups)
                statementGroups2.add(s.clone());
        }

        return new Document(header2, statementGroups2, namespaceGroup2,
                definitions2);
    }
}
