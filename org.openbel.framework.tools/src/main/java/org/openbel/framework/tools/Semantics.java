/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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
