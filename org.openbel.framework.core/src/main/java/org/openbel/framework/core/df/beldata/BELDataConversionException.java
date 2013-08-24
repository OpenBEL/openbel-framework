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
package org.openbel.framework.core.df.beldata;

import static org.openbel.framework.common.BELUtilities.getFirstCause;
import static org.openbel.framework.common.BELUtilities.getFirstMessage;

import org.openbel.framework.common.BELErrorException;

public class BELDataConversionException extends BELErrorException {
    private static final long serialVersionUID = -6828651970299348458L;
    private static final String MSG_FMT =
            "Cannot parse '%s' for property name '%s'.";
    private final String propertyName;
    private final String propertyValue;

    public BELDataConversionException(String resourceLocation,
            String propertyName,
            String propertyValue) {
        super(resourceLocation, buildMessage(propertyName, propertyValue, null));
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public BELDataConversionException(String resourceLocation,
            String propertyName,
            String propertyValue, Throwable cause) {
        super(resourceLocation,
                buildMessage(propertyName, propertyValue, cause), cause);
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("PARSE ERROR");
        final String name = getName();
        if (name != null) {
            bldr.append(" in ");
            bldr.append(name);
        }
        bldr.append("\n\treason: ");
        bldr.append(String.format(MSG_FMT, propertyValue, propertyName));
        bldr.append("\n");
        return bldr.toString();
    }

    private static String buildMessage(final String propertyName,
            final String propertyValue, final Throwable cause) {

        final StringBuilder bldr = new StringBuilder();
        bldr.append(String.format(MSG_FMT, propertyValue, propertyName));

        if (cause != null) {
            bldr.append(" (reason: ");
            String reason = getFirstMessage(cause);
            if (reason == null) {
                reason = getFirstCause(cause).getClass().getSimpleName();
            }
            bldr.append(reason);
            bldr.append(")");
        }

        return bldr.toString();
    }
}
