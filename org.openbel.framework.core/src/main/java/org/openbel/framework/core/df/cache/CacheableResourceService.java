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
package org.openbel.framework.core.df.cache;

import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * CacheableResourceService defines a service to handle cacheable BELFramework
 * resource locations.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface CacheableResourceService extends ResourceService {

    /**
     * Resolves the BELFramework resource with a specific <tt>type</tt> and
     * located at <tt>location</tt>.
     * 
     * @param type {@link ResourceType}, the resource type
     * @param location {@link String}, the resource location
     * @return {@link ResolvedResource}, the resource resolved in the cache
     * indicating the action taken
     * @throws ResourceDownloadError Thrown if the resource could not be
     * downloaded from <tt>location</tt> and if that resource was not located
     * in the cache
     */
    public ResolvedResource resolveResource(ResourceType type, String location)
            throws ResourceDownloadError;
}
