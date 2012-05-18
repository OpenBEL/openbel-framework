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
