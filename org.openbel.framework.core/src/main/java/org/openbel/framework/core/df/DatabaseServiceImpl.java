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
