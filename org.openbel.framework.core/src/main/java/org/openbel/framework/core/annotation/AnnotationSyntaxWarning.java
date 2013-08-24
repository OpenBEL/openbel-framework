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
package org.openbel.framework.core.annotation;

import static java.lang.String.format;
import static org.openbel.framework.common.Strings.ANNOTATION_WARNING;

import org.openbel.framework.core.compiler.ResourceSyntaxWarning;

/**
 * A resource syntax warning indicating a given value is syntactically incorrect
 * for an annotation.
 *
 * @author Steve Ungerer
 */
public class AnnotationSyntaxWarning extends ResourceSyntaxWarning {
    private static final long serialVersionUID = 1118997488168602331L;

    private final String annotationName;

    /**
     * Construct an {@link AnnotationSyntaxWarning} indicating that a given
     * value, defined with the given name is syntactically incorrect based on
     * the definition located at the given resourceLocation.
     *
     * @param resourceLocation
     *            the URL defining the annotation definition
     * @param annotationName
     *            the name of the annotation
     * @param value
     *            the value of the annotation
     */
    public AnnotationSyntaxWarning(String resourceLocation,
            String annotationName,
            String value) {
        super(resourceLocation, value);
        this.annotationName = annotationName;
    }

    /**
     * Construct an {@link AnnotationSyntaxWarning} indicating that a given
     * value, defined internally with the given name is syntactically incorrect.
     *
     * @param annotationName the name of the annotation
     * @param value the value of the annotation
     */
    public AnnotationSyntaxWarning(String annotationName, String value) {
        super(annotationName, value);
        this.annotationName = annotationName;
    }

    /**
     * @return the name of the annotation
     */
    public String getAnnotationName() {
        return annotationName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        String name = getAnnotationName();
        String val = getValue();
        return format(ANNOTATION_WARNING, val, name);
    }
}
