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

import org.openbel.framework.api.EquivalencerException;
import org.openbel.framework.ws.model.EquivalenceId;
import org.openbel.framework.ws.model.Namespace;
import org.openbel.framework.ws.model.NamespaceValue;

/**
 * Defines an equivalencer service that finds equivalences for namespace values.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface EquivalencerService {

    /**
     * Finds an equivalent namespace value, for <tt>sourceNamespaceValue</tt>,
     * in the <tt>targetNamespace</tt>.
     *
     * @param sourceNamespaceValue {@link NamespaceValue}, the source namespace
     *            value
     * @param targetNamespace {@link Namespace}, the target namespace
     * @return the equivalent {@link NamespaceValue} in the target namespace
     * @throws EquivalencerException Thrown if an equivalencing error occurred
     *             while finding equivalent namespace value
     */
    public NamespaceValue findNamespaceEquivalence(
            final NamespaceValue sourceNamespaceValue,
            final Namespace targetNamespace) throws EquivalencerException;

    /**
     * Finds all equivalent namespace values for <tt>sourceNamespaceValue</tt>.
     *
     * @param sourceNamespaceValue {@link NamespaceValue}, the source namespace
     *            value
     * @return the equivalent {@link NamespaceValue} objects, or an empty
     *         {@link List} if there are no equivalent {@link NamespaceValue}s
     * @throws EquivalencerException Thrown if an equivalencing error occurred
     *             while finding all equivalent namespace values
     */
    public List<NamespaceValue> findEquivalences(
            final NamespaceValue sourceNamespaceValue)
            throws EquivalencerException;

    /**
     * Obtain the {@link EquivalenceId} for a {@link NamespaceValue}.
     *
     * @param namespaceValue. If {@link EquivalenceId} is populated in the
     *            namespace value, it will be ignored.
     * @return {@link EquivalenceId} or <code>null</code> if no equivalent can
     *         be found.
     * @throws EquivalencerException Thrown if an equivalencing error occurred
     *             while finding all equivalent namespace values
     */
    public EquivalenceId getEquivalenceId(final NamespaceValue namespaceValue)
            throws EquivalencerException;
}
