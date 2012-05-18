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
package org.openbel.framework.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.openbel.bel.xbel.model.XBELDocument;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.xbel.converters.DocumentConverter;
import org.openbel.framework.common.xbel.parser.XBELConverter;
import org.xml.sax.SAXException;

/**
 * Converter implementation; converts between XBEL XML documents and Java
 * objects.
 *
 */
public class XBELConverterServiceImpl implements XBELConverterService {
    private final XBELConverter converter;

    /**
     * Creates a XBEL converter service implementation from the provided schema
     * path locations.
     *
     * @throws SAXException Thrown if an error is found during processing the
     * specified schemas; the current API does not provide
     * {@link org.xml.sax.ErrorHandler SAX error handler} functionality
     * @throws URISyntaxException Thrown if a {@link URL} in {@code schemas} has
     * invalid {@link URI} syntax
     * @throws IOException Thrown if an I/O error occurs creating the validating
     * converter
     * @throws JAXBException Thrown if an error was encountered creating the validating converter
     */
    public XBELConverterServiceImpl() throws SAXException,
            URISyntaxException, IOException, JAXBException {
        converter = new XBELConverter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toXML(final XBELDocument d) throws JAXBException, IOException {
        return converter.marshal(d);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toXML(final Document d) throws JAXBException, IOException {
        XBELDocument xdoc = new DocumentConverter().convert(d);
        return converter.marshal(xdoc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toXML(final Document d, final File f) throws JAXBException,
            IOException {
        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            fw.write(toXML(d));
        } finally {
            if (fw != null) {
                fw.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELDocument toJAXB(final String s) throws JAXBException,
            IOException {
        return converter.unmarshal(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELDocument toJAXB(final File f) throws JAXBException, IOException {
        return converter.unmarshal(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document toCommon(final String s) throws JAXBException, IOException {
        XBELDocument xdoc = toJAXB(s);
        return convert(xdoc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document toCommon(final File f) throws JAXBException, IOException {
        XBELDocument xdoc = toJAXB(f);
        return convert(xdoc);
    }

    private Document convert(final XBELDocument xdoc) {
        return new DocumentConverter().convert(xdoc);
    }
}
