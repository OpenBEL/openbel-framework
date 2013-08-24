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
package org.openbel.framework.common.bel.converters;

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openbel.bel.model.BELStatement;
import org.openbel.bel.model.BELStatementGroup;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.StatementGroup;

public class BELStatementGroupConverter extends
        BELConverter<BELStatementGroup, StatementGroup> {
    private final Map<String, Namespace> ndefs;
    private final Map<String, AnnotationDefinition> adefs;

    public BELStatementGroupConverter(Map<String, Namespace> ndefs,
            Map<String, AnnotationDefinition> adefs) {
        this.ndefs = ndefs;
        this.adefs = adefs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatementGroup convert(BELStatementGroup bsg) {
        if (bsg == null) {
            return null;
        }

        final StatementGroup sg = new StatementGroup();

        if (bsg.getName() != null) {
            sg.setName(bsg.getName());
        }

        final List<Statement> statements = new ArrayList<Statement>();
        final BELStatementConverter bsc =
                new BELStatementConverter(ndefs, adefs);
        if (hasItems(bsg.getStatements())) {
            for (BELStatement statement : bsg.getStatements()) {
                statements.add(bsc.convert(statement));
            }

            sg.setStatements(statements);
        }

        List<StatementGroup> childStatementGroups =
                new ArrayList<StatementGroup>();
        if (hasItems(bsg.getChildStatementGroups())) {
            for (BELStatementGroup childGroup : bsg.getChildStatementGroups()) {
                childStatementGroups.add(convert(childGroup));
            }

            sg.setStatementGroups(childStatementGroups);
        }

        return sg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELStatementGroup convert(StatementGroup sg) {
        if (sg == null) {
            return null;
        }

        return null;
    }
}
