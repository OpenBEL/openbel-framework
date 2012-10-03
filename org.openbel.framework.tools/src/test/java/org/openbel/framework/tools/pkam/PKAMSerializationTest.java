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
package org.openbel.framework.tools.pkam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.SystemConfigurationFile;
import org.openbel.framework.common.cfg.SystemConfigurationBasedTest;

/**
 * PKAMSerializationTest provides a {@link SystemConfigurationBasedTest} to
 * assert that PKAM serialization and PKAM deserialization produce equal data.
 *
 * TODO Test UTF-8 encoded data.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
@SystemConfigurationFile(path = "src/test/resources/org/openbel/framework/tools/pkam/belframework.cfg")
public class PKAMSerializationTest extends SystemConfigurationBasedTest {

    /**
     * {@inheritDoc}
     *
     * Setup the spring application context.
     */
    @Before
    @Override
    public void setup() {
        super.setup();
    }

    /**
     * {@inheritDoc}
     *
     * Teardown the spring application context.
     */
    @After
    @Override
    public void teardown() {
        super.teardown();
    }

    /**
     * Test that writing a PKAM then reading that PKAM produces equal data.
     */
    @Test
    public void testPKAMWriteAndRead() {
        // set up temporary file for PKAM, assert existence
        final String tempPath;
        try {
            File pkamTempFile = File.createTempFile("PKAM", ".kam");
            assertTrue(pkamTempFile.exists());

            // set temp file to be deleted on JVM exit
            pkamTempFile.deleteOnExit();

            // retrieve the temp file path for the PKAMWriter and PKAMReader
            tempPath = pkamTempFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }

        // write PKAM data
        try {
            PKAMWriter writer = new PKAMWriter(tempPath);
            writer.write("Test data 1, Test data 2, Test data 3");
            writer.write("\n");
            writer.write("Test data 4, Test data 5, Test data 6");
            writer.write("\n");
            writer.write("Test data 7, Test data 8, Test data 9");
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }

        // read PKAM data and assert it equals the original data
        try {
            PKAMReader reader = new PKAMReader(tempPath);
            assertEquals("Test data 1, Test data 2, Test data 3",
                    reader.readLine());
            assertEquals("Test data 4, Test data 5, Test data 6",
                    reader.readLine());
            assertEquals("Test data 7, Test data 8, Test data 9",
                    reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }

    }
}
