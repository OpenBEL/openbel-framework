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

import static java.util.Collections.emptyList;
import static org.openbel.framework.common.BELUtilities.deleteDirectory;
import static org.openbel.framework.common.Strings.DIRECTORY_CREATION_FAILED;
import static org.openbel.framework.common.Strings.DIRECTORY_DELETION_FAILED;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.common.enums.BELFrameworkVersion.VERSION_NUMBER;
import static org.openbel.framework.common.enums.ExitCode.GENERAL_FAILURE;

import java.io.File;
import java.util.List;

import org.apache.commons.cli.Option;
import org.openbel.framework.common.DBConnectionFailure;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.core.CommandLineApplication;
import org.openbel.framework.core.df.DatabaseService;
import org.openbel.framework.core.df.DatabaseServiceImpl;

/**
 * This application validates the command line arguments passed by the
 * <i>belc</i> frontend against all phase applications.
 * <p>
 * This class supplants a proper frontend to all phases. Each phase application
 * ignores unrecognized options and this class is a safety net to ensure all
 * phases have what they need before executing.
 * </p>
 * <p>
 * If <i>belc</i> ever dies and the BEL compiler gets a proper frontend, this
 * application should be removed.
 * </p>
 */
public class PhaseZeroApplication extends CommandLineApplication {
    private final String[] commandLineArgs;

    /**
     * Creates the phase zero application.
     *
     * @param args Command-line arguments
     * @param configs
     */
    public PhaseZeroApplication(String[] args) {
        super(args);
        this.commandLineArgs = args;

        final SimpleOutput reportable = new SimpleOutput();
        reportable.setErrorStream(System.err);
        reportable.setOutputStream(System.out);
        setReportable(reportable);
    }

    public void start() {
        printApplicationInfo("Compiler/Assembler");

        PhaseOneApplication p1App = new PhaseOneApplication(commandLineArgs);
        if (!p1App.validCommandLine()) {
            printUsage();
            bail(GENERAL_FAILURE);
        }

        PhaseTwoApplication p2App = new PhaseTwoApplication(commandLineArgs);
        if (!p2App.validCommandLine()) {
            printUsage();
            bail(GENERAL_FAILURE);
        }

        PhaseThreeApplication p3App =
                new PhaseThreeApplication(commandLineArgs);
        if (!p3App.validCommandLine()) {
            printUsage();
            bail(GENERAL_FAILURE);
        }

        PhaseFourApplication p4App = new PhaseFourApplication(commandLineArgs);
        if (!p4App.validCommandLine()) {
            printUsage();
            bail(GENERAL_FAILURE);
        }

        initializeSystemConfiguration();
        SystemConfiguration syscfg = getSystemConfiguration();

        final File wrkdir = syscfg.getWorkingDirectory();
        File[] dircontents = wrkdir.listFiles();
        if (dircontents != null && dircontents.length > 0) {
            if (!deleteDirectory(wrkdir)) {
                reportable.error(DIRECTORY_DELETION_FAILED + wrkdir);
                bail(ExitCode.DIRECTORY_DELETION_FAILED);
            }
            if (!wrkdir.mkdir()) {
                reportable.error(DIRECTORY_CREATION_FAILED + wrkdir);
                bail(ExitCode.DIRECTORY_CREATION_FAILED);
            }
            wrkdir.mkdir();
        }

        DatabaseService dbsvc = new DatabaseServiceImpl();

        // validate kamstore connection
        final String url = syscfg.getKamURL();
        final String user = syscfg.getKamUser();
        final String pass = syscfg.getKamPassword();
        try {
            if (!dbsvc.validConnection(url, user, pass)) {
                bail(ExitCode.KAM_CONNECTION_FAILURE);
            }
        } catch (DBConnectionFailure e) {
            reportable.error(e.getUserFacingMessage());
            bail(ExitCode.KAM_CONNECTION_FAILURE);
        }
    }

    /**
     * Returns {@code "Phase 0: Command-line validation"}.
     *
     * @return String
     */
    @Override
    public String getApplicationName() {
        return "OpenBEL Framework Compiler/Assembler " + VERSION_NUMBER;
    }

    /**
     * Returns {@code "Backtrack"}.
     *
     * @return String
     */
    @Override
    public String getApplicationShortName() {
        return "Compiler/Assembler";
    }

    /**
     * Returns {@code "Validates the BEL Compiler command-line."}.
     *
     * @return String
     */
    @Override
    public String getApplicationDescription() {
        return "Phase 0: Validates the Compiler/Assembler before execution.";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {

        StringBuilder bldr = new StringBuilder();
        bldr.append("[OPTIONS]...");
        bldr.append(" [-p <path> [-p <path 2> [...] ] ]");
        bldr.append(" [-f <filename> [-f <filename 2> [...] ] ]");
        bldr.append(" [-k <kam_name>]");
        bldr.append(" [-d <kam_description>]");

        return bldr.toString();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Option> getCommandLineOptions() {
        return emptyList();
    }

    /**
     * PhaseZeroApplication {@code main()}.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        PhaseZeroApplication p = new PhaseZeroApplication(args);
        p.start();
    }
}
