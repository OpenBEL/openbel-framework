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

import static java.lang.System.out;
import static junit.framework.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openbel.framework.common.xbel.parser.XBELValidator;
import org.xml.sax.SAXException;

/**
 * Test proper successful and unsuccessful validation of XBEL test files.
 *
 * @author abargnesi
 */
public class XBELValidationTest {

    private final static String XSD_ROOT = "target/test-classes/schemas/";
    private final static String XBEL_XSD = XSD_ROOT + "xbel.xsd";
    private final static String ANNO_XSD = XSD_ROOT + "xbel-annotations.xsd";
    private final static String XML_ROOT = "../docs/xbel/examples/";
    XBELValidator xv;

    /**
     * Ensure XSDs can be read.
     */
    @BeforeClass
    public static void xsdCheck() {
        if (!new File(XBEL_XSD).canRead())
            fail("can't read " + XBEL_XSD);
        if (!new File(ANNO_XSD).canRead())
            fail("can't read " + ANNO_XSD);

        out.println("XSD configuration:");
        out.println("\tRoot: ".concat(XSD_ROOT));
        out.println("\tXSD 1: ".concat(XBEL_XSD));
        out.println("\tXSD 2: ".concat(ANNO_XSD));
    }

    /**
     * Test case setup.
     */
    @Before
    public void setup() {
        try {
            xv = new XBELValidator(XBEL_XSD, ANNO_XSD);
        } catch (SAXException e) {
            String err = "SAX exception validating XSDs";
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        }
    }

    /**
     * Test successful validation of an empty XBEL file.
     */
    @Test
    public void testEmptyDocumentValidatesSuccessfully() {
        final String testxml = XML_ROOT + "valid/beldocument_simple.xbel";
        final File testFile = new File(testxml);
        if (!testFile.canRead())
            fail("can't read " + testxml);

        try {
            xv.validate(testFile);
        } catch (SAXException e) {
            String err = "SAX exception validating " + testxml;
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        } catch (IOException e) {
            String err = "IO exception validating " + testxml;
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        }
    }

    /**
     * Test successful validation of a knowledge taken from a book publication.
     */
    @Test
    public void testBookCitationValidatesSuccessfully() {
        final String testxml = XML_ROOT + "valid/beldocument_book.xbel";
        final File testFile = new File(testxml);
        if (!testFile.canRead())
            fail("can't read " + testxml);

        try {
            xv.validate(testFile);
        } catch (SAXException e) {
            String err = "SAX exception validating " + testxml;
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        } catch (IOException e) {
            String err = "IO exception validating " + testxml;
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        }
    }

    @Test
    public void testInvalidXBELThrowsErrors() {
        final String testxml = XML_ROOT + "invalid/beldocument_invalid.xbel";
        final File testFile = new File(testxml);
        if (!testFile.canRead())
            fail("can't read " + testxml);

        try {
            xv.validate(testFile);
            String err = "SAX exception not thrown invalidating " + testxml;
            fail(err);
        } catch (SAXException e) {
            // Success
        } catch (IOException e) {
            String err = "IO exception invalidating " + testxml;
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        }
    }
}
