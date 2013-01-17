package org.openbel.framework.api;

import java.util.Map;

import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;

/**
 * Orthologize defines functions for orthologizing a {@link Kam kam}.
 */
public interface Orthologize {

    /**
     * Retrieve a mapping of orthologous {@link KamNode kam nodes} to the
     * target species {@link KamNode kam nodes}.  
     * 
     * @param kam {@link Kam}; may not be {@code null}
     * @param kAMStore {@link KAMStore}; may not be {@code null}
     * @param dialect {@link SpeciesDialect}; may not be {@code null}
     * @return {@link Map} of orthologous {@link KamNode kam node} to the
     * target species {@link KamNode kam nodes}
     * @throws InvalidArgument when {@code kam}, {@code kAMStore}, or
     * {@code dialect} is {@code null}
     */
    public Map<KamNode, KamNode> orthologousNodes(Kam kam, KAMStore kAMStore,
            SpeciesDialect dialect);
    
    /**
     * Collapses orthologous relationships in a {@link Kam kam} and returns an
     * {@link OrthologizedKam orthologized kam}.
     * 
     * @param kam {@link Kam}; may not be {@code null}
     * @param kAMStore {@link KAMStore}; may not be {@code null}
     * @param dialect {@link SpeciesDialect}; may not be {@code null}
     * @return {@link OrthologizedKam}
     * @throws InvalidArgument when {@code kam}, {@code kAMStore}, or
     * {@code dialect} is {@code null}
     */
    public OrthologizedKam orthologize(Kam kam, KAMStore kAMStore,
            SpeciesDialect dialect);
}
