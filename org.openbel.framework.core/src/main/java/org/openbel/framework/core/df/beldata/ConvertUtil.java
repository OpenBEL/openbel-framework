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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

public class ConvertUtil {

    private ConvertUtil() {
    }

    private static final Pattern KEYWORD_REGEX = Pattern.compile("\\w+");

    private static final String KEYWORD_TYPE = "Keyword";

    private static final String STRING_TYPE = "String";

    private static final String DATE_TYPE = "Date";

    private static final String DATE_TIME_TYPE = "DateTime";

    private static final String BOOLEAN_TYPE = "Flag";

    private static final String URL_TYPE = "URL";

    @SuppressWarnings("unchecked")
    public static <T> T typeConvert(String resourceLocation,
            String propertyName,
            String propertyValue, Class<T> type)
            throws BELDataConversionException {
        if (propertyValue == null) {
            return null;
        }

        if (type.equals(String.class) && propertyName.endsWith(KEYWORD_TYPE)) {
            Matcher keywordMatcher = KEYWORD_REGEX.matcher(propertyValue);
            if (!keywordMatcher.matches()) {
                throw new BELDataConversionException(resourceLocation,
                        propertyName, propertyValue);
            }

            return (T) propertyValue;
        }
        else if (type.equals(String.class)
                && propertyName.endsWith(STRING_TYPE)) {
            return (T) propertyValue;
        } else if (type.equals(Date.class) && propertyName.endsWith(DATE_TYPE)) {
            try {
                return (T) DatatypeConverter.parseDate(propertyValue).getTime();
            } catch (IllegalArgumentException e) {
                throw new BELDataConversionException(resourceLocation,
                        propertyName, propertyValue);
            }
        } else if (type.equals(Date.class)
                && propertyName.endsWith(DATE_TIME_TYPE)) {
            try {
                return (T) DatatypeConverter.parseDateTime(propertyValue)
                        .getTime();
            } catch (IllegalArgumentException e) {
                throw new BELDataConversionException(resourceLocation,
                        propertyName, propertyValue);
            }
        } else if (type.equals(Boolean.class)
                && propertyName.endsWith(BOOLEAN_TYPE)) {
            return (T) Boolean.valueOf(propertyValue.toLowerCase()
                    .equals("yes"));
        } else if (type.equals(URL.class) && propertyName.endsWith(URL_TYPE)) {
            try {
                return (T) new URL(propertyValue);
            } catch (MalformedURLException e) {
                throw new BELDataConversionException(resourceLocation,
                        propertyName, propertyValue);
            }
        } else {
            throw new BELDataConversionException(resourceLocation,
                    propertyName, propertyValue);
        }
    }
}
