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
