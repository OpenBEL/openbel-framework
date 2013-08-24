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
 * Indicates an abundance of proteins with truncation sequence variants.
 * <p>
 * Function {@link Signature signature(s)}:
 *
 * <pre>
 * truncation(E:truncation)truncation
 * </pre>
 *
 * </p>
 *
 * @see Signature
 */
public class Truncation extends Function {

    /**
     * {@link Strings#TRUNCATION}
     */
    public final static String NAME;

    /**
     * {@link Strings#TRUNCATION_ABBREV}
     */
    public final static String ABBREVIATION;

    /**
     * Function description.
     */
    public final static String DESC;

    static {
        NAME = Strings.TRUNCATION;
        ABBREVIATION = Strings.TRUNCATION_ABBREV;
        DESC = "Indicates an abundance of proteins with truncation " +
                "sequence variants";
    }

    public Truncation() {
        super(NAME, ABBREVIATION, DESC,
                "truncation(E:*)truncation");
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
