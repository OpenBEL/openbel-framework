package org.openbel.framework.common.index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.openbel.framework.common.InvalidArgument;

/**
 * ResourceIndex defines a singleton instance to hold an {@link Index}.
 *
 * <p>
 * An {@link Index} must first be loaded by calling {@link #loadIndex(Index)}
 * before trying to access it.  Accessing an index before it has been loaded
 * or trying to load a subsequent index will trigger a
 * {@link IllegalStateException}.
 * </p>
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public enum ResourceIndex {
    INSTANCE;

    private final String ANNOTATION_DEFINITION_ELEMENT = "annotationdefinition";
    private final String NAMESPACE_ELEMENT = "namespace";
    private final String EQUIVALENCE_ELEMENT = "equivalence";
    private final String RESOURCE_LOCATION_ATTR = "resourceLocation";
    private final String NAMESPACE_REF = "namespace-ref";
    private final String PROTEIN_FAMILIES = "protein-families";
    private final String NAMED_COMPLEXES = "named-complexes";
    private final String GENE_SCAFFOLDING = "gene-scaffolding";
    private final String ORTHOLOGY = "orthology";

    /**
     * The resource {@link Index} instance which will be assigned only once.
     */
    private Index index;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private ResourceIndex() {
    }

    /**
     * Loads a default {@link Index} with empty resources.
     *
     * <p>
     * See {@link #loadIndex(File)} in order to load the {@link Index} from an
     * index file.
     * </p>
     *
     * @throws IllegalStateException Thrown if the <tt>index</tt> has already
     * been loaded
     */
    public synchronized void loadIndex() {
        if(this.index != null) {
            throw new IllegalStateException("index already initialized");
        }

        this.index = new Index(new ArrayList<ResourceLocation>(),
                new ArrayList<ResourceLocation>(),
                new ArrayList<Equivalence>(),
                null, null, null, null);
    }

    /**
     * Loads the {@link Index} instance once.
     *
     * <p>
     * Subsequent attempts to load an index will trigger an
     * {@link IllegalStateException}.
     * </p>
     *
     * @param indexFile {@link File}, the index xml file to load which cannot
     * be null
     * @throws XMLStreamException Thrown if there was an xml processing error
     * when parsing the index file
     * @throws FileNotFoundException Thrown if the indexFile {@link File}
     * resource could not be found for reading
     * @throws IllegalStateException Thrown if the <tt>index</tt> has already
     * been loaded
     * @throws InvalidArgument Thrown if the <tt>indexFile</tt> to load the
     * index from is null.
     */
    public synchronized void loadIndex(File indexFile)
            throws FileNotFoundException,
            XMLStreamException {
        if(this.index != null) {
            throw new IllegalStateException("index already initialized");
        }

        if(indexFile == null) {
            throw new InvalidArgument("indexFile", indexFile);
        }

        this.index = load(indexFile);
    }

    /**
     * Returns the loaded {@link Index}.
     *
     * <p>
     * If the index has not be loaded then an {@link IllegalStateException}
     * will be thrown.
     * </p>
     *
     * @return {@link Index} the loaded index
     * @throws IllegalStateException Thrown if the index has not been loaded
     * by calling {@link #loadIndex(File)}
     */
    public synchronized Index getIndex() {
        if (index == null) {
            throw new IllegalStateException(
                    "ResourceIndex must be loaded first");
        }
        return index;
    }

    /**
     * Determines if the {@link Index} has been loaded.
     *
     * @return the load status of {@link Index}
     */
    public synchronized boolean isLoaded() {
        return index != null;
    }

    /**
     * Internal method to parse the index xml {@link File} to an {@link Index}.
     *
     * TODO We should validate the index file before the parsing.
     *
     * @param indexFile {@link File}, the index xml file
     * @return {@link Index} the index parsed from <tt>indexFile</tt>
     * @throws XMLStreamException Thrown if there was an xml processing error
     * when parsing the index file
     * @throws FileNotFoundException Thrown if the indexFile {@link File}
     * resource could not be found for reading
     */
    private Index load(File indexFile) throws XMLStreamException,
            FileNotFoundException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Reader fileReader = new FileReader(indexFile);
        XMLEventReader reader = factory.createXMLEventReader(fileReader);

        final List<ResourceLocation> adResources = new ArrayList<ResourceLocation>();
        final List<ResourceLocation> nsResources = new ArrayList<ResourceLocation>();
        final List<Equivalence> eqResources = new ArrayList<Equivalence>();
        ResourceLocation pfResource = null;
        ResourceLocation ncResource = null;
        ResourceLocation gsResource = null;
        Set<ResourceLocation> orthoResources = new LinkedHashSet<ResourceLocation>();

        String eqResourceLocation = null;
        ResourceLocation namespaceRef = null;
        while(reader.hasNext()) {
            XMLEvent xev = reader.nextEvent();
            if (xev.isStartElement()) {
                StartElement sel = (StartElement) xev;
                String elname = sel.getName().getLocalPart();
                if (ANNOTATION_DEFINITION_ELEMENT.equals(elname)) {
                    String rloc = findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    adResources.add(new ResourceLocation(rloc));
                } else if (NAMESPACE_ELEMENT.equals(elname)) {
                    String rloc = findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    nsResources.add(new ResourceLocation(rloc));
                } else if (EQUIVALENCE_ELEMENT.equals(elname)) {
                    eqResourceLocation = findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                } else if (NAMESPACE_REF.equals(elname)) {
                    String rloc = findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    namespaceRef = new ResourceLocation(rloc);
                } else if (PROTEIN_FAMILIES.equals(elname)) {
                    String rloc = findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    pfResource = new ResourceLocation(rloc);
                } else if (NAMED_COMPLEXES.equals(elname)) {
                    String rloc = findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    ncResource = new ResourceLocation(rloc);
                } else if (GENE_SCAFFOLDING.equals(elname)) {
                    String rloc = findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    gsResource = new ResourceLocation(rloc);
                } else if (ORTHOLOGY.equals(elname)) {
                    String rloc = findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    orthoResources.add(new ResourceLocation(rloc));
                }
            } else if (xev.isEndElement()) {
                EndElement eel = (EndElement) xev;
                String elname = eel.getName().getLocalPart();
                if (EQUIVALENCE_ELEMENT.equals(elname)) {
                    eqResources.add(new Equivalence(eqResourceLocation,
                            namespaceRef));
                }
            }
        }

        return new Index(adResources, nsResources, eqResources, pfResource,
                ncResource, gsResource, orthoResources);
    }

    private String findAttributeValue(String name, StartElement el) {
        if(name == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Iterator<Attribute> attrIt = el.getAttributes();

        while (attrIt.hasNext()) {
            Attribute attr = attrIt.next();

            if(name.equals(attr.getName().getLocalPart())) {
                return attr.getValue();
            }
        }
        return null;
    }
}
