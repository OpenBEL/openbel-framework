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
package org.openbel.framework.common.bel.parser;

import java.util.ArrayList;
import java.util.List;

import org.openbel.bel.model.BELDocument;
import org.openbel.bel.model.BELParseErrorException;
import org.openbel.bel.model.BELParseWarningException;

public class BELParseResults {

    private final List<BELParseWarningException> syntaxWarnings =
            new ArrayList<BELParseWarningException>();
    private final List<BELParseErrorException> syntaxErrors =
            new ArrayList<BELParseErrorException>();
    private BELDocument document;

    public BELParseResults(final List<BELParseWarningException> syntaxWarnings,
            final List<BELParseErrorException> syntaxErrors,
            final BELDocument document) {
        if (syntaxWarnings != null) {
            this.syntaxWarnings.addAll(syntaxWarnings);
        }

        if (syntaxErrors != null) {
            this.syntaxErrors.addAll(syntaxErrors);
        }

        this.document = document;
    }

    public List<BELParseWarningException> getSyntaxWarnings() {
        return syntaxWarnings;
    }

    public List<BELParseErrorException> getSyntaxErrors() {
        return syntaxErrors;
    }

    public BELDocument getDocument() {
        return document;
    }
}
