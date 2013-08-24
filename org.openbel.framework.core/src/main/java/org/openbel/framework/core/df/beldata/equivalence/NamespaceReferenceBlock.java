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
package org.openbel.framework.core.df.beldata.equivalence;

import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.core.df.beldata.ConvertUtil.typeConvert;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;

/**
 * CitationBlock encapsulates the '[NamespaceReference]' block of a BEL
 * equivalence document.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NamespaceReferenceBlock implements Serializable {
    private static final long serialVersionUID = -3396692450474433684L;

    /**
     * Defines the text within the namespace reference block.
     */
    public static final String BLOCK_NAME = "NamespaceReference";

    /**
     * Defines the value holder for the namespace reference block's 'NameString'
     * property.
     */
    private String nameString;

    /**
     * Defines the value holder for the namespace reference block's
     * 'PublishedVersionString' property.
     */
    private String versionString;

    public NamespaceReferenceBlock(final String nameString) {
        if (noLength(nameString)) {
            throw new InvalidArgument("nameString", nameString);
        }

        this.nameString = nameString;
        this.versionString = null;
    }

    public NamespaceReferenceBlock(final String nameString,
            final String versionString) {
        if (noLength(nameString)) {
            throw new InvalidArgument("nameString", nameString);
        }

        this.nameString = nameString;
        this.versionString = versionString;
    }

    /**
     * Retrieves the name string.
     *
     * @return {@link String}, the name string
     */
    public String getNameString() {
        return nameString;
    }

    /**
     * Sets the name string.
     *
     * @param nameString {@link String}, the name string
     */
    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    /**
     * Retrieves the version string.
     *
     * @return {@link String}, the version string
     */
    public String getVersionString() {
        return versionString;
    }

    /**
     * Sets the version string.
     *
     * @param versionString {@link String}, the version string
     */

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    public static NamespaceReferenceBlock create(final String resourceLocation,
            final Properties blockProperties)
            throws BELDataMissingPropertyException, BELDataConversionException {
        String name = null;
        String version = null;

        name = blockProperties.getProperty("NameString");
        if (StringUtils.isBlank(name)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "NameString");
        }

        version = blockProperties.getProperty("VersionString");
        if (StringUtils.isBlank(version)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "VersionString");
        }

        return new NamespaceReferenceBlock(typeConvert(resourceLocation,
                "NameString", name, String.class), typeConvert(
                resourceLocation, "VersionString", version, String.class));
    }
}
