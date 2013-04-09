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
