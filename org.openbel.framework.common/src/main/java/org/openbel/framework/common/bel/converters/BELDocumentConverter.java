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
        if (d == null) {
            return null;
        }

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
