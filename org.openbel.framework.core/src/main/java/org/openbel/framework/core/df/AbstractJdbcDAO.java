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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.openbel.framework.common.InvalidArgument;

/**
 * AbstractJdbcDataAccessor provides low-level JDBC operations for working
 * with a database.
 * <p>
 * The {@link PreparedStatement} objects used are cached to avoid repeated
 * statement compilation and increase application performance.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public abstract class AbstractJdbcDAO {
    public static final String SCHEMA_NAME_PLACEHOLDER = "@";
    protected static final int DEFAULT_FETCH_SIZE = 100000;

    protected DBConnection dbConnection = null;

    protected final String schemaName;

    /**
     * Protected constructor created with a <tt>dbConnection</tt> and the
     * <tt>schemaName</tt>.
     *
     * @param dbConnection {@link DBConnection}, the db connection, which
     * cannot be null
     * @param schemaName {@link String}, the schema name to use in
     * SQL statements
     * @throws SQLException Thrown if a database exception occurred
     * while checking if the <tt>dbConnection</tt> is closed or if the
     * <tt>dbConnection</tt> is no longer valid and cannot be re-established
     * @throws InvalidArgument Thrown if the <tt>dbConnection</tt> argument
     * is null or the <tt>schemaName</tt> is null
     */
    protected AbstractJdbcDAO(final DBConnection dbConnection,
            String schemaName) throws SQLException {
        if (dbConnection == null) {
            throw new InvalidArgument("dbConnection", dbConnection);
        }

        if (dbConnection.getConnection().isClosed()) {
            throw new InvalidArgument("dbc is closed and cannot be used");
        }

        if (schemaName == null) {
            throw new InvalidArgument("schemaName", dbConnection);
        }

        this.dbConnection = dbConnection;
        this.schemaName = schemaName;
    }

    /**
     * Closes the <tt>dbConnection</tt> associated with this instance.
     *
     * <p>
     * This must be called before the instance goes out of scope.
     * </p>
     */
    public void terminate() {
        dbConnection.close();
    }

    /**
     * Returns the schema name associated with this JDBC dao.
     *
     * @return {@link String} schema name
     */
    public final String getSchemaName() {
        return schemaName;
    }

    /**
     * Returns a {@link PreparedStatement} for the SQL statement in
     * <tt>sql</tt>.
     *
     * @param sql {@link String}, the sql to prepare which can take certain
     * placeholders:<ul>
     * <il>? - paremeter substitution marker for the {@link PreparedStatement}</li>
     * <il>@ - schema name substitution marker defined</li></ul>
     * @return {@link PreparedStatement}
     * @throws SQLException Thrown if a database exception occurred or if the
     * <tt>dbConnection</tt> is no longer valid and cannot be re-established
     */
    protected PreparedStatement getPreparedStatement(String sql)
            throws SQLException {
        // Inject the schema name into the SQL
        String replacedSQL = injectSchema(sql);
        return getPreparedStatement(replacedSQL, replacedSQL);
    }

    /**
     * Returns a {@link PreparedStatement} for the SQL statement in <tt>sql</tt>
     * providing a {@link ResultSet result set} of a given type and concurrency.
     *
     * @param sql {@link String}, the sql to prepare which can take certain
     * placeholders:
     * <ul>
     * <il>? - paremeter substitution marker for the {@link PreparedStatement}
     * </li> <il>@ - schema name substitution marker defined</li>
     * </ul>
     * @param type Result set type
     * @param concurrency  Result set concurrency
     * @return {@link PreparedStatement}
     * @throws SQLException Thrown if a database exception occurred or if the
     * <tt>dbConnection</tt> is no longer valid and cannot be re-established
     * @see ResultSet#TYPE_SCROLL_INSENSITIVE
     * @see ResultSet#CONCUR_READ_ONLY
     */
    protected PreparedStatement getPreparedStatement(String sql, int type,
            int concurrency) throws SQLException {
        // Inject the schema name into the SQL
        String isql = injectSchema(sql);
        return dbConnection.getPreparedStatement(isql, isql, type, concurrency);
    }

    /**
     * Returns the {@link PreparedStatement} wrapping a sql statement in
     * <tt>sql</tt>.
     *
     * <p>
     * The prepared statement's <tt>autoGeneratedKeys</tt> feature is set via
     * the additional parameter.
     * </p>
     *
     * @param sql {@link String} the sql statement to use in the
     * {@link PreparedStatement}, which can take parameter substitutions - '?'.
     * @param autoGeneratedKeys <tt>int</tt> the auto generated keys feature
     * in the {@link PreparedStatement}
     * @return {@link PreparedStatement} the prepared statement, which could
     * be either new or cached
     * @throws SQLException Thrown if a database exception occurred or if the
     * <tt>dbConnection</tt> is no longer valid and cannot be re-established
     */
    protected PreparedStatement getPreparedStatement(String sql,
            int autoGeneratedKeys) throws SQLException {
        String replacedSQL = injectSchema(sql);

        // use the auto generated key parameter as part of the key
        return dbConnection.getPreparedStatement(replacedSQL, autoGeneratedKeys
                + replacedSQL, autoGeneratedKeys);
    }

    /**
     * Returns the {@link PreparedStatement} for the sql statement defined by
     * <tt>sql</tt> and that expects the columns defined in <tt>columnNames</tt>
     * to be returned.
     *
     * @param sql {@link String}, the sql statement
     * @param columnNames <tt>String[]</tt>, the columns names expected in the
     * prepared statement's result set
     * @return {@link PreparedStatement} the prepared statement for this sql
     * statement
     * @throws SQLException Thrown if a database exception occurred or if the
     * <tt>dbConnection</tt> is no longer valid and cannot be re-established
     */
    protected PreparedStatement getPreparedStatement(String sql,
            String[] columnNames) throws SQLException {
        String replacedSQL = injectSchema(sql);
        return dbConnection.getPreparedStatement(replacedSQL,
                Arrays.hashCode(columnNames)
                        + replacedSQL, columnNames);
    }

    protected String injectSchema(String sql) {
        return sql.replace(SCHEMA_NAME_PLACEHOLDER, schemaName);
    }

    private PreparedStatement getPreparedStatement(String sql, String key)
            throws SQLException {
        return dbConnection.getPreparedStatement(sql, key,
                Statement.NO_GENERATED_KEYS);
    }

    /**
     * Closes the {@link Statement}, swallowing any {@link SQLException}
     * exceptions that occur.
     *
     * @param stmt {@link Statement} the statement to close
     */
    protected void close(Statement stmt) {
        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException e) {}
        }
    }

    /**
     * Closes the {@link ResultSet}, swallowing any {@link SQLException}
     * exceptions that occur.
     *
     * @param rset {@link ResultSet} the result set to close
     */
    protected void close(ResultSet rset) {
        if (null != rset) {
            try {
                rset.close();
            } catch (SQLException e) {}
        }
    }

    /**
     * Closes the {@link PreparedStatement}, swallowing any
     * {@link SQLException} exceptions that occur.
     *
     * @param pstmt {@link PreparedStatement} the prepared statement to close
     */
    protected void close(PreparedStatement pstmt) {
        if (null != pstmt) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                // swallowed
            }
        }
    }

    /**
     * Closes the {@link Connection}, swallowing any {@link SQLException}
     * exceptions that occur.
     *
     * @param c {@link Connection} the connection to close
     */
    protected void close(Connection c) {
        if (null != c) {
            try {
                c.close();
            } catch (SQLException e) {
                // swallowed
            }
        }
    }
}
