package org.openbel.framework.api.internal;

import java.sql.SQLException;

import org.openbel.framework.api.Kam;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.RelationshipType;

/**
 * KAMUpdateDao defines {@link Kam} update operations.
 */
public interface KAMUpdateDao extends KAMDao {

    /**
     * Collapse a {@link KamNode kam node} to another {@link KamNode kam node}.
     * 
     * <p>
     * Collapsing a {@link KamNode kam node} involves the following:<ul>
     * <li>Redirect incoming and outgoing {@link KamEdge kam edges} from
     * {@code collapsingNode} to {@code collapseToNode}</li>
     * <li>Delete {@code collapsingNode}</li>
     * <li>Move supporting {@link BelTerm BEL term} data from
     * {@code collapsingNode} to {@code collapseToNode}</li></ul>  
     * 
     * @param collapsing {@link KamNode} node to collapse; may not be
     * {@code null}
     * @param collapseTo {@link KamNode} the collapse target; may not be
     * {@code null}
     * @return {@code true} if the collapse occurred; {@code false} if the
     * collapse did not occur
     * @throws SQLException when a SQL error occurred collapsing kam nodes and
     * edges
     * @throws InvalidArgument when {@code collapsing} is {@code null},
     * {@code collapseTo} is {@code null}, either node references a
     * {@code null} kam, or both node kams are not the same
     */
    public boolean collapseKamNode(KamNode collapsing, KamNode collapseTo)
            throws SQLException;
    
    /**
     * Remove {@link KamEdge kam edges} for a specific
     * {@link RelationshipType relationship}.
     * 
     * @param relationship {@link RelationshipType}; may not be {@code null}
     * @return {@code int} records deleted (kam edges + statements)
     * @throws SQLException when a SQL error occurred deleting records
     */
    public int removeKamEdges(RelationshipType relationship)
            throws SQLException;
}
