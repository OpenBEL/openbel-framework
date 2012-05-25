package org.openbel.framework.api;

import java.util.List;

import org.openbel.framework.common.model.Namespace;

/**
 * TODO Document
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public interface SpeciesDialect extends Dialect {

    /**
     * TODO Document
     *
     * @return
     */
    public List<Namespace> getSpeciesNamespaces();
}
