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
 * A user-facing BEL exception, requiring {@link #getUserFacingMessage()
 * user-facing messages}.
 */
abstract class UserFacingException extends BELException {
    private static final long serialVersionUID = 91157638662677162L;
    private String name;

    /**
     * Creates a user-facing exception for the resource name with the provided
     * detail message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable)}.
     *
     * @param name Resource name on which the exception was caused
     * @param msg The detailed exception message
     * @see #initCause(Throwable)
     */
    public UserFacingException(String name, String msg) {
        super(msg);
        this.name = name;
    }

    /**
     * Creates a user-facing exception for the resource name with the provided
     * detail message and cause.
     *
     * @param name Resource name on which the exception was caused
     * @param msg The detailed exception message
     * @param cause The cause of the exception
     * @see Throwable#getMessage() getMessage
     * @see Throwable#getCause() getCause
     */
    public UserFacingException(String name, String msg, Throwable cause) {
        super(msg, cause);
        this.name = name;
    }

    /**
     * Returns the resource name failing validation.
     *
     * @return String, may be null
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the resource name failing validation.
     *
     * @param name String, may be null
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the user-facing message.
     *
     * @return User-facing message
     */
    public abstract String getUserFacingMessage();
}
