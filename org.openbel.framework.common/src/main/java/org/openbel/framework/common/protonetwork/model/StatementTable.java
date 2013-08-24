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

import static org.openbel.framework.common.BELUtilities.index;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.external.ExternalType;
import org.openbel.framework.common.external.ReadCache;
import org.openbel.framework.common.external.WriteCache;
import org.openbel.framework.common.model.AnnotationGroup;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;

/**
 * StatementTable holds the statement values. This class manages the statements
 * through the {@link #addStatement(TableStatement, int)} operation.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @version 1.3 Derives from {@link ExternalType}
 */
public class StatementTable extends ExternalType {
    private static final long serialVersionUID = -6486699009265767001L;

    /**
     * Defines an ordered list to hold statement values. Note: This collection
     * is backed by {@link ArrayList} which is not thread-safe.
     */
    private List<TableStatement> statements = new ArrayList<TableStatement>();

    /**
     * Defines a map from statement index to global index. Note: This collection
     * is backed by {@link HashMap} which is not thread-safe.
     */
    private Map<Integer, Integer> globalStatementIndex =
            new HashMap<Integer, Integer>();

    /**
     * Defines a map from document index to a {@link List} of statement index.
     * Note: This collection is backed by {@code LinkedHashMap} which is not
     * threadsafe.
     */
    private Map<Integer, Integer> statementDocumentMap =
            new LinkedHashMap<Integer, Integer>();

    /**
     * Defines a map of {@link TableStatement} to {@link Integer} index. Note:
     * This collection is backed by {@link HashMap} which is not thread-safe.
     */
    private Map<TableStatement, Integer> tableStatementIndex =
            new HashMap<StatementTable.TableStatement, Integer>();

    /**
     * Defines a map of {@link Statement} to {@link Integer} index. This
     * collection stores the index of the common-model statement object for
     * duplication checking. Note: This collection is backed by {@link HashMap}
     * which is not thread-safe.
     */
    private Map<Statement, Integer> visitedStatements =
            new HashMap<Statement, Integer>();

    /**
     * Defines a map of {@link Integer} index to {@link Statement}. This
     * collection stores the common-model statement for duplication checking.
     * Note: This collection is backed by {@link HashMap} which is not
     * thread-safe.
     */
    private Map<Integer, Statement> indexedStatements =
            new HashMap<Integer, Statement>();

    /**
     * These objects should only be constructed by {@link ProtoNetwork}.
     */
    StatementTable() {

    }

    /**
     * Adds a statement to the {@code statements} set. The insertion index and
     * statement occurrences are also indexed in the {@code index} and
     * {@code count} map. <br/>
     * <br/>
     * Uses statement ids as 1-based to support nested statements as an
     * <tt>int</tt>.
     *
     * @param tblStatement {@link TableStatement}, the statement to be added to
     * the {@code statements} set
     * @param did Document index
     * @return int, the index of the added statement, which is 'int' datatype
     * since it cannot be null
     * @throws InvalidArgument Thrown if {@code statement} is null
     */
    public int addStatement(TableStatement tblStatement, Statement statement,
            int did) {
        if (tblStatement == null) {
            throw new InvalidArgument("statement is null");
        }

        int nsid = statements.size();
        statements.add(tblStatement);

        visitedStatements.put(statement, nsid);
        indexedStatements.put(nsid, statement);

        globalStatementIndex.put(nsid, nsid);
        statementDocumentMap.put(nsid, did);
        tableStatementIndex.put(tblStatement, nsid);

        return nsid;
    }

    /**
     * Returns the statement table's <tt>statements</tt> list. This list is
     * unmodifiable to preserve the state of the statement table.
     *
     * @return {@link List}, which cannot be null or modified
     */
    public List<TableStatement> getStatements() {
        return Collections.unmodifiableList(statements);
    }

    /**
     * Returns the statement table's global index {@link Map}. This map is
     * unmodifiable to preserve the state of the statement table.
     *
     * @return {@link Map} of {@link Integer} statement index to {@link Integer}
     * global index, which cannot be null
     */
    public Map<Integer, Integer> getGlobalStatementIndex() {
        return globalStatementIndex;
    }

    /**
     * Returns the statement table's table statement index {@link Map}. This map
     * is unmodifiable to preserve the state of the statement table.
     *
     * @return {@link Map} of {@link TableStatement} to {@link Integer}
     * statement index, which cannot be null or modified
     */
    public Map<TableStatement, Integer> getTableStatementIndex() {
        return Collections.unmodifiableMap(tableStatementIndex);
    }

    /**
     * Returns the map of statement index to document index. This map is
     * unmodifiable to preserve the state of the statement table.
     *
     * @return {@link Map}, which cannot be null or modified
     */
    public Map<Integer, Integer> getStatementDocument() {
        return statementDocumentMap;
    }

    /**
     * Returns the map of common-model {@link Statement} to {@link Integer}
     * statement index. This map it unmodifiable to preserve the state of the
     * statement table.
     *
     * @return {@link Map} of {@link Statement} to {@link Integer} index, which
     * cannot be null or modified
     */
    public Map<Statement, Integer> getVisitedStatements() {
        return Collections.unmodifiableMap(visitedStatements);
    }

    /**
     * Returns the map of {@link Integer} index to common-model
     * {@link Statement}. This map it unmodifiable to preserve the state of the
     * statement table.
     *
     * @return {@link Map} of {@link Integer} index to {@link Statement} index,
     * which cannot be null or modified
     */
    public Map<Integer, Statement> getIndexedStatements() {
        return Collections.unmodifiableMap(indexedStatements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime * result
                        + ((statements == null) ? 0 : statements.hashCode());
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
        StatementTable other = (StatementTable) obj;
        if (statements == null) {
            if (other.statements != null)
                return false;
        } else if (!statements.equals(other.statements))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in) throws IOException,
            ClassNotFoundException {
        // 1: read number of stmts
        final int stmts = in.readInt();
        statements = sizedArrayList(stmts);
        for (int i = 0; i < stmts; i++) {
            TableStatement ts = new TableStatement();
            // 1: read each table statement
            ts.readExternal(in);
            // 2: read the relevant document index
            final int did = in.readInt();
            // 3: read the statement
            final Statement statement = readStatement(in);
            addStatement(ts, statement, did);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _to(ObjectOutput out) throws IOException {
        final int stmts = statements.size();
        // 1: write number of stmts
        out.writeInt(stmts);
        Integer[] stmtdocs = index(Integer.class, statementDocumentMap);
        Statement[] stmtidxs = index(Statement.class, indexedStatements);
        for (int i = 0; i < stmts; i++) {
            TableStatement ts = statements.get(i);
            // 1: write each table statement
            ts.writeExternal(out);
            // 2: write the relevant document index
            out.writeInt(stmtdocs[i]);
            // 3: write statement
            writeStatement(out, stmtidxs[i]);
        }
    }

    static void writeRelationship(ObjectOutput out, RelationshipType rel)
            throws IOException {
        if (rel == null) {
            out.writeByte(0);
        } else {
            out.writeByte(1);
            out.writeInt(rel.ordinal());
        }
    }

    static RelationshipType readRelationship(ObjectInput in)
            throws IOException {
        RelationshipType ret = null;
        byte obyte = in.readByte();
        if (obyte == 1) {
            int relOrdinal = in.readInt();
            ret = RelationshipType.values()[relOrdinal];
        }
        return ret;
    }

    static void writeStatement(ObjectOutput out, Statement stmt)
            throws IOException {
        // 1: comment, may be null
        String comment = stmt.getComment();
        writeObject(out, comment);
        // 2: annotation group, may be null
        AnnotationGroup ag = stmt.getAnnotationGroup();
        writeObject(out, ag);
        // 3: subject term, never null
        Term subject = stmt.getSubject();
        writeTerm(out, subject);

        // 4: statement object, may be null
        Statement.Object object = stmt.getObject();
        if (object == null) {
            out.writeByte(0);
        } else if (object.getTerm() != null) {
            out.writeByte(1);
            writeTerm(out, object.getTerm());
        } else if (object.getStatement() != null) {
            out.writeByte(2);
            writeStatement(out, object.getStatement());
        }

        // 5: relationship type, may be null
        writeRelationship(out, stmt.getRelationshipType());
    }

    static Statement readStatement(ObjectInput in) throws IOException,
            ClassNotFoundException {
        // 1: comment
        String comment = (String) readObject(in);
        // 2: annotation group
        AnnotationGroup ag = (AnnotationGroup) readObject(in);
        // 3: subject term
        Term subject = readTerm(in);

        // 4: statement object
        Statement.Object object;
        byte obyte = in.readByte();
        if (obyte == 1) {
            object = new Statement.Object(readTerm(in));
        } else if (obyte == 2) {
            object = new Statement.Object(readStatement(in));
        } else {
            object = null;
        }

        // 5: relationship type
        RelationshipType rel = readRelationship(in);
        return new Statement(subject, comment, ag, object, rel);
    }

    static void writeTerm(ObjectOutput out, Term term) throws IOException {
        // 1: function enum, never null
        FunctionEnum fx = term.getFunctionEnum();
        out.writeObject(fx);
        // 2: function arguments, may be null
        List<BELObject> fxargs = term.getFunctionArguments();
        if (fxargs == null) {
            out.writeByte(0);
        } else {
            out.writeByte(1);
            out.writeInt(fxargs.size());
            for (final BELObject bo : fxargs) {
                if (bo instanceof Parameter) {
                    out.writeByte(0);
                    writeParameter(out, (Parameter) bo);
                } else if (bo instanceof Term) {
                    out.writeByte(1);
                    writeTerm(out, (Term) bo);
                }
            }
        }
    }

    static Term readTerm(ObjectInput in) throws IOException,
            ClassNotFoundException {
        // 1: function enum
        FunctionEnum fx = (FunctionEnum) in.readObject();
        // 2: functions arguments
        List<BELObject> fxargs = null;
        byte obyte = in.readByte();
        if (obyte == 1) {
            int size = in.readInt();
            fxargs = sizedArrayList(size);
            for (int i = 0; i < size; i++) {
                obyte = in.readByte();
                if (obyte == 0) {
                    fxargs.add(readParameter(in));
                } else if (obyte == 1) {
                    fxargs.add(readTerm(in));
                }
            }
        }
        return new Term(fx, fxargs);
    }

    static void writeParameter(ObjectOutput out, Parameter param)
            throws IOException {
        writeObject(out, param.getNamespace());
        writeObject(out, param.getValue());
    }

    static Parameter readParameter(ObjectInput in) throws IOException,
            ClassNotFoundException {
        Namespace ns = (Namespace) readObject(in);
        String value = (String) readObject(in);
        return new Parameter(ns, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void _from(ObjectInput in, ReadCache cache) throws IOException {
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
     * TableStatement holds the statement values.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     * @version 1.3 Derives from {@link ExternalType}
     */
    public static class TableStatement extends ExternalType {
        private static final long serialVersionUID = -6486699009265767002L;

        /**
         * Defines the subject term index for this table statement.
         */
        private/* final */Integer subjectTerm;

        /**
         * Defines the relationship name for this table statement.
         */
        private/* final */String relationship;

        /**
         * Defines the object index for this table statement.
         */
        private/* final */Integer objectTerm;

        /**
         * Defines the nested subject index for this table statement.
         */
        private/* final */Integer nestedSubject;

        /**
         * Defines the nested relationship for this table statement.
         */
        private/* final */String nestedRelationship;

        /**
         * Defines the nested object index for this table statement.
         */
        private/* final */Integer nestedObject;

        /**
         * Defines the hash for {@link TableStatement this table statement}.
         */
        private/* final */int hash;

        /**
         * Create a definitional {@link TableStatement statement}.
         *
         * @param subjectTerm the subject term index
         * @throws InvalidArgument Thrown if the {@code subjectTerm} is
         * {@code null} is null
         */
        public TableStatement(Integer subjectTerm) {
            if (subjectTerm == null) {
                throw new InvalidArgument("subjectTerm", subjectTerm);
            }
            this.subjectTerm = subjectTerm;
            this.relationship = null;
            this.objectTerm = null;
            this.nestedSubject = null;
            this.nestedRelationship = null;
            this.nestedObject = null;
            this.hash = computeHash();
        }

        /**
         * Creates a simple statement from a
         *
         * <pre>
         * subject term, relationship, object term
         * </pre>
         *
         * @param subjectTerm {@link Integer}, the subject term id, which cannot
         * be null
         * @param relationship {@link String}, the relationship name, which can
         * be null
         * @param objectTerm {@link Integer}, the object term index
         * @throws InvalidArgument Thrown if the {@code subjectTerm} is
         * {@code null}
         */
        public TableStatement(Integer subjectTerm, String relationship,
                Integer objectTerm) {
            if (subjectTerm == null) {
                throw new InvalidArgument("subjectTerm", subjectTerm);
            }
            this.subjectTerm = subjectTerm;
            this.relationship = relationship;
            this.objectTerm = objectTerm;
            this.nestedSubject = null;
            this.nestedRelationship = null;
            this.nestedObject = null;
            this.hash = computeHash();
        }

        /**
         * Creates a nested statement from a
         *
         * <pre>
         * subject term, relationship,
         * nested subject term, nested relationship, nested object term
         * </pre>
         *
         * @param subjectTerm the subject term index
         * @param relationship the relationship name
         * @param nestedSubject the nested subject term index
         * @param nestedRelationship the nested relationship name
         * @param nestedObject the nested object term index
         * @throws InvalidArgument Thrown if the {@code subjectTerm} is
         * {@code null}
         */
        public TableStatement(Integer subjectTerm, String relationship,
                Integer nestedSubject, String nestedRelationship,
                Integer nestedObject) {
            if (subjectTerm == null) {
                throw new InvalidArgument("subjectTerm", subjectTerm);
            }
            this.subjectTerm = subjectTerm;
            this.relationship = relationship;
            this.objectTerm = null;
            this.nestedSubject = nestedSubject;
            this.nestedRelationship = nestedRelationship;
            this.nestedObject = nestedObject;
            this.hash = computeHash();
        }

        /**
         * These objects should only be constructed by {@link ProtoNetwork}.
         */
        TableStatement() {
        }

        /**
         * Return the subject term id.
         *
         * @return {@link Integer}, the subject term id, which cannot be null
         */
        public Integer getSubjectTermId() {
            return subjectTerm;
        }

        /**
         * Return the relationship name.
         *
         * @return {@link String}, the relationship name, which can be null
         */
        public String getRelationshipName() {
            return relationship;
        }

        /**
         * Return the object term index.
         *
         * @return the {@link Integer object term index}
         */
        public Integer getObjectTermId() {
            return objectTerm;
        }

        /**
         * Return the nested subject term index.
         *
         * @return the {@link Integer nested subject term index}
         */
        public Integer getNestedSubject() {
            return nestedSubject;
        }

        /**
         * Return the {@link RelationshipType nested relationship}.
         *
         * @return the {@link Integer nested subject term index}
         */
        public String getNestedRelationship() {
            return nestedRelationship;
        }

        /**
         * Return the {@link Integer nested object term index}.
         *
         * @return the {@link Integer nested object term index}
         */
        public Integer getNestedObject() {
            return nestedObject;
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
                    + ((subjectTerm == null) ? 0 : subjectTerm.hashCode());
            result = prime * result
                    + ((relationship == null) ? 0 : relationship.hashCode());
            result = prime * result
                    + ((objectTerm == null) ? 0 : objectTerm.hashCode());
            result = prime * result
                    + ((nestedSubject == null) ? 0 : nestedSubject.hashCode());
            result = prime
                    * result
                    + ((nestedRelationship == null) ? 0 : nestedRelationship
                            .hashCode());
            result = prime * result
                    + ((nestedObject == null) ? 0 : nestedObject.hashCode());
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
            TableStatement other = (TableStatement) obj;
            if (subjectTerm == null) {
                if (other.subjectTerm != null)
                    return false;
            } else if (!subjectTerm.equals(other.subjectTerm))
                return false;
            if (relationship == null) {
                if (other.relationship != null)
                    return false;
            } else if (!relationship.equals(other.relationship))
                return false;
            if (objectTerm == null) {
                if (other.objectTerm != null)
                    return false;
            } else if (!objectTerm.equals(other.objectTerm))
                return false;
            if (nestedSubject == null) {
                if (other.nestedSubject != null)
                    return false;
            } else if (!nestedSubject.equals(other.nestedSubject))
                return false;
            if (nestedRelationship == null) {
                if (other.nestedRelationship != null)
                    return false;
            } else if (!nestedRelationship.equals(other.nestedRelationship))
                return false;
            if (nestedObject == null) {
                if (other.nestedObject != null)
                    return false;
            } else if (!nestedObject.equals(other.nestedObject))
                return false;
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in) throws IOException,
                ClassNotFoundException {
            // subject term is guaranteed non-null
            subjectTerm = in.readInt();

            relationship = null;
            RelationshipType rel = readRelationship(in);
            if (rel != null) {
                relationship = rel.getDisplayValue();
            }

            objectTerm = readInteger(in);
            nestedSubject = readInteger(in);
            nestedRelationship = readString(in);
            nestedObject = readInteger(in);
            hash = computeHash();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _to(ObjectOutput out) throws IOException {
            // subject term is guaranteed non-null
            out.writeInt(subjectTerm);
            RelationshipType rel = RelationshipType.fromString(relationship);
            writeRelationship(out, rel);
            writeInteger(out, objectTerm);
            writeInteger(out, nestedSubject);
            out.writeObject(nestedRelationship);
            writeInteger(out, nestedObject);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void _from(ObjectInput in, ReadCache cache)
                throws IOException, ClassNotFoundException {
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
