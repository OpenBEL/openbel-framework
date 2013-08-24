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
