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
package org.openbel.framework.core;

import java.io.File;
import java.util.List;

import org.openbel.framework.core.compiler.ValidationError;

/**
 * Validates XBEL documents against the XBEL schema.
 */
public interface XBELValidatorService {

    // TODO Push this service into ValidationService, use ValidationResults

    /**
     * Returns {@code true} if the provided string is a valid XBEL document,
     * {@code false} otherwise.
     *
     * @param s XBEL XML string
     * @return boolean
     */
    public boolean isValid(String s);

    /**
     * Returns {@code true} if the provided file is a valid XBEL document,
     * {@code false} otherwise.
     *
     * @param f XBEL XML file
     * @return boolean
     */
    public boolean isValid(File f);

    /**
     * Validates the provided string as a XBEL document.
     *
     * @param s XBEL XML string
     * @throws ValidationError Thrown to indicate the provided string fails
     * validation. If the string contains one or more errors, only the first
     * error will cause an exception.
     * @see #validateWithErrors(String)
     */
    public void validate(String s) throws ValidationError;

    /**
     * Validates the provided string as a XBEL document, returning any and all
     * validation errors.
     *
     * @param s XBEL XML string
     * @return A non-null list of validation errors for the XML string
     */
    public List<ValidationError> validateWithErrors(String s);

    /**
     * Validates the provided file as a XBEL document.
     *
     * @param f XBEL XML file
     * @throws ValidationError Thrown to indicate the provided file fails
     * validation. If the file contains one or more errors, only the first error
     * will cause an exception.
     */
    public void validate(File f) throws ValidationError;

    /**
     * Validates the provided file as a XBEL document, returning any and all
     * validation errors.
     *
     * @param f XBEL XML file
     * @return A non-null list of validation errors for the XML file
     */
    public List<ValidationError> validateWithErrors(File f);

}
