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
package org.openbel.framework.api;

import java.util.Set;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

/**
 * Note: {@link EdgeFilter}s are not currently designed to perform filtering on
 * node labels. If such functionality is added, be sure to refactor
 * {@link KamDialect} to support this.
 *
 * @author tchu
 */
public class EdgeFilter extends Filter {

    protected EdgeFilter(KamInfo kamInfo) {
        super(kamInfo);
    }

    /**
     * Adds a new RelationshipTypeFilterCriteria to the Filter
     *
     * @param criteria
     * @return current {@link EdgeFilter} for chaining
     */
    public EdgeFilter add(RelationshipTypeFilterCriteria criteria) {
        getFilterCriteria().add(criteria);
        return this;
    }

    /**
     *
     * @param kam
     * @return
     */
    public static EdgeFilter createDirectEdgeFilter(Kam kam) {
        return createEdgeFilter(kam, RelationshipType.getDirectRelationships());
    }

    /**
     *
     * @param kam
     * @return
     */
    public static EdgeFilter createCausalEdgeFilter(Kam kam) {
        return createEdgeFilter(kam, RelationshipType.getCausalRelationships());
    }

    /**
     *
     * @param kam
     * @return
     */
    public static EdgeFilter createCorrelativeEdgeFilter(Kam kam) {
        return createEdgeFilter(kam,
                RelationshipType.getCorrelativeRelationships());
    }

    public boolean accept(KamEdge kamEdge) {
        boolean ret = false;

        // Need to test every criteria
        for (FilterCriteria filterCriteria : getFilterCriteria()) {
            boolean isInclude = filterCriteria.isInclude();
            RelationshipTypeFilterCriteria relationshipTypeFilterCriteria =
                    (RelationshipTypeFilterCriteria) filterCriteria;

            boolean matches = matches(kamEdge, relationshipTypeFilterCriteria);

            if (matches) {
                if (isInclude) {
                    // matches the included criteria, so it passes
                    return true;
                }

                // matches the excluded criteria, so it fails
                return false;
            } else if (!isInclude) {
                // doesn't match the excluded criteria, so it passes
                ret = true;
            }
        }

        return ret;
    }

    private boolean matches(KamEdge kamEdge,
            RelationshipTypeFilterCriteria relationshipTypeFilterCriteria) {
        // Check to see if this node function return is in the list
        for (RelationshipType relationshipType : relationshipTypeFilterCriteria
                .getValues()) {
            // Check for a match
            if (kamEdge.getRelationshipType() == relationshipType) {
                return true;
            }
        }
        return false;
    }

    private static EdgeFilter createEdgeFilter(Kam kam,
            Set<RelationshipType> relationships) {
        final RelationshipTypeFilterCriteria filter =
                new RelationshipTypeFilterCriteria();
        filter.setInclude(true);
        filter.getValues().addAll(relationships);

        EdgeFilter edgeFilter = kam.createEdgeFilter();
        edgeFilter.add(filter);
        return edgeFilter;
    }
}
