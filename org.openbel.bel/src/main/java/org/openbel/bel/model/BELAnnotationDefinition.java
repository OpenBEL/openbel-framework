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
package org.openbel.bel.model;

import java.util.List;

public class BELAnnotationDefinition extends BELObject {
    private static final long serialVersionUID = 8501531304605385496L;
    private final String name;
    private final BELAnnotationType annotationType;
    private final String value;
    private final List<String> list;

    public BELAnnotationDefinition(final String name,
            final BELAnnotationType annotationType, final String value) {
        if (name == null) {
            throw new IllegalArgumentException("name must be set.");
        }

        if (annotationType == null) {
            throw new IllegalArgumentException("annotion type must be set.");
        }

        if (value == null) {
            throw new IllegalArgumentException("value must be set.");
        }

        this.name = clean(name);
        this.annotationType = annotationType;

        if (value.startsWith("\"") && value.endsWith("\"")) {
            this.value = clean(value.substring(1, value.length() - 1));
        } else {
            this.value = clean(value);
        }

        this.list = null;
    }

    public BELAnnotationDefinition(final String name,
            final BELAnnotationType annotationType, final List<String> list) {
        if (name == null) {
            throw new IllegalArgumentException("name must be set.");
        }

        if (annotationType == null) {
            throw new IllegalArgumentException("annotion type must be set.");
        }

        if (list == null) {
            throw new IllegalArgumentException("list must be set.");
        }

        this.name = clean(name);
        this.annotationType = annotationType;

        for (int i = 0; i < list.size(); i++) {
            String v = list.get(i);

            if (v.startsWith("\"") && v.endsWith("\"")) {
                list.set(i, clean(v.substring(1, v.length() - 1)));
            }
        }

        this.list = list;
        this.value = null;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public BELAnnotationType getAnnotationType() {
        return annotationType;
    }

    public String getValue() {
        return value;
    }

    public List<String> getList() {
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((annotationType == null) ? 0 : annotationType.hashCode());
        result = prime * result + ((list == null) ? 0 : list.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BELAnnotationDefinition other = (BELAnnotationDefinition) obj;
        if (annotationType != other.annotationType)
            return false;
        if (list == null) {
            if (other.list != null)
                return false;
        } else if (!list.equals(other.list))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}
