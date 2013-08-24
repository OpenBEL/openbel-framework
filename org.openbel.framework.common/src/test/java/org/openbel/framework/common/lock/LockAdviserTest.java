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
package org.openbel.framework.common.lock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.lock.LockAdviser;
import org.openbel.framework.common.lock.LockAdviser.Lock;

/**
 * TODO Provide documentation for unit tests
 *
 * @author abargnesi
 */
public class LockAdviserTest {
    private static final String TEST_LOCK_DIR = "lockPath";
    private File lockDir;

    @Before
    public void setup() {
        this.lockDir = new File(TEST_LOCK_DIR);
        if (!lockDir.mkdir()) {
            fail(TEST_LOCK_DIR + " directory could not be created");
        }

        assertTrue(lockDir.exists());
        assertTrue(lockDir.isDirectory());
    }

    @After
    public void teardown() {
        assertNotNull(lockDir);

        if (!lockDir.delete()) {
            fail(TEST_LOCK_DIR + " directory could not be deleted, " +
                    "does directory have any contents?");
        }
    }

    @Test
    public void testMultipleReadLocks() {
        final LockAdviser lockAdviser = LockAdviser.instance(lockDir);

        final Lock rlock1;
        final Lock rlock2;
        try {
            rlock1 = lockAdviser.obtainReadLock();
            rlock2 = lockAdviser.obtainReadLock();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }

        assertNotNull(rlock1);
        assertNotNull(rlock2);

        try {
            lockAdviser.releaseReadLock(rlock1);
            lockAdviser.releaseReadLock(rlock2);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertEquals(0, lockDir.listFiles().length);
    }

    @Test
    public void testSingleWriteLock() {
        final LockAdviser lockAdviser = LockAdviser.instance(lockDir);

        final Lock wlock1;
        final Lock wlock2;
        try {
            wlock1 = lockAdviser.obtainWriteLock();
            wlock2 = lockAdviser.obtainWriteLock();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }

        assertNotNull(wlock1);
        assertNull(wlock2);

        try {
            lockAdviser.releaseWriteLock(wlock1);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertEquals(0, lockDir.listFiles().length);
    }

    @Test
    public void testReleaseLockDeletion() {
        final LockAdviser lockAdviser = LockAdviser.instance(lockDir);

        final Lock rlock;
        try {
            rlock = lockAdviser.obtainReadLock();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }

        assertNotNull(rlock);

        try {
            lockAdviser.releaseReadLock(rlock);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertEquals(0, lockDir.listFiles().length);
    }

    @Test
    public void testInitialReadLock() {
        // create initial read lock
        final File readLock = new File(lockDir, "rdr_111111.lck1");
        try {
            if (!readLock.createNewFile()) {
                fail("Could not create initial read lock");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        final LockAdviser lockAdviser = LockAdviser.instance(lockDir);

        // attempt write lock, which should return null
        final Lock wlock;
        try {
            wlock = lockAdviser.obtainWriteLock();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }
        assertNull(wlock);

        // remove read lock file
        if (!readLock.delete()) {
            fail("Could not delete initial read lock");
        }
    }

    @Test
    public void testInitialWriteLock() {
        // create initial write lock
        final File writeLock = new File(lockDir, "wtr_111111.lck1");
        try {
            if (!writeLock.createNewFile()) {
                fail("Could not create initial write lock");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        final LockAdviser lockAdviser = LockAdviser.instance(lockDir);

        // attempt read lock, which should return null
        final Lock rlock;
        try {
            rlock = lockAdviser.obtainReadLock();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            return;
        }
        assertNull(rlock);

        // remove read lock file
        if (!writeLock.delete()) {
            fail("Could not delete initial write lock");
        }
    }

    @Test
    public void testMultipleAdvisers() {

    }
}
