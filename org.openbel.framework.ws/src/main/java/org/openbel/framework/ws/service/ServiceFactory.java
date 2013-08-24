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

import java.io.File;

import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Factory bean for initializing services, resources, etc in openbel-ws
 *
 * @author Steve Ungerer
 */
public class ServiceFactory implements InitializingBean {

    private SystemConfiguration systemConfiguration;
    private CacheableResourceService cacheService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(systemConfiguration,
                "SystemConfiguration must be injected");
        Assert.notNull(cacheService,
                "CacheableResourceService (cacheService) must be injected");
    }

    /**
     * Create an {@link Index} instance
     * @return
     * @throws Exception
     */
    public Index createResourceIndex() throws Exception {
        if (!ResourceIndex.INSTANCE.isLoaded()) {
            // Load resource index defined by the BELFramework instance
            final SystemConfiguration sysConfig = SystemConfiguration
                    .createSystemConfiguration(null);

            final String resourceIndexURL = sysConfig.getResourceIndexURL();
            File indexFile = new File(resourceIndexURL);
            if (!indexFile.exists() || !indexFile.canRead()) {
                // try the index as an online resource.
                ResolvedResource resolvedResource = cacheService
                        .resolveResource(ResourceType.RESOURCE_INDEX,
                                resourceIndexURL);
                indexFile = resolvedResource.getCacheResourceCopy();
            }

            ResourceIndex.INSTANCE.loadIndex(indexFile);
        }
        return ResourceIndex.INSTANCE.getIndex();
    }

    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public void setCacheService(CacheableResourceService cacheService) {
        this.cacheService = cacheService;
    }

}
