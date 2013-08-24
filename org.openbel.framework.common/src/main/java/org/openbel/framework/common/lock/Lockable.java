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

import java.io.File;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.lock.LockAdviser.Lock;

/**
 * {@link Lockable} defines an object which can be locked for both reading and
 * writing.  The user of this {@link Lockable lockable} must obtain the
 * {@link Lock} before processing and release the {@link Lock} after
 * processing.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface Lockable {

    /**
     * Attempts to obtain a {@link Lock read lock} from the target
     * {@link Lockable lockable}.
     *
     * @return the {@link Lock read lock}, or <tt>null</tt> if it could not be
     * obtained
     */
    public Lock obtainReadLock();

    /**
     * Attempts to obtain a {@link Lock write lock} from the target
     * {@link Lockable lockable}.
     *
     * @return the {@link Lock write lock}, or <tt>null</tt> if it could not be
     * obtained
     */
    public Lock obtainWriteLock();

    /**
     * Attempts to release a {@link Lock read lock} from the target
     * {@link Lockable lockable}.
     *
     * @param lock the {@link Lock read lock} to release from the target
     * {@link Lockable lockable}, which cannot be null or have an invalid lock
     * name
     * @throws InvalidArgument Thrown if <tt>lock</tt> is <tt>null</tt>, or has
     * an invalid lock name
     * @throws IllegalStateException Thrown if the {@link File read lock file}
     * could not be deleted immediately
     */
    public void releaseReadLock(final Lock lock);

    /**
     * Attempts to release a {@link Lock write lock} from the target
     * {@link Lockable lockable}
     *
     * @param lock the {@link Lock write lock} to release from the target
     * {@link Lockable lockable}, which cannot be null or have an invalid lock
     * name
     * @throws InvalidArgument Thrown if <tt>lock</tt> is <tt>null</tt>, or has
     * an invalid lock name
     * @throws IllegalStateException Thrown if the {@link File write lock file}
     * could not be deleted immediately
     */
    public void releaseWriteLock(final Lock lock);
}
