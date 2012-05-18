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
