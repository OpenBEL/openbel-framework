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
package org.openbel.framework.ws.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceLocation;
import org.openbel.framework.core.df.beldata.namespace.NamespaceHeader;
import org.openbel.framework.core.df.beldata.namespace.NamespaceHeaderParser;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.openbel.framework.ws.model.Namespace;
import org.openbel.framework.ws.model.NamespaceDescriptor;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.utils.ObjectFactorySingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Steve Ungerer
 */
@Service
public class NamespaceResourceServiceImpl implements InitializingBean,
        NamespaceResourceService {
    private static final Logger logger = LoggerFactory
            .getLogger(NamespaceResourceServiceImpl.class);
    private static final ObjectFactory OBJECT_FACTORY = ObjectFactorySingleton
            .getInstance();

    @Autowired(required = true)
    private CacheableResourceService cacheableResourceService;

    @Autowired(required = true)
    private Index resourceIndex;

    private Map<String, NamespaceHeader> headers;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("Loading BEL namespaces");
        NamespaceHeaderParser parser = new NamespaceHeaderParser();
        this.headers = new HashMap<String, NamespaceHeader>(resourceIndex
                .getNamespaceResources().size());

        for (ResourceLocation rl : resourceIndex.getNamespaceResources()) {
            String loc = rl.getResourceLocation();
            ResolvedResource nsResource = cacheableResourceService
                    .resolveResource(ResourceType.NAMESPACES, loc);
            NamespaceHeader header = parser.parseNamespace(loc,
                    nsResource.getCacheResourceCopy());
            headers.put(loc, header);
        }
        logger.debug("Finished loading BEL namespaces");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NamespaceDescriptor> getAllNamespaceDescriptors() {
        List<NamespaceDescriptor> l =
                new ArrayList<NamespaceDescriptor>(headers.size());
        for (Map.Entry<String, NamespaceHeader> entry : headers.entrySet()) {
            Namespace ns = new Namespace();
            ns.setPrefix(entry.getValue().getNamespaceBlock().getKeyword());
            ns.setResourceLocation(entry.getKey());
            NamespaceDescriptor nsd = OBJECT_FACTORY.createNamespaceDescriptor();
            nsd.setNamespace(ns);
            nsd.setName(entry.getValue().getNamespaceBlock().getNameString());
            l.add(nsd);
        }
        return Collections.unmodifiableList(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamespaceHeader getHeader(String resourceLocation) {
        return headers.get(resourceLocation);
    }

}
