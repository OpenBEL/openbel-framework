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
package org.openbel.framework.compiler.kam;

import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.core.df.AbstractJdbcDAO.SCHEMA_NAME_PLACEHOLDER;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.DatabaseService;
import org.openbel.framework.core.kamstore.KamDbObject;
import org.openbel.framework.core.kamstore.data.jdbc.KAMCatalogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KAMStoreSchemaServiceImpl implements a service to setup a KAM schema
 * instance to store a single KAM.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class KAMStoreSchemaServiceImpl implements KAMStoreSchemaService {
    private final String SQL_SCRIPT_DELIMITER = "##";

    /**
     * Statically-loads the slf4j application log.
     */
    private static Logger log = LoggerFactory
            .getLogger(KAMStoreSchemaServiceImpl.class);

    /**
     * The classpath location where the KAM schema .sql files are located:
     * {@value #KAM_SQL_PATH}
     */
    private static final String KAM_SQL_PATH = "/kam/";

    /**
     * The classpath location where the .sql files to delete KAM schema are located:
     * {@value #DELETE_KAM_SQL_PATH}
     */
    private static final String DELETE_KAM_SQL_PATH = "/kam_delete/";

    /**
     * The classpath location where the KAM catalog schema .sql files are
     * located: {@value #KAM_CATALOG_SQL_PATH}
     */
    private static final String KAM_CATALOG_SQL_PATH = "/kam_catalog/";

    /**
     * The database service bean dependency.
     */
    private final DatabaseService databaseService;

    /**
     * Create the KAMStoreSchemaServiceImpl with the {@link DatabaseService}
     * bean.
     *
     * @param databaseService {@link DatabaseService} the database service
     * bean
     */
    public KAMStoreSchemaServiceImpl(final DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupKAMCatalogSchema() throws SQLException, IOException {
        DBConnection kamDbc = null;
        try {
            kamDbc = createConnection();
            runScripts(kamDbc, "/" + kamDbc.getType() + KAM_CATALOG_SQL_PATH,
                    getSystemConfiguration().getKamCatalogSchema(),
                    getSchemaManagementStatus(kamDbc));
        } finally {
            closeConnection(kamDbc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setupKAMStoreSchema(DBConnection dbc, String schemaName)
            throws IOException {

        runScripts(dbc, "/" + dbc.getType() + KAM_SQL_PATH, schemaName,
                getSchemaManagementStatus(dbc));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteKAMStoreSchema(DBConnection dbc, String schemaName)
            throws IOException {
        boolean deleteSchemas = getSchemaManagementStatus(dbc);
        if (deleteSchemas) {
            runScripts(dbc, "/" + dbc.getType() + DELETE_KAM_SQL_PATH,
                    schemaName, deleteSchemas);
        } else {
            // Truncate the schema instead of deleting it.
            InputStream sqlStream = null;
            if (dbc.isMysql()) {
                sqlStream =
                        getClass().getResourceAsStream(
                                "/" + dbc.getType() + KAM_SQL_PATH + "1.sql");
            } else if (dbc.isOracle()) {
                sqlStream =
                        getClass().getResourceAsStream(
                                "/" + dbc.getType() + KAM_SQL_PATH + "0.sql");
            }
            if (sqlStream != null) {
                runScript(dbc, sqlStream, schemaName);
            }
        }

        return deleteSchemas;
    }

    /**
     * Check to see if the system is managing the KAMStore schemas. The system manages Derby schemas
     * by default. Oracle is only DBA managed and MySQL can be either although the default is to
     * enable system managed
     *
     * @param dbc
     * @return
     */
    private boolean getSchemaManagementStatus(DBConnection dbc) {

        boolean createSchemas = false;
        if (dbc.isDerby()) {
            createSchemas = true;
        } else if (dbc.isMysql() || dbc.isPostgresql()) {
            createSchemas = getSystemConfiguration().getSystemManagedSchemas();
        }
        return createSchemas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String saveToKAMCatalog(KamDbObject kamDb)
            throws SQLException {
        KAMCatalogDao catalogDAO = null;

        try {
            catalogDAO = createCatalogDao();
            catalogDAO.saveToCatalog(kamDb);
        } finally {
            if (catalogDAO != null) {
                closeCatalogDao(catalogDAO);
            }
        }

        return kamDb.getSchemaName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFromKAMCatalog(final String kamName)
            throws SQLException {
        KAMCatalogDao catalogDAO = null;

        try {
            catalogDAO = createCatalogDao();
            catalogDAO.deleteFromCatalog(kamName);
        } finally {
            if (catalogDAO != null) {
                closeCatalogDao(catalogDAO);
            }
        }
    }

    private KAMCatalogDao createCatalogDao() throws SQLException {
        String catalogSchema = getSystemConfiguration().getKamCatalogSchema();
        String kamSchemaPrefix = getSystemConfiguration().getKamSchemaPrefix();
        return new KAMCatalogDao(createConnection(), catalogSchema,
                kamSchemaPrefix);
    }

    private void closeCatalogDao(KAMCatalogDao catalogDAO) {
        if (catalogDAO != null) {
            catalogDAO.terminate();
        }
    }

    private DBConnection createConnection() throws SQLException {
        return databaseService.dbConnection(
                getSystemConfiguration().getKamURL(),
                getSystemConfiguration().getKamUser(),
                getSystemConfiguration().getKamPassword());
    }

    private void closeConnection(DBConnection dbc) {
        if (dbc != null) {
            try {
                dbc.getConnection().close();
            } catch (SQLException e) {}
        }
    }

    /**
     * Finds .sql scripts in <tt>dropRoot</tt> and executes each statement
     * againsts the {@link DBConnection} <tt>dbc</tt>.
     *
     * @param dbc {@link DBConnection}, the database connection
     * @param dropRoot {@link String}, the classpath root where .sql files
     * are found
     * @param schemaName {@link String}, the schema name, which can be null
     * @param createSchemas {@link boolean}, true if the system should create new schemas, false otherwise
     * @throws IOException Thrown if an I/O error occurred reading a .sql file
     * from the classpath
     */
    private void runScripts(DBConnection dbc, String dropRoot,
            String schemaName, boolean createSchemas) throws IOException {
        int fi = 0;
        boolean fnf = false;
        while (!fnf) {
            InputStream sqlStream =
                    getClass().getResourceAsStream(dropRoot + fi + ".sql");
            if (sqlStream == null) {
                fnf = true;
                break;
            }

            String script = IOUtils.toString(sqlStream);
            String[] sqlStatements = script.split(SQL_SCRIPT_DELIMITER);

            for (String sqlStatement : sqlStatements) {

                // MySQL allows schemas to by system or dba managed. If dba managed we must
                // skip the schema creation phase which is always the first sql script (0.sql)
                if (createSchemas == false && fi == 0) {
                    continue;
                }

                if (schemaName != null) {
                    sqlStatement =
                            sqlStatement.replace(SCHEMA_NAME_PLACEHOLDER,
                                    schemaName);
                }

                try {
                    databaseService.update(dbc, sqlStatement);
                } catch (SQLException e) {
                    log.debug("Error running script, message:\n{}\n\n{}\n",
                            new Object[] { e.getMessage(), sqlStatement });
                    //swallow since DROP statements might fail.
                }
            }
            fi++;
        }
    }

    private void runScript(DBConnection dbc, InputStream sqlStream,
            String schemaName) throws IOException {

        String script = IOUtils.toString(sqlStream);
        String[] sqlStatements = script.split(SQL_SCRIPT_DELIMITER);

        for (String sqlStatement : sqlStatements) {

            if (schemaName != null) {
                sqlStatement =
                        sqlStatement.replace(SCHEMA_NAME_PLACEHOLDER,
                                schemaName);
            }

            try {
                databaseService.update(dbc, sqlStatement);
            } catch (SQLException e) {
                log.debug("Error running script, message:\n{}\n\n{}\n",
                        new Object[] { e.getMessage(), sqlStatement });
                //swallow since DROP statements might fail.
            }
        }
    }
}
