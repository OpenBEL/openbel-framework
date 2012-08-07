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

import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static org.openbel.framework.common.BELUtilities.entries;
import static org.openbel.framework.common.BELUtilities.mapfx;
import static org.openbel.framework.common.BELUtilities.noNulls;
import static org.openbel.framework.common.BELUtilities.readable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openbel.framework.common.BELUtilities;
import org.openbel.framework.common.MapFunction;
import org.openbel.framework.common.PathConstants;

/**
 * This class encapsulates a map-based <i>configuration</i>.
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
     * The name-value configuration map, in expanded form.
     */
    private Map<String, String> cfgMap;

    /**
     * Boolean {@code true} if defaults are used, {@code false} otherwise. The
     * defaults provide a way for subclasses to use a predefined set of defaults
     * in the event no configuration can be provided.
     */
    protected final boolean defaults;

    /**
     * Creates a configuration instance derived from the supplied file. If the
     * file is not {@link BELUtilities#readable(File) readable},
     * {@link #initializeDefaults()} will be called on {@link #init()}.
     * 
     * @param file File to use as configuration; may be null in which case
     * {@link #defaults} are used
     * @throws IOException Thrown if an I/O error occurs
     */
    protected Configuration(final File file) throws IOException {
        if (readable(file)) {
            cfgMap = new HashMap<String, String>();
            read(file);
            defaults = false;
            return;
        }
        cfgMap = null;
        defaults = true;
    }

    /**
     * Creates a configuration instance derived from the supplied map. If the
     * map is null, {@link #initializeDefaults()} will be called on
     * {@link #init()}.
     * 
     * @param map Map to use as configuration; may be null in which case
     * {@link #defaults} are used
     */
    protected Configuration(final Map<String, String> map) {
        if (noNulls(map)) {
            cfgMap = new HashMap<String, String>();
            read(map);
            defaults = false;
            return;
        }
        cfgMap = null;
        defaults = true;
    }

    /**
     * Creates a configuration instance using {@link #defaults}. This will
     * result in {@link #initializeDefaults()} being called on {@link #init()}.
     */
    protected Configuration() {
        cfgMap = null;
        defaults = true;
    }

    /**
     * Initializes the configuration instance. The
     * {@link Configuration#processSetting(String, String) processSetting}
     * method will be invoked for each setting, or {@link #initializeDefaults()
     * initializeDefaults} if defaults are being used.
     */
    protected void init() {
        if (defaults) {
            initializeDefaults();
            return;
        }
        mapfx(cfgMap, new MapFunction<String, String>() {
            @Override
            public void apply(String name, String value) {
                processSetting(name, value);
            }
        });
        readComplete();
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

    /*
     * Reads a file populating a map for a call to read(Map) below.
     * 
     * @throws IOException Thrown if an I/O error occurs
     */
    private void read(final File f) throws IOException {
        final FileReader fr = new FileReader(f);
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

            cfgMap.put(name, valueSubstitution(value));
        }
        br.close();
    }

    /*
     * Reads the configuration map of name/value strings, invoking the process
     * setting callback.
     */
    private void read(final Map<String, String> map) {
        String name, value;
        for (final Entry<String, String> entry : entries(map)) {
            name = entry.getKey();
            value = entry.getValue();
            cfgMap.put(name, valueSubstitution(value));
        }
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
