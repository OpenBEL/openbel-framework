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

import static java.util.Arrays.asList;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.openbel.framework.common.xbel.parser.XBELValidator;
import org.openbel.framework.core.compiler.ValidationError;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * XBEL XML validation service.
 */
public class XBELValidatorServiceImpl implements XBELValidatorService {
    private final XBELValidator xv;

    /**
     * Creates an XBEL validator service implementation validating against the
     * schemas in the input XML document.
     *
     * @throws SAXException thrown if an error is found during processing the
     * specified schemas; the current API does not provide
     * {@link org.xml.sax.ErrorHandler SAX error handler} functionality
     * @throws MalformedURLException thrown if the schema file {@link URL}s
     * syntax is malformed
     * @throws URISyntaxException thrown if a {@link URL} in {@code schemas} has
     * invalid {@link URI} syntax
     * @throws IOException thrown if an i/o error occurs reading from the schema
     * url's {@link InputStream}
     */
    public XBELValidatorServiceImpl() throws SAXException,
            MalformedURLException, IOException, URISyntaxException {
        xv = new XBELValidator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(String s) {
        return xv.isValid(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(File f) {
        return xv.isValid(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(String s) throws ValidationError {
        try {
            xv.validate(s);
        } catch (SAXParseException e) {
            final String name = e.getSystemId();
            final String msg = e.getMessage();
            final int line = e.getLineNumber();
            final int column = e.getColumnNumber();
            throw new ValidationError(name, msg, e, line, column);
        } catch (SAXException e) {
            // TODO This isn't the intended design here
            throw new RuntimeException(e);
        } catch (IOException e) {
            // TODO This isn't the intended design here
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> validateWithErrors(String s) {
        List<SAXParseException> saxErrors;
        try {
            saxErrors = xv.validateWithErrors(s);
        } catch (SAXException e) {
            // TODO This isn't the intended design here
            throw new RuntimeException(e);
        } catch (IOException e) {
            // TODO This isn't the intended design here
            throw new RuntimeException(e);
        }

        List<ValidationError> ret = sizedArrayList(saxErrors.size());
        for (final SAXParseException saxError : saxErrors) {
            final String name = saxError.getSystemId();
            final String msg = saxError.getMessage();
            final int line = saxError.getLineNumber();
            final int column = saxError.getColumnNumber();
            ret.add(new ValidationError(name, msg, saxError, line, column));
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(File f) throws ValidationError {
        try {
            xv.validate(f);
        } catch (SAXParseException e) {
            final String name = e.getSystemId();
            final String msg = e.getMessage();
            final int line = e.getLineNumber();
            final int column = e.getColumnNumber();
            throw new ValidationError(name, msg, e, line, column);
        } catch (SAXException e) {
            // TODO This isn't the intended design here
            throw new RuntimeException(e);
        } catch (IOException e) {
            // TODO This isn't the intended design here
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> validateWithErrors(File f) {
        List<SAXParseException> saxErrors;
        try {
            saxErrors = xv.validateWithErrors(f);
        } catch (SAXParseException e) {
            final String name = e.getSystemId();
            final String msg = e.getMessage();
            final int line = e.getLineNumber();
            final int column = e.getColumnNumber();
            return asList(new ValidationError(name, msg, e, line, column));
        } catch (SAXException e) {
            final String name = f.toString();
            final String msg = e.getMessage();
            return asList(new ValidationError(name, msg, e, -1, -1));
        } catch (IOException e) {
            final String name = f.toString();
            final String msg = e.getMessage();
            return asList(new ValidationError(name, msg, e, -1, -1));
        }

        List<ValidationError> ret = sizedArrayList(saxErrors.size());
        for (final SAXParseException saxError : saxErrors) {
            final String name = saxError.getSystemId();
            final String msg = saxError.getMessage();
            final int line = saxError.getLineNumber();
            final int column = saxError.getColumnNumber();
            ret.add(new ValidationError(name, msg, saxError, line, column));
        }
        return ret;
    }
}
