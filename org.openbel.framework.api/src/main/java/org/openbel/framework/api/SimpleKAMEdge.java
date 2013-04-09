package org.openbel.framework.api;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.common.enums.RelationshipType;

/**
 * This interface describes the absolute minimum needed to represent a KAM edge.
 * <p>
 * This interface provides a small and simple contract for the sufficient
 * conditions to be a KAM edge.
 * </p>
 *
 * @since 3.0.0
 * @see KamEdge
 */
public interface SimpleKAMEdge {

    /**
     * Returns the database identifier for this edge.
     *
     * @return int
     */
    public int getID();

    /**
     * Returns the edge's relationship.
     *
     * @return {@link RelationshipType}
     */
    public RelationshipType getRelationship();

    /**
     * Returns the integer uniquely identifying the source node.
     *
     * @return int
     */
    public int getSourceID();

    /**
     * Returns the integer uniquely identifying the target node.
     *
     * @return int
     */
    public int getTargetID();

}
