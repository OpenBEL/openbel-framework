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
 * A user-facing unchecked BEL exception, requiring
 * {@link #getUserFacingMessage() user-facing messages}. BEL base class for
 *
 * @see BELUncheckedException
 */
public abstract class UserFacingUncheckedException extends
        BELUncheckedException {
    private static final long serialVersionUID = 5679954339908254380L;

    /**
     * Creates a user-facing unchecked exception with the provided detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable)}.
     *
     * @param msg The detailed exception message
     * @see #initCause(Throwable)
     */
    public UserFacingUncheckedException(String msg) {
        super(msg);
    }

    /**
     * Creates a user-facing unchecked exception with the provided detail
     * message and cause.
     *
     * @param msg The detailed exception message
     * @param cause The cause of the exception
     * @see Throwable#getMessage() getMessage
     * @see Throwable#getCause() getCause
     */
    public UserFacingUncheckedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Returns the user-facing message.
     *
     * @return User-facing message
     */
    public abstract String getUserFacingMessage();

}
