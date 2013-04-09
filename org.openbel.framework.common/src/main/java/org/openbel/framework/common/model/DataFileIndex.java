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
