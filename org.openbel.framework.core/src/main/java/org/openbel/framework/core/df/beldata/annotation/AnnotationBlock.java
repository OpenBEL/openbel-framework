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
package org.openbel.framework.core.df.beldata.annotation;

import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.core.df.beldata.ConvertUtil.typeConvert;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.AnnotationType;
import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataInvalidPropertyException;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;

public class AnnotationBlock implements Serializable {
    private static final long serialVersionUID = 5230655302222434095L;
    public static final String BLOCK_NAME = "AnnotationDefinition";
    public static final String LIST_TYPE = "list";
    public static final String PATTERN_TYPE = "pattern";
    private static final String[] TYPE_STRING_VALUES = new String[] {
            LIST_TYPE,
            PATTERN_TYPE
    };

    private final String keyword;
    private final AnnotationType annotationType;
    private final String descriptionString;
    private final String usageString;
    private final String versionString;
    private final Date createdDateTime;

    public AnnotationBlock(final String keyword,
            final AnnotationType annotationType,
            final String descriptionString, final String usageString,
            final Date createdDateTime) {
        if (noLength(keyword, descriptionString, usageString)
                || createdDateTime == null || annotationType == null) {
            throw new InvalidArgument("blank argument(s) to annotation block.");
        }

        this.keyword = keyword;
        this.annotationType = annotationType;
        this.descriptionString = descriptionString;
        this.usageString = usageString;
        this.createdDateTime = createdDateTime;
        this.versionString = null;
    }

    public AnnotationBlock(final String keyword,
            final AnnotationType annotationType,
            final String descriptionString, final String usageString,
            final Date createdDateTime, final String versionString) {
        if (noLength(keyword, descriptionString, usageString)
                || createdDateTime == null || annotationType == null) {
            throw new InvalidArgument("blank argument(s) to annotation block.");
        }

        this.keyword = keyword;
        this.annotationType = annotationType;
        this.descriptionString = descriptionString;
        this.usageString = usageString;
        this.versionString = versionString;
        this.createdDateTime = createdDateTime;
    }

    /**
     * Retrieves the keyword; the annotation block's {@code Keyword}
     * entry.
     * 
     * @return the keyword {@link String}, will not be null
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Retrieves the annotation type; the annotation block's {@code TypeString}
     * entry converted to {@link AnnotationType}.
     * 
     * @return the type {@link AnnotationType}, will not be null
     */
    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    /**
     * Retrieves the description; the annotation block's
     * {@code DescriptionString} entry.
     * 
     * @return the description {@link String}, will not be null
     */
    public String getDescriptionString() {
        return descriptionString;
    }

    /**
     * Retrieves the usage; the annotation block's {@code UsageString}
     * entry.
     * 
     * @return the usage {@link String}, will not be null
     */
    public String getUsageString() {
        return usageString;
    }

    /**
     * Retrieves the version; the annotation block's {@code VersionString}
     * entry.
     * 
     * @return the version {@link String}, may be null
     */
    public String getVersionString() {
        return versionString;
    }

    /**
     * Retrieves the created date time; the annotation block's
     * {@code CreatedDateTime} entry.
     * 
     * @return the created date time {@link Date}, will not be null
     */
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public static AnnotationBlock create(final String resourceLocation,
            final Properties blockProperties)
            throws BELDataMissingPropertyException,
            BELDataConversionException,
            BELDataInvalidPropertyException {

        String keyword = null;
        String type = null;
        AnnotationType annotationType = null;
        String description = null;
        String usage = null;
        String version = null;
        String createdDateTime = null;

        keyword = blockProperties.getProperty("Keyword");
        if (StringUtils.isBlank(keyword)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "Keyword");
        }

        type = blockProperties.getProperty("TypeString");
        if (StringUtils.isBlank(type)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "TypeString");
        }
        if (LIST_TYPE.equalsIgnoreCase(type)) {
            annotationType = AnnotationType.ENUMERATION;
        } else if (PATTERN_TYPE.equalsIgnoreCase(type)) {
            annotationType = AnnotationType.REGULAR_EXPRESSION;
        } else {
            throw new BELDataInvalidPropertyException(resourceLocation,
                    BLOCK_NAME, "TypeString", TYPE_STRING_VALUES);
        }

        description = blockProperties.getProperty("DescriptionString");
        if (StringUtils.isBlank(description)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "DescriptionString");
        }

        usage = blockProperties.getProperty("UsageString");
        if (StringUtils.isBlank(usage)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "UsageString");
        }

        createdDateTime = blockProperties.getProperty("CreatedDateTime");
        if (StringUtils.isBlank(createdDateTime)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "CreatedDateTime");
        }

        version = blockProperties.getProperty("VersionString");
        description = blockProperties.getProperty("DescriptionString");

        return new AnnotationBlock(typeConvert(resourceLocation, "Keyword",
                keyword, String.class),
                annotationType,
                typeConvert(resourceLocation, "DescriptionString", description,
                        String.class),
                typeConvert(resourceLocation, "UsageString", usage,
                        String.class),
                typeConvert(resourceLocation, "CreatedDateTime",
                        createdDateTime, Date.class),
                typeConvert(resourceLocation, "VersionString", version,
                        String.class));
    }
}
