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
package org.openbel.framework.compiler.kam;

import java.io.IOException;
import java.sql.SQLException;

import org.openbel.framework.api.internal.KamDbObject;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.core.df.DBConnection;

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
