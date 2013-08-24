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
package org.openbel.framework.core.df.beldata.namespace;

import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.core.df.beldata.ConvertUtil.typeConvert;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;

/**
 * NamespaceBlock encapsulates the {@code [Namespace]} block of a BEL namespace
 * document.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NamespaceBlock implements Serializable {
    public static final String BLOCK_NAME = "Namespace";

    private static final String PROPERTY_KEYWORD = "Keyword";
    private static final String PROPERTY_NAME = "NameString";
    private static final String PROPERTY_DESCRIPTION = "DescriptionString";
    private static final String PROPERTY_VERSION = "VersionString";
    private static final String PROPERTY_DOMAIN = "DomainString";
    private static final String PROPERTY_SPECIES = "SpeciesString";
    private static final String PROPERTY_CREATED_DATE_TIME = "CreatedDateTime";
    private static final String PROPERTY_QUERY_VALUE_URL = "QueryValueURL";
    private static final long serialVersionUID = -2765703816205854262L;

    private final String keyword;
    private final String nameString;
    private final String domainString;
    private final String speciesString;
    private final String descriptionString;
    private final String versionString;
    private final Date createdDateTime;
    private final URL queryValueURL;

    /**
     * Create the {@link NamespaceBlock} with the required fields.
     *
     * @param keyword the {@link String keyword property} value
     * @param nameString the {@link String name property} value
     * @param domainString the {@link String domain property} value
     * @param createdDateTime the {@link String created date time property}
     * value
     */
    public NamespaceBlock(final String keyword, final String nameString,
            final String domainString, final Date createdDateTime) {
        if (createdDateTime == null
                || noLength(keyword, nameString, domainString)) {
            throw new InvalidArgument("blank argument(s) to namespace block.");
        }

        this.keyword = keyword;
        this.nameString = nameString;
        this.domainString = domainString;
        this.createdDateTime = createdDateTime;
        this.descriptionString = null;
        this.speciesString = null;
        this.versionString = null;
        this.queryValueURL = null;
    }

    /**
     * Create the {@link NamespaceBlock} with the required and optional fields.
     *
     * @param keyword the {@link String keyword property} value
     * @param nameString the {@link String name property} value
     * @param domainString the {@link String domain property} value
     * @param createdDateTime the {@link String created date time property}
     * value
     * @param descriptionString the {@link String description property} value
     * @param speciesString the {@link String species property} value
     * @param versionString the {@link String version property} value
     * @param queryValueURL the {@link String query value url property} value
     */
    public NamespaceBlock(final String keyword, final String nameString,
            final String domainString, final Date createdDateTime,
            final String descriptionString, final String speciesString,
            final String versionString, final URL queryValueURL) {
        if (createdDateTime == null
                || noLength(keyword, nameString, domainString)) {
            throw new InvalidArgument("blank argument(s) to namespace block.");
        }

        this.keyword = keyword;
        this.nameString = nameString;
        this.domainString = domainString;
        this.createdDateTime = createdDateTime;
        this.descriptionString = descriptionString;
        this.speciesString = speciesString;
        this.versionString = versionString;
        this.queryValueURL = queryValueURL;
    }

    /**
     * Retrieves the keyword; the namespace block's {@code Keyword}
     * entry.
     *
     * @return the keyword {@link String}, will not be null
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Retrieves the name string; the namespace block's {@code NameString}
     * entry.
     *
     * @return the name {@link String string}, will not be null
     */
    public String getNameString() {
        return nameString;
    }

    /**
     * Retrieves the domain string; the namespace block's {@code DomainString}
     * entry.
     *
     * @return the domain {@link String}, will not be null
     */
    public String getDomainString() {
        return domainString;
    }

    /**
     * Retrieves the species string; the namespace block's {@code SpeciesString}
     * entry.
     *
     * @return the species {@link String string}, may be null
     */
    public String getSpeciesString() {
        return speciesString;
    }

    /**
     * Retrieves the description string; the namespace block's
     * {@code DescriptionString} entry.
     *
     * @return the description {@link String string}, may be null
     */
    public String getDescriptionString() {
        return descriptionString;
    }

    /**
     * Retrieves the version string; the namespace block's {@code VersionString}
     * entry.
     *
     * @return the version {@link String string}, may be null
     */
    public String getVersionString() {
        return versionString;
    }

    /**
     * Retrieves the created date time; the namespace block's
     * {@code CreatedDateTime} entry.
     *
     * @return the created {@link Date date-time}, will not be null
     */
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * Retrieves the optional {@link URL query value} for the namespace; the
     * namespace block's {@code QueryValueURL} entry.
     *
     * @return the optional {@link URL query value}, may be null
     */
    public URL getQueryValueURL() {
        return queryValueURL;
    }

    /**
     * Creates a new instance of {@link NamespaceBlock} using
     * {@link Properties block properties}.
     *
     * @param resourceLocation the {@link String resource location} of the
     * namespace used for exception reporting; which cannot be {@code null}
     * @param blockProperties the {@link Properties block properties} to
     * populate the {@link NamespaceBlock namespace block} with, which
     * cannot be {@code null}
     * @return a new {@link NamespaceBlock namespace block} instance, which
     * will not be {@code null}
     * @throws BELDataMissingPropertyException Thrown if a property was
     * required but not provided
     * @throws BELDataConversionException Thrown if a property value could not
     * be converted to the right type
     * @throws InvalidArgument Thrown if {@code ResourceLocation} or
     * {@code blockProperties} is {@code null}
     */
    public static NamespaceBlock create(final String resourceLocation,
            final Properties blockProperties)
            throws BELDataMissingPropertyException, BELDataConversionException {

        if (resourceLocation == null) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }
        if (blockProperties == null) {
            throw new InvalidArgument("blockProperties", blockProperties);
        }

        // handle blank keyword
        String keyword = blockProperties.getProperty(PROPERTY_KEYWORD);
        if (StringUtils.isBlank(keyword)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, PROPERTY_KEYWORD);
        }

        // handle blank name
        String name = blockProperties.getProperty(PROPERTY_NAME);
        if (StringUtils.isBlank(name)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, PROPERTY_NAME);
        }

        // handle blank domain
        String domain = blockProperties.getProperty(PROPERTY_DOMAIN);
        if (StringUtils.isBlank(domain)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, PROPERTY_DOMAIN);
        }

        // handle blank created date time
        String createdDateTime =
                blockProperties.getProperty(PROPERTY_CREATED_DATE_TIME);
        if (StringUtils.isBlank(createdDateTime)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, PROPERTY_CREATED_DATE_TIME);
        }

        // retrieve optional properties
        String species = blockProperties.getProperty(PROPERTY_SPECIES);
        String description = blockProperties.getProperty(PROPERTY_DESCRIPTION);
        String version = blockProperties.getProperty(PROPERTY_VERSION);
        String queryValueURL =
                blockProperties.getProperty(PROPERTY_QUERY_VALUE_URL);

        // convert to proper type
        return new NamespaceBlock(
                typeConvert(resourceLocation, PROPERTY_KEYWORD, keyword,
                        String.class),
                typeConvert(resourceLocation, PROPERTY_NAME, name, String.class),
                typeConvert(resourceLocation, PROPERTY_DOMAIN, domain,
                        String.class),
                typeConvert(resourceLocation, PROPERTY_CREATED_DATE_TIME,
                        createdDateTime, Date.class),
                typeConvert(resourceLocation, PROPERTY_DESCRIPTION,
                        description, String.class),
                typeConvert(resourceLocation, PROPERTY_SPECIES, species,
                        String.class),
                typeConvert(resourceLocation, PROPERTY_VERSION, version,
                        String.class),
                typeConvert(resourceLocation, PROPERTY_QUERY_VALUE_URL,
                        queryValueURL, URL.class));
    }
}
