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
