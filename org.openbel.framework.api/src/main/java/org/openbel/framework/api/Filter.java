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

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

public abstract class Filter {

    private KamInfo kamInfo;
    private List<FilterCriteria> filterCriteria =
            new ArrayList<FilterCriteria>();

    protected Filter(KamInfo kamInfo) {
        this.kamInfo = kamInfo;
    }

    public KamInfo getKamInfo() {
        return this.kamInfo;
    }

    public List<FilterCriteria> getFilterCriteria() {
        return filterCriteria;
    }

    protected void setKamInfo(KamInfo kamInfo) {
        this.kamInfo = kamInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        if (kamInfo != null) {
            result *= prime;
            result += kamInfo.hashCode();
        }

        if (filterCriteria != null) {
            result *= prime;
            result += filterCriteria.hashCode();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Filter)) return false;
        Filter f = (Filter) obj;

        if (kamInfo == null) {
            if (f.kamInfo != null) return false;
        } else if (!kamInfo.equals(f.kamInfo)) return false;

        if (filterCriteria == null) {
            if (f.filterCriteria != null) return false;
        } else if (!filterCriteria.equals(f.filterCriteria)) return false;

        return true;
    }

}
