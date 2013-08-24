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
package org.openbel.framework.ws.dialect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.Equivalencer;
import org.openbel.framework.api.EquivalencerException;
import org.openbel.framework.api.Kam;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.KamStore;
import org.openbel.framework.api.KamStoreException;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelTerm;

/**
 * {@link Dialect} implementation that allows control over the {@link Namespace}
 * s used in conversion. An ordered list of {@link Namespace}s can be assigned
 * to each of the {@link NamespaceDomain}s; labeling for each {@link Parameter}
 * in a supporting term will use these priorities to determine a String value.
 * If a value cannot be found for a {@link Parameter} in the desired namespace,
 * the next namespace in the list will be attempted. When all preferences are
 * exhausted the label will default to the default label of the term.<br>
 * This implementation also allows definition of the {@link BELSyntax} to be used
 * and whether or not to strip the namespace prefixes from the returned labels.<br>
 * <br>
 * Construction of this class is limited to
 * {@link DialectFactory#createCustomDialect(org.openbel.framework.core.kamstore.data.jdbc.KAMCatalogDao.KamInfo, List, List, List, BELSyntax, boolean)}
 * <br>
 * This implementation caches labels for nodes and is thus suitable only for a
 * single {@link Kam}. Usage across {@link Kam}s is not guarded against but will
 * cause unpredictable results.<br>
 * This implementation is thread-safe.
 *
 * @author Steve Ungerer
 */
public class CustomDialect implements Dialect {
    private final KamStore kamStore;
    private final Equivalencer equivalencer = new Equivalencer();

    private boolean removeNamespacePrefix = false;
    private boolean displayLongForm = false;
    private List<Namespace> geneNamespaces = new ArrayList<Namespace>(0);
    private List<Namespace> bpNamespaces = new ArrayList<Namespace>(0);
    private List<Namespace> chemNamespaces = new ArrayList<Namespace>(0);

    private Map<String, String> labelCache =
            new ConcurrentHashMap<String, String>();

    private Map<String, Namespace> kamNamespaces; // prefix : namespace
    private Map<String, NamespaceDomain> nsDomains; // prefix : domain

    private boolean initialized;
    private int hashCode;

    /**
     * Construct a new instance of a {@link CustomDialect}.
     * Post-construction the appropriate mutation must occur with
     * {@link #initialize()} being invoked prior to usage.
     *
     * @param kamStore
     * @throws KamStoreException
     */
    CustomDialect(KamStore kamStore) throws KamStoreException {
        this.kamStore = kamStore;
    }

    /**
     * Set the {@link Namespace} priority for {@link NamespaceDomain#Gene}
     * namespaces.
     *
     * @param geneNamespaces
     */
    void setGeneNamespaces(List<Namespace> geneNamespaces) {
        if (initialized) {
            throw new IllegalStateException(
                    "Dialect already initialized; mutation not allowed");
        }
        this.geneNamespaces = geneNamespaces;
    }

    /**
     * Set the {@link Namespace} priority for
     * {@link NamespaceDomain#BiologicalProcess} namespaces.
     *
     * @param bpNamespaces
     */
    void setBpNamespaces(List<Namespace> bpNamespaces) {
        if (initialized) {
            throw new IllegalStateException(
                    "Dialect already initialized; mutation not allowed");
        }
        this.bpNamespaces = bpNamespaces;
    }

    /**
     * Set the {@link Namespace} priority for {@link NamespaceDomain#Chemical}
     * namespaces.
     *
     * @param chemNamespaces
     */
    void setChemNamespaces(List<Namespace> chemNamespaces) {
        if (initialized) {
            throw new IllegalStateException(
                    "Dialect already initialized; mutation not allowed");
        }
        this.chemNamespaces = chemNamespaces;
    }

    /**
     * Set the {@link Namespace} {@link Map} used in finding resource locations
     * for a namespace prefix.<br>
     * Map key: the namespace prefix used in the {@link Kam}<br>
     * Map value: resource location of the namespace.
     *
     * @param kamNamespaces
     */
    void setKamNamespaces(Map<String, Namespace> kamNamespaces) {
        if (initialized) {
            throw new IllegalStateException(
                    "Dialect already initialized; mutation not allowed");
        }
        this.kamNamespaces = kamNamespaces;
    }

    /**
     * Set the {@link NamespaceDomain} {@link Map} used to determine the domain
     * of a particular {@link Namespace}.<br>
     * Map key: the namespace prefix used in the {@link Kam}<br>
     * Map value: {@link NamespaceDomain} of the namespace.
     *
     * @param nsDomains
     */
    void setNsDomains(Map<String, NamespaceDomain> nsDomains) {
        if (initialized) {
            throw new IllegalStateException(
                    "Dialect already initialized; mutation not allowed");
        }
        this.nsDomains = nsDomains;
    }

    /**
     * Set the dialect to display labels in BEL long form
     *
     * @param displayLongForm
     */
    void setDisplayLongForm(boolean displayLongForm) {
        if (initialized) {
            throw new IllegalStateException(
                    "Dialect already initialized; mutation not allowed");
        }
        this.displayLongForm = displayLongForm;
    }

    /**
     * Set the dialect to strip namespace prefixes from labels.<br>
     * Note that prefixes will remain in place if none of the user-configured
     * namespaces can be used to display the label and the default label is
     * used.
     *
     * @param removeNamespacePrefix
     */
    void setRemoveNamespacePrefix(boolean removeNamespacePrefix) {
        if (initialized) {
            throw new IllegalStateException(
                    "Dialect already initialized; mutation not allowed");
        }
        this.removeNamespacePrefix = removeNamespacePrefix;
    }

    /**
     * Initialize the dialect. Must be invoked post-mutation.
     */
    void initialize() {
        if (initialized) {
            throw new IllegalStateException(
                    "Dialect already initialized; re-initialization not allowed");
        }
        this.hashCode = new HashCodeBuilder().append(geneNamespaces)
                .append(bpNamespaces).append(chemNamespaces)
                .append(displayLongForm).append(removeNamespacePrefix)
                .toHashCode();
        this.initialized = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel(KamNode kamNode) {
        if (!initialized) {
            throw new IllegalStateException("Dialect not initialized");
        }
        String label = labelCache.get(kamNode.getLabel());
        if (label == null) {
            try {
                label = labelNode(kamNode);
            } catch (Exception e) {
                // TODO exception
            }
            labelCache.put(kamNode.getLabel(), label);
        }
        return label;
    }

    /**
     * Internal method to obtain a label for a node. Grabs the first
     * {@link BelTerm} that supports the node, parses it, and converts it to the
     * preferred namespaces to obtain a label.
     *
     * @param node
     * @return
     * @throws KamStoreException
     */
    protected String labelNode(KamNode node) throws KamStoreException {
        List<BelTerm> terms = kamStore.getSupportingTerms(node);
        if (!terms.isEmpty()) {
            BelTerm bt = terms.get(0);
            Term parsed;
            try {
                parsed = BELParser.parseTerm(bt.getLabel());
            } catch (Exception e) {
                return null;
            }
            Term converted = convert(parsed);

            return displayLongForm ? converted.toBELLongForm() : converted
                    .toBELShortForm();
        }
        return null;
    }

    /**
     * Convert a {@link Term} to preferred namespaces
     *
     * @param orig
     * @return
     */
    protected Term convert(Term orig) {
        Term t = new Term(orig.getFunctionEnum());
        for (BELObject o : orig.getFunctionArguments()) {
            t.addFunctionArgument(convert(o));
        }
        return t;
    }

    /**
     * Convert a {@link BELObject}. Currently handles only {@link Term}s and
     * {@link Parameter}s as these are the only objects supported by Term.
     *
     * @param o
     * @return
     * @see Term#addFunctionArgument(BELObject)
     */
    protected BELObject convert(BELObject o) {
        Class<?> clazz = o.getClass();
        if (Term.class.isAssignableFrom(clazz)) {
            return convert((Term) o);
        } else if (Parameter.class.isAssignableFrom(clazz)) {
            return convert((Parameter) o);
        } else {
            throw new UnsupportedOperationException("BEL object type "
                    + o.getClass() + " is not supported");
        }
    }

    /**
     * Convert a parameter to the preferred namespaces.
     *
     * @param orig
     * @return the converted {@link Parameter} or the original parameter if no
     *         conversion was possible
     */
    protected Parameter convert(Parameter orig) {
        if (orig.getNamespace() == null) {
            return orig;
        }
        // determine domain of parameter namespace
        NamespaceDomain domain = nsDomains.get(orig.getNamespace().getPrefix());
        if (domain == null) {
            return orig;
        }

        // iterate appropriate collection to find equiv
        List<Namespace> coll;
        switch (domain) {
        case BiologicalProcess:
            coll = bpNamespaces;
            break;
        case Chemical:
            coll = chemNamespaces;
            break;
        case Gene:
            coll = geneNamespaces;
            break;
        default:
            throw new UnsupportedOperationException("Unknown domain: " + domain);
        }

        try {
            SkinnyUUID uuid = equivalencer.getUUID(
                    getNamespace(orig.getNamespace()), orig.getValue());
            if (uuid == null) {
                // no equivalents anywhere
                return orig;
            }
            // find first equivalent in list of desired
            for (Namespace ns : coll) {
                String v = equivalencer.equivalence(uuid, ns);
                if (v != null) {
                    return removeNamespacePrefix ? new Parameter(null, v)
                            : new Parameter(ns, v);
                }
            }
        } catch (EquivalencerException e) {
            // TODO exception
            return null;
        }

        // if no equiv, use param as-is
        return orig;
    }

    /**
     * Get a fully populated namespace object with valid resource location.
     * (namespaces parsed by BELParser will not have a valid resource location)
     *
     * @param ns
     * @return
     */
    protected Namespace getNamespace(Namespace ns) {
        return kamNamespaces.get(ns.getPrefix());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CustomDialect)) {
            return false;
        }
        CustomDialect rhs = (CustomDialect) obj;
        return new EqualsBuilder().append(geneNamespaces, rhs.geneNamespaces)
                .append(bpNamespaces, rhs.bpNamespaces)
                .append(chemNamespaces, rhs.chemNamespaces)
                .append(displayLongForm, rhs.displayLongForm)
                .append(removeNamespacePrefix, rhs.removeNamespacePrefix)
                .isEquals();
    }

}
