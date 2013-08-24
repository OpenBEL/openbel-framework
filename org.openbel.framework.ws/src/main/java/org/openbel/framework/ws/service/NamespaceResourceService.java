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
