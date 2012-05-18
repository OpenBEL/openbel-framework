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
