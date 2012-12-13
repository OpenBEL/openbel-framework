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
package org.openbel.framework.common.model;

import static org.openbel.framework.common.BELUtilities.quoteParameter;

/**
 * BEL term function parameter.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Parameter implements BELObject {
    private static final long serialVersionUID = -4017778627771812562L;

    private Namespace namespace;
    private String value;

    /**
     * Creates a parameter with the optional properties.
     * 
     * @param ns Namespace
     * @param value Value
     */
    public Parameter(Namespace ns, String value) {
        this.namespace = ns;
        this.value = value;
    }

    /**
     * Creates a parameter.
     */
    public Parameter() {
    }

    /**
     * Returns the parameter's namespace.
     * 
     * @return Namespace, which may be null
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * Sets the parameter's namespace.
     * 
     * @param namespace Parameter's namespace
     */
    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    /**
     * Returns the parameter's value.
     * 
     * @return String, which may be null
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the parameter's value.
     * 
     * @param value Parameter's value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Parameter [");

        if (namespace != null) {
            builder.append("namespace=");
            builder.append(namespace);
            builder.append(", ");
        }

        if (value != null) {
            builder.append("value=");
            builder.append(value);
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result *= prime;
        if (namespace != null) result += namespace.hashCode();

        result *= prime;
        if (value != null) result += value.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;

        final Parameter av = (Parameter) o;

        if (namespace == null) {
            if (av.namespace != null) return false;
        } else if (!namespace.equals(av.namespace)) return false;

        if (value == null) {
            if (av.value != null) return false;
        } else if (!value.equals(av.value)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toBELLongForm() {
        StringBuilder sb = new StringBuilder();
        sb.append(namespace != null ? (namespace.toBELLongForm()) : "");
        sb.append(quoteParameter(value));
        return sb.toString();
    }

    /**
     * {@inheritDoc}<br>
     * Same as {@link #toBELLongForm()}
     */
    @Override
    public String toBELShortForm() {
        return toBELLongForm();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter clone() {
        return CommonModelFactory.getInstance().createParameter(namespace,
                value);
    }
}
