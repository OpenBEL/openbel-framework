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
 * Denotes the frequency or abundance of events in which a member acts to
 * perform post-translational modification of proteins.
 * <p>
 * Function {@link Signature signature(s)}:
 *
 * <pre>
 * ribosylationActivity(F:abundance)abundance
 * </pre>
 *
 * </p>
 *
 * @see Signature
 */
public class RibosylationActivity extends Function {

    /**
     * {@link Strings#RIBOSYLATION_ACTIVITY}
     */
    public final static String NAME;

    /**
     * {@link Strings#RIBOSYLATION_ACTIVITY_ABBREV}
     */
    public final static String ABBREVIATION;

    /**
     * Function description.
     */
    public final static String DESC;

    static {
        NAME = Strings.RIBOSYLATION_ACTIVITY;
        ABBREVIATION = Strings.RIBOSYLATION_ACTIVITY_ABBREV;
        DESC = "Denotes the frequency or abundance of events in which a " +
                "member acts to perform post-translational modification of " +
                "proteins";
    }

    public RibosylationActivity() {
        super(NAME, ABBREVIATION, DESC,
                "ribosylationActivity(F:complexAbundance)abundance",
                "ribosylationActivity(F:proteinAbundance)abundance");
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
