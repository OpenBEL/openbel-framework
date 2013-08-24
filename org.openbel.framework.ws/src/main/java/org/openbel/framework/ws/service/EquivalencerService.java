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
