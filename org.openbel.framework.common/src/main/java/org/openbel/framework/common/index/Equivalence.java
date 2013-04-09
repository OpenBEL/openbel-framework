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
