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
