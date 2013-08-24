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

import org.openbel.framework.common.BELErrorException;

public class BELDataMissingPropertyException extends BELErrorException {
    private static final long serialVersionUID = 6911684147514604764L;
    private static final String MSG_FMT =
            "Missing required property '%s' in block '%s'.";
    private final String propertyName;
    private final String blockName;

    public BELDataMissingPropertyException(String resourceLocation,
            String blockName, String propertyName) {
        super(resourceLocation, buildMessage(propertyName, blockName));
        this.propertyName = propertyName;
        this.blockName = blockName;
    }

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
        bldr.append(String.format(MSG_FMT, propertyName, blockName));
        bldr.append("\n");
        return bldr.toString();
    }

    private static String buildMessage(final String propertyName,
            final String blockName) {
        return String.format(MSG_FMT, propertyName, blockName);
    }
}
