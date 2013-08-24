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
package org.openbel.framework.tools;

import static java.lang.System.out;

import java.util.List;

import org.apache.commons.cli.Option;
import org.openbel.framework.common.SimpleOutput;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.lang.Signature;
import org.openbel.framework.core.CommandLineApplication;

/**
 * The <tt>Semantics</tt> BEL command-line application.
 *
 */
public final class Semantics extends CommandLineApplication {

    /**
     * Creates the application with the provided command-line arguments.
     *
     * @param args Command-line arguments
     */
    public Semantics(String[] args) {
        super(args);

        final SimpleOutput reportable = new SimpleOutput();
        reportable.setErrorStream(System.err);
        reportable.setOutputStream(System.out);
        setReportable(reportable);

        FunctionEnum[] values = FunctionEnum.values();

        // Print the number of functions.
        out.println("Number of functions: " + values.length);
        out.println();

        out.println("Function definitions:");
        // Print the function and function signatures.
        for (int i = 1; i <= values.length; i++) {
            FunctionEnum fe = values[i - 1];
            out.println("\t" + i + ": " + fe);
            out.println("\t\treturns: " + fe.getReturnType());
            out.println("\t\tsignatures:");
            for (final Signature sig : fe.getFunction().getSignatures()) {
                out.println("\t\t\t" + sig);
            }
            out.println();
        }
    }

    /**
     * Returns {@code "BEL Semantics"}.
     */
    @Override
    public String getApplicationName() {
        return "BEL Semantics";
    }

    /**
     * Returns {@code "Semantics"}.
     *
     * @return String
     */
    @Override
    public String getApplicationShortName() {
        return "Semantics";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationDescription() {
        return "Prints BEL semantics to the standard output stream.";
    }

    /**
     * Returns {@code ""}.
     */
    @Override
    public String getUsage() {
        return "";
    }

    /**
     * Returns {@code null}.
     */
    @Override
    public List<Option> getCommandLineOptions() {
        return null;
    }

    public static void main(String... args) {
        Semantics s = new Semantics(args);
        s.end();
    }
}
