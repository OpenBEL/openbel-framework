package org.openbel.framework.api.internal;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.common.model.Statement;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Defines a {@link KAMDao} that retrieves BEL evidence for one or more edges.
 */
public interface EvidenceDao extends KAMDao {

    /**
     * Retrieves evidence for one or more edges; {@code null} if all edges
     * have no BEL evidence (e.g. do not exist in the KAM).
     *
     * @param edges {@link Collection} of {@link KamEdge}
     * @throws SQLException if an error occurred querying the KAM
     * @return {@link List} of {@link Statement}
     */
    public Map<KamEdge, List<Statement>> evidence(Collection<KamEdge> edges)
            throws SQLException;

    /**
     * Retrieves evidence for one or more edges.  A {@link KAMCatalogDao.AnnotationFilter}
     * is applied to the evidence to filter the returned results.
     *
     * @param edges {@link Collection} of {@link KamEdge}
     * @throws SQLException if an error occurred querying the KAM
     * @return {@link List} of {@link Statement}
     */
    public Map<KamEdge, List<Statement>> evidence(Collection<KamEdge> edges, KAMCatalogDao.AnnotationFilter filter)
            throws SQLException;
}
