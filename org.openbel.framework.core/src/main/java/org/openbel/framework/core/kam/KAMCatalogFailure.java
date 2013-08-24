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
package org.openbel.framework.core.kam;

import org.openbel.framework.common.BELErrorException;

/**
 * KAMCatalogFailure defines an error with creating or updating
 * the kam catalog schema.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class KAMCatalogFailure extends BELErrorException {

    private static final long serialVersionUID = -1284782536196562412L;

    /**
     * Create the KAMCatalogFailure with a <tt>name</tt> and a <tt>msg</tt>.
     *
     * @param name {@link String}, the kam name
     * @param msg {@link String}, the failure message
     */
    public KAMCatalogFailure(String name, String msg) {
        super(name, msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("KAM CATALOG FAILURE");

        final String name = getName();
        if (name != null) {
            bldr.append(" saving ");
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
