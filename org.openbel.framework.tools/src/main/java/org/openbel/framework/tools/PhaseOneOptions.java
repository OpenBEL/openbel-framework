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
package org.openbel.framework.tools;

import static java.lang.System.out;
import static org.openbel.framework.common.Strings.RC_READ_FAILURE;
import static org.openbel.framework.common.enums.ExitCode.UNRECOVERABLE_ERROR;

import java.io.IOException;

import org.openbel.framework.common.BELRuntimeException;
import org.openbel.framework.common.cfg.RuntimeConfiguration;

/**
 * Phase one compiler options.
 * <p>
 * This is a {@link #phaseOneOptions() singleton object} that sets its
 * properties based on the presence and contents of a file.
 * </p>
 *
 * @see org.openbel.framework.common.PathConstants BEL framework path
 * constants
 */
public class PhaseOneOptions extends RuntimeConfiguration {

    /**
     * Input path setting name: {@value #INPUT_PATH_DESC}
     */
    public static final String INPUT_PATH_DESC = "input_path";
    private String inputPath;

    private static final PhaseOneOptions self;
    static {
        try {
            self = new PhaseOneOptions();
        } catch (IOException e) {
            final String err = RC_READ_FAILURE;
            throw new BELRuntimeException(err, UNRECOVERABLE_ERROR, e);
        }
    }

    /**
     * Default private constructor.
     *
     * @throws IOException Thrown if an I/O error occurs
     */
    private PhaseOneOptions() throws IOException {
        super();
    }

    /**
     * Returns the phase one compiler options.
     *
     * @return PhaseOneOptions
     */
    public static PhaseOneOptions phaseOneOptions() {
        return self;
    }

    /**
     * Returns phase one's input path option.
     *
     * @return String; may be null
     */
    public final String getInputPath() {
        return inputPath;
    }

    /**
     * Sets phase one's input path option.
     *
     * @param inputPath Phase one's input path
     */
    public final void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processSetting(String name, String value) {
        super.processSetting(name, value);
        if (INPUT_PATH_DESC.equals(name))
            inputPath = value;
    }

    /**
     * Prints the default configuration for phase one.
     *
     * @param args Ignored command-line arguments
     */
    public static void main(String... args) {
        out.println("Phase one defaults are:");
        out.println(phaseOneOptions().defaultConfiguration());
    }

}
