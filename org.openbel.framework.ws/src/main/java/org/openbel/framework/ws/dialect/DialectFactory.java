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
package org.openbel.framework.ws.dialect;

import java.util.List;

import org.openbel.framework.api.Dialect;
import org.openbel.framework.api.KamStoreException;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

/**
 * Factory class for creation of {@link Dialect}s
 *
 * @author Steve Ungerer
 */
public interface DialectFactory {

    /**
     * Construct a new {@link DefaultDialect}
     * @param kamInfo
     * @return
     */
    Dialect createDefaultDialect(KamInfo kamInfo);

    /**
     * Construct a new {@link CustomDialect}
     *
     * @param kamInfo {@link KamInfo} of the {@link Kam} the dialect is being
     *            constructed for
     * @param geneNamespaces {@link List} of {@link Namespace}s to display
     *            {@link NamespaceDomain#Gene} values in. Items with a lower
     *            list index are assigned higher priority.
     * @param bpNamespaces {@link List} of {@link Namespace}s to display
     *            {@link NamespaceDomain#BiologicalProcess} values in. Items
     *            with a lower list index are assigned higher priority.
     * @param chemicalNamespaces {@link List} of {@link Namespace}s to display
     *            {@link NamespaceDomain#Chemical} values in. Items with a lower
     *            list index are assigned higher priority.
     * @param form {@link BELSyntax} to use for label displays
     * @param removeNamespacePrefix Should the {@link Namespace} prefix be
     *            removed from the node labels
     * @return
     * @throws KamStoreException
     */
    Dialect createCustomDialect(KamInfo kamInfo,
            List<Namespace> geneNamespaces, List<Namespace> bpNamespaces,
            List<Namespace> chemicalNamespaces, BELSyntax form,
            boolean removeNamespacePrefix) throws KamStoreException;

}
