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
package org.openbel.framework.common.index;

import java.util.List;

/**
 * Index encapsulates the information in a Resource Index file.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public final class Index {
    private final List<ResourceLocation> adResources;
    private final List<ResourceLocation> nsResources;
    private final List<Equivalence> eqResources;
    private final ResourceLocation pfResource;
    private final ResourceLocation ncResource;
    private final ResourceLocation gsResource;

    /**
     * Constructs the Index with the namespace resources,
     * equivalence resources, protein family resource, named
     * complex resource, and gene scaffolding resource.
     * 
     * @param adResources {@link List} of {@link ResourceLocation}, the
     * annotation definition resource locations
     * @param nsResources {@link List} of {@link ResourceLocation}, the
     * namespace resource locations
     * @param eqResources {@link List} of {@link Equivalence}, the equivalence
     * resource locations
     * @param pfResource {@link ResourceLocation}, the protein family resource
     * @param ncResource {@link ResourceLocation}, the named complex resource
     * @param gsResource {@link ResourceLocation}, the gene scaffolding resource
     */
    protected Index(
            List<ResourceLocation> adResources,
            List<ResourceLocation> nsResources,
            List<Equivalence> eqResources,
            ResourceLocation pfResource,
            ResourceLocation ncResource,
            ResourceLocation gsResource) {
        this.adResources = adResources;
        this.nsResources = nsResources;
        this.eqResources = eqResources;
        this.pfResource = pfResource;
        this.ncResource = ncResource;
        this.gsResource = gsResource;
    }

    /**
     * Returns the annotation definition {@link ResourceLocation} list.
     * 
     * @return {@link List} of {@link ResourceLocation}, which can be null
     */
    public List<ResourceLocation> getAnnotationDefinitionResources() {
        return adResources;
    }

    /**
     * Returns the namespace {@link ResourceLocation} list.
     * 
     * @return {@link List} of {@link ResourceLocation}, which can be null
     */
    public List<ResourceLocation> getNamespaceResources() {
        return nsResources;
    }

    /**
     * Returns the list of {@link Equivalence} objects.
     * 
     * @return {@link List} of {@link Equivalence}, which can be null
     */
    public List<Equivalence> getEquivalenceResources() {
        return eqResources;
    }

    /**
     * Return the protein family {@link ResourceLocation}.
     * 
     * @return {@link ResourceLocation} the protein family resource which
     * can be null
     */
    public ResourceLocation getProteinFamilyResource() {
        return pfResource;
    }

    /**
     * Return the named complexes {@link ResourceLocation}.
     * 
     * @return {@link ResourceLocation} the named complexes resource which
     * can be null
     */
    public ResourceLocation getNamedComplexesResource() {
        return ncResource;
    }

    /**
     * Return the gene scaffolding {@link ResourceLocation}.
     * 
     * @return {@link ResourceLocation} the gene scaffolding resource which
     * can be null
     */
    public ResourceLocation getGeneScaffoldingResource() {
        return gsResource;
    }
}
