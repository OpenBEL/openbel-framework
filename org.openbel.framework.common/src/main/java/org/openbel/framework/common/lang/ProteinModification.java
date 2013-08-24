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
 * Denotes a covalently modified protein abundance.
 * <p>
 * Function {@link Signature signature(s)}:
 *
 * <pre>
 * proteinModification()proteinModification
 * proteinModification(E:phosphorylation)proteinModification
 * proteinModification(E:phosphorylation,E:phosphorylation)proteinModification
 * </pre>
 *
 * </p>
 *
 * @see Signature
 */
public class ProteinModification extends Function {

    /**
     * {@link Strings#PROTEIN_MODIFICATION}
     */
    public final static String NAME;

    /**
     * {@link Strings#MODIFICATION_ABBREV}
     */
    public final static String ABBREVIATION;

    /**
     * Function description.
     */
    public final static String DESC;

    static {
        NAME = Strings.PROTEIN_MODIFICATION;
        ABBREVIATION = Strings.PROTEIN_MODIFICATION_ABBREV;
        DESC = "Denotes a covalently modified protein abundance";
    }

    public ProteinModification() {
        super(NAME, ABBREVIATION, DESC,
                "proteinModification(E:*)proteinModification",
                "proteinModification(E:*,E:*)proteinModification",
                "proteinModification(E:*,E:*,E:*)proteinModification");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validArgumentCount(int count) {
        switch (count) {
        case 1:
        case 2:
        case 3:
            return true;
        default:
            return false;
        }
    }
}
