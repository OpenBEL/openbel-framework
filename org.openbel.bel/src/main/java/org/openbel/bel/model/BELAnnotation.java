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
package org.openbel.bel.model;

import java.util.ArrayList;
import java.util.List;

public class BELAnnotation extends BELObject {
    private static final long serialVersionUID = 7396393708657440051L;
    private final BELAnnotationDefinition annotationDefinition;
    private final List<String> values = new ArrayList<String>();

    public BELAnnotation(BELAnnotationDefinition annotationDefinition,
            String value) {
        if (annotationDefinition == null) {
            throw new IllegalArgumentException(
                    "annotationDefinition must be set.");
        }

        if (value == null) {
            throw new IllegalArgumentException("value must be set.");
        }

        this.annotationDefinition = annotationDefinition;
        this.values.add(clean(value));
    }

    public BELAnnotation(BELAnnotationDefinition annotationDefinition,
            List<String> values) {
        if (annotationDefinition == null) {
            throw new IllegalArgumentException(
                    "annotationDefinition must be set.");
        }

        if (values == null) {
            throw new IllegalArgumentException("values must be set.");
        }

        this.annotationDefinition = annotationDefinition;
        for (String value : values) {
            value = clean(value);
            this.values.add(value);
        }
    }

    public BELAnnotationDefinition getAnnotationDefinition() {
        return annotationDefinition;
    }

    public List<String> getValues() {
        return values;
    }
}
