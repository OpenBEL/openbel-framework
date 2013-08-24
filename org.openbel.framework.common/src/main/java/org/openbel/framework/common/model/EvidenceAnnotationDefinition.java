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
 * TODO DOCUMENT
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class EvidenceAnnotationDefinition extends AnnotationDefinition {
    private static final long serialVersionUID = 7659446258441600547L;

    public static final String ANNOTATION_DEFINITION_ID = "Evidence";

    public EvidenceAnnotationDefinition() {
        super(ANNOTATION_DEFINITION_ID, REGULAR_EXPRESSION,
                "The evidence line that supports one or more statements.",
                "Use this annotation to link statements to an evidence line " +
                        "in a citation.");
        setValue(".*");
    }
}
