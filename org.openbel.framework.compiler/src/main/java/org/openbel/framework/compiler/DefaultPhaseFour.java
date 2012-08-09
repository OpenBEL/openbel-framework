package org.openbel.framework.compiler;

import org.openbel.framework.api.internal.KamDbObject;
import org.openbel.framework.common.DBConnectionFailure;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.core.compiler.CreateKAMFailure;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.DatabaseError;
import org.openbel.framework.core.kam.KAMCatalogFailure;

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
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public interface DefaultPhaseFour {

    /**
     * Executes stage one creation of {@link DBConnection} for the KAMstore
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
