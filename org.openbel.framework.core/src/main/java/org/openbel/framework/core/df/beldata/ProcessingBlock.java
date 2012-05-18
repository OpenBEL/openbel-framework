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
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;

/**
 * ProcessingBlock encapsulates the '[Processing]' block of a BEL namespace
 * document.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ProcessingBlock implements Serializable {
    private static final long serialVersionUID = 8118881317623256468L;

    /**
     * Defines the text within the citation block: {@value}
     */
    public static final String BLOCK_NAME = "Processing";

    private static final boolean DEFAULT_CASE_SENSITIVITY = false;

    private static final boolean DEFAULT_CACHEABLE = true;

    /**
     * Defines the value holder for the processing block's 'CaseSensitiveFlag'
     * property. This field is defaulted to {@value #DEFAULT_CASE_SENSITIVITY}
     * if the 'CaseSensitiveFlag' is not defined.
     */
    private boolean caseSensitiveFlag = DEFAULT_CASE_SENSITIVITY;

    /**
     * Defines the value holder for the processing block's 'DelimiterString'
     * property. This field defines the delimiter used in the lines following
     * the 'Values' block.
     */
    private String delimiterString;

    /**
     * Defines the value holder for the processing block's 'CacheableFlag'
     * property. This field is defaulted to {@value #DEFAULT_CACHEABLE} if the
     * 'CacheableFlag' is not defined.
     */
    private boolean cacheableFlag = DEFAULT_CACHEABLE;

    public ProcessingBlock(final String delimiterString) {
        if (noLength(delimiterString)) {
            throw new InvalidArgument("delimiterString", delimiterString);
        }

        this.delimiterString = delimiterString;
    }

    public ProcessingBlock(final String delimiterString,
            final boolean caseSensitiveFlag, final boolean cacheableFlag) {
        if (noLength(delimiterString)) {
            throw new InvalidArgument("delimiterString", delimiterString);
        }

        this.delimiterString = delimiterString;
        this.caseSensitiveFlag = caseSensitiveFlag;
        this.cacheableFlag = cacheableFlag;
    }

    /**
     * Retrieves the case sensitive flag.
     * 
     * @return {@code boolean}, the case sensitive flag
     */
    public boolean isCaseSensitiveFlag() {
        return caseSensitiveFlag;
    }

    /**
     * Retrieves the delimiter string.
     * 
     * @return {@link String}, the delimiter string
     */
    public String getDelimiterString() {
        return delimiterString;
    }

    /**
     * Retrieves the cacheable flag.
     * 
     * @return {@code boolean}, the cacheable flag
     */
    public boolean isCacheableFlag() {
        return cacheableFlag;
    }

    public static ProcessingBlock create(final String resourceLocation,
            final Properties blockProperties)
            throws BELDataMissingPropertyException, BELDataConversionException {
        String delimiterString = null;
        String caseSensitiveFlag = null;
        String cacheableFlag = null;

        delimiterString = blockProperties.getProperty("DelimiterString");
        if (StringUtils.isBlank(delimiterString)) {
            throw new BELDataMissingPropertyException(resourceLocation,
                    BLOCK_NAME, "DelimiterString");
        }

        caseSensitiveFlag = blockProperties.getProperty("CaseSensitiveFlag");
        cacheableFlag = blockProperties.getProperty("CacheableFlag");

        Boolean caseSensitive = typeConvert(resourceLocation,
                "CaseSensitiveFlag", caseSensitiveFlag, Boolean.class);
        if (caseSensitive == null) {
            caseSensitive = DEFAULT_CASE_SENSITIVITY;
        }

        Boolean cacheable = typeConvert(resourceLocation, "CacheableFlag",
                cacheableFlag,
                Boolean.class);
        if (cacheable == null) {
            cacheable = DEFAULT_CACHEABLE;
        }

        return new ProcessingBlock(typeConvert(resourceLocation,
                "DelimiterString", delimiterString,
                String.class),
                caseSensitive,
                cacheable);
    }
}
