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
