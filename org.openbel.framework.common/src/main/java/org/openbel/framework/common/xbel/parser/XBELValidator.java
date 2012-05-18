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

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import static javax.xml.validation.SchemaFactory.newInstance;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.Strings.UTF_8;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.openbel.framework.common.InvalidArgument;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Implements an XBEL validator.
 * <p>
 * This is nothing XBEL-specific about this class, apart from its name.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class XBELValidator implements XMLValidator {
    private final Schema schema;

    /**
     * Creates an XBEL validator that processes the document's defined schema
     * to validate against.
     *
     * @throws SAXException Thrown if an error is found during processing the
     * specified schemas; the current API does not provide {@link ErrorHandler
     * SAX error handler} functionality
     */
    public XBELValidator() throws SAXException {
        final SchemaFactory sf = newInstance(W3C_XML_SCHEMA_NS_URI);
        schema = sf.newSchema();
    }

    /**
     * Creates a XBEL validator associated with any number of XML schemas.
     *
     * @param schemas Paths to XML schemas
     * @throws SAXException Thrown if an error is found during processing the
     * specified schemas; the current API does not provide {@link ErrorHandler
     * SAX error handler} functionality
     * @throws InvalidArgument Thrown if schemas is null or zero-length
     */
    public XBELValidator(final String... schemas)
            throws SAXException {
        if (noItems(schemas))
            throw new InvalidArgument("schemas is null or empty");

        final List<Source> sources = new ArrayList<Source>(schemas.length);
        for (final String schema : schemas) {
            final Source s = new StreamSource(new File(schema));
            sources.add(s);
        }

        final SchemaFactory sf = newInstance(W3C_XML_SCHEMA_NS_URI);
        schema = sf.newSchema(sources.toArray(new Source[0]));
    }

    /**
     * Creates a XBEL validator associated with any number of XML schemas.
     *
     * @param schemas XML schema files
     * @throws SAXException Thrown if an error is found during processing the
     * specified schemas; the current API does not provide {@link ErrorHandler
     * SAX error handler} functionality
     * @throws InvalidArgument Thrown if schemas is null or zero-length
     */
    public XBELValidator(final File... schemas) throws SAXException {
        if (noItems(schemas))
            throw new InvalidArgument("schemas is null or empty");

        final List<Source> sources = new ArrayList<Source>(schemas.length);
        for (final File schema : schemas) {
            final Source s = new StreamSource(schema);
            sources.add(s);
        }

        final SchemaFactory sf = newInstance(W3C_XML_SCHEMA_NS_URI);
        schema = sf.newSchema(sources.toArray(new Source[0]));
    }

    /**
     * Creates a XBEL validator associated with any number of XML schemas.
     *
     * @param schemas {@link URL}s, the XML schema urls
     * @throws SAXException thrown if an error is found during processing the
     * specified schemas; the current API does not provide {@link ErrorHandler
     * SAX error handler} functionality
     * @throws URISyntaxException thrown if a {@link URL} in {@code schemas} has
     * invalid {@link URI} syntax
     * @throws IOException thrown if an i/o error occurs reading from the schema
     * url's {@link InputStream}
     * @throws InvalidArgument Thrown if schemas is null or zero-length
     */
    public XBELValidator(final URL... schemas) throws SAXException,
            IOException, URISyntaxException {
        if (noItems(schemas))
            throw new InvalidArgument("schemas is null or empty");

        final List<Source> sources = new ArrayList<Source>(schemas.length);
        for (final URL schema : schemas) {
            if (schema == null) {
                throw new InvalidArgument("XBEL schemas contain a null element");
            }

            InputStream is = schema.openStream();
            URI uri = schema.toURI();
            final Source s = new StreamSource(is, uri.toString());
            sources.add(s);
        }

        final SchemaFactory sf = newInstance(W3C_XML_SCHEMA_NS_URI);
        schema = sf.newSchema(sources.toArray(new Source[0]));
    }

    /**
     * Creates a XBEL validator associated with any number of XML schemas.
     *
     * @param schemas XML schema sources
     * @throws SAXException Thrown if an error is found during processing the
     * specified schemas; the current API does not provide {@link ErrorHandler
     * SAX error handler} functionality
     * @throws InvalidArgument thrown if
     * <ul>
     * <li>{@code schemas} is null</li>
     * <li>{@code schemas} is a zero-length array</li>
     * </ul>
     */
    public XBELValidator(final Source... schemas) throws SAXException {
        if (schemas == null || schemas.length == 0)
            throw new InvalidArgument("Invalid constructor argument schemas.");

        final SchemaFactory sf = newInstance(W3C_XML_SCHEMA_NS_URI);
        schema = sf.newSchema(schemas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final String s) {
        try {
            validate(s);
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final File f) {
        try {
            validate(f);
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final Source s) {
        try {
            validate(s);
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final String s) throws SAXException, IOException {
        final Source s2 = new StreamSource(new StringReader(s));
        validate(s2, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SAXParseException> validateWithErrors(final String s)
            throws SAXException, IOException {
        final Source s2 = new StreamSource(new StringReader(s));

        final Validator errorValidator = createNewErrorValidator();
        errorValidator.validate(s2, null);

        return ((Handler) errorValidator.getErrorHandler()).exceptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final File f) throws SAXException, IOException {
        validate(utf8SourceForFile(f), null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SAXParseException> validateWithErrors(final File f)
            throws SAXException, IOException {
        final Validator errorValidator = createNewErrorValidator();
        errorValidator.validate(utf8SourceForFile(f), null);

        return ((Handler) errorValidator.getErrorHandler()).exceptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Source s) throws SAXException, IOException {
        validate(s, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SAXParseException> validateWithErrors(final Source s)
            throws SAXException, IOException {
        final Validator errorValidator = createNewErrorValidator();
        errorValidator.validate(s, null);
        return ((Handler) errorValidator.getErrorHandler()).exceptions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final String s, final Result r) throws SAXException,
            IOException {
        final Source s2 = new StreamSource(new StringReader(s));
        validate(s2, r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final File f, final Result r) throws SAXException,
            IOException {
        validate(utf8SourceForFile(f), r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Source s, final Result r) throws SAXException,
            IOException {
        final Validator validator = createNewValidator();
        validator.validate(s, r);
    }

    /**
     * Create a new instance of a {@link Validator} based on {@link #schema}.
     *
     * @return the new instance of {@link Validator}
     */
    private Validator createNewValidator() {
        return schema.newValidator();
    }

    /**
     * Create a new instance of a {@link Validator}, based on {@link #schema},
     * that captures validation errors/warnings using {@link Handler}.
     *
     * @return the new instance of {@link Validator} that captures validation
     * errors/warnings
     */
    private Validator createNewErrorValidator() {
        final Validator validator = schema.newValidator();
        validator.setErrorHandler(new Handler());
        return validator;
    }

    /**
     * Creates a UTF-8 {@link StreamSource} for a {@link File} by reading it
     * using the UTF-8 encoding.
     *
     * @param f {@link File} to read as UTF-8
     * @return the {@link StreamSource} for <tt>f</tt>
     * @throws FileNotFoundException Thrown if <tt>f</tt> does not exist when
     * trying to read it
     * @throws UnsupportedEncodingException Thrown if <tt>UTF-8</tt> is not
     * supported
     */
    private StreamSource utf8SourceForFile(final File f)
            throws FileNotFoundException, UnsupportedEncodingException {
        FileInputStream fis = new FileInputStream(f);
        Reader reader = new InputStreamReader(fis, UTF_8);
        return new StreamSource(reader);
    }

    private static class Handler implements ErrorHandler {
        private final List<SAXParseException> exceptions =
                new ArrayList<SAXParseException>();

        /**
         * {@inheritDoc}
         */
        @Override
        public void warning(SAXParseException exception) throws SAXException {
            exceptions.add(exception);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void error(SAXParseException exception) throws SAXException {
            exceptions.add(exception);

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            exceptions.add(exception);
        }

    }
}
