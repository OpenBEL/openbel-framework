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
package org.openbel.framework.common.model;

import static org.openbel.framework.common.enums.AnnotationType.REGULAR_EXPRESSION;

/**
 * This is a specialization of annotation definitions specific to the
 * identification of a citation.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class CitationReferenceAnnotationDefinition extends AnnotationDefinition {
    private static final long serialVersionUID = 1956833893542516604L;

    public static final String ANNOTATION_DEFINITION_ID = "CitationReference";

    public CitationReferenceAnnotationDefinition() {
        super(ANNOTATION_DEFINITION_ID, REGULAR_EXPRESSION,
                "The unique identifier of the citation resource.",
                "Use this annotation to link statements to a unique " +
                        "identifier that represents the citation.");
        setValue(".*");
    }
}
