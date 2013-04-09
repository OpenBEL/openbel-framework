/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;

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
