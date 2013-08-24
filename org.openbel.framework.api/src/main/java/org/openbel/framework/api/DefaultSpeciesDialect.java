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
package org.openbel.framework.api;

import static org.openbel.framework.common.BELUtilities.constrainedHashMap;
import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KAMStoreDaoImpl;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.internal.KAMStoreDaoImpl.TermParameter;

/**
 * {@link DefaultSpeciesDialect} defines a {@link SpeciesDialect}
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class DefaultSpeciesDialect implements SpeciesDialect {

    private final KamStore kamStore;
    private final Map<String, Namespace> nsmap;
    private final List<Namespace> speciesNs;
    private final Map<String, String> labelCache;
    private final boolean displayLongForm;

    public DefaultSpeciesDialect(final KamInfo info, final KamStore kamStore,
            final int taxId, final boolean displayLongForm) {
        this.kamStore = kamStore;
        this.labelCache = new HashMap<String, String>();
        try {
            Namespaces ns = Namespaces.loadNamespaces();

            List<KAMStoreDaoImpl.Namespace> nsl = kamStore.getNamespaces(info);
            nsmap = constrainedHashMap(nsl.size());
            for (final KAMStoreDaoImpl.Namespace n : nsl) {
                nsmap.put(n.getPrefix(),
                        new Namespace(n.getPrefix(), n.getResourceLocation()));
            }

            speciesNs = ns.getSpeciesNamespaces(taxId);
        } catch (Exception e) {
            throw new IllegalStateException("Failure to load namespaces.", e);
        }

        this.displayLongForm = displayLongForm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel(KamNode kamNode, TermParameter speciesParam) {
        final String nodeLabel = kamNode.getLabel();

        // return converted label stored in cache
        String cached = labelCache.get(nodeLabel);
        if (cached != null) {
            return cached;
        }

        final Parameter param = convert(speciesParam);

        try {
            // find first term and convert to species namespaces
            final List<BelTerm> terms = kamStore
                    .getSupportingTerms(kamNode);
            if (hasItems(terms)) {
                final BelTerm term = terms.get(0);
                final Term ts = BELParser.parseTerm(term.getLabel());
                final Term converted = convert(ts, param);

                cached = displayLongForm ? converted.toBELLongForm()
                        : converted.toBELShortForm();
                labelCache.put(nodeLabel, cached);
                return cached;
            }

            // if there are no supporting terms, return node label,
            // should never be true
            return nodeLabel;
        } catch (KamStoreException e) {
            // error returns original label
            return nodeLabel;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Namespace> getSpeciesNamespaces() {
        return speciesNs;
    }

    /**
     * Convert a {@link Term} to preferred namespaces
     *
     * @param orig
     * @return
     */
    protected Term convert(Term orig, Parameter speciesParam) {
        Term t = new Term(orig.getFunctionEnum());
        for (BELObject o : orig.getFunctionArguments()) {
            t.addFunctionArgument(convert(o, speciesParam));
        }
        return t;
    }

    /**
     * Convert a {@link BELObject}. Currently handles only {@link Term}s and
     * {@link Parameter}s as these are the only objects supported by Term.
     *
     * @param o
     * @param speciesParam
     * @return
     * @see Term#addFunctionArgument(BELObject)
     */
    protected BELObject convert(BELObject o, Parameter speciesParam) {
        Class<?> clazz = o.getClass();
        if (Term.class.isAssignableFrom(clazz)) {
            return convert((Term) o, speciesParam);
        } else if (Parameter.class.isAssignableFrom(clazz)) {
            return speciesParam;
        } else {
            throw new UnsupportedOperationException("BEL object type "
                    + o.getClass() + " is not supported");
        }
    }

    private Parameter convert(KAMStoreDaoImpl.TermParameter tp) {
        final KAMStoreDaoImpl.Namespace kns = tp.getNamespace();
        final Namespace ns =
                new Namespace(kns.getPrefix(), kns.getResourceLocation());
        return new Parameter(ns, tp.getParameterValue());
    }
}
