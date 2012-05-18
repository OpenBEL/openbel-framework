/**
 * Copyright (C) 2012 Selventa, Inc.
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
package org.openbel.framework.compiler;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.Strings.INVALID_SYMBOLS;
import static org.openbel.framework.common.enums.AnnotationType.ENUMERATION;
import static org.openbel.framework.common.model.CommonModelFactory.getInstance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.openbel.bel.model.BELParseErrorException;
import org.openbel.framework.common.AnnotationDefinitionResolutionException;
import org.openbel.framework.common.bel.parser.BELParseResults;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.core.BELConverterService;
import org.openbel.framework.core.BELValidatorService;
import org.openbel.framework.core.ConversionError;
import org.openbel.framework.core.XBELConverterService;
import org.openbel.framework.core.XBELValidatorService;
import org.openbel.framework.core.annotation.AnnotationDefinitionService;
import org.openbel.framework.core.annotation.AnnotationService;
import org.openbel.framework.core.compiler.ResourceSyntaxWarning;
import org.openbel.framework.core.compiler.SemanticFailure;
import org.openbel.framework.core.compiler.SemanticService;
import org.openbel.framework.core.compiler.SemanticWarning;
import org.openbel.framework.core.compiler.SymbolWarning;
import org.openbel.framework.core.compiler.ValidationError;
import org.openbel.framework.core.compiler.expansion.ExpansionService;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protonetwork.ProtoNetworkService;

/**
 * BEL compiler phase one implementation.
 */
public class PhaseOneImpl implements DefaultPhaseOne {
    private final XBELValidatorService validator;
    private final XBELConverterService converter;
    private final BELValidatorService belValidator;
    private final BELConverterService belConverter;
    private final NamespaceService namespace;
    private final SemanticService semantic;
    private final ExpansionService expansion;
    private final ProtoNetworkService protoNetwork;
    private final AnnotationService annotation;
    private final AnnotationDefinitionService annotationDefinition;

    /**
     * Creates a phase one implementation.
     *
     * @param xbelValidator {@link XBELValidatorService}, the xbel validator
     * service
     * @param xbelConverter {@link XBELConverterService}, the xbel converter
     * service
     * @param belValidator {@link BELValidatorService}, the bel validate service
     * @param belConverter {@link BELConverterService}, the bel converter
     * service
     * @param namespace {@link NamespaceService}, the namespace service
     * @param semantic {@link SemanticService}, the semantic service
     * @param expansion {@link ExpansionService}, the expansion service
     * @param protoNetwork {@link ProtoNetworkService}, the proto network
     * service
     * @param annotation {@link AnnotationService}, the annotation service
     * @param annotationDefinition {@link AnnotationDefinitionService}, the
     * annotation definition service
     */
    public PhaseOneImpl(final XBELValidatorService xbelValidator,
            final XBELConverterService xbelConverter,
            final BELValidatorService belValidator,
            final BELConverterService belConverter,
            final NamespaceService namespace,
            final SemanticService semantic,
            final ExpansionService expansion,
            final ProtoNetworkService protoNetwork,
            final AnnotationService annotation,
            final AnnotationDefinitionService annotationDefinition) {
        this.validator = xbelValidator;
        this.converter = xbelConverter;
        this.belValidator = belValidator;
        this.belConverter = belConverter;
        this.namespace = namespace;
        this.semantic = semantic;
        this.expansion = expansion;
        this.protoNetwork = protoNetwork;
        this.annotation = annotation;
        this.annotationDefinition = annotationDefinition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stage1Output stage1XBELValidation(final File file) {
        Stage1Output output = new Stage1Output();

        output.addValidationErrors(validator.validateWithErrors(file));
        if (output.hasValidationErrors()) {
            return output;
        }

        Document d = null;
        try {
            d = converter.toCommon(file);
            output.setDocument(d);
        } catch (JAXBException e) {
            final String name = file.toString();
            final String msg = e.getMessage();
            final Throwable cause = e;
            output.setConversionError(new ConversionError(name, msg, cause));
            return output;
        } catch (IOException e) {
            final String name = file.toString();
            final String msg = e.getMessage();
            final Throwable cause = e;
            output.setConversionError(new ConversionError(name, msg, cause));
            return output;
        }

        List<AnnotationDefinition> definitions = d.getDefinitions();
        if (definitions == null) {
            return output;
        }

        verifyAnnotations(output, d, definitions);
        return output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stage1Output stage1BELValidation(File f) {
        Stage1Output output = new Stage1Output();

        BELParseResults rslts = belValidator.validateBELScript(f);
        List<BELParseErrorException> sntxErrs = rslts.getSyntaxErrors();
        if (hasItems(sntxErrs)) {
            ValidationError ve;
            for (BELParseErrorException err : sntxErrs) {
                final String name = f.getAbsolutePath();
                final String msg = err.getMessage();
                final int line = err.getLine();
                final int col = err.getCharacter();
                ve = new ValidationError(name, msg, line, col);
                output.addValidationError(ve);
            }
            return output;
        }

        final Document d = belConverter.toCommon(rslts.getDocument());
        output.setDocument(d);

        List<AnnotationDefinition> definitions = d.getDefinitions();
        if (definitions == null) {
            return output;
        }

        verifyAnnotations(output, d, definitions);
        return output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stage2NamespaceCompilation(final Document doc)
            throws ResourceDownloadError, IndexingFailure {
        for (Namespace ns : doc.getAllNamespaces()) {
            String resourceLocation = ns.getResourceLocation();

            if (!namespace.isOpen(resourceLocation)) {
                namespace.compileNamespace(resourceLocation);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stage3SymbolVerification(final Document doc)
            throws SymbolWarning, IndexingFailure, ResourceDownloadError {

        final List<ResourceSyntaxWarning> exceptions =
                new ArrayList<ResourceSyntaxWarning>();

        try {
            namespace.verify(doc);
        } catch (SymbolWarning e) {
            exceptions.addAll(e.getResourceSyntaxWarnings());
        }

        if (exceptions.isEmpty()) {
            return;
        }

        final String name = doc.getName();
        final String fmt = INVALID_SYMBOLS;
        final String msg = format(fmt, exceptions.size());
        throw new SymbolWarning(name, msg, exceptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stage4SemanticVerification(final Document doc)
            throws SemanticFailure, IndexingFailure {

        // Linked lists are favored here to avoid array resizes
        final Set<Term> allDocTerms = doc.getAllTerms();
        final List<Statement> allDocStmts = doc.getAllStatements();

        List<SemanticWarning> failures = semanticCheck(allDocTerms);
        failures.addAll(semanticCheck(allDocStmts, doc));

        final int failureCount = failures.size();
        if (failureCount == 0) return;
        final StringBuilder bldr = new StringBuilder();
        bldr.append(failureCount);
        bldr.append(" semantic warning");
        if (failureCount > 1) bldr.append("s");
        bldr.append(" for document");
        throw new SemanticFailure(null, bldr.toString(), failures);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtoNetwork stage5Building(final Document doc) {
        return protoNetwork.compile(doc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stage6Expansion(final Document doc, final ProtoNetwork pn,
            boolean expandNestedStatements) {
        // expand statements, including whether nested statement should be
        // expanded
        expansion.expandStatements(doc, pn, expandNestedStatements);

        // expand terms if not disabled
        expansion.expandTerms(doc, pn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stage7Saving(final ProtoNetwork pn,
            final File path) throws ProtoNetworkError {
        protoNetwork.write(path.getAbsolutePath(), pn);
    }

    private void verifyAnnotations(Stage1Output output, Document d,
            List<AnnotationDefinition> definitions) {
        int size = definitions.size();
        List<AnnotationDefinition> resolved = sizedArrayList(size);
        for (final AnnotationDefinition annodef : definitions) {
            String url = annodef.getURL();
            if (url == null) {
                resolved.add(annodef);
                continue;
            }

            AnnotationDefinition ad;
            try {
                ad = annotationDefinition.resolveAnnotationDefinition(url);
                annodef.setType(ad.getType());
                annodef.setDescription(ad.getDescription());
                annodef.setUsage(ad.getUsage());
                annodef.setEnums(ad.getEnums());
                annodef.setValue(ad.getValue());

                resolved.add(annodef);
            } catch (AnnotationDefinitionResolutionException e) {
                ad = getInstance().createAnnotationDefinition(annodef.getId());
                ad.setType(ENUMERATION);
                List<String> enums = emptyList();
                ad.setEnums(enums);
                resolved.add(ad);
                output.addResolutionException(e);
            }
        }

        d.setDefinitions(resolved);
        d.resolveReferences();

        try {
            annotation.verify(output.getDocument());
        } catch (SymbolWarning sw) {
            output.setSymbolWarning(sw);
        }
    }

    private List<SemanticWarning> semanticCheck(
            final List<Statement> statements, final Document doc) {
        final List<SemanticWarning> ret = new LinkedList<SemanticWarning>();
        for (final Statement stmt : statements) {
            try {
                semantic.checkRelationship(stmt);
            } catch (SemanticWarning se) {
                ret.add(se);
            }
            try {
                semantic.checkListUsage(stmt, doc);
            } catch (SemanticWarning se) {
                ret.add(se);
            }
            try {
                semantic.checkNested(stmt);
            } catch (SemanticWarning se) {
                ret.add(se);
            }
            try {
                semantic.checkMultiNested(stmt, doc);
            } catch (SemanticWarning se) {
                ret.add(se);
            }
        }
        return ret;
    }

    private List<SemanticWarning> semanticCheck(final Set<Term> terms)
            throws IndexingFailure {
        final List<SemanticWarning> ret = new LinkedList<SemanticWarning>();

        // Signature verification
        for (final Term term : terms) {
            final List<Parameter> params = term.getParameters();

            // Method choice depends on whether a term has parameters or not

            if (hasItems(params)) {
                // Semantically check terms with parameters
                try {
                    semantic.checkParameterizedTerm(term);
                } catch (SemanticWarning se) {
                    ret.add(se);
                }
            } else {
                // Semantically check terms w/out parameters
                try {
                    semantic.checkTerm(term);
                } catch (SemanticWarning se) {
                    ret.add(se);
                }
            }
        }

        // Protein abundance subset verification
        for (final Term term : terms) {
            ret.addAll(semantic.checkAbundanceSubsets(term));
        }

        return ret;
    }
}
