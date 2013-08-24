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
package org.openbel.framework.common.cfg;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static org.openbel.framework.common.BELUtilities.readable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.openbel.framework.common.PathConstants;

/**
 * This class encapsulates a file-based <i>configuration</i>.
 * <p>
 * This class contains a set of variables that will be automatically expanded if
 * found as a value within a configuration file.
 * </p>
 * <p>
 * <dl>
 * <dt><tt>{tmp}</tt></dt>
 * <dd>Expanded to the system temporary directory.
 * <dt><tt>{home}</tt></dt>
 * <dd>Expanded to the user's home directory.
 * <dt><tt>{name}</tt></dt>
 * <dd>Expanded to the user's name.</dd>
 * <dt><tt>{dir}</tt></dt>
 * <dd>Expanded to the current working directory.</dd>
 * </dl>
 * </p>
 */
public abstract class Configuration {

    /**
     * The comment prefix, {@value #COMMENT}. Lines beginning with this string
     * will be ignored.
     */
    public static final String COMMENT = "#";

    /**
     * The delimiter, {@value #NAME_VALUE_DELIMITER}. Name-value pairs are
     * separated in configuration files by this string.
     */
    public static final String NAME_VALUE_DELIMITER = "=";

    /**
     * The configuration file associated with this configuration, which may be
     * null.
     */
    protected final File configurationFile;

    /**
     * Creates a configuration instance associated with the supplied file.
     *
     * @param file File to use as configuration; may be null
     */
    protected Configuration(final File file) {
        this.configurationFile = file;
    }

    /**
     * Reads the configuration file, resulting in
     * {@link #processSetting(String, String) processSetting} callbacks. If the
     * {@link #configurationFile configuration file} cannot be read,
     * {@link #initializeDefaults() initializeDefaults} will be called.
     *
     * @throws IOException Thrown if an I/O error occurs
     */
    protected void init() throws IOException {
        if (readable(configurationFile)) {
            read();
            return;
        }
        initializeDefaults();
    }

    /**
     * Process a name-value setting during reading {@link #configurationFile} by
     *
     * @param name Non-null name
     * @param value Non-null value
     */
    protected abstract void processSetting(String name, String value);

    /**
     * Called at the end of {@link #read()}.
     */
    protected abstract void readComplete();

    /**
     * Initializes the configuration to default settings.
     */
    protected abstract void initializeDefaults();

    /**
     * Returns the name-value default settings.
     *
     * @return Name-value mappings
     */
    protected abstract Map<String, String> defaults();

    /**
     * Returns the default configuration provided by {@link #defaults()}.
     *
     * @return Non-null string
     */
    protected String defaultConfiguration() {
        final StringBuilder bldr = new StringBuilder();

        for (final Entry<String, String> entry : defaults().entrySet()) {
            final String name = entry.getKey();
            final String value = entry.getValue();
            bldr.append(name);
            bldr.append(" ");
            bldr.append(NAME_VALUE_DELIMITER);
            bldr.append(" ");
            bldr.append(value);
            bldr.append("\n");
        }

        return bldr.toString();
    }

    /**
     * Reads {@link #configurationFile}, invoking
     * {@link #processSetting(String, String)} as name-value pairs are
     * encountered.
     *
     * @throws IOException Thrown if an I/O error occurs
     * @throws IllegalStateException If {@code read()} has been called with a
     * null configuration file
     */
    public void read() throws IOException {
        if (configurationFile == null) throw new IllegalStateException();
        final FileReader fr = new FileReader(configurationFile);
        final BufferedReader br = new BufferedReader(fr);
        String input = null;
        while ((input = br.readLine()) != null) {

            // Trim whitespace.
            input = input.trim();

            // Skip comments
            if (input.startsWith(COMMENT)) continue;

            final int idx = input.indexOf(NAME_VALUE_DELIMITER);
            if (idx == -1) continue;

            String name, value;
            try {
                name = input.substring(0, idx);
                value = input.substring(idx + 1);
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            name = name.trim();
            value = value.trim();

            processSetting(name, valueSubstitution(value));
        }
        br.close();
        readComplete();
    }

    /**
     * Performs substitution against the configuration {@code value} for the
     * system's temporary directory, user's home directory, user's name, or
     * user's current working directory.
     *
     * @param value Non-null string
     * @return String resulting from value replacement
     */
    public static String valueSubstitution(final String value) {
        final String tmpProp = "java.io.tmpdir";
        final String tmpVar = "{tmp}";
        final String tmpRE = "{tmp}";

        final String homeProp = "user.home";
        final String homeVar = "{home}";
        final String homeRE = "{home}";

        final String nameProp = "user.name";
        final String nameVar = "{name}";
        final String nameRE = "{name}";

        final String cwdProp = "user.dir";
        final String cwdVar = "{dir}";
        final String cwdRE = "{dir}";

        final String bfHomeEnv = PathConstants.BELFRAMEWORK_HOME_ENV_VAR;
        final String bfHomeVar = "{belframework_home}";
        final String bfHomeRE = "{belframework_home}";

        String ret = value;

        if (value.contains(tmpVar)) {
            ret = ret.replace(tmpRE, getProperty(tmpProp));
        }
        if (value.contains(homeVar)) {
            ret = ret.replace(homeRE, getProperty(homeProp));
        }
        if (value.contains(nameVar)) {
            ret = ret.replace(nameRE, getProperty(nameProp));
        }
        if (value.contains(cwdVar)) {
            ret = ret.replace(cwdRE, getProperty(cwdProp));
        }
        if (value.contains(bfHomeVar)) {
            String bfHome = getenv(bfHomeEnv);
            if (bfHome == null) {
                bfHome = ""; //belframework home is not set. Use empty string.
            }
            ret = ret.replace(bfHomeRE, bfHome);
        }
        return ret;
    }
}
