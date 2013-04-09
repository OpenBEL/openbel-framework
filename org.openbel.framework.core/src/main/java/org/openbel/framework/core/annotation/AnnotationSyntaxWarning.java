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
