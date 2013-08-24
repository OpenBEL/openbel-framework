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

    /**
     * Default private constructor.
     */
    private StandardOptions() {

    }

}
