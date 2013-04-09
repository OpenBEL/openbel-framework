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
package org.openbel.framework.common.util;

import static java.util.regex.Pattern.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.AnnotationType;
import org.openbel.framework.common.model.Annotation;
import org.openbel.framework.common.model.AnnotationDefinition;

/**
 * Utilities for performing validation.
 *
 * @author Steve Ungerer
 */
public abstract class ValidationUtilities {
    private static Map<String, Pattern> patternCache =
            new ConcurrentHashMap<String, Pattern>();

    /**
     * Determine if the given {@link Annotation} is valid. A valid
     * {@link Annotation} is one in which its value is valid for its defined
     * {@link AnnotationDefinition}.
     *
     * @param annotation
     *            the {@link Annotation} to validate. This {@link Annotation}
     *            <em>must</em> return a <code>non-null</code>
     *            {@link AnnotationDefinition} for
     *            {@link Annotation#getDefinition()}.
     * @return whether the {@link Annotation} is valid
     * @throws PatternSyntaxException
     *             if the {@link AnnotationDefinition} used by the
     *             {@link Annotation} is of type
     *             {@value AnnotationType#REGULAR_EXPRESSION} and the specified
     *             pattern is invalid.
     * @see #isValid(AnnotationDefinition, String)
     */
    public static boolean isValid(Annotation annotation)
            throws PatternSyntaxException {
        return isValid(annotation.getDefinition(), annotation.getValue());
    }

    /**
     * Determine if the given value is valid as defined by the specified
     * {@link AnnotationDefinition}. A valid value is one that is valid for the
     * specified domain of the {@link AnnotationDefinition}.
     *
     * @param annoDef
     *            a <code>non-null</code> {@link AnnotationDefinition}. Although
     *            the {@link AnnotationType} of the annoDef may be null, this
     *            method will always evaluate to <code>false</code> in such a
     *            case.
     * @param value
     *            the value to validate, possibly <code>null</code>
     * @return whether the given value is valid as defined by the given
     *         {@link AnnotationDefinition}
     * @throws PatternSyntaxException
     *             if the {@link AnnotationDefinition} used by the
     *             {@link Annotation} is of type
     *             {@value AnnotationType#REGULAR_EXPRESSION} and the specified
     *             pattern is invalid.
     * @throws InvalidArgument
     *             Thrown if the <tt>annoDef</tt> argument isnull
     */
    public static boolean isValid(AnnotationDefinition annoDef, String value)
            throws PatternSyntaxException {
        if (annoDef == null) {
            throw new InvalidArgument("annoDef", annoDef);
        }
        if (annoDef.getType() == null) {
            return false;
        }
        switch (annoDef.getType()) {
        case ENUMERATION:
            return validateEnumeration(annoDef.getEnums(), value);
        case REGULAR_EXPRESSION:
            return validateRegExp(annoDef.getValue(), value);
        default:
            return false;
        }
    }

    /**
     * Validate the given value against a collection of allowed values.
     *
     * @param allowedValues
     * @param value Value to test
     * @return boolean {@code true} if {@code value} in {@code allowedValues}
     */
    private static boolean validateEnumeration(
            Collection<String> allowedValues, String value) {
        return (allowedValues != null && allowedValues.contains(value));
    }

    /**
     * Validate the given value against a regular expression pattern
     *
     * @param pattern Regular expression pattern
     * @param value Value to test
     * @return boolean {@code true} if {@code value} matches {@code pattern}
     * @throws PatternSyntaxException
     *             if the given pattern is invalid
     */
    protected static boolean validateRegExp(String pattern, String value)
            throws PatternSyntaxException {
        Pattern re = patternCache.get(pattern);
        if (re == null) {
            re = compile(pattern, MULTILINE | DOTALL);
            patternCache.put(pattern, re);
        }

        return (re.matcher(value).matches());
    }
}
