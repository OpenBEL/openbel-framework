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
package org.openbel.framework.tools.pkam;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.core.df.DBConnection;

class KAMExportDAO extends AbstractJdbcDAO {

    KAMExportDAO(final DBConnection dbConnection, final String schemaName)
            throws SQLException {
        super(dbConnection, schemaName);
    }

    public List<String[]> getAllRowsForTable(final KAMStoreTables1_0 table)
            throws SQLException {
        List<String[]> tableRows = new ArrayList<String[]>();
        PreparedStatement allRowsPs = getPreparedStatement(table
                .getSQLSelect(schemaName));

        ResultSet allRowsRs = null;
        try {
            allRowsRs = allRowsPs.executeQuery();

            while (allRowsRs.next()) {
                tableRows.add(extractData(allRowsRs, table));
            }
        } finally {
            close(allRowsRs);
        }

        return tableRows;
    }

    private String[] extractData(final ResultSet allRowsRs,
            final KAMStoreTables1_0 table) throws SQLException {
        final String[] cols = table.getColumnNames();
        final Integer[] types = table.getColumnTypes();
        final String[] data = new String[cols.length];

        for (int i = 0; i < cols.length; i++) {
            final String colName = cols[i];

            // store sql timestamps as epoch time to avoid date formats
            if (types[i] == Types.TIMESTAMP) {
                Timestamp timeVal = allRowsRs.getTimestamp(colName);

                if (allRowsRs.wasNull()) {
                    data[i] = null;
                } else {
                    data[i] = String.valueOf(timeVal.getTime());
                }
            } else if (types[i] == Types.BIGINT) {
                long l = allRowsRs.getLong(colName);
                if (allRowsRs.wasNull()) {
                    data[i] = null;
                } else {
                    data[i] = String.valueOf(l);
                }
            } else {
                final String colValue = allRowsRs.getString(colName);
                if (allRowsRs.wasNull()) {
                    data[i] = "NULL";
                } else {
                    data[i] = colValue;
                }
            }
        }

        return data;
    }
}
