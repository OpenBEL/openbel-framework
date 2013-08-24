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
import static junit.framework.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openbel.bel.xbel.model.XBELDocument;
import org.openbel.framework.common.xbel.parser.XBELValidatingConverter;
import org.xml.sax.SAXException;

/**
 * XBELValidatingConverter unit tests.
 *
 */
public class XBELValidatingConverterTest {

    private final static String XSD_ROOT = "target/test-classes/schemas/";
    private final static String XBEL_XSD = XSD_ROOT + "xbel.xsd";
    private final static String ANNO_XSD = XSD_ROOT + "xbel-annotations.xsd";
    private final static String XML_ROOT = "../docs/xbel/examples/valid/";
    XBELValidatingConverter xvc;

    /**
     * Ensure XSDs can be read.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
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
            xvc = new XBELValidatingConverter();
        } catch (SAXException e) {
            String err = "SAX exception validating XSDs";
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        } catch (MalformedURLException e) {
            String err = "Malformed URL exception validating XSDs";
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        } catch (URISyntaxException e) {
            String err = "URI Syntax exception validating XSDs";
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        } catch (IOException e) {
            String err = "IO exception validating XSDs";
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        } catch (JAXBException e) {
            String err = "JAXB exception validating XSDs";
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        }
    }

    /**
     * Template test case.
     */
    @Test
    public void testTemplate() {
        final String testxml = XML_ROOT + "beldocument_template.xbel";
        final File testFile = new File(testxml);
        if (!testFile.canRead())
            fail("can't read " + testxml);

        try {
            XBELDocument d = xvc.unmarshal(testFile);
            assertNotNull(d);
        } catch (JAXBException e) {
            String err = "JAXB exception unmarshalling " + testxml;
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        } catch (IOException e) {
            String err = "IO exception unmarshalling " + testxml;
            err += ", exception message follows:\n\t";
            err += e.getMessage();
            fail(err);
        }
    }

}
