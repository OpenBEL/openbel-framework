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
package org.openbel.framework.common.xbel.converters;

import org.openbel.bel.xbel.model.XBELAnnotation;
import org.openbel.framework.common.enums.AnnotationType;
import org.openbel.framework.common.model.Annotation;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.CommonModelFactory;

/**
 * Converter class for converting between {@link XBELAnnotation} and
 * {@link Annotation}.
 * <p>
 * The XBEL schema defines a referential {@code refID} property of annotations
 * that refers to a specific annotation definition instance. This instance is
 * not available to the converter and will need to be resolved prior to use as a
 * common {@link Annotation} object.
 * </p>
 */
public final class AnnotationConverter extends
        JAXBConverter<XBELAnnotation, Annotation> {

    /**
     * Constant used in annotation definition types. Only annotation definition
     * references are available to this converter. This field will be used as a
     * placeholder in annotation definition types.
     */
    public static final AnnotationType UNRESOLVED_ANNOTATION_DEFINITION_TYPE =
            AnnotationType.REGULAR_EXPRESSION;

    /**
     * Constant used in annotation definition values. Only annotation definition
     * references are available to this converter. This field will be used as a
     * placeholder in annotation definition values.
     */
    public static final String UNRESOLVED_ANNOTATION_DEFINITION_VALUE =
            "<unresolved_annotation>";

    /**
     * Constant used in annotation definition values. Only annotation definition
     * references are available to this converter. This field will be used as a
     * placeholder in annotation definition description.
     */
    public static final String UNRESOLVED_ANNOTATION_DEFINITION_DESCRIPTION =
            "<unresolved_annotation>";

    /**
     * Constant used in annotation definition values. Only annotation definition
     * references are available to this converter. This field will be used as a
     * placeholder in annotation definition usage.
     */
    public static final String UNRESOLVED_ANNOTATION_DEFINITION_USAGE =
            "<unresolved_annotation>";

    /**
     * {@inheritDoc}
     */
    @Override
    public Annotation convert(XBELAnnotation source) {
        if (source == null) return null;

        String refID = source.getRefID();
        String value = source.getValue();

        final AnnotationDefinition ad = CommonModelFactory.getInstance()
                .createAnnotationDefinition(refID,
                        UNRESOLVED_ANNOTATION_DEFINITION_TYPE,
                        UNRESOLVED_ANNOTATION_DEFINITION_DESCRIPTION,
                        UNRESOLVED_ANNOTATION_DEFINITION_USAGE,
                        UNRESOLVED_ANNOTATION_DEFINITION_VALUE);

        // Destination type
        final Annotation dest = CommonModelFactory.getInstance()
                .createAnnotation(value, ad);
        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELAnnotation convert(Annotation source) {
        if (source == null) return null;

        AnnotationDefinition definition = source.getDefinition();
        String refID = definition.getId();
        String value = source.getValue();

        XBELAnnotation xa = new XBELAnnotation();
        xa.setRefID(refID);
        xa.setValue(value);

        return xa;
    }
}
