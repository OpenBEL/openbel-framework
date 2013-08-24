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
