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
package org.openbel.framework.common.index;

import static org.openbel.framework.common.BELUtilities.nulls;

import org.openbel.framework.common.InvalidArgument;

/**
 * Equivalence encapsulates an equivalence resource and it's associated
 * source and target namespace resources.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public final class Equivalence extends ResourceLocation {
    private ResourceLocation namespaceResourceLocation;

    /**
     * Constructs the Equivalence object with an equivalence resource and a
     * namespace resource.
     *
     * @param equivalenceResource {@link String}, the equivalence resource
     * location, which cannot be null
     * @param namespaceResourceLocation {@link ResourceLocation}, the
     * namespace location, which cannot be null
     * @throws InvalidArgument Thrown if the <tt>equivalenceResource</tt>
     * or <tt>namespaceResourceLocation</tt> {@link ResourceLocation} is null
     */
    protected Equivalence(String equivalenceResource,
            ResourceLocation namespaceResourceLocation) {
        super(equivalenceResource);

        if (nulls(equivalenceResource, namespaceResourceLocation)) {
            throw new InvalidArgument("null resource location(s)");
        }

        this.namespaceResourceLocation = namespaceResourceLocation;
    }

    /**
     * Returns the namespace {@link ResourceLocation} that should point to a
     * namespace document.
     *
     * @return {@link ResourceLocation} the namespace resource location
     */
    public ResourceLocation getNamespaceResourceLocation() {
        return namespaceResourceLocation;
    }
}
