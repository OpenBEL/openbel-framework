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
package org.openbel.framework.tools.kamstore;

import static java.lang.String.format;
import static java.util.Arrays.copyOfRange;
import static org.apache.commons.lang.StringUtils.join;
import static org.openbel.framework.common.BELUtilities.constrainedHashMap;
import static org.openbel.framework.common.Strings.SYSTEM_CONFIG_PATH;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.common.enums.ExitCode.GENERAL_FAILURE;
import static org.openbel.framework.core.StandardOptions.ARG_SYSCFG;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_DEBUG;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_SYSCFG;
import static org.openbel.framework.core.StandardOptions.SHORT_OPT_VERBOSE;
import static org.openbel.framework.core.StandardOptions.SHRT_OPT_SYSCFG;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.Option;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openbel.framework.api.Kam;
import org.openbel.framework.api.KamStore;
import org.openbel.framework.api.KamStoreException;
import org.openbel.framework.api.KamStoreImpl;
import org.openbel.framework.common.BELUtilities;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.compiler.kam.KAMStoreSchemaService;
import org.openbel.framework.compiler.kam.KAMStoreSchemaServiceImpl;
import org.openbel.framework.core.CommandLineApplication;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.DatabaseService;
import org.openbel.framework.core.df.DatabaseServiceImpl;
import org.openbel.framework.core.df.encryption.KamStoreEncryptionServiceImpl;
import org.openbel.framework.internal.KAMCatalogDao;
import org.openbel.framework.internal.KamDbObject;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.tools.pkam.DefaultPKAMSerializationService;
import org.openbel.framework.tools.pkam.PKAMSerializationFailure;
import org.openbel.framework.tools.pkam.PKAMSerializationService;
import org.openbel.framework.tools.rdf.RDFExporter;
import org.openbel.framework.tools.xgmml.XGMMLExporter;

public final class KamManager extends CommandLineApplication {

    private static final String KAM_MANAGER_NAME = "Kam Manager";
    private static final String KAM_MANAGER_DESC =
            "Provides utilities to manage the KAM store";

    private static final String LONG_OPT_NO_PRESERVE = "no-preserve";
    private static final String USAGE_NO_PRESERVE =
            " [-f | --force | --no-preserve]";
    private static final String USAGE_PORTABLE_KAM_TYPE =
            " [{-t | --type} {kam | KAM}]";
    private static final String USAGE_TYPE =
            " [{-t | --type} {kam | KAM | rdf | RDF | xgmml | XGMML}]";

    private static final String KAM_EXTENSION = ".kam";
    private static final String XGMML_EXTENSION = ".xgmml";
    private static final String N3_EXTENSION = ".n3";

    // The name of the program that invoked main()
    private final String kamManager;

    // The command option passed to the program
    private Command command;
    private String commandStr;

    // Flags specified by command line options
    private boolean help, verbose, quiet, debug;

    // OpenBEL Framework system configuration to use, either default or from a specified
    // configuration file.
    private final SystemConfiguration sysconfig;

    // Objects used by the various commands.  They are initialized in setUp() and
    // destroyed in teardown().
    private DatabaseService dbservice;
    private DBConnection dbConnection;
    private KamStore kamStore;
    private KAMStoreSchemaService kamSchemaService;
    private PKAMSerializationService pkamService;
    private KAMCatalogDao kamCatalogDao;

    /**
     * Static main method to launch the KAM Manager tool.
     *
     * @param args {@link String String[]} the command-line arguments
     */
    public static void main(String[] args) throws Exception {

        KamManager app = new KamManager(args);
        app.run();
    }

    private static String[] withoutKamManagerName(final String[] args) {
        final int length = args.length;
        if (length >= 1 && (
                "KamManager.sh".equals(args[0]) ||
                "KamManager.cmd".equals(args[0]))) {

            return copyOfRange(args, 1, length);
        }
        return args;
    }

    public KamManager(String[] args) {
        super(withoutKamManagerName(args), false);
        if ("KamManager.sh".equals(args[0]) || "KamManager.cmd".equals(args[0])) {
            kamManager = args[0];
        } else {
            kamManager = "KamManager";
        }

        final SimpleOutput reportable = new SimpleOutput();
        reportable.setErrorStream(System.err);
        reportable.setOutputStream(System.out);
        setReportable(reportable);

        printApplicationInfo();

        initializeSystemConfiguration();
        sysconfig = getSystemConfiguration();

        // Determine the command that the user wants to use
        determineCommand();
    }

    private void setUp() throws SQLException, IOException {

        // Setup a database connector to the KAM Store.
        dbservice = new DatabaseServiceImpl();

        if (verbose || debug) {
            reportable.output("Using KAM Store URL: " + sysconfig.getKamURL());
            reportable
                    .output("Using KAM Store User: " + sysconfig.getKamUser());
        }

        dbConnection = dbservice.dbConnection(
                sysconfig.getKamURL(),
                sysconfig.getKamUser(),
                sysconfig.getKamPassword());

        // Connect to the KAM Store. This establishes a connection to the
        // KAM store database and sets up the system to read and process
        // KAMs.
        kamStore = new KamStoreImpl(dbConnection);

        // Set up the KAM Store schemas
        kamSchemaService = new KAMStoreSchemaServiceImpl(dbservice);

        // Load the KAM catalog schema, if it doesn't exist (see #88).
        kamSchemaService.setupKAMCatalogSchema();

        if (command == Command.SET_DESCRIPTION || command == Command.RENAME) {
            kamCatalogDao = new KAMCatalogDao(dbConnection,
                    syscfg.getKamCatalogSchema(), syscfg.getKamSchemaPrefix());
        }

        if (command == Command.IMPORT || command == Command.EXPORT) {

            KamStoreEncryptionServiceImpl encryptionService =
                    new KamStoreEncryptionServiceImpl();
            pkamService = new DefaultPKAMSerializationService(
                    dbservice, encryptionService, kamSchemaService);
        }
    }

    private void teardown() {

        // Tears down the KAM store. This removes any cached data and queries
        if (kamStore != null) {
            kamStore.teardown();
        }

        // Close the DBConnection
        if (dbConnection != null) {
            try {
                final Connection conn = dbConnection.getConnection();
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Do nothing.
            }
        }

        dbservice = null;
        dbConnection = null;
        kamStore = null;
        kamSchemaService = null;
        pkamService = null;
        kamCatalogDao = null;
    }

    public void run() {
        // Determine the values of the common options (the constructor has already initialized
        // some of the options:  syscfg (from -s/--system-config-file), command and commandStr
        // (from one of the command options), and kamManager (which is always the first
        // argument).
        verbose = hasOption(SHORT_OPT_VERBOSE);
        quiet = !verbose && hasOption("q"); // --verbose supersedes --quiet if both are provided
        debug = hasOption(LONG_OPT_DEBUG);

        final String[] extraArgs =
                getExtraneousArguments().toArray(new String[0]);

        if (help || command == Command.HELP) {
            // The help command requires no extra arguments, although this is not
            // enforced since the user seems to be looking for usage help.
            printHelp(true);

        } else if (command == Command.IMPORT) {
            // The import command requires one or two extra arguments:
            // the KAM file to import and, optionally, the new KAM name.
            checkExtraArguments(extraArgs, 1, 2);

            final String pkamPath = extraArgs[0];
            // check that the indicated PKAM file exists
            final File pkamFile = new File(pkamPath);
            if (!pkamFile.exists()) {
                reportable.error("KAM file does not exist - "
                        + pkamFile.getAbsolutePath());
                bail(ExitCode.GENERAL_FAILURE);
            }

            final String kamName = (extraArgs.length > 1 ? extraArgs[1] : null);
            final String password = determinePassword();
            ExportFormat exportFormat = determineType();
            if (exportFormat == null) {
                exportFormat = inferType(pkamFile);
            }
            if (exportFormat == null) {
                // Assume the default format:  portable KAM
                exportFormat = ExportFormat.PORTABLE_KAM;
            }
            final boolean noPreserve = isNoPreserve();

            runImportCommand(kamName, pkamFile, password, exportFormat,
                    noPreserve);
            if (!quiet) {
                reportable.output(format("Imported %s file from '%s'.",
                        exportFormat.toString(), pkamPath));
            }

        } else if (command == Command.EXPORT) {
            // The export command uses two extra arguments:
            // the name of the KAM to export and an output file name (which can be
            // a default value if it is not specified).
            checkExtraArguments(extraArgs, 1, 2);
            ExportFormat exportFormat = determineType();
            if (exportFormat == null) {
                // Assume the default format:  portable KAM
                exportFormat = ExportFormat.PORTABLE_KAM;
            }
            final String kamName = extraArgs[0];
            final String outputFilename =
                    (extraArgs.length > 1 ? extraArgs[1] : null);
            final String password = determinePassword();
            final boolean noPreserve = isNoPreserve();
            runExportCommand(kamName, outputFilename, password, exportFormat,
                    noPreserve);

        } else if (command == Command.SUMMARIZE) {
            // The summarize command requires one extra argument:
            // the name of the KAM to summarize.
            checkExtraArguments(extraArgs, 1);
            final String outputFilename = determineOutputFileName();
            final String kamName = extraArgs[0];
            final boolean noPreserve = isNoPreserve();
            runSummarizeCommand(kamName, outputFilename, noPreserve);

        } else if (command == Command.DELETE) {
            // The delete command requires one extra argument:
            // the name of the KAM to delete.
            checkExtraArguments(extraArgs, 1);
            final String kamName = extraArgs[0];
            runDeleteCommand(kamName);

        } else if (command == Command.LIST) {
            // The list command requires zero extra arguments.
            checkExtraArguments(extraArgs, 0);
            runListCommand();

        } else if (command == Command.SET_DESCRIPTION) {
            // The set-description command requires two extra arguments:
            // the name of the KAM and the new description.
            checkExtraArguments(extraArgs, 2);
            final String kamName = extraArgs[0];
            final String newDescription = extraArgs[1];
            runSetDescriptionCommand(kamName, newDescription);

        } else if (command == Command.RENAME) {
            // The rename command requires two extra arguments:
            // the old KAM name and the new KAM name.
            checkExtraArguments(extraArgs, 2);
            final String oldKamName = extraArgs[0];
            final String newKamName = extraArgs[1];
            final boolean noPreserve = isNoPreserve();
            runRenameCommand(oldKamName, newKamName, noPreserve);

        }
    }

    private void checkExtraArguments(final String[] extraArgs,
            final int expectedNumber) {

        final int extraArgsSize = extraArgs.length;
        if (extraArgsSize != expectedNumber) {
            fatal(format(
                    "`%s --%s` expects %s argument%s but received %s.",
                    kamManager,
                    commandStr,
                    (expectedNumber != 0 ? Integer.toString(expectedNumber)
                            : "no"),
                    (expectedNumber != 1 ? "s" : ""),
                    (extraArgsSize == 0 ? "none" :
                            format("%d:  '%s'", extraArgsSize,
                                    join(extraArgs, "', '")))));
        }
    }

    private void checkExtraArguments(final String[] extraArgs,
            final int minNumber, final int maxNumber) {
        final int extraArgsSize = extraArgs.length;
        if (extraArgsSize < minNumber) {
            fatal(format(
                    "`%s --%s` expects at least %d argument%s but received %s.",
                    kamManager,
                    commandStr,
                    minNumber,
                    (minNumber != 1 ? "s" : ""),
                    (extraArgsSize == 0 ? "none" :
                            format("%d:  '%s'", extraArgsSize,
                                    join(extraArgs, "', '")))));

        } else if (extraArgsSize > maxNumber) {
            fatal(format(
                    "`%s --%s` expects no more than %d argument%s but received %s.",
                    kamManager,
                    commandStr,
                    maxNumber,
                    (maxNumber != 1 ? "s" : ""),
                    (extraArgsSize == 0 ? "none" :
                            format("%d:  '%s'", extraArgsSize,
                                    join(extraArgs, "', '")))));
        }
    }

    /*
     * The following methods determine values for certain command line options:
     */

    /**
     * Initializes the member variables command, commandStr, and help, according to the command
     * option specified by the user in the command line.
     */
    private void determineCommand() {
        command = null;
        commandStr = null;

        boolean tooManyCommands = false;
        for (Command c : Command.values()) {
            final String alias = c.getAlias();
            if (hasOption(alias)) {
                if (c == Command.HELP) {
                    // `--help` can be a command or just a long option for another command.
                    help = true;
                } else if (command == null) {
                    // Found a command option
                    command = c;
                    commandStr = alias;
                } else {
                    tooManyCommands = true;
                }
            }
        }

        if (tooManyCommands) {
            command = null;
            commandStr = null;

            if (!help) {
                reportable.error(format(
                        "You must specify only one command (%s).%n",
                        showCommandAliases()));
                printHelp(true);
            }
        }
        if (help && command == null) {
            // `--help` is considered as the command if no other command is provided.
            command = Command.HELP;
            commandStr = command.getAlias();

        } else if (command == null) {
            reportable
                    .error("No command option given. Please specify one of:  "
                            + showCommandAliases());
            end();
        }
    }

    /**
     * Converts the -t/--type argument to an ExportFormat.
     * @return
     */
    private ExportFormat determineType() {
        ExportFormat exportFormat = null;
        if (hasOption("t")) {
            final String value = getOptionValue("t");
            exportFormat = ExportFormat.getFormatByLabel(value);
            if (exportFormat == null) {
                reportable.error(format(
                        "The KAM file format '%s' is not valid.%n", value));
                printHelp(true);
            }
        }
        return exportFormat;
    }

    /**
     * If {@link determineType} returns {@code null} then this function may be able to determine
     * an appropriate type of KAM file from its file extension.
     * @param file
     * @return
     */
    private static ExportFormat inferType(final File file) {
        final int index = file.getName().lastIndexOf(".");
        if (index == -1) {
            return null;
        }

        final String extension = file.getName().substring(index);
        if (extension.equals(KAM_EXTENSION)) {
            return ExportFormat.PORTABLE_KAM;
        } else if (extension.equals(XGMML_EXTENSION)) {
            return ExportFormat.XGMML;
        } else if (extension.equals(N3_EXTENSION)) {
            return ExportFormat.RDF;
        } else {
            return null;
        }
    }

    /**
     * Gets the -p/--password option.
     * @return
     */
    private String determinePassword() {
        return (hasOption("p") ? getOptionValue("p") : null);
    }

    /**
     * Gets the -o/--output-file option.
     * @return
     */
    private String determineOutputFileName() {
        return (hasOption("o") ? getOptionValue("o") : null);
    }

    /**
     * Determines whether -f/--force/--no-preserve was specified on the command line.
     * @return
     */
    private boolean isNoPreserve() {
        return hasOption("f") || hasOption(LONG_OPT_NO_PRESERVE);
    }

    /*
     * The following methods are responsible for performing one of the commands.  They all:
     * <ol>
     * <li>throw no exceptions, and therefore report their own error messages</li>
     * <li>take the values of the related command line options as method arguments (except for common options)</li>
     * <li>may utilize the services provided by those objects initialized in {@link setUp} and destroyed in {@link teardown}, and</li>
     * <li>call {@link setUp} and {@link teardown} before and after performing the command.</li>
     * </ol>
     */

    private void runImportCommand(final String kamName, final File pkamFile,
            final String password, final ExportFormat exportFormat,
            final boolean noPreserve) {

        if (exportFormat != ExportFormat.PORTABLE_KAM) {
            fatal(format(
                    "`%s --%s` only supports the portable KAM file format.",
                    kamManager, commandStr));
        }

        final String pkamPath = pkamFile.getAbsolutePath();
        try {
            setUp();

            // find KAM by name and see if validate preserve option
            if (kamName != null) {
                List<KamInfo> kamInfos = kamStore.readCatalog();
                for (KamInfo kamInfo : kamInfos) {
                    if (kamName.equals(kamInfo.getName()) && !noPreserve) {
                        teardown();
                        fatal("KAM cannot be overridden, specify --"
                                + LONG_OPT_NO_PRESERVE + " to override");
                    }
                }
            }

            pkamService.deserializeKAM(kamName, pkamPath, password, noPreserve);
        } catch (KamStoreException e) {
            teardown();
            bailOnException(e);
        } catch (PKAMSerializationFailure e) {
            teardown();
            bailOnException(e);
        } catch (IllegalStateException e) {
            // FIXME Hack to catch noPreserve check error thrown from DefaultPKAMSerializationService
            teardown();
            fatal("KAM cannot be overridden, specify --" + LONG_OPT_NO_PRESERVE
                    + " to override");
        } catch (SQLException e) {
            teardown();
            bailOnException(e);
        } catch (IOException e) {
            teardown();
            bailOnException(e);
        } finally {
            teardown();
        }
    }

    private void runExportCommand(final String kamName, String outputFilename,
            final String password, final ExportFormat exportFormat,
            final boolean noPreserve) {
        if (kamName == null) {
            return;
        }
        final Kam kam;

        try {
            setUp();
            kam = kamStore.getKam(kamName);

            // establish a default if output path is not set
            if (outputFilename == null) {
                outputFilename =
                        kam.getKamInfo().getName()
                                + defaultExtension(exportFormat);
            }

            if (!noPreserve && new File(outputFilename).exists()) {
                teardown();
                fatal("File '" + outputFilename + "' exists, specify --"
                        + LONG_OPT_NO_PRESERVE + " to override.");
            }

            final String formatString;
            if (exportFormat == ExportFormat.XGMML) {
                formatString = "XGMML";
                XGMMLExporter.exportKam(kam, kamStore, outputFilename);
            } else if (exportFormat == ExportFormat.RDF) {
                formatString = "RDF";
                RDFExporter.exportRdf(kam, kamStore.getKamInfo(kamName),
                        kamStore, outputFilename);
            } else if (exportFormat == ExportFormat.PORTABLE_KAM) {
                formatString = "portable";
                pkamService.serializeKAM(kamName, outputFilename, password);
            } else {
                throw new UnsupportedOperationException(exportFormat.toString()
                        + " is not supported.");
            }

            if (!quiet) {
                reportable.output(format(
                        "Exported '%s' KAM to %s format in '%s'.",
                        kam.getKamInfo().getName(), formatString,
                        outputFilename));
            }
        } catch (PKAMSerializationFailure e) {
            reportable.error(e.getUserFacingMessage());
        } catch (KamStoreException e) {
            teardown();
            bailOnException(e);
        } catch (IOException e) {
            teardown();
            bailOnException(e);
        } catch (InvalidArgument e) {
            teardown();
            bailOnException(e);
        } catch (SQLException e) {
            teardown();
            bailOnException(e);
        } finally {
            teardown();
        }
    }

    private static String defaultExtension(final ExportFormat type) {
        switch (type) {
        case PORTABLE_KAM:
            return KAM_EXTENSION;
        case XGMML:
            return XGMML_EXTENSION;
        case RDF:
            return N3_EXTENSION;
        }
        throw new UnsupportedOperationException(type.toString()
                + " is not supported.");
    }

    private void runSummarizeCommand(final String kamName,
            final String outputFilename, final boolean noPreserve) {

        KamSummaryXHTMLWriter xhtmlWriter = null;
        try {
            setUp();

            xhtmlWriter = createOutputFileWriter(outputFilename, noPreserve);
            Kam kam;
            if (kamName != null) {
                // Look up the requested KAM and summarize.
                kam = kamStore.getKam(kamName);
                KamSummarizer summarizer = new KamSummarizer(reportable);
                KamSummary summary = KamSummarizer.summarizeKam(kamStore, kam);
                summarizer.printKamSummary(kamStore, summary);
                if (xhtmlWriter != null) {
                    xhtmlWriter.write(summary);
                }
            }
        } catch (KamStoreException e) {
            teardown();
            bailOnException(e);
        } catch (IOException e) {
            teardown();
            bailOnException(e);
        } catch (InvalidArgument e) {
            teardown();
            bailOnException(e);
        } catch (SQLException e) {
            teardown();
            bailOnException(e);
        } finally {
            teardown();
            if (xhtmlWriter != null) {
                try {
                    xhtmlWriter.close();
                } catch (IOException e) {
                    teardown();
                    bailOnException(e);
                }
            }
        }
    }

    private KamSummaryXHTMLWriter createOutputFileWriter(String outputFilename,
            final boolean noPreserve) throws IOException {
        FileWriter writer = null;
        KamSummaryXHTMLWriter xhtmlWriter = null;

        if (verbose || debug) {
            reportable.output("Creating output file: " + outputFilename);
        }
        if (!StringUtils.isBlank(outputFilename)) {
            File outputFile = new File(outputFilename);
            if (!outputFile.exists() || noPreserve) {
                writer = new FileWriter(outputFile);
                xhtmlWriter = new KamSummaryXHTMLWriter(writer);
                xhtmlWriter.open();
            } else {
                reportable
                        .warning(format(
                                "Output file already exist: %s, specify --%s option to overwrite.",
                                outputFilename, LONG_OPT_NO_PRESERVE));
            }
        }

        return xhtmlWriter;
    }

    private void runDeleteCommand(final String kamName) {
        try {
            setUp();

            KamInfo kamInfo = kamStore.getKamInfo(kamName);
            if (kamInfo != null) {
                deleteKam(kamInfo, quiet);
            } else {
                reportable.warning("No KAM named '" + kamName
                        + "' exists in the catalog.");
            }
        } catch (SQLException e) {
            teardown();
            bailOnException(e);
        } catch (IOException e) {
            teardown();
            bailOnException(e);
        } catch (KamStoreException e) {
            teardown();
            bailOnException(e);
        } finally {
            teardown();
        }
    }

    private void deleteKam(final KamInfo kamInfo, final boolean quiet)
            throws SQLException, IOException {
        final String kamName = kamInfo.getName();
        kamSchemaService.deleteFromKAMCatalog(kamName);
        final String schemaName = kamInfo.getSchemaName();
        boolean deletedSchema =
                kamSchemaService.deleteKAMStoreSchema(dbConnection, schemaName);
        if (deletedSchema && !quiet) {
            reportable.output("The KAM named '" + kamName
                    + "' has been removed.");
        } else if (!deletedSchema) {
            reportable
                    .warning("The KAM data was truncated, but the database schema '"
                            + schemaName
                            + "' still exists. The schema deletion must be handled separately in dba-managed configurations.");
        }
    }

    private void runListCommand() {
        try {
            setUp();

            final List<KamInfo> kamInfos = kamStore.readCatalog();
            // Get a list of all the KAMs available in the KAM store
            reportable.output("Available KAMs:");
            reportable.output("\tName\tLast Compiled\tSchema Name");
            reportable.output("\t------\t-------------\t-----------");
            for (KamInfo kamInfo : kamInfos) {
                KamDbObject kamDb = kamInfo.getKamDbObject();
                reportable.output(format("\t%s\t%s\t%s",
                        kamDb.getName(), kamDb.getLastCompiled(),
                        kamDb.getSchemaName()));
            }
            reportable.output("\n");
        } catch (KamStoreException e) {
            teardown();
            bailOnException(e);
        } catch (SQLException e) {
            teardown();
            bailOnException(e);
        } catch (IOException e) {
            teardown();
            bailOnException(e);
        } finally {
            teardown();
        }
    }

    private void runSetDescriptionCommand(final String kamName,
            final String newDescription) {
        try {
            setUp();

            final KamInfo kamInfo = kamStore.getKamInfo(kamName);
            if (kamInfo != null) {
                if (!BELUtilities.equals(kamInfo.getDescription(),
                        newDescription)) {
                    final KamDbObject updated =
                            new KamDbObject(kamInfo.getId(), kamInfo.getName(),
                                    newDescription, kamInfo.getLastCompiled(),
                                    kamInfo.getSchemaName());
                    kamCatalogDao.saveToCatalog(updated);
                }
            } else {
                reportable.warning("No KAM named '" + kamName
                        + "' exists in the catalog.");
            }
        } catch (SQLException e) {
            teardown();
            bailOnException(e);
        } catch (KamStoreException e) {
            teardown();
            bailOnException(e);
        } catch (IOException e) {
            teardown();
            bailOnException(e);
        } finally {
            teardown();
        }
    }

    private void runRenameCommand(final String oldKamName,
            final String newKamName, final boolean noPreserve) {
        if (BELUtilities.equals(oldKamName, newKamName)) {
            return;
        }

        try {
            setUp();

            final KamInfo kamInfo = kamStore.getKamInfo(oldKamName);
            if (kamInfo != null) {
                final KamInfo kamInfo2 = kamStore.getKamInfo(newKamName);
                if (kamInfo2 != null) {
                    if (noPreserve) {
                        deleteKam(kamInfo2, true);
                    } else {
                        teardown();
                        fatal(format(
                                "A KAM named '%s' already exists in the KAM store. Specify --%s to override.",
                                newKamName, LONG_OPT_NO_PRESERVE));
                    }
                }
                final KamDbObject updated =
                        new KamDbObject(kamInfo.getId(), newKamName,
                                kamInfo.getDescription(),
                                kamInfo.getLastCompiled(),
                                kamInfo.getSchemaName());
                kamCatalogDao.saveToCatalog(updated);
            } else {
                reportable.warning("No KAM named '" + oldKamName
                        + "' exists in the catalog.");
            }
        } catch (SQLException e) {
            teardown();
            bailOnException(e);
        } catch (KamStoreException e) {
            teardown();
            bailOnException(e);
        } catch (IOException e) {
            teardown();
            bailOnException(e);
        } finally {
            teardown();
        }
    }

    /*
     * The following are utility methods used for usage and error messages:
     */

    private static String showCommandAliases() {
        final Command[] commands = Command.values();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < commands.length; ++i) {
            if (i != 0) {
                sb.append(", ");
                if (i == commands.length - 1) {
                    sb.append("or ");
                }
            }
            sb.append("`--");
            sb.append(commands[i].getAlias());
            sb.append("`");
        }
        return sb.toString();
    }

    private void bailOnException(final Exception e) {
        final Throwable cause = ExceptionUtils.getRootCause(e);
        reportable.error("Unable to run " + KAM_MANAGER_NAME);
        reportable.error("Reason: "
                + (cause == null ? e.getMessage() : cause.getMessage()));
        if (debug) {
            e.printStackTrace(reportable.errorStream());
        }
        bail(GENERAL_FAILURE);
    }

    /*
     * The following are overrides of {@link org.openbel.framework.clf.CommandLineApplication CommandLineApplication}'s methods:
     */

    @Override
    public String getApplicationName() {
        return KAM_MANAGER_NAME;
    }

    @Override
    public String getApplicationShortName() {
        return KAM_MANAGER_NAME;
    }

    @Override
    public String getApplicationDescription() {
        return KAM_MANAGER_DESC;
    }

    @Override
    public String getUsage() {
        final StringBuilder bldr = new StringBuilder();

        if (command == null || command == Command.HELP) {
            bldr.append(" {");
            final Command[] commands = Command.values();
            for (int i = 0; i < commands.length; ++i) {
                if (commands[i] != Command.HELP) {
                    if (i != 0) {
                        bldr.append(" | ");
                    }
                    bldr.append("--").append(commands[i].getAlias());
                }
            }
            bldr.append("}");

        } else {
            bldr.append(" --").append(commandStr);

            if (command == Command.IMPORT) {
                bldr.append(" <exported KAM file> [<new KAM name>]")
                        .append(USAGE_NO_PRESERVE)
                        .append(USAGE_PORTABLE_KAM_TYPE);
            } else if (command == Command.EXPORT) {
                bldr.append(" <KAM name> [<output file name>]")
                        .append(USAGE_NO_PRESERVE).append(USAGE_TYPE);
            } else if (command == Command.SUMMARIZE
                    || command == Command.DELETE) {
                bldr.append(" <KAM name>");
            } else if (command == Command.SET_DESCRIPTION) {
                bldr.append(" <KAM name> <new description>");
            } else if (command == Command.RENAME) {
                bldr.append(" <old KAM name> <new KAM name>").append(
                        USAGE_NO_PRESERVE);
            }
        }

        bldr.append(" [-h | --help] [-v | --verbose] [-q | --quiet] [--debug] [{-s | --system-config-file} <OpenBEL Framework configuration file>]");

        return bldr.toString();
    }

    private static EnumMap<Command, String> commandToUsageHelp =
            new EnumMap<Command, String>(Command.class);
    static {
        commandToUsageHelp.put(Command.DELETE,
                "Deletes a KAM from the KAM store.");
        commandToUsageHelp.put(Command.EXPORT, "Exports a KAM to a file.");
        commandToUsageHelp.put(Command.IMPORT,
                "Imports a KAM from a file to a KAM store.");
        commandToUsageHelp.put(Command.LIST, "Lists the KAMs in a KAM store.");
        commandToUsageHelp.put(Command.RENAME, "Changes the name of a KAM.");
        commandToUsageHelp.put(Command.SET_DESCRIPTION,
                "Changes the description of a KAM.");
        commandToUsageHelp.put(Command.SUMMARIZE,
                "Prints summary information for a KAM.");
    }

    @Override
    public List<Option> getCommandLineOptions() {
        List<Option> options = new LinkedList<Option>();

        // Add the long options that serve as the commands
        for (Command c : Command.values()) {
            if (c != Command.HELP) {
                options.add(new Option(null, c.getAlias(), false,
                        "The " + c.getAlias() + " command. "
                                + commandToUsageHelp.get(c)));
            }
        }

        // -f/--force/--no-preserve used for the import, export, and rename commands
        final String noPreserveHelp =
                "Optional.  Used with the summarize or export commands, if a file with the same name "
                        +
                        "as the output file already exists then it will be overwritten. "
                        +
                        "If used with the import command, if a KAM with the same name as "
                        +
                        "being imported already exists in the KAM store, it will be "
                        +
                        "overwritten.  If used with the rename command, if the new name for a KAM "
                        +
                        "is also the name of another KAM in the KAM store, then that KAM "
                        +
                        "will be overwritten.\n"
                        +
                        "The default is always to preserve the existing file or KAM and "
                        +
                        "generate a warning.";
        options.add(new Option("f", "force", false,
                "Identical to --no-preserve."));
        options.add(new Option(null, LONG_OPT_NO_PRESERVE, false,
                noPreserveHelp));

        // -o/--output-file used to set the file to output XHTML output (for the
        // summarize command, for example)
        final String outputFileHelp =
                "Optional.  If present, it indicates the name of the output file "
                        +
                        "to write the summary to.  The file will be written as XHTML.";
        options.add(new Option("o", "output-file", true, outputFileHelp));

        // -p/--password used for the import and export commands
        final String passwordHelp =
                "Optional.  With the import command, the password is used to decrypt the "
                        +
                        "portable KAM file if the file was encrypted during export. "
                        +
                        "With the export command, the password is used to encrypt the "
                        +
                        "portable KAM file.  This same password must be used to import "
                        +
                        "the portable KAM with the "
                        + getApplicationShortName() + " import command.\n" +
                        "The default is to not encrypt or decrypt.";
        options.add(new Option("p", "password", true, passwordHelp));

        // -t/--type used for the import and export commands
        final String typeHelp =
                "With the import command, this option specifies the format of the imported KAM file. "
                        +
                        "By default, "
                        + getApplicationShortName()
                        + " infers the format of the KAM file by "
                        +
                        "its extension ("
                        + KAM_EXTENSION
                        + ", "
                        + XGMML_EXTENSION
                        + ", or "
                        + N3_EXTENSION
                        + ").\n"
                        +
                        "With the export command, this option specifies the type of export file to generate. "
                        +
                        "If XGMML the KAM will be exported in XGMML format.  If RDF "
                        +
                        "the KAM will be exported as an RDF using N3 format.  If KAM "
                        +
                        "is specified, the KAM will be exported in a portable binary "
                        +
                        "format that can be subsequently imported to another KAM store "
                        +
                        "using the "
                        + getApplicationShortName()
                        + " import command. The default is to export to " +
                        "a portable KAM format.";
        options.add(new Option("t", "type", true, typeHelp));

        // Add the options common to all commands:

        options.add(new Option(
                "q",
                "quiet",
                false,
                "Enables quiet mode. This mode outputs very little additional information "
                        +
                        "as the application runs.\n" +
                        "The default is to disable quiet mode."));

        final Option sOpt = new Option(SHRT_OPT_SYSCFG, LONG_OPT_SYSCFG, true,
                SYSTEM_CONFIG_PATH);
        sOpt.setArgName(ARG_SYSCFG);
        options.add(sOpt);

        // The base class CommandLineApplication will add the --debug, -h/--help,
        // and -v/--verbose options.

        return options;
    }

    /*
     * The following are the Command and ExportFormat enums:
     */

    private static enum Command {
        IMPORT("import"),
        EXPORT("export"),
        SUMMARIZE("summarize"),
        DELETE("delete"),
        LIST("list"),
        SET_DESCRIPTION("set-description"),
        RENAME("rename"),
        HELP("help");

        private static final Map<String, Command> aliasToCommand;
        static {
            aliasToCommand = constrainedHashMap(values().length);
            for (Command command : values()) {
                aliasToCommand.put(command.alias, command);
            }
        }

        private String alias;

        private Command(String alias) {
            this.alias = alias;
        }

        public String getAlias() {
            return alias;
        }
    }

    /**
     * ExportFormat represents the supported export formats.
     */
    public static enum ExportFormat {
        XGMML("xgmml", "XGMML"),
        RDF("rdf", "RDF"),
        PORTABLE_KAM("kam", "KAM");

        private static final Map<String, ExportFormat> STRINGTOENUM;
        static {
            STRINGTOENUM = constrainedHashMap(values().length);
            for (ExportFormat exportFormat : values()) {
                for (String label : exportFormat.labels) {
                    STRINGTOENUM.put(label, exportFormat);
                }
            }
        }

        private final String[] labels;

        private ExportFormat(final String... labels) {
            this.labels = labels;
        }

        @Override
        public String toString() {
            switch (this) {
            case XGMML:
                return "XGMML";
            case RDF:
                return "RDF";
            case PORTABLE_KAM:
                return "Portable KAM";
            default:
                throw new UnsupportedOperationException(
                        "ExportFormat." + name()
                                + ".toString() is not supported.");
            }
        }

        public static ExportFormat getFormatByLabel(final String label) {
            return STRINGTOENUM.get(label);
        }
    }
}
