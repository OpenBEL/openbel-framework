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
package org.openbel.framework.ws.dialect;

import java.util.List;

import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.core.kamstore.data.jdbc.KAMCatalogDao.KamInfo;
import org.openbel.framework.core.kamstore.model.Kam;
import org.openbel.framework.core.kamstore.model.KamStoreException;
import org.openbel.framework.core.kamstore.model.dialect.Dialect;

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
