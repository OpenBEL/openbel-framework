package org.openbel.framework.compiler;

import static java.lang.String.format;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.api.internal.KamDbObject;
import org.openbel.framework.common.DBConnectionFailure;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.protonetwork.model.DocumentTable.DocumentHeader;
import org.openbel.framework.common.protonetwork.model.NamespaceTable;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.compiler.kam.KAMStoreSchemaService;
import org.openbel.framework.core.compiler.CreateKAMFailure;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.DatabaseError;
import org.openbel.framework.core.df.DatabaseService;
import org.openbel.framework.core.kam.JdbcKAMLoaderImpl;
import org.openbel.framework.core.kam.KAMCatalogFailure;

/**
 * BEL compiler phase four implementation.
 */
public class PhaseFourImpl implements DefaultPhaseFour {

    /**
     * Defines the DatabaseService used to connect/execute against a database.
     */
    private final DatabaseService ds;

    /**
     * Defines the KAMStoreSchemaService used to create a KAMstore.
     */
    private final KAMStoreSchemaService ksss;

    /**
     * Creates a phase four implementation.
     *
     * @param ds {@link DatabaseService}, the database service
     * @param ksss {@link KAMStoreSchemaService}, the kam store schema service
     */
    public PhaseFourImpl(final DatabaseService ds,
            final KAMStoreSchemaService ksss) {
        this.ds = ds;
        this.ksss = ksss;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DBConnection stage1ConnectKAMStore(String jdbcUrl, String user,
            String pass) throws DBConnectionFailure {
        try {
            return ds.dbConnection(jdbcUrl, user, pass);
        } catch (SQLException e) {
            // rethrow as fatal exception since we couldn't connect to KAMStore
            throw new DBConnectionFailure(jdbcUrl, e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String stage2SaveToKAMCatalog(KamDbObject kamDb)
            throws KAMCatalogFailure {
        if (kamDb == null) {
            throw new InvalidArgument("kamDb", kamDb);
        }

        try {
            // Load the KAM catalog schema, if it doesn't exist.
            ksss.setupKAMCatalogSchema();

            // Saves to KAM catalog and retrieves schema name.
            String schemaName = ksss.saveToKAMCatalog(kamDb);

            return schemaName;
        } catch (SQLException e) {
            throw new KAMCatalogFailure(kamDb.getName(), e.getMessage());
        } catch (IOException e) {
            throw new KAMCatalogFailure(kamDb.getName(), e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stage3CreateKAMstore(final DBConnection db, String schemaName)
            throws CreateKAMFailure {
        if (db == null) {
        	throw new InvalidArgument("db", db);
        }

        try {
            ksss.setupKAMStoreSchema(db, schemaName);
        } catch (IOException e) {
            throw new CreateKAMFailure(db, e.getMessage());
        } catch (SQLException e) {
            throw new CreateKAMFailure(db, e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws SQLException
     */
    @Override
    public void stage4LoadKAM(DBConnection dbConnection, ProtoNetwork p2pn,
            String schema) throws DatabaseError, CreateKAMFailure {
        JdbcKAMLoaderImpl jkl;

        try {
            jkl = new JdbcKAMLoaderImpl(dbConnection, schema);
        } catch (SQLException e) {
            final String msg = "Error creating KAM loader";
            throw new DatabaseError(schema, msg, e);
        }

        if (!jkl.schemaExists()) {
            final String fmt = "schema \"%s\" does not exist";
            final String msg = format(fmt, jkl.getSchemaName());
            throw new CreateKAMFailure(dbConnection, msg);
        }

        try {
            // load type tables
            jkl.loadObjectTypes();
        } catch (SQLException e) {
            final String msg = "Error loading object types";
            throw new DatabaseError(schema, msg, e);
        }

        try {
            jkl.loadFunctionTypes();
        } catch (SQLException e) {
            final String msg = "Error loading function types";
            throw new DatabaseError(schema, msg, e);
        }

        try {
            jkl.loadRelationshipTypes();
        } catch (SQLException e) {
            final String msg = "Error loading relationship types";
            throw new DatabaseError(schema, msg, e);
        }

        try {
            jkl.loadAnnotationDefinitionTypes();
        } catch (SQLException e) {
            final String msg = "Error loading annotation definitions types";
            throw new DatabaseError(schema, msg, e);
        }

        // load documents
        List<DocumentHeader> dhs = p2pn.getDocumentTable().getDocumentHeaders();
        try {
            jkl.loadDocuments(dhs);
        } catch (SQLException e) {
            final String msg = "Error loading documents";
            throw new DatabaseError(schema, msg, e);
        }

        // load namespaces
        NamespaceTable nt = p2pn.getNamespaceTable();
        Set<TableNamespace> nsl = nt.getNamespaces();
        Map<TableNamespace, Integer> nsi = nt.getNamespaceIndex();
        for (TableNamespace ns : nsl) {
            try {
                jkl.loadNamespace(nsi.get(ns), ns);
            } catch (SQLException e) {
                final String fmt = "Error loading namespace %s/%s";
                final String msg =
                        format(fmt, ns.getPrefix(), ns.getResourceLocation());
                throw new DatabaseError(schema, msg, e);
            }
        }

        // load annotation definitions
        try {
            jkl.loadAnnotationDefinitions(p2pn.getAnnotationDefinitionTable());
        } catch (SQLException e) {
            final String msg = "Error loading annotation definitions";
            throw new DatabaseError(schema, msg, e);
        }

        try {
            // load annotations
            jkl.loadAnnotationValues(p2pn.getAnnotationValueTable());
        } catch (SQLException e) {
            final String msg = "Error loading annotation values";
            throw new DatabaseError(schema, msg, e);
        }

        try {
            // load document to namespace map
            jkl.loadDocumentNamespaceMap(nt.getDocumentNamespaces());
        } catch (SQLException e) {
            final String msg = "Error loading document namespaces";
            throw new DatabaseError(schema, msg, e);
        }

        try {
            // load nodes
            jkl.loadNodes(p2pn.getNamespaceTable(), p2pn.getParameterTable(),
                    p2pn.getTermTable(),
                    p2pn.getTermParameterMapTable(),
                    p2pn.getProtoNodeTable());
        } catch (SQLException e) {
            final String msg = "Error loading nodes";
            throw new DatabaseError(schema, msg, e);
        }

        try {
            // load edges
            jkl.loadEdges(p2pn.getStatementTable(), p2pn.getTermTable(),
                    p2pn.getProtoNodeTable(), p2pn.getProtoEdgeTable());
        } catch (SQLException e) {
            final String msg = "Error loading edges";
            throw new DatabaseError(schema, msg, e);
        }

        try {
            // associate annotations to statements
            jkl.loadStatementAnnotationMap(p2pn
                    .getStatementAnnotationMapTable());
        } catch (SQLException e) {
            final String msg = "Error loading statement annotations";
            throw new DatabaseError(schema, msg, e);
        }

        // close loader dao
        jkl.terminate();
    }
}
