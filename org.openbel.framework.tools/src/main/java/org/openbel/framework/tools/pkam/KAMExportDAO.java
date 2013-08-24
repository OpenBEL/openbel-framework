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
package org.openbel.framework.tools.pkam;

import static org.openbel.framework.tools.pkam.KAMStoreTables1_0.KAM_OBJECTS;
import static org.openbel.framework.tools.pkam.KAMStoreTables1_0.KAM_OBJECTS_TEXT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.encryption.EncryptionService;
import org.openbel.framework.core.df.encryption.EncryptionServiceException;

class KAMExportDAO extends AbstractJdbcDAO {
    private final EncryptionService encryptionService;

    KAMExportDAO(final DBConnection dbConnection,
            final String schemaName, final EncryptionService encryptionService)
            throws SQLException {
        super(dbConnection, schemaName);
        this.encryptionService = encryptionService;
    }

    public List<String[]> getAllRowsForTable(final KAMStoreTables1_0 table)
            throws SQLException, EncryptionServiceException {
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
            final KAMStoreTables1_0 table) throws SQLException,
            EncryptionServiceException {
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

                // can only decrypt a non-null value
                if (data[i] != null) {
                    // decrypt column values if text value of objects tables
                    if ((table == KAM_OBJECTS && "varchar_value"
                            .equals(colName))
                            || (table == KAM_OBJECTS_TEXT && "text_value"
                                    .equals(colName))) {
                        data[i] = encryptionService.decrypt(data[i]);
                    }
                }
            }
        }

        return data;
    }
}
