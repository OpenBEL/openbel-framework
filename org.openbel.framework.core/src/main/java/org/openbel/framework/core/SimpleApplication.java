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
package org.openbel.framework.core;

import static java.lang.System.err;
import static java.lang.System.exit;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.enums.ExitCode.GENERAL_FAILURE;
import static org.openbel.framework.common.enums.ExitCode.PARSE_ERROR;
import static org.openbel.framework.common.enums.ExitCode.SUCCESS;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openbel.framework.common.enums.ExitCode;

/**
 * Base class for simple command-line applications.
 * <p>
 * This class is a clean design of {@link CommandLineApplication}.
 * CommandLineApplication has three characteristics that make it undesirable to
 * use:
 * <ul>
 * <li>Spring</li>
 * <li>Logging</li>
 * <li>Has hacks put in place to support "special" functionality</li>
 * </ul>
 * </p>
 */
public abstract class SimpleApplication {
    private final CommandLineParser cliParser;
    private final Options cliOptions;
    private final String[] cliArgs;
    private final CommandLine commandLine;

    /**
     * Constructs the simple application with the command-line arguments.
     * 
     * @param args Command-line arguments
     */
    public SimpleApplication(final String[] args) {
        cliParser = new GnuParser();
        cliOptions = new Options();
        this.cliArgs = args;

        List<Option> opts = getCommandLineOptions();
        if (opts != null) {
            for (final Option opt : opts) {
                cliOptions.addOption(opt);
            }
        }

        addOption("h", "help", "prints this help");

        CommandLine cl = null;
        try {
            cl = cliParser.parse(cliOptions, cliArgs);
        } catch (ParseException e) {
            err.println(e.getMessage());
            printHelp(cliOptions, false, err);
            bail(PARSE_ERROR);
        }
        commandLine = cl;

        if (hasOption('h')) {
            printHelp(cliOptions, true, out);
        }
    }

    /**
     * Returns the non-null application name.
     * 
     * @return Non-null string
     */
    public abstract String getApplicationName();

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
     * Adds a command-line option.
     * 
     * @param shortOpt Short, one character option (e.g., {@code -t})
     * @param longOpt Long, one or two word option (e.g., {@code --long-option})
     * @param desc Option description (e.g., {@code does something great})
     */
    public final void addOption(String shortOpt, String longOpt, String desc) {
        cliOptions.addOption(new Option(shortOpt, longOpt, false, desc));
    }

    /**
     * Adds a command-line option.
     * 
     * @param shortOpt Short, one character option (e.g., {@code -t})
     * @param desc Option description (e.g., {@code does something great})
     */
    public final void addOption(String shortOpt, String desc) {
        cliOptions.addOption(new Option(shortOpt, desc));
    }

    /**
     * Adds a command-line option.
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
     * Adds a command-line option.
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
     * Adds a command-line option.
     * 
     * @param o Option
     */
    public final void addOption(final Option o) {
        cliOptions.addOption(o);
    }

    /**
     * Adds a command-line option group.
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
     * Write command-line help to {@code stdout}.
     * 
     * @param o Options
     * @param exit Exit flag
     */
    private void printHelp(Options o, boolean exit) {
        printHelp(o, exit, out);
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
        hf.printHelp(pw, 80, syntax, header, o, 2, 2, null, false);
        pw.flush();
        if (exit) {
            bail(SUCCESS);
        }

    }

    /**
     * Exits, with {@link ExitCode#SUCCESS}.
     */
    protected final void exitSuccess() {
        bail(SUCCESS);
    }

    /**
     * Prints the provided message to {@code stderr}, and invokes
     * {@link #bail(ExitCode)} with {@link ExitCode#GENERAL_FAILURE}.
     * 
     * @param msg Message
     */
    protected final void fatal(final String msg) {
        err.println(msg);
        bail(GENERAL_FAILURE);
    }

    /**
     * Stops the application's context and invokes {@link #exit(ExitCode) exit}
     * with the provided status code.
     * 
     * @param e Exit code
     */
    protected final void bail(final ExitCode e) {
        systemExit(e);
    }

    /**
     * The <b>ONLY</b> call to {@link System#exit(int)}!
     * 
     * @param e Exit code
     */
    protected final static void systemExit(final ExitCode e) {
        exit(e.getValue());
    }
}
