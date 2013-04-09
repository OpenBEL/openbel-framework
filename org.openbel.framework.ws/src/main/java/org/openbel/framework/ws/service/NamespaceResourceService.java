/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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

import java.util.List;

import org.openbel.framework.core.df.beldata.namespace.NamespaceHeader;
import org.openbel.framework.ws.model.NamespaceDescriptor;

/**
 * Service for providing access to Namespaces defined in the resource index of
 * the system configuration.
 *
 * @author Steve Ungerer
 */
public interface NamespaceResourceService {

    /**
     * Retrieve all namespaces as {@link NamespaceDescriptor}s that are
     * configured in the resource file of the BELFramework system configuration.
     *
     * @return {@link List} of {@link NamespaceDescriptor}s. The prefix given
     *         for each namespace contained within the descriptor will be the
     *         suggested prefix defined in the 'keyword' field of the namespace
     *         file. If no namespaces are defined, an empty list will be
     *         returned.
     */
    List<NamespaceDescriptor> getAllNamespaceDescriptors();

    /**
     * Retrieve the {@link NamespaceHeader} for a given namespace resource
     * location. If no configured namespaces match the given resource location,
     * null will be returned.
     *
     * @param resourceLocation
     * @return {@link NamespaceHeader} or <code>null</code> if no namespace with
     *         the given resource location is configured.
     */
    NamespaceHeader getHeader(String resourceLocation);

}
