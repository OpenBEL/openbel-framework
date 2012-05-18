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
import java.util.List;
import java.util.Map.Entry;

import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ParameterTable.TableParameter;

/**
 * ProtoNetwork encapsulates {@code *Table} objects.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3
 */
public class ProtoNetwork extends ExternalType {
    private static final long serialVersionUID = -6486699009265767010L;

    /**
     * Constant defining the position of the term index in the raw term index
     * data.
     * 
     * @see #getTermIndices()
     */
    public static final int TERM_INDEX = 0;

    /**
     * Constant defining the position of the parameter index in the raw term
     * index data.
     * 
     * @see #getTermIndices()
     */
    public static final int PARAM_INDEX = 1;

    /**
     * Constant defining the position of the namespace index in the raw term
     * index data.
     * 
     * @see #getTermIndices()
     * @see #NO_NAMESPACE
     */
    public static final int NAMESPACE_INDEX = 2;

    /**
     * Namespaces are not required on parameters; namespace index values may be
     * {@value #NO_NAMESPACE}.
     */
    public static final int NO_NAMESPACE = -1;

    /**
     * Defines a value separator to be used in a multi-valued field of a
     * ProtoNetwork.
     */
    public static final char VALUE_SEPARATOR = '|';

    /**
     * Defines and instantiates the {@link DocumentTable}.
     */
    private DocumentTable documentTable = new DocumentTable();

    /**
     * Defines and instantiates the {@link NamespaceTable}.
     */
    private NamespaceTable namespaceTable = new NamespaceTable();

    /**
     * Defines and instantiates the {@link ParameterTable}.
     */
    private ParameterTable parameterTable = new ParameterTable();

    /**
     * Defines and instantiates the {@link TermTable}.
     */
    private TermTable termTable = new TermTable();

    /**
     * Defines and instantiates the {@link TermParameterMapTable}.
     */
    private TermParameterMapTable termParameterMapTable =
            new TermParameterMapTable();

    /**
     * Defines and instantiates the {@link StatementTable}.
     */
    private StatementTable statementTable = new StatementTable();

    /**
     * Defines and instantiates the {@link AnnotationDefinitionTable}.
     */
    private AnnotationDefinitionTable annotationDefinitionTable =
            new AnnotationDefinitionTable();

    /**
     * Defines and instantiates the {@link AnnotationValueTable}.
     */
    private AnnotationValueTable annotationValueTable =
            new AnnotationValueTable();

    /**
     * Defines and instantiates the {@link StatementAnnotationMapTable}.
     */
    private StatementAnnotationMapTable statementAnnotationMapTable =
            new StatementAnnotationMapTable();

    /**
     * Defines and instantiates the {@link ProtoNodeTable proto node table}.
     */
    private ProtoNodeTable protoNodeTable = new ProtoNodeTable();

    /**
     * Defines and instantiates the {@link ProtoEdgeTable proto edge table}.
     */
    private ProtoEdgeTable protoEdgeTable = new ProtoEdgeTable();

    /**
     * Returns the document table.
     * 
     * @return {@link DocumentTable}, the document table, which cannot be null
     */
    public DocumentTable getDocumentTable() {
        return documentTable;
    }

    /**
     * Returns the namespace table.
     * 
     * @return {@link NamespaceTable}, the namespace table, which cannot be null
     */
    public NamespaceTable getNamespaceTable() {
        return namespaceTable;
    }

    /**
     * Returns the parameter table.
     * 
     * @return {@link ParameterTable}, the parameter table, which cannot be null
     */
    public ParameterTable getParameterTable() {
        return parameterTable;
    }

    /**
     * Returns the term table.
     * 
     * @return {@link TermTable}, the term table, which cannot be null
     */
    public TermTable getTermTable() {
        return termTable;
    }

    /**
     * Returns the term parameter map table.
     * 
     * @return {@link TermParameterMapTable}, the term parameter map table,
     * which cannot be null
     */
    public TermParameterMapTable getTermParameterMapTable() {
        return termParameterMapTable;
    }

    /**
     * Returns the statement table.
     * 
     * @return {@link StatementTable}, the statement table, which cannot be null
     */
    public StatementTable getStatementTable() {
        return statementTable;
    }

    /**
     * Returns the annotation definition table.
     * 
     * @return {@link AnnotationDefinitionTable}, the annotation definition
     * table, which cannot be null
     */
    public AnnotationDefinitionTable getAnnotationDefinitionTable() {
        return annotationDefinitionTable;
    }

    /**
     * Returns the annotation value table.
     * 
     * @return {@link AnnotationValueTable}, the annotation value table, which
     * cannot be null
     */
    public AnnotationValueTable getAnnotationValueTable() {
        return annotationValueTable;
    }

    /**
     * Returns the statement annotation map table.
     * 
     * @return {@link StatementAnnotationMapTable}, the statement annotation map
     * table, which cannot be null
     */
    public StatementAnnotationMapTable getStatementAnnotationMapTable() {
        return statementAnnotationMapTable;
    }

    /**
     * Returns the {@link ProtoNodeTable proto node table}.
     * 
     * @return the {@link ProtoNodeTable proto node table}
     */
    public ProtoNodeTable getProtoNodeTable() {
        return protoNodeTable;
    }

    /**
     * Returns the {@link ProtoNodeTable proto edge table}.
     * 
     * @return the {@link ProtoNodeTable proto edge table}
     */
    public ProtoEdgeTable getProtoEdgeTable() {
        return protoEdgeTable;
    }

    /**
     * Returns the number of statements in this proto-network.
     * 
     * @return int The number of statements
     */
    public int getNumberOfStatements() {
        return statementTable.getStatements().size();
    }

    /**
     * Returns a two-dimensional array of term data.
     * <p>
     * The length of the returned array along the first dimension is equivalent
     * to the number of term-parameter mappings contained by the proto-network.
     * The length of the second dimension, {@code 3}, defines the tuple
     * <tt>(term_index, parameter_index, namespace_index)</tt>).
     * </p>
     * <p>
     * For example: <br>
     * 
     * <pre>
     * int[][] termIndices = getTermIndices();
     * int termParameterEntry = termIndices[0];
     * int termIndex = termIndices[0][0];
     * int paramIndex = termIndices[0][1];
     * int nsIndex = termIndices[0][2];
     * </pre>
     * 
     * </p>
     * 
     * @return term data as a two-dimensional array ({@code int[][]})
     * @see #TERM_INDEX
     * @see #PARAM_INDEX
     * @see #NAMESPACE_INDEX
     */
    public int[][] getTermIndices() {

        // Data is constructed by iterating term-parameter mappings
        final TermParameterMapTable myTPM = getTermParameterMapTable();
        final ParameterTable myPT = getParameterTable();
        final NamespaceTable nt = getNamespaceTable();
        final int count = myTPM.getNumberOfTermParameterMappings();

        final int[][] ret = new int[count][3];

        int i = 0;
        for (final Entry<Integer, List<Integer>> entry : myTPM.entrySet()) {

            // TPM entries are term index to list of param indices
            final int termIdx = entry.getKey();
            final List<Integer> paramIdxs = entry.getValue();

            for (final int paramIdx : paramIdxs) {
                ret[i] = new int[3];
                ret[i][TERM_INDEX] = termIdx;
                ret[i][PARAM_INDEX] = paramIdx;

                final TableParameter tp = myPT.getTableParameter(paramIdx);
                final TableNamespace tn = tp.getNamespace();
                Integer nsIndex = nt.getNamespaceIndex().get(tn);

                if (nsIndex == null)
                    ret[i][NAMESPACE_INDEX] = NO_NAMESPACE;
                else
                    ret[i][NAMESPACE_INDEX] = nsIndex;

                i++;
            }
        }

        return ret;
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
                + ((annotationDefinitionTable == null) ? 0
                        : annotationDefinitionTable.hashCode());
        result = prime
                * result
                + ((annotationValueTable == null) ? 0 : annotationValueTable
                        .hashCode());
        result = prime * result
                + ((documentTable == null) ? 0 : documentTable.hashCode());
        result = prime * result
                + ((namespaceTable == null) ? 0 : namespaceTable.hashCode());
        result = prime * result
                + ((parameterTable == null) ? 0 : parameterTable.hashCode());
        result = prime
                * result
                + ((statementAnnotationMapTable == null) ? 0
                        : statementAnnotationMapTable.hashCode());
        result = prime * result
                + ((statementTable == null) ? 0 : statementTable.hashCode());
        result = prime
                * result
                + ((termParameterMapTable == null) ? 0 : termParameterMapTable
                        .hashCode());
        result = prime * result
                + ((termTable == null) ? 0 : termTable.hashCode());
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
        ProtoNetwork other = (ProtoNetwork) obj;
        if (annotationDefinitionTable == null) {
            if (other.annotationDefinitionTable != null)
                return false;
        } else if (!annotationDefinitionTable
                .equals(other.annotationDefinitionTable))
            return false;
        if (annotationValueTable == null) {
            if (other.annotationValueTable != null)
                return false;
        } else if (!annotationValueTable.equals(other.annotationValueTable))
            return false;
        if (documentTable == null) {
            if (other.documentTable != null)
                return false;
        } else if (!documentTable.equals(other.documentTable))
            return false;
        if (namespaceTable == null) {
            if (other.namespaceTable != null)
                return false;
        } else if (!namespaceTable.equals(other.namespaceTable))
            return false;
        if (parameterTable == null) {
            if (other.parameterTable != null)
                return false;
        } else if (!parameterTable.equals(other.parameterTable))
            return false;
        if (statementAnnotationMapTable == null) {
            if (other.statementAnnotationMapTable != null)
                return false;
        } else if (!statementAnnotationMapTable
                .equals(other.statementAnnotationMapTable))
            return false;
        if (statementTable == null) {
            if (other.statementTable != null)
                return false;
        } else if (!statementTable.equals(other.statementTable))
            return false;
        if (termParameterMapTable == null) {
            if (other.termParameterMapTable != null)
                return false;
        } else if (!termParameterMapTable.equals(other.termParameterMapTable))
            return false;
        if (termTable == null) {
            if (other.termTable != null)
                return false;
        } else if (!termTable.equals(other.termTable))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        documentTable = new DocumentTable();
        documentTable.readExternal(in);
        namespaceTable = new NamespaceTable();
        namespaceTable.readExternal(in);
        parameterTable = new ParameterTable();
        parameterTable.readExternal(in);
        termTable = new TermTable();
        termTable.readExternal(in);
        termParameterMapTable = new TermParameterMapTable();
        termParameterMapTable.readExternal(in);
        statementTable = new StatementTable();
        statementTable.readExternal(in);
        annotationDefinitionTable = new AnnotationDefinitionTable();
        annotationDefinitionTable.readExternal(in);
        annotationValueTable = new AnnotationValueTable();
        annotationValueTable.readExternal(in);
        statementAnnotationMapTable = new StatementAnnotationMapTable();
        statementAnnotationMapTable.readExternal(in);
        protoNodeTable = new ProtoNodeTable();
        protoNodeTable.readExternal(in);
        protoEdgeTable = new ProtoEdgeTable();
        protoEdgeTable.readExternal(in);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        documentTable.writeExternal(out);
        namespaceTable.writeExternal(out);
        parameterTable.writeExternal(out);
        termTable.writeExternal(out);
        termParameterMapTable.writeExternal(out);
        statementTable.writeExternal(out);
        annotationDefinitionTable.writeExternal(out);
        annotationValueTable.writeExternal(out);
        statementAnnotationMapTable.writeExternal(out);
        protoNodeTable.writeExternal(out);
        protoEdgeTable.writeExternal(out);
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
}
