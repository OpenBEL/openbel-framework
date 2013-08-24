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
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Defines an XML validator to validate for well-formed and rule-conforming XML.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface XMLValidator {

    /**
     * Returns {@code true} if the provided string is valid XML, {@code false}
     * otherwise.
     *
     * @param string XML string
     * @return boolean
     */
    public boolean isValid(String string);

    /**
     * Returns {@code true} if the provided file is valid XML, {@code false}
     * otherwise.
     *
     * @param file XML file
     * @return boolean
     */
    public boolean isValid(File file);

    /**
     * Returns {@code true} if the provided source is valid XML, {@code false}
     * otherwise.
     *
     * @param source XML source
     * @return boolean
     */
    public boolean isValid(Source source);

    /**
     * Validates the provided XML string against this validator's schema.
     *
     * @param s String to validate against the schema; assumed to be non-null
     * @throws SAXException Thrown by the SAX error handler, or if a fatal error
     * occurred; an embedded exception may be present in the SAX exception, see
     * {@link SAXException#getException()}
     * @throws IOException Thrown by the underlying reader (or stream)
     * processing the XML string
     */
    public void validate(String s) throws SAXException, IOException;

    /**
     * Validates the provided XML strings against this validator's schema.
     *
     * @param s String to validate against the schema; assumed to be non-null
     * @return A non-null list of SAX parse exceptions
     * @throws SAXException Thrown by the SAX error handler, or if a fatal error
     * occurred; an embedded exception may be present in the SAX exception, see
     * {@link SAXException#getException()}
     * @throws IOException Thrown by the underlying reader (or stream)
     * processing the XML string
     */
    public List<SAXParseException> validateWithErrors(String s)
            throws SAXException, IOException;

    /**
     * Validates the provided XML file against this validator's schema.
     *
     * @param f File to validate against the schema; assumed to be non-null and
     * readable
     * @throws SAXException Thrown by the SAX error handler, or if a fatal error
     * occurred; an embedded exception may be present in the SAX exception, see
     * {@link SAXException#getException()}
     * @throws IOException Thrown by the underlying stream processing the XML
     * file
     */
    public void validate(File f) throws SAXException, IOException;

    /**
     * Validates the provided XML file against this validator's schema.
     *
     * @param f File to validate against the schema; assumed to be non-null and
     * readable
     * @return A non-null list of SAX parse exceptions
     * @throws SAXException Thrown by the SAX error handler, or if a fatal error
     * occurred; an embedded exception may be present in the SAX exception, see
     * {@link SAXException#getException()}
     * @throws IOException Thrown by the underlying stream processing the XML
     * file
     */
    public List<SAXParseException> validateWithErrors(File f)
            throws SAXException, IOException;

    /**
     * Validates the provided XML source against this validator's schema.
     *
     * @param s Source to validate against the schema; assumed to be non-null
     * @throws SAXException Thrown by the SAX error handler, or if a fatal error
     * occurred; an embedded exception may be present in the SAX exception, see
     * {@link SAXException#getException()}
     * @throws IOException Thrown by the underlying stream processing the XML
     * file
     */
    public void validate(Source s) throws SAXException, IOException;

    /**
     * Validates the provided XML source against this validator's schema.
     *
     * @param s Source to validate against the schema; assumed to be non-null
     * @return A non-null list of SAX parse exceptions
     * @throws SAXException Thrown by the SAX error handler, or if a fatal error
     * occurred; an embedded exception may be present in the SAX exception, see
     * {@link SAXException#getException()}
     * @throws IOException Thrown by the underlying stream processing the XML
     * file
     */
    public List<SAXParseException> validateWithErrors(Source s)
            throws SAXException, IOException;

    /**
     * Validates the provided XML string against this validator's schema,
     * placing XML results in the provided object {@code r}.
     *
     * @param s String to validate against the schema; assumed to be non-null
     * @param r The result object that receives the XML; may be null
     * @throws IOException Thrown by the underlying reader (or stream)
     * processing the XML string
     * @throws SAXException Thrown by the SAX error handler, or if a fatal error
     * occurred; an embedded exception may be present in the SAX exception, see
     * {@link SAXException#getException()}
     */
    public void validate(String s, Result r) throws SAXException, IOException;

    /**
     * Validates the provided XML file against this validator's schema, placing
     * XML results in the provided object {@code r}.
     *
     * @param f File to validate against the schema; assumed to be non-null and
     * readable
     * @param r The result object that receives the XML; may be null
     * @throws IOException Thrown by the underlying stream processing the XML
     * file {@link SAXException#getException()}
     * @throws SAXException Thrown by the SAX error handler, or if a fatal error
     * occurred; an embedded exception may be present in the SAX exception, see
     * {@link SAXException#getException()}
     */
    public void validate(File f, Result r) throws SAXException, IOException;

    /**
     * Validates the provided XML source against the validator's schema, placing
     * XML results in the provided object {@code r}.
     *
     * @param s Source to validate against the schema; assumed to be non-null
     * @param r The result object that receives the XML; may be null
     * @throws IOException Thrown by the underlying stream processing the XML
     * file
     * @throws SAXException Thrown by the SAX error handler, or if a fatal error
     * occurred; an embedded exception may be present in the SAX exception, see
     * {@link SAXException#getException()}
     */
    public void validate(Source s, Result r) throws SAXException, IOException;
}
