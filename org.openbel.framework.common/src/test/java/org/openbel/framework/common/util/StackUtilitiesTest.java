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
package org.openbel.framework.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openbel.framework.common.util.StackUtilities;

/**
 * Stack utilities unit tests.
 */
public class StackUtilitiesTest {

    /**
     * {@link StackUtilities#myFrame()} test.  This test asserts a specific
     * line number of this file so be aware when adding to this class.
     */
    @Test
    public void test() {
        StackTraceElement myFrame = StackUtilities.myFrame();
        assertEquals(34, myFrame.getLineNumber());
        assertEquals(getClass().getName(), myFrame.getClassName());
        assertEquals("StackUtilitiesTest.java", myFrame.getFileName());
        assertEquals("test", myFrame.getMethodName());
    }

}
