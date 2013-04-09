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
package org.openbel.framework.core.compiler;

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.core.annotation.AnnotationService;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * Service to handle validation of statements.
 *
 * @author James McMahon
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class DefaultValidationService implements
        ValidationService {

    private final NamespaceService nsService;
    private final SemanticService semantics;
    private final AnnotationService annoService;

    public DefaultValidationService(
            final NamespaceService nsService,
            final SemanticService semantics,
            final AnnotationService annoService) {
        this.nsService = nsService;
        this.semantics = semantics;
        this.annoService = annoService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationResult validate(Document doc) {
        ValidationResult validationResult =
                new ValidationResult(new ArrayList<String>(),
                        new ArrayList<String>());

        // note: this doesn't call validate(statement) for performance reasons

        // XXX verify(doc) *must* be invoked prior to semantic check to ensure
        // namespace index is open.
        try {
            nsService.verify(doc);
        } catch (SymbolWarning e) {
            for (ResourceSyntaxWarning rsw : e.getResourceSyntaxWarnings()) {
                validationResult.getWarnings().add(rsw.getUserFacingMessage());
            }
        } catch (ResourceDownloadError rde) {
            validationResult.getErrors().add(rde.getUserFacingMessage());
        } catch (IndexingFailure idxf) {
            validationResult.getErrors().add(idxf.getUserFacingMessage());
        }

        List<SemanticWarning> semanticWarnings = semanticCheck(doc.getAllTerms());
        semanticWarnings.addAll(semanticCheck(doc.getAllStatements()));
        for (SemanticWarning sw : semanticWarnings) {
            validationResult.getWarnings().add(sw.getUserFacingMessage());
        }

        try {
            annoService.verify(doc);
        } catch (SymbolWarning sw) {
            for (ResourceSyntaxWarning rsw : sw.getResourceSyntaxWarnings()) {
                validationResult.getWarnings().add(rsw.getUserFacingMessage());
            }
        }

        return validationResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationResult validate(Statement statement) {
        ValidationResult validationResult =
                new ValidationResult(new ArrayList<String>(),
                        new ArrayList<String>());

        // XXX verify(statement) *must* be invoked prior to semantic check to
        // ensure namespace index is open.
        try {
            nsService.verify(statement);
        } catch (SymbolWarning e) {
            for (ResourceSyntaxWarning rsw : e.getResourceSyntaxWarnings()) {
                validationResult.getWarnings().add(rsw.getUserFacingMessage());
            }
        } catch (ResourceDownloadError rde) {
            validationResult.getErrors().add(rde.getUserFacingMessage());
        } catch (IndexingFailure idxf) {
            validationResult.getErrors().add(idxf.getUserFacingMessage());
        }

        List<SemanticWarning> semanticWarnings = semanticCheck(statement
                .getAllTerms());
        semanticWarnings.addAll(semanticCheck(statement));
        for (SemanticWarning sw : semanticWarnings) {
            validationResult.getWarnings().add(sw.getUserFacingMessage());
        }

        try {
            annoService.verify(statement);
        } catch (SymbolWarning sw) {
            for (ResourceSyntaxWarning rsw : sw.getResourceSyntaxWarnings()) {
                validationResult.getWarnings().add(rsw.getUserFacingMessage());
            }
        }

        return validationResult;
    }

    private List<SemanticWarning> semanticCheck(
            final List<Statement> statements) {
        final List<SemanticWarning> ret = new LinkedList<SemanticWarning>();
        for (final Statement stmt : statements) {
            ret.addAll(semanticCheck(stmt));
        }
        return ret;
    }

    private List<SemanticWarning> semanticCheck(final Statement stmt) {
        final List<SemanticWarning> ret = new LinkedList<SemanticWarning>();
        try {
            semantics.checkRelationship(stmt);
        } catch (SemanticWarning se) {
            ret.add(se);
        }
        try {
            semantics.checkNested(stmt);
        } catch (SemanticWarning se) {
            ret.add(se);
        }
        return ret;
    }

    private List<SemanticWarning> semanticCheck(
            final Collection<Term> terms) {
        final List<SemanticWarning> ret = new LinkedList<SemanticWarning>();

        // Signature verification
        for (final Term term : terms) {
            final List<Parameter> params = term.getParameters();

            // Method choice depends on whether a term has parameters or not

            if (hasItems(params)) {
                // Semantically check terms with parameters
                for (final Parameter p : params) {
                    Namespace ns = p.getNamespace();
                    if (ns == null) {
                        continue;
                    }
                    try {
                        semantics.checkParameterizedTerm(term);
                    } catch (SemanticWarning se) {
                        ret.add(se);
                    }
                }
            } else {
                // Semantically check terms w/out parameters
                try {
                    semantics.checkTerm(term);
                } catch (SemanticWarning se) {
                    ret.add(se);
                }
            }
        }

        // Protein abundance subset verification
        for (final Term term : terms) {
            ret.addAll(semantics.checkAbundanceSubsets(term));
        }
        return ret;
    }
}
