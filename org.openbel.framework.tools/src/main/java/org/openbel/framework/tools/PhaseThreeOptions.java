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
}
