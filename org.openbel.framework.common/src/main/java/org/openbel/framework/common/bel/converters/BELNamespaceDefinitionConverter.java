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
package org.openbel.framework.common.bel.converters;

import org.openbel.bel.model.BELNamespaceDefinition;
import org.openbel.framework.common.model.Namespace;

public class BELNamespaceDefinitionConverter extends
        BELConverter<BELNamespaceDefinition, Namespace> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Namespace convert(BELNamespaceDefinition bnd) {
        if (bnd == null) {
            return null;
        }

        return new Namespace(bnd.getPrefix(), bnd.getResourceLocation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELNamespaceDefinition convert(Namespace n) {
        if (n == null) {
            return null;
        }

        return new BELNamespaceDefinition(n.getPrefix(),
                n.getResourceLocation(), false);
    }
}
