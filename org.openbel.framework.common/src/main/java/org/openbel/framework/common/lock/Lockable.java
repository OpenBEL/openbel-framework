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
