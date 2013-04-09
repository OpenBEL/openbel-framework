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
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;

/**
 * Defines an XML converter to convert between XML and JAXB objects.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @param <T>
 *            The generic JAXB object type
 */
public interface XMLConverter<T> {

    /**
     * Marshals the generic JAXB object into XML.
     *
     * @param t
     *            JAXB object
     * @return XML string
     * @throws JAXBException
     *             Thrown if an error was encountered processing the JAXB
     *             context or marshalling the generic type
     * @throws IOException
     *             Thrown if an error occurred in writing the XML string
     */
    public String marshal(T t) throws JAXBException, IOException;

    /**
     * Marshals the generic JAXB object into XML and writes it to the given
     * stream.
     *
     * @param t
     *            JAXB object
     * @param out
     *            the {@link OutputStream} to write to
     * @throws JAXBException
     *             Thrown if an error was encountered processing the JAXB
     *             context or marshalling the generic type
     * @throws IOException
     *             Thrown if an error occurred in writing the XML string
     */
    public void marshal(T t, OutputStream out) throws JAXBException,
            IOException;

    /**
     * Unmarshals the XML string into JAXB objects.
     *
     * @param s
     *            XML string
     * @return JAXB object
     * @throws JAXBException
     *             Thrown if an error was encountered processing the JAXB
     *             context or unmarshalling to the generic type
     * @throws IOException
     *             Thrown if an error occurred in reading the XML string
     */
    public T unmarshal(String s) throws JAXBException, IOException;

    /**
     * Unmarshals the XML file into JAXB objects.
     *
     * @param f
     *            XML file
     * @throws JAXBException
     *             Thrown if an error was encountered processing the JAXB
     *             context or unmarshalling to the generic type
     * @throws IOException
     *             Thrown if an error occurred in reading the XML file
     */
    public T unmarshal(File f) throws JAXBException, IOException;

    /**
     * Unmarshals the XML source into JAXB objects.
     *
     * @param s
     *            XML source
     * @return JAXB object
     * @return JAXB object Thrown if an error was encountered processing the
     *         JAXB context or unmarshalling to the generic type
     * @throws IOException
     *             Thrown if an error occurred in reading the XML source
     */
    public T unmarshal(Source s) throws JAXBException, IOException;

}
