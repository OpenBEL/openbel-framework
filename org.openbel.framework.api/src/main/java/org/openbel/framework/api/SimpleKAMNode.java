package org.openbel.framework.api;

import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.enums.FunctionEnum;

/**
 * This interface describes the absolute minimum needed to represent a KAM node.
 * <p>
 * This interface provides a small and simple contract for the sufficient
 * conditions to be a KAM node.
 * </p>
 * 
 * @since 3.0.0
 * @see KamNode
 */
public interface SimpleKAMNode {

    /**
     * Returns the database identifier for this node.
     * 
     * @return int
     */
    public int getID();

    /**
     * Returns the node's function.
     * 
     * @return {@link FunctionEnum}
     */
    public FunctionEnum getFunction();

    /**
     * Returns the node's label.
     * 
     * @return {@link String}
     */
    public String getLabel();

}
