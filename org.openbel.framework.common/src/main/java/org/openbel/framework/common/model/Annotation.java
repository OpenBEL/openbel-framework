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

import org.openbel.framework.common.InvalidArgument;

/**
 * An annotation applied to a BEL statement, referencing an annotation
 * definition.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Annotation implements BELModelObject {
    private static final long serialVersionUID = -3363916022157211893L;

    private final String value;
    private AnnotationDefinition definition;

    /**
     * Creates an annotation with the required value property.
     *
     * @param value Value
     * @throws InvalidArgument Thrown if {@code value} is null
     */
    public Annotation(final String value) {
        if (value == null) throw new InvalidArgument("null value");
        this.value = value;
    }

    /**
     * Creates an annotation with the required value and optional properties.
     *
     * @param value Value
     * @param ad Annotation definition
     * @throws InvalidArgument Thrown if {@code value} is null
     */
    public Annotation(final String value, AnnotationDefinition ad) {
        if (value == null) throw new InvalidArgument("null value");
        this.value = value;
        this.definition = ad;
    }

    /**
     * Returns the annotation's annotation definition.
     *
     * @return Annotation definition, which may be null
     */
    public AnnotationDefinition getDefinition() {
        return definition;
    }

    /**
     * Sets the annotation's annotation definition.
     *
     * @param definition Annotation definition
     */
    public void setDefinition(AnnotationDefinition definition) {
        this.definition = definition;
    }

    /**
     * Returns the annotation's value.
     *
     * @return Non-null string
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("Annotation [value=");
        // value is non-null by contract
        builder.append(value);

        if (definition != null) {
            builder.append(", definition=");
            builder.append(definition);
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
        if (definition != null) result += definition.hashCode();

        result *= prime;
        // value is non-null by contract
        result += value.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation)) return false;

        final Annotation a = (Annotation) o;

        if (definition == null) {
            if (a.definition != null) return false;
        } else if (!definition.equals(a.definition)) return false;

        // value is non-null by contract
        if (!value.equals(a.value)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Annotation clone() {
        Annotation ret = CommonModelFactory.getInstance()
                .createAnnotation(value);
        if (definition != null) {
            ret.definition = definition.clone();
        }
        return ret;
    }
}
