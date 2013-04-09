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
package org.openbel.framework.core.df;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openbel.framework.common.DBConnectionFailure;

/**
 * Database service interface.
 */
public interface DatabaseService {

    /**
     * Gets a {@link DBConnection} for the provided URL, username, and password.
     *
     * @param url JDBC URL, in
     * {@code protocol:driver/connectivity mechanism:database identification}
     * format
     * @param user Username
     * @param pass Password
     * @return DBConnection
     * @throws SQLException Thrown if a database access error occurs
     * @throws UnsupportedOperationException Thrown if the database type could
     * not be determined
     */
    DBConnection dbConnection(String url, String user, String pass)
            throws SQLException;

    /**
     * Creates a statement and executes the SQL.
     *
     * @param dbc {@link DBConnection}
     * @param sql SQL string
     * @throws SQLException Thrown if a database error occurs or the provided
     * SQL produces a {@link ResultSet}
     */
    public void update(final DBConnection dbc, final String sql)
            throws SQLException;

    /**
     * Returns {@code true} if the connection to the provided URL succeeds,
     * {@code false} otherwise.
     *
     * @param url JDBC URL, in
     * {@code protocol:driver/connectivity mechanism:database identification}
     * format
     * @param user Username
     * @param pass Password
     * @return boolean {@code true} on success, {@code false} otherwise
     * @throws DBConnectionFailure Thrown if a database error occurs connecting to
     * the specified database
     */
    public boolean validConnection(String url, String user, String pass)
            throws DBConnectionFailure;
}
