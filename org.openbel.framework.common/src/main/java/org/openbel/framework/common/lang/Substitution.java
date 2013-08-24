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
 * Indicates the abundance of proteins with amino acid substitution sequence.
 * <p>
 * Function {@link Signature signature(s)}:
 *
 * <pre>
 * substitution(E:substitution,E:substitution,E:substitution)substitution
 * </pre>
 *
 * </p>
 *
 * @see Signature
 */
public class Substitution extends Function {

    /**
     * {@link Strings#SUBSTITUTION}
     */
    public final static String NAME;

    /**
     * {@link Strings#SUBSTITUTION_ABBREV}
     */
    public final static String ABBREVIATION;

    /**
     * Function description.
     */
    public final static String DESC;

    static {
        NAME = Strings.SUBSTITUTION;
        ABBREVIATION = Strings.SUBSTITUTION_ABBREV;
        DESC = "Indicates the abundance of proteins with amino acid " +
                "substitution sequence";
    }

    public Substitution() {
        super(NAME, ABBREVIATION, DESC,
                "substitution(E:*,E:*,E:*)substitution");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validArgumentCount(int count) {
        switch (count) {
        case 3:
            return true;
        default:
            return false;
        }
    }
}
