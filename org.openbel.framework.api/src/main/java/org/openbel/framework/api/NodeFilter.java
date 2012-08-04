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

import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.ReturnType;

/**
 * Note: {@link NodeFilter}s are not currently designed to perform filtering on
 * node labels. If such functionality is added, be sure to refactor
 * {@link KamDialect} to support this.
 *
 * @author tchu
 */
public class NodeFilter extends Filter {

    protected NodeFilter(KamInfo kamInfo) {
        super(kamInfo);
    }

    /**
     * Adds a new FunctionReturnFilterCriteria to the Filter
     *
     * @param criteria
     * @return current {@link NodeFilter} for chaining
     */
    public NodeFilter add(FunctionReturnFilterCriteria criteria) {
        getFilterCriteria().add(criteria);
        return this;
    }

    public NodeFilter add(FunctionTypeFilterCriteria criteria) {
        getFilterCriteria().add(criteria);
        return this;
    }

    public static NodeFilter createAbudancesFilter(Kam kam) {
        return createFunctionReturnNodeFilter(kam, ReturnType.getAbundances());
    }

    public static NodeFilter createProcessesFilter(Kam kam) {
        return createFunctionReturnNodeFilter(kam, ReturnType.getProcesses());
    }

    public static NodeFilter createAbundanceFunctionFilter(Kam kam) {
        return createFunctionTypeNodeFilter(kam, FunctionEnum.getAbundances());
    }

    public static NodeFilter createActivityFunctionFilter(Kam kam) {
        return createFunctionTypeNodeFilter(kam, FunctionEnum.getActivities());
    }

    public boolean accept(KamNode node) {
        boolean ret = false;

        // Need to test every criteria
        for (FilterCriteria filterCriteria : getFilterCriteria()) {
            boolean isInclude = filterCriteria.isInclude();
            final boolean matches;
            if (filterCriteria instanceof FunctionTypeFilterCriteria) {
                matches =
                        matches(node,
                                (FunctionTypeFilterCriteria) filterCriteria);
            } else {
                matches =
                        matches(node,
                                (FunctionReturnFilterCriteria) filterCriteria);
            }

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

    private boolean
            matches(KamNode node, FunctionReturnFilterCriteria criteria) {
        for (ReturnType returnType : criteria.getValues()) {
            // Check for a match
            if (node.getFunctionType().getReturnType() == returnType) {
                return true;
            }
        }

        return false;
    }

    private boolean matches(final KamNode node,
            FunctionTypeFilterCriteria criteria) {
        for (FunctionEnum functionType : criteria.getValues()) {
            // Check for a match
            if (node.getFunctionType() == functionType) {
                return true;
            }
        }

        return false;
    }

    private static NodeFilter createFunctionReturnNodeFilter(Kam kam,
            Set<ReturnType> returnTypes) {
        FunctionReturnFilterCriteria filter =
                new FunctionReturnFilterCriteria();
        filter.setInclude(true);
        filter.getValues().addAll(returnTypes);

        NodeFilter nodeFilter = kam.createNodeFilter();
        nodeFilter.add(filter);

        return nodeFilter;
    }

    private static NodeFilter createFunctionTypeNodeFilter(final Kam kam,
            Set<FunctionEnum> functionTypes) {
        final FunctionTypeFilterCriteria criteria =
                new FunctionTypeFilterCriteria();
        criteria.setInclude(true);
        criteria.getValues().addAll(functionTypes);

        NodeFilter nodeFilter = kam.createNodeFilter();
        nodeFilter.add(criteria);
        return nodeFilter;
    }
}
