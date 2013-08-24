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

import static org.openbel.framework.common.BELUtilities.getPID;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.openbel.framework.common.InvalidArgument;

/**
 * {@link LockAdviser} is responsible for obtaining and releasing {@link Lock
 * file locks} for read and write access. The {@link Lock file locks} are stored
 * on disk at the specified {@link File lock path directory}. This locking
 * mechanism allows for multiple read locks to be obtained, but only one write
 * lock.
 * <p>
 * Each unique {@link File lock path directory} will have a single instance of
 * {@link LockAdviser lock adviser}. {@link LockAdviser} is thread-safe which
 * allows multiple threads to obtain and release {@link Lock locks} from the
 * same {@link LockAdviser lock adviser instance} without concurrency problems.
 * </p>
 * TODO Document Lockable implementations with LockAdviser-specific details.
 * TODO Review and refactor if necessary. TODO Document fully once you're happy
 * with the design.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class LockAdviser implements Lockable {
    private static final String LOCK_EXTENSION = ".lck";
    private static final String READER_LOCK_PREFIX = "rdr_";
    private static final String WRITER_LOCK_PREFIX = "wtr_";
    private static LockFileFilter lockFilter;
    private static Map<File, LockAdviser> instances;
    private final Map<String, Lock> locks;
    private final File lockPath;

    /**
     * Private constructor for {@link LockAdviser lock advisers} that uses a
     * {@link File lock path directory} where {@link Lock read / write locks}
     * will be stored.
     *
     * @param lockPath {@link File}, the lock path directory where {@link Lock
     * read / write locks} will be strored, which cannot be <tt>null</tt>, must
     * exist as a directory, and must be readable and writeable.
     * @throws InvalidArgument Thrown if <tt>lockPath</tt> is <tt>null</tt>,
     * does not exist as a directory, or is not readable or writeable.
     */
    private LockAdviser(final File lockPath) {
        if (lockPath == null) {
            throw new InvalidArgument("lockPath", lockPath);
        }

        if (!lockPath.exists() || !lockPath.isDirectory()) {
            throw new InvalidArgument(
                    "lockPath directory does not exist");
        }

        if (!lockPath.canRead() || !lockPath.canWrite()) {
            throw new InvalidArgument(
                    "lockPath is not readable/writeable");
        }

        this.lockPath = lockPath;
        this.locks = new HashMap<String, LockAdviser.Lock>();
    }

    public synchronized static final LockAdviser instance(final File dirPath) {
        if (instances == null) {
            lockFilter = new LockFileFilter();
            instances = new HashMap<File, LockAdviser>();
            Runtime.getRuntime().addShutdownHook(new Thread(new LockCleanup()));
        }

        LockAdviser lr = instances.get(dirPath);
        if (lr == null) {
            lr = new LockAdviser(dirPath);
            instances.put(dirPath, lr);
        }

        return lr;
    }

    /**
     * {@inheritDoc}
     * <p>
     * A {@link Lock read lock} cannot be obtained if the {@link Lock write
     * lock} has been obtained.
     * </p>
     */
    @Override
    public synchronized Lock obtainReadLock() {
        File writeLock = getWriteLock();

        if (writeLock != null && writeLock.exists()) {
            return null;
        }

        return createLock(false);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This will create a {@link File write lock file} at
     * {@link LockAdviser#lockPath} that represents the write lock.
     * </p>
     * <p>
     * A {@link Lock write lock} cannot be obtained if a {@link Lock read lock}
     * is held.
     * </p>
     */
    @Override
    public synchronized Lock obtainWriteLock() {
        Set<File> readLocks = getReadLocks();
        if (readLocks.isEmpty()) {
            final File writeLock = getWriteLock();
            if (writeLock == null) {
                return createLock(true);
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This will remove the {@link File read lock file} in
     * {@link LockAdviser#readLocks} that represents the read lock.
     * </p>
     */
    @Override
    public synchronized void releaseReadLock(final Lock lock) {
        if (lock == null || lock.name == null || lock.name.isEmpty()) {
            throw new InvalidArgument("lock is invalid");
        }

        if (locks.containsKey(lock.name)) {
            locks.remove(lock.name);

            final Set<File> readLocks = getReadLocks();
            for (final File readLock : readLocks) {
                if (readLock.getName().endsWith(lock.name)) {
                    if (!readLock.delete()) {
                        throw new IllegalStateException(
                                "read lock could not be deleted");
                    }

                    readLocks.remove(readLock);
                    return;
                }
            }
        }

        throw new InvalidArgument("read lock '" + lock.name
                + "' is unknown");
    }

    /**
     * {@inheritDoc}
     * <p>
     * This will remove the {@link File read lock file} in
     * {@link LockAdviser#readLocks} that represents the read lock.
     * </p>
     */
    @Override
    public synchronized void releaseWriteLock(final Lock lock) {
        if (lock == null || lock.name == null || lock.name.isEmpty()) {
            throw new InvalidArgument("lock is invalid");
        }

        locks.remove(lock.name);

        final File writeLock = getWriteLock();
        if (!writeLock.delete()) {
            throw new IllegalStateException(
                    "write lock could not be deleted");
        }
    }

    /**
     * Reads {@link LockAdviser#lockPath lock path directory} and finds the
     * {@link File read lock files}, if any.
     *
     * @return the {@link Set set} of {@link read lock files}, or an empty
     * {@link Set} if no read locks exist
     */
    private Set<File> getReadLocks() {
        final Set<File> readLocks = new HashSet<File>();
        File[] lckFiles = this.lockPath.listFiles(lockFilter);
        for (final File lckFile : lckFiles) {
            if (lockFilter.isReaderLockFile(lckFile)) {
                readLocks.add(lckFile);
            }
        }

        return readLocks;
    }

    /**
     * Reads {@link LockAdviser#lockPath lock path directory} and finds the
     * {@link File write lock file}, if any.
     *
     * @return the {@link write lock file}, or <tt>null</tt> if no write lock
     * exists
     */
    private File getWriteLock() {
        File[] lckFiles = this.lockPath.listFiles(lockFilter);
        for (final File lckFile : lckFiles) {
            if (lockFilter.isWriterLockFile(lckFile)) {
                return lckFile;
            }
        }

        return null;
    }

    /**
     * Creates a {@link Lock lock} on the this instance of the
     * {@link LockAdviser}. If <tt>writer</tt> is false then a {@link File read
     * lock file} is created, otherwise a {@link File write lock file is
     * created}.
     *
     * @param writer, <tt>true</tt> if a {@link File write lock file} should be
     * created, <tt>false</tt> otherwise
     * @return the created {@link Lock}
     * @throws IllegalStateException Thrown if the {@link File lock file} could
     * not be created
     */
    private Lock createLock(boolean writer) {
        final int pid = getPID();

        final String baseLock;
        if (!writer) {
            baseLock = READER_LOCK_PREFIX + pid + LOCK_EXTENSION;
        } else {
            baseLock = WRITER_LOCK_PREFIX + pid + LOCK_EXTENSION;
        }

        int i = 0;
        while (locks.containsKey(baseLock + (++i))) {
            // no logic needed
        }

        final String lockName = baseLock + i;
        final Lock lock = new Lock(lockName);

        final File lockFile = new File(lockPath, lockName);
        try {
            if (!lockFile.createNewFile()) {
                throw new IllegalStateException("could not create lock");
            }
        } catch (IOException e) {
            throw new IllegalStateException("could not create lock", e);
        }

        locks.put(lockName, lock);
        return lock;
    }

    public static class Lock {
        private final String name;
        private final int hashcode;

        private Lock(String name) {
            if (name == null || name.isEmpty()) {
                throw new InvalidArgument("name is invalid");
            }

            this.name = name;
            this.hashcode = generateHash();
        }

        private int generateHash() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return hashcode;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null) {
                return false;
            }

            if (getClass() != o.getClass()) {
                return false;
            }

            Lock other = (Lock) o;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }

            return true;
        }
    }

    private static final class LockFileFilter implements FileFilter {
        private static final Pattern LOCK_PATTERN = Pattern
                .compile("(rdr_|wtr_)\\d+\\.lck\\d+");

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean accept(final File file) {
            if (file == null) {
                return false;
            }

            return LOCK_PATTERN.matcher(file.getName()).matches();
        }

        public boolean isReaderLockFile(final File file) {
            if (file == null) {
                return false;
            }

            return file.getName().startsWith(READER_LOCK_PREFIX);
        }

        public boolean isWriterLockFile(final File file) {
            if (file == null) {
                return false;
            }

            return file.getName().startsWith(WRITER_LOCK_PREFIX);
        }
    }

    private static final class LockCleanup implements Runnable {

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            Collection<LockAdviser> advisercol = instances.values();
            if (advisercol.isEmpty()) {
                return;
            }

            final LockAdviser[] advisers = advisercol
                    .toArray(new LockAdviser[advisercol.size()]);

            for (final LockAdviser adviser : advisers) {
                final Collection<Lock> lockcol = adviser.locks.values();
                final Lock[] locks = lockcol.toArray(new Lock[lockcol.size()]);
                for (final Lock lock : locks) {
                    if (lock.name.startsWith(READER_LOCK_PREFIX)) {
                        adviser.releaseReadLock(lock);
                    } else {
                        adviser.releaseWriteLock(lock);
                    }
                }
            }
        }
    }
}
