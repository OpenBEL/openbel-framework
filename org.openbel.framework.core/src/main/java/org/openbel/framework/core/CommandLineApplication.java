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
package org.openbel.framework.core;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.PathConstants.SLF4J_LOGGER_FILE;
import static org.openbel.framework.common.Strings.DEBUG_HELP;
import static org.openbel.framework.common.Strings.ERROR_INIT_LOGGING;
import static org.openbel.framework.common.Strings.SYSCFG_READ_FAILURE;
import static org.openbel.framework.common.Strings.VERBOSE_HELP;
import static org.openbel.framework.common.cfg.SystemConfiguration.createSystemConfiguration;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;
import static org.openbel.framework.common.enums.ExitCode.GENERAL_FAILURE;
import static org.openbel.framework.common.enums.ExitCode.PARSE_ERROR;
import static org.openbel.framework.common.enums.ExitCode.SUCCESS;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_DEBUG;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_SYSCFG;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_VERBOSE;
import static org.openbel.framework.core.StandardOptions.SHORT_OPT_VERBOSE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openbel.framework.common.BELRuntimeException;
import org.openbel.framework.common.Reportable;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.common.enums.BELFrameworkVersion;
import org.openbel.framework.common.enums.ExitCode;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * Base class for BEL command-line applications.
 * <p>
 * <ol>
 * <li>The only call to {@link System#exit(int)} should be done in
 * {@link #systemExit(ExitCode)}.</li>
 * <li>Command-line applications should support output/error redirection to
 * streams beyond {@code stdout} and {@code stderr}.</li>
 * <li>Command-line applications should needs to be constructed before any
 * logging statement are executed.</li>
 * </ol>
 * </p>
 * <p>
 * BEL command-line applications are built using a mix of Java and XML-centric
 * container configuration mechanisms (for more information, see <a href=
 * "http://static.springsource.org/spring/docs/3.0.x/reference/beans.html">The
 * IoC container</a>).
 * </p>
 * <p>
 * This approach has a number of advantages:
 * <ul>
 * <li>It is simple to create new command-line applications</li>
 * <li>Deriving from command-line applications is feasible and straightforward</li>
 * <li>Unmatched debugging capabilities</li>
 * <li>Execution times can be very quick, providing typical command-line
 * execution speeds</li>
 * </ul>
 * </p>
 */
public abstract class CommandLineApplication {

    /**
     * Error, warnings, and outputs.
     */
    protected Reportable reportable;

    private final CommandLineParser cliParser;
    private final Options cliOptions;
    private final String[] cliArgs;

    /**
     * System configuration; may be null.
     *
     * @see #initializeSystemConfiguration()
     * @see #attemptSystemConfiguration()
     */
    protected SystemConfiguration syscfg;

    /**
     * The command line, post argument processing.
     */
    private CommandLine commandLine;

    /**
     * Defines the {@link Logger} that can be used by the concrete
     * {@link CommandLineApplication}.
     */
    private final Logger cliLogger;

    /**
     * Creates a command-line application with the provided command-line
     * arguments to be parsed.
     *
     * @param args Command-line arguments, derived from {@code main}
     */
    public CommandLineApplication(final String[] args) {
        this(args, true);
    }

    protected CommandLineApplication(final String[] args,
            final boolean checkForHelp) {
        cliParser = new AntelopeParser();
        cliOptions = new Options();
        this.cliArgs = args;

        List<Option> opts = getCommandLineOptions();
        if (opts != null) {
            for (final Option opt : opts) {
                cliOptions.addOption(opt);
            }
        }
        addOption(new Option(null, LONG_OPT_DEBUG, false, DEBUG_HELP));
        addOption(new Option(SHORT_OPT_VERBOSE, LONG_OPT_VERBOSE, false,
                VERBOSE_HELP));
        addOption("h", "help", "Shows command line options.");

        try {
            // TODO -- parse() should NOT be stopping at non-options
            // (false arg)
            commandLine = cliParser.parse(cliOptions, cliArgs, false);
        } catch (ParseException e) {
            err.println(e.getMessage());
            printHelp(cliOptions, false, err);
            bail(PARSE_ERROR);
        }

        if (checkForHelp && hasOption('h')) {
            printHelp(cliOptions, true, out);
        }

        ILoggerFactory lf = LoggerFactory.getILoggerFactory();

        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext((Context) lf);
        try {
            InputStream is = getClass().getResourceAsStream(SLF4J_LOGGER_FILE);
            jc.doConfigure(is);
        } catch (JoranException e) {
            String msg = format(ERROR_INIT_LOGGING, e.getMessage());
            err.println(msg);
        }

        cliLogger = lf.getLogger(Logger.ROOT_LOGGER_NAME);
        ((ch.qos.logback.classic.Logger) cliLogger).detachAppender("console");

        reportable = null;
    }

    protected void setReportable(Reportable reportable) {
        if (reportable != null) {
            this.reportable = reportable;
        } else {
            SimpleOutput so = new SimpleOutput();
            so.setErrorStream(err);
            so.setOutputStream(out);
            this.reportable = so;
        }
    }

    protected Reportable getReportable() {
        return reportable;
    }

    /**
     * Returns the non-null application name.
     *
     * @return Non-null string
     */
    public abstract String getApplicationName();

    /**
     * Returns the non-null application <i>short name</i>.
     *
     * @return Non-null string
     */
    public abstract String getApplicationShortName();

    /**
     * Returns the non-null application description.
     *
     * @return Non-null string
     */
    public abstract String getApplicationDescription();

    /**
     * Returns a detailed description of the application's command-line.
     * <p>
     * Optional arguments should be wrapped in {@code [] brackets}. For example:
     * {@code [-o somearg]}. Arguments that can occur more than once should be
     * followed with ellipses ({@code ...}), for example: {@code [-f FILE...]}.
     * </p>
     *
     * @return Non-null string
     */
    public abstract String getUsage();

    /**
     * Returns the command-line arguments.
     *
     * @return Command-line arguments
     */
    protected final String[] getCommandLineArguments() {
        return cliArgs;
    }

    /**
     * Returns the command-line application {@link Logger} instance.
     *
     * @return {@link Logger}, the command-line application logger
     */
    protected final Logger getLogger() {
        return cliLogger;
    }

    /**
     * Returns the number of command-line arguments, or {@code 0} if none are
     * available.
     *
     * @return int
     */
    public final int getNumberOfCommandLineArgs() {
        if (commandLine == null) {
            return 0;
        }
        List<?> args = commandLine.getArgList();
        if (args == null) {
            return 0;
        }
        return args.size();
    }

    /**
     * Returns the extraneous command-line arguments.
     *
     * @return Non-null list of strings, may be empty
     */
    protected final List<String> getExtraneousArguments() {
        if (commandLine == null) {
            return emptyList();
        }
        String[] args = commandLine.getArgs();
        if (noItems(args)) {
            return emptyList();
        }
        return asList(args);
    }

    /**
     * Returns {@code true} if extraneous arguments are present, {@code false}
     * otherwise.
     *
     * @return boolean
     */
    protected final boolean hasExtraneousArguments() {
        return !getExtraneousArguments().isEmpty();
    }

    /**
     * Returns {@code true} if the option specified by {@code name} has been
     * set, {@code false} otherwise.
     *
     * @param name Single character option name
     * @return boolean
     */
    protected final boolean hasOption(final char name) {
        return commandLine.hasOption(name);
    }

    /**
     * Returns {@code true} if the option specified by {@code name} has been
     * set, {@code false} otherwise.
     *
     * @param name string name
     * @return boolean
     */
    protected final boolean hasOption(final String name) {
        return commandLine.hasOption(name);
    }

    /**
     * Returns the option value for the option specified by {@code name}.
     *
     * @param name Single character option name
     * @return String, which may be null
     */
    protected final String getOptionValue(final char name) {
        return commandLine.getOptionValue(name);
    }

    /**
     * Returns the option values for the option specified by {@code name}.
     *
     * @param name Option name
     * @return String[]
     */
    protected final String[] getOptionValues(final String name) {
        return commandLine.getOptionValues(name);
    }

    /**
     * Returns the option value for the option specified by {@code name}.
     *
     * @param name Option string name
     * @return String, which may be null
     */
    protected final String getOptionValue(final String name) {
        return commandLine.getOptionValue(name);
    }

    /**
     * Returns the ordered, parsed command-line options.
     *
     * @return {@link Option}[] the ordered options array
     */
    protected final Option[] getOptions() {
        return commandLine.getOptions();
    }

    /**
     * Adds a command-line option. This is only useful before calling
     * {@link #start() start}.
     *
     * @param shortOpt Short, one character option (e.g., {@code -t})
     * @param longOpt Long, one or two word option (e.g., {@code --long-option})
     * @param desc Option description (e.g., {@code does something great})
     */
    public final void addOption(String shortOpt, String longOpt, String desc) {
        cliOptions.addOption(new Option(shortOpt, longOpt, false, desc));
    }

    /**
     * Adds a command-line option. This is only useful before calling
     * {@link #start() start}.
     *
     * @param shortOpt Short, one character option (e.g., {@code -t})
     * @param desc Option description (e.g., {@code does something great})
     */
    public final void addOption(String shortOpt, String desc) {
        cliOptions.addOption(new Option(shortOpt, desc));
    }

    /**
     * Adds a command-line option. This is only useful before calling
     * {@link #start() start}.
     *
     * @param shortOpt Short, one character option (e.g., {@code -t})
     * @param desc Option description (e.g., {@code does something great})
     * @param hasArg boolean {@code true} if the option requires an argument,
     * {@code false} otherwise
     */
    public final void addOption(String shortOpt, String desc, boolean hasArg) {
        cliOptions.addOption(new Option(shortOpt, hasArg, desc));
    }

    /**
     * Adds a command-line option. This is only useful before calling
     * {@link #start() start}.
     *
     * @param shortOpt Short, one character option (e.g., {@code -t})
     * @param longOpt Long, one or two word option (e.g., {@code --long-option})
     * @param desc Option description (e.g., {@code does something great})
     * @param hasArg boolean {@code true} if the option requires an argument,
     * {@code false} otherwise
     */
    public final void addOption(String shortOpt, String longOpt, String desc,
            boolean hasArg) {
        cliOptions.addOption(new Option(shortOpt, longOpt, hasArg, desc));
    }

    /**
     * Adds a command-line option. This is only useful before calling
     * {@link #start() start}.
     *
     * @param o Option
     */
    public final void addOption(final Option o) {
        cliOptions.addOption(o);
    }

    /**
     * Adds a command-line option group. This is only useful before calling
     * {@link #start() start}.
     *
     * @param o Option group
     */
    public final void addOptionGroup(final OptionGroup o) {
        cliOptions.addOptionGroup(o);
    }

    /**
     * Returns command-line options used by the application.
     * <p>
     * Options specifies by subclasses should adhere to the convention of a
     * single-character short option with an optional long option.
     * </p>
     *
     * @return List of options, may be null or empty
     * @see #addOption(String, String, String, boolean)
     */
    public abstract List<Option> getCommandLineOptions();

    /**
     * Prints out application information.
     */
    protected final void printApplicationInfo(final String applicationName) {
        final StringBuilder bldr = new StringBuilder();
        bldr.append("\n");
        bldr.append(BELFrameworkVersion.VERSION_LABEL).append(": ")
                .append(applicationName).append("\n");
        bldr.append("Copyright (c) 2011-2012, Selventa. All Rights Reserved.\n");
        bldr.append("\n");
        reportable.output(bldr.toString());
    }

    /**
     * Prints out application information, using the value returned
     * by {@link #getApplicationName() getApplicationName} as the printed
     * application name.
     * @see #printApplicationInfo(String)
     */
    protected final void printApplicationInfo() {
        printApplicationInfo(getApplicationName());
    }

    /**
     * Prints usage information to {@code stdout}.
     */
    public void printUsage() {
        printUsage(out);
    }

    /**
     * Prints usage information to the provided output stream.
     *
     * @param os Non-null output stream
     */
    public void printUsage(final OutputStream os) {
        final StringBuilder bldr = new StringBuilder();
        bldr.append("Usage: ");
        bldr.append(getUsage());
        bldr.append("\n");
        bldr.append("Try '--help' for more information.\n");
        PrintWriter pw = new PrintWriter(os);
        pw.write(bldr.toString());
        pw.close();
    }

    /**
     * Write command-line help to standard out and invokes
     * {@link #printHelp(Options, boolean)}.
     *
     * @param exit Exit flag
     */
    protected void printHelp(boolean exit) {
        printHelp(cliOptions, exit);
    }

    /**
     * Write command-line help to {@link Reportable#outputStream()}.
     *
     * @param o Options
     * @param exit Exit flag
     */
    private void printHelp(Options o, boolean exit) {
        printHelp(o, exit, reportable.outputStream());
    }

    /**
     * Write command-line help to the provided stream. If {@code exit} is
     * {@code true}, exit with status code {@link #EXIT_FAILURE}.
     *
     * @param o Options
     * @param exit Exit flag
     */
    private void printHelp(Options o, boolean exit, OutputStream os) {
        final PrintWriter pw = new PrintWriter(os);
        final String syntax = getUsage();
        String header = getApplicationName();
        header = header.concat("\nOptions:");
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp(pw, 100, syntax, header, o, 2, 2, null, false);
        pw.flush();
        if (exit) {
            bail(SUCCESS);
        }

    }

    /**
     * Exits, with {@link ExitCode#SUCCESS}.
     */
    protected final void end() {
        bail(SUCCESS);
    }

    /**
     * Prints the provided message to {@code stderr}, and invokes
     * {@link #bail(ExitCode)} with {@link ExitCode#GENERAL_FAILURE}.
     *
     * @param msg Message
     */
    protected final void fatal(final String msg) {
        reportable.error(msg);
        bail(GENERAL_FAILURE);
    }

    /**
     * Invokes {@link #exit(ExitCode) exit} with the provided status code.
     *
     * @param e Exit code
     */
    protected final void bail(final ExitCode e) {
        if (e.isFailure()) {
            final StringBuilder bldr = new StringBuilder();
            bldr.append("\n");
            bldr.append(getApplicationShortName());
            bldr.append(" failed with error ");
            bldr.append(e);
            bldr.append(".");
            err.println(bldr.toString());
        }

        systemExit(e);
    }

    /**
     * The <b>ONLY</b> call to {@link System#exit(int)}!
     *
     * @param e Exit code
     */
    protected final static void systemExit(final ExitCode e) {
        System.exit(e.getValue());
    }

    /**
     * Initializes the system configuration from either {@link #LONG_OPT_SYSCFG}
     * or system defaults.
     * <p>
     * This is an alternative means of initializing the system configuration for
     * applications interested in handling the possibility of an
     * {@link IOException}.
     * </p>
     *
     * @throws IOException Thrown if an I/O error occurs during initialization
     * of the system configuration
     * @see #initializeSystemConfiguration()
     */
    protected final void attemptSystemConfiguration() throws IOException {
        if (hasOption(LONG_OPT_SYSCFG)) {
            String syscfgLoc = getOptionValue(LONG_OPT_SYSCFG);
            createSystemConfiguration(new File(syscfgLoc));
        } else {
            createSystemConfiguration(null);
        }
        syscfg = getSystemConfiguration();
    }

    /**
     * Initializes the system configuration from either {@link #SHRT_OPT_SYSCFG}
     * or system defaults.
     * <p>
     * This method will initialize the system configuration or die trying.
     * </p>
     *
     * @throws IOException Thrown if an I/O error occurs during initialization
     * of the system configuration
     * @see #attemptSystemConfiguration()
     */
    protected final void initializeSystemConfiguration() {
        try {
            attemptSystemConfiguration();
        } catch (IOException e) {
            // Can't recover from this
            final String err = SYSCFG_READ_FAILURE;
            throw new BELRuntimeException(err, ExitCode.UNRECOVERABLE_ERROR, e);
        }
    }

    /**
     * This class is a commons-cli hack to change the default behavior of
     * command-line argument parsing. The library will throw
     * UnrecognizedOptionExceptions. We need unrecognized options to be ignored,
     * due to the way "belc" behaves.
     */
    public static class AntelopeParser extends GnuParser {

        /**
         * {@inheritDoc}
         */
        // Suppressing commons-cli use of raw types
        @SuppressWarnings("rawtypes")
        @Override
        protected void processOption(String arg0, ListIterator arg1)
                throws ParseException {
            boolean hasOption = getOptions().hasOption(arg0);
            if (!hasOption) {
                return;
            }
            super.processOption(arg0, arg1);
        }

    }

}
