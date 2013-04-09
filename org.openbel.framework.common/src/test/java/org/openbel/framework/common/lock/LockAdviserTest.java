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
