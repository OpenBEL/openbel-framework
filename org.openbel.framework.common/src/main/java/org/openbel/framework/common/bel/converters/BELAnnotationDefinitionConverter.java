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
package org.openbel.framework.common.bel.converters;

import org.openbel.bel.model.BELAnnotationDefinition;
import org.openbel.bel.model.BELAnnotationType;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.AnnotationType;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.CommonModelFactory;

public final class BELAnnotationDefinitionConverter extends
        BELConverter<BELAnnotationDefinition, AnnotationDefinition> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationDefinition convert(BELAnnotationDefinition bad) {
        if (bad == null) {
            return null;
        }

        AnnotationDefinition ad = CommonModelFactory.getInstance()
                .createAnnotationDefinition(bad.getName());
        switch (bad.getAnnotationType()) {
        case LIST:
            ad.setType(AnnotationType.ENUMERATION);
            ad.setEnums(bad.getList());
            break;
        case PATTERN:
            ad.setType(AnnotationType.REGULAR_EXPRESSION);
            ad.setValue(bad.getValue());
            break;
        case URL:
            ad.setType(null);
            ad.setURL(bad.getValue());
            break;
        default:
            throw new InvalidArgument("BELAnnotationType '"
                    + bad.getAnnotationType() + "' not known.");
        }

        return ad;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELAnnotationDefinition convert(AnnotationDefinition ad) {
        if (ad == null) {
            return null;
        }

        if (ad.getType() == null) {
            return new BELAnnotationDefinition(
                    ad.getId(), BELAnnotationType.URL, ad.getURL());
        }

        switch (ad.getType()) {
        case ENUMERATION:
            return new BELAnnotationDefinition(ad.getId(),
                    BELAnnotationType.LIST, ad.getEnums());
        case REGULAR_EXPRESSION:
            return new BELAnnotationDefinition(ad.getId(),
                    BELAnnotationType.PATTERN, ad.getValue());
        default:
            throw new InvalidArgument("AnnotationType '"
                    + ad.getType() + "' not known.");
        }
    }
}
