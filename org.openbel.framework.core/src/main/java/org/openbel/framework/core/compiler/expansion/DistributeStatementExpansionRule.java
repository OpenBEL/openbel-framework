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
class DistributeStatementExpansionRule extends ExpansionRule<Statement> {

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
