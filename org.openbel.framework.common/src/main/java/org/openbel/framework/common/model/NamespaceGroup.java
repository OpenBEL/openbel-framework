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

import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.model.Namespace.DEFAULT_NAMESPACE_PREFIX;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains any number of namespaces.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NamespaceGroup implements BELModelObject {
    private static final long serialVersionUID = 1141228737427133678L;

    private String defaultResourceLocation;
    private List<Namespace> namespaces;

    /**
     * Creates a namespace group with the optional properties.
     *
     * @param location
     * @param namespaces
     */
    public NamespaceGroup(String location, List<Namespace> namespaces) {
        this.defaultResourceLocation = location;
        this.namespaces = namespaces;
    }

    /**
     * Creates a namespace group.
     */
    public NamespaceGroup() {
    }

    /**
     * Returns the default resource location of the default namespace used by
     * the document.
     *
     * @return String, which may be null
     */
    public String getDefaultResourceLocation() {
        return defaultResourceLocation;
    }

    /**
     * Sets the default resource location of the default namespace used by the
     * document.
     *
     * @param s Default resource location
     */
    public void setDefaultResourceLocation(final String s) {
        this.defaultResourceLocation = s;
    }

    /**
     * Returns the group's namespaces.
     * <p>
     * The namespace representing the {@link #getDefaultResourceLocation()
     * default resource location} is not included, if present.
     * </p>
     *
     * @return {@link List} of {@link Namespace namespaces}, which may be null
     * @see #getAllNamespaces()
     */
    public List<Namespace> getNamespaces() {
        return namespaces;
    }

    /**
     * Returns all the group's namespaces.
     * <p>
     * This is equivalent to {@link #getNamespaces()} with the addition of the
     * namespace representing the {@link #getDefaultResourceLocation() default
     * resource location}, if present.
     * </p>
     *
     * @return {@link List} of {@link Namespace namespaces}; which may be empty
     * @see #getNamespaces()
     */
    public List<Namespace> getAllNamespaces() {
        List<Namespace> ret = new ArrayList<Namespace>();
        if (namespaces != null) {
            ret.addAll(namespaces);
        }
        if (defaultResourceLocation != null) {
            String prefix = DEFAULT_NAMESPACE_PREFIX;
            String drl = defaultResourceLocation;
            final Namespace defaultNS = new Namespace(prefix, drl);
            ret.add(defaultNS);
        }
        return ret;
    }

    /**
     * Sets the group's namespaces.
     *
     * @param namespaces List of namespaces
     */
    public void setNamespaces(List<Namespace> namespaces) {
        this.namespaces = namespaces;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("NamespaceGroup [");

        if (defaultResourceLocation != null) {
            builder.append("defaultResourceLocation=");
            builder.append(defaultResourceLocation);
            builder.append(", ");
        }

        if (namespaces != null) {
            builder.append("namespaces=");
            builder.append(namespaces);
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
        if (defaultResourceLocation != null)
            result += defaultResourceLocation.hashCode();

        result *= prime;
        if (namespaces != null) result += namespaces.hashCode();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamespaceGroup)) return false;

        final NamespaceGroup ng = (NamespaceGroup) o;

        if (defaultResourceLocation == null) {
            if (ng.defaultResourceLocation != null) return false;
        } else if (!defaultResourceLocation.equals(ng.defaultResourceLocation))
            return false;

        if (namespaces == null) {
            if (ng.namespaces != null) return false;
        } else if (!namespaces.equals(ng.namespaces)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamespaceGroup clone() {
        List<Namespace> namespaces2 = null;
        if (namespaces != null) {
            namespaces2 = sizedArrayList(namespaces.size());
            for (final Namespace ns : namespaces)
                namespaces2.add(ns.clone());
        }
        return new NamespaceGroup(defaultResourceLocation, namespaces2);
    }
}
