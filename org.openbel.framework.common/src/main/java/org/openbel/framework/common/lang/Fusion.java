/**
 * Copyright (C) 2012-2013 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
 */
package org.openbel.framework.common.lang;

import org.openbel.framework.common.Strings;

/**
 * Specifies the abundance of a protein translated from the fusion of a gene.
 * <p>
 * Function {@link Signature signature(s)}:
 *
 * <pre>
 * fusion(E:geneAbundance,E:geneAbundance)proteinAbundance
 * </pre>
 *
 * </p>
 *
 * @see Signature
 */
public class Fusion extends Function {

    /**
     * {@link Strings#FUSION}
     */
    public final static String NAME;

    /**
     * {@link Strings#FUSION_ABBREV}
     */
    public final static String ABBREVIATION;

    /**
     * Function description.
     */
    public final static String DESC;

    static {
        NAME = Strings.FUSION;
        ABBREVIATION = Strings.FUSION_ABBREV;
        DESC = "Specifies the abundance of a protein translated from the " +
                "fusion of a gene";
    }

    public Fusion() {
        super(NAME, ABBREVIATION, DESC,
                "fusion(E:geneAbundance)fusion",
                "fusion(E:geneAbundance,E:*,E:*)fusion",
                "fusion(E:proteinAbundance)fusion",
                "fusion(E:proteinAbundance,E:*,E:*)fusion",
                "fusion(E:rnaAbundance)fusion",
                "fusion(E:rnaAbundance,E:*,E:*)fusion");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validArgumentCount(int count) {
        switch (count) {
        case 1:
        case 3:
            return true;
        default:
            return false;
        }
    }
}
