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

import java.util.HashSet;
import java.util.Set;

import org.openbel.framework.internal.KAMStoreDaoImpl.Citation;

public class CitationFilterCriteria extends FilterCriteria {

    private Set<Citation> valueSet;

    /**
     *
     */
    public CitationFilterCriteria() {
        super();
        this.valueSet = new HashSet<Citation>();
    }

    /**
     * @return
     */
    @Override
    public Set<Citation> getValues() {
        return valueSet;
    }

    /**
     * @param citation
     */
    public void add(Citation citation) {
        valueSet.add(citation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof CitationFilterCriteria)) {
            return false;
        } else {
            CitationFilterCriteria other = (CitationFilterCriteria) obj;
            return ((isInclude() == other.isInclude()) && valueSet
                    .equals(other.valueSet));
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 0;
        hash += (isInclude() ? 1 : 0);
        hash *= prime;
        hash += valueSet.hashCode();
        return hash;
    }
}
