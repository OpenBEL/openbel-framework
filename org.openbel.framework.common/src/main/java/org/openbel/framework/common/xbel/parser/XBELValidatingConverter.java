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
package org.openbel.framework.common.xbel.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;

import org.openbel.bel.xbel.model.XBELDocument;
import org.xml.sax.SAXException;

/**
 * Specialization of the XBEL converter providing XML validation against
 * schemas.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class XBELValidatingConverter extends XBELConverter {
    /**
     * BELCLI path to XML schema.
     */
    public final static String SCHEMA_PATH = "schemas";

    /**
     * Name of XBEL schema.
     */
    public final static String XBEL_SCHEMA = "xbel.xsd";

    /**
     * Name of XBEL annotation schema.
     */
    public final static String XBEL_ANNO = "xbel-annotations.xsd";

    /**
     * Defines the xbel validator used to validate XBEL xml documents.
     */
    private final XBELValidator validator;

    /**
     * Creates a XBEL validating converter.
     *
     * @throws SAXException thrown if an error is found during processing the
     * specified schemas; the current API does not provide
     * {@link org.xml.sax.ErrorHandler SAX error handler} functionality
     * @throws URISyntaxException thrown if a {@link URL} in {@code schemas} has
     * invalid {@link URI} syntax
     * @throws IOException thrown if an i/o error occurs reading from the schema
     * URL's {@link InputStream}
     * @throws NullPointerException thrown if the schema resources cannot be
     * found
     * @throws JAXBException Thrown if an error was encountered processing the
     * JAXB context or unmarshalling to the generic type
     */
    public XBELValidatingConverter()
            throws SAXException, URISyntaxException, IOException, JAXBException {
        validator = new XBELValidator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String marshal(final XBELDocument d) throws JAXBException,
            IOException {
        final String marshalledOutput = super.marshal(d);
        try {
            validator.validate(marshalledOutput);
        } catch (SAXException e) {
            throw new JAXBException(e);
        }
        return marshalledOutput;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELDocument unmarshal(final String str) throws JAXBException,
            IOException {
        try {
            validator.validate(str);
        } catch (SAXException e) {
            throw new JAXBException(e);
        }
        return super.unmarshal(str);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELDocument unmarshal(File f) throws JAXBException, IOException {
        try {
            validator.validate(f);
        } catch (SAXException e) {
            throw new JAXBException(e);
        }
        return super.unmarshal(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELDocument unmarshal(Source s) throws JAXBException, IOException {
        try {
            validator.validate(s);
        } catch (SAXException e) {
            throw new JAXBException(e);
        }
        return super.unmarshal(s);
    }
}
