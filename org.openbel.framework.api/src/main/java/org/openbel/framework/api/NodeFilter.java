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

import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.ReturnType;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

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
