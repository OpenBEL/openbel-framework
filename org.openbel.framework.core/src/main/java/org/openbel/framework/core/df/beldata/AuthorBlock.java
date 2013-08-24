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
package org.openbel.framework.core.df.beldata;

import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.core.df.beldata.ConvertUtil.typeConvert;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;

/**
 * AuthorBlock encapsulates the '[Author]' block of a BEL namespace document.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class AuthorBlock implements Serializable {
    private static final long serialVersionUID = -3033032711287479979L;

    /**
     * Defines the text within the author block.
     */
    public static final String BLOCK_NAME = "Author";

    /**
     * Defines the value holder for the author block's 'NameString' property.
     */
    private String nameString;

    /**
     * Defines the value holder for the author block's 'CopyrightString' property.
     */
    private String copyrightString;

    /**
     * Defines the value holder for the author block's 'ContactInfoString' property.
     */
    private String contactInfoString;

    public AuthorBlock(final String nameString) {
        if (noLength(nameString)) {
            throw new InvalidArgument("nameString", nameString);
        }
        this.nameString = nameString;
        this.copyrightString = null;
        this.contactInfoString = null;
    }

    public AuthorBlock(final String nameString, String copyrightString,
            String contactInfoString) {
        if (noLength(nameString)) {
            throw new InvalidArgument("nameString", nameString);
        }
        this.nameString = nameString;
        this.copyrightString = copyrightString;
        this.contactInfoString = contactInfoString;
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
     * Retrieves the copyright string.
     *
     * @return {@link String}, the copyright string, may be null
     */
    public String getCopyrightString() {
        return copyrightString;
    }

    /**
     * Retrieves the contact info string.
     *
     * @return {@link String}, the contact info string, may be null
     */
    public String getContactInfoString() {
        return contactInfoString;
    }

    public static AuthorBlock create(final String resourceLocation,
            final Properties blockProperties)
            throws BELDataMissingPropertyException, BELDataConversionException {
        String name = null;
        String copyright = null;
        String contactInfo = null;

        name = blockProperties.getProperty("NameString");
        if (StringUtils.isBlank(name)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "NameString");
        }

        copyright = blockProperties.getProperty("CopyrightString");
        if (StringUtils.isBlank(copyright)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "CopyrightString");
        }

        contactInfo = blockProperties.getProperty("ContactInfoString");
        if (StringUtils.isBlank(contactInfo)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "ContactInfoString");
        }

        return new AuthorBlock(typeConvert(resourceLocation, "NameString",
                name,
                String.class),
                typeConvert(resourceLocation, "CopyrightString", copyright,
                        String.class),
                typeConvert(resourceLocation, "ContactInfoString", contactInfo,
                        String.class));
    }
}
