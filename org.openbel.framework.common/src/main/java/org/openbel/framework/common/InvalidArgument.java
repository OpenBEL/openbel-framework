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
