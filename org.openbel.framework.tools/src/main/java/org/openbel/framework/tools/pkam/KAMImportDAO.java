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
package org.openbel.framework.tools.pkam;

import static org.openbel.framework.tools.pkam.KAMStoreTables1_0.KAM_OBJECTS;
import static org.openbel.framework.tools.pkam.KAMStoreTables1_0.KAM_OBJECTS_TEXT;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.encryption.EncryptionService;
import org.openbel.framework.core.df.encryption.EncryptionServiceException;

class KAMImportDAO extends AbstractJdbcDAO {
    private static final int BATCH_THRESHOLD = 1000;
    private final EncryptionService encryptionService;
    private KAMStoreTables1_0 table;
    private PreparedStatement insertPs;
    private int batchCounter = 0;

    KAMImportDAO(final DBConnection dbConnection, final String schemaName,
            final EncryptionService encryptionService)
            throws SQLException {
        super(dbConnection, schemaName);
        this.encryptionService = encryptionService;
    }

    public void startTableBatch(final KAMStoreTables1_0 table)
            throws SQLException {
        if (table == null) {
            throw new InvalidArgument("table", table);
        }

        if (batchCounter > 0) {
            throw new IllegalStateException("Batch not committed for table '"
                    + this.table.getTableName() + "'");
        }

        this.table = table;
        this.insertPs = getPreparedStatement(table.getSQLInsert(schemaName));
    }

    public void importDataRow(final String[] data) throws SQLException,
            EncryptionServiceException {
        if (data == null) {
            throw new InvalidArgument("data", data);
        }

        if (table.getInsertPrimaryKey()) {
            final String key = data[0];
            if (!StringUtils.isNumeric(key)) {
                throw new IllegalStateException("Primary key column '"
                        + table.getPrimaryKeyColumn()
                        + "' does not have a numeric value");
            }

            // set SQL parameter for primary key
            try {
                insertPs.setInt(1, Integer.parseInt(key));
            } catch (NumberFormatException e) {
                // swallowed since we check if the data is numeric
            }

            // set other columns, start after primary key at 2
            for (int i = 1; i < data.length; i++) {
                String value = data[i];
                int sqlType = table.getColumnTypes()[i];
                setParameterValue(i + 1, table.getColumnNames()[i], sqlType,
                        value);
            }
        } else {
            // only set other columns, start from 1
            for (int i = 1; i < data.length; i++) {
                String value = data[i];
                int sqlType = table.getColumnTypes()[i];
                setParameterValue(i, table.getColumnNames()[i], sqlType,
                        value);
            }
        }

        // add batch after setting SQL parameters
        insertPs.addBatch();
        batchCounter++;

        // execute batch if we reached the threshold, and reset counter
        if (batchCounter == BATCH_THRESHOLD) {
            commitTableBatch();
        }
    }

    public void commitTableBatch() throws SQLException {
        if (batchCounter > 0) {
            insertPs.executeBatch();
            batchCounter = 0;
        }
    }

    private void setParameterValue(final int parameterIndex,
            final String columnName, final int sqlType,
            String value) throws SQLException, EncryptionServiceException {
        if (value == null || value.equals("NULL")) {
            insertPs.setNull(parameterIndex, sqlType);
        } else if (sqlType == Types.INTEGER) {
            if (!StringUtils.isNumeric(value)) {
                throw new IllegalStateException("Column '"
                        + columnName
                        + "' does not have a numeric value");
            }

            try {
                insertPs.setInt(parameterIndex, Integer.parseInt(value));
            } catch (NumberFormatException e) {
                // swallowed since we check if the data is numeric
            }
        } else if (sqlType == Types.BIGINT) {
            insertPs.setLong(parameterIndex, Long.parseLong(value));
        } else if (sqlType == Types.VARCHAR) {
            // decrypt column values if text value of objects tables
            if ((table == KAM_OBJECTS && "varchar_value".equals(columnName))
                    || (table == KAM_OBJECTS_TEXT && "text_value"
                            .equals(columnName))) {
                value = encryptionService.encrypt(value);
            }

            insertPs.setString(parameterIndex, value);
        } else if (sqlType == Types.TIMESTAMP) {
            if (!StringUtils.isNumeric(value)) {
                throw new IllegalStateException("Column '"
                        + columnName
                        + "' does not have a numeric value for timestamp");
            }

            try {
                insertPs.setTimestamp(parameterIndex,
                        new Timestamp(Long.parseLong(value)));
            } catch (NumberFormatException e) {
                // swallowed since we check if the data is numeric
            }
        } else if (sqlType == Types.CLOB) {
            // decrypt column values if text value of objects tables
            if ((table == KAM_OBJECTS && "varchar_value".equals(columnName))
                    || (table == KAM_OBJECTS_TEXT && "text_value"
                            .equals(columnName))) {
                value = encryptionService.encrypt(value);
            }

            final StringReader sr = new StringReader(value);
            insertPs.setClob(parameterIndex, sr);
        } else {
            throw new UnsupportedOperationException(
                    "Cannot convert String to SQL Type - " + sqlType);
        }
    }
}
