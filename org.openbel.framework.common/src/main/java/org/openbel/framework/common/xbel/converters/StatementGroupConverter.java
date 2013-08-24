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
package org.openbel.framework.common.xbel.converters;

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.ArrayList;
import java.util.List;

import org.openbel.bel.xbel.model.XBELAnnotationGroup;
import org.openbel.bel.xbel.model.XBELStatement;
import org.openbel.bel.xbel.model.XBELStatementGroup;
import org.openbel.framework.common.model.AnnotationGroup;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.StatementGroup;

/**
 * Converter class for converting between {@link XBELStatementGroup} and
 * {@link StatementGroup}.
 *
 */
public final class StatementGroupConverter extends
        JAXBConverter<XBELStatementGroup, StatementGroup> {

    /**
     * {@inheritDoc}
     */
    @Override
    public StatementGroup convert(XBELStatementGroup source) {
        if (source == null) return null;

        // Destination type
        StatementGroup dest = new StatementGroup();

        XBELAnnotationGroup annotationGroup = source.getAnnotationGroup();
        AnnotationGroupConverter agConverter = new AnnotationGroupConverter();
        // Defer to AnnotationGroupConverter
        AnnotationGroup ag = agConverter.convert(annotationGroup);
        dest.setAnnotationGroup(ag);

        String name = source.getName();
        dest.setName(name);

        String comment = source.getComment();
        dest.setComment(comment);

        List<XBELStatement> xstatements = source.getStatement();
        int size = xstatements.size();
        List<Statement> statements = new ArrayList<Statement>(size);
        StatementConverter sConverter = new StatementConverter();
        for (final XBELStatement xstmt : xstatements) {
            // Defer to StatementConverter
            statements.add(sConverter.convert(xstmt));
        }
        dest.setStatements(statements);

        List<XBELStatementGroup> xstmtGroup = source.getStatementGroup();
        size = xstmtGroup.size();
        List<StatementGroup> stmtGroup = new ArrayList<StatementGroup>(size);
        for (final XBELStatementGroup xsg : xstmtGroup) {
            stmtGroup.add(convert(xsg));
        }
        dest.setStatementGroups(stmtGroup);

        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELStatementGroup convert(StatementGroup source) {
        if (source == null) return null;

        XBELStatementGroup xsg = new XBELStatementGroup();

        AnnotationGroup ag = source.getAnnotationGroup();
        AnnotationGroupConverter agConverter = new AnnotationGroupConverter();
        // Defer to AnnotationGroupConverter
        XBELAnnotationGroup xag = agConverter.convert(ag);
        xsg.setAnnotationGroup(xag);

        String name = source.getName();
        xsg.setName(name);

        String comment = source.getComment();
        xsg.setComment(comment);

        List<Statement> statements = source.getStatements();
        StatementConverter sConverter = new StatementConverter();
        if (hasItems(statements)) {
            List<XBELStatement> xstmts = xsg.getStatement();
            for (final Statement stmt : statements) {
                // Defer to StatementConverter
                xstmts.add(sConverter.convert(stmt));
            }
        }

        List<StatementGroup> statementGroups = source.getStatementGroups();
        if (hasItems(statementGroups)) {
            List<XBELStatementGroup> xstmtgroup = xsg.getStatementGroup();
            for (final StatementGroup sg : statementGroups) {
                xstmtgroup.add(convert(sg));
            }
        }

        return xsg;
    }
}
