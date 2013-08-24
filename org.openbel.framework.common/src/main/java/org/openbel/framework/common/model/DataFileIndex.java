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

import java.io.File;

/**
 * DataFileIndex represents an index BEL data file that allows efficient,
 * lookup of key/value pairs stored on disk.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DataFileIndex {
    private static final String DBF_DB_EXT = ".dbf.0";
    private static final String DBF_LOG_EXT = ".dbf.t";
    private static final String DBR_DB_EXT = ".dbr.0";
    private static final String DBR_LOG_EXT = ".dbr.t";
    private static final String IDF_DB_EXT = ".idf.0";
    private static final String IDF_LOG_EXT = ".idf.t";
    private static final String IDR_DB_EXT = ".idr.0";
    private static final String IDR_LOG_EXT = ".idr.t";
    private String resourceLocation;
    private String indexPath;
    private File headerFile;

    public DataFileIndex(String resourceLocation, String indexPath,
            File headerFile) {
        this.resourceLocation = resourceLocation;
        this.indexPath = indexPath;
        this.headerFile = headerFile;
    }

    public String getNamespaceResourceLocation() {
        return resourceLocation;
    }

    public void setNamespaceResourceLocation(String namespaceResourceLocation) {
        this.resourceLocation = namespaceResourceLocation;
    }

    public String getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

    public File[] getIndexFiles() {
        if (indexPath == null) {
            throw new IllegalStateException(
                    "Namespace index database file cannot be determined using a null namespace index path.");
        }

        File[] files = new File[] {
                new File(indexPath + DBF_DB_EXT),
                new File(indexPath + DBF_LOG_EXT),
                new File(indexPath + DBR_DB_EXT),
                new File(indexPath + DBR_LOG_EXT),
                new File(indexPath + IDF_DB_EXT),
                new File(indexPath + IDF_LOG_EXT),
                new File(indexPath + IDR_DB_EXT),
                new File(indexPath + IDR_LOG_EXT)
        };

        return files;
    }

    public File getHeaderFile() {
        return headerFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime * result
                        + ((indexPath == null) ? 0 : indexPath.hashCode());
        result =
                prime
                        * result
                        + ((resourceLocation == null) ? 0 : resourceLocation
                                .hashCode());
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
        DataFileIndex other = (DataFileIndex) obj;
        if (indexPath == null) {
            if (other.indexPath != null)
                return false;
        } else if (!indexPath.equals(other.indexPath))
            return false;
        if (resourceLocation == null) {
            if (other.resourceLocation != null)
                return false;
        } else if (!resourceLocation.equals(other.resourceLocation))
            return false;
        return true;
    }
}
