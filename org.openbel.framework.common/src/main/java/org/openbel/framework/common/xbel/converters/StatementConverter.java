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
package org.openbel.framework.common.xbel.converters;

import static org.openbel.framework.common.enums.RelationshipType.fromString;

import org.openbel.bel.xbel.model.Relationship;
import org.openbel.bel.xbel.model.XBELAnnotationGroup;
import org.openbel.bel.xbel.model.XBELObject;
import org.openbel.bel.xbel.model.XBELStatement;
import org.openbel.bel.xbel.model.XBELSubject;
import org.openbel.bel.xbel.model.XBELTerm;
import org.openbel.framework.common.model.AnnotationGroup;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.model.Statement.Object;

/**
 * Converter class for converting between {@link XBELStatement} and
 * {@link Statement}.
 * 
 */
public final class StatementConverter extends
        JAXBConverter<XBELStatement, Statement> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement convert(XBELStatement source) {
        if (source == null) return null;

        XBELTerm subject = source.getSubject().getTerm();
        TermConverter tConverter = new TermConverter();
        // Defer to TermConverter for statement construction
        Statement dest = new Statement(tConverter.convert(subject));

        XBELAnnotationGroup annotationGroup = source.getAnnotationGroup();
        AnnotationGroupConverter agConverter = new AnnotationGroupConverter();
        // Defer to AnnotationGroupConverter
        dest.setAnnotationGroup(agConverter.convert(annotationGroup));

        String comment = source.getComment();
        dest.setComment(comment);

        // Set statement relationship, if present on source
        Relationship r = source.getRelationship();
        if (r != null)
            dest.setRelationshipType(fromString(r.value()));

        XBELObject object = source.getObject();
        if (object != null) {
            if (object.isSetStatement()) {
                XBELStatement statement = object.getStatement();
                StatementConverter sConverter = new StatementConverter();
                // Defer to StatementConverter
                dest.setObject(new Object(sConverter.convert(statement)));
            } else if (object.isSetTerm()) {
                XBELTerm term = object.getTerm();
                // Defer to TermConverter
                dest.setObject(new Object(tConverter.convert(term)));
            }
        }

        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELStatement convert(Statement source) {
        if (source == null) return null;

        XBELStatement xstmt = new XBELStatement();

        Term subject = source.getSubject();
        TermConverter tConverter = new TermConverter();
        XBELSubject xs = new XBELSubject();
        // Defer to TermConverter
        xs.setTerm(tConverter.convert(subject));
        xstmt.setSubject(xs);

        AnnotationGroup ag = source.getAnnotationGroup();
        AnnotationGroupConverter agConverter = new AnnotationGroupConverter();
        // Defer to AnnotationGroupConverter
        xstmt.setAnnotationGroup(agConverter.convert(ag));

        String comment = source.getComment();
        xstmt.setComment(comment);

        // Set statement relationship, if present on source
        org.openbel.framework.common.enums.RelationshipType r =
                source.getRelationshipType();
        if (r != null)
            xstmt.setRelationship(Relationship.fromValue(r.getDisplayValue()));

        Object object = source.getObject();
        if (object != null) {
            if (object.getStatement() != null) {
                XBELObject xo = new XBELObject();
                StatementConverter sConverter = new StatementConverter();
                // Defer to StatementConverter
                xo.setStatement(sConverter.convert(object.getStatement()));
                xstmt.setObject(xo);
            } else if (object.getTerm() != null) {
                XBELObject xo = new XBELObject();
                // Defer to TermConverter
                xo.setTerm(tConverter.convert(object.getTerm()));
                xstmt.setObject(xo);
            }
        }

        return xstmt;
    }
}
