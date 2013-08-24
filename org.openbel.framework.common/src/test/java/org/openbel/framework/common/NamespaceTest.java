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

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.openbel.framework.common.model.Namespace;

/**
 * {@link Namespace} unit tests.
 *
 */
public class NamespaceTest {

    /**
     * Tests {@link Namespace#copy()}.
     */
    @Test
    public void testCopy() {
        Namespace n1 = new Namespace("prefix", "resourceLocation");
        Namespace n2 = n1.clone();
        assertEquals(n1, n2);
        assertFalse(n1 == n2);

        Set<Object> set = new HashSet<Object>();
        set.add(n1);
        set.add(n2);
        assertEquals(1, set.size());
    }

}
