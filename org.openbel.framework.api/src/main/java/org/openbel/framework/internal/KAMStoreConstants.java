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
package org.openbel.framework.internal;

import org.openbel.framework.common.model.CitationAuthorsAnnotationDefinition;
import org.openbel.framework.common.model.CitationCommentAnnotationDefinition;
import org.openbel.framework.common.model.CitationDateAnnotationDefinition;
import org.openbel.framework.common.model.CitationNameAnnotationDefinition;
import org.openbel.framework.common.model.CitationReferenceAnnotationDefinition;
import org.openbel.framework.common.model.CitationTypeAnnotationDefinition;

public interface KAMStoreConstants {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String[] CITATION_ANNOTATION_DEFINITION_IDS = {
            CitationAuthorsAnnotationDefinition.ANNOTATION_DEFINITION_ID,
            CitationDateAnnotationDefinition.ANNOTATION_DEFINITION_ID,
            CitationNameAnnotationDefinition.ANNOTATION_DEFINITION_ID,
            CitationTypeAnnotationDefinition.ANNOTATION_DEFINITION_ID,
            CitationCommentAnnotationDefinition.ANNOTATION_DEFINITION_ID,
            CitationReferenceAnnotationDefinition.ANNOTATION_DEFINITION_ID };

}
