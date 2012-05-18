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
 * Phase four compiler options.
 * <p>
 * This is a {@link #phaseFourOptions() singleton object} that sets its
 * properties based on the presence and contents of a file.
 * </p>
 * 
 * @see org.openbel.framework.common.PathConstants BEL framework path
 * constants
 */
public class PhaseFourOptions extends RuntimeConfiguration {

    /**
     * KAM setting name: {@value #KAM_NAME_DESC}
     */
    public static final String KAM_NAME_DESC = "kam_name";

    /**
     * KAM setting name: {@value #KAM_DESCRIPTION_DESC}
     */
    public static final String KAM_DESCRIPTION_DESC = "kam_description";

    private String kamName;
    private String kamDescription;

    private static final PhaseFourOptions self;
    static {
        try {
            self = new PhaseFourOptions();
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
    private PhaseFourOptions() throws IOException {
        super();
    }

    /**
     * Returns the phase four compiler options.
     * 
     * @return PhaseFourOptions
     */
    public static PhaseFourOptions phaseFourOptions() {
        return self;
    }

    /**
     * Returns phase four's KAM name option.
     * 
     * @return String; may be null
     */
    public final String getKAMName() {
        return kamName;
    }

    /**
     * Sets phase four's KAM name option.
     * 
     * @param kamName Phase four's KAM name
     */
    public final void setKAMName(String kamName) {
        this.kamName = kamName;
    }

    /**
     * Returns phase four's KAM description option value.
     * 
     * @return {@link String}, the kam description option value
     */
    public final String getKAMDescription() {
        return kamDescription;
    }

    /**
     * Sets phase four'r KAM description option value.
     * 
     * @param kamDescription {@link String}, the kam description option value
     */
    public final void setKAMDescription(String kamDescription) {
        this.kamDescription = kamDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processSetting(String name, String value) {
        super.processSetting(name, value);
        if (KAM_NAME_DESC.equals(name)) {
            kamName = value;
        } else if (KAM_DESCRIPTION_DESC.equals(name)) {
            kamDescription = value;
        }
    }

    /**
     * Prints the default configuration for phase four.
     * 
     * @param args Ignored command-line arguments
     */
    public static void main(String... args) {
        out.println("Phase four defaults are:");
        out.println(phaseFourOptions().defaultConfiguration());
    }
}
