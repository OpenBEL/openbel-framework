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
package org.openbel.framework.core.protocol.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
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
}
