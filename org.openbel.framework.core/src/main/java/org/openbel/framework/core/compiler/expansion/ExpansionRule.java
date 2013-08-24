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
