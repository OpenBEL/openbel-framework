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
