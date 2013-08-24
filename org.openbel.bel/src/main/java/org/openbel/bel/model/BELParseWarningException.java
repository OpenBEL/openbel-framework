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

public abstract class BELParseWarningException extends Exception {
    private static final long serialVersionUID = 897952748848423459L;
    private final static String ERROR_PREFIX =
            "Warning at line %d, character %d: %s";
    private final int line;
    private final int character;

    public BELParseWarningException(final int line, final int character,
            String message) {
        super(String.format(ERROR_PREFIX, line, character, message));
        this.line = line;
        this.character = character;
    }

    public int getLine() {
        return line;
    }

    public int getCharacter() {
        return character;
    }

    public static final class UnsetUndefinedAnnotationException extends
            BELParseWarningException {
        private static final long serialVersionUID = 313633006453020352L;
        private static final String MESSAGE =
                "Annotation cannot be UNSET since it is not defined.";

        public UnsetUndefinedAnnotationException(int line, int character) {
            super(line, character, MESSAGE);
        }
    }
}
