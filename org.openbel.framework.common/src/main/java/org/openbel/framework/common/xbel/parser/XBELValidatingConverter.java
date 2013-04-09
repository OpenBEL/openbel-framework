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
