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
