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
package org.openbel.framework.tools.pkam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.SystemConfigurationFile;
import org.openbel.framework.common.cfg.SystemConfigurationBasedTest;
import org.openbel.framework.core.df.encryption.EncryptionServiceException;
import org.openbel.framework.core.df.encryption.KamStoreEncryptionServiceImpl;

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
    private KamStoreEncryptionServiceImpl encryptionService;

    /**
     * {@inheritDoc}
     *
     * Setup the spring application context.
     */
    @Before
    @Override
    public void setup() {
        super.setup();
        encryptionService = new KamStoreEncryptionServiceImpl();
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
        assertNotNull(encryptionService);

        // set up temporary file for encrypted PKAM, assert existence
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
            PKAMWriter writer = new PKAMWriter(tempPath, "", encryptionService);
            writer.write("Test data 1, Test data 2, Test data 3");
            writer.write("\n");
            writer.write("Test data 4, Test data 5, Test data 6");
            writer.write("\n");
            writer.write("Test data 7, Test data 8, Test data 9");
            writer.write("\n");
            writer.close();
        } catch (EncryptionServiceException e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }

        // read PKAM data and assert it equals the original data
        try {
            PKAMReader reader = new PKAMReader(tempPath, "", encryptionService);
            assertEquals("Test data 1, Test data 2, Test data 3",
                    reader.readLine());
            assertEquals("Test data 4, Test data 5, Test data 6",
                    reader.readLine());
            assertEquals("Test data 7, Test data 8, Test data 9",
                    reader.readLine());
        } catch (EncryptionServiceException e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }

    }
}
