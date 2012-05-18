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
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protocol.ResourceResolver;

/**
 * Defines a resource resolver for downloading any OpenBEL Framework resource.
 * This is a common service to resolve any OpenBEL Framework resource files.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface ResourceService {

    /**
     * Resolves a OpenBEL Framework resource by determining how to resolve the
     * <tt>resourceLocation</tt> using
     * {@link ResourceResolver#resolveResource(String, String)}.
     * 
     * <p>
     * The file is resolved locally to:
     * <code>
     * localRoot + {@link File#separator} + {@link System#currentTimeMillis()} + localExtension
     * </code>
     * </p>
     * 
     * @param resourceLocation {@link String}, the location of the resource,
     * which cannot be null
     * @param localPath {@link String}, the local folder path where the resource
     * should be saved to, which cannot be null
     * @param localFileName {@link String}, the local file name
     * @throws ResourceDownloadError Thrown if there was an I/O error resolving
     * the resource location.
     * @throws InvalidArgument Thrown if any of the arguments are null. 
     */
    public File resolveResource(String resourceLocation, String localPath,
            String localFileName)
            throws ResourceDownloadError;
}
