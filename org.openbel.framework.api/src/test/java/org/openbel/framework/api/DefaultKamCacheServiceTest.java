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
package org.openbel.framework.api;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.openbel.framework.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KamDbObject;

/**
 * {@link DefaultKamCacheService} test cases.
 */
public class DefaultKamCacheServiceTest {

    /**
     * Test cache mechanisms.
     */
    @Test
    public void test() {
        KamCacheService kcSvc = new DefaultKamCacheService(new MockKAMStore1());
        KamDbObject kdbobj = new KamDbObject(1, null, null, null, null);
        KamInfo ki = new KamInfo(kdbobj);
        long t1 = currentTimeMillis();
        try {
            kcSvc.loadKam(ki, null);
        } catch (KamCacheServiceException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        long t2 = currentTimeMillis();

        // CACHE MISS ASSERTION
        String msg = "expected load of KAM to take more than 5 seconds";
        assertTrue(msg, (t2 - t1) >= (MockKAMStore1.KAM_LOAD_TIME * 1000));

        String handle = null;
        try {
            handle = kcSvc.loadKam(ki, null);
        } catch (KamCacheServiceException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        long t3 = currentTimeMillis();

        // CACHE HIT ASSERTION
        msg = "expected load of KAM to take less than 5 seconds";
        assertTrue(msg, (t3 - t2) < (MockKAMStore1.KAM_LOAD_TIME * 1000));

        kcSvc.releaseKam(handle);

        long t4 = currentTimeMillis();
        try {
            kcSvc.loadKam(ki, null);
        } catch (KamCacheServiceException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        long t5 = currentTimeMillis();

        // CACHE MISS ASSERTION
        msg = "expected second load of KAM to take more than 5 seconds";
        assertTrue(msg, (t5 - t4) >= (MockKAMStore1.KAM_LOAD_TIME * 1000));
    }

    static class MockKAMStore1 extends MockKamStore {
        // Time to load "KAM" 5
        static final int KAM_LOAD_TIME = 5;

        @Override
        public Kam getKam(KamInfo info, KamFilter fltr)
                throws KamStoreException {
            try {
                sleep(KAM_LOAD_TIME * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Kam getKam(KamInfo info) throws KamStoreException {
            try {
                sleep(KAM_LOAD_TIME * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
