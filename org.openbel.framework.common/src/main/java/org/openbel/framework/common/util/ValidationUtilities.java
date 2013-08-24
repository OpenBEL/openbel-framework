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
