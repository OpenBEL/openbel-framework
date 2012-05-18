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
