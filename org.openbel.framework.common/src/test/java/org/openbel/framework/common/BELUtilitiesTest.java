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
