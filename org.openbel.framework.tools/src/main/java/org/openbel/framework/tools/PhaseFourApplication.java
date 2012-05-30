package org.openbel.framework.tools;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.createDirectories;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.BELUtilities.sizedHashMap;
import static org.openbel.framework.common.Strings.NOT_A_DIRECTORY;
import static org.openbel.framework.common.Strings.PHASE4_DESCRIPTION;
import static org.openbel.framework.common.Strings.PHASE4_NAME;
import static org.openbel.framework.common.Strings.PHASE4_NO_ORTHOLOGY_HELP;
import static org.openbel.framework.common.Strings.PHASE4_NO_ORTHOLOGY_LONG_OPTION;
import static org.openbel.framework.common.Strings.PHASE4_SHORT_NAME;
import static org.openbel.framework.common.Strings.PHASE4_STAGE1_HDR;
import static org.openbel.framework.common.Strings.PHASE4_STAGE2_HDR;
import static org.openbel.framework.common.Strings.PHASE4_STAGE3_HDR;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.common.enums.ExitCode.NO_CONVERTED_DOCUMENTS;
import static org.openbel.framework.common.enums.ExitCode.NO_PROTO_NETWORKS_SAVED;
import static org.openbel.framework.common.enums.ExitCode.NO_VALID_DOCUMENTS;
import static org.openbel.framework.core.df.cache.ResourceType.BEL;
import static org.openbel.framework.core.df.cache.ResourceType.fromLocation;
import static org.openbel.framework.tools.PhaseFourOptions.phaseFourOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.cli.Option;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.common.index.ResourceLocation;
import org.openbel.framework.common.model.DataFileIndex;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.EquivalenceDataIndex;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.common.util.BELPathFilters.GlobalProtonetworkFilter;
import org.openbel.framework.compiler.DefaultPhaseOne;
import org.openbel.framework.compiler.DefaultPhaseOne.Stage1Output;
import org.openbel.framework.compiler.DefaultPhaseThree;
import org.openbel.framework.compiler.DefaultPhaseTwo;
import org.openbel.framework.compiler.PhaseFourImpl;
import org.openbel.framework.compiler.PhaseOneImpl;
import org.openbel.framework.compiler.PhaseThreeImpl;
import org.openbel.framework.compiler.PhaseTwoImpl;
import org.openbel.framework.compiler.DefaultPhaseFour;
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
import org.openbel.framework.core.indexer.JDBMEquivalenceLookup;
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
 * {@link PhaseFourApplication} defines the BEL compiler phase four
 * {@link PhaseApplication phase application}.
 *
 * @author Anthony Bargnesi &lt;abargnesi@selventa.com&gt;
 */
public class PhaseFourApplication extends PhaseApplication {

    public final static String DIR_ARTIFACT = "phaseIV";
    private final static String NUM_PHASES = "3";
    private final DefaultPhaseOne p1;
    private final DefaultPhaseTwo p2;
    private final DefaultPhaseThree p3;
    private final DefaultPhaseFour p4;
    private final SystemConfiguration sysconfig;
    private final CacheableResourceService cache;
    private Set<EquivalenceDataIndex> eqindexes;
    private Map<String, JDBMEquivalenceLookup> eqlookups;

    /**
     * Construct the BEL compiler {@link PhaseApplication phase four}
     *
     * @param args {@code String[]} command-line arguments
     */
    public PhaseFourApplication(String[] args) {
        super(args);

        sysconfig = getSystemConfiguration();
        cache = new DefaultCacheableResourceService();

        final ProtoNetworkService pnsvc = new ProtoNetworkServiceImpl();
        final EquivalenceIndexerService indexer = new EquivalenceIndexerServiceImpl();
        final ExpansionService exsvc = new ExpansionServiceImpl();
        final XBELValidatorService validator = createValidator();
        final XBELConverterService converter = createConverter();
        final BELValidatorService belValidator = new BELValidatorServiceImpl();
        final BELConverterService belConverter = new BELConverterServiceImpl();
        final NamespaceIndexerService nsindexer = new NamespaceIndexerServiceImpl();
        final CacheLookupService cacheLookup = new DefaultCacheLookupService();
        final NamespaceService nsService = new DefaultNamespaceService(
                cache, cacheLookup, nsindexer);
        final SemanticService semantics = new SemanticServiceImpl(nsService);
        final ExpansionService expansion = new ExpansionServiceImpl();
        final AnnotationService annotationService = new DefaultAnnotationService();
        final AnnotationDefinitionService annotationDefinitionService =
                new DefaultAnnotationDefinitionService(cache, cacheLookup);
        
        p1 = new PhaseOneImpl(validator, converter,
                belValidator, belConverter, nsService, semantics,
                expansion, pnsvc, annotationService,
                annotationDefinitionService);

        final PhaseTwoImpl phaseTwo = new PhaseTwoImpl(cache, indexer, pnsvc);
        phaseTwo.setReportable(getReportable());
        p2 = phaseTwo;

        p3 = new PhaseThreeImpl(pnsvc, p2);

        p4 = new PhaseFourImpl(pnsvc, exsvc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        super.start();

        PhaseFourOptions options = getPhaseConfiguration();
        if (hasOption(PHASE4_NO_ORTHOLOGY_LONG_OPTION)) {
            options.setNoOrthology(true);
        }

        phaseOutput(format("=== %s ===", getApplicationName()));

        if (options.isNoOrthology()) {
            final StringBuilder bldr = new StringBuilder();
            bldr.append(getApplicationShortName());
            bldr.append(" has been skipped.");
            phaseOutput(bldr.toString());
            return;
        }

        // load the resource index for phase IV use.
        String resourceIndexURL = sysconfig.getResourceIndexURL();
        try {
            final ResolvedResource resource = cache.resolveResource(
                    ResourceType.RESOURCE_INDEX, resourceIndexURL);
            ResourceIndex.INSTANCE.loadIndex(resource.getCacheResourceCopy());
        } catch (ResourceDownloadError e) {
            failIndex(options, e.getUserFacingMessage());
        } catch (FileNotFoundException e) {
            failIndex(options, e.getMessage());
        } catch (XMLStreamException e) {
            failIndex(options, e.getMessage());
        }

        // create output directory for orthologized proto network
        artifactPath = createDirectoryArtifact(outputDirectory, DIR_ARTIFACT);

        final Index index = ResourceIndex.INSTANCE.getIndex();
        final Set<ResourceLocation> resources = index.getOrthologyResources();
        if (noItems(resources)) {
            phaseOutput("No orthology documents included.");
        } else {
            // open equivalences
            // XXX Should organize into a stage
            openEquivalences();

            // run stage 1
            final ProtoNetwork merged = stage1pruning(index.getOrthologyResources());

            if (withDebug()) {
                final String rootpath = artifactPath.getAbsolutePath();
                final String pfpath = asPath(rootpath, "merged");
                createDirectories(pfpath);

                try {
                    TextProtoNetworkExternalizer textExternalizer =
                            new TextProtoNetworkExternalizer();
                    textExternalizer.writeProtoNetwork(merged, pfpath);
                } catch (ProtoNetworkError e) {
                    stageError(e.getUserFacingMessage());
                }
            }

            // run stage 2
            stage2equivalencing(merged);

            if (withDebug()) {
                final String rootpath = artifactPath.getAbsolutePath();
                final String pfpath = asPath(rootpath, "equivalenced");
                createDirectories(pfpath);

                try {
                    TextProtoNetworkExternalizer textExternalizer =
                            new TextProtoNetworkExternalizer();
                    textExternalizer.writeProtoNetwork(merged, pfpath);
                } catch (ProtoNetworkError e) {
                    stageError(e.getUserFacingMessage());
                }
            }

            // run stage 3
            stage3writing(merged);

            // close equivalences
            closeEquivalences();
        }
    }

    public ProtoNetwork stage1pruning(final Set<ResourceLocation> resources) {
        beginStage(PHASE4_STAGE1_HDR, "1", NUM_PHASES);
        long t1 = currentTimeMillis();

        final ProtoNetwork pn = reconstituteNetwork();

        int i = 1;
        final Iterator<ResourceLocation> it = resources.iterator();

        final ResourceLocation first = it.next();
        final ProtoNetwork orthoMerge = pruneResource(pn, first);
        
        while (it.hasNext()) {
            final ResourceLocation resource = it.next();
            final ProtoNetwork opn = pruneResource(pn, resource);

            if (withDebug()) {
                final String rootpath = artifactPath.getAbsolutePath();
                final String pfpath = asPath(rootpath, "pruned"+(i++));
                createDirectories(pfpath);

                try {
                    TextProtoNetworkExternalizer textExternalizer =
                            new TextProtoNetworkExternalizer();
                    textExternalizer.writeProtoNetwork(opn, pfpath);
                } catch (ProtoNetworkError e) {
                    stageError(e.getUserFacingMessage());
                }
            }

            try {
                p4.merge(orthoMerge, opn);
            } catch (ProtoNetworkError e) {
                e.printStackTrace();
            }
        }
        
        if (withDebug()) {
            final String rootpath = artifactPath.getAbsolutePath();
            final String pfpath = asPath(rootpath, "ortho-merge");
            createDirectories(pfpath);

            try {
                TextProtoNetworkExternalizer textExternalizer =
                        new TextProtoNetworkExternalizer();
                textExternalizer.writeProtoNetwork(orthoMerge, pfpath);
            } catch (ProtoNetworkError e) {
                stageError(e.getUserFacingMessage());
            }
        }
        
        try {
            runPhaseThree(orthoMerge);
        } catch (ProtoNetworkError e) {
            stageError(e.getUserFacingMessage());
            bail(ExitCode.GENERAL_FAILURE);
        }

        try {
            p4.merge(pn, orthoMerge);
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
        final ProtoNetwork pfpn = p4.compile(pfdoc);
        p4.merge(orthoPn, pfpn);
        
        final ResourceLocation ncResource = resourceIndex
                .getNamedComplexesResource();
        final Document ncdoc = readResource(ncResource);
        p3.pruneComplexes(false, ncdoc, orthoPn);
        final ProtoNetwork ncpn = p4.compile(ncdoc);
        p4.merge(orthoPn, ncpn);
        
        final ResourceLocation gsResource = resourceIndex
                .getGeneScaffoldingResource();
        final Document gsdoc = readResource(gsResource);
        p3.pruneGene(gsdoc, orthoPn);
        final ProtoNetwork gspn = p4.compile(gsdoc);
        p4.merge(orthoPn, gspn);
     
        equivalence(orthoPn);
    }

    private ProtoNetwork pruneResource(final ProtoNetwork pn,
            final ResourceLocation resource) {
        Document doc = readResource(resource);

        stageOutput(format("Processing orthology document '%s'", doc.getName()));
        p4.pruneOrthologyDocument(doc, pn, eqlookups);

        final ProtoNetwork opn = p4.compile(doc);
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

    public void stage2equivalencing(final ProtoNetwork pn) {
        beginStage(PHASE4_STAGE2_HDR, "2", NUM_PHASES);

        equivalence(pn);

        final StringBuilder bldr = new StringBuilder();
        markEndStage(bldr);
        stageOutput(bldr.toString());
    }

    public void stage3writing(final ProtoNetwork pn) {
        beginStage(PHASE4_STAGE3_HDR, "3", NUM_PHASES);

        long t1 = currentTimeMillis();
        final String rootpath = artifactPath.getAbsolutePath();
        try {
            p4.write(rootpath, pn);
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
     * Open all {@link JDBMEquivalenceLookup equivalences} for BEL Framework
     * namespaces.  The intention is to set up
     * {@link PhaseFourApplication#eqindexes} and
     * {@link PhaseFourApplication#eqlookups} fields.
     */
    private void openEquivalences() {
        // Load the equivalences
        try {
            eqindexes = p2.stage2LoadNamespaceEquivalences();
        } catch (EquivalenceMapResolutionFailure f) {
            // Unrecoverable error
            // TODO Real error handling
            f.printStackTrace();
            return;
        }

        // Map namespace to lookup
        eqlookups = sizedHashMap(eqindexes.size());
        for (final EquivalenceDataIndex edi : eqindexes) {
            String rl = edi.getNamespaceResourceLocation();
            DataFileIndex dfi = edi.getEquivalenceIndex();
            eqlookups.put(rl, new JDBMEquivalenceLookup(dfi.getIndexPath()));
        }

        // Open the indices
        for (final JDBMEquivalenceLookup jl : eqlookups.values()) {
            try {
                jl.open();
            } catch (IOException e) {
                // TODO Real error handling
                e.printStackTrace();
                return;
            }
        }
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
        int pct = stage4Parameter(pn, equivalences, bldr);
        stage4Term(pn, pct);
        stage4Statement(pn, pct);

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
     * Closes all {@link JDBMEquivalenceLookup equivalence lookups} referenced
     * in {@link PhaseFourApplication#eqlookups}.
     */
    private void closeEquivalences() {
        if (hasItems(eqlookups)) {
            Collection<JDBMEquivalenceLookup> lookups = eqlookups.values();
            for (final JDBMEquivalenceLookup l : lookups) {
                try {
                    l.close();
                } catch (IOException e) {
                    // TODO Real error handling
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Stage four parameter equivalencing.
     *
     * @param network the {@link ProtoNetwork network} to equivalence
     * @param equivalences the {@link Set set} of {@link EquivalenceDataIndex}
     * @param bldr the {@link StringBuilder}
     * @return the {@code int} count of parameter equivalences
     */
    private int stage4Parameter(final ProtoNetwork network,
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
     * Stage four term equivalencing.
     *
     * @param network the {@link ProtoNetwork network} to equivalence
     * @param pct the parameter equivalencing count to control output
     */
    private void stage4Term(final ProtoNetwork network, int pct) {
        if (pct > 0) {
            stageOutput("Equivalencing terms");
            int tct = p2.stage3EquivalenceTerms(network);
            stageOutput("(" + tct + " equivalences)");
        } else {
            stageOutput("Skipping term equivalencing");
        }
    }

    /**
     * Stage four statement equivalencing.
     *
     * @param network the {@link ProtoNetwork network} to equivalence
     * @param pct the parameter equivalencing count to control output
     */
    private void stage4Statement(final ProtoNetwork network, int pct) {
        if (pct > 0) {
            stageOutput("Equivalencing statements");
            int sct = p2.stage3EquivalenceStatements(network);
            stageOutput("(" + sct + " equivalences)");
        } else {
            stageOutput("Skipping statement equivalencing");
        }
    }

    /**
     * Determine whether phase IV orthologization will be skipped (such as
     * with the --no-orthology command line option).
     *
     * @return {@code true} if <tt>--no-orthology</tt> is set, {@code false}
     * otherwise
     */
    public boolean isNoOrthology() {
        return hasOption(PHASE4_NO_ORTHOLOGY_LONG_OPTION);
    }

    private ProtoNetwork reconstituteNetwork() {
        final String outputPath = outputDirectory.getAbsolutePath();
        final PhaseThreeApplication p3App =
                new PhaseThreeApplication(getCommandLineArguments());
        final String folder = (p3App.isSkipped() ? PhaseTwoApplication.DIR_ARTIFACT :
            PhaseThreeApplication.DIR_ARTIFACT);

        final File pnPath = new File(asPath(outputPath, folder));

        // Fail if path is not a directory
        if (!pnPath.isDirectory()) {
            error(NOT_A_DIRECTORY + ": " + pnPath);
            failUsage();
        }

        final File[] files = pnPath.listFiles(new GlobalProtonetworkFilter());
        if (files.length == 0 || files.length > 1) {
            bail(ExitCode.NO_GLOBAL_PROTO_NETWORK);
        }

        final File pnf = files[0];
        final BinaryProtoNetworkDescriptor inputProtoNetworkDesc =
                new BinaryProtoNetworkDescriptor(pnf);
        final ProtoNetworkExternalizer pne = new BinaryProtoNetworkExternalizer();

        ProtoNetwork global = null;
        try {
            global = pne.readProtoNetwork(inputProtoNetworkDesc);
        } catch (ProtoNetworkError e) {
            error(e.getUserFacingMessage());
            final ExitCode ec = ExitCode.NO_GLOBAL_PROTO_NETWORK;
            exit(ec);
        }

        return global;
    }

    /**
     * Logic to recover from a missing resource index.
     *
     * XXX Copied from PhaseThreeApplication!
     */
    private void failIndex(PhaseFourOptions phasecfg, String errorMessage) {
        stageError(errorMessage);
        final StringBuilder bldr = new StringBuilder();
        bldr.append("Could not find resource index file.");
        bldr.append("Orthology information will not be added.");
        stageError(bldr.toString());
        ResourceIndex.INSTANCE.loadIndex();
        phasecfg.setNoOrthology(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationShortName() {
        return PHASE4_SHORT_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationName() {
        return PHASE4_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationDescription() {
        return PHASE4_DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhaseFourOptions getPhaseConfiguration() {
        return phaseFourOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validCommandLine() {
        return true;
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
        final List<Option> ret = super.getCommandLineOptions();

        ret.add(new Option(null, PHASE4_NO_ORTHOLOGY_LONG_OPTION, false,
                PHASE4_NO_ORTHOLOGY_HELP));
        return ret;
    }

    /**
     * Invokes {@link #harness(PhaseApplication)} for
     * {@link PhaseFourApplication}.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        harness(new PhaseFourApplication(args));
    }
}
