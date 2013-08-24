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

import java.util.Arrays;

import org.openbel.framework.common.BELErrorException;

public class BELDataInvalidPropertyException extends BELErrorException {
    private static final long serialVersionUID = 3941173845394522106L;
    private static final String MSG_FMT =
            "Invalid property '%s' in block '%s'.  Possible values are '%s'.";

    private final String propertyName;
    private final String blockName;
    private final String[] possibleValues;

    public BELDataInvalidPropertyException(String resourceLocation,
            String blockName, String propertyName, String... possibleValues) {
        super(resourceLocation, buildMessage(propertyName, blockName,
                possibleValues));
        this.propertyName = propertyName;
        this.blockName = blockName;
        this.possibleValues = possibleValues;
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
        bldr.append(String.format(MSG_FMT, propertyName, blockName,
                Arrays.toString(possibleValues)));
        bldr.append("\n");
        return bldr.toString();
    }

    private static String buildMessage(final String propertyName,
            final String blockName, final String... possibleValues) {
        return String.format(MSG_FMT, propertyName, blockName,
                Arrays.toString(possibleValues));
    }
}
