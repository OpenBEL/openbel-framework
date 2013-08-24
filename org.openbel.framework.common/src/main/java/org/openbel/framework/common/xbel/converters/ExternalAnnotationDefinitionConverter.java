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

import org.openbel.bel.xbel.model.XBELExternalAnnotationDefinition;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.CommonModelFactory;

/**
 * Converter class for converting between
 * {@link XBELExternalAnnotationDefinition} and {@link AnnotationDefinition}.
 */
public class ExternalAnnotationDefinitionConverter extends
        JAXBConverter<XBELExternalAnnotationDefinition, AnnotationDefinition> {

    /**
     * {@inheritDoc}
     */
    @Override
    public XBELExternalAnnotationDefinition convert(AnnotationDefinition t) {
        if (t == null || t.getURL() == null) {
            return null;
        }

        XBELExternalAnnotationDefinition dest =
                new XBELExternalAnnotationDefinition();

        String id = t.getId();
        String url = t.getURL();

        dest.setId(id);
        dest.setUrl(url);

        return dest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationDefinition convert(XBELExternalAnnotationDefinition t) {
        if (t == null) {
            return null;
        }

        String id = t.getId();
        String url = t.getUrl();

        AnnotationDefinition dest = CommonModelFactory.getInstance()
                .createAnnotationDefinition(id);
        dest.setURL(url);

        return dest;
    }

}
