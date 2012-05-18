/**
 * Copyright (C) 2012 Selventa, Inc.
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
 * BEL runtime exception indicating an invalid argument was provided.
 * <p>
 * This class should be used in favor of {@link IllegalArgumentException} as it
 * forces a standard for runtime exception reporting.
 * </p>
 */
public class InvalidArgument extends BELUncheckedException {
    private static final long serialVersionUID = 3550275196600044808L;
    public static final String DEFAULT_MSG = "Invalid argument";

    /**
     * Creates an invalid argument exception with {@value #DEFAULT_MSG} as its
     * detail message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable)}.
     * <p>
     * The constructors {@link #InvalidArgument(String)} and
     * {@link #InvalidArgument(String, Throwable)} are favored in preference to
     * using the default message.
     * </p>
     * 
     * @deprecated You should be explaining {@link #InvalidArgument(String) why}
     * the argument is invalid.
     */
    @Deprecated
    public InvalidArgument() {
        super(DEFAULT_MSG);
    }

    /**
     * Creates an invalid argument exception with the specified cause and
     * {@value #DEFAULT_MSG} as its detail message.
     * 
     * @param cause The cause of the exception
     */
    public InvalidArgument(Throwable cause) {
        super(DEFAULT_MSG, cause);
    }

    /**
     * Creates an invalid argument exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause(Throwable)}.
     * 
     * @param message The detailed exception message
     */
    public InvalidArgument(String message) {
        super(message);
    }

    /**
     * Creates an invalid argument exception with the following detail message:
     * <dl>
     * <dt>If {@code argument} is null:</dt>
     * <dd>{@code <argumentName> is null}</dd>
     * <dt>If {@code argument} is non-null:</dt>
     * <dd>{@code invalid <argumentName>}</dd>
     * </dl>
     * 
     * @param argumentName The invalid argument's name
     * @param argument The invalid argument (may be null)
     */
    public InvalidArgument(String argumentName, Object argument) {
        super(makeDetail(argumentName, argument));
    }

    /**
     * Creates an invalid argument exception with the specified cause and detail
     * message.
     * 
     * @param message The detailed exception message
     * @param cause The cause of the exception
     */
    public InvalidArgument(String message, Throwable cause) {
        super(message, cause);
    }

    private static String makeDetail(final String name, final Object o) {
        if (o == null)
            return name.concat(" is null");
        return "invalid ".concat(name);
    }
}
