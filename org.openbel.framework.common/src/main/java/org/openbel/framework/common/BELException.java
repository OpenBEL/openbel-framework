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
package org.openbel.framework.common;

/**
 * BEL base class for checked exceptions.
 */
public abstract class BELException extends Exception {
    private static final long serialVersionUID = -155295517620826771L;

    /**
     * Creates a BEL exception with {@code null} as its detail message. The
     * cause is not initialized, and may subsequently be initialized by a call
     * to {@link #initCause(Throwable)}.
     *
     * @see Throwable#initCause(Throwable) initCause
     */
    public BELException() {
    }

    /**
     * Creates a BEL exception with the specified detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to
     * {@link #initCause(Throwable)}.
     *
     * @param message The detailed exception message
     * @see Throwable#getMessage() getMessage
     * @see Throwable#initCause(Throwable) initCause
     */
    public BELException(String message) {
        super(message);
    }

    /**
     * Creates a BEL exception with the specified cause and {@code null} as its
     * detail message.
     *
     * @param cause The cause of the exception
     * @see Throwable#getCause() getCause
     */
    public BELException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a BEL exception with the specified cause and detail message.
     *
     * @param message The detailed exception message
     * @param cause The cause of the exception
     * @see Throwable#getMessage() getMessage
     * @see Throwable#getCause() getCause
     */
    public BELException(String message, Throwable cause) {
        super(message, cause);
    }

}
