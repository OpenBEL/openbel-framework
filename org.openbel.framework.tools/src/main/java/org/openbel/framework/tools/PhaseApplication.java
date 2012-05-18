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

import static java.lang.Runtime.getRuntime;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.err;
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.timeFormat;
import static org.openbel.framework.common.Strings.DIRECTORY_CREATION_FAILED;
import static org.openbel.framework.common.Strings.SYSTEM_CONFIG_PATH;
import static org.openbel.framework.common.Strings.TIME_HELP;
import static org.openbel.framework.common.Strings.WARNINGS_AS_ERRORS;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.common.enums.ExitCode.GENERAL_FAILURE;
import static org.openbel.framework.core.StandardOptions.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.Option;
import org.openbel.framework.common.BELRuntimeException;
import org.openbel.framework.common.Reportable;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.cfg.RuntimeConfiguration;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.core.CommandLineApplication;
import org.openbel.framework.core.XBELConverterService;
import org.openbel.framework.core.XBELConverterServiceImpl;
import org.openbel.framework.core.XBELValidatorService;
import org.openbel.framework.core.XBELValidatorServiceImpl;
import org.xml.sax.SAXException;

/**
 * Base class for BEL phase compilers.
 */
public abstract class PhaseApplication extends CommandLineApplication {
    private final static String INDENT = "    ";

    /**
     * The artifact path.
     */
    protected File artifactPath;

    /**
     * The output directory.
     */
    protected File outputDirectory;

    /**
     * Time command-line argument short option, {@value #SHORT_OPT_TIME}.
     *
     * @deprecated In favor of {@link #LONG_OPT_TIME} only
     */
    @Deprecated
    protected final static String SHORT_OPT_TIME = "t";

    /**
     * Pedantic command-line argument long option, {@value #LONG_OPT_PEDANTIC}.
     */
    protected final static String LONG_OPT_PEDANTIC = "pedantic";

    private long starttime;
    private long endtime;

    /**
     * @param args
     * @param configs
     */
    public PhaseApplication(String[] args) {
        super(args);

        final SimpleOutput reportable = new SimpleOutput();
        reportable.setErrorStream(System.err);
        reportable.setOutputStream(System.out);
        setReportable(reportable);

        setOptions(getPhaseConfiguration());

        // Initialize the system configuration either through the
        // command-line or system defaults.
        initializeSystemConfiguration();
        SystemConfiguration syscfg = getSystemConfiguration();
        outputDirectory = syscfg.getWorkingDirectory();
    }

    public void start() {
        starttime = currentTimeMillis();
    }

    public void stop() {
        endtime = currentTimeMillis();

        final StringBuilder bldr = new StringBuilder();
        bldr.append(INDENT);
        bldr.append("=== Phase Complete === ");
        markTime(bldr, starttime, endtime);
        output(bldr.toString());
    }

    /**
     * Fail with a {@link ExitCode#GENERAL_FAILURE general failure}, printing
     * the application's usage.
     */
    public void failUsage() {
        printUsage();
        bail(GENERAL_FAILURE);
    }

    /**
     * Returns a list of phase-agnostic command-line options.
     *
     * @return List of options
     */
    @Override
    public List<Option> getCommandLineOptions() {
        final List<Option> ret = new LinkedList<Option>();

        String help;

        help = TIME_HELP;
        ret.add(new Option(SHORT_OPT_TIME, LONG_OPT_TIME, false, help));

        help = WARNINGS_AS_ERRORS;
        ret.add(new Option(null, LONG_OPT_PEDANTIC, false, help));

        help = SYSTEM_CONFIG_PATH;
        Option o = new Option(SHRT_OPT_SYSCFG, LONG_OPT_SYSCFG, true, help);
        o.setArgName(ARG_SYSCFG);
        ret.add(o);

        return ret;
    }

    /**
     * Returns the phase options.
     *
     * @return {@link RuntimeConfiguration}
     */
    public abstract RuntimeConfiguration getPhaseConfiguration();

    /**
     * Returns {@code true} if the command-line arguments are valid for this
     * phase, {@code false} otherwise.
     *
     * @param args Command-line arguments
     * @return boolean
     */
    public abstract boolean validCommandLine();

    /**
     * Returns {@link #getPhaseConfiguration()}
     * {@link RuntimeConfiguration#isTime()}, a delegate method for convenience.
     *
     * @return boolean
     */
    protected boolean withTime() {
        return getPhaseConfiguration().isTime();
    }

    /**
     * Returns {@link #getPhaseConfiguration()}
     * {@link RuntimeConfiguration#isDebug()}, a delegate method for
     * convenience.
     *
     * @return boolean
     */
    protected boolean withDebug() {
        return getPhaseConfiguration().isDebug();
    }

    /**
     * Returns {@link #getPhaseConfiguration()}
     * {@link RuntimeConfiguration#isVerbose()}, a delegate method for
     * convenience.
     *
     * @return boolean
     */
    protected boolean withVerbose() {
        return getPhaseConfiguration().isVerbose();
    }

    /**
     * Returns {@link #getPhaseConfiguration()}
     * {@link RuntimeConfiguration#isWarningsAsErrors()}, a delegate method for
     * convenience.
     *
     * @return boolean
     */
    protected boolean withPedantic() {
        return getPhaseConfiguration().isWarningsAsErrors();
    }

    /**
     * Sets the phase-agnostic runtime configuration.
     */
    private void setOptions(RuntimeConfiguration rc) {
        if (hasOption(SHORT_OPT_TIME)) {
            rc.setTime(true);
        }
        if (hasOption(LONG_OPT_DEBUG)) {
            rc.setDebug(true);
        }
        if (hasOption(LONG_OPT_VERBOSE)) {
            rc.setVerbose(true);
        }
        if (hasOption(LONG_OPT_PEDANTIC)) {
            rc.setWarningsAsErrors(true);
        }
    }

    /**
     * Sends {@code "=== <header> ==="} to {@link Reportable#output(String...)
     * output}
     *
     * @param header Non-null header
     * @param phase Non-null phase
     * @param stage Non-null stage
     */
    protected void beginStage(final String header, final String stage,
            final String numStages) {
        boolean print = getPhaseConfiguration().isVerbose();
        if (print) {
            StringBuilder bldr = new StringBuilder();
            bldr.append(INDENT);
            bldr.append("Stage ");
            bldr.append(stage);
            bldr.append(" of ");
            bldr.append(numStages);
            bldr.append(": ");
            bldr.append(header);
            reportable.output(bldr.toString());
        }
    }

    /**
     * Prints phase output.
     *
     * @param output Output
     */
    protected void phaseOutput(final String output) {
        output(INDENT.concat(output));
    }

    /**
     * Prints stage output with two levels of indentation.
     *
     * @param output Output
     */
    protected void stageOutput(final String output) {
        boolean print = getPhaseConfiguration().isVerbose();
        if (print) {
            output(INDENT + INDENT + output);
        }
    }

    /**
     * Prints stage error with two levels of indentation and {@code "[ERROR]: "}
     * prefix.
     *
     * @param error Error
     */
    protected void stageError(final String error) {
        error(INDENT + INDENT + "[ERROR]: " + error);
    }

    /**
     * Prints stage warning with two levels of indentation and
     * {@code "[WARNING]: "} prefix.
     *
     * @param warning Warning
     */
    protected void stageWarning(final String warning) {
        warning(INDENT + INDENT + "[WARNING]: " + warning);
    }

    /**
     * Creates the directory artifact, failing if the directory couldn't be
     * created.
     *
     * @param outputDirectory Phase output directory
     * @param artifact Directory artifact to be created
     */
    protected final File createDirectoryArtifact(final File outputDirectory,
            final String artifact) {
        // Construct artifact path based on output directory
        String path = asPath(outputDirectory.getAbsolutePath(), artifact);
        final File ret = new File(path);
        if (!ret.isDirectory()) {
            // Fail if the directory artifact couldn't be created
            if (!ret.mkdir()) {
                error(DIRECTORY_CREATION_FAILED + ret);
                bail(ExitCode.BAD_OUTPUT_DIRECTORY);
            }
        }
        return ret;
    }

    /**
     * Invokes {@link Reportable#error(String...)} with {@code errors}.
     *
     * @param errors Error strings
     * @see org.openbel.framework.common.Reportable#error(java.lang.String[])
     */
    public void error(String... errors) {
        reportable.error(errors);
    }

    /**
     * Invokes {@link Reportable#warning(String...)} with {@code warnings}.
     *
     * @param warnings Warning strings
     * @see org.openbel.framework.common.Reportable#warning(java.lang.String[])
     */
    public void warning(String... warnings) {
        reportable.warning(warnings);
    }

    /**
     * Invokes {@link Reportable#output(String...)} with {@code outputs}.
     *
     * @param outputs Output strings
     * @see org.openbel.framework.common.Reportable#output(java.lang.String[])
     */
    public void output(String... outputs) {
        reportable.output(outputs);
    }

    /**
     * Appends {@code (#.### seconds)} to the builder, if
     * {@link RuntimeConfiguration#isTime() runtime configuration timing} is
     * set.
     *
     * @param b Non-null builder
     * @param t1 Time at {@code t1}
     * @param t2 Time at {@code t2}
     */
    protected void markTime(final StringBuilder b, long t1, long t2) {
        boolean time = getPhaseConfiguration().isTime();
        if (time) {
            String seconds = timeFormat((t2 - t1));
            b.append("(");
            b.append(seconds);
            b.append(" seconds) ");
        }
    }

    /**
     * Marks the end of a stage by appending a newline to the builder.
     *
     * @param b Non-null builder
     */
    protected void markEndStage(final StringBuilder b) {
        b.append("\n");
    }

    /**
     * Reports {@link ExitCode#toString()} to error, and invokes
     * {@link #bail(ExitCode)} with the provided exit code.
     *
     * @param e Exit code
     */
    protected void exit(ExitCode e) {
        reportable.error(e.toString());
        bail(e);
    }

    /**
     * Directs phase application lifecycle.
     *
     * @param phase Phase application
     */
    protected static void harness(PhaseApplication phase) {
        try {
            phase.start();
            phase.stop();
            phase.end();
        } catch (BELRuntimeException bre) {
            err.println(bre.getUserFacingMessage());
            systemExit(bre.getExitCode());
        } catch (OutOfMemoryError oom) {
            err.println();
            oom.printStackTrace();
            long upperlimit = getRuntime().maxMemory();
            double ulMB = upperlimit * 9.53674316e-7;
            final NumberFormat fmt = new DecimalFormat("#0");
            String allocation = fmt.format(ulMB);
            err.println("\n(current allocation is " + allocation + " MB)");
            systemExit(ExitCode.OOM_ERROR);
        }
    }

    protected XBELValidatorService createValidator() {
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

    protected XBELConverterService createConverter() {
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
