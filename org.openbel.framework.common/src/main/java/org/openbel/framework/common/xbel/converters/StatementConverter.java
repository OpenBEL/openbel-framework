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
