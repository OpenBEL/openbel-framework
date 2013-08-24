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
