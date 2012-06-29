package org.openbel.framework.common.index;

import java.util.List;
import java.util.Set;

/**
 * Index encapsulates the information in a Resource Index file.
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public final class Index {
    private final List<ResourceLocation> adResources;
    private final List<ResourceLocation> nsResources;
    private final List<Equivalence> eqResources;
    private final ResourceLocation pfResource;
    private final ResourceLocation ncResource;
    private final ResourceLocation gsResource;
    private Set<ResourceLocation> orthoResources;

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
     * @param orthoResources {@link Set} of {@link ResourceLocation}, the
     * orthology resources
     */
    protected Index(
            List<ResourceLocation> adResources,
            List<ResourceLocation> nsResources,
            List<Equivalence> eqResources,
            ResourceLocation pfResource,
            ResourceLocation ncResource,
            ResourceLocation gsResource,
            Set<ResourceLocation> orthoResources) {
        this.adResources = adResources;
        this.nsResources = nsResources;
        this.eqResources = eqResources;
        this.pfResource = pfResource;
        this.ncResource = ncResource;
        this.gsResource = gsResource;
        this.orthoResources = orthoResources;
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

    /**
     * Return the orthology document's {@link ResourceLocation}.
     *
     * @return the orthology resources, which may be {@code null}
     */
    public Set<ResourceLocation> getOrthologyResources() {
        return orthoResources;
    }
}
