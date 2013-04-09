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

import static java.lang.System.currentTimeMillis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.openbel.framework.common.DBConnectionFailure;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.DatabaseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DBConnection encapsulates the database connection and which
 * {@link DatabaseType} it originated from.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @author Steve Ungerer {@code sungerer@selventa.com}
 */
public class DBConnection {
    private static final Logger logger =
            LoggerFactory.getLogger(DBConnection.class);

    /**
     * Time (in seconds) to wait for Connections.isValid
     */
    private static final int VALIDITY_TIMEOUT = 10;

    /**
     * Number of times to attempt to re-establish a connection
     * if the current connection is no longer valid.
     */
    private static final int REATTEMPTS = 5;

    /**
     * Time (in milliseconds) between validity checks.
     * Defaults to 600000: 10 minutes
     */
    private static final long VALIDITY_CHECK_INTERVAL = 600000;

    private ThreadLocal<Map<String, PreparedStatement>> threadedPsMap =
            new ThreadLocal<Map<String, PreparedStatement>>() {
                @Override
                protected Map<String, PreparedStatement> initialValue() {
                    return new HashMap<String, PreparedStatement>();
                }
            };

    private ThreadLocal<Map<String, Long>> threadedPsTimeMap =
            new ThreadLocal<Map<String, Long>>() {
                @Override
                protected Map<String, Long> initialValue() {
                    return new HashMap<String, Long>();
                }
            };

    private final String url;
    private final DatabaseType type;
    private final String user;
    private final String pass;
    private Connection connection;
    private long lastChecked;

    /**
     * Creates a DB connection.
     *
     * @param url Database JDBC URL
     * @param c Backing {@link Connection connection}
     * @param t {@link DatabaseType database type}
     * @throws InvalidArgument Thrown if any argument is null
     */
    public DBConnection(final Connection c, final DatabaseType t,
            final String url, final String user, final String pass) {
        if (url == null) throw new InvalidArgument("url", url);
        if (c == null) throw new InvalidArgument("c", c);
        if (t == null) throw new InvalidArgument("t", t);
        this.url = url;
        this.connection = c;
        this.type = t;
        this.user = user;
        this.pass = pass;
        this.lastChecked = currentTimeMillis();
    }

    /**
     * Returns the JDBC URL.
     *
     * @return String
     */
    public String getURL() {
        return url;
    }

    /**
     * Returns the {@link Connection connection}.
     *
     * @return The database connection
     * @throws SQLException Thrown if the connection is no longer valid and
     * cannot be re-established
     */
    public synchronized Connection getConnection() throws SQLException {
        if ((currentTimeMillis() - lastChecked)
        > VALIDITY_CHECK_INTERVAL) {
            logger.trace("Checking connection validity");
            // check the validity of the connection
            boolean valid = false;
            int t = 0;

            while (!valid && t < REATTEMPTS) {
                try {
                    if (connection == null || connection.isClosed()
                            || !connection.isValid(VALIDITY_TIMEOUT)) {
                        logger.info(
                                "Connection to {} is closed or invalid; attempting to re-establish",
                                url);
                        // remove any cached prepared statements for this connection
                        threadedPsMap.remove();

                        // attempt re-establishment
                        this.connection = DriverManager
                                .getConnection(url, user, pass);
                        logger.info("Re-established connection to {}", url);
                        // do not set valid to true - check this new connection
                    } else {
                        valid = true;
                    }
                } catch (SQLException e) {
                    logger.info("Failed to re-establish connection to " +
                            "{}, try {}/{}; Reason: {}",
                            new Object[] { url, t, REATTEMPTS, e.getMessage() });
                    // set connection to null to prevent the same error from continuously
                    // occurring when checking isClosed() or isValid()
                    this.connection = null;
                }
                t++;
            }
            if (!valid) {
                logger.error(
                        "Failed to re-establish a connection to {} after {} attempts",
                        url, REATTEMPTS);
                throw new SQLException(
                        "Unable to re-establish a valid connection",
                        new DBConnectionFailure(url,
                                "Unable to re-establish a valid connection"));
            }
            this.lastChecked = currentTimeMillis();
        }
        return connection;
    }

    /**
     * Retrieve a {@link PreparedStatement} by a reference key.
     *
     * @param sql
     * @param key
     * @param autoGeneratedKeys
     * @return
     * @throws SQLException
     */
    PreparedStatement getPreparedStatement(String sql, String key,
            int autoGeneratedKeys)
            throws SQLException {
        PreparedStatement ps = null;

        if (threadedPsMap.get().containsKey(key)) {
            ps = threadedPsMap.get().get(key);
            ps = checkValidity(ps, key);
        }

        if (ps == null) { // not in map or ps is null in map
            ps = getConnection().prepareStatement(sql, autoGeneratedKeys);
            threadedPsMap.get().put(key, ps);
            threadedPsTimeMap.get().put(key, currentTimeMillis());
        }

        return ps;
    }

    /**
     * Retrieve a {@link PreparedStatement} by a reference key.
     *
     * @param sql
     * @param key
     * @param columnNames
     * @return
     * @throws SQLException
     */
    PreparedStatement getPreparedStatement(String sql, String key,
            String[] columnNames) throws SQLException {
        PreparedStatement ps = null;

        if (threadedPsMap.get().containsKey(key)) {
            ps = threadedPsMap.get().get(key);
            ps = checkValidity(ps, key);
        }

        if (ps == null) { // not in map or ps is null in map
            ps = getConnection().prepareStatement(sql, columnNames);
            threadedPsMap.get().put(key, ps);
            threadedPsTimeMap.get().put(key, currentTimeMillis());
        }

        return ps;
    }

    /**
     * Retrieve a {@link PreparedStatement} by a reference key.
     *
     * @param sql {@link String} SQL statement
     * @param key {@link String} key as a means of identifying the {@code sql}
     * parameter
     * @param rsType Result set type (i.e.,
     * {@link java.sql.ResultSet#TYPE_SCROLL_INSENSITIVE},
     * {@link java.sql.ResultSet#TYPE_SCROLL_SENSITIVE}, or
     * {@link java.sql.ResultSet#TYPE_FORWARD_ONLY})
     * @param rsConcurrency Result set constants (i.e.,
     * {@link java.sql.ResultSet#CONCUR_READ_ONLY},
     * {@link java.sql.ResultSet#CONCUR_UPDATABLE})
     * @return {@link PreparedStatement}
     * @throws SQLException Thrown if a database access error occurs, the
     * connection is closed, or the given parameters are not {@code ResultSet}
     * constants
     */
    PreparedStatement getPreparedStatement(String sql, String key,
            int rsType, int rsConcurrency) throws SQLException {
        PreparedStatement ps = null;

        if (threadedPsMap.get().containsKey(key)) {
            ps = threadedPsMap.get().get(key);
            ps = checkValidity(ps, key);
        }

        if (ps == null) { // not in map or ps is null in map
            ps = getConnection().prepareStatement(sql, rsType, rsConcurrency);
            threadedPsMap.get().put(key, ps);
            threadedPsTimeMap.get().put(key, currentTimeMillis());
        }

        return ps;
    }

    /**
     * Validate the {@link Connection} backing a {@link PreparedStatement}.
     *
     * @param ps
     * @param key
     * @return the provided {@link PreparedStatement} if valid,
     *         <code>null</code> if the statement's {@link Connection} was
     *         invalid.
     */
    private PreparedStatement checkValidity(PreparedStatement ps, String key) {
        long currentTime = currentTimeMillis();
        Long lastChecked = threadedPsTimeMap.get().get(key);
        if (lastChecked == null) {
            // if never checked, setting to 0 ensures check will occur
            // (as long as we're not less than 10min past 1 Jan 1970)
            lastChecked = 0L;
        }
        if (currentTime - lastChecked.longValue() > VALIDITY_CHECK_INTERVAL) {
            try {
                Connection psConn = ps.getConnection();
                if (psConn == null || psConn.isClosed()
                        || !psConn.isValid(VALIDITY_TIMEOUT)) {
                    logger.info("Connection for PreparedStatement is closed or "
                            + "invalid; removing from cache");
                    // remove the prepared statement w/ invalid connection
                    threadedPsMap.get().remove(key);
                    ps = null;
                }
            } catch (SQLException e) {
                logger.info("Failed to check Prepared Statement validity.", e);
                ps = null;
            }
        }
        return ps;
    }

    /**
     * Closes all cached {@link PreparedStatement} objects and then
     * closes the database {@link Connection}.
     *
     * <p>
     * This must be called before the instance goes out of scope.
     * </p>
     */
    public synchronized void close() {
        for (PreparedStatement ps : threadedPsMap.get().values()) {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    // swallowed
                }
            }
        }
        try {
            if (null != connection) {
                this.connection.close();
            }
        } catch (SQLException e) {
            // swallowed
        }
    }

    /**
     * Returns the {@link DatabaseType type}.
     *
     * @return The database type
     */
    public DatabaseType getType() {
        return type;
    }

    /**
     * Returns {@code true} if the database type is {@link DatabaseType#DERBY},
     * {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isDerby() {
        return type == DatabaseType.DERBY;
    }

    /**
     * Returns {@code true} if the database type is {@link DatabaseType#MYSQL},
     * {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isMysql() {
        return type == DatabaseType.MYSQL;
    }

    /**
     * Returns {@code true} if the database type is {@link DatabaseType#ORACLE},
     * {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isOracle() {
        return type == DatabaseType.ORACLE;
    }

    /**
     * Returns {@code true} if the database type is {@link DatabaseType#POSTGRESQL},
     * {@code false} otherwise.
     *
     * @return boolean
     */
    public boolean isPostgresql() {
        return type == DatabaseType.POSTGRESQL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DBConnection [\n");

        builder.append("\turl=");
        builder.append(url);
        builder.append(",\n");

        builder.append("\ttype=");
        builder.append(type);
        builder.append(",\n");

        builder.append("\tconnection=");
        builder.append(connection);
        builder.append("\n");

        builder.append("]");
        return builder.toString();
    }
}
