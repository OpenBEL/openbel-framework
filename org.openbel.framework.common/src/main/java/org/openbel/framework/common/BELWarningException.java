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
 * A class of BEL user-facing exceptions considered warnings. In general,
 * warnings do not interrupt a workflow beyond being reported.
 */
public abstract class BELWarningException extends UserFacingException {
    private static final long serialVersionUID = 3137418444859673684L;

    /**
     * Creates a user-facing BEL warning exception, for a resource name and
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable)}.
     *
     * @param name Resource name
     * @param msg The detailed warning message
     * @see #initCause(Throwable)
     */
    public BELWarningException(String name, String msg) {
        super(name, msg);
    }

    /**
     * Creates a user-facing BEL warning exception, for a resource name,
     * message, and cause.
     *
     * @param name Resource name
     * @param msg The detailed warning message
     * @param cause The cause of the exception
     * @see Throwable#getMessage() getMessage
     * @see Throwable#getCause() getCause
     */
    public BELWarningException(String name, String msg, Throwable cause) {
        super(name, msg, cause);
    }

}
