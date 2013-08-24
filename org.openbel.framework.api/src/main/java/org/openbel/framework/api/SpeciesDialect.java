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
package org.openbel.framework.api;

import java.util.List;

import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.internal.KAMStoreDaoImpl.TermParameter;

/**
 * {@link SpeciesDialect} constrains a {@link Kam kam} to a specific species.
 * The {@link SpeciesDialect species dialect} must provide the
 * {@link Namespace namespaces} that supported the target species.
 *
 * @see Namespaces#getSpeciesNamespaces(int)
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public interface SpeciesDialect {

    public String getLabel(KamNode kamNode, TermParameter speciesParam);

    /**
     * Returns the {@link Namespace namespaces} that identify the target
     * species for this {@link SpeciesDialect species dialect}.
     *
     * @return {@link List} of {@link Namespace}
     */
    public List<Namespace> getSpeciesNamespaces();
}
