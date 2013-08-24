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
