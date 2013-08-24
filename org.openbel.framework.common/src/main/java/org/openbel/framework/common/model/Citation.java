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

import java.util.Calendar;
import java.util.List;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.CitationType;

/**
 * A citation indicates some knowledge source is cited to support a statement.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 * @author James McMahon
 */
public class Citation implements BELModelObject {
    private static final long serialVersionUID = -3979842128328834892L;

    // Immutable name to ensure that is set at initializing.
    // A citation always has a name, so it is declared final and
    // required during construction.
    private final String name;
    private String reference;
    private List<String> authors;
    private String comment;
    private Calendar date;
    private CitationType type;

    /**
     * Creates a citation with the required name property.
     *
     * @param name Name
     * @throws InvalidArgument Thrown if {@code name} is null
     */
    public Citation(final String name) {
        if (name == null) throw new InvalidArgument("null name");
        this.name = name;
    }

    /**
     * Creates a citation with the required name and optional properties.
     *
     * @param name Name
     * @param reference Reference
     * @param comment Comment
     * @param date Date
     * @param authors List of authors
     * @param t Type of citation
     * @throws InvalidArgument Thrown if {@code name} is null
     */
    public Citation(final String name, String reference, String comment,
            Calendar date, List<String> authors, CitationType t) {
        if (name == null) throw new InvalidArgument("null name");
        this.name = name;
        this.reference = reference;
        this.comment = comment;
        this.date = date;
        this.authors = authors;
        this.type = t;
    }

    /**
     * Returns the citation's identifier.
     *
     * @return String, which may be null
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the citation's identifier.
     *
     * @param id Citation's identifier
     */
    public void setReference(String id) {
        this.reference = id;
    }

    /**
     * Returns the citation's comment.
     *
     * @return String, which may be null
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the citation's comment.
     *
     * @param comment Citation's comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the citation's date.
     *
     * @return Calendar, which may be null
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Sets the citation's date.
     *
     * @param date Citation's date
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     * Get the citation's authors.
     *
     * @return authors, which may be null
     */
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * Set the citation's authors.
     *
     * @param authors List of strings
     */
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    /**
     * Returns the citation's type.
     *
     * @return CitationType, which may be null
     */
    public CitationType getType() {
        return type;
    }

    /**
     * Sets the citation's type.
     *
     * @param type Citation's type
     */
    public void setType(CitationType type) {
        this.type = type;
    }

    /**
     * Returns the citation's name.
     *
     * @return Non-null string
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Citation [");

        // name is non-null by contract
        builder.append("name=");
        builder.append(name);
        builder.append(", ");

        if (reference != null) {
            builder.append("reference=");
            builder.append(reference);
            builder.append(", ");
        }

        if (comment != null) {
            builder.append("comment=");
            builder.append(comment);
            builder.append(", ");
        }

        if (date != null) {
            builder.append("date=");
            builder.append(date);
            builder.append(", ");
        }

        if (authors != null) {
            builder.append("authors=");
            builder.append(authors);
            builder.append(", ");
        }

        if (type != null) {
            builder.append("type=");
            builder.append(type);
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
        if (date != null) result += date.hashCode();

        result *= prime;
        if (comment != null) result += comment.hashCode();

        result *= prime;
        if (reference != null) result += reference.hashCode();

        result *= prime;
        // name is non-null by contract
        result += name.hashCode();

        result *= prime;
        if (type != null) result += type.hashCode();

        result *= prime;
        if (authors != null) result += authors.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Citation)) return false;

        final Citation c = (Citation) o;

        if (date == null) {
            if (c.date != null) return false;
        } else if (!date.equals(c.date)) return false;

        if (comment == null) {
            if (c.comment != null) return false;
        } else if (!comment.equals(c.comment)) return false;

        if (reference == null) {
            if (c.reference != null) return false;
        } else if (!reference.equals(c.reference)) return false;

        // name is non-null by contract
        if (!name.equals(c.name)) return false;

        if (type != c.type) return false;

        if (authors == null) {
            if (c.authors != null) return false;
        } else if (!authors.equals(c.authors)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Citation clone() {
        Calendar newdate = null;
        if (date != null) {
            newdate = (Calendar) date.clone();
        }

        return CommonModelFactory.getInstance().createCitation(name, reference,
                comment, newdate, authors, type);
    }
}
