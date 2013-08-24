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

import static org.junit.Assert.fail;
import static org.openbel.framework.common.BELUtilities.deleteDirectory;
import static org.openbel.framework.common.BELUtilities.deleteDirectoryContents;
import static org.openbel.framework.common.cfg.SystemConfiguration.createSystemConfiguration;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.openbel.framework.common.SystemConfigurationFile;
import org.openbel.framework.common.cfg.SystemConfiguration;

/**
 * SystemConfigurationBasedTest defines a unit-testing parent class that
 * establishes and removes a {@link SystemConfiguration} for each unit test.
 *
 * <p>
 * The derived unit test type must be annotated with
 * {@link SystemConfigurationFile} which will provide the test system
 * configuration.  This is provided because it may need customization
 * pertaining to the derived unit tests.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public abstract class SystemConfigurationBasedTest {

    /**
     * Establishes the {@link SystemConfiguration} using the configuration
     * file path defined in the {@link SystemConfigurationFile} annotation.
     *
     * <p>
     * The setup fails if the {@link SystemConfigurationFile} annotation does
     * not exist or if the {@link SystemConfiguration} file is invalid.
     * </p>
     */
    @Before
    public void setup() {
        SystemConfigurationFile configAnnotation = getClass().getAnnotation(
                SystemConfigurationFile.class);
        if (configAnnotation == null) {
            fail("System configuration-based test does not declare SystemConfigurationFile annotation.");
            return;
        }

        try {
            createSystemConfiguration(new File(configAnnotation.path()));
        } catch (IOException ioex) {
            ioex.printStackTrace();
            fail(ioex.getMessage());
        }
    }

    /**
     * Removes the resources used in the unit test {@link SystemConfiguration}.
     *
     * <p>
     * Note that the file / folder resources in the config file defined in
     * {@link SystemConfigurationFile} will be deleted in order to clean up
     * after the tests.
     * </p>
     */
    @After
    public void teardown() {
        SystemConfiguration config = SystemConfiguration
                .getSystemConfiguration();
        config.getApplicationLogPath().delete();

        deleteDirectoryContents(config.getCacheDirectory());
        deleteDirectory(config.getCacheDirectory());

        deleteDirectoryContents(config.getWorkingDirectory());
        deleteDirectory(config.getWorkingDirectory().getParentFile());

        SystemConfiguration.destroySystemConfiguration();
    }
}
