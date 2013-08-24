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

import static java.util.Arrays.asList;

import java.util.List;

import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.model.Statement.Object;

/**
 * Reciprocal expansion rule.
 * <p>
 * <ol>
 * <li>A analogous B implies B analogous A</li>
 * <li>A -- B implies B -- A</li>
 * <li>A negativeCorrelation B implies B negativeCorrelation A</li>
 * <li>A orthologous B implies B orthologous A</li>
 * <li>A positiveCorrelation B implies B positiveCorrelation A</li>
 * </ol>
 * </p>
 */
class ReciprocalExpansionRule extends ExpansionRule<Statement> {

    /**
     * Returns {@code true} if the statement's {@link Statement#getObject()
     * object} is non-null and its {@link Statement#getRelationshipType()
     * relationship} does not match {@link RelationshipType#isDirected()}.
     */
    @Override
    public boolean match(Statement s) {
        if (s.getObject() == null) {
            return false;
        }
        if (s.getObject().getTerm() == null) {
            return false;
        }
        final RelationshipType r = s.getRelationshipType();
        if (!r.isDirected()) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Statement> expand(Statement s) {
        Term sub = s.getSubject();
        Term obj = s.getObject().getTerm();
        final RelationshipType rel = s.getRelationshipType();

        final Statement stmt = new Statement(obj);
        stmt.setObject(new Object(sub));
        stmt.setRelationshipType(rel);

        return asList(new Statement[] { stmt });
    }

    /**
     * Returns {@code "Reciprocal relationship expansion rule"}.
     */
    @Override
    public String getName() {
        return "Reciprocal relationship expansion rule";
    }
}
