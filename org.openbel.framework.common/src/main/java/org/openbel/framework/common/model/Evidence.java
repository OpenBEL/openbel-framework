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
package org.openbel.framework.common.model;

/**
 * Evidence in support of a statement.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Evidence implements BELModelObject {
    private static final long serialVersionUID = 5100155904137028490L;

    private String value;

    /**
     * Creates evidence with the optional value property.
     *
     * @param value Value
     */
    public Evidence(String value) {
        this.value = value;
    }

    /**
     * Creates evidence.
     */
    public Evidence() {
    }

    /**
     * Returns the evidence's value.
     *
     * @return String, which may be null
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the evidence's value.
     *
     * @param value Evidence's value
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
        builder.append("Evidence [");

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
        if (value != null) result += value.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evidence)) return false;

        final Evidence e = (Evidence) o;

        if (value == null) {
            if (e.value != null) return false;
        } else if (!value.equals(e.value)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Evidence clone() {
        return CommonModelFactory.getInstance().createEvidence(value);
    }

}
