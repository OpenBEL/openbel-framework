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
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;

/**
 * EquivalenceBlock encapsulates the '[Equivalence]' block of a BEL equivalence
 * document.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class EquivalenceBlock implements Serializable {
    private static final long serialVersionUID = -8889115453159088323L;

    public static final String BLOCK_NAME = "Equivalence";

    /**
     * Defines the value holder for the equivalence block's 'NameString'
     * property.
     */
    private String nameString;

    /**
     * Defines the value holder for the equivalence block's 'DescriptionString'
     * property.
     */
    private String descriptionString;

    /**
     * Defines the value holder for the equivalence block's 'VersionString'
     * property.
     */
    private String versionString;

    /**
     * Defines the value holder for the equivalence block's 'CreatedDateTime'
     * property.
     */
    private Date createdDateTime;

    public EquivalenceBlock(final String nameString, final Date createdDateTime) {
        if (noLength(nameString)) {
            throw new InvalidArgument("nameString", nameString);
        }

        if (createdDateTime == null) {
            throw new InvalidArgument("createdDateTime", createdDateTime);
        }

        this.nameString = nameString;
        this.createdDateTime = createdDateTime;
        this.descriptionString = null;
        this.versionString = null;
    }

    public EquivalenceBlock(final String nameString,
            final Date createdDateTime,
            final String descriptionString, String versionString) {
        if (noLength(nameString)) {
            throw new InvalidArgument("nameString", nameString);
        }

        if (createdDateTime == null) {
            throw new InvalidArgument("createdDateTime", createdDateTime);
        }

        this.nameString = nameString;
        this.createdDateTime = createdDateTime;
        this.descriptionString = descriptionString;
        this.versionString = versionString;
    }

    /**
     * Retrieves the name string.
     *
     * @return {@link String}, the name string, will not be null
     */
    public String getNameString() {
        return nameString;
    }

    /**
     * Retrieves the description string.
     *
     * @return {@link String}, the description string, may be null
     */
    public String getDescriptionString() {
        return descriptionString;
    }

    /**
     * Retrieves the version string.
     *
     * @return {@link String}, the version string, may be null
     */
    public String getVersionString() {
        return versionString;
    }

    /**
     * Retrieves the created date time.
     *
     * @return {@link Date}, the created date time, will not be null
     */
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public static EquivalenceBlock create(final String resourceLocation,
            final Properties blockProperties)
            throws BELDataMissingPropertyException, BELDataConversionException {
        String name = null;
        String createdDateTime = null;
        String description = null;
        String version = null;

        name = blockProperties.getProperty("NameString");
        if (StringUtils.isBlank(name)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "NameString");
        }

        createdDateTime = blockProperties.getProperty("CreatedDateTime");
        if (StringUtils.isBlank(createdDateTime)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "CreatedDateTime");
        }

        description = blockProperties.getProperty("DescriptionString");
        if (StringUtils.isBlank(description)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "DescriptionString");
        }

        version = blockProperties.getProperty("VersionString");
        if (StringUtils.isBlank(version)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "VersionString");
        }

        return new EquivalenceBlock(typeConvert(resourceLocation, "NameString",
                name,
                String.class),
                typeConvert(resourceLocation, "CreatedDateTime",
                        createdDateTime,
                        Date.class),
                typeConvert(resourceLocation, "DescriptionString", description,
                        String.class),
                typeConvert(resourceLocation, "VersionString", version,
                        String.class));
    }
}
