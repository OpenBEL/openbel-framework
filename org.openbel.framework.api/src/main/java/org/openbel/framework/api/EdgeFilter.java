/**
 * Copyright (C) 2012 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
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
