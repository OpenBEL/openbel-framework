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
package org.openbel.framework.core.df.beldata;

/**
 * AbortParsingException represents an exception thrown to stop the parsing of a BEL
 * Data file.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class AbortParsingException extends Exception {
    private static final long serialVersionUID = 3720907620275936588L;

    /**
     * Creates the {@link AbortParsingException} from a {@code message}.
     *
     * @param message {@link String}, the message
     */
    public AbortParsingException(String message) {
        super(message);
    }

    /**
     * Creates the {@link AbortParsingException} from a {@code message} and the exception's {@code cause}
     *
     * @param message {@link String}, the message
     * @param cause {@link Throwable}, the cause
     */
    public AbortParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
