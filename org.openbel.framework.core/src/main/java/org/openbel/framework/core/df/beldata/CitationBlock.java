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
package org.openbel.framework.core.df.beldata;

import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.core.df.beldata.ConvertUtil.typeConvert;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;

/**
 * CitationBlock encapsulates the '[Citation]' block of a BEL namespace
 * document.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class CitationBlock implements Serializable {
    private static final long serialVersionUID = -917948299701772907L;
    private String nameString;
    private String descriptionString;
    private String publishedVersionString;
    private Date publishedDate;
    private URL referenceURL;

    /**
     * Defines the text within the citation block: {@value}
     */
    public static final String BLOCK_NAME = "Citation";

    public CitationBlock(final String nameString) {
        if (noLength(nameString)) {
            throw new InvalidArgument("nameString", nameString);
        }

        this.nameString = nameString;
        this.descriptionString = null;
        this.publishedVersionString = null;
        this.publishedDate = null;
        this.referenceURL = null;

    }

    public CitationBlock(final String nameString,
            final String descriptionString,
            final String publishedVersionString,
            final Date publishedDate, final URL referenceURL) {
        if (noLength(nameString)) {
            throw new InvalidArgument("nameString", nameString);
        }

        this.nameString = nameString;
        this.descriptionString = descriptionString;
        this.publishedVersionString = publishedVersionString;
        this.publishedDate = publishedDate;
        this.referenceURL = referenceURL;

    }

    /**
     * Retrieves the name string; the citation block's {@code NameString} entry.
     * 
     * @return the name {@link String string}; will not be null
     */
    public String getNameString() {
        return nameString;
    }

    /**
     * Retrieves the description string; the citation block's
     * {@code DescriptionString} entry.
     * 
     * @return the description {@link String string}; may be null
     */
    public String getDescriptionString() {
        return descriptionString;
    }

    /**
     * Retrieves the published version string; the citation block's
     * {@code PublishedVersionString} entry.
     * 
     * @return the published version {@link String string}; may be null
     */
    public String getPublishedVersionString() {
        return publishedVersionString;
    }

    /**
     * Retrieves the published date; the citation block's {@code PublishedDate}
     * entry.
     * 
     * @return the published {@link Dated date}; may be null
     */
    public Date getPublishedDate() {
        return publishedDate;
    }

    /**
     * Retrieves the reference URL; the citation block's {@code ReferenceURL}
     * entry.
     * 
     * @return the reference {@link URL URL}; may be null
     */
    public URL getReferenceURL() {
        return referenceURL;
    }

    public static CitationBlock create(final String resourceLocation,
            final Properties blockProperties)
            throws BELDataMissingPropertyException, BELDataConversionException {
        String name = null;
        String description = null;
        String publishedVersionString = null;
        String publishedDate = null;
        String referenceURL = null;

        name = blockProperties.getProperty("NameString");
        if (StringUtils.isBlank(name)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "NameString");
        }

        description = blockProperties.getProperty("DescriptionString");
        publishedVersionString =
                blockProperties.getProperty("PublishedVersionString");
        publishedDate = blockProperties.getProperty("PublishedDate");
        referenceURL = blockProperties.getProperty("ReferenceURL");

        return new CitationBlock(typeConvert(resourceLocation, "NameString",
                name,
                String.class),
                typeConvert(resourceLocation, "DescriptionString", description,
                        String.class),
                typeConvert(resourceLocation, "PublishedVersionString",
                        publishedVersionString,
                        String.class),
                typeConvert(resourceLocation, "PublishedDate", publishedDate,
                        Date.class),
                typeConvert(resourceLocation, "ReferenceURL", referenceURL,
                        URL.class));
    }
}
