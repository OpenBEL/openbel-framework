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
