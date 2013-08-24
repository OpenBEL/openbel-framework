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

import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.openbel.framework.common.model.CommonModelFactory;
import org.openbel.framework.common.model.Evidence;

/**
 * {@link Evidence} unit tests.
 *
 */
public class EvidenceTest {

    /**
     * Tests {@link Evidence#copy()}.
     */
    @Test
    public void testCopy() {
        Evidence e1 = CommonModelFactory.getInstance().createEvidence(
                valueOf(currentTimeMillis()));
        Evidence e2 = e1.clone();
        assertEquals(e1, e2);
        assertFalse(e1 == e2);

        Set<Object> set = new HashSet<Object>();
        set.add(e1);
        set.add(e2);
        assertEquals(1, set.size());
    }
}
