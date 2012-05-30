package org.openbel.framework.tools;

import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.Strings.KAM_DESCRIPTION_HELP;
import static org.openbel.framework.common.Strings.KAM_NAME_HELP;
import static org.openbel.framework.common.Strings.MISSING_KAM_DESCRIPTION;
import static org.openbel.framework.common.Strings.MISSING_KAM_NAME;
import static org.openbel.framework.common.Strings.NOT_A_DIRECTORY;
import static org.openbel.framework.common.Strings.NO_PRESERVE_HELP;
import static org.openbel.framework.common.Strings.PHASE5_STAGE1_HDR;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.tools.PhaseFiveOptions.phaseFiveOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openbel.framework.common.DBConnectionFailure;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.common.index.Index;
import org.openbel.framework.common.index.ResourceIndex;
import org.openbel.framework.common.index.ResourceLocation;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.common.util.BELPathFilters.GlobalProtonetworkFilter;
import org.openbel.framework.compiler.DefaultPhaseFive;
import org.openbel.framework.compiler.PhaseFiveImpl;
import org.openbel.framework.compiler.kam.KAMStoreSchemaService;
import org.openbel.framework.compiler.kam.KAMStoreSchemaServiceImpl;
import org.openbel.framework.core.compiler.CreateKAMFailure;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.DatabaseError;
import org.openbel.framework.core.df.DatabaseService;
import org.openbel.framework.core.df.DatabaseServiceImpl;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.df.cache.ResolvedResource;
import org.openbel.framework.core.df.cache.ResourceType;
import org.openbel.framework.core.kam.KAMCatalogFailure;
import org.openbel.framework.core.protocol.ResourceDownloadError;
import org.openbel.framework.core.protonetwork.BinaryProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.BinaryProtoNetworkExternalizer;
import org.openbel.framework.core.protonetwork.ProtoNetworkDescriptor;
import org.openbel.framework.internal.KamDbObject;

/**
 * BEL phase five compiler.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public final class PhaseFiveApplication extends PhaseApplication {
    private final static String SHORT_OPT_KAM_NAME = "k";
    private final static String LONG_OPT_KAM_NAME = "kam-name";
    private final static String SHORT_OPT_KAM_DESCRIPTION = "d";
    private final static String LONG_OPT_KAM_DESCRIPTION = "kam-description";
    private final static String LONG_OPT_NO_PRESERVE = "no-preserve";
    private final DefaultPhaseFive p5;
    private final SystemConfiguration sysconfig;
    private final CacheableResourceService cacheService;

    private final static String numPhases = "1";

    /**
     * Holds the value of the KAM name option.
     */
    private String kamName;

    /**
     * Holds the value of the KAM description option.
     */
    private String kamDescription;

    /**
     * Phase five application constructor.
     *
     * @param args Command-line arguments
     */
    public PhaseFiveApplication(String[] args) {
        super(args);

        sysconfig = getSystemConfiguration();
        cacheService = new DefaultCacheableResourceService();
        DatabaseService dbservice = new DatabaseServiceImpl();
        KAMStoreSchemaService kamStoreSchemaService = new KAMStoreSchemaServiceImpl(
                dbservice);
        p5 = new PhaseFiveImpl(dbservice, kamStoreSchemaService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        super.start();

        // Fail if no KAM name is available
        kamName = getOptionValue(SHORT_OPT_KAM_NAME);
        if (kamName == null) {
            kamName = getPhaseConfiguration().getKAMName();
            if (kamName == null) {
                error(MISSING_KAM_NAME);
                failUsage();
            }
        }

        // Fail if no KAM description is available
        kamDescription = getOptionValue(SHORT_OPT_KAM_DESCRIPTION);
        if (kamDescription == null) {
            kamDescription = getPhaseConfiguration().getKAMDescription();
            if (kamDescription == null) {
                error(MISSING_KAM_DESCRIPTION);
                failUsage();
            }
        }

        processOutputDirectory();
    }

    /**
     * Process output directory.
     */
    private void processOutputDirectory() {

        // Phase V may have to use the proto network output of phase II if
        // phase III is skipped.

        final String outputPath = outputDirectory.getAbsolutePath();
        final String[] args = getCommandLineArguments();
        final PhaseFourApplication p4App = new PhaseFourApplication(args);

        String folder;

        // if phase IV was skipped or no orthology documents were merged
        if (p4App.isNoOrthology() || emptyOrthology()) {
            final PhaseThreeApplication p3App =
                    new PhaseThreeApplication(getCommandLineArguments());

            if (p3App.isSkipped()) {
                folder = PhaseTwoApplication.DIR_ARTIFACT;
            } else {
                folder = PhaseThreeApplication.DIR_ARTIFACT;
            }
        } else {
            folder = PhaseFourApplication.DIR_ARTIFACT;
        }

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

        processFiles(files[0]);
    }

    /**
     * Returns {@code true} if there are no orthology documents referenced in
     * the {@link ResourceIndex resource index} or if the resource index file
     * cannot be found.  A {@code false} is returned if at least one orthology
     * document is referenced in the {@link ResourceIndex resource index}.
     *
     * @return {@code boolean}
     */
    private boolean emptyOrthology() {
        // load the resource index to find orthology documents
        String resourceIndexURL = sysconfig.getResourceIndexURL();
        try {
            final ResolvedResource resource = cacheService.resolveResource(
                    ResourceType.RESOURCE_INDEX, resourceIndexURL);
            ResourceIndex.INSTANCE.loadIndex(resource.getCacheResourceCopy());
        } catch (ResourceDownloadError e) {
            stageWarning("Could not find resource index file.");
            return true;
        } catch (FileNotFoundException e) {
            stageWarning("Could not find resource index file.");
            return true;
        } catch (XMLStreamException e) {
            stageWarning("Could not find resource index file.");
            return true;
        }

        final Index index = ResourceIndex.INSTANCE.getIndex();
        final Set<ResourceLocation> orthologies = index.getOrthologyResources();
        return noItems(orthologies);
    }

    /**
     * Starts phase five creation of KAM.
     *
     * @param protoNetwork {@link File}, the proto network file
     */

    private void processFiles(File protoNetwork) {
        // final StringBuilder bldr = new StringBuilder();
        // bldr.append("Executing ");
        // bldr.append(getApplicationName());
        // bldr.append(" of ");
        // bldr.append(protoNetwork);
        // phaseOutput(bldr.toString());

        phaseOutput(format("=== %s ===", getApplicationName()));

        ProtoNetworkDescriptor pnd = new BinaryProtoNetworkDescriptor(
                protoNetwork);

        BinaryProtoNetworkExternalizer bpne =
                new BinaryProtoNetworkExternalizer();
        ProtoNetwork gpn = null;
        try {
            gpn = bpne.readProtoNetwork(pnd);
        } catch (ProtoNetworkError e) {
            error("Unable to read merged network.");
            exit(ExitCode.NO_GLOBAL_PROTO_NETWORK);
        }

        stage1(gpn);
    }

    /**
     * Stage one connection to KAM store.
     *
     * @return {@link DBConnection}, the KAM DB connection
     */
    private void stage1(ProtoNetwork gpn) {
        beginStage(PHASE5_STAGE1_HDR, "1", numPhases);
        final StringBuilder bldr = new StringBuilder();

        final String kamURL = sysconfig.getKamURL();
        final String kamUser = sysconfig.getKamUser();
        final String kamPass = sysconfig.getKamPassword();

        // stageOutput("Building KAM at - " + kamURL);
        DBConnection dbc = null;
        try {
            dbc = p5.stage1ConnectKAMStore(kamURL, kamUser, kamPass);
        } catch (DBConnectionFailure e) {
            e.printStackTrace();
            stageError(e.getUserFacingMessage());
            exit(ExitCode.KAM_CONNECTION_FAILURE);
        }

        KamDbObject kamDb = new KamDbObject(null, kamName, kamDescription,
                new Date(), null);

        String kamSchemaName = null;
        try {
            kamSchemaName = p5.stage2SaveToKAMCatalog(kamDb);
        } catch (KAMCatalogFailure e) {
            e.printStackTrace();
            stageError(e.getUserFacingMessage());
            exit(ExitCode.KAM_CONNECTION_FAILURE);
        }

        try {
            p5.stage3CreateKAMstore(dbc, kamSchemaName);
        } catch (CreateKAMFailure e) {
            e.printStackTrace();
            stageError("Unable to build KAM store - " + e.getMessage());
            exit(ExitCode.KAM_CONNECTION_FAILURE);
        }

        try {
            p5.stage4LoadKAM(dbc, gpn, kamSchemaName);
        } catch (DatabaseError e) {
            stageError("Unable to load KAM.");
            stageError(e.getUserFacingMessage());
            exit(ExitCode.KAM_LOAD_FAILURE);
        } catch (CreateKAMFailure e) {
            stageError("Unable to build KAM store - " + e.getMessage());
            exit(ExitCode.KAM_CONNECTION_FAILURE);
        }

        markEndStage(bldr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhaseFiveOptions getPhaseConfiguration() {
        return phaseFiveOptions();
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

        // Verify KAM name and description are present.

        if (!parse.hasOption(SHORT_OPT_KAM_NAME)) {
            if (getPhaseConfiguration().getKAMName() == null) {
                return false;
            }
        }

        if (!parse.hasOption(SHORT_OPT_KAM_DESCRIPTION)) {
            if (getPhaseConfiguration().getKAMDescription() == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Option> getCommandLineOptions() {
        List<Option> options = super.getCommandLineOptions();

        String help;
        Option o;

        help = KAM_NAME_HELP;
        o = new Option(SHORT_OPT_KAM_NAME, LONG_OPT_KAM_NAME, true, help);
        o.setArgName("kam");
        options.add(o);

        help = KAM_DESCRIPTION_HELP;
        o = new Option(SHORT_OPT_KAM_DESCRIPTION, LONG_OPT_KAM_DESCRIPTION,
                true, help);
        o.setArgName("description");
        options.add(o);

        help = NO_PRESERVE_HELP;
        o = new Option(null, LONG_OPT_NO_PRESERVE, false, help);
        options.add(o);

        return options;
    }

    /**
     * Returns {@code "Phase V: Exporting final network to the KAM store"}.
     *
     * @return String
     */
    @Override
    public String getApplicationName() {
        return "Phase V: Exporting final network to the KAM Store";
    }

    /**
     * Returns {@code "Phase V"}.
     *
     * @return String
     */
    @Override
    public String getApplicationShortName() {
        return "Phase V";
    }

    /**
     * Returns {@code "Compiles the global proto-network into a KAM."}.
     *
     * @return String
     */
    @Override
    public String getApplicationDescription() {
        return "Exports a completed network to the KAM Store.";
    }

    /**
     * Returns phase four's usage.
     *
     * @return String
     */
    @Override
    public String getUsage() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("[OPTION]...");
        bldr.append(" -k <kam>");
        bldr.append(" -d <description>");
        return bldr.toString();
    }

    /**
     * Invokes {@link #harness(PhaseApplication)} for
     * {@link PhaseFourApplication}.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        harness(new PhaseFiveApplication(args));
    }

    /**
     * {@inheritDoc}
     */
    public static String getRequiredArguments() {
        return "-k <kam> --kam-description <description>";
    }

}
