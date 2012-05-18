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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.RelationshipType;

/**
 * A statement is a specific assertion of a fact in some context: it is
 * implicitly based on some source of knowledge, implicitly true in at least one
 * specific situation.
 * <p>
 * BEL statements primarily represent relationships between {@link Term terms}.
 * As such, statements are term-iterable:
 * <pre>
 * <code>
 * for (final Term term : statement) {
 *     // ...
 * }
 * </code>
 * </pre>
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Statement implements BELObject, Iterable<Term> {
    private static final long serialVersionUID = -4295358543715077939L;

    private String comment;
    private AnnotationGroup annotationGroup;
    private final Term subject;
    private Object object;
    private RelationshipType relationshipType;

    /**
     * Creates a statement with the required subject property.
     *
     * @param subject Subject
     * @throws InvalidArgument Thrown if {@code subject} is null
     */
    public Statement(Term subject) {
        if (subject == null) throw new InvalidArgument("subject is null");
        this.subject = subject;
    }

    /**
     * Creates a statement with the required subject and optional properties.
     *
     * @param subject Subject
     * @param comment Comment
     * @param ag Annotation group
     * @param object Object
     * @param r Relationship
     * @throws InvalidArgument Thrown if {@code subject} is null
     */
    public Statement(Term subject, String comment, AnnotationGroup ag,
            Object object, RelationshipType r) {
        if (subject == null) throw new InvalidArgument("subject is null");
        this.subject = subject;
        this.comment = comment;
        this.annotationGroup = ag;
        this.object = object;
        this.relationshipType = r;
    }

    /**
     * Returns the statement's comment.
     *
     * @return String, which may be null
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the statement's comment.
     *
     * @param comment Statement's comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the statement's relationship.
     *
     * @return Relationship, which may be null
     */
    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    /**
     * Sets the statement's relationship type.
     *
     * @param relationshipType Statement's relationship type
     */
    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
     * Returns the statement's annotation group.
     *
     * @return AnnotationGroup, which may be null
     */
    public AnnotationGroup getAnnotationGroup() {
        return annotationGroup;
    }

    /**
     * Sets the statement's annotation group.
     *
     * @param annotationGroup Annotation group
     */
    public void setAnnotationGroup(AnnotationGroup annotationGroup) {
        this.annotationGroup = annotationGroup;
    }

    /**
     * Returns the statement's object.
     *
     * @return Object, which may be null
     */
    public Object getObject() {
        return object;
    }

    /**
     * Sets the statement's object.
     *
     * @param object Object
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * Returns the statement's subject.
     *
     * @return Non-null term
     */
    public Term getSubject() {
        return subject;
    }

    /**
     * Returns a list of all parameters contained by this statement and object.
     *
     * @return List of parameters
     */
    public List<Parameter> getAllParameters() {
        List<Parameter> ret = new ArrayList<Parameter>();

        ret.addAll(subject.getAllParameters());

        if (object != null) {
            if (object.getStatement() != null)
                ret.addAll(object.getStatement()
                        .getAllParameters());
            else
                ret.addAll(object.getTerm().getAllParameters());
        }

        return ret;
    }

    /**
     * Returns a list of all terms contained by this statement's subject and
     * object.
     *
     * @return List of terms
     */
    public List<Term> getAllTerms() {
        List<Term> ret = new ArrayList<Term>();
        ret.add(subject);

        List<Term> subjectTerms = subject.getTerms();
        if (subjectTerms != null) {
            ret.addAll(subjectTerms);
            for (final Term term : subjectTerms) {
                ret.addAll(term.getAllTerms());
            }
        }

        if (object != null) {
            ret.addAll(object.getAllTerms());
        }
        return ret;
    }

    /**
     * Returns an {@link Iterator iterator} over the document's {@link Term
     * terms}.
     * <p>
     * The following code is guaranteed to be safe (no
     * {@link NullPointerException null pointer exceptions} will be thrown):
     *
     * <pre>
     * <code>
     * for (final Term term : statement) {
     * }
     * </code>
     * </pre>
     *
     * </p>
     *
     * @return {@link Iterator} of {@link Term terms}
     */
    @Override
    public Iterator<Term> iterator() {
        return getAllTerms().iterator();
    }

    /**
     * Returns {@code true} if this statement only contains a
     * {@link Term subject term}, {@code false} otherwise.
     *
     * <p>
     * "In its simplest form, a BEL Statement can be used to establish that a
     * BEL Term has been observed in the context of the BEL Statement. A
     * typical example of such a statement would be one that contains a
     * molecular complex term. Such a BEL Statement would assert that the
     * complex has been observed."
     * </p>
     *
     * <p>
     * For further reference, see <i>BEL Language Overview</i>, BEL Statements
     * (BEL Overview).
     * </p>
     *
     * @see Statement#hasNestedStatement()
     * @see Statement#isStatementTriple()
     * @return {@code true} if this statement only contains a
     * {@link Term subject term}, {@code false} otherwise
     */
    public boolean isSubjectOnly() {
        return object == null;
    }

    /**
     * Returns {@code true} if this statement is a triple consisting of
     * <p>
     * <tt>TERM RELATIONSHIP TERM</tt>
     * </p>
     *
     * <p>
     * Otherwise return {@code false}.
     * </p>
     *
     * <p>
     * <blockquote>
     * "Most BEL Statements represent relationships between one BEL Term and
     * another BEL Term or BEL Statement. This type of BEL Statement encodes a
     * semantic triple (subject, relationship type, object), which represents
     * an assertion of a relationship between the subject and object."
     * </blockquote>
     * </p>
     *
     * <p>
     * For further reference, see <i>BEL Language Overview</i>, BEL Statements
     * (BEL Overview).
     * </p>
     *
     * @see Statement#hasNestedStatement()
     * @see Statement#isSubjectOnly()
     * @return {@code true} if this statement is a term-rel-term triple,
     * {@code false} otherwise
     */
    public boolean isStatementTriple() {
        if (object != null && object.getTerm() != null) {
            return true;
        }
        return false;
    }

    /**
     * Returns {@code true} if this statement contains a nested statement of
     * the form:
     *
     * <p>
     * <tt>TERM RELATIONSHIP STATEMENT</tt>
     * </p>
     *
     * <p>
     * <blockquote>"If the object of a BEL statement is another BEL statement,
     * the BEL statement is said to be nested and the relationship type is
     * constrained to the set of causal relationship types."</blockquote>
     * </p>
     *
     * <p>
     * For further reference, see <i>BEL Language Overview</i>, BEL Statements
     * (BEL Overview).
     * </p>
     *
     * @see Statement#isStatementTriple()
     * @see Statement#isSubjectOnly()
     * @return {@code true} if this statement contains a nested statement,
     * {@code false} otherwise
     */
    public boolean hasNestedStatement() {
        if (object != null && object.getStatement() != null) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Statement [");

        if (comment != null) {
            builder.append("comment=");
            builder.append(comment);
            builder.append(", ");
        }

        if (annotationGroup != null) {
            builder.append("annotationGroup=");
            builder.append(annotationGroup);
            builder.append(", ");
        }

        // subject is non-null by contract
        builder.append("subject=");
        builder.append(subject);
        builder.append(", ");

        if (object != null) {
            builder.append("object=");
            builder.append(object);
            builder.append(", ");
        }

        if (relationshipType != null) {
            builder.append("relationshipType=");
            builder.append(relationshipType);
            builder.append(", ");
        }

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
        if (comment != null) result += comment.hashCode();

        result *= prime;
        if (annotationGroup != null) result += annotationGroup.hashCode();

        result *= prime;
        // subject is non-null by contract
        result += subject.hashCode();

        result *= prime;
        if (object != null) result += object.hashCode();

        result *= prime;
        if (relationshipType != null) result += relationshipType.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (!(o instanceof Statement)) return false;

        final Statement s = (Statement) o;

        if (comment == null) {
            if (s.comment != null) return false;
        } else if (!comment.equals(s.comment)) return false;

        if (annotationGroup == null) {
            if (s.annotationGroup != null) return false;
        } else if (!annotationGroup.equals(s.annotationGroup)) return false;

        // subject is non-null by contract
        if (!subject.equals(s.subject)) return false;

        if (object == null) {
            if (s.object != null) return false;
        } else if (!object.equals(s.object)) return false;

        if (relationshipType != s.relationshipType) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toBELLongForm() {
        return toBEL(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toBELShortForm() {
        return toBEL(true);
    }

    private String toBEL(boolean shortForm) {
        final StringBuilder sb = new StringBuilder();
        if (shortForm) {
            sb.append(subject.toBELShortForm());
        } else {
            sb.append(subject.toBELLongForm());
        }

        if (object != null) {
            sb.append(" ");
            if (shortForm) {
                String abbr = relationshipType.getAbbreviation();
                sb.append(abbr == null ? relationshipType.getDisplayValue()
                        : abbr);
            } else {
                sb.append(relationshipType.getDisplayValue());
            }

            sb.append(" ");

            if (object.getStatement() != null) {
                sb.append("(");
                if (shortForm) {
                    sb.append(object.getStatement().toBELShortForm());
                } else {
                    sb.append(object.getStatement().toBELLongForm());
                }
                sb.append(")");
            } else {
                if (shortForm) {
                    sb.append(object.getTerm().toBELShortForm());
                } else {
                    sb.append(object.getTerm().toBELLongForm());
                }
            }
        }

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement clone() {
        Term subject2 = subject.clone();
        Object object2 = null;
        if (object != null) {
            object2 = object.clone();
        }

        AnnotationGroup annotationGroup2 = null;
        if (annotationGroup != null) {
            annotationGroup2 = annotationGroup.clone();
        }

        return new Statement(subject2, comment, annotationGroup2, object2,
                relationshipType);
    }

    /**
     * Defines the object of a statement.
     */
    public static class Object implements BELObject {
        private static final long serialVersionUID = 7947363393651852678L;

        private Term term;
        private Statement statement;

        /**
         * Creates an object.
         * <p>
         * Objects require either a term or a statement.
         * </p>
         *
         * @param t Term
         * @throws InvalidArgument Thrown if {@code t} is null
         */
        public Object(Term t) {
            if (t == null) throw new InvalidArgument("term is null");
            this.term = t;
        }

        /**
         * Creates an object.
         * <p>
         * Objects require either a term or a statement.
         * </p>
         *
         * @param s Statement
         * @throws InvalidArgument Thrown if {@code statement} is null
         */
        public Object(Statement s) {
            if (s == null) throw new InvalidArgument("statement is null");
            this.statement = s;
        }

        /**
         * Returns the term of the object.
         *
         * @return Term, may be null, in which case, statement is non-null
         */
        public Term getTerm() {
            return term;
        }

        /**
         * Returns the statement of the object.
         *
         * @return Statement, may be null, in which case, term is non-null
         */
        public Statement getStatement() {
            return statement;
        }

        /**
         * Returns this object's terms or all the terms contained by the nested
         * statement.
         *
         * @return List of terms
         */
        public List<Term> getAllTerms() {
            if (term != null) {
                final List<Term> ret = new ArrayList<Term>(1);
                ret.add(term);
                ret.addAll(term.getAllTerms());
                return ret;
            }
            return statement.getAllTerms();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;

            result *= prime;
            if (statement == null)
                result += term.hashCode();

            result *= prime;
            if (term == null)
                result += statement.hashCode();

            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(java.lang.Object o) {
            if (this == o) return true;
            if (!(o instanceof Object)) return false;

            final Object o2 = (Object) o;

            if (statement == null) {
                if (o2.statement != null) return false;
            } else if (!statement.equals(o2.statement)) return false;

            if (term == null) {
                if (o2.term != null) return false;
            } else if (!term.equals(o2.term)) return false;

            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Object [");

            if (term != null) {
                builder.append("term=");
                builder.append(term);
                builder.append(", ");
            }

            if (statement != null) {
                builder.append("statement=");
                builder.append(statement);
            }

            builder.append("]");
            return builder.toString();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toBELLongForm() {
            return term != null
                    ? term.toBELLongForm()
                    : statement.toBELLongForm();
        }

        @Override
        public String toBELShortForm() {
            return term != null
                    ? term.toBELShortForm()
                    : statement.toBELShortForm();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object clone() {
            if (term != null)
                return new Object(term.clone());
            return new Object(statement.clone());
        }
    }
}
