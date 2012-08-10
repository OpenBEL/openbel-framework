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
package org.openbel.framework.tools.kamstore;

import static java.lang.String.format;
import static org.openbel.framework.common.Strings.SYSTEM_CONFIG_PATH;
import static org.openbel.framework.common.Strings.VERBOSE_HELP;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.core.StandardOptions.ARG_SYSCFG;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_DEBUG;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_HELP;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_SYSCFG;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_VERBOSE;
import static org.openbel.framework.core.StandardOptions.SHORT_OPT_VERBOSE;
import static org.openbel.framework.core.StandardOptions.SHRT_OPT_HELP;
import static org.openbel.framework.core.StandardOptions.SHRT_OPT_SYSCFG;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openbel.framework.api.KAMStore;
import org.openbel.framework.api.KAMStoreException;
import org.openbel.framework.api.KAMStoreImpl;
import org.openbel.framework.api.internal.KamDbObject;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.compiler.kam.KAMStoreSchemaService;
import org.openbel.framework.compiler.kam.KAMStoreSchemaServiceImpl;
import org.openbel.framework.core.CommandLineApplication;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.DatabaseService;
import org.openbel.framework.core.df.DatabaseServiceImpl;

public class KamComparator extends CommandLineApplication {

    private static final String KAM_COMPARATOR_NAME = "Kam Comparator";
    private static final String KAM_COMPARATOR_DESC =
            "Compares two KAMs by topology and data";

    public static final String[] TOPOLOGY_LABELS = new String[] {
            "KAM node count", "KAM edge count", "Average KAM node degree",
            "Average KAM node in degree", "Average KAM node out degree",
            "Density of KAM (0...1)" };
    public static final String[] DATA_LABELS = new String[] {
            "BEL document count", "Namespace count",
            "Annotation definition count",
            "Annotation count", "Statement count", "Term count",
            "Parameter count", "Unique parameter count" };

    public static final int PRECISION = 4;

    private enum Mode {
        LIST, HELP, COMPARE
    }

    private Mode mode;
    private String kam1Name, kam2Name;
    private boolean noPreserve, verbose;
    private String outputFilename;

    private final DatabaseService dbservice;
    private final SystemConfiguration sysconfig;

    public KamComparator(String[] args) {
        super(args);

        final SimpleOutput reportable = new SimpleOutput();
        reportable.setErrorStream(System.err);
        reportable.setOutputStream(System.out);
        setReportable(reportable);

        printApplicationInfo();

        initializeSystemConfiguration();
        sysconfig = getSystemConfiguration();
        dbservice = new DatabaseServiceImpl();
    }

    public void run() throws IOException, KAMStoreException, SQLException {
        processOptions();

        if (mode == Mode.HELP) {
            printHelp(true);
        }

        if (verbose) {
            reportable.output("Using KAM Store URL: " + sysconfig.getKamURL());
            reportable
                    .output("Using KAM Store User: " + sysconfig.getKamUser());
        }

        DBConnection dbConnection = dbservice.dbConnection(
                sysconfig.getKamURL(),
                sysconfig.getKamUser(),
                sysconfig.getKamPassword());

        KAMStore kAMStore = new KAMStoreImpl(dbConnection);

        // See if we need to set up the KAM Store schemas
        final KAMStoreSchemaService kamSchemaService =
                new KAMStoreSchemaServiceImpl(dbservice);

        // Load the KAM catalog schema, if it doesn't exist (see #88).
        kamSchemaService.setupKAMCatalogSchema();

        try {
            if (mode == Mode.LIST) {
                List<KamInfo> kamInfos = kAMStore.getCatalog();
                printKamCatalogSummary(kamInfos);

            } else if (mode == Mode.COMPARE) {
                final KamComparisonXHTMLWriter xhtmlWriter =
                        createOutputFileWriter();

                final String[] kamNames = new String[] { kam1Name, kam2Name };

                // Get the KAM catalog information for each KAM.
                final KamInfo[] kamInfos = new KamInfo[kamNames.length];
                for (int i = 0; i < kamNames.length; ++i) {
                    final KamInfo kamInfo = kAMStore.getKamInfo(kamNames[i]);
                    if (kamInfo == null) {
                        reportable.error("No KAM found with name '"
                                + kamNames[i] + "'");
                        bail(ExitCode.GENERAL_FAILURE);
                    }
                    kamInfos[i] = kamInfo;
                }

                // Create the KamComparison that will be displayed and/or written to the
                // output file.
                final KamComparison cmp = new KamComparison(kamNames);
                for (KamInfo kamInfo : kamInfos) {
                    final String kamName = kamInfo.getName();
                    final KAMStoreStatisticsDaoImpl dao =
                            new KAMStoreStatisticsDaoImpl(
                                    kamInfo.getKamDbObject().getSchemaName(),
                                    dbConnection);

                    cmp.setKamNodeCount(kamName, dao.getKamNodeCount());
                    cmp.setKamEdgeCount(kamName, dao.getKamEdgeCount());
                    cmp.setBELDocumentCount(kamName, dao.getBELDocumentCount());
                    cmp.setNamespaceCount(kamName, dao.getNamespaceCount());
                    cmp.setAnnotationDefinitionCount(kamName,
                            dao.getAnnotationDefinitonCount());
                    cmp.setAnnotationCount(kamName, dao.getAnnotationCount());
                    cmp.setStatementCount(kamName, dao.getStatementCount());
                    cmp.setTermCount(kamName, dao.getTermCount());
                    cmp.setParameterCount(kamName, dao.getParameterCount());
                    cmp.setUniqueParameterCount(kamName,
                            dao.getUniqueParameterCount());
                }

                // Write to stdout.
                writeComparison(cmp);

                // Write the output to the output file.
                if (xhtmlWriter != null) {
                    xhtmlWriter.writeKamComparison(cmp);
                    xhtmlWriter.close();
                }
            }
        } finally {
            kAMStore.teardown();
            dbConnection.getConnection().close();
        }
    }

    private KamComparisonXHTMLWriter createOutputFileWriter()
            throws IOException {
        KamComparisonXHTMLWriter xhtmlWriter = null;

        if (!StringUtils.isBlank(outputFilename)) {
            File outputFile = new File(outputFilename);
            if (outputFile.isDirectory()) {
                reportable.error(format(
                        "Output file can not be a directory - %s",
                        outputFile.getAbsolutePath()));
                bail(ExitCode.GENERAL_FAILURE);

            } else if (!noPreserve && outputFile.isFile()) {
                reportable.warning(format("Output file already exists: %s, " +
                        "specify the --no-preserve option to overwrite.",
                        outputFilename));

            } else {
                if (verbose) {
                    reportable.output(format("Creating output file: %s",
                            outputFilename));
                }
                xhtmlWriter =
                        new KamComparisonXHTMLWriter(new FileWriter(outputFile));
                xhtmlWriter.open();
            }
        }

        return xhtmlWriter;
    }

    private void writeComparison(KamComparison cmp) {
        final String[] kamNames = cmp.getKamNames();
        final String kam1Name = kamNames[0], kam2Name = kamNames[1];

        // Set the column widths to the next multiple of 4, and to at least
        // 16 characters.
        final int min = 16;
        final int c0 =
                (Math.max(maxLength(TOPOLOGY_LABELS), maxLength(DATA_LABELS)) / 4 + 1) * 4;
        final int c1 = Math.max(min, (kam1Name.length() / 4 + 1) * 4);
        final int c2 = Math.max(min, (kam2Name.length() / 4 + 1) * 4);

        final String titleFormat =
                "%-" + (c0 + 4) + "s%-" + c1 + "s%-" + c2 + "s%n";
        final String sectionFormat = "%-" + (c0 + 4) + "s%n";
        final String integralRowFormat =
                "    %-" + c0 + "s%-" + c1 + "d%-" + c2 + "d%n";
        final String floatingPointRowFormat = "    %-" + c0 + "s%-" + c1 + "." +
                PRECISION + "f%-" + c2 + "." + PRECISION + "f%n";

        StringBuilder bldr = new StringBuilder();

        bldr.append(format(titleFormat, "", kam1Name, kam2Name));

        // Compare the KAMs by topology.
        bldr.append(format(sectionFormat, "Topology"));

        bldr.append(format(integralRowFormat, TOPOLOGY_LABELS[0],
                cmp.getKamNodeCount(kam1Name),
                cmp.getKamNodeCount(kam2Name)));
        bldr.append(format(integralRowFormat, TOPOLOGY_LABELS[1],
                cmp.getKamEdgeCount(kam1Name),
                cmp.getKamEdgeCount(kam2Name)));
        bldr.append(format(floatingPointRowFormat, TOPOLOGY_LABELS[2],
                cmp.getAverageKamNodeDegree(kam1Name),
                cmp.getAverageKamNodeDegree(kam2Name)));
        bldr.append(format(floatingPointRowFormat, TOPOLOGY_LABELS[3],
                cmp.getAverageKamNodeInDegree(kam1Name),
                cmp.getAverageKamNodeInDegree(kam2Name)));
        bldr.append(format(floatingPointRowFormat, TOPOLOGY_LABELS[4],
                cmp.getAverageKamNodeOutDegree(kam1Name),
                cmp.getAverageKamNodeOutDegree(kam2Name)));
        bldr.append(format(floatingPointRowFormat, TOPOLOGY_LABELS[5],
                cmp.getDensity(kam1Name), cmp.getDensity(kam2Name)));

        // Compare the KAMs by data.
        bldr.append(format(sectionFormat, "Data"));

        bldr.append(format(integralRowFormat, DATA_LABELS[0],
                cmp.getBELDocumentCount(kam1Name),
                cmp.getBELDocumentCount(kam2Name)));
        bldr.append(format(integralRowFormat, DATA_LABELS[1],
                cmp.getNamespaceCount(kam1Name),
                cmp.getNamespaceCount(kam2Name)));
        bldr.append(format(integralRowFormat, DATA_LABELS[2],
                cmp.getAnnotationDefinitionCount(kam1Name),
                cmp.getAnnotationDefinitionCount(kam2Name)));
        bldr.append(format(integralRowFormat, DATA_LABELS[3],
                cmp.getAnnotationCount(kam1Name),
                cmp.getAnnotationCount(kam2Name)));
        bldr.append(format(integralRowFormat, DATA_LABELS[4],
                cmp.getStatementCount(kam1Name),
                cmp.getStatementCount(kam2Name)));
        bldr.append(format(integralRowFormat, DATA_LABELS[5],
                cmp.getTermCount(kam1Name),
                cmp.getTermCount(kam2Name)));
        bldr.append(format(integralRowFormat, DATA_LABELS[6],
                cmp.getParameterCount(kam1Name),
                cmp.getParameterCount(kam2Name)));
        bldr.append(format(integralRowFormat, DATA_LABELS[7],
                cmp.getUniqueParameterCount(kam1Name),
                cmp.getUniqueParameterCount(kam2Name)));

        reportable.output("", bldr.toString());
    }

    private static int maxLength(String[] strings) {
        int max = 0;
        for (String s : strings) {
            final int length = s.length();
            if (length > max) {
                max = length;
            }
        }
        return max;
    }

    protected void processOptions() {
        if (hasOption(SHRT_OPT_HELP)) {
            mode = Mode.HELP;
        } else if (hasOption("l")) {
            mode = Mode.LIST;
        } else {
            mode = Mode.COMPARE;

            final List<String> kamArgs = getExtraneousArguments();
            if (kamArgs == null || kamArgs.size() != 2) {
            	// print out the usage if less then 2 arguments are given
            	printUsage();
            	getReportable().error("\n");
                getReportable().error(
                        "You must specify two KAM names to compare.");
                end();
                // Unreachable return stmt - for static analysis purposes
                return;
            }
            kam1Name = kamArgs.get(0);
            kam2Name = kamArgs.get(1);
        }
        if (hasOption("o")) {
            outputFilename = getOptionValue("o");
        }
        noPreserve = hasOption("no-preserve");
        verbose = hasOption(SHORT_OPT_VERBOSE);
    }

    protected void printKamCatalogSummary(List<KamInfo> kamInfos) {
        // Get a list of all the KAMs available in the KAM store
        reportable.output("Available KAMs:");
        reportable.output("\tName\tLast Compiled\tSchema Name");
        reportable.output("\t------\t-------------\t-----------");
        for (KamInfo kamInfo : kamInfos) {
            KamDbObject kamDb = kamInfo.getKamDbObject();
            reportable.output(String.format("\t%s\t%s\t%s",
                    kamDb.getName(), kamDb.getLastCompiled(),
                    kamDb.getSchemaName()));
        }
        System.out.print("\n");
    }

    @Override
    public String getApplicationName() {
        return KAM_COMPARATOR_NAME;
    }

    @Override
    public String getApplicationShortName() {
        return KAM_COMPARATOR_NAME;
    }

    @Override
    public String getApplicationDescription() {
        return KAM_COMPARATOR_DESC;
    }

    @Override
    public String getUsage() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("[--debug]");
        bldr.append(" [-h]");
        bldr.append(" [-l]");
        bldr.append(" -o <output file name> [--no-preserve]");
        bldr.append(" [-s <system config file name>]");
        bldr.append(" [-v]");
        bldr.append(" <KAM name> <KAM name>");
        return bldr.toString();
    }

    @Override
    public List<Option> getCommandLineOptions() {
        final List<Option> options = new LinkedList<Option>();

        options.add(new Option(
                null,
                LONG_OPT_DEBUG,
                false,
                "Enables debug mode. This mode outputs additional debug information "
                        +
                        "as the application runs."));

        options.add(new Option(SHRT_OPT_HELP, LONG_OPT_HELP, false,
                "Enables help mode. This mode shows the help information."));

        options.add(new Option("l", "list", false,
                "Lists the KAMs stored in the KAMStore."));

        final Option oOpt =
                new Option(
                        "o",
                        "output-file",
                        true,
                        "Optional.  If present, indicates the name of the output file "
                                +
                                "to write the KAM comparisions to in XHTML format.");
        oOpt.setArgName("output filename");
        options.add(oOpt);

        options.add(new Option(
                null,
                "no-preserve",
                false,
                "Optional.  If present, if a file exist with the same name as the output "
                        +
                        "file then that file will be overwritten."));

        final Option sOpt = new Option(SHRT_OPT_SYSCFG, LONG_OPT_SYSCFG, false,
                SYSTEM_CONFIG_PATH);
        sOpt.setArgName(ARG_SYSCFG);
        options.add(sOpt);

        options.add(new Option(SHORT_OPT_VERBOSE, LONG_OPT_VERBOSE, false,
                VERBOSE_HELP));

        return options;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            KamComparator app = new KamComparator(args);
            app.run();
        } catch (Exception e) {
            Throwable cause = ExceptionUtils.getRootCause(e);
            System.err.println("Unable to run " + KAM_COMPARATOR_NAME);
            System.err.println("Reason: "
                    + (cause == null ? e.getMessage() : cause.getMessage()));
        }
    }

}
