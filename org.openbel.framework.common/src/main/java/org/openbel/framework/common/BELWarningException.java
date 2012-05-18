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
