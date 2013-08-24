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

import org.openbel.framework.common.enums.RelationshipType;

/**
 *
 * @author julianjray
 *
 */
public class RelationshipTypeFilterCriteria extends FilterCriteria {

    private Set<RelationshipType> valueSet;

    /**
     *
     */
    public RelationshipTypeFilterCriteria() {
        super();
        this.valueSet = new HashSet<RelationshipType>();
    }

    /**
     *
     * @return
     */
    @Override
    public Set<RelationshipType> getValues() {
        return valueSet;
    }

    /**
     *
     * @param relationshipType
     * @return current {@link RelationshipTypeFilterCriteria} for chaining
     */
    public RelationshipTypeFilterCriteria
            add(RelationshipType relationshipType) {
        valueSet.add(relationshipType);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof RelationshipTypeFilterCriteria)) {
            return false;
        } else {
            RelationshipTypeFilterCriteria other =
                    (RelationshipTypeFilterCriteria) obj;
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
