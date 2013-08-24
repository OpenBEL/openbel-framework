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
