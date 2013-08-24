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

import static java.lang.System.out;
import static junit.framework.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openbel.framework.core.XBELValidatorService;
import org.openbel.framework.core.XBELValidatorServiceImpl;
import org.openbel.framework.core.compiler.ValidationError;

/**
 * Test validation service.
 *
 */
public class XBELValidatorServiceImplTest {

    private final static String ROOT = "../docs/xbel/";
    private final static String XBEL_XSD = ROOT + "xbel.xsd";
    private final static String ANNO_XSD = ROOT + "xbel-annotations.xsd";
    private XBELValidatorService xvs;

    /**
     * @throws Exception
     */
    @BeforeClass
    public static void xsdCheck() throws Exception {
        if (!new File(XBEL_XSD).canRead())
            fail("can't read " + XBEL_XSD);
        if (!new File(ANNO_XSD).canRead())
            fail("can't read " + ANNO_XSD);

        out.println("XSD configuration:");
        out.println("\tRoot: ".concat(ROOT));
        out.println("\tXSD 1: ".concat(XBEL_XSD));
        out.println("\tXSD 2: ".concat(ANNO_XSD));
    }

    /**
     * Test case setup.
     */
    @Before
    public void setup() throws Exception {
        xvs = new XBELValidatorServiceImpl();
    }

    @Test(expected = ValidationError.class)
    public void test() throws ValidationError {
        final String testxml =
                ROOT + "examples/invalid/beldocument_invalid.xbel";
        final File testFile = new File(testxml);
        if (!testFile.canRead())
            fail("can't read " + testxml);

        xvs.validate(testFile);
    }

}
