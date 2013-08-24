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

import junit.framework.Assert;

import org.junit.Test;
import org.openbel.framework.core.protocol.handler.FileProtocolHandler;

/**
 * {@link FileLoaderTest} tests the {@link FileProtocolHandler}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class FileLoaderTest extends AbstractProtocolTest {

    /**
     * Test the successful use of a relative file url.
     */
    @Test
    public void testRelativeFilePath() {
        try {
            File f =
                    new FileProtocolHandler().downloadResource("file:"
                            + TEST_FILE_PATH, "test.belns");
            tempFiles.add(f);
        } catch (Exception e) {
            Assert.fail("Error reading relative file path: " + e.getMessage());
        }
    }

    /**
     * Test the successful use of an absolute file url.
     */
    @Test
    public void testAbsoluteFilePath() {
        try {
            String canonicalPath = new File(".").getCanonicalPath();
            File f =
                    new FileProtocolHandler().downloadResource("file:"
                            + canonicalPath + "/" + TEST_FILE_PATH,
                            "test.belns");
            tempFiles.add(f);
        } catch (Exception e) {
            Assert.fail("Error reading relative file path: " + e.getMessage());
        }
    }
}
