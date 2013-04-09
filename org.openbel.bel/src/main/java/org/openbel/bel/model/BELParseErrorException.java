/**
 * Copyright (C) 2012-2013 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
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
