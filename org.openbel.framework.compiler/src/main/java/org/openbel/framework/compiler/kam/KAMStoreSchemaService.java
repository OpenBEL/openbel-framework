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
package org.openbel.framework.compiler.kam;

import java.io.IOException;
import java.sql.SQLException;

import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.internal.KamDbObject;

/**
 * KAMSchemaSetupService defines a service to setup a KAM schema instance
 * to store a single KAM.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface KAMStoreSchemaService {

    /**
     * Set up the KAM catalog schema (DDL) in the kam catalog schema defined
     * by {@link SystemConfiguration}.
     *
     * @throws SQLException Thrown if a SQL error occurred setting up schema
     * @throws IOException Thrown if an I/O error occurred reading SQL files
     * needed to set up schema
     */
    public void setupKAMCatalogSchema()
            throws SQLException, IOException;

    /**
     * Set up a KAMstore schema for the specific {@link DBConnection}.  This
     * includes the DDL to establish the schema.
     *
     * @param dbc {@link DBConnection}, the database connection to use
     * @param schemaName {@link String}, the kam schema name, which cannot be
     * null or empty
     * @throws SQLException Thrown if a SQL error occurred setting up schema
     * @throws IOException Thrown if an I/O error occurred reading SQL files
     * needed to set up schema
     */
    public void setupKAMStoreSchema(DBConnection dbc, String schemaName)
            throws SQLException, IOException;

    /**
     * Delete a KAMstore schema for the specific {@link DBConnection}.
     *
     * @param dbc {@link DBConnection}, the database connection to use
     * @param schemaName {@link String}, the kam schema name
     * @throws SQLException Thrown if a SQL error occurred deleting the schema
     * @throws IOException Thrown if an I/O error occurred reading SQL files
     * needed to delete schema
     */
    public boolean deleteKAMStoreSchema(DBConnection dbc, String schemaName)
            throws SQLException, IOException;

    /**
     * Saves the {@link KamDbObject} to the kam catalog.
     *
     * @param kamDb {@link KamDbObject}, the kam database object
     * @return {@link String} the KAM schema name
     * @throws SQLException Thrown if a SQL error occurred saving the
     * <tt>kamDb</tt> to the KAM catalog.
     */
    public String saveToKAMCatalog(KamDbObject kamDb)
            throws SQLException;

    /**
     * Deletes the kam info object with a given name from the kam catalog.
     *
     * @param kamName, the name of a kam database object
     * @throws SQLException Thrown if a SQL error occurred deleting the
     * kam database object from the KAM catalog.
     */
    public void deleteFromKAMCatalog(String kamName)
            throws SQLException;
}
