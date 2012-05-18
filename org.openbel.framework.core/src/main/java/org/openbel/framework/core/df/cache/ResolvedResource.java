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

import java.io.File;

import org.openbel.framework.common.InvalidArgument;

/**
 * ResolvedResource represents the result of resolving a resource to the cache.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ResolvedResource {
    /**
     * Defines the cache resource copy ready for processing.
     */
    private final File cacheResourceCopy;

    /**
     * Defines whether or not this resource needed to be downloaded and cached
     * because a new copy existed at its remote location. 
     */
    private final boolean cacheUpdated;

    /**
     * Constructs the ResolvedResource with the <tt>cachedResource</tt> and
     * the <tt>cacheUpdated</tt> flag.
     * 
     * @param cachedResource {@link CachedResource}, the cached resource which
     * cannot be null
     * @param cacheUpdated <tt>boolean</tt>, <tt>true</tt> indicating a new 
     * resource has been cached, <tt>false</tt> if the cached copy is
     * up-to-date
     * @throws InvalidArgument Thrown if <tt>cachedResource</tt> is null
     */
    public ResolvedResource(final File cacheResourceCopy,
            final boolean cacheUpdated) {
        if (cacheResourceCopy == null) {
            throw new InvalidArgument("cacheResourceCopy", cacheResourceCopy);
        }

        this.cacheResourceCopy = cacheResourceCopy;
        this.cacheUpdated = cacheUpdated;
    }

    /**
     * Returns the cached resource copy read for processing.
     * 
     * @return {@link File}, the cache resource copy which cannot be null
     */
    public File getCacheResourceCopy() {
        return cacheResourceCopy;
    }

    /**
     * Returns the cache updated flag.
     * 
     * @return <tt>boolean</tt>, the cache updated flag, <tt>true</tt>
     * indicating a new resource has been cached, <tt>false</tt> if the cached
     * copy is up-to-date
     */
    public boolean isCacheUpdated() {
        return cacheUpdated;
    }
}
