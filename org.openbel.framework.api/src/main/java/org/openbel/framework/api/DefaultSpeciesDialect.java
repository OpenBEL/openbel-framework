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
package org.openbel.framework.api;

import static org.openbel.framework.common.BELUtilities.constrainedHashMap;
import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.internal.KAMStoreDaoImpl;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Term;

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
