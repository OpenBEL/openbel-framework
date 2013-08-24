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
package org.openbel.framework.core.compiler;

import org.openbel.framework.common.BELFatalException;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.core.df.DBConnection;

/**
 * A BEL KAM-creation failure, generated when saving a {@link ProtoNetwork} as a
 * KAM.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class CreateKAMFailure extends BELFatalException {
    private static final long serialVersionUID = 5545640805582223223L;

    /**
     * Creates a KAM-creation failure from a {@code name} and {@code msg}.
     *
     * @param dbc {@link DBConnection}, the database connection
     * @param msg {@link String}, the message
     */
    public CreateKAMFailure(DBConnection dbc, String msg) {
        super(dbc.toString(), msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("KAM CREATION FAILURE");

        final String name = getName();
        if (name != null) {
            bldr.append(" using ");
            bldr.append(name);
        }

        bldr.append("\n\treason: ");
        final String msg = getMessage();
        if (msg != null)
            bldr.append(msg);
        else
            bldr.append("Unknown");
        bldr.append("\n");
        return bldr.toString();
    }
}
