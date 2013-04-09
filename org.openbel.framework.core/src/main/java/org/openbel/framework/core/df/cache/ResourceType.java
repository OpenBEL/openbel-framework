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
package org.openbel.framework.core.df.cache;

import static org.openbel.framework.common.PathConstants.*;

/**
 * ResourceType states the possible BELFramework cache sub-directories that
 * correspond to BELFramework resources.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public enum ResourceType {
    NAMESPACES("namespaces", NAMESPACE_EXTENSION),
    EQUIVALENCES("equivalences", EQUIVALENCE_EXTENSION),
    BEL("bel", BEL_SCRIPT_EXTENSION),
    XBEL("xbel", XBEL_EXTENSION),
    RESOURCE_INDEX("resource-index", XML_EXTENSION),
    ANNOTATIONS("annotations", ANNOTATION_EXTENSION),
    TMP_FILE("tmp", TMP_EXTENSION),
    GENERIC_XML("xml", XML_EXTENSION);

    /**
     * Defines the sub-directory resource folder name.
     */
    private String resourceFolderName;

    /**
     * Defines the file extension of the resources within the resource folder.
     */
    private String resourceExtension;

    /**
     * Constructs the ResourceType with the sub-directory resource folder name.
     *
     * @param resourceFolderName {@link String}, the sub-directory resource
     * folder name
     * @param resourceExtension {@link String}, the resource extension for
     * resources in the sub-directory
     */
    private ResourceType(String resourceFolderName, String resourceExtension) {
        this.resourceFolderName = resourceFolderName;
        this.resourceExtension = resourceExtension;
    }

    /**
     * Returns the sub-directory resource folder name.
     *
     * @return {@link String} the sub-directory resource folder name
     */
    public String getResourceFolderName() {
        return resourceFolderName;
    }

    /**
     * Returns the resource extension for resources in the sub-directory.
     *
     * @return {@link String}, the resource extension for resources in the
     * sub-directory
     */
    public String getResourceExtension() {
        return resourceExtension;
    }

    /**
     * Returns the {@link ResourceType} matching the folder name.
     *
     * @param resourceTypeFolder {@link String}, the resource folder
     * @return {@link ResourceType} the resource type for the folder name or
     * null if there was no match
     */
    public static ResourceType fromFolder(String resourceTypeFolder) {
        for (ResourceType type : values()) {
            if (type.resourceFolderName.equals(resourceTypeFolder)) {
                return type;
            }
        }

        return null;
    }

    /**
     * Returns the {@link ResourceType} inferred from the resource location.
     * <p>
     * The resource location must end with the {@link #resourceExtension} or the
     * {@link #resourceExtension} + {@value #COMPRESSED_EXTENSION}.
     * </p>
     *
     * @param resourceLocation {@link String}, the resource location
     * @return {@link ResourceType} the resource type inferred from the resource
     * location. A null will be returned if there was no match or if the
     * <tt>resourceLocation</tt> argument was null
     */
    public static ResourceType fromLocation(String resourceLocation) {
        if (resourceLocation == null) {
            return null;
        }

        for (ResourceType type : values()) {
            if (resourceLocation.endsWith(type.resourceExtension)) {
                return type;
            } else if (resourceLocation.endsWith(type.resourceExtension
                    + COMPRESSED_EXTENSION)) {
                return type;
            }
        }

        return null;
    }
}
