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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * Container Object to hold all warnings and errors that occurred as the result
 * of a validation action.
 *
 * @author Steve Ungerer
 */
public class ValidationResult {
    private final List<String> warnings;
    private final List<String> errors;

    public ValidationResult(List<String> warnings,
            List<String> errors) {
        if (warnings == null) {
            throw new IllegalArgumentException("warnings must not be null");
        }
        this.warnings = warnings;
        if (errors == null) {
            throw new IllegalArgumentException("errors must not be null");
        }
        this.errors = errors;
    }

    /**
     * @return {@link List} of warnings that occurred, never
     *         <code>null</code>
     */
    public List<String> getWarnings() {
        return warnings;
    }

    /**
     * @return {@link List} of errors that occurred, never <code>null</code>
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * @return true if the object has warnings, false if not
     */
    public boolean hasWarnings() {
        return CollectionUtils.isNotEmpty(warnings);
    }

    /**
     * @return true if the object has errors, false if not
     */
    public boolean hasErrors() {
        return CollectionUtils.isNotEmpty(errors);
    }

}
