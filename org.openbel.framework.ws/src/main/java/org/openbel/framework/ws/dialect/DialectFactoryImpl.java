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
package org.openbel.framework.ws.dialect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.KAMStore;
import org.openbel.framework.api.KAMStoreException;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.core.df.beldata.namespace.NamespaceHeader;
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
    private KAMStore kAMStore;

    @Autowired(required = true)
    private NamespaceResourceService namespaceResourceService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Dialect createDefaultDialect(KamInfo kamInfo) {
        return new DefaultDialect(kamInfo, kAMStore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dialect createCustomDialect(KamInfo kamInfo,
            List<Namespace> geneNamespaces, List<Namespace> bpNamespaces,
            List<Namespace> chemicalNamespaces, BELSyntax form,
            boolean removeNamespacePrefix) throws KAMStoreException {

        // cache all namespaces of the kam by prefix
        Map<String, Namespace> kamNamespaces = new HashMap<String, Namespace>();
        Map<String, NamespaceDomain> domains =
                new HashMap<String, NamespaceDomain>();
        for (org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace ns : kAMStore.getNamespaces(kamInfo)) {
            Namespace cns = new Namespace(ns.getPrefix(),
                    ns.getResourceLocation());
            kamNamespaces.put(cns.getPrefix(), cns);
            NamespaceHeader hdr = namespaceResourceService.getHeader(cns
                    .getResourceLocation());
            domains.put(cns.getPrefix(), NamespaceDomain.forDomainString(hdr
                    .getNamespaceBlock().getDomainString()));
        }

        CustomDialect pd = new CustomDialect(kAMStore);
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
