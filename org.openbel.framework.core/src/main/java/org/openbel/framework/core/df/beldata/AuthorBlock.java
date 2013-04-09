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
