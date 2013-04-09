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
