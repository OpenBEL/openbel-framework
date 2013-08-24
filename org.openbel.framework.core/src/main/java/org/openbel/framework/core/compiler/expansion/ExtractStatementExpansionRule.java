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

import org.openbel.framework.common.model.Statement;

/**
 * For any terms A, B, and C,
 *
 * A -> (B -> C) implies
 *   A
 *   B -> C
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
class ExtractStatementExpansionRule extends ExpansionRule<Statement> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Extract nested statement expansion rule";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(Statement statement) {
        // returns true if
        // - statement is a nested statement
        // - statement is only nested one level
        Statement.Object object = statement.getObject();
        return statement.hasNestedStatement()
                && object.getStatement().getObject().getTerm() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Statement> expand(Statement statement) {
        List<Statement> statements = new ArrayList<Statement>();

        // A -> (B -> C) implies A
        Statement definitional = new Statement(statement.getSubject());
        attachExpansionRuleCitation(definitional);
        statements.add(definitional);

        // A -> (B -> C) implies B -> C
        Statement object = statement.getObject().getStatement();
        Statement extracted = new Statement(object.getSubject());
        extracted.setRelationshipType(object.getRelationshipType());
        extracted.setObject(new Statement.Object(object.getObject().getTerm()));
        attachExpansionRuleCitation(extracted);
        statements.add(extracted);

        return statements;
    }
}
