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
import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.util.ArrayList;
import java.util.List;

import org.openbel.bel.xbel.model.XBELAnnotationDefinitionGroup;
import org.openbel.bel.xbel.model.XBELDocument;
import org.openbel.bel.xbel.model.XBELExternalAnnotationDefinition;
import org.openbel.bel.xbel.model.XBELHeader;
import org.openbel.bel.xbel.model.XBELInternalAnnotationDefinition;
import org.openbel.bel.xbel.model.XBELNamespaceGroup;
import org.openbel.bel.xbel.model.XBELStatementGroup;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Header;
import org.openbel.framework.common.model.NamespaceGroup;
import org.openbel.framework.common.model.StatementGroup;

/**
 * Converter class for converting between {@link XBELDocument} and
 * {@link Document}.
 */
public final class DocumentConverter extends
        JAXBConverter<XBELDocument, Document> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Document convert(XBELDocument source) {
        if (source == null) return null;

        List<XBELStatementGroup> xstmtGroups = source.getStatementGroup();
        List<StatementGroup> stmtGroups =
                new ArrayList<StatementGroup>(xstmtGroups.size());
        StatementGroupConverter sgConverter = new StatementGroupConverter();
        for (final XBELStatementGroup xsg : xstmtGroups) {
            // Defer to StatementGroupConverter
            stmtGroups.add(sgConverter.convert(xsg));
        }

        XBELHeader header = source.getHeader();
        HeaderConverter hConverter = new HeaderConverter();
        // Defer to HeaderConverter
        Document dest = new Document(hConverter.convert(header), stmtGroups);

        if (source.isSetAnnotationDefinitionGroup()) {
            XBELAnnotationDefinitionGroup adGroup =
                    source.getAnnotationDefinitionGroup();
            List<XBELInternalAnnotationDefinition> internals =
                    adGroup.getInternalAnnotationDefinition();
            List<XBELExternalAnnotationDefinition> externals =
                    adGroup.getExternalAnnotationDefinition();
            int size = internals.size() + externals.size();
            List<AnnotationDefinition> l2 = sizedArrayList(size);

            // Defer to converters
            InternalAnnotationDefinitionConverter iConverter =
                    new InternalAnnotationDefinitionConverter();
            for (final XBELInternalAnnotationDefinition iad : internals) {
                l2.add(iConverter.convert(iad));
            }
            ExternalAnnotationDefinitionConverter eConverter =
                    new ExternalAnnotationDefinitionConverter();
            for (final XBELExternalAnnotationDefinition ead : externals) {
                l2.add(eConverter.convert(ead));
            }
            dest.setDefinitions(l2);
        }

        if (source.isSetNamespaceGroup()) {
            XBELNamespaceGroup namespaceGroup = source.getNamespaceGroup();
            NamespaceGroupConverter ngConverter = new NamespaceGroupConverter();
            // Defer to DocumentHeaderConverter
            dest.setNamespaceGroup(ngConverter.convert(namespaceGroup));
        }

        // Resolve any property references to instances
        dest.resolveReferences();

        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELDocument convert(Document source) {
        if (source == null) return null;

        XBELDocument xd = new XBELDocument();

        List<StatementGroup> stmtGroups = source.getStatementGroups();
        List<XBELStatementGroup> xstmtGroups = xd.getStatementGroup();
        StatementGroupConverter sgConverter = new StatementGroupConverter();
        for (final StatementGroup sg : stmtGroups) {
            // Defer to StatementGroupConverter
            xstmtGroups.add(sgConverter.convert(sg));
        }

        List<AnnotationDefinition> definitions = source.getDefinitions();
        if (hasItems(definitions)) {
            XBELAnnotationDefinitionGroup xadGroup =
                    new XBELAnnotationDefinitionGroup();
            List<XBELInternalAnnotationDefinition> internals =
                    xadGroup.getInternalAnnotationDefinition();
            List<XBELExternalAnnotationDefinition> externals =
                    xadGroup.getExternalAnnotationDefinition();

            InternalAnnotationDefinitionConverter iConverter =
                    new InternalAnnotationDefinitionConverter();
            ExternalAnnotationDefinitionConverter eConverter =
                    new ExternalAnnotationDefinitionConverter();

            for (final AnnotationDefinition ad : definitions) {

                XBELInternalAnnotationDefinition iad = iConverter.convert(ad);
                if (iad != null) {
                    internals.add(iad);
                    continue;
                }

                XBELExternalAnnotationDefinition ead = eConverter.convert(ad);
                if (ead != null) {
                    externals.add(ead);
                }
            }
            xd.setAnnotationDefinitionGroup(xadGroup);
        }

        Header header = source.getHeader();
        HeaderConverter hConverter = new HeaderConverter();
        // Defer to HeaderConverter
        xd.setHeader(hConverter.convert(header));

        NamespaceGroup nsGroup = source.getNamespaceGroup();
        if (nsGroup != null) {
            NamespaceGroupConverter ngConverter = new NamespaceGroupConverter();
            // Defer to NamespaceGroupConverter
            xd.setNamespaceGroup(ngConverter.convert(nsGroup));
        }

        return xd;
    }
}
