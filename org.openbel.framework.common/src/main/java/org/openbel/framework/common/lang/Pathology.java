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
package org.openbel.framework.common.lang;

import org.openbel.framework.common.Strings;

/**
 * Denotes a disease or pathology process.
 * <p>
 * Function {@link Signature signature(s)}:
 *
 * <pre>
 * pathology(E:pathology)pathology
 * </pre>
 *
 * </p>
 *
 * @see Signature
 */
public class Pathology extends Function {

    /**
     * {@link Strings#PATHOLOGY}
     */
    public final static String NAME;

    /**
     * {@link Strings#PATHOLOGY_ABBREV}
     */
    public final static String ABBREVIATION;

    /**
     * Function description.
     */
    public final static String DESC;

    static {
        NAME = Strings.PATHOLOGY;
        ABBREVIATION = Strings.PATHOLOGY_ABBREV;
        DESC = "Denotes a disease or pathology process";
    }

    public Pathology() {
        super(NAME, ABBREVIATION, DESC,
                "pathology(E:pathology)pathology");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validArgumentCount(int count) {
        switch (count) {
        case 1:
            return true;
        default:
            return false;
        }
    }
}
