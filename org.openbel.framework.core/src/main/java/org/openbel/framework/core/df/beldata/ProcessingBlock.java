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
