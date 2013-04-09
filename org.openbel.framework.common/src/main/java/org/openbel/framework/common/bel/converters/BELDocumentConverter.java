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

import static java.util.Collections.emptyMap;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.model.Namespace.DEFAULT_NAMESPACE_PREFIX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openbel.bel.model.BELAnnotationDefinition;
import org.openbel.bel.model.BELDocument;
import org.openbel.bel.model.BELNamespaceDefinition;
import org.openbel.bel.model.BELStatementGroup;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Header;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.NamespaceGroup;
import org.openbel.framework.common.model.StatementGroup;

public class BELDocumentConverter extends BELConverter<BELDocument, Document> {
    private final BELDocumentHeaderConverter dhc =
            new BELDocumentHeaderConverter();
    private final BELAnnotationDefinitionConverter badc =
            new BELAnnotationDefinitionConverter();
    private final BELNamespaceDefinitionConverter bndc =
            new BELNamespaceDefinitionConverter();

    /**
     * {@inheritDoc}
     */
    @Override
    public Document convert(BELDocument bd) {
        if (bd == null) {
            return null;
        }

        final Header header = dhc.convert(bd.getDocumentHeader());

        // handle annotation definitions
        Set<BELAnnotationDefinition> badlist = bd.getAnnotationDefinitions();
        List<AnnotationDefinition> adlist = null;
        if (badlist != null) {
            adlist = sizedArrayList(badlist.size());
            if (hasItems(badlist)) {
                for (BELAnnotationDefinition bad : badlist) {
                    adlist.add(badc.convert(bad));
                }
            }
        }

        // handle namespace definitions
        Set<BELNamespaceDefinition> bndlist = bd.getNamespaceDefinitions();
        NamespaceGroup nsgroup = null;
        if (bndlist != null) {
            nsgroup = new NamespaceGroup();
            if (hasItems(bndlist)) {
                List<Namespace> nslist = new ArrayList<Namespace>();
                for (BELNamespaceDefinition bnd : bndlist) {
                    // handle namespace default on the namespace group
                    if (bnd.isNsDefault()) {
                        nsgroup.setDefaultResourceLocation(bnd
                                .getResourceLocation());
                    } else {
                        nslist.add(bndc.convert(bnd));
                    }
                }
                nsgroup.setNamespaces(nslist);
            }
        }

        // handle statements
        final BELStatementGroupConverter sgc = new BELStatementGroupConverter(
                getNamespaceMap(nsgroup), getDefinitionMap(adlist));
        List<StatementGroup> statementGroups = new ArrayList<StatementGroup>();
        for (BELStatementGroup bsg : bd.getBelStatementGroups()) {
            statementGroups.add(sgc.convert(bsg));
        }

        return new Document(header, statementGroups, nsgroup, adlist);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELDocument convert(Document d) {
        return null;
    }

    private Map<String, Namespace>
            getNamespaceMap(final NamespaceGroup nsgroup) {
        final Map<String, Namespace> ret = new HashMap<String, Namespace>();
        if (nsgroup == null) {
            return ret;
        }

        final List<Namespace> nsl = nsgroup.getNamespaces();
        if (hasItems(nsl)) {
            for (final Namespace ns : nsl)
                ret.put(ns.getPrefix(), ns);
        }

        final String defns = nsgroup.getDefaultResourceLocation();
        if (defns != null) {
            Namespace ns = new Namespace(DEFAULT_NAMESPACE_PREFIX, defns);
            ret.put(DEFAULT_NAMESPACE_PREFIX, ns);
        }

        return ret;
    }

    public Map<String, AnnotationDefinition> getDefinitionMap(
            final List<AnnotationDefinition> definitions) {
        if (definitions == null) {
            return emptyMap();
        }
        final Map<String, AnnotationDefinition> ret =
                new HashMap<String, AnnotationDefinition>(definitions.size());
        for (final AnnotationDefinition ad : definitions) {
            ret.put(ad.getId(), ad);
        }
        return ret;
    }
}
