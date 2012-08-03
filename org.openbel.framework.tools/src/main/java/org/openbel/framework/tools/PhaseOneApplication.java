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

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.isBELScript;
import static org.openbel.framework.common.Strings.BAD_INPUT_PATH;
import static org.openbel.framework.common.Strings.INPUT_FILE_UNREADABLE;
import static org.openbel.framework.common.Strings.NO_DOCUMENT_FILES;
import static org.openbel.framework.common.Strings.NO_XBEL_INPUT;
import static org.openbel.framework.common.Strings.PHASE1_STAGE1_HDR;
import static org.openbel.framework.common.Strings.PHASE1_STAGE2_HDR;
import static org.openbel.framework.common.Strings.PHASE1_STAGE3_HDR;
import static org.openbel.framework.common.Strings.PHASE1_STAGE4_HDR;
import static org.openbel.framework.common.Strings.PHASE1_STAGE5_HDR;
import static org.openbel.framework.common.Strings.PHASE1_STAGE6_HDR;
import static org.openbel.framework.common.Strings.PHASE1_STAGE7_HDR;
import static org.openbel.framework.common.Strings.PHASE_1_DISABLE_NESTED_STMTS;
import static org.openbel.framework.common.Strings.PHASE_1_INPUT_FILE;
import static org.openbel.framework.common.Strings.PHASE_1_INPUT_PATH;
import static org.openbel.framework.common.Strings.PHASE_1_NO_SEMANTICS;
import static org.openbel.framework.common.Strings.PHASE_1_NO_SYNTAX;
import static org.openbel.framework.common.Strings.SEMANTIC_CHECKS_DISABLED;
import static org.openbel.framework.common.Strings.SYMBOL_CHECKS_DISABLED;
import static org.openbel.framework.common.enums.ExitCode.NAMESPACE_RESOLUTION_FAILURE;
import static org.openbel.framework.common.enums.ExitCode.NO_VALID_DOCUMENTS;
import static org.openbel.framework.common.enums.ExitCode.PROTO_NETWORK_SAVE_FAILURE;
import static org.openbel.framework.common.enums.ExitCode.SEMANTIC_VERIFICATION_FAILURE;
import static org.openbel.framework.common.enums.ExitCode.STATEMENT_EXPANSION_FAILURE;
import static org.openbel.framework.common.enums.ExitCode.SYMBOL_VERIFICATION_FAILURE;
import static org.openbel.framework.common.enums.ExitCode.VALIDATION_FAILURE;
import static org.openbel.framework.tools.PhaseOneOptions.phaseOneOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.common.util.BELPathFilters.BELFileFilter;
import org.openbel.framework.common.util.BELPathFilters.XBELFileFilter;
import org.openbel.framework.compiler.DefaultPhaseOne;
import org.openbel.framework.compiler.DefaultPhaseOne.Stage1Output;
import org.openbel.framework.compiler.PhaseOneImpl;
import org.openbel.framework.core.BELConverterService;
import org.openbel.framework.core.BELConverterServiceImpl;
import org.openbel.framework.core.BELValidatorService;
import org.openbel.framework.core.BELValidatorServiceImpl;
import org.openbel.framework.core.XBELConverterService;
import org.openbel.framework.core.XBELValidatorService;
import org.openbel.framework.core.annotation.AnnotationDefinitionService;
import org.openbel.framework.core.annotation.AnnotationService;
import org.openbel.framework.core.annotation.DefaultAnnotationDefinitionService;
import org.openbel.framework.core.annotation.DefaultAnnotationService;
import org.openbel.framework.core.compiler.SemanticFailure;
import org.openbel.framework.core.compiler.SemanticService;
import org.openbel.framework.core.compiler.SemanticServiceImpl;
import org.openbel.framework.core.compiler.SymbolWarning;
import org.openbel.framework.core.compiler.ValidationError;
import org.openbel.framework.core.compiler.expansion.ExpansionService;
import org.openbel.framework.core.compiler.expansion.ExpansionServiceImpl;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheLookupService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.namespace.DefaultNamespaceService;
import org.openbel.framework.core.namespace.NamespaceIndexerService;
import org.openbel.framework.core.namespace.NamespaceIndexerServiceImpl;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protonetwork.ProtoNetworkBuilder;
import org.openbel.framework.core.protonetwork.ProtoNetworkService;
import org.openbel.framework.core.protonetwork.ProtoNetworkServiceImpl;
import org.openbel.framework.core.protonetwork.TextProtoNetworkExternalizer;

/**
 * BEL phase one compiler.
 */
public final class PhaseOneApplication extends PhaseApplication {
    private final DefaultPhaseOne p1;
    
    /** Phase one artifact directory. */
    public final static String DIR_ARTIFACT = "phaseI";

    private final static String SHORT_OPT_IN_PATH = "p";
    private final static String LONG_OPT_IN_PATH = "input-path";

    private final static String INFILE_SHORT_OPT = "f";
    private final static String INFILE_LONG_OPT = "file";

    private final static String NO_NS_LONG_OPT = "no-nested-statements";
    private final static String NO_SEMANTIC_CHECK = "no-semantic-check";
    private final static String NO_SYNTAX_CHECK = "no-syntax-check";
    
    private final static String NUM_PHASES = "7";

    /**
     * Phase one application constructor.
     *
     * @param args Command-line arguments
     */
    public PhaseOneApplication(String[] args) {
        super(args);
        final XBELValidatorService validator = createValidator();
        final XBELConverterService converter = createConverter();
        final BELValidatorService belValidator = new BELValidatorServiceImpl();
        final BELConverterService belConverter = new BELConverterServiceImpl();
        final NamespaceIndexerService nsindexer =
                new NamespaceIndexerServiceImpl();
        final CacheableResourceService cache =
                new DefaultCacheableResourceService();
        final CacheLookupService cacheLookup = new DefaultCacheLookupService();
        final NamespaceService nsService = new DefaultNamespaceService(
                cache, cacheLookup, nsindexer);
        final ProtoNetworkService protonetService =
                new ProtoNetworkServiceImpl();
        final SemanticService semantics = new SemanticServiceImpl(nsService);
        final ExpansionService expansion = new ExpansionServiceImpl();
        final AnnotationService annotationService =
                new DefaultAnnotationService();
        final AnnotationDefinitionService annotationDefinitionService =
                new DefaultAnnotationDefinitionService(cache, cacheLookup);
        
        p1 = new PhaseOneImpl(validator, converter,
                belValidator, belConverter, nsService, semantics,
                expansion, protonetService, annotationService,
                annotationDefinitionService);
    }

    /**
     * {@inheritDoc}
     */
    @Override     
    public void start() {
        super.start();

        if (hasOption(SHORT_OPT_IN_PATH)) {
            processInputDirectories();
        } else if (hasOption(INFILE_SHORT_OPT)) {
            processFiles();
        } else {
            error(NO_XBEL_INPUT);
            failUsage();
        }
    }

    /**
     * Process documents in the input directories.
     */
    private void processInputDirectories() {
        String[] inputdirs = getOptionValues(SHORT_OPT_IN_PATH);
        final List<File> files = new ArrayList<File>();
        final XBELFileFilter xbelFileFilter = new XBELFileFilter();
        final BELFileFilter belFileFilter = new BELFileFilter();

        for (String inputdir : inputdirs) {
            final File workingPath = new File(inputdir);
            if (!workingPath.canRead()) {
                error(BAD_INPUT_PATH + inputdir);
                failUsage();
            }

            final File[] xbels = workingPath.listFiles(xbelFileFilter);
            if (xbels != null && xbels.length != 0) {
                files.addAll(asList(xbels));
            }

            final File[] bels = workingPath.listFiles(belFileFilter);
            if (bels != null && bels.length != 0) {
                files.addAll(asList(bels));
            }
        }

        // Fail if none of the input directories contain BEL/XBEL files
        if (files.size() == 0) {
            error(NO_DOCUMENT_FILES);
            failUsage();
        }

        // Create the directory artifact or fail
        artifactPath = createDirectoryArtifact(outputDirectory, DIR_ARTIFACT);
        processFiles(files.toArray(new File[0]));
    }

    /**
     * Process file arguments.
     */
    private void processFiles() {
        String[] fileArgs = getOptionValues(INFILE_SHORT_OPT);
        final List<File> files = new ArrayList<File>(fileArgs.length);
        for (final String fileArg : fileArgs) {
            File file = new File(fileArg);
            if (!file.canRead()) {
                error(INPUT_FILE_UNREADABLE + file);
            } else {
                files.add(file);
            }
        }

        if (files.size() == 0) {
            error(NO_DOCUMENT_FILES);
            failUsage();
        }

        // Create the directory artifact or fail
        artifactPath = createDirectoryArtifact(outputDirectory, DIR_ARTIFACT);
        processFiles(files.toArray(new File[0]));
    }

    /**
     * Starts phase one compilation from {@link File XBEL files}.
     *
     * @param files the {@link File XBEL files} to compile
     */
    private void processFiles(File[] files) {
        phaseOutput(format("=== %s ===", getApplicationName()));
        phaseOutput(format("Compiling %d BEL Document(s)", files.length));

        boolean pedantic = withPedantic();
        boolean verbose = withVerbose();

        int validated = 0;

        for (int i = 1; i <= files.length; i++) {
            File file = files[i - 1];

            if (verbose) {
                phaseOutput("Compiling " + i + " of " + files.length
                        + " BEL Document(s)");
            }

            // validate document
            Document document = stage1(file);
            if (document == null) {
                if (pedantic) {
                    bail(VALIDATION_FAILURE);
                }
                continue;
            }
            validated++;

            // run stages 2 - 7
            runCommonStages(pedantic, document);
        }

        if (validated == 0) {
            bail(NO_VALID_DOCUMENTS);
        }
    }

    /**
     * Runs stages 2 - 7 which remain static regardless of the BEL document
     * input.
     *
     * @param pedantic the flag for pedantic-ness
     * @param document the {@link Document BEL document}
     */
    private void runCommonStages(boolean pedantic, Document document) {
        if (!stage2(document)) {
            if (pedantic) {
                bail(NAMESPACE_RESOLUTION_FAILURE);
            }
        }

        if (!stage3(document)) {
            if (pedantic) {
                bail(SYMBOL_VERIFICATION_FAILURE);
            }
        }

        if (!stage4(document)) {
            if (pedantic) {
                bail(SEMANTIC_VERIFICATION_FAILURE);
            }
        }

        ProtoNetwork pn = stage5(document);

        if (!stage6(document, pn)) {
            if (pedantic) {
                bail(STATEMENT_EXPANSION_FAILURE);
            }
        }

        if (!stage7(pn, document)) {
            if (pedantic) {
                bail(PROTO_NETWORK_SAVE_FAILURE);
            }
        }
    }

    /**
     * Stage one validation of the file, returning the converted document or
     * {@code null}.
     *
     * @param file XBEL file
     * @return Document
     */
    private Document stage1(final File file) {
        beginStage(PHASE1_STAGE1_HDR, "1", NUM_PHASES);
        final StringBuilder bldr = new StringBuilder();

        stageOutput("Validating " + file);
        long t1 = currentTimeMillis();

        final Stage1Output output;
        if (isBELScript(file)) {
            output = p1.stage1BELValidation(file);
        } else {
            output = p1.stage1XBELValidation(file);
        }

        if (output.hasValidationErrors()) {
            for (final ValidationError error : output.getValidationErrors()) {
                stageError(error.getUserFacingMessage());
            }
            return null;
        }

        if (output.hasConversionError()) {
            stageError(output.getConversionError().getUserFacingMessage());
            return null;
        }

        if (output.getSymbolWarning() != null) {
            stageWarning(output.getSymbolWarning().getUserFacingMessage());
        }

        long t2 = currentTimeMillis();
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());
        return output.getDocument();
    }

    /**
     * Stage two resolution of document namespaces.
     *
     * @param document BEL common document
     * @return boolean true if success, false otherwise
     */
    private boolean stage2(final Document document) {
        beginStage(PHASE1_STAGE2_HDR, "2", NUM_PHASES);
        final StringBuilder bldr = new StringBuilder();

        Collection<Namespace> namespaces = document.getNamespaceMap().values();
        final int docNSCount = namespaces.size();

        bldr.append("Compiling ");
        bldr.append(docNSCount);
        bldr.append(" namespace");
        if (docNSCount > 1) {
            bldr.append("s");
        }
        bldr.append(" for ");
        bldr.append(document.getName());
        stageOutput(bldr.toString());

        boolean success = true;
        long t1 = currentTimeMillis();
        try {
            p1.stage2NamespaceCompilation(document);
        } catch (ResourceDownloadError e) {
            success = false;
            stageError(e.getUserFacingMessage());
        } catch (IndexingFailure e) {
            success = false;
            stageError(e.getUserFacingMessage());
        }

        long t2 = currentTimeMillis();
        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());
        return success;
    }

    /**
     * Stage three symbol verification of the document.
     *
     * @param document BEL common document
     * @return boolean true if success, false otherwise
     */
    private boolean stage3(final Document document) {
        beginStage(PHASE1_STAGE3_HDR, "3", NUM_PHASES);
        final StringBuilder bldr = new StringBuilder();
        if (hasOption(NO_SYNTAX_CHECK)) {
            bldr.append(SYMBOL_CHECKS_DISABLED);
            markEndStage(bldr);
            stageOutput(bldr.toString());
            return true;
        }

        bldr.append("Verifying symbols in ");
        bldr.append(document.getName());
        stageOutput(bldr.toString());

        boolean warnings = false;
        long t1 = currentTimeMillis();
        try {
            p1.stage3SymbolVerification(document);
        } catch (SymbolWarning e) {
            warnings = true;
            String resname = e.getName();
            if (resname == null) {
                e.setName(document.getName());
            } else {
                e.setName(resname + " (" + document.getName() + ")");
            }
            stageWarning(e.getUserFacingMessage());
        } catch (IndexingFailure e) {
            stageError("Failed to open namespace index file for symbol verification.");
        } catch (ResourceDownloadError e) {
            stageError("Failed to resolve namespace during symbol verification.");
        }
        long t2 = currentTimeMillis();

        bldr.setLength(0);
        if (warnings) {
            bldr.append("Symbol verification resulted in warnings in ");
            bldr.append(document.getName());
            bldr.append("\n");
        }
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());

        if (warnings) {
            return false;
        }
        return true;
    }

    /**
     * Stage four semantic verification of the document.
     *
     * @param document BEL common document
     * @return boolean true if success, false otherwise
     */
    private boolean stage4(final Document document) {
        beginStage(PHASE1_STAGE4_HDR, "4", NUM_PHASES);
        final StringBuilder bldr = new StringBuilder();
        if (hasOption(NO_SEMANTIC_CHECK)) {
            bldr.append(SEMANTIC_CHECKS_DISABLED);
            markEndStage(bldr);
            stageOutput(bldr.toString());
            return true;
        }

        bldr.append("Verifying semantics in ");
        bldr.append(document.getName());
        stageOutput(bldr.toString());

        boolean warnings = false;
        long t1 = currentTimeMillis();
        try {
            p1.stage4SemanticVerification(document);
        } catch (SemanticFailure sf) {
            warnings = true;
            String resname = sf.getName();
            if (resname == null) {
                sf.setName(document.getName());
            } else {
                sf.setName(resname + " (" + document.getName() + ")");
            }
            stageWarning(sf.getUserFacingMessage());
        } catch (IndexingFailure e) {
            stageError("Failed to process namespace index files for semantic verification.");
        }
        long t2 = currentTimeMillis();

        bldr.setLength(0);
        if (warnings) {
            bldr.append("Semantic verification resulted in warnings in ");
            bldr.append(document.getName());
            bldr.append("\n");
        }
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());

        if (warnings) {
            return false;
        }
        return true;
    }

    /**
     * Stage five build of proto-network.
     *
     * @param document BEL common document
     */
    private ProtoNetwork stage5(final Document document) {
        beginStage(PHASE1_STAGE5_HDR, "5", NUM_PHASES);
        final StringBuilder bldr = new StringBuilder();

        bldr.append("Building proto-network for ");
        bldr.append(document.getName());
        stageOutput(bldr.toString());

        long t1 = currentTimeMillis();
        ProtoNetwork ret = p1.stage5Building(document);
        long t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());

        return ret;
    }

    /**
     * Stage six expansion of the document.
     *
     * @param doc the original {@link Document document} needed as a dependency
     * when using {@link ProtoNetworkBuilder proto network builder}
     * @param pn the {@link ProtoNetwork proto network} to expand into
     */
    public boolean stage6(final Document doc, final ProtoNetwork pn) {
        beginStage(PHASE1_STAGE6_HDR, "6", NUM_PHASES);
        final StringBuilder bldr = new StringBuilder();
        boolean stmtSuccess = false, termSuccess = false;

        bldr.append("Expanding statements and terms");
        stageOutput(bldr.toString());

        long t1 = currentTimeMillis();
        boolean stmtExpand = !hasOption(NO_NS_LONG_OPT);
        p1.stage6Expansion(doc, pn, stmtExpand);
        termSuccess = true;
        stmtSuccess = true;

        long t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());

        return stmtSuccess && termSuccess;
    }

    /**
     * Stage seven saving of proto-network.
     *
     * @param pn Proto-network
     * @param doc {@link Document}, the document
     * @return boolean true if success, false otherwise
     */
    private boolean stage7(final ProtoNetwork pn, final Document doc) {
        beginStage(PHASE1_STAGE7_HDR, "7", NUM_PHASES);
        final StringBuilder bldr = new StringBuilder();

        bldr.append("Saving proto-network for ");
        bldr.append(doc.getName());
        stageOutput(bldr.toString());

        long t1 = currentTimeMillis();
        String dir = artifactPath.getAbsolutePath();
        final String path = asPath(dir, nextUniqueFolderName());
        File pnpathname = new File(path);

        boolean success = true;
        try {
            p1.stage7Saving(pn, pnpathname);

            if (withDebug()) {
                TextProtoNetworkExternalizer textExternalizer =
                        new TextProtoNetworkExternalizer();
                textExternalizer.writeProtoNetwork(pn,
                        pnpathname.getAbsolutePath());
            }
        } catch (ProtoNetworkError e) {
            success = false;
            error("failed to save proto-network");
            e.printStackTrace();
        }
        long t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());

        return success;
    }

    private static int unique = 1;

    private static String nextUniqueFolderName() {
        return Integer.toString(unique++);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhaseOneOptions getPhaseConfiguration() {
        return phaseOneOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validCommandLine() {
        final CommandLineParser parser = new AntelopeParser();
        List<Option> myOpts = getCommandLineOptions();
        final Options options = new Options();
        for (final Option option : myOpts) {
            options.addOption(option);
        }

        CommandLine parse;
        try {
            parse = parser.parse(options, getCommandLineArguments(), false);
        } catch (ParseException e) {
            return false;
        }

        // Verify one form of valid input is present.

        if (parse.hasOption(SHORT_OPT_IN_PATH)) {
            return true;
        }

        if (parse.hasOption(INFILE_SHORT_OPT)) {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Option> getCommandLineOptions() {
        List<Option> options = super.getCommandLineOptions();

        String help;
        Option o;

        help = PHASE_1_INPUT_PATH;
        o = new Option(SHORT_OPT_IN_PATH, LONG_OPT_IN_PATH, true, help);
        o.setArgName("path");
        options.add(o);

        help = PHASE_1_INPUT_FILE;
        o = new Option(INFILE_SHORT_OPT, INFILE_LONG_OPT, true, help);
        o.setArgName("filename");
        options.add(o);

        help = PHASE_1_DISABLE_NESTED_STMTS;
        o = new Option(null, NO_NS_LONG_OPT, false, help);
        options.add(o);

        help = PHASE_1_NO_SEMANTICS;
        o = new Option(null, NO_SEMANTIC_CHECK, false, help);
        options.add(o);

        help = PHASE_1_NO_SYNTAX;
        o = new Option(null, NO_SYNTAX_CHECK, false, help);
        options.add(o);

        return options;
    }

    /**
     * Returns {@code "Phase I: Compiling proto-networks"}.
     *
     * @return String
     */
    @Override
    public String getApplicationName() {
        return "Phase I: Compiling proto-networks";
    }

    /**
     * Returns {@code "Phase I"}.
     *
     * @return String
     */
    @Override
    public String getApplicationShortName() {
        return "Phase I";
    }

    /**
     * Returns {@code "Compiles BEL documents into proto-networks."}.
     *
     * @return String
     */
    @Override
    public String getApplicationDescription() {
        return "Compiles BEL Documents into proto-networks.";
    }

    /**
     * Returns phase one usage.
     *
     * @return String
     */
    @Override
    public String getUsage() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("[OPTION]...");
        bldr.append(" [-p <path>]...");
        bldr.append(" [-f <filename>]...");
        return bldr.toString();
    }

    /**
     * Invokes {@link #harness(PhaseApplication)} for
     * {@link PhaseOneApplication}.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        harness(new PhaseOneApplication(args));
    }

    /**
     * {@inheritDoc}
     */
    public static String getRequiredArguments() {
        return "[-p <path>...] [-f <filename>...]";
    }
}
