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
