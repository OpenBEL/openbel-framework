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
package org.openbel.framework.compiler;

import org.openbel.framework.common.DBConnectionFailure;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.core.compiler.CreateKAMFailure;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.DatabaseError;
import org.openbel.framework.core.kam.KAMCatalogFailure;
import org.openbel.framework.internal.KamDbObject;

/**
 * BEL compiler phase four interface.
 * <p>
 * Phase four compilation consists of:
 * <ol>
 * <li><b>Stage 1 -- Create KAMstore</b><br>
 * Create the KAMstore schema using the database connection.</li>
 * </ol>
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface DefaultPhaseFour {

    /**
     * Executes stage on creation of {@link DBConnection} for the KAMstore
     * identified by the {@code jdbcUrl}, {@code user}, and {@code pass}.
     *
     * @param jdbcUrl {@link String}, the jdbc url
     * @param user {@link String}, the database username
     * @param pass {@link String}, the database password
     * @return {@link DBConnection}, the database connection for the
     * {@code jdbcUrl}
     * @throws DBConnectionFailure Thrown if the KamStore database cannot be
     * connected to
     */
    public DBConnection stage1ConnectKAMStore(String jdbcUrl, String user,
            String pass) throws DBConnectionFailure;

    /**
     * Executes stage two updating of the kam entry in the KAM catalog.
     *
     * @param kamDb {@link KamDbObject}, the kam database object to save
     * @throws KAMCatalogFailure Thrown if an error occurred when setting up or
     * updating the KAM catalog
     */
    public String stage2SaveToKAMCatalog(KamDbObject kamDb)
            throws KAMCatalogFailure;

    /**
     * Executes stage three creation of KAMstore using a {@link DBConnection}
     * database connection.
     *
     * @param dbConnection {@link DBConnection} the database connection
     * @param schemaName {@link String}, the schema name to create KAM in
     * @throws CreateKAMFailure - Thrown if an error occurred while creating the
     * KAMstore schema.
     */
    public void stage3CreateKAMstore(DBConnection dbConnection,
            String schemaName) throws CreateKAMFailure;

    /**
     * Executes stage four loading of KAM using a {@link DBConnection} database
     * connection against a KAMstore schema.
     *
     * @param dbConnection {@link DBConnection}, the database connection
     * @param p2pn {@link ProtoNetwork}, the phase II proto network output
     * @throws CreateKAMFailure Thrown if the KAM schema to be loaded does not
     * exist
     * @throws DatabaseError Thrown if a database error occurred while loading
     * the KAM.
     */
    public void stage4LoadKAM(DBConnection dbConnection, ProtoNetwork p2pn,
            String kamSchemaName) throws DatabaseError, CreateKAMFailure;
}
