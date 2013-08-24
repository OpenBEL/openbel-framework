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
package org.openbel.framework.core.equivalence;

import org.openbel.framework.common.BELWarningException;

/**
 * A BELFramework warning indicating there was an I/O failure resolving
 * the equivalence map file.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class EquivalenceMapResolutionFailure extends BELWarningException {

    private static final long serialVersionUID = 5928848208722666864L;

    /**
     * Construct the EquivalenceResolutionFailure exception.
     *
     * @param name
     * @param msg
     * @param cause
     */
    public EquivalenceMapResolutionFailure(String name, String msg) {
        super(name, msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("EQUIVALENCE MAP RESOLUTION FAILURE");

        final String name = getName();
        if (name != null) {
            bldr.append(" in ");
            bldr.append(name);
        }

        bldr.append("\n\treason: ");
        final String msg = getMessage();
        if (msg != null) {
            bldr.append(msg);
        } else {
            bldr.append("Unknown");
        }

        bldr.append("\n");

        bldr.append(getCause());
        return bldr.toString();
    }
}
