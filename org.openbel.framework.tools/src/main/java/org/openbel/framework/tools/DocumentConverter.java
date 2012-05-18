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
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.Strings.UTF_8;
import static org.openbel.framework.common.enums.ExitCode.GENERAL_FAILURE;
import static org.openbel.framework.core.StandardOptions.LONG_OPT_DEBUG;
import static org.openbel.framework.core.StandardOptions.SHORT_OPT_VERBOSE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.Option;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openbel.bel.model.BELParseErrorException;
import org.openbel.bel.model.BELParseWarningException;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.bel.converters.BELDocumentConverter;
import org.openbel.framework.common.bel.parser.BELParseResults;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.belscript.BELScriptExporter;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.xbel.parser.XBELConverter;
import org.openbel.framework.core.CommandLineApplication;
import org.openbel.framework.core.XBELConverterServiceImpl;
import org.openbel.framework.core.compiler.ValidationError;
import org.xml.sax.SAXException;

public final class DocumentConverter extends CommandLineApplication {

    private static final String DOCUMENT_CONVERTER_NAME = "Document Converter";
    private static final String DOCUMENT_CONVERTER_DESC =
            "Convert between BEL and XBEL formats";

    private static final String LONG_OPT_NO_PRESERVE = "no-preserve";
    private static final String USAGE_OUTPUT_FILE =
            " [{-o | --output-file} <output file name> [-f | --force | --no-preserve] ]";
    private static final String USAGE_TYPE =
            " [{-t | --type} {bel | BEL | xbel | XBEL}]";

    private static final String BEL_EXTENSION = ".bel";
    private static final String XBEL_EXTENSION = ".xbel";

    // Flags specified by command line options
    private boolean verbose, debug;

    /**
     * Static main method to launch the Document Converter tool.
     *
     * @param args {@link String String[]} the command-line arguments
     */
    public static void main(String[] args) {
        DocumentConverter app = new DocumentConverter(args);
        app.run();
    }

    public DocumentConverter(String[] args) {
        super(args, false);

        final SimpleOutput reportable = new SimpleOutput();
        reportable.setErrorStream(System.err);
        // Normal output is written to stderr because the output document
        // may be written to stdout.
        reportable.setOutputStream(System.err);
        setReportable(reportable);

        printApplicationInfo();

        if (hasOption('h')) {
            printHelp(true);
        }
    }

    private void run() {
        verbose = hasOption(SHORT_OPT_VERBOSE);
        debug = hasOption(LONG_OPT_DEBUG);

        final List<String> extraArgs = getExtraneousArguments();

        // Determine the input document file path
        if (extraArgs.size() == 0) {
            reportable.error("No documents specified.");
            end();
        } else if (extraArgs.size() > 1) {
            fatal("Only a single document can be specified.");
        }
        final String inputFileName = extraArgs.get(0);

        // Check that the input file exists
        final File inputFile = new File(inputFileName);
        if (!inputFile.exists()) {
            fatal("Input file does not exist - "
                    + inputFile.getAbsolutePath());
        }

        // Determine the format of the input document
        final DocumentFormat format = determineType(inputFile);
        if (format == null) {
            fatal("Could not determine the format of the input file '"
                    + inputFileName + "'.  " +
                    "Please specify '-t BEL' or '-t XBEL'.");
        }

        OutputStream output = null;
        if (hasOption("o")) {
            final String outputFilename = getOptionValue("o");
            final boolean noPreserve =
                    (hasOption("f") || hasOption(LONG_OPT_NO_PRESERVE));
            final File outputFile = new File(outputFilename);

            if (!noPreserve && outputFile.exists()) {
                fatal("File '" + outputFilename + "' exists, specify --"
                        + LONG_OPT_NO_PRESERVE + " to override.");
            } else if (inputFile.equals(outputFile)) {
                fatal("The input file cannot be the same as the output file.");
            } else {
                // Create a file to write the output document
                try {
                    outputFile.createNewFile();
                    output = new FileOutputStream(outputFile);
                } catch (IOException ex) {
                    bailOnException(ex);
                }
            }
        } else {
            output = System.out;
        }

        if (format == DocumentFormat.BEL) {
            convertBelToXbel(inputFile, output);
        } else if (format == DocumentFormat.XBEL) {
            convertXbelToBel(inputFile, output);
        }
    }

    private DocumentFormat determineType(final File inputFile) {
        DocumentFormat format = null;
        if (hasOption("t")) {
            format = DocumentFormat.getFormatByLabel(getOptionValue("t"));
        }
        if (format == null) {
            final int index = inputFile.getName().lastIndexOf(".");
            if (index == -1) {
                return null;
            }

            final String extension = inputFile.getName().substring(index);
            if (extension.equals(BEL_EXTENSION)) {
                format = DocumentFormat.BEL;
            } else if (extension.equals(XBEL_EXTENSION)) {
                format = DocumentFormat.XBEL;
            }
        }
        return format;
    }

    private void convertBelToXbel(final File inputFile,
            final OutputStream output) {
        try {
            final String belText = FileUtils.readFileToString(inputFile, UTF_8);
            final BELParseResults result = BELParser.parse(belText);

            // Report any BEL parser warnings or errors if verbose mode is enabled
            if (verbose) {
                final List<BELParseWarningException> warnings =
                        result.getSyntaxWarnings();
                final List<BELParseErrorException> errors =
                        result.getSyntaxErrors();
                final String inputFilePath = inputFile.getAbsolutePath();

                if (hasItems(warnings)) {
                    final int numWarnings = warnings.size();
                    reportable.output(format("%d BEL parser warning%s:",
                            numWarnings, (numWarnings == 1 ? "" : "s")));
                    int i = 0;
                    for (BELParseWarningException warning : warnings) {
                        final ValidationError err = new ValidationError(
                                inputFilePath,
                                warning.getMessage(),
                                warning.getLine(),
                                warning.getCharacter());
                        reportable.output(format("%5d. %s", ++i,
                                err.getUserFacingMessage()));
                    }
                }
                if (hasItems(errors)) {
                    final int numErrors = errors.size();
                    reportable.output(format("%d BEL parser error%s:",
                            numErrors, (numErrors == 1 ? "" : "s")));
                    int i = 0;
                    for (BELParseErrorException error : errors) {
                        final ValidationError err = new ValidationError(
                                inputFilePath,
                                error.getMessage(),
                                error.getLine(),
                                error.getCharacter());
                        reportable.output(format("%5d. %s", ++i,
                                err.getUserFacingMessage()));
                    }
                }
            }

            final Document bel =
                    new BELDocumentConverter().convert(result.getDocument());

            final org.openbel.framework.common.xbel.converters.DocumentConverter converter =
                    new org.openbel.framework.common.xbel.converters.DocumentConverter();

            new XBELConverter().marshal(converter.convert(bel), output);

        } catch (JAXBException ex) {
            close(output);
            bailOnException(ex);
        } catch (IOException ex) {
            close(output);
            bailOnException(ex);
        } catch (RuntimeException ex) {
            close(output);
            bailOnException(ex);
        }
    }

    private void convertXbelToBel(final File inputFile,
            final OutputStream output) {
        try {
            final boolean useShortForm = false;
            final BELScriptExporter exporter = new BELScriptExporter();
            exporter.setUseShortForm(useShortForm);
            exporter.export(new XBELConverterServiceImpl().toCommon(inputFile),
                    output);

        } catch (SAXException ex) {
            close(output);
            bailOnException(ex);
        } catch (URISyntaxException ex) {
            close(output);
            bailOnException(ex);
        } catch (IOException ex) {
            close(output);
            bailOnException(ex);
        } catch (JAXBException ex) {
            close(output);
            bailOnException(ex);
        } catch (RuntimeException ex) {
            close(output);
            bailOnException(ex);
        }
    }

    private void close(final OutputStream os) {
        if (os != System.out && os != System.err) {
            try {
                os.close();
            } catch (IOException ex) {}
        }
    }

    private void bailOnException(final Exception e) {
        final Throwable cause = ExceptionUtils.getRootCause(e);
        reportable.error("Unable to run " + DOCUMENT_CONVERTER_NAME);
        final String reason =
                (cause == null ? e.getMessage() : cause.getMessage());
        reportable.error("Reason: "
                +
                (reason != null ? reason
                        : "none.  Specify --debug for more information."));
        if (debug) {
            e.printStackTrace(reportable.errorStream());
        }
        bail(GENERAL_FAILURE);
    }

    @Override
    public String getApplicationName() {
        return DOCUMENT_CONVERTER_NAME;
    }

    @Override
    public String getApplicationShortName() {
        return DOCUMENT_CONVERTER_NAME;
    }

    @Override
    public String getApplicationDescription() {
        return DOCUMENT_CONVERTER_DESC;
    }

    @Override
    public String getUsage() {
        final StringBuilder bldr = new StringBuilder();
        bldr.append(" <BEL or XBEL document name>")
                .append(USAGE_OUTPUT_FILE)
                .append(USAGE_TYPE)
                .append(" [-h | --help] [-v | --verbose]");
        return bldr.toString();
    }

    @Override
    public List<Option> getCommandLineOptions() {
        List<Option> options = new LinkedList<Option>();

        // -f/--force/--no-preserve used with -o/--output-file to force the
        // file to be overwritten if it already exists
        final String noPreserveHelp =
                "Optional.  If specified, if a file with the same name as the output file "
                        +
                        "already exists then it will be overwritten.\n"
                        +
                        "The default is always to preserve the existing file and "
                        +
                        "generate a warning.";
        options.add(new Option("f", "force", false,
                "Identical to --no-preserve."));
        options.add(new Option(null, LONG_OPT_NO_PRESERVE, false,
                noPreserveHelp));

        // -o/--output-file used to set the file to write the new BEL or XBEL document
        final String outputFileHelp =
                "Optional.  If present, it indicates the name of the output file "
                        +
                        "to write the converted BEL or XBEL document to.";
        options.add(new Option("o", "output-file", true, outputFileHelp));

        // -t/--type used to specify whether the input file is BEL (or XBEL), and
        // therefore whether the output file will be XBEL (or BEL).
        final String typeHelp =
                "Specifies the format of the input file.  By default, "
                        + getApplicationShortName()
                        +
                        " infers the format of the document by "
                        +
                        "its extension ("
                        + BEL_EXTENSION
                        + " or "
                        + XBEL_EXTENSION
                        + ").\n"
                        +
                        "If BEL then the input document is expected to be in BEL format and the "
                        +
                        "output document will be in XBEL format.  If XBEL then the input document "
                        +
                        "is expected to be in XBEL format and the output document will be in BEL format.";
        options.add(new Option("t", "type", true, typeHelp));

        // The base class CommandLineApplication will add the --debug, -h/--help,
        // and -v/--verbose options.

        return options;
    }

    public static enum DocumentFormat {
        BEL, XBEL;

        public static DocumentFormat getFormatByLabel(final String label) {
            if (label.equals("bel") || label.equals("BEL")) {
                return BEL;
            } else if (label.equals("xbel") || label.equals("XBEL")) {
                return XBEL;
            } else {
                return null;
            }
        }
    }

    /*
     * Override the printUsage() methods to avoid using stdout to
     * output anything other than a converted BEL or XBEL document.
     *
     * @see org.openbel.framework.clf.CommandLineApplication#printUsage()
     */

    @Override
    public void printUsage() {
        super.printUsage(reportable.outputStream());
    }

    @Override
    public void printUsage(final OutputStream os) {
        super.printUsage(reportable.outputStream());
    }
}
