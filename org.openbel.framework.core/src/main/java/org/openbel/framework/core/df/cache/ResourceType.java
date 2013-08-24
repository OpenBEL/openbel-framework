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
