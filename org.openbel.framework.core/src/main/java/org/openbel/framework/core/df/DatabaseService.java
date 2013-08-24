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
