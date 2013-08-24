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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openbel.bel.model.BELAnnotation;
import org.openbel.framework.common.model.Annotation;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.CommonModelFactory;

public class BELAnnotationConverter extends
        BELConverter<BELAnnotation, List<Annotation>> {
    private final BELAnnotationDefinitionConverter badc =
            new BELAnnotationDefinitionConverter();
    private final Map<String, AnnotationDefinition> adefs;

    public BELAnnotationConverter(Map<String, AnnotationDefinition> adefs) {
        this.adefs = adefs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Annotation> convert(BELAnnotation ba) {
        if (ba == null) {
            return null;
        }

        List<Annotation> annotations = new ArrayList<Annotation>();
        AnnotationDefinition ad =
                adefs.get(ba.getAnnotationDefinition().getName());

        for (String value : ba.getValues()) {
            annotations.add(CommonModelFactory.getInstance().createAnnotation(
                    value, ad));
        }

        return annotations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BELAnnotation convert(List<Annotation> a) {
        if (a == null) {
            return null;
        }

        List<String> values = new ArrayList<String>();
        AnnotationDefinition ad = null;
        for (int i = 0; i < a.size(); i++) {
            Annotation ann = a.get(i);
            if (i == 0) {
                ad = ann.getDefinition();
            } else if (ad != null && !ad.equals(ann.getDefinition())) {
                throw new IllegalStateException(
                        "annotations must have equal definitions.");
            }

            values.add(ann.getValue());
        }

        return new BELAnnotation(badc.convert(ad), values);
    }
}
