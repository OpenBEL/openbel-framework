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
package org.openbel.framework.core.compiler;

import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Statement;

// TODO Document
public interface ValidationService {

    // TODO Pull BELValidationService, XBELValidatorService into here
    //       Use ValidationResult as standard return type

    /**
     * Validate a {@link Document}.
     *
     * @param document {@link Document} to validate, can not be null.
     * @return {@link ValidationResult} containing all errors and/or
     * warnings that occurred during the validation, never null but can be
     * empty.
     */
    ValidationResult validate(Document document);

    /***
     * Validate a {@link Statement}.
     *
     * @param statement {@link Statement} to validate, can not be null.
     * @return {@link ValidationResult} containing all errors and/or
     * warnings that occurred during the validation, never null but can be
     * empty.
     */
    ValidationResult validate(Statement statement);
}
