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
