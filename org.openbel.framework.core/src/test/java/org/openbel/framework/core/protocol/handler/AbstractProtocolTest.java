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
package org.openbel.framework.core.protocol.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.mina.util.AvailablePortFinder;
import org.junit.After;
import org.junit.Before;

/**
 * AbstractProtocolTest provides boilerplate setup and assertions
 * around testing resource retrieval with different supported protocols.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public abstract class AbstractProtocolTest {
    /**
     * Defines the test file to attempt retrievel on.
     */
    protected static final String TEST_FILE_PATH =
            "src/test/resources/org/openbel/framework/namespace/service/test.namespace";

    /**
     * Defines the number of times we should attempt to locate an
     * open ephemeral port.
     */
    private static final int PORT_ATTEMPTS = 100;

    /**
     * Defines the {@link List} of test {@link File}s to delete after
     * the testcase is done.
     */
    protected List<File> tempFiles;

    /**
     * Set up the test.
     */
    @Before
    public void setup() {
        tempFiles = new ArrayList<File>();
    }

    /**
     * Tear down the test.
     */
    @After
    public void teardown() {
        for (File f : tempFiles) {
            boolean deleted = f.delete();
            assert deleted;
        }
    }

    /**
     * Test the resource file to ensure it is the right file contents.
     *
     * @param resourceFile {@link File}, the resource file
     * @throws IOException - Thrown if the resource file could not be read
     */
    protected void testFile(File resourceFile) throws IOException {
        Assert.assertTrue(FileUtils.readFileToString(resourceFile).startsWith(
                "[Namespace]"));
    }

    /**
     * Retrieve an available ephemeral port given {@link #PORT_ATTEMPTS}
     * number of attempts.
     *
     * @return {@code int}, the available ephemeral port
     * @throws IllegalStateException - Thrown if no available ephemeral port
     * could be found given {@link #PORT_ATTEMPTS} number of attempts.
     */
    protected int getAvailablePort() {
        for (int i = 0; i < PORT_ATTEMPTS; i++) {
            int ephemeralPort = nextEphemeralPort();

            if (AvailablePortFinder.available(ephemeralPort)) {
                return ephemeralPort;
            }
        }

        throw new IllegalStateException(
                "Cannot obtain open, ephemeral port in 100 tries.");
    }

    /**
     * Generates a random port number between 1024 - 11024.
     *
     * @return {@code int}, the next random ephemeral port
     */
    private int nextEphemeralPort() {
        return new Random().nextInt(10000) + 1024;
    }
}
