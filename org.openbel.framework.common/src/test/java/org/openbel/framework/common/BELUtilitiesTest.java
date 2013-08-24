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
package org.openbel.framework.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openbel.framework.common.BELUtilities.computeHashSHA256;
import static org.openbel.framework.common.BELUtilities.getFirstMessage;
import static org.openbel.framework.common.BELUtilities.getPID;
import static org.openbel.framework.common.BELUtilities.getThreadID;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.openbel.framework.common.BELUtilities;

/**
 * {@link BELUtilities} test cases.
 */
public class BELUtilitiesTest {

    /**
     * Test {@link BELUtilities#getPID()}.
     */
    @Test
    public void testGetPid() {
        assertFalse(getPID() == -1);
    }

    /**
     * Test {@link BELUtilities#getThreadID()}.
     */
    @Test
    public void testGetThreadID() {
        assertTrue(getThreadID() > 0);
    }

    /**
     * Test {@link BELUtilities#computeHashSHA256(InputStream)}.
     */
    @Test
    public void testSHA256Hash() throws IOException {
        final String checksum = "e96530c04be684f1a468a98d8ab8174bba704503fb6" +
                "9e1ccb5376bfe3f8390c6";

        final InputStream input = BELUtilitiesTest.class
                .getResourceAsStream("test.belns");
        assertEquals(checksum, computeHashSHA256(input));
    }

    /**
     * Test {@link BELUtilities#getFirstMessage(Throwable)}.
     */
    @Test
    public void testGetFirstMessage() {
        Exception thrown = new Exception("the thrown exception");
        Exception cause = new Exception("the cause");
        thrown.initCause(cause);
        assertEquals("the cause", getFirstMessage(thrown));

        thrown = new Exception("the thrown exception");
        Exception middle = new Exception("intermediate exception");
        cause = new Exception("the cause");
        thrown.initCause(middle);
        middle.initCause(cause);
        assertEquals("the cause", getFirstMessage(thrown));

        thrown = new Exception("the thrown exception");
        cause = new Exception();
        thrown.initCause(cause);
        assertEquals("the thrown exception", getFirstMessage(thrown));
    }
}
