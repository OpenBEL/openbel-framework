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
 * Phase three compiler options.
 * <p>
 * This is a {@link #phaseThreeOptions() singleton object} that sets its
 * properties based on the presence and contents of a file.
 * </p>
 *
 * @see org.openbel.framework.common.PathConstants BEL framework path
 * constants
 */
public class PhaseThreeOptions extends RuntimeConfiguration {
    private boolean expandNamedComplexes;
    private boolean expandProteinFamilies;
    private boolean injectProteinFamilies = true;
    private boolean injectNamedComplexes = true;
    private boolean injectGeneScaffolding = true;
    private boolean injectOrthology = true;

    private static final PhaseThreeOptions self;
    static {
        try {
            self = new PhaseThreeOptions();
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
    private PhaseThreeOptions() throws IOException {
        super();
    }

    /**
     * Returns the phase three compiler options.
     *
     * @return PhaseThreeOptions
     */
    public static PhaseThreeOptions phaseThreeOptions() {
        return self;
    }

    /**
     * Prints the default configuration for phase three.
     *
     * @param args Ignored command-line arguments
     */
    public static void main(String... args) {
        out.println("Phase three defaults are:");
        out.println(phaseThreeOptions().defaultConfiguration());
    }

    /**
     * Returns whether named complexes should be expanded.
     *
     * @return boolean
     */
    public boolean getExpandNamedComplexes() {
        return expandNamedComplexes;
    }

    /**
     * Sets whether named complexes should be expanded.
     *
     * @param b boolean
     */
    public void setExpandNamedComplexes(final boolean b) {
        this.expandNamedComplexes = b;
    }

    /**
     * Returns whether protein families should be expanded.
     *
     * @return boolean
     */
    public boolean getExpandProteinFamilies() {
        return expandProteinFamilies;
    }

    /**
     * Sets whether protein families should be expanded.
     *
     * @param b boolean
     */
    public void setExpandProteinFamilies(final boolean b) {
        this.expandProteinFamilies = b;
    }

    /**
     * Returns whether protein families should be injected.
     *
     * @return boolean
     */
    public boolean getInjectProteinFamilies() {
        return injectProteinFamilies;
    }

    /**
     * Sets whether protein families should be injected.
     *
     * @param b boolean
     */
    public void setInjectProteinFamilies(final boolean b) {
        this.injectProteinFamilies = b;
    }

    /**
     * Returns whether named complexes should be injected.
     *
     * @return boolean
     */
    public boolean getInjectNamedComplexes() {
        return injectNamedComplexes;
    }

    /**
     * Sets whether named complexes should be injected.
     *
     * @param b boolean
     */
    public void setInjectNamedComplexes(final boolean b) {
        this.injectNamedComplexes = b;
    }

    /**
     * Returns whether gene scaffolding should be injected.
     *
     * @return boolean
     */
    public boolean getInjectGeneScaffolding() {
        return injectGeneScaffolding;
    }

    /**
     * Sets whether gene scaffolding should be injected.
     *
     * @param b boolean
     */
    public void setInjectGeneScaffolding(final boolean b) {
        this.injectGeneScaffolding = b;
    }

    /**
     * Returns whether orthology knowledge should be injected.
     *
     * @return b boolean
     */
    public boolean getInjectOrthology() {
        return injectOrthology;
    }

    /**
     * Sets whether orthology knowledge should be injected.
     *
     * @param b boolean
     */
    public void setInjectOrthology(final boolean b) {
        this.injectOrthology = b;
    }
}
