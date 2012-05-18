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

/**
 * EquivalenceDataIndex represents a {@link DataFileIndex} that is bound to a
 * source and target namespace.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class EquivalenceDataIndex {

    private String namespaceResourceLocation;

    private DataFileIndex equivalenceIndex;

    public EquivalenceDataIndex(String namespaceResourceLocation,
            DataFileIndex equivalenceIndex) {
        super();
        this.namespaceResourceLocation = namespaceResourceLocation;
        this.equivalenceIndex = equivalenceIndex;
    }

    public String getNamespaceResourceLocation() {
        return namespaceResourceLocation;
    }

    public void setNamespaceResourceLocation(String namespaceResourceLocation) {
        this.namespaceResourceLocation = namespaceResourceLocation;
    }

    public DataFileIndex getEquivalenceIndex() {
        return equivalenceIndex;
    }

    public void setEquivalenceIndex(DataFileIndex equivalenceIndex) {
        this.equivalenceIndex = equivalenceIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime
                        * result
                        + ((equivalenceIndex == null) ? 0 : equivalenceIndex
                                .hashCode());
        result = prime
                * result
                + ((namespaceResourceLocation == null) ? 0
                        : namespaceResourceLocation.hashCode());
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
        EquivalenceDataIndex other = (EquivalenceDataIndex) obj;
        if (equivalenceIndex == null) {
            if (other.equivalenceIndex != null)
                return false;
        } else if (!equivalenceIndex.equals(other.equivalenceIndex))
            return false;
        if (namespaceResourceLocation == null) {
            if (other.namespaceResourceLocation != null)
                return false;
        } else if (!namespaceResourceLocation
                .equals(other.namespaceResourceLocation))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder bldr = new StringBuilder();
        bldr.append("Equivalence for ");

        if (namespaceResourceLocation != null) {
            int i = namespaceResourceLocation.lastIndexOf('/');
            if (i == -1) {
                bldr.append(namespaceResourceLocation);
            } else {
                bldr.append(namespaceResourceLocation.substring(i + 1));
            }
        }

        return bldr.toString();
    }
}
