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
