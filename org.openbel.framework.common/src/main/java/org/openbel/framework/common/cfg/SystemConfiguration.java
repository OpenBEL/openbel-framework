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
import static java.lang.System.out;
import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.PathConstants.*;
import static org.openbel.framework.common.Strings.CANT_WRITE_TO_PATH;
import static org.openbel.framework.common.Strings.DIRECTORY_CREATION_FAILED;
import static org.openbel.framework.common.Strings.MISSING_SYSCFG;
import static org.openbel.framework.common.enums.ExitCode.MISSING_SYSTEM_CONFIGURATION;
import static org.openbel.framework.common.enums.ExitCode.UNRECOVERABLE_ERROR;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.common.BELRuntimeException;
import org.openbel.framework.common.PathConstants;

/**
 * Provides system configuration capabilities.
 * <p>
 * Two types of configuration exist within the BEL framework, system
 * configuration and {@link RuntimeConfiguration runtime configuration}. System
 * configuration allows access and modification of settings that affect the
 * system's behavior, i.e., the installation of the BEL framework.
 * </p>
 */
public final class SystemConfiguration extends Configuration {

    private static SystemConfiguration self;

    /**
     * KAM URL setting name: {@value #KAMSTORE_URL_DESC}
     */
    public static final String KAMSTORE_URL_DESC = "kamstore_url";

    /**
     * KAM user setting name: {@value #KAMSTORE_USER_DESC}
     */
    public static final String KAMSTORE_USER_DESC = "kamstore_user";

    /**
     * KAM password setting name: {@value #KAMSTORE_PASSWORD_DESC}
     */
    public static final String KAMSTORE_PASSWORD_DESC = "kamstore_password";

    /**
     * KAM password setting name: {@value #SYSTEM_MANAGED_SCHEMAS}
     */
    public static final String SYSTEM_MANAGED_SCHEMAS =
            "system_managed_schemas";

    /**
     * KamStore catalog schema setting name:
     * {@value #KAMSTORE_CATALOG_SCHEMA_DESC}
     */
    public static final String KAMSTORE_CATALOG_SCHEMA_DESC =
            "kamstore_catalog_schema";

    /**
     * KamStore KAM schema prefix setting name:
     * {@value #KAMSTORE_SCHEMA_PREFIX_DESC}
     */
    public static final String KAMSTORE_SCHEMA_PREFIX_DESC =
            "kamstore_schema_prefix";

    /**
     * Framework working area: {@value #FRAMEWORK_WORKING_AREA_DESC}
     */
    public static final String FRAMEWORK_WORKING_AREA_DESC =
            "belframework_work";

    /**
     * Framework cache directory setting name:
     * {@value #FRAMEWORK_CACHE_DIRECTORY_DESC}
     */
    public static final String FRAMEWORK_CACHE_DIRECTORY_DESC =
            "belframework_cache";

    /**
     * Application log path: {@value #APPLICATION_LOG_PATH_DESC}
     */
    public static final String APPLICATION_LOG_PATH_DESC =
            "application_log_path";

    /**
     * URL to the BELFramework resource index: {@value #RESOURCE_INDEX_URL_DESC}
     */
    public static final String RESOURCE_INDEX_URL_DESC =
            "resource_index_url";

    /**
     * KAM store encryption passphrase: {@value #SYMMETRIC_ENCRYPTION_KEY}
     */
    public static final String KAM_STORE_ENCRYPTION_PASSPHRASE =
            "kamstore_encryption_passphrase";

    /**
     * Location of BEL Templates (for BELWorkbench document creation):
     * {@value #BEL_TEMPLATE_LOCATION}
     */
    public static final String BEL_TEMPLATE_LOCATION = "beltemplate_path";

    /**
     * Defines the default value, {@value #DEFAULT_KAM_CATALOG_SCHEMA}, for the
     * kam_catalog_schema setting.
     */
    private static final String DEFAULT_KAM_CATALOG_SCHEMA = "kam_catalog";

    /**
     * Defines the default value, {@value #DEFAULT_KAM_SCHEMA_PREFIX}, for the
     * kam_schema_prefix setting.
     */
    private static final String DEFAULT_KAM_SCHEMA_PREFIX = "kam";

    /**
     * Set to 0 if the databases are DBA managed or 1 if the BELFramework is allowed
     * to create new KAM schemas (default)
     */
    private static final String DEFAULT_SYSTEM_MANAGED_SCHEMAS = "1";

    private String kamURL;
    private String kamUser;
    private String kamPassword;
    private String kamCatalogSchema;
    private String kamSchemaPrefix;
    private String systemManagedSchemas;
    private File workingDirectory;
    private File cacheDirectory;
    private File applicationLogPath;
    private String resourceIndexURL;
    private String kamstoreEncryptionPassphrase;
    private File belTemplateDirectory;

    /**
     * Creates a system configuration instance.
     * <p>
     * The configuration file to be used is
     * {@link PathConstants#SYSCONFIG_FILENAME} in
     * {@link PathConstants#SYS_PATH}.
     * </p>
     *
     * @throws IOException Thrown if an I/O error occurs
     */
    private SystemConfiguration() throws IOException {
        super(new File(
                asPath(SYS_PATH == null ? "" : SYS_PATH.getAbsolutePath(),
                        SYSCONFIG_FILENAME)));
        init();
    }

    /**
     * Creates a system configuration instance, using {@code file} as the
     * configuration file.
     *
     * @param file Configuration file
     * @throws IOException Thrown if an I/O error occurs
     */
    private SystemConfiguration(final File file) throws IOException {
        super(file);
        init();
    }

    /**
     * Creates the system configuration from the provided file, which may be
     * null.
     *
     * @param file System configuration file, which may be null
     * @return {@link SystemConfiguration}
     * @throws IOException Thrown if an I/O error occurs
     */
    public static synchronized SystemConfiguration createSystemConfiguration(
            final File file) throws IOException {
        if (self == null) {
            if (file == null) {
                if (getenv(BELFRAMEWORK_HOME_ENV_VAR) == null) {
                    String err = MISSING_SYSCFG;
                    err = err.concat(": environment variable not set ");
                    err = err.concat(BELFRAMEWORK_HOME_ENV_VAR);
                    throw new IOException(err);
                }
                self = new SystemConfiguration();
            } else {
                self = new SystemConfiguration(file);
            }
        }
        return self;
    }

    /**
     * Returns the system configuration.
     * <p>
     * It is an error to access the system configuration before it has been
     * created. As a result, this method throws {@link IllegalStateException} to
     * indicate this has occurred.
     * </p>
     *
     * @return {@link SystemConfiguration}
     * @see #createSystemConfiguration(File)
     * @throws IllegalStateException Thrown if no system configuration has been
     * created
     */
    public static synchronized SystemConfiguration getSystemConfiguration() {
        if (self == null) {
            throw new IllegalStateException(
                    "System configuration has not been created.");
        }
        return self;
    }

    /**
     * Destroys the system configuration.
     *
     * <p>
     * It is an error to destroy the system configuration before it has been
     * created. As a result, this method throws {@link IllegalStateException} to
     * indicate this has occurred.
     * </p>
     *
     * @see #createSystemConfiguration(File)
     */
    protected static synchronized void destroySystemConfiguration() {
        if (self == null) {
            throw new IllegalStateException(
                    "System configuration has not been created.");
        }
        self = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processSetting(String name, String value) {
        if (KAMSTORE_URL_DESC.equals(name)) {
            kamURL = value;
        } else if (KAMSTORE_USER_DESC.equals(name)) {
            kamUser = value;
        } else if (KAMSTORE_PASSWORD_DESC.equals(name)) {
            kamPassword = value;
        } else if (KAMSTORE_CATALOG_SCHEMA_DESC.equals(name)) {
            kamCatalogSchema = value;
        } else if (KAMSTORE_SCHEMA_PREFIX_DESC.equals(name)) {
            kamSchemaPrefix = value;
        } else if (FRAMEWORK_WORKING_AREA_DESC.equals(name)) {
            final String workingPath = asPath(value, NESTED_OUTPUT_DIRECTORY);
            workingDirectory = new File(workingPath);
        } else if (FRAMEWORK_CACHE_DIRECTORY_DESC.equals(name)) {
            cacheDirectory = new File(value);
        } else if (APPLICATION_LOG_PATH_DESC.equals(name)) {
            applicationLogPath = new File(value);
        } else if (RESOURCE_INDEX_URL_DESC.equals(name)) {
            resourceIndexURL = value;
        } else if (KAM_STORE_ENCRYPTION_PASSPHRASE.equals(name)) {
            kamstoreEncryptionPassphrase = value;
        } else if (BEL_TEMPLATE_LOCATION.equals(name)) {
            belTemplateDirectory = new File(value);
        } else if (SYSTEM_MANAGED_SCHEMAS.equals(name)) {
            systemManagedSchemas = value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeDefaults() {
        String err = "A valid configuration file must be present";
        if (configurationFile != null) {
            err += " (using " + configurationFile + ")";
        }
        throw new BELRuntimeException(err, MISSING_SYSTEM_CONFIGURATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readComplete() {
        Set<String> unsetStrings = new HashSet<String>();

        if (kamURL == null || kamURL.isEmpty()) {
            unsetStrings.add(KAMSTORE_URL_DESC);
        }

        if (resourceIndexURL == null || resourceIndexURL.isEmpty()) {
            unsetStrings.add(RESOURCE_INDEX_URL_DESC);
        }

        if (kamstoreEncryptionPassphrase == null
                || kamstoreEncryptionPassphrase.isEmpty()) {
            unsetStrings.add(KAM_STORE_ENCRYPTION_PASSPHRASE);
        }

        if (workingDirectory == null) {
            String value = getProperty("java.io.tmpdir");
            String pathel2 = DEFAULT_OUTPUT_DIRECTORY;
            String pathel3 = NESTED_OUTPUT_DIRECTORY;
            value = asPath(value, pathel2, pathel3);
            workingDirectory = new File(value);
        }
        validateOutputPath(workingDirectory);

        if (cacheDirectory == null) {
            String value = getProperty("user.home");
            value = asPath(value, DEFAULT_CACHE_DIRECTORY);
            cacheDirectory = new File(value);
        }
        validateOutputPath(cacheDirectory);

        if (applicationLogPath == null) {
            String outputPath = workingDirectory.getAbsolutePath();
            applicationLogPath = new File(outputPath);
        }
        validateOutputPath(applicationLogPath);

        if (kamCatalogSchema == null) {
            kamCatalogSchema = DEFAULT_KAM_CATALOG_SCHEMA;
        }

        if (kamSchemaPrefix == null) {
            kamSchemaPrefix = DEFAULT_KAM_SCHEMA_PREFIX;
        }

        if (systemManagedSchemas == null) {
            systemManagedSchemas = DEFAULT_SYSTEM_MANAGED_SCHEMAS;
        }

        if (belTemplateDirectory == null) {
            String value = getProperty("user.home");
            value = asPath(value, DEFAULT_BEL_TEMPLATE_DIRECTORY);
            belTemplateDirectory = new File(value);
        }
        validateOutputPath(belTemplateDirectory);

        if (!unsetStrings.isEmpty()) {
            final StringBuilder bldr = new StringBuilder();
            bldr.append("The configuration file is not complete.");
            bldr.append("\n(missing");
            for (final String setting : unsetStrings) {
                bldr.append(" ");
                bldr.append(setting);
            }
            bldr.append(")");
            final String err = bldr.toString();
            throw new BELRuntimeException(err, UNRECOVERABLE_ERROR);
        }

        // Five-by-five.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, String> defaults() {
        Map<String, String> ret = new HashMap<String, String>();

        String value = getProperty("java.io.tmpdir");
        value = asPath(value, DEFAULT_OUTPUT_DIRECTORY);
        ret.put(FRAMEWORK_WORKING_AREA_DESC, value);

        value = getProperty("user.home");
        value = asPath(value, DEFAULT_CACHE_DIRECTORY);
        ret.put(FRAMEWORK_CACHE_DIRECTORY_DESC, value);

        value = getProperty("java.io.tmpdir");
        value = asPath(value, DEFAULT_OUTPUT_DIRECTORY);
        ret.put(APPLICATION_LOG_PATH_DESC, value);

        return ret;
    }

    /**
     * Returns the KAM URL system configuration setting.
     *
     * @return {@link String}, the KAM store database URL, which cannot be null
     */
    public final String getKamURL() {
        return kamURL;
    }

    /**
     * Returns the KAM user system configuration setting.
     *
     * @return {@link String}, the KAM database username, which can be null
     */
    public final String getKamUser() {
        return kamUser;
    }

    /**
     * Returns the KAM password configuration setting.
     *
     * @return {@link String}, the KAM database password, which can be null
     */
    public final String getKamPassword() {
        return kamPassword;
    }

    /**
     * Returns the KAM catalog schema name.
     *
     * @return {@link String}, the KAM catalog schema name
     */
    public final String getKamCatalogSchema() {
        return kamCatalogSchema;
    }

    /**
     * Returns the KAM schema prefix.
     *
     * @return {@link String}, the KAM schema prefix
     */
    public final String getKamSchemaPrefix() {
        return kamSchemaPrefix;
    }

    /**
     * Returns the framework working directory system configuration setting.
     *
     * @return Non-null, writable directory
     */
    public final File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Returns the framework cache directory system configuration setting.
     *
     * @return Non-null, writable directory
     */
    public final File getCacheDirectory() {
        return cacheDirectory;
    }

    /**
     * Returns true if the OpenBEL Framework is managing the KAMStore schemas false
     * otherwise.
     *
     * @return Non-null, writable directory
     */
    public final boolean getSystemManagedSchemas() {
        if ("1".equals(systemManagedSchemas)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the application log path system configuration setting.
     *
     * @return Non-null, writable directory
     */
    public final File getApplicationLogPath() {
        return applicationLogPath;
    }

    /**
     * Returns the URL to the OpenBEL Framework resource index.
     *
     * @return Non-null, string
     */
    public String getResourceIndexURL() {
        return resourceIndexURL;
    }

    /**
     * Returns the encryption passphrase for the KAM store.
     *
     * @return Non-null, string
     */
    public String getKamstoreEncryptionPassphrase() {
        return kamstoreEncryptionPassphrase;
    }

    /**
     * Returns the BEL Template directory system configuration setting.
     *
     * @return Non-null, writable directory
     */
    public final File getBELTemplateDirectory() {
        return belTemplateDirectory;
    }

    /**
     * Prints the default system configuration.
     *
     * @param args Ignored command-line arguments
     */
    public static void main(String... args) {
        final String cfgpath = "../docs/configuration/belframework.cfg";
        final File cfgfile = new File(cfgpath);
        try {
            SystemConfiguration syscfg = createSystemConfiguration(cfgfile);
            out.println(syscfg.defaultConfiguration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Throws a system configuration error if the {@code path} cannot be written
     * to. If {@code path} does not exist, an attempt to create it will be made.
     *
     * @param path Path for validation
     */
    private void validateOutputPath(final File path) {
        if (path.canWrite()) return;

        // If the path doesn't exist...
        if (!path.exists()) {
            // ... try creating it...
            if (!path.mkdirs()) {
                // ... or die trying.
                String err = DIRECTORY_CREATION_FAILED.concat(path.toString());
                throw new BELRuntimeException(err, UNRECOVERABLE_ERROR);
            }
            return;
        }

        // ... can't write to it.
        String err = CANT_WRITE_TO_PATH.concat(path.toString());
        throw new BELRuntimeException(err, UNRECOVERABLE_ERROR);
    }
}
