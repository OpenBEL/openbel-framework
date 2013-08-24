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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.core.df.DBConnection;

class KAMCatalogImportDAO extends AbstractJdbcDAO {

    KAMCatalogImportDAO(DBConnection dbConnection, String schemaName)
            throws SQLException {
        super(dbConnection, schemaName);
    }

    public void importKamCatalogEntry(final KAMCatalogEntry kamCatalogEntry,
            final KAMStoreTables1_0 kcTable)
            throws SQLException {
        final PreparedStatement kcPs = getPreparedStatement(kcTable
                .getSQLInsert(kamCatalogEntry.getSchemaName()));
        kcPs.setString(1, kamCatalogEntry.getName());
        kcPs.setString(2, kamCatalogEntry.getDescription());
        kcPs.setTimestamp(3, new Timestamp(kamCatalogEntry.getLastCompiled()));
        kcPs.setString(4, kamCatalogEntry.getSchemaName());
        kcPs.execute();
    }
}
