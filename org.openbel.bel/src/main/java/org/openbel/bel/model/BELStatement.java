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

import java.util.List;

public class BELStatement extends BELObject {
    private static final long serialVersionUID = 8315449656918398835L;
    private final String statementSyntax;
    private final List<BELAnnotation> annotations;
    private final BELCitation citation;
    private final BELEvidence evidence;
    private final String comment;

    public BELStatement(String belStatement) {
        if (belStatement == null) {
            throw new IllegalArgumentException("belstatement must be set");
        }

        this.statementSyntax = belStatement;
        this.annotations = null;
        this.citation = null;
        this.evidence = null;
        this.comment = null;
    }

    public BELStatement(final String belStatement,
            final List<BELAnnotation> annotations,
            final BELCitation citation, final BELEvidence evidence,
            String comment) {
        if (belStatement == null) {
            throw new IllegalArgumentException("belstatement must be set");
        }

        this.statementSyntax = belStatement;
        this.annotations = annotations;
        this.citation = citation;
        this.evidence = evidence;

        if (comment != null && comment.startsWith("//")) {
            comment = comment.substring(2);
        }

        this.comment = clean(comment);
    }

    public String getStatementSyntax() {
        return statementSyntax;
    }

    public List<BELAnnotation> getAnnotations() {
        return annotations;
    }

    public BELCitation getCitation() {
        return citation;
    }

    public BELEvidence getEvidence() {
        return evidence;
    }

    public String getComment() {
        return comment;
    }
}
