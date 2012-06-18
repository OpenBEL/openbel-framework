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
package org.openbel.framework.tools;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.isBELDocument;
import static org.openbel.framework.common.BELUtilities.isBELScript;
import static org.openbel.framework.common.BELUtilities.isXBEL;
import static org.openbel.framework.common.BELUtilities.readable;
import static org.openbel.framework.common.Strings.*;
import static org.openbel.framework.core.StandardOptions.ARG_SYSCFG;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_SYSCFG;
import static org.openbel.framework.core.StandardOptions.SHRT_OPT_SYSCFG;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.Option;
import org.openbel.bel.model.BELParseErrorException;
import org.openbel.framework.common.AnnotationDefinitionResolutionException;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.Strings;
import org.openbel.framework.common.bel.parser.BELParseResults;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.core.BELConverterService;
import org.openbel.framework.core.BELConverterServiceImpl;
import org.openbel.framework.core.BELValidatorService;
import org.openbel.framework.core.BELValidatorServiceImpl;
import org.openbel.framework.core.CommandLineApplication;
import org.openbel.framework.core.StandardOptions;
import org.openbel.framework.core.XBELConverterService;
import org.openbel.framework.core.XBELConverterServiceImpl;
import org.openbel.framework.core.XBELValidatorService;
import org.openbel.framework.core.XBELValidatorServiceImpl;
import org.openbel.framework.core.annotation.AnnotationDefinitionService;
import org.openbel.framework.core.annotation.AnnotationService;
import org.openbel.framework.core.annotation.DefaultAnnotationDefinitionService;
import org.openbel.framework.core.annotation.DefaultAnnotationService;
import org.openbel.framework.core.compiler.DefaultValidationService;
import org.openbel.framework.core.compiler.SemanticService;
import org.openbel.framework.core.compiler.SemanticServiceImpl;
import org.openbel.framework.core.compiler.ValidationError;
import org.openbel.framework.core.compiler.ValidationResult;
import org.openbel.framework.core.compiler.ValidationService;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheLookupService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.namespace.DefaultNamespaceService;
import org.openbel.framework.core.namespace.NamespaceIndexerService;
import org.openbel.framework.core.namespace.NamespaceIndexerServiceImpl;
import org.openbel.framework.core.namespace.NamespaceService;
import org.xml.sax.SAXException;

/**
 * BelCheck is a {@link CommandLineApplication} to validate the syntax and
 * semantics of a BEL or XBEL document.
 *
 * @author Steve Ungerer
 */
public class BelCheck extends CommandLineApplication {

    private static final String BEL_CHECK_APP_NAME = "BelCheck";
    private static final String BEL_CHECK_DESC =
            "Validates BEL and XBEL documents.";

    private static final String PEDANTIC_OPTION = "pedantic";
    private static final String PERMISSIVE_OPTION = "permissive";
    private static final String QUIET_OPTION = "q";
    private static final String SUMMARY_OPTION = "summary";

    private final XBELValidatorService validator;
    private final XBELConverterService converter;
    private final BELConverterService belConverter;
    private final BELValidatorService belValidator;
    private final AnnotationDefinitionService annoDefService;
    private final ValidationService validationService;

    public BelCheck(String[] args) {
        super(args);

        final SimpleOutput reportable = new SimpleOutput();
        reportable.setErrorStream(System.err);
        reportable.setOutputStream(System.out);
        setReportable(reportable);

        printApplicationInfo("BEL Check Utility");

        initializeSystemConfiguration();

        validator = createValidator();
        converter = createConverter();
        belValidator = new BELValidatorServiceImpl();
        belConverter = new BELConverterServiceImpl();
        final CacheableResourceService cache =
                new DefaultCacheableResourceService();
        final CacheLookupService cacheLookup = new DefaultCacheLookupService();
        annoDefService = new DefaultAnnotationDefinitionService(
                cache, cacheLookup);
        final NamespaceIndexerService nsindexer =
                new NamespaceIndexerServiceImpl();
        final NamespaceService nsService = new DefaultNamespaceService(
                cache, cacheLookup, nsindexer);
        final SemanticService semantics = new SemanticServiceImpl(nsService);
        final AnnotationService annotationService =
                new DefaultAnnotationService();
        validationService = new DefaultValidationService(
                nsService, semantics, annotationService);

        boolean pedantic = hasOption(PEDANTIC_OPTION);
        boolean permissive = hasOption(PERMISSIVE_OPTION);
        boolean verbose = hasOption(StandardOptions.LONG_OPT_VERBOSE);
        boolean quiet = hasOption(QUIET_OPTION);
        boolean summary = hasOption(SUMMARY_OPTION);

        if (pedantic && permissive) {
            fatal(CHECK_PEDANTIC_PERMISSIVE_ERROR);
        }

        List<String> fileArgs = getExtraneousArguments();
        if (!hasItems(fileArgs)) {
            reportable.error(NO_DOCUMENT_FILES);
            end();
        }
        if (fileArgs.size() > 1) {
            fatal("Only a single document can be specified.");
        }

        String fileArg = fileArgs.get(0);

        if (!isBELDocument(fileArg)) {
            final String error = fileArg + " is not a BEL document.";
            reportable.error(error);
            bail(ExitCode.GENERAL_FAILURE);
            return;
        }

        File file = new File(fileArg);
        if (!readable(file)) {
            final String error = Strings.INPUT_FILE_UNREADABLE + file;
            reportable.error(error);
            bail(ExitCode.GENERAL_FAILURE);
            return;
        }

        try {
            final String abspath = file.getAbsolutePath();

            if (verbose) {
                reportable.output("Validating BEL Document: " + abspath);
            }

            int numWarnings = 0;
            int numErrors = 0;

            final Document document;
            if (isXBEL(file)) {
                List<ValidationError> validationErrors = validator
                        .validateWithErrors(file);

                // if validation errors exist then report and fail document
                if (hasItems(validationErrors)) {
                    numErrors += validationErrors.size();
                    if (!quiet) {
                        reportValidationError(validationErrors);
                    }
                    if (summary) {
                        printSummary(fileArg, numWarnings, numErrors);
                    }
                    bail(ExitCode.VALIDATION_FAILURE);
                    return;
                }

                document = converter.toCommon(file);
            } else if (isBELScript(file)) {
                List<ValidationError> validationErrors =
                        new ArrayList<ValidationError>();
                BELParseResults results = belValidator
                        .validateBELScript(file);

                // if validation errors exist then report and fail document
                if (hasItems(results.getSyntaxErrors())) {
                    for (BELParseErrorException syntaxError : results
                            .getSyntaxErrors()) {
                        validationErrors.add(new ValidationError(file
                                .getAbsolutePath(), syntaxError
                                .getMessage(),
                                syntaxError.getLine(), syntaxError
                                        .getCharacter()));
                    }
                    numErrors += validationErrors.size();

                    if (!quiet) {
                        reportValidationError(validationErrors);
                    }
                    if (summary) {
                        printSummary(fileArg, numWarnings, numErrors);
                    }
                    bail(ExitCode.VALIDATION_FAILURE);
                    return;
                }

                document = belConverter.toCommon(results.getDocument());
            } else {
                // unreachable; checked with isBelDocument
                fatal("Unsupported document type");
                return;
            }

            int numAnnoDefErrors =
                    validateAnnotationDefinitions(document, quiet, permissive,
                            verbose);
            if (permissive) {
                numWarnings += numAnnoDefErrors;
            } else {
                numErrors += numAnnoDefErrors;
            }

            ValidationResult vr = validationService.validate(document);
            for (String e : vr.getErrors()) {
                if (permissive) {
                    numWarnings++;
                    if (!quiet) {
                        reportable.warning("VALIDATION WARNING: " + e);
                    }
                } else {
                    numErrors++;
                    if (!quiet) {
                        reportable.error("VALIDATION ERROR: " + e);
                    }
                }
            }

            for (String w : vr.getWarnings()) {
                if (pedantic) {
                    numErrors++;
                    if (!quiet) {
                        reportable.error("VALIDATION ERROR: " + w);
                    }
                } else {
                    numWarnings++;
                    if (!quiet) {
                        reportable.warning("VALIDATION WARNING: " + w);
                    }
                }
            }

            if (summary) {
                printSummary(fileArg, numWarnings, numErrors);
            }
            if (numErrors == 0) {
                reportable.output(ALL_DOCUMENTS_PASSED_VALIDATION);
                bail(ExitCode.SUCCESS);
            } else {
                bail(ExitCode.VALIDATION_FAILURE);
            }
        } catch (Exception e) {
            reportable.error("Failed to import BEL Document.");
            reportable.error("Reason: " + e.getMessage());
            bail(ExitCode.GENERAL_FAILURE);
        }
    }

    private void reportValidationError(List<ValidationError> validationErrors) {
        if (hasItems(validationErrors)) {
            for (ValidationError validationError : validationErrors) {
                reportable.error(validationError.getUserFacingMessage());
            }
        }
    }

    private int validateAnnotationDefinitions(Document document, boolean quiet,
            boolean permissive, boolean verbose) {
        int adFailures = 0;
        if (hasItems(document.getDefinitions())) {
            for (final AnnotationDefinition annodef : document.getDefinitions()) {
                String url = annodef.getURL();
                if (url == null) {
                    continue;
                }

                try {
                    if (verbose) {
                        reportable.output("Resolving Annotation Definition: "
                                + url);
                    }
                    AnnotationDefinition external =
                            annoDefService.resolveAnnotationDefinition(url);
                    annodef.setDescription(external.getDescription());
                    annodef.setEnums(external.getEnums());
                    annodef.setType(external.getType());
                    annodef.setURL(external.getURL());
                    annodef.setUsage(external.getUsage());
                    annodef.setValue(external.getValue());
                } catch (AnnotationDefinitionResolutionException e) {
                    adFailures++;
                    if (!quiet) {
                        if (permissive) {
                            reportable.warning(e.getUserFacingMessage());
                        } else {
                            reportable.error(e.getUserFacingMessage());
                        }
                    }
                }
            }
        }

        return adFailures;
    }

    private void printSummary(String file, int numWarnings, int numErrors) {
        reportable.output("---------------------------------------");
        reportable.output("BelCheck results for " + file);
        reportable.output(numWarnings + " Warnings");
        reportable.output(numErrors + " Errors");
        reportable.output("---------------------------------------");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationName() {
        return BEL_CHECK_APP_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationShortName() {
        // Can't abbreviate 'BelCheck' much more
        return getApplicationName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationDescription() {
        return BEL_CHECK_DESC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        StringBuilder bldr = new StringBuilder();
        bldr.append(" [FILE]");
        return bldr.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Option> getCommandLineOptions() {
        final List<Option> ret = new LinkedList<Option>();

        ret.add(new Option(null, PEDANTIC_OPTION, false, CHECK_PEDANTIC));
        ret.add(new Option(null, PERMISSIVE_OPTION, false, CHECK_PERMISSIVE));
        ret.add(new Option(QUIET_OPTION, "quiet", false, CHECK_QUIET));
        ret.add(new Option(null, SUMMARY_OPTION, false, CHECK_SUMMARY));

        Option o =
                new Option(SHRT_OPT_SYSCFG, LONG_OPT_SYSCFG, true,
                        SYSTEM_CONFIG_PATH);
        o.setArgName(ARG_SYSCFG);
        ret.add(o);

        return ret;
    }

    public static void main(String... args) {
        BelCheck xi = new BelCheck(args);
        xi.end();
    }

    private XBELValidatorService createValidator() {
        try {
            return new XBELValidatorServiceImpl();
        } catch (SAXException e) {
            fatal("SAX exception creating validator service");
        } catch (MalformedURLException e) {
            fatal("Malformed URL exception creating validator service");
        } catch (IOException e) {
            fatal("IO exception creating validator service");
        } catch (URISyntaxException e) {
            fatal("URL syntax exception creating validator service");
        }
        return null;
    }

    private XBELConverterService createConverter() {
        try {
            return new XBELConverterServiceImpl();
        } catch (SAXException e) {
            fatal("SAX exception creating converter service");
        } catch (MalformedURLException e) {
            fatal("Malformed URL excpetion creating converter service");
        } catch (URISyntaxException e) {
            fatal("URI Syntax excpetion creating converter service");
        } catch (IOException e) {
            fatal("IO exception creating converter service");
        } catch (JAXBException e) {
            fatal("JAXB exception creating converter service");
        }
        return null;
    }
}
