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
import static java.util.Collections.emptySet;
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.createDirectories;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.Strings.EXPAND_NAMED_COMPLEXES_HELP;
import static org.openbel.framework.common.Strings.EXPAND_PROTEIN_FAMILIES_HELP;
import static org.openbel.framework.common.Strings.EXPECTED_ONE_NETWORK;
import static org.openbel.framework.common.Strings.GS_INJECTION_DISABLED;
import static org.openbel.framework.common.Strings.INJECTIONS_DISABLED;
import static org.openbel.framework.common.Strings.INPUT_FILE_UNREADABLE;
import static org.openbel.framework.common.Strings.NC_INJECTION_DISABLED;
import static org.openbel.framework.common.Strings.NOT_A_PHASE2_DIR;
import static org.openbel.framework.common.Strings.NO_GENE_SCAFFOLDING_HELP;
import static org.openbel.framework.common.Strings.NO_NAMED_COMPLEXES_HELP;
import static org.openbel.framework.common.Strings.NO_PHASE_THREE;
import static org.openbel.framework.common.Strings.NO_PROTEIN_FAMILIES_HELP;
import static org.openbel.framework.common.Strings.PF_INJECTION_DISABLED;
import static org.openbel.framework.common.Strings.PHASE3_STAGE1_HDR;
import static org.openbel.framework.common.Strings.PHASE3_STAGE2_HDR;
import static org.openbel.framework.common.Strings.PHASE3_STAGE3_HDR;
import static org.openbel.framework.common.Strings.PHASE3_STAGE4_HDR;
import static org.openbel.framework.common.Strings.PHASE3_STAGE5_HDR;
import static org.openbel.framework.common.Strings.PHASE3_STAGE6_HDR;
import static org.openbel.framework.common.Strings.PHASE4_NO_ORTHOLOGY_LONG_OPTION;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.common.enums.ExitCode.FAILED_TO_MERGE_PROTO_NETWORKS;
import static org.openbel.framework.common.enums.ExitCode.NO_CONVERTED_DOCUMENTS;
import static org.openbel.framework.common.enums.ExitCode.NO_PROTO_NETWORKS_SAVED;
import static org.openbel.framework.common.enums.ExitCode.NO_VALID_DOCUMENTS;
import static org.openbel.framework.core.df.cache.ResourceType.BEL;
import static org.openbel.framework.core.df.cache.ResourceType.fromLocation;
import static org.openbel.framework.tools.PhaseThreeOptions.phaseThreeOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.cli.Option;
import org.openbel.framework.common.Strings;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.common.index.ResourceLocation;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.EquivalenceDataIndex;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.common.util.BELPathFilters.GlobalProtonetworkFilter;
import org.openbel.framework.compiler.DefaultPhaseOne;
import org.openbel.framework.compiler.DefaultPhaseOne.Stage1Output;
import org.openbel.framework.compiler.DefaultPhaseThree;
import org.openbel.framework.compiler.DefaultPhaseThree.DocumentModificationResult;
import org.openbel.framework.compiler.DefaultPhaseTwo;
import org.openbel.framework.compiler.PhaseOneImpl;
import org.openbel.framework.compiler.PhaseThreeImpl;
import org.openbel.framework.compiler.PhaseTwoImpl;
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
import org.openbel.framework.core.compiler.SemanticService;
import org.openbel.framework.core.compiler.SemanticServiceImpl;
import org.openbel.framework.core.compiler.ValidationError;
import org.openbel.framework.core.compiler.expansion.ExpansionService;
import org.openbel.framework.core.compiler.expansion.ExpansionServiceImpl;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheLookupService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.openbel.framework.core.equivalence.EquivalenceIndexerService;
import org.openbel.framework.core.equivalence.EquivalenceIndexerServiceImpl;
import org.openbel.framework.core.equivalence.EquivalenceMapResolutionFailure;
import org.openbel.framework.core.namespace.DefaultNamespaceService;
import org.openbel.framework.core.namespace.NamespaceIndexerService;
import org.openbel.framework.core.namespace.NamespaceIndexerServiceImpl;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protonetwork.BinaryProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.BinaryProtoNetworkExternalizer;
import org.openbel.framework.core.protonetwork.ProtoNetworkExternalizer;
import org.openbel.framework.core.protonetwork.ProtoNetworkService;
import org.openbel.framework.core.protonetwork.ProtoNetworkServiceImpl;
import org.openbel.framework.core.protonetwork.TextProtoNetworkExternalizer;

/**
 * BEL phase three compiler.
 */
public final class PhaseThreeApplication extends PhaseApplication {
    private final DefaultPhaseThree p3;
    private final DefaultPhaseTwo p2;
    private final DefaultPhaseOne p1;
    private final CacheableResourceService cache;
    private final SystemConfiguration sysconfig;

    /* Phase-specific command-line options. */
    private final static String EXPAND_PF_LONG_OPT = "expand-protein-families";
    private final static String EXPAND_NC_LONG_OPT = "expand-named-complexes";
    private final static String NO_PF_LONG_OPT = "no-protein-families";
    private final static String NO_NC_LONG_OPT = "no-named-complexes";
    private final static String NO_GS_LONG_OPT = "no-gene-scaffolding";
    private final static String NO_P3_LONG_OPT = "no-phaseIII";

    /** Phase three artifact directory. */
    public final static String DIR_ARTIFACT = "phaseIII";
    /** Phase three pruned PF proto-network. */
    public final static String PRUNED_PF_NAME = "pruned-protein-families";
    /** Phase three pruned GS proto-network. */
    public final static String PRUNED_GS_NAME = "pruned-gene-scaffolding";
    /** Phase three pruned NC proto-network. */
    public final static String PRUNED_NC_NAME = "pruned-named-complexes";

    /** Intermediate output. */
    private final static String STAGE1_OUTPUT = "stage1";
    private final static String STAGE2_OUTPUT = "stage2";
    private final static String STAGE3_OUTPUT = "stage3";

    public final static String INJECTED_PF_NETWORK = "injected-protfam";
    public final static String INJECTED_GS_NETWORK = "injected-genescaff";

    private final static String NUM_PHASES = "6";

    /**
     * Phase three application constructor.
     *
     * @param args Command-line arguments
     */
    public PhaseThreeApplication(String[] args) {
        super(args);

        sysconfig = getSystemConfiguration();
        cache = new DefaultCacheableResourceService();

        final ProtoNetworkService protonetService =
                new ProtoNetworkServiceImpl();
        final EquivalenceIndexerService indexer =
                new EquivalenceIndexerServiceImpl();
        final PhaseTwoImpl phaseTwo =
                new PhaseTwoImpl(cache, indexer, protonetService);
        phaseTwo.setReportable(getReportable());

        p2 = phaseTwo;
        p3 = new PhaseThreeImpl(protonetService, p2);

        final XBELValidatorService validator = createValidator();
        final XBELConverterService converter = createConverter();
        final BELValidatorService belValidator = new BELValidatorServiceImpl();
        final BELConverterService belConverter = new BELConverterServiceImpl();
        final NamespaceIndexerService nsindexer =
                new NamespaceIndexerServiceImpl();
        final CacheLookupService cacheLookup = new DefaultCacheLookupService();
        final NamespaceService nsService = new DefaultNamespaceService(
                cache, cacheLookup, nsindexer);
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
     * Determine whether phase III will be skipped (such as with the --no-phaseIII
     * command line option).
     *
     * @return
     */
    public boolean isSkipped() {
        // --no-phaseIII is the same as:
        // --no-gene-scaffolding --no-named-complexes --no-protein-families --no-orthology
        return (hasOption(NO_P3_LONG_OPT) || (hasOption(NO_GS_LONG_OPT)
                && hasOption(NO_NC_LONG_OPT) && hasOption(NO_PF_LONG_OPT)
                && hasOption(PHASE4_NO_ORTHOLOGY_LONG_OPTION)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        super.start();

        PhaseThreeOptions phasecfg = getPhaseConfiguration();
        if (hasOption(EXPAND_NC_LONG_OPT)) {
            phasecfg.setExpandNamedComplexes(true);
        }
        if (hasOption(EXPAND_PF_LONG_OPT)) {
            phasecfg.setExpandProteinFamilies(true);
        }
        if (hasOption(NO_NC_LONG_OPT)) {
            phasecfg.setInjectNamedComplexes(false);
        }
        if (hasOption(NO_PF_LONG_OPT)) {
            phasecfg.setInjectProteinFamilies(false);
        }
        if (hasOption(NO_GS_LONG_OPT)) {
            phasecfg.setInjectGeneScaffolding(false);
        }
        if (hasOption(PHASE4_NO_ORTHOLOGY_LONG_OPTION)) {
            phasecfg.setInjectOrthology(false);
        }

        phaseOutput(format("=== %s ===", getApplicationName()));

        if (isSkipped()) {
            final StringBuilder bldr = new StringBuilder();
            bldr.append(getApplicationShortName());
            bldr.append(" has been skipped.");
            phaseOutput(bldr.toString());
            return;
        }

        // load the resource index for phase III use.
        String resourceIndexURL = sysconfig.getResourceIndexURL();
        try {
            final ResolvedResource resource = cache.resolveResource(
                    ResourceType.RESOURCE_INDEX, resourceIndexURL);
            ResourceIndex.INSTANCE.loadIndex(resource.getCacheResourceCopy());
        } catch (ResourceDownloadError e) {
            failIndex(phasecfg, e.getUserFacingMessage());
        } catch (FileNotFoundException e) {
            failIndex(phasecfg, e.getMessage());
        } catch (XMLStreamException e) {
            failIndex(phasecfg, e.getMessage());
        }

        processOutputDirectory();
    }

    /**
     * Logic to recover from a missing resource index.
     *
     * @param phasecfg
     * @param errorMessage
     */
    private void failIndex(PhaseThreeOptions phasecfg, String errorMessage) {
        stageError(errorMessage);
        final StringBuilder bldr = new StringBuilder();
        bldr.append("Could not find resource index file.");
        bldr.append("Expansion of protein families, named complexes, ");
        bldr.append("gene scaffolding, and orthology will not occur.");
        stageError(bldr.toString());
        ResourceIndex.INSTANCE.loadIndex();
        phasecfg.setInjectProteinFamilies(false);
        phasecfg.setInjectNamedComplexes(false);
        phasecfg.setInjectGeneScaffolding(false);
        phasecfg.setInjectOrthology(false);
    }

    /*
     * Processes the output directory.
     */
    private void processOutputDirectory() {
        final String root = outputDirectory.getAbsolutePath();
        final String leaf = PhaseTwoApplication.DIR_ARTIFACT;
        final String path = asPath(root, leaf);
        final File phaseIIPath = new File(path);

        // Fail if the working path doesn't contain a phase II artifact
        if (!phaseIIPath.isDirectory()) {
            error(NOT_A_PHASE2_DIR + ": " + phaseIIPath);
            failUsage();
        }

        // Fail if the working path doesn't contain any proto-networks
        final File[] networks =
                phaseIIPath.listFiles(new GlobalProtonetworkFilter());
        if (networks.length == 0) {
            final String err = Strings.NO_GLOBAL_PROTO_NETWORK;
            error(err + " in: " + phaseIIPath);
            failUsage();
        }

        // Fail if the working path contains more than one proto-network
        if (networks.length > 1) {
            error(EXPECTED_ONE_NETWORK + ", found " + networks.length);
            failUsage();
        }

        // Create the directory artifact or fail
        artifactPath = createDirectoryArtifact(outputDirectory, DIR_ARTIFACT);

        reconstituteNetwork(networks[0]);
    }

    /*
     * Reconstitutes the global proto-network.
     */
    private void reconstituteNetwork(final File file) {

        BinaryProtoNetworkDescriptor inputProtoNetworkDesc =
                new BinaryProtoNetworkDescriptor(file);
        ProtoNetworkExternalizer pne = new BinaryProtoNetworkExternalizer();
        ProtoNetwork global = null;

        try {
            global = pne.readProtoNetwork(inputProtoNetworkDesc);
        } catch (ProtoNetworkError e) {
            error(e.getUserFacingMessage());
            final ExitCode ec = ExitCode.NO_GLOBAL_PROTO_NETWORK;
            exit(ec);
        }

        processNetwork(global);
    }

    /**
     * Runs the phase three stages over the input proto-network.
     *
     * @param pn Proto-network input by user
     */
    private void processNetwork(final ProtoNetwork pn) {
        // Inject protein families
        ProtoNetwork pfamMerged = stage1(pn);
        // Inject named complexes
        ProtoNetwork ncMerged = stage2(pfamMerged);
        // Inject gene scaffolding
        ProtoNetwork geneMerged = stage3(ncMerged);
        // Inject homology knowledge
        ProtoNetwork orthoMerged = stage4(geneMerged);
        // Equivalence the network
        ProtoNetwork equived = stage5(orthoMerged);
        stage6(equived);
    }

    /**
     * Runs stage two protein family injection, if requested by the user.
     *
     * @param pn Proto-network input by user
     * @return Merged proto-network
     */
    private ProtoNetwork stage1(final ProtoNetwork pn) {
        beginStage(PHASE3_STAGE1_HDR, "1", NUM_PHASES);

        final StringBuilder bldr = new StringBuilder();

        // Load the pfam resource from the resource index.
        Index resourceIndex = ResourceIndex.INSTANCE.getIndex();
        ResourceLocation pfResource = resourceIndex.getProteinFamilyResource();

        if (pfResource == null || pfResource.getResourceLocation() == null) {
            getPhaseConfiguration().setInjectProteinFamilies(false);
            stageError("Resource location for protein family is not set in " +
                    "resource index, disabling protein family expansion.");
            markEndStage(bldr);
            return pn;
        }

        if (!withProteinFamilyInjection()) {
            bldr.append(PF_INJECTION_DISABLED);
            markEndStage(bldr);
            stageOutput(bldr.toString());
            return pn;
        }

        String pfLocation = pfResource.getResourceLocation();
        File pfamResource = null;
        final ResourceType pfamType = fromLocation(pfLocation);
        try {
            ResolvedResource resolvedResource =
                    cache.resolveResource(pfamType, pfLocation);
            pfamResource = resolvedResource.getCacheResourceCopy();
        } catch (ResourceDownloadError e) {
            return failProteinFamilies(pn, bldr, pfLocation,
                    e.getUserFacingMessage());
        }

        // Fail if the protein family file is not readable
        if (!pfamResource.canRead()) {
            stageError(INPUT_FILE_UNREADABLE + pfamResource);
            failUsage();
        }

        stageOutput("Processing protein families");
        long t1 = currentTimeMillis();
        final Stage1Output output;
        if (pfamType == BEL) {
            output = p1.stage1BELValidation(pfamResource);
        } else {
            output = p1.stage1XBELValidation(pfamResource);
        }

        if (output.hasValidationErrors()) {
            for (final ValidationError error : output.getValidationErrors()) {
                stageError(error.getUserFacingMessage());
            }
            bail(NO_VALID_DOCUMENTS);
            return pn; // Dead code
        }
        if (output.hasConversionError()) {
            stageError(output.getConversionError().getUserFacingMessage());
            bail(NO_CONVERTED_DOCUMENTS);
            return pn; // Dead code
        }
        if (output.getSymbolWarning() != null) {
            stageError(output.getSymbolWarning().getUserFacingMessage());
        }
        long t2 = currentTimeMillis();
        Document pfDoc = output.getDocument();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        stageOutput(bldr.toString());

        stageOutput("Pruning protein families");
        boolean expand = withProteinFamilyExpansion();
        t1 = currentTimeMillis();
        DocumentModificationResult r = p3.pruneFamilies(expand, pfDoc, pn);
        t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        stageOutput(bldr.toString());

        if (r.isFailure()) {
            for (final String error : r.getErrors()) {
                stageError(error);
            }
            return pn;
        }
        for (final String warning : r.getWarnings()) {
            stageError(warning);
        }

        if (r.getRemainingStatements() == 0) {
            bldr.setLength(0);
            bldr.append("No statements remain after pruning");
            markEndStage(bldr);
            stageOutput(bldr.toString());
            return pn;
        }

        bldr.setLength(0);
        bldr.append(r.getRemainingStatements());
        bldr.append(" of ");
        bldr.append(r.getTotalStatements());
        bldr.append(" statements remain after pruning");
        stageOutput(bldr.toString());

        stageOutput("Inferring protein family relationships");
        t1 = currentTimeMillis();
        r = p3.inferFamilies(pfDoc, pn);
        t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        stageOutput(bldr.toString());

        if (r.isFailure()) {
            for (final String error : r.getErrors()) {
                stageError(error);
            }
            return pn;
        }
        for (final String warning : r.getWarnings()) {
            stageError(warning);
        }

        bldr.setLength(0);
        if (r.getDeltaStatements() == 0) {
            bldr.append("No statements inferred for protein families");
        } else {
            bldr.append(r.getDeltaStatements());
            bldr.append(" of ");
            bldr.append(r.getTotalStatements());
            bldr.append(" statements inferred for protein families");
        }
        stageOutput(bldr.toString());

        stageOutput("Compiling pruned protein families");
        t1 = currentTimeMillis();
        ProtoNetwork pfpn = p3.compile(pfDoc);
        t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        stageOutput(bldr.toString());

        if (withDebug()) {
            final String rootpath = artifactPath.getAbsolutePath();
            final String pfpath = asPath(rootpath, PRUNED_PF_NAME);
            createDirectories(pfpath);

            try {
                TextProtoNetworkExternalizer textExternalizer =
                        new TextProtoNetworkExternalizer();
                textExternalizer.writeProtoNetwork(pfpn, pfpath);
            } catch (ProtoNetworkError e) {
                stageError(e.getUserFacingMessage());
            }
        }

        stageOutput("Merging proto-networks");
        t1 = currentTimeMillis();
        try {

            p3.merge(pn, pfpn);

            // Change the input proto-network descriptor to merged network
            ProtoNetworkExternalizer pne = new BinaryProtoNetworkExternalizer();
            final String artpath = artifactPath.getAbsolutePath();
            final String s1path = asPath(artpath, STAGE1_OUTPUT);
            createDirectoryArtifact(artifactPath, STAGE1_OUTPUT);
            pne.writeProtoNetwork(pn, s1path);

            // if debug, then save the text-based merged network
            if (withDebug()) {
                TextProtoNetworkExternalizer textExternalizer =
                        new TextProtoNetworkExternalizer();
                textExternalizer.writeProtoNetwork(pn, s1path);
            }
        } catch (ProtoNetworkError e) {
            stageError(e.getUserFacingMessage());
            bail(FAILED_TO_MERGE_PROTO_NETWORKS);
            return pn; // Dead code
        }
        t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());

        return pn;
    }

    /**
     * Logic to recover from a failed protein family document.
     *
     * @param pn
     * @param bldr
     * @param pfLocation
     * @param e
     * @return
     */
    private ProtoNetwork failProteinFamilies(final ProtoNetwork pn,
            final StringBuilder bldr, String pfLocation, String errorMessage) {
        bldr.append("PROTEIN FAMILY RESOLUTION FAILURE in ");
        bldr.append(pfLocation);
        bldr.append("\n\treason: ");
        bldr.append(errorMessage);
        stageWarning(bldr.toString());

        // could not resolve protein family resource so return original
        // proto network.
        return pn;
    }

    /**
     * Runs stage two named complexes injection, if requested by the user.
     *
     * @param pn Proto-network input by user
     * @return Merged proto-network
     */
    private ProtoNetwork stage2(final ProtoNetwork pn) {
        beginStage(PHASE3_STAGE2_HDR, "2", NUM_PHASES);

        final StringBuilder bldr = new StringBuilder();

        // Load the named complexes resource from the resource index.
        Index resourceIndex = ResourceIndex.INSTANCE.getIndex();
        ResourceLocation ncResource = resourceIndex.getNamedComplexesResource();

        if (ncResource == null || ncResource.getResourceLocation() == null) {
            getPhaseConfiguration().setInjectNamedComplexes(false);
            stageError("Resource location for named complexes is not set " +
                    "in resource index, disabling named complex expansion.");
            markEndStage(bldr);
            return pn;
        }

        if (!withNamedComplexInjection()) {
            bldr.append(NC_INJECTION_DISABLED);
            markEndStage(bldr);
            stageOutput(bldr.toString());
            return pn;
        }

        String ncLocation = ncResource.getResourceLocation();
        File ncFile = null;
        final ResourceType ncType = fromLocation(ncLocation);
        try {
            ResolvedResource resolvedResource = cache.resolveResource(
                    ncType, ncLocation);
            ncFile = resolvedResource.getCacheResourceCopy();
        } catch (ResourceDownloadError e) {
            return failNamedComplexes(pn, bldr, ncLocation,
                    e.getUserFacingMessage());
        }

        // Fail if the named complexes file is not readable
        if (!ncFile.canRead()) {
            stageError(INPUT_FILE_UNREADABLE + ncFile);
            failUsage();
        }

        stageOutput("Processing named complexes");
        long t1 = currentTimeMillis();

        final Stage1Output output;
        if (ncType == BEL) {
            output = p1.stage1BELValidation(ncFile);
        } else {
            output = p1.stage1XBELValidation(ncFile);
        }

        if (output.hasValidationErrors()) {
            for (final ValidationError error : output.getValidationErrors()) {
                stageError(error.getUserFacingMessage());
            }
            bail(NO_VALID_DOCUMENTS);
            return pn; // Dead code
        }
        if (output.hasConversionError()) {
            stageError(output.getConversionError().getUserFacingMessage());
            bail(NO_CONVERTED_DOCUMENTS);
            return pn; // Dead code
        }
        if (output.getSymbolWarning() != null) {
            stageError(output.getSymbolWarning().getUserFacingMessage());
        }
        long t2 = currentTimeMillis();
        Document ncDoc = output.getDocument();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        stageOutput(bldr.toString());

        stageOutput("Pruning named complexes");
        boolean expand = withNamedComplexExpansion();
        t1 = currentTimeMillis();
        DocumentModificationResult pr = p3.pruneComplexes(expand, ncDoc, pn);
        t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        stageOutput(bldr.toString());

        if (pr.isFailure()) {
            for (final String error : pr.getErrors()) {
                stageError(error);
            }
            return pn;
        }
        for (final String warning : pr.getWarnings()) {
            stageError(warning);
        }

        if (pr.getRemainingStatements() == 0) {
            bldr.setLength(0);
            bldr.append("No statements remain after pruning");
            markEndStage(bldr);
            stageOutput(bldr.toString());
            return pn;
        }

        bldr.setLength(0);
        bldr.append(pr.getRemainingStatements());
        bldr.append(" of ");
        bldr.append(pr.getTotalStatements());
        bldr.append(" statements remain after pruning");
        stageOutput(bldr.toString());

        stageOutput("Compiling named complexes proto-network");
        t1 = currentTimeMillis();
        ProtoNetwork ncpn = p3.compile(ncDoc);
        t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        stageOutput(bldr.toString());

        // if debug, then save named complexes proto network
        if (withDebug()) {
            final String rootpath = artifactPath.getAbsolutePath();
            final String ncpath = asPath(rootpath, PRUNED_NC_NAME);
            createDirectories(ncpath);

            try {
                TextProtoNetworkExternalizer textExternalizer =
                        new TextProtoNetworkExternalizer();
                textExternalizer.writeProtoNetwork(ncpn, ncpath);
            } catch (ProtoNetworkError e) {
                stageError(e.getUserFacingMessage());
            }
        }

        stageOutput("Merging proto-networks");

        t1 = currentTimeMillis();
        try {
            p3.merge(pn, ncpn);

            // Change the input proto-network descriptor to merged network
            ProtoNetworkExternalizer pne = new BinaryProtoNetworkExternalizer();
            final String artpath = artifactPath.getAbsolutePath();
            final String s2path = asPath(artpath, STAGE2_OUTPUT);
            createDirectoryArtifact(artifactPath, STAGE2_OUTPUT);
            pne.writeProtoNetwork(pn, s2path);

            // if debug, then save the text-based merged network
            if (withDebug()) {
                TextProtoNetworkExternalizer textExternalizer =
                        new TextProtoNetworkExternalizer();
                textExternalizer.writeProtoNetwork(pn, s2path);
            }
        } catch (ProtoNetworkError e) {
            stageError(e.getUserFacingMessage());
            bail(FAILED_TO_MERGE_PROTO_NETWORKS);
            return pn; // Dead code
        }
        t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());

        return pn;
    }

    /**
     * Logic to recover from a failure to resolve a named complexes resource.
     *
     * @param pn
     * @param bldr
     * @param ncLocation
     * @param errorMessage
     * @return
     */
    private ProtoNetwork failNamedComplexes(final ProtoNetwork pn,
            final StringBuilder bldr, String ncLocation, String errorMessage) {
        bldr.append("NAMED COMPLEXES RESOLUTION FAILURE in ");
        bldr.append(ncLocation);
        bldr.append("\n\treason: ");
        bldr.append(errorMessage);
        stageWarning(bldr.toString());

        // could not resolve named complexes resource so return original
        // proto network.
        return pn;
    }

    /**
     * Runs stage three gene scaffolding, if requested by the user.
     *
     * @param pn Proto-network input by user
     * @return Merged proto-network
     */
    private ProtoNetwork stage3(final ProtoNetwork pn) {
        beginStage(PHASE3_STAGE3_HDR, "3", NUM_PHASES);

        final StringBuilder bldr = new StringBuilder();

        // Load the gene scaffolding resource from the resource index.
        Index resourceIndex = ResourceIndex.INSTANCE.getIndex();
        ResourceLocation gsResource =
                resourceIndex.getGeneScaffoldingResource();

        if (gsResource == null || gsResource.getResourceLocation() == null) {
            getPhaseConfiguration().setInjectGeneScaffolding(false);
            bldr.append("Resource location for gene scaffolding is not set ");
            bldr.append("in resource index, disabling gene scaffolding ");
            bldr.append("expansion.");
            stageError(bldr.toString());
            markEndStage(bldr);
            return pn;
        }

        if (!withGeneScaffoldingInjection()) {
            bldr.append(GS_INJECTION_DISABLED);
            markEndStage(bldr);
            stageOutput(bldr.toString());
            return pn;
        }

        // TODO Timing for cache resolving.
        String gsloc = gsResource.getResourceLocation();
        File gsFile = null;
        final ResourceType gsType = fromLocation(gsloc);
        try {
            ResolvedResource resolvedResource = cache.resolveResource(gsType,
                    gsloc);
            gsFile = resolvedResource.getCacheResourceCopy();
        } catch (ResourceDownloadError e) {
            return failGeneScaffolding(pn, bldr, gsloc,
                    e.getUserFacingMessage());
        }

        // Fail if the gene scaffolding file is not readable
        if (!gsFile.canRead()) {
            stageError(INPUT_FILE_UNREADABLE + gsFile);
            failUsage();
        }

        stageOutput("Processing gene scaffolding");
        long t1 = currentTimeMillis();

        final Stage1Output output;
        if (gsType == BEL) {
            output = p1.stage1BELValidation(gsFile);
        } else {
            output = p1.stage1XBELValidation(gsFile);
        }

        if (output.hasValidationErrors()) {
            for (final ValidationError error : output.getValidationErrors()) {
                stageError(error.getUserFacingMessage());
            }
            bail(NO_VALID_DOCUMENTS);
            return pn; // Dead code
        }
        if (output.hasConversionError()) {
            stageError(output.getConversionError().getUserFacingMessage());
            bail(NO_CONVERTED_DOCUMENTS);
            return pn; // Dead code
        }
        if (output.getSymbolWarning() != null) {
            stageError(output.getSymbolWarning().getUserFacingMessage());
        }
        long t2 = currentTimeMillis();
        Document gsDoc = output.getDocument();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        stageOutput(bldr.toString());

        stageOutput("Pruning gene scaffolding");
        t1 = currentTimeMillis();
        DocumentModificationResult pr = p3.pruneGene(gsDoc, pn);
        t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        stageOutput(bldr.toString());

        if (pr.isFailure()) {
            for (final String error : pr.getErrors()) {
                stageError(error);
            }
        }
        for (final String warning : pr.getWarnings()) {
            stageError(warning);
        }

        if (pr.getRemainingStatements() == 0) {
            bldr.setLength(0);
            bldr.append("No statements remain after pruning");
            markEndStage(bldr);
            stageOutput(bldr.toString());
            return pn;
        }

        bldr.setLength(0);
        bldr.append(pr.getRemainingStatements());
        bldr.append(" of ");
        bldr.append(pr.getTotalStatements());
        bldr.append(" statements remain after pruning");
        stageOutput(bldr.toString());

        stageOutput("Compiling gene scaffolding proto-network");
        t1 = currentTimeMillis();
        ProtoNetwork gpn = p3.compile(gsDoc);
        t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        stageOutput(bldr.toString());

        // if debug, write out proto network for gene scaffolds
        if (withDebug()) {
            final String rootpath = artifactPath.getAbsolutePath();
            final String gpath = asPath(rootpath, PRUNED_NC_NAME);
            createDirectories(gpath);

            try {
                TextProtoNetworkExternalizer textExternalizer =
                        new TextProtoNetworkExternalizer();
                textExternalizer.writeProtoNetwork(gpn, gpath);
            } catch (ProtoNetworkError e) {
                stageError(e.getUserFacingMessage());
            }
        }

        stageOutput("Merging proto-networks");

        t1 = currentTimeMillis();
        try {
            p3.merge(pn, gpn);

            // Change the input proto-network descriptor to merged network
            ProtoNetworkExternalizer pne = new BinaryProtoNetworkExternalizer();
            final String artpath = artifactPath.getAbsolutePath();
            final String s3path = asPath(artpath, STAGE3_OUTPUT);
            createDirectoryArtifact(artifactPath, STAGE3_OUTPUT);
            pne.writeProtoNetwork(pn, s3path);

            // if debug, then save the text-based merged network
            if (withDebug()) {
                TextProtoNetworkExternalizer textExternalizer =
                        new TextProtoNetworkExternalizer();
                textExternalizer.writeProtoNetwork(pn, s3path);
            }
        } catch (ProtoNetworkError e) {
            stageError(e.getUserFacingMessage());
            bail(FAILED_TO_MERGE_PROTO_NETWORKS);
            return pn; // Dead code
        }
        t2 = currentTimeMillis();

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());

        return pn;
    }

    /**
     * Logic to recover from a failure to resolve the gene scaffolding resource.
     *
     * @param pn
     * @param bldr
     * @param gsLocation
     * @param errorMessage
     * @return
     */
    private ProtoNetwork failGeneScaffolding(final ProtoNetwork pn,
            final StringBuilder bldr, String gsLocation, String errorMessage) {
        bldr.append("GENE SCAFFOLDING RESOURCE RESOLUTION FAILURE in ");
        bldr.append(gsLocation);
        bldr.append("\n\treason: ");
        bldr.append(errorMessage);
        stageWarning(bldr.toString());

        // could not resolve gene scaffolding resource so return original
        // proto network.
        return pn;
    }

    /**
     * Runs stage four injecting of homology knowledge.
     *
     * @param pn {@link ProtoNetwork}
     * @return the {@link ProtoNetwork} with homology knowledge injected
     */
    private ProtoNetwork stage4(final ProtoNetwork pn) {
        beginStage(PHASE3_STAGE4_HDR, "4", NUM_PHASES);

        if (!getPhaseConfiguration().getInjectOrthology()) {
            final StringBuilder bldr = new StringBuilder();
            bldr.append(getApplicationShortName());
            bldr.append(" has been skipped.");
            phaseOutput(bldr.toString());
            return pn;
        }

        // create output directory for orthologized proto network
        artifactPath = createDirectoryArtifact(outputDirectory, DIR_ARTIFACT);

        final Index index = ResourceIndex.INSTANCE.getIndex();
        final Set<ResourceLocation> resources = index.getOrthologyResources();
        if (noItems(resources)) {
            phaseOutput("No orthology documents included.");
            return pn;
        }

        long t1 = currentTimeMillis();

        final Iterator<ResourceLocation> it = resources.iterator();

        final ResourceLocation first = it.next();
        final ProtoNetwork orthoMerge = pruneResource(pn, first);

        while (it.hasNext()) {
            final ResourceLocation resource = it.next();
            final ProtoNetwork opn = pruneResource(pn, resource);

            try {
                p3.merge(orthoMerge, opn);
            } catch (ProtoNetworkError e) {
                e.printStackTrace();
            }
        }

        try {
            runPhaseThree(orthoMerge);
        } catch (ProtoNetworkError e) {
            stageError(e.getUserFacingMessage());
            bail(ExitCode.GENERAL_FAILURE);
        }

        try {
            p3.merge(pn, orthoMerge);
        } catch (ProtoNetworkError e) {
            stageError(e.getUserFacingMessage());
            bail(ExitCode.GENERAL_FAILURE);
        }

        long t2 = currentTimeMillis();

        final StringBuilder bldr = new StringBuilder();
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());

        return pn;
    }

    private void runPhaseThree(final ProtoNetwork orthoPn)
            throws ProtoNetworkError {
        final Index resourceIndex = ResourceIndex.INSTANCE.getIndex();
        final ResourceLocation pfResource = resourceIndex
                .getProteinFamilyResource();

        final Document pfdoc = readResource(pfResource);
        p3.pruneFamilies(false, pfdoc, orthoPn);
        p3.inferFamilies(pfdoc, orthoPn);
        final ProtoNetwork pfpn = p3.compile(pfdoc);
        p3.merge(orthoPn, pfpn);

        final ResourceLocation ncResource = resourceIndex
                .getNamedComplexesResource();
        final Document ncdoc = readResource(ncResource);
        p3.pruneComplexes(false, ncdoc, orthoPn);
        final ProtoNetwork ncpn = p3.compile(ncdoc);
        p3.merge(orthoPn, ncpn);

        final ResourceLocation gsResource = resourceIndex
                .getGeneScaffoldingResource();
        final Document gsdoc = readResource(gsResource);
        p3.pruneGene(gsdoc, orthoPn);
        final ProtoNetwork gspn = p3.compile(gsdoc);
        p3.merge(orthoPn, gspn);

        equivalence(orthoPn);
    }

    private ProtoNetwork pruneResource(final ProtoNetwork pn,
            final ResourceLocation resource) {
        Document doc = readResource(resource);

        stageOutput(format("Processing orthology document '%s'", doc.getName()));
        p3.pruneOrthologyDocument(doc, pn);

        final ProtoNetwork opn = p3.compile(doc);
        return opn;
    }

    private Document readResource(final ResourceLocation resource) {
        final String rloc = resource.getResourceLocation();
        final ResourceType type = fromLocation(rloc);

        File res = null;
        try {
            final ResolvedResource rsv = cache.resolveResource(type, rloc);
            res = rsv.getCacheResourceCopy();
        } catch (ResourceDownloadError e) {
            e.printStackTrace();
            return null;
        }

        final Stage1Output output;
        if (type == BEL) {
            output = p1.stage1BELValidation(res);
        } else {
            output = p1.stage1XBELValidation(res);
        }

        if (output.hasValidationErrors()) {
            for (final ValidationError error : output.getValidationErrors()) {
                stageError(error.getUserFacingMessage());
            }
            bail(NO_VALID_DOCUMENTS);
            return null; // Dead code
        }
        if (output.hasConversionError()) {
            stageError(output.getConversionError().getUserFacingMessage());
            bail(NO_CONVERTED_DOCUMENTS);
            return null; // Dead code
        }
        if (output.getSymbolWarning() != null) {
            stageError(output.getSymbolWarning().getUserFacingMessage());
        }
        Document doc = output.getDocument();
        return doc;
    }

    /**
     * Runs equivalencing of the proto-network.
     *
     * @param pn Proto-network, post-scaffolding
     * @return Equivalenced proto-network
     */
    private ProtoNetwork equivalence(ProtoNetwork pn) {
        final StringBuilder bldr = new StringBuilder();

        // Load the equivalences
        final Set<EquivalenceDataIndex> equivalences;
        try {
            equivalences = p2.stage2LoadNamespaceEquivalences();
        } catch (EquivalenceMapResolutionFailure f) {
            // Unrecoverable error
            // TODO Real error handling
            f.printStackTrace();
            return null;
        }

        long t1 = currentTimeMillis();
        int pct = stage5Parameter(pn, equivalences, bldr);
        stage5Term(pn, pct);
        stage5Statement(pn, pct);

        long t2 = currentTimeMillis();

        final int paramct = pn.getParameterTable().getTableParameters().size();
        final int termct = pn.getTermTable().getTermValues().size();
        final int stmtct = pn.getStatementTable().getStatements().size();

        bldr.setLength(0);
        bldr.append(stmtct);
        bldr.append(" statements, ");
        bldr.append(termct);
        bldr.append(" terms, ");
        bldr.append(paramct);
        bldr.append(" parameters");
        stageOutput(bldr.toString());

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        return pn;
    }

    /**
     * Runs stage five equivalencing of the proto-network.
     *
     * @param pn Proto-network, post-scaffolding
     * @return Equivalenced proto-network
     */
    private ProtoNetwork stage5(ProtoNetwork pn) {
        beginStage(PHASE3_STAGE5_HDR, "5", NUM_PHASES);
        final StringBuilder bldr = new StringBuilder();

        if (!withGeneScaffoldingInjection() &&
                !withNamedComplexInjection() &&
                !withProteinFamilyInjection()) {
            bldr.append(INJECTIONS_DISABLED);
            markEndStage(bldr);
            stageOutput(bldr.toString());
            return pn;
        }

        // load equivalences
        Set<EquivalenceDataIndex> equivs;
        try {
            equivs = p2.stage2LoadNamespaceEquivalences();
        } catch (EquivalenceMapResolutionFailure e) {
            stageError(e.getUserFacingMessage());
            equivs = emptySet();
        }

        long t1 = currentTimeMillis();
        int pct = stage5Parameter(pn, equivs, bldr);
        stage5Term(pn, pct);
        stage5Statement(pn, pct);

        long t2 = currentTimeMillis();

        final int paramct = pn.getParameterTable().getTableParameters().size();
        final int termct = pn.getTermTable().getTermValues().size();
        final int stmtct = pn.getStatementTable().getStatements().size();

        bldr.setLength(0);
        bldr.append(stmtct);
        bldr.append(" statements, ");
        bldr.append(termct);
        bldr.append(" terms, ");
        bldr.append(paramct);
        bldr.append(" parameters");
        stageOutput(bldr.toString());

        bldr.setLength(0);
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());
        return pn;
    }

    /**
     * Stage five parameter equivalencing.
     *
     * @param network the {@link ProtoNetwork network} to equivalence
     * @param equivalences the {@link Set set} of {@link EquivalenceDataIndex}
     * @param bldr the {@link StringBuilder}
     * @return the {@code int} count of parameter equivalences
     */
    private int stage5Parameter(final ProtoNetwork network,
            Set<EquivalenceDataIndex> equivalences, final StringBuilder bldr) {
        bldr.append("Equivalencing parameters");
        stageOutput(bldr.toString());
        ProtoNetwork ret = network;
        int ct = 0;
        try {
            ct = p2.stage3EquivalenceParameters(ret, equivalences);
            stageOutput("(" + ct + " equivalences)");
        } catch (IOException ioex) {
            final String err = ioex.getMessage();
            fatal(err);
        }

        return ct;
    }

    /**
     * Stage five term equivalencing.
     *
     * @param network the {@link ProtoNetwork network} to equivalence
     * @param pct the parameter equivalencing count to control output
     */
    private void stage5Term(final ProtoNetwork network, int pct) {
        if (pct > 0) {
            stageOutput("Equivalencing terms");
            int tct = p2.stage3EquivalenceTerms(network);
            stageOutput("(" + tct + " equivalences)");
        } else {
            stageOutput("Skipping term equivalencing");
        }
    }

    /**
     * Stage five statement equivalencing.
     *
     * @param network the {@link ProtoNetwork network} to equivalence
     * @param pct the parameter equivalencing count to control output
     */
    private void stage5Statement(final ProtoNetwork network, int pct) {
        if (pct > 0) {
            stageOutput("Equivalencing statements");
            int sct = p2.stage3EquivalenceStatements(network);
            stageOutput("(" + sct + " equivalences)");
        } else {
            stageOutput("Skipping statement equivalencing");
        }
    }

    /**
     * Runs stage six network saving.
     *
     * @param pn Proto-network
     */
    private void stage6(ProtoNetwork pn) {
        beginStage(PHASE3_STAGE6_HDR, "6", NUM_PHASES);
        stageOutput("Saving augmented network");

        final String rootpath = artifactPath.getAbsolutePath();
        long t1 = currentTimeMillis();
        try {
            p3.write(rootpath, pn);

            if (withDebug()) {
                try {
                    TextProtoNetworkExternalizer textExternalizer =
                            new TextProtoNetworkExternalizer();
                    textExternalizer.writeProtoNetwork(pn,
                            rootpath);
                } catch (ProtoNetworkError e) {
                    error("Could not write out equivalenced proto network.");
                }
            }
        } catch (ProtoNetworkError e) {
            stageError(e.getUserFacingMessage());
            bail(NO_PROTO_NETWORKS_SAVED);
        }
        long t2 = currentTimeMillis();

        final StringBuilder bldr = new StringBuilder();
        markTime(bldr, t1, t2);
        markEndStage(bldr);
        stageOutput(bldr.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhaseThreeOptions getPhaseConfiguration() {
        return phaseThreeOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validCommandLine() {
        // We only need output from phase two. Any command-line is valid.
        return true;
    }

    /**
     * Returns
     * {@code "Phase III: Expansion and augmentation of composite network"}.
     *
     * @return String
     */
    @Override
    public String getApplicationName() {
        return "Phase III: Expansion and augmentation of composite network";
    }

    /**
     * Returns {@code "Phase III"}.
     *
     * @return String
     */
    @Override
    public String getApplicationShortName() {
        return "Phase III";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationDescription() {
        return "Performs expansion and augmentation of a merged composite network.";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("[OPTION]...");
        return bldr.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Option> getCommandLineOptions() {
        List<Option> ret = super.getCommandLineOptions();

        String help;

        help = EXPAND_PROTEIN_FAMILIES_HELP;
        ret.add(new Option(null, EXPAND_PF_LONG_OPT, false, help));

        help = EXPAND_NAMED_COMPLEXES_HELP;
        ret.add(new Option(null, EXPAND_NC_LONG_OPT, false, help));

        help = NO_PROTEIN_FAMILIES_HELP;
        ret.add(new Option(null, NO_PF_LONG_OPT, false, help));

        help = NO_NAMED_COMPLEXES_HELP;
        ret.add(new Option(null, NO_NC_LONG_OPT, false, help));

        help = NO_GENE_SCAFFOLDING_HELP;
        ret.add(new Option(null, NO_GS_LONG_OPT, false, help));

        help = NO_PHASE_THREE;
        ret.add(new Option(null, NO_P3_LONG_OPT, false, help));

        return ret;
    }

    /**
     * Returns {@link #getPhaseConfiguration()}
     * {@link PhaseThreeOptions#getExpandNamedComplexes()}, a delegate method
     * for convenience.
     *
     * @return boolean
     */
    private boolean withNamedComplexExpansion() {
        return getPhaseConfiguration().getExpandNamedComplexes();
    }

    /**
     * Returns {@link #getPhaseConfiguration()}
     * {@link PhaseThreeOptions#getExpandProteinFamilies()}, a delegate method
     * for convenience.
     *
     * @return boolean
     */
    private boolean withProteinFamilyExpansion() {
        return getPhaseConfiguration().getExpandProteinFamilies();
    }

    /**
     * Returns {@link #getPhaseConfiguration()}
     * {@link PhaseThreeOptions#getInjectNamedComplexes()}, a delegate method
     * for convenience.
     *
     * @return boolean
     */
    private boolean withNamedComplexInjection() {
        return getPhaseConfiguration().getInjectNamedComplexes();
    }

    /**
     * Returns {@link #getPhaseConfiguration()}
     * {@link PhaseThreeOptions#getInjectProteinFamilies()}, a delegate method
     * for convenience.
     *
     * @return boolean
     */
    private boolean withProteinFamilyInjection() {
        return getPhaseConfiguration().getInjectProteinFamilies();
    }

    /**
     * Returns {@link #getPhaseConfiguration()}
     * {@link PhaseThreeOptions#getInjectGeneScaffolding()}, a delegate method
     * for convenience.
     *
     * @return boolean
     */
    private boolean withGeneScaffoldingInjection() {
        return getPhaseConfiguration().getInjectGeneScaffolding();
    }

    /**
     * Invokes {@link #harness(PhaseApplication)} for
     * {@link PhaseThreeApplication}.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        harness(new PhaseThreeApplication(args));
    }

    /**
     * {@inheritDoc}
     */
    public static String getRequiredArguments() {
        // Nothing is required
        return "";
    }
}
