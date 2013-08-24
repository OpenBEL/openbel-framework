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

public abstract class BELParseErrorException extends Exception {
    private static final long serialVersionUID = 897952748848423459L;
    private final static String ERROR_PREFIX =
            "Error at line %d, character %d: %s";
    private final int line;
    private final int character;

    public BELParseErrorException(final int line, final int character,
            String message, Throwable cause) {
        super(String.format(ERROR_PREFIX, line, character, message), cause);
        this.line = line;
        this.character = character;
    }

    public BELParseErrorException(final int line, final int character,
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

    public static final class SyntaxException extends BELParseErrorException {
        private static final long serialVersionUID = 3991530653585956624L;
        private static final String MESSAGE = "Syntax error ";

        public SyntaxException(int line, int character, String message,
                Throwable cause) {
            super(line, character, MESSAGE + message, cause);
        }
    }

    public static final class DocumentNameException extends
            BELParseErrorException {
        private static final long serialVersionUID = 313633006453020352L;
        private static final String MESSAGE =
                "Document 'Name' must be SET in properties section of the document.";

        public DocumentNameException(int line, int character) {
            super(line, character, MESSAGE);
        }
    }

    public static final class DocumentDescriptionException extends
            BELParseErrorException {
        private static final long serialVersionUID = 1999629197235687032L;
        private static final String MESSAGE =
                "Document 'Description' must be SET in properties section of the document.";

        public DocumentDescriptionException(int line, int character) {
            super(line, character, MESSAGE);
        }
    }

    public static final class DocumentVersionException extends
            BELParseErrorException {
        private static final long serialVersionUID = 232516805732892029L;
        private static final String MESSAGE =
                "Document 'Version' must be SET in properties section of the document.";

        public DocumentVersionException(int line, int character) {
            super(line, character, MESSAGE);
        }
    }

    public static final class DefineAnnotationBeforeUsageException extends
            BELParseErrorException {
        private static final long serialVersionUID = 7513269364258478717L;
        private static final String MESSAGE =
                "Annotation must be declared with DEFINE before it is SET.";

        public DefineAnnotationBeforeUsageException(int line, int character) {
            super(line, character, MESSAGE);
        }
    }

    public static final class SetDocumentPropertiesFirstException extends
            BELParseErrorException {
        private static final long serialVersionUID = 7513269364258478717L;
        private static final String MESSAGE =
                "Document properties must be SET before annotations and statements.";

        public SetDocumentPropertiesFirstException(int line, int character) {
            super(line, character, MESSAGE);
        }
    }

    public static final class UnsetDocumentPropertiesException extends
            BELParseErrorException {
        private static final long serialVersionUID = 7513269364258478717L;
        private static final String MESSAGE =
                "Document properties cannot be cleared with UNSET.";

        public UnsetDocumentPropertiesException(int line, int character) {
            super(line, character, MESSAGE);
        }
    }

    public static final class NamespaceUndefinedException extends
            BELParseErrorException {
        private static final long serialVersionUID = 7513269364258478717L;
        private static final String MESSAGE = "Namespace has not been defined.";

        public NamespaceUndefinedException(int line, int character) {
            super(line, character, MESSAGE);
        }
    }

    public static final class InvalidCitationException extends
            BELParseErrorException {
        private static final long serialVersionUID = 7513269364258478717L;
        private static final String MESSAGE = "Invalid definition of Citation.";

        public InvalidCitationException(int line, int character) {
            super(line, character, MESSAGE);
        }
    }

    public static final class InvalidEvidenceException extends
            BELParseErrorException {
        private static final long serialVersionUID = 7513269364258478717L;
        private static final String MESSAGE = "Invalid definition of Evidence.";

        public InvalidEvidenceException(int line, int character) {
            super(line, character, MESSAGE);
        }
    }
}
