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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BEL term function parameter.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class Parameter implements BELObject {
    private static final long serialVersionUID = -4017778627771812562L;

    /**
     * Creates a static regex {@link Pattern} to match a non-word character or
     * an underscore.
     */
    private static Pattern NON_WORD_CHAR_PATTERN = Pattern.compile("[\\W_]");

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

        Matcher m = NON_WORD_CHAR_PATTERN.matcher(value);
        if (m.find() && (!value.startsWith("\"") && !value.endsWith("\""))) {
            sb.append("\"").append(value).append("\"");
        } else {
            sb.append(value);
        }

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
