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
