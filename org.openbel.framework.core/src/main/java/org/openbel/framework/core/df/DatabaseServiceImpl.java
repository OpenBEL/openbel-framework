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

import static java.lang.String.format;
import static java.sql.DriverManager.getConnection;
import static org.openbel.framework.common.BELUtilities.getFirstCause;
import static org.openbel.framework.common.enums.DatabaseType.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.openbel.framework.common.DBConnectionFailure;
import org.openbel.framework.common.enums.DatabaseType;

/**
 * Database service implementation.
 */
public final class DatabaseServiceImpl implements DatabaseService {

    /**
     * {@inheritDoc}
     */
    @Override
    public DBConnection dbConnection(final String url, final String user,
            final String pass) throws SQLException {

        final DatabaseType type = getDatabaseTypeForURL(url);
        if (type == null) {
            final String err = "failed to determine database type (URL: %s)";
            throw new UnsupportedOperationException(format(err, url));
        }
        try {
            Class.forName(type.getDriverClassName());
        } catch (ClassNotFoundException e) {
            final String err = "failed to load database driver (URL: %s)";
            final String msg = format(err, url);
            throw new UnsupportedOperationException(msg, e);
        }
        final Connection c = getConnection(url, user, pass);
        switch (type) {
        case DERBY:
            return new DBConnection(c, DERBY, url, user, pass);
        case MYSQL:
            return new DBConnection(c, MYSQL, url, user, pass);
        case ORACLE:
            return new DBConnection(c, ORACLE, url, user, pass);
        case POSTGRESQL:
            return new DBConnection(c, POSTGRESQL, url, user, pass);
        default:
            throw new UnsupportedOperationException(type + " is unknown");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(DBConnection dbc, String sql) throws SQLException {
        final Connection con = dbc.getConnection();
        // Note current autocommit status -- we're turning it off.
        final boolean ac = con.getAutoCommit();
        con.setAutoCommit(false);

        final Statement stmt = con.createStatement();
        try {
            stmt.execute(sql);
            con.commit();
            stmt.close();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            // Restore autocommit status.
            con.setAutoCommit(ac);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validConnection(final String url, final String user,
            final String pass) throws DBConnectionFailure {
        Connection c = null;
        try {
            if (getDatabaseTypeForURL(url) == null) {
                // Unknown database types are not considered valid
                return false;
            }
            c = getConnection(url, user, pass);
            return true;
        } catch (SQLException e) {
            Throwable originalSqlException = getFirstCause(e);
            throw new DBConnectionFailure(url,
                    originalSqlException.getMessage(),
                    originalSqlException);
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {}
            }
        }

    }
}
