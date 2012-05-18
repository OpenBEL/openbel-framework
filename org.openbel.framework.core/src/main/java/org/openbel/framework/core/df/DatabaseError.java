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
package org.openbel.framework.core.df;

import java.sql.SQLException;

import org.openbel.framework.common.BELErrorException;
import org.openbel.framework.common.InvalidArgument;

/**
 * A {@link BELErrorException BEL error} resulting from {@link SQLException SQL
 * exceptions}.
 */
public class DatabaseError extends BELErrorException {
    private static final long serialVersionUID = -3212744210490285691L;

    /**
     * Creates a database error for the provided database resource {@code name},
     * with the supplied {@code message}, and underlying {@link SQLException
     * cause}.
     * 
     * @param name Database resource name
     * @param msg Message indicative of error
     * @param cause {@link SQLException SQL exception} causing error
     * @throws InvalidArgument Thrown if {@code name}, {@code msg}, or
     * {@code cause} is null or empty
     */
    public DatabaseError(String name, String msg, SQLException cause) {
        super(name, msg, cause);
        if (name == null || name.isEmpty()) {
            throw new InvalidArgument("msg", msg);
        }
        if (msg == null || msg.isEmpty()) {
            throw new InvalidArgument("msg", msg);
        }
        if (cause == null) {
            throw new InvalidArgument("cause", cause);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append(getMessage());
        bldr.append(" for ");
        bldr.append(getName());

        bldr.append("\n\treason: ");
        final SQLException cause = (SQLException) getCause();
        final int code = cause.getErrorCode();
        final String msg = cause.getMessage();
        bldr.append("(");
        bldr.append(code);
        bldr.append(") ");
        bldr.append(msg);

        return bldr.toString();
    }
}
