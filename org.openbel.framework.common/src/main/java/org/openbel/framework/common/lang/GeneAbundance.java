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
 * Denotes the abundance of a gene.
 * <p>
 * Function {@link Signature signature(s)}:
 *
 * <pre>
 * geneAbundance(E:geneAbundance)geneAbundance
 * </pre>
 *
 * </p>
 *
 * @see Signature
 */
public class GeneAbundance extends Function {

    /**
     * {@link Strings#GENE_ABUNDANCE}
     */
    public final static String NAME;

    /**
     * {@link Strings#GENE_ABUNDANCE_ABBREV}
     */
    public final static String ABBREVIATION;

    /**
     * Function description.
     */
    public final static String DESC;

    static {
        NAME = Strings.GENE_ABUNDANCE;
        ABBREVIATION = Strings.GENE_ABUNDANCE_ABBREV;
        DESC = "Denotes the abundance of a gene";
    }

    public GeneAbundance() {
        super(NAME, ABBREVIATION, DESC,
                "geneAbundance(E:geneAbundance)geneAbundance",
                "geneAbundance(E:geneAbundance,F:fusion)geneAbundance");
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
