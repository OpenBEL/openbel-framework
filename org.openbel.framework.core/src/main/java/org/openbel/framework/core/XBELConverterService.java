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
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.openbel.bel.xbel.model.XBELDocument;
import org.openbel.framework.common.model.Document;

/**
 * Converts between XBEL XML documents and Java objects.
 *
 */
public interface XBELConverterService {

    /**
     * Converts from an XML model document to XML.
     *
     * @param d XBEL model document
     * @return XML
     * @throws IOException Thrown if an I/O exception occurred during JAXB
     * processing
     * @throws JAXBException Thrown if an error was encountered during JAXB
     * processing
     */
    public String toXML(XBELDocument d) throws JAXBException, IOException;

    /**
     * Converts from XML string data to a XBEL XML model document.
     *
     * @param s XML string
     * @return XBEL model document
     * @throws IOException Thrown if an I/O exception occurred during JAXB
     * processing
     * @throws JAXBException Thrown if an error was encountered during JAXB
     * processing
     */
    public XBELDocument toJAXB(String s) throws JAXBException, IOException;

    /**
     * Converts from an XML file to a XBEL XML model document.
     *
     * @param f XBEL XML file
     * @return XBEL model document
     * @throws IOException Thrown if an I/O exception occurred during JAXB
     * processing
     * @throws JAXBException Thrown if an error was encountered during JAXB
     * processing
     */
    public XBELDocument toJAXB(File f) throws JAXBException, IOException;

    /**
     * Converts from a common model document to an XML string.
     *
     * @param d Common model document
     * @return XML
     * @throws IOException Thrown if an I/O exception occurred during JAXB
     * processing
     * @throws JAXBException Thrown if an error was encountered during JAXB
     * processing
     */
    public String toXML(Document d) throws JAXBException, IOException;

    /**
     * Writes a common model document as XML to a file.
     *
     * @param d Common model document
     * @param f File
     * @throws IOException Thrown if an I/O exception occurred during JAXB
     * processing
     * @throws JAXBException Thrown if an error was encountered during JAXB
     * processing
     */
    public void toXML(Document d, File f) throws JAXBException, IOException;

    /**
     * Converts from XML string data to a XBEL common model document.
     *
     * @param s XML string
     * @return XBEL common model document
     * @throws IOException Thrown if an I/O exception occurred during JAXB
     * processing
     * @throws JAXBException Thrown if an error was encountered during JAXB
     * processing
     */
    public Document toCommon(String s) throws JAXBException, IOException;

    /**
     * Converts from an XML file to a XBEL common model document.
     *
     * @param f XBEL XML file
     * @return XBEL common model document
     * @throws IOException Thrown if an I/O exception occurred during JAXB
     * processing
     * @throws JAXBException Thrown if an error was encountered during JAXB
     * processing
     */
    public Document toCommon(File f) throws JAXBException, IOException;

}
