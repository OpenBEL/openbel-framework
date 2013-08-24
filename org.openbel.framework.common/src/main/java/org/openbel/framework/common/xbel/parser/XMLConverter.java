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
