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
 * Denotes a list of terms.
 * <p>
 * Function {@link Signature signature(s)}:
 *
 * <pre>
 * list(E:abundance)list
 * </pre>
 *
 * </p>
 *
 * @author Tat Chu
 * @see Signature
 */
public class TermList extends Function {

    /**
     * {@link Strings#LIST}
     */
    public final static String NAME;

    /**
     * {@code null}
     */
    public final static String ABBREVIATION;

    /**
     * Function description.
     */
    public final static String DESC;

    static {
        NAME = Strings.LIST;
        ABBREVIATION = null;
        DESC = "Groups a list of terms together";
    }

    public TermList() {
        super(NAME, ABBREVIATION, DESC,
                "list(E:abundance...)list",
                "list(F:abundance...)list");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validArgumentCount(int count) {
        if (count > 0) {
            return true;
        }
        return false;
    }
}
