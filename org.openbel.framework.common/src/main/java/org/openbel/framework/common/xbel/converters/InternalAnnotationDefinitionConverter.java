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
