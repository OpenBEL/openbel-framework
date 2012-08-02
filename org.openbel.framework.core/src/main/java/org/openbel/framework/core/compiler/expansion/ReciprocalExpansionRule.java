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
