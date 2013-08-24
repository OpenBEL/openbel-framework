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
package org.openbel.framework.common;

/**
 * MissingEncodingException defines a
 * {@link BELUncheckedException unchecked exception} that represents a
 * character encoding not available in the current environment.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class MissingEncodingException extends BELUncheckedException {
    private static final long serialVersionUID = 6448780981733610067L;

    public MissingEncodingException(String encoding) {
        super(createMessage(encoding));
    }

    public MissingEncodingException(String encoding, Throwable cause) {
        super(createMessage(encoding), cause);
    }

    private static String createMessage(String encoding) {
        return "Missing character encoding '" + encoding + "'.";
    }
}
