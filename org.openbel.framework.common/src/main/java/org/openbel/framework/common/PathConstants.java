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
package org.openbel.framework.common;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static org.openbel.framework.common.BELUtilities.asPath;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * BEL path constants.
 */
public final class PathConstants {

    // EXTENSIONS

    /**
     * Namespace filename extension, {@value}
     */
    public static final String NAMESPACE_EXTENSION = ".belns";

    /**
     * Equivalence filename extension, {@value}
     */
    public static final String EQUIVALENCE_EXTENSION = ".beleq";

    /**
     * Equivalence map filename extension, {@value}
     */
    public static final String EQUIVALENCE_MAP_EXTENSION = ".tbl";

    /**
     * BEL script document extension, {@value}
     */
    public static final String BEL_SCRIPT_EXTENSION = ".bel";

    /**
     * XBEL document extension, {@value}
     */
    public static final String XBEL_EXTENSION = ".xbel";

    /**
     * Resource index extension, {@value}
     */
    public static final String XML_EXTENSION = ".xml";

    /**
     * Tmp file extension, {@value}
     */
    public static final String TMP_EXTENSION = ".tmp";

    /**
     * The SHA256 file extension, {@value}
     */
    public static final String SHA256_EXTENSION = ".sha256";

    /**
     * The compressed file extension, {@value}
     */
    public static final String COMPRESSED_EXTENSION = ".gz";

    /**
     * The annotation file extension, {@value}
     */
    public static final String ANNOTATION_EXTENSION = ".belanno";

    /**
     * BEL Template filename extension, {@value}
     */
    public static final String BEL_TEMPLATE_EXTENSION = ".beldot";

    // FILENAMES AND PATHS

    /**
     * Defines the {@link String} name for the namespace directory root.
     */
    public static final String NAMESPACE_ROOT_DIRECTORY_NAME = "namespace";

    /**
     * Defines the {@link String} name for the equivalence directory root.
     */
    public static final String EQUIVALENCE_ROOT_DIRECTORY_NAME = "equivalence";

    /**
     * Defines the {@link String} name for the resources (knowledge) directory
     * root.
     */
    public static final String RESOURCES_ROOT_DIRECTORY_NAME = "resources";

    /**
     * Defines the file name {@link String} for the bootstrap SLF4J logback
     * configuration file.
     */
    public static final String SLF4J_LOGGER_FILE = "/logback-configuration.xml";

    /**
     * Runtime configuration filenames. Either {@code belrc} or {@code .belrc}
     * is supported.
     */
    public static final List<String> RC_FILENAMES = new LinkedList<String>();

    /**
     * Runtime configuration file locations. Either {@code user.dir} or
     * {@code user.home} is supported.
     */
    public static final List<File> RC_PATHS = new LinkedList<File>();

    /**
     * System configuration filename. Only {@code belframework.cfg} is
     * supported.
     */
    public static final String SYSCONFIG_FILENAME = "belframework.cfg";

    /**
     * System configuration path name in BEL home. Only {@code config} is
     * supported.
     */
    public static final String SYSCONFIG_PATH = "config";

    /**
     * System configuration file location; may be null.
     */
    public static final File SYS_PATH;

    /**
     * Environment variable that overrides default system configuration path.
     */
    public static final String BELFRAMEWORK_HOME_ENV_VAR = "BELFRAMEWORK_HOME";

    /**
     * Proto-network filename, {@value}
     */
    public static final String PROTO_NETWORK_FILENAME = "network.bin";

    /**
     * Global proto-network filename, {@value}
     */
    public static final String GLOBAL_PROTO_NETWORK_FILENAME =
            "network.bin";

    /**
     * Default output directory: {@value}
     */
    public static final String DEFAULT_OUTPUT_DIRECTORY = "belframework";

    /**
     * Nested output directory: {@value}
     * <p>
     * The output directory is purged at the start of compiler execution. This
     * directory is nested in the output directory to prevent purging the user's
     * data inadvertently.
     * </p>
     */
    public static final String NESTED_OUTPUT_DIRECTORY = "belframework_output";

    /**
     * Default cache directory: {@value}
     */
    public static final String DEFAULT_CACHE_DIRECTORY = ".belcache";

    /**
     * Default BEL Template directory: {@value}
     */
    public static final String DEFAULT_BEL_TEMPLATE_DIRECTORY = ".beltemplates";

    static {
        // TODO migrate this into sys cfg - use .belframework
        String envVar = getenv(BELFRAMEWORK_HOME_ENV_VAR);
        if (envVar != null) {
            SYS_PATH = new File(asPath(envVar, SYSCONFIG_PATH));
        } else {
            SYS_PATH = null;
        }
        RC_FILENAMES.add("belrc");
        RC_FILENAMES.add(".belrc");
        RC_PATHS.add(new File(getProperty("user.dir")));
        RC_PATHS.add(new File(getProperty("user.home")));
    }

    /**
     * Default private constructor.
     */
    private PathConstants() {

    }
}
