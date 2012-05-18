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
package org.openbel.framework.core.compiler.expansion;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.Statement;

/**
 * For any terms A, B, and C,
 *
 * A -> (B -> C) implies B -> C, A -> C
 * A -| (B -| C) implies B -| C, A -> C
 * A -> (B -| C) implies B -| C, A -| C
 * A -| (B -> C) implies B -> C, A -| C
 */
class DistributeStatementExpansionRule extends StatementExpansionRule {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Distribute nested statement expansion rule";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(Statement statement) {
        // returns true if
        // - statement is a nested statement
        // - statement is a non-direct causal
        // - statement is only nested one level
        Statement.Object object = statement.getObject();
        return statement.hasNestedStatement() && isCausal(statement)
                && isCausal(object.getStatement())
                && object.getStatement().getObject().getTerm() != null;
    }

    /**
     * Determines if the {@link Statement} represents a causal relationship.
     *
     * @param statement the {@link Statement} to check for causal relationship
     * @return <tt>true</tt> if the {@link Statement} represents a causal
     * relationship, <tt>false</tt> if it does not
     *
     * @see RelationshipType#isCausal()
     */
    protected boolean isCausal(Statement statement) {
        boolean isCausal = false;
        if (statement.getRelationshipType() != null) {
            isCausal = statement.getRelationshipType().isCausal();
        }
        return isCausal;
    }

    /**
     * Resolves the relatinoship between A and C, given A parentRelationship (B
     * childRelationship C)
     *
     * @param parentRelationship {@link RelationshipType} the parent
     * relationship type
     * @param childRelationship {@link RelationshipType} the child
     * relationship type
     * @return {@link RelationshipType} the relationship type to use for the
     * inferred nested object term
     */
    protected RelationshipType resolveMediatedRelationship(
            RelationshipType parentRelationship,
            RelationshipType childRelationship) {
        if (isIncrease(parentRelationship) == isIncrease(childRelationship)) {
            // either increase of increase OR decrease or decrease, net increase
            // in either case
            return RelationshipType.INCREASES;
        }
        return RelationshipType.DECREASES;
    }

    /**
     * Determines if the {@link RelationshipType} represents an increasing
     * relationship.
     *
     * @param relationship the {@link RelationshipType}
     * @return <tt>true</tt> if the {@link RelationshipType} represents an
     * increasing relationship, <tt>false</tt> if it does not
     */
    protected boolean isIncrease(RelationshipType relationship) {
        return relationship == RelationshipType.INCREASES
                || relationship == RelationshipType.DIRECTLY_INCREASES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Statement> expand(Statement statement) {
        List<Statement> statements = new ArrayList<Statement>();

        // A -> (B -> C) implies B -> C
        Statement object = statement.getObject().getStatement();
        Statement s1 = new Statement(object.getSubject());
        s1.setRelationshipType(object.getRelationshipType());
        s1.setObject(new Statement.Object(object.getObject().getTerm()));
        attachExpansionRuleCitation(s1);
        statements.add(s1);

        // A -> (B -> C) implies A -> C
        Statement s2 = new Statement(statement.getSubject());
        s2.setRelationshipType(resolveMediatedRelationship(
                statement.getRelationshipType(), object.getRelationshipType()));
        s2.setObject(new Statement.Object(object.getObject().getTerm()));
        attachExpansionRuleCitation(s2);
        statements.add(s2);

        return statements;
    }
}
