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
 * Denotes the abundance of a protein.
 * <p>
 * Function {@link Signature signature(s)}:
 *
 * <pre>
 * proteinAbundance(E:proteinAbundance)proteinAbundance
 * proteinAbundance(E:proteinAbundance,F:proteinModification)proteinAbundance
 * proteinAbundance(E:proteinAbundance,F:substitution)proteinAbundance
 * proteinAbundance(E:proteinAbundance,F:truncation)proteinAbundance
 * </pre>
 *
 * </p>
 *
 * @see Signature
 */
public class ProteinAbundance extends Function {

    /**
     * {@link Strings#PROTEIN_ABUNDANCE}
     */
    public final static String NAME;

    /**
     * {@link Strings#PROTEIN_ABUNDANCE_ABBREV}
     */
    public final static String ABBREVIATION;

    /**
     * Function description.
     */
    public final static String DESC;

    static {
        NAME = Strings.PROTEIN_ABUNDANCE;
        ABBREVIATION = Strings.PROTEIN_ABUNDANCE_ABBREV;
        DESC = "Denotes the abundance of a protein";
    }

    public ProteinAbundance() {
        super(
                NAME,
                ABBREVIATION,
                DESC,
                "proteinAbundance(E:proteinAbundance)proteinAbundance",
                "proteinAbundance(E:proteinAbundance,F:proteinModification)proteinAbundance",
                "proteinAbundance(E:proteinAbundance,F:substitution)proteinAbundance",
                "proteinAbundance(E:proteinAbundance,F:fusion)proteinAbundance",
                "proteinAbundance(E:proteinAbundance,F:truncation)proteinAbundance");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validArgumentCount(int count) {
        switch (count) {
        case 1:
        case 2:
            return true;
        default:
            return false;
        }
    }
}
