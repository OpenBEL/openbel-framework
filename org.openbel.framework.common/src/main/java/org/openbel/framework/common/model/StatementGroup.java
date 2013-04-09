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
package org.openbel.framework.common.model;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * A group of BEL statements.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class StatementGroup implements BELModelObject {
    private static final long serialVersionUID = -2304104840227984992L;

    private String name;
    private String comment;
    private AnnotationGroup annotationGroup;
    private List<Statement> statements = new ArrayList<Statement>();
    private List<StatementGroup> statementGroups =
            new ArrayList<StatementGroup>();

    /**
     * Creates a statement group with the optional properties.
     *
     * @param name {@link String}, the statement group name
     * @param comment Comment
     * @param ag Annotation group
     * @param statements Statements
     * @param statementGroups Statement groups
     */
    public StatementGroup(String name, String comment, AnnotationGroup ag,
            List<Statement> statements, List<StatementGroup> statementGroups) {
        this.name = name;
        this.comment = comment;
        this.annotationGroup = ag;
        this.statements = statements;
        this.statementGroups = statementGroups;
    }

    /**
     * Creates a statement group.
     */
    public StatementGroup() {
    }

    /**
     * Returns the statement group's name.
     *
     * @return {@link String} the statement group name, which may be null
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the statement group's name.
     *
     * @param name {@link String}, the statement group's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the statement group's comment.
     *
     * @return String, which may be null
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the statement group's comment.
     *
     * @param comment Statement group's comment
     */
    public void setComment(String comment) {
        this.comment = comment;
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
     * Sets the statement group's annotation group.
     *
     * @param annotationGroup Annotation group
     */
    public void setAnnotationGroup(AnnotationGroup annotationGroup) {
        this.annotationGroup = annotationGroup;
    }

    /**
     * Returns the {@link StatementGroup statement groups's}
     * {@link Statement statements}.
     *
     * @return List of {@link Statement statements}, which may be empty
     */
    public List<Statement> getStatements() {
        return statements;
    }

    /**
     * Sets the statement group's statements.
     *
     * @param statements List of statements
     */
    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    /**
     * Returns the nested {@link StatementGroup statement groups}
     * for this {@link StatementGroup statement group}.
     *
     * @return List of {@link StatementGroup statement groups}, which may be
     * empty
     */
    public List<StatementGroup> getStatementGroups() {
        return statementGroups;
    }

    /**
     * Sets statement group's statement groups.
     *
     * @param statementGroups List of statement groups
     */
    public void setStatementGroups(List<StatementGroup> statementGroups) {
        this.statementGroups = statementGroups;
    }

    /**
     * Returns a list of all annotations contained by this statement group, its
     * statements, and nested statement groups.
     *
     * @return List of annotations
     */
    public List<Annotation> getAllAnnotations() {
        List<Annotation> ret = new ArrayList<Annotation>();

        // Annotations within the annotation group
        if (annotationGroup != null)
            ret.addAll(annotationGroup.getAnnotations());

        // Annotations within the statements
        if (statements != null) {
            for (final Statement stmt : statements) {
                AnnotationGroup ag = stmt.getAnnotationGroup();
                if (ag != null && hasItems(ag.getAnnotations())) {
                    ret.addAll(ag.getAnnotations());
                }
            }
        }

        // Annotations within nested statement groups
        if (statementGroups != null) {
            for (final StatementGroup sg : statementGroups)
                ret.addAll(sg.getAllAnnotations());
        }

        return ret;
    }

    /**
     * Returns a list of all parameters contained by this statement group's
     * statements, and nested statement groups.
     *
     * @return List of parameters
     */
    public List<Parameter> getAllParameters() {
        List<Parameter> ret = new ArrayList<Parameter>();

        // Parameters within statements
        if (statements != null) {
            for (final Statement stmt : statements)
                ret.addAll(stmt.getAllParameters());
        }

        // Parameters within nested statement groups
        if (statementGroups != null) {
            for (final StatementGroup sg : statementGroups)
                ret.addAll(sg.getAllParameters());
        }

        return ret;
    }

    /**
     * Returns a list of all statement groups contained by this statement group
     * and nested statement groups.
     *
     * @return List of statement groups
     */
    public List<StatementGroup> getAllStatementGroups() {
        List<StatementGroup> ret = new ArrayList<StatementGroup>();

        if (statementGroups != null) {
            ret.addAll(statementGroups);
            for (final StatementGroup sg : statementGroups) {
                ret.addAll(sg.getAllStatementGroups());
            }
        }

        return ret;
    }

    /**
     * Returns a list of all statements contained by this statement group's
     * statements, and nested statement groups.
     *
     * @return List of statements
     */
    public List<Statement> getAllStatements() {
        List<Statement> ret = new ArrayList<Statement>();

        // Statements within this group
        if (statements != null) {
            ret.addAll(statements);
        }

        // Statements within nested statement groups
        if (statementGroups != null) {
            for (final StatementGroup sg : statementGroups)
                ret.addAll(sg.getAllStatements());
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

        result *= prime;
        if (name != null) result += name.hashCode();

        result *= prime;
        if (comment != null) result += comment.hashCode();

        result *= prime;
        if (annotationGroup != null) result += annotationGroup.hashCode();

        result *= prime;
        if (statements != null) result += statements.hashCode();

        result *= prime;
        if (statementGroups != null) result += statementGroups.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatementGroup)) return false;

        final StatementGroup sg = (StatementGroup) o;

        if (name == null) {
            if (sg.name != null) return false;
        } else if (!name.equals(sg.name)) return false;

        if (comment == null) {
            if (sg.comment != null) return false;
        } else if (!comment.equals(sg.comment)) return false;

        if (annotationGroup == null) {
            if (sg.annotationGroup != null) return false;
        } else if (!annotationGroup.equals(sg.annotationGroup)) return false;

        if (statements == null) {
            if (sg.statements != null) return false;
        } else if (!statements.equals(sg.statements)) return false;

        if (statementGroups == null) {
            if (sg.statementGroups != null) return false;
        } else if (!statementGroups.equals(sg.statementGroups)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("StatementGroup [");

        if (name != null) {
            builder.append("name=");
            builder.append(name);
            builder.append(", ");
        }

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

        if (statements != null) {
            builder.append("statements=");
            builder.append(statements);
            builder.append(", ");
        }

        if (statementGroups != null) {
            builder.append("statementGroups=");
            builder.append(statementGroups);
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatementGroup clone() {
        AnnotationGroup annotationGroup2 = null;
        if (annotationGroup != null)
            annotationGroup2 = annotationGroup.clone();

        List<Statement> statements2 = null;
        if (statements != null) {
            statements2 = sizedArrayList(statements.size());
            for (final Statement s : statements)
                statements2.add(s.clone());
        }

        List<StatementGroup> statementGroups2 = null;
        if (statementGroups != null) {
            statementGroups2 = sizedArrayList(statementGroups.size());
            for (final StatementGroup s : statementGroups)
                statementGroups2.add(s.clone());
        }

        return new StatementGroup(name, comment, annotationGroup2, statements2,
                statementGroups2);
    }
}
