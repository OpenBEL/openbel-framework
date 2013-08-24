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

import static javax.xml.bind.JAXBContext.newInstance;
import static javax.xml.bind.Marshaller.JAXB_ENCODING;
import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static javax.xml.bind.Marshaller.JAXB_SCHEMA_LOCATION;
import static org.openbel.framework.common.Strings.UTF_8;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;

import org.openbel.bel.xbel.XBELConstants;
import org.openbel.bel.xbel.model.XBELDocument;

/**
 * Implements an XML converter to convert from XML to {@link XBELDocument}
 * objects.
 *
 * The {@link JAXBContext} is established on object construction, but creating
 * the {@link Marshaller} and {@link Unmarshaller} is deferred until the
 * marshal and unmarshal operations are called.  This allows the
 * {@link Marshaller} and {@link Unmarshaller} instances to be garbage
 * collected when the operations complete.
 */
public class XBELConverter implements XMLConverter<XBELDocument> {
    private final JAXBContext ctxt;

    /**
     * Creates a XBEL converter.
     *
     * @throws JAXBException Thrown if an error was encountered processing
     * the JAXB context or unmarshalling to the generic type
     */
    public XBELConverter() throws JAXBException {
        ctxt = newInstance(XBELDocument.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String marshal(final XBELDocument d) throws JAXBException,
            IOException {
        final Marshaller marshaller = createNewMarshaller();

        StringWriter sw = new StringWriter();
        marshaller.marshal(d, sw);
        sw.close();
        return sw.toString();
    }

    @Override
    public void marshal(XBELDocument t, OutputStream out) throws JAXBException,
            IOException {
        final Marshaller marshaller = createNewMarshaller();

        marshaller.marshal(t, out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELDocument unmarshal(String s) throws JAXBException, IOException {
        final Unmarshaller unmarshaller = createNewUnmashaller();

        return (XBELDocument) unmarshaller.unmarshal(new StringReader(s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELDocument unmarshal(File f) throws JAXBException, IOException {
        final Unmarshaller unmarshaller = createNewUnmashaller();

        FileInputStream fis = new FileInputStream(f);
        Reader reader = new InputStreamReader(fis, UTF_8);
        return (XBELDocument) unmarshaller.unmarshal(reader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELDocument unmarshal(Source s) throws JAXBException, IOException {
        final Unmarshaller unmarshaller = createNewUnmashaller();

        return (XBELDocument) unmarshaller.unmarshal(s);
    }

    /**
     * Create a new JAXB {@link Marshaller} instance to handle conversion to
     * XBEL.
     *
     * @return a new {@link Marshaller} instance
     * @throws JAXBException Thrown if an error was encountered creating the
     * JAXB {@link Marshaller}
     * @throws PropertyException Thrown if an error was encountered setting
     * properties on the {@link Marshaller}
     */
    private Marshaller createNewMarshaller() throws JAXBException,
            PropertyException {
        final Marshaller marshaller = ctxt.createMarshaller();
        marshaller.setProperty(JAXB_ENCODING, UTF_8);
        marshaller.setProperty(JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(JAXB_SCHEMA_LOCATION, XBELConstants.SCHEMA_URI
                + " " + XBELConstants.SCHEMA_URL);
        return marshaller;
    }

    /**
     * Create a new JAXB {@link Unmarshaller} instance to handle conversion
     * from XBEL.
     *
     * @return a new {@link Unmarshaller} instance
     * @throws JAXBException Thrown if an error was encountered creating the
     * JAXB {@link Unmarshaller}
     */
    private Unmarshaller createNewUnmashaller() throws JAXBException {
        return ctxt.createUnmarshaller();
    }
}
