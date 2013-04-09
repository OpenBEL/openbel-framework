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
