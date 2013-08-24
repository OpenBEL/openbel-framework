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
 * This is a specialization of annotation definitions specific to the date of a
 * citation.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class CitationDateAnnotationDefinition extends AnnotationDefinition {
    private static final long serialVersionUID = -546824774034578760L;

    public static final String ANNOTATION_DEFINITION_ID = "CitationDate";
    private static final String DATE_REGEX =
            "[0-9]{4}[-]{0,1}[0-9]{2}[-]{0,1}[0-9]{2}";

    public CitationDateAnnotationDefinition() {
        super(ANNOTATION_DEFINITION_ID, REGULAR_EXPRESSION,
                "The date of the citation resource.",
                "Use this annotation to link statements to a citation date " +
                        "in ISO 8601 date format (YYYY-MM-DD or YYYYMMDD).");
        setValue(DATE_REGEX);
    }
}
