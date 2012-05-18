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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
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
        if (this.index != null) {
            throw new IllegalStateException("index already initialized");
        }

        this.index = new Index(new ArrayList<ResourceLocation>(),
                new ArrayList<ResourceLocation>(),
                new ArrayList<Equivalence>(), null, null, null);
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
        if (this.index != null) {
            throw new IllegalStateException("index already initialized");
        }

        if (indexFile == null) {
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

        final List<ResourceLocation> adResources =
                new ArrayList<ResourceLocation>();
        final List<ResourceLocation> nsResources =
                new ArrayList<ResourceLocation>();
        final List<Equivalence> eqResources = new ArrayList<Equivalence>();
        ResourceLocation pfResource = null;
        ResourceLocation ncResource = null;
        ResourceLocation gsResource = null;

        String eqResourceLocation = null;
        ResourceLocation namespaceRef = null;
        while (reader.hasNext()) {
            XMLEvent xev = reader.nextEvent();
            if (xev.isStartElement()) {
                StartElement sel = (StartElement) xev;
                String elname = sel.getName().getLocalPart();
                if (ANNOTATION_DEFINITION_ELEMENT.equals(elname)) {
                    String rloc =
                            findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    adResources.add(new ResourceLocation(rloc));
                } else if (NAMESPACE_ELEMENT.equals(elname)) {
                    String rloc =
                            findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    nsResources.add(new ResourceLocation(rloc));
                } else if (EQUIVALENCE_ELEMENT.equals(elname)) {
                    eqResourceLocation =
                            findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                } else if (NAMESPACE_REF.equals(elname)) {
                    String rloc =
                            findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    namespaceRef = new ResourceLocation(rloc);
                } else if (PROTEIN_FAMILIES.equals(elname)) {
                    String rloc =
                            findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    pfResource = new ResourceLocation(rloc);
                } else if (NAMED_COMPLEXES.equals(elname)) {
                    String rloc =
                            findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    ncResource = new ResourceLocation(rloc);
                } else if (GENE_SCAFFOLDING.equals(elname)) {
                    String rloc =
                            findAttributeValue(RESOURCE_LOCATION_ATTR, sel);
                    gsResource = new ResourceLocation(rloc);
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
                ncResource, gsResource);
    }

    private String findAttributeValue(String name, StartElement el) {
        if (name == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Iterator<Attribute> attrIt = el.getAttributes();

        while (attrIt.hasNext()) {
            Attribute attr = attrIt.next();

            if (name.equals(attr.getName().getLocalPart())) {
                return attr.getValue();
            }
        }
        return null;
    }
}
