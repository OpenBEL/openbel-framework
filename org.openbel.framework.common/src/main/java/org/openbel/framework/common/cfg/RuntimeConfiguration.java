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
package org.openbel.framework.common.cfg;

import static java.lang.String.valueOf;
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.PathConstants.RC_FILENAMES;
import static org.openbel.framework.common.PathConstants.RC_PATHS;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openbel.framework.common.PathConstants;

/**
 * Provides a base class for runtime configuration capabilities.
 * <p>
 * Two types of configuration exist within the BEL framework, runtime
 * configuration and {@link SystemConfiguration system configuration}. Runtime
 * configuration allows access and modification of settings that affect
 * application behavior during runtime.
 * </p>
 */
public abstract class RuntimeConfiguration extends Configuration {

    /** Default debug value: {@code false} */
    public static final boolean DEFAULT_DEBUG = false;

    /** Debug setting name: {@value #DEBUG_DESC} */
    public static final String DEBUG_DESC = "debug";

    /** Default time value: {@code false} */
    public static final boolean DEFAULT_TIME = false;

    /** Time setting name: {@value #TIME_DESC} */
    public static final String TIME_DESC = "time";

    /** Default verbose value: {@code false} */
    public static final boolean DEFAULT_VERBOSE = false;

    /** Verbose setting name: {@value #VERBOSE_DESC} */
    public static final String VERBOSE_DESC = "verbose";

    /** Default warnings-as-errors value: {@code false} */
    public static final boolean DEFAULT_WARNINGS_AS_ERRORS = false;

    /** Warnings-as-errors setting name: {@value #WARNINGS_AS_ERRORS_DESC} */
    public static final String WARNINGS_AS_ERRORS_DESC = "warnings-as-errors";

    private boolean debug = DEFAULT_DEBUG;
    private boolean time = DEFAULT_TIME;
    private boolean verbose = DEFAULT_VERBOSE;
    private boolean warningsAsErrors = DEFAULT_WARNINGS_AS_ERRORS;

    /**
     * Creates a runtime configuration instance.
     * <p>
     * The configuration file to be used is the first file found while checking
     * each {@link PathConstants#RC_PATHS} / {@link PathConstants#RC_FILENAMES}
     * combination. If no runtime configuration file is found, it will be null.
     * </p>
     * 
     * @throws IOException Thrown if an I/O error occurs
     */
    public RuntimeConfiguration() throws IOException {
        super(decide());
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processSetting(String name, String value) {
        if (DEBUG_DESC.equals(name)) {
            debug = Boolean.valueOf(value);
        } else if (VERBOSE_DESC.equals(name)) {
            verbose = Boolean.valueOf(value);
        } else if (TIME_DESC.equals(name)) {
            time = Boolean.valueOf(value);
        } else if (WARNINGS_AS_ERRORS_DESC.equals(name)) {
            warningsAsErrors = Boolean.valueOf(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, String> defaults() {
        Map<String, String> ret = new HashMap<String, String>();
        ret.put(DEBUG_DESC, valueOf(DEFAULT_DEBUG));
        ret.put(TIME_DESC, valueOf(DEFAULT_TIME));
        ret.put(VERBOSE_DESC, valueOf(DEFAULT_VERBOSE));
        ret.put(WARNINGS_AS_ERRORS_DESC, valueOf(DEFAULT_WARNINGS_AS_ERRORS));
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeDefaults() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readComplete() {

    }

    /**
     * Returns {@code true} if debug is set, {@code false} otherwise.
     * 
     * @return boolean
     */
    public final boolean isDebug() {
        return debug;
    }

    /**
     * Sets the debug flag.
     * 
     * @param debug boolean
     */
    public final void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Returns {@code true} if time is set, {@code false} otherwise.
     * 
     * @return boolean
     */
    public final boolean isTime() {
        return time;
    }

    /**
     * Sets the time flag.
     * 
     * @param time boolean
     */
    public final void setTime(boolean time) {
        this.time = time;
    }

    /**
     * Returns {@code true} if verbose is set, {@code false} otherwise.
     * 
     * @return boolean
     */
    public final boolean isVerbose() {
        return verbose;
    }

    /**
     * Sets the verbose flag.
     * 
     * @param verbose boolean
     */
    public final void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Returns {@code true} if warnings-as-errors is set, {@code false}
     * otherwise.
     * 
     * @return boolean
     */
    public final boolean isWarningsAsErrors() {
        return warningsAsErrors;
    }

    /**
     * Sets the warnings-as-errors flag.
     * 
     * @param warningsAsErrors boolean
     */
    public final void setWarningsAsErrors(boolean warningsAsErrors) {
        this.warningsAsErrors = warningsAsErrors;
    }

    private static File decide() {
        for (final File path : RC_PATHS) {
            for (final String filename : RC_FILENAMES) {
                String rcpath = asPath(path.getAbsolutePath(), filename);
                File rcfile = new File(rcpath);
                if (rcfile.canRead()) return rcfile;
            }
        }
        return null;
    }
}
