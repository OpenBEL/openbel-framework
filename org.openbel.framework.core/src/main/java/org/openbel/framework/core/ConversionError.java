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
package org.openbel.framework.core;

import org.openbel.framework.common.BELErrorException;

/**
 * A BEL error indicating conversion among BEL formats and object models has
 * failed.
 */
public class ConversionError extends BELErrorException {
    private static final long serialVersionUID = 8510289948521312868L;

    /**
     * Creates a conversion error for the provided resource {@code name} and
     * with the supplied message. The cause is not initialized, and may
     * subsequently be initialized by a call to {@link #initCause(Throwable)}.
     *
     * @param name Resource name failing conversion
     * @param msg Message indicative of conversion failure
     */
    public ConversionError(String name, String msg) {
        super(name, msg);
    }

    /**
     * Creates a conversion error for the provided resource {@code name}, with
     * the supplied message and cause.
     *
     * @param name Resource name failing conversion
     * @param msg Message indicative of conversion failure
     * @param cause The cause of the exception
     */
    public ConversionError(String name, String msg, Throwable cause) {
        super(name, msg, cause);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("CONVERSION ERROR");
        final String name = getName();
        if (name != null) {
            bldr.append(" in ");
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
