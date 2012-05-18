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
