package org.openbel.framework.api;

import static org.openbel.framework.common.BELUtilities.constrainedHashMap;
import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.RecognitionException;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelTerm;

public class DefaultSpeciesDialect implements SpeciesDialect {

    private final int speciesTaxId;
    private final KamStore kamStore;
    private final List<Namespace> namespaces;
    private final Map<String, Namespace> nsmap;
    private final Map<String, String> labelCache;
    private final Equivalencer equivalencer = new Equivalencer();

    public DefaultSpeciesDialect(final KamStore kamStore, final int taxId) {
        this.kamStore = kamStore;
        this.speciesTaxId = taxId;
        this.labelCache = new HashMap<String, String>();
        namespaces = getSpeciesNamespaces();
        nsmap = constrainedHashMap(namespaces.size());
        for (final Namespace n : namespaces) {
            nsmap.put(n.getResourceLocation(), n);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel(KamNode kamNode) {
        final String nodeLabel = kamNode.getLabel();

        // return converted label stored in cache
        String cached = labelCache.get(nodeLabel);
        if (cached != null) {
            return cached;
        }

        try {
            // find first term and convert to species namespaces
            final List<BelTerm> terms = kamStore.getSupportingTerms(kamNode);
            if (hasItems(terms)) {
                final BelTerm term = terms.get(0);
                final Term ts = BELParser.parseTerm(term.getLabel());
                final Term converted = convert(ts);
                cached = converted.toBELShortForm();
                labelCache.put(nodeLabel, cached);
                return cached;
            }

            // if there are no supporting terms, return node label,
            // should never be true
            return nodeLabel;
        } catch (KamStoreException e) {
            // error returns original label
            return nodeLabel;
        } catch (RecognitionException e) {
            // error returns original label
            return nodeLabel;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Namespace> getSpeciesNamespaces() {
        try {
            Namespaces ns = Namespaces.loadNamespaces();
            return ns.getSpeciesNamespaces(speciesTaxId);
        } catch (Exception e) {
            throw new IllegalStateException("Error loading namespaces.", e);
        }
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
        final Namespace origNs = orig.getNamespace();
        if (origNs == null) {
            return orig;
        }

        try {
            final Namespace ns = nsmap.get(origNs.getResourceLocation());
            final SkinnyUUID uuid = equivalencer.getUUID(ns, orig.getValue());
            if (uuid == null) {
                // no equivalents anywhere
                return orig;
            }
            // find first equivalent in list of desired
            for (Namespace n : namespaces) {
                String e = equivalencer.equivalence(uuid, n);
                if (e != null) {
                    return new Parameter(n, e);
                }
            }
        } catch (EquivalencerException e) {
            // TODO exception
            return null;
        }

        // if no equiv, use param as-is
        return orig;
    }
}
