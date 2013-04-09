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
package org.openbel.framework.core;

/**
 * Standard command-line options.
 * <p>
 * <h1>Conventions</h1>
 * <ol>
 * <li>Single character options are either a lower case letter, an upper case
 * letter, or a digit</li>
 * <li>Long options are one or more lowercase words separated by a hyphen</li>
 * <li>Argument names are separated by underscores</li>
 * </ol>
 * </p>
 * <p>
 * <h1>Examples</h1>
 * <h2>Single character options</h2>
 * <ul>
 * <li>{@code -v}
 * <li>{@code -3}
 * <li>{@code -E}
 * </ul>
 * <h2>Long options</h2>
 * <ul>
 * <li>{@code --force}</li>
 * <li>{@code --no-preserve}</li>
 * </ul>
 * <h2>Argument names</h2>
 * <ul>
 * <li>{@code <filename>}</li>
 * <li>{@code <gzip_file>}</li>
 * </ul>
 * </p>
 * <p>
 * TODO: Add standard option {@code no-preserve} to preserve output (for
 * applications generating output).
 * </p>
 */
public class StandardOptions {

    /**
     * The short one-character option to specify a system configuration file. <br>
     * For example: <br>{@code -s /path/to/file}
     */
    public final static String SHRT_OPT_SYSCFG = "s";

    /**
     * The long option to specify a system configuration file.<br>
     * For example:<br>{@code --system-config-file /path/to/file}
     */
    public final static String LONG_OPT_SYSCFG = "system-config-file";

    /**
     * The argument name used by {@link #SHRT_OPT_SYSCFG} and
     * {@link #LONG_OPT_SYSCFG}.<br>
     * For example:<br>{@code --system-config-file <filename>}
     */
    public final static String ARG_SYSCFG = "filename";

    /**
     * The long option to enable timing of operations.<br>
     * For example:<br>{@code --timing}
     */
    public final static String LONG_OPT_TIME = "timing";

    /**
     * The short one-character option to enable verbose mode.<br>
     * For example {@code -v}
     */
    public final static String SHORT_OPT_VERBOSE = "v";

    /**
     * The long option to enable verbose mode.<br>
     * For example:<br>{@code --verbose}
     */
    public final static String LONG_OPT_VERBOSE = "verbose";

    /**
     * The long option to enable debug mode.<br>
     * For example:<br>{@code --debug}
     */
    public final static String LONG_OPT_DEBUG = "debug";

    /**
     * The short one-character option get help.<br>
     * For example:<br>{@code -h}
     */
    public final static String SHRT_OPT_HELP = "h";

    /**
     * The long option to get help.<br>
     * For example:<br>{@code --help}
     */
    public final static String LONG_OPT_HELP = "help";

    /*
     * The long option to enable no-preserve.<br>
     * For example:<br>{@code --no-preserve}
     */
    public final static String LONG_OPT_NO_PRESERVE= "no-preserve";

    /**
     * Default private constructor.
     */
    private StandardOptions() {

    }

}
