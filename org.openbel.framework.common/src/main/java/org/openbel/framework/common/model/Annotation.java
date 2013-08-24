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
