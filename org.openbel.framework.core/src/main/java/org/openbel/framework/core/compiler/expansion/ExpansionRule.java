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

import static org.openbel.framework.common.model.CommonModelFactory.getInstance;

import java.util.List;

import org.openbel.framework.common.enums.CitationType;
import org.openbel.framework.common.model.AnnotationGroup;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Citation;
import org.openbel.framework.common.model.Statement;

/**
 * Defines a rule to expand some {@link BELObject} {@code <T>} into a number of
 * statements.
 */
public abstract class ExpansionRule<T extends BELObject> {

    /**
     * Returns the name of this expansion rule.
     * 
     * @return {@link String}
     */
    public abstract String getName();

    /**
     * Returns {@code true} if the {@link BELObject} {@code T} matches this
     * expansion rule, {@code false} otherwise.
     * 
     * @param t {@link BELObject} {@code <T>}
     * @return boolean
     */
    public abstract boolean match(T t);

    /**
     * Expands the {@link Statement statement's} knowledge.
     * 
     * @param t The {@link BELObject} {@code <T>} matching this expansion rule
     * @return {@link List} of the {@link Statement statements} produced as a
     * result of expanding the supplied {@code <T>}
     */
    public abstract List<Statement> expand(T t);

    /**
     * Attach citation for expansion rule
     * 
     * @param statement {@link Statement}
     */
    protected void attachExpansionRuleCitation(Statement statement) {
        AnnotationGroup ag = new AnnotationGroup();
        Citation citation = getInstance().createCitation(getName());
        citation.setType(CitationType.OTHER);
        citation.setReference(getName());
        ag.setCitation(citation);
        statement.setAnnotationGroup(ag);
    }

}
