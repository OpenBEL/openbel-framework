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

import org.openbel.framework.common.BELUtilities;
import org.openbel.framework.internal.KAMStoreDaoImpl.AnnotationType;

/**
 * @author julianjray
 */
public class AnnotationFilterCriteria extends FilterCriteria {

    private final AnnotationType annotationType;
    private Set<String> valueSet;

    /**
     * @param annotationType
     */
    public AnnotationFilterCriteria(AnnotationType annotationType) {
        super();
        this.annotationType = annotationType;
        this.valueSet = new HashSet<String>();
    }

    /**
     * @return
     */
    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    /**
     * @return
     */
    @Override
    public Set<String> getValues() {
        return valueSet;
    }

    /**
     * @param value
     */
    public void add(String value) {
        valueSet.add(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof AnnotationFilterCriteria)) {
            return false;
        } else {
            AnnotationFilterCriteria other = (AnnotationFilterCriteria) obj;
            return ((isInclude() == other.isInclude()) &&
                    BELUtilities.equals(annotationType, other.annotationType) && valueSet
                        .equals(other.valueSet));
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 0;
        hash += (isInclude() ? 1 : 0);
        hash *= prime;
        hash += (annotationType != null ? annotationType.hashCode() : 0);
        hash *= prime;
        hash += valueSet.hashCode();
        return hash;
    }
}
