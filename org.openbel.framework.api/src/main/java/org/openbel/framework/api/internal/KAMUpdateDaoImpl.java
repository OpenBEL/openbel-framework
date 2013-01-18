package org.openbel.framework.api.internal;

import static org.openbel.framework.api.EdgeDirectionType.FORWARD;
import static org.openbel.framework.api.EdgeDirectionType.REVERSE;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import org.openbel.framework.api.Kam;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.core.df.DBConnection;

/**
 * KAMUpdateDaoImpl implements {@link Kam} update operations.
 */
public class KAMUpdateDaoImpl extends AbstractJdbcDAO implements KAMUpdateDao {

    private static final String UPDATE_KAM_EDGES_SOURCE = "update @.kam_edge " +
    		"set kam_source_node_id = ? where kam_edge_id = ?";
    private static final String UPDATE_KAM_EDGES_TARGET = "update @.kam_edge " +
            "set kam_target_node_id = ? where kam_edge_id = ?";
    private static final String UPDATE_TERM = "update @.term set kam_node_id = ? " +
    		"where kam_node_id = ?";
    private static final String DELETE_KAM_NODE_PARAMETER =
            "delete from @.kam_node_parameter where kam_node_id = ?";
    private static final String DELETE_KAM_NODE =
            "delete from @.kam_node where kam_node_id = ?";
    private static final String DELETE_ORTHOLOGOUS_KAM_EDGES =
            "delete from @.kam_edge where relationship_type_id = ?";
    private static final String DELETE_ORTHOLOGOUS_STATEMENTS =
            "delete from @.statement where relationship_type_id = ?";
    
    /**
     * Constructs the dao.
     * 
     * @param c {@link DBConnection}; may not be {@code null}
     * @param schema {@link String}; may not be {@code null}
     * @throws SQLException when an error occurs checking if {@code c} is open
     * @throws InvalidArgument when {@code c} is {@code null}, {@code c} is
     * closed, or {@code schema} is {@code null}
     */
    public KAMUpdateDaoImpl(DBConnection c, String schema)
            throws SQLException {
        super(c, schema);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean collapseKamNode(KamNode collapsing, KamNode collapseTo)
            throws SQLException {
        if (collapsing == null)
            throw new InvalidArgument("collapsing node is null");
        if (collapseTo == null)
            throw new InvalidArgument("collapseTo node is null");
        if (collapsing.getKam() == null || collapseTo.getKam() == null
                || collapsing.getKam() != collapseTo.getKam())
            throw new InvalidArgument("nodes reference invalid kams");
        if (collapsing.getId() == null || collapseTo.getId() == null)
            throw new InvalidArgument("node id is null");
        
        Kam kam = collapsing.getKam();
        
        PreparedStatement esps = getPreparedStatement(UPDATE_KAM_EDGES_SOURCE);
        PreparedStatement etps = getPreparedStatement(UPDATE_KAM_EDGES_TARGET);
        remapEdges(collapsing, collapseTo, kam, esps, etps);
        
        PreparedStatement utps = getPreparedStatement(UPDATE_TERM);
        remapTerms(collapsing, collapseTo, utps);
        
        PreparedStatement knps = getPreparedStatement(DELETE_KAM_NODE);
        PreparedStatement knpps = getPreparedStatement(DELETE_KAM_NODE_PARAMETER);
        removeKamNode(collapsing, knps, knpps);

        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int removeKamEdges(RelationshipType relationship)
            throws SQLException {
        if (relationship == null || relationship.getValue() == null)
            throw new InvalidArgument("relationship is null");
        
        int updates = 0;
        int rvalue = relationship.getValue();
        PreparedStatement keps = getPreparedStatement(DELETE_ORTHOLOGOUS_KAM_EDGES);
        keps.setInt(1, rvalue);
        updates += keps.executeUpdate();
        
        PreparedStatement sps = getPreparedStatement(DELETE_ORTHOLOGOUS_STATEMENTS);
        sps.setInt(1, rvalue);
        updates += sps.executeUpdate();
        
        return updates;
    }

    /**
     * Remaps outgoing and incoming edges to {@code collapseTo}.
     * 
     * @param collapsing {@link KamNode} being collapsed
     * @param collapseTo {@link KamNode} collapsing to
     * @param kam {@link Kam} to retrieve adjacent edges
     * @param esps {@link PreparedStatement} for updating edge source
     * @param etps {@link PreparedStatement} for updating edge target
     * @return {@code int} update count
     * @throws SQLException when a SQL error occurred with update
     */
    private static int remapEdges(KamNode collapsing, KamNode collapseTo,
            Kam kam, PreparedStatement esps, PreparedStatement etps)
            throws SQLException {
        int updates = 0;
        Set<KamEdge> outgoing = kam.getAdjacentEdges(collapsing, FORWARD);
        int collapseToId = collapseTo.getId();
        for (KamEdge edge : outgoing) {
            esps.setInt(1, collapseToId);
            esps.setInt(2, edge.getId());
            updates += esps.executeUpdate();
        }
        Set<KamEdge> incoming = kam.getAdjacentEdges(collapsing, REVERSE);
        for (KamEdge edge : incoming) {
            etps.setInt(1, collapseToId);
            etps.setInt(2, edge.getId());
            updates += etps.executeUpdate();
        }
        return updates;
    }
    
    /**
     * Remaps terms to {@code collapseTo}.
     * 
     * @param collapsing {@link KamNode} being collapsed
     * @param collapseTo {@link KamNode} collapsing to
     * @param utps {@link PreparedStatement} for updating term
     * @return {@code int} update count
     * @throws SQLException when a SQL error occurred with update
     */
    private static int remapTerms(KamNode collapsing, KamNode collapseTo,
            PreparedStatement utps) throws SQLException {
        int collapsingId = collapsing.getId();
        int collapseToId = collapseTo.getId();
        
        utps.setInt(1, collapseToId);
        utps.setInt(2, collapsingId);
        return utps.executeUpdate();
    }
    
    /**
     * Removes {@code collapsing} node.
     * 
     * @param collapsing {@link KamNode} being collapsed
     * @param knps {@link PreparedStatement} for updating kam_node
     * @param knpps {@link PreparedStatement} for updating kam_node_parameter
     * @return {@code int} update count
     * @throws SQLException when a SQL error occurred with delete
     */
    private static int removeKamNode(KamNode collapsing,
            PreparedStatement knps, PreparedStatement knpps)
            throws SQLException {
        int collapsingId = collapsing.getId();
        
        int updates = 0;
        knpps.setInt(1, collapsingId);
        updates += knpps.executeUpdate();
        knps.setInt(1, collapsingId);
        updates += knps.executeUpdate();
        return updates;
    }
}
