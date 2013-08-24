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

import static org.openbel.framework.common.enums.AnnotationType.REGULAR_EXPRESSION;

import java.util.List;

import org.openbel.bel.xbel.model.XBELInternalAnnotationDefinition;
import org.openbel.bel.xbel.model.XBELListAnnotation;
import org.openbel.framework.common.enums.AnnotationType;
import org.openbel.framework.common.model.AnnotationDefinition;

/**
 * Converter class for converting between
 * {@link XBELInternalAnnotationDefinition} and {@link AnnotationDefinition}.
 */
public class InternalAnnotationDefinitionConverter extends
        JAXBConverter<XBELInternalAnnotationDefinition, AnnotationDefinition> {

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELInternalAnnotationDefinition convert(AnnotationDefinition t) {
        if (t == null || t.getURL() != null) {
            return null;
        }

        XBELInternalAnnotationDefinition dest =
                new XBELInternalAnnotationDefinition();

        String description = t.getDescription();
        String id = t.getId();
        String usage = t.getUsage();

        dest.setDescription(description);
        dest.setId(id);
        dest.setUsage(usage);

        AnnotationType type = t.getType();
        String value = t.getValue();

        switch (type) {
        case ENUMERATION:
            List<String> enums = t.getEnums();
            XBELListAnnotation xla = new XBELListAnnotation();
            List<String> xlaval = xla.getListValue();
            xlaval.addAll(enums);
            dest.setListAnnotation(xla);
            break;
        case REGULAR_EXPRESSION:
            dest.setPatternAnnotation(value);
            break;
        default:
            throw new UnsupportedOperationException("unknown type: " + type);
        }

        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationDefinition convert(XBELInternalAnnotationDefinition t) {
        if (t == null) {
            return null;
        }

        String desc = t.getDescription();
        String id = t.getId();
        String use = t.getUsage();

        AnnotationDefinition dest = null;
        if (t.isSetListAnnotation()) {
            XBELListAnnotation listAnnotation = t.getListAnnotation();
            List<String> vals = listAnnotation.getListValue();
            dest = new AnnotationDefinition(id, desc, use, vals);
        } else if (t.isSetPatternAnnotation()) {
            String regex = t.getPatternAnnotation();
            dest = new AnnotationDefinition(id, REGULAR_EXPRESSION, desc, use);
            dest.setValue(regex);
        } else {
            throw new UnsupportedOperationException("unhandled");
        }

        return dest;
    }

}
