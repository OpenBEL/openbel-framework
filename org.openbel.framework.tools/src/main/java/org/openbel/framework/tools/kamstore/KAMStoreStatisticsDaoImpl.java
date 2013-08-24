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
package org.openbel.framework.tools.kamstore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.internal.KAMDao;

public class KAMStoreStatisticsDaoImpl extends AbstractJdbcDAO implements
        KAMDao {

    private static final String SELECT_COUNT_PROTO_NODES_SQL =
            "SELECT COUNT(*) FROM @.kam_node";
    private static final String SELECT_COUNT_PROTO_EDGES_SQL =
            "SELECT COUNT(*) FROM @.kam_edge";
    private static final String SELECT_COUNT_TERMS_SQL =
            "SELECT COUNT(*) FROM @.term";
    private static final String SELECT_COUNT_ANNOTATIONS_SQL =
            "SELECT COUNT(*) FROM @.annotation";
    private static final String SELECT_COUNT_ANNOTATION_TYPES_SQL =
            "SELECT COUNT(*) FROM @.annotation_definition";
    private static final String SELECT_COUNT_DOCUMENTS_SQL =
            "SELECT COUNT(*) FROM @.document_header_information";
    private static final String SELECT_COUNT_NAMESPACES_SQL =
            "SELECT COUNT(*) FROM @.namespace";
    private static final String SELECT_COUNT_STATEMENTS_SQL =
            "SELECT COUNT(DISTINCT kesm.statement_id) FROM @.kam_edge_statement_map kesm";
    private static final String SELECT_COUNT_KAM_NODE_PARAMETERS_SQL =
            "SELECT COUNT(*) FROM @.kam_node_parameter";
    private static final String SELECT_COUNT_UNIQUE_KAM_NODE_PARAMETERS_SQL =
            "SELECT COUNT(kam_global_parameter_id) FROM @.kam_node_parameter";

    /**
     * Creates a KAMStoreStatisticsDaoImpl from a JDBC {@link Connection} to the
     * KAM store.
     *
     * @param dbc {@link Connection}, the database connection which should be
     * non-null and already open for SQL execution.
     * @throws InvalidArgument - Thrown if {@code dbc} is null or the SQL
     * connection is already closed.
     * @throws SQLException - Thrown if an SQL error occurred.
     */
    public KAMStoreStatisticsDaoImpl(String schemaName, DBConnection dbc)
            throws SQLException {
        super(dbc, schemaName);

        if (dbc == null) {
            throw new InvalidArgument("dbc is null");
        }

        if (dbc.getConnection().isClosed()) {
            throw new InvalidArgument("dbc is closed and cannot be used");
        }
    }

    /**
     * Returns the number of nodes in the KAM.
     * @return
     * @throws SQLException
     */
    public int getKamNodeCount() throws SQLException {
        return getSingleResult(SELECT_COUNT_PROTO_NODES_SQL);
    }

    /**
     * Returns the number of edges in the KAM.
     * @return
     * @throws SQLException
     */
    public int getKamEdgeCount() throws SQLException {
        return getSingleResult(SELECT_COUNT_PROTO_EDGES_SQL);
    }

    /**
     * Returns the number of BEL documents in the KAM.
     * @return
     * @throws SQLException
     */
    public int getBELDocumentCount() throws SQLException {
        return getSingleResult(SELECT_COUNT_DOCUMENTS_SQL);
    }

    /**
     * Returns the number of namespaces in the KAM.
     * @return
     * @throws SQLException
     */
    public int getNamespaceCount() throws SQLException {
        return getSingleResult(SELECT_COUNT_NAMESPACES_SQL);
    }

    /**
     * Returns the number of annotation definitions in the KAM.
     * @return
     * @throws SQLException
     */
    public int getAnnotationDefinitonCount() throws SQLException {
        return getSingleResult(SELECT_COUNT_ANNOTATION_TYPES_SQL);
    }

    /**
     * Returns the number of annotations in the KAM.
     * @return
     * @throws SQLException
     */
    public int getAnnotationCount() throws SQLException {
        return getSingleResult(SELECT_COUNT_ANNOTATIONS_SQL);
    }

    /**
     * Returns the number of statements in the KAM.
     * @return
     * @throws SQLException
     */
    public int getStatementCount() throws SQLException {
        return getSingleResult(SELECT_COUNT_STATEMENTS_SQL);
    }

    /**
     * Returns the number of terms in the KAM.
     * @return
     * @throws SQLException
     */
    public int getTermCount() throws SQLException {
        return getSingleResult(SELECT_COUNT_TERMS_SQL);
    }

    /**
     * Returns the number of parameters in the KAM.
     * @return
     * @throws SQLException
     */
    public int getParameterCount() throws SQLException {
        return getSingleResult(SELECT_COUNT_KAM_NODE_PARAMETERS_SQL);
    }

    /**
     * Returns the number of unique parameters in the KAM.
     * @return
     * @throws SQLException
     */
    public int getUniqueParameterCount() throws SQLException {
        return getSingleResult(SELECT_COUNT_UNIQUE_KAM_NODE_PARAMETERS_SQL);
    }

    private int getSingleResult(String sql) throws SQLException {
        ResultSet rset = null;
        try {
            PreparedStatement ps = getPreparedStatement(sql);
            rset = ps.executeQuery();
            rset.next();
            return rset.getInt(1);
        } finally {
            close(rset);
        }
    }

}
