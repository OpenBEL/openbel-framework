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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.KamStore;
import org.openbel.framework.api.KamStoreException;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.core.df.beldata.namespace.NamespaceHeader;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.ws.service.DialectCacheService;
import org.openbel.framework.ws.service.NamespaceResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to construct {@link Dialect}s.<br>
 * This service will always create new {@link Dialect} instances. As many
 * implementations benefit from caching, it is recommended that constructed
 * {@link Dialect}s be cached with the {@link DialectCacheService}.
 *
 * @author Steve Ungerer
 */
@Service
public class DialectFactoryImpl implements DialectFactory {

    @Autowired(required = true)
    private KamStore kamStore;

    @Autowired(required = true)
    private NamespaceResourceService namespaceResourceService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Dialect createDefaultDialect(KamInfo kamInfo) {
        return new DefaultDialect(kamInfo, kamStore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dialect createCustomDialect(KamInfo kamInfo,
            List<Namespace> geneNamespaces, List<Namespace> bpNamespaces,
            List<Namespace> chemicalNamespaces, BELSyntax form,
            boolean removeNamespacePrefix) throws KamStoreException {

        // cache all namespaces of the kam by prefix
        Map<String, Namespace> kamNamespaces = new HashMap<String, Namespace>();
        Map<String, NamespaceDomain> domains =
                new HashMap<String, NamespaceDomain>();
        for (org.openbel.framework.internal.KAMStoreDaoImpl.Namespace ns : kamStore.getNamespaces(kamInfo)) {
            Namespace cns = new Namespace(ns.getPrefix(),
                    ns.getResourceLocation());
            kamNamespaces.put(cns.getPrefix(), cns);
            NamespaceHeader hdr = namespaceResourceService.getHeader(cns
                    .getResourceLocation());
            domains.put(cns.getPrefix(), NamespaceDomain.forDomainString(hdr
                    .getNamespaceBlock().getDomainString()));
        }

        CustomDialect pd = new CustomDialect(kamStore);
        pd.setKamNamespaces(kamNamespaces);
        pd.setNsDomains(domains);
        pd.setGeneNamespaces(geneNamespaces);
        pd.setBpNamespaces(bpNamespaces);
        pd.setChemNamespaces(chemicalNamespaces);
        if (BELSyntax.LONG_FORM.equals(form)) {
            pd.setDisplayLongForm(true);
        }
        pd.setRemoveNamespacePrefix(removeNamespacePrefix);

        pd.initialize();
        return pd;
    }
}
